package net.warphan.iss_magicfromtheeast.setup;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.configs.MFTEServerConfigs;
import net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands.BoneHandsEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner.JadeExecutionerEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi.JiangshiEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi.SummonedJiangshiEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.kitsune.SummonedKitsune;
import net.warphan.iss_magicfromtheeast.entity.mobs.mfte_wizards.onmyoji.OnmyojiEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.mfte_wizards.taoist.TaoistEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_ashigaru.SpiritAshigaruEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_samurai.SpiritSamuraiEntity;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ExtractedSoul;
import net.warphan.iss_magicfromtheeast.entity.spells.summoned_cloud.SummonCloudEntity;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import net.warphan.iss_magicfromtheeast.util.MFTEUtils;

@Mod.EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {
    @SubscribeEvent
    public static void onModConfigLoadingEvent(ModConfigEvent.Loading event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            MFTESpellRegistries.onConfigReload();
            MFTEServerConfigs.onConfigReload();
        }
    }

    @SubscribeEvent
    public static void onModConfigReloadingEvent(ModConfigEvent.Reloading event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            MFTESpellRegistries.onConfigReload();
            MFTEServerConfigs.onConfigReload();
        }
    }

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
//        event.put(MFTEEntityRegistries.JADE_SENTINEL.get(), JadeSentinel.prepareAttributes().build());
        event.put(MFTEEntityRegistries.JIANGSHI.get(), JiangshiEntity.prepareAttributes().build());
        event.put(MFTEEntityRegistries.SUMMONED_JIANGSHI.get(), SummonedJiangshiEntity.prepareAttributes().build());
        event.put(MFTEEntityRegistries.JADE_EXECUTIONER.get(), JadeExecutionerEntity.prepareAttributes().build());
        event.put(MFTEEntityRegistries.SUMMON_CLOUD_ENTITY.get(), SummonCloudEntity.createAttributes().build());

        event.put(MFTEEntityRegistries.EXTRACTED_SOUL.get(), ExtractedSoul.prepareAttributes().build());
        event.put(MFTEEntityRegistries.BONE_HAND_ENTITY.get(), BoneHandsEntity.prepareAttributes().build());
        event.put(MFTEEntityRegistries.SUMMONED_KITSUNE.get(), SummonedKitsune.prepareAttributes().build());
        event.put(MFTEEntityRegistries.REVENANT.get(), SpiritSamuraiEntity.prepareAttributes().build());
        event.put(MFTEEntityRegistries.ASHIGARU.get(), SpiritAshigaruEntity.prepareAttributes().build());

        event.put(MFTEEntityRegistries.TAOIST.get(), TaoistEntity.prepareAttributes().build());
        event.put(MFTEEntityRegistries.ONMYOJI.get(), OnmyojiEntity.prepareAttributes().build());
    }

    @SubscribeEvent
    public static void spawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(MFTEEntityRegistries.JIANGSHI.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, serverLevelAccessor, spawnType, blockPos, random) -> MFTEUtils.checkMonsterSpawnRules(serverLevelAccessor, spawnType, blockPos, random), SpawnPlacementRegisterEvent.Operation.OR);
    }
}
