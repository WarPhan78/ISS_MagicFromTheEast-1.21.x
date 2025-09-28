package net.warphan.iss_magicfromtheeast.spells;

import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class MFTESpellAnimations{
    public static ResourceLocation ANIMATION_RESOURCE = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "animation");

    public static final AnimationHolder ANIMATION_LOONG_CAST = new AnimationHolder(ISS_MagicFromTheEast.MOD_ID + ":loong_cast", true);
    public static final AnimationHolder ANIMATION_CALLING_IMPERMANENCE = new AnimationHolder(ISS_MagicFromTheEast.MOD_ID + ":calling_impermanence", true);
    public static final AnimationHolder ANIMATION_SOUL_EXTRACT = new AnimationHolder(ISS_MagicFromTheEast.MOD_ID + ":soul_extracting", true);
    public static final AnimationHolder ANIMATION_INVOKING = new AnimationHolder(ISS_MagicFromTheEast.MOD_ID + ":invoking_jiangshi", true);
    public static final AnimationHolder ANIMATION_KATANA_STRIKE_ACTION = new AnimationHolder(ISS_MagicFromTheEast.MOD_ID + ":katana_strike_action", true);
}
