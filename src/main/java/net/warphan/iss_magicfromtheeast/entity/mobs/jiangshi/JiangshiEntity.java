package net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class JiangshiEntity extends Zombie implements IMagicSummon, GeoAnimatable {
    private static final EntityDataAccessor<Boolean> DATA_IS_ANIMATING_RISE = SynchedEntityData.defineId(JiangshiEntity.class, EntityDataSerializers.BOOLEAN);
//    private static final EntityDataAccessor<Boolean> DATA_IS_WILD = SynchedEntityData.defineId(JiangshiEntity.class, EntityDataSerializers.BOOLEAN);
//    private static final EntityDataAccessor<Boolean> DATA_ANIMATING_CORPSE_RISE = SynchedEntityData.defineId(JiangshiEntity.class, EntityDataSerializers.BOOLEAN);
//    private static final EntityDataAccessor<Boolean> DATA_IS_CORPSE_IDLE = SynchedEntityData.defineId(JiangshiEntity.class, EntityDataSerializers.BOOLEAN);

    public JiangshiEntity(EntityType<? extends Zombie> pEntityType, Level plevel) {
        super(pEntityType, plevel);
//        if (!isWild()) {
        xpReward = 25;
//        } else xpReward = 25;
    }

    public JiangshiEntity(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this(MFTEEntityRegistries.SUMMONED_JIANGSHI.get(), level);
        setSummoner(owner);
        if (playRiseAnimation)
            triggerRiseAnimation();
    }

    private int riseAnimTime = 45;
//    private int corpseRiseTick = 20;

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2f, true));
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.6f, 15, 5, false, 30));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8d));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.STEP_HEIGHT, 4)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.8)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0);
    }

    @Override
    public boolean isPreventingPlayerRest(Player player) {
        return !this.isAlliedTo(player);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_ANIMATING_RISE, false);
//        pBuilder.define(DATA_ANIMATING_CORPSE_RISE, false);
//        pBuilder.define(DATA_IS_CORPSE_IDLE, true);
//        pBuilder.define(DATA_IS_WILD, true);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(MFTEItemRegistries.JIANGSHI_HAT));
        this.setDropChance(EquipmentSlot.HEAD, 0.75f);

        return pSpawnData;
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity) || this.isAlliedHelper(entity);
    }

    //Summon Stuffs
    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner == null) return;
        SummonManager.setOwner(this, owner);
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
    public boolean doHurtTarget(Entity entity) {
//        if (this.isWild()) {
//            return super.doHurtTarget(entity);
//        }
        return Utils.doMeleeAttack(this, entity, MFTESpellRegistries.JIANGSHI_INVOKE_SPELL.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && isAnimatingRise() || shouldIgnoreDamage(source)) {
            return false;
        }
        return super.hurt(source, amount);
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

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.POOF, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            setRemoved(RemovalReason.DISCARDED);
        }
    }

    protected void clientDiggingParticles(LivingEntity livingEntity) {
        RandomSource randomSource = livingEntity.getRandom();
        BlockState blockState = livingEntity.getBlockStateOn();
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            for (int i = 0; i < 15; ++i) {
                double d0 = livingEntity.getX() + (double) Mth.randomBetween(randomSource, -0.5f, 0.5f);
                double d1 = livingEntity.getY();
                double d2 = livingEntity.getZ() + (double) Mth.randomBetween(randomSource, -0.5f, 0.5f);
                livingEntity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d0, d1, d2, 0.0d, 0.0d, 0.0d);
            }
        }
    }


//    //Wild data
//    public boolean isWild() {
//        return entityData.get(DATA_IS_WILD);
//    }
//
//    public boolean isCorpseAnimating() {
//        return entityData.get(DATA_ANIMATING_CORPSE_RISE);
//    }
//
//    public boolean isCorpseIdle() {
//        return entityData.get(DATA_IS_CORPSE_IDLE);
//    }
//
//    public void triggerCorpseRiseAnim() {
//        entityData.set(DATA_IS_CORPSE_IDLE, false);
//        entityData.set(DATA_ANIMATING_CORPSE_RISE, true);
//    }

    public boolean isAnimatingRise() {
        return entityData.get(DATA_IS_ANIMATING_RISE);
    }

    public void triggerRiseAnimation() {
        entityData.set(DATA_IS_ANIMATING_RISE, true);
    }

    //Immune
    @Override
    public boolean isPushable() {
        return super.isPushable() && (isAnimatingRise());
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || isAnimatingRise();
    }

    //Animation
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "rise", 0, this::risePredicate));
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "hop", 0, this::movePredicate));
//        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "corpse", 0, this::corpsePredicate));
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final RawAnimation animation = RawAnimation.begin().thenPlay("jiangshi_rise");
    private final RawAnimation animationHop = RawAnimation.begin().thenPlay("j_hop");
    private final RawAnimation animationCorpseIdle = RawAnimation.begin().thenPlay("corpse_idle");
    private final RawAnimation animationCorpseRise = RawAnimation.begin().thenPlay("corpse_rise");

    @Override
    public double getTick(Object o) {
        return this.tickCount;
    }

    private PlayState risePredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (!isAnimatingRise())
            return PlayState.STOP;
        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(animation);
        }
        return PlayState.CONTINUE;
    }

    private PlayState movePredicate(software.bernie.geckolib.animation.AnimationState event) {
        if (!this.walkAnimation.isMoving() || isAnimatingRise())
            return PlayState.STOP;
        if (this.walkAnimation.isMoving() && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(animationHop);
        }
        return PlayState.CONTINUE;
    }

//    private PlayState corpsePredicate(software.bernie.geckolib.animation.AnimationState event) {
//        if (this.isWild()) {
//            if (isCorpseIdle())
//                event.getController().setAnimation(animationCorpseIdle);
//            if (isCorpseAnimating())
//             event.getController().setAnimation(animationCorpseRise);
//            return PlayState.CONTINUE;
//        }
//        return PlayState.STOP;
//    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
