package net.warphan.iss_magicfromtheeast.setup;

import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

import java.util.List;

public class MFTEHeartTypes {

    public static final EnumProxy<Gui.HeartType> JADE_HEART_PROXY = new EnumProxy<>(Gui.HeartType.class, createParameter("jade"));
    public static final EnumProxy<Gui.HeartType> SOUL_HEART_PROXY = new EnumProxy<>(Gui.HeartType.class, createParameter("soul"));

    private static List<Object> createParameter(String type) {
        return List.of(
        ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "hud/heart/" + type + "_full"),
        ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "hud/heart/" + type + "_full_blinking"),
        ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "hud/heart/" + type + "_half"),
        ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "hud/heart/" + type + "_half_blinking"),
        ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "hud/heart/" + type + "_hardcore_full"),
        ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "hud/heart/" + type + "_hardcore_full_blinking"),
        ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "hud/heart/" + type + "_hardcore_half"),
        ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "hud/heart/" + type + "_hardcore_half_blinking")
        );
    }
}
