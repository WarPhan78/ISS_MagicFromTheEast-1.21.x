package net.warphan.iss_magicfromtheeast.util;

import net.minecraftforge.common.Tags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;

public class MFTEUtils {
    // TODO PORT 1.20.1: the EnchantmentEffectComponents helper (custom DeferredRegister for
    //  enchantment effect component types) was removed - enchantment effect components do not exist
    //  on 1.20.1. The related logic lives in the Enchantment classes (see enchantment package).

    public static boolean checkMonsterSpawnRules(ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        // NOTE PORT 1.20.1: a tag neoforge:no_default_monsters nao existe no Forge 1.20.1 (nem no ISS 3.16.2);
        // Tags.Biomes.IS_MUSHROOM cobre o caso vanilla de bioma sem spawn de monstros.
        return !pLevel.getBiome(pPos).is(Tags.Biomes.IS_MUSHROOM) && pLevel.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(pLevel, pPos, pRandom) && Monster.checkMobSpawnRules(MFTEEntityRegistries.JIANGSHI.get(), pLevel, pSpawnType, pPos, pRandom);
    }
}
