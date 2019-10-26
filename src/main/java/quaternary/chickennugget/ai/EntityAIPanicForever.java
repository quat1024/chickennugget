package quaternary.chickennugget.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

//I mean, honestly? Same.
public class EntityAIPanicForever extends PanicGoal {
	EntityAIPanicForever(CreatureEntity creature, double speedIn) {
		super(creature, speedIn);
		setMutexFlags(EnumSet.of(Flag.MOVE));
	}
	
	@Override
	public boolean shouldExecute() {
		shufflePos();
		
		return true;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(creature.getNavigator().noPath()) shufflePos();
		return true;
	}

	@Override
	public boolean isPreemptible() {
		return false;
	}
	
	private void shufflePos() {
		int tries = 0;
		Vec3d v;
		
		do {
			v = RandomPositionGenerator.findRandomTarget(this.creature, 10, 10);
			tries++;
		} while(v == null && tries < 25);
		
		if(v != null) {
			this.randPosX = v.x;
			this.randPosY = v.y;
			this.randPosZ = v.z;
		}
		
		this.creature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
	}
}
