package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.TaoistArmorModel;
import net.warphan.iss_magicfromtheeast.registries.MFTEArmorMaterialRegistries;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class TaoistArmorItem extends ImbuableChestplateArmorItem {
    public TaoistArmorItem(Type slot, Properties settings) {
        // PORT 1.20.1: 1.21 ArmorMaterialRegistry.SCHOOL + schoolAttributes(...) -> the TAOIST
        // IronsExtendedArmorMaterial entry carries the same attributes (ISS 3.16.2-1.20.1 style).
        super(MFTEArmorMaterialRegistries.TAOIST, slot, settings);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new TaoistArmorModel());
    }
}
