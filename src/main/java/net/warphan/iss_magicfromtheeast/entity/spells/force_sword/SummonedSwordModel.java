package net.warphan.iss_magicfromtheeast.entity.spells.force_sword;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class SummonedSwordModel extends GeoModel<SummonedSword> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/summoned_sword.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/summoned_sword.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/summoned_sword.animation.json");

    @Override
    public ResourceLocation getModelResource(SummonedSword object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(SummonedSword object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(SummonedSword animatable) {
        return animationResource;
    }
}
