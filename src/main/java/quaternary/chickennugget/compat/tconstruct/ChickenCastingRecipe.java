package quaternary.chickennugget.compat.tconstruct;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import quaternary.chickennugget.ChickenNuggetFluids;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;

public class ChickenCastingRecipe implements ICastingRecipe {
	public static CastingRecipe getDummyCastingRecipe() {
		// Dummy ItemStack as it does not allow ItemStack.EMPTY
		return new CastingRecipe(new ItemStack(Items.SNOWBALL), ChickenNuggetFluids.chickenFluid, 144, 30);
	}
	
	@Override
	public ItemStack getResult(ItemStack cast, Fluid fluid) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(ItemStack cast, Fluid fluid) {
		return ChickenNuggetFluids.chickenFluid == fluid;
	}

	@Override
	public boolean switchOutputs() {
		return false;
	}

	@Override
	public boolean consumesCast() {
		return false;
	}

	@Override
	public int getTime() {
		return 30;
	}

	@Override
	public int getFluidAmount() {
		return 144;
	}
}
