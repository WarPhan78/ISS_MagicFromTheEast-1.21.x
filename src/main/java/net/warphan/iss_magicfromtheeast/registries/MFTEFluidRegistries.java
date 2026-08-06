package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

import java.util.function.Consumer;

public class MFTEFluidRegistries {
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, ISS_MagicFromTheEast.MOD_ID);
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }

    // TODO PORT 1.20.1: RegisterClientExtensionsEvent does not exist on Forge 1.20.1 - the client
    //  texture (formerly ISS SimpleClientFluidType, registered in ClientSetup) is now supplied via
    //  FluidType#initializeClient below.
    public static final RegistryObject<FluidType> SOUL_TYPE = FLUID_TYPES.register("soul", () -> new FluidType(FluidType.Properties.create()) {
        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                private final ResourceLocation texture = ISS_MagicFromTheEast.id("block/soul");

                @Override
                public ResourceLocation getStillTexture() {
                    return texture;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return texture;
                }
            });
        }
    });

    public static final RegistryObject<Fluid> SOUL = FLUIDS.register("soul", () -> new NoopFluid(
            new ForgeFlowingFluid.Properties(SOUL_TYPE, () -> MFTEFluidRegistries.SOUL.get(), () -> MFTEFluidRegistries.SOUL.get())
                    .bucket(() -> Items.AIR)));

    /**
     * TODO PORT 1.20.1: ISS 1.20.1 has no NoopFluid/BaseFlowingFluid - minimal local port of the
     * ISS 1.21 NoopFluid on top of Forge's ForgeFlowingFluid.
     */
    public static class NoopFluid extends ForgeFlowingFluid {
        public NoopFluid(Properties properties) {
            super(properties);
        }

        @Override
        public Item getBucket() {
            return Items.AIR;
        }

        @Override
        protected BlockState createLegacyBlock(FluidState state) {
            return Blocks.AIR.defaultBlockState();
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        public int getAmount(FluidState state) {
            return 0;
        }
    }
}
