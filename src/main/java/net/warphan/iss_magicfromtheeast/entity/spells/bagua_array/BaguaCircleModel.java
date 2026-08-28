package net.warphan.iss_magicfromtheeast.entity.spells.bagua_array;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class BaguaCircleModel extends GeoModel<BaguaCircle> {
    public static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "geo/bagua_circle.geo.json");
    public static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/entity/bagua_circle.png");
    public static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "animations/bagua_circle.animation.json");

    @Override
    public ResourceLocation getModelResource(BaguaCircle object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(BaguaCircle object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(BaguaCircle animatable) {
        return animationResource;
    }
}
