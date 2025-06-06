package net.warphan.iss_magicfromtheeast.entity.spells.qigong_controlling;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;

public class PullField extends AbstractConeProjectile {
    public PullField(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public PullField(Level level, LivingEntity entity) {
        super(MFTEEntityRegistries.PULL_FIELD.get(), level, entity);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void spawnParticles() {}

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        var owner = getOwner();
        var entity = entityHitResult.getEntity();
        if (owner != null && !DamageSources.isFriendlyFireBetween(owner, entity)) {
            Vec3 pullIn = owner.position().subtract(entity.position().add(0, 0, 0));
            pullIn = pullIn.normalize().scale(-0.5);
            entity.setDeltaMovement(entity.getDeltaMovement().subtract(pullIn));
        }
    }
}
