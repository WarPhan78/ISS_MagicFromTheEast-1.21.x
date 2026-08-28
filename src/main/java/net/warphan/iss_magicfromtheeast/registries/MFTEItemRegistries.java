package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
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

import java.util.function.Function;


public class MFTEItemRegistries {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ISS_MagicFromTheEast.MOD_ID);

    //Magic Stuffs
    public static final RegistryObject<Item> TAIJI_SWORD = registerItem("taiji_sword",
            (properties) -> new StaffItem(properties.stacksTo(1).fireResistant(), MFTEStaffTier.TAIJI_SWORD));
    public static final RegistryObject<Item> RITUAL_ORIHON = registerItem("ritual_orihon",
            (properties) -> new RitualOrihonSpellbookItem(properties.stacksTo(1).fireResistant().rarity(Rarity.RARE)));

    //Stuffs
    public static final RegistryObject<Item> YIN_YANG_CORE = registerItem("yin_yang_core",
            (properties) -> new Item(properties.rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BOTTLE_OF_SOULS = registerItem("bottle_of_souls",
            (properties) -> new Item(properties.rarity(Rarity.UNCOMMON)));
//    public static final RegistryObject<Item> ARCANE_RELICS = registerItem("arcane_relics",
//            (properties) -> new Item(properties.rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> JADE = registerItem("jade", Item::new);
    public static final RegistryObject<Item> RED_STRING = registerItem("red_string", Item::new);
    public static final RegistryObject<Item> COPPER_COINS = registerItem("copper_coins", Item::new);
    public static final RegistryObject<Item> RED_SHAFT = registerItem("red_shaft", Item::new);
    public static final RegistryObject<Item> REFINED_JADE_INGOT = registerItem("refined_jade_ingot", Item::new);
    public static final RegistryObject<Item> CRYSTALLIZED_SOUL = registerItem("crystallized_soul", Item::new);

    public static final RegistryObject<Item> RICE_WINE_BOTTLE = registerItem("rice_wine_bottle",
            (properties) -> new RiceWineBottleItem(properties.stacksTo(4)));

    //Weapons
    // NOTE PORT 1.20.1: Item.Properties#attributes does not exist on 1.20.1 - the ISS 3.16.2 weapon
    //  items build their own modifiers from the (Tier & IronsWeaponTier) tiers.
    public static final RegistryObject<Item> JADE_GUANDAO = registerItem("jade_guandao",
            (properties) -> new MagicSwordItem(MFTEExtendedWeaponTier.JADE_GUANDAO, properties.rarity(MFTERarity.JADELIGHT), SpellDataRegistryHolder.of(
                    new SpellDataRegistryHolder(MFTESpellRegistries.NEPHRITE_SLASH_SPELL, 5))));
    public static final RegistryObject<Item> SOUL_BREAKER = registerItem("soul_breaker",
            (properties) -> new SoulBreakerItem(MFTEExtendedWeaponTier.SOUL_BREAKER, properties.rarity(Rarity.RARE).fireResistant()));

    public static final RegistryObject<Item> SPIRIT_CRUSHER = registerItem("spirit_crusher",
            (properties) -> new SpiritCrusherItem(MFTEExtendedWeaponTier.SPIRIT_CRUSHER, properties.rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> MURAMASA = registerItem("muramasa",
            (properties) -> new MuramasaItem(MFTEExtendedWeaponTier.MURAMASA, properties.rarity(MFTERarity.BLOODFUL).fireResistant(), SpellDataRegistryHolder.of(
                    new SpellDataRegistryHolder(SpellRegistry.BLOOD_SLASH_SPELL, 5))));
    public static final RegistryObject<Item> SOUL_KATANA = registerItem("soul_katana",
            (properties) -> new MasamuneItem(MFTEExtendedWeaponTier.SOUL_KATANA, properties.rarity(Rarity.EPIC).fireResistant(), SpellDataRegistryHolder.of(
                    new SpellDataRegistryHolder(MFTESpellRegistries.CALAMITY_CUT_SPELL, 5))));

    public static final RegistryObject<Item> SOULPIERCER = registerItem("soulpiercer",
            (properties) -> new SoulPiercer(properties.durability(635).rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> REPEATING_CROSSBOW = registerItem("repeating_crossbow",
            (properties) -> new RepeatingCrossbow(properties.durability(465)));

    // Curios
    // PORT 1.20.1: ISS 3.16.2 backports CurioBaseItem#withAttributes(slot, AttributeContainer...),
    //  so the original 1.21 belt-curio wiring is restored. 1.21 ADD_MULTIPLIED_BASE -> 1.20.1 MULTIPLY_BASE.
    public static final RegistryObject<CurioBaseItem> BAGUA_MIRROR = registerItem("bagua_mirror",
            (properties) -> new CurioBaseItem(properties.stacksTo(1)).withAttributes(MFTECurios.BELT_SLOT,
                    new AttributeContainer(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER, 0.10, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.FIRE_SPELL_POWER, 0.05, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, 0.05, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, 0.05, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, 0.05, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, 0.03, AttributeModifier.Operation.MULTIPLY_BASE)));
    public static final RegistryObject<CurioBaseItem> COINS_SWORD = registerItem("coins_sword",
            (properties) -> new CurioBaseItem(properties.stacksTo(1)).withAttributes(MFTECurios.BELT_SLOT,
                    new AttributeContainer(AttributeRegistry.BLOOD_MAGIC_RESIST, 0.15, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.ENDER_MAGIC_RESIST, 0.10, AttributeModifier.Operation.MULTIPLY_BASE),
                    new AttributeContainer(AttributeRegistry.ELDRITCH_MAGIC_RESIST, 0.05, AttributeModifier.Operation.MULTIPLY_BASE)));
    public static final RegistryObject<CurioBaseItem> SOULWARD_RING = registerItem("soulward_ring", SoulwardRing::new);
    public static final RegistryObject<CurioBaseItem> JADE_PENDANT = ITEMS.register("jade_pendant", JadePendant::new);
    public static final RegistryObject<CurioBaseItem> RUSTED_COINS_SWORD = registerItem("rusted_coins_sword",
            (properties) -> new RustedCoinsSword(properties.stacksTo(1)).withAttributes(MFTECurios.BELT_SLOT,
            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.10, AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeContainer(AttributeRegistry.SUMMON_DAMAGE, 0.05, AttributeModifier.Operation.MULTIPLY_BASE)));

    //Rune and Orbs
    public static final RegistryObject<Item> SYMMETRY_RUNE = registerItem("symmetry_rune", Item::new);
    public static final RegistryObject<Item> SPIRIT_RUNE = registerItem("spirit_rune", Item::new);
//    public static final RegistryObject<Item> DUNE_RUNE = registerItem("dune_rune", Item::new);

    // PORT 1.20.1: ISS 3.16.2-1.20.1 keeps the UpgradeOrbType datapack registry; the orb item takes the
    //  ResourceKey via UpgradeOrbItem(Properties, ResourceKey<UpgradeOrbType>) and stores it on the stack
    //  as NBT (no data component on 1.20.1). The legacy UpgradeType ctor is a deprecated no-op, so it is NOT used.
    public static final RegistryObject<Item> SYMMETRY_UPGRADE_ORB = registerItem("symmetry_upgrade_orb",
            (properties) -> new UpgradeOrbItem(properties.rarity(Rarity.UNCOMMON), MFTEUpgradeOrbTypeRegistries.SYMMETRY_SPELL_POWER));
    public static final RegistryObject<Item> SPIRIT_UPGRADE_ORB = registerItem("spirit_upgrade_orb",
            (properties) -> new UpgradeOrbItem(properties.rarity(Rarity.UNCOMMON), MFTEUpgradeOrbTypeRegistries.SPIRIT_SPELL_POWER));

    //Taoist Set
    public static final RegistryObject<Item> TAOIST_HAT = registerItem("taoist_helmet",
            (properties) -> new TaoistArmorItem(ArmorItem.Type.HELMET, properties.stacksTo(1).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.HELMET, 37))));
    public static final RegistryObject<Item> TAOIST_ROBES = registerItem("taoist_chestplate",
            (properties) -> new TaoistArmorItem(ArmorItem.Type.CHESTPLATE, properties.stacksTo(1).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.CHESTPLATE, 37))));
    public static final RegistryObject<Item> TAOIST_LEGGINGS = registerItem("taoist_leggings",
            (properties) -> new TaoistArmorItem(ArmorItem.Type.LEGGINGS, properties.stacksTo(1).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.LEGGINGS, 37))));
    public static final RegistryObject<Item> TAOIST_BOOTS = registerItem("taoist_boots",
            (properties) -> new TaoistArmorItem(ArmorItem.Type.BOOTS, properties.stacksTo(1).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.BOOTS, 37))));

    //Symmetry Artifact
    public static final RegistryObject<Item> ELEMENTAL_COMMANDER_CHESTPLATE = registerItem("elemental_commander_chestplate",
            (properties) -> new ElementalCommanderArmorItem(ArmorItem.Type.CHESTPLATE, properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant().durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.CHESTPLATE, 37))));

    //Onmyoji Set
    public static final RegistryObject<Item> ONMYOJI_HAT = registerItem("onmyoji_helmet",
            (properties) -> new OnmyojiArmorItem(ArmorItem.Type.HELMET, properties.stacksTo(1).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.HELMET, 37))));
    public static final RegistryObject<Item> ONMYOJI_ROBES = registerItem("onmyoji_chestplate",
            (properties) -> new OnmyojiArmorItem(ArmorItem.Type.CHESTPLATE, properties.stacksTo(1).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.CHESTPLATE, 37))));
    public static final RegistryObject<Item> ONMYOJI_LEGGINGS = registerItem("onmyoji_leggings",
            (properties) -> new OnmyojiArmorItem(ArmorItem.Type.LEGGINGS, properties.stacksTo(1).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.LEGGINGS, 37))));
    public static final RegistryObject<Item> ONMYOJI_GETA = registerItem("onmyoji_boots",
            (properties) -> new OnmyojiArmorItem(ArmorItem.Type.BOOTS, properties.stacksTo(1).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.BOOTS, 37))));

    //Spirit Artifact
    public static final RegistryObject<Item> BOOTS_OF_MIST = registerItem("mist_boots",
            (properties) -> new BootsOfMistArmorItem(ArmorItem.Type.BOOTS, properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant().durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.BOOTS, 37))));

    //Armor
    public static final RegistryObject<Item> JIANGSHI_HAT = registerItem("jiangshi_hat",
            (properties) -> new JiangshiHatItem(ArmorItem.Type.HELMET, properties.stacksTo(1).rarity(Rarity.UNCOMMON).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.HELMET, 24))));

    public static final RegistryObject<Item> JADE_PAGODA_HELMET = registerItem("jade_pagoda_helmet",
            (properties) -> new JadePagodaArmorItem(ArmorItem.Type.HELMET, properties.stacksTo(1).rarity(MFTERarity.JADELIGHT).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.HELMET, 45))));
    public static final RegistryObject<Item> JADE_PAGODA_CHESTPLATE = registerItem("jade_pagoda_chestplate",
            (properties) -> new JadePagodaArmorItem(ArmorItem.Type.CHESTPLATE, properties.stacksTo(1).rarity(MFTERarity.JADELIGHT).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.CHESTPLATE, 45))));
    public static final RegistryObject<Item> JADE_PAGODA_LEGGINGS = registerItem("jade_pagoda_leggings",
            (properties) -> new JadePagodaArmorItem(ArmorItem.Type.LEGGINGS, properties.stacksTo(1).rarity(MFTERarity.JADELIGHT).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.LEGGINGS, 45))));
    public static final RegistryObject<Item> JADE_PAGODA_BOOTS = registerItem("jade_pagoda_boots",
            (properties) -> new JadePagodaArmorItem(ArmorItem.Type.BOOTS, properties.stacksTo(1).rarity(MFTERarity.JADELIGHT).durability(MFTEExtendedArmorMaterial.durabilityFor(ArmorItem.Type.BOOTS, 45))));

    // NOTE PORT 1.20.1: AnimalArmorItem does not exist on 1.20.1 - HorseArmorItem with the former
    //  "BODY" defense value (16) of the jade material and the mod texture.
    public static final RegistryObject<Item> JADE_PAGODA_HORSE_ARMOR = ITEMS.register("jade_pagoda_horse_armor",
            () -> new HorseArmorItem(16, ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/entity/horse/armor/horse_armor_jade.png"),
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
    public static final RegistryObject<ForgeSpawnEggItem> TAOIST_SPAWN_EGG = registerItem("taoist_spawn_egg", (properties) -> new ForgeSpawnEggItem(MFTEEntityRegistries.TAOIST, 0xdfff00, 0x222021, properties.stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> ONMYOJI_SPAWN_EGG = registerItem("onmyoji_spawn_egg", (properties) -> new ForgeSpawnEggItem(MFTEEntityRegistries.ONMYOJI, 0xfbfbf9, 0x01ffff, properties.stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> JIANGSHI_SPAWN_EGG = registerItem("jiangshi_spawn_egg", (properties) -> new ForgeSpawnEggItem(MFTEEntityRegistries.JIANGSHI, 0x281e5d, 0x006a4e, properties.stacksTo(64)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static <T extends Item> RegistryObject<T> registerItem(String name, Function<Item.Properties, T> itemFactory) {
        return ITEMS.register(name, () -> itemFactory.apply(new Item.Properties()));
    }
}
