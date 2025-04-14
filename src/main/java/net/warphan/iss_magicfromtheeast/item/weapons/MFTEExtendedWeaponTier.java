package net.warphan.iss_magicfromtheeast.item.weapons;


import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.warphan.iss_magicfromtheeast.registries.ItemRegistries;

import java.util.function.Supplier;

public class MFTEExtendedWeaponTier implements Tier, IronsWeaponTier {

    public static MFTEExtendedWeaponTier JADE_GUANDAO = new MFTEExtendedWeaponTier(2031, 10, -2.5f, 12, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of(ItemRegistries.JADE.get()), new AttributeContainer(Attributes.ARMOR_TOUGHNESS, 1, AttributeModifier.Operation.ADD_VALUE));
    public static MFTEExtendedWeaponTier SOUL_BREAKER = new MFTEExtendedWeaponTier(1561, 4, -2.5f, 10, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of(ItemRegistries.BOTTLE_OF_SOULS.get()));

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
