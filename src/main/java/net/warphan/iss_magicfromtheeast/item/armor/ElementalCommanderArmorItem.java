package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.ElementalCommanderArmorModel;
import net.warphan.iss_magicfromtheeast.registries.MFTEArmorMaterialRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEAttributeRegistries;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;

public class ElementalCommanderArmorItem extends ImbuableChestplateArmorItem {
    public static final int COOLDOWN_TICKS = 20 * 15;

    public ElementalCommanderArmorItem(Type slot, Properties settings) {
        // PORT 1.20.1: the 1.21 AttributeContainers (+150 max mana, +10% spell power) are carried
        // by the ELEMENTAL_COMMANDER material (ISS 3.16.2-1.20.1 style).
        super(MFTEArmorMaterialRegistries.ELEMENTAL_COMMANDER, slot, settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.irons_spellbooks.passive_ability",
                Component.literal(Utils.timeFromTicks(COOLDOWN_TICKS, 1)).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.DARK_PURPLE));
        tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".description")).withStyle(ChatFormatting.LIGHT_PURPLE));

        //Shift Tooltip
        TooltipsUtils.addShiftTooltip(tooltipComponents, List.of(
                Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".flag_red")).withStyle(ChatFormatting.RED),
                Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".flag_green")).withStyle(ChatFormatting.GREEN),
                Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".flag_white")).withStyle(ChatFormatting.BLUE),
                Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".flag_black")).withStyle(ChatFormatting.AQUA),
                Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".flag_yellow")).withStyle(ChatFormatting.YELLOW)));

        tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".effect_bonus",
                Component.literal(Utils.stringTruncation(getDisplayEffectBonus(MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.DARK_AQUA))).withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    public static double getDisplayEffectBonus(@Nullable Entity entity) {
        double effectBonus;
        if (entity instanceof LivingEntity livingEntity) {
            effectBonus = (livingEntity.getAttributeValue(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER.get()) / 4) * 100;
            return effectBonus;
        }
        return 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new ElementalCommanderArmorModel());
    }
}
