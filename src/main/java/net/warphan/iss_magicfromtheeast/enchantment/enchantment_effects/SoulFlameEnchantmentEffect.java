package net.warphan.iss_magicfromtheeast.enchantment.enchantment_effects;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.entity.spirit_arrow.SpiritArrow;

public record SoulFlameEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<SoulFlameEnchantmentEffect> CODEC = MapCodec.unit(SoulFlameEnchantmentEffect::new);

    @Override
    public void apply(ServerLevel serverLevel, int i, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
        if (entity instanceof SpiritArrow spiritArrow) {
            spiritArrow.setSoulFlame();
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
