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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.kitsune.SummonedKitsune;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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
                Component.translatable("ui.irons_spellbooks.damage", getKitsuneDamage(spellLevel, null)),
                Component.translatable("ui.irons_spellbooks.summon_count", getKitsuneCount(spellLevel, null))
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
        return Optional.of(SoundEvents.EVOKER_PREPARE_SUMMON);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.FOX_SCREECH);
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

            for (int i = 0; i < getKitsuneCount(spellLevel, entity); i++) {
                SummonedKitsune kitsune = new SummonedKitsune(world, entity);

                kitsune.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getKitsuneDamage(spellLevel, entity));
                kitsune.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getKitsuneHealth(spellLevel, entity));
                kitsune.setHealth(kitsune.getMaxHealth());

                var yrot = 6.281f / getKitsuneCount(spellLevel, entity) * i + entity.getYRot() * Mth.DEG_TO_RAD;
                Vec3 spawn = Utils.moveToRelativeGroundLevel(world, entity.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 0, radius * Mth.sin(yrot))), 10);
                kitsune.setPos(spawn.x, spawn.y, spawn.z);
                kitsune.setYRot(entity.getYRot());
                kitsune.setOldPosAndRot();
                var creature = NeoForge.EVENT_BUS.post(new SpellSummonEvent<>(entity, kitsune, this.spellId, spellLevel)).getCreature();
                world.addFreshEntity(creature);

                SummonManager.initSummon(entity, creature, summonTime, summonedEntitiesCastData);
            }

            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
            recast.addRecast(recastInstance, playerMagicData);
        }

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
