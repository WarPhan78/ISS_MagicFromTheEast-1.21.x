package net.warphan.iss_magicfromtheeast.item.curios;

import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.warphan.iss_magicfromtheeast.datagen.MFTEDamageTypeTagGenerator;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import top.theillusivec4.curios.api.SlotContext;

// PORT 1.20.1: LivingIncomingDamageEvent -> LivingHurtEvent (the handler rescales the damage).
@Mod.EventBusSubscriber
public class SoulwardRing extends SimpleDescriptiveCurio {
    public SoulwardRing() {
        super(ItemPropertiesHelper.equipment().stacksTo(1), Curios.RING_SLOT);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        slotContext.entity().removeEffect(MFTEEffectRegistries.SOULBURN.get());
    }

    @SubscribeEvent
    public static void soulProtectionEvent(LivingHurtEvent event) {
        var RING = ((SoulwardRing) MFTEItemRegistries.SOULWARD_RING.get());
        var damage = event.getSource();
        if (event.getEntity() instanceof ServerPlayer player) {
            if (RING.isEquippedBy(player) && damage.is(MFTEDamageTypeTagGenerator.SOUL_HURTING)) {
                var damageAmount = event.getAmount();
                event.setAmount(damageAmount * 0.8f);
                return;
            }
        }
    }

}
