package quaternary.chickennugget.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.item.ItemChickenHead;

@GameRegistry.ObjectHolder(ChickenNugget.MODID)
public final class ChickenNuggetBlocks {
	
	public static final class RegistryNames {
		private RegistryNames() {}
		
		public static final String CHICKEN_HEAD_BLOCK = "chicken_head";
	}
	
	@GameRegistry.ObjectHolder(RegistryNames.CHICKEN_HEAD_BLOCK)
	public static final BlockChickenHead CHICKEN_HEAD_BLOCK = notNullISwear();
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		registerBlock(new BlockChickenHead(), RegistryNames.CHICKEN_HEAD_BLOCK, reg);
	}
	
	private static void registerBlock(Block b, String regName, IForgeRegistry<Block> reg) {
		b.setRegistryName(new ResourceLocation(ChickenNugget.MODID, regName));
		b.setTranslationKey(ChickenNugget.MODID + '.' + regName);
		b.setCreativeTab(ChickenNugget.TAB);
		
		reg.register(b);
	}
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T notNullISwear() {
		return null;
	}
}
