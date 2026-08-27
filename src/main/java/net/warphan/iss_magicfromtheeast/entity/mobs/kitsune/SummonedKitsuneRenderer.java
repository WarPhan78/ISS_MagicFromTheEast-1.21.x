package net.warphan.iss_magicfromtheeast.entity.mobs.kitsune;

import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class SummonedKitsuneRenderer extends MobRenderer<SummonedKitsune, FoxModel<SummonedKitsune>> {

    public SummonedKitsuneRenderer(EntityRendererProvider.Context context) {
        super(context, new FoxModel<>(context.bakeLayer(ModelLayers.FOX)), 0.55f);
    }

    @Override
    public ResourceLocation getTextureLocation(SummonedKitsune summonedKitsune) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/fox/kitsune.png");
    }
}
