package quaternary.chickennugget;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

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
	
	static void registerItems(IForgeRegistry<Item> reg) {
		registerItem(new ItemFastFood(1, 0.05f, true), RegistryNames.RAW_NUGGET, reg);
		registerItem(new ItemFastFood(3, 0.2f, true), RegistryNames.COOKED_NUGGET, reg);
	}
	
	private static void registerItem(Item i, String regName, IForgeRegistry<Item> reg) {
		i.setRegistryName(new ResourceLocation(ChickenNugget.MODID, regName));
		i.setTranslationKey(ChickenNugget.MODID + '.' + regName);
		i.setCreativeTab(ChickenNugget.TAB);
		
		reg.register(i);
	}
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T notNullISwear() {
		return null;
	}
}
