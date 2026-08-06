package net.warphan.iss_magicfromtheeast.effects;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

// TODO PORT 1.20.1: ParticleTypes.WHITE_SMOKE does not exist on 1.20.1 - CLOUD is the closest substitute.
public class MistStepEffect extends MagicMobEffect {
    public MistStepEffect(MobEffectCategory category, int pColor) {
        super(category, pColor);
    }

    @Override
    public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectAdded(pLivingEntity, pAmplifier);
        playVisuals(pLivingEntity);
    }

    @Override
    public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectRemoved(pLivingEntity, pAmplifier);
        playVisuals(pLivingEntity);
    }

    private static void playVisuals(LivingEntity pLivingEntity) {
        Vec3 pos = pLivingEntity.getBoundingBox().getCenter();
        pLivingEntity.level().playSound((Player) null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), SoundRegistry.BLOOD_STEP.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
        if (!pLivingEntity.level().isClientSide) {
            MagicManager.spawnParticles(pLivingEntity.level(), ParticleTypes.CLOUD, pos.x, pos.y, pos.z, 12, 0.4, - 0.2, 0.4, 2.0, false);
        }
    }
}
