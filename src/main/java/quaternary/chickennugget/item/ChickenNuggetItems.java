package quaternary.chickennugget.item;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.block.ChickenNuggetBlocks;

import java.util.Objects;

@GameRegistry.ObjectHolder(ChickenNugget.MODID)
public final class ChickenNuggetItems {
	private ChickenNuggetItems() {}
	
	public static final class RegistryNames {
		private RegistryNames() {}
		
		public static final String RAW_NUGGET = "chicken_nugget";
		public static final String COOKED_NUGGET = "chicken_nugget_cooked";
	}
	
	@GameRegistry.ObjectHolder(RegistryNames.RAW_NUGGET)
	public static final ItemFood RAW_NUGGET = notNullISwear();
	
	@GameRegistry.ObjectHolder(RegistryNames.COOKED_NUGGET)
	public static final ItemFood COOKED_NUGGET = notNullISwear();
	
	@GameRegistry.ObjectHolder(ChickenNuggetBlocks.RegistryNames.CHICKEN_HEAD_BLOCK)
	public static final ItemChickenHead CHICKEN_HEAD = notNullISwear();
	
	public static void registerItems(IForgeRegistry<Item> reg) {
		registerItem(new ItemFastFood(1, 0.05f, true), RegistryNames.RAW_NUGGET, reg);
		registerItem(new ItemFastFood(3, 0.2f, true), RegistryNames.COOKED_NUGGET, reg);
		
		registerItemBlock(new ItemChickenHead(ChickenNuggetBlocks.CHICKEN_HEAD_BLOCK), reg);
	}
	
	private static void registerItem(Item i, String regName, IForgeRegistry<Item> reg) {
		i.setRegistryName(new ResourceLocation(ChickenNugget.MODID, regName));
		i.setTranslationKey(ChickenNugget.MODID + '.' + regName);
		i.setCreativeTab(ChickenNugget.TAB);
		
		reg.register(i);
	}
	
	private static void registerItemBlock(ItemBlock i, IForgeRegistry<Item> reg) {
		i.setRegistryName(Objects.requireNonNull(i.getBlock().getRegistryName()));
		i.setCreativeTab(ChickenNugget.TAB);
		
		reg.register(i);
	}
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T notNullISwear() {
		return null;
	}
}
