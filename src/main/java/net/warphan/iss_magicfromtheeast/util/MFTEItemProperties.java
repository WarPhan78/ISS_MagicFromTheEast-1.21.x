package net.warphan.iss_magicfromtheeast.util;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.item.weapons.RepeatingCrossbow;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;

@OnlyIn(Dist.CLIENT)
public class MFTEItemProperties {
    public static void addCustomItemProperties() {
        makeCustomBow(MFTEItemRegistries.SOULPIERCER.get());
        makeRepeatingCrossbow(MFTEItemRegistries.REPEATING_CROSSBOW.get());
    }

    // NOTE PORT 1.20.1: ItemStack#getUseDuration takes no LivingEntity on 1.20.1;
    //  RepeatingCrossbow#getChargeDuration should follow vanilla 1.20.1 CrossbowItem (ItemStack only).
    private static void makeCustomBow(Item item) {
        ItemProperties.register(item, new ResourceLocation("pull"), (p_344163_, p_344164_, p_344165_, p_344166_) -> {
            if (p_344165_ == null) {
                return 0.0F;
            } else {
                return p_344165_.getUseItem() != p_344163_ ? 0.0F : (float)(p_344163_.getUseDuration() - p_344165_.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(
                item,
                new ResourceLocation("pulling"),
                (p_174630_, p_174631_, p_174632_, p_174633_) -> p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F
        );
    }

    private static void makeRepeatingCrossbow(Item item) {
        ItemProperties.register(item, new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return RepeatingCrossbow.isCharged(itemStack) ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (float)RepeatingCrossbow.getChargeDuration(itemStack);
            }
        });
        ItemProperties.register(item, new ResourceLocation("pulling"), (itemStack, level, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack && !RepeatingCrossbow.isCharged(itemStack) ? 1.0F : 0.0F;
        });
        ItemProperties.register(item, new ResourceLocation("charged"), (itemStack, level, livingEntity, i) -> {
            return RepeatingCrossbow.isCharged(itemStack) ? 1.0F : 0.0F;
        });
    }
}
