package net.warphan.iss_magicfromtheeast.enchantment.enchantment_effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.warphan.iss_magicfromtheeast.entity.spirit_arrow.SpiritArrow;

/**
 * TODO PORT 1.20.1: EnchantmentEntityEffect (enchantment effect components) does not exist on
 * 1.20.1. This used to be a PROJECTILE_SPAWNED effect component; it is now a plain helper invoked
 * from {@link net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantmentHelper#onSpiritArrowSpawned}.
 */
public final class SoulFlameEnchantmentEffect {

    private SoulFlameEnchantmentEffect() {
    }

    public static void apply(ServerLevel serverLevel, int enchantmentLevel, Entity entity) {
        if (entity instanceof SpiritArrow spiritArrow) {
            spiritArrow.setSoulFlame();
        }
    }
}
