package net.warphan.iss_magicfromtheeast.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 1.20.1 port of the {@code inner_impact} data-driven enchantment (1.21 parameters: weight 10,
 * maxLevel 5, dynamic cost (4,12)..(20,12), MAINHAND, exclusive with vanilla damage enchantments).
 * Effect: +1 soul damage per level, applied through {@link MFTEEnchantmentHelper#modifySoulDamage}.
 */
public class InnerImpactEnchantment extends Enchantment {
    public InnerImpactEnchantment() {
        super(Rarity.COMMON, MFTEEnchantments.SOUL_MELEE_WEAPON_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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
        // 1.21: exclusive with minecraft:exclusive_set/damage (sharpness, smite, etc.)
        return super.checkCompatibility(other) && !(other instanceof DamageEnchantment);
    }
}
