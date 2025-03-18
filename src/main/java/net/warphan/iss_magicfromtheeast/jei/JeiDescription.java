package net.warphan.iss_magicfromtheeast.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.ItemRegistries;

@mezz.jei.api.JeiPlugin
public class JeiDescription implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addItemStackInfo(new ItemStack(ItemRegistries.RAW_JADE.get()), Component.translatable("item.iss_magicfromtheeast.raw_jade.guide"));
    }
}
