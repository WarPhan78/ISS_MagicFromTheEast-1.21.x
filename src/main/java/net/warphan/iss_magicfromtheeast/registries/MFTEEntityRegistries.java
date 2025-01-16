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
import net.warphan.iss_magicfromtheeast.entity.spells.bagua_array.BaguaCircle;
import net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide.JadeLoong;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_judgement.JadeDao;

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
}
