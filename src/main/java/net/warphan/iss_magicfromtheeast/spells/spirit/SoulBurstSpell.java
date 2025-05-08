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
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class SoulBurstSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "soul_burst");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(MFTESchoolRegistries.SPIRIT_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(30)
            .build();

    public SoulBurstSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 2;
        this.spellPowerPerLevel = 1;
        this.castTime = 20;
        this.baseManaCost = 40;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((getSpellPower(spellLevel, caster)), 1)),
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getDuration(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(8, 2))
        );
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.WARDEN_SONIC_CHARGE);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.SCULK_SHRIEKER_SHRIEK);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = getRadius(spellLevel, entity);

        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(MFTESchoolRegistries.SPIRIT.get().getTargetingColor(), radius), entity.getX(), entity.getY() + .165f, entity.getZ(), 1, 0, 0, 0, 0, true);
        MagicManager.spawnParticles(level, ParticleTypes.SOUL_FIRE_FLAME, entity.getX(), entity.getY() + 1, entity.getZ(), 100, .25, .25, .25, 0.7f + radius * .1f, false);
        MagicManager.spawnParticles(level, ParticleTypes.SCULK_SOUL, entity.getX(), entity.getY() + 1, entity.getZ(), 40, .15, .15, .15, 0.7f + radius * .1f, false);
        CameraShakeManager.addCameraShake(new CameraShakeData(10, entity.position(), radius * 2));

        float damage = getDamage(spellLevel, entity);
        int duration = getDuration(spellLevel, entity);
        level.getEntities(entity, entity.getBoundingBox().inflate(radius, 4, radius), (target) ->
                    !DamageSources.isFriendlyFireBetween(target, entity)
                    && Utils.hasLineOfSight(level, entity, target, true))
                .forEach(target -> {
            if (target instanceof LivingEntity livingEntity && livingEntity.distanceToSqr(entity) < radius * radius) {
                DamageSources.applyDamage(target, damage, getDamageSource(entity));
                livingEntity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN, duration, 0));
                MagicManager.spawnParticles(level, ParticleTypes.SOUL_FIRE_FLAME, livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() / 2, livingEntity.getZ(), 10, livingEntity.getBbWidth() / 3, livingEntity.getBbHeight() / 3, livingEntity.getBbWidth() / 3, 0.1, false);
            }
        });

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getRadius(int spellLevel, LivingEntity caster) {
        return 8;
    }

    public int getDuration(int spellLevel, LivingEntity caster) {
        return 40 + spellLevel * 20;
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.PREPARE_CROSS_ARMS;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.CAST_T_POSE;
    }
}
