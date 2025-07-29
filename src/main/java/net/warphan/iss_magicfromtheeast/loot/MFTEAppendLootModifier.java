package net.warphan.iss_magicfromtheeast.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MFTEAppendLootModifier extends LootModifier {
    public static final Supplier<MapCodec<MFTEAppendLootModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.mapCodec(builder -> codecStart(builder).and(
            Codec.STRING.fieldOf("key").forGetter(m -> m.resourceLocationKey)).apply(builder, MFTEAppendLootModifier::new)));
    private final String resourceLocationKey;

    protected MFTEAppendLootModifier(LootItemCondition[] conditions, String resourceLocationKey) {
        super(conditions);
        this.resourceLocationKey = resourceLocationKey;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ISS_MagicFromTheEast.LOGGER.debug("AppendLootModifier.doApply {}", resourceLocationKey);
        ResourceLocation path = ResourceLocation.parse(resourceLocationKey);
        var lootTable = context.getLevel().getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, path));
        ObjectArrayList<ItemStack> objectarraylist = new ObjectArrayList<>();
        lootTable.getRandomItemsRaw(context, objectarraylist::add);
        generatedLoot.addAll(objectarraylist);
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
