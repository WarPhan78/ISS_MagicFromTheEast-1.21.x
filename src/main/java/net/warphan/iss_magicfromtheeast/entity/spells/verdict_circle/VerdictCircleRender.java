package net.warphan.iss_magicfromtheeast.entity.spells.verdict_circle;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class VerdictCircleRender extends GeoEntityRenderer<VerdictCircle> {
    public static final ResourceLocation textureLocation = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/verdict_circle.png");

    public VerdictCircleRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VerdictCircleModel());
        this.shadowRadius = 0f;
    }

    @Override
    public ResourceLocation getTextureLocation(VerdictCircle animatable) {
        return textureLocation;
    }

    @Override
    public RenderType getRenderType(VerdictCircle animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
