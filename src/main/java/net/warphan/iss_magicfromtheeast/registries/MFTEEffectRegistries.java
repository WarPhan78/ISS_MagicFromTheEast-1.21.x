package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.effects.CloudTimer;
import net.warphan.iss_magicfromtheeast.effects.ReversalHealingEffect;
import net.warphan.iss_magicfromtheeast.effects.SoulburnEffect;

public class MFTEEffectRegistries{
    public static final DeferredRegister<MobEffect> MFTE_MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTE_MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }

    public static final DeferredHolder<MobEffect, MobEffect> SUMMON_CLOUD_TIMER = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("summon_cloud_timer", () -> new CloudTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final DeferredHolder<MobEffect, MobEffect> SOULBURN = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("soulburn", () -> new SoulburnEffect(MobEffectCategory.HARMFUL, 0x00ffff));
    public static final DeferredHolder<MobEffect, MobEffect> REVERSAL_HEALING = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("reversal_healing", () -> new ReversalHealingEffect(MobEffectCategory.BENEFICIAL, 0x00bb77)
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ISS_MagicFromTheEast.id("mobeffect_reversal_healing"), 0.05f, AttributeModifier.Operation.ADD_VALUE));
}
