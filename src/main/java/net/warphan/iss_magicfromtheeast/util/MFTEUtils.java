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
    public static boolean checkMonsterSpawnRules(ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return !pLevel.getBiome(pPos).is(Tags.Biomes.IS_MUSHROOM) && pLevel.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(pLevel, pPos, pRandom) && Monster.checkMobSpawnRules(MFTEEntityRegistries.JIANGSHI.get(), pLevel, pSpawnType, pPos, pRandom);
    }
}
