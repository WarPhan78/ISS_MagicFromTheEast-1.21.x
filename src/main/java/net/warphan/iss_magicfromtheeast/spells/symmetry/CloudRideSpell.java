package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonedEntitiesCastData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.summoned_cloud.SummonCloudEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class CloudRideSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "cloud_ride");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.duration", Utils.timeFromTicks(getDuration(spellLevel, caster), 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(9)
            .setCooldownSeconds(90)
            .build();

    public CloudRideSpell() {
        this.manaCostPerLevel = 6;
        this.baseSpellPower = 15;
        this.spellPowerPerLevel = 10;
        this.baseManaCost = 20;
        this.castTime = 0;
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        var recast = playerMagicData.getPlayerRecasts();
        int summonTick = getDuration(spellLevel, entity);
        Vec3 spawn = entity.position();
        Vec3 forward = entity.getForward().normalize().scale(1.5f);

        if (!recast.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();

            spawn.add(forward.x, 0.25f, forward.z);
            SummonCloudEntity cloudEntity = new SummonCloudEntity(level, entity);
            cloudEntity.setPos(spawn);
            level.addFreshEntity(cloudEntity);

            SummonManager.initSummon(entity, cloudEntity, summonTick, summonedEntitiesCastData);

            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, entity), summonTick, castSource, summonedEntitiesCastData);
            recast.addRecast(recastInstance, playerMagicData);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDuration(int spellLevel, LivingEntity caster) {
        return (int) (getSpellPower(spellLevel, caster) * 20);
    }
}
