package net.warphan.iss_magicfromtheeast.entity.spells.phantom_cavalry;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class PhantomCavalryVisualEntity extends AbstractMagicProjectile implements GeoEntity {
    public PhantomCavalryVisualEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
    }

    public PhantomCavalryVisualEntity(Level level, LivingEntity caster) {
        this(MFTEEntityRegistries.PHANTOM_CAVALRY.get(),level);
        setOwner(caster);
    }

    @Override
    public void trailParticles() {
        Vec3 pos = this.getBoundingBox().getBottomCenter().add(getDeltaMovement());
        pos = pos.add(getDeltaMovement());
        Vec3 random = new Vec3(Utils.getRandomScaled(.1f), Utils.getRandomScaled(.05f), Utils.getRandomScaled(.1f));
        level.addParticle(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, random.x, random.y, random.z);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        return;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    public float getSpeed() {
        return 1.0f;
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount < 35 && tickCount%10 == 0) {
            if (!level.isClientSide) {
                this.playSound(SoundEvents.HORSE_GALLOP);
            }
        }
        if (tickCount > 40) {
            discard();
            if (!level.isClientSide) {
                MagicManager.spawnParticles(level(), ParticleTypes.SCULK_SOUL, getX(), getY(), getZ(), 25, 1.5, 2.5, 1.5, .08, false);
            }
        }
    }

    @Override
    public boolean canHitEntity(Entity pTarget) {
        return super.canHitEntity(pTarget);
    }

    @Override
    public void handleHitDetection() {
        Vec3 vec3 = this.getDeltaMovement();
        Vec3 pos = this.position();
        Vec3 vec32 = pos.add(vec3);
        HitResult hitResult = level.clip(new ClipContext(pos, vec32, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            onHit(hitResult);
        } else {
            var entities = level.getEntities(this, this.getBoundingBox().inflate(0.25f), this::canHitEntity);
            for (Entity entity : entities) {
                onHit(new EntityHitResult(entity, this.getBoundingBox().getCenter().add(entity.getBoundingBox().getCenter()).scale(0.5f)));
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        var target = entityHitResult.getEntity();
        if (target instanceof LivingEntity livingEntity) {
            DamageSources.ignoreNextKnockback(livingEntity);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80));
        }
        DamageSources.applyDamage(entityHitResult.getEntity(), damage, MFTESpellRegistries.PHANTOM_CHARGE_SPELL.get().getDamageSource(this, getOwner()));
    }

    //ANIMATION
    private final RawAnimation charge = RawAnimation.begin().thenPlay("phantom_charging");

    private PlayState predicate(software.bernie.geckolib.animation.AnimationState event) {
        event.getController().setAnimationSpeed(2).setAnimation(charge);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
}
