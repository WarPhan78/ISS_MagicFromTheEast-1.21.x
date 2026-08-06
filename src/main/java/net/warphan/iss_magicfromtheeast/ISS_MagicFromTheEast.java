package net.warphan.iss_magicfromtheeast;

import net.minecraft.resources.ResourceLocation;
import net.warphan.iss_magicfromtheeast.configs.MFTEServerConfigs;
import net.warphan.iss_magicfromtheeast.datagen.MFTEBannerPatterns;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantments;
import net.warphan.iss_magicfromtheeast.registries.*;
import net.warphan.iss_magicfromtheeast.setup.ModSetup;
import net.warphan.iss_magicfromtheeast.util.MFTEItemProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ISS_MagicFromTheEast.MOD_ID)
public class ISS_MagicFromTheEast {
    public static final String MOD_ID = "iss_magicfromtheeast";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ISS_MagicFromTheEast() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        ModSetup.setup();

        modEventBus.addListener(ModSetup::init);

        MFTEItemRegistries.register(modEventBus);
        MFTEBlockRegistries.register(modEventBus);
        MFTECreativeTabRegistries.register(modEventBus);
        MFTESchoolRegistries.register(modEventBus);
        MFTEAttributeRegistries.register(modEventBus);
        MFTESoundRegistries.register(modEventBus);
        MFTESpellRegistries.register(modEventBus);
        MFTEEntityRegistries.register(modEventBus);
        MFTEEffectRegistries.register(modEventBus);
        MFTEFluidRegistries.register(modEventBus);
        // TODO PORT 1.20.1: MFTEArmorMaterialRegistries is now a plain ArmorMaterial enum (armor materials are not a registry on 1.20.1) - nothing to register.
        // TODO PORT 1.20.1: MFTEEnchantmentEffectRegistries emptied (enchantment effect components do not exist on 1.20.1);
        //  enchantments are classic Enchantment classes registered through MFTEEnchantments below.
        MFTEEnchantments.register(modEventBus);
        // TODO PORT 1.20.1: MFTEDataComponentRegistries is now an NBT helper class (data components do not exist on 1.20.1) - nothing to register.
        MFTELootRegistries.register(modEventBus);
        MFTEParticleRegistries.register(modEventBus);
        MFTEFeaturesRegistries.register(modEventBus);
        MFTEBannerPatterns.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MFTEServerConfigs.SPEC, String.format("%s-server.toml", ISS_MagicFromTheEast.MOD_ID));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(MFTEItemProperties::addCustomItemProperties);
        }
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, path);
    }
}
