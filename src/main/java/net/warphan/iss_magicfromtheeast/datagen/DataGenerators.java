package net.warphan.iss_magicfromtheeast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput =  generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider =  event.getLookupProvider();

        generator.addProvider(event.includeServer(), new MFTEDataPackProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new MFTERecipeProvider(packOutput, lookupProvider));
    }
}
