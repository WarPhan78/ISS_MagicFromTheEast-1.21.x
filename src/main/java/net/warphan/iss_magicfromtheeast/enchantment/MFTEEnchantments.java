package net.warphan.iss_magicfromtheeast.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.util.MFTETags;

/**
 * 1.20.1 port: on 1.21 these enchantments were data-driven (bootstrapped ResourceKeys +
 * enchantment effect components). On 1.20.1 each enchantment is a class extending
 * {@link Enchantment}, registered below. The 1.21 "supported items" tags are mapped to custom
 * {@link EnchantmentCategory} entries (Forge IExtensibleEnum), and the effect components are
 * applied imperatively through {@link MFTEEnchantmentHelper}.
 */
public class MFTEEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ISS_MagicFromTheEast.MOD_ID);

    // Replacements for the 1.21 "supported items" tags of the data-driven definitions.
    public static final EnchantmentCategory SOULPIERCER_CATEGORY =
            EnchantmentCategory.create("MFTE_SOULPIERCER", item -> item.getDefaultInstance().is(MFTETags.SOULPIERCER));
    public static final EnchantmentCategory SOUL_MELEE_WEAPON_CATEGORY =
            EnchantmentCategory.create("MFTE_SOUL_MELEE_WEAPON", item -> item.getDefaultInstance().is(MFTETags.SOUL_MELEE_WEAPON));
    public static final EnchantmentCategory AMMO_LOAD_WEAPON_CATEGORY =
            EnchantmentCategory.create("MFTE_AMMO_LOAD_WEAPON", item -> item.getDefaultInstance().is(MFTETags.AMMO_LOAD_WEAPON));

    public static final RegistryObject<Enchantment> SOUL_FLAME = ENCHANTMENTS.register("soul_flame", SoulFlameEnchantment::new);
    public static final RegistryObject<Enchantment> GHOSTLY_COLD = ENCHANTMENTS.register("ghostly_cold", GhostlyColdEnchantment::new);
    public static final RegistryObject<Enchantment> SPIRITUAL_FOCUS = ENCHANTMENTS.register("spiritual_focus", SpiritualFocusEnchantment::new);
    public static final RegistryObject<Enchantment> WISELY_WILL = ENCHANTMENTS.register("wisely_will", WiselyWillEnchantment::new);
    public static final RegistryObject<Enchantment> INNER_IMPACT = ENCHANTMENTS.register("inner_impact", InnerImpactEnchantment::new);
    public static final RegistryObject<Enchantment> EXPANDING = ENCHANTMENTS.register("expanding", ExpandingEnchantment::new);

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
