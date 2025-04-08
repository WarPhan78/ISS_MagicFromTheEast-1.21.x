package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.jade_sentinel.JadeSentinel;
import net.warphan.iss_magicfromtheeast.events.MFTESpellSummonEvent;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class JadeSentinelSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_sentinel");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", getSentinelHealth(spellLevel, null)),
                Component.translatable("ui.irons_spellbooks.damage", getSentinelDamage(spellLevel, null))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(300)
            .build();

    public JadeSentinelSpell() {
        this.manaCostPerLevel = 125;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 5;
        this.castTime = 120;
        this.baseManaCost = 500;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.EVOKER_PREPARE_SUMMON);
    }

    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(MFTESoundRegistries.SYMMETRY_CAST.get());
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (entity.isHolding(ItemRegistry.SCROLL.get())) {
            return false;
        }
        return true;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int summonTime = 20 * 60 * 3;

        JadeSentinel jadeSentinel = new JadeSentinel(world, entity, true);
        jadeSentinel.setPos(entity.position());

        jadeSentinel.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getSentinelDamage(spellLevel, entity));
        jadeSentinel.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getSentinelHealth(spellLevel, entity));
        jadeSentinel.setHealth(jadeSentinel.getMaxHealth());
        var event = NeoForge.EVENT_BUS.post(new MFTESpellSummonEvent<JadeSentinel>(entity, jadeSentinel, this.spellId, spellLevel));
        world.addFreshEntity(event.getCreature());

        jadeSentinel.addEffect(new MobEffectInstance(MFTEEffectRegistries.SUMMON_SENTINEL_TIMER, summonTime, 0, false, false, false));
        int effectAmplifier = 0;
        if (entity.hasEffect(MFTEEffectRegistries.SUMMON_SENTINEL_TIMER))
            effectAmplifier += entity.getEffect(MFTEEffectRegistries.SUMMON_SENTINEL_TIMER).getAmplifier() + 1;
        entity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SUMMON_SENTINEL_TIMER, summonTime, effectAmplifier, false, false, true));

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getSentinelHealth(int spellLevel, LivingEntity caster) {
        return 100 + spellLevel * 100;
    }

    private float getSentinelDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }
}
