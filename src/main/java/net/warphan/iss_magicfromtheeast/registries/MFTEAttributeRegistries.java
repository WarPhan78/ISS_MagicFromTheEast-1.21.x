package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.attribute.MagicPercentAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

@Mod.EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MFTEAttributeRegistries {

    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final RegistryObject<Attribute> SYMMETRY_MAGIC_RESIST = newResistanceAttribute("symmetry");
    public static final RegistryObject<Attribute> SPIRIT_MAGIC_RESIST = newResistanceAttribute("spirit");
    public static final RegistryObject<Attribute> DUNE_MAGIC_RESIST = newResistanceAttribute("dune");

    public static final RegistryObject<Attribute> SYMMETRY_SPELL_POWER = newPowerAttribute("symmetry");
    public static final RegistryObject<Attribute> SPIRIT_SPELL_POWER = newPowerAttribute("spirit");
    public static final RegistryObject<Attribute> DUNE_SPELL_POWER = newPowerAttribute("dune");

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, attribute.get())));
    }

    // PORT 1.20.1: ISS 3.16.2-1.20.1 backports MagicPercentAttribute (api/attribute/MagicPercentAttribute),
    // so the original 1.21 attribute type is restored. Registry wiring stays Forge (RegistryObject +
    // ForgeRegistries.ATTRIBUTES), matching ISS 3.16.2 AttributeRegistry on 1.20.1.
    private static RegistryObject<Attribute> newResistanceAttribute(String id) {
        return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicPercentAttribute("attribute.iss_magicfromtheeast." + id + "_magic_resist",
                1.0D, -100, 100).setSyncable(true)));
    }

    private static RegistryObject<Attribute> newPowerAttribute(String id) {
        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicPercentAttribute("attribute.iss_magicfromtheeast." + id + "_spell_power",
                1.0D, -100, 100).setSyncable(true)));
    }
}
