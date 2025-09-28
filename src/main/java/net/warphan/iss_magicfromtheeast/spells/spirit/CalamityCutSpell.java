package net.warphan.iss_magicfromtheeast.spells.spirit;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import net.warphan.iss_magicfromtheeast.spells.MFTESpellAnimations;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class CalamityCutSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "calamity_cut");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(MFTESchoolRegistries.SPIRIT_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(25)
            .build();

    public CalamityCutSpell() {
        this.manaCostPerLevel = 18;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 4;
        this.castTime = 24;
        this.baseManaCost = 30;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", (getDamageText(spellLevel, caster))),
                Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getRange(spellLevel, caster), 1))
        );
    }

    @Override
    public boolean canBeInterrupted(Player player) {
        return false;
    }

    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        return getCastTime(spellLevel);
        //no spamming or anim break
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellID;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(MFTESoundRegistries.KATANA_WIND_UP.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(MFTESoundRegistries.SWORD_STRIKE.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = 0.5f;
        Vec3 forward = entity.getForward().multiply(1, 0, 1).normalize();
        Vec3 start = entity.getEyePosition().subtract(0, 0.5, 0).add(forward.scale(1.5));
        float count = getRange(spellLevel, entity);
        CameraShakeManager.addCameraShake(new CameraShakeData(10, entity.position(), 10));


        for (int i = 0; i < count; i++) {
            Vec3 strikeLocation = start.add(forward.scale(i));
            Vec3 particleLocation = level.clip(new ClipContext(strikeLocation, strikeLocation.add(0, -2, 0), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty())).getLocation().add(0, 0.1, 0);
            MagicManager.spawnParticles(level, ParticleTypes.SCULK_SOUL, particleLocation.x, particleLocation.y, particleLocation.z, 8, 0, 0, 0, 1, false);
            var entities = level.getEntities(entity, AABB.ofSize(strikeLocation, radius, radius * 3, radius));
            var damageSource = this.getDamageSource(entity);
            for (Entity targetEntity : entities) {
                if (targetEntity.isAlive() && targetEntity.isPickable() && Utils.hasLineOfSight(level, strikeLocation.add(0, 1, 0), targetEntity.getBoundingBox().getCenter(), true)) {
                    if (DamageSources.applyDamage(targetEntity, getDamage(spellLevel, entity), damageSource)) {
                        EnchantmentHelper.doPostAttackEffects((ServerLevel) level, targetEntity, damageSource);
                    }
                }
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getRange(int spellLevel, LivingEntity entity) {
        return 9 + spellLevel * 2;
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) + getAdditionalDamage(entity);
    }

    private float getAdditionalDamage(LivingEntity entity) {
        if (entity == null) {
            return 0;
        }
        float weaponDamage = Utils.getWeaponDamage(entity);
        return weaponDamage;
    }


    private String getDamageText(int spellLevel, LivingEntity entity) {
        if (entity != null) {
            float weaponDamage = getAdditionalDamage(entity);
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
        return MFTESpellAnimations.ANIMATION_KATANA_STRIKE_ACTION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.pass();
    }}
