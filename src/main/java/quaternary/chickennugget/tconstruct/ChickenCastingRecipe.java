package quaternary.chickennugget.tconstruct;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.chickennugget.ChickenNuggetCommonEvents;
import quaternary.chickennugget.ChickenNuggetCommonEvents.SpawnType;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.smeltery.events.TinkerCastingEvent;

public class ChickenCastingRecipe implements ICastingRecipe {
	
	public static CastingRecipe getDummyCastingRecipe() {
		// Dummy ItemStack as it does not allow ItemStack.EMPTY
		return new CastingRecipe(new ItemStack(Items.SNOWBALL), ChickenNuggetFluids.CHICKEN_FLUID, 144, 30);
	}
	
	public ChickenCastingRecipe() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void castedChicken(TinkerCastingEvent.OnCasted event) {
		if (equals(event.recipe) && event.tile != null) {
			World world = event.tile.getWorld();
			if(!world.isRemote) {
				ChickenNuggetCommonEvents.markPositionAsNeedingNewChickens(world, event.tile.getPos(), 1, SpawnType.CASTED);
			}
		}
	}
	
	@Override
	public ItemStack getResult(ItemStack cast, Fluid fluid) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(ItemStack cast, Fluid fluid) {
		return ChickenNuggetFluids.CHICKEN_FLUID == fluid;
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
