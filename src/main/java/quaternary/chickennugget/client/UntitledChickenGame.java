package quaternary.chickennugget.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.net.PacketPlayCluck;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ChickenNugget.MODID)
public class UntitledChickenGame {
	private static final KeyBinding CLUCK = new KeyBinding("key.chickennugget.cluck", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW.GLFW_KEY_C), ChickenNugget.NAME);

	public static void setupClient() {
		ClientRegistry.registerKeyBinding(CLUCK);
	}

	@SubscribeEvent
	public static void keyPress(InputEvent.KeyInputEvent e) {
		if (CLUCK.isPressed()) {
			PacketPlayCluck.syncToServer();
		}
	}
}
