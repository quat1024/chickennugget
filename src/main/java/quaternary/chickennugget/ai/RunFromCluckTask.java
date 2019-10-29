package quaternary.chickennugget.ai;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class RunFromCluckTask extends Task<CreatureEntity> {
	public static final Activity RUN_FROM_CLUCK = new Activity("run_from_cluck");
	public static final MemoryModuleType<Cluck> LAST_CLUCK = new MemoryModuleType<>(Optional.empty());
	private static final int XZ_RANGE = 6;
	private static final int Y_RANGE = 7;
	private static final float SPEED = 0.9f;

	RunFromCluckTask() {
		super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT, LAST_CLUCK, MemoryModuleStatus.VALUE_PRESENT));
	}

	protected boolean shouldExecute(ServerWorld world, CreatureEntity owner) {
		return owner.getBrain().getMemory(LAST_CLUCK).filter(c -> c.shouldExecute(owner, world.getGameTime())).isPresent();
	}

	protected void startExecuting(ServerWorld world, CreatureEntity entity, long gameTime) {
		entity.getBrain().getMemory(LAST_CLUCK).map(Cluck::getPosition).ifPresent(pos -> {
			for(int i = 0; i < 10; ++i) {
				Vec3d vec3d1 = RandomPositionGenerator.func_223548_b(entity, XZ_RANGE, Y_RANGE, pos);
				if (vec3d1 != null) {
					entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3d1, SPEED, 0));
					return;
				}
			}
		});
	}

	static class Cluck {
		static final double DISTANCE = 5.0D;
		static final int DURATION_TICKS = 20;
		private Vec3d cluckLocation;
		private long cluckTime;

		Cluck(LivingEntity clucked, long gameTime) {
			cluckLocation = clucked.getPositionVec();
			cluckTime = gameTime;
		}

		boolean shouldExecute(CreatureEntity owner, long gameTime) {
			return owner.getDistanceSq(cluckLocation) < DISTANCE && (gameTime - cluckTime) < DURATION_TICKS;
		}

		Vec3d getPosition() {
			return cluckLocation;
		}
	}
}
