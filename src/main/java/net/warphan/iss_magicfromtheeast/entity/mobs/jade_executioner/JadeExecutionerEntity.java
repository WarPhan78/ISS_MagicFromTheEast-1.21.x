package net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.registries.*;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;

public class JadeExecutionerEntity extends AbstractSpellCastingMob implements GeoEntity, IAnimatedAttacker, IMagicSummon {
    private static final EntityDataAccessor<Boolean> DATA_IS_SHOWDOWN = SynchedEntityData.defineId(JadeExecutionerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_DYING = SynchedEntityData.defineId(JadeExecutionerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_ATTACKING = SynchedEntityData.defineId(JadeExecutionerEntity.class, EntityDataSerializers.BOOLEAN);

    public enum AttackAnim {
        AXE_DOWN(25, "axe_down", 15),
        AXE_LEFT(20, "axe_sweep_left", 10),
        AXE_RIGHT(25, "axe_sweep_right", 13),
        SHIELD_BASH(20, "shield_bash", 8),
        BITE(15, "bite", 10);
        //CHARGE_ATTACK(50, "charge", 40, 41, 42, 43, 44, 45); need to be fixed

        AttackAnim(int lengthTick, String animationID, int... attackTimeStamp) {
            this.data = new AttackAnimationData(lengthTick, animationID, attackTimeStamp);
        }

        public final AttackAnimationData data;
    }

    public JadeExecutionerEntity(EntityType<? extends JadeExecutionerEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        xpReward = 0;
        this.lookControl = createLookControl();
        this.moveControl = createMoveControl();
    }

    public JadeExecutionerEntity(Level level, LivingEntity owner, boolean playShowdownAnimation) {
        this(MFTEEntityRegistries.JADE_EXECUTIONER.get(), level);
        setSummoner(owner);
        if (playShowdownAnimation)
            triggerShowdownAnimation();
    }

    protected UUID summonerUUID;
    protected LivingEntity cachedSummoner;
    private int showdownAnimTick = 40;

    //Goal & AI
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new JadeExecutionerAttackGoal(this, 1f, 0, 0));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 48, 0.7f));
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 1.0, 16, 8, false, 35));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Mob.class, 8.0f));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.ATTACK_DAMAGE, 9.0)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 3.5)
                .add(Attributes.STEP_HEIGHT, 2)
                .add(Attributes.ARMOR, 12.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0);
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
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.SCRAPE, getX(), getY(), getZ(), 60, 1.5, 2.5, 1.5, .08, false);
            discard();
        }
    }

    @Override
    public void onRemovedFromLevel() {
        this.onRemovedHelper(this, MFTEEffectRegistries.SUMMON_EXECUTIONER_TIMER);
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

    //Sounds
    @Override
    public void playAmbientSound() {
        this.playSound(getAmbientSound(), 2, Mth.randomBetweenInclusive(getRandom(), 2, 10) * .1f);
    }

    protected SoundEvent getAmbientSound() {
        return MFTESoundRegistries.EXECUTIONER_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return MFTESoundRegistries.EXECUTIONER_HURT.get();
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
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && (isAnimatingShowdown() || shouldIgnoreDamage(pSource))) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack(this, pEntity, MFTESpellRegistries.PUNISHING_HEAVEN_SPELL.get().getDamageSource(this, getSummoner()));
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
        if (!this.level.isClientSide && deathTime >= 15 && !this.isRemoved()) {
            this.remove(RemovalReason.KILLED);
        }
    }

    //Animation Data
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_SHOWDOWN, false);
        pBuilder.define(DATA_IS_DYING, false);
        pBuilder.define(DATA_IS_ATTACKING, false);
    }

    public boolean isAnimatingShowdown() {
        return entityData.get(DATA_IS_SHOWDOWN);
    }

    public boolean isAnimatingDead() {
        return entityData.get(DATA_IS_DYING);
    }

//    public boolean isAnimatingAttack() {
//        return entityData.get(DATA_IS_ATTACKING);
//    }
//
//    public void stopAnimationAttack() {
//        entityData.set(DATA_IS_ATTACKING, false);
//    }
//
//    public void triggerAnimatingAttack() {
//        entityData.set(DATA_IS_ATTACKING, true);
//    }

    public void triggerShowdownAnimation() {
        entityData.set(DATA_IS_SHOWDOWN, true);
    }

    @Override
    public boolean isImmobile() {
        return super.isImmobile() || isAnimatingShowdown();
    }

    //Tick stuffs
    @Override
    public void tick() {
        if (isAnimatingShowdown()) {
            if (!level.isClientSide) {
                if (tickCount == 5) {
                    playSound(MFTESoundRegistries.EXECUTIONER_SHOWDOWN.value(), 16, .75f);
                }
                if (tickCount == 10) {
                    damageOnLandingImpact(this);
                }
                if (tickCount == 20) {
                    playSound(MFTESoundRegistries.ROAR.value(), 20, .95f);
                }
            }
            if (--showdownAnimTick < 0) {
                entityData.set(DATA_IS_SHOWDOWN, false);
                this.setXRot(0);
                this.setOldPosAndRot();
            }
        } else {
            super.tick();
        }
    }

    protected void damageOnLandingImpact(LivingEntity entity) {
        var level = entity.level;
        float radius = 4.5f;

        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(MFTESchoolRegistries.SYMMETRY.get().getTargetingColor(), radius), getX(), getY() + .165f, getZ(), 1, 0, 0, 0, 0, true);
        MagicManager.spawnParticles(level, ParticleTypes.SCRAPE, getX(), getY() + 0.1, getZ(), 60, 2.0, .2, 2.0, 0.5, false);
        CameraShakeManager.addCameraShake(new CameraShakeData(10, entity.position(), radius * 2));

        float damage = entity.getMaxHealth() / 10;
        level.getEntities(entity, entity.getBoundingBox().inflate(radius, 4, radius), (target) -> !DamageSources.isFriendlyFireBetween(target, entity)
                && Utils.hasLineOfSight(level, entity, target, true)).forEach(target -> {
            if (target instanceof LivingEntity livingEntity && livingEntity.distanceToSqr(entity) < radius * radius) {
            DamageSources.applyDamage(target, damage, MFTESpellRegistries.PUNISHING_HEAVEN_SPELL.get().getDamageSource(entity));}});
    }

    //Animations
    @Override
    public double getTick(Object o) {
        return this.tickCount;
    }

    private final RawAnimation EXECUTIONER_SHOWDOWN = RawAnimation.begin().thenPlay("showdown");
    private final RawAnimation EXECUTIONER_IDLE = RawAnimation.begin().thenPlay("idle");
    private final RawAnimation EXECUTIONER_MOVING = RawAnimation.begin().thenPlay("moving");
    private final RawAnimation EXECUTIONER_DEAD = RawAnimation.begin().thenPlay("defeat");

    private final AnimationController<JadeExecutionerEntity> showdownController = new AnimationController<>(this, "showdown_controller", 0, this::showdownPredicate);
    private final AnimationController<JadeExecutionerEntity> idleController = new AnimationController<>(this, "idle_controller", 0, this::idlePredicate);
    private final AnimationController<JadeExecutionerEntity> movingController = new AnimationController<>(this, "moving_controller", 5, this::movePredicate);
    private final AnimationController<JadeExecutionerEntity> deadController = new AnimationController<>(this, "dead_controller", 0, this::deadPredicate);
    private final AnimationController<JadeExecutionerEntity> combatController = new AnimationController<>(this, "combat_controller", 0, this::combatPredicate);
    RawAnimation animationToPlay = null;

    private PlayState showdownPredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (!isAnimatingShowdown())
            return PlayState.STOP;
        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(EXECUTIONER_SHOWDOWN);
        }
        return PlayState.CONTINUE;
    }

    private PlayState idlePredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (isAnimatingShowdown() || this.walkAnimation.speed() > 0.05f)
            return PlayState.STOP;
        else {
            event.getController().setAnimation(EXECUTIONER_IDLE);
            return PlayState.CONTINUE;
        }
    }

    private PlayState movePredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (isAnimatingShowdown() || !this.walkAnimation.isMoving() || this.walkAnimation.speed() <= 0.05f)
            return PlayState.STOP;
        else {
            event.getController().setAnimation(EXECUTIONER_MOVING);
            return PlayState.CONTINUE;
        }
    }

    private PlayState deadPredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (isAnimatingDead())
            event.getController().setAnimation(EXECUTIONER_DEAD);
        return PlayState.CONTINUE;
    }

    @Override
    public void playAnimation(String animationID) {
        try {
            var attackAnim = JadeExecutionerEntity.AttackAnim.valueOf(animationID);
            animationToPlay = RawAnimation.begin().thenPlay(attackAnim.data.animationId);
        } catch (Exception ignore) {
            IronsSpellbooks.LOGGER.error("Entity {} Failed to play animation: {}", this, animationID);
        }
    }

    private PlayState combatPredicate(AnimationState<JadeExecutionerEntity> animationEvent) {
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
        controllerRegistrar.add(showdownController);
        controllerRegistrar.add(idleController);
        controllerRegistrar.add(movingController);
        controllerRegistrar.add(deadController);
        controllerRegistrar.add(combatController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
}
