package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands.BoneHandsEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner.JadeExecutionerEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi.JiangshiEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi.SummonedJiangshiEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.mfte_wizards.onmyoji.OnmyojiEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.mfte_wizards.taoist.TaoistEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_ashigaru.SpiritAshigaruEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_samurai.SpiritSamuraiEntity;
import net.warphan.iss_magicfromtheeast.entity.spells.anchoring_kunai.AnchoringKunaiProjectile;
import net.warphan.iss_magicfromtheeast.entity.spells.bagua_array.BaguaCircle;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_bullet.JadeBulletProjectile;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_drape.JadeDrapesEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.kitsune.SummonedKitsune;
import net.warphan.iss_magicfromtheeast.entity.spells.nephrite_slash.NephriteCrystalEntity;
import net.warphan.iss_magicfromtheeast.entity.spells.phantom_cavalry.PhantomCavalryVisualEntity;
import net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide.JadeLoong;
import net.warphan.iss_magicfromtheeast.entity.spells.soul_skull.SoulSkullProjectile;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ExtractedSoul;
import net.warphan.iss_magicfromtheeast.entity.spells.splitting_shuriken.SplittingShurikenProjectile;
import net.warphan.iss_magicfromtheeast.entity.spells.summoned_cloud.SummonCloudEntity;
import net.warphan.iss_magicfromtheeast.entity.spells.verdict_circle.VerdictCircle;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_judgement.JadeDao;
import net.warphan.iss_magicfromtheeast.entity.spells.sword_dance.JadeSword;
import net.warphan.iss_magicfromtheeast.entity.spirit_arrow.SpiritArrow;
import net.warphan.iss_magicfromtheeast.entity.spirit_bullet.SpiritBulletProjectile;

public class MFTEEntityRegistries {
    private static final DeferredRegister<EntityType<?>> MFTE_ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTE_ENTITIES.register(eventBus);
    }

    public static final RegistryObject<EntityType<BaguaCircle>> BAGUA_CIRCLE =
            MFTE_ENTITIES.register("bagua_circle", () -> EntityType.Builder.<BaguaCircle>of(BaguaCircle::new, MobCategory.MISC)
                    .sized(10f, 0.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "bagua_circle").toString()));
    public static final RegistryObject<EntityType<JadeLoong>> JADE_LOONG =
            MFTE_ENTITIES.register("jade_loong",() -> EntityType.Builder.<JadeLoong>of(JadeLoong::new, MobCategory.MISC)
                    .sized(1.5f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_loong").toString()));
    public static final RegistryObject<EntityType<JadeDao>> JADE_DAO =
            MFTE_ENTITIES.register("jade_dao", () -> EntityType.Builder.<JadeDao>of(JadeDao::new, MobCategory.MISC)
                    .sized(0.8f, 4f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_dao").toString()));
    public static final RegistryObject<EntityType<JadeSword>> JADE_SWORD =
            MFTE_ENTITIES.register("jade_sword", () -> EntityType.Builder.<JadeSword>of(JadeSword::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_sword").toString()));
    public static final RegistryObject<EntityType<JiangshiEntity>> JIANGSHI =
            MFTE_ENTITIES.register("jiangshi", () -> EntityType.Builder.<JiangshiEntity>of(JiangshiEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jiangshi").toString()));
    public static final RegistryObject<EntityType<SummonedJiangshiEntity>> SUMMONED_JIANGSHI =
            MFTE_ENTITIES.register("summoned_jiangshi", () -> EntityType.Builder.<SummonedJiangshiEntity>of(SummonedJiangshiEntity::new, MobCategory.MISC)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jiangshi").toString()));
    public static final RegistryObject<EntityType<VerdictCircle>> VERDICT_CIRCLE =
            MFTE_ENTITIES.register("impermanence", () -> EntityType.Builder.<VerdictCircle>of(VerdictCircle::new,MobCategory.MISC)
                    .sized(7f, 0.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "impermanence").toString()));
    public static final RegistryObject<EntityType<SoulSkullProjectile>> SOUL_SKULL =
            MFTE_ENTITIES.register("soul_skull", () -> EntityType.Builder.<SoulSkullProjectile>of(SoulSkullProjectile::new, MobCategory.MISC)
                    .sized(.4f, .4f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "soul_skull").toString()));
    public static final RegistryObject<EntityType<ExtractedSoul>> EXTRACTED_SOUL =
            MFTE_ENTITIES.register("extracted_soul", () -> EntityType.Builder.<ExtractedSoul>of(ExtractedSoul::new, MobCategory.MISC)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "extracted_soul").toString()));
    public static final RegistryObject<EntityType<BoneHandsEntity>> BONE_HAND_ENTITY =
            MFTE_ENTITIES.register("bone_hand", () -> EntityType.Builder.<BoneHandsEntity>of(BoneHandsEntity::new, MobCategory.MISC)
                    .sized(0.4f, 8.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "bone_hand").toString()));
    public static final RegistryObject<EntityType<JadeExecutionerEntity>> JADE_EXECUTIONER =
            MFTE_ENTITIES.register("jade_executioner", () -> EntityType.Builder.<JadeExecutionerEntity>of(JadeExecutionerEntity::new, MobCategory.MISC)
                    .sized(2.5f, 5.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_executioner").toString()));
    public static final RegistryObject<EntityType<JadeDrapesEntity>> JADE_DRAPES_ENTITY =
            MFTE_ENTITIES.register("jade_drapes", () -> EntityType.Builder.<JadeDrapesEntity>of(JadeDrapesEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_drapes").toString()));
    public static final RegistryObject<EntityType<SummonCloudEntity>> SUMMON_CLOUD_ENTITY =
            MFTE_ENTITIES.register("summon_cloud", () -> EntityType.Builder.<SummonCloudEntity>of(SummonCloudEntity::new, MobCategory.MISC)
                    .sized(1f, 0.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "summon_cloud").toString()));
    public static final RegistryObject<EntityType<SpiritArrow>> SPIRIT_ARROW =
            MFTE_ENTITIES.register("spirit_arrow", () -> EntityType.Builder.<SpiritArrow>of(SpiritArrow::new, MobCategory.MISC)
                    .sized(.5f, .5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "spirit_arrow").toString()));
    public static final RegistryObject<EntityType<SummonedKitsune>> SUMMONED_KITSUNE =
            MFTE_ENTITIES.register("summoned_kitsune", () -> EntityType.Builder.<SummonedKitsune>of(SummonedKitsune::new, MobCategory.MISC)
                    .sized(.7f, .6f)
                    .immuneTo(Blocks.POWDER_SNOW)
                    .fireImmune()
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "summoned_kitsune").toString()));
    // TODO PORT 1.20.1: EntityType.Builder#eyeHeight does not exist on 1.20.1 - the spirit samurai
    //  eye height (2.3f) must be handled by overriding getStandingEyeHeight in SpiritSamuraiEntity.
    public static final RegistryObject<EntityType<SpiritSamuraiEntity>> REVENANT =
            MFTE_ENTITIES.register("spirit_samurai", () -> EntityType.Builder.<SpiritSamuraiEntity>of(SpiritSamuraiEntity::new, MobCategory.MISC)
                    .sized(.85f, 2.3f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "spirit_samurai").toString()));
    public static final RegistryObject<EntityType<SpiritAshigaruEntity>> ASHIGARU =
            MFTE_ENTITIES.register("spirit_ashigaru", () -> EntityType.Builder.<SpiritAshigaruEntity>of(SpiritAshigaruEntity::new, MobCategory.MISC)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "spirit_ashigaru").toString()));
    public static final RegistryObject<EntityType<SpiritBulletProjectile>> SPIRIT_BULLET =
            MFTE_ENTITIES.register("spirit_bullet", () -> EntityType.Builder.<SpiritBulletProjectile>of(SpiritBulletProjectile::new, MobCategory.MISC)
                    .sized(.1f, .1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "spirit_bullet").toString()));
    public static final RegistryObject<EntityType<PhantomCavalryVisualEntity>> PHANTOM_CAVALRY =
            MFTE_ENTITIES.register("phantom_cavalry", () -> EntityType.Builder.<PhantomCavalryVisualEntity>of(PhantomCavalryVisualEntity::new, MobCategory.MISC)
                    .sized(1.2f, 2.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "phantom_cavalry").toString()));
    public static final RegistryObject<EntityType<AnchoringKunaiProjectile>> ANCHORING_KUNAI =
            MFTE_ENTITIES.register("anchoring_kunai", () -> EntityType.Builder.<AnchoringKunaiProjectile>of(AnchoringKunaiProjectile::new, MobCategory.MISC)
                    .sized(.5f, .5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "anchoring_kunai").toString()));
    public static final RegistryObject<EntityType<SplittingShurikenProjectile>> SPIRIT_SHURIKEN =
            MFTE_ENTITIES.register("spirit_shuriken", () -> EntityType.Builder.<SplittingShurikenProjectile>of(SplittingShurikenProjectile::new, MobCategory.MISC)
                    .sized(.3f, .3f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "spirit_shuriken").toString()));
    public static final RegistryObject<EntityType<NephriteCrystalEntity>> NEPHRITE_CRYSTAL =
            MFTE_ENTITIES.register("nephrite_crystal", () -> EntityType.Builder.<NephriteCrystalEntity>of(NephriteCrystalEntity::new, MobCategory.MISC)
                    .sized(0.4f, 0.7f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "nephrite_crystal").toString()));
    public static final RegistryObject<EntityType<JadeBulletProjectile>> JADE_PROJECTILE =
            MFTE_ENTITIES.register("jade_projectile", () -> EntityType.Builder.<JadeBulletProjectile>of(JadeBulletProjectile::new, MobCategory.MISC)
                    .sized(.4f, .4f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jade_projectile").toString()));

    //MFTE WIZARDS
    public static final RegistryObject<EntityType<TaoistEntity>> TAOIST =
            MFTE_ENTITIES.register("taoist", () -> EntityType.Builder.<TaoistEntity>of(TaoistEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "taoist").toString()));
    public static final RegistryObject<EntityType<OnmyojiEntity>> ONMYOJI =
            MFTE_ENTITIES.register("onmyoji", () -> EntityType.Builder.<OnmyojiEntity>of(OnmyojiEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "onmyoji").toString()));

    //MFTE BOSSES

}
