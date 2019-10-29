package quaternary.chickennugget.net;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.ai.AIHelpers;
import quaternary.chickennugget.compat.curios.CuriosHandler;
import quaternary.chickennugget.item.ItemChickenHead;

import java.util.List;
import java.util.function.Supplier;

public class PacketPlayCluck {
	void encode(@SuppressWarnings("unused") PacketBuffer buf) {}

	static PacketPlayCluck decode(@SuppressWarnings("unused") PacketBuffer buf) {
		return new PacketPlayCluck();
	}

	private static final int SCAN_RANGE = 4;

	void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();

			if (player != null) {
				boolean hasHead = false;
				for (ItemStack itemStack : player.getArmorInventoryList()) {
					if (itemStack.getItem() instanceof ItemChickenHead) {
						hasHead = true;
						break;
					}
				}
				if (!hasHead && ChickenNugget.curiosCompat) {
					hasHead = CuriosHandler.wearingChickenHead(player);
				}

				if (hasHead) {
					World world = player.getEntityWorld();
					List<Entity> scaredVillagers = world.getEntitiesWithinAABB(EntityType.VILLAGER,
							new AxisAlignedBB(player.getPosition()).grow(SCAN_RANGE), e -> true);
					//Scare the villagers
					scaredVillagers.stream()
							.filter(VillagerEntity.class::isInstance)
							.map(VillagerEntity.class::cast)
							.forEach(v -> AIHelpers.scareVillager(v, player));
					world.playSound(null, player.getPosition(), SoundEvents.ENTITY_CHICKEN_AMBIENT,
							SoundCategory.PLAYERS, 1, world.rand.nextFloat() * 0.5f + 0.75f);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public static void syncToServer() {
		PacketHandler.INSTANCE.send(PacketDistributor.SERVER.with(() -> null), new PacketPlayCluck());
	}
}
