package net.warphan.iss_magicfromtheeast.entity.armor;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.armor.RoninArmorItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class RoninArmorModel extends DefaultedItemGeoModel<RoninArmorItem> {
    public RoninArmorModel() {
        super(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(RoninArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/ronin_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RoninArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/models/armor/ronin.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RoninArmorItem animatable) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID,"animations/master_armor_animation.json");
    }
}
