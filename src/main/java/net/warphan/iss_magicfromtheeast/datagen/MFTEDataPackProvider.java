package net.warphan.iss_magicfromtheeast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.worldgen.MFTEBiomeModifiers;
import net.warphan.iss_magicfromtheeast.worldgen.MFTEConfiguredFeatures;
import net.warphan.iss_magicfromtheeast.worldgen.MFTEPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MFTEDataPackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, MFTEConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, MFTEPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, MFTEBiomeModifiers::bootstrap);

    public MFTEDataPackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ISS_MagicFromTheEast.MOD_ID));
    }
}
