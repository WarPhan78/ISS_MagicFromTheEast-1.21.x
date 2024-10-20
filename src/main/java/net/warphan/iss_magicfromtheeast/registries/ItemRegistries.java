package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.item.weapons.*;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.item.*;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.item.armor.TaoistArmorItem;
import net.warphan.iss_magicfromtheeast.item.weapons.MFTEStaffTier;

public class ItemRegistries {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ISS_MagicFromTheEast.MOD_ID);

    public static final DeferredHolder<Item, Item> JADE = ITEMS.register("jade",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOTTLE_OF_SOULS = ITEMS.register("bottle_of_souls",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ARCANE_RELICS = ITEMS.register("arcane_relics",
            () -> new Item(new Item.Properties()));

    //Taoist Set
    public static final DeferredHolder<Item, Item> TAOIST_HAT = ITEMS.register("taoist_hat",
            () -> new TaoistArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredHolder<Item, Item> TAOIST_ROBES = ITEMS.register("taoist_robes",
            () -> new TaoistArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredHolder<Item, Item> TAOIST_LEGGINGS = ITEMS.register("taoist_leggings",
            () -> new TaoistArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredHolder<Item, Item> TAOIST_BOOTS = ITEMS.register("taoist_boots",
            () -> new TaoistArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.BOOTS.getDurability(37))));


    public static final DeferredHolder<Item, Item> TAIJI_SWORD = ITEMS.register("taiji_sword",
            () -> new StaffItem(ItemPropertiesHelper.equipment(1).attributes(ExtendedSwordItem.createAttributes(MFTEStaffTier.TAIJI_SWORD))));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}


