package quaternary.chickennugget.jei;

import java.util.Collections;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.tconstruct.jei.ChickenNuggetTinkersJEIUtil;

@JEIPlugin
public class ChickenNuggetJEIPlugin implements IModPlugin {
	public static IGuiHelper guiHelper = null;
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration reg) {
		guiHelper = reg.getJeiHelpers().getGuiHelper();
		
		reg.addRecipeCategories(new RecipeCategoryCraftNugget());
		reg.addRecipeCategories(new RecipeCategoryCraftChicken());
		
		if(ChickenNugget.tinkersCompat) {
			ChickenNuggetTinkersJEIUtil.registerJEIRecipeCategories(reg);
		}
	}
	
	@Override
	public void register(IModRegistry reg) {
		reg.addRecipeCatalyst(new ItemStack(Blocks.CRAFTING_TABLE), RecipeCategoryCraftNugget.UID);
		reg.addRecipeCatalyst(new ItemStack(Blocks.CRAFTING_TABLE), RecipeCategoryCraftChicken.UID);
		
		reg.addRecipes(Collections.singletonList(new RecipeWrapperCraftNugget()), RecipeCategoryCraftNugget.UID);
		reg.addRecipes(Collections.singletonList(new RecipeWrapperCraftChicken()), RecipeCategoryCraftChicken.UID);
		
		IRecipeTransferRegistry recipeTransferRegistry = reg.getRecipeTransferRegistry();
		recipeTransferRegistry.addRecipeTransferHandler(ContainerWorkbench.class, RecipeCategoryCraftChicken.UID, 1, 9, 10, 36);
		
		if(ChickenNugget.tinkersCompat) {
			ChickenNuggetTinkersJEIUtil.registerJEIRecipes(reg);
		}
	}
}
