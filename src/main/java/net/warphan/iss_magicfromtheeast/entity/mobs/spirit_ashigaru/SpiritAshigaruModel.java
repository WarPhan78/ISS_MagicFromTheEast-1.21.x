package net.warphan.iss_magicfromtheeast.entity.mobs.spirit_ashigaru;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class SpiritAshigaruModel extends AbstractSpellCastingMobModel {
    public static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/entity/ashigaru.png");
    public static final ResourceLocation modelMeleeResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "geo/ashigaru_melee.geo.json");
    public static final ResourceLocation modelRangeResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "geo/ashigaru_range.geo.json");
    public static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "animations/ashigaru.animation.json");

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return object instanceof SpiritAshigaruEntity ashigaru && ashigaru.isRangeType() ? modelRangeResource : modelMeleeResource;
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractSpellCastingMob animatable) {
        return animationResource;
    }
}
