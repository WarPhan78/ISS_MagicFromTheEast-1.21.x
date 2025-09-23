package net.warphan.iss_magicfromtheeast.entity.spirit_arrow;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantmentHelper;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;

import javax.annotation.Nullable;
import java.util.Optional;

public class SpiritArrow extends AbstractMagicProjectile {
    private static final EntityDataAccessor<Boolean> SOUL_FLAME = SynchedEntityData.defineId(SpiritArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> GHOSTLY_COLD = SynchedEntityData.defineId(SpiritArrow.class, EntityDataSerializers.BOOLEAN);

    public SpiritArrow(EntityType<? extends SpiritArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        this.firedFromWeapon = null;
    }

    public SpiritArrow(Level level, Entity shooter, @Nullable ItemStack stack) {
        this (MFTEEntityRegistries.SPIRIT_ARROW.get(), level);
        setOwner(shooter);
        this.firedFromWeapon = stack;

        if (stack != null && level instanceof ServerLevel serverLevel) {
            MFTEEnchantmentHelper.onSpiritArrowSpawned(serverLevel, stack, this);
        }
    }

    private ItemStack firedFromWeapon;

    @Override
    public void shoot(Vec3 rotation) {
        this.setDeltaMovement(rotation);
    }

    @Override
    public void trailParticles() {
        Vec3 pos = this.getBoundingBox().getCenter().add(getDeltaMovement());
        pos = pos.add(getDeltaMovement());

        level.addParticle(ParticleTypes.ENCHANTED_HIT, pos.x, pos.y - 0.25f, pos.z, 0, 0, 0);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        return;
    }

    @Override
    public float getSpeed() {
        return 64f;
    }

    int onWorldTick = -1;

    @Override
    public void tick() {
        super.tick();
        onWorldTick--;
        if (onWorldTick == 0) {
            discard();
        }
        if (this.entityData.get(SOUL_FLAME)) {
            Vec3 pos = this.getBoundingBox().getCenter().add(getDeltaMovement());
            pos = pos.add(getDeltaMovement());

            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y - 0.25f, pos.z, 0, 0, 0);
        }
        if (this.entityData.get(GHOSTLY_COLD)) {
            Vec3 pos = this.getBoundingBox().getCenter().add(getDeltaMovement());
            pos = pos.add(getDeltaMovement());

            level.addParticle(ParticleTypes.SNOWFLAKE, pos.x, pos.y - 0.25f, pos.z, 0, 0, 0);
        }
    }

    public void setOnWorldTick(int onWorldTick) {
        this.onWorldTick = onWorldTick;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    public boolean canHitEntity(Entity pTarget) {
        return super.canHitEntity(pTarget);
    }

    @Override
    public void handleHitDetection() {
        Vec3 vec3 = this.getDeltaMovement();
        Vec3 pos = this.position();
        Vec3 vec32 = pos.add(vec3);
        HitResult hitResult = level.clip(new ClipContext(pos, vec32, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            onHit(hitResult);
        } else {
            var entities = level.getEntities(this, this.getBoundingBox().inflate(0.25f), this::canHitEntity);
            for (Entity entity : entities) {
                onHit(new EntityHitResult(entity, this.getBoundingBox().getCenter().add(entity.getBoundingBox().getCenter()).scale(0.5f)));
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        var target = entityHitResult.getEntity();
        if (this.getOwner() != null) {
            if (target instanceof LivingEntity livingEntity) {
                DamageSources.ignoreNextKnockback(livingEntity);
                if (this.getSoulFlame()) {
                    livingEntity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN, 100, 0));
                }
                if (this.getGhostlyCold()) {
                    livingEntity.setTicksFrozen(500);
                    if (!level.isClientSide) {
                        MagicManager.spawnParticles(level, ParticleTypes.SNOWFLAKE, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 4, 0.6, 0.6, 0.6, 0.3, false);
                    }
                }
            }
            DamageSources.applyDamage(target, damage, this.damageSources().source(MFTEDamageTypes.SPIRIT_ARROW, this.getOwner()));
        }
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.firedFromWeapon;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.firedFromWeapon != null) {
            compoundTag.put("weapon", this.firedFromWeapon.save(this.registryAccess(), new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("weapon", 10)) {
            this.firedFromWeapon = ItemStack.parse(this.registryAccess(), compoundTag.getCompound("weapon")).orElse(null);
        } else {
            this.firedFromWeapon = null;
        }
    }

    //DATA
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(SOUL_FLAME, false);
        pBuilder.define(GHOSTLY_COLD, false);
    }

    public boolean getSoulFlame() {
        return entityData.get(SOUL_FLAME);
    }

    public void setSoulFlame() {
        entityData.set(SOUL_FLAME, true);
    }

    public boolean getGhostlyCold() {
        return entityData.get(GHOSTLY_COLD);
    }

    public void setGhostlyCold() {
        entityData.set(GHOSTLY_COLD, true);
    }
}
