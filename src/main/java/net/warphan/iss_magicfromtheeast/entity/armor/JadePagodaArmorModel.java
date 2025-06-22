package net.warphan.iss_magicfromtheeast.entity.armor;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.armor.JadePagodaArmorItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class JadePagodaArmorModel extends DefaultedItemGeoModel<JadePagodaArmorItem> {
    public JadePagodaArmorModel() {
        super(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(JadePagodaArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/jade_pagoda.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JadePagodaArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/models/armor/jade_pagoda.png");
    }

    @Override
    public ResourceLocation getAnimationResource(JadePagodaArmorItem animatable) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID,"animations/master_armor_animation.json");
    }
}
