package net.warphan.iss_magicfromtheeast.effects;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.configs.MFTEServerConfigs;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;

public class SoulburnEffect extends MagicMobEffect implements ISyncedMobEffect {
    public SoulburnEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        float minDamage = 1.0f;
        float damageBasedOnHealth = ((livingEntity.getMaxHealth()) / 100) * MFTEServerConfigs.SOULBURN_DAMAGE_SCALING.get();
        double maxDamage = MFTEServerConfigs.SOULBURN_MAX_DAMAGE.get();
        if (damageBasedOnHealth < minDamage) {
            livingEntity.hurt(DamageSources.get(livingEntity.level, MFTEDamageTypes.SOUL_DAMAGE), minDamage);
        } else if (damageBasedOnHealth >= 1.0f && damageBasedOnHealth <= maxDamage) {
            livingEntity.hurt(DamageSources.get(livingEntity.level, MFTEDamageTypes.SOUL_DAMAGE), damageBasedOnHealth);
        } else if (damageBasedOnHealth > maxDamage) {
            livingEntity.hurt(DamageSources.get(livingEntity.level, MFTEDamageTypes.SOUL_DAMAGE), (float) maxDamage);
        }
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = 20 >> amplifier;
        return i <= 0 || duration % i == 0;
    }

    @Override
    public void clientTick(LivingEntity entity, MobEffectInstance instance) {
        for (int i = 0; i < 2; i++) {
            Vec3 pos = new Vec3(Utils.getRandomScaled(1), Utils.getRandomScaled(1.0f) + 1.0f, Utils.getRandomScaled(1)).add(entity.position());
            Vec3 random = new Vec3(Utils.getRandomScaled(.08f), Utils.getRandomScaled(.08f), Utils.getRandomScaled(.08f));
            entity.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, random.x, random.y, random.z);
        }
    }
}
