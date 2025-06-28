package net.warphan.iss_magicfromtheeast.util;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;

public class MFTEItemProperties {
    public static void addCustomItemProperties() {
        makeCustomBow(MFTEItemRegistries.SOULPIERCER.get());
        makeCustomCrossbow(MFTEItemRegistries.REPEATING_CROSSBOW.get());
    }

    private static void makeCustomBow(Item item) {
        ItemProperties.register(item, ResourceLocation.withDefaultNamespace("pull"), (p_344163_, p_344164_, p_344165_, p_344166_) -> {
            if (p_344165_ == null) {
                return 0.0F;
            } else {
                return p_344165_.getUseItem() != p_344163_ ? 0.0F : (float)(p_344163_.getUseDuration(p_344165_) - p_344165_.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(
                item,
                ResourceLocation.withDefaultNamespace("pulling"),
                (p_174630_, p_174631_, p_174632_, p_174633_) -> p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F
        );
    }

    private static void makeCustomCrossbow(Item item) {
        ItemProperties.register(item, ResourceLocation.withDefaultNamespace("pull"), (p_351682_, p_351683_, p_351684_, p_351685_) -> {
            if (p_351684_ == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(p_351682_) ? 0.0F : (float)(p_351682_.getUseDuration(p_351684_) - p_351684_.getUseItemRemainingTicks()) / (float)CrossbowItem.getChargeDuration(p_351682_, p_351684_);
            }
        });
        ItemProperties.register(item,
                ResourceLocation.withDefaultNamespace("pulling"),
                (p_174605_, p_174606_, p_174607_, p_174608_) -> {
            return p_174607_ != null && p_174607_.isUsingItem() && p_174607_.getUseItem() == p_174605_ && !CrossbowItem.isCharged(p_174605_) ? 1.0F : 0.0F;
        });
        ItemProperties.register(item,
                ResourceLocation.withDefaultNamespace("charged"),
                (p_275891_, p_275892_, p_275893_, p_275894_) -> {
            return CrossbowItem.isCharged(p_275891_) ? 1.0F : 0.0F;
        });
    }
}
