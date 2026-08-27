package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

@Mod.EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MFTECreativeTabRegistries {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus){
        TABS.register(eventBus);
    }

    public static final RegistryObject<CreativeModeTab> MFTE_ITEM_TAB = TABS.register("magicfromtheeast_items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + ISS_MagicFromTheEast.MOD_ID + ".item_tab"))
            .icon(() -> new ItemStack(MFTEItemRegistries.YIN_YANG_CORE.get()))
            .displayItems((enabledFeatures, entries) -> {
                entries.accept(MFTEItemRegistries.TAIJI_SWORD.get());
                entries.accept(MFTEItemRegistries.RITUAL_ORIHON.get());

                entries.accept(MFTEItemRegistries.JADE_GUANDAO.get());
                entries.accept(MFTEItemRegistries.SOUL_BREAKER.get());

                entries.accept(MFTEItemRegistries.SPIRIT_CRUSHER.get());
                entries.accept(MFTEItemRegistries.MURAMASA.get());
                entries.accept(MFTEItemRegistries.SOUL_KATANA.get());

                entries.accept(MFTEItemRegistries.SOULPIERCER.get());
                entries.accept(MFTEItemRegistries.REPEATING_CROSSBOW.get());

                entries.accept(MFTEItemRegistries.BAGUA_MIRROR.get());
                entries.accept(MFTEItemRegistries.COINS_SWORD.get());
                entries.accept(MFTEItemRegistries.RUSTED_COINS_SWORD.get());
                entries.accept(MFTEItemRegistries.SOULWARD_RING.get());
                entries.accept(MFTEItemRegistries.JADE_PENDANT.get());

                entries.accept(MFTEItemRegistries.JADE.get());
                entries.accept(MFTEItemRegistries.YIN_YANG_CORE.get());
                entries.accept(MFTEItemRegistries.BOTTLE_OF_SOULS.get());
                entries.accept(MFTEItemRegistries.ARCANE_RELICS.get());
                entries.accept(MFTEItemRegistries.RED_STRING.get());
                entries.accept(MFTEItemRegistries.COPPER_COINS.get());

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

                entries.accept(MFTEItemRegistries.ELEMENTAL_COMMANDER_CHESTPLATE.get());
                entries.accept(MFTEItemRegistries.BOOTS_OF_MIST.get());

                entries.accept(MFTEItemRegistries.SYMMETRY_RUNE.get());
                entries.accept(MFTEItemRegistries.SPIRIT_RUNE.get());
                entries.accept(MFTEItemRegistries.DUNE_RUNE.get());
                entries.accept(MFTEItemRegistries.SYMMETRY_UPGRADE_ORB.get());
                entries.accept(MFTEItemRegistries.SPIRIT_UPGRADE_ORB.get());

                entries.accept(MFTEItemRegistries.RICE_WINE_BOTTLE.get());

                entries.accept(MFTEItemRegistries.TAOIST_SPAWN_EGG.get());
                entries.accept(MFTEItemRegistries.ONMYOJI_SPAWN_EGG.get());
                entries.accept(MFTEItemRegistries.JIANGSHI_SPAWN_EGG.get());

                entries.accept(MFTEItemRegistries.BALANCE_PATTERN.get());
            })
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .build());

    public static final RegistryObject<CreativeModeTab> MFTE_BLOCK_TAB = TABS.register("magicfromtheeast_blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + ISS_MagicFromTheEast.MOD_ID + ".block_tab"))
            .icon(() -> new ItemStack(MFTEItemRegistries.JADE_BLOCK_ITEM.get()))
            .displayItems((enabledFeature, entries) -> {
                entries.accept(MFTEItemRegistries.JADE_ORE_ITEM.get());
                entries.accept(MFTEItemRegistries.JADE_ORE_DEEPSLATE_ITEM.get());

                entries.accept(MFTEItemRegistries.JADE_BLOCK_ITEM.get());
                entries.accept(MFTEItemRegistries.JADE_SLAB_ITEM.get());
                entries.accept(MFTEItemRegistries.JADE_STAIR_ITEM.get());
                entries.accept(MFTEItemRegistries.JADE_WALL_ITEM.get());
                entries.accept(MFTEItemRegistries.CHISELED_JADE.get());
                entries.accept(MFTEItemRegistries.JADE_LION_HEAD_BLOCK_ITEMS.get());

                entries.accept(MFTEItemRegistries.JADESTONE_BRICKS_ITEM.get());
                entries.accept(MFTEItemRegistries.JADESTONE_BRICKS_SLAB_ITEM.get());
                entries.accept(MFTEItemRegistries.JADESTONE_BRICKS_STAIR_ITEM.get());
                entries.accept(MFTEItemRegistries.JADESTONE_BRICKS_WALL_ITEM.get());
                entries.accept(MFTEItemRegistries.CHISELED_JADESTONE_BRICKS_ITEMS.get());
                entries.accept(MFTEItemRegistries.JADESTONE_BRICKS_PILLAR.get());

                entries.accept(MFTEItemRegistries.REFINED_JADE_BLOCK_ITEM.get());
                entries.accept(MFTEItemRegistries.VASE_OF_RICE_WINE.get());
            })
            .withTabsBefore(MFTECreativeTabRegistries.MFTE_ITEM_TAB.getKey())
            .build());

    @SubscribeEvent
    public static void fillCreativeTabs(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(MFTEItemRegistries.JADE_ORE_ITEM.get());
            event.accept(MFTEItemRegistries.JADE_ORE_DEEPSLATE_ITEM.get());
        }
    }
}
