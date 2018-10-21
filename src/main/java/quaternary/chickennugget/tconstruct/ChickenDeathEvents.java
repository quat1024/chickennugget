package quaternary.chickennugget.tconstruct;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.smeltery.tileentity.TileSmeltery;

public class ChickenDeathEvents {
	
	private static DamageSource smelteryDeath = new DamageSource("smelteryMeltingChicken").setDamageBypassesArmor();
	private static final String deathTag = "chickenGotMelted";
	
	public ChickenDeathEvents() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	//Give chicken tag when about to be killed by smeltery (and smeltery will gain liquid)
	@SubscribeEvent
	public void chickenDamaged(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		//If chicken would die, don't kill it, so that attackEntityFrom succeeds
		if (event.getSource().equals(smelteryDeath) && (entity.getHealth() - event.getAmount()) <= 0F) {
			event.setAmount(0F);
			entity.addTag(deathTag);
		}
	}
	
	//Only the attack by the smeltery that would kill the chicken is allowed, all others are done using smelteryDeath
	@SubscribeEvent
	public void chickenAttacked(LivingAttackEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (event.getSource().equals(TileSmeltery.smelteryDamage) && entity instanceof EntityChicken && !entity.isDead) {
			//If chicken dies due to this, it will have deathTag so drops are removed
			if (entity.attackEntityFrom(smelteryDeath, 2F) && entity.getTags().contains(deathTag)) {
				entity.hurtResistantTime = 0; //Reset damage timer
				event.setCanceled(false); //Give liquid
			} else {
				event.setCanceled(true); //Cancel attack event so that smeltery doesn't increase liquid
			}
		}
	}
	
	//Disable drops for the entity death
	@SubscribeEvent
	public void chickenDrops(LivingDropsEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityChicken && entity.getTags().contains(deathTag)) {
			event.setCanceled(true);
		}
	}
	
	//Disable XP drops for the entity death
	@SubscribeEvent
	public void chickenXPDrops(LivingExperienceDropEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityChicken && entity.getTags().contains(deathTag)) {
			event.setCanceled(true);
		}
	}
}
