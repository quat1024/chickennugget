package quaternary.chickennugget.block;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import quaternary.chickennugget.ChickenNuggetFluids;

public class BlockFluidChicken extends BlockFluidClassic {
	public BlockFluidChicken() {
		super(ChickenNuggetFluids.chickenFluid, Material.WATER);
	}
}
