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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_ashigaru.SpiritAshigaruEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class AshigaruSquadSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "ashigaru_squad");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(MFTESchoolRegistries.SPIRIT_RESOURCE)
            .setMaxLevel(8)
            .setCooldownSeconds(180)
            .build();

    public AshigaruSquadSpell() {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 3;
        this.castTime = 20;
        this.baseManaCost = 60;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", getAshigaruHealth(spellLevel, null)),
                Component.translatable("ui.irons_spellbooks.damage", getAshigaruDamage(spellLevel, null)),
                Component.translatable("ui.iss_magicfromtheeast.armor", getAshigaruArmor(spellLevel, null)),
                Component.translatable("ui.irons_spellbooks.summon_count", spellLevel)
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
        float radius = 1.5f + .185f * 2;
        var recast = playerMagicData.getPlayerRecasts();

        if (!recast.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();

            for (int i = 0; i < spellLevel; i++) {
                SpiritAshigaruEntity ashigaru = new SpiritAshigaruEntity(world, entity, true);

                if (i % 3 == 0) {
                    ashigaru.setRangeType();
                    ashigaru.getAttributes().getInstance(Attributes.ENTITY_INTERACTION_RANGE).setBaseValue(25.0);
                } else ashigaru.setMeleeType();

                ashigaru.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getAshigaruDamage(spellLevel, entity));
                ashigaru.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getAshigaruHealth(spellLevel, entity));
                ashigaru.getAttributes().getInstance(Attributes.ARMOR).setBaseValue(getAshigaruArmor(spellLevel, entity));
                ashigaru.setHealth(ashigaru.getMaxHealth());

                var yrot = 6.281f / spellLevel * i + entity.getYRot() * Mth.DEG_TO_RAD;
                Vec3 spawn = Utils.moveToRelativeGroundLevel(world, entity.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 0, radius * Mth.sin(yrot))), 10);
                ashigaru.setPos(spawn.x, spawn.y, spawn.z);
                ashigaru.setYRot(entity.getYRot());
                ashigaru.setOldPosAndRot();
                var creature = NeoForge.EVENT_BUS.post(new SpellSummonEvent<>(entity, ashigaru, this.spellId, spellLevel)).getCreature();
                world.addFreshEntity(creature);

                SummonManager.initSummon(entity, creature, summonTime, summonedEntitiesCastData);
            }

            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
            recast.addRecast(recastInstance, playerMagicData);
        }

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getAshigaruDamage(int spellLevel, LivingEntity summoner) {
        return 2 + (float) (spellLevel / 2);
    }

    private float getAshigaruHealth(int spellLevel, LivingEntity summoner) {
        return 20 + spellLevel * spellPowerPerLevel;
    }

    private float getAshigaruArmor(int spellLevel, LivingEntity summoner) {
        return 2 + (float) (spellLevel / 2);
    }
}
