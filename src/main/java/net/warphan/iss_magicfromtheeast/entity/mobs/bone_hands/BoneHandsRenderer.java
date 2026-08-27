package net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BoneHandsRenderer extends GeoEntityRenderer<BoneHandsEntity> {
    public static final ResourceLocation textureLocation = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/bone_hand.png");

    public BoneHandsRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoneHandsModel());
        this.shadowRadius = 0f;
    }

    @Override
    public ResourceLocation getTextureLocation(BoneHandsEntity animatable) {
        return textureLocation;
    }

    @Override
    protected float getDeathMaxRotation(BoneHandsEntity degree) {
        return 0.0f;
    }
//    @Override
//    public RenderType getRenderType(BoneHandsEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTicks) {
//        return RenderType.energySwirl(texture, 0, 0);
//    }
}
