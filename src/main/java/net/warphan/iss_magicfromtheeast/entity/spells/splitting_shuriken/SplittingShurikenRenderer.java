package net.warphan.iss_magicfromtheeast.entity.spells.splitting_shuriken;

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

public class SplittingShurikenRenderer extends GeoEntityRenderer<SplittingShurikenProjectile> {
    private static final ResourceLocation TEXTURE = ISS_MagicFromTheEast.id("textures/entity/spirit_shuriken.png");

    public SplittingShurikenRenderer(EntityRendererProvider.Context context) {
        super(context, new SplittingShurikenModel());
        this.shadowRadius = 0;
    }

    @Override
    public void preRender(PoseStack poseStack, SplittingShurikenProjectile animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (animatable.isPrimary()) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        } else poseStack.scale(0.3f, 0.3f, 0.3f);
        Vec3 motion = animatable.getDeltaMovement();
        float xRot = -((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 90.0F);
        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) - 90.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(SplittingShurikenProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.energySwirl(texture, 0, 0);
    }

    @Override
    public ResourceLocation getTextureLocation(SplittingShurikenProjectile entity) {
        return TEXTURE;
    }

}
