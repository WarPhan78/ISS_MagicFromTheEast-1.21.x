package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.effects.AnchoredSoulEffect;
import net.warphan.iss_magicfromtheeast.effects.MistStepEffect;
import net.warphan.iss_magicfromtheeast.effects.ReversalHealingEffect;
import net.warphan.iss_magicfromtheeast.effects.SoulburnEffect;

public class MFTEEffectRegistries{
    public static final DeferredRegister<MobEffect> MFTE_MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTE_MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }

    // TODO PORT 1.20.1: Attributes.SAFE_FALL_DISTANCE does not exist on 1.20.1 - the fall damage
    //  protection of Cloud Bless must be handled in event/effect logic instead of an attribute.
    public static final RegistryObject<MobEffect> CLOUD_BLESS_EFFECT = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("cloud_bless", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<MobEffect> SOULBURN = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("soulburn", () -> new SoulburnEffect(MobEffectCategory.HARMFUL, 0x00ffff));
    public static final RegistryObject<MobEffect> REVERSAL_HEALING = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("reversal_healing", () -> new ReversalHealingEffect(MobEffectCategory.BENEFICIAL, 0x00bb77)
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60101", 0.05f, AttributeModifier.Operation.ADDITION));
    // TODO PORT 1.20.1: generic JUMP_STRENGTH and SAFE_FALL_DISTANCE attributes do not exist on 1.20.1
    //  (jump boost / fall protection of Mist Step must be handled in the effect logic).
    //  STEP_HEIGHT -> ForgeMod.STEP_HEIGHT_ADDITION, WATER_MOVEMENT_EFFICIENCY -> ForgeMod.SWIM_SPEED.
    public static final RegistryObject<MobEffect> MIST_STEP = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("mist_step", () -> new MistStepEffect(MobEffectCategory.BENEFICIAL, 8356754)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60201", 0.5f, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60202", 0.5f, AttributeModifier.Operation.ADDITION)
            .addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60203", 0.5f, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> ANCHORED_SOUL = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("anchored_soul", () -> new AnchoredSoulEffect(MobEffectCategory.HARMFUL, 0X00ffff));

    public static final RegistryObject<MobEffect> FLAG_FIRE = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("effect_banner_red", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0xff3403)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60301", 0.10f, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(Attributes.ATTACK_SPEED, "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60302", 0.10f, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> FLAG_NATURE = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("effect_banner_green", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0x008000)
            .addAttributeModifier(Attributes.ARMOR, "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60401", 4, AttributeModifier.Operation.ADDITION)
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60402", 0.10f, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> FLAG_LIGHTNING = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("effect_banner_white", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0x560bad)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60501", 0.10f, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(AttributeRegistry.MANA_REGEN.get(), "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60502", 0.10f, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> FLAG_ICE = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("effect_banner_black", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0x4895ef)
            .addAttributeModifier(AttributeRegistry.SPELL_RESIST.get(), "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60601", 0.10f, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION.get(), "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60602", 0.10f, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> FLAG_HOLY = MFTE_MOB_EFFECT_DEFERRED_REGISTER.register("effect_banner_yellow", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0xffd301)
            .addAttributeModifier(Attributes.MAX_HEALTH, "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60701", 4, AttributeModifier.Operation.ADDITION)
            .addAttributeModifier(AttributeRegistry.CAST_TIME_REDUCTION.get(), "8f3f2d5a-2c40-4a41-a1b0-6f0a53c60702", 0.10f, AttributeModifier.Operation.MULTIPLY_BASE));
}
