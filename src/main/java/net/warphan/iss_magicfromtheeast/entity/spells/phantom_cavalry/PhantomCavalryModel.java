package net.warphan.iss_magicfromtheeast.entity.spells.phantom_cavalry;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class PhantomCavalryModel extends GeoModel<PhantomCavalryVisualEntity> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/phantom_cavalry.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/phantom_cavalry.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/cavalry.animation.json");

    @Override
    public ResourceLocation getModelResource(PhantomCavalryVisualEntity object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(PhantomCavalryVisualEntity object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(PhantomCavalryVisualEntity animatable) {
        return animationResource;
    }
}
