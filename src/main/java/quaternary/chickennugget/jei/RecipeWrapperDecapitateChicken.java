package quaternary.chickennugget.jei;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import quaternary.chickennugget.ChickenNuggetBlocks;

public class RecipeWrapperDecapitateChicken implements IRecipeWrapper {
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, ImmutableList.of(ChickenNuggetJEIPlugin.getAllAxes()));
		ingredients.setOutput(ItemStack.class, new ItemStack(ChickenNuggetBlocks.CHICKEN_HEAD_BLOCK));
	}
}
