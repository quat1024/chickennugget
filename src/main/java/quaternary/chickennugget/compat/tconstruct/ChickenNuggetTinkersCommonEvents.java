package quaternary.chickennugget.compat.tconstruct;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.chickennugget.ChickenNuggetCommonEvents;
import slimeknights.tconstruct.smeltery.events.TinkerCastingEvent;
import slimeknights.tconstruct.smeltery.tileentity.TileSmeltery;

public class ChickenNuggetTinkersCommonEvents {
	
	private static DamageSource smelteryDeath = new DamageSource("smelteryMeltingChicken").setDamageBypassesArmor();
	private static final String deathTag = "chickenGotMelted";

	//Give chicken tag when about to be killed by smeltery (and smeltery will gain liquid)
	@SubscribeEvent
	public static void chickenDamaged(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		//If chicken would die, don't kill it, so that attackEntityFrom succeeds
		if (event.getSource().equals(smelteryDeath) && (entity.getHealth() - event.getAmount()) <= 0F) {
			event.setAmount(0F);
			entity.addTag(deathTag);
		}
	}
	
	//Only the attack by the smeltery that would kill the chicken is allowed, all others are done using smelteryDeath
	@SubscribeEvent
	public static void chickenAttacked(LivingAttackEvent event) {
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
	public static void chickenDrops(LivingDropsEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityChicken && entity.getTags().contains(deathTag)) {
			event.setCanceled(true);
		}
	}
	
	//Disable XP drops for the entity death
	@SubscribeEvent
	public static void chickenXPDrops(LivingExperienceDropEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityChicken && entity.getTags().contains(deathTag)) {
			event.setCanceled(true);
		}
	}
	
	//Allow casting out chickens
	@SubscribeEvent
	public static void castedChicken(TinkerCastingEvent.OnCasted event) {
		if (event.recipe instanceof ChickenCastingRecipe && event.tile != null) {
			World world = event.tile.getWorld();
			if(!world.isRemote) {
				ChickenNuggetCommonEvents.markPositionAsNeedingNewChickens(world, event.tile.getPos(), 1, ChickenNuggetCommonEvents.SpawnType.CASTED);
			}
		}
	}
}
