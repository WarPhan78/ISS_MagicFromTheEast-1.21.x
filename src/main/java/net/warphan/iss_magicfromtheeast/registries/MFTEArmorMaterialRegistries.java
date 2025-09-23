package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class MFTEArmorMaterialRegistries {
    private static final DeferredRegister<ArmorMaterial> MFTE_ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        MFTE_ARMOR_MATERIALS.register(eventBus);
    }

    public static DeferredHolder<ArmorMaterial, ArmorMaterial> JADE = register("jade",
            makeArmorMap(4, 10, 6, 4, 16),
            40,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            () -> Ingredient.of(MFTEItemRegistries.REFINED_JADE_INGOT.get()),
            2.0f,
            0.05f);

    public static DeferredHolder<ArmorMaterial, ArmorMaterial> ELEMENTAL_COMMANDER = register("elemental",
            magicArmorMap(),
            40,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(ModTags.MITHRIL_INGOT),
            0,
            0f);

    private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(
            String name,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            Supplier<Ingredient> repairIngredient,
            float toughness,
            float knockbackResistance
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(ISS_MagicFromTheEast.id(name)));
        return MFTE_ARMOR_MATERIALS.register(name, ()-> new ArmorMaterial(defense, enchantmentValue, equipSound, repairIngredient, list, toughness, knockbackResistance));
    }

    static public EnumMap<ArmorItem.Type, Integer> makeArmorMap(int helmet, int chestplate, int leggings, int boots, int body) {
        return Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266655_) -> {
            p_266655_.put(ArmorItem.Type.BOOTS, boots);
            p_266655_.put(ArmorItem.Type.LEGGINGS, leggings);
            p_266655_.put(ArmorItem.Type.CHESTPLATE, chestplate);
            p_266655_.put(ArmorItem.Type.HELMET, helmet);
            p_266655_.put(ArmorItem.Type.BODY, body);
        });
    }

    static public EnumMap<ArmorItem.Type, Integer> magicArmorMap() {
        return makeArmorMap(3, 8, 6, 3, 0);
    }
}
