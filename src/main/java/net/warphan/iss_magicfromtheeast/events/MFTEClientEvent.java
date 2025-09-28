package net.warphan.iss_magicfromtheeast.events;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import net.warphan.iss_magicfromtheeast.setup.MFTEHeartTypes;

@EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, value =  Dist.CLIENT)
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

    @SubscribeEvent
    public static void effectChangeHeartType(PlayerHeartTypeEvent event) {
        var player = event.getEntity();
        if (player.hasEffect(MFTEEffectRegistries.REVERSAL_HEALING)) {
            event.setType(MFTEHeartTypes.JADE_HEART_PROXY.getValue());
        } else if (player.hasEffect(MFTEEffectRegistries.SOULBURN)) {
            event.setType(MFTEHeartTypes.SOUL_HEART_PROXY.getValue());
        }
    }
}
