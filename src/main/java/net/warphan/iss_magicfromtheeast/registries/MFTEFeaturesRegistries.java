package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

import java.util.List;

public class MFTEFeaturesRegistries {
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, ISS_MagicFromTheEast.MOD_ID);
    private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registries.CONFIGURED_FEATURE, ISS_MagicFromTheEast.MOD_ID);
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registries.PLACED_FEATURE, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
        CONFIGURED_FEATURES.register(eventBus);
        PLACED_FEATURES.register(eventBus);
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> JADESTONE_ORE_FEATURE = configuredFeatureResourceKey("ore_jadestone_feature");

    public static final ResourceKey<PlacedFeature> JADESTONE_ORE_JUNGLE = placedFeatureResourceKey("ore_jadestone_jungle");
    public static final ResourceKey<PlacedFeature> JADESTONE_ORE_MOUNTAIN = placedFeatureResourceKey("ore_jadestone_mountain");

    public static final ResourceKey<BiomeModifier> ADD_JADESTONE_TO_JUNGLE = biomeModifierResourceKey("add_jadestone_jungle");
    public static final ResourceKey<BiomeModifier> ADD_JADESTONE_TO_MOUNTAIN = biomeModifierResourceKey("add_jadestone_mountain");

    public static void boostrapConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneTest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateTest = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> jadestoneList = List.of(
                OreConfiguration.target(stoneTest, MFTEBlockRegistries.JADE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateTest, MFTEBlockRegistries.JADE_ORE_DEEPSLATE.get().defaultBlockState())
        );

        FeatureUtils.register(context, JADESTONE_ORE_FEATURE, Feature.ORE, new OreConfiguration(jadestoneList, 10));
    }

    public static void boostrapPlacedFeature(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holderGetter = context.lookup(CONFIGURED_FEATURES.getRegistryKey());
        Holder<ConfiguredFeature<?, ?>> holderJadestone = holderGetter.getOrThrow(JADESTONE_ORE_FEATURE);

        List<PlacementModifier> listJungle = List.of(CountPlacement.of(12), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-56), VerticalAnchor.absolute(48)), BiomeFilter.biome());
        List<PlacementModifier> listMountain = List.of(CountPlacement.of(30), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(64), VerticalAnchor.absolute(440)), BiomeFilter.biome());

        PlacementUtils.register(context, JADESTONE_ORE_JUNGLE, holderJadestone, listJungle);
        PlacementUtils.register(context, JADESTONE_ORE_MOUNTAIN, holderJadestone, listMountain);
    }

    public static void boostrapBiomesModifier(final BootstapContext<BiomeModifier> context) {
        final var biomes = context.lookup(Registries.BIOME);
        final var features = context.lookup(PLACED_FEATURES.getRegistryKey());

        context.register(ADD_JADESTONE_TO_JUNGLE,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        tag(biomes, BiomeTags.IS_JUNGLE),
                        feature(features, JADESTONE_ORE_JUNGLE),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );
        context.register(ADD_JADESTONE_TO_MOUNTAIN,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        tag(biomes, BiomeTags.IS_MOUNTAIN),
                        feature(features, JADESTONE_ORE_MOUNTAIN),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureResourceKey(final String name) {
        return ResourceKey.create(CONFIGURED_FEATURES.getRegistryKey(), new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, name));
    }

    private static ResourceKey<PlacedFeature> placedFeatureResourceKey(final String name) {
        return ResourceKey.create(PLACED_FEATURES.getRegistryKey(), new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, name));
    }

    private static ResourceKey<BiomeModifier> biomeModifierResourceKey(final String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, name));
    }

    private static HolderSet<Biome> tag(final HolderGetter<Biome> holderGetter, final TagKey<Biome> key) {
        return holderGetter.getOrThrow(key);
    }

    private static HolderSet<PlacedFeature> feature(final HolderGetter<PlacedFeature> holderGetter, final ResourceKey<PlacedFeature> features) {
        return HolderSet.direct(holderGetter.getOrThrow(features));
    }
}
