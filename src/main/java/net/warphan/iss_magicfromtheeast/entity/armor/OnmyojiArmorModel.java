package net.warphan.iss_magicfromtheeast.entity.armor;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.armor.OnmyojiArmorItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class OnmyojiArmorModel extends DefaultedItemGeoModel<OnmyojiArmorItem> {

    public OnmyojiArmorModel() {
        super(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, ""));
    }
    @Override
    public ResourceLocation getModelResource(OnmyojiArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/onmyoji_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OnmyojiArmorItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/models/armor/onmyoji.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OnmyojiArmorItem animatable) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID,"animations/master_armor_animation.json");
    }
}
