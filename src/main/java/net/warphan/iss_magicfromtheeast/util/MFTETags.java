package net.warphan.iss_magicfromtheeast.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class MFTETags {
    public static final TagKey<Item> SYMMETRY_FOCUS = ItemTags.create(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "symmetry_focus"));
    public static final TagKey<Item> SPIRIT_FOCUS = ItemTags.create(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "spirit_focus"));
    public static final TagKey<Item> DUNE_FOCUS = ItemTags.create(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "dune_focus.json"));

    public static final TagKey<Item> SOULPIERCER = ItemTags.create(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "soulpiercer"));
    public static final TagKey<Item> REPEATING_CROSSBOW = ItemTags.create(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "repeating_crossbow"));

    public static final TagKey<BannerPattern> BALANCE_PATTERN_ITEM = bannerTag("pattern_item/balance");

    private static TagKey<BannerPattern> bannerTag(String name) {
        return TagKey.create(Registries.BANNER_PATTERN, ISS_MagicFromTheEast.id(name));
    }
}
