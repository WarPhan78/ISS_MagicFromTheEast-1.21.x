package net.warphan.iss_magicfromtheeast.util;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class MFTEUtils {
    public static EnchantmentEffectComponents createEnchantmentEffectComponent(String modID) {
        return new EnchantmentEffectComponents(modID);
    }

    public static class EnchantmentEffectComponents extends DeferredRegister<DataComponentType<?>> {
        protected EnchantmentEffectComponents(String name) {
            super(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, name);
        }

        public <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerComponentType(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
            return this.register(name, () -> ((DataComponentType.Builder)builder.apply(DataComponentType.builder())).build());
        }
    }
}
