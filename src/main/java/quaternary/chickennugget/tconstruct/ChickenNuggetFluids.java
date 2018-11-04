package quaternary.chickennugget.tconstruct;

import java.util.Collections;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.ChickenNuggetItems;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.BlockCasting;

public final class ChickenNuggetFluids {
	
	public static final class RegistryNames {
		private RegistryNames() {}
		
		public static final String CHICKEN_FLUID = "chicken";
	}
	
	public static final Fluid CHICKEN_FLUID = new Fluid(RegistryNames.CHICKEN_FLUID, new ResourceLocation(ChickenNugget.MODID, "fluid_chicken"), new ResourceLocation(ChickenNugget.MODID, "fluid_chicken"));
	
	public void registerFluids() {
		FluidRegistry.registerFluid(CHICKEN_FLUID);
		FluidRegistry.addBucketForFluid(CHICKEN_FLUID);
	}
	
	public void addFluids(NonNullList<ItemStack> itemList) {
		itemList.add(FluidUtil.getFilledBucket(new FluidStack(CHICKEN_FLUID, 1000)));
	}
	
	public void registerTConRecipes() {
		TinkerRegistry.registerMelting(ChickenNuggetItems.RAW_NUGGET, CHICKEN_FLUID, 16);
		TinkerRegistry.registerMelting(ChickenNuggetItems.COOKED_NUGGET, CHICKEN_FLUID, 16);
		TinkerRegistry.registerTableCasting(new ItemStack(ChickenNuggetItems.COOKED_NUGGET), TinkerSmeltery.castNugget, CHICKEN_FLUID, 16);
		TinkerRegistry.registerBasinCasting(new ChickenCastingRecipe());
		//144mb, killed by ChickenDeathEvents
		TinkerRegistry.registerEntityMelting(EntityChicken.class, new FluidStack(CHICKEN_FLUID, 144));
		new ChickenDeathEvents(); // Register death events
	}
	
	public void registerJEIRecipes(IModRegistry registry) {
		registry.addRecipes(Collections.singletonList(new RecipeWrapperCastChicken()), RecipeCategoryCastChicken.UID);
		registry.addRecipeCatalyst(new ItemStack(TinkerSmeltery.castingBlock, 1, BlockCasting.CastingType.BASIN.meta), RecipeCategoryCastChicken.UID);
		registry.addRecipes(Collections.singletonList(new RecipeWrapperMeltChicken()), RecipeCategoryMeltChicken.UID);
		registry.addRecipeCatalyst(new ItemStack(TinkerSmeltery.smelteryController), RecipeCategoryMeltChicken.UID);
	}

	public void registerJEIRecipeCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new RecipeCategoryCastChicken());
		registry.addRecipeCategories(new RecipeCategoryMeltChicken());
	}
	
}
