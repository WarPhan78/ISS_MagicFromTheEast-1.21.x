package net.warphan.iss_magicfromtheeast.item.weapons;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.warphan.iss_magicfromtheeast.registries.MFTEAttributeRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;

import java.util.function.Supplier;

/**
 * PORT 1.20.1: mirrors ISS 1.20.1-3.16.2 ExtendedWeaponTier ({@code Tier & IronsWeaponTier}).
 * The vanilla 1.20.1 Tier has no getIncorrectBlocksForDrops - the 1.21
 * BlockTags.INCORRECT_FOR_DIAMOND_TOOL entries map to {@link #getLevel()} = 3; like the ISS
 * tiers, {@link #getSpeed()} is the weapon attack speed (mining speed is unused for these items).
 * 1.21 ADD_VALUE -> ADDITION, ADD_MULTIPLIED_BASE -> MULTIPLY_BASE.
 */
public class MFTEExtendedWeaponTier implements Tier, IronsWeaponTier {

    public static MFTEExtendedWeaponTier JADE_GUANDAO = new MFTEExtendedWeaponTier(2031, 11, -2.5f, 12, () -> Ingredient.of(MFTEItemRegistries.REFINED_JADE_INGOT.get()), new AttributeContainer(() -> Attributes.ARMOR_TOUGHNESS, 2, AttributeModifier.Operation.ADDITION));
    public static MFTEExtendedWeaponTier SOUL_BREAKER = new MFTEExtendedWeaponTier(2031, 5, -2.5f, 12, () -> Ingredient.of(MFTEItemRegistries.CRYSTALLIZED_SOUL.get()), new AttributeContainer(AttributeRegistry.SPELL_RESIST, 0.10, AttributeModifier.Operation.MULTIPLY_BASE));
    public static MFTEExtendedWeaponTier SPIRIT_CRUSHER = new MFTEExtendedWeaponTier(2031, 17, -3.4f, 12, () -> Ingredient.of(MFTEItemRegistries.CRYSTALLIZED_SOUL.get()),
            new AttributeContainer(AttributeRegistry.SPELL_RESIST, 0.20, AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeContainer(() -> Attributes.MOVEMENT_SPEED, -0.10f, AttributeModifier.Operation.MULTIPLY_BASE));
    public static MFTEExtendedWeaponTier MURAMASA = new MFTEExtendedWeaponTier(2031, 7, -2.0f, 12, () -> Ingredient.of(ItemRegistry.BLOOD_RUNE.get()), new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
    public static MFTEExtendedWeaponTier SOUL_KATANA = new MFTEExtendedWeaponTier(2031, 6.5f, -1.9f, 12, () -> Ingredient.of(MFTEItemRegistries.SPIRIT_RUNE.get()), new AttributeContainer(MFTEAttributeRegistries.SPIRIT_SPELL_POWER, 0.15, AttributeModifier.Operation.MULTIPLY_BASE));


    int uses;
    float damage;
    float speed;
    int enchantmentValue;
    Supplier<Ingredient> repairIngredient;
    AttributeContainer[] attributes;

    public MFTEExtendedWeaponTier(int uses, float damage, float speed, int enchantmentValue, Supplier<Ingredient> repairIngredient, AttributeContainer... attributes) {
        this.uses = uses;
        this.damage = damage;
        this.speed = speed;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
        this.attributes = attributes;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getAttackDamageBonus() {
        return damage;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    /** Replacement for the 1.21 getIncorrectBlocksForDrops (was INCORRECT_FOR_DIAMOND_TOOL -> diamond level 3). */
    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public AttributeContainer[] getAdditionalAttributes() {
        return this.attributes;
    }
}
