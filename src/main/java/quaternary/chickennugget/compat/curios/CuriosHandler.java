package quaternary.chickennugget.compat.curios;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.InterModComms;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.item.ChickenNuggetItems;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

public class CuriosHandler {
	
	public static boolean wearingChickenHead(PlayerEntity player) {
		if (!ChickenNugget.curiosCompat) {
			return false;
		}
		return CuriosAPI.getCurioEquipped(ChickenNuggetItems.CHICKEN_HEAD, player).isPresent();
	}

	public static void registerHeadSlot() {
		if (!ChickenNugget.curiosCompat) {
			return;
		}
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("head"));
	}

}
