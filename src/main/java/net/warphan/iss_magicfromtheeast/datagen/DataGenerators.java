package net.warphan.iss_magicfromtheeast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new MFTEDataPackProvider(packOutput, provider));
        // Forge 1.20.1 doesn't properly support tagging datagen'd custom registry entries; append our
        // datapack registry patch to the lookup, mirroring ISS 1.20.1 (RegistryDataGenerator#addProviders).
        generator.addProvider(event.includeServer(), new MFTEDamageTypeTagGenerator(packOutput,
                provider.thenApply(r -> append(r, MFTEDataPackProvider.BUILDER)), existingFileHelper));
        generator.addProvider(event.includeServer(), new MFTERecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(MFTELootTableProvider.Block::new, LootContextParamSets.BLOCK))
        ));
        generator.addProvider(event.includeServer(), new MFTEBlockStatesProvider(packOutput, existingFileHelper));
    }

    private static HolderLookup.Provider append(HolderLookup.Provider original, RegistrySetBuilder builder) {
        return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }
}
