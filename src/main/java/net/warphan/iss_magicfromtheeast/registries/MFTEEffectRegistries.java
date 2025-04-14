package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.effects.SoulburnEffect;

public class MFTEEffectRegistries extends MobEffectRegistry {
    public static final DeferredRegister<MobEffect> MFTE_MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTE_MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }

    public static final DeferredHolder<MobEffect, SummonTimer> SUMMON_SENTINEL_TIMER = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("summon_sentinel_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    //public static final DeferredHolder<MobEffect, MobEffect> SWORD_DANCE_EFFECT = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("sword_dance_effect", () -> new SwordDanceEffect(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final DeferredHolder<MobEffect, SummonTimer> SUMMON_JIANGSHI_TIMER = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("summon_jiangshi_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final DeferredHolder<MobEffect, MobEffect> SOULBURN = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("soulburn", () -> new SoulburnEffect(MobEffectCategory.HARMFUL, 0x00ffff));
}
