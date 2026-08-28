package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public enum MFTEExtendedArmorMaterial implements IronsExtendedArmorMaterial {
    JADE("iss_magicfromtheeast:jade", 45, makeArmorMap(4, 10, 6, 4), 40, SoundEvents.ARMOR_EQUIP_CHAIN,
            2.0f, 0.05f, () -> Ingredient.of(MFTEItemRegistries.REFINED_JADE_INGOT.get()), () -> Map.of(
            Attributes.MOVEMENT_SPEED, new AttributeModifier("Movement Speed", -0.025, AttributeModifier.Operation.MULTIPLY_BASE),
            AttributeRegistry.SPELL_RESIST.get(), new AttributeModifier("Spell Resist", 0.10, AttributeModifier.Operation.MULTIPLY_BASE),
            AttributeRegistry.MAX_MANA.get(), new AttributeModifier("Max Mana", 50, AttributeModifier.Operation.ADDITION))),

    ELEMENTAL_COMMANDER("iss_magicfromtheeast:elemental", 37, magicArmorMap(), 40, SoundEvents.ARMOR_EQUIP_LEATHER,
            0f, 0f, () -> Ingredient.of(ItemRegistry.ARCANE_INGOT.get()), () -> Map.of(
            AttributeRegistry.MAX_MANA.get(), new AttributeModifier("Max Mana", 150, AttributeModifier.Operation.ADDITION),
            AttributeRegistry.SPELL_POWER.get(), new AttributeModifier("Spell Power", 0.10, AttributeModifier.Operation.MULTIPLY_BASE))),

    BOOTS_OF_MIST("iss_magicfromtheeast:mist", 37, magicArmorMap(), 40, SoundEvents.ARMOR_EQUIP_LEATHER,
            0f, 0f, () -> Ingredient.of(ItemRegistry.ARCANE_INGOT.get()), () -> Map.of(
            AttributeRegistry.MAX_MANA.get(), new AttributeModifier("Max Mana", 150, AttributeModifier.Operation.ADDITION),
            AttributeRegistry.SPELL_POWER.get(), new AttributeModifier("Spell Power", 0.10, AttributeModifier.Operation.MULTIPLY_BASE))),

    ONMYOJI("iss_magicfromtheeast:onmyoji", 37, magicArmorMap(), 15, SoundEvents.ARMOR_EQUIP_LEATHER,
            0f, 0f, () -> Ingredient.of(ItemRegistry.MAGIC_CLOTH.get()), () -> Map.of(
            AttributeRegistry.MAX_MANA.get(), new AttributeModifier("Max Mana", 125, AttributeModifier.Operation.ADDITION),
            MFTEAttributeRegistries.SPIRIT_SPELL_POWER.get(), new AttributeModifier("School Power", .10, AttributeModifier.Operation.MULTIPLY_BASE),
            AttributeRegistry.SPELL_POWER.get(), new AttributeModifier("Base Power", .05, AttributeModifier.Operation.MULTIPLY_BASE))),

    TAOIST("iss_magicfromtheeast:taoist", 37, magicArmorMap(), 15, SoundEvents.ARMOR_EQUIP_LEATHER,
            0f, 0f, () -> Ingredient.of(ItemRegistry.MAGIC_CLOTH.get()), () -> Map.of(
            AttributeRegistry.MAX_MANA.get(), new AttributeModifier("Max Mana", 125, AttributeModifier.Operation.ADDITION),
            MFTEAttributeRegistries.SYMMETRY_SPELL_POWER.get(), new AttributeModifier("School Power", .10, AttributeModifier.Operation.MULTIPLY_BASE),
            AttributeRegistry.SPELL_POWER.get(), new AttributeModifier("Base Power", .05, AttributeModifier.Operation.MULTIPLY_BASE))),

    JIANGSHI("iss_magicfromtheeast:jiangshi", 24, makeArmorMap(1, 3, 2, 1), 15, SoundEvents.ARMOR_EQUIP_LEATHER,
            0f, 0f, () -> Ingredient.of(Items.LEATHER), Map::of);

    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 13);
        map.put(ArmorItem.Type.LEGGINGS, 15);
        map.put(ArmorItem.Type.CHESTPLATE, 16);
        map.put(ArmorItem.Type.HELMET, 11);
    });

    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;
    private final LazyLoadedValue<Map<Attribute, AttributeModifier>> additionalAttributes;

    MFTEExtendedArmorMaterial(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionMap,
                              int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance,
                              Supplier<Ingredient> repairIngredient, Supplier<Map<Attribute, AttributeModifier>> additionalAttributes) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionFunctionForType = protectionMap;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
        this.additionalAttributes = new LazyLoadedValue<>(additionalAttributes);
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.protectionFunctionForType.get(type);
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    @Override
    public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
        return this.additionalAttributes.get();
    }

    /**
     * Replacement for {@code ArmorItem.Type#getDurability(int)} which only exists on 1.20.5+.
     */
    public static int durabilityFor(ArmorItem.Type type, int multiplier) {
        return HEALTH_FUNCTION_FOR_TYPE.get(type) * multiplier;
    }

    static public EnumMap<ArmorItem.Type, Integer> makeArmorMap(int helmet, int chestplate, int leggings, int boots) {
        return Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.BOOTS, boots);
            map.put(ArmorItem.Type.LEGGINGS, leggings);
            map.put(ArmorItem.Type.CHESTPLATE, chestplate);
            map.put(ArmorItem.Type.HELMET, helmet);
        });
    }

    static public EnumMap<ArmorItem.Type, Integer> magicArmorMap() {
        return makeArmorMap(3, 8, 6, 3);
    }
}
