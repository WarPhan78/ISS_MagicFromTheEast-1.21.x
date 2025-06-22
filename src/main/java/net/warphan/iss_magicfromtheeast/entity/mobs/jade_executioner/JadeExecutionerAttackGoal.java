package net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;

public class JadeExecutionerAttackGoal extends WarlockAttackGoal {
    final JadeExecutionerEntity jadeExecutioner;

    public JadeExecutionerAttackGoal(JadeExecutionerEntity executionerEntity, double speedModifier, int minAttackInterval, int maxAttackInterval) {
        super(executionerEntity, speedModifier, minAttackInterval, maxAttackInterval);
        jadeExecutioner = executionerEntity;
        nextAttack = randomizeNextAttack(0);
        this.wantsToMelee = true;
    }

    @Override
    protected float meleeBias() {
        return 1f;
    }

    int meleeAnimTimer = -1;
    public JadeExecutionerEntity.AttackAnim currentAttack;
    public JadeExecutionerEntity.AttackAnim nextAttack;
    public JadeExecutionerEntity.AttackAnim queueCombo;

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
            if (currentAttack.data.isHitFrame(meleeAnimTimer)) {
                Vec3 lunge = target.position().subtract(mob.position()).normalize().scale(-.25f);
                mob.push(lunge.x, lunge.y, lunge.z);
                float radius = 0.8f;
                var damage = (float) mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
                Vec3 forward = mob.getForward().multiply(1, 0, 1).normalize();
                Vec3 start = mob.getEyePosition().subtract(0, 3.0, 0).add(forward.scale(1.5));
                if (distance <= meleeRange && Utils.hasLineOfSight(mob.level, mob, target, true)) {
                    if (currentAttack == JadeExecutionerEntity.AttackAnim.AXE_DOWN) {
                        playChopSound();
                        for (int i = 0; i < 3; i++) {
                            Vec3 chopLocation = start.add(forward.scale(i));
                            var entities = mob.level.getEntities(mob, AABB.ofSize(chopLocation, radius, radius * 3, radius));
                            for (Entity targetEntity : entities) {
                                if (targetEntity.isAlive() && targetEntity.isPickable() && Utils.hasLineOfSight(mob.level, chopLocation.add(0, 1, 0), targetEntity.getBoundingBox().getCenter(), true)) {
                                    DamageSources.applyDamage(targetEntity, (float) (damage * 1.5), MFTESpellRegistries.PUNISHING_HEAVEN_SPELL.get().getDamageSource(mob));
                                }
                            }
                        }
                    } else if (currentAttack == JadeExecutionerEntity.AttackAnim.AXE_LEFT) {
                        playSweepSound();
                        Vec3 leftSweepPos = start.add(forward.scale(0.5));
                        var entities = mob.level.getEntities(mob, AABB.ofSize(leftSweepPos, radius * 3.5, radius * 3, radius * 3.5));
                        for (Entity targetEntity : entities) {
                            if (targetEntity.isAlive() && targetEntity.isPickable() && Utils.hasLineOfSight(mob.level, leftSweepPos.add(0, 0.5, 0), targetEntity.getBoundingBox().getCenter(), true)) {
                                DamageSources.applyDamage(targetEntity, damage, MFTESpellRegistries.PUNISHING_HEAVEN_SPELL.get().getDamageSource(mob));
                                if (currentAttack.data.isSingleHit() && ((mob.getRandom().nextFloat() < .75f) || target.isBlocking())) {
                                    queueCombo = randomizeNextAttack(0);
                                }
                            }
                        }
                    } else if (currentAttack == JadeExecutionerEntity.AttackAnim.AXE_RIGHT) {
                        playSweepSound();
                        Vec3 rightSweepPos = mob.position().add(0, 0.5, 0).add(forward.scale(2.0));
                        var entities = mob.level.getEntities(mob, AABB.ofSize(rightSweepPos, radius * 3.5, radius * 3, radius * 3.5));
                        for (Entity targetEntity : entities) {
                            DamageSources.applyDamage(targetEntity, damage, MFTESpellRegistries.PUNISHING_HEAVEN_SPELL.get().getDamageSource(mob));
                            if (currentAttack.data.isSingleHit() && ((mob.getRandom().nextFloat() < .75f) || target.isBlocking())) {
                                queueCombo = randomizeNextAttack(0);
                            }
                        }
                    } else if (currentAttack == JadeExecutionerEntity.AttackAnim.BITE) {
                        boolean flag = this.mob.doHurtTarget(target);
                        if (flag) {
                            playBiteSound();
                            mob.heal( (float) (damage * 0.75));
                            if (currentAttack.data.isSingleHit() && ((mob.getRandom().nextFloat() < .75f) || target.isBlocking())) {
                                queueCombo = randomizeNextAttack(0);
                            }
                        }
                    } else if (currentAttack == JadeExecutionerEntity.AttackAnim.SHIELD_BASH) {
                        playBashSound();
                        Vec3 bashPos = start.add(forward.scale(0.5));
                        var entities = mob.level.getEntities(mob, AABB.ofSize(bashPos, radius * 2.0, radius * 3, radius * 2.0));
                        for (Entity targetEntity : entities) {
                            if (targetEntity.isAlive() && targetEntity.isPickable() && Utils.hasLineOfSight(mob.level, bashPos.add(0, 0.5, 0), targetEntity.getBoundingBox().getCenter(), true)) {
                                DamageSources.applyDamage(targetEntity,damage/2, MFTESpellRegistries.PUNISHING_HEAVEN_SPELL.get().getDamageSource(mob));
                                Vec3 knockBack = mob.position().subtract(targetEntity.position());
                                target.knockback(2, knockBack.x, knockBack.z);
                                targetEntity.hurtMarked = true;
                            }
                        }
                    }
                }
            }
        } else if (queueCombo != null && target != null && !target.isDeadOrDying()) {
            nextAttack = queueCombo;
            queueCombo = null;
            doMeleeAction();
        } else if (meleeAnimTimer == 0) {
            nextAttack = randomizeNextAttack(distance);
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
                nextAttack = randomizeNextAttack(distance);
            }
        }
    }

    private JadeExecutionerEntity.AttackAnim randomizeNextAttack(float distance) {
        var meleeRange = meleeRange();
        int i;
        if (distance < meleeRange * 1.2f) {
            i = JadeExecutionerEntity.AttackAnim.values().length - 1;
        }else {
            i = JadeExecutionerEntity.AttackAnim.values().length;
        }
        return JadeExecutionerEntity.AttackAnim.values()[mob.getRandom().nextInt(i)];
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
            meleeAnimTimer = currentAttack.data.lengthInTicks;
            PacketDistributor.sendToPlayersTrackingEntity(jadeExecutioner, new SyncAnimationPacket<>(currentAttack.toString(), jadeExecutioner));
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
        this.queueCombo = null;
    }

    public void playSweepSound() {
        mob.playSound(MFTESoundRegistries.AXE_SWEEP.get(), 1, Mth.randomBetweenInclusive(mob.getRandom(), 9, 13) * .1f);
    }

    public void playChopSound() {
        mob.playSound(MFTESoundRegistries.AXE_CHOP.get(), 1, Mth.randomBetweenInclusive(mob.getRandom(), 9, 13) * .1f);
    }

    public void playBashSound() {
        mob.playSound(MFTESoundRegistries.SHIELD_BASH.get(), 1, Mth.randomBetweenInclusive(mob.getRandom(), 9, 13) * .1f);
    }

    public void playBiteSound() {
        mob.playSound(SoundEvents.EVOKER_FANGS_ATTACK, 1, Mth.randomBetweenInclusive(mob.getRandom(), 9, 13) * .1f);
    }
}
