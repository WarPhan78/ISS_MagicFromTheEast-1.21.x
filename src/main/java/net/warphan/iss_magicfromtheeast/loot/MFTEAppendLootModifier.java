package net.warphan.iss_magicfromtheeast.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * PORT 1.20.1: global loot modifiers on 1.20.1 use a plain {@link Codec} (no MapCodec) and loot
 * tables are resolved through the LootDataManager (mirrors ISS 1.20.1 AppendLootModifier).
 */
public class MFTEAppendLootModifier extends LootModifier {
    public static final Supplier<Codec<MFTEAppendLootModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(builder -> codecStart(builder).and(
            Codec.STRING.fieldOf("key").forGetter(m -> m.resourceLocationKey)).apply(builder, MFTEAppendLootModifier::new)));
    private final String resourceLocationKey;

    protected MFTEAppendLootModifier(LootItemCondition[] conditions, String resourceLocationKey) {
        super(conditions);
        this.resourceLocationKey = resourceLocationKey;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ISS_MagicFromTheEast.LOGGER.debug("AppendLootModifier.doApply {}", resourceLocationKey);
        ResourceLocation path = new ResourceLocation(resourceLocationKey);
        var lootTable = context.getLevel().getServer().getLootData().getLootTable(path);
        ObjectArrayList<ItemStack> objectarraylist = new ObjectArrayList<>();
        lootTable.getRandomItemsRaw(context, objectarraylist::add);
        generatedLoot.addAll(objectarraylist);
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
