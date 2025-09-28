package net.warphan.iss_magicfromtheeast.events;

import io.redspace.ironsspellbooks.api.events.CounterSpellEvent;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ChallengedSoul;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;

@EventBusSubscriber
public class SoulChallengingEvents {

    @SubscribeEvent
    public static void linkedSoulChallengingEvent(EntityTickEvent.Pre event) {
        var entity = event.getEntity();
        if (entity instanceof ChallengedSoul challengedSoul) {
            var soulOwner = challengedSoul.getSummoner();
            float linkingRange = 10.0f;
            if (!challengedSoul.level.isClientSide && soulOwner != null) {
                float distance = challengedSoul.distanceTo(soulOwner);
                if (distance > linkingRange) {
                    challengedSoul.onUnSummon();
                    punishingOwner(soulOwner);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void soulOwnerHurtEvent(LivingDamageEvent.Post event) {
        var entity = event.getEntity();
        if (entity instanceof ChallengedSoul challengedSoul1) {
            var soulOwner = challengedSoul1.getSummoner();
            var damageOnSoul = event.getOriginalDamage();
            var damageAmount = damageOnSoul * challengedSoul1.bonusPercent;
            if (soulOwner != null) {
                if (damageOnSoul <= challengedSoul1.getHealth()) {
                    soulOwner.hurt(soulOwner.damageSources().source(MFTEDamageTypes.SOUL_DAMAGE), damageAmount);
                } else if (damageOnSoul > challengedSoul1.getHealth()) {
                    soulOwner.hurt(soulOwner.damageSources().source(MFTEDamageTypes.SOUL_DAMAGE), challengedSoul1.getHealth() * challengedSoul1.bonusPercent);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSoulCrushedEvent(LivingDeathEvent event) {
        var entity = event.getEntity();
        if (!entity.level.isClientSide) {
            if (entity instanceof ChallengedSoul challengedSoul) {
                var soulOwner = challengedSoul.getSummoner();
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
        if (entity instanceof ChallengedSoul challengedSoul) {
            if (caster == challengedSoul.getSummoner()) {
                var soulOwner = challengedSoul.getSummoner();
                punishingOwner(soulOwner);
            }
        }
    }

    public static void punishingOwner(LivingEntity livingEntity) {
        livingEntity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN, 200, 0));
        livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.SLOWED, 200, 3));
    }

}
