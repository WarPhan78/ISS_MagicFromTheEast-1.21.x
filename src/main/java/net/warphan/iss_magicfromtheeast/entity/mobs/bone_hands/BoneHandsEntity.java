package net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericCopyOwnerTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;

public class BoneHandsEntity extends AbstractSpellCastingMob implements GeoEntity, IMagicSummon, IAnimatedAttacker {
    private static final EntityDataAccessor<Boolean> DATA_IS_RISING = SynchedEntityData.defineId(BoneHandsEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_RETREATING = SynchedEntityData.defineId(BoneHandsEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_DYING = SynchedEntityData.defineId(BoneHandsEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_ATTACKING = SynchedEntityData.defineId(BoneHandsEntity.class, EntityDataSerializers.BOOLEAN);

    public enum AttackAnim {
        BONE_SLAM(40, "bone_slam", 15);

        AttackAnim(int lengthTick, String animationID, int... attackTimeStamp) {
            this.data = new AttackAnimationData(lengthTick, animationID, attackTimeStamp);
        }

        public final AttackAnimationData data;
    }

    public BoneHandsEntity(EntityType<? extends BoneHandsEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BoneHandsEntity(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this(MFTEEntityRegistries.BONE_HAND_ENTITY.get(), level);
        setNoGravity(true);
        noPhysics = true;
        setSummoner(owner);
        if (playRiseAnimation)
            triggerRiseAnimation();
    }

    protected UUID summonerUUID;
    protected LivingEntity cachedSummoner;
    private int riseAnimTick = 20;
    private int retreatAnimTick = 20;
    public int duration;

    //Duration
    public void setDuration(int durationTick) {
        this.duration = durationTick;
    }

    //Summon Stuffs
    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    public void setSummoner(@Nullable LivingEntity summoner) {
        if (summoner != null) {
            this.summonerUUID = summoner.getUUID();
            this.cachedSummoner = summoner;
        }
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity) || this.isAlliedHelper(entity);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.summonerUUID = OwnerHelper.deserializeOwner(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        OwnerHelper.serializeOwner(pCompound, summonerUUID);
    }

    @Override
    public void onUnSummon() {
        this.isAnimatingRetreat();
        this.entityData.set(DATA_IS_RETREATING, true);
    }

    //Goal and Attributes
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new BoneHandAttackGoal(this, 1f, 0, 0));

        this.targetSelector.addGoal(1, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0f)
                .add(Attributes.ATTACK_DAMAGE, 5.0f)
                .add(Attributes.MAX_HEALTH, 30.0f);
    }

    //Body Rotation
    @Override
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this);
    }

    @Override
    protected LookControl createLookControl() {
        return super.createLookControl();
    }

    //Immunity - may expand in the future
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    //Hurt, Die and Damage
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && (isAnimatingRise() || shouldIgnoreDamage(pSource))) {
            return false;
        }
        if (pAmount > 10 && !pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            pAmount = 10;
            return super.hurt(pSource, pAmount);
        } else
            return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack(this, pEntity, MFTESpellRegistries.BONE_HANDS_SPELL.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
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

    //Sounds
    @Override
    public void playAmbientSound() {
        this.playSound(getAmbientSound(), 2, Mth.randomBetweenInclusive(getRandom(), 2, 10) * .1f);
    }

    protected SoundEvent getAmbientSound() {
        return MFTESoundRegistries.BONE_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return MFTESoundRegistries.BONE_HURT.get();
    }

    //Rising Data
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_RISING, false);
        pBuilder.define(DATA_IS_RETREATING, false);
        pBuilder.define(DATA_IS_DYING, false);
        pBuilder.define(DATA_IS_ATTACKING, false);
    }

    public boolean isAnimatingRise() {
        return entityData.get(DATA_IS_RISING);
    }

    public boolean isAnimatingRetreat() {
        return entityData.get(DATA_IS_RETREATING);
    }

    public boolean isAnimatingDead() {
        return entityData.get(DATA_IS_DYING);
    }

    public boolean isAnimatingAttack() {
        return entityData.get(DATA_IS_ATTACKING);
    }

    public void stopAnimationAttack() {
        entityData.set(DATA_IS_ATTACKING, false);
    }

    public void triggerAnimatingAttack() {
        entityData.set(DATA_IS_ATTACKING, true);
    }

    public void triggerRiseAnimation() {
        entityData.set(DATA_IS_RISING, true);
    }

    @Override
    public boolean isImmobile() {
        return super.isImmobile() || isAnimatingRise() || isAnimatingRetreat();
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
        } else {
            duration--;
            if (duration == 0) {
                this.onUnSummon();
            }
        }
        if (isAnimatingRetreat()) {
            if (--retreatAnimTick < 0) {
                entityData.set(DATA_IS_RETREATING, false);
                this.discard();
            }
        }
    }

    @Override
    public double getTick(Object o) {
        return this.tickCount;
    }

    private final RawAnimation BONE_RISE = RawAnimation.begin().thenPlay("bone_rise");
    private final RawAnimation BONE_IDLE = RawAnimation.begin().thenPlay("bone_idle");
    private final RawAnimation BONE_DEFEATED = RawAnimation.begin().thenPlay("bone_dead");
    private final RawAnimation BONE_RETREAT = RawAnimation.begin().thenPlay("bone_retreat");

    private final AnimationController<BoneHandsEntity> riseController = new AnimationController<>(this, "bone_rise_control", 0, this::risePredicate);
    private final AnimationController<BoneHandsEntity> idleController = new AnimationController<>(this, "bone_idle_control", 0, this::idlePredicate);
    private final AnimationController<BoneHandsEntity> slamController = new AnimationController<>(this, "bone_slamming", 0, this::slamPredicate);
    private final AnimationController<BoneHandsEntity> defeatedController = new AnimationController<>(this, "bone_ded", 0, this::deadPredicate);
    private final AnimationController<BoneHandsEntity> retreatController = new AnimationController<>(this, "bone_bye", 0, this::retreatPredicate);
    RawAnimation animationToPlay = null;

    private PlayState risePredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (!isAnimatingRise())
            return PlayState.STOP;
        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(BONE_RISE);
        }
        return PlayState.CONTINUE;
    }

    private PlayState idlePredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (isAnimatingRise() || isAnimatingAttack())
            return PlayState.STOP;
        else {
            event.getController().setAnimation(BONE_IDLE);
            return PlayState.CONTINUE;
        }
    }

    private PlayState retreatPredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (isAnimatingRetreat()) {
            event.getController().setAnimation(BONE_RETREAT);
        }
        return PlayState.CONTINUE;
    }

    private PlayState deadPredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (isAnimatingDead())
            event.getController().setAnimation(BONE_DEFEATED);
        return PlayState.CONTINUE;
    }

    @Override
    public void playAnimation(String animationID) {
        try {
            var attackAnim = AttackAnim.valueOf(animationID);
            animationToPlay = RawAnimation.begin().thenPlay(attackAnim.data.animationId);
        } catch (Exception ignore) {
            IronsSpellbooks.LOGGER.error("Entity {} Failed to play animation: {}", this, animationID);
        }
    }

    private PlayState slamPredicate(AnimationState<BoneHandsEntity> animationEvent) {
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
        controllerRegistrar.add(slamController);
        controllerRegistrar.add(riseController);
        controllerRegistrar.add(idleController);
        controllerRegistrar.add(retreatController);
        controllerRegistrar.add(defeatedController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
}
