package net.warphan.iss_magicfromtheeast.setup;

import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands.BoneHandsRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner.JadeExecutionerRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi.JiangshiRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.mfte_wizards.onmyoji.OnmyojiRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.mfte_wizards.taoist.TaoistRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_ashigaru.SpiritAshigaruRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_samurai.SpiritSamuraiRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.anchoring_kunai.AnchoringKunaiRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.bagua_array.BaguaCircleRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide.JadeLoongRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_bullet.JadeBulletRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_drape.JadeDrapesRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.kitsune.SummonedKitsuneRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.nephrite_slash.NephriteCrystalRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.phantom_cavalry.PhantomCavalryRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.soul_skull.SoulSkullRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ExtractedSoulRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.splitting_shuriken.SplittingShurikenRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.verdict_circle.VerdictCircleRender;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_judgement.JadeDaoRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.sword_dance.JadeSwordRenderer;
import net.warphan.iss_magicfromtheeast.entity.spirit_arrow.SpiritArrowRenderer;
import net.warphan.iss_magicfromtheeast.particle.JadeShatterParticle;
import net.warphan.iss_magicfromtheeast.particle.JadeSlashParticle;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEParticleRegistries;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MFTEParticleRegistries.JADE_SHATTER_PARTICLE.get(), JadeShatterParticle.Provider::new);
        event.registerSpriteSet(MFTEParticleRegistries.JADE_SLASH_PARTICLE.get(), JadeSlashParticle.Provider::new);
    }

    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MFTEEntityRegistries.BAGUA_CIRCLE.get(), BaguaCircleRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_LOONG.get(), JadeLoongRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_DAO.get(), JadeDaoRenderer::new);
//      event.registerEntityRenderer(MFTEEntityRegistries.JADE_SENTINEL.get(), JadeSentinelRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JIANGSHI.get(), JiangshiRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.SUMMONED_JIANGSHI.get(), JiangshiRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_SWORD.get(), JadeSwordRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.VERDICT_CIRCLE.get(), VerdictCircleRender::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_EXECUTIONER.get(), JadeExecutionerRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_DRAPES_ENTITY.get(), JadeDrapesRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.SUMMON_CLOUD_ENTITY.get(), NoopRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.SUMMONED_KITSUNE.get(), SummonedKitsuneRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.NEPHRITE_CRYSTAL.get(), NephriteCrystalRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_PROJECTILE.get(), JadeBulletRenderer::new);

        event.registerEntityRenderer(MFTEEntityRegistries.TAOIST.get(), TaoistRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.ONMYOJI.get(), OnmyojiRenderer::new);

        event.registerEntityRenderer(MFTEEntityRegistries.SOUL_SKULL.get(), SoulSkullRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.EXTRACTED_SOUL.get(), ExtractedSoulRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.BONE_HAND_ENTITY.get(), BoneHandsRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.REVENANT.get(), SpiritSamuraiRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.ASHIGARU.get(), SpiritAshigaruRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.PHANTOM_CAVALRY.get(), PhantomCavalryRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.ANCHORING_KUNAI.get(), AnchoringKunaiRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.SPIRIT_SHURIKEN.get(), SplittingShurikenRenderer::new);

        event.registerEntityRenderer(MFTEEntityRegistries.SPIRIT_ARROW.get(), SpiritArrowRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.SPIRIT_BULLET.get(), NoopRenderer::new);

        CuriosRendererRegistry.register(MFTEItemRegistries.RITUAL_ORIHON.get(), SpellBookCurioRenderer::new);

        CuriosRendererRegistry.register(MFTEItemRegistries.BAGUA_MIRROR.get(), MFTEBeltCurioRenderer::new);
        CuriosRendererRegistry.register(MFTEItemRegistries.COINS_SWORD.get(), MFTEBeltCurioRenderer::new);
        CuriosRendererRegistry.register(MFTEItemRegistries.RUSTED_COINS_SWORD.get(), MFTEBeltCurioRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(NephriteCrystalRenderer.NephriteCrystalModel.LAYER_LOCATION, NephriteCrystalRenderer.NephriteCrystalModel::createBodyLayer);
        event.registerLayerDefinition(JadeBulletRenderer.MODEL_LAYER_LOCATION, JadeBulletRenderer::createBodyLayer);
    }

    // TODO PORT 1.20.1: RegisterClientExtensionsEvent does not exist on Forge 1.20.1.
    //  The soul fluid client texture (formerly ISS SimpleClientFluidType registered here) is now
    //  provided via FluidType#initializeClient inside MFTEFluidRegistries.SOUL_TYPE.

    @SubscribeEvent
    public static void registerClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ClientLoadableWeaponTooltip.LoadableWeaponTooltipComponent.class, ClientLoadableWeaponTooltip::new);
    }
}
