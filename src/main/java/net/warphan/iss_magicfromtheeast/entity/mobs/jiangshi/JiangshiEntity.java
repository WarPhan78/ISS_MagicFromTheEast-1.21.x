package net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.curios.RustedCoinsSword;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class JiangshiEntity extends AbstractSpellCastingMob implements GeoEntity, IAnimatedAttacker, Enemy {
    private static final EntityDataAccessor<Boolean> DATA_IS_ANIMATING_RISE = SynchedEntityData.defineId(JiangshiEntity.class, EntityDataSerializers.BOOLEAN);
    private int riseAnimTime = 45;

    public enum AttackType {
        MELEE(15, "attack", 5);

        AttackType(int lengthTick, String animationID, int... attackTimeStamp) {
            this.data = new AttackAnimationData(lengthTick, animationID, attackTimeStamp);
        }

        public final AttackAnimationData data;
    }

    public JiangshiEntity(EntityType<? extends JiangshiEntity> pEntityType, Level plevel) {
        super(pEntityType, plevel);
        this.xpReward = 15;
        this.lookControl = createLookControl();
        this.moveControl = createMoveControl();
    }

    public JiangshiEntity(Level level, LivingEntity owner) {
        this(MFTEEntityRegistries.JIANGSHI.get(), level);
    }

    @Override
    protected boolean shouldDropLoot() {
        return super.shouldDropLoot();
    }

    @Override
    public boolean shouldDropExperience() {
        return super.shouldDropExperience();
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this);
    }

    protected LookControl createLookControl() {
        return new LookControl(this) {
            @Override
            protected float rotateTowards(float pFrom, float pTo, float pMaxDelta) {
                return super.rotateTowards(pFrom, pTo, pMaxDelta * 2.5f);
            }
        };
    }

    protected MoveControl createMoveControl() {
        return new MoveControl(this) {
            @Override
            protected float rotlerp(float pSourceAngle, float pTargetAngle, float pMaximumChange) {
                double d0 = this.wantedX - this.mob.getX();
                double d1 = this.wantedZ - this.mob.getZ();
                if (d0 * d0 + d1 * d1 < .5f) {
                    return pSourceAngle;
                } else {
                    return super.rotlerp(pSourceAngle, pTargetAngle, pMaximumChange * .25f);
                }
            }
        };
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    public void playAmbientSound() {
        this.playSound(getAmbientSound(), 1, Mth.randomBetweenInclusive(getRandom(), 5, 10) * .1f);
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new JiangshiAttackGoal(this, 1.0f, 5, 10));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0d));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f));

        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        // TODO PORT 1.20.1: STEP_HEIGHT(3)/ENTITY_INTERACTION_RANGE(1.8) mapped to Forge attributes; SAFE_FALL_DISTANCE(7) emulated via calculateFallDamage override
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 2.4)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7)
                .add(Attributes.FOLLOW_RANGE, 24)
                .add(ForgeMod.ENTITY_REACH.get(), 1.8f);
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        // TODO PORT 1.20.1: SAFE_FALL_DISTANCE attribute (7) does not exist; reduce effective fall distance by the extra 4 blocks
        return super.calculateFallDamage(Math.max(0, pFallDistance - 4), pDamageMultiplier);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return super.doHurtTarget(entity);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_DROWNING)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ANIMATING_RISE, false);
    }

    public boolean isAnimatingRise() {
        return entityData.get(DATA_IS_ANIMATING_RISE);
    }

    public void triggerRiseAnimation() {
        entityData.set(DATA_IS_ANIMATING_RISE, true);
    }

    @Override
    public void tick() {
        if (isAnimatingRise()) {
            if (level().isClientSide)
                clientDiggingParticles(this);
            if (--riseAnimTime < 0) {
                entityData.set(DATA_IS_ANIMATING_RISE, false);
                this.setXRot(0);
                this.setOldPosAndRot();
            }
        }
        else {
            super.tick();
        }
    }

    protected void clientDiggingParticles(LivingEntity livingEntity) {
        RandomSource randomSource = livingEntity.getRandom();
        BlockState blockState = livingEntity.getBlockStateOn();
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            for (int i = 0; i < 15; ++i) {
                double d0 = livingEntity.getX() + (double) Mth.randomBetween(randomSource, -0.5f, 0.5f);
                double d1 = livingEntity.getY();
                double d2 = livingEntity.getZ() + (double) Mth.randomBetween(randomSource, -2.5f, 0.5f);
                livingEntity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d0, d1, d2, 0.0d, 0.0d, 0.0d);
            }
        }
    }

    //Animation
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "movementController", 2, this::movePredicate));
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "riseController", 0, this::risePredicate));
        controllerRegistrar.add(attackController);
    }
    private final AnimationController<JiangshiEntity> attackController = new AnimationController<>(this, "attackController", 0, this::attackPredicate);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final RawAnimation animationHop = RawAnimation.begin().thenPlay("hop");
    private final RawAnimation animationIdle = RawAnimation.begin().thenPlay("idle");
    private final RawAnimation animationRise = RawAnimation.begin().thenPlay("rise");

    @Override
    public double getTick(Object o) {
        return this.tickCount;
    }

    private PlayState movePredicate(software.bernie.geckolib.core.animation.AnimationState event) {
        Vec3 motion = this.getDeltaMovement();
        float horizontalSpeed = (float) Math.sqrt(motion.x * motion.x + motion.z * motion.z);

        if (isAnimatingRise()) {
            return PlayState.STOP;
        } else if (horizontalSpeed > 0.001f) {
            event.getController().setAnimation(animationHop);
            if (horizontalSpeed > 0.5f) {
                event.getController().setAnimationSpeed(this.walkAnimation.speed() * 1.8f);
            } else event.getController().setAnimationSpeed(1);
        } else {
            event.getController().setAnimation(animationIdle);
        }
        return PlayState.CONTINUE;
    }

    private PlayState risePredicate(software.bernie.geckolib.core.animation.AnimationState event) {
        if (!isAnimatingRise())
            return PlayState.STOP;
        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(animationRise);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    RawAnimation animationToPlay = null;

    @Override
    public void playAnimation(String animationID) {
        try {
            var attackType = AttackType.valueOf(animationID);
            animationToPlay = RawAnimation.begin().thenPlay(attackType.data.animationId);
        } catch (Exception ignore) {
            ISS_MagicFromTheEast.LOGGER.error("Entity {} Failed to play animation: {}", this, animationID);
        }
    }

    private PlayState attackPredicate(AnimationState<JiangshiEntity> animationEvent) {
        var controller = animationEvent.getController();

        if (this.animationToPlay != null) {
            controller.forceAnimationReset();
            controller.setAnimation(animationToPlay);
            animationToPlay = null;
        }
        return PlayState.CONTINUE;
    }

    static class JiangshiAttackGoal extends WarlockAttackGoal {

        final JiangshiEntity jiangshi;
        int meleeAnimTimer = 1;
        public AttackType attacks = AttackType.MELEE;

        public JiangshiAttackGoal(JiangshiEntity jiangshi, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
            super(jiangshi, pSpeedModifier, minAttackInterval, maxAttackInterval);
            this.jiangshi = jiangshi;
            this.wantsToMelee = true;
        }

        @Override
        protected float meleeBias() {
            return 1f;
        }

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
                if (attacks.data.isHitFrame(meleeAnimTimer)) {
                    Vec3 push = target.position().subtract(mob.position()).normalize().scale(.1f);
                    mob.push(push.x, push.y, push.z);
                    if (distance <= meleeRange && Utils.hasLineOfSight(mob.level, mob, target, true)) {
                        boolean flag = this.mob.doHurtTarget(target);
                        if (flag) {
                            if (jiangshi instanceof SummonedJiangshiEntity summonedJiangshi
                                    && summonedJiangshi.getSummoner() != null
                                    && summonedJiangshi.getSummoner() instanceof ServerPlayer serverPlayer) {
                                var RUSTED_SWORD = ((RustedCoinsSword) MFTEItemRegistries.RUSTED_COINS_SWORD.get());
                                if (RUSTED_SWORD.isEquippedBy(serverPlayer)) {
                                    summonedJiangshi.heal((float) summonedJiangshi.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5f);
                                }
                            } else jiangshi.heal((float) jiangshi.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.25f);
                        }
                    }
                }
            } else if (target != null && !target.isDeadOrDying()) {
                doMeleeAction();
            } else if (meleeAnimTimer == 0) {
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
                }
            }
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
            if (attacks != null) {
                this.mob.swing(InteractionHand.MAIN_HAND);
                meleeAnimTimer = attacks.data.lengthInTicks;
                PacketDistributor.sendToPlayersTrackingEntity(jiangshi, new SyncAnimationPacket<>(attacks.toString(), jiangshi));
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
    }
}
