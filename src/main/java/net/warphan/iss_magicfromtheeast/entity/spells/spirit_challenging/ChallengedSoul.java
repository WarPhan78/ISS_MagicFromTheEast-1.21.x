package net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.UUID;

public class ChallengedSoul extends LivingEntity implements GeoEntity, IMagicSummon {
    public ChallengedSoul(EntityType<? extends ChallengedSoul> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ChallengedSoul(Level level, LivingEntity target) {
        this(MFTEEntityRegistries.CHALLENGING_SOUL.get(), level);
        this.setNoGravity(true);
        setSoulOwner(target);
    }

    protected UUID targetUUID;
    protected LivingEntity cachedTarget;
    public int duration = -1;
    public float bonusPercent;

    //Summoner = Targeted Entity = Soul Owner
    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level(), cachedTarget, targetUUID);
    }

    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.SCULK_SOUL, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            this.discard();
        }
    }

    public void setSoulOwner(@Nullable LivingEntity target) {
        if (target != null) {
            this.targetUUID = target.getUUID();
            this.cachedTarget = target;
        }
    }

    @Override
    public boolean shouldShowName() {
        return false;
    }

    //Attribute
    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
                .add(Attributes.MAX_HEALTH, 20.0);
    }

    public void setBonusPercent(float bonusAmount) {
        this.bonusPercent = bonusAmount / 100;
    }

    //Duration
    public void setDuration(int durationTick) {
        this.duration = durationTick;
    }

    @Override
    public void tick() {
        super.tick();
        if (duration > 0) {
            duration--;
            if (duration == 0) {
                this.onUnSummon();
            }
        }
    }

    //Sounds, Hurt and Die
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SOUL_ESCAPE.value();
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (this.isDeadOrDying() && !this.level.isClientSide) {
            this.onUnSummon();
        }
    }

    //Misc
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        return;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
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
        return  true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.targetUUID = OwnerHelper.deserializeOwner(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        OwnerHelper.serializeOwner(pCompound, targetUUID);
    }

    //ANIMATION
    private final AnimationController<ChallengedSoul> idleController = new AnimationController<>(this, "controller", 0, this::idlePredicate);
    private final RawAnimation soul_idle = RawAnimation.begin().thenPlay("soul_idle");

    private PlayState idlePredicate(AnimationState<ChallengedSoul> animationState) {
        animationState.getController().setAnimation(soul_idle);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
}
