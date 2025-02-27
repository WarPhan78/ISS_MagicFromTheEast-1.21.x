package net.warphan.iss_magicfromtheeast.entity.spells.throw_circle;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Optional;

public class ThrowCircleEntity extends AoeEntity implements AntiMagicSusceptible, GeoEntity {
    //public static final int TRIGGER_TIME = 20;
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
        if (!level.isClientSide) {
            checkHits();
            MagicManager.spawnParticles(level, ParticleTypes.POOF, getX(), getY(), getZ(), 20, getRadius() * .5f, .2f, getRadius() * .5f, 0.6f, true);
            level.playSound(null, this.blockPosition(), MFTESoundRegistries.SPROING.get(), SoundSource.NEUTRAL, 4.5f, Utils.random.nextIntBetweenInclusive(9, 11) * .1f);
        }
            discard();
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    public void applyEffect(LivingEntity target) {
            var knockup = new Vec3(this.getX() - target.getX(), this.getY() - target.getY(), this.getZ() - target.getZ()).normalize().scale(-strength);
            target.setDeltaMovement(target.getDeltaMovement().add(knockup));
            target.hurtMarked = true;
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

    //ANIMATION (CURRENTLY NOT USE)
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
}
