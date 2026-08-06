package net.warphan.iss_magicfromtheeast.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;

@Mod.EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, value = Dist.CLIENT)
public class MFTEClientEvent {

    @SubscribeEvent
    public static void onComputeFovModifierEvent(ComputeFovModifierEvent event) {
        if(event.getPlayer().isUsingItem() && event.getPlayer().getUseItem().getItem() == MFTEItemRegistries.SOULPIERCER.get()) {
            float fovModifier = 1f;
            int ticksUsingItem = event.getPlayer().getTicksUsingItem();
            float deltaTicks = (float)ticksUsingItem / 20f;
            if(deltaTicks > 1f) {
                deltaTicks = 1f;
            } else {
                deltaTicks *= deltaTicks;
            }
            fovModifier *= 1f - deltaTicks * 0.15f;
            event.setNewFovModifier(fovModifier);
        }
    }

    // PORT 1.20.1: PlayerHeartTypeEvent does not exist on Forge 1.20.1. The jade/soul heart
    //  visuals are reimplemented in MFTEHeartOverlayHandler (custom PLAYER_HEALTH overlay,
    //  Overflowing Bars approach). Original 1.21 handler kept below for reference.
    /*
    @SubscribeEvent
    public static void effectChangeHeartType(PlayerHeartTypeEvent event) {
        var player = event.getEntity();
        if (player.hasEffect(MFTEEffectRegistries.REVERSAL_HEALING)) {
            event.setType(MFTEHeartTypes.JADE_HEART_PROXY.getValue());
        } else if (player.hasEffect(MFTEEffectRegistries.SOULBURN)) {
            event.setType(MFTEHeartTypes.SOUL_HEART_PROXY.getValue());
        }
    }
    */
}
