package net.warphan.iss_magicfromtheeast.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

// NOTE PORT 1.20.1: enchantment exclusivity is no longer tag-driven on 1.20.1 - these tags are
// kept for the Enchantment classes to use in checkCompatibility (see enchantment package).
public interface MFTEEnchantmentTags {
    TagKey<Enchantment> SOULPIERCER_EFFECT_EXCLUSIVE = create("soulpiercer_effect_exclusive");
    TagKey<Enchantment> SOULPIERCER_FUNCTION_EXCLUSIVE = create("soulpiercer_function_exclusive");

    private static TagKey<Enchantment> create(String string) {
        return TagKey.create(Registries.ENCHANTMENT, new ResourceLocation(string));
    }
}
