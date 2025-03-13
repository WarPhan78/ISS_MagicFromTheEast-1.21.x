package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.throw_circle.ThrowCircleEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class ThrowUpSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "throw_up");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.strength", String.format("%s%%", (int) (getStrength(spellLevel, caster) * 100 / getStrength(1, null))))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(6)
            .setCooldownSeconds(15)
            .build();

    public ThrowUpSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 3;
        this.castTime = 0;
        this.baseManaCost = 20;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, .35f);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 spawn = null;
        ThrowCircleEntity throwCircle = new ThrowCircleEntity(level);
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargeting) {
            spawn = castTargeting.getTargetPosition((ServerLevel) level);
            throwCircle.setTarget(castTargeting.getTarget((ServerLevel) level));
        }
        if (spawn == null) {
            HitResult raycast = Utils.raycastForEntity(level, entity, 48, true);
            spawn = ((EntityHitResult) raycast).getEntity().position();
        }

        float strength = getStrength(spellLevel, entity);

        throwCircle.setOwner(throwCircle);
        throwCircle.moveTo(spawn);
        throwCircle.strength = strength;
        throwCircle.amplifier = spellLevel - 1;
        level.addFreshEntity(throwCircle);
        throwCircle.tick();

        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargeting) {
            var target = castTargeting.getTarget((ServerLevel) level);
            float kickup = (float) target.getBoundingBox().getBottomCenter().distanceToSqr(Utils.getTargetBlock(level, target, ClipContext.Fluid.NONE, 0.5f).getLocation());
            kickup = Mth.clamp(1 / (kickup + 1) - .01f, -.95f, .1f);
            if (kickup > 0) {
                target.setDeltaMovement(target.getDeltaMovement().subtract(target.position().scale(kickup * ((float)(1 + spellLevel) / 2) * .15f)));
                target.resetFallDistance();
                target.hurtMarked = true;
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getStrength(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster) * 0.2f;
    }
}
