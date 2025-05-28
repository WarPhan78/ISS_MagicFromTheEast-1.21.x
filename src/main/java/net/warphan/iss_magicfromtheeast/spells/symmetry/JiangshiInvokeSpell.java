package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi.SummonedJiangshi;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.spells.MFTESpellAnimations;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class JiangshiInvokeSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jiangshi_invoke");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", getJiangshiHealth(spellLevel, null)),
                Component.translatableEscape("ui.irons_spellbooks.damage", getJiangshiDamage(spellLevel, null))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(7)
            .setCooldownSeconds(150)
            .build();

    public JiangshiInvokeSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 30;
        this.baseManaCost = 30;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.RAISE_DEAD_START.value());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.RAISE_DEAD_FINISH.value());
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int summonTime = 20 * 60 * 10;
        float radius = 1.5f + .185f * 2;
        for (int i = 0; i < 2; i++) {
            SummonedJiangshi jiangshi = new SummonedJiangshi(world, entity, true);

            jiangshi.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getJiangshiDamage(spellLevel, entity));
            jiangshi.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getJiangshiHealth(spellLevel, entity));
            jiangshi.setHealth(jiangshi.getMaxHealth());

            jiangshi.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(jiangshi.getOnPos()), MobSpawnType.MOB_SUMMONED, null);
            jiangshi.addEffect(new MobEffectInstance(MFTEEffectRegistries.SUMMON_JIANGSHI_TIMER, summonTime, 0, false, false, false));

            var yrot = 6.281f / 2 * i + entity.getYRot() * Mth.DEG_TO_RAD;
            Vec3 spawn = Utils.moveToRelativeGroundLevel(world, entity.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 0, radius * Mth.sin(yrot))), 10);
            jiangshi.setPos(spawn.x, spawn.y, spawn.z);
            jiangshi.setYRot(entity.getYRot());
            jiangshi.setOldPosAndRot();
            var event = NeoForge.EVENT_BUS.post(new SpellSummonEvent<>(entity, jiangshi, this.spellId, spellLevel));
            world.addFreshEntity(event.getCreature());
        }

        int effectAmplifier = spellLevel - 1;
        if (entity.hasEffect(MFTEEffectRegistries.SUMMON_JIANGSHI_TIMER))
            effectAmplifier += entity.getEffect(MFTEEffectRegistries.SUMMON_JIANGSHI_TIMER).getAmplifier() + 1;
        entity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SUMMON_JIANGSHI_TIMER, summonTime, effectAmplifier, false, false, true));

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getJiangshiDamage(int spellLevel, LivingEntity summoner) {
        return 2 + spellLevel * 2;
    }

    private float getJiangshiHealth(int spellLevel, LivingEntity summoner) {
        return 20 + spellLevel * 10;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return MFTESpellAnimations.ANIMATION_INVOKING;
    }
}
