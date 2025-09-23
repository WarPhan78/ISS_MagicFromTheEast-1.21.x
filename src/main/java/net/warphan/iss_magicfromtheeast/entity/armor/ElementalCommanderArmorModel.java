package net.warphan.iss_magicfromtheeast.entity.armor;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.armor.ElementalCommanderArmorItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ElementalCommanderArmorModel extends DefaultedItemGeoModel<ElementalCommanderArmorItem> {
    public ElementalCommanderArmorModel() {
        super(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(ElementalCommanderArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/elemental_commander_chestplate.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElementalCommanderArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/models/armor/elemental_commander.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElementalCommanderArmorItem animatable) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID,"animations/master_armor_animation.json");
    }
}
