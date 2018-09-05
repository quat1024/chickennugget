package quaternary.chickennugget;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = ChickenNugget.MODID)
public class ChickenNuggetCommonEvents {
	public static final Map<World, List<BlockPos>> positionsNeedingChickens = new WeakHashMap<>();
	
	public static void markPositionAsNeedingNewChicken(World world, BlockPos pos) {
		List<BlockPos> positions = positionsNeedingChickens.computeIfAbsent(world, (w) -> new ArrayList<>(1));
		positions.add(pos);
	}
	
	private static final String craftedTag = "CraftedChicken";
	
	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent e) {
		if(e.phase != TickEvent.Phase.END) return;
		World world = e.world;
		if(e.world.isRemote) return;
		
		world.profiler.startSection("chickennuggets");
		
		if(!positionsNeedingChickens.isEmpty() && positionsNeedingChickens.containsKey(world)) {
			for(BlockPos pos : positionsNeedingChickens.remove(world)) {
				EntityChicken chicken = new EntityChicken(world);
				chicken.setPosition(pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5);
				chicken.setLocationAndAngles(pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, world.rand.nextFloat() * 360, 0);
				chicken.addTag(craftedTag);
				world.spawnEntity(chicken);
				world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, 1, 1);
			}
		}
		
		for(EntityChicken ent : world.getEntities(EntityChicken.class, (ent) -> true)) {
			boolean hasTag = ent.getTags().contains(craftedTag);
			boolean onTable = world.getBlockState(ent.getPosition().down()).getBlock() == Blocks.CRAFTING_TABLE;
							
			if(!hasTag && onTable) {
				for(int i = 0; i < 9; i++) {
					EntityItem nug = new EntityItem(world, ent.posX, ent.posY, ent.posZ, new ItemStack(ChickenNuggetItems.RAW_NUGGET));
					nug.motionX *= 3;
					nug.motionZ *= 3; //lol
					nug.setPickupDelay(30);
					world.spawnEntity(nug);
				}
				world.playSound(null, ent.posX, ent.posY, ent.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, .5f, 1.3f);
				ent.setDead();
				continue;
			}
			
			if(hasTag && !onTable) {
				ent.removeTag(craftedTag);
			}
		}
		
		world.profiler.endSection();
	}
}
