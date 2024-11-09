package net.warphan.iss_magicfromtheeast.setup;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.entity.spells.force_sword.SummonedSword;
import net.warphan.iss_magicfromtheeast.registries.MFTEEntityRegistries;

@EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModSetup {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(MFTEEntityRegistries.FORCE_SWORD.get(), SummonedSword.prepareAttributes());
    }
}
