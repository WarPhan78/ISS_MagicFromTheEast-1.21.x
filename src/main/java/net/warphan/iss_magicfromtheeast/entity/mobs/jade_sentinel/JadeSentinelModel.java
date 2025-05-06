//package net.warphan.iss_magicfromtheeast.entity.mobs.jade_sentinel;
//
//import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.TransformStack;
//import net.minecraft.client.model.geom.PartNames;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.Mth;
//import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
//import software.bernie.geckolib.animation.AnimationState;
//import software.bernie.geckolib.cache.object.GeoBone;
//import software.bernie.geckolib.model.GeoModel;
//
//public class JadeSentinelModel extends GeoModel<JadeSentinel> {
//    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/jade_sentinel.geo.json");
//    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jade_sentinel.png");
//    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/jade_sentinel.animation.json");
//
//    protected TransformStack transformStack = new TransformStack();
//
//    @Override
//    public ResourceLocation getModelResource(JadeSentinel object) {
//        return modelResource;
//    }
//
//    @Override
//    public ResourceLocation getTextureResource(JadeSentinel object) {
//        return textureResource;
//    }
//
//    @Override
//    public ResourceLocation getAnimationResource(JadeSentinel animatable) {
//        return animationResource;
//    }
//
//    @Override
//    public void setCustomAnimations(JadeSentinel entity, long instanceID, AnimationState<JadeSentinel> animationState) {
//        super.setCustomAnimations(entity, instanceID, animationState);
//
//        float partialTick = animationState.getPartialTick();
//
//        GeoBone head = this.getAnimationProcessor().getBone(PartNames.HEAD);
//        GeoBone body = this.getAnimationProcessor().getBone(PartNames.BODY);
//
//        if (!entity.isAnimating() || entity.shouldAlwaysAnimatedHead()) {
//            transformStack.pushRotation(head,
//                    Mth.lerp(partialTick, -entity.xRotO, -entity.getXRot()) * Mth.DEG_TO_RAD,
//                    Mth.lerp(partialTick,
//                            Mth.wrapDegrees(-entity.yHeadRotO + entity.yBodyRotO) * Mth.DEG_TO_RAD,
//                            Mth.wrapDegrees(-entity.yHeadRot + entity.yBodyRot) * Mth.DEG_TO_RAD
//                    ),
//                    0);
//        }
//    }
//}
