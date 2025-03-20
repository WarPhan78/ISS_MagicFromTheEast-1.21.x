package net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.entity.mobs.HumanoidRenderer;
import io.redspace.ironsspellbooks.render.SpellTargetingLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class JiangshiRenderer extends HumanoidRenderer<SummonedJiangshi> {
    ZombieRenderer vanillaRenderer;

    public JiangshiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new JiangshiModel());
        vanillaRenderer = new ZombieRenderer(renderManager) {
            @Override
            public ResourceLocation getTextureLocation(Zombie jiangshi) {
                return JiangshiModel.textureResource;
            }
        };
        vanillaRenderer.addLayer(new SpellTargetingLayer.Vanilla<>(vanillaRenderer));
    }

    @Override
    public void render(SummonedJiangshi jiangshi, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (jiangshi.isAnimatingRise())
            super.render(jiangshi, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        else
            vanillaRenderer.render(jiangshi, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
