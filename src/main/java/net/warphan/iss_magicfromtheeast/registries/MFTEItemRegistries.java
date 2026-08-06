package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.compat.MFTECurios;
import net.warphan.iss_magicfromtheeast.item.RitualOrihonSpellbookItem;
import net.warphan.iss_magicfromtheeast.item.armor.*;
import net.warphan.iss_magicfromtheeast.item.consumables.RiceWineBottleItem;
import net.warphan.iss_magicfromtheeast.item.curios.JadePendant;
import net.warphan.iss_magicfromtheeast.item.curios.RustedCoinsSword;
import net.warphan.iss_magicfromtheeast.item.curios.SoulwardRing;
import net.warphan.iss_magicfromtheeast.item.weapons.*;
import net.warphan.iss_magicfromtheeast.setup.MFTERarity;
import net.warphan.iss_magicfromtheeast.util.MFTETags;


public class MFTEItemRegistries {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ISS_MagicFromTheEast.MOD_ID);

    //Magic Stuffs
    public static final RegistryObject<Item> TAIJI_SWORD = ITEMS.register("taiji_sword",
            () -> new StaffItem(ItemPropertiesHelper.equipment(1), MFTEStaffTier.TAIJI_SWORD));
    public static final RegistryObject<Item> RITUAL_ORIHON = ITEMS.register("ritual_orihon", RitualOrihonSpellbookItem::new);

    //Stuffs
    public static final RegistryObject<Item> JADE = ITEMS.register("jade",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final RegistryObject<Item> BOTTLE_OF_SOULS = ITEMS.register("bottle_of_souls",
            () -> new Item(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ARCANE_RELICS = ITEMS.register("arcane_relics",
            () -> new Item(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> RED_STRING = ITEMS.register("red_string",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final RegistryObject<Item> COPPER_COINS = ITEMS.register("copper_coins",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final RegistryObject<Item> YIN_YANG_CORE = ITEMS.register("yin_yang_core",
            () -> new Item(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> RED_SHAFT = ITEMS.register("red_shaft",
            () -> new Item(ItemPropertiesHelper.material()));

    public static final RegistryObject<Item> REFINED_JADE_INGOT = ITEMS.register("refined_jade_ingot",
            () -> new Item(ItemPropertiesHelper.material().rarity(MFTERarity.JADELIGHT)));
    public static final RegistryObject<Item> CRYSTALLIZED_SOUL = ITEMS.register("crystallized_soul",
            () -> new Item(ItemPropertiesHelper.material().rarity(Rarity.RARE).fireResistant()));

    public static final RegistryObject<Item> RICE_WINE_BOTTLE = ITEMS.register("rice_wine_bottle",
            () -> new RiceWineBottleItem(ItemPropertiesHelper.material(4)));

    //Weapons
    // NOTE PORT 1.20.1: Item.Properties#attributes does not exist on 1.20.1 - the ISS 3.16.2 weapon
    //  items build their own modifiers from the (Tier & IronsWeaponTier) tiers.
    public static final RegistryObject<Item> JADE_GUANDAO = ITEMS.register("jade_guandao",
            () -> new MagicSwordItem(MFTEExtendedWeaponTier.JADE_GUANDAO, ItemPropertiesHelper.equipment().rarity(MFTERarity.JADELIGHT), SpellDataRegistryHolder.of(
                    new SpellDataRegistryHolder(MFTESpellRegistries.NEPHRITE_SLASH_SPELL, 5))));
    public static final RegistryObject<Item> SOUL_BREAKER = ITEMS.register("soul_breaker",
            () -> new SoulBreakerItem(MFTEExtendedWeaponTier.SOUL_BREAKER, ItemPropertiesHelper.equipment().rarity(Rarity.RARE).fireResistant()));

    public static final RegistryObject<Item> SPIRIT_CRUSHER = ITEMS.register("spirit_crusher",
            () -> new SpiritCrusherItem(MFTEExtendedWeaponTier.SPIRIT_CRUSHER, ItemPropertiesHelper.equipment().rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> MURAMASA = ITEMS.register("muramasa",
            () -> new MuramasaItem(MFTEExtendedWeaponTier.MURAMASA, ItemPropertiesHelper.equipment().rarity(MFTERarity.BLOODFUL).fireResistant(), SpellDataRegistryHolder.of(
                    new SpellDataRegistryHolder(SpellRegistry.BLOOD_SLASH_SPELL, 5))));
    public static final RegistryObject<Item> SOUL_KATANA = ITEMS.register("soul_katana",
            () -> new MasamuneItem(MFTEExtendedWeaponTier.SOUL_KATANA, ItemPropertiesHelper.equipment().rarity(Rarity.EPIC).fireResistant(), SpellDataRegistryHolder.of(
                    new SpellDataRegistryHolder(MFTESpellRegistries.CALAMITY_CUT_SPELL, 5))));

    public static final RegistryObject<Item> SOULPIERCER = ITEMS.register("soulpiercer",
            () -> new SoulPiercer(ItemPropertiesHelper.hidden().durability(635).rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> REPEATING_CROSSBOW = ITEMS.register("repeating_crossbow",
            () -> new RepeatingCrossbow(ItemPropertiesHelper.hidden().durability(465)));

    // Curios
    // PORT 1.20.1: ISS 3.16.2 backports CurioBaseItem#withAttributes(slot, AttributeContainer...),
    //  so the original 1.21 belt-curio wiring is restored. 1.21 ADD_MULTIPLIED_BASE -> 1.20.1 MULTIPLY_BASE.
    public static final RegistryObject<CurioBaseItem> BAGUA_MIRROR = ITEMS.register("bagua_mirror",
            () -> new CurioBaseItem(ItemPropertiesHelper.equipment(1)).withAttributes(MFTECurios.BELT_SLOT,
                    new AttributeContainer(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER, 0.10, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.FIRE_SPELL_POWER, 0.05, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, 0.05, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, 0.05, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, 0.05, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, 0.03, AttributeModifier.Operation.MULTIPLY_BASE)));
    public static final RegistryObject<CurioBaseItem> COINS_SWORD = ITEMS.register("coins_sword",
            () -> new CurioBaseItem(ItemPropertiesHelper.equipment(1)).withAttributes(MFTECurios.BELT_SLOT,
                    new AttributeContainer(AttributeRegistry.BLOOD_MAGIC_RESIST, 0.15, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.ENDER_MAGIC_RESIST, 0.10, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.ELDRITCH_MAGIC_RESIST, 0.05, AttributeModifier.Operation.MULTIPLY_BASE)));
    public static final RegistryObject<CurioBaseItem> SOULWARD_RING = ITEMS.register("soulward_ring", SoulwardRing::new);
    public static final RegistryObject<CurioBaseItem> JADE_PENDANT = ITEMS.register("jade_pendant", JadePendant::new);
    public static final RegistryObject<CurioBaseItem> RUSTED_COINS_SWORD = ITEMS.register("rusted_coins_sword",
            () -> new RustedCoinsSword(ItemPropertiesHelper.equipment(1)).withAttributes(MFTECurios.BELT_SLOT,
            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.10, AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeContainer(AttributeRegistry.SUMMON_DAMAGE, 0.05, AttributeModifier.Operation.MULTIPLY_BASE)));

    //Rune and Orbs
    public static final RegistryObject<Item> SYMMETRY_RUNE = ITEMS.register("symmetry_rune",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final RegistryObject<Item> SPIRIT_RUNE = ITEMS.register("spirit_rune",
            () -> new Item(ItemPropertiesHelper.material()));
    public static final RegistryObject<Item> DUNE_RUNE = ITEMS.register("dune_rune",
            () -> new Item(ItemPropertiesHelper.material()));
    // PORT 1.20.1: ISS 3.16.2-1.20.1 keeps the UpgradeOrbType datapack registry; the orb item takes the
    //  ResourceKey via UpgradeOrbItem(Properties, ResourceKey<UpgradeOrbType>) and stores it on the stack
    //  as NBT (no data component on 1.20.1). The legacy UpgradeType ctor is a deprecated no-op, so it is NOT used.
    public static final RegistryObject<Item> SYMMETRY_UPGRADE_ORB = ITEMS.register("symmetry_upgrade_orb",
            () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON), MFTEUpgradeOrbTypeRegistries.SYMMETRY_SPELL_POWER));
    public static final RegistryObject<Item> SPIRIT_UPGRADE_ORB = ITEMS.register("spirit_upgrade_orb",
            () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON), MFTEUpgradeOrbTypeRegistries.SPIRIT_SPELL_POWER));

    //Taoist Set
    public static final RegistryObject<Item> TAOIST_HAT = ITEMS.register("taoist_helmet",
            () -> new TaoistArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.HELMET, 37))));
    public static final RegistryObject<Item> TAOIST_ROBES = ITEMS.register("taoist_chestplate",
            () -> new TaoistArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.CHESTPLATE, 37))));
    public static final RegistryObject<Item> TAOIST_LEGGINGS = ITEMS.register("taoist_leggings",
            () -> new TaoistArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.LEGGINGS, 37))));
    public static final RegistryObject<Item> TAOIST_BOOTS = ITEMS.register("taoist_boots",
            () -> new TaoistArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.BOOTS, 37))));

    //Symmetry Artifact
    public static final RegistryObject<Item> ELEMENTAL_COMMANDER_CHESTPLATE = ITEMS.register("elemental_commander_chestplate",
            () -> new ElementalCommanderArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC).fireResistant().durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.CHESTPLATE, 37))));

    //Onmyoji Set
    public static final RegistryObject<Item> ONMYOJI_HAT = ITEMS.register("onmyoji_helmet",
            () -> new OnmyojiArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.HELMET, 37))));
    public static final RegistryObject<Item> ONMYOJI_ROBES = ITEMS.register("onmyoji_chestplate",
            () -> new OnmyojiArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.CHESTPLATE, 37))));
    public static final RegistryObject<Item> ONMYOJI_LEGGINGS = ITEMS.register("onmyoji_leggings",
            () -> new OnmyojiArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.LEGGINGS, 37))));
    public static final RegistryObject<Item> ONMYOJI_GETA = ITEMS.register("onmyoji_boots",
            () -> new OnmyojiArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.BOOTS, 37))));

    //Spirit Artifact
    public static final RegistryObject<Item> BOOTS_OF_MIST = ITEMS.register("mist_boots",
            () -> new BootsOfMistArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC).fireResistant().durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.BOOTS, 37))));

    //Armor
    public static final RegistryObject<Item> JIANGSHI_HAT = ITEMS.register("jiangshi_hat",
            () -> new JiangshiHatItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).rarity(Rarity.UNCOMMON).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.HELMET, 24))));

    public static final RegistryObject<Item> JADE_PAGODA_HELMET = ITEMS.register("jade_pagoda_helmet",
            () -> new JadePagodaArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).rarity(MFTERarity.JADELIGHT).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.HELMET, 45))));
    public static final RegistryObject<Item> JADE_PAGODA_CHESTPLATE = ITEMS.register("jade_pagoda_chestplate",
            () -> new JadePagodaArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).rarity(MFTERarity.JADELIGHT).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.CHESTPLATE, 45))));
    public static final RegistryObject<Item> JADE_PAGODA_LEGGINGS = ITEMS.register("jade_pagoda_leggings",
            () -> new JadePagodaArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).rarity(MFTERarity.JADELIGHT).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.LEGGINGS, 45))));
    public static final RegistryObject<Item> JADE_PAGODA_BOOTS = ITEMS.register("jade_pagoda_boots",
            () -> new JadePagodaArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).rarity(MFTERarity.JADELIGHT).durability(MFTEArmorMaterialRegistries.durabilityFor(ArmorItem.Type.BOOTS, 45))));

    // NOTE PORT 1.20.1: AnimalArmorItem does not exist on 1.20.1 - HorseArmorItem with the former
    //  "BODY" defense value (16) of the jade material and the mod texture.
    public static final RegistryObject<Item> JADE_PAGODA_HORSE_ARMOR = ITEMS.register("jade_pagoda_horse_armor",
            () -> new HorseArmorItem(16, new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "textures/entity/horse/armor/horse_armor_jade.png"),
                    new Item.Properties().stacksTo(1).rarity(MFTERarity.JADELIGHT)));

    //Block Items
    public static final RegistryObject<Item> JADE_ORE_ITEM = ITEMS.register("jade_ore",
            () -> new BlockItem(MFTEBlockRegistries.JADE_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> JADE_ORE_DEEPSLATE_ITEM = ITEMS.register("deepslate_jade_ore",
            () -> new BlockItem(MFTEBlockRegistries.JADE_ORE_DEEPSLATE.get(), new Item.Properties()));

    public static final RegistryObject<Item> JADE_BLOCK_ITEM = ITEMS.register("jade_block",
            () -> new BlockItem(MFTEBlockRegistries.JADE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> JADE_SLAB_ITEM = ITEMS.register("jade_slab",
            () -> new BlockItem(MFTEBlockRegistries.JADE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> JADE_STAIR_ITEM = ITEMS.register("jade_stair",
            () -> new BlockItem(MFTEBlockRegistries.JADE_STAIR.get(), new Item.Properties()));
    public static final RegistryObject<Item> JADE_WALL_ITEM = ITEMS.register("jade_wall",
            () -> new BlockItem(MFTEBlockRegistries.JADE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHISELED_JADE = ITEMS.register("chiseled_jade",
            () -> new BlockItem(MFTEBlockRegistries.CHISELED_JADE.get(), new Item.Properties()));
    public static final RegistryObject<Item> JADE_LION_HEAD_BLOCK_ITEMS = ITEMS.register("jade_lion_head_block",
            () -> new BlockItem(MFTEBlockRegistries.JADE_LION_HEAD_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> JADESTONE_BRICKS_ITEM = ITEMS.register("jadestone_bricks",
            () -> new BlockItem(MFTEBlockRegistries.JADE_BRICK_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> JADESTONE_BRICKS_SLAB_ITEM = ITEMS.register("jadestone_bricks_slab",
            () -> new BlockItem(MFTEBlockRegistries.JADE_BRICK_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> JADESTONE_BRICKS_STAIR_ITEM = ITEMS.register("jadestone_bricks_stair",
            () -> new BlockItem(MFTEBlockRegistries.JADE_BRICK_STAIR.get(), new Item.Properties()));
    public static final RegistryObject<Item> JADESTONE_BRICKS_WALL_ITEM = ITEMS.register("jadestone_bricks_wall",
            () -> new BlockItem(MFTEBlockRegistries.JADE_BRICK_WALL.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHISELED_JADESTONE_BRICKS_ITEMS = ITEMS.register("chiseled_jadestone_bricks",
            () -> new BlockItem(MFTEBlockRegistries.CHISELED_JADE_BRICK.get(), new Item.Properties()));
    public static final RegistryObject<Item> JADESTONE_BRICKS_PILLAR = ITEMS.register("jadestone_bricks_pillar",
            () -> new BlockItem(MFTEBlockRegistries.JADE_BRICK_PILLAR.get(), new Item.Properties()));

    public static final RegistryObject<Item> REFINED_JADE_BLOCK_ITEM = ITEMS.register("refined_jade_block",
            () -> new BlockItem(MFTEBlockRegistries.REFINED_JADE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> VASE_OF_RICE_WINE = ITEMS.register("vase_rice_wine",
            () -> new BlockItem(MFTEBlockRegistries.RICE_WINE_VASE.get(), new Item.Properties().stacksTo(1)));

    //Patterns
    public static final RegistryObject<BannerPatternItem> BALANCE_PATTERN = ITEMS.register("balance_pattern", () -> new BannerPatternItem(MFTETags.BALANCE_PATTERN_ITEM, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    //Spawn Eggs
    public static final RegistryObject<ForgeSpawnEggItem> TAOIST_SPAWN_EGG = ITEMS.register("taoist_spawn_egg", () -> new ForgeSpawnEggItem(MFTEEntityRegistries.TAOIST, 0xdfff00, 0x222021, ItemPropertiesHelper.material().stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> ONMYOJI_SPAWN_EGG = ITEMS.register("onmyoji_spawn_egg", () -> new ForgeSpawnEggItem(MFTEEntityRegistries.ONMYOJI, 0xfbfbf9, 0x01ffff, ItemPropertiesHelper.material().stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> JIANGSHI_SPAWN_EGG = ITEMS.register("jiangshi_spawn_egg", () -> new ForgeSpawnEggItem(MFTEEntityRegistries.JIANGSHI, 0x281e5d, 0x006a4e, ItemPropertiesHelper.material().stacksTo(64)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
