package quaternary.chickennugget.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockChickenHead extends Block {
	
	BlockChickenHead() {
		super(Properties.create(Material.SAND).hardnessAndResistance(0.15F));
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Nonnull
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(BlockStateProperties.HORIZONTAL_FACING)) {
			case SOUTH:
				// 6, 0, 6 to 10, 6, 11 (translated)
				return Block.makeCuboidShape(6, 0, 6, 10, 6, 11);
			case WEST:
				// 5, 0, 6 to 10, 6, 10 (rotated)
				return Block.makeCuboidShape(5, 0, 6, 10, 6, 10);
			case EAST:
				// 6, 0, 6 to 11, 6, 10 (translated + rotated)
				return Block.makeCuboidShape(6, 0, 6, 11, 6, 10);
			default:
				// 6, 0, 5 to 10, 6, 10
				return Block.makeCuboidShape(6, 0, 5, 10, 6, 10);
		}
	}
}
