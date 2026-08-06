package net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
import java.util.function.Supplier;

public class JadeLoong extends AbstractMagicProjectile implements GeoEntity {
    public JadeLoong(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
    }

    public JadeLoong(Level level, LivingEntity caster) {
        this(MFTEEntityRegistries.JADE_LOONG.get(),level);
        setOwner(caster);
    }

    @Override
    public void trailParticles() {
        float yHeading = -((float) (Mth.atan2(getDeltaMovement().z, getDeltaMovement().x) * (double) (180F / (float) Math.PI)) + 90.0F);
        float radius = .25f;
        int steps = 2;
        var vec = getDeltaMovement();
        double x2 = getX();
        double x1 = x2 - vec.x;
        double y2 = getY();
        double y1 = y2 - vec.y;
        double z2 = getZ();
        double z1 = z2 - vec.z;
        for (int j = 0; j < steps; j++) {
            float offset = (1f / steps) * j;
            double radians = ((tickCount + offset) / 7.5f) * 360 * Mth.DEG_TO_RAD;
            Vec3 swirl = new Vec3(Math.cos(radians) * radius, Math.sin(radians) * radius, 0).yRot(yHeading * Mth.DEG_TO_RAD);
            double x = Mth.lerp(offset, x1, x2) + swirl.x;
            double y = Mth.lerp(offset, y1, y2) + swirl.y + getBbHeight() / 2;
            double z = Mth.lerp(offset, z1, z2) + swirl.z;
            Vec3 jitter = Vec3.ZERO;
            level.addParticle(ParticleTypes.GLOW, x, y, z, jitter.x, jitter.y, jitter.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level, ParticleTypes.SCRAPE, x, y, z, 5, 0.8, 0.8, 0.8, 1, false);
        MagicManager.spawnParticles(level, MFTEParticleHelper.JADE_SHATTER, x, y, z, 2, 0.2, 0.2, 0.2, 0.2, false);
    }

    @Override
    public Optional<Supplier<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    public float getSpeed() {
        return 0.6f;
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 80) {
            discard();
            if (!level.isClientSide) {
                impactParticles(getX(), this.getBoundingBox().getCenter().y,getZ());
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
        }
        DamageSources.applyDamage(entityHitResult.getEntity(), damage, MFTESpellRegistries.DRAGON_GLIDE_SPELL.get().getDamageSource(this, getOwner()));
    }

    //ANIMATION
    private final RawAnimation loongglide = RawAnimation.begin().thenPlay("glide");

    private PlayState predicate(software.bernie.geckolib.core.animation.AnimationState event) {
        event.getController().setAnimation(loongglide);
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
