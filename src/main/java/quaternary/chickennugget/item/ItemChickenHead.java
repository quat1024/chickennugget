package quaternary.chickennugget.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.chickennugget.ChickenNuggetCommonEvents;
import quaternary.chickennugget.net.PacketUpdateChicken;
import quaternary.chickennugget.block.BlockChickenHead;

import javax.annotation.Nullable;
import java.util.List;

public class ItemChickenHead extends ItemBlock {
	public ItemChickenHead(BlockChickenHead block) {
		super(block);
	}
	
	@Override
	public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
		return armorType == EntityEquipmentSlot.HEAD;
	}
	
	// Allow heads to be placed back onto chickens
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
		if (!(target instanceof EntityChicken)) return false;
		
		if (target.getEntityWorld().isRemote) return false;

		if (target.getTags().contains(ChickenNuggetCommonEvents.headlessTag)) {
			target.getTags().remove(ChickenNuggetCommonEvents.headlessTag);
			PacketUpdateChicken.syncToClients((EntityChicken) target);
			target.getEntityWorld().playSound(null, target.posX, target.posY, target.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, .5f, 1.0F);
			stack.shrink(1);
			return true;
		} else {
			return false;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag mistake) {
		super.addInformation(stack, worldIn, tooltip, mistake);
		
		tooltip.add(TextFormatting.DARK_GRAY.toString() + TextFormatting.ITALIC + I18n.format("item.chickennugget.chicken_head.hint"));
	}
}
