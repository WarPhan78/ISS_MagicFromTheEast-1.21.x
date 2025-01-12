package net.warphan.iss_magicfromtheeast.setup;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.bagua_array.BaguaCircleRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.dragon_glide.JadeLoongRenderer;
import net.warphan.iss_magicfromtheeast.entity.spells.force_sword.SummonedSwordRenderer;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;

@EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MFTEEntityRegistries.FORCE_SWORD.get(), SummonedSwordRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.BAGUA_CIRCLE.get(), BaguaCircleRenderer::new);
        event.registerEntityRenderer(MFTEEntityRegistries.JADE_LOONG.get(), JadeLoongRenderer::new);
    }
}
