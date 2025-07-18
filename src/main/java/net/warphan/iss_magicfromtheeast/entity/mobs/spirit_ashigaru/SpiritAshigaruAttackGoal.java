package net.warphan.iss_magicfromtheeast.entity.mobs.spirit_ashigaru;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.warphan.iss_magicfromtheeast.entity.spirit_bullet.SpiritBulletProjectile;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;

public class SpiritAshigaruAttackGoal extends WarlockAttackGoal {
    final SpiritAshigaruEntity ashigaru;

    public SpiritAshigaruAttackGoal(SpiritAshigaruEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
        ashigaru = abstractSpellCastingMob;
        this.wantsToMelee = true;
    }

    @Override
    protected float meleeBias() {
        return 1f;
    }

    int meleeAnimTimer = -1;
    public SpiritAshigaruEntity.AttackType currentAttack;
    public SpiritAshigaruEntity.AttackType nextAttack;

    @Override
    public boolean isActing() {
        return super.isActing() || meleeAnimTimer > 0;
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        var meleeRange = meleeRange();
        float distance = Mth.sqrt((float) distanceSquared);
        mob.getLookControl().setLookAt(target);
        if (meleeAnimTimer > 0) {
            forceFaceTarget();
            meleeAnimTimer--;
            if (mob instanceof SpiritAshigaruEntity spiritAshigaru) {
                if (currentAttack.data.isHitFrame(meleeAnimTimer)) {
                    if (spiritAshigaru.isMeleeType()) {
                        currentAttack = SpiritAshigaruEntity.AttackType.MELEE;
                        Vec3 push = target.position().subtract(mob.position()).normalize().scale(.1f);
                        mob.push(push.x, push.y, push.z);
                        if (distance <= meleeRange && Utils.hasLineOfSight(mob.level, mob, target, true)) {
                            boolean flag = this.mob.doHurtTarget(target);
                            target.invulnerableTime = 0;
                            if (flag) {
                                playPierceSound();
                            }
                        }
                    } else if (spiritAshigaru.isRangeType()){
                        currentAttack = SpiritAshigaruEntity.AttackType.RANGE;
                        SpiritBulletProjectile spiritBullet = new SpiritBulletProjectile(ashigaru.level, ashigaru);
                        spiritBullet.setPos(ashigaru.position().add(0, ashigaru.getEyeHeight() - spiritBullet.getBoundingBox().getYsize() * .5f,0).add(ashigaru.getForward()));
                        spiritBullet.shoot(ashigaru.getLookAngle());
                        spiritBullet.setDamage((float) ashigaru.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        ashigaru.level.addFreshEntity(spiritBullet);
                        playShotSound();
                    }
                }
            }
        } else if (target != null && !target.isDeadOrDying()) {
            nextAttack = continueAttacking(distance);
            doMeleeAction();
        } else if (meleeAnimTimer == 0) {
            nextAttack = continueAttacking(distance);
            resetMeleeAttackInterval(distanceSquared);
            meleeAnimTimer = -1;
        } else {
            if (distance < meleeRange) {
                if (hasLineOfSight && --this.meleeAttackDelay == 0) {
                    doMeleeAction();
                } else if (this.meleeAttackDelay < 0) {
                    resetMeleeAttackInterval(distanceSquared);
                }
            } else if (--this.meleeAttackDelay < 0) {
                resetMeleeAttackInterval(distanceSquared);
                nextAttack = continueAttacking(distance);
            }
        }
    }

    private SpiritAshigaruEntity.AttackType continueAttacking(float distance) {
        var meleeRange = meleeRange();
        if (ashigaru.isRangeType() && distance < meleeRange * 1.2f) {
            return SpiritAshigaruEntity.AttackType.RANGE;
        } else return SpiritAshigaruEntity.AttackType.MELEE;
    }

    private void forceFaceTarget() {
        double d0 = target.getX() - mob.getX();
        double d1 = target.getZ() - mob.getZ();
        float yRot = (float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
        mob.setYBodyRot(yRot);
        mob.setYHeadRot(yRot);
        mob.setYRot(yRot);
    }

    @Override
    protected void doMeleeAction() {
        currentAttack = nextAttack;
        if (currentAttack != null) {
            this.mob.swing(InteractionHand.MAIN_HAND);
            meleeAnimTimer = currentAttack.data.lengthInTicks;
            PacketDistributor.sendToPlayersTrackingEntity(ashigaru, new SyncAnimationPacket<>(currentAttack.toString(), ashigaru));
        }
    }

    @Override
    protected void doMovement(double distanceSquared) {
        var meleeRange = meleeRange();
        if (target.isDeadOrDying()) {
            this.mob.getNavigation().stop();
        } else if (distanceSquared > meleeRange * meleeRange) {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier * 1.3f);
        }
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

    public void playPierceSound() {
        mob.playSound(MFTESoundRegistries.ASHIGARU_SPEAR.get(), 1, Mth.randomBetweenInclusive(mob.getRandom(), 7, 11) * .1f);
    }

    public void playShotSound() {
        mob.playSound(MFTESoundRegistries.ASHIGARU_GUNSHOT.get(), 1, Mth.randomBetweenInclusive(mob.getRandom(), 9, 13) * .1f);
    }
}
