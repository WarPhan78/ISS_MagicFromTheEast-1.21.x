package net.warphan.iss_magicfromtheeast.spells.spirit;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands.BoneHandsEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import javax.annotation.Nullable;
import java.util.List;

@AutoSpellConfig
public class BoneHandsSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "bone_hands");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(MFTESchoolRegistries.SPIRIT_RESOURCE)
            .setMaxLevel(7)
            .setCooldownSeconds(60)
            .build();

    public BoneHandsSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 3;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 40;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((getSpellPower(spellLevel, caster)), 1)),
                Component.translatable("ui.irons_spellbooks.duration", Utils.timeFromTicks(getDuration(spellLevel, caster), 1)),
                Component.translatable("ui.iss_magicfromtheeast.hand_attack_range", Utils.stringTruncation(9.0f, 1))
        );
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
        return spellID;
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity caster) {
        return 1 + (spellLevel / 3);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId())) {
            playerMagicData.getPlayerRecasts().addRecast(new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 60, castSource, null), playerMagicData);
        }
        Vec3 location = Utils.getTargetBlock(level, entity, ClipContext.Fluid.ANY, 24).getLocation();

        BoneHandsEntity boneHandsEntity = new BoneHandsEntity(level, entity, true);
        boneHandsEntity.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getHandsDamage(spellLevel, entity));
        boneHandsEntity.setDuration(getDuration(spellLevel, entity));

        boneHandsEntity.setPos(location);
        boneHandsEntity.setOnGround(true);

        level.addFreshEntity(boneHandsEntity);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDuration(int spellLevel, LivingEntity caster) {
        return 240 + 40 * spellLevel;
    }

    private float getHandsDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }
}
