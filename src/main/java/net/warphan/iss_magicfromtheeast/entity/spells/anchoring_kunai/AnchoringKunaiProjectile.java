package net.warphan.iss_magicfromtheeast.entity.spells.anchoring_kunai;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;

import java.util.Optional;
import java.util.function.Supplier;

public class AnchoringKunaiProjectile extends AbstractMagicProjectile {
    public int effectLevel = 0;
    public int effectTick = 20;

    public AnchoringKunaiProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public AnchoringKunaiProjectile(Level level, LivingEntity caster) {
        this(MFTEEntityRegistries.ANCHORING_KUNAI.get(), level);
        setOwner(caster);
    }

    public void setEffectLevel(int level) {
        this.effectLevel = level;
    }

    public void setEffectTick(int second) {
        this.effectTick = effectTick * second;
    }

    @Override
    public void trailParticles() {
        Vec3 pos = this.getBoundingBox().getCenter().add(getDeltaMovement());
        pos = pos.add(getDeltaMovement());
        Vec3 random = new Vec3(Utils.getRandomScaled(.05f), Utils.getRandomScaled(.05f), Utils.getRandomScaled(.05f));
        for (int i = 0; i < 3; i++) {
            level.addParticle(ParticleTypes.SCULK_SOUL, pos.x, pos.y - .25f, pos.z, random.x, random.y, random.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level, ParticleTypes.SCULK_SOUL, x, y, z, 5, .3, .3, .3, 0.1, true);
    }

    @Override
    public float getSpeed() {
        return 1.2f;
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
        DamageSources.applyDamage(entity, getDamage(), MFTESpellRegistries.ANCHORING_KUNAI.get().getDamageSource(this, getOwner()));
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MFTEEffectRegistries.ANCHORED_SOUL.get(), effectTick, effectLevel, false, false, true));
        }
        discard();
    }

    @Override
    protected boolean shouldPierceShields() {
        return true;
    }
}
