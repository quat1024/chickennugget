package quaternary.chickennugget.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;

import java.util.Collections;

@JEIPlugin
public class ChickenNuggetJEIPlugin implements IModPlugin {
	public static IGuiHelper guiHelper = null;
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new RecipeCategoryCraftNugget());
		registry.addRecipeCategories(new RecipeCategoryCraftChicken());
	}
	
	@Override
	public void register(IModRegistry registry) {
		guiHelper = registry.getJeiHelpers().getGuiHelper();
		
		registry.addRecipeCatalyst(new ItemStack(Blocks.CRAFTING_TABLE), RecipeCategoryCraftNugget.UID);
		registry.addRecipeCatalyst(new ItemStack(Blocks.CRAFTING_TABLE), RecipeCategoryCraftChicken.UID);
		
		registry.addRecipes(Collections.singletonList(new RecipeWrapperCraftNugget()), RecipeCategoryCraftNugget.UID);
		registry.addRecipes(Collections.singletonList(new RecipeWrapperCraftChicken()), RecipeCategoryCraftChicken.UID);
		
		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		recipeTransferRegistry.addRecipeTransferHandler(ContainerWorkbench.class, RecipeCategoryCraftChicken.UID, 1, 9, 10, 36);
	}
}
