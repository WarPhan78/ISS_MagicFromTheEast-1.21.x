package net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class ChallengedSoulRenderer extends GeoEntityRenderer<ChallengedSoul> {
    public static final ResourceLocation textureLocation = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/challenged_soul.png");

    public ChallengedSoulRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ChallengedSoulModel());
        this.shadowRadius = 0f;
    }

    @Override
    public ResourceLocation getTextureLocation(ChallengedSoul animatable) {
        return textureLocation;
    }

    @Override
    public RenderType getRenderType(ChallengedSoul animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTicks) {
        return RenderType.energySwirl(texture, 0, 0);
    }
}
