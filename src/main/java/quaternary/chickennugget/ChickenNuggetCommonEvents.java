package quaternary.chickennugget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import quaternary.chickennugget.ai.AIHelpers;
import quaternary.chickennugget.ai.EntityAIPanicForever;
import quaternary.chickennugget.block.ChickenNuggetBlocks;
import quaternary.chickennugget.item.ChickenNuggetItems;
import quaternary.chickennugget.net.PacketUpdateChicken;

@Mod.EventBusSubscriber(modid = ChickenNugget.MODID)
public class ChickenNuggetCommonEvents {
	public static enum SpawnType {
		CRAFTED,
		CASTED,
		SMELTED // spooky
	}

	public static class ChickenPosition {
		public final BlockPos pos;
		public final int count;
		public final SpawnType type;
		public ChickenPosition(BlockPos pos, int count, SpawnType type) {
			this.pos = pos;
			this.count = count;
			this.type = type;
		}
	}
	
	public static final Map<World, List<ChickenPosition>> positionsNeedingChickens = new WeakHashMap<>();
	
	public static void markPositionAsNeedingNewChickens(World world, BlockPos pos, int count) {
		markPositionAsNeedingNewChickens(world, pos, count, SpawnType.CRAFTED);
	}
	
	public static void markPositionAsNeedingNewChickens(World world, BlockPos pos, int count, SpawnType type) {
		positionsNeedingChickens.computeIfAbsent(world, (w) -> new ArrayList<>(1)).add(new ChickenPosition(pos, count, type));
	}
	
	private static final String craftedTag = "CraftedChicken";
	private static final String castedTag = "CastedChicken";
	public static final String headlessTag = "HeadlessChicken";
	
	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent e) {
		if(e.phase != TickEvent.Phase.END) return;
		World world = e.world;
		if(e.world.isRemote) return;
		
		world.profiler.startSection("chickennuggets");
		
		int cramming = world.getGameRules().getInt("maxEntityCramming");
		
		if(!positionsNeedingChickens.isEmpty() && positionsNeedingChickens.containsKey(world)) {
			for(ChickenPosition cPos : positionsNeedingChickens.remove(world)) {
				BlockPos pos = cPos.pos;
				int count = cPos.count;
				SpawnType type = cPos.type;
				
				boolean lots = count >= cramming;
				
				for(int i = 0; i < count; i++) {
					EntityChicken chicken = new EntityChicken(world);
					
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
						chicken.motionY = 0.5;
						chicken.noClip = true;
						chicken.addTag(castedTag);
					}
					
					if(lots) {
						//So they don't immediately cram themselves to death lmao
						chicken.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 5, 5));
						//Boing!!!
						chicken.motionY = 0.2;
					}
					
					world.spawnEntity(chicken);
					
					world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, 1, world.rand.nextFloat() * 0.5f + 0.75f);
				}
			}
		}
		
		for(EntityChicken ent : world.getEntities(EntityChicken.class, (ent) -> true)) {
			boolean hasCraftedTag = ent.getTags().contains(craftedTag);
			boolean hasHeadlessTag = ent.getTags().contains(headlessTag);
			boolean onTable = world.getBlockState(ent.getPosition().down()).getBlock() == Blocks.CRAFTING_TABLE;
			
			if(!hasCraftedTag && onTable) {
				boolean babby = ent.getGrowingAge() < 0;
				int nuggetCount = babby ? 5 : 9;
				if(hasHeadlessTag) nuggetCount--;
				
				for(int i = 0; i < nuggetCount; i++) {
					EntityItem nug = new EntityItem(world, ent.posX, ent.posY, ent.posZ, new ItemStack(ChickenNuggetItems.RAW_NUGGET));
					nug.motionX *= 3;
					nug.motionZ *= 3; //lol
					nug.setPickupDelay(30);
					world.spawnEntity(nug);
				}
				world.playSound(null, ent.posX, ent.posY, ent.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, .5f, babby ? 2f : 1.3f);
				ent.setDead();
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
		
		world.profiler.endSection();
	}
	
	@SubscribeEvent
	public static void attackChicken(AttackEntityEvent e) {
		if (!(e.getTarget() instanceof EntityChicken)) return;
		EntityPlayer player = e.getEntityPlayer();
		if (player.getHeldItemMainhand().getItem() instanceof ItemAxe) {
			EntityChicken target = (EntityChicken) e.getTarget();
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
				EntityItem head = new EntityItem(world, posX, posY, posZ, new ItemStack(ChickenNuggetBlocks.CHICKEN_HEAD_BLOCK));
				head.motionX = Math.cos(calculatedAngle) / 6.0F;
				head.motionZ = Math.sin(calculatedAngle) / 6.0F;
				head.setPickupDelay(15);
				world.spawnEntity(head);
				
				// pop!
				world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, .5f, 1.0F);
				
				//make it run around like, well, like a headless chicken
				AIHelpers.scareChickenForever(target);
			}
		}
	}

	@SubscribeEvent
	public static void chickenJoinedWorld(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityChicken && e.getEntity().getTags().contains(headlessTag)) {
			EntityChicken chicken = (EntityChicken) e.getEntity();
			
			PacketUpdateChicken.syncToClients(chicken);
			//Reset the aitask since these are not saved to nbt
			AIHelpers.scareChickenForever(chicken);
		}
	}

	@SubscribeEvent
	public static void playerStartedTrackingChicken(PlayerEvent.StartTracking e) {
		if (e.getTarget() instanceof EntityChicken && e.getTarget().getTags().contains(headlessTag)) {
			PacketUpdateChicken.syncToClient((EntityChicken) e.getTarget(), e.getEntityPlayer());
		}
	}
}
