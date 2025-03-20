package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.JiangshiHatModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class JiangshiHatItem extends ExtendedArmorItem {
    public JiangshiHatItem(ArmorItem.Type slot, Properties settings) {
        super(ArmorMaterials.LEATHER, slot, settings,
                new AttributeContainer(Attributes.JUMP_STRENGTH, 0.2, AttributeModifier.Operation.ADD_VALUE)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new JiangshiHatModel());
    }
}
