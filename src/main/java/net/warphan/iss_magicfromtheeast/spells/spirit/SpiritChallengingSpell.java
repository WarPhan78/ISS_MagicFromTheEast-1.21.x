package net.warphan.iss_magicfromtheeast.spells.spirit;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ChallengedSoul;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;

@AutoSpellConfig
public class SpiritChallengingSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "spirit_challenging");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(MFTESchoolRegistries.SPIRIT_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(120)
            .build();

    public SpiritChallengingSpell() {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 10;
        this.castTime = 20;
        this.baseManaCost = 65;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(getConnectionRange(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.duration", Utils.timeFromTicks(getDuration(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.percent_damage", Utils.stringTruncation(getBonusPercent(spellLevel, caster), 0))
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 6, .35f);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetEntityData) {
            var challengedEntity = targetEntityData.getTarget((ServerLevel) level);
            if (challengedEntity != null) {

                ChallengedSoul challengedSoul = new ChallengedSoul(level, challengedEntity);
                challengedSoul.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(challengedEntity.getHealth());
                challengedSoul.setHealth(challengedSoul.getMaxHealth());

                challengedSoul.setDuration(getDuration(spellLevel, entity));
                challengedSoul.setBonusPercent(getBonusPercent(spellLevel, entity));
                challengedSoul.setPos(entity.position().add(entity.getViewVector(5)));

                TargetedAreaEntity visualEntity = TargetedAreaEntity.createTargetAreaEntity(level, challengedEntity.position(), 10.0f, 0x00ffff);
                visualEntity.setDuration(challengedSoul.duration);
                visualEntity.setOwner(challengedSoul);
                visualEntity.setShouldFade(true);

                level.addFreshEntity(challengedSoul);
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getConnectionRange(int spellLevel, LivingEntity caster) {
        return 10.0f;
    }

    private int getDuration(int spellLevel, LivingEntity caster) {
        return 160 + 40 * spellLevel;
    }

    private int getBonusPercent(int spellLevel, LivingEntity caster) {
        int bonusAmount = (int) getSpellPower(spellLevel, caster);
        return Math.min(bonusAmount, 80);
    }
}
