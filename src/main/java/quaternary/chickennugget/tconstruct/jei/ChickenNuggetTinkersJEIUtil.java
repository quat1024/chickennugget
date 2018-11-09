package quaternary.chickennugget.tconstruct.jei;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.BlockCasting;

import java.util.Collections;

//Three-way mod compat means there's lots of fun classloading going on.
public class ChickenNuggetTinkersJEIUtil {
	public static void registerJEIRecipeCategories(IRecipeCategoryRegistration reg) {
		reg.addRecipeCategories(new RecipeCategoryCastChicken());
		reg.addRecipeCategories(new RecipeCategoryMeltChicken());
	}
	
	public static void registerJEIRecipes(IModRegistry reg) {
		reg.addRecipes(Collections.singletonList(new RecipeWrapperCastChicken()), RecipeCategoryCastChicken.UID);
		reg.addRecipeCatalyst(new ItemStack(TinkerSmeltery.castingBlock, 1, BlockCasting.CastingType.BASIN.meta), RecipeCategoryCastChicken.UID);
		reg.addRecipes(Collections.singletonList(new RecipeWrapperMeltChicken()), RecipeCategoryMeltChicken.UID);
		reg.addRecipeCatalyst(new ItemStack(TinkerSmeltery.smelteryController), RecipeCategoryMeltChicken.UID);
	}
}
