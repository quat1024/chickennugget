package quaternary.chickennugget;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.IForgeRegistry;

public final class ChickenNuggetFluids {
	public static final class RegistryNames {
		private RegistryNames() {}
		
		public static final String CHICKEN_FLUID = "chicken";
		public static final String CHICKEN_FLUID_BLOCK = "fluid_chicken";
	}
	
	public static Fluid chickenFluid = null;
	public static BlockFluidClassic chickenFluidBlock = null;
	
	public static void registerFluids() {
		chickenFluid = FluidRegistry.getFluid(RegistryNames.CHICKEN_FLUID);
		if(chickenFluid == null) {
			chickenFluid = new Fluid(
				RegistryNames.CHICKEN_FLUID,
				new ResourceLocation(ChickenNugget.MODID, "chicken_still"),
				new ResourceLocation(ChickenNugget.MODID, "chicken_flowing")
			);
			chickenFluid.setViscosity(2000);
			chickenFluid.setDensity(2000);
			chickenFluid.setTemperature(600);
			
			FluidRegistry.registerFluid(chickenFluid);
			FluidRegistry.addBucketForFluid(chickenFluid);
		}
	}
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		//Oh look putting half the registerBlocks
		chickenFluidBlock = new BlockFluidChicken();
		chickenFluidBlock.setRegistryName(new ResourceLocation(ChickenNugget.MODID, RegistryNames.CHICKEN_FLUID_BLOCK));
		chickenFluidBlock.setTranslationKey(ChickenNugget.MODID + '.' + RegistryNames.CHICKEN_FLUID_BLOCK);
		reg.register(chickenFluidBlock);
	}
	
	public static void populateCreativeTabWithFluids(NonNullList<ItemStack> itemList) {
		itemList.add(FluidUtil.getFilledBucket(new FluidStack(chickenFluid, 1000)));
	}
}
