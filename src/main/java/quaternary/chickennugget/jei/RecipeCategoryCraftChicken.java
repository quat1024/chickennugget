package quaternary.chickennugget.jei;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.ChickenNuggetItems;

public class RecipeCategoryCraftChicken implements IRecipeCategory<RecipeWrapperCraftChicken> {
	public static final String UID = "chickennugget_craft_chicken";
	
	@Override
	public String getUid() {
		return UID;
	}
	
	@Override
	public String getTitle() {
		return I18n.translateToLocal("chickennugget.jei.craft_chicken.title");
	}
	
	@Override
	public String getModName() {
		return ChickenNugget.NAME;
	}
	
	@Override
	public IDrawable getBackground() {
		return ChickenNuggetJEIPlugin.guiHelper.createDrawable(new ResourceLocation(ChickenNugget.MODID, "textures/jei/craft_chicken.png"), 0, 0, 150, 75);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperCraftChicken recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
		for(int i = 0; i < 9; i++) {
			stackGroup.init(i, true, 12 + (i % 3) * 18, (75 / 2) - 26 + (i / 3) * 18); //MAGIC NUMBERS
			stackGroup.set(i, new ItemStack(ChickenNuggetItems.RAW_NUGGET));
		}
	}
	
	@Override
	public void drawExtras(Minecraft mc) {
		RenderingWeirdness.drawChicken(120, 50, false);
	}
}
