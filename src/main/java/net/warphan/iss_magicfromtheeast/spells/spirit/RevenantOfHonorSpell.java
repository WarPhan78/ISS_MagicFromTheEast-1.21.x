package net.warphan.iss_magicfromtheeast.spells.spirit;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonedEntitiesCastData;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_samurai.SpiritSamuraiEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class RevenantOfHonorSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "revenant_of_honor");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", getSamuraiHealth(spellLevel, null)),
                Component.translatableEscape("ui.irons_spellbooks.damage", getSamuraiDamage(spellLevel, null)),
                Component.translatable("ui.iss_magicfromtheeast.armor", getSamuraiArmor(spellLevel, null))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(MFTESchoolRegistries.SPIRIT_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(240)
            .build();

    public RevenantOfHonorSpell() {
        this.manaCostPerLevel = 35;
        this.baseSpellPower = 3;
        this.spellPowerPerLevel = 5;
        this.castTime = 20;
        this.baseManaCost = 80;
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
        return CastType.LONG;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.RAISE_DEAD_START.value());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(MFTESoundRegistries.SPIRIT_INVOKING.value());
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

        int summonTime = 20 * 60 * 10;
        var recast = playerMagicData.getPlayerRecasts();

        if (!recast.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();

            Vec3 location = Utils.getTargetBlock(world, entity, ClipContext.Fluid.ANY, 8).getLocation();

            SpiritSamuraiEntity spiritSamurai = new SpiritSamuraiEntity(world, entity, true);

            spiritSamurai.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getSamuraiDamage(spellLevel, entity));
            spiritSamurai.getAttributes().getInstance(Attributes.ARMOR).setBaseValue(getSamuraiArmor(spellLevel, entity));
            spiritSamurai.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getSamuraiHealth(spellLevel, entity));
            spiritSamurai.setHealth(spiritSamurai.getMaxHealth());

            spiritSamurai.setPos(location);
            spiritSamurai.setOnGround(true);
            var creature = NeoForge.EVENT_BUS.post(new SpellSummonEvent<SpiritSamuraiEntity>(entity, spiritSamurai, this.spellID, spellLevel)).getCreature();
            world.addFreshEntity(creature);

            spiritSamurai.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(spiritSamurai.getOnPos()), MobSpawnType.MOB_SUMMONED, null);

            SummonManager.initSummon(entity, creature, summonTime, summonedEntitiesCastData);

            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
            recast.addRecast(recastInstance, playerMagicData);
        }

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getSamuraiDamage(int spellLevel, LivingEntity summoner) {
        return 6 + ((spellPowerPerLevel - 2) * spellLevel);
    }

    private float getSamuraiHealth(int spellLevel, LivingEntity summoner) {
        return 50 + spellPowerPerLevel * spellLevel * 2.5f;
    }

    private float getSamuraiArmor(int spellLevel, LivingEntity summoner) {
        return 6.0f + spellLevel * 2;
    }
}
