package quaternary.chickennugget;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.chickennugget.tconstruct.TinkersCompat;

@Mod(modid = ChickenNugget.MODID, name = ChickenNugget.NAME, version = ChickenNugget.VERSION)
@Mod.EventBusSubscriber(modid = ChickenNugget.MODID)
public class ChickenNugget {
	public static final String MODID = "chickennugget";
	public static final String NAME = "Chicken Nugget";
	public static final String VERSION = "GRADLE:VERSION";
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	public static boolean tinkersCompat = false;
	
	public static final CreativeTabs TAB = new CreativeTabs(MODID) {
		@SideOnly(Side.CLIENT)
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ChickenNuggetItems.RAW_NUGGET);
		}
		
		@SideOnly(Side.CLIENT)
		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {
			super.displayAllRelevantItems(itemList);
			
			ChickenNuggetFluids.populateCreativeTabWithFluids(itemList);
		}
	};
	
	@SubscribeEvent
	public static void blocks(RegistryEvent.Register<Block> e) {
		ChickenNuggetFluids.registerBlocks(e.getRegistry());
	}
	
	@SubscribeEvent
	public static void items(RegistryEvent.Register<Item> e) {
		ChickenNuggetItems.registerItems(e.getRegistry());
	}
	
	@Mod.EventHandler
	public static void preinit(FMLPreInitializationEvent e) {
		ChickenNuggetFluids.registerFluids();
		
		if(Loader.isModLoaded("tconstruct")) {
			tinkersCompat = true;
			TinkersCompat.preinit();
		}
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(ChickenNuggetItems.RAW_NUGGET), new ItemStack(ChickenNuggetItems.COOKED_NUGGET), 0.1f);
		
		if(tinkersCompat) {
			TinkersCompat.init();
		}
	}
	
	@SubscribeEvent
	public static void irecipes(RegistryEvent.Register<IRecipe> e) {
		IForgeRegistry<IRecipe> reg = e.getRegistry();
		
		reg.register(new CraftChickenRecipe().setRegistryName(new ResourceLocation(MODID, "craft_chicken")));
	}
}
