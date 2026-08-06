package net.warphan.iss_magicfromtheeast.item.armor;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.entity.armor.JiangshiHatModel;
import net.warphan.iss_magicfromtheeast.registries.MFTEArmorMaterialRegistries;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class JiangshiHatItem extends ExtendedArmorItem {
    public JiangshiHatItem(Type slot, Properties settings) {
        // TODO PORT 1.20.1: the +0.2 jump strength bonus was cut - on 1.20.1
        //  Attributes.JUMP_STRENGTH is horse-only ("horse.jumpStrength") and there is no
        //  generic/player jump strength attribute (added in 1.20.5+). The JIANGSHI material
        //  mirrors the vanilla LEATHER stats the 1.21 version used.
        super(MFTEArmorMaterialRegistries.JIANGSHI, slot, settings);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new JiangshiHatModel());
    }
}
