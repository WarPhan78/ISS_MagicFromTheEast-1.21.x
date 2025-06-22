package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.summoned_cloud.SummonCloudEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;

@AutoSpellConfig
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int summonTime = getDuration(spellLevel, entity);
        Vec3 spawn = entity.position();
        Vec3 forward = entity.getForward().normalize().scale(1.5f);
        spawn.add(forward.x, 0.25f, forward.z);

        SummonCloudEntity cloudEntity = new SummonCloudEntity(level, entity);

        cloudEntity.setPos(spawn);
        cloudEntity.removeEffectNoUpdate(MFTEEffectRegistries.SUMMON_CLOUD_TIMER);
        cloudEntity.forceAddEffect(new MobEffectInstance(MFTEEffectRegistries.SUMMON_CLOUD_TIMER, summonTime, 0, false, false, false), null);
        var event = NeoForge.EVENT_BUS.post(new SpellSummonEvent<SummonCloudEntity>(entity, cloudEntity, this.spellId, spellLevel));
        level.addFreshEntity(event.getCreature());
        entity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SUMMON_CLOUD_TIMER, summonTime, 0, false, false, true));

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDuration(int spellLevel, LivingEntity caster) {
        return (int) (getSpellPower(spellLevel, caster) * 20);
    }
}
