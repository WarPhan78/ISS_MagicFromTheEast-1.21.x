package net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;

import javax.annotation.Nullable;

public class SummonedJiangshiEntity extends JiangshiEntity implements IMagicSummon {
    public SummonedJiangshiEntity(EntityType<? extends SummonedJiangshiEntity> pEntityType, Level plevel) {
        super(pEntityType, plevel);
        xpReward = 0;
    }

    public SummonedJiangshiEntity(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this(MFTEEntityRegistries.SUMMONED_JIANGSHI.get(), level);
        setSummoner(owner);
        if (playRiseAnimation)
            triggerRiseAnimation();
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new JiangshiAttackGoal(this, 1.0f, 5, 10));
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
        // TODO PORT 1.20.1: STEP_HEIGHT(3)/ENTITY_INTERACTION_RANGE(1.8) mapped to Forge attributes; SAFE_FALL_DISTANCE(7) emulated in JiangshiEntity#calculateFallDamage
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 2.4)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7)
                .add(ForgeMod.ENTITY_REACH.get(), 1.8f);
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
    public void onRemovedFromWorld() {
        this.onRemovedHelper(this);
        super.onRemovedFromWorld();
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.doMeleeAttack(this, entity, MFTESpellRegistries.JIANGSHI_INVOKE_SPELL.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && isAnimatingRise()
                || shouldIgnoreDamage(source)
                || source.is(DamageTypes.DROWN)
        ) {
            return false;
        }
        return super.hurt(source, amount);
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

    //Immune
    @Override
    public boolean isPushable() {
        return super.isPushable() && (isAnimatingRise());
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || isAnimatingRise();
    }
}
