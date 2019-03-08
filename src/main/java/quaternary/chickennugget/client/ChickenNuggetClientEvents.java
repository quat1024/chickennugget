package quaternary.chickennugget.client;

import java.util.Iterator;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.ChickenNuggetCommonEvents;
import quaternary.chickennugget.ChickenNuggetFluids;
import quaternary.chickennugget.block.ChickenNuggetBlocks;
import quaternary.chickennugget.compat.baubles.BaublesHandler;
import quaternary.chickennugget.compat.baubles.PlayerLayerHeadBauble;
import quaternary.chickennugget.item.ChickenNuggetItems;
import quaternary.chickennugget.item.ItemChickenHead;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ChickenNugget.MODID)
public class ChickenNuggetClientEvents {
	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		setSimpleModel(ChickenNuggetItems.RAW_NUGGET);
		setSimpleModel(ChickenNuggetItems.COOKED_NUGGET);
		setSimpleModel(ChickenNuggetBlocks.CHICKEN_HEAD_BLOCK);
		
		ModelLoader.setCustomStateMapper(ChickenNuggetFluids.chickenFluidBlock, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());
	}
	
	private static void setSimpleModel(Item i) {
		ResourceLocation res = Preconditions.checkNotNull(i.getRegistryName());
		ModelResourceLocation mrl = new ModelResourceLocation(res, "inventory");
		ModelLoader.setCustomModelResourceLocation(i, 0, mrl);
	}
	
	private static void setSimpleModel(Block i) {
		ResourceLocation res = Preconditions.checkNotNull(i.getRegistryName());
		ModelResourceLocation mrl = new ModelResourceLocation(res, "inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(i), 0, mrl);
	}
	
	private static boolean hasAddedLayer = false;
	public static final String wearingChickenHeadTag = "wearingChickenHead";
	
	@SubscribeEvent
	public static void headlessPlayers(RenderPlayerEvent.Pre e) {
		EntityPlayer player = e.getEntityPlayer();
		boolean hasHead = false;
		Iterator<ItemStack> iter = player.getArmorInventoryList().iterator();
		while (iter.hasNext()) {
			if (iter.next().getItem() instanceof ItemChickenHead) {
				hasHead = true;
				break;
			}
		}
		if (!hasHead && ChickenNugget.baublesCompat) {
			hasHead = BaublesHandler.wearingChickenHead(player);
			if (hasHead && !hasAddedLayer) {
				e.getRenderer().addLayer(new PlayerLayerHeadBauble(e.getRenderer().getMainModel().bipedHead));
				hasAddedLayer = true;
			}
		}
		
		e.getRenderer().getMainModel().bipedHead.isHidden = hasHead;
		
		if (hasHead) {
			player.addTag(wearingChickenHeadTag);
		} else {
			player.removeTag(wearingChickenHeadTag);
		}
	}
	
	@SubscribeEvent
	public static void headlessChickens(RenderLivingEvent.Pre<EntityChicken> e) {
		if (!(e.getRenderer() instanceof RenderChicken)) return;
		RenderChicken render = (RenderChicken) e.getRenderer();
		ModelChicken model = (ModelChicken) render.getMainModel();
		if (e.getEntity().getTags().contains(ChickenNuggetCommonEvents.headlessTag)) {
			model.head.isHidden = true;
			model.bill.isHidden = true;
			model.chin.isHidden = true;
		} else {
			model.head.isHidden = false;
			model.bill.isHidden = false;
			model.chin.isHidden = false;
		}
	}
}
