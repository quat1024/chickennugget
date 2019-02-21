package quaternary.chickennugget;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(ChickenNugget.MODID)
public final class ChickenNuggetBlocks {
	
	public static final class RegistryNames {
		private RegistryNames() {}
		
		public static final String CHICKEN_HEAD_BLOCK = "chicken_head";
	}
	
	@GameRegistry.ObjectHolder(RegistryNames.CHICKEN_HEAD_BLOCK)
	public static final BlockHeadChicken CHICKEN_HEAD_BLOCK = notNullISwear();
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		BlockHeadChicken chickenHeadBlock = new BlockHeadChicken();
		chickenHeadBlock.setRegistryName(new ResourceLocation(ChickenNugget.MODID, RegistryNames.CHICKEN_HEAD_BLOCK));
		chickenHeadBlock.setTranslationKey(ChickenNugget.MODID + '.' + RegistryNames.CHICKEN_HEAD_BLOCK);
		chickenHeadBlock.setCreativeTab(ChickenNugget.TAB);
		reg.register(chickenHeadBlock);
	}
	
	public static void registerItems(IForgeRegistry<Item> reg) {
		BlockHeadChicken.Item chickenHeadItemBlock = new BlockHeadChicken.Item(CHICKEN_HEAD_BLOCK);
		chickenHeadItemBlock.setRegistryName(new ResourceLocation(ChickenNugget.MODID, RegistryNames.CHICKEN_HEAD_BLOCK));
		reg.register(chickenHeadItemBlock);
	}
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T notNullISwear() {
		return null;
	}

}
