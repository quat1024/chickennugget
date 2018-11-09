package quaternary.chickennugget;

import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ChickenNugget.MODID)
public class ChickenNuggetClientEvents {
	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		setSimpleModel(ChickenNuggetItems.RAW_NUGGET);
		setSimpleModel(ChickenNuggetItems.COOKED_NUGGET);
		
		ModelLoader.setCustomStateMapper(ChickenNuggetFluids.chickenFluidBlock, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());
	}
	
	private static void setSimpleModel(Item i) {
		ResourceLocation res = Preconditions.checkNotNull(i.getRegistryName());
		ModelResourceLocation mrl = new ModelResourceLocation(res, "inventory");
		ModelLoader.setCustomModelResourceLocation(i, 0, mrl);
	}
}
