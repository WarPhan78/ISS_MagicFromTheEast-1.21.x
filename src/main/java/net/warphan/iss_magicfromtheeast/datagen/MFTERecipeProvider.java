package net.warphan.iss_magicfromtheeast.datagen;

import io.redspace.ironsspellbooks.datagen.IronRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;

import java.util.concurrent.CompletableFuture;

public class MFTERecipeProvider extends IronRecipeProvider {
    public MFTERecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        schoolArmorSmithing(recipeOutput, ISS_MagicFromTheEast.MOD_ID, "symmetry", "taoist");
        schoolArmorSmithing(recipeOutput, ISS_MagicFromTheEast.MOD_ID, "spirit", "onmyoji");
    }
}
