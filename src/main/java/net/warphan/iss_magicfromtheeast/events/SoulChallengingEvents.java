package net.warphan.iss_magicfromtheeast.events;

import io.redspace.ironsspellbooks.api.events.CounterSpellEvent;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import net.warphan.iss_magicfromtheeast.datagen.MFTEDamageTypeTagGenerator;
import net.warphan.iss_magicfromtheeast.configs.MFTEServerConfigs;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ExtractedSoul;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;

import javax.annotation.Nullable;

/**
 * PORT 1.20.1: EntityTickEvent.Pre -> LivingEvent.LivingTickEvent; LivingDamageEvent.Pre ->
 * LivingHurtEvent.
 */
@Mod.EventBusSubscriber
public class SoulChallengingEvents {

    @SubscribeEvent
    public static void linkedSoulChallengingEvent(LivingEvent.LivingTickEvent event) {
        var entity = event.getEntity();
        if (entity instanceof ExtractedSoul challengedSoul) {
            var soulOwner = challengedSoul.getOwner();
            float linkingRange = 12.0f;
            if (!challengedSoul.level().isClientSide && soulOwner != null && challengedSoul.hasRadius()) {
                float distance = challengedSoul.distanceTo(soulOwner);
                if (distance > linkingRange) {
                    challengedSoul.onUnSummon();
                    punishingOwner(soulOwner);
                    event.setCanceled(true);
                } else if (soulOwner.isDeadOrDying()) {
                    challengedSoul.onUnSummon();
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void soulOwnerHurtEvent(LivingHurtEvent event) {
        var entity = event.getEntity();
        var source = event.getSource();
        @Nullable var attacker = event.getSource().getEntity();
        if (entity instanceof ExtractedSoul extractedSoul) {
            var soulOwner = extractedSoul.getOwner();
            float damageOnSoul = event.getAmount();
            float damageAmount = damageOnSoul * extractedSoul.bonusPercent;
            float damageAmountOver = extractedSoul.getHealth() * extractedSoul.bonusPercent;
            if (soulOwner != null) {
                if (source.is(MFTEDamageTypeTagGenerator.SOUL_HURTING)) {
                //if damage on soul is soul damage type -> deal 100% damage back to the owner
                soulOwner.hurt(soulOwner.damageSources().source(MFTEDamageTypes.SOUL_DAMAGE, attacker), damageOnSoul);
                } else if (damageOnSoul <= extractedSoul.getHealth()) {
                    soulOwner.hurt(soulOwner.damageSources().source(MFTEDamageTypes.SOUL_DAMAGE, attacker), damageAmount);
                } else if (damageOnSoul > extractedSoul.getHealth()) {
                    soulOwner.hurt(soulOwner.damageSources().source(MFTEDamageTypes.SOUL_DAMAGE, attacker), damageAmountOver);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSoulCrushedEvent(LivingDeathEvent event) {
        var entity = event.getEntity();
        if (!entity.level().isClientSide) {
            if (entity instanceof ExtractedSoul challengedSoul) {
                var soulOwner = challengedSoul.getOwner();
                if (soulOwner != null) {
                    punishingOwner(soulOwner);
                }
            }
        }
    }

    @SubscribeEvent
    public static void counterSpellCastOnSoul(CounterSpellEvent event) {
        var entity = event.target;
        var caster = event.caster;
        if (entity instanceof ExtractedSoul challengedSoul) {
            var soulOwner = challengedSoul.getOwner();
            if (caster == challengedSoul.getExtractor() || MFTEServerConfigs.PASS_CHALLENGING.get()) {
                challengedSoul.onUnSummon();
            } else {
                punishingOwner(soulOwner);
            }
        }
    }

    public static void punishingOwner(LivingEntity livingEntity) {
        livingEntity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN.get(), 200, 0));
        livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.SLOWED.get(), 200, 3));
    }

}
