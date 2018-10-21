package quaternary.chickennugget.tconstruct;

import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.plugin.jei.smelting.SmeltingRecipeWrapper;

public class RecipeWrapperMeltChicken extends SmeltingRecipeWrapper {
	public RecipeWrapperMeltChicken() {
		super(new MeltingRecipe(RecipeMatch.of(Items.AIR, 144), ChickenNuggetFluids.CHICKEN_FLUID));
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setOutputs(FluidStack.class, outputs);
	}
}
