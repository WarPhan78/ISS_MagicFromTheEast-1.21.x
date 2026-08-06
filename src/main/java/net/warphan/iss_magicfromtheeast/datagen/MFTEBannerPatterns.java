package net.warphan.iss_magicfromtheeast.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

/**
 * TODO PORT 1.20.1: banner patterns are NOT a datapack registry on 1.20.1 - they are a built-in
 * registry that must be registered in code. The 1.21 datagen bootstrap was replaced by this
 * DeferredRegister; ISS_MagicFromTheEast must call {@code MFTEBannerPatterns.register(modEventBus)}
 * during mod construction (the generated data/&lt;ns&gt;/banner_pattern/ JSON was deleted).
 * Tooltip translation keys on 1.20.1 are "block.minecraft.banner.iss_magicfromtheeast.balance.&lt;color&gt;".
 */
public class MFTEBannerPatterns {
    public static final DeferredRegister<BannerPattern> BANNER_PATTERNS =
            DeferredRegister.create(Registries.BANNER_PATTERN, ISS_MagicFromTheEast.MOD_ID);

    public static final ResourceKey<BannerPattern> BALANCE = ResourceKey.create(Registries.BANNER_PATTERN,
            new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "balance"));

    // On 1.20.1 the "hashname" is what gets written into banner NBT - keep it stable and unique.
    public static final RegistryObject<BannerPattern> BALANCE_PATTERN =
            BANNER_PATTERNS.register("balance", () -> new BannerPattern("mfte_balance"));

    public static void register(IEventBus eventBus) {
        BANNER_PATTERNS.register(eventBus);
    }
}
