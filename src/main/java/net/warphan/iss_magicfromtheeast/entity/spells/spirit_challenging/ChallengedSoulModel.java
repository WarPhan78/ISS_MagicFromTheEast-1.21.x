package net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class ChallengedSoulModel extends GeoModel<ChallengedSoul> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/challenged_soul.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/challenged_soul.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/challenged_soul.animation.json");


    @Override
    public ResourceLocation getModelResource(ChallengedSoul animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(ChallengedSoul animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(ChallengedSoul animatable) {
        return animationResource;
    }
}
