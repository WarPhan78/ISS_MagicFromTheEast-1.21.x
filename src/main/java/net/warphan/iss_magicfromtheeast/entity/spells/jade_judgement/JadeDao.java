package net.warphan.iss_magicfromtheeast.entity.spells.jade_judgement;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JadeDao extends AbstractMagicProjectile implements GeoEntity {

    private UUID targetUUID;
    private Entity cachedTarget;
    private List<Entity> targets;

    public JadeDao(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        targets = new ArrayList<>();

        this.setNoGravity(true);
    }

    public JadeDao(Level pLevel, LivingEntity owner, LivingEntity target) {
        this(MFTEEntityRegistries.JADE_DAO.get(), pLevel);
        this.setOwner(owner);
        this.setTarget(target);
    }

    int airTime;

    public void setAirTime(int airTimeInTicks) {
        airTime = airTimeInTicks;
    }

    public void setTarget(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.targetUUID = pOwner.getUUID();
            this.cachedTarget = pOwner;
        }
    }

    @Nullable
    public Entity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        } else if (this.targetUUID != null && this.level instanceof ServerLevel) {
            this.cachedTarget = ((ServerLevel) this.level).getEntity(this.targetUUID);
            return this.cachedTarget;
        } else {
            return null;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.targetUUID != null) {
            tag.putUUID("Target", this.targetUUID);
        }
        if (this.airTime > 0) {
            tag.putInt("airTime", airTime);
        }
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Target")) {
            this.targetUUID = tag.getUUID("Target");
        }
        if (tag.contains("airTime")) {
            this.airTime = tag.getInt("airTime");
        }
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 1; i++) {
            Vec3 random = new Vec3(
                    Utils.getRandomScaled(this.getBbWidth() * .5f), 0,
                    Utils.getRandomScaled(this.getBbWidth() * .5f));
            level.addParticle(ParticleTypes.GLOW, getX() + random.x, getY(), getZ() + random.z, 0, -.05, 0);
        }
    }

    private void doFallingDamage(Entity target) {
        if (level.isClientSide)
            return;
        if (!canHitEntity(target) || targets.contains(target))
            return;
        boolean flag = DamageSources.applyDamage(target, getDamage() / 4 * 1, MFTESpellRegistries.JADE_JUDGEMENT_SPELL.get().getDamageSource(this, getOwner()));
        if (flag) {
            targets.add(target);
            target.invulnerableTime = 0;
        }
    }

    private void doImpactDamage() {
        float boomRadius = 5f;
        level.getEntities(this, this.getBoundingBox().inflate(boomRadius)).forEach((entity) -> {
            if (canHitEntity(entity)) {
                double distance = entity.distanceToSqr(position());
                if (distance < boomRadius * boomRadius) {
                    double p = (1 - Math.pow(Math.sqrt(distance) / (boomRadius), 3));
                    float damage = (float) (this.damage * p);
                    DamageSources.applyDamage(entity, damage, MFTESpellRegistries.JADE_JUDGEMENT_SPELL.get().getDamageSource(this, getOwner()));
                }
            }
        });
    }

    @Override
    public void tick() {
        this.firstTick = false;
        xo = getX();
        yo = getY();
        zo = getZ();
        xOld = getX();
        yOld = getY();
        zOld = getZ();
        yRotO = getYRot();
        xRotO = getXRot();
        if (!level.isClientSide) {
            if (airTime <= 0) {
                if (onGround()) {
                    doImpactDamage();
                    this.playSound(SoundEvents.TRIDENT_THUNDER.value(), 8, .65f);
                    impactParticles(getX(), getY(), getZ());
                    discard();
                } else {
                    level.getEntities(this, getBoundingBox().inflate(0.35)).forEach(this::doFallingDamage);
                }
            }
            if (airTime-- > 0) {
                boolean tooHigh = false;
                this.setDeltaMovement(getDeltaMovement().multiply(.95f, .75f, .95f));
                if (getTarget() != null) {
                    var target = getTarget();
                    Vec3 diff = target.position().subtract(this.position());
                    if (diff.horizontalDistanceSqr() > 1) {
                        this.setDeltaMovement(getDeltaMovement().add(diff.multiply(1, 0, 1).normalize().scale(.025f)));
                    }
                    if (this.getY() - target.getY() > 18) {
                        tooHigh = true;
                    }
                } else {
                    if (airTime % 3 == 0) {
                        HitResult ground = Utils.raycastForBlock(level, position(), position().subtract(0, 3.5, 0), ClipContext.Fluid.ANY);
                        if (ground.getType() == HitResult.Type.MISS) {
                            tooHigh = true;
                        } else if (Math.abs(position().y - ground.getLocation().y) < 4) {
                        }
                    }
                }
                if (tooHigh) {
                    this.setDeltaMovement(getDeltaMovement().add(0, -.005, 0));
                } else {
                    this.setDeltaMovement(getDeltaMovement().add(0, .01, 0));
                }
                if (airTime == 0) {
                    this.setDeltaMovement(0, 0.5, 0);
                }
            } else {
                this.setDeltaMovement(0, getDeltaMovement().y - .15, 0);
            }
        } else {
            trailParticles();
        }
        move(MoverType.SELF, getDeltaMovement());
    }

    @Override
    public void setYRot(float pYRot) {}

    @Override
    public void setXRot(float pXRot) {}

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return pTarget != getOwner() && super.canHitEntity(pTarget);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level, ParticleTypes.SCRAPE, x, y, z, 50, .8, .1, .8, 0.2, false);
        MagicManager.spawnParticles(level, ParticleTypes.GLOW, x, y, z, 25, .5, .1, .5, 0.3, false);
    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
}
