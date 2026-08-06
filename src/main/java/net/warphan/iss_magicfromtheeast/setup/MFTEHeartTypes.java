package net.warphan.iss_magicfromtheeast.setup;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

import java.util.HashMap;
import java.util.Map;

/**
 * PORT 1.20.1: on 1.21 this class extended the Gui.HeartType enum via NeoForge enum
 * extensions. Forge 1.20.1 has no enum extension mechanism, so instead this class just
 * resolves the standalone heart textures used by {@link net.warphan.iss_magicfromtheeast.events.MFTEHeartOverlayHandler},
 * which redraws the health bar when Reversal Healing (jade) or Soulburn (soul) is active.
 */
public class MFTEHeartTypes {

    private static final Map<String, ResourceLocation> CACHE = new HashMap<>();

    public static ResourceLocation texture(String set, boolean half, boolean blinking, boolean hardcore) {
        String key = set + (hardcore ? "_hardcore" : "") + (half ? "_half" : "_full") + (blinking ? "_blinking" : "");
        return CACHE.computeIfAbsent(key, k ->
                new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/gui/sprites/hud/heart/" + k + ".png"));
    }
}
