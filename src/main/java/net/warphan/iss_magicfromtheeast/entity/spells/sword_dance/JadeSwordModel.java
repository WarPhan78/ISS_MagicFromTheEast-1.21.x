package net.warphan.iss_magicfromtheeast.entity.spells.sword_dance;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class JadeSwordModel extends GeoModel<JadeSword> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/jade_sword.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jade_sword.png");

    public JadeSwordModel() {}

    @Override
    public ResourceLocation getModelResource(JadeSword object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(JadeSword object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(JadeSword animatable) {
        return null;
    }
}
