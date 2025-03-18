package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.weapons.*;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.compat.MFTECurios;
import net.warphan.iss_magicfromtheeast.item.armor.TaoistArmorItem;
import net.warphan.iss_magicfromtheeast.item.curios.MFTECuriosBaseItem;
import net.warphan.iss_magicfromtheeast.item.weapons.MFTEExtendedWeaponTier;
import net.warphan.iss_magicfromtheeast.item.weapons.MFTEStaffTier;

import java.util.function.Supplier;

public class ItemRegistries {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ISS_MagicFromTheEast.MOD_ID);

    //Stuff
    public static final DeferredHolder<Item, Item> JADE = ITEMS.register("jade",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item, Item> BOTTLE_OF_SOULS = ITEMS.register("bottle_of_souls",
            () -> new Item(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> ARCANE_RELICS = ITEMS.register("arcane_relics",
            () -> new Item(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> RED_STRING = ITEMS.register("red_string",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item, Item> COPPER_COINS = ITEMS.register("copper_coins",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item, Item> YIN_YANG_CORE = ITEMS.register("yin_yang_core",
            () -> new Item(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON)));

    public static final DeferredHolder<Item, Item> RAW_JADE = ITEMS.register("raw_jade",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item,Item> REFINED_JADE_INGOT = ITEMS.register("refined_jade_ingot",
            () -> new Item(ItemPropertiesHelper.material().rarity(Rarity.RARE)));

    //Weapons
    public static final DeferredHolder<Item, Item> JADE_GUANDAO = ITEMS.register("jade_guandao",
            () -> new MagicSwordItem(MFTEExtendedWeaponTier.JADE_GUANDAO, ItemPropertiesHelper.equipment().rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(MFTEExtendedWeaponTier.JADE_GUANDAO)), SpellDataRegistryHolder.of(new SpellDataRegistryHolder(MFTESpellRegistries.SWORD_DANCE_SPELL, 8))));

    // Curios
    public static final Supplier<MFTECuriosBaseItem> BAGUA_MIRROR = ITEMS.register("bagua_mirror", () -> new MFTECuriosBaseItem(ItemPropertiesHelper.equipment(1)).withAttributes(MFTECurios.BELT_SLOT,
            new AttributeContainer(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.FIRE_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, 0.03, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
    public static final Supplier<MFTECuriosBaseItem> COINS_SWORD = ITEMS.register("coins_sword", () -> new MFTECuriosBaseItem(ItemPropertiesHelper.equipment(1)).withAttributes(MFTECurios.BELT_SLOT,
            new AttributeContainer(AttributeRegistry.BLOOD_MAGIC_RESIST, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ENDER_MAGIC_RESIST, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ELDRITCH_MAGIC_RESIST, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));

    //Rune and Orbs
    public static final DeferredHolder<Item, Item> SYMMETRY_RUNE = ITEMS.register("symmetry_rune",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item, Item> SPIRIT_RUNE = ITEMS.register("spirit_rune",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item, Item> DUNE_RUNE = ITEMS.register("dune_rune",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item, Item> SYMMETRY_UPGRADE_ORB = ITEMS.register("symmetry_upgrade_orb",
            () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON).component(ComponentRegistry.UPGRADE_ORB_TYPE, MFTEUpgradeOrbTypeRegistries.SYMMETRY_SPELL_POWER)));

    //Taoist Set
    public static final DeferredHolder<Item, Item> TAOIST_HAT = ITEMS.register("taoist_hat",
            () -> new TaoistArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredHolder<Item, Item> TAOIST_ROBES = ITEMS.register("taoist_robes",
            () -> new TaoistArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredHolder<Item, Item> TAOIST_LEGGINGS = ITEMS.register("taoist_leggings",
            () -> new TaoistArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredHolder<Item, Item> TAOIST_BOOTS = ITEMS.register("taoist_boots",
            () -> new TaoistArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.BOOTS.getDurability(37))));

    //Staff
    public static final DeferredHolder<Item, Item> TAIJI_SWORD = ITEMS.register("taiji_sword",
            () -> new StaffItem(ItemPropertiesHelper.equipment(1).attributes(ExtendedSwordItem.createAttributes(MFTEStaffTier.TAIJI_SWORD))));

    //Block Items
    public static final DeferredHolder<Item, Item> JADE_ORE_ITEM = ITEMS.register("jade_ore",
            () -> new BlockItem(BlockRegistries.JADE_ORE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> JADE_ORE_DEEPSLATE_ITEM = ITEMS.register("deepslate_jade_ore",
            () -> new BlockItem(BlockRegistries.JADE_ORE_DEEPSLATE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> RAW_JADE_BLOCK_ITEM = ITEMS.register("raw_jade_block",
            () -> new BlockItem(BlockRegistries.RAW_JADE_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> JADE_BLOCK_ITEM = ITEMS.register("jade_block",
            () -> new BlockItem(BlockRegistries.JADE_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> REFINED_JADE_BLOCK_ITEM = ITEMS.register("refined_jade_block",
            () -> new BlockItem(BlockRegistries.REFINED_JADE_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}


