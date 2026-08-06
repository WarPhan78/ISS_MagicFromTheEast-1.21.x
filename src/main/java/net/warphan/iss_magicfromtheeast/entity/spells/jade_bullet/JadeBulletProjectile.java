package net.warphan.iss_magicfromtheeast.entity.spells.jade_bullet;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.*;

import java.util.Optional;
import java.util.function.Supplier;

public class JadeBulletProjectile extends AbstractMagicProjectile {
    public JadeBulletProjectile(EntityType<? extends JadeBulletProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public JadeBulletProjectile(Level level, LivingEntity caster) {
        this(MFTEEntityRegistries.JADE_PROJECTILE.get(), level);
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

        Vec3 pos = this.getBoundingBox().getCenter().add(getDeltaMovement());
        pos = pos.add(getDeltaMovement());
        Vec3 random = new Vec3(Utils.getRandomScaled(.05f), Utils.getRandomScaled(.05f), Utils.getRandomScaled(.05f));
        for (int i = 0; i < 3; i++) {
            level.addParticle(new DustParticleOptions(MFTESchoolRegistries.SYMMETRY.get().getTargetingColor(), 1.8f), pos.x, pos.y, pos.z, random.x, random.y, random.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level, MFTEParticleRegistries.JADE_SHATTER_PARTICLE.get(), x, y, z, 8, .3, .3, .3, 0.1, true);
    }

    @Override
    public float getSpeed() {
        return 1.6f;
    }

    @Override
    public Optional<Supplier<SoundEvent>> getImpactSound() {
        return Optional.of(MFTESoundRegistries.JADE_SWORD_IMPACT);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        doAoeDamage(blockHitResult.getLocation());
        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        var entity = entityHitResult.getEntity();
        DamageSources.applyDamage(entity, getDamage(), MFTESpellRegistries.JADE_BULLET_SPELL.get().getDamageSource(this, getOwner()));
        doAoeDamage(entityHitResult.getLocation());

        discard();
    }

    private void doAoeDamage(Vec3 location) {
        float boomRadius = 2.5f;
        level.getEntities(this, this.getBoundingBox().inflate(boomRadius)).forEach((entity) -> {
            if (canHitEntity(entity)) {
                double distance = entity.distanceToSqr(position());
                if (distance < boomRadius * boomRadius) {
                    float damage = this.damage;
                    DamageSources.applyDamage(entity, damage * 0.5f, MFTESpellRegistries.JADE_BULLET_SPELL.get().getDamageSource(this, getOwner()));
                }
            }
        });

        if (!level.isClientSide) {
            MagicManager.spawnParticles(level, new BlastwaveParticleOptions(MFTESchoolRegistries.SYMMETRY.get().getTargetingColor(), 1.2f), location.x, location.y + .165f, location.z, 1, 0, 0, 0, 0, true);
        }
    }
}
