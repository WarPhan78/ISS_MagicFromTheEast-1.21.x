package net.warphan.iss_magicfromtheeast.setup;

import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.mobs.jade_sentinel.JadeSentinelRenderer;
import net.warphan.iss_magicfromtheeast.entity.mobs.jiangshi.JiangshiRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.bagua_array.BaguaCircleRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide.JadeLoongRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.verdict_circle.VerdictCircleRender;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_judgement.JadeDaoRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.sword_dance.JadeSwordRenderer;
import net.warphan.iss_magicfromtheeast.registries.ItemRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MFTEEntityRegistries.BAGUA_CIRCLE.get(), BaguaCircleRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.THROW_CIRCLE_ENTITY.get(), NoopRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_LOONG.get(), JadeLoongRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_DAO.get(), JadeDaoRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_SENTINEL.get(), JadeSentinelRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.SUMMONED_JIANGSHI.get(), JiangshiRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.PULL_FIELD.get(), NoopRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_SWORD.get(), JadeSwordRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.VERDICT_CIRCLE.get(), VerdictCircleRender::new);

        CuriosRendererRegistry.register(ItemRegistries.RITUAL_ORIHON.get(), SpellBookCurioRenderer::new);
    }
}
