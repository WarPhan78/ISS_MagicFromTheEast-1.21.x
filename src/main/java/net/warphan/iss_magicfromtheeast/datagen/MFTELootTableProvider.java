package net.warphan.iss_magicfromtheeast.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;
import net.warphan.iss_magicfromtheeast.registries.MFTEBlockRegistries;

import java.util.HashSet;
import java.util.Set;

public class MFTELootTableProvider {
    static class Block extends BlockLootSubProvider {
        HashSet<net.minecraft.world.level.block.Block> knowBlocks = new HashSet<>();

        public Block() {
            // 1.20.1: BlockLootSubProvider has no HolderLookup.Provider parameter
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            this.add(MFTEBlockRegistries.JADE_SLAB.get(), block -> this.createSlabItemTable(MFTEBlockRegistries.JADE_SLAB.get()));
            this.add(MFTEBlockRegistries.JADE_STAIR.get(), block -> this.createSingleItemTable(MFTEBlockRegistries.JADE_STAIR.get()));
            this.add(MFTEBlockRegistries.JADE_WALL.get(), block -> this.createSingleItemTable(MFTEBlockRegistries.JADE_WALL.get()));
            this.add(MFTEBlockRegistries.CHISELED_JADE.get(), block -> this.createSingleItemTable(MFTEBlockRegistries.CHISELED_JADE.get()));
            this.add(MFTEBlockRegistries.JADE_LION_HEAD_BLOCK.get(), block -> this.createSingleItemTable(MFTEBlockRegistries.JADE_LION_HEAD_BLOCK.get()));

            this.add(MFTEBlockRegistries.JADE_BRICK_SLAB.get(), block -> this.createSlabItemTable(MFTEBlockRegistries.JADE_BRICK_SLAB.get()));
            this.add(MFTEBlockRegistries.JADE_BRICK_STAIR.get(), block -> this.createSingleItemTable(MFTEBlockRegistries.JADE_BRICK_STAIR.get()));
            this.add(MFTEBlockRegistries.JADE_BRICK_WALL.get(), block -> this.createSingleItemTable(MFTEBlockRegistries.JADE_BRICK_WALL.get()));
            this.add(MFTEBlockRegistries.CHISELED_JADE_BRICK.get(), block -> this.createSingleItemTable(MFTEBlockRegistries.CHISELED_JADE_BRICK.get()));
            this.add(MFTEBlockRegistries.JADE_BRICK_PILLAR.get(), block -> this.createSingleItemTable(MFTEBlockRegistries.JADE_BRICK_PILLAR.get()));
        }

        @Override
        protected void add(net.minecraft.world.level.block.Block p_250610_, LootTable.Builder p_249817_) {
            knowBlocks.add(p_250610_);
            super.add(p_250610_, p_249817_);
        }

        @Override
        protected Iterable<net.minecraft.world.level.block.Block> getKnownBlocks() {
            return knowBlocks;
        }
    }
}
