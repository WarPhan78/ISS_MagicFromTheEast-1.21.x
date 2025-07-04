package net.warphan.iss_magicfromtheeast.entity.spells.verdict_circle;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
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

import javax.annotation.Nullable;
import java.util.Optional;

public class VerdictCircle extends AoeEntity implements GeoEntity, AntiMagicSusceptible {

    public VerdictCircle(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public float percentBonus;
    public static int onWorldTick = 80;
    @Nullable
    LivingEntity targets;

    public VerdictCircle(Level level) {
        this(MFTEEntityRegistries.VERDICT_CIRCLE.get(), level);
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return !pTarget.isSpectator() && pTarget.isAlive() && pTarget.isPickable();
    }

    @Override
    public void tick() {
        if (this.tickCount > onWorldTick) {
            discard();
        }
        super.tick();
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (target != getOwner()) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 4));
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 1));
            target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20, 1));

            float targetLostHealth = target.getMaxHealth() - target.getHealth();
            float damageAmount = this.getDamage() + (targetLostHealth * percentBonus);
            float limitDamage = target.getMaxHealth() * 0.4f;
            if (damageAmount >= limitDamage) {
                damageAmount = limitDamage;
                DamageSources.applyDamage(target, damageAmount, MFTESpellRegistries.UNDERWORLD_AID_SPELL.get().getDamageSource(this, getOwner()));
            }
            else DamageSources.applyDamage(target, damageAmount, MFTESpellRegistries.UNDERWORLD_AID_SPELL.get().getDamageSource(this, getOwner()));
        }
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
        return 0.35f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(ParticleTypes.SOUL.getType());
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        discard();
    }

    //ANIMATION
    private final RawAnimation action = RawAnimation.begin().thenPlay("animation.verdict_circle.action");

    private PlayState predicate(software.bernie.geckolib.animation.AnimationState event) {
        event.getController().setAnimation(action);
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
