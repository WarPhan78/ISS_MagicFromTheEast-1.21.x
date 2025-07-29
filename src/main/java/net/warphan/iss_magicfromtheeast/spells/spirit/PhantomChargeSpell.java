package net.warphan.iss_magicfromtheeast.spells.spirit;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.phantom_cavalry.PhantomCavalryVisualEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class PhantomChargeSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "phantom_charge");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(MFTESchoolRegistries.SPIRIT_RESOURCE)
            .setMaxLevel(6)
            .setCooldownSeconds(80)
            .build();

    public PhantomChargeSpell() {
        this.manaCostPerLevel = 45;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 4;
        this.castTime = 0;
        this.baseManaCost = 70;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((getSpellPower(spellLevel, caster) / 2), 1)),
                Component.translatable("ui.iss_magicfromtheeast.row_length", Utils.stringTruncation(spellLevel, 1)));
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.SKELETON_HORSE_DEATH);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

        Vec3 orth = new Vec3(-Mth.cos(-entity.getYRot() * Mth.DEG_TO_RAD - (float) Math.PI), 0, Mth.sin(-entity.getYRot() * Mth.DEG_TO_RAD - (float) Math.PI));

        for (int i = 1; i < spellLevel + 1; i++) {

            PhantomCavalryVisualEntity phantomCavalry = new PhantomCavalryVisualEntity(level, entity);

            if (i % 2 == 0) {
                phantomCavalry.setPos(entity.position().add(orth.scale(i)));
            } else {
                phantomCavalry.setPos(entity.position().add(orth.scale(-i + 1)));
            }

            phantomCavalry.shoot(entity.getLookAngle().multiply(1, 0, 1));
            phantomCavalry.setDamage(getSpellPower(spellLevel, entity));
            level.addFreshEntity(phantomCavalry);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
