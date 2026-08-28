package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.OnmyojiArmorModel;
import net.warphan.iss_magicfromtheeast.registries.MFTEExtendedArmorMaterial;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class OnmyojiArmorItem extends ImbuableChestplateArmorItem {
    public OnmyojiArmorItem(Type slot, Properties setting) {
        // PORT 1.20.1: 1.21 ArmorMaterialRegistry.SCHOOL + schoolAttributes(...) -> the ONMYOJI
        // IronsExtendedArmorMaterial entry carries the same attributes (ISS 3.16.2-1.20.1 style).
        super(MFTEExtendedArmorMaterial.ONMYOJI, slot, setting);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new OnmyojiArmorModel());
    }
}
