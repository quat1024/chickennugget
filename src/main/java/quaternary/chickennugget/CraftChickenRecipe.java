package quaternary.chickennugget;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.network.play.server.SWindowItemsPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import quaternary.chickennugget.item.ChickenNuggetItems;

import javax.annotation.Nonnull;

public class CraftChickenRecipe extends SpecialRecipe {
	//Craft up to 64 chickens at once
	private static final int MAXIMUM_CHICKENS_CRAFTED = 64;

	static final IRecipeSerializer<?> SERIALIZER = new SpecialRecipeSerializer<>(CraftChickenRecipe::new).setRegistryName(new ResourceLocation(ChickenNugget.MODID, "chicken_crafting"));

	private CraftChickenRecipe(ResourceLocation idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(CraftingInventory inv, @Nonnull World world) {
		int nugCount = 0;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() == ChickenNuggetItems.RAW_NUGGET) nugCount++;
			else return false;
		}

		return nugCount == 9;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		//Anyway this is really stupid and sideeffecty.
		//Access transformers 5 annoying 27 me
		Container container;
		try {
			container = (Container) ObfuscationReflectionHelper.findField(CraftingInventory.class, "field_70465_c").get(inv);
		} catch (IllegalAccessException | NullPointerException e) {
			return ItemStack.EMPTY;
		}
		if(!(container instanceof WorkbenchContainer)) return ItemStack.EMPTY;
		WorkbenchContainer table = (WorkbenchContainer) container;

		IWorldPosCallable posGetter;
		try {
			posGetter = (IWorldPosCallable) ObfuscationReflectionHelper.findField(WorkbenchContainer.class, "field_217070_e").get(table);
		} catch (IllegalAccessException | NullPointerException e) {
			return ItemStack.EMPTY;
		}

		ServerPlayerEntity player;
		try {
			player = (ServerPlayerEntity) ObfuscationReflectionHelper.findField(WorkbenchContainer.class, "field_192390_i").get(table);
		} catch (IllegalAccessException | NullPointerException e) {
			player = null;
		}

		//Check minimum number of items in table
		int chickenCount = MAXIMUM_CHICKENS_CRAFTED;
		for(int i = 0; i < 9; i++) {
			if (inv.getStackInSlot(i).getCount() < chickenCount) {
				chickenCount = inv.getStackInSlot(i).getCount();
			}
		}

		for(int i = 0; i < 9; i++) {
			//Use getStackInSlot rather than decrStackSize as it sends useless packets (to clear/set the result)
			inv.getStackInSlot(i).split(chickenCount);
		}
		//Manually sync the slots since vanilla doesn't feel like doing it, apparently
		if(player != null) {
			player.connection.sendPacket(new SWindowItemsPacket(table.windowId, table.getInventory()));
		}

		int finalChickenCount = chickenCount;
		posGetter.consume((world, pos) -> {
			if(!world.isRemote) {
				ChickenNuggetCommonEvents.markPositionAsNeedingNewChickens(world, pos, finalChickenCount);
			}
		});

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height == 9;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
