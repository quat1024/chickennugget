package quaternary.chickennugget.compat.curios;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import quaternary.chickennugget.client.ChickenNuggetClientEvents;
import quaternary.chickennugget.item.ChickenNuggetItems;

import javax.annotation.Nonnull;

public class PlayerLayerHeadCurio extends HeadLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

	public PlayerLayerHeadCurio(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	// TODO: change to use the Curio rendering system?
	// https://github.com/TheIllusiveC4/CuriousShulkerBoxes/blob/master/src/main/java/top/theillusivec4/curiousshulkerboxes/common/capability/CurioShulkerBox.java
	// https://github.com/TheIllusiveC4/CuriousShulkerBoxes/blob/master/src/main/java/top/theillusivec4/curiousshulkerboxes/CuriousShulkerBoxes.java
	
	@Override
	public void render(@Nonnull AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		//dodgy hack to render chicken head and other stuff at the same time
		if (player.getTags().contains(ChickenNuggetClientEvents.wearingChickenHeadTag)) {
			ItemStack existing = player.inventory.armorInventory.get(EquipmentSlotType.HEAD.getIndex());

			player.inventory.armorInventory.set(EquipmentSlotType.HEAD.getIndex(), new ItemStack(ChickenNuggetItems.CHICKEN_HEAD));
			super.render(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
			player.inventory.armorInventory.set(EquipmentSlotType.HEAD.getIndex(), existing);
		}
	}

}
