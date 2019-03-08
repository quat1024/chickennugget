package quaternary.chickennugget.compat.baubles;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.item.ChickenNuggetItems;

public class BaublesHandler {
	
	public static boolean wearingChickenHead(EntityPlayer player) {
		if (!ChickenNugget.baublesCompat) {
			return false;
		}
		return BaublesApi.isBaubleEquipped(player, ChickenNuggetItems.CHICKEN_HEAD) > -1;
	}

}
