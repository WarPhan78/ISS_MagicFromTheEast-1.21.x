package net.warphan.iss_magicfromtheeast.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTEBlockRegistries;

public class MFTEBlockStatesProvider extends BlockStateProvider {
    public MFTEBlockStatesProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, ISS_MagicFromTheEast.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        slabBlock(MFTEBlockRegistries.JADE_SLAB.get(), blockTexture(MFTEBlockRegistries.JADE_BLOCK.get()), blockTexture(MFTEBlockRegistries.JADE_BLOCK.get()));
        slabBlock(MFTEBlockRegistries.JADE_BRICK_SLAB.get(), blockTexture(MFTEBlockRegistries.JADE_BRICK_BLOCK.get()), blockTexture(MFTEBlockRegistries.JADE_BRICK_BLOCK.get()));

        stairsBlock(MFTEBlockRegistries.JADE_STAIR.get(), blockTexture(MFTEBlockRegistries.JADE_BLOCK.get()));
        stairsBlock(MFTEBlockRegistries.JADE_BRICK_STAIR.get(), blockTexture(MFTEBlockRegistries.JADE_BRICK_BLOCK.get()));

        wallBlock(MFTEBlockRegistries.JADE_WALL.get(), blockTexture(MFTEBlockRegistries.JADE_BLOCK.get()));
        wallBlock(MFTEBlockRegistries.JADE_BRICK_WALL.get(), blockTexture(MFTEBlockRegistries.JADE_BRICK_BLOCK.get()));

        //item model
        blockItem(MFTEBlockRegistries.JADE_SLAB);
        blockItem(MFTEBlockRegistries.JADE_STAIR);
        blockItem(MFTEBlockRegistries.JADE_BRICK_SLAB);
        blockItem(MFTEBlockRegistries.JADE_BRICK_STAIR);
    }

//    private void blockWithItem(RegistryObject<? extends Block> deferredBlock) {
//        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
//    }

    private void blockItem(RegistryObject<? extends Block> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("iss_magicfromtheeast:block/" + deferredBlock.getId().getPath()));
    }
}
