package net.warphan.iss_magicfromtheeast.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpiritCrusherItem extends ExtendedSwordItem {
    public SpiritCrusherItem(MFTEExtendedWeaponTier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, components, flag);
        components.add(Component.translatable("item.iss_magicfromtheeast.spirit_crusher.description").
                withStyle(ChatFormatting.AQUA)
        );
    }
}
