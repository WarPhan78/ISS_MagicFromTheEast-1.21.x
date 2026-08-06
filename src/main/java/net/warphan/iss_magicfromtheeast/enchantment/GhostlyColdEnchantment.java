package net.warphan.iss_magicfromtheeast.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 1.20.1 port of the {@code ghostly_cold} data-driven enchantment (1.21 parameters: weight 4,
 * maxLevel 1, constant cost 25..50, MAINHAND, exclusive with the other soulpiercer effect
 * enchantment). Its effect (flagging spawned SpiritArrows via
 * {@link net.warphan.iss_magicfromtheeast.enchantment.enchantment_effects.GhostlyColdEnchantmentEffect})
 * is applied from {@link MFTEEnchantmentHelper#onSpiritArrowSpawned}.
 */
public class GhostlyColdEnchantment extends Enchantment {
    public GhostlyColdEnchantment() {
        super(Rarity.RARE, MFTEEnchantments.SOULPIERCER_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 25;
    }

    @Override
    public int getMaxCost(int level) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean checkCompatibility(Enchantment other) {
        // 1.21 tag minecraft:soulpiercer_effect_exclusive = {soul_flame, ghostly_cold}
        return super.checkCompatibility(other)
                && !(other instanceof SoulFlameEnchantment)
                && !(other instanceof GhostlyColdEnchantment);
    }
}
