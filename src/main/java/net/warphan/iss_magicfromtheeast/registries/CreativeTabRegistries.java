package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

@EventBusSubscriber (modid = ISS_MagicFromTheEast.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CreativeTabRegistries {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus){
        TABS.register(eventBus);
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EQUIPMENT_TAB = TABS.register("magic_from_the_east", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + ISS_MagicFromTheEast.MOD_ID + ".magic_from_the_east"))
            .icon(() -> new ItemStack(MFTEItemRegistries.YIN_YANG_CORE.get()))
            .displayItems((enabledFeatures, entries) -> {
                entries.accept(MFTEItemRegistries.TAIJI_SWORD.get());
                entries.accept(MFTEItemRegistries.RITUAL_ORIHON.get());

                entries.accept(MFTEItemRegistries.JADE_GUANDAO.get());
                entries.accept(MFTEItemRegistries.SOUL_BREAKER.get());
                entries.accept(MFTEItemRegistries.MURAMASA.get());

                entries.accept(MFTEItemRegistries.SOULPIERCER.get());
                entries.accept(MFTEItemRegistries.REPEATING_CROSSBOW.get());

                entries.accept(MFTEItemRegistries.BAGUA_MIRROR.get());
                entries.accept(MFTEItemRegistries.COINS_SWORD.get());
                entries.accept(MFTEItemRegistries.RUSTED_COINS_SWORD.get());
                entries.accept(MFTEItemRegistries.SOULWARD_RING.get());
                entries.accept(MFTEItemRegistries.JADE_PENDANT.get());

                entries.accept(MFTEItemRegistries.RAW_JADE.get());
                entries.accept(MFTEItemRegistries.JADE.get());
                entries.accept(MFTEItemRegistries.BOTTLE_OF_SOULS.get());
                entries.accept(MFTEItemRegistries.ARCANE_RELICS.get());
                entries.accept(MFTEItemRegistries.RED_STRING.get());
                entries.accept(MFTEItemRegistries.COPPER_COINS.get());
                entries.accept(MFTEItemRegistries.YIN_YANG_CORE.get());
                entries.accept(MFTEItemRegistries.RED_SHAFT.get());

                entries.accept(MFTEItemRegistries.REFINED_JADE_INGOT.get());
                entries.accept(MFTEItemRegistries.CRYSTALLIZED_SOUL.get());

                entries.accept(MFTEItemRegistries.TAOIST_HAT.get());
                entries.accept(MFTEItemRegistries.TAOIST_ROBES.get());
                entries.accept(MFTEItemRegistries.TAOIST_LEGGINGS.get());
                entries.accept(MFTEItemRegistries.TAOIST_BOOTS.get());

                entries.accept(MFTEItemRegistries.ONMYOJI_HAT.get());
                entries.accept(MFTEItemRegistries.ONMYOJI_ROBES.get());
                entries.accept(MFTEItemRegistries.ONMYOJI_LEGGINGS.get());
                entries.accept(MFTEItemRegistries.ONMYOJI_GETA.get());

                entries.accept(MFTEItemRegistries.JIANGSHI_HAT.get());

                entries.accept(MFTEItemRegistries.JADE_PAGODA_HELMET.get());
                entries.accept(MFTEItemRegistries.JADE_PAGODA_CHESTPLATE.get());
                entries.accept(MFTEItemRegistries.JADE_PAGODA_LEGGINGS.get());
                entries.accept(MFTEItemRegistries.JADE_PAGODA_BOOTS.get());
                entries.accept(MFTEItemRegistries.JADE_PAGODA_HORSE_ARMOR.get());

                entries.accept(MFTEItemRegistries.SYMMETRY_RUNE.get());
                entries.accept(MFTEItemRegistries.SPIRIT_RUNE.get());
                entries.accept(MFTEItemRegistries.DUNE_RUNE.get());
                entries.accept(MFTEItemRegistries.SYMMETRY_UPGRADE_ORB.get());
                entries.accept(MFTEItemRegistries.SPIRIT_UPGRADE_ORB.get());

                entries.accept(MFTEItemRegistries.JADE_ORE_ITEM.get());
                entries.accept(MFTEItemRegistries.JADE_ORE_DEEPSLATE_ITEM.get());
                entries.accept(MFTEItemRegistries.JADE_BLOCK_ITEM.get());
                entries.accept(MFTEItemRegistries.RAW_JADE_BLOCK_ITEM.get());
                entries.accept(MFTEItemRegistries.REFINED_JADE_BLOCK_ITEM.get());
            })
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .build());
    @SubscribeEvent
    public static void fillCreativeTabs(final BuildCreativeModeTabContentsEvent event) {}
}