package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.TaoistArmorModel;
import net.warphan.iss_magicfromtheeast.registries.MFTEAttributeRegistries;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class TaoistArmorItem extends ImbuableChestplateArmorItem {
    public TaoistArmorItem(ArmorItem.Type slot, Properties settings) {
        super(ArmorMaterialRegistry.SCHOOL, slot, settings, schoolAttributes(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new TaoistArmorModel());
    }
}
