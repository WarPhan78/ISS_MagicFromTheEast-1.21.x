package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.weapons.RepeatingCrossbow;
import net.warphan.iss_magicfromtheeast.util.MFTEUtils;

import java.util.List;
import java.util.function.UnaryOperator;

public class MFTEDataComponentRegistries {
    private static final DeferredRegister<DataComponentType<?>> MFTE_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ISS_MagicFromTheEast.MOD_ID);
    private static final MFTEUtils.EnchantmentEffectComponents MFTE_ENCHANTMENT_EFFECT_COMPONENTS = MFTEUtils.createEnchantmentEffectComponent(ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTE_COMPONENTS.register(eventBus);
        MFTE_ENCHANTMENT_EFFECT_COMPONENTS.register(eventBus);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String pName, UnaryOperator<DataComponentType.Builder<T>> pBuilder) {
        return MFTE_COMPONENTS.register(pName, () -> pBuilder.apply(DataComponentType.builder()).build());
    }

    //Repeating Crossbow
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RepeatingCrossbow.ProjectileAmountComponent>> CROSSBOW_AMMO_AMOUNT = register("crossbow_ammo_amount", (builder) -> builder.persistent(RepeatingCrossbow.ProjectileAmountComponent.CODEC).networkSynchronized(RepeatingCrossbow.ProjectileAmountComponent.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RepeatingCrossbow.LoadingStateComponent>> CROSSBOW_LOADING_STATE = register("crossbow_loading_state", (builder) -> builder.persistent(RepeatingCrossbow.LoadingStateComponent.CODEC).networkSynchronized(RepeatingCrossbow.LoadingStateComponent.STREAM_CODEC).cacheEncoding());

    //Specific Enchantments
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>> SOUL_DAMAGE = MFTE_ENCHANTMENT_EFFECT_COMPONENTS.registerComponentType(
            "soul_damage", builder -> builder.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>> MANA_USE = MFTE_ENCHANTMENT_EFFECT_COMPONENTS.registerComponentType(
            "mana_use", builder -> builder.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));

}
