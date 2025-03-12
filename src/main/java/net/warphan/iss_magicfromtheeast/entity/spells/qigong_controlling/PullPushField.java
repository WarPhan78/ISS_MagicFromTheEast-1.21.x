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

public class PullPushField extends AbstractConeProjectile {
    public PullPushField(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public PullPushField(Level level, LivingEntity entity) {
        super(MFTEEntityRegistries.PULL_FIELD.get(), level, entity);
    }

    public float pushForce;
    public float damage;
    public float castTime;

    @Override
    public void tick() {
        if (tickCount < (castTime - 30)) {
            this.dealDamageActive = true;
        } else if (tickCount == (castTime - 15)) {
            this.dealDamageActive = true;
            this.playSound(MFTESoundRegistries.PUSH_BURST.get());
        } else if (tickCount > castTime) {
            this.discard();
        }
        super.tick();
    }

    @Override
    public void spawnParticles() {}

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        var owner = getOwner();
        var entity = entityHitResult.getEntity();
        if (owner != null && !DamageSources.isFriendlyFireBetween(owner, entity)) {
            if (tickCount < (castTime - 30)) {
                Vec3 pullIn = owner.position().subtract(entity.position().add(0, 0, 0));
                pullIn = pullIn.normalize().scale(-0.08);
                entity.setDeltaMovement(entity.getDeltaMovement().subtract(pullIn));
            } else if (tickCount == (castTime - 15)) {
                Vec3 pushOut = owner.position().subtract(entity.position().add(0, 0, 0));
                pushOut = pushOut.normalize().scale(pushForce);
                entity.setDeltaMovement(entity.getDeltaMovement().subtract(pushOut));
                DamageSources.applyDamage(entity, damage, MFTESpellRegistries.QIGONG_CONTROLLING_SPELL.get().getDamageSource(this, owner));
                //Sometimes push targets a bit earlier. However, it will make the spell work with cast time reduction stuffs.
            }
        }
    }
}
