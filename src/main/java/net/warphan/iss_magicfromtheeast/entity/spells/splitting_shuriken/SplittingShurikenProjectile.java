package net.warphan.iss_magicfromtheeast.entity.spells.splitting_shuriken;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.Supplier;

public class SplittingShurikenProjectile extends AbstractMagicProjectile implements GeoEntity {
    private static final EntityDataAccessor<Boolean> IS_PRIMARY = SynchedEntityData.defineId(SplittingShurikenProjectile.class, EntityDataSerializers.BOOLEAN);
    public int splitCount;

    public SplittingShurikenProjectile(EntityType<? extends SplittingShurikenProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public SplittingShurikenProjectile(Level level, LivingEntity caster) {
        this (MFTEEntityRegistries.SPIRIT_SHURIKEN.get(), level);
        setOwner(caster);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_PRIMARY, false);
    }

    public void setIsPrimary() {
        entityData.set(IS_PRIMARY, true);
    }

    public boolean isPrimary() {
        return entityData.get(IS_PRIMARY);
    }

    public void setSplitCount(int count) {
        this.splitCount = count;
    }

    @Override
    public void trailParticles() {

    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level, ParticleTypes.SCULK_SOUL, x, y, z, 3, .2, .2, .2, 0.1, true);
    }

    @Override
    public float getSpeed() {
        return 1.5f;
    }

    @Override
    public Optional<Supplier<SoundEvent>> getImpactSound() {
        return Optional.of(() -> SoundEvents.SOUL_ESCAPE);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        var entity = entityHitResult.getEntity();
        Vec3 center = entityHitResult.getLocation();
        var level = this.level;
        DamageSources.applyDamage(entity, getDamage(), MFTESpellRegistries.SPLITTING_SHURIKEN.get().getDamageSource(this, getOwner()));
        if (this.isPrimary() && this.getOwner() instanceof LivingEntity livingEntity && this.splitCount > 0) {
            int count = this.splitCount;
            int offset = 360 / count;

            for (int i = 0; i < count; i++) {
                Vec3 motion = new Vec3(0, 0, 1.0);
                motion = motion.xRot(Mth.DEG_TO_RAD);
                motion = motion.yRot(offset * i * Mth.DEG_TO_RAD);

                SplittingShurikenProjectile splitShuriken = new SplittingShurikenProjectile(level, livingEntity);
                splitShuriken.setDamage(this.getDamage() / 2);
                splitShuriken.setDeltaMovement(motion);

                splitShuriken.moveTo(center.x, center.y, center.z);
                level.addFreshEntity(splitShuriken);
            }
        }
        discard();
    }

    //ANIMATION
    private final RawAnimation shuriken_spin = RawAnimation.begin().thenPlay("spin");

    private PlayState predicate(software.bernie.geckolib.core.animation.AnimationState event) {
        event.getController().setAnimation(shuriken_spin);
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
