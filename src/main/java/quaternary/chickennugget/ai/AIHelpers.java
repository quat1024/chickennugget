package quaternary.chickennugget.ai;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import quaternary.chickennugget.ChickenNugget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class AIHelpers {
	private AIHelpers() {}

	//I decided I wasn't using enough generics, and I needed to use more
	public static class GoalManager<E extends MobEntity, G extends Goal> {
		private final WeakHashMap<E, G> cachedGoals = new WeakHashMap<>();
		private final Function<E, G> goalSupplier;
		private final BiConsumer<E, G> removed;

		GoalManager(@Nonnull Function<E, G> goalSupplier, @Nullable BiConsumer<E, G> removed) {
			this.goalSupplier = goalSupplier;
			this.removed = removed;
		}

		/**
		 * Adds a Goal to this entity, if it doesn't exist already
		 * @param ent The MobEntity to add the goal to
		 */
		public void add(E ent) {
			//Does the vanilla panic goal need to be removed?
			//The 1.12 code removed the vanilla panic goal, but it seems to work fine without doing so
			cachedGoals.computeIfAbsent(ent, ent2 -> {
				G goal = goalSupplier.apply(ent2);
				ent2.goalSelector.addGoal(9999, goal);
				return goal;
			});
		}

		public void remove(E ent) {
			cachedGoals.computeIfPresent(ent, (entUnused, goal) -> {
				ent.goalSelector.removeGoal(goal);
				if (removed != null) {
					removed.accept(ent, goal);
				}
				return null;
			});
		}
	}

	public static final GoalManager<ChickenEntity, EntityAIPanicForever> chicken = new GoalManager<>(
			c -> new EntityAIPanicForever(c, 1.4d),
			//Stop moving instantly
			(chicken, g) -> chicken.getNavigator().clearPath()
	);

	public static void scareVillager(VillagerEntity villager, PlayerEntity scaryPlayer) {
		Brain<VillagerEntity> brian = villager.getBrain();
		if (!brian.hasActivity(RunFromCluckTask.RUN_FROM_CLUCK)) {
			brian.registerActivity(RunFromCluckTask.RUN_FROM_CLUCK, ImmutableList.of(Pair.of(0, new RunFromCluckTask())));
		}
		//Dodgy reflection to add memories, as setMemory only sets existing memories
		Field memories = ObfuscationReflectionHelper.findField(Brain.class, "field_218230_a");
		RunFromCluckTask.Cluck cluck = new RunFromCluckTask.Cluck(scaryPlayer, villager.getWorld().getGameTime());
		try {
			//noinspection unchecked
			((Map<MemoryModuleType<?>, Optional<?>>) memories.get(brian)).put(RunFromCluckTask.LAST_CLUCK, Optional.of(cluck));
		} catch (IllegalAccessException e) {
			//Villagers need to remember that someone has clucked
			ChickenNugget.LOGGER.warn("Failed to inject false memories into villager", e);
		}
		brian.switchTo(RunFromCluckTask.RUN_FROM_CLUCK);
	}
}