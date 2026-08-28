package net.warphan.iss_magicfromtheeast.entity.mobs.kitsune;

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

public class SummonedKitsuneModel extends GeoModel<SummonedKitsune> {
    public static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "geo/kitsune.geo.json");
    public static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/entity/kitsune.png");
    public static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "animations/kitsune.animation.json");

    protected TransformStack transformStack = new TransformStack();
    private long lastRendererInstance = -1;

    @Override
    public ResourceLocation getModelResource(SummonedKitsune object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(SummonedKitsune object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(SummonedKitsune animatable) {
        return animationResource;
    }

    @Override
    public void handleAnimations(SummonedKitsune animatable, long instanceId, AnimationState<SummonedKitsune> animationState) {
        var manager = animatable.getAnimatableInstanceCache().getManagerForId(instanceId);
        var partialTick = animationState.getPartialTick();
        Double currentTick = animationState.getData(DataTickets.TICK);
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
    public void setCustomAnimations(SummonedKitsune animatable, long instanceId, AnimationState<SummonedKitsune> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (!animatable.shouldBeExtraAnimated()) {
            return;
        }
        var partialTick = animationState.getPartialTick();
        CoreGeoBone head = this.getAnimationProcessor().getBone(PartNames.HEAD);

        if (animatable.shouldAlwaysAnimateHead()) {
            transformStack.pushRotation(head,
                    Mth.lerp(partialTick, -animatable.xRotO, -animatable.getXRot()) * Mth.DEG_TO_RAD,
                    Mth.lerp(partialTick,
                            Mth.wrapDegrees(-animatable.yHeadRotO + animatable.yBodyRotO) * Mth.DEG_TO_RAD,
                            Mth.wrapDegrees(-animatable.yHeadRot + animatable.yBodyRot) * Mth.DEG_TO_RAD
                    ),
                    0);
        }
    }
}
