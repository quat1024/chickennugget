package quaternary.chickennugget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
			boolean onTable = world.getBlockState(ent.getPosition().down()).getBlock() == Blocks.CRAFTING_TABLE;
			
			if(!hasCraftedTag && onTable) {
				boolean babby = ent.getGrowingAge() < 0;
				
				for(int i = 0; i < (babby ? 5 : 9); i++) {
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
}
