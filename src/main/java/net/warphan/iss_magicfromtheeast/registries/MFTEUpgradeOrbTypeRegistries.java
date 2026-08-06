package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

/**
 * PORT 1.20.1: ISS 3.16.2-1.20.1 backports the {@code upgrade_orb_type} datapack registry
 * ({@link UpgradeOrbTypeRegistry} / {@link UpgradeOrbType}). The legacy {@code UpgradeType} interface
 * still exists but the {@code UpgradeOrbItem(UpgradeType, Properties)} ctor is a deprecated no-op that
 * defaults to the mana orb, so the original 1.21 ResourceKey-based orb types are restored here.
 * <p>
 * The concrete {@link UpgradeOrbType} values are provided through datagen ({@link #bootstrap}, wired in
 * {@code MFTEDataPackProvider}); the orb item stores its {@link ResourceKey} on the stack via NBT
 * ({@code UpgradeOrbTypeData}) on 1.20.1 (no data components).
 */
public class MFTEUpgradeOrbTypeRegistries {

    public static final ResourceKey<UpgradeOrbType> SYMMETRY_SPELL_POWER =
            ResourceKey.create(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, ISS_MagicFromTheEast.id("symmetry_power"));
    public static final ResourceKey<UpgradeOrbType> SPIRIT_SPELL_POWER =
            ResourceKey.create(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, ISS_MagicFromTheEast.id("spirit_power"));

    /**
     * Datagen bootstrap for the {@code upgrade_orb_type} datapack registry (mirrors
     * {@code UpgradeOrbTypeRegistry#bootstrap}). +5% school spell power, MULTIPLY_BASE, with the
     * matching upgrade orb as the container item.
     */
    public static void bootstrap(BootstapContext<UpgradeOrbType> bootstrap) {
        bootstrap.register(SYMMETRY_SPELL_POWER,
                new UpgradeOrbType(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER::get, 0.05,
                        AttributeModifier.Operation.MULTIPLY_BASE, () -> MFTEItemRegistries.SYMMETRY_UPGRADE_ORB.get()));
        bootstrap.register(SPIRIT_SPELL_POWER,
                new UpgradeOrbType(MFTEAttributeRegistries.SPIRIT_SPELL_POWER::get, 0.05,
                        AttributeModifier.Operation.MULTIPLY_BASE, () -> MFTEItemRegistries.SPIRIT_UPGRADE_ORB.get()));
    }
}
