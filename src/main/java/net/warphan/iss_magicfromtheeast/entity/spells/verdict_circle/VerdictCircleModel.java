package net.warphan.iss_magicfromtheeast.entity.spells.verdict_circle;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class VerdictCircleModel extends GeoModel<VerdictCircle> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/verdict_circle.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/verdict_circle.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/verdict_circle.animation.json");

    @Override
    public ResourceLocation getModelResource(VerdictCircle object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(VerdictCircle object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(VerdictCircle animatable) {
        return animationResource;
    }
}
