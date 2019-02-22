package quaternary.chickennugget.ai;

import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.passive.EntityChicken;

public class AIHelpers {
	private AIHelpers() {}
	
	public static void scareChickenForever(EntityChicken chicken) {
		EntityAITasks.EntityAITaskEntry vanillaPanicEntry = null;
		
		for(EntityAITasks.EntityAITaskEntry entry : chicken.tasks.taskEntries) {
			if(entry.action instanceof EntityAIPanicForever) return;
			if(entry.action instanceof EntityAIPanic) {
				vanillaPanicEntry = entry;
				break;
			}
		}
		
		if(vanillaPanicEntry != null) chicken.tasks.removeTask(vanillaPanicEntry.action);
		
		chicken.tasks.addTask(9999, new EntityAIPanicForever(chicken, 1.4d));
	}
	
	public static void calmDownChickenItsOk(EntityChicken chicken) {
		EntityAITasks.EntityAITaskEntry endlessPanicEntry = null;
		
		for(EntityAITasks.EntityAITaskEntry entry : chicken.tasks.taskEntries) {
			if(entry.action instanceof EntityAIPanicForever) {
				endlessPanicEntry = entry;
				break;
			}
		}
		
		if(endlessPanicEntry != null) chicken.tasks.removeTask(endlessPanicEntry.action);
		
		//Same as vanilla
		chicken.tasks.addTask(1, new EntityAIPanic(chicken, 1.4d));
		
		//Stop moving instantly
		chicken.getNavigator().clearPath();
	}
}
