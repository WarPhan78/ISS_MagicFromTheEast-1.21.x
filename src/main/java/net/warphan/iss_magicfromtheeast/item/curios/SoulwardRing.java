package net.warphan.iss_magicfromtheeast.item.curios;

import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.item.ItemStack;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import top.theillusivec4.curios.api.SlotContext;

public class SoulwardRing extends SimpleDescriptiveCurio {
    public SoulwardRing() {
        super(ItemPropertiesHelper.equipment().stacksTo(1), Curios.RING_SLOT);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        slotContext.entity().removeEffect(MFTEEffectRegistries.SOULBURN);
    }
}
