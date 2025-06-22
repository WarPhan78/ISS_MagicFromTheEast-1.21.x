package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_drape.JadeDrapesEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;

@AutoSpellConfig
public class DrapesOfReflectionSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "drapes_of_reflection");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", Utils.stringTruncation(getDrapesHP(spellLevel, caster), 1)),
                Component.translatable("ui.iss_magicfromtheeast.reflect_damage_percent", Utils.stringTruncation(getDrapesReflectionPercent(spellLevel, caster) * 100, 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(6)
            .setCooldownSeconds(30)
            .build();

    public DrapesOfReflectionSpell() {
        this.manaCostPerLevel = 6;
        this.baseSpellPower = 3;
        this.spellPowerPerLevel = 4;
        this.baseManaCost = 35;
        this.castTime = 0;
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
        JadeDrapesEntity drapes = new JadeDrapesEntity(level, getDrapesHP(spellLevel, entity), entity, true);
        Vec3 spawn = Utils.raycastForEntity(level, entity, 3, true).getLocation();
        drapes.setPos(spawn);
        drapes.setRotation(entity.getYRot());
        drapes.setPercentReflectDamage(getDrapesReflectionPercent(spellLevel, entity));
        level.addFreshEntity(drapes);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDrapesHP(int spellLevel, LivingEntity caster) {
        return 4 + getSpellPower(spellLevel, caster);
    }

    private float getDrapesReflectionPercent(int spellLevel, LivingEntity caster) {
        return (float) (10 + (spellLevel * 10)) / 100;
    }
}
