package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.force_sword.SummonedSword;
import net.warphan.iss_magicfromtheeast.events.MFTESpellSummonEvent;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;

@AutoSpellConfig
public class ForceSwordSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "force_sword");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(30)
            .build();

    public ForceSwordSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 2;
        this.castTime = 20;
        this.baseManaCost = 50;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", getSwordHealth(spellLevel, null)),
                Component.translatable("ui.irons_spellbooks.damage", getSwordDamage(spellLevel, null))
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
        int summonTime = 20 * 60 *10;

        SummonedSword sword = new SummonedSword(world, entity);
        sword.setPos(entity.position());

        sword.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getSwordDamage(spellLevel, entity));
        sword.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getSwordHealth(spellLevel, entity));
        sword.setHealth(sword.getMaxHealth());

        sword.removeEffectNoUpdate(MFTEEffectRegistries.SUMMON_SWORD_TIMER);
        sword.forceAddEffect(new MobEffectInstance(MFTEEffectRegistries.SUMMON_SWORD_TIMER, summonTime, 0, false, false, false), null);
        setAttributes(sword, getSpellPower(spellLevel, entity));
        var event = NeoForge.EVENT_BUS.post(new MFTESpellSummonEvent<SummonedSword>(entity, sword, this.spellId, spellLevel));
        world.addFreshEntity(event.getCreature());
        entity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SUMMON_SWORD_TIMER, summonTime, 0, false, false, true));

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getSwordHealth(int spellLevel, LivingEntity caster) {
        return 15 + spellLevel * 5;
    }

    private float getSwordDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

    private void setAttributes(PathfinderMob sword, float power) {
        int maxPower = baseSpellPower + (ServerConfigs.getSpellConfig(this).maxLevel() - 1) * spellPowerPerLevel;
        float quality = power / (float) maxPower;

        float minSpeed = .7f;
        float maxSpeed = .21f;

        float minJump = .4f;
        float maxJump = 1.2f;

        sword.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.lerp(quality, minSpeed, maxSpeed));
        sword.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(Mth.lerp(quality, minJump, maxJump));
    }
}
