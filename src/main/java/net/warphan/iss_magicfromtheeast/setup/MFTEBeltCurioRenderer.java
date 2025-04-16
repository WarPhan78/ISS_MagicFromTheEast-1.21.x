package net.warphan.iss_magicfromtheeast.setup;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@OnlyIn(Dist.CLIENT)
public class MFTEBeltCurioRenderer implements ICurioRenderer {
    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource bufferSource, int light, float limbSwing, float limbSwingAmount, float partialTick, float ageInTick, float headYaw, float headPitch) {
        if (renderLayerParent.getModel() instanceof HumanoidModel<?>) {
            var humanoidModel = (HumanoidModel<LivingEntity>) renderLayerParent.getModel();

            poseStack.pushPose();
            humanoidModel.body.translateAndRotate(poseStack);
            poseStack.translate((slotContext.entity() != null && !slotContext.entity().getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? -5.5 : -4.5) * .0625f + 0.3, 0.7, 0);
            poseStack.scale(.625f, .625f, .625f);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, null, 0);
            poseStack.popPose();
        }
    }
}
