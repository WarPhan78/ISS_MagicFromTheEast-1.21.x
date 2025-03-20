package net.warphan.iss_magicfromtheeast.entity.armor;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.armor.JiangshiHatItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class JiangshiHatModel extends DefaultedItemGeoModel<JiangshiHatItem> {

    public JiangshiHatModel() {
        super(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(JiangshiHatItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/jiangshi_hat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JiangshiHatItem object) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/models/armor/jiangshi_hat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(JiangshiHatItem animatable) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/master_armor_animation.json");
    }
}
