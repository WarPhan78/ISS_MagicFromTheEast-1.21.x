package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.ElementalCommanderArmorModel;
import net.warphan.iss_magicfromtheeast.registries.MFTEArmorMaterialRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEAttributeRegistries;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;

public class ElementalCommanderArmorItem extends ImbuableChestplateArmorItem {
    public static final int COOLDOWN_TICKS = 20 * 15;

    public ElementalCommanderArmorItem(ArmorItem.Type slot, Properties settings) {
        super(MFTEArmorMaterialRegistries.ELEMENTAL_COMMANDER, slot, settings,
                new AttributeContainer(AttributeRegistry.MAX_MANA, 150, AttributeModifier.Operation.ADD_VALUE),
                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
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
            effectBonus = (livingEntity.getAttributeValue(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER) / 4) * 100;
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
