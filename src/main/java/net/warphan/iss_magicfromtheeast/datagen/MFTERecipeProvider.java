package net.warphan.iss_magicfromtheeast.datagen;

import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.function.Consumer;

public class MFTERecipeProvider extends RecipeProvider {
    public MFTERecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        // TODO PORT 1.20.1: on 1.21 these were smithing recipes onto ISS' "wizard_base_*" item tags,
        // which do not exist on ISS 1.20.1. Ported to shaped crafting recipes matching the ISS 1.20.1
        // school armor recipes (magic cloth + school rune), keeping the same "<armor>_crafting" ids.
        schoolArmorCrafting(consumer, MFTESchoolRegistries.SYMMETRY.get(), "taoist");
        schoolArmorCrafting(consumer, MFTESchoolRegistries.SPIRIT.get(), "onmyoji");

        // TODO PORT 1.20.1: ISS 1.20.1 has no mithril_scrap; arcane_salvage is the curio core item there.
        simpleRingSalvageRecipe(consumer, MFTEItemRegistries.SOULWARD_RING.get(), Ingredient.of(MFTEItemRegistries.CRYSTALLIZED_SOUL.get()));

        // TODO PORT 1.20.1: the alchemist cauldron is NOT recipe/datagen driven on 1.20.1
        // (recipe_types.alchemist_cauldron.* does not exist). The removed recipes must be re-implemented
        // in common setup code instead:
        // - bottle_of_souls fill/empty interactions: subscribe to
        //   io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronBuildInteractionsEvent
        //   on MinecraftForge.EVENT_BUS and add interactions for MFTEItemRegistries.BOTTLE_OF_SOULS
        //   (see AlchemistCauldronTile.newInteractionMap / addSimpleBottleEmptyInteraction).
        // - soak recipe (soul fluid + echo shard -> crystallized_soul): register via
        //   AlchemistCauldronRecipeRegistry.addRecipe(new AlchemistCauldronRecipe(
        //       MFTEItemRegistries.BOTTLE_OF_SOULS.get(), Items.ECHO_SHARD, MFTEItemRegistries.CRYSTALLIZED_SOUL.get()));
    }

    /**
     * 1.20.1 replacement for ISS 1.21 IronRecipeProvider#schoolArmorSmithing:
     * shaped crafting of magic cloth + school rune per armor slot, in the ISS 1.20.1 patterns.
     */
    public static void schoolArmorCrafting(Consumer<FinishedRecipe> output, SchoolType school, String armorName) {
        String[] slots = {"boots", "leggings", "chestplate", "helmet"};
        String[][] patterns = {
                {"C C", "CRC"},
                {"CCC", "CRC", "C C"},
                {"CRC", "CCC", "CCC"},
                {"CCC", "CRC"}
        };
        ResourceLocation schoolId = school.getId();
        Item cloth = ItemRegistry.MAGIC_CLOTH.get();
        Item rune = ForgeRegistries.ITEMS.getValue(new ResourceLocation(schoolId.getNamespace(), String.format("%s_rune", schoolId.getPath())));
        for (int i = 0; i < slots.length; i++) {
            ResourceLocation itemId = new ResourceLocation(schoolId.getNamespace(), String.format("%s_%s", armorName, slots[i]));
            Item result = ForgeRegistries.ITEMS.getValue(itemId);
            ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                    .define('C', cloth)
                    .define('R', rune);
            for (String row : patterns[i]) {
                builder.pattern(row);
            }
            builder.unlockedBy("has_magic_cloth", has(cloth))
                    .save(output, new ResourceLocation(itemId.getNamespace(), itemId.getPath() + "_crafting"));
        }
    }

    protected void simpleRingSalvageRecipe(Consumer<FinishedRecipe> output, Item result, Ingredient modifier) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                .define('M', modifier)
                .define('X', ItemRegistry.ARCANE_SALVAGE.get())
                .pattern("M ")
                .pattern(" X")
                .unlockedBy("arcane_salvage", has(ItemRegistry.ARCANE_SALVAGE.get()))
                .save(output);
    }
}
