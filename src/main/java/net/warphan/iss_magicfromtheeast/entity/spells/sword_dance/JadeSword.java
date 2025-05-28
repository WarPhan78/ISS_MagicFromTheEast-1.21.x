package net.warphan.iss_magicfromtheeast.entity.spells.sword_dance;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class JadeSword extends AbstractMagicProjectile implements GeoEntity {

    private int hitPerTicks;
    private int waitTimer = -1;
    private LivingEntity owner;

    public JadeSword(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public JadeSword(Level level, LivingEntity owner) {
        this(MFTEEntityRegistries.JADE_SWORD.get(), level);
        this.setOwner(owner);
        this.owner = owner;
        this.setDamage(damage);
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.position().subtract(getDeltaMovement());
        level.addParticle(ParticleTypes.GLOW, vec3.x, vec3.y, vec3.z, 0, 0, 0);
    }

    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level, ParticleTypes.SCRAPE, x, y, z, 5, .6, .6, .6, 0.2, true);
        MagicManager.spawnParticles(level, ParticleTypes.GLOW, x, y, z, 3, .4, .4, .4, 0.3, true);
    }

    @Override
    public float getSpeed() {
        return 1.2f;
    }

    @Override
    public void tick() {
        super.tick();
        hitPerTicks = 0;
        if (waitTimer > 0) {
            waitTimer--;
            if (waitTimer <= 110) {
                Vec3 stop = new Vec3(0, 0, 0);
                this.setDeltaMovement(stop);
                if (waitTimer <= 100 && waitTimer > 10) {
                    LivingEntity target;
                    target = owner.getLastHurtMob();
                    if (target != null && !target.isDeadOrDying()) {
                        Vec3 attack = target.getBoundingBox().getCenter().subtract(this.position());
                        this.setDeltaMovement(getDeltaMovement().add(attack.multiply(1, 1, 1).normalize().scale(2)));
                    }
                }
                else if (waitTimer == 5) {
                    this.doBreaking();
                }
                else if (waitTimer == 0) {
                    this.discard();
                }
            }
        }
    }

    public void setWaitTimer(int timerDeath) {
        this.waitTimer = timerDeath;
    }

    protected void onHitBlock(BlockHitResult bResult) {}

    private void doBreaking() {
        var center = this.position();
        MagicManager.spawnParticles(level, ParticleTypes.SCRAPE, center.x, center.y, center.z, 10, .8, .8, .8, 0.2, true);
        level.playSound(null, BlockPos.containing(position()), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL, 2f, .5f);
    }

    @Override
    public void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        DamageSources.applyDamage(entity, damage, MFTESpellRegistries.SWORD_DANCE_SPELL.get().getDamageSource(this, getOwner()));
        if (hitPerTicks++ < 5) {
            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitResult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitResult)) {
                onHit(hitResult);
            }
        }
        if (this.tickCount > 10) {
            entity.invulnerableTime = 0;
        }
    }

    BlockPos lastHitBlock;

    public void onHit(HitResult result) {
        if (!level.isClientSide) {
            var blockPos = BlockPos.containing(result.getLocation());
            if (result.getType() == HitResult.Type.BLOCK && !blockPos.equals(lastHitBlock)) {
                lastHitBlock = blockPos;
                this.discard();
            } else if (result.getType() == HitResult.Type.ENTITY) {
                level.playSound(null, BlockPos.containing(position()),MFTESoundRegistries.JADE_SWORD_IMPACT.get(), SoundSource.NEUTRAL, 0.2f, 1f);
                if (this.tickCount > 10 && result.getType() == HitResult.Type.ENTITY) {
                    this.discard();
                }
            }
        }
        super.onHit(result);
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    public void onAntiMagic(MagicData pMagicData) {

    }

    @Override
    public boolean shouldPierceShields() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        return;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
}

