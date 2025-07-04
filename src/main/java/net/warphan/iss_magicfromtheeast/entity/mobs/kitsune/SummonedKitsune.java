package net.warphan.iss_magicfromtheeast.entity.mobs.kitsune;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;

import javax.annotation.Nullable;
import java.util.UUID;

public class SummonedKitsune extends Fox implements IMagicSummon {
    public SummonedKitsune(EntityType<? extends Fox> pEntityType, Level plevel) {
        super(pEntityType, plevel);
        xpReward = 0;
    }

    public SummonedKitsune(Level level, LivingEntity owner) {
        this(MFTEEntityRegistries.SUMMONED_KITSUNE.get(), level);
        setSummoner(owner);
    }

    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity) || this.isAlliedHelper(entity);
    }

    @Override
    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            discard();
        }
    }

    @Override
    public void onRemovedFromLevel() {
        this.onRemovedHelper(this, MFTEEffectRegistries.SUMMON_KITSUNE_TIMER);
        super.onRemovedFromLevel();
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.doMeleeAttack(this, entity, MFTESpellRegistries.KITSUNE_PACK_SPELL.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (shouldIgnoreDamage(source)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.summonerUUID = OwnerHelper.deserializeOwner(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OwnerHelper.serializeOwner(compoundTag, summonerUUID);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new KitsuneMeleeAttack(1.2000000476837158, true));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.6f, 15, 5, false, 25));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0d));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }

    class KitsuneMeleeAttack extends MeleeAttackGoal {
        public KitsuneMeleeAttack(double p_28720_, boolean p_28721_) {
            super(SummonedKitsune.this, p_28720_, p_28721_);
        }

        protected void checkAndPerformAttack(LivingEntity p_28724_) {
            if (this.canPerformAttack(p_28724_)) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(p_28724_);
                SummonedKitsune.this.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
            }

        }

        public void start() {
            SummonedKitsune.this.setIsInterested(false);
            super.start();
        }

        public boolean canUse() {
            return !SummonedKitsune.this.isSitting() && !SummonedKitsune.this.isSleeping() && !SummonedKitsune.this.isCrouching() && !SummonedKitsune.this.isFaceplanted() && super.canUse();
        }
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFood(ItemStack p_28594_) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }
}
