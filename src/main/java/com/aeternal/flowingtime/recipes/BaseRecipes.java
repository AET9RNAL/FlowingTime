package com.aeternal.flowingtime.recipes;

import com.aeternal.flowingtime.api.Recipes;
import com.aeternal.flowingtime.api.annotation.AutoRecipeProvider;
import com.aeternal.flowingtime.registry.ModItems;
import net.minecraft.world.item.Items;

@AutoRecipeProvider
public class BaseRecipes {

    public BaseRecipes() {

        Recipes.recipe.addRecipe(ModItems.FL_PEDESTAL_ITEM,
                "SOS",
                "SOS",
                "OEO",
                'S', Items.NETHER_STAR,
                'O', Items.OBSIDIAN,
                'E', Items.DRAGON_EGG);

        Recipes.recipe.addRecipe(ModItems.TIME_WATCH,
                "GSG",
                "SCS",
                "GSG",
                'G', Items.GOLD_BLOCK,
                'S', Items.NETHER_STAR,
                'C', Items.END_CRYSTAL);
    }
}
