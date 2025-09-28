package net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.Optional;

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
    public void preRender(PoseStack poseStack, JadeLoong animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        Vec3 motion = animatable.getDeltaMovement();
        float xRot = -((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 90.0F);
        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 90.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void render(JadeLoong jadeLoong, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Level level = jadeLoong.getCommandSenderWorld();
        Optional<GeoBone> anchor = model.getBone("particle_anchor");
        anchor.ifPresent((bone) ->
        {
            Vector3d pos = bone.getWorldPosition();
                level.addParticle(ParticleTypes.SCRAPE, pos.x, pos.y, pos.z, 0, 0, 0);
        });
        super.render(jadeLoong, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public RenderType getRenderType(JadeLoong animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.energySwirl(texture, 0, 0);
    }
}
