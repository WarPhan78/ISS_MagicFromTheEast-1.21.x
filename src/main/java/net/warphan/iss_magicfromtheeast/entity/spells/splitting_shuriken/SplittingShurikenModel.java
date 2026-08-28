package net.warphan.iss_magicfromtheeast.entity.spells.splitting_shuriken;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class SplittingShurikenModel extends GeoModel<SplittingShurikenProjectile> {
    public static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "geo/spirit_shuriken.geo.json");
    public static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/entity/spirit_shuriken.png");
    public static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "animations/shuriken.animation.json");

    @Override
    public ResourceLocation getModelResource(SplittingShurikenProjectile object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(SplittingShurikenProjectile object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(SplittingShurikenProjectile animatable) {
        return animationResource;
    }
}
