package net.warphan.iss_magicfromtheeast.entity.spirit_bullet;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_ashigaru.SpiritAshigaruEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;

import java.util.Optional;

public class SpiritBulletProjectile extends AbstractMagicProjectile {
    public SpiritBulletProjectile(EntityType<? extends SpiritBulletProjectile> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
    }

    public SpiritBulletProjectile(Level level, LivingEntity shooter) {
        this(MFTEEntityRegistries.SPIRIT_BULLET.get(), level);
        setOwner(shooter);
    }

    @Override
    public void trailParticles() {
        Vec3 pos = this.getBoundingBox().getCenter().add(getDeltaMovement());
        pos = pos.add(getDeltaMovement());
        Vec3 random = new Vec3(Utils.getRandomScaled(.05f), Utils.getRandomScaled(.05f), Utils.getRandomScaled(.05f));
        for (int i = 0; i < 5; i++) {
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, random.x, random.y, random.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level, ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 5, .2, .2, .2, 0.2, true);
        MagicManager.spawnParticles(level, ParticleTypes.SCULK_SOUL, x, y, z, 3, .1, .1, .1, 0.1, true);
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundEvents.SOUL_ESCAPE);
    }

    @Override
    public float getSpeed() {
        return 3.0f;
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
        if (entity != this.getOwner() || !isAlliedTo(this.getOwner())) {
            if (this.getOwner() instanceof SpiritAshigaruEntity ashigaru) {
                var highOwner = ashigaru.getSummoner();
                if (highOwner != null) {
                    DamageSources.applyDamage(entity, getDamage(), MFTESpellRegistries.ASHIGARU_SQUAD_SPELL.get().getDamageSource(this, highOwner));
                } else
                    DamageSources.applyDamage(entity, getDamage(), MFTESpellRegistries.ASHIGARU_SQUAD_SPELL.get().getDamageSource(this, getOwner()));
            }
            entity.invulnerableTime = 0;
            discard();
        }
    }
}
