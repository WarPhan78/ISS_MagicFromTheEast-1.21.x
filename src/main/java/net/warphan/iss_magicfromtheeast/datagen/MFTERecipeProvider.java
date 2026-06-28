package net.warphan.iss_magicfromtheeast.datagen;

import io.redspace.ironsspellbooks.datagen.IronRecipeProvider;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.BrewAlchemistCauldronRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTEFluidRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;

import java.util.concurrent.CompletableFuture;

public class MFTERecipeProvider extends IronRecipeProvider {
    public MFTERecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        schoolArmorSmithing(recipeOutput, MFTESchoolRegistries.SYMMETRY.get(), "taoist");
        schoolArmorSmithing(recipeOutput, MFTESchoolRegistries.SPIRIT.get(), "onmyoji");

        simpleRingSalvageRecipe(recipeOutput, MFTEItemRegistries.SOULWARD_RING.get(), Ingredient.of(MFTEItemRegistries.CRYSTALLIZED_SOUL.get()));

        cauldronBottledInteraction(recipeOutput, MFTEItemRegistries.BOTTLE_OF_SOULS, MFTEFluidRegistries.SOUL);

        BrewAlchemistCauldronRecipe.builder()
                .withInput(MFTEFluidRegistries.SOUL, 1000)
                .withReagent(Items.ECHO_SHARD)
                .withByproduct(MFTEItemRegistries.CRYSTALLIZED_SOUL)
                .saveSoak(recipeOutput);
    }
}
