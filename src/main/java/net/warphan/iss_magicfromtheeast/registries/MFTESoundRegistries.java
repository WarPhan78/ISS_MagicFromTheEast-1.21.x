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
    public static DeferredHolder<SoundEvent, SoundEvent> SPROING = registerSoundEvent("cast.generic.sproing");
    public static DeferredHolder<SoundEvent, SoundEvent> LOONG_CAST = registerSoundEvent("cast.generic.loong");

    //JADE SENTINEL
    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_AMBIENT = registerSoundEvent("sentinel.generic.ambient");
    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_STEP = registerSoundEvent("sentinel.generic.step");
    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_HURT = registerSoundEvent("sentinel.generic.hurt");
    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_STAB = registerSoundEvent("sentinel.generic.stab");
    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_SWEEP = registerSoundEvent("sentinel.generic.sweep");
    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_STOMP = registerSoundEvent("sentinel.generic.stomp");
    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_CHARGE = registerSoundEvent("sentinel.generic.charge");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return MFTESOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, name)));
    }
}
