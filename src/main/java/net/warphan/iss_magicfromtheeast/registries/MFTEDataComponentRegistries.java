package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.weapons.RepeatingCrossbow;

import java.util.function.UnaryOperator;

public class MFTEDataComponentRegistries {
    private static final DeferredRegister<DataComponentType<?>> MFTE_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTE_COMPONENTS.register(eventBus);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String pName, UnaryOperator<DataComponentType.Builder<T>> pBuilder) {
        return MFTE_COMPONENTS.register(pName, () -> pBuilder.apply(DataComponentType.builder()).build());
    }

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RepeatingCrossbow.ProjectileAmountComponent>> CROSSBOW_AMMO_AMOUNT = register("crossbow_ammo_amount", (builder) -> builder.persistent(RepeatingCrossbow.ProjectileAmountComponent.CODEC).networkSynchronized(RepeatingCrossbow.ProjectileAmountComponent.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RepeatingCrossbow.LoadingStateComponent>> CROSSBOW_LOADING_STATE = register("crossbow_loading_state", (builder) -> builder.persistent(RepeatingCrossbow.LoadingStateComponent.CODEC).networkSynchronized(RepeatingCrossbow.LoadingStateComponent.STREAM_CODEC).cacheEncoding());

}
