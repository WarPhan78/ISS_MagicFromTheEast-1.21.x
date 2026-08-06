package net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.TransformStack;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class JiangshiModel extends GeoModel<JiangshiEntity> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/jiangshi.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jiangshi.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/jiangshi.animation.json");

    protected TransformStack transformStack = new TransformStack();
    private long lastRendererInstance = -1;

    @Override
    public ResourceLocation getModelResource(JiangshiEntity object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(JiangshiEntity object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(JiangshiEntity animatable) {
        return animationResource;
    }

    // 1.20.1 (GeckoLib 4.2): handleAnimations has no partialTick parameter; taken from animationState instead (mirrors AbstractSpellCastingMobModel 3.16.2-1.20.1)
    @Override
    public void handleAnimations(JiangshiEntity animatable, long instanceId, AnimationState<JiangshiEntity> animationState) {
        var manager = animatable.getAnimatableInstanceCache().getManagerForId(instanceId);
        Double currentTick = animationState.getData(DataTickets.TICK);
        var partialTick = animationState.getPartialTick();
        double currentFrameTime = animatable instanceof Entity || animatable instanceof GeoReplacedEntity ? currentTick + partialTick : currentTick - manager.getFirstTickTime();
        boolean isReRender = !manager.isFirstTick() && currentFrameTime == manager.getLastUpdateTime();
        if (isReRender && instanceId == this.lastRendererInstance)
            return;
        this.lastRendererInstance = instanceId;
        transformStack.resetDirty();
        super.handleAnimations(animatable, instanceId, animationState);
        transformStack.popStack();
    }

    @Override
    public void setCustomAnimations(JiangshiEntity animatable, long instanceId, AnimationState<JiangshiEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (!animatable.shouldBeExtraAnimated()) {
            return;
        }
        var partialTick = animationState.getPartialTick();
        CoreGeoBone head = this.getAnimationProcessor().getBone(PartNames.HEAD);
        CoreGeoBone hair = this.getAnimationProcessor().getBone("hair");

        if (animatable.shouldAlwaysAnimateHead()) {
            transformStack.pushRotation(head,
                    Mth.lerp(partialTick, -animatable.xRotO, -animatable.getXRot()) * Mth.DEG_TO_RAD,
                    Mth.lerp(partialTick,
                            Mth.wrapDegrees(-animatable.yHeadRotO + animatable.yBodyRotO) * Mth.DEG_TO_RAD,
                            Mth.wrapDegrees(-animatable.yHeadRot + animatable.yBodyRot) * Mth.DEG_TO_RAD
                    ),
                    0);
            transformStack.pushRotation(hair, -(Mth.lerp(partialTick, -animatable.xRotO, -animatable.getXRot()) * Mth.DEG_TO_RAD), 0, 0);
        }
    }
}
