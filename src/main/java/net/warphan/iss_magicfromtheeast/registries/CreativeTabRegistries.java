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
            .icon(() -> new ItemStack(ItemRegistries.YIN_YANG_CORE.get()))
            .displayItems((enabledFeatures, entries) -> {
                entries.accept(ItemRegistries.TAIJI_SWORD.get());
                entries.accept(ItemRegistries.JADE_GUANDAO.get());

                entries.accept(ItemRegistries.BAGUA_MIRROR.get());
                entries.accept(ItemRegistries.COINS_SWORD.get());

                entries.accept(ItemRegistries.RAW_JADE.get());
                entries.accept(ItemRegistries.JADE.get());
                entries.accept(ItemRegistries.BOTTLE_OF_SOULS.get());
                entries.accept(ItemRegistries.ARCANE_RELICS.get());
                entries.accept(ItemRegistries.RED_STRING.get());
                entries.accept(ItemRegistries.COPPER_COINS.get());
                entries.accept(ItemRegistries.YIN_YANG_CORE.get());

                entries.accept(ItemRegistries.REFINED_JADE_INGOT.get());

                entries.accept(ItemRegistries.TAOIST_HAT.get());
                entries.accept(ItemRegistries.TAOIST_ROBES.get());
                entries.accept(ItemRegistries.TAOIST_LEGGINGS.get());
                entries.accept(ItemRegistries.TAOIST_BOOTS.get());

                entries.accept(ItemRegistries.SYMMETRY_RUNE.get());
                entries.accept(ItemRegistries.SPIRIT_RUNE.get());
                entries.accept(ItemRegistries.DUNE_RUNE.get());
                entries.accept(ItemRegistries.SYMMETRY_UPGRADE_ORB.get());

                entries.accept(ItemRegistries.JADE_ORE_ITEM.get());
                entries.accept(ItemRegistries.JADE_ORE_DEEPSLATE_ITEM.get());
                entries.accept(ItemRegistries.JADE_BLOCK_ITEM.get());
                entries.accept(ItemRegistries.RAW_JADE_BLOCK_ITEM.get());
                entries.accept(ItemRegistries.REFINED_JADE_BLOCK_ITEM.get());
            })
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .build());
    @SubscribeEvent
    public static void fillCreativeTabs(final BuildCreativeModeTabContentsEvent event) {}
}