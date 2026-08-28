package net.warphan.iss_magicfromtheeast.entity.mobs.kitsune;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SummonedKitsuneRenderer extends GeoEntityRenderer<SummonedKitsune> {
    public static final ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/entity/kitsune.png");

    public SummonedKitsuneRenderer(EntityRendererProvider.Context context) {
        super(context, new SummonedKitsuneModel());
        this.shadowRadius = 0.55f;
    }

    @Override
    public ResourceLocation getTextureLocation(SummonedKitsune summonedKitsune) {
        return textureLocation;
    }
}
