package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.block.MFTEDirectionalBlock;
import net.warphan.iss_magicfromtheeast.block.RiceWineVaseBlock;

import java.util.Collection;

public class MFTEBlockRegistries {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

    public static final RegistryObject<Block> JADE_ORE = BLOCKS.register("jade_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE), UniformInt.of(0, 3)));
    public static final RegistryObject<Block> JADE_ORE_DEEPSLATE = BLOCKS.register("deepslate_jade_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE), UniformInt.of(1, 3)));

    public static final RegistryObject<Block> JADE_BLOCK = BLOCKS.register("jade_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f)));
    public static final RegistryObject<SlabBlock> JADE_SLAB = BLOCKS.register("jade_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(MFTEBlockRegistries.JADE_BLOCK.get())));
    public static final RegistryObject<StairBlock> JADE_STAIR = BLOCKS.register("jade_stair",() -> new StairBlock(() -> MFTEBlockRegistries.JADE_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(MFTEBlockRegistries.JADE_BLOCK.get())));
    public static final RegistryObject<WallBlock> JADE_WALL = BLOCKS.register("jade_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(MFTEBlockRegistries.JADE_BLOCK.get())));
    public static final RegistryObject<Block> CHISELED_JADE = BLOCKS.register("chiseled_jade", () -> new Block(BlockBehaviour.Properties.copy(MFTEBlockRegistries.JADE_BLOCK.get())));
    public static final RegistryObject<Block> JADE_LION_HEAD_BLOCK = BLOCKS.register("jade_lion_head_block", () -> new MFTEDirectionalBlock(BlockBehaviour.Properties.copy(MFTEBlockRegistries.JADE_BLOCK.get())));

    public static final RegistryObject<Block> JADE_BRICK_BLOCK = BLOCKS.register("jadestone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f)));
    public static final RegistryObject<SlabBlock> JADE_BRICK_SLAB = BLOCKS.register("jadestone_bricks_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(MFTEBlockRegistries.JADE_BRICK_BLOCK.get())));
    public static final RegistryObject<StairBlock> JADE_BRICK_STAIR = BLOCKS.register("jadestone_bricks_stair",() -> new StairBlock(() -> MFTEBlockRegistries.JADE_BRICK_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(MFTEBlockRegistries.JADE_BRICK_BLOCK.get())));
    public static final RegistryObject<WallBlock> JADE_BRICK_WALL = BLOCKS.register("jadestone_bricks_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(MFTEBlockRegistries.JADE_BRICK_BLOCK.get())));
    public static final RegistryObject<Block> CHISELED_JADE_BRICK = BLOCKS.register("chiseled_jadestone_bricks", () -> new Block(BlockBehaviour.Properties.copy(MFTEBlockRegistries.JADE_BRICK_BLOCK.get())));
    public static final RegistryObject<Block> JADE_BRICK_PILLAR = BLOCKS.register("jadestone_bricks_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(MFTEBlockRegistries.JADE_BRICK_BLOCK.get())));

    public static final RegistryObject<Block> REFINED_JADE_BLOCK = BLOCKS.register("refined_jade_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.EMERALD_BLOCK)));

    public static final RegistryObject<Block> RICE_WINE_VASE = BLOCKS.register("rice_wine_vase", RiceWineVaseBlock::new);

    public static Collection<RegistryObject<Block>> blocks() {
        return BLOCKS.getEntries();
    }
}
