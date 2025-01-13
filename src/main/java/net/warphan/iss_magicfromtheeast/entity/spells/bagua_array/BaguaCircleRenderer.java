package net.warphan.iss_magicfromtheeast.entity.spells.bagua_array;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class BaguaCircleRenderer extends GeoEntityRenderer<BaguaCircle> {
    public static final ResourceLocation textureLocation = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/bagua_circle.png");

    public BaguaCircleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BaguaCircleModel());
        this.shadowRadius = 0f;
    }

    @Override
    public ResourceLocation getTextureLocation(BaguaCircle animatable) {
        return textureLocation;
    }

    @Override
    public RenderType getRenderType(BaguaCircle animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.energySwirl(texture, 0, 0);
    }
}
