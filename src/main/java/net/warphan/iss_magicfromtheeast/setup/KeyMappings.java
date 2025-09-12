//package net.warphan.iss_magicfromtheeast.setup;
//
//import com.mojang.blaze3d.platform.InputConstants;
//import net.minecraft.client.KeyMapping;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
//import net.neoforged.neoforge.client.settings.KeyConflictContext;
//import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
//
//@EventBusSubscriber(value = Dist.CLIENT, modid = ISS_MagicFromTheEast.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
//public class KeyMappings {
//    public static final String KEY_BIND_GENERAL_CATEGORY = "key.iss_magicfromtheeast.general";
//
//    private static String getResourceName(String name) {
//        return String.format("key.iss_magicfromtheeast.%s", name);
//    }
//
//    @SubscribeEvent
//    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
//    }
//}
