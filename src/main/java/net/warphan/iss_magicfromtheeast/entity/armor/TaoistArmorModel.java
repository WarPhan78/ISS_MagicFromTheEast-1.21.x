package net.warphan.iss_magicfromtheeast.entity.armor;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.armor.TaoistArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class TaoistArmorModel extends DefaultedItemGeoModel<TaoistArmorItem> {

    public TaoistArmorModel() {
        super(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(TaoistArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/taoist_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TaoistArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/models/armor/taoist.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TaoistArmorItem animatable) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID,"animations/master_armor_animation.json");
    }
}
