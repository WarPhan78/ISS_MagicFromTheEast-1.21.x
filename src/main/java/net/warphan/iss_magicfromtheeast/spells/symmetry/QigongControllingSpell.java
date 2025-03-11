package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.spells.EntityCastData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.qigong_controlling.PullPushField;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class QigongControllingSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "qigong_controlling");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(20)
            .build();

    public QigongControllingSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 5;
    }

    @Override
    public CastType getCastType() {
        return CastType.CONTINUOUS;
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
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(MFTESoundRegistries.FORCE_PULLING.get());
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.isCasting() && playerMagicData.getCastingSpellId().equals(this.getSpellId())
                && playerMagicData.getAdditionalCastData() instanceof EntityCastData entityCastData
                && entityCastData.getCastingEntity() instanceof AbstractConeProjectile cone) {
            cone.setDealDamageActive();
        } else {
            PullPushField pullPushField = new PullPushField(world, entity);
            pullPushField.damage = getDamage(spellLevel, entity);
            pullPushField.pushForce = getPushForce(spellLevel, entity);
            pullPushField.castTime = castTime * (float)entity.getAttributeValue(AttributeRegistry.CAST_TIME_REDUCTION);
            pullPushField.setPos(entity.position().add(0, entity.getEyeHeight() * .7, 0));
            pullPushField.setDealDamageActive();
            pullPushField.tick();
            world.addFreshEntity(pullPushField);

            playerMagicData.setAdditionalCastData(new EntityCastData(pullPushField));
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

    public float getPushForce(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster) / 2;
    }

    @Override
    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        return mob.distanceToSqr(target) > (10 * 10) * 1.2;
    }
}
