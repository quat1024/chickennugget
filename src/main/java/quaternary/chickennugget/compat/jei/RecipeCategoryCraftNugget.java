package quaternary.chickennugget.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.item.ChickenNuggetItems;

import javax.annotation.Nonnull;

public class RecipeCategoryCraftNugget implements IRecipeCategory<RecipeWrapperCraftNugget> {
	static final ResourceLocation UID = new ResourceLocation(ChickenNugget.MODID, "craft_nugget");

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends RecipeWrapperCraftNugget> getRecipeClass() {
		return RecipeWrapperCraftNugget.class;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.format("chickennugget.jei.craft_nugget.title");
	}

	@Nonnull
	@Override
	public IDrawableStatic getBackground() {
		return ChickenNuggetJEIPlugin.guiHelper.createDrawable(new ResourceLocation(ChickenNugget.MODID, "textures/jei/craft_nugget.png"), 0, 0, 150, 75);
	}

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return ChickenNuggetJEIPlugin.guiHelper.createDrawableIngredient(new ItemStack(Blocks.CRAFTING_TABLE));
	}

	@Override
	public void setIngredients(@Nonnull RecipeWrapperCraftNugget recipeWrapperCraftNugget, IIngredients ingredients) {
		//what could go wrong?
		ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(ChickenNuggetItems.RAW_NUGGET, 9));
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull RecipeWrapperCraftNugget recipeWrapper, @Nonnull IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, false, 100, (75 / 2) - 8);
		recipeLayout.getItemStacks().set(0, new ItemStack(ChickenNuggetItems.RAW_NUGGET, 9));
	}

	@Override
	public void draw(RecipeWrapperCraftNugget recipe, double mouseX, double mouseY) {
		RenderingWeirdness.drawChicken(45, 40, false);
		RenderingWeirdness.drawCraftingTable(24, 30);
	}
}
