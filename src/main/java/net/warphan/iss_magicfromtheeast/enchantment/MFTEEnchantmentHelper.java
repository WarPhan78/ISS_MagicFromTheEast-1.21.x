package net.warphan.iss_magicfromtheeast.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.warphan.iss_magicfromtheeast.entity.spirit_arrow.SpiritArrow;
import org.apache.commons.lang3.mutable.MutableFloat;

public class MFTEEnchantmentHelper extends EnchantmentHelper {

    public static void onSpiritArrowSpawned(ServerLevel serverLevel, ItemStack stack, SpiritArrow spiritArrow) {
        LivingEntity livingentity = spiritArrow.getOwner() instanceof LivingEntity livingentity1 ? livingentity1 : null;
        if (livingentity != null) {
            EnchantedItemInUse enchantediteminuse = new EnchantedItemInUse(stack, null, livingentity);
            runIterationOnItem(stack, (p_344580_, p_344581_) -> p_344580_.value().onProjectileSpawned(serverLevel, p_344581_, enchantediteminuse, spiritArrow));
        }
    }

    public static int modifyPowerScale(ServerLevel serverLevel, ItemStack stack, Entity entity, float amount) {
        MutableFloat mutableInt = new MutableFloat(amount);
        runIterationOnItem(stack, (p_344525_, p_344526_) -> {
            ((Enchantment) p_344525_.value()).modifyDamage(serverLevel, p_344526_, stack, entity, entity.damageSources().generic(), mutableInt);
        });
        return mutableInt.intValue();
    }

    public static int processManaUse(ServerLevel serverLevel, ItemStack stack, int amount) {
        MutableFloat mutablefloat = new MutableFloat((float) amount);
        runIterationOnItem(stack, (p_344545_, p_344546_) -> {
            ((Enchantment)p_344545_.value()).modifyAmmoCount(serverLevel, p_344546_, null, mutablefloat);
        });
        return mutablefloat.intValue();
    }
}
