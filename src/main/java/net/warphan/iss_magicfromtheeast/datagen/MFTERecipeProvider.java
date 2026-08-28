//package net.warphan.iss_magicfromtheeast.datagen;
//
//import io.redspace.ironsspellbooks.api.spells.SchoolType;
//import io.redspace.ironsspellbooks.registries.ItemRegistry;
//import net.minecraft.data.PackOutput;
//import net.minecraft.data.recipes.FinishedRecipe;
//import net.minecraft.data.recipes.RecipeCategory;
//import net.minecraft.data.recipes.RecipeProvider;
//import net.minecraft.data.recipes.ShapedRecipeBuilder;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
//import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
//
//import java.util.function.Consumer;
//
//public class MFTERecipeProvider extends RecipeProvider {
//    public MFTERecipeProvider(PackOutput output) {
//        super(output);
//    }
//
//    @Override
//    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
//        schoolArmorCrafting(consumer, MFTESchoolRegistries.SYMMETRY.get(), "taoist");
//        schoolArmorCrafting(consumer, MFTESchoolRegistries.SPIRIT.get(), "onmyoji");
//}
