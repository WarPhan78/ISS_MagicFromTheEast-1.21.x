package net.warphan.iss_magicfromtheeast;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.registries.*;
import net.warphan.iss_magicfromtheeast.setup.KeyMappings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.function.Consumer;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ISS_MagicFromTheEast.MOD_ID)
public class ISS_MagicFromTheEast {
    public static final String MOD_ID = "iss_magicfromtheeast";
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ISS_MagicFromTheEast(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ItemRegistries.register(modEventBus);
        CreativeTabRegistries.register(modEventBus);
        MFTESchoolRegistries.register(modEventBus);
        MFTEAttributeRegistries.register(modEventBus);
        MFTESoundRegistries.register(modEventBus);
        MFTESpellRegistries.register(modEventBus);
        MFTEEntityRegistries.register(modEventBus);
        MFTEEffectRegistries.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @SuppressWarnings("ConstantConditions")
    static void registerKeyBindings(Consumer<KeyMapping> registrar) {
        KeyMappings.registerKeybinds(registrar);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, path);
    }
}
