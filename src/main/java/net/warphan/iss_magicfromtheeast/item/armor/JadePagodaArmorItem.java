package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.JadePagodaArmorModel;
import net.warphan.iss_magicfromtheeast.registries.MFTEArmorMaterialRegistries;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class JadePagodaArmorItem extends ImbuableChestplateArmorItem {
    public JadePagodaArmorItem(Type slot, Properties properties) {
        // PORT 1.20.1: the 1.21 AttributeContainers (-2.5% movement speed, +10% spell resist,
        // +50 max mana) are carried by the JADE material (ISS 3.16.2-1.20.1 style).
        super(MFTEArmorMaterialRegistries.JADE, slot, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new JadePagodaArmorModel());
    }
}
