package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.jade_sentinel.JadeSentinel;
import net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi.SummonedJiangshi;
import net.warphan.iss_magicfromtheeast.entity.spells.bagua_array.BaguaCircle;
import net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide.JadeLoong;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_judgement.JadeDao;
import net.warphan.iss_magicfromtheeast.entity.spells.qigong_controlling.PullPushField;
import net.warphan.iss_magicfromtheeast.entity.spells.sword_dance.JadeSword;
import net.warphan.iss_magicfromtheeast.entity.spells.throw_circle.ThrowCircleEntity;

public class MFTEEntityRegistries extends EntityRegistry {
    private static final DeferredRegister<EntityType<?>> MFTE_ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTE_ENTITIES.register(eventBus);
    }

    public static final DeferredHolder<EntityType<?>, EntityType<BaguaCircle>> BAGUA_CIRCLE =
            MFTE_ENTITIES.register("bagua_circle", () -> EntityType.Builder.<BaguaCircle>of(BaguaCircle::new, MobCategory.MISC)
                    .sized(10f, 0.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "bagua_circle").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<JadeLoong>> JADE_LOONG =
            MFTE_ENTITIES.register("jade_loong",() -> EntityType.Builder.<JadeLoong>of(JadeLoong::new, MobCategory.MISC)
                    .sized(1.5f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_loong").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<JadeDao>> JADE_DAO =
            MFTE_ENTITIES.register("jade_dao", () -> EntityType.Builder.<JadeDao>of(JadeDao::new, MobCategory.MISC)
                    .sized(0.8f, 4f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_dao").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<JadeSentinel>> JADE_SENTINEL =
            MFTE_ENTITIES.register("jade_sentinel", () -> EntityType.Builder.<JadeSentinel>of(JadeSentinel::new, MobCategory.MONSTER)
                    .sized(4f, 12f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_sentinel").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowCircleEntity>> THROW_CIRCLE_ENTITY =
            MFTE_ENTITIES.register("throw_circle", () -> EntityType.Builder.<ThrowCircleEntity>of(ThrowCircleEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.0f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "throw_circle").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PullPushField>> PULL_FIELD =
            MFTE_ENTITIES.register("pull_field", () -> EntityType.Builder.<PullPushField>of(PullPushField::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "pull_field").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<JadeSword>> JADE_SWORD =
            MFTE_ENTITIES.register("jade_sword", () -> EntityType.Builder.<JadeSword>of(JadeSword::new, MobCategory.MISC)
                    .sized(0.3f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_sword").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedJiangshi>> SUMMONED_JIANGSHI =
            MFTE_ENTITIES.register("summoned_jiangshi", () -> EntityType.Builder.<SummonedJiangshi>of(SummonedJiangshi::new, MobCategory.MISC)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "summoned_jiangshi").toString()));
}
