package net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.UUID;

public class ExtractedSoul extends LivingEntity implements IEntityWithComplexSpawn, AntiMagicSusceptible {
    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        var owner = getOwner();
        var extractor = getExtractor();
        buffer.writeInt(owner == null ? -1 : owner.getId());
        buffer.writeInt(extractor == null ? -1 : extractor.getId());
        if (entityToCopy == null) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            buffer.writeResourceLocation(BuiltInRegistries.ENTITY_TYPE.getKey(this.entityToCopy));
        }
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        Entity owner = this.level.getEntity(additionalData.readInt());
        Entity extractor = this.level.getEntity(additionalData.readInt());
        if (owner instanceof LivingEntity livingEntity) {
            this.setOwner(livingEntity);
        }
        if (extractor instanceof LivingEntity livingEntity) {
            this.setExtractor(livingEntity);
        }
        if (additionalData.readBoolean()) {
            setEntityTypeToCopy(BuiltInRegistries.ENTITY_TYPE.get(additionalData.readResourceLocation()));
        }
    }

    protected static final EntityDataAccessor<Boolean> DATA_IS_BABY = SynchedEntityData.defineId(ExtractedSoul.class, EntityDataSerializers.BOOLEAN);

    public int duration = -1;
    public float bonusPercent;
    private UUID ownerUUID;
    private UUID extractorUUID;
    private LivingEntity cachedExtractor;
    private LivingEntity cachedOwner;
    @Nullable EntityType<?> entityToCopy;

    public ExtractedSoul(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_BABY, false);
    }

    private HumanoidArm mainArm = HumanoidArm.RIGHT;

    protected static void copyEntityVisualProperties(LivingEntity baseEntity, LivingEntity entityToCopy) {
        baseEntity.moveTo(entityToCopy.getX(), entityToCopy.getY(), entityToCopy.getZ(), entityToCopy.getYRot(), entityToCopy.getXRot());

        baseEntity.setYBodyRot(entityToCopy.yBodyRot);
        baseEntity.yBodyRotO = baseEntity.yBodyRot;
        baseEntity.setYHeadRot(entityToCopy.getYHeadRot());
        baseEntity.yHeadRotO = baseEntity.yHeadRot;

        baseEntity.setPose(entityToCopy.getPose());
        if (baseEntity instanceof ExtractedSoul extractedSoul) {
            extractedSoul.mainArm = entityToCopy.getMainArm();
            if (entityToCopy.isBaby()) {
                extractedSoul.getEntityData().set(DATA_IS_BABY, true);
            }
        } else if (baseEntity.level.isClientSide) {
            if (entityToCopy.isBaby()) {
                if (baseEntity instanceof AgeableMob ageableMob) {
                    ageableMob.setAge(-10);
                } else if (baseEntity instanceof Zombie zombie) {
                    zombie.setBaby(true);
                }
            }
        }
        if (baseEntity.getAttributes().hasAttribute(Attributes.SCALE) && entityToCopy.getAttributes().hasAttribute(Attributes.SCALE)) {
            baseEntity.getAttributes().getInstance(Attributes.SCALE).setBaseValue(entityToCopy.getAttributeValue(Attributes.SCALE));
        }
        if (entityToCopy instanceof Player player) {
            baseEntity.setCustomName(player.getDisplayName());
            baseEntity.setCustomNameVisible(true);
        }
    }

    @Override
    public boolean isBaby() {
        return entityData.get(DATA_IS_BABY);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return entityToCopy == null ? super.getDefaultDimensions(pose) : entityToCopy.getDimensions();
    }

    public void setEntityTypeToCopy(@Nullable EntityType<?> entityToCopy) {
        this.entityToCopy = entityToCopy;
        refreshDimensions();
    }

    public ExtractedSoul(Level level, LivingEntity entityToCopy, LivingEntity extractorEntity) {
        this(MFTEEntityRegistries.EXTRACTED_SOUL.get(), level);
        copyEntityVisualProperties(this, entityToCopy);
        if (!(entityToCopy instanceof Player)) {
            setEntityTypeToCopy(entityToCopy.getType());
        }
        this.setNoGravity(true);
        setOwner(entityToCopy);
        setExtractor(extractorEntity);
    }

    //Owner =  target
    public void setOwner(@javax.annotation.Nullable LivingEntity owner) {
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.cachedOwner = owner;
        }
    }

    public LivingEntity getOwner() {
        if (this.cachedOwner != null && this.cachedOwner.isAlive()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel) {
            if (((ServerLevel) this.level()).getEntity(this.ownerUUID) instanceof LivingEntity livingEntity)
                this.cachedOwner = livingEntity;
            return this.cachedOwner;
        } else {
            return null;
        }
    }

    //Extractor = spell caster
    public void setExtractor(@javax.annotation.Nullable LivingEntity extractor) {
        if (extractor != null) {
            this.extractorUUID = extractor.getUUID();
            this.cachedExtractor = extractor;
        }
    }

    public LivingEntity getExtractor() {
        if (this.cachedExtractor != null && this.cachedExtractor.isAlive()) {
            return this.cachedExtractor;
        } else if (this.extractorUUID != null && this.level() instanceof ServerLevel) {
            if (((ServerLevel) this.level()).getEntity(this.extractorUUID) instanceof LivingEntity livingEntity)
                this.cachedExtractor = livingEntity;
            return this.cachedExtractor;
        } else {
            return null;
        }
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

    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.SCULK_SOUL, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            this.discard();
        }
    }

    public void setDuration(int durationTick) {
        this.duration = durationTick;
    }

    public void setBonusPercent(float bonusAmount) {
        this.bonusPercent = bonusAmount / 100;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return  true;
    }


    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public void setTicksFrozen(int ticksFrozen) {
        return;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SOUL_ESCAPE.value();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SOUL_ESCAPE.value();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.level().isClientSide || this.isInvulnerableTo(pSource) || shouldIgnoreDamage(pSource))
            return false;
        return super.hurt(pSource, pAmount);
    }

    public boolean shouldIgnoreDamage(DamageSource damageSource) {
        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !ServerConfigs.CAN_ATTACK_OWN_SUMMONS.get() && damageSource.getEntity() != null) {
            return DamageSources.isFriendlyFireBetween(this.getOwner(), this);
        }
        return false;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (this.isDeadOrDying() && !this.level.isClientSide) {
            this.onUnSummon();
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.hasUUID("Owner")) {
            this.ownerUUID = compoundTag.getUUID("Owner");
        }
        if (compoundTag.hasUUID("Extractor")) {
            this.extractorUUID = compoundTag.getUUID("Extractor");
        }
        if (compoundTag.contains("entityToCopy")) {
            try {
                setEntityTypeToCopy(BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(compoundTag.getString("entityToCopy"))));
            } catch (Exception ignore) {
            }
        }
        this.duration = compoundTag.getInt("durationTimer");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.ownerUUID != null) {
            compoundTag.putUUID("Owner", this.ownerUUID);
        }
        if (this.extractorUUID != null) {
            compoundTag.putUUID("Extractor", this.extractorUUID);
        }
        if (this.entityToCopy != null) {
            compoundTag.putString("entityToCopy", BuiltInRegistries.ENTITY_TYPE.getKey(entityToCopy).toString());
        }
        compoundTag.putInt("durationTimer", duration);
    }

    @Override
    public HumanoidArm getMainArm() {
        return mainArm;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FOLLOW_RANGE, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        onUnSummon();
    }
}
