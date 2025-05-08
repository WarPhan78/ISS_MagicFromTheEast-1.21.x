package net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands;

import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;

public class BoneHandAttackGoal extends WarlockAttackGoal {
    final BoneHandsEntity boneHands;

    public BoneHandAttackGoal(BoneHandsEntity boneHandsEntity, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(boneHandsEntity, pSpeedModifier, minAttackInterval, maxAttackInterval);
        boneHands = boneHandsEntity;
        attack = actionAttack(0);
        this.wantsToMelee = true;
    }

    @Override
    protected float meleeBias() {
        return 1f;
    }

    int meleeAnimTimer = -1;
    public BoneHandsEntity.AttackAnim attack;
    float meleeRange = 9.0f;

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        float distance = Mth.sqrt((float) distanceSquared);
        mob.getLookControl().setLookAt(target);
        if (distance > meleeRange) {
            if (mob instanceof BoneHandsEntity boneHands) {
                boneHands.stopAnimationAttack();
            }
            forceFaceTarget();
            meleeAnimTimer = 0;
        } else if (distance <= meleeRange) {
            if (mob instanceof BoneHandsEntity boneHands) {
                boneHands.triggerAnimatingAttack();
            }
            if (meleeAnimTimer > 0) {
                forceFaceTarget();
                meleeAnimTimer--;
                if (attack.data.isHitFrame(meleeAnimTimer)) {
                    boolean flag = this.mob.doHurtTarget(target);
                        target.invulnerableTime = 0;
                        if (flag) {
                            playSlamSound();
                        }
                }
            } else if (target != null && !target.isDeadOrDying()) {
                doMeleeAction();
            } else if (meleeAnimTimer == 0) {
                resetMeleeAttackInterval(distanceSquared);
                meleeAnimTimer = -1;
            }
            if (target == null || target.isDeadOrDying()) {
                if (mob instanceof BoneHandsEntity boneHands) {
                    boneHands.stopAnimationAttack();
                }
            }
        }
    }

    private BoneHandsEntity.AttackAnim actionAttack(float distance) {
        return BoneHandsEntity.AttackAnim.BONE_SLAM;
    }

    private void forceFaceTarget() {
        double d0 = target.getX() - mob.getX();
        double d1 = target.getZ() - mob.getZ();
        float yRot = (float) (Mth.atan2(d1, d0) * (double) (180 / (float) Math.PI)) - 90.0F;
        mob.setYBodyRot(yRot);
        mob.setYHeadRot(yRot);
        mob.setYRot(yRot);
    }

    @Override
    protected void doMeleeAction() {
        if (attack != null) {
            meleeAnimTimer = attack.data.lengthInTicks;
            PacketDistributor.sendToPlayersTrackingEntity(boneHands, new SyncAnimationPacket<>(attack.toString(), boneHands));
        }
    }

    @Override
    protected void doMovement(double distanceSquared) {
        return;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() || meleeAnimTimer > 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.meleeAnimTimer = -1;
    }

    public void playSlamSound() {
        mob.playSound(MFTESoundRegistries.BONE_SLAM.get(), 1, Mth.randomBetweenInclusive(mob.getRandom(),9,13) * .1f);
    }
}
