package net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class JiangshiRenderer extends GeoEntityRenderer<JiangshiEntity> {
    public static final ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jiangshi.png");

    public JiangshiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new JiangshiModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(JiangshiEntity animatable) {
        return textureLocation;
    }
}
