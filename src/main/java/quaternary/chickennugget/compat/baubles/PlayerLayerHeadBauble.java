package quaternary.chickennugget.compat.baubles;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import quaternary.chickennugget.client.ChickenNuggetClientEvents;
import quaternary.chickennugget.item.ChickenNuggetItems;

public class PlayerLayerHeadBauble extends LayerCustomHead {

	public PlayerLayerHeadBauble(ModelRenderer renderer) {
		super(renderer);
	}
	
	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		//dodgy hack to render chicken head and other stuff at the same time
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			
			if (player.getTags().contains(ChickenNuggetClientEvents.wearingChickenHeadTag)) {
				ItemStack existing = player.inventory.armorInventory.get(EntityEquipmentSlot.HEAD.getIndex());
				
				player.inventory.armorInventory.set(EntityEquipmentSlot.HEAD.getIndex(), new ItemStack(ChickenNuggetItems.CHICKEN_HEAD));
				super.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
				player.inventory.armorInventory.set(EntityEquipmentSlot.HEAD.getIndex(), existing);
			}
		}
	}

}
