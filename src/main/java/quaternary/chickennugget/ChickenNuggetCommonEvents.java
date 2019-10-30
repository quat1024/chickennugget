package quaternary.chickennugget;

import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import quaternary.chickennugget.ai.AIHelpers;
import quaternary.chickennugget.block.ChickenNuggetBlocks;
import quaternary.chickennugget.compat.curios.CuriosHandler;
import quaternary.chickennugget.item.ChickenNuggetItems;
import quaternary.chickennugget.item.ItemChickenHead;
import quaternary.chickennugget.net.PacketUpdateChicken;

import java.util.*;

@Mod.EventBusSubscriber(modid = ChickenNugget.MODID)
public class ChickenNuggetCommonEvents {
	public enum SpawnType {
		CRAFTED,
		CASTED
	}

	public static class ChickenPosition {
		final BlockPos pos;
		final int count;
		final SpawnType type;
		ChickenPosition(BlockPos pos, int count, SpawnType type) {
			this.pos = pos;
			this.count = count;
			this.type = type;
		}
	}
	
	private static final Map<World, List<ChickenPosition>> positionsNeedingChickens = new WeakHashMap<>();

	@SuppressWarnings("WeakerAccess") // TiCon integration uses this
	public static void markPositionAsNeedingNewChickens(World world, BlockPos pos, int count) {
		markPositionAsNeedingNewChickens(world, pos, count, SpawnType.CRAFTED);
	}

	@SuppressWarnings("WeakerAccess") // TiCon integration uses this
	public static void markPositionAsNeedingNewChickens(World world, BlockPos pos, int count, SpawnType type) {
		positionsNeedingChickens.computeIfAbsent(world, (w) -> new ArrayList<>(1)).add(new ChickenPosition(pos, count, type));
	}
	
	private static final String craftedTag = "CraftedChicken";
	private static final String castedTag = "CastedChicken";
	public static final String headlessTag = "HeadlessChicken";

	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent e) {
		if(e.phase != TickEvent.Phase.END) return;
		if(e.world.isRemote) return;
		ServerWorld world = (ServerWorld) e.world;
		
		world.getProfiler().startSection("chickennuggets");

		int cramming = world.getGameRules().getInt(GameRules.MAX_ENTITY_CRAMMING);
		
		if(!positionsNeedingChickens.isEmpty() && positionsNeedingChickens.containsKey(world)) {
			for(ChickenPosition cPos : positionsNeedingChickens.remove(world)) {
				BlockPos pos = cPos.pos;
				int count = cPos.count;
				SpawnType type = cPos.type;
				
				boolean lots = count >= cramming;
				
				for(int i = 0; i < count; i++) {
					ChickenEntity chicken = EntityType.CHICKEN.create(world);
					if (chicken == null) {
						// chicken factory broke
						continue;
					}
					
					double angle = world.rand.nextDouble() * Math.PI * 2;
					double xOff = Math.cos(angle) * 0.2;
					double zOff = Math.sin(angle) * 0.2;
					
					if(type == SpawnType.CASTED) {
						chicken.setLocationAndAngles(pos.getX() + .5 + xOff, pos.getY(), pos.getZ() + .5 + zOff, 90 + (float) (angle * 180 / Math.PI), 0);
					} else {
						chicken.setLocationAndAngles(pos.getX() + .5 + xOff, pos.getY() + 1, pos.getZ() + .5 + zOff, 90 + (float) (angle * 180 / Math.PI), 0);
					}
					
					if(type == SpawnType.CRAFTED) {
						chicken.addTag(craftedTag);
					} else if(type == SpawnType.CASTED) {
						chicken.setMotion(chicken.getMotion().add(0, 0.5, 0));
						chicken.noClip = true;
						chicken.addTag(castedTag);
					}
					
					if(lots) {
						//So they don't immediately cram themselves to death lmao
						chicken.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 5, 5));
						//Boing!!!
						chicken.setMotion(chicken.getMotion().add(0, 0.2, 0));
					}
					
					world.addEntity(chicken);
					
					world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, 1, world.rand.nextFloat() * 0.5f + 0.75f);
				}
			}
		}
		
		for(Entity ent : world.getEntities(EntityType.CHICKEN, ent -> true)) {
			boolean hasCraftedTag = ent.getTags().contains(craftedTag);
			boolean hasHeadlessTag = ent.getTags().contains(headlessTag);
			boolean onTable = world.getBlockState(ent.getPosition().down()).getBlock() == Blocks.CRAFTING_TABLE;
			
			if(!hasCraftedTag && onTable) {
				boolean babby = ((AgeableEntity)ent).getGrowingAge() < 0;
				int nuggetCount = babby ? 5 : 9;
				if(hasHeadlessTag) nuggetCount--;
				
				for(int i = 0; i < nuggetCount; i++) {
					ItemEntity nug = new ItemEntity(world, ent.posX, ent.posY, ent.posZ, new ItemStack(ChickenNuggetItems.RAW_NUGGET));
					nug.setMotion(nug.getMotion().mul(3, 0, 3)); // lol
					nug.setPickupDelay(30);
					world.addEntity(nug);
				}
				world.playSound(null, ent.posX, ent.posY, ent.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, .5f, babby ? 2f : 1.3f);
				ent.remove();
				continue;
			}
			
			if(hasCraftedTag && !onTable) {
				ent.removeTag(craftedTag);
			}
			
			boolean hasCastedTag = ent.getTags().contains(castedTag);
			if (hasCastedTag && ent.ticksExisted > 8) {
				ent.noClip = false;
				ent.removeTag(castedTag);
			}
		}
		
		world.getProfiler().endSection();
	}
	
	@SubscribeEvent
	public static void attackChicken(AttackEntityEvent e) {
		if (!(e.getTarget() instanceof ChickenEntity)) return;
		PlayerEntity player = e.getPlayer();
		if (player.getHeldItemMainhand().getToolTypes().contains(ToolType.AXE)) {
			ChickenEntity target = (ChickenEntity) e.getTarget();
			if (target.getTags().contains(headlessTag)) {
				return; // Already been made headless
			}
			e.setCanceled(true);
			World world = target.getEntityWorld();
			if (!world.isRemote) {
				target.addTag(headlessTag);
				PacketUpdateChicken.syncToClients(target);
				
				// HORRIBLE TRIG
				float calculatedAngle = (float) ((target.renderYawOffset + 90.0F) * (Math.PI / 180.0));
				double posX = target.posX + (Math.cos(calculatedAngle) / 4.0);
				double posZ = target.posZ + (Math.sin(calculatedAngle) / 4.0);
				double posY = target.posY + 0.5;
				ItemEntity head = new ItemEntity(world, posX, posY, posZ, new ItemStack(ChickenNuggetBlocks.CHICKEN_HEAD_BLOCK));
				head.setMotion(Math.cos(calculatedAngle) / 6.0F, head.getMotion().getY(), Math.sin(calculatedAngle) / 6.0F);
				head.setPickupDelay(15);
				world.addEntity(head);
				
				// pop!
				world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, .5f, 1.0F);
				
				//make it run around like, well, like a headless chicken
				AIHelpers.chicken.add(target);
			}
		}
	}

	@SubscribeEvent
	public static void chickenJoinedWorld(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof ChickenEntity && e.getEntity().getTags().contains(headlessTag)) {
			ChickenEntity chicken = (ChickenEntity) e.getEntity();
			
			PacketUpdateChicken.syncToClients(chicken);
			//Reset the aitask since these are not saved to nbt
			AIHelpers.chicken.add(chicken);
		}
	}

	@SubscribeEvent
	public static void playerStartedTrackingChicken(PlayerEvent.StartTracking e) {
		if (e.getTarget() instanceof ChickenEntity && e.getTarget().getTags().contains(headlessTag)) {
			PacketUpdateChicken.syncToClient((ChickenEntity) e.getTarget(), e.getPlayer());
		}
	}

	@SubscribeEvent
	public static void endermanTrackingPlayer(LivingSetAttackTargetEvent e) {
		Optional.ofNullable(e.getEntityLiving())
				.filter(EndermanEntity.class::isInstance)
				.map(EndermanEntity.class::cast)
				.ifPresent(endermanEntity -> {
					if (Optional.ofNullable(e.getTarget())
							.filter(PlayerEntity.class::isInstance)
							.map(PlayerEntity.class::cast)
							.filter(player -> {
								for (ItemStack itemStack : player.getArmorInventoryList()) {
									if (itemStack.getItem() instanceof ItemChickenHead) {
										return true;
									}
								}
								if (ChickenNugget.curiosCompat) {
									return CuriosHandler.wearingChickenHead(player);
								}
								return false;
							})
							.isPresent()) {
						//Reset the FindPlayerGoal, so the enderman doesn't think it's trying to locate (due to aggression) the player
						Optional<PrioritizedGoal> findPlayerGoal = endermanEntity.targetSelector.getRunningGoals().filter(g -> g.getGoal().getClass().getEnclosingClass() == EndermanEntity.class).findFirst();
						if (findPlayerGoal.isPresent()) {
							//This branch also calls setAttackTarget(null) implicitly
							findPlayerGoal.get().resetTask();
						} else {
							//Clear the attack target, so the enderman isn't aggressive towards the player
							endermanEntity.setAttackTarget(null);
						}
					}
				});
	}
}
