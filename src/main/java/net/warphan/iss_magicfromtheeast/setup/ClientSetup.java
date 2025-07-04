package net.warphan.iss_magicfromtheeast.setup;

import io.redspace.ironsspellbooks.fluids.SimpleClientFluidType;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands.BoneHandsRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner.JadeExecutionerRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi.JiangshiRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.bagua_array.BaguaCircleRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide.JadeLoongRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_drape.JadeDrapesRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.kitsune.SummonedKitsuneRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.soul_skull.SoulSkullRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ChallengedSoulRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.verdict_circle.VerdictCircleRender;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_judgement.JadeDaoRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.sword_dance.JadeSwordRenderer;
import net.warphan.iss_magicfromtheeast.entity.spirit_arrow.SpiritArrowRenderer;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEFluidRegistries;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MFTEEntityRegistries.BAGUA_CIRCLE.get(), BaguaCircleRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.THROW_CIRCLE_ENTITY.get(), NoopRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_LOONG.get(), JadeLoongRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_DAO.get(), JadeDaoRenderer::new);
//      event.registerEntityRenderer(MFTEEntityRegistries.JADE_SENTINEL.get(), JadeSentinelRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.SUMMONED_JIANGSHI.get(), JiangshiRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.PULL_FIELD.get(), NoopRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.PUSH_ZONE.get(), NoopRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_SWORD.get(), JadeSwordRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.VERDICT_CIRCLE.get(), VerdictCircleRender::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_EXECUTIONER.get(), JadeExecutionerRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_DRAPES_ENTITY.get(), JadeDrapesRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.SUMMON_CLOUD_ENTITY.get(), NoopRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.SUMMONED_KITSUNE.get(), SummonedKitsuneRenderer::new);

        event.registerEntityRenderer(MFTEEntityRegistries.SOUL_SKULL.get(), SoulSkullRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.CHALLENGING_SOUL.get(), ChallengedSoulRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.BONE_HAND_ENTITY.get(), BoneHandsRenderer::new);

        event.registerEntityRenderer(MFTEEntityRegistries.SPIRIT_ARROW.get(), SpiritArrowRenderer::new);

        CuriosRendererRegistry.register(MFTEItemRegistries.RITUAL_ORIHON.get(), SpellBookCurioRenderer::new);

        CuriosRendererRegistry.register(MFTEItemRegistries.BAGUA_MIRROR.get(), MFTEBeltCurioRenderer::new);
        CuriosRendererRegistry.register(MFTEItemRegistries.COINS_SWORD.get(), MFTEBeltCurioRenderer::new);
        CuriosRendererRegistry.register(MFTEItemRegistries.RUSTED_COINS_SWORD.get(), MFTEBeltCurioRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new SimpleClientFluidType(ISS_MagicFromTheEast.id("block/soul")), MFTEFluidRegistries.SOUL_TYPE);
    }
}
