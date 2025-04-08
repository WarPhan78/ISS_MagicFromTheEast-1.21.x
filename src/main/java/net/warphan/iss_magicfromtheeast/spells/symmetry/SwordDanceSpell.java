package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.sword_dance.JadeSword;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;

@AutoSpellConfig
public class SwordDanceSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "sword_dance");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((getSpellPower(spellLevel, caster)), 1)),
                Component.translatable("ui.irons_spellbooks.projectile_count", Utils.stringTruncation(getSwordCount(spellLevel, caster), 1))
                );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(8)
            .setCooldownSeconds(90)
            .build();

    public SwordDanceSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 3;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 30;
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
        int swordCount = getSwordCount(spellLevel, entity);
        int offset = 360 / swordCount;
        Vec3 center = entity.getEyePosition().add(0, - 0.75f, 0);

        for (int i = 0; i < swordCount; i++) {

            Vec3 motion = new Vec3(0, 0, 1.0);
            motion = motion.xRot(Mth.DEG_TO_RAD);
            motion = motion.yRot(offset * i * Mth.DEG_TO_RAD);

            JadeSword jadeSword = new JadeSword(level, entity);
            jadeSword.setDamage(getSpellPower((spellLevel), entity));
            jadeSword.setWaitTimer(120);
            jadeSword.getSpeed();
            jadeSword.setDeltaMovement(motion);

            jadeSword.moveTo(center.x, center.y, center.z);
            level.addFreshEntity(jadeSword);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getSwordCount(int spellLevel, LivingEntity entity) {
        return spellLevel + 2;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }
}
