package net.warphan.iss_magicfromtheeast.item.weapons;


import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.warphan.iss_magicfromtheeast.registries.MFTEAttributeRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;

import java.util.function.Supplier;

public class MFTEExtendedWeaponTier implements Tier, IronsWeaponTier {

    public static MFTEExtendedWeaponTier JADE_GUANDAO = new MFTEExtendedWeaponTier(2031, 11, -2.5f, 12, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of(MFTEItemRegistries.REFINED_JADE_INGOT.get()), new AttributeContainer(Attributes.ARMOR_TOUGHNESS, 1, AttributeModifier.Operation.ADD_VALUE));
    public static MFTEExtendedWeaponTier SOUL_BREAKER = new MFTEExtendedWeaponTier(2031, 5, -2.5f, 12, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of(MFTEItemRegistries.CRYSTALLIZED_SOUL.get()), new AttributeContainer(AttributeRegistry.SPELL_RESIST, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static MFTEExtendedWeaponTier SPIRIT_CRUSHER = new MFTEExtendedWeaponTier(2031, 17, - 3.4f, 12, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of(MFTEItemRegistries.CRYSTALLIZED_SOUL.get()),
            new AttributeContainer(AttributeRegistry.SPELL_RESIST, 0.20, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(Attributes.MOVEMENT_SPEED, -0.10f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static MFTEExtendedWeaponTier MURAMASA = new MFTEExtendedWeaponTier(2031, 7, -2.0f, 12, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of(ItemRegistry.BLOOD_RUNE.get()), new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static MFTEExtendedWeaponTier SOUL_KATANA = new MFTEExtendedWeaponTier(2031, 6.5f, -1.9f, 12, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of(MFTEItemRegistries.SPIRIT_RUNE.get()), new AttributeContainer(MFTEAttributeRegistries.SPIRIT_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));


    int uses;
    float damage;
    float speed;
    int enchantmentValue;
    TagKey<Block> incorrectBlocksForDrops;
    Supplier<Ingredient> repairIngredient;
    AttributeContainer[] attributes;

    public MFTEExtendedWeaponTier(int uses, float damage, float speed, int enchantmentValue, TagKey<Block> incorrectBlocksForDrops, Supplier<Ingredient> repairIngredient, AttributeContainer... attributes) {
        this.uses = uses;
        this.damage = damage;
        this.speed = speed;
        this.enchantmentValue = enchantmentValue;
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
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

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlocksForDrops;
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
