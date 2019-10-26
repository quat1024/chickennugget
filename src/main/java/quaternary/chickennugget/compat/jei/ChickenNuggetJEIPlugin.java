package quaternary.chickennugget.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.CraftChickenRecipe;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@mezz.jei.api.JeiPlugin
public class ChickenNuggetJEIPlugin implements IModPlugin {
	static IGuiHelper guiHelper = null;

	@Override
	@Nonnull
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(ChickenNugget.MODID, "main");
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration reg) {
		guiHelper = reg.getJeiHelpers().getGuiHelper();
		
		reg.addRecipeCategories(new RecipeCategoryCraftNugget());
		reg.addRecipeCategories(new RecipeCategoryCraftChicken());
		reg.addRecipeCategories(new RecipeCategoryDecapitateChicken());

		// TODO: integrate tinkers
//		if(ChickenNugget.tinkersCompat) {
//			ChickenNuggetTinkersJEIUtil.registerJEIRecipeCategories(reg);
//		}

	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
		reg.addRecipeCatalyst(new ItemStack(Blocks.CRAFTING_TABLE), RecipeCategoryCraftNugget.UID);
		reg.addRecipeCatalyst(new ItemStack(Blocks.CRAFTING_TABLE), RecipeCategoryCraftChicken.UID);
		for (ItemStack stack : getAllAxes()) { // Add all axes as catalysts
			reg.addRecipeCatalyst(stack, RecipeCategoryDecapitateChicken.UID);
		}
	}

	@Override
	public void registerRecipes(IRecipeRegistration reg) {
		RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();

		reg.addRecipes(Collections.singletonList(new RecipeWrapperCraftNugget()), RecipeCategoryCraftNugget.UID);
		reg.addRecipes(recipeManager.getRecipes().stream().filter(r -> r instanceof CraftChickenRecipe).collect(Collectors.toList()), RecipeCategoryCraftChicken.UID);
		reg.addRecipes(Collections.singletonList(new RecipeWrapperDecapitateChicken()), RecipeCategoryDecapitateChicken.UID);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration reg) {
		reg.addRecipeTransferHandler(WorkbenchContainer.class, RecipeCategoryCraftChicken.UID, 1, 9, 10, 36);
	}

	// TODO: integrate tinkers
//	@Override
//	public void register(IModRegistry reg) {
//		if(ChickenNugget.tinkersCompat) {
//			ChickenNuggetTinkersJEIUtil.registerJEIRecipes(reg);
//		}
//	}
	
	private static List<ItemStack> cachedAxes = null;
	
	static List<ItemStack> getAllAxes() {
		// Get everything that has an axe ToolType
		// Is there a better way to do this?
		if (cachedAxes == null) {
			cachedAxes = ForgeRegistries.ITEMS.getValues().parallelStream()
					.map(ItemStack::new)
					.filter(st -> st.getToolTypes().contains(ToolType.AXE))
					.collect(Collectors.toList());
		}
		return cachedAxes;
	}
}
