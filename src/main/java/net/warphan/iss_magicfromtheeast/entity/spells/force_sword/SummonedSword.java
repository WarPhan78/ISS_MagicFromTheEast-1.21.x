package net.warphan.iss_magicfromtheeast.entity.spells.force_sword;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import net.warphan.iss_magicfromtheeast.setup.KeyMappings;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;

public class SummonedSword extends PathfinderMob implements IMagicSummon, GeoEntity {
    public SummonedSword(EntityType<? extends SummonedSword> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
        xpReward = 0;
    }

    public SummonedSword(Level pLevel, LivingEntity owner) {
        this(MFTEEntityRegistries.FORCE_SWORD.get(), pLevel);
        this.moveControl = new FlyingMoveControl(this, 6, true);
        setSummoner(owner);
    }

    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GenericFollowOwnerGoal(this, this::getSummoner, .8f, 5, 2, true, 15));
    }

    public static AttributeSupplier prepareAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.8D)
                .add(Attributes.FLYING_SPEED, 0.8D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 20.0D)
                .build();
    }

    @Override
    public void tick() {
        spawnParticles();
        if (this.isImmobile() && this.isVehicle()){
            this.getGravity();
        };
        super.tick();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.AMETHYST_BLOCK_CHIME;
    }

    @Override
    public void onUnSummon() {
        if (!level.isClientSide) {
            MagicManager.spawnParticles(level, ParticleTypes.GLOW, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            discard();
        }
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (this.isVehicle()) {
            return super.mobInteract(pPlayer, pHand);
        }
        if (pPlayer == getSummoner()) {
            this.doPlayerRide(pPlayer);
        }
        return InteractionResult.sidedSuccess((this.level.isClientSide));
    }
    protected void doPlayerRide(Player pPlayer) {
        if (!this.level.isClientSide) {
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding(this);
        }
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level, cachedSummoner, summonerUUID);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return false;
        return super.hurt(pSource,pAmount);
    }

    public boolean dealDamage(LivingEntity target) {
        if (target != getSummoner())
            if (DamageSources.applyDamage(target, 0, MFTESpellRegistries.FORCE_SWORD_SPELL.get().getDamageSource(this, getSummoner()))) {
                return true;
            }
        return false;
    }

    public void spawnParticles() {
        if (level.isClientSide) {
            if (Utils.random.nextFloat() < .10f) {
                float radius = .30f;
                Vec3 vec = new Vec3(
                        random.nextFloat() * 2 * radius - radius,
                        random.nextFloat() * 2 * radius - radius,
                        random.nextFloat() * 2 * radius - radius
                );
                level.addParticle(ParticleTypes.SCRAPE, this.getX() + vec.x, this.getY() + vec.y + 0.3, this.getZ() + vec.z, vec.x * .01f, .08 + vec.y * .01f, vec.z * .01f);
            }
        }
    }

    @Override
    public void onRemovedFromLevel() {
        this.onRemovedHelper(this, MFTEEffectRegistries.SUMMON_SWORD_TIMER);
        super.onRemovedFromLevel();
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
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Mob) {
            return (Mob) entity;
        } else {
            entity = this.getFirstPassenger();
            if (entity instanceof Player) {
                return (Player) entity;
            }
        }
        return null;
    }

    @Override
    protected void tickRidden(Player player, Vec3 p_275242_) {
        super.tickRidden(player, p_275242_);
        this.yRotO = this.getYRot();
        this.setYRot(player.getYRot());
        this.setXRot(player.getXRot());
        this.setRot(this.getYRot(), this.getXRot());
        this.yBodyRot = this.yRotO;
        this.yHeadRot = this.getYRot();
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 move) {
        float f = player.xxa * 0.5F;
        float f1 = player.zza;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }
        float f2 = player.yya;
        f1 = f1 > 0? f1 : 0;
        if (KeyMappings.FLIGHT_ASCENT_KEY.isDown()) f2 = 0.5F;
        else if (KeyMappings.FLIGHT_DESCENT_KEY.isDown()) f2 = -0.5F;
        return new Vec3(f, f2, f1);
    }

    @Override
    protected float getRiddenSpeed(Player p_278336_) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    //ANIMATION
    private final RawAnimation swordidle = RawAnimation.begin().thenPlay("animation.force_sword.idle");

    private PlayState predicate(AnimationState event) {
        event.getController().setAnimation(swordidle);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

}
