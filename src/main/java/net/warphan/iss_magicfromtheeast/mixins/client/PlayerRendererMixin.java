package net.warphan.iss_magicfromtheeast.mixins.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.item.weapons.RepeatingCrossbow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context p_174289_, PlayerModel<AbstractClientPlayer> p_174290_, float p_174291_) {
        super(p_174289_, p_174290_, p_174291_);
    }

    @Inject(at = @At("HEAD"), method = "getArmPose", cancellable = true)
    private static void S$getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> callback) {
        ItemStack itemStack = player.getItemInHand(hand);
        // PORT 1.20.1: ammo amount data component -> NBT helper on RepeatingCrossbow.
        if (!player.swinging && itemStack.getItem() instanceof RepeatingCrossbow && !player.isUsingItem() && RepeatingCrossbow.getAmmoAmount(itemStack) > 0) {
            callback.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
        }
    }
}
