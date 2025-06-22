package net.warphan.iss_magicfromtheeast.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public interface MFTEEnchantmentTags {
    TagKey<Enchantment> SOULPIERCER_EFFECT_EXCLUSIVE = create("soulpiercer_effect_exclusive");
    TagKey<Enchantment> SOULPIERCER_FUNCTION_EXCLUSIVE = create("soulpiercer_function_exclusive");

    private static TagKey<Enchantment> create(String string) {
        return TagKey.create(Registries.ENCHANTMENT, ResourceLocation.withDefaultNamespace(string));
    }
}
