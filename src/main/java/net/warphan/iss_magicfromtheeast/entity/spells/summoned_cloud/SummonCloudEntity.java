package net.warphan.iss_magicfromtheeast.entity.spells.summoned_cloud;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.particle.FogParticleOptions;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.setup.KeyMappings;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class SummonCloudEntity extends PathfinderMob implements IMagicSummon {
    public SummonCloudEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public SummonCloudEntity(Level level, LivingEntity caster) {
        this(MFTEEntityRegistries.SUMMON_CLOUD_ENTITY.get(),level);
        setSummoner(caster);
    }

    int livingTick = -1;

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0)
                .add(Attributes.STEP_HEIGHT, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.5D);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Mob) {
            return (Mob) entity;
        } else {
            entity = this.getFirstPassenger();
            if (entity instanceof Player) {
                return (Player) entity;
            }

            return null;
        }
    }

    @Override
    public void tick() {
        super.tick();
        livingTick--;
        if (livingTick == 0) {
            this.discard();
        }
        if (!level.isClientSide) {
            MagicManager.spawnParticles(level, new FogParticleOptions(new Vector3f(198 / 255f, 1f, 226 / 255f), 0.4f), this.getX(), this.getY() - 0.25f, this.getZ(), 1, 0.4, - 0.2, 0.4, 2.0, false);
        }
    }

    public void setLivingTick(int livingTick) {
        this.livingTick = livingTick;
    }

    //Summon Stuffs
    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner == null) return;
        SummonManager.setOwner(this, owner);
    }

    @Override
    public void onUnSummon() {
        if (!level.isClientSide) {
            MagicManager.spawnParticles(level, ParticleTypes.SCRAPE, getX(), getY(), getZ(), 25, .5, .5, .5, .25, false);
            setRemoved(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    @Override
    public void onRemovedFromLevel() {
        this.onRemovedHelper(this);
        super.onRemovedFromLevel();
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity) || this.isAlliedHelper(entity);
    }

    //Immunity
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

//    @Override
//    public boolean isPickable() {
//        return false;
//    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    //Riding
    @Override
    public boolean shouldRiderSit() {
        return false;
    }


    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (this.isVehicle()) {
            return super.mobInteract(pPlayer, pHand);
        }
        if (pPlayer == getSummoner()) {
            this.doPlayerRide(pPlayer);
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }

    protected void doPlayerRide(Player pPlayer) {
        if (!this.level.isClientSide) {
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding(this);
        }
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
        double f2 = move.y;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }

        if (KeyMappings.FLIGHT_ASCENT_KEY.isDown()) f2 = 1;
        else if (KeyMappings.FLIGHT_DESCENT_KEY.isDown() && !this.onGround()) f2 = -1;

        return new Vec3(f, f2, f1);
    }

    @Override
    protected float getRiddenSpeed(Player p_278336_) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * .8f;
    }

    @Override
    protected void positionRider(Entity passanger, MoveFunction moveFunction) {
        if (this.hasPassenger(passanger)) {
            Vec3 vec3 = this.getPassengerRidingPosition(passanger);
            Vec3 vec31 = passanger.getVehicleAttachmentPoint(this);
            moveFunction.accept(passanger, vec3.x - vec31.x, (vec3.y + 0.5f) - vec31.y, vec3.z - vec31.z);
        }
    }

}
