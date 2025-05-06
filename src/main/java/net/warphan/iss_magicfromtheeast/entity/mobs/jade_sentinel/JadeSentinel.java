//package net.warphan.iss_magicfromtheeast.entity.mobs.jade_sentinel;
//
//import io.redspace.ironsspellbooks.IronsSpellbooks;
//import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
//import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
//import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
//import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
//import io.redspace.ironsspellbooks.entity.mobs.goals.*;
//import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
//import io.redspace.ironsspellbooks.util.OwnerHelper;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.tags.DamageTypeTags;
//import net.minecraft.util.Mth;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.*;
//import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.ai.control.BodyRotationControl;
//import net.minecraft.world.entity.ai.control.LookControl;
//import net.minecraft.world.entity.ai.control.MoveControl;
//import net.minecraft.world.entity.ai.goal.FloatGoal;
//import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
//import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
//import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
//import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
//import net.minecraft.world.entity.monster.Monster;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
//import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
//import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
//import software.bernie.geckolib.animatable.GeoEntity;
//import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
//import software.bernie.geckolib.animation.*;
//import software.bernie.geckolib.animation.AnimationState;
//import software.bernie.geckolib.util.GeckoLibUtil;
//
//import javax.annotation.Nullable;
//import java.util.UUID;
//
//public class JadeSentinel extends AbstractSpellCastingMob implements GeoEntity, IMagicSummon, IAnimatedAttacker {
//    private static final EntityDataAccessor<Boolean> DATA_IS_RISING = SynchedEntityData.defineId(JadeSentinel.class, EntityDataSerializers.BOOLEAN);
//
//    public enum AttackAnim {
//        JADE_STOMP(60, "animation.jade_sentinel.jade_stomp", 35),
//        DAO_STAB(40, "animation.jade_sentinel.dao_stab", 20),
//        DAO_SWEEP(40, "animation.jade_sentinel.dao_sweep", 23);
//        //DAO_CHARGE(80, "animation.jade_sentinel.dao_charge", 10, 20, 30, 40, 50, 60, 70, 71, 72, 73, 74);
//
//        AttackAnim(int lengthInTicks, String animationID, int... attackTimestamps) {
//            this.data = new AttackAnimationData(lengthInTicks, animationID, attackTimestamps);
//        }
//
//        public final AttackAnimationData data;
//    }
//
//    public JadeSentinel(EntityType<? extends JadeSentinel> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
//        xpReward = 0;
//        this.lookControl = createLookControl();
//        this.moveControl = createMoveControl();
//    }
//
//    public JadeSentinel(Level level, LivingEntity owner, boolean playRaiseAnim) {
//        this(MFTEEntityRegistries.JADE_SENTINEL.get(), level);
//        setSummoner(owner);
//        if (playRaiseAnim)
//            triggerRaiseAnim();
//    }
//
//    protected UUID summonerUUID;
//    protected LivingEntity cachedSummoner;
//    private int raiseAnimTime = 50;
//
//    @Override
//    protected void registerGoals() {
//        this.goalSelector.addGoal(0, new FloatGoal(this));
//
//        this.goalSelector.addGoal(1, new JadeSentinelAttackGoal(this, 1f, 0, 0));
//
//        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
//        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
//        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 48, 0.0f));
//        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 5.0f));
//        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Mob.class, 8.0f));
//
//        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
//        this.targetSelector.addGoal(2, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
//        this.targetSelector.addGoal(3, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
//        this.targetSelector.addGoal(4, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
//    }
//
//    public static AttributeSupplier.Builder prepareAttributes() {
//        return Monster.createMonsterAttributes()
//                .add(Attributes.MAX_HEALTH, 200.0)
//                .add(Attributes.ATTACK_DAMAGE, 15.0)
//                .add(Attributes.STEP_HEIGHT, 1)
//                .add(Attributes.ARMOR, 10.0)
//                .add(Attributes.KNOCKBACK_RESISTANCE, 1.5)
//                .add(Attributes.MOVEMENT_SPEED, 0.16)
//                .add(Attributes.FOLLOW_RANGE, 32.0)
//                .add(Attributes.ATTACK_KNOCKBACK, 4.0);
//    }
//
//    @Override
//    protected BodyRotationControl createBodyControl() {
//        return new BodyRotationControl(this);
//    }
//
//    protected LookControl createLookControl() {
//        return new LookControl(this) {
//            @Override
//            protected float rotateTowards(float pFrom, float pTo, float pMaxDelta) {
//                return super.rotateTowards(pFrom, pTo, pMaxDelta * 2.5f);
//            }
//
//            @Override
//            protected boolean resetXRotOnTick() {
//                return getTarget() == null;
//            }
//        };
//    }
//
//    protected MoveControl createMoveControl() {
//        return new MoveControl(this) {
//            @Override
//            protected float rotlerp(float pSourceAngle, float pTargetAngle, float pMaximumChange) {
//                double d0 = this.wantedX - this.mob.getX();
//                double d1 = this.wantedZ - this.mob.getZ();
//                if (d0 * d0 + d1 * d1 < .5f) {
//                    return pSourceAngle;
//                } else {
//                    return super.rotlerp(pSourceAngle, pTargetAngle, pMaximumChange * .25f);
//                }
//            }
//        };
//    }
//
//    //Immunity
//    @Override
//    public boolean isPushable() {
//        return false;
//    }
//
//    @Override
//    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
//        return false;
//    }
//
//    @Override
//    public boolean fireImmune() {
//        return  true;
//    }
//
//    @Override
//    public boolean isPushedByFluid() {
//        return false;
//    }
//
//    @Override
//    public void tick() {
//        if (isAnimatingRise()) {
//            if (--raiseAnimTime < 0) {
//                entityData.set(DATA_IS_RISING, false);
//                this.setXRot(0);
//                this.setOldPosAndRot();
//            }
//        } else {
//            super.tick();
//        }
//    }
//
//    //Sounds
//    @Override
//    public void playAmbientSound() {
//        this.playSound(getAmbientSound(), 2, Mth.randomBetweenInclusive(getRandom(), 5, 10) * .1f);
//    }
//
//    protected SoundEvent getAmbientSound() {
//        return MFTESoundRegistries.JADE_SENTINEL_AMBIENT.get();
//    }
//
//    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
//        return MFTESoundRegistries.JADE_SENTINEL_HURT.get();
//    }
//
//    protected void playStepSound(BlockPos pPos, BlockState pState) {
//        this.playSound(MFTESoundRegistries.JADE_SENTINEL_STEP.get(), .25f, 1f);
//    }
//
//    //Summoning
//    @Override
//    public LivingEntity getSummoner() {
//        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
//    }
//
//    public void setSummoner(@Nullable LivingEntity owner) {
//        if (owner != null) {
//            this.summonerUUID = owner.getUUID();
//            this.cachedSummoner = owner;
//        }
//    }
//
//    @Override
//    public boolean isAlliedTo(Entity pEntity) {
//        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
//    }
//
//    //Hurt, Die and Damage
//    @Override
//    public boolean hurt(DamageSource pSource, float pAmount) {
//        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && (isAnimatingRise() || shouldIgnoreDamage(pSource))) {
//            return false;
//        }
//        return super.hurt(pSource, pAmount);
//    }
//
//    @Override
//    public void die(DamageSource pDamageSource) {
//        this.onDeathHelper();
//        super.die(pDamageSource);
//    }
//
//    @Override
//    public void onRemovedFromLevel() {
//        this.onRemovedHelper(this, MFTEEffectRegistries.SUMMON_SENTINEL_TIMER);
//        super.onRemovedFromLevel();
//    }
//
//    @Override
//    public void readAdditionalSaveData(CompoundTag pCompound) {
//        super.readAdditionalSaveData(pCompound);
//        this.summonerUUID = OwnerHelper.deserializeOwner(pCompound);
//    }
//
//    @Override
//    public void addAdditionalSaveData(CompoundTag pCompound) {
//        super.addAdditionalSaveData(pCompound);
//        OwnerHelper.serializeOwner(pCompound, summonerUUID);
//    }
//
//    @Override
//    public void onUnSummon() {
//        if (!level().isClientSide) {
//            MagicManager.spawnParticles(level(), ParticleTypes.SCRAPE, getX(), getY(), getZ(), 100, 3, 5, 3, .08, false);
//            discard();
//        }
//    }
//
//    @Override
//    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
//        super.defineSynchedData(pBuilder);
//        pBuilder.define(DATA_IS_RISING, false);
//    }
//
//    public boolean isAnimatingRise() {
//        return entityData.get(DATA_IS_RISING);
//    }
//
//    public void triggerRaiseAnim() {
//        entityData.set(DATA_IS_RISING, true);
//    }
//
//    @Override
//    public boolean isImmobile() {
//        return super.isImmobile() || isAnimatingRise();
//    }
//
//    //ANIMATION
//    @Override
//    public double getTick(Object o) {
//        return this.tickCount;
//    }
//
//    private PlayState risePredicate(software.bernie.geckolib.animation.AnimationState event) {
//        if (!isAnimatingRise())
//            return PlayState.STOP;
//        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
//            event.getController().setAnimation(JADE_RISE);
//        }
//        return PlayState.CONTINUE;
//    }
//
//    private PlayState idlePredicate(software.bernie.geckolib.animation.AnimationState event) {
//        if (isAnimating())
//            return PlayState.STOP;
//        event.getController().setAnimation(JADE_IDLE);
//        this.shouldAlwaysAnimatedHead();
//        return PlayState.CONTINUE;
//    }
//
//    private PlayState movePredicate(software.bernie.geckolib.animation.AnimationState event) {
//        if (!this.walkAnimation.isMoving() || isAnimating())
//            return PlayState.STOP;
//        if (this.walkAnimation.isMoving() && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
//            event.getController().setAnimationSpeed(1.2).setAnimation(JADE_MOVE);
//        }
//        return PlayState.CONTINUE;
//    }
//
//    //Death animation will be fixed in the future.
//    private PlayState deadPredicate(software.bernie.geckolib.animation.AnimationState event) {
//        if (this.isAlive())
//            return PlayState.STOP;
//        if (this.isDeadOrDying()) {
//            event.getController().setAnimation(JADE_DEFEATED);
//        }
//        return PlayState.STOP;
//    }
//
//    private final RawAnimation JADE_RISE = RawAnimation.begin().thenPlay("animation.jade_sentinel.jade_rise");
//    private final RawAnimation JADE_IDLE = RawAnimation.begin().thenPlay("animation.jade_sentinel.jade_idle");
//    //private final RawAnimation JADE_MOVESTART = RawAnimation.begin().thenPlay("animation.jade_sentinel.jade_movestart");
//    private final RawAnimation JADE_MOVE = RawAnimation.begin().thenPlay("animation.jade_sentinel.jade_move");
//    //private final RawAnimation JADE_MOVEND = RawAnimation.begin().thenPlay("animation.jade_sentinel.jade_movend");
//    private final RawAnimation JADE_DEFEATED = RawAnimation.begin().thenPlay("animation.jade_sentinel.jade_defeated");
//
//    private final AnimationController<JadeSentinel> riseController = new AnimationController<>(this, "js_rise_controller", 0, this::risePredicate);
//    private final AnimationController<JadeSentinel> idleController = new AnimationController<>(this, "js_idle_controller", 0, this::idlePredicate);
//    private final AnimationController<JadeSentinel> deadController = new AnimationController<>(this, "js_dead_controller", 0, this::deadPredicate);
//    private final AnimationController<JadeSentinel> moveController = new AnimationController<>(this, "js_move_controller", 0, this::movePredicate);
//    private final AnimationController<JadeSentinel> combatController = new AnimationController<>(this, "js_combat_controller", 0, this::combatPredicate);
//    RawAnimation animationToPlay = null;
//
//    @Override
//    public void playAnimation(String animationID) {
//        try {
//            var attackAnim = AttackAnim.valueOf(animationID);
//            animationToPlay = RawAnimation.begin().thenPlay(attackAnim.data.animationId);
//        } catch (Exception ignore) {
//            IronsSpellbooks.LOGGER.error("Entity {} Failed to play animation: {}", this, animationID);
//        }
//    }
//
//    private PlayState combatPredicate(AnimationState<JadeSentinel> animationEvent) {
//        var controller = animationEvent.getController();
//
//        if (this.animationToPlay != null) {
//            controller.forceAnimationReset();
//            controller.setAnimation(animationToPlay);
//            animationToPlay = null;
//        }
//        return PlayState.CONTINUE;
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
//        controllerRegistrar.add(riseController);
//        controllerRegistrar.add(idleController);
//        controllerRegistrar.add(deadController);
//        controllerRegistrar.add(moveController);
//        controllerRegistrar.add(combatController);
//        super.registerControllers(controllerRegistrar);
//    }
//
//    @Override
//    public AnimatableInstanceCache getAnimatableInstanceCache() {
//        return this.cache;
//    }
//
//    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
//
//    public boolean isAnimating() {
//        return isAnimatingRise();
//    }
//
//    public boolean shouldAlwaysAnimatedHead() {
//        return true;
//    }
//}
