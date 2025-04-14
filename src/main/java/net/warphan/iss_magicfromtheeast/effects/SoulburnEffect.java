package net.warphan.iss_magicfromtheeast.effects;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class SoulburnEffect extends MagicMobEffect {
    public SoulburnEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        float damageBasedOnHealth = (livingEntity.getMaxHealth()) / 100 * 5;
        if (damageBasedOnHealth < 1.0f) {
            livingEntity.hurt(DamageSources.get(livingEntity.level, DamageTypes.MAGIC), 1.0f);
        } else if (damageBasedOnHealth >= 1.0f && damageBasedOnHealth <= 5.0f) {
            livingEntity.hurt(DamageSources.get(livingEntity.level, DamageTypes.MAGIC), damageBasedOnHealth);
        } else if (damageBasedOnHealth > 5.0f) {
            livingEntity.hurt(DamageSources.get(livingEntity.level, DamageTypes.MAGIC), 5.0f);
        }
        for (int i = 0; i < 7; i++) {
            Vec3 pos = new Vec3(Utils.getRandomScaled(1), Utils.getRandomScaled(1.0f) + 1.0f, Utils.getRandomScaled(1)).add(livingEntity.position());
            Vec3 random = new Vec3(Utils.getRandomScaled(.08f), Utils.getRandomScaled(.08f), Utils.getRandomScaled(.08f));
            livingEntity.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, random.x, random.y, random.z);
        }
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = 20 >> amplifier;
        return i <= 0 || duration % i == 0;
    }
}
