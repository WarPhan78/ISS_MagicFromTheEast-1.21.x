package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonedEntitiesCastData;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner.JadeExecutionerEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.spells.MFTESpellAnimations;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class PunishingHeavenSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "punishing_heaven");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", getExecutionerHealth(spellLevel, null)),
                Component.translatableEscape("ui.irons_spellbooks.damage", getExecutionerDamage(spellLevel, null)),
                Component.translatable("ui.iss_magicfromtheeast.armor", getExecutionerArmor(spellLevel, null))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(4)
            .setCooldownSeconds(320)
            .build();

    public PunishingHeavenSpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 5;
        this.castTime = 30;
        this.baseManaCost = 250;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.RAISE_DEAD_START.value());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.BEACON_ACTIVATE);
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable)) {
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        }
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

        int summonTime = 20 * 60 * 5;
        var recast = playerMagicData.getPlayerRecasts();

        if (!recast.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();

            Vec3 location = Utils.getTargetBlock(world, entity, ClipContext.Fluid.ANY, 12).getLocation();

            JadeExecutionerEntity jadeExecutioner = new JadeExecutionerEntity(world, entity, true);

            jadeExecutioner.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getExecutionerDamage(spellLevel, entity));
            jadeExecutioner.getAttributes().getInstance(Attributes.ARMOR).setBaseValue(getExecutionerArmor(spellLevel, entity));
            jadeExecutioner.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getExecutionerHealth(spellLevel, entity));
            jadeExecutioner.setHealth(jadeExecutioner.getMaxHealth());

            jadeExecutioner.setPos(location);
            jadeExecutioner.setOnGround(true);
            var creature = NeoForge.EVENT_BUS.post(new SpellSummonEvent<JadeExecutionerEntity>(entity, jadeExecutioner, this.spellID, spellLevel)).getCreature();
            world.addFreshEntity(creature);

            SummonManager.initSummon(entity, creature, summonTime, summonedEntitiesCastData);

            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
            recast.addRecast(recastInstance, playerMagicData);
        }

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getExecutionerDamage(int spellLevel, LivingEntity summoner) {
        return 8 + ((spellPowerPerLevel - 1) * spellLevel);
    }

    private float getExecutionerHealth(int spellLevel, LivingEntity summoner) {
        return 250 + spellPowerPerLevel * spellLevel * 10;
    }

    private float getExecutionerArmor(int spellLevel, LivingEntity summoner) {
        return (float) 10.0 + (spellLevel * 2);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return MFTESpellAnimations.ANIMATION_INVOKING;
    }
}
