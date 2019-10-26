package quaternary.chickennugget.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.block.ChickenNuggetBlocks;

import javax.annotation.Nonnull;
import java.util.Objects;

@ObjectHolder(ChickenNugget.MODID)
public final class ChickenNuggetItems {
	private ChickenNuggetItems() {}
	
	public static final class RegistryNames {
		private RegistryNames() {}
		
		static final String RAW_NUGGET = "chicken_nugget";
		static final String COOKED_NUGGET = "chicken_nugget_cooked";
	}
	
	@ObjectHolder(RegistryNames.RAW_NUGGET)
	public static final Item RAW_NUGGET = notNullISwear();
	private static final Food RAW_NUGGET_FOOD = (new Food.Builder()).hunger(1).saturation(0.05f).fastToEat().meat().build();
	
	@ObjectHolder(RegistryNames.COOKED_NUGGET)
	public static final Item COOKED_NUGGET = notNullISwear();
	private static final Food COOKED_NUGGET_FOOD = (new Food.Builder()).hunger(3).saturation(0.2f).fastToEat().meat().build();
	
	@ObjectHolder(ChickenNuggetBlocks.RegistryNames.CHICKEN_HEAD_BLOCK)
	public static final ItemChickenHead CHICKEN_HEAD = notNullISwear();
	
	public static void registerItems(IForgeRegistry<Item> reg) {
		Item.Properties props = new Item.Properties().group(ChickenNugget.TAB);

		registerItem(new Item(props.food(RAW_NUGGET_FOOD)), RegistryNames.RAW_NUGGET, reg);
		registerItem(new Item(props.food(COOKED_NUGGET_FOOD)), RegistryNames.COOKED_NUGGET, reg);
		
		registerItemBlock(new ItemChickenHead(ChickenNuggetBlocks.CHICKEN_HEAD_BLOCK, props), reg);
	}
	
	private static void registerItem(Item i, String regName, IForgeRegistry<Item> reg) {
		i.setRegistryName(regName);
		reg.register(i);
	}
	
	private static void registerItemBlock(BlockItem i, IForgeRegistry<Item> reg) {
		i.setRegistryName(Objects.requireNonNull(i.getBlock().getRegistryName()));
		reg.register(i);
	}
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T notNullISwear() {
		return null;
	}
}
