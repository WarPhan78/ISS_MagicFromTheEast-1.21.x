package net.warphan.iss_magicfromtheeast.entity.mobs.kitsune;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;

import javax.annotation.Nullable;

public class SummonedKitsune extends Fox implements IMagicSummon {
    public SummonedKitsune(EntityType<? extends Fox> pEntityType, Level plevel) {
        super(pEntityType, plevel);
        xpReward = 0;
    }

    public SummonedKitsune(Level level, LivingEntity owner) {
        this(MFTEEntityRegistries.SUMMONED_KITSUNE.get(), level);
        setSummoner(owner);
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
            MagicManager.spawnParticles(level(), ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
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
    public boolean doHurtTarget(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN, 60, 0));
            livingEntity.invulnerableTime = 0;
        }
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
    public void tick() {
        super.tick();
        if (tickCount % 10 == 0) {
            for (int i = 0; i < 2; i++) {
                Vec3 pos = new Vec3(Utils.getRandomScaled(1), Utils.getRandomScaled(1.0f) + 0.2f, Utils.getRandomScaled(1)).add(this.position());
                Vec3 random = new Vec3(Utils.getRandomScaled(.04f), Utils.getRandomScaled(.04f), Utils.getRandomScaled(.04f));
                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, random.x, random.y, random.z);
            }
        }
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
