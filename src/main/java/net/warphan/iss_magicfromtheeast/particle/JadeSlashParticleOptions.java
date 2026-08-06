package net.warphan.iss_magicfromtheeast.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.warphan.iss_magicfromtheeast.registries.MFTEParticleRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.util.Locale;

/**
 * PORT 1.20.1: StreamCodec/MapCodec-based particle options do not exist on 1.20.1.
 * Rewritten in the ISS 1.20.1 ShockwaveParticleOptions pattern: {@link #DESERIALIZER} +
 * {@link #CODEC} + {@link #writeToNetwork}/{@link #writeToString}.
 */
public class JadeSlashParticleOptions implements ParticleOptions {
    public final float scale;
    public final float xf;
    public final float yf;
    public final float zf;
    public final float xu;
    public final float yu;
    public final float zu;

    public JadeSlashParticleOptions(float xf, float yf, float zf, float xu, float yu, float zu, float scale) {
        this.scale = scale;
        this.xf = xf;
        this.yf = yf;
        this.zf = zf;
        this.xu = xu;
        this.yu = yu;
        this.zu = zu;
    }

    public static final Codec<JadeSlashParticleOptions> CODEC = RecordCodecBuilder.create(object ->
            object.group(
                    Codec.FLOAT.fieldOf("xf").forGetter(p -> p.xf),
                    Codec.FLOAT.fieldOf("yf").forGetter(p -> p.yf),
                    Codec.FLOAT.fieldOf("zf").forGetter(p -> p.zf),
                    Codec.FLOAT.fieldOf("xu").forGetter(p -> p.xu),
                    Codec.FLOAT.fieldOf("yu").forGetter(p -> p.yu),
                    Codec.FLOAT.fieldOf("zu").forGetter(p -> p.zu),
                    Codec.FLOAT.fieldOf("scale").forGetter(p -> p.scale)
            ).apply(object, JadeSlashParticleOptions::new
            ));

    @SuppressWarnings("deprecation")
    public static final Deserializer<JadeSlashParticleOptions> DESERIALIZER = new Deserializer<JadeSlashParticleOptions>() {
        public @NotNull JadeSlashParticleOptions fromCommand(@NotNull ParticleType<JadeSlashParticleOptions> particleType, @NotNull StringReader reader) throws CommandSyntaxException {
            float xf = readFloat(reader);
            float yf = readFloat(reader);
            float zf = readFloat(reader);
            float xu = readFloat(reader);
            float yu = readFloat(reader);
            float zu = readFloat(reader);
            float scale = readFloat(reader);
            return new JadeSlashParticleOptions(xf, yf, zf, xu, yu, zu, scale);
        }

        private float readFloat(StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return reader.readFloat();
        }

        public @NotNull JadeSlashParticleOptions fromNetwork(@NotNull ParticleType<JadeSlashParticleOptions> particleType, @NotNull FriendlyByteBuf buf) {
            return new JadeSlashParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    };

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(this.xf);
        buf.writeFloat(this.yf);
        buf.writeFloat(this.zf);
        buf.writeFloat(this.xu);
        buf.writeFloat(this.yu);
        buf.writeFloat(this.zu);
        buf.writeFloat(this.scale);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT, "%.2f %.2f %.2f %.2f %.2f %.2f %.2f", this.xf, this.yf, this.zf, this.xu, this.yu, this.zu, this.scale);
    }

    public @Nonnull ParticleType<JadeSlashParticleOptions> getType() {
        return MFTEParticleRegistries.JADE_SLASH_PARTICLE.get();
    }
}
