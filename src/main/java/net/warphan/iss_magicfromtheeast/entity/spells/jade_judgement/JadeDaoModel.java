package net.warphan.iss_magicfromtheeast.entity.spells.jade_judgement;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class JadeDaoModel extends GeoModel<JadeDao> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/jade_dao.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jade_dao.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/jade_dao_animations.json");

    public JadeDaoModel() {
    }

    @Override
    public ResourceLocation getModelResource(JadeDao object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(JadeDao object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(JadeDao animatable) {
        return animationResource;
    }
}
