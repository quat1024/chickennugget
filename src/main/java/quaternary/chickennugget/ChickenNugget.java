package quaternary.chickennugget;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.chickennugget.block.ChickenNuggetBlocks;
import quaternary.chickennugget.compat.curios.CuriosHandler;
import quaternary.chickennugget.item.ChickenNuggetItems;
import quaternary.chickennugget.net.PacketHandler;

@Mod(ChickenNugget.MODID)
@Mod.EventBusSubscriber(modid = ChickenNugget.MODID)
public class ChickenNugget {
	public static final String MODID = "chickennugget";
	private static final String NAME = "Chicken Nugget";

	public static final Logger LOGGER = LogManager.getLogger(NAME);

	@SuppressWarnings("WeakerAccess") // TiCon integration uses this
	public static boolean tinkersCompat = false;
	public static boolean curiosCompat = false;

	public ChickenNugget() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupIMC);

		tinkersCompat = ModList.get().isLoaded("tconstruct");
		curiosCompat = ModList.get().isLoaded("curios");
	}

	private void setupCommon(final FMLCommonSetupEvent event) {
		PacketHandler.registerMessages();
	}

	private void setupIMC(final InterModEnqueueEvent evt) {
		if (curiosCompat) {
			CuriosHandler.registerHeadSlot();
		}
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> e) {
			ChickenNuggetBlocks.registerBlocks(e.getRegistry());
			ChickenNuggetFluids.registerBlocks(e.getRegistry());
		}

		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> e) {
			ChickenNuggetItems.registerItems(e.getRegistry());
			ChickenNuggetFluids.registerItems(e.getRegistry());
		}

		@SubscribeEvent
		public static void onRecipesRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> e) {
			e.getRegistry().register(CraftChickenRecipe.SERIALIZER);
		}

		@SubscribeEvent
		public static void onFluidsRegistry(final RegistryEvent.Register<Fluid> e) {
			ChickenNuggetFluids.registerFluids(e.getRegistry());
		}
	}
	
	public static final ItemGroup TAB = new ItemGroup(MODID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ChickenNuggetItems.RAW_NUGGET);
		}
	};
	
//	@Mod.EventHandler
//	public static void preinit(FMLPreInitializationEvent e) {
//		if(Loader.isModLoaded("tconstruct")) {
//			TinkersCompat.preinit();
//		}
//	}
//
//	@Mod.EventHandler
//	public static void init(FMLInitializationEvent e) {
//		if(tinkersCompat) {
//			TinkersCompat.init();
//		}
//	}
}
