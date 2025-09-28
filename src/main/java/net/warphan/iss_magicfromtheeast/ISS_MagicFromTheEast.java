package net.warphan.iss_magicfromtheeast;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.configs.MFTEServerConfigs;
import net.warphan.iss_magicfromtheeast.registries.*;
import net.warphan.iss_magicfromtheeast.setup.ModSetup;
import net.warphan.iss_magicfromtheeast.util.MFTEItemProperties;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ISS_MagicFromTheEast.MOD_ID)
public class ISS_MagicFromTheEast {
    public static final String MOD_ID = "iss_magicfromtheeast";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ISS_MagicFromTheEast(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        ModSetup.setup();

        modEventBus.addListener(ModSetup::init);

        MFTEItemRegistries.register(modEventBus);
        MFTEBlockRegistries.register(modEventBus);
        CreativeTabRegistries.register(modEventBus);
        MFTESchoolRegistries.register(modEventBus);
        MFTEAttributeRegistries.register(modEventBus);
        MFTESoundRegistries.register(modEventBus);
        MFTESpellRegistries.register(modEventBus);
        MFTEEntityRegistries.register(modEventBus);
        MFTEEffectRegistries.register(modEventBus);
        MFTEFluidRegistries.register(modEventBus);
        MFTEArmorMaterialRegistries.register(modEventBus);
        MFTEEnchantmentEffectRegistries.register(modEventBus);
        MFTEDataComponentRegistries.register(modEventBus);
        MFTELootRegistries.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, MFTEServerConfigs.SPEC, String.format("%s-server.toml", ISS_MagicFromTheEast.MOD_ID));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MFTEItemProperties.addCustomItemProperties();
        }
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, path);
    }
}
