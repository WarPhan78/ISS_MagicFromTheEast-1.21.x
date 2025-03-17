package net.warphan.iss_magicfromtheeast.util;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class MFTETags {
    public static final TagKey<Item> SYMMETRY_FOCUS = ItemTags.create(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "symmetry_focus"));
    public static final TagKey<Item> SPIRIT_FOCUS = ItemTags.create(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "spirit_focus"));
    public static final TagKey<Item> DUNE_FOCUS = ItemTags.create(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "dune_focus.json"));
}
