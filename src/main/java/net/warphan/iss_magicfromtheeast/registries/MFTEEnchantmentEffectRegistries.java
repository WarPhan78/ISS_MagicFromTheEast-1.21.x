package net.warphan.iss_magicfromtheeast.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.enchantment.enchantment_effects.GhostlyColdEnchantmentEffect;
import net.warphan.iss_magicfromtheeast.enchantment.enchantment_effects.SoulFlameEnchantmentEffect;

import java.util.function.Supplier;

public class MFTEEnchantmentEffectRegistries {
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> MFTE_ENTITY_ENCHANTMENT_EFFECTS = DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, ISS_MagicFromTheEast.MOD_ID);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> SOUL_FLAME = MFTE_ENTITY_ENCHANTMENT_EFFECTS.register("soul_flame", () -> SoulFlameEnchantmentEffect.CODEC);
    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> GHOSTLY_COLD = MFTE_ENTITY_ENCHANTMENT_EFFECTS.register("ghostly_cold", () -> GhostlyColdEnchantmentEffect.CODEC);

    public static void register(IEventBus eventBus) {
        MFTE_ENTITY_ENCHANTMENT_EFFECTS.register(eventBus);
    }
}
