package net.warphan.iss_magicfromtheeast.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.loot.MFTEAppendLootModifier;

public class MFTELootRegistries {
    public static final DeferredRegister<LootItemFunctionType> MFTE_LOOT_FUNCTION = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, ISS_MagicFromTheEast.MOD_ID);
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> MFTE_LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTE_LOOT_FUNCTION.register(eventBus);
        MFTE_LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }

    //why gray?
    // NOTE PORT 1.20.1: MFTEAppendLootModifier.CODEC must be a Supplier<Codec<MFTEAppendLootModifier>> (1.20.1 GLMs use Codec, not MapCodec).
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> MFTE_APPEND_LOOT_MODIFIER = MFTE_LOOT_MODIFIER_SERIALIZERS.register("append_loot", MFTEAppendLootModifier.CODEC);
}
