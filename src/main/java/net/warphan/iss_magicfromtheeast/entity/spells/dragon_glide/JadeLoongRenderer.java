package net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class JadeLoongRenderer extends GeoEntityRenderer<JadeLoong> {
    public static final ResourceLocation textureLocation = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jade_loong.png");

    public JadeLoongRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new JadeLoongModel());
        this.shadowRadius = 0f;
    }

    @Override
    public ResourceLocation getTextureLocation(JadeLoong animatable) {
        return textureLocation;
    }

    @Override
    public RenderType getRenderType(JadeLoong animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.energySwirl(texture, 0, 0);
    }
}
