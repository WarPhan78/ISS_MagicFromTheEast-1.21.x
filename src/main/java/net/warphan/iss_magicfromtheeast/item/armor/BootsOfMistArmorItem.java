package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.BootsOfMistArmorModel;
import net.warphan.iss_magicfromtheeast.registries.MFTEArmorMaterialRegistries;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;

public class BootsOfMistArmorItem extends ImbuableChestplateArmorItem {
    public static final int COOLDOWN_TICKS = 20 * 10;

    public BootsOfMistArmorItem(Type type, Properties properties) {
        super(MFTEArmorMaterialRegistries.BOOTS_OF_MIST, type, properties,
                new AttributeContainer(AttributeRegistry.MAX_MANA, 150, AttributeModifier.Operation.ADD_VALUE),
                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(Attributes.SNEAKING_SPEED, 0.50, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
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
