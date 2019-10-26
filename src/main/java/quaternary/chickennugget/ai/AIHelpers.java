package quaternary.chickennugget.ai;

import net.minecraft.entity.passive.ChickenEntity;

import java.util.WeakHashMap;

public class AIHelpers {
	private static final WeakHashMap<ChickenEntity, EntityAIPanicForever> cachedGoals = new WeakHashMap<>();

	private AIHelpers() {}
	
	public static void scareChickenForever(ChickenEntity chicken) {
		//Does the vanilla panic goal need to be removed?
		//The 1.12 code removed the vanilla panic goal, but it seems to work fine without doing so
		EntityAIPanicForever aaaaaaaaaaaaaaaaaaaaaaaaaaa = cachedGoals.computeIfAbsent(chicken, c -> new EntityAIPanicForever(c, 1.4d));
		chicken.goalSelector.addGoal(9999, aaaaaaaaaaaaaaaaaaaaaaaaaaa);
	}
	
	public static void calmDownChickenItsOk(ChickenEntity chicken) {
		cachedGoals.computeIfPresent(chicken, (chickenUnused, goal) -> {
			chicken.goalSelector.removeGoal(goal);
			return null;
		});
		
		//Stop moving instantly
		chicken.getNavigator().clearPath();
	}
}
