package net.warphan.iss_magicfromtheeast.entity.mobs.spirit_ashigaru;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class SpiritAshigaruEntity extends AbstractSpellCastingMob implements GeoEntity, IAnimatedAttacker, IMagicSummon {
    private static final EntityDataAccessor<Boolean> DATA_IS_RISING = SynchedEntityData.defineId(SpiritAshigaruEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_MELEE_TYPE = SynchedEntityData.defineId(SpiritAshigaruEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_RANGE_TYPE = SynchedEntityData.defineId(SpiritAshigaruEntity.class, EntityDataSerializers.BOOLEAN);

    public enum AttackType {
        MELEE(20, "melee_attack", 13),
        RANGE(50, "range_attack", 30);

        AttackType(int lengthTick, String animationID, int... attackTimeStamp) {
            this.data = new AttackAnimationData(lengthTick, animationID, attackTimeStamp);
        }

        public final AttackAnimationData data;
    }

    public SpiritAshigaruEntity(EntityType<? extends SpiritAshigaruEntity> entityType, Level level) {
        super(entityType, level);
        xpReward = 0;
        this.lookControl = createLookControl();
        this.moveControl = createMoveControl();
    }

    public SpiritAshigaruEntity(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this(MFTEEntityRegistries.ASHIGARU.get(), level);
        setSummoner(owner);
        if (playRiseAnimation)
            triggerRiseAnimation();
    }

    public int riseTick = 25;

    //Goal & AI
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new SpiritAshigaruAttackGoal(this, 1f, 10, 30));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new GenericFollowOwnerGoal(this, this::getSummoner, 1.0, 12, 5, false, 25));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Mob.class, 8.0f));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
        this.targetSelector.addGoal(5, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 4.0)
                .add(Attributes.STEP_HEIGHT, 1)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.6)
                .add(Attributes.MOVEMENT_SPEED, .33)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.6);
    }

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
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.getBoolean("melee")) {
            setMeleeType();
        }
        if (pCompound.getBoolean("range")) {
            setRangeType();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (isMeleeType()) {
            pCompound.putBoolean("melee", true);
        }
        if (isRangeType()) {
            pCompound.putBoolean("range", true);
        }
    }

    @Override
    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.SCULK_SOUL, getX(), getY(), getZ(), 20, 1.5, 2.5, 1.5, .08, false);
            setRemoved(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void onRemovedFromLevel() {
        this.onRemovedHelper(this);
        super.onRemovedFromLevel();
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    //Controlling
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

            @Override
            protected boolean resetXRotOnTick() {
                return getTarget() == null;
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

    //Data
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_RISING, false);
        builder.define(DATA_IS_MELEE_TYPE, false);
        builder.define(DATA_IS_RANGE_TYPE, false);
    }

    public boolean isAnimatingRise() {
        return entityData.get(DATA_IS_RISING);
    }

    public void triggerRiseAnimation() {
        entityData.set(DATA_IS_RISING, true);
    }

    public boolean isMeleeType() {
        return entityData.get(DATA_IS_MELEE_TYPE);
    }

    public void setMeleeType() {
        entityData.set(DATA_IS_MELEE_TYPE, true);
    }

    public boolean isRangeType() {
        return entityData.get(DATA_IS_RANGE_TYPE);
    }

    public void setRangeType() {
        entityData.set(DATA_IS_RANGE_TYPE, true);
    }

    //Sounds
    @Override
    public void playAmbientSound() {
        this.playSound(getAmbientSound(), 2, Mth.randomBetweenInclusive(getRandom(), 2, 10) * .1f);
    }

    protected SoundEvent getAmbientSound() {
        return MFTESoundRegistries.ASHIGARU_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return MFTESoundRegistries.SPIRIT_MOB_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return MFTESoundRegistries.SPIRIT_MOB_DEATH.get();
    }

    //Immunity
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    //Hurt, Die and Damage
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && (isAnimatingRise() || shouldIgnoreDamage(pSource))) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack(this, pEntity, MFTESpellRegistries.ASHIGARU_SQUAD_SPELL.get().getDamageSource(this, getSummoner()));
    }

    //Tick stuffs
    @Override
    public void tick() {
        super.tick();
        if (isAnimatingRise()) {
            if (--riseTick < 0) {
                entityData.set(DATA_IS_RISING, false);
                this.setXRot(0);
                this.setOldPosAndRot();
            }
        }
    }

    //Animations
    @Override
    public double getTick(Object o) {
        return this.tickCount;
    }

    private final RawAnimation ASHIGARU_RISE = RawAnimation.begin().thenPlay("rise");

    private final AnimationController<SpiritAshigaruEntity> riseController = new AnimationController<>(this, "rise_controller", 0, this::risePredicate);
    private final AnimationController<SpiritAshigaruEntity> attackTypeController = new AnimationController<>(this, "attack_type_controller", 0, this::attackTypePredicate);

    RawAnimation animationToPlay = null;

    private PlayState risePredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (!isAnimatingRise())
            return PlayState.STOP;
        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(ASHIGARU_RISE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void playAnimation(String animationID) {
        try {
            var attackType = SpiritAshigaruEntity.AttackType.valueOf(animationID);
            animationToPlay = RawAnimation.begin().thenPlay(attackType.data.animationId);
        } catch (Exception ignore) {
            IronsSpellbooks.LOGGER.error("Entity {} Failed to play animation: {}", this, animationID);
        }
    }

    private PlayState attackTypePredicate(AnimationState<SpiritAshigaruEntity> animationEvent) {
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
        controllerRegistrar.add(riseController);
        controllerRegistrar.add(attackTypeController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
}
