package net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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
                    playSlamSound();
                    float radius = 1.0f;
                    Vec3 forward = boneHands.getForward().multiply(1, 0, 1).normalize();
                    Vec3 start = boneHands.getEyePosition().subtract(0, 8, 0).add(forward.scale(1.5));
                    Vec3 bbBox = new Vec3(radius, radius * 5.0f, radius);
                    float slamDamage = (float) boneHands.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    for (int i = 0; i <= 6; i++) {
                        Vec3 slamPos = start.add(forward.scale(i));
                        boneHands.level.getEntitiesOfClass(LivingEntity.class, new AABB(slamPos.subtract(bbBox), slamPos.add(bbBox))).forEach(entity -> {
                            if (entity.isPickable() && !DamageSources.isFriendlyFireBetween(boneHands, entity)) {
                                entity.hurt(boneHands.level().damageSources().mobAttack(boneHands), slamDamage);
                                entity.hurtMarked =  true;
                            }
                        });
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
