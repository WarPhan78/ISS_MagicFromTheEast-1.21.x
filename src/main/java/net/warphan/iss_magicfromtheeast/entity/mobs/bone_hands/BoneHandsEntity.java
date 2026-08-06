package net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

public class BoneHandsEntity extends AbstractSpellCastingMob implements IMagicSummon, IAnimatedAttacker {
    @Override
    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
        return;
    }

    private static final EntityDataAccessor<Boolean> DATA_IS_RISING = SynchedEntityData.defineId(BoneHandsEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_DYING = SynchedEntityData.defineId(BoneHandsEntity.class, EntityDataSerializers.BOOLEAN);

//    public enum AttackAnim {
//        PUNCH(30, "punch", 21, 22, 23, 24, 25),
//        POKE(15, "poke", 9),
//        SMASH(30, "smash", 15),
//        SLASH(15, "slash", 10);
//
//        AttackAnim(int lengthTick, String animationID, int... attackTimeStamp) {
//            this.data = new AttackAnimationData(lengthTick, animationID, attackTimeStamp);
//        }
//
//        public final AttackAnimationData data;
//    }

    GenericAnimatedWarlockAttackGoal<? extends BoneHandsEntity> attackGoal;

    public GenericAnimatedWarlockAttackGoal<BoneHandsEntity> makeSimpleAttackGoal() {
        return new GenericAnimatedWarlockAttackGoal<>(this, 1.5, 20, 30)
                .setMoveset(List.of(
                        AttackAnimationData.builder("poke").length(15).attacks(new AttackKeyframe(9, new Vec3(0, 0, 0.75f))).build(),
                        AttackAnimationData.builder("slash").length(15).attacks(new AttackKeyframe(10, new Vec3(0,0,0.45f), new Vec3(0,0,1f))).area(.5f).build()
                ));
    }

    public BoneHandsEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public BoneHandsEntity(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this(MFTEEntityRegistries.BONE_HAND_ENTITY.get(), level);
        setSummoner(owner);
        if (playRiseAnimation)
            triggerRiseAnimation();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @org.jetbrains.annotations.Nullable SpawnGroupData p_21437_) {
        this.setNoGravity(true);
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_);
    }

    //Immunity - may expand in the future
    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void checkFallDamage(double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {}

    private int riseAnimTick = 20;

    //Summon Stuffs
    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner == null) return;
        SummonManager.setOwner(this, owner);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity) || this.isAlliedHelper(entity);
    }

    @Override
    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.SCULK_SOUL, getX(), getY(), getZ(), 20, 1.5, 2.5, 1.5, .08, false);
            setRemoved(RemovalReason.DISCARDED);
        }
    }

    //Goal and Attributes
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        attackGoal = makeSimpleAttackGoal();
        this.goalSelector.addGoal(1, attackGoal.setMeleeBias(1f, 1f));
//        this.goalSelector.addGoal(1, new BoneHandAttackGoal(this, 1.8f, 5, 10));
        this.goalSelector.addGoal(3, new GenericFollowOwnerGoal(this, this::getSummoner, 1.2, 12, 6, true, 30));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 0.8));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 5.0f)
                .add(Attributes.MAX_HEALTH, 60.0f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2f)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.FLYING_SPEED, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 3);
    }

    //Flying movement from ISS Summoned Weapon class
    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, pLevel);
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        flyingPathNavigation.setCanPassDoors(true);
        return flyingPathNavigation;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.tickCount % 8 == 0) {
            var owner = getSummoner();
            var target = getTarget();
            var trackEntity = target == null ? owner : target;
            var targetY = trackEntity == null ? Utils.moveToRelativeGroundLevel(level, this.position(), 3).y + 1 : trackEntity.getY() + 1;
            var f = targetY - getY();
            var force = Math.clamp(f * 0.05, -0.15, 0.15);
            this.setDeltaMovement(this.getDeltaMovement().add(0, force, 0));
        }
        if (this.tickCount % 80 == 0) {
            //self-heal by 2% max health per 4 second
            heal(this.getMaxHealth() * 0.02f);
        }
    }

    //Hurt, Die and Damage
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                && (isAnimatingRise() || shouldIgnoreDamage(pSource)))
        {
            return false;
        }
        else return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack(this, pEntity, MFTESpellRegistries.BONE_HANDS_SPELL.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
        this.onDeathHelper();
        if (this.isDeadOrDying() && !this.level.isClientSide) {
            this.isAnimatingDead();
            this.entityData.set(DATA_IS_DYING, true);
        }
    }

    @Override
    public void tickDeath() {
        this.deathTime++;
        if (!this.level.isClientSide && deathTime >= 20 && !this.isRemoved()) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public void onRemovedFromLevel() {
        this.onRemovedHelper(this);
        super.onRemovedFromLevel();
    }

    //Sounds
    @Override
    public void playAmbientSound() {
        this.playSound(getAmbientSound(), 2, Mth.randomBetweenInclusive(getRandom(), 2, 10) * .1f);
    }

    protected SoundEvent getAmbientSound() {
        return MFTESoundRegistries.ASHIGARU_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        return;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return MFTESoundRegistries.BONE_HURT.get();
    }

    //Rising and Death state Data
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_RISING, false);
        pBuilder.define(DATA_IS_DYING, false);
    }

    public boolean isAnimatingRise() {
        return entityData.get(DATA_IS_RISING);
    }

    public boolean isAnimatingDead() {
        return entityData.get(DATA_IS_DYING);
    }

    public void triggerRiseAnimation() {
        entityData.set(DATA_IS_RISING, true);
    }

    @Override
    public boolean isImmobile() {
        return super.isImmobile() || isAnimatingRise();
    }

    //Animations
    @Override
    public void tick() {
        super.tick();
        if (isAnimatingRise()) {
            if (--riseAnimTick < 0) {
                entityData.set(DATA_IS_RISING, false);
                this.setXRot(0);
                this.setOldPosAndRot();
            }
        }
    }

    @Override
    public double getTick(Object o) {
        return this.tickCount;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final AnimationController<BoneHandsEntity> simpleMeleeController = new AnimationController<>(this, "hand_simple_combat", 0, this::simpleCombatPredicate);
    RawAnimation animationToPlay = null;

    @Override
    public double getBoneResetTime() {
        return 3;
    }



    private final RawAnimation BONE_RISE = RawAnimation.begin().thenPlay("rise");
    private final RawAnimation BONE_IDLE = RawAnimation.begin().thenPlay("idle");
    private final RawAnimation BONE_DEFEATED = RawAnimation.begin().thenPlay("death");

    private final AnimationController<BoneHandsEntity> riseController = new AnimationController<>(this, "bone_rise_control", 0, this::risePredicate);
    private final AnimationController<BoneHandsEntity> idleController = new AnimationController<>(this, "bone_idle_control", 2, this::idlePredicate);
    private final AnimationController<BoneHandsEntity> defeatedController = new AnimationController<>(this, "bone_ded", 0, this::deadPredicate);

    private PlayState risePredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (!isAnimatingRise())
            return PlayState.STOP;
        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(BONE_RISE);
        }
        return PlayState.CONTINUE;
    }

    private PlayState idlePredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (this.isAggressive() || this.isAnimatingRise() || this.isAnimatingDead()) {
            return PlayState.STOP;
        } else
            event.getController().setAnimation(BONE_IDLE);
        return PlayState.CONTINUE;
    }

    private PlayState deadPredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (isAnimatingDead())
            event.getController().setAnimation(BONE_DEFEATED);
        return PlayState.CONTINUE;
    }

    @Override
    public void playAnimation(String animationID) {
        animationToPlay = RawAnimation.begin().thenPlay(animationID);
    }

    private PlayState simpleCombatPredicate(AnimationState<BoneHandsEntity> animationEvent) {
        var controller = animationEvent.getController();

        if (this.animationToPlay != null) {
            controller.forceAnimationReset();
            controller.setAnimation(animationToPlay);
            animationToPlay = null;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(simpleMeleeController);
        controllerRegistrar.add(riseController);
        controllerRegistrar.add(idleController);
        controllerRegistrar.add(defeatedController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
