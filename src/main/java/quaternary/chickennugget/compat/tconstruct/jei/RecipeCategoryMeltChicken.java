package quaternary.chickennugget.compat.tconstruct.jei;

import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.compat.jei.ChickenNuggetJEIPlugin;
import quaternary.chickennugget.compat.jei.RenderingWeirdness;
import slimeknights.tconstruct.plugin.jei.smelting.SmeltingRecipeCategory;

public class RecipeCategoryMeltChicken extends SmeltingRecipeCategory {
	
	public RecipeCategoryMeltChicken() {
		super(ChickenNuggetJEIPlugin.guiHelper);
	}

	public static final String UID = "chickennugget_melt_chicken";
	
	@Override
	public String getUid() {
		return UID;
	}
	
	@Override
	public String getTitle() {
		return I18n.translateToLocal("chickennugget.jei.melt_chicken.title");
	}
	
	@Override
	public String getModName() {
		return ChickenNugget.NAME;
	}
	
	@Override
	public void drawExtras(Minecraft mc) {
		super.drawExtras(mc);
		RenderingWeirdness.drawChicken(35, 35, true);
	}

	@Override
	public IDrawable getBackground() {
		return ChickenNuggetJEIPlugin.guiHelper.createDrawable(new ResourceLocation("tconstruct", "textures/gui/jei/smeltery.png"), 0, 0, 160, 60);
	}
}
