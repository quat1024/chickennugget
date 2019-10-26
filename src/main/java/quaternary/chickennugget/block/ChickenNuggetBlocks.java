package quaternary.chickennugget.block;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import quaternary.chickennugget.ChickenNugget;

import javax.annotation.Nonnull;

@ObjectHolder(ChickenNugget.MODID)
public final class ChickenNuggetBlocks {
	
	public static final class RegistryNames {
		private RegistryNames() {}
		
		public static final String CHICKEN_HEAD_BLOCK = "chicken_head";
	}
	
	@ObjectHolder(RegistryNames.CHICKEN_HEAD_BLOCK)
	public static final BlockChickenHead CHICKEN_HEAD_BLOCK = notNullISwear();
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		registerBlock(new BlockChickenHead(), RegistryNames.CHICKEN_HEAD_BLOCK, reg);
	}

	private static void registerBlock(Block b, String regName, IForgeRegistry<Block> reg) {
		b.setRegistryName(new ResourceLocation(ChickenNugget.MODID, regName));
		reg.register(b);
	}
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T notNullISwear() {
		return null;
	}
}
