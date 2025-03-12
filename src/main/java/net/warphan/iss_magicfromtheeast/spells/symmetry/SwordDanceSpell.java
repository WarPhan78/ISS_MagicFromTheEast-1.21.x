package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;

@AutoSpellConfig
public class SwordDanceSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "sword_dance");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getAmplifierForLevel(spellLevel, caster), 1)));
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(8)
            .setCooldownSeconds(60)
            .build();

    public SwordDanceSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 3;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 20;
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
        return spellId;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SWORD_DANCE_EFFECT, (int) (getSpellPower(spellLevel, entity) * 60), getAmplifierForLevel(spellLevel, entity), false, false, true));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getAmplifierForLevel(int spellLevel, LivingEntity caster) {
        return spellLevel + (int) (getEntityPowerMultiplier(caster) * 0 + 3);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }
}
