package net.warphan.iss_magicfromtheeast.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.particle.JadeSlashParticleOptions;

public class MFTEParticleRegistries {
    public static final DeferredRegister<ParticleType<?>> MFTE_PARTICLE_TYPE = DeferredRegister.create(Registries.PARTICLE_TYPE, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTE_PARTICLE_TYPE.register(eventBus);
    }

    public static final RegistryObject<SimpleParticleType> JADE_SHATTER_PARTICLE = MFTE_PARTICLE_TYPE.register("jade_shatter", () -> new SimpleParticleType(false));

    // NOTE PORT 1.20.1: ParticleType requires a ParticleOptions.Deserializer and a Codec (no StreamCodec/MapCodec on 1.20.1).
    //  JadeSlashParticleOptions must expose DESERIALIZER and CODEC (mirror ISS 1.20.1 ShockwaveParticleOptions).
    public static final RegistryObject<ParticleType<JadeSlashParticleOptions>> JADE_SLASH_PARTICLE = MFTE_PARTICLE_TYPE.register("jade_slash", () -> new ParticleType<JadeSlashParticleOptions>(true, JadeSlashParticleOptions.DESERIALIZER) {
        public Codec<JadeSlashParticleOptions> codec() {
            return JadeSlashParticleOptions.CODEC;
        }
    });
}
