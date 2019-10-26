package quaternary.chickennugget.client;

import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.ChickenNuggetCommonEvents;
import quaternary.chickennugget.compat.curios.CuriosHandler;
import quaternary.chickennugget.compat.curios.PlayerLayerHeadCurio;
import quaternary.chickennugget.item.ItemChickenHead;

import java.lang.reflect.Field;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber
public class ChickenNuggetClientEvents {
	private static boolean hasAddedLayer = false;
	public static final String wearingChickenHeadTag = "wearingChickenHead";
	
	@SubscribeEvent
	public static void headlessPlayers(RenderPlayerEvent.Pre e) {
		PlayerEntity player = e.getPlayer();
		boolean hasHead = false;
		for (ItemStack itemStack : player.getArmorInventoryList()) {
			if (itemStack.getItem() instanceof ItemChickenHead) {
				hasHead = true;
				break;
			}
		}
		if (!hasHead && ChickenNugget.curiosCompat) {
			hasHead = CuriosHandler.wearingChickenHead(player);
			if (hasHead && !hasAddedLayer) {
				e.getRenderer().addLayer(new PlayerLayerHeadCurio(e.getRenderer()));
				hasAddedLayer = true;
			}
		}
		
		e.getRenderer().getEntityModel().bipedHead.isHidden = hasHead;
		
		if (hasHead) {
			player.addTag(wearingChickenHeadTag);
		} else {
			player.removeTag(wearingChickenHeadTag);
		}
	}
	
	@SubscribeEvent
	public static void headlessChickens(RenderLivingEvent.Pre<ChickenEntity, ChickenModel<ChickenEntity>> e) {
		if (!(e.getEntity() instanceof ChickenEntity)) return;
		ChickenModel<ChickenEntity> model = e.getRenderer().getEntityModel();
		ChickenModelReflectionHandler handler = chickenModelReflectionHandlerWeakHashMap.computeIfAbsent(model, ChickenModelReflectionHandler::new);

		if (e.getEntity().getTags().contains(ChickenNuggetCommonEvents.headlessTag)) {
			handler.setHidden(true);
		} else {
			handler.setHidden(false);
		}
	}

	//intellij named this for me
	private static WeakHashMap<ChickenModel, ChickenModelReflectionHandler> chickenModelReflectionHandlerWeakHashMap = new WeakHashMap<>();

	private static class ChickenModelReflectionHandler {
		private static Field head;
		private static Field bill;
		private static Field chin;

		//i believe holding references to these won't cause a memory leak (if the ChickenModel ever gets GC'd)
		//as they don't reference the ChickenModel, so if the ChickenModel gets GC'd the WeakHashMap will discard this
		private RendererModel headModel;
		private RendererModel billModel;
		private RendererModel chinModel;

		ChickenModelReflectionHandler(ChickenModel m) {
			if (head == null) {
				head = ObfuscationReflectionHelper.findField(ChickenModel.class, "field_78142_a");
			}
			if (bill == null) {
				bill = ObfuscationReflectionHelper.findField(ChickenModel.class, "field_78143_h");
			}
			if (chin == null) {
				chin = ObfuscationReflectionHelper.findField(ChickenModel.class, "field_78137_g");
			}
			try {
				headModel = (RendererModel) head.get(m);
				billModel = (RendererModel) bill.get(m);
				chinModel = (RendererModel) chin.get(m);
			} catch (IllegalAccessException e) {
				ChickenNugget.LOGGER.warn("Failed to retrieve ChickenModel parts", e);
			}
		}

		void setHidden(boolean isHidden) {
			if (headModel == null) return;
			headModel.isHidden = isHidden;
			billModel.isHidden = isHidden;
			chinModel.isHidden = isHidden;
		}
	}
}
