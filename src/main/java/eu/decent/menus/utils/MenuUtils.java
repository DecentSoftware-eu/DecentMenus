package eu.decent.menus.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class with some useful methods related to inventories and item stacks.
 */
public final class MenuUtils {

	/**
	 * Find first empty slot in the given inventory.
	 *
	 * @param inventory The inventory.
	 * @return The slot or -1 if there is no empty slot.
	 */
	public static int getFirstFreeSlot(Inventory inventory) {
		Validate.notNull(inventory);

		ItemStack[] contents = inventory.getContents();
		for (int i = 0; i < contents.length; i++) {
			ItemStack itemStack = contents[i];
			if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
				return i;
			}
		}
		return -1;
	}

}
