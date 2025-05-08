package net.warphan.iss_magicfromtheeast.entity.spells.soul_skull;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.model.GeoModel;

public class SoulSkullModel extends GeoModel<SoulSkullProjectile> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/soul_skull.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/soul_skull.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/soul_skull.animation.json");

    @Override
    public ResourceLocation getModelResource(SoulSkullProjectile object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(SoulSkullProjectile object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(SoulSkullProjectile animatable) {
        return animationResource;
    }
}
