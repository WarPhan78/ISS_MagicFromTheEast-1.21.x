package net.warphan.iss_magicfromtheeast.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class MFTEDamageTypes {
    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, name));
    }

    public static final ResourceKey<DamageType> SYMMETRY_MAGIC = register("symmetry_magic");
    public static final ResourceKey<DamageType> SPIRIT_MAGIC = register("spirit_magic");
    public static final ResourceKey<DamageType> DUNE_MAGIC = register("dune_magic");

    public static final ResourceKey<DamageType> SOUL_DAMAGE = register("soul_damage");
    public static final ResourceKey<DamageType> SPIRIT_ARROW = register("spirit_arrow");

    public static final ResourceKey<DamageType> PENDANT_REFLECT = register("pendant_reflect");

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(SYMMETRY_MAGIC, new DamageType(SYMMETRY_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,0f));
        context.register(SPIRIT_MAGIC, new DamageType(SPIRIT_MAGIC.location().getPath(), DamageScaling. WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f));
        context.register(DUNE_MAGIC, new DamageType(DUNE_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f));

        context.register(SOUL_DAMAGE, new DamageType(SOUL_DAMAGE.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f));
        context.register(SPIRIT_ARROW, new DamageType(SPIRIT_ARROW.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f));
        context.register(PENDANT_REFLECT, new DamageType(PENDANT_REFLECT.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f));
    }
}
