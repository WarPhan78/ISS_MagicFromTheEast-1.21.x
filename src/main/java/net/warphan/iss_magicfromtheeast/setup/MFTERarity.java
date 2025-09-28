package net.warphan.iss_magicfromtheeast.setup;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class MFTERarity {
    public static final EnumProxy<Rarity> BLOODFUL_RARITY_PROXY = new EnumProxy<>(Rarity.class,
            -1,
            "iss_magicfromtheeast:bloodful",
            (UnaryOperator<Style>) ((style) -> style.withColor(0xd10000)));
    public static final EnumProxy<Rarity> JADELIGHT_RARITY_PROXY = new EnumProxy<>(Rarity.class,
            -1,
            "iss_magicfromtheeast:jadelight",
            (UnaryOperator<Style>) ((style) -> style.withColor(0x77dd77)));
}
