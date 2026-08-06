package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/**
 * TODO PORT 1.20.1: Data components do not exist on 1.20.1. This class used to register
 * {@code DataComponentType}s; it is now a plain ItemStack-NBT helper class. Same class name is kept
 * to minimize call-site churn.
 * <p>
 * Key mapping (1.21 data component -> 1.20.1 NBT key on the ItemStack root tag):
 * <ul>
 *   <li>{@code LOADABLE_WEAPON_CONTENTS} (DataComponentType&lt;LoadableWeaponContents&gt;)
 *       -> {@link #LOADABLE_WEAPON_CONTENTS} : CompoundTag serialized by LoadableWeaponContents (item list of loaded projectiles).</li>
 *   <li>{@code CROSSBOW_AMMO_AMOUNT} (RepeatingCrossbow.ProjectileAmountComponent, int ammoAmount)
 *       -> {@link #CROSSBOW_AMMO_AMOUNT} : int.</li>
 *   <li>{@code CROSSBOW_CHARGE_STATE} (RepeatingCrossbow.ChargeStateComponent, boolean charge)
 *       -> {@link #CROSSBOW_CHARGE_STATE} : boolean.</li>
 *   <li>{@code CROSSBOW_LOADING_STATE} (RepeatingCrossbow.LoadingStateComponent, boolean isLoading)
 *       -> {@link #CROSSBOW_LOADING_STATE} : boolean.</li>
 *   <li>{@code SOUL_DAMAGE} / {@code MANA_USE} (enchantment effect components) -> REMOVED.
 *       Enchantment effect components do not exist on 1.20.1; that logic lives in the
 *       Enchantment classes registered by {@code MFTEEnchantments} (see enchantment package).</li>
 * </ul>
 */
public class MFTEDataComponentRegistries {
    public static final String LOADABLE_WEAPON_CONTENTS = "loadable_weapon_contents";
    public static final String CROSSBOW_AMMO_AMOUNT = "crossbow_ammo_amount";
    public static final String CROSSBOW_CHARGE_STATE = "crossbow_charge_state";
    public static final String CROSSBOW_LOADING_STATE = "crossbow_loading_state";

    private MFTEDataComponentRegistries() {
    }

    //------ generic helpers (replacement for stack.get/set/has/remove of data components) ------

    public static boolean has(ItemStack stack, String key) {
        return stack.getTag() != null && stack.getTag().contains(key);
    }

    public static void remove(ItemStack stack, String key) {
        if (stack.getTag() != null) {
            stack.getTag().remove(key);
        }
    }

    public static boolean getBoolean(ItemStack stack, String key, boolean defaultValue) {
        return stack.getTag() != null && stack.getTag().contains(key) ? stack.getTag().getBoolean(key) : defaultValue;
    }

    public static void setBoolean(ItemStack stack, String key, boolean value) {
        stack.getOrCreateTag().putBoolean(key, value);
    }

    public static int getInt(ItemStack stack, String key, int defaultValue) {
        return stack.getTag() != null && stack.getTag().contains(key) ? stack.getTag().getInt(key) : defaultValue;
    }

    public static void setInt(ItemStack stack, String key, int value) {
        stack.getOrCreateTag().putInt(key, value);
    }

    /**
     * @return the compound stored under {@code key}, or null when absent (mirrors {@code stack.get(component) == null}).
     */
    public static CompoundTag getCompound(ItemStack stack, String key) {
        return stack.getTag() != null && stack.getTag().contains(key) ? stack.getTag().getCompound(key) : null;
    }

    public static void setCompound(ItemStack stack, String key, CompoundTag tag) {
        stack.getOrCreateTag().put(key, tag);
    }
}
