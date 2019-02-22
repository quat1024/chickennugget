package quaternary.chickennugget.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import quaternary.chickennugget.ChickenNuggetCommonEvents;

public class PacketUpdateChicken implements IMessage {
	private boolean isHeadless = false;
	private int entityId;

	@Override
	public void fromBytes(ByteBuf buf) {
		entityId = buf.readInt();
		isHeadless = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeBoolean(isHeadless);
	}

	public PacketUpdateChicken(EntityChicken entity, boolean isHeadless) {
		this.isHeadless = isHeadless;
		this.entityId = entity.getEntityId();
	}

	public PacketUpdateChicken() {
		// For client initialisation
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateChicken, IMessage> {
		@Override
		public IMessage onMessage(PacketUpdateChicken message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketUpdateChicken message, MessageContext ctx) {
			Entity ent = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
			
			if (ent instanceof EntityChicken) {
				if (message.isHeadless) {
					ent.addTag(ChickenNuggetCommonEvents.headlessTag);
				} else {
					ent.getTags().remove(ChickenNuggetCommonEvents.headlessTag);
				}
			}
		}
	}
	
	public static void syncToClients(EntityChicken entity) {
		boolean hasTag = entity.getTags().contains(ChickenNuggetCommonEvents.headlessTag);
		EntityTracker tracker = ((WorldServer) entity.getEntityWorld()).getEntityTracker();
		PacketUpdateChicken message = new PacketUpdateChicken(entity, hasTag);

		for (EntityPlayer player : tracker.getTrackingPlayers(entity)) {
			PacketHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
		}
	}
	
	public static void syncToClient(EntityChicken entity, EntityPlayer player) {
		boolean hasTag = entity.getTags().contains(ChickenNuggetCommonEvents.headlessTag);
		PacketHandler.INSTANCE.sendTo(new PacketUpdateChicken(entity, hasTag), (EntityPlayerMP)player);
	}
}