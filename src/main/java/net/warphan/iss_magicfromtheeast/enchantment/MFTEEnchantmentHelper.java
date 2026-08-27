package net.warphan.iss_magicfromtheeast.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.warphan.iss_magicfromtheeast.enchantment.enchantment_effects.GhostlyColdEnchantmentEffect;
import net.warphan.iss_magicfromtheeast.enchantment.enchantment_effects.SoulFlameEnchantmentEffect;
import net.warphan.iss_magicfromtheeast.entity.spirit_arrow.SpiritArrow;

/**
 * 1.20.1 port: the 1.21 version iterated the stack's enchantment holders and evaluated
 * enchantment effect components (PROJECTILE_SPAWNED, SOUL_DAMAGE, MANA_USE, AMMO_USE).
 * Effect components do not exist on 1.20.1, so the same behavior is computed directly from
 * {@link EnchantmentHelper#getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantment, ItemStack)}.
 * Method signatures are kept identical to the 1.21 version to avoid call-site churn.
 */
public class MFTEEnchantmentHelper {

    public static void onSpiritArrowSpawned(ServerLevel serverLevel, ItemStack stack, SpiritArrow spiritArrow) {
        int soulFlame = EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.SOUL_FLAME.get(), stack);
        if (soulFlame > 0) {
            SoulFlameEnchantmentEffect.apply(serverLevel, soulFlame, spiritArrow);
        }
        int ghostlyCold = EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.GHOSTLY_COLD.get(), stack);
        if (ghostlyCold > 0) {
            GhostlyColdEnchantmentEffect.apply(serverLevel, ghostlyCold, spiritArrow);
        }
    }

    /**
     * 1.21 SOUL_DAMAGE component: AddValue(perLevel(1)) on spiritual_focus and inner_impact.
     */
    public static int modifySoulDamage(ServerLevel serverLevel, ItemStack stack, Entity entity, float amount) {
        int bonus = EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.SPIRITUAL_FOCUS.get(), stack)
                + EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.INNER_IMPACT.get(), stack);
        return (int) (amount + bonus);
    }

    /**
     * 1.21 MANA_USE component: AddValue(perLevel(-15)) on wisely_will.
     */
    public static int processManaUse(ServerLevel serverLevel, ItemStack stack, int amount) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.WISELY_WILL.get(), stack);
        return amount - 15 * level;
    }

    /**
     * 1.21 AMMO_USE component: AddValue(perLevel(1)) on expanding.
     */
    public static int increaseAmmoLoad(ServerLevel serverLevel, ItemStack stack, int amount) {
        return amount + EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.EXPANDING.get(), stack);
    }

//    TODO PORT 1.20.1: barrage enchantment was already disabled on 1.21 (kept for reference).
//    public static float processProjectileBarrage(ServerLevel serverLevel, ItemStack stack, Entity entity, float f) {
//        return Math.max(0.0F, f + 3.0F * EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.BARRAGE.get(), stack));
//    }
}
