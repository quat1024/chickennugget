package quaternary.chickennugget.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import quaternary.chickennugget.item.ChickenNuggetItems;

public class RecipeWrapperCraftNugget implements IRecipeWrapper {
	@Override
	public void getIngredients(IIngredients ingredients) {
		//what could go wrong?
		ingredients.setOutput(ItemStack.class, new ItemStack(ChickenNuggetItems.RAW_NUGGET, 9));
	}
}
