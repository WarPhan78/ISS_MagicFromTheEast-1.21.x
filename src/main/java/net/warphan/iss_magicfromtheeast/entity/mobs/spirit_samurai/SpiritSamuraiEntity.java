package net.warphan.iss_magicfromtheeast.entity.mobs.spirit_samurai;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericCopyOwnerTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericFollowOwnerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.warphan.iss_magicfromtheeast.registries.*;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class SpiritSamuraiEntity extends AbstractSpellCastingMob implements GeoEntity, IAnimatedAttacker, IMagicSummon {
    private static final EntityDataAccessor<Boolean> DATA_IS_RISING = SynchedEntityData.defineId(SpiritSamuraiEntity.class, EntityDataSerializers.BOOLEAN);

    public enum AttackAnim {
        KATANA_CUT(15, "samurai_cut", 10),
        KATANA_SLASH_1(15, "samurai_slash_1", 10),
        KATANA_SLASH_2(15, "samurai_slash_2", 10),
        KATANA_UPPER_CUT(15, "samurai_upper_cut", 10),
        KATANA_STRIKE(15, "samurai_strike", 10),
        KATANA_TECHNIQUE(40, "samurai_technique", 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35);

        AttackAnim(int lengthTick, String animationID, int... attackTimeStamp) {
            this.data = new AttackAnimationData(lengthTick, animationID, attackTimeStamp);
        }

        public final AttackAnimationData data;
    }

    public SpiritSamuraiEntity(EntityType<? extends SpiritSamuraiEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        xpReward = 0;
        this.lookControl = createLookControl();
        this.moveControl = createMoveControl();
    }

    public SpiritSamuraiEntity(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this(MFTEEntityRegistries.REVENANT.get(), level);
        setSummoner(owner);
        if (playRiseAnimation)
            triggerRiseAnimation();
    }

    public int riseTick = 30;

    //Goal & AI
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new SpiritSamuraiAttackGoal(this, 1f, 10, 30));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new GenericFollowOwnerGoal(this, this::getSummoner, 1.0, 12, 5, false, 25));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Mob.class, 8.0f));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 3.5)
                .add(Attributes.STEP_HEIGHT, 1)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MOVEMENT_SPEED, .21)
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
    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.SCULK_SOUL, getX(), getY(), getZ(), 40, 1.5, 2.5, 1.5, .08, false);
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

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(MFTEItemRegistries.SOUL_KATANA));

        return pSpawnData;
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

    //Sounds
    @Override
    public void playAmbientSound() {
        this.playSound(getAmbientSound(), 2, Mth.randomBetweenInclusive(getRandom(), 2, 10) * .1f);
    }

    protected SoundEvent getAmbientSound() {
        return MFTESoundRegistries.SAMURAI_AMBIENT.get();
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
        return Utils.doMeleeAttack(this, pEntity, MFTESpellRegistries.REVENANT_OF_HONOR_SPELL.get().getDamageSource(this, getSummoner()));
    }

    //Animation Data
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_RISING, false);
    }

    public boolean isAnimatingRise() {
        return entityData.get(DATA_IS_RISING);
    }

    public void triggerRiseAnimation() {
        entityData.set(DATA_IS_RISING, true);
    }

    @Override
    public boolean isImmobile() {
        return super.isImmobile() || isAnimatingRise();
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

    private final RawAnimation SAMURAI_RISE = RawAnimation.begin().thenPlay("samurai_rise");

    private final AnimationController<SpiritSamuraiEntity> riseController = new AnimationController<>(this, "rise_controller", 0, this::risePredicate);
    private final AnimationController<SpiritSamuraiEntity> combatController = new AnimationController<>(this, "combat_controller", 0, this::combatPredicate);

    RawAnimation animationToPlay = null;

    private PlayState risePredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (!isAnimatingRise())
            return PlayState.STOP;
        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(SAMURAI_RISE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void playAnimation(String animationID) {
        try {
            var attackAnim = SpiritSamuraiEntity.AttackAnim.valueOf(animationID);
            animationToPlay = RawAnimation.begin().thenPlay(attackAnim.data.animationId);
        } catch (Exception ignore) {
            IronsSpellbooks.LOGGER.error("Entity {} Failed to play animation: {}", this, animationID);
        }
    }

    private PlayState combatPredicate(AnimationState<SpiritSamuraiEntity> animationEvent) {
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
        controllerRegistrar.add(combatController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
}
