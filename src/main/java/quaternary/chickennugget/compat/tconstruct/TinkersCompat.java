package quaternary.chickennugget.compat.tconstruct;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import quaternary.chickennugget.ChickenNuggetFluids;
import quaternary.chickennugget.item.ChickenNuggetItems;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public final class TinkersCompat {
	public static void preinit() {
		MinecraftForge.EVENT_BUS.register(ChickenNuggetTinkersCommonEvents.class);
	}
	
	public static void init() {
		TinkerRegistry.registerMelting(ChickenNuggetItems.RAW_NUGGET, ChickenNuggetFluids.chickenFluid, 16);
		TinkerRegistry.registerMelting(ChickenNuggetItems.COOKED_NUGGET, ChickenNuggetFluids.chickenFluid, 16);
		TinkerRegistry.registerTableCasting(new ItemStack(ChickenNuggetItems.COOKED_NUGGET), TinkerSmeltery.castNugget, ChickenNuggetFluids.chickenFluid, 16);
		TinkerRegistry.registerBasinCasting(new ChickenCastingRecipe());
		//144mb, killed by ChickenNuggetTinkersCommonEvents
		TinkerRegistry.registerEntityMelting(EntityChicken.class, new FluidStack(ChickenNuggetFluids.chickenFluid, 144));
	}
}
