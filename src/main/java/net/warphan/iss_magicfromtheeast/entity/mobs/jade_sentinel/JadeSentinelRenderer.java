package net.warphan.iss_magicfromtheeast.entity.mobs.jade_sentinel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class JadeSentinelRenderer extends GeoEntityRenderer<JadeSentinel> {
    public static final ResourceLocation textureLocation = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jade_sentinel.png");

    public JadeSentinelRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new JadeSentinelModel());
        this.shadowRadius = 2f;
    }

    @Override
    public ResourceLocation getTextureLocation(JadeSentinel animatable) {
        return textureLocation;
    }

    @Override
    public void preRender(PoseStack poseStack, JadeSentinel animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(JadeSentinel animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
