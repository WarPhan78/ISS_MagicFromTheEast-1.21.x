package net.warphan.iss_magicfromtheeast.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 1.20.1 port of the {@code wisely_will} data-driven enchantment (1.21 parameters: weight 5,
 * maxLevel 3, dynamic cost (3,9)..(18,9), MAINHAND, exclusive with the other soulpiercer
 * function enchantment). Effect: -15 mana use per level, applied through
 * {@link MFTEEnchantmentHelper#processManaUse}.
 */
public class WiselyWillEnchantment extends Enchantment {
    public WiselyWillEnchantment() {
        super(Rarity.UNCOMMON, MFTEEnchantments.SOULPIERCER_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 3 + 9 * (level - 1);
    }

    @Override
    public int getMaxCost(int level) {
        return 18 + 9 * (level - 1);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean checkCompatibility(Enchantment other) {
        // 1.21 tag minecraft:soulpiercer_function_exclusive = {spiritual_focus, wisely_will}
        return super.checkCompatibility(other)
                && !(other instanceof SpiritualFocusEnchantment)
                && !(other instanceof WiselyWillEnchantment);
    }
}
