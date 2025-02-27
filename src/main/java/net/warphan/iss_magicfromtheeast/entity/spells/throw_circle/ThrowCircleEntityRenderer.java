package net.warphan.iss_magicfromtheeast.entity.spells.throw_circle;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class ThrowCircleEntityRenderer extends GeoEntityRenderer<ThrowCircleEntity> {
    public static final ResourceLocation textureLocation = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/throw_circle.png");

    public ThrowCircleEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ThrowCircleEntityModel());
        this.shadowRadius = 0f;
    }

    @Override
    public ResourceLocation getTextureLocation(ThrowCircleEntity animatable) {
        return textureLocation;
    }

    @Override
    public RenderType getRenderType(ThrowCircleEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.energySwirl(texture, 0, 0);
    }
}
