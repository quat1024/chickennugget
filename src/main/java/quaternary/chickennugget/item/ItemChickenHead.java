package quaternary.chickennugget.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import quaternary.chickennugget.ChickenNuggetCommonEvents;
import quaternary.chickennugget.ai.AIHelpers;
import quaternary.chickennugget.block.BlockChickenHead;
import quaternary.chickennugget.net.PacketUpdateChicken;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemChickenHead extends BlockItem {
	ItemChickenHead(BlockChickenHead block, Properties props) {
		super(block, props);
	}

	@Override
	public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
		return EquipmentSlotType.HEAD;
	}
	
	// Allow heads to be placed back onto chickens
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		if (!(target instanceof ChickenEntity)) return false;
		ChickenEntity chicken = (ChickenEntity) target;
		
		if (chicken.getEntityWorld().isRemote) return false;

		if (chicken.getTags().contains(ChickenNuggetCommonEvents.headlessTag)) {
			chicken.getTags().remove(ChickenNuggetCommonEvents.headlessTag);
			PacketUpdateChicken.syncToClients(chicken);
			chicken.getEntityWorld().playSound(null, chicken.posX, chicken.posY, chicken.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, .5f, 1.0F);
			if(!player.isCreative()) stack.shrink(1);
			
			//reset the endless chicken panic
			AIHelpers.calmDownChickenItsOk(chicken);
			
			return true;
		} else {
			return false;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag mistake) {
		super.addInformation(stack, worldIn, tooltip, mistake);

		tooltip.add(new TranslationTextComponent("item.chickennugget.chicken_head.hint").applyTextStyles(TextFormatting.DARK_GRAY, TextFormatting.ITALIC));
	}
}
