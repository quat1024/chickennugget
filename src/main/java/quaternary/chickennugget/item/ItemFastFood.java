package quaternary.chickennugget.item;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemFastFood extends ItemFood {
	public ItemFastFood(int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 10;
	}
}
