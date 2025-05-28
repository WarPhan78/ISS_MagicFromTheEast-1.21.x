package net.warphan.iss_magicfromtheeast.entity.spells.qigong_controlling;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;

public class PushZone extends AbstractConeProjectile {
    public PushZone(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public PushZone(Level level, LivingEntity entity) {
        super(MFTEEntityRegistries.PUSH_ZONE.get(), level, entity);
    }

    public float pushForce;
    public float pushDamage;

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 8) {
            this.discard();
        }
    }

    @Override
    public void spawnParticles() {}

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        var owner = getOwner();
        var entity = entityHitResult.getEntity();
        if (owner != null && !DamageSources.isFriendlyFireBetween(owner, entity)) {
            Vec3 pushOut = owner.position().subtract(entity.position().add(0, 0, 0));
            pushOut = pushOut.normalize().scale(pushForce);
            entity.setDeltaMovement(entity.getDeltaMovement().subtract(pushOut));
            DamageSources.applyDamage(entity, pushDamage, MFTESpellRegistries.QIGONG_CONTROLLING_SPELL.get().getDamageSource(this, owner));
            playSound(MFTESoundRegistries.PUSH_BURST.get());
            discard();
        }
    }
}
