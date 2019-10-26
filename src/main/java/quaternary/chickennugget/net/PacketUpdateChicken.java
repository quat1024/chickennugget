package quaternary.chickennugget.net;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.chickennugget.ChickenNuggetCommonEvents;

import java.util.function.Supplier;

public class PacketUpdateChicken {
	private static final Logger LOGGER = LogManager.getLogger();

	private final boolean isHeadless;
	private final int entityId;

	private PacketUpdateChicken(ChickenEntity entity, boolean isHeadless) {
		this.isHeadless = isHeadless;
		this.entityId = entity.getEntityId();
	}

	private PacketUpdateChicken(int entityId, boolean isHeadless) {
		this.isHeadless = isHeadless;
		this.entityId = entityId;
	}

	void encode(PacketBuffer buf) {
		buf.writeInt(entityId);
		buf.writeBoolean(isHeadless);
	}

	static PacketUpdateChicken decode(PacketBuffer buf) {
		int entityId = buf.readInt();
		boolean isHeadless = buf.readBoolean();
		return new PacketUpdateChicken(entityId, isHeadless);
	}

	void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Entity ent = Minecraft.getInstance().world.getEntityByID(entityId);

			if (ent instanceof ChickenEntity) {
				// TODO: change to a Capability?
				if (isHeadless) {
					ent.addTag(ChickenNuggetCommonEvents.headlessTag);
				} else {
					ent.getTags().remove(ChickenNuggetCommonEvents.headlessTag);
				}
			} else {
				LOGGER.warn("Server tried to modify invalid chicken!");
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public static void syncToClients(ChickenEntity entity) {
		boolean hasTag = entity.getTags().contains(ChickenNuggetCommonEvents.headlessTag);
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new PacketUpdateChicken(entity, hasTag));
	}

	public static void syncToClient(ChickenEntity entity, PlayerEntity player) {
		boolean hasTag = entity.getTags().contains(ChickenNuggetCommonEvents.headlessTag);
		PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketUpdateChicken(entity, hasTag));
	}
}