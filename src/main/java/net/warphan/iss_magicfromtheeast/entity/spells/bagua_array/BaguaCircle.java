package net.warphan.iss_magicfromtheeast.entity.spells.bagua_array;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import net.warphan.iss_magicfromtheeast.util.MFTEParticleHelper;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class BaguaCircle extends AoeEntity implements GeoEntity, AntiMagicSusceptible {
    public BaguaCircle(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BaguaCircle(Level level) {
        this(MFTEEntityRegistries.BAGUA_CIRCLE.get(), level);
    }

    public int healingAmplifier;

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return !pTarget.isSpectator() && pTarget.isAlive() && pTarget.isPickable();
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (target != getOwner() && target.getMobType() == MobType.UNDEAD) {
            DamageSources.applyDamage(target, getDamage(), MFTESpellRegistries.BAGUA_ARRAY_CIRCLE_SPELL.get().getDamageSource(this, getOwner()));
            DamageSources.ignoreNextKnockback(target);
        } else if (target == getOwner()) {
            target.addEffect(new MobEffectInstance(MFTEEffectRegistries.REVERSAL_HEALING.get(), 40, healingAmplifier));
        }
    }

    public void setAmplifier(int amplifier) {
        this.healingAmplifier = amplifier;
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3(0, 1, 0);
    }
    @Override
    public boolean shouldBeSaved() {
        return false;
    }
    @Override
    public void refreshDimensions() {
        return;
    }
    @Override
    public float getParticleCount() {
        return 0.125f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(MFTEParticleHelper.JADE_SHATTER);
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        discard();
    }

    // ANIMATION
    private final RawAnimation baguaspin = RawAnimation.begin().thenPlay("animation.bagua_circle.spin");

    private PlayState predicate(software.bernie.geckolib.core.animation.AnimationState event) {
        event.getController().setAnimation(baguaspin);
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
