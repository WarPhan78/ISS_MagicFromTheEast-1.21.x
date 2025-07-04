package net.warphan.iss_magicfromtheeast.spells.spirit;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.kitsune.SummonedKitsune;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;

@AutoSpellConfig
public class KitsunePackSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "kitsune_pack");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(MFTESchoolRegistries.SPIRIT_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(180)
            .build();

    public KitsunePackSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 5;
        this.castTime = 20;
        this.baseManaCost = 30;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", getKitsuneHealth(spellLevel, null)),
                Component.translatableEscape("ui.irons_spellbooks.damage", getKitsuneDamage(spellLevel, null))
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
        return spellId;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int summonTime = 20 * 60 * 10;
        float radius = 1.5f + .185f * 2;
        for (int i = 0; i < getKitsuneCount(spellLevel, entity); i++) {
            SummonedKitsune kitsune = new SummonedKitsune(world, entity);

            kitsune.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getKitsuneDamage(spellLevel, entity));
            kitsune.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getKitsuneHealth(spellLevel, entity));
            kitsune.setHealth(kitsune.getMaxHealth());

            kitsune.addEffect(new MobEffectInstance(MFTEEffectRegistries.SUMMON_KITSUNE_TIMER, summonTime, 0, false, false, false));

            var yrot = 6.281f / getKitsuneCount(spellLevel, entity) * i + entity.getYRot() * Mth.DEG_TO_RAD;
            Vec3 spawn = Utils.moveToRelativeGroundLevel(world, entity.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 0, radius * Mth.sin(yrot))), 10);
            kitsune.setPos(spawn.x, spawn.y, spawn.z);
            kitsune.setYRot(entity.getYRot());
            kitsune.setOldPosAndRot();
            var event = NeoForge.EVENT_BUS.post(new SpellSummonEvent<>(entity, kitsune, this.spellId, spellLevel));
            world.addFreshEntity(event.getCreature());
        }

        int effectAmplifier = spellLevel - 1;
        if (entity.hasEffect(MFTEEffectRegistries.SUMMON_KITSUNE_TIMER))
            effectAmplifier += entity.getEffect(MFTEEffectRegistries.SUMMON_KITSUNE_TIMER).getAmplifier() + 1;
        entity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SUMMON_KITSUNE_TIMER, summonTime, effectAmplifier, false, false, true));

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getKitsuneDamage(int spellLevel, LivingEntity summoner) {
        return 0.5f + ((float) spellLevel / 2);
    }

    private float getKitsuneHealth(int spellLevel, LivingEntity summoner) {
        return 10 + (spellPowerPerLevel * spellLevel);
    }

    private int getKitsuneCount(int spellLevel, LivingEntity summoner) {
        return 1 + (spellLevel / 2);
    }
}
