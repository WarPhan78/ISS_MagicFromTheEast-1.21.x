package net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class JadeExecutionerModel extends GeoModel<JadeExecutionerEntity> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/jade_executioner.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jade_executioner.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/jade_executioner.animation.json");

    @Override
    public ResourceLocation getModelResource(JadeExecutionerEntity animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(JadeExecutionerEntity animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(JadeExecutionerEntity animatable) {
        return animationResource;
    }
}
