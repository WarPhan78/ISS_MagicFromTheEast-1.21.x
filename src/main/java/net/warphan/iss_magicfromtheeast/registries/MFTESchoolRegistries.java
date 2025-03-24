package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import net.warphan.iss_magicfromtheeast.util.MFTETags;

import java.util.function.Supplier;

public class MFTESchoolRegistries extends SchoolRegistry {
    private static final DeferredRegister<SchoolType> MFTE_SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus)
    {
        MFTE_SCHOOLS.register(eventBus);
    }

    private static Supplier<SchoolType> registerSchool(SchoolType schoolType) {
        return MFTE_SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }

    public static SchoolType getSchool(ResourceLocation resourceLocation) {
        return REGISTRY.get(resourceLocation);
    }

    public static final ResourceLocation SYMMETRY_RESOURCE = ISS_MagicFromTheEast.id("symmetry");
    public static final ResourceLocation SPIRIT_RESOURCE = ISS_MagicFromTheEast.id("spirit");
    public static final ResourceLocation DUNE_RESOURCE = ISS_MagicFromTheEast.id("dune");

    public static final Supplier<SchoolType> SYMMETRY = registerSchool(new SchoolType(
            SYMMETRY_RESOURCE,
            MFTETags.SYMMETRY_FOCUS,
            Component.translatable("school.iss_magicfromtheeast.symmetry").withStyle(Style.EMPTY.withColor(0x00a36c)),
            MFTEAttributeRegistries.SYMMETRY_SPELL_POWER,
            MFTEAttributeRegistries.SYMMETRY_MAGIC_RESIST,
            MFTESoundRegistries.SYMMETRY_CAST,
            MFTEDamageTypes.SYMMETRY_MAGIC
    ));

    public static final Supplier<SchoolType> SPIRIT = registerSchool(new SchoolType(
            SPIRIT_RESOURCE,
            MFTETags.SPIRIT_FOCUS,
            Component.translatable("school.iss_magicfromtheeast.spirit").withStyle(Style.EMPTY.withColor(0x009dc4)),
            MFTEAttributeRegistries.SPIRIT_SPELL_POWER,
            MFTEAttributeRegistries.SPIRIT_MAGIC_RESIST,
            MFTESoundRegistries.SPIRIT_CAST,
            MFTEDamageTypes.SPIRIT_MAGIC
    ));

    public static final Supplier<SchoolType> DUNE = registerSchool(new SchoolType(
            DUNE_RESOURCE,
            MFTETags.DUNE_FOCUS,
            Component.translatable("school.iss_magicfromtheeast.dune").withStyle(Style.EMPTY.withColor(0xff4d00)),
            MFTEAttributeRegistries.DUNE_SPELL_POWER,
            MFTEAttributeRegistries.DUNE_MAGIC_RESIST,
            MFTESoundRegistries.DUNE_CAST,
            MFTEDamageTypes.DUNE_MAGIC
    ));

}