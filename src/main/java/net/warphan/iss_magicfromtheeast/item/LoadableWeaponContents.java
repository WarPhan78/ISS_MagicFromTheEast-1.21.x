package net.warphan.iss_magicfromtheeast.item;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.warphan.iss_magicfromtheeast.registries.MFTEDataComponentRegistries;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * PORT 1.20.1: this used to be a data component (with Codec/StreamCodec). On 1.20.1 the contents
 * are stored as ItemStack NBT under {@link MFTEDataComponentRegistries#LOADABLE_WEAPON_CONTENTS}
 * (see {@link #get(ItemStack)} / {@link #set(ItemStack, LoadableWeaponContents)}).
 */
public final class LoadableWeaponContents implements TooltipComponent {
    public static final LoadableWeaponContents EMPTY = new LoadableWeaponContents(List.of());
    private static final String ITEMS_TAG = "Items";
    private static final Fraction PROJECTILE_IN_STORAGE_WEIGHT = Fraction.getFraction(1, 16);

    final List<ItemStack> items;
    final Fraction weight;

    LoadableWeaponContents(List<ItemStack> items, Fraction weight) {
        this.items = items;
        this.weight = weight;
    }

    public static LoadableWeaponContents of(List<ItemStack> stacks) {
        return new LoadableWeaponContents(List.copyOf(Lists.transform(stacks, ItemStack::copy)));
    }

    public LoadableWeaponContents(List<ItemStack> stacks) {
        this(stacks, computeContentWeight(stacks));
    }

    private static Fraction computeContentWeight(List<ItemStack> stacks) {
        Fraction fraction = Fraction.ZERO;

        ItemStack itemStack;
        for (Iterator<ItemStack> var2 = stacks.iterator(); var2.hasNext(); fraction = fraction.add(getWeight(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)))) {
            itemStack = var2.next();
        }

        return fraction;
    }

    static Fraction getWeight(ItemStack itemStack) {
        LoadableWeaponContents contents = get(itemStack);
        if (contents != null)
            return PROJECTILE_IN_STORAGE_WEIGHT.add(contents.weight);
        return Fraction.ONE;
    }

    //------ NBT (replacement for the 1.21 data component codecs) ------

    /**
     * @return the contents stored on the stack's NBT, or null when absent
     * (mirrors {@code stack.get(MFTEDataComponentRegistries.LOADABLE_WEAPON_CONTENTS)}).
     */
    @Nullable
    public static LoadableWeaponContents get(ItemStack stack) {
        CompoundTag tag = MFTEDataComponentRegistries.getCompound(stack, MFTEDataComponentRegistries.LOADABLE_WEAPON_CONTENTS);
        return tag == null ? null : fromTag(tag);
    }

    public static void set(ItemStack stack, LoadableWeaponContents contents) {
        MFTEDataComponentRegistries.setCompound(stack, MFTEDataComponentRegistries.LOADABLE_WEAPON_CONTENTS, contents.toTag());
    }

    public static void remove(ItemStack stack) {
        MFTEDataComponentRegistries.remove(stack, MFTEDataComponentRegistries.LOADABLE_WEAPON_CONTENTS);
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (ItemStack itemStack : this.items) {
            listTag.add(itemStack.save(new CompoundTag()));
        }
        tag.put(ITEMS_TAG, listTag);
        return tag;
    }

    public static LoadableWeaponContents fromTag(@Nullable CompoundTag tag) {
        if (tag == null) {
            return EMPTY;
        }
        ListTag listTag = tag.getList(ITEMS_TAG, Tag.TAG_COMPOUND);
        List<ItemStack> stacks = new ArrayList<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            ItemStack stack = ItemStack.of(listTag.getCompound(i));
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        return new LoadableWeaponContents(List.copyOf(stacks));
    }

    //------ accessors (unchanged from 1.21) ------

    public ItemStack getItemUnsafe(int p_330802_) {
        return this.items.get(p_330802_);
    }

    public Stream<ItemStack> itemCopyStream() {
        return this.items.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> items() {
        return this.items;
    }

    public Iterable<ItemStack> itemsCopy() {
        return Lists.transform(this.items, ItemStack::copy);
    }

    public int size() {
        return this.items.size();
    }

    public Fraction weight() {
        return this.weight;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof LoadableWeaponContents loadableWeaponContents) {
            // PORT 1.20.1: ItemStack.listMatches does not exist - inlined equivalent.
            if (!this.weight.equals(loadableWeaponContents.weight) || this.items.size() != loadableWeaponContents.items.size()) {
                return false;
            }
            for (int i = 0; i < this.items.size(); i++) {
                if (!ItemStack.matches(this.items.get(i), loadableWeaponContents.items.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        // PORT 1.20.1: ItemStack.hashStackList does not exist - inlined equivalent.
        int i = 0;
        for (ItemStack itemStack : this.items) {
            i = i * 31 + (itemStack.getItem().hashCode() * 31 + itemStack.getCount());
        }
        return i;
    }

    public String toString() {
        return "LoadableWeaponContents" + String.valueOf(this.items);
    }

    public static class Mutable {
        private final List<ItemStack> items;
        private Fraction weight;

        public Mutable(LoadableWeaponContents contents) {
            this.items = new ArrayList<>(contents.items);
            this.weight = contents.weight;
        }

        public List<ItemStack> getItems() {
            return Lists.transform(this.items, ItemStack::copy);
        }

        public int tryInsert(ItemStack itemStack) {
            if (!itemStack.isEmpty() && itemStack.getItem().canFitInsideContainerItems()) {
                int i = 1;
                this.weight = this.weight.add(LoadableWeaponContents.getWeight(itemStack).multiplyBy(Fraction.getFraction(i, 1)));
                this.items.add(0, itemStack.split(i));
                return i;
            } else {
                return 0;
            }
        }

        @Nullable
        public ItemStack removeOne() {
            if (this.items.isEmpty()) {
                return null;
            } else {
                ItemStack itemstack = this.items.remove(0).copy();
                this.weight = this.weight.subtract(LoadableWeaponContents.getWeight(itemstack).multiplyBy(Fraction.getFraction(itemstack.getCount(), 1)));
                return itemstack;
            }
        }

        public Fraction weight() {
            return this.weight;
        }

        public LoadableWeaponContents toImmutable() {
            return new LoadableWeaponContents(List.copyOf(this.items), this.weight);
        }
    }
}
