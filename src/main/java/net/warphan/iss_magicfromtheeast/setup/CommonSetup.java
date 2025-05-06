package net.warphan.iss_magicfromtheeast.setup;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi.SummonedJiangshi;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ChallengedSoul;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
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

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
//        event.put(MFTEEntityRegistries.JADE_SENTINEL.get(), JadeSentinel.prepareAttributes().build());
        event.put(MFTEEntityRegistries.SUMMONED_JIANGSHI.get(), SummonedJiangshi.prepareAttributes().build());
        event.put(MFTEEntityRegistries.CHALLENGING_SOUL.get(), ChallengedSoul.prepareAttributes().build());
    }
}