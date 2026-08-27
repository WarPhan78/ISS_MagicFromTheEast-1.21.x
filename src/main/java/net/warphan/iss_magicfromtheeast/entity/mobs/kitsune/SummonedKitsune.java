package net.warphan.iss_magicfromtheeast.entity.mobs.kitsune;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class SummonedKitsune extends PathfinderMob implements GeoEntity, IMagicSummon {
    public SummonedKitsune(EntityType<? extends PathfinderMob> pEntityType, Level plevel) {
        super(pEntityType, plevel);
        xpReward = 0;
        this.lookControl = createLookControl();
        this.moveControl = createMoveControl();
    }

    public SummonedKitsune(Level level, LivingEntity owner) {
        this(MFTEEntityRegistries.SUMMONED_KITSUNE.get(), level);
        setSummoner(owner);
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
        return SoundEvents.FOX_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.FOX_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    //Summon Stuffs
    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner == null) return;
        SummonManager.setOwner(this, owner);
    }

//    @Override
//    public boolean canStandOnFluid(FluidState p_204042_) {
//        return true;
//    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity) || this.isAlliedHelper(entity);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            setRemoved(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void onRemovedFromWorld() {
        this.onRemovedHelper(this);
        super.onRemovedFromWorld();
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    public void die(DamageSource damageSource) {
        this.onDeathHelper();
        super.die(damageSource);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN.get(), 60, 0));
            livingEntity.invulnerableTime = 0;
        }
        return Utils.doMeleeAttack(this, entity, MFTESpellRegistries.KITSUNE_PACK_SPELL.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (shouldIgnoreDamage(source) || source.is(DamageTypes.FALL)
        ) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new KitsuneMeleeAttack(1.2000000476837158, true));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 1.2f, 15, 5, false, 25));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.4d));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }



    //mimic fox attack goals
    class KitsuneMeleeAttack extends MeleeAttackGoal {
        public KitsuneMeleeAttack(double p_28720_, boolean p_28721_) {
            super(SummonedKitsune.this, p_28720_, p_28721_);
        }

        // 1.20.1: checkAndPerformAttack takes the squared distance and uses getAttackReachSqr (mirrors vanilla Fox.FoxMeleeAttackGoal)
        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            if (this.getTicksUntilNextAttack() <= 0 && pDistToEnemySqr <= this.getAttackReachSqr(pEnemy)) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(pEnemy);
                SummonedKitsune.this.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
            }
        }
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.STEP_HEIGHT, 1);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount % 10 == 0) {
            for (int i = 0; i < 2; i++) {
                Vec3 pos = new Vec3(Utils.getRandomScaled(1), Utils.getRandomScaled(1.0f) + 0.2f, Utils.getRandomScaled(1)).add(this.position());
                Vec3 random = new Vec3(Utils.getRandomScaled(.04f), Utils.getRandomScaled(.04f), Utils.getRandomScaled(.04f));
                level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, random.x, random.y, random.z);
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.tickCount % 80 == 0) {
            heal(1);
        }
    }

    // TODO PORT 1.20.1: canBeLeashed() (1.21, no-arg) -> canBeLeashed(Player) (1.20.1 signature)
    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    //Animation
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "movementController", 2, this::movePredicate));
    }

    private final RawAnimation animationWalk = RawAnimation.begin().thenPlay("walk");
    private final RawAnimation animationRun = RawAnimation.begin().thenPlay("chase");
    private final RawAnimation animationIdle = RawAnimation.begin().thenPlay("idle");

    @Override
    public double getTick(Object o) {return this.tickCount;}

    private PlayState movePredicate(software.bernie.geckolib.animation.AnimationState event) {
        Vec3 motion = this.getDeltaMovement();
        float horizontalSpeed = (float) Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        float runSpeed = 0.12f;

        if (horizontalSpeed > 0.001f && horizontalSpeed < runSpeed) {
            event.getController().setAnimation(animationWalk);
        } else if (horizontalSpeed >= runSpeed) {
            event.getController().setAnimation(animationRun);
            event.getController().setAnimationSpeed(1.8f);
        } else {
            event.getController().setAnimation(animationIdle);
        }
        return PlayState.CONTINUE;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public boolean shouldBeExtraAnimated() {
        return true;
    }

    public boolean shouldAlwaysAnimateHead() {
        return true;
    }
}
