package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.block.RiceWineVaseBlock;

import java.util.Collection;

public class MFTEBlockRegistries {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

    public static final DeferredHolder<Block, Block> JADE_ORE = BLOCKS.register("jade_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final DeferredHolder<Block, Block> JADE_ORE_DEEPSLATE = BLOCKS.register("deepslate_jade_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)));
    public static final DeferredHolder<Block, Block> RAW_JADE_BLOCK = BLOCKS.register("raw_jade_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> JADE_BLOCK = BLOCKS.register("jade_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE)));
    public static final DeferredHolder<Block, Block> REFINED_JADE_BLOCK = BLOCKS.register("refined_jade_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_BLOCK)));
    public static final DeferredHolder<Block, Block> RICE_WINE_VASE = BLOCKS.register("rice_wine_vase", RiceWineVaseBlock::new);

    public static Collection<DeferredHolder<Block, ? extends Block>> blocks() {
        return BLOCKS.getEntries();
    }
}
