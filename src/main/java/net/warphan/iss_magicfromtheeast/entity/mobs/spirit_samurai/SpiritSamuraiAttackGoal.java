package net.warphan.iss_magicfromtheeast.entity.mobs.spirit_samurai;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;

public class SpiritSamuraiAttackGoal extends WarlockAttackGoal {
    final SpiritSamuraiEntity samurai;

    public SpiritSamuraiAttackGoal(SpiritSamuraiEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
        samurai = abstractSpellCastingMob;
        nextAttack = randomizeNextAttack(0);
        this.wantsToMelee = true;
    }

    @Override
    protected float meleeBias() {
        return 1f;
    }

    int meleeAnimTimer = -1;
    public SpiritSamuraiEntity.AttackAnim currentAttack;
    public SpiritSamuraiEntity.AttackAnim nextAttack;
    public SpiritSamuraiEntity.AttackAnim queueCombo;
    private boolean hasUsedTechnique;
    private boolean hasHitTechnique;
    private Vec3 oldTechniquePos;

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
            if (currentAttack.data.isHitFrame(meleeAnimTimer - 4)) {
                if (currentAttack != SpiritSamuraiEntity.AttackAnim.KATANA_TECHNIQUE) {
                    playSlashSound();
                }
            } else if (currentAttack.data.isHitFrame(meleeAnimTimer)) {
                if (currentAttack != SpiritSamuraiEntity.AttackAnim.KATANA_TECHNIQUE) {
                    Vec3 lunge = target.position().subtract(mob.position()).normalize().scale(.55f);
                    mob.push(lunge.x, lunge.y, lunge.z);
                    if (distance <= meleeRange && Utils.hasLineOfSight(mob.level, mob, target, true)) {
                        boolean flag = this.mob.doHurtTarget(target);
                        target.invulnerableTime = 0;
                        if (flag) {
                            if (currentAttack.data.isSingleHit() && ((mob.getRandom().nextFloat() < .75f) || target.isBlocking())) {
                                queueCombo = randomizeNextAttack(0);
                            }
                        }
                    }
                } else {
                    if (!hasUsedTechnique) {
                        Vec3 charge = target.position().subtract(mob.position()).normalize().multiply(2.4, .5, 2.4).add(0, 0.15, 0);
                        mob.push(charge.x, charge.y, charge.z);
                        oldTechniquePos = mob.position();
                        mob.getNavigation().stop();
                        hasUsedTechnique = true;
                        playSlashSound();
                        Vec3 AABBPos = mob.position();
                        var entities = mob.level.getEntities(mob, AABB.ofSize(AABBPos, 5, 3, 5));
                        for (Entity targetEntity : entities) {
                            if (targetEntity.isAlive() && targetEntity.isPickable() && Utils.hasLineOfSight(mob.level, AABBPos, targetEntity.getBoundingBox().getCenter(), true)) {
                                DamageSources.applyDamage(targetEntity, (float) mob.getAttributeValue(Attributes.ATTACK_DAMAGE), MFTESpellRegistries.REVENANT_OF_HONOR_SPELL.get().getDamageSource(mob));
                            }
                        }
                    }
                    if (!hasHitTechnique && distance <= meleeRange * .45f) {
                        Vec3 knockback = oldTechniquePos.subtract(target.position());
                        target.knockback(1, knockback.x, knockback.z);
                        hasHitTechnique = true;
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
            if (distance < meleeRange * (nextAttack == SpiritSamuraiEntity.AttackAnim.KATANA_TECHNIQUE ? 3 : 1)) {
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

    private SpiritSamuraiEntity.AttackAnim randomizeNextAttack(float distance) {
        var meleeRange = meleeRange();
        int i;
        if (distance < meleeRange * 1.5f) {
            i = SpiritSamuraiEntity.AttackAnim.values().length - 1;
        } else if (mob.getRandom().nextFloat() < .25f && distance > meleeRange * 2.5f) {
            return SpiritSamuraiEntity.AttackAnim.KATANA_TECHNIQUE;
        } else {
            i = SpiritSamuraiEntity.AttackAnim.values().length;
        }
        return SpiritSamuraiEntity.AttackAnim.values()[mob.getRandom().nextInt(i)];
    }


    private void forceFaceTarget() {
        if (hasUsedTechnique)
            return;
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
            hasUsedTechnique = false;
            hasHitTechnique = false;
            PacketDistributor.sendToPlayersTrackingEntity(samurai, new SyncAnimationPacket<>(currentAttack.toString(), samurai));
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

    public void playSlashSound() {
        mob.playSound(MFTESoundRegistries.SAMURAI_SLASH.get(), 1, Mth.randomBetweenInclusive(mob.getRandom(), 9, 13) * .1f);
    }
}
