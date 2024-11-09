package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

@AutoSpellConfig
public class JadeSentinelSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_sentinel");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(240)
            .build();

    public JadeSentinelSpell() {
        this.manaCostPerLevel = 25;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 5;
        this.castTime = 30;
        this.baseManaCost = 100;
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
        return spellId;
    }
}
