package quaternary.chickennugget.jei;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
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
		reg.addRecipeCategories(new RecipeCategoryDecapitateChicken());
		
		if(ChickenNugget.tinkersCompat) {
			ChickenNuggetTinkersJEIUtil.registerJEIRecipeCategories(reg);
		}
	}
	
	@Override
	public void register(IModRegistry reg) {
		reg.addRecipeCatalyst(new ItemStack(Blocks.CRAFTING_TABLE), RecipeCategoryCraftNugget.UID);
		reg.addRecipeCatalyst(new ItemStack(Blocks.CRAFTING_TABLE), RecipeCategoryCraftChicken.UID);
		for (ItemStack stack : getAllAxes()) { // Add all axes as catalysts
			reg.addRecipeCatalyst(stack, RecipeCategoryDecapitateChicken.UID);
		}
		
		reg.addRecipes(Collections.singletonList(new RecipeWrapperCraftNugget()), RecipeCategoryCraftNugget.UID);
		reg.addRecipes(Collections.singletonList(new RecipeWrapperCraftChicken()), RecipeCategoryCraftChicken.UID);
		reg.addRecipes(Collections.singletonList(new RecipeWrapperDecapitateChicken()), RecipeCategoryDecapitateChicken.UID);
		
		IRecipeTransferRegistry recipeTransferRegistry = reg.getRecipeTransferRegistry();
		recipeTransferRegistry.addRecipeTransferHandler(ContainerWorkbench.class, RecipeCategoryCraftChicken.UID, 1, 9, 10, 36);
		
		if(ChickenNugget.tinkersCompat) {
			ChickenNuggetTinkersJEIUtil.registerJEIRecipes(reg);
		}
	}
	
	private static List<ItemStack> cachedAxes = null;
	
	public static List<ItemStack> getAllAxes() {
		// Get everything that extends ItemAxe
		// Is there a better way to do this?
		if (cachedAxes == null) {
			cachedAxes = ImmutableList.copyOf(Item.REGISTRY).stream()
					.filter(item -> item instanceof ItemAxe)
					.map(item -> new ItemStack(item))
					.collect(Collectors.toList());
		}
		return cachedAxes;
	}
}
