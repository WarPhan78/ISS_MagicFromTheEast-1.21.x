package net.warphan.iss_magicfromtheeast.item.weapons;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.warphan.iss_magicfromtheeast.registries.MFTEAttributeRegistries;

public class MFTEStaffTier implements IronsWeaponTier {

    public static final MFTEStaffTier TAIJI_SWORD = new MFTEStaffTier (5, -2.2f,
            new AttributeContainer(AttributeRegistry.MANA_REGEN, .25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER, .15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.SPELL_POWER, .05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );

    float damage;
    float speed;
    AttributeContainer[] attributes;

    public MFTEStaffTier (float damage, float speed, AttributeContainer...attributes) {
        this.damage = damage;
        this.speed = speed;
        this.attributes = attributes;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return damage;
    }

    public AttributeContainer[] getAdditionalAttributes() {
        return this.attributes;
    }
}
