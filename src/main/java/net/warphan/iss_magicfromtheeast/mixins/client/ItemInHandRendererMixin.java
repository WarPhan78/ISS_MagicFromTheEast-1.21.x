package net.warphan.iss_magicfromtheeast.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.item.weapons.RepeatingCrossbow;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow protected abstract void applyItemArmTransform(PoseStack p_109383_, HumanoidArm p_109384_, float p_109385_);
    @Shadow protected abstract void applyItemArmAttackTransform(PoseStack p_109336_, HumanoidArm p_109337_, float p_109338_);
    @Shadow public abstract void renderItem(LivingEntity p_270072_, ItemStack p_270793_, ItemDisplayContext p_270837_, boolean p_270203_, PoseStack p_270974_, MultiBufferSource p_270686_, int p_270103_);

    @Inject(at = @At("HEAD"), method = "evaluateWhichHandsToRender", cancellable = true)
    private static void S$evaluateWhichHandsToRender(LocalPlayer localPlayer, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> callback) {
        ItemStack itemstack = localPlayer.getUseItem();
        ItemStack mainStack = localPlayer.getMainHandItem();
        ItemStack offStack = localPlayer.getOffhandItem();
        if (offStack.is(MFTEItemRegistries.REPEATING_CROSSBOW.get()) && !mainStack.isEmpty()) {
            callback.setReturnValue(ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY);
        }
        if (itemstack.getItem() instanceof RepeatingCrossbow) {
            if (localPlayer.isUsingItem()) {
                InteractionHand interactionHand = localPlayer.getUsedItemHand();
                callback.setReturnValue(ItemInHandRenderer.HandRenderSelection.onlyForHand(interactionHand));
            }
            else
                // PORT 1.20.1: ammo amount data component -> NBT helper on RepeatingCrossbow.
                callback.setReturnValue(RepeatingCrossbow.getAmmoAmount(itemstack) > 0
                ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS);
        }
    }

    @Inject(at = @At("HEAD"), method = "selectionUsingItemWhileHoldingBowLike", cancellable = true)
    private static void S$selectionUsingItemWhileHoldingBowLike(LocalPlayer localPlayer, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> callback) {
        ItemStack stack = localPlayer.getUseItem();
        InteractionHand interactionHand = localPlayer.getUsedItemHand();
        if (stack.getItem() instanceof RepeatingCrossbow) {
            callback.setReturnValue(ItemInHandRenderer.HandRenderSelection.onlyForHand(interactionHand));
        }
    }

    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    private void renderRepeatingCrossbowItem(AbstractClientPlayer player, float v, float v1, InteractionHand hand, float v2, ItemStack stack, float v3, PoseStack poseStack, MultiBufferSource bufferSource, int i0, CallbackInfo callback) {
        if (!player.isScoping()) {
            boolean flag3;
            float f12;
            float f11;
            float f14;
            float f17;
            float f7;
            if (stack.getItem() instanceof RepeatingCrossbow) {
                if (player.getUseItem() == stack) {
                    boolean flag = hand == InteractionHand.MAIN_HAND;
                    HumanoidArm humanoidarm = flag ? player.getMainArm().getOpposite() : player.getMainArm();

                    callback.cancel();
                    poseStack.pushPose();
                    flag3 = RepeatingCrossbow.getAmmoAmount(stack) > 0;
                    boolean flag2 = humanoidarm == HumanoidArm.LEFT;
                    int i = flag2 ? 1 : -1;
                    if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
                        this.applyItemArmTransform(poseStack, humanoidarm.getOpposite(), v3);
                        poseStack.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
                        poseStack.mulPose(Axis.XP.rotationDegrees(-11.935F));
                        poseStack.mulPose(Axis.YP.rotationDegrees((float) i * 65.3F));
                        poseStack.mulPose(Axis.ZP.rotationDegrees((float) i * -9.785F));
                        f12 = (float) stack.getUseDuration() - ((float) player.getUseItemRemainingTicks() - v + 1.0F);
                        f7 = f12 / (float) RepeatingCrossbow.getUsingDuration(stack, player);
                        if (f7 > 1.0F) {
                            f7 = 1.0F;
                        }

                        if (f7 > 0.1F) {
                            f11 = Mth.sin((f12 - 0.1F) * 1.3F);
                            f14 = f7 - 0.1F;
                            f17 = f11 * f14;
                            poseStack.translate(f17 * 0.0F, f17 * 0.004F, f17 * 0.0F);
                        }

                        poseStack.translate(f7 * 0.0F, f7 * 0.0F, f7 * 0.04F);
                        poseStack.scale(1.0F, 1.0F, 1.0F + f7 * 0.2F);
                        poseStack.mulPose(Axis.YN.rotationDegrees((float) i * 45.0F));
                    } else {
                        f12 = -0.4F * Mth.sin(Mth.sqrt(v2) * 3.1415927F);
                        f7 = 0.2F * Mth.sin(Mth.sqrt(v2) * 6.2831855F);
                        f11 = -0.2F * Mth.sin(v2 * 3.1415927F);
                        poseStack.translate((float) i * f12, f7, f11);
                        this.applyItemArmTransform(poseStack, humanoidarm.getOpposite(), v3);
                        this.applyItemArmAttackTransform(poseStack, humanoidarm.getOpposite(), v2);
                        if (flag3 && v2 < 0.001F && flag) {
                            poseStack.translate((float) i * -0.641864F, 0.0F, 0.0F);
                            poseStack.mulPose(Axis.YP.rotationDegrees((float) i * 10.0F));
                        }
                    }

                    this.renderItem(player, stack, flag2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !flag2, poseStack, bufferSource, i0);
                    poseStack.popPose();
                } else {
                    hand = InteractionHand.MAIN_HAND;
                    HumanoidArm humanoidarm = player.getMainArm();

                    callback.cancel();
                    poseStack.pushPose();
                    flag3 = RepeatingCrossbow.getAmmoAmount(stack) > 0;
                    boolean flag2 = humanoidarm == HumanoidArm.RIGHT;
                    int i = flag2 ? 1 : -1;
                    if (player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
                        this.applyItemArmTransform(poseStack, humanoidarm, v3);
                        poseStack.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
                        poseStack.mulPose(Axis.XP.rotationDegrees(-11.935F));
                        poseStack.mulPose(Axis.YP.rotationDegrees((float) i * 65.3F));
                        poseStack.mulPose(Axis.ZP.rotationDegrees((float) i * -9.785F));
                        f12 = (float) stack.getUseDuration() - ((float) player.getUseItemRemainingTicks() - v + 1.0F);
                        f7 = f12 / (float) RepeatingCrossbow.getUsingDuration(stack, player);
                        if (f7 > 1.0F) {
                            f7 = 1.0F;
                        }

                        if (f7 > 0.1F) {
                            f11 = Mth.sin((f12 - 0.1F) * 1.3F);
                            f14 = f7 - 0.1F;
                            f17 = f11 * f14;
                            poseStack.translate(f17 * 0.0F, f17 * 0.004F, f17 * 0.0F);
                        }

                        poseStack.translate(f7 * 0.0F, f7 * 0.0F, f7 * 0.04F);
                        poseStack.scale(1.0F, 1.0F, 1.0F + f7 * 0.2F);
                        poseStack.mulPose(Axis.YN.rotationDegrees((float) i * 45.0F));
                    } else {
                        f12 = -0.4F * Mth.sin(Mth.sqrt(v2) * 3.1415927F);
                        f7 = 0.2F * Mth.sin(Mth.sqrt(v2) * 6.2831855F);
                        f11 = -0.2F * Mth.sin(v2 * 3.1415927F);
                        poseStack.translate((float) i * f12, f7, f11);
                        this.applyItemArmTransform(poseStack, humanoidarm, v3);
                        this.applyItemArmAttackTransform(poseStack, humanoidarm, v2);
                        if (flag3 && v2 < 0.001F) {
                            poseStack.translate((float) i * -0.641864F, 0.0F, 0.0F);
                            poseStack.mulPose(Axis.YP.rotationDegrees((float) i * 10.0F));
                        }
                    }

                    this.renderItem(player, stack, flag2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !flag2, poseStack, bufferSource, i0);
                    poseStack.popPose();
                }
            }
        }
    }
}
