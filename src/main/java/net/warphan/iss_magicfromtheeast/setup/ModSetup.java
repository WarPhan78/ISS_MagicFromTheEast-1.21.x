package net.warphan.iss_magicfromtheeast.setup;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.warphan.iss_magicfromtheeast.compat.CompatHandler;

public class ModSetup {
    public static void setup() {}

    public static void init(FMLCommonSetupEvent event) {
        CompatHandler.init();
    }
}
