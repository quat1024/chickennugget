package quaternary.chickennugget.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import quaternary.chickennugget.ChickenNugget;

//I mean, honestly? Same.
public class EntityAIPanicForever extends EntityAIPanic {
	public EntityAIPanicForever(EntityCreature creature, double speedIn) {
		super(creature, speedIn);
		setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute() {
		ChickenNugget.LOGGER.info("shouldExecute");
		shufflePos();
		
		return true;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(creature.getNavigator().noPath()) shufflePos();
		return true;
	}
	
	@Override
	public boolean isInterruptible() {
		return false;
	}
	
	private void shufflePos() {
		int tries = 0;
		Vec3d v = null;
		
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
