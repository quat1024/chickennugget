package quaternary.chickennugget.compat.jei;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.item.ChickenNuggetItems;

public class RecipeCategoryCraftNugget implements IRecipeCategory<RecipeWrapperCraftNugget> {
	public static final String UID = "chickennugget_craft_nugget";
	
	@Override
	public String getUid() {
		return UID;
	}
	
	@Override
	public String getTitle() {
		return I18n.translateToLocal("chickennugget.jei.craft_nugget.title");
	}
	
	@Override
	public String getModName() {
		return ChickenNugget.NAME;
	}
	
	@Override
	public IDrawable getBackground() {
		return ChickenNuggetJEIPlugin.guiHelper.createDrawable(new ResourceLocation(ChickenNugget.MODID, "textures/jei/craft_nugget.png"), 0, 0, 150, 75);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperCraftNugget recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, false, 100, (75 / 2) - 8);
		recipeLayout.getItemStacks().set(0, new ItemStack(ChickenNuggetItems.RAW_NUGGET, 9));
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		RenderingWeirdness.drawChicken(45, 40, false);
		RenderingWeirdness.drawCraftingTable(24, 30);
	}
}
