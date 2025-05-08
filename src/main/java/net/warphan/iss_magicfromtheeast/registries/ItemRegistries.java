package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.*;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.compat.MFTECurios;
import net.warphan.iss_magicfromtheeast.item.RitualOrihonSpellbookItem;
import net.warphan.iss_magicfromtheeast.item.armor.JiangshiHatItem;
import net.warphan.iss_magicfromtheeast.item.armor.OnmyojiArmorItem;
import net.warphan.iss_magicfromtheeast.item.armor.TaoistArmorItem;
import net.warphan.iss_magicfromtheeast.item.curios.RustedCoinsSword;
import net.warphan.iss_magicfromtheeast.item.curios.SoulwardRing;
import net.warphan.iss_magicfromtheeast.item.weapons.MFTEExtendedWeaponTier;
import net.warphan.iss_magicfromtheeast.item.weapons.MFTEStaffTier;
import net.warphan.iss_magicfromtheeast.item.weapons.MuramasaItem;
import net.warphan.iss_magicfromtheeast.item.weapons.SoulBreakerItem;
import net.warphan.iss_magicfromtheeast.setup.BloodfulRarity;

import java.util.function.Supplier;

public class ItemRegistries {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ISS_MagicFromTheEast.MOD_ID);

    //Magic Stuffs
    public static final DeferredHolder<Item, Item> TAIJI_SWORD = ITEMS.register("taiji_sword",
            () -> new StaffItem(ItemPropertiesHelper.equipment(1).attributes(ExtendedSwordItem.createAttributes(MFTEStaffTier.TAIJI_SWORD))));
    public static final DeferredHolder<Item, Item> RITUAL_ORIHON = ITEMS.register("ritual_orihon", RitualOrihonSpellbookItem::new);

    //Stuffs
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
    public static final DeferredHolder<Item, Item> RED_SHAFT = ITEMS.register("red_shaft",
            () -> new Item(ItemPropertiesHelper.material()));

    public static final DeferredHolder<Item, Item> RAW_JADE = ITEMS.register("raw_jade",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item,Item> REFINED_JADE_INGOT = ITEMS.register("refined_jade_ingot",
            () -> new Item(ItemPropertiesHelper.material().rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, Item> CRYSTALLIZED_SOUL = ITEMS.register("crystallized_soul",
            () -> new Item(ItemPropertiesHelper.material().rarity(Rarity.RARE)));

    //Weapons
    public static final DeferredHolder<Item, Item> JADE_GUANDAO = ITEMS.register("jade_guandao",
            () -> new MagicSwordItem(MFTEExtendedWeaponTier.JADE_GUANDAO, ItemPropertiesHelper.equipment().rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(MFTEExtendedWeaponTier.JADE_GUANDAO)), SpellDataRegistryHolder.of(
                    new SpellDataRegistryHolder(MFTESpellRegistries.SWORD_DANCE_SPELL, 8))));
    public static final DeferredHolder<Item, Item> SOUL_BREAKER = ITEMS.register("soul_breaker",
            () -> new SoulBreakerItem(MFTEExtendedWeaponTier.SOUL_BREAKER, ItemPropertiesHelper.equipment().rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(MFTEExtendedWeaponTier.SOUL_BREAKER))));
    public static final DeferredHolder<Item, Item> MURAMASA = ITEMS.register("muramasa",
            () -> new MuramasaItem(MFTEExtendedWeaponTier.MURAMASA, ItemPropertiesHelper.equipment().rarity(BloodfulRarity.BLOODFUL_RARITY_PROXY.getValue()).attributes(ExtendedSwordItem.createAttributes(MFTEExtendedWeaponTier.MURAMASA)), SpellDataRegistryHolder.of(
                    new SpellDataRegistryHolder(SpellRegistry.BLOOD_SLASH_SPELL, 5))));

    // Curios
    public static final Supplier<CurioBaseItem> BAGUA_MIRROR = ITEMS.register("bagua_mirror",
            () -> new CurioBaseItem(ItemPropertiesHelper.equipment(1)).withAttributes(MFTECurios.BELT_SLOT,
            new AttributeContainer(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.FIRE_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, 0.03, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
    public static final Supplier<CurioBaseItem> COINS_SWORD = ITEMS.register("coins_sword",
            () -> new CurioBaseItem(ItemPropertiesHelper.equipment(1)).withAttributes(MFTECurios.BELT_SLOT,
            new AttributeContainer(AttributeRegistry.BLOOD_MAGIC_RESIST, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ENDER_MAGIC_RESIST, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ELDRITCH_MAGIC_RESIST, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
    public static final Supplier<CurioBaseItem> SOULWARD_RING = ITEMS.register("soulward_ring", SoulwardRing::new);
    public static final Supplier<CurioBaseItem> JADE_PENDANT = ITEMS.register("jade_pendant",
            () -> new CurioBaseItem(ItemPropertiesHelper.equipment(1)).withAttributes(MFTECurios.BELT_SLOT,
            new AttributeContainer(AttributeRegistry.SPELL_RESIST, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(Attributes.ARMOR_TOUGHNESS, 2, AttributeModifier.Operation.ADD_VALUE),
            new AttributeContainer(Attributes.KNOCKBACK_RESISTANCE, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
    public static final Supplier<CurioBaseItem> RUSTED_COINS_SWORD = ITEMS.register("rusted_coins_sword",
            () -> new RustedCoinsSword(ItemPropertiesHelper.equipment(1)).withAttributes(MFTECurios.BELT_SLOT,
            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));

    //Rune and Orbs
    public static final DeferredHolder<Item, Item> SYMMETRY_RUNE = ITEMS.register("symmetry_rune",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item, Item> SPIRIT_RUNE = ITEMS.register("spirit_rune",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item, Item> DUNE_RUNE = ITEMS.register("dune_rune",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final DeferredHolder<Item, Item> SYMMETRY_UPGRADE_ORB = ITEMS.register("symmetry_upgrade_orb",
            () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON).component(ComponentRegistry.UPGRADE_ORB_TYPE, MFTEUpgradeOrbTypeRegistries.SYMMETRY_SPELL_POWER)));
    public static final DeferredHolder<Item, Item> SPIRIT_UPGRADE_ORB = ITEMS.register("spirit_upgrade_orb",
            () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON).component(ComponentRegistry.UPGRADE_ORB_TYPE, MFTEUpgradeOrbTypeRegistries.SPIRIT_SPELL_POWER)));

    //Taoist Set
    public static final DeferredHolder<Item, Item> TAOIST_HAT = ITEMS.register("taoist_hat",
            () -> new TaoistArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredHolder<Item, Item> TAOIST_ROBES = ITEMS.register("taoist_robes",
            () -> new TaoistArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredHolder<Item, Item> TAOIST_LEGGINGS = ITEMS.register("taoist_leggings",
            () -> new TaoistArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredHolder<Item, Item> TAOIST_BOOTS = ITEMS.register("taoist_boots",
            () -> new TaoistArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.BOOTS.getDurability(37))));

    //Onmyoji Set
    public static final DeferredHolder<Item, Item> ONMYOJI_HAT = ITEMS.register("onmyoji_hat",
            () -> new OnmyojiArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredHolder<Item, Item> ONMYOJI_ROBES = ITEMS.register("onmyoji_robes",
            () -> new OnmyojiArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredHolder<Item, Item> ONMYOJI_LEGGINGS = ITEMS.register("onmyoji_leggings",
            () -> new OnmyojiArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredHolder<Item, Item> ONMYOJI_GETA = ITEMS.register("onmyoji_geta",
            () -> new OnmyojiArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.BOOTS.getDurability(37))));

    //Armor
    public static final DeferredHolder<Item, Item> JIANGSHI_HAT = ITEMS.register("jiangshi_hat",
            () -> new JiangshiHatItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).rarity(Rarity.UNCOMMON).durability(ArmorItem.Type.HELMET.getDurability(24))));

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


