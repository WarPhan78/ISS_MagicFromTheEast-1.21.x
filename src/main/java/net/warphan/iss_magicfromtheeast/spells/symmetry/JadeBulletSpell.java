package net.warphan.iss_magicfromtheeast.spells.symmetry;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_bullet.JadeBulletProjectile;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.List;

public class JadeBulletSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_bullet");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", getDamage(spellLevel, caster)));
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(MFTESchoolRegistries.SYMMETRY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(5)
            .build();

    public JadeBulletSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 2;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 10;
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

        JadeBulletProjectile jadeBullet = new JadeBulletProjectile(level, entity);
        jadeBullet.setDamage(getSpellPower(spellLevel, entity));
        jadeBullet.setPos(entity.position().add(0, entity.getEyeHeight() - jadeBullet.getBoundingBox().getYsize() * .5f,0));
        jadeBullet.shoot(entity.getLookAngle());
        level.addFreshEntity(jadeBullet);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

}
