package net.warphan.iss_magicfromtheeast.registries;

/**
 * TODO PORT 1.20.1: enchantment entity effect components ({@code EnchantmentEntityEffect},
 * {@code Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE}) do not exist on 1.20.1.
 * <p>
 * The previous registrations:
 * <ul>
 *   <li>{@code SOUL_FLAME} (SoulFlameEnchantmentEffect) - logic must move into the SoulFlame
 *       Enchantment class / events (see enchantment package).</li>
 *   <li>{@code GHOSTLY_COLD} (GhostlyColdEnchantmentEffect) - logic must move into the GhostlyCold
 *       Enchantment class / events (see enchantment package).</li>
 * </ul>
 * Classic {@code Enchantment} registration now lives in
 * {@link net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantments} (DeferredRegister&lt;Enchantment&gt;),
 * registered on the mod event bus by the main class.
 */
public class MFTEEnchantmentEffectRegistries {
    private MFTEEnchantmentEffectRegistries() {
    }
}
