package net.warphan.iss_magicfromtheeast.entity.spells.nephrite_slash;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class NephriteCrystalRenderer extends EntityRenderer<NephriteCrystalEntity> {

    private final NephriteCrystalModel model;

    public NephriteCrystalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new NephriteCrystalModel(context.bakeLayer(NephriteCrystalModel.LAYER_LOCATION));
    }

    public void render(NephriteCrystalEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        if (entity.tickCount < entity.getWaitTime())
            return;
        float f = entity.tickCount + partialTicks;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
        float anim = entity.getPositionOffset(partialTicks);
        poseStack.scale(1, -1, 1);
        float scale = entity.getCrystalSize();
        scale = (scale - 1) * .25f + 1;
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0, -anim * (22 + 22 + 24) / 16f, 0);

        this.model.setupAnim(entity, partialTicks, 0.0F, 0.0F, entity.getYRot(), entity.getXRot());
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(NephriteCrystalEntity pEntity) {
        return ISS_MagicFromTheEast.id("textures/entity/nephrite_crystal.png");
    }

    public static class NephriteCrystalModel extends EntityModel<NephriteCrystalEntity> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "nephrite_crystal"), "main");
        private final ModelPart bottom;
        private final ModelPart middle;
        private final ModelPart top;

        public NephriteCrystalModel(ModelPart root) {
            this.bottom = root.getChild("bottom");
            this.middle = root.getChild("middle");
            this.top = root.getChild("top");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -25.0F, -9.0F, 8.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 25.0F, 4.0F));

            PartDefinition middle = partdefinition.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -25.0F, -1.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 3.0F, -3.0F));

            PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(32, 34).addBox(-2.0F, -25.0F, -4.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -19.0F, 1.0F));

            return LayerDefinition.create(meshdefinition, 48, 48);
        }

        @Override
        public void setupAnim(NephriteCrystalEntity entity, float partialTicks, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            float scale = entity.getCrystalSize();
            top.visible = false;
            bottom.visible = false;

            int ypos = 26;
            if (scale >= 3) {
                bottom.visible = true;
                ypos -= 15;
                bottom.y = ypos;
            }
            ypos -= 12;
            middle.y = ypos;
            if (scale >= 2) {
                top.visible = true;
                ypos -= 10;
                top.y = ypos;
            }
        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            bottom.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            middle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            top.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}
