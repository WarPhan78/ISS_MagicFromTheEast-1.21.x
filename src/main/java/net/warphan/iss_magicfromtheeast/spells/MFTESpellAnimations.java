package net.warphan.iss_magicfromtheeast.spells;

import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class MFTESpellAnimations{
    public static ResourceLocation ANIMATION_RESOURCE = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animation");

    public static final AnimationHolder ANIMATION_LOONG_CAST = new AnimationHolder(ISS_MagicFromTheEast.id("loong_cast"), true);
    public static final AnimationHolder ANIMATION_CALLING_IMPERMANENCE = new AnimationHolder(ISS_MagicFromTheEast.id("calling_impermanence"), true);
    public static final AnimationHolder ANIMATION_SOUL_EXTRACT = new AnimationHolder(ISS_MagicFromTheEast.id("soul_extracting"), true);
    public static final AnimationHolder ANIMATION_INVOKING = new AnimationHolder(ISS_MagicFromTheEast.id("invoking_jiangshi"), true);
    public static final AnimationHolder ANIMATION_KATANA_STRIKE_ACTION = new AnimationHolder(ISS_MagicFromTheEast.id("katana_strike_action"), true);
    public static final AnimationHolder ANIMATION_CIRCLE_DRAW = new AnimationHolder(ISS_MagicFromTheEast.id("circle_draw"), true);
}
