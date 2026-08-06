package net.warphan.iss_magicfromtheeast.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class MFTESoundRegistries{
    private static final DeferredRegister<SoundEvent> MFTESOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTESOUND_EVENTS.register(eventBus);
    }

    public static RegistryObject<SoundEvent> SYMMETRY_CAST = registerSoundEvent("cast.generic.symmetry");
    public static RegistryObject<SoundEvent> SPIRIT_CAST = registerSoundEvent("cast.generic.spirit");
    public static RegistryObject<SoundEvent> DUNE_CAST = registerSoundEvent("cast.generic.dune");

    public static RegistryObject<SoundEvent> LOONG_CAST = registerSoundEvent("cast.generic.loong");
    public static RegistryObject<SoundEvent> JADE_SWORD_IMPACT = registerSoundEvent("cast.generic.jade_sword_impact");
    public static RegistryObject<SoundEvent> JADE_DAO_IMPACT = registerSoundEvent("cast.generic.jade_dao_impact");
    public static RegistryObject<SoundEvent> JADE_DEFLECT = registerSoundEvent("cast.generic.jade_deflect");
    public static RegistryObject<SoundEvent> JADE_SLASH = registerSoundEvent("cast.generic.jade_slash");
    public static RegistryObject<SoundEvent> JADE_EMERGE = registerSoundEvent("cast.generic.jade_emerge");
    public static RegistryObject<SoundEvent> SOUL_CAST = registerSoundEvent("cast.generic.soul_cast");
    public static RegistryObject<SoundEvent> BONE_HURT = registerSoundEvent("cast.generic.bone_hit");
    public static RegistryObject<SoundEvent> BONE_SLAM = registerSoundEvent("cast.generic.bone_slam");
    public static RegistryObject<SoundEvent> KATANA_WIND_UP = registerSoundEvent("cast.generic.katana_wind_up");
    public static RegistryObject<SoundEvent> SWORD_STRIKE = registerSoundEvent("cast.generic.sword_strike");
    public static RegistryObject<SoundEvent> DEATH_BELL = registerSoundEvent("cast.generic.death_bell");
    public static RegistryObject<SoundEvent> MAGIC_PROJECTILE_THROW= registerSoundEvent("cast.generic.projectile_throw");
    public static RegistryObject<SoundEvent> SPIRIT_EXTRACTING = registerSoundEvent("cast.generic.soul_challenging");
    public static RegistryObject<SoundEvent> SPIRIT_ARROW_SHOT = registerSoundEvent("cast.generic.spirit_arrow_shot");
    public static RegistryObject<SoundEvent> PROJECTILE_LOAD = registerSoundEvent("cast.generic.projectile_loading");
    public static RegistryObject<SoundEvent> SPIRIT_INVOKING = registerSoundEvent("cast.generic.soul_rise");
    public static RegistryObject<SoundEvent> SOUL_CRUSHED = registerSoundEvent("cast.generic.soul_crushed");

    //JADE EXECUTIONER
    public static RegistryObject<SoundEvent> AXE_CHOP = registerSoundEvent("executioner.generic.axe_chop");
    public static RegistryObject<SoundEvent> AXE_SWEEP = registerSoundEvent("executioner.generic.axe_sweep");
    public static RegistryObject<SoundEvent> EXECUTIONER_AMBIENT = registerSoundEvent("executioner.generic.executioner_ambient");
    public static RegistryObject<SoundEvent> EXECUTIONER_HURT = registerSoundEvent("executioner.generic.executioner_hurt");
    public static RegistryObject<SoundEvent> ROAR = registerSoundEvent("executioner.generic.roar");
    public static RegistryObject<SoundEvent> EXECUTIONER_SHOWDOWN = registerSoundEvent("executioner.generic.showdown");

    //JADE SENTINEL
//    public static RegistryObject<SoundEvent> JADE_SENTINEL_AMBIENT = registerSoundEvent("sentinel.generic.ambient");
//    public static RegistryObject<SoundEvent> JADE_SENTINEL_STEP = registerSoundEvent("sentinel.generic.step");
//    public static RegistryObject<SoundEvent> JADE_SENTINEL_HURT = registerSoundEvent("sentinel.generic.hurt");
//    public static RegistryObject<SoundEvent> JADE_SENTINEL_STAB = registerSoundEvent("sentinel.generic.stab");
//    public static RegistryObject<SoundEvent> JADE_SENTINEL_SWEEP = registerSoundEvent("sentinel.generic.sweep");
//    public static RegistryObject<SoundEvent> JADE_SENTINEL_STOMP = registerSoundEvent("sentinel.generic.stomp");
//    public static RegistryObject<SoundEvent> JADE_SENTINEL_CHARGE = registerSoundEvent("sentinel.generic.charge");

    //SPIRIT MOB
    public static RegistryObject<SoundEvent> ASHIGARU_SPEAR = registerSoundEvent("spiritmob.generic.ashigaru_spear");
    public static RegistryObject<SoundEvent> ASHIGARU_AMBIENT = registerSoundEvent("spiritmob.generic.ashigaru_voice");
    public static RegistryObject<SoundEvent> ASHIGARU_GUNSHOT = registerSoundEvent("spiritmob.generic.ashigaru_gun");
    public static RegistryObject<SoundEvent> SAMURAI_AMBIENT = registerSoundEvent("spiritmob.generic.samurai_ambient");
    public static RegistryObject<SoundEvent> SAMURAI_SLASH = registerSoundEvent("spiritmob.generic.samurai_slash");
    public static RegistryObject<SoundEvent> SPIRIT_MOB_HURT = registerSoundEvent("spiritmob.generic.spirit_hurt");
    public static RegistryObject<SoundEvent> SPIRIT_MOB_DEATH = registerSoundEvent("spiritmob.generic.spirit_dead");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return MFTESOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, name)));
    }
}
