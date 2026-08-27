package net.warphan.iss_magicfromtheeast.entity.mobs.kitsune;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import javax.annotation.Nullable;

public class SummonedKitsuneAlphaRenderer extends SummonedKitsuneRenderer{
    public SummonedKitsuneAlphaRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.85f;
    }

    @Override
    public void preRender(PoseStack poseStack, SummonedKitsune animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.scale(1.8f, 1.8f, 1.8f);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}
