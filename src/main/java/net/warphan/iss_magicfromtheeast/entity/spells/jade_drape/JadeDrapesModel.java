package net.warphan.iss_magicfromtheeast.entity.spells.jade_drape;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class JadeDrapesModel extends GeoModel<JadeDrapesEntity> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/jade_drape_entity.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jade_drape.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/jade_drape.animation.json");

    @Override
    public ResourceLocation getModelResource(JadeDrapesEntity object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(JadeDrapesEntity object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(JadeDrapesEntity animatable) {
        return animationResource;
    }
}
