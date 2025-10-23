package net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.render.IExtendedSimpleTexture;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class ExtractedSoulRenderer extends LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/entity/extracted_soul.png");
    private static final ResourceLocation TEXTURE_ALT = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/entity/extracted_soul_alt.png");
    final EntityModel<LivingEntity> originalModel;

    public ExtractedSoulRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.36f);
        this.originalModel = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER));
    }

    boolean rectangular = false;

    @Override
    public void render(LivingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        LivingEntity entityToRender = entity;
        this.rectangular = false;
        if (entity instanceof ExtractedSoul extractedSoul && extractedSoul.entityToCopy != null) {
            EntityRenderer<?> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(extractedSoul.entityToCopy);
            var fakeEntity = extractedSoul.entityToCopy.create(Minecraft.getInstance().level);
            if (fakeEntity instanceof LivingEntity livingFakeEntity) {
                ExtractedSoul.copyEntityVisualProperties(livingFakeEntity, entity);
                entityToRender = livingFakeEntity;
            }
            if (entityRenderer instanceof LivingEntityRenderer<?, ?> renderer) {
                this.model = (EntityModel<LivingEntity>) renderer.getModel();
                var texturelocation = ((LivingEntityRenderer) renderer).getTextureLocation(fakeEntity);
                var texture = Minecraft.getInstance().getTextureManager().getTexture(texturelocation);
                if (texture instanceof SimpleTexture) {
                    this.rectangular = ((IExtendedSimpleTexture) texture).irons_spellbooks$isRectangular();
                }
            }
        }
        try {
            super.render(entityToRender, entityYaw, partialTicks, poseStack, buffer, packedLight);
        } catch (Exception e) {
            ISS_MagicFromTheEast.LOGGER.error("Failed to render Extracted Soul of {}: {}", ((ExtractedSoul) entity).entityToCopy, e.getMessage());
            ((ExtractedSoul) entity).entityToCopy = null;
        }
        this.rectangular = false;
        this.model = originalModel;
    }

    @Override
    protected boolean shouldShowName(LivingEntity entity) {
        double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
        float f = entity.isCrouching() ? 32.0F : 64.0F;
        return d0 >= (double) (f * f) ? false : entity.isCustomNameVisible();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntity pEntity) {
        return rectangular ? TEXTURE_ALT : TEXTURE;
    }

    @Override
    protected float getBob(LivingEntity pLivingBase, float pPartialTick) {
        return 0;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    protected RenderType getRenderType(LivingEntity p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        return RenderHelper.CustomerRenderType.magic(getTextureLocation(p_115322_));
    }
}
