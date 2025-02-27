package net.warphan.iss_magicfromtheeast.entity.spells.throw_circle;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class ThrowCircleEntityModel extends GeoModel<ThrowCircleEntity> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/throw_circle.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/throw_circle.png");

    @Override
    public ResourceLocation getModelResource(ThrowCircleEntity object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(ThrowCircleEntity object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(ThrowCircleEntity animatable) {
        return null;
    }
}
