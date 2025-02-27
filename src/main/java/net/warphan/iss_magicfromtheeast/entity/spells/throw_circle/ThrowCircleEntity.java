package net.warphan.iss_magicfromtheeast.entity.spells.throw_circle;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Optional;

public class ThrowCircleEntity extends AoeEntity implements AntiMagicSusceptible, GeoEntity {
    public static final int TRIGGER_TIME = 20;
    public float strength;
    public int amplifier;

    @Nullable
    LivingEntity target;

    public ThrowCircleEntity(EntityType<? extends Projectile> pEntityType, Level plevel) {
        super(pEntityType, plevel);
        this.setRadius((float) this.getBoundingBox().getXsize());
        this.setNoGravity(false);
    }

    public ThrowCircleEntity(Level level) {
        this(MFTEEntityRegistries.THROW_CIRCLE_ENTITY.get(), level);
    }

    @Override
    public void tick() {
        this.setOldPosAndRot();
        if (tickCount == TRIGGER_TIME) {
            if (!level.isClientSide) {
                checkHits();
                MagicManager.spawnParticles(level, ParticleTypes.POOF, getX(), getY(), getZ(), 25, getRadius() * .5f, .2f, getRadius() * .5f, 0.6f, true);
                level.playSound(null, this.blockPosition(), SoundRegistry.FORCE_IMPACT.get(), SoundSource.NEUTRAL, 4.5f, Utils.random.nextIntBetweenInclusive(9, 11) * .1f);
            }
        }

        if (this.tickCount > TRIGGER_TIME) {
            discard();
        }
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    public void applyEffect(LivingEntity target) {
            var knockup = new Vec3(this.getX() - target.getX(), this.getY() - target.getY(), this.getZ() - target.getZ()).normalize().scale(-strength);
            target.setDeltaMovement(target.getDeltaMovement().add(knockup));
            target.hurtMarked = true;
            target.addEffect(new MobEffectInstance(MobEffectRegistry.AIRBORNE, 40, amplifier));
    }

    @Override
    public float getParticleCount() {
        return 0f;
    }

    @Override
    public void refreshDimensions() {
        return;
    }

    @Override
    public void ambientParticles() {
        return;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        discard();
    }

    //ANIMATION
    private final RawAnimation CIRCLE_ACTIVE = RawAnimation.begin().thenPlay("animation.throw_circle.active");

    private PlayState predicate(software.bernie.geckolib.animation.AnimationState event) {
        event.getController().setAnimation(CIRCLE_ACTIVE);
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
