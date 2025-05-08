package net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class BoneHandsModel extends GeoModel<BoneHandsEntity>{
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/bone_hand.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/bone_hand.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/bone_hand.animation.json");


    @Override
    public ResourceLocation getModelResource(BoneHandsEntity animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(BoneHandsEntity animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(BoneHandsEntity animatable) {
        return animationResource;
    }
}
