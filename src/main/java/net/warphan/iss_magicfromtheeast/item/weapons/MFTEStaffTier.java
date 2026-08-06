package net.warphan.iss_magicfromtheeast.item.weapons;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffTier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * PORT 1.20.1: on 1.21 this implemented IronsWeaponTier directly; the ISS 1.20.1-3.16.2 StaffItem
 * constructor takes the concrete {@link StaffTier}, so the addon tier extends it (same values;
 * 1.21 ADD_VALUE -> ADDITION, ADD_MULTIPLIED_BASE -> MULTIPLY_BASE).
 */
public class MFTEStaffTier extends StaffTier {

    public static final MFTEStaffTier TAIJI_SWORD = new MFTEStaffTier(5, -2.2f,
            new AttributeContainer(() -> Attributes.ARMOR, 2, AttributeModifier.Operation.ADDITION),
            new AttributeContainer(AttributeRegistry.MANA_REGEN, .20, AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeContainer(AttributeRegistry.SPELL_POWER, .10, AttributeModifier.Operation.MULTIPLY_BASE)
    );

    public MFTEStaffTier(float damage, float speed, AttributeContainer... attributes) {
        super(damage, speed, attributes);
    }
}
