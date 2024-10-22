package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class MFTESoundRegistries extends SoundRegistry {
    private static final DeferredRegister<SoundEvent> MFTESOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTESOUND_EVENTS.register(eventBus);
    }

    public static DeferredHolder<SoundEvent, SoundEvent> SYMMETRY_CAST = registerSoundEvent("cast.generic.symmetry");
    public static DeferredHolder<SoundEvent, SoundEvent> SPIRIT_CAST = registerSoundEvent("cast.generic.spirit");
    public static DeferredHolder<SoundEvent, SoundEvent> DUNE_CAST = registerSoundEvent("cast.generic.dune");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return MFTESOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, name)));
    }
}
