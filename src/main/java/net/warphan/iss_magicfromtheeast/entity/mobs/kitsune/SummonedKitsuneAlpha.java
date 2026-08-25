package net.warphan.iss_magicfromtheeast.entity.mobs.kitsune;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import org.jetbrains.annotations.Nullable;

public class SummonedKitsuneAlpha extends SummonedKitsune {
    public SummonedKitsuneAlpha(EntityType<? extends SummonedKitsuneAlpha> entityType, Level level) {
        super(entityType, level);
        xpReward = 0;
    }

    public SummonedKitsuneAlpha(Level level, LivingEntity owner) {
        this(MFTEEntityRegistries.SUMMONED_KITSUNE_ALPHA.get(), level);
        setSummoner(owner);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.STEP_HEIGHT, 1.5);
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
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }

        return new Vec3(f,0.0d, f1);
    }

    @Override
    protected float getRiddenSpeed(Player p_278336_) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.75f;
    }

    @Override
    protected void positionRider(Entity passanger, MoveFunction moveFunction) {
        if (this.hasPassenger(passanger)) {
            Vec3 vec3 = this.getPassengerRidingPosition(passanger);
            Vec3 vec31 = passanger.getVehicleAttachmentPoint(this);
            moveFunction.accept(passanger, vec3.x - vec31.x, (vec3.y - 0.2f) - vec31.y, vec3.z - vec31.z);
        }
    }
}
