package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class MFTESoundRegistries{
    private static final DeferredRegister<SoundEvent> MFTESOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTESOUND_EVENTS.register(eventBus);
    }

    public static DeferredHolder<SoundEvent, SoundEvent> SYMMETRY_CAST = registerSoundEvent("cast.generic.symmetry");
    public static DeferredHolder<SoundEvent, SoundEvent> SPIRIT_CAST = registerSoundEvent("cast.generic.spirit");
    public static DeferredHolder<SoundEvent, SoundEvent> DUNE_CAST = registerSoundEvent("cast.generic.dune");
    public static DeferredHolder<SoundEvent, SoundEvent> SPROING = registerSoundEvent("cast.generic.sproing");
    public static DeferredHolder<SoundEvent, SoundEvent> LOONG_CAST = registerSoundEvent("cast.generic.loong");
    public static DeferredHolder<SoundEvent, SoundEvent> FORCE_PULLING = registerSoundEvent("cast.generic.force_pulling");
    public static DeferredHolder<SoundEvent, SoundEvent> PUSH_BURST = registerSoundEvent("cast.generic.push_burst");
    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SWORD_IMPACT = registerSoundEvent("cast.generic.jade_sword_impact");
    public static DeferredHolder<SoundEvent, SoundEvent> SOUL_CAST = registerSoundEvent("cast.generic.soul_cast");
    public static DeferredHolder<SoundEvent, SoundEvent> BONE_AMBIENT = registerSoundEvent("cast.generic.bone_ambient");
    public static DeferredHolder<SoundEvent, SoundEvent> BONE_HURT = registerSoundEvent("cast.generic.bone_hit");
    public static DeferredHolder<SoundEvent, SoundEvent> BONE_SLAM = registerSoundEvent("cast.generic.bone_slam");
    public static DeferredHolder<SoundEvent, SoundEvent> KATANA_WIND_UP = registerSoundEvent("cast.generic.katana_wind_up");
    public static DeferredHolder<SoundEvent, SoundEvent> SWORD_STRIKE = registerSoundEvent("cast.generic.sword_strike");
    public static DeferredHolder<SoundEvent, SoundEvent> DEATH_BELL = registerSoundEvent("cast.generic.death_bell");

    //JADE EXECUTIONER
    public static DeferredHolder<SoundEvent, SoundEvent> AXE_CHOP = registerSoundEvent("executioner.generic.axe_chop");
    public static DeferredHolder<SoundEvent, SoundEvent> AXE_SWEEP = registerSoundEvent("executioner.generic.axe_sweep");
    public static DeferredHolder<SoundEvent, SoundEvent> EXECUTIONER_AMBIENT = registerSoundEvent("executioner.generic.executioner_ambient");
    public static DeferredHolder<SoundEvent, SoundEvent> EXECUTIONER_HURT = registerSoundEvent("executioner.generic.executioner_hurt");
    public static DeferredHolder<SoundEvent, SoundEvent> ROAR = registerSoundEvent("executioner.generic.roar");
    public static DeferredHolder<SoundEvent, SoundEvent> SHIELD_BASH = registerSoundEvent("executioner.generic.shield_bash");
    public static DeferredHolder<SoundEvent, SoundEvent> EXECUTIONER_SHOWDOWN = registerSoundEvent("executioner.generic.showdown");

    //JADE SENTINEL
//    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_AMBIENT = registerSoundEvent("sentinel.generic.ambient");
//    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_STEP = registerSoundEvent("sentinel.generic.step");
//    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_HURT = registerSoundEvent("sentinel.generic.hurt");
//    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_STAB = registerSoundEvent("sentinel.generic.stab");
//    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_SWEEP = registerSoundEvent("sentinel.generic.sweep");
//    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_STOMP = registerSoundEvent("sentinel.generic.stomp");
//    public static DeferredHolder<SoundEvent, SoundEvent> JADE_SENTINEL_CHARGE = registerSoundEvent("sentinel.generic.charge");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return MFTESOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, name)));
    }
}
