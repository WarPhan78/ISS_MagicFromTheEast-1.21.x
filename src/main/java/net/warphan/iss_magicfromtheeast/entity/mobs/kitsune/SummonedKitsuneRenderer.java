package net.warphan.iss_magicfromtheeast.entity.mobs.kitsune;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class SummonedKitsuneRenderer extends GeoEntityRenderer<SummonedKitsune> {
    public static final ResourceLocation textureLocation = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/kitsune.png");

    public SummonedKitsuneRenderer(EntityRendererProvider.Context context) {
        super(context, new SummonedKitsuneModel());
        this.shadowRadius = 0.55f;
    }

    @Override
    public void preRender(PoseStack poseStack, SummonedKitsune animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.scale(1.2f, 1.2f, 1.2f);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public ResourceLocation getTextureLocation(SummonedKitsune summonedKitsune) {
        return textureLocation;
    }
}
