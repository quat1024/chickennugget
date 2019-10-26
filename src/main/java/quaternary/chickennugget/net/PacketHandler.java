package quaternary.chickennugget.net;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import quaternary.chickennugget.ChickenNugget;

public class PacketHandler {
	private static int packetId = 0;

	private static final String PROTOCOL_VERSION = "1";
	static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(ChickenNugget.MODID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private PacketHandler() {}

	public static void registerMessages() {
		INSTANCE.registerMessage(packetId++, PacketUpdateChicken.class, PacketUpdateChicken::encode, PacketUpdateChicken::decode, PacketUpdateChicken::handle);
	}
}