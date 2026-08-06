package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.nephrite_slash.NephriteCrystalEntity;
import net.warphan.iss_magicfromtheeast.particle.JadeSlashParticleOptions;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import net.warphan.iss_magicfromtheeast.util.MFTEParticleHelper;

import java.util.List;
import java.util.Optional;

public class NephriteSlashSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "nephrite_slash");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", getDamageText(spellLevel, caster)));
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(20)
            .build();

    public NephriteSlashSpell() {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 40;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellID;
    }

    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        super.onClientCast(level, spellLevel, entity, castData);
        entity.setYBodyRot(entity.getYRot());
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(MFTESoundRegistries.JADE_SLASH.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = 0.5f;
        Vec3 forward = entity.getForward().multiply(1, 0, 1).normalize();
        Vec3 start = entity.getEyePosition().subtract(0, 0.5, 0).add(forward.scale(1.5));
        float count = 4;

        //Slash hit and damage
        for (int i = 0; i < count; i++) {
            Vec3 hitLocation = start.add(forward.scale(i));
            var entities = level.getEntities(entity, AABB.ofSize(hitLocation, radius, radius * 5, radius));
            var damageSource = this.getDamageSource(entity);
            for (Entity targetEntity : entities) {
                if (targetEntity.isAlive() && targetEntity.isPickable() && Utils.hasLineOfSight(level, hitLocation.add(0, 1, 0), targetEntity.getBoundingBox().getCenter(), true)) {
                    if (DamageSources.applyDamage(targetEntity, getDamage(spellLevel, entity), damageSource)) {
                        targetEntity.invulnerableTime = 0;
                        MagicManager.spawnParticles(level, MFTEParticleHelper.JADE_SHATTER, targetEntity.getX(), targetEntity.getY() + targetEntity.getBbHeight() * .5f, targetEntity.getZ(), 5, targetEntity.getBbWidth() * .5f, targetEntity.getBbHeight() * .5f, targetEntity.getBbWidth() * .5f, .03, false);
                        // 1.20.1: doPostAttackEffects does not exist; doPostDamageEffects is the equivalent
                        EnchantmentHelper.doPostDamageEffects(entity, targetEntity);
                    }
                }
            }
        }

        //Spawning nephrite crystal
        float damage =  getDamage(spellLevel, entity);
        float minScale = 1.2f;
        float maxScale = 3.0f;
        int crystalCount = 8;
        start = Utils.moveToRelativeGroundLevel(level, start, 1, 3).add(0, 0.1, 0);

        float distanceCovered = 0;
        for (int i = 0; i < crystalCount; i++) {
            float f = (float) Math.max(i / (float) crystalCount, (distanceCovered + 1) / crystalCount);
            float scale = Mth.lerp(f, minScale, maxScale);
            Vec3 spawn = start.add(forward.scale((double) i * 0.75));
            var ground = Utils.moveToRelativeGroundLevel(level, spawn, 8);
            spawn = ground.subtract(spawn).scale(Mth.clamp(i / 3f, 0, 1)).add(spawn);
            boolean isBiggestCrystal = i == crystalCount - 1 || distanceCovered + 1 > crystalCount;
            if (isBiggestCrystal) {
                scale = maxScale * 1.2f;
            }

            distanceCovered += (float) forward.horizontalDistance();
            int delay = i;
            if (level.getBlockState(BlockPos.containing(spawn).below()).isFaceSturdy(level, BlockPos.containing(spawn).below(), Direction.UP)) {
                NephriteCrystalEntity crystal = new NephriteCrystalEntity(level, entity);
                if (i % 3 == 0) {
                    crystal.setSilent(true);
                }
                crystal.setCrystalSize(scale);
                crystal.moveTo(spawn.add(0, 0, 0));
                crystal.setWaitTime(delay);
                //biggest crystal deal 75% dmg while smaller crystals deal 30%
                crystal.setDamage(damage * (isBiggestCrystal ? 0.75f : 0.3f));
                crystal.setYRot((entity.getYRot() - 45 + Utils.random.nextIntBetweenInclusive(-20, 20)));
                crystal.setXRot(Utils.random.nextIntBetweenInclusive(-15, 15));
                level.addFreshEntity(crystal);
            }
            if (isBiggestCrystal) {
                break;
            }
        }

        //Slash Particle
        Vec3 up = new Vec3(0, 1, 0);
        if (forward.dot(up) > .999) {
            up = new Vec3(1, 0, 0);
        }
        Vec3 right = up.cross(forward);
        Vec3 particlePos = start.add(forward.scale(2.5)).subtract(forward.scale(4)).add(right.scale(-0.3));
        MagicManager.spawnParticles(level,
                new JadeSlashParticleOptions(
                        (float) forward.x,
                        (float) forward.y,
                        (float) forward.z,
                        (float) right.x,
                        (float) right.y,
                        (float) right.z,
                        1f),
                particlePos.x, particlePos.y + .3, particlePos.z, 1, 0, 0, 0, 0, true);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) + Utils.getWeaponDamage(entity);
    }

    private String getDamageText(int spellLevel, LivingEntity entity) {
        if (entity != null) {
            float weaponDamage = Utils.getWeaponDamage(entity);
            String plus = "";
            if (weaponDamage > 0) {
                plus = String.format(" (+%s)", Utils.stringTruncation(weaponDamage, 1));
            }
            String damage = Utils.stringTruncation(getDamage(spellLevel, entity), 1);
            return damage + plus;
        }
        return "" + getSpellPower(spellLevel, entity);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ONE_HANDED_VERTICAL_UPSWING_ANIMATION;
    }
}
