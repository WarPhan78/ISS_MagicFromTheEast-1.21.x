package net.warphan.iss_magicfromtheeast.datagen;

import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MFTEDamageTypeTagGenerator extends TagsProvider<DamageType> {
    public MFTEDamageTypeTagGenerator(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, ISS_MagicFromTheEast.MOD_ID, existingFileHelper);
    }

    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, name));
    }

    public static final TagKey<DamageType> SYMMETRY_MAGIC = create("symmetry_magic");
    public static final TagKey<DamageType> SPIRIT_MAGIC = create("spirit_magic");
    public static final TagKey<DamageType> DUNE_MAGIC = create("dune_magic");

    public static final TagKey<DamageType> SOUL_HURTING = create("is_soul");
    public static final TagKey<DamageType> BYPASS_REVERSAL_HEALING = create("bypass_reversal_healing");

    protected void addTags(@NotNull Provider provider) {
        tag(SYMMETRY_MAGIC).add(MFTEDamageTypes.SYMMETRY_MAGIC);
        tag(SPIRIT_MAGIC).add(MFTEDamageTypes.SPIRIT_MAGIC);
        tag(DUNE_MAGIC).add(MFTEDamageTypes.DUNE_MAGIC);

        tag(SOUL_HURTING).add(
                MFTEDamageTypes.SOUL_DAMAGE,
                MFTEDamageTypes.SPIRIT_ARROW
        );

        tag(BYPASS_REVERSAL_HEALING).add(
                MFTEDamageTypes.SOUL_DAMAGE,
                DamageTypes.ON_FIRE,
                DamageTypes.WITHER,
                DamageTypes.FREEZE,
                DamageTypes.STARVE,
                DamageTypes.DROWN,
                DamageTypes.OUTSIDE_BORDER,
                DamageTypes.FELL_OUT_OF_WORLD,
                DamageTypes.DRY_OUT,
                DamageTypes.IN_WALL
        );
        tag(BYPASS_REVERSAL_HEALING).addOptional(NeoForgeMod.POISON_DAMAGE.location());
        tag(BYPASS_REVERSAL_HEALING).addOptional(ISSDamageTypes.CAULDRON.location());
        tag(BYPASS_REVERSAL_HEALING).addOptional(ISSDamageTypes.HEARTSTOP.location());
    }
}
