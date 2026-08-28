package net.warphan.iss_magicfromtheeast.entity.armor;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.armor.BootsOfMistArmorItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class BootsOfMistArmorModel extends DefaultedItemGeoModel<BootsOfMistArmorItem> {
    public BootsOfMistArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(BootsOfMistArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "geo/mist_boots.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BootsOfMistArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/models/armor/mist_boots.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BootsOfMistArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "animations/master_armor_animation.json");
    }
}
