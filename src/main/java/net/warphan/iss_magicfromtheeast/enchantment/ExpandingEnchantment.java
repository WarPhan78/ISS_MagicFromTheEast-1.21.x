package net.warphan.iss_magicfromtheeast.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 1.20.1 port of the {@code expanding} data-driven enchantment (1.21 parameters: weight 10,
 * maxLevel 5, dynamic cost (5,10)..(15,10), MAINHAND). Effect: +1 loaded ammo per level, applied
 * through {@link MFTEEnchantmentHelper#increaseAmmoLoad}.
 */
public class ExpandingEnchantment extends Enchantment {
    public ExpandingEnchantment() {
        super(Rarity.COMMON, MFTEEnchantments.AMMO_LOAD_WEAPON_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 5 + 10 * (level - 1);
    }

    @Override
    public int getMaxCost(int level) {
        return 15 + 10 * (level - 1);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}
