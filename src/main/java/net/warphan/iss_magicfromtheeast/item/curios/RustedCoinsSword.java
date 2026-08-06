package net.warphan.iss_magicfromtheeast.item.curios;

import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;
import net.warphan.iss_magicfromtheeast.setup.MFTERarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * PORT 1.20.1: the belt attributes come from CurioBaseItem#withAttributes in MFTEItemRegistries
 * (same as 1.21); the 1.21 AFFINITY_COMPONENT data component -> ISS 3.16.2 AffinityData NBT
 * (same spell -> +2 levels map).
 */
public class RustedCoinsSword extends SimpleDescriptiveCurio implements IPresetSpellContainer {
    public RustedCoinsSword(Properties properties) {
        super(properties.rarity(MFTERarity.BLOODFUL));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        var affinity = AffinityData.getAffinityData(stack);
        if (!affinity.affinityData().isEmpty()) {
            int i = TooltipsUtils.indexOfComponent(components, "tooltip.irons_spellbooks.spellbook_spell_count");
            components.addAll(i < 0 ? components.size() : i + 1, affinity.getDescriptionComponent());
        }
    }

    @Override
    public void initializeSpellContainer(ItemStack stack) {
        if (stack == null) {
            return;
        }

        AffinityData.set(stack, new AffinityData(Map.of(
                MFTESpellRegistries.JIANGSHI_INVOKE_SPELL.get().getSpellResource(), 2
        )));
    }
}
