package quaternary.chickennugget.compat.jei;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import quaternary.chickennugget.ChickenNuggetCommonEvents;

import java.util.WeakHashMap;

public class RenderingWeirdness {
	private RenderingWeirdness() {}

	//This is probably overkill..... but I like computeIfAbsent
	private static final WeakHashMap<ClientWorld, ChickenEntity> cachedChickens = new WeakHashMap<>();
	
	static void drawChicken(int x, int y, boolean small) {
		Minecraft mc = Minecraft.getInstance();
		ChickenEntity chicky = cachedChickens.computeIfAbsent(mc.world, EntityType.CHICKEN::create);
		if (chicky == null) return;
		
		//Based a little bit on Just Enough Resource's gui entity renderer.
		GlStateManager.pushMatrix();
		GlStateManager.enableColorMaterial();
		GlStateManager.translatef(x, y, 120);
		if (small) {
			GlStateManager.scalef(20, -20, 20);
		} else {
			GlStateManager.scalef(40, -40, 40);
		}
		GlStateManager.rotatef(30f, 1f, 0f, 0f);
		GlStateManager.rotatef(45f, 0f, 1f, 0f);
		RenderHelper.enableStandardItemLighting();

		mc.getRenderManager().renderEntity(chicky, 0, 0, 0, 0, 0, false);
		
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();

		GlStateManager.activeTexture(GLX.GL_TEXTURE1);
		GlStateManager.disableTexture();
		GlStateManager.activeTexture(GLX.GL_TEXTURE0);
		GlStateManager.popMatrix();
	}
	
	static void drawHeadlessChicken(int x, int y) {
		ChickenEntity chicky = cachedChickens.computeIfAbsent(Minecraft.getInstance().world, EntityType.CHICKEN::create);
		if (chicky == null) return;
		chicky.addTag(ChickenNuggetCommonEvents.headlessTag);
		drawChicken(x, y, false);
		chicky.getTags().remove(ChickenNuggetCommonEvents.headlessTag);
	}
	
	static void drawCraftingTable(int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.translatef(x, y, 0);
		GlStateManager.scalef(2.7f, 2.7f, 1);
		RenderHelper.enableStandardItemLighting();
		Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(new ItemStack(Blocks.CRAFTING_TABLE), 0, 0);
		RenderHelper.disableStandardItemLighting();
		
		GlStateManager.popMatrix();
	}
}
