package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

import java.util.function.Supplier;

@EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MFTEAttributeRegistries {

    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final DeferredHolder<Attribute, Attribute> SYMMETRY_MAGIC_RESIST = newResistanceAttribute("symmetry");
    public static final DeferredHolder<Attribute, Attribute> SPIRIT_MAGIC_RESIST = newResistanceAttribute("spirit");
    public static final DeferredHolder<Attribute, Attribute> DUNE_MAGIC_RESIST = newResistanceAttribute("dune");

    public static final DeferredHolder<Attribute, Attribute> SYMMETRY_SPELL_POWER = newPowerAttribute("symmetry");
    public static final DeferredHolder<Attribute, Attribute> SPIRIT_SPELL_POWER = newPowerAttribute("spirit");
    public static final DeferredHolder<Attribute, Attribute> DUNE_SPELL_POWER = newPowerAttribute("dune");

    public static Holder<Attribute> holder(Supplier<Attribute> attributeSupplier) {
        return Holder.direct(attributeSupplier.get());
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, attribute)));
    }

    private static DeferredHolder<Attribute, Attribute> newResistanceAttribute(String id) {
        return (DeferredHolder<Attribute, Attribute>) ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicRangedAttribute("attribute.iss_magicfromtheeast." + id + "_magic_resist",
                1.0D, 0,10).setSyncable(true)));
    }

    private static DeferredHolder<Attribute, Attribute> newPowerAttribute(String id) {
        return (DeferredHolder<Attribute, Attribute>) ATTRIBUTES.register(id + "_spell_power", () -> (new MagicRangedAttribute("attribute.iss_magicfromtheeast." + id + "_spell_power",
                1.0D, 0, 10).setSyncable(true)));
    }
}
