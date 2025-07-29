package net.warphan.iss_magicfromtheeast.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

public class MFTEBannerPatterns {
    public static final ResourceKey<BannerPattern> BALANCE = ResourceKey.create(Registries.BANNER_PATTERN,
            ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "balance"));

    public static void bootstrap(BootstrapContext<BannerPattern> context) {
        register(context, BALANCE);
    }

    public static void register(BootstrapContext<BannerPattern> pattern, ResourceKey<BannerPattern> key) {
        pattern.register(key, new BannerPattern(key.location(), "block.banner." + key.location().toShortLanguageKey()));
    }
}
