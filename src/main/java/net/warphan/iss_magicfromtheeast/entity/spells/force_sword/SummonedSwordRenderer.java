package net.warphan.iss_magicfromtheeast.entity.spells.force_sword;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class SummonedSwordRenderer extends GeoEntityRenderer<SummonedSword> {
    public static final ResourceLocation textureLocation = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/summoned_sword.png");

    public SummonedSwordRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SummonedSwordModel());
        this.shadowRadius = 0.2f;
    }

    @Override
    public ResourceLocation getTextureLocation(SummonedSword animatable) {
        return textureLocation;
    }

    @Override
    public RenderType getRenderType(SummonedSword animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
