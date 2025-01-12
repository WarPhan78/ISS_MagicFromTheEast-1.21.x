package net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class JadeLoongModel extends GeoModel<JadeLoong> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/jade_loong.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jade_loong.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/jade_loong.animation.json");

    @Override
    public ResourceLocation getModelResource(JadeLoong object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(JadeLoong object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(JadeLoong animatable) {
        return animationResource;
    }
}
