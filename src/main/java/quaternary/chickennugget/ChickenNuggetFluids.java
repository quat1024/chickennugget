package quaternary.chickennugget;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.IForgeRegistry;

final class ChickenNuggetFluids {
	public static final class RegistryNames {
		private RegistryNames() {}

		static final String CHICKEN_FLUID = "chicken";
		static final String CHICKEN_FLUID_FLOWING = "chicken_flowing";
		static final String CHICKEN_FLUID_BLOCK = "fluid_chicken";
		static final String CHICKEN_FLUID_BUCKET = "fluid_chicken_bucket";
	}

	private static FlowingFluid chickenFluid = null;
	private static FlowingFluid chickenFluidFlowing = null;
	private static FlowingFluidBlock chickenFluidBlock = null;
	private static BucketItem chickenFluidBucket = null;

	static void registerFluids(IForgeRegistry<Fluid> reg) {
		// TODO: figure out how to make it do collisions correctly, and burn entities like lava
		ForgeFlowingFluid.Properties props = new ForgeFlowingFluid.Properties(() -> chickenFluid, () -> chickenFluidFlowing,
				FluidAttributes.builder(new ResourceLocation(ChickenNugget.MODID, "chicken_still"), new ResourceLocation(ChickenNugget.MODID, "chicken_flowing"))
						.viscosity(2000)
						.density(2000)
						.temperature(600)
						.color(0xFFFFFFFF)
						.sound(SoundEvents.ENTITY_CHICKEN_HURT, SoundEvents.ENTITY_CHICKEN_DEATH))
				.bucket(() -> chickenFluidBucket)
				.block(() -> chickenFluidBlock)
				.levelDecreasePerBlock(2) //Slow like lava
				.slopeFindDistance(2);
		chickenFluid = new ForgeFlowingFluid.Source(props);
		chickenFluid.setRegistryName(new ResourceLocation(ChickenNugget.MODID, RegistryNames.CHICKEN_FLUID));
		reg.register(chickenFluid);
		chickenFluidFlowing = new ForgeFlowingFluid.Flowing(props);
		chickenFluidFlowing.setRegistryName(new ResourceLocation(ChickenNugget.MODID, RegistryNames.CHICKEN_FLUID_FLOWING));
		reg.register(chickenFluidFlowing);
	}

	static void registerBlocks(IForgeRegistry<Block> reg) {
		//Oh look putting half the registerBlocks
		chickenFluidBlock = new FlowingFluidBlock(() -> chickenFluid, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops());
		chickenFluidBlock.setRegistryName(new ResourceLocation(ChickenNugget.MODID, RegistryNames.CHICKEN_FLUID_BLOCK));
		reg.register(chickenFluidBlock);
	}

	static void registerItems(IForgeRegistry<Item> reg) {
		chickenFluidBucket = new BucketItem(() -> chickenFluid, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ChickenNugget.TAB));
		chickenFluidBucket.setRegistryName(new ResourceLocation(ChickenNugget.MODID, RegistryNames.CHICKEN_FLUID_BUCKET));
		reg.register(chickenFluidBucket);
	}
}
