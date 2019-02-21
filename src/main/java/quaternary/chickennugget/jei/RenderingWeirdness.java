package quaternary.chickennugget.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import quaternary.chickennugget.ChickenNuggetCommonEvents;

public class RenderingWeirdness {
	private RenderingWeirdness() {}
	
	private static final EntityChicken chicky = new EntityChicken(null);
	
	public static void drawChicken(int x, int y, boolean small) {
		Minecraft mc = Minecraft.getMinecraft();
		chicky.world = mc.world;
		
		//Based a little bit on Just Enough Resource's gui entity renderer.
		GlStateManager.pushMatrix();
		GlStateManager.enableColorMaterial();
		GlStateManager.translate(x, y, 120);
		if (small) {
			GlStateManager.scale(20, -20, 20);
		} else {
			GlStateManager.scale(40, -40, 40);
		}
		GlStateManager.rotate(30f, 1f, 0f, 0f);
		GlStateManager.rotate(45f, 0f, 1f, 0f);
		RenderHelper.enableStandardItemLighting();
		
		mc.getRenderManager().renderEntity(chicky, 0, 0, 0, 0, 0, false);
		
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.popMatrix();
	}
	
	public static void drawHeadlessChicken(int x, int y) {
		chicky.addTag(ChickenNuggetCommonEvents.headlessTag);
		drawChicken(x, y, false);
		chicky.getTags().remove(ChickenNuggetCommonEvents.headlessTag);
	}
	
	public static void drawCraftingTable(int x, int y) {		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		GlStateManager.scale(2.7, 2.7, 1);
		RenderHelper.enableStandardItemLighting();
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(Blocks.CRAFTING_TABLE), 0, 0);
		RenderHelper.disableStandardItemLighting();
		
		GlStateManager.popMatrix();
	}
}
