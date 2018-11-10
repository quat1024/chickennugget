package quaternary.chickennugget;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHeadChicken extends Block {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public static class Item extends ItemBlock {
		public Item(BlockHeadChicken block) {
			super(block);
		}
		
		@Override
		public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
			return armorType == EntityEquipmentSlot.HEAD;
		}
		
		// Allow heads to be placed back onto chickens
		@Override
		public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
			if (!(target instanceof EntityChicken)) return false;
			
			if (target.getEntityWorld().isRemote) return false;

			if (target.getTags().contains(ChickenNuggetCommonEvents.headlessTag)) {
				target.getTags().remove(ChickenNuggetCommonEvents.headlessTag);
				PacketUpdateChicken.syncToClients((EntityChicken) target);
				target.getEntityWorld().playSound(null, target.posX, target.posY, target.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, .5f, 1.0F);
				stack.shrink(1);
				return true;
			} else {
				return false;
			}
		}
	}

	public BlockHeadChicken() {
		super(Material.SAND);
		setHardness(0.15F);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}

	public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
		return EnumFacing.getFacingFromVector(
				(float) (entity.posX - clickedBlock.getX()),
				(float) (entity.posY - clickedBlock.getY()),
				(float) (entity.posZ - clickedBlock.getZ()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.byIndex((meta & 3) + 2));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		// -2 because up/down not allowed
		return state.getValue(FACING).getIndex()-2;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		switch (state.getValue(FACING)) {
		case SOUTH:
			// 6, 0, 6 to 10, 6, 11 (translated)
			return new AxisAlignedBB(0.375, 0, 0.375, 0.625, 0.375, 0.6875);
		case WEST:
			// 5, 0, 6 to 10, 6, 10 (rotated)
			return new AxisAlignedBB(0.3125, 0, 0.375, 0.625, 0.375, 0.625);
		case EAST:
			// 6, 0, 6 to 11, 6, 10 (translated + rotated)
			return new AxisAlignedBB(0.375, 0, 0.375, 0.6875, 0.375, 0.625);
		default:
			// 6, 0, 5 to 10, 6, 10
			return new AxisAlignedBB(0.375, 0, 0.3125, 0.625, 0.375, 0.625);
		}
	}
}
