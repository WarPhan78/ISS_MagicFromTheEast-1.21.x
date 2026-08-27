package net.warphan.iss_magicfromtheeast.entity.armor;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.armor.ImmortalArmorItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ImmortalArmorModel extends DefaultedItemGeoModel<ImmortalArmorItem> {
    public ImmortalArmorModel() {
        super(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(ImmortalArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/immortal_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ImmortalArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/models/armor/immortal.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ImmortalArmorItem animatable) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID,"animations/master_armor_animation.json");
    }
}
