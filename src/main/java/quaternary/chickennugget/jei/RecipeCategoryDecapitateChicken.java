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
import quaternary.chickennugget.ChickenNuggetBlocks;

public class RecipeCategoryDecapitateChicken implements IRecipeCategory<RecipeWrapperDecapitateChicken> {
	public static final String UID = "chickennugget_decapitate_chicken";
	
	@Override
	public String getUid() {
		return UID;
	}
	
	@Override
	public String getTitle() {
		return I18n.translateToLocal("chickennugget.jei.decapitate_chicken.title");
	}
	
	@Override
	public String getModName() {
		return ChickenNugget.NAME;
	}
	
	@Override
	public IDrawable getBackground() {
		return ChickenNuggetJEIPlugin.guiHelper.createDrawable(new ResourceLocation(ChickenNugget.MODID, "textures/jei/decapitate_chicken.png"), 0, 0, 150, 75);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperDecapitateChicken recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
		stackGroup.init(0, true, 48, (75 / 2) - 8);
		stackGroup.set(0, ChickenNuggetJEIPlugin.getAllAxes());
		stackGroup.init(1, false, 135, (75 / 2) - 8);
		stackGroup.set(1, new ItemStack(ChickenNuggetBlocks.CHICKEN_HEAD_BLOCK));
	}
	
	@Override
	public void drawExtras(Minecraft mc) {
		RenderingWeirdness.drawChicken(15, 50, false);
		RenderingWeirdness.drawHeadlessChicken(107, 50);
	}
}
