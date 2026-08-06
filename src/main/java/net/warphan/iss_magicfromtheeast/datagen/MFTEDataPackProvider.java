package net.warphan.iss_magicfromtheeast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import net.warphan.iss_magicfromtheeast.registries.MFTEFeaturesRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEUpgradeOrbTypeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MFTEDataPackProvider extends DatapackBuiltinEntriesProvider {
    // TODO PORT 1.20.1: Registries.ENCHANTMENT bootstrap removed - enchantments are classic
    // Enchantment classes on 1.20.1 (see MFTEEnchantments), not a datapack registry.
    // TODO PORT 1.20.1: Registries.BANNER_PATTERN bootstrap removed - banner patterns are a
    // built-in registry on 1.20.1, registered in code (see MFTEBannerPatterns).
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, MFTEFeaturesRegistries::boostrapConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, MFTEFeaturesRegistries::boostrapPlacedFeature)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, MFTEFeaturesRegistries::boostrapBiomesModifier)
            .add(Registries.DAMAGE_TYPE, MFTEDamageTypes::bootstrap)
            // PORT 1.20.1: ISS 3.16.2-1.20.1 backports the upgrade_orb_type datapack registry;
            // datagen the addon's symmetry/spirit orb types (mirrors ISS RegistryDataGenerator).
            .add(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, MFTEUpgradeOrbTypeRegistries::bootstrap);

    public MFTEDataPackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", ISS_MagicFromTheEast.MOD_ID));
    }
}
