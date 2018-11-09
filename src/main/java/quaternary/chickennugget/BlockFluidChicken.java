package quaternary.chickennugget;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockFluidChicken extends BlockFluidClassic {
	public BlockFluidChicken() {
		super(ChickenNuggetFluids.chickenFluid, Material.WATER);
	}
}
