package quaternary.chickennugget.compat.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.block.ChickenNuggetBlocks;

import javax.annotation.Nonnull;

public class RecipeCategoryDecapitateChicken implements IRecipeCategory<RecipeWrapperDecapitateChicken> {
	static final ResourceLocation UID = new ResourceLocation(ChickenNugget.MODID, "decapitate_chicken");

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends RecipeWrapperDecapitateChicken> getRecipeClass() {
		return RecipeWrapperDecapitateChicken.class;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.format("chickennugget.jei.decapitate_chicken.title");
	}

	@Nonnull
	@Override
	public IDrawableStatic getBackground() {
		return ChickenNuggetJEIPlugin.guiHelper.createDrawable(new ResourceLocation(ChickenNugget.MODID, "textures/jei/decapitate_chicken.png"), 0, 0, 150, 75);
	}

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return ChickenNuggetJEIPlugin.guiHelper.createDrawableIngredient(new ItemStack(Items.IRON_AXE));
	}

	@Override
	public void setIngredients(@Nonnull RecipeWrapperDecapitateChicken recipeWrapperDecapitateChicken, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, ImmutableList.of(ChickenNuggetJEIPlugin.getAllAxes()));
		ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(ChickenNuggetBlocks.CHICKEN_HEAD_BLOCK));
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull RecipeWrapperDecapitateChicken recipeWrapper, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
		stackGroup.init(0, true, 48, (75 / 2) - 8);
		stackGroup.set(0, ChickenNuggetJEIPlugin.getAllAxes());
		stackGroup.init(1, false, 135, (75 / 2) - 8);
		stackGroup.set(1, new ItemStack(ChickenNuggetBlocks.CHICKEN_HEAD_BLOCK));
	}

	@Override
	public void draw(RecipeWrapperDecapitateChicken recipe, double mouseX, double mouseY) {
		RenderingWeirdness.drawChicken(15, 50, false);
		RenderingWeirdness.drawHeadlessChicken(107, 50);
	}
}
