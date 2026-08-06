package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.BootsOfMistArmorModel;
import net.warphan.iss_magicfromtheeast.registries.MFTEArmorMaterialRegistries;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;

public class BootsOfMistArmorItem extends ImbuableChestplateArmorItem {
    public static final int COOLDOWN_TICKS = 20 * 15;

    public BootsOfMistArmorItem(Type type, Properties properties) {
        // PORT 1.20.1: the 1.21 AttributeContainers (+150 max mana, +10% spell power; sneaking
        // speed cut) are carried by the BOOTS_OF_MIST material (ISS 3.16.2-1.20.1 style).
        super(MFTEArmorMaterialRegistries.BOOTS_OF_MIST, type, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, components, tooltipFlag);
        components.add(Component.translatable("tooltip.irons_spellbooks.passive_ability",
                Component.literal(Utils.timeFromTicks(COOLDOWN_TICKS, 1)).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.DARK_PURPLE));
        components.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".description")).withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new BootsOfMistArmorModel());
    }
}
