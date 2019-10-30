package quaternary.chickennugget.net;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import quaternary.chickennugget.ChickenNugget;
import quaternary.chickennugget.ai.AIHelpers;
import quaternary.chickennugget.compat.curios.CuriosHandler;
import quaternary.chickennugget.item.ItemChickenHead;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

public class PacketPlayCluck {
	void encode(@SuppressWarnings("unused") PacketBuffer buf) {}

	static PacketPlayCluck decode(@SuppressWarnings("unused") PacketBuffer buf) {
		return new PacketPlayCluck();
	}

	private static final int SCAN_RANGE = 4;
	private static Method endermanTeleportRandomly = null;

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
					world.playSound(null, player.getPosition(), SoundEvents.ENTITY_CHICKEN_AMBIENT,
							SoundCategory.PLAYERS, 1, world.rand.nextFloat() * 0.5f + 0.75f);

					List<Entity> scaredVillagers = world.getEntitiesWithinAABB(EntityType.VILLAGER,
							new AxisAlignedBB(player.getPosition()).grow(SCAN_RANGE), e -> true);
					//Scare the villagers
					scaredVillagers.stream()
							.filter(VillagerEntity.class::isInstance)
							.map(VillagerEntity.class::cast)
							.forEach(v -> AIHelpers.scareVillager(v, player));

					List<Entity> scaredEndermen = world.getEntitiesWithinAABB(EntityType.ENDERMAN,
							new AxisAlignedBB(player.getPosition()).grow(SCAN_RANGE), e -> true);
					//Make endermen drop their blocks
					scaredEndermen.stream()
							.filter(EndermanEntity.class::isInstance)
							.map(EndermanEntity.class::cast)
							.forEach(e -> {
								BlockState existingBlock = e.getHeldBlockState();
								if (existingBlock != null) {
									//Set the block to null
									e.func_195406_b(null);
									ItemEntity item = new ItemEntity(world, e.posX, e.posY, e.posZ, new ItemStack(existingBlock.getBlock()));
									e.getEntityWorld().addEntity(item);

									//Make the enderman teleport randomly
									if (endermanTeleportRandomly == null) {
										endermanTeleportRandomly = ObfuscationReflectionHelper.findMethod(EndermanEntity.class, "func_70820_n");
									}
									try {
										endermanTeleportRandomly.invoke(e);
									} catch (IllegalAccessException | InvocationTargetException ex) {
										ChickenNugget.LOGGER.warn("Failed to make the enderman teleport randomly", ex);
									}
								}
							});
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public static void syncToServer() {
		PacketHandler.INSTANCE.send(PacketDistributor.SERVER.with(() -> null), new PacketPlayCluck());
	}
}
