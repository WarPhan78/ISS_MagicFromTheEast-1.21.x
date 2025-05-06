//package net.warphan.iss_magicfromtheeast.entity.mobs.jade_sentinel;
//
//import io.redspace.ironsspellbooks.damage.DamageSources;
//import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
//import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import net.neoforged.neoforge.network.PacketDistributor;
//import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
//
//public class JadeSentinelAttackGoal extends WarlockAttackGoal {
//    final JadeSentinel sentinel;
//
//    public JadeSentinelAttackGoal(JadeSentinel jadeSentinel, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
//        super(jadeSentinel, pSpeedModifier, minAttackInterval, maxAttackInterval);
//        sentinel = jadeSentinel;
//        nextAttack = calculateNextAttack(0);
//        this.wantsToMelee = true;
//    }
//
//    @Override
//    protected float meleeBias() {
//        return 1f;
//    }
//
//    int meleeAnimTimer = -1;
//    public JadeSentinel.AttackAnim currentAttack;
//    public JadeSentinel.AttackAnim nextAttack;
//    float meleeRange = 12.0f;
//
//    @Override
//    protected void handleAttackLogic(double distanceSquared) {
//        float distance = Mth.sqrt((float) distanceSquared);
//        mob.getLookControl().setLookAt(target);
//        if (distance > meleeRange) {
//            doMovement(distanceSquared);
//            forceFaceTarget();
//            meleeAnimTimer = 0;
//        } else if (distance <= meleeRange) {
//            if (meleeAnimTimer > 0) {
//                forceFaceTarget();
//                meleeAnimTimer--;
//                if (currentAttack.data.isHitFrame(meleeAnimTimer)) {
//                    if (currentAttack == JadeSentinel.AttackAnim.DAO_SWEEP) {
//                        Vec3 sweepPos = mob.position().add(mob.getForward().multiply(5, 0 ,1).normalize().scale(5f));
//                        Vec3 bbHalf = new Vec3(meleeRange, meleeRange, meleeRange).scale(0.8);
//                        float sweepDamage = (float) mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.75f;
//                        mob.level.getEntitiesOfClass(LivingEntity.class, new AABB(sweepPos.subtract(bbHalf), sweepPos.add(bbHalf))).forEach(entity -> {
//                            if (entity.isPickable() && !DamageSources.isFriendlyFireBetween(mob, entity)) {
//                                entity.hurt(mob.level().damageSources().mobAttack(mob), sweepDamage);
//                                playSweepSound();
//                                entity.hurtMarked = true;
//                            }
//                        });
//                    } else if (currentAttack == JadeSentinel.AttackAnim.DAO_STAB) {
//                        boolean flagStab = this.mob.doHurtTarget(target);
//                        target.invulnerableTime = 0;
//                        if (flagStab) {
//                            playStabSound();
//                        }
//                    } else if (currentAttack == JadeSentinel.AttackAnim.JADE_STOMP) {
//                        Vec3 stompPos = mob.position().add(mob.getForward().multiply(2, 0, 2).normalize().scale(2f));
//                        Vec3 bbHalf = new Vec3(meleeRange, meleeRange, meleeRange).scale(0.4);
//                        float stompDamage = (float) mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5f;
//                        mob.level.getEntitiesOfClass(LivingEntity.class, new AABB(stompPos.subtract(bbHalf), stompPos.add(bbHalf))).forEach(entity -> {
//                            if (entity.isPickable() && !DamageSources.isFriendlyFireBetween(mob, entity)) {
//                                entity.hurt(mob.level().damageSources().mobAttack(mob), stompDamage);
//                                playStompSound();
//                                Vec3 knockBack = mob.position().subtract(entity.position());
//                                entity.knockback(2, knockBack.x, knockBack.z);
//                                entity.hurtMarked = true;
//                            }
//                        });
//                    }
//                }
//            } else if (target != null && !target.isDeadOrDying()) {
//                nextAttack = calculateNextAttack(distance);
//                doMeleeAction();
//            } else if (meleeAnimTimer == 0) {
//                nextAttack = calculateNextAttack(distance);
//                resetMeleeAttackInterval(distanceSquared);
//                meleeAnimTimer = -1;
//            }
//        }
//    }
//
//    private JadeSentinel.AttackAnim calculateNextAttack(float distance) {
//        if (distance > meleeRange * 0.6f && distance <= meleeRange * 0.8f) {
//            return JadeSentinel.AttackAnim.DAO_SWEEP;
//        } else if (distance > meleeRange * 0.8f) {
//            return JadeSentinel.AttackAnim.DAO_STAB;
//        } else
//        return JadeSentinel.AttackAnim.JADE_STOMP;
//    }
//
//    private void forceFaceTarget() {
//        double d0 = target.getX() - mob.getX();
//        double d1 = target.getZ() - mob.getZ();
//        float yRot = (float) (Mth.atan2(d1, d0) * (double) (180 / (float) Math.PI)) - 90.0F;
//        mob.setYBodyRot(yRot);
//        mob.setYHeadRot(yRot);
//        mob.setYRot(yRot);
//    }
//
//    @Override
//    protected void doMeleeAction() {
//        currentAttack = nextAttack;
//        if (currentAttack != null) {
//            meleeAnimTimer = currentAttack.data.lengthInTicks;
//            PacketDistributor.sendToPlayersTrackingEntity(sentinel, new SyncAnimationPacket<>(currentAttack.toString(), sentinel));
//        }
//    }
//
//    @Override
//    protected void doMovement(double distanceSquared) {
//        if (target.isDeadOrDying()) {
//            this.mob.getNavigation().stop();
//        } else if (distanceSquared > meleeRange * meleeRange) {
//            this.mob.getNavigation().moveTo(this.target, this.speedModifier * 1.0f);
//        }
//    }
//
//    @Override
//    public boolean canContinueToUse() {
//        return super.canContinueToUse() || meleeAnimTimer > 0;
//    }
//
//    @Override
//    public void stop() {
//        super.stop();
//        this.meleeAnimTimer = -1;
//    }
//
//    public void playStabSound() {
//        mob.playSound(MFTESoundRegistries.JADE_SENTINEL_STAB.get(), 1, Mth.randomBetweenInclusive(mob.getRandom(),9,13) * .1f);
//    }
//    public void playSweepSound() {
//        mob.playSound(MFTESoundRegistries.JADE_SENTINEL_SWEEP.get(), 1, Mth.randomBetweenInclusive(mob.getRandom(), 9, 13) * .1f);
//    }
//    public void playStompSound() {
//        mob.playSound(MFTESoundRegistries.JADE_SENTINEL_STOMP.get(), 1, Mth.randomBetweenInclusive(mob.getRandom(), 9, 13) * .1f);
//    }
//}
