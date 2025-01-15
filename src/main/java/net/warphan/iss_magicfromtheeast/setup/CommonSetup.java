package net.warphan.iss_magicfromtheeast.setup;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;

@EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CommonSetup {
    @SubscribeEvent
    public static void onModConfigLoadingEvent(ModConfigEvent.Loading event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            MFTESpellRegistries.onConfigReload();
        }
    }

    @SubscribeEvent
    public static void onModConfigReloadEvent(ModConfigEvent.Reloading event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            MFTESpellRegistries.onConfigReload();
        }
    }
}