package net.warphan.iss_magicfromtheeast.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 1.20.1 port of the {@code spiritual_focus} data-driven enchantment (1.21 parameters: weight 10,
 * maxLevel 5, dynamic cost (4,12)..(20,12), MAINHAND, exclusive with the other soulpiercer
 * function enchantment). Effect: +1 soul damage per level, applied through
 * {@link MFTEEnchantmentHelper#modifySoulDamage}.
 */
public class SpiritualFocusEnchantment extends Enchantment {
    public SpiritualFocusEnchantment() {
        super(Rarity.COMMON, MFTEEnchantments.SOULPIERCER_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 4 + 12 * (level - 1);
    }

    @Override
    public int getMaxCost(int level) {
        return 20 + 12 * (level - 1);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean checkCompatibility(Enchantment other) {
        // 1.21 tag minecraft:soulpiercer_function_exclusive = {spiritual_focus, wisely_will}
        return super.checkCompatibility(other)
                && !(other instanceof SpiritualFocusEnchantment)
                && !(other instanceof WiselyWillEnchantment);
    }
}
