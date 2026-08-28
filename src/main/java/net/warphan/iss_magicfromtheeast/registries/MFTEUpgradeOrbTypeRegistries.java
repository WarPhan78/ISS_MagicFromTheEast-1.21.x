package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class MFTEUpgradeOrbTypeRegistries {

    public static final ResourceKey<UpgradeOrbType> SYMMETRY_SPELL_POWER =
            ResourceKey.create(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, ISS_MagicFromTheEast.id("symmetry_power"));
    public static final ResourceKey<UpgradeOrbType> SPIRIT_SPELL_POWER =
            ResourceKey.create(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, ISS_MagicFromTheEast.id("spirit_power"));

    public static void bootstrap(BootstapContext<UpgradeOrbType> bootstrap) {
        bootstrap.register(SYMMETRY_SPELL_POWER,
                new UpgradeOrbType(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER::get, 0.05,
                        AttributeModifier.Operation.MULTIPLY_BASE, () -> MFTEItemRegistries.SYMMETRY_UPGRADE_ORB.get()));
        bootstrap.register(SPIRIT_SPELL_POWER,
                new UpgradeOrbType(MFTEAttributeRegistries.SPIRIT_SPELL_POWER::get, 0.05,
                        AttributeModifier.Operation.MULTIPLY_BASE, () -> MFTEItemRegistries.SPIRIT_UPGRADE_ORB.get()));
    }
}
