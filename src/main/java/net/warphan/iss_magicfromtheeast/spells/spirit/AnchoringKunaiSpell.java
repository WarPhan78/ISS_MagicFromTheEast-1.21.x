package net.warphan.iss_magicfromtheeast.spells.spirit;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.anchoring_kunai.AnchoringKunaiProjectile;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class AnchoringKunaiSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "anchoring_kunai");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(MFTESchoolRegistries.SPIRIT_RESOURCE)
            .setMaxLevel(8)
            .setCooldownSeconds(90)
            .build();

    public AnchoringKunaiSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 3;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 20;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((getSpellPower(spellLevel, caster)), 1)),
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(20 * (spellLevel * 5 + 10), 1)),
                Component.translatable("ui.irons_spellbooks.percent_damage", Utils.stringTruncation((spellLevel + 1) * 5, 0))
        );
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
        return CastType.INSTANT;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(MFTESoundRegistries.MAGIC_PROJECTILE_THROW.get());
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity caster) {
        return 3;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId())) {
            playerMagicData.getPlayerRecasts().addRecast(new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 100, castSource, null), playerMagicData);
        }

        AnchoringKunaiProjectile kunaiProjectile = new AnchoringKunaiProjectile(level, entity);
        kunaiProjectile.setEffectLevel(spellLevel);
        kunaiProjectile.setEffectTick(spellLevel * 5 + 10);
        kunaiProjectile.setDamage(getSpellPower(spellLevel, entity));
        kunaiProjectile.setPos(entity.position().add(0, entity.getEyeHeight() - kunaiProjectile.getBoundingBox().getYsize() * .5f,0));
        kunaiProjectile.shoot(entity.getLookAngle());
        level.addFreshEntity(kunaiProjectile);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
