package net.warphan.iss_magicfromtheeast.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.warphan.iss_magicfromtheeast.entity.spirit_arrow.SpiritArrow;
import net.warphan.iss_magicfromtheeast.registries.MFTEDataComponentRegistries;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;

public class MFTEEnchantmentHelper extends EnchantmentHelper {

    public static void onSpiritArrowSpawned(ServerLevel serverLevel, ItemStack stack, SpiritArrow spiritArrow) {
        LivingEntity livingentity = spiritArrow.getOwner() instanceof LivingEntity livingentity1 ? livingentity1 : null;
        if (livingentity != null) {
            EnchantedItemInUse enchantediteminuse = new EnchantedItemInUse(stack, null, livingentity);
            runIterationOnItem(stack, (enchantmentHolder, i) -> enchantmentHolder.value().onProjectileSpawned(serverLevel, i, enchantediteminuse, spiritArrow));
        }
    }

    public static int modifySoulDamage(ServerLevel serverLevel, ItemStack stack, Entity entity, float amount) {
        MutableFloat mutableInt = new MutableFloat(amount);
        runIterationOnItem(stack, (enchantmentHolder, i) -> {
            ((Enchantment) enchantmentHolder.value()).modifyDamageFilteredValue(MFTEDataComponentRegistries.SOUL_DAMAGE.get(), serverLevel, i, stack, entity, entity.damageSources().generic(), mutableInt);
        });
        return mutableInt.intValue();
    }

    public static int processManaUse(ServerLevel serverLevel, ItemStack stack, int amount) {
        MutableFloat mutablefloat = new MutableFloat((float) amount);
        runIterationOnItem(stack, (enchantmentHolder, i) -> {
            ((Enchantment) enchantmentHolder.value()).modifyItemFilteredCount(MFTEDataComponentRegistries.MANA_USE.get(), serverLevel, i, null, mutablefloat);
        });
        return mutablefloat.intValue();
    }

    public static int increaseAmmoLoad(ServerLevel serverLevel, ItemStack stack, int amount) {
        MutableFloat mutableFloat = new MutableFloat((float) amount);
        runIterationOnItem(stack, (enchantmentHolder, i) -> {
            ((Enchantment) enchantmentHolder.value()).modifyAmmoCount(serverLevel, i, null, mutableFloat);
        });
        return mutableFloat.intValue();
    }
}
