package net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.TransformStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import org.joml.Vector2f;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class JadeExecutionerModel extends GeoModel<JadeExecutionerEntity> {
    public static final ResourceLocation modelResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "geo/jade_executioner.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/jade_executioner.png");
    public static final ResourceLocation animationResource = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "animations/jade_executioner.animation.json");

    protected TransformStack transformStack = new TransformStack();

    @Override
    public ResourceLocation getModelResource(JadeExecutionerEntity animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(JadeExecutionerEntity animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(JadeExecutionerEntity animatable) {
        return animationResource;
    }

    @Override
    public void handleAnimations(JadeExecutionerEntity jadeExecutioner, long instanceId, AnimationState<JadeExecutionerEntity> animationState, float partialTick) {
        transformStack.resetDirty();
        super.handleAnimations(jadeExecutioner, instanceId, animationState, partialTick);
    }

    @Override
    public void setCustomAnimations(JadeExecutionerEntity jadeExecutioner, long instanceId, AnimationState<JadeExecutionerEntity> animationState) {
        super.setCustomAnimations(jadeExecutioner, instanceId, animationState);
        if (!(Minecraft.getInstance().isPaused())) {
            var partialTick = animationState.getPartialTick();

            GeoBone body = this.getAnimationProcessor().getBone("body");
            GeoBone rightArm = this.getAnimationProcessor().getBone("right_hand");
            GeoBone leftArm = this.getAnimationProcessor().getBone("left_hand");
            GeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
            GeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");

            Vector2f limbSwing = getLimbSwing(jadeExecutioner, jadeExecutioner.walkAnimation, partialTick);
            float limbSwingAmount = limbSwing.x;
            float limbSwingSpeed = limbSwing.y;

// Leg Controls

            if (jadeExecutioner.walkAnimation.isMoving()) {
                float strength = .75f;
                Vec3 facing = jadeExecutioner.getForward().multiply(1, 0, 1).normalize();
                Vec3 momentum = jadeExecutioner.getDeltaMovement().multiply(1, 0, 1).normalize();
                Vec3 facingOrth = new Vec3(-facing.z, 0, facing.x);
                float directionForward = (float) facing.dot(momentum);
                float directionSide = (float) facingOrth.dot(momentum) * .35f;
                float rightLateral = -Mth.sin(limbSwingSpeed * 0.6662F) * 4 * limbSwingAmount;
                float leftLateral = -Mth.sin(limbSwingSpeed * 0.6662F - Mth.PI) * 4 * limbSwingAmount;
                transformStack.pushPosition(rightLeg, rightLateral * directionSide, Mth.cos(limbSwingSpeed * 0.6662F) * 4 * strength * limbSwingAmount, rightLateral * directionForward);
                transformStack.pushRotation(rightLeg, Mth.cos(limbSwingSpeed * 0.6662F) * 1.4F * limbSwingAmount * strength, 0, 0);

                transformStack.pushPosition(leftLeg, leftLateral * directionSide, Mth.cos(limbSwingSpeed * 0.6662F - Mth.PI) * 4 * strength * limbSwingAmount, leftLateral * directionForward);
                transformStack.pushRotation(leftLeg, Mth.cos(limbSwingSpeed * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * strength, 0, 0);

                if (jadeExecutioner.bobBodyWhileWalking()) {
                    transformStack.pushPosition(body, 0, Mth.abs(Mth.cos((limbSwingSpeed * 1.2662F - Mth.PI * .5f) * .5f)) * 2 * strength * limbSwingAmount, 0);
                }
            }

//Arm Controls

            if (jadeExecutioner.walkAnimation.isMoving()) {
                transformStack.pushRotation(rightArm, Mth.cos(limbSwingSpeed * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F, 0, 0);
                transformStack.pushRotation(leftArm, Mth.cos(limbSwingSpeed * 0.6662F) * 2.0F * limbSwingAmount * 0.5F, 0, 0);
                bobBone(rightArm, jadeExecutioner.tickCount + partialTick, 1);
                bobBone(leftArm, jadeExecutioner.tickCount + partialTick, -1);
            }
        }
    }

    protected void bobBone(GeoBone bone, float offset, float multiplier) {
        float z = multiplier * (Mth.cos(offset * 0.09F) * 0.05F + 0.05F);
        float x = multiplier * Mth.sin(offset * 0.067F) * 0.05F;
        transformStack.pushRotation(bone, x, 0, z);
    }

    protected Vector2f getLimbSwing(JadeExecutionerEntity entity, WalkAnimationState walkAnimationState, float partialTick) {
        float limbSwingAmount = 2;
        float limbSwingSpeed = 2;
        if (entity.isAlive()) {
            limbSwingAmount = walkAnimationState.speed(partialTick);
            limbSwingSpeed = walkAnimationState.position(partialTick);

            if (limbSwingAmount > 1.0F) {
                limbSwingAmount = 1.0F;
            }
        }
        return new Vector2f(limbSwingAmount, limbSwingSpeed);
    }
}
