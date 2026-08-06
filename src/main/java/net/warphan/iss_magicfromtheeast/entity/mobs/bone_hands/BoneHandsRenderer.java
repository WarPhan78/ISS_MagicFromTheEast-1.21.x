package net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BoneHandsRenderer extends GeoEntityRenderer<BoneHandsEntity> {
    public static final ResourceLocation textureLocation = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/bone_hand.png");

    public BoneHandsRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoneHandsModel());
        this.shadowRadius = 0f;
    }

    @Override
    public ResourceLocation getTextureLocation(BoneHandsEntity animatable) {
        return textureLocation;
    }

    @Override
    protected float getDeathMaxRotation(BoneHandsEntity degree) {
        return 0.0f;
    }

    @Override
    public void preRender(PoseStack poseStack, BoneHandsEntity animatable, BakedGeoModel model, @javax.annotation.Nullable MultiBufferSource bufferSource, @javax.annotation.Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.scale(2.5f, 2.5f, 2.5f);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(BoneHandsEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTicks) {
        return RenderType.energySwirl(texture, 0, 0);
    }
}
