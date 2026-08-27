package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.RoninArmorModel;
import net.warphan.iss_magicfromtheeast.registries.MFTEArmorMaterialRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEAttributeRegistries;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class RoninArmorItem extends ImbuableChestplateArmorItem {
    public RoninArmorItem(ArmorItem.Type slot, Properties properties) {
        super(MFTEArmorMaterialRegistries.MASTER_SCHOOL_ARMOR, slot, properties,
                new AttributeContainer(AttributeRegistry.MAX_MANA, 150, AttributeModifier.Operation.ADD_VALUE),
                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(MFTEAttributeRegistries.SPIRIT_SPELL_POWER, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new RoninArmorModel());
    }
}
