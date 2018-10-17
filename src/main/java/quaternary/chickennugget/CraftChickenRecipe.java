package quaternary.chickennugget;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class CraftChickenRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	//Craft up to 64 chickens at once
	public static final int MAXIMUM_CHICKENS_CRAFTED = 64;
	
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		int nugCount = 0;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() == ChickenNuggetItems.RAW_NUGGET) nugCount++;
			else return false;
		}
		
		return nugCount == 9;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		//Anyway this is really stupid and sideeffecty.
		//Access transformers 5 annoying 27 me
		Container container = ReflectionHelper.getPrivateValue(InventoryCrafting.class, inv, "eventHandler", "field_70465_c");
		if(container == null || !(container instanceof ContainerWorkbench)) return ItemStack.EMPTY;
		ContainerWorkbench table = (ContainerWorkbench) container;
		
		BlockPos pos = ReflectionHelper.getPrivateValue(ContainerWorkbench.class, table, "pos", "field_178145_h");
		World world = ReflectionHelper.getPrivateValue(ContainerWorkbench.class, table, "world", "field_75161_g");
		if(pos == null || world == null) return ItemStack.EMPTY; //both are needed to spawn the chicken
		
		EntityPlayerMP player = ReflectionHelper.getPrivateValue(ContainerWorkbench.class, table, "player", "field_192390_i");
		
		//Check minimum number of items in table
		int chickenCount = MAXIMUM_CHICKENS_CRAFTED;
		for(int i = 0; i < 9; i++) {
			if(table.craftMatrix.getStackInSlot(i).getCount() < chickenCount) {
				chickenCount = table.craftMatrix.getStackInSlot(i).getCount();
			}
		}
		
		for(int i = 0; i < 9; i++) {
			table.craftMatrix.getStackInSlot(i).shrink(chickenCount);
			//Manually sync the slot since vanilla doesn't feel like doing it, apparently
			if(player != null) {
				player.connection.sendPacket(new SPacketSetSlot(player.currentWindowId, i + 1, table.craftMatrix.getStackInSlot(i)));
			}
		}
		
		if(!world.isRemote) {
			ChickenNuggetCommonEvents.markPositionAsNeedingNewChickens(world, pos, chickenCount);
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width * height == 9;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
}
