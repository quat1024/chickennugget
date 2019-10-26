package quaternary.chickennugget.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.CraftChickenRecipe;
import quaternary.chickennugget.item.ChickenNuggetItems;

import javax.annotation.Nonnull;

public class RecipeCategoryCraftChicken implements IRecipeCategory<CraftChickenRecipe> {
	static final ResourceLocation UID = new ResourceLocation(ChickenNugget.MODID, "chicken_crafting");

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends CraftChickenRecipe> getRecipeClass() {
		return CraftChickenRecipe.class;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.format("chickennugget.jei.craft_chicken.title");
	}

	@Nonnull
	@Override
	public IDrawableStatic getBackground() {
		return ChickenNuggetJEIPlugin.guiHelper.createDrawable(new ResourceLocation(ChickenNugget.MODID, "textures/jei/craft_chicken.png"), 0, 0, 150, 75);
	}

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return ChickenNuggetJEIPlugin.guiHelper.createDrawableIngredient(new ItemStack(Blocks.CRAFTING_TABLE));
	}

	@Override
	public void setIngredients(@Nonnull CraftChickenRecipe recipeWrapperCraftChicken, IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, new ItemStack(ChickenNuggetItems.RAW_NUGGET, 9));
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull CraftChickenRecipe recipeWrapper, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
		for(int i = 0; i < 9; i++) {
			stackGroup.init(i, true, 12 + (i % 3) * 18, (75 / 2) - 26 + (i / 3) * 18); //MAGIC NUMBERS
			stackGroup.set(i, new ItemStack(ChickenNuggetItems.RAW_NUGGET));
		}
	}

	@Override
	public void draw(CraftChickenRecipe recipe, double mouseX, double mouseY) {
		RenderingWeirdness.drawChicken(120, 50, false);
	}
}
