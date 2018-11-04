package quaternary.chickennugget.tconstruct;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;

public final class TConstructCompat {

	//Only construct ChickenNuggetFluids when isModLoaded("tconstruct") has been checked
	private static ChickenNuggetFluids fluidsClass;
	
	private static void loadFluidsClass() {
		if (fluidsClass != null) {
			return;
		}

		fluidsClass = new ChickenNuggetFluids();
	}

	public static boolean isTConstructCompatEnabled() {
		return Loader.isModLoaded("tconstruct");
	}
	
	public static void registerFluids() {
		if (isTConstructCompatEnabled()) {
			loadFluidsClass();
			if (fluidsClass == null) {
				return; // load failed
			}
			fluidsClass.registerFluids();
		}
	}

	public static void addFluidsToList(NonNullList<ItemStack> itemList) {
		if (isTConstructCompatEnabled()) {
			loadFluidsClass();
			if (fluidsClass == null) {
				return; // load failed
			}
			fluidsClass.addFluids(itemList);
		}
	}
	
	public static void registerTConRecipes() {
		if (isTConstructCompatEnabled()) {
			loadFluidsClass();
			if (fluidsClass == null) {
				return; // load failed
			}
			fluidsClass.registerTConRecipes();
		}
	}
	
	public static void registerJEIRecipes(IModRegistry registry) {
		if (isTConstructCompatEnabled()) {
			loadFluidsClass();
			if (fluidsClass == null) {
				return; // load failed
			}
			fluidsClass.registerJEIRecipes(registry);
		}
	}

	public static void registerJEIRecipeCategories(IRecipeCategoryRegistration registry) {
		if (isTConstructCompatEnabled()) {
			loadFluidsClass();
			if (fluidsClass == null) {
				return; // load failed
			}
			fluidsClass.registerJEIRecipeCategories(registry);
		}
	}
	
}
