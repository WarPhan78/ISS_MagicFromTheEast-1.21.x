package net.warphan.iss_magicfromtheeast.effects;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ExtractedSoul;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;

@Mod.EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID)
public class AnchoredSoulEffect extends MagicMobEffect implements ISyncedMobEffect {
    public AnchoredSoulEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void onEntityTeleportEvent(EntityTeleportEvent event) {
        Entity entity = event.getEntity();
        Vec3 oldPos = new Vec3(event.getPrevX(), event.getPrevY(), event.getPrevZ());
        if (entity instanceof LivingEntity livingEntity) {
            var level = livingEntity.level();
            var effect = livingEntity.getEffect(MFTEEffectRegistries.ANCHORED_SOUL.get());
            if (effect != null) {

                float bonusPercent = (effect.getAmplifier() + 1) * 0.5f;

                ExtractedSoul extractedSoul = new ExtractedSoul(level, livingEntity, null);
                extractedSoul.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(livingEntity.getHealth());
                extractedSoul.setHealth(extractedSoul.getMaxHealth());

                extractedSoul.setDuration(20 * 10);
                extractedSoul.setBonusPercent(bonusPercent);
                extractedSoul.setPos(oldPos);

                level.addFreshEntity(extractedSoul);

                livingEntity.removeEffect(effect.getEffect());
            }
        }
    }

    @Override
    public void clientTick(LivingEntity entity, MobEffectInstance instance) {
        for (int i = 0; i < 1; i++) {
            Vec3 pos = new Vec3(Utils.getRandomScaled(1), Utils.getRandomScaled(1.0f) + 1.0f, Utils.getRandomScaled(1)).add(entity.position());
            Vec3 random = new Vec3(Utils.getRandomScaled(.08f), Utils.getRandomScaled(.08f), Utils.getRandomScaled(.08f));
            entity.level().addParticle(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, random.x, random.y, random.z);
        }
    }
}
