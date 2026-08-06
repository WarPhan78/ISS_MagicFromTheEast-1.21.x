package net.warphan.iss_magicfromtheeast.setup;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;

import java.util.function.UnaryOperator;

/**
 * PORT 1.20.1: EnumProxy does not exist on Forge - custom rarities use Forge's
 * IExtensibleEnum extension via {@code Rarity.create(String, UnaryOperator<Style>)}.
 * Call sites use {@link #BLOODFUL}/{@link #JADELIGHT} directly (formerly *_RARITY_PROXY.getValue()).
 */
public class MFTERarity {
    public static final Rarity BLOODFUL = Rarity.create("iss_magicfromtheeast:bloodful",
            (UnaryOperator<Style>) ((style) -> style.withColor(0xd10000)));
    public static final Rarity JADELIGHT = Rarity.create("iss_magicfromtheeast:jadelight",
            (UnaryOperator<Style>) ((style) -> style.withColor(0x77dd77)));
}
