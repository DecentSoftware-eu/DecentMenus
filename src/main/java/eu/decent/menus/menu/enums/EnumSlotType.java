package eu.decent.menus.menu.enums;

import eu.decent.menus.menu.item.MenuItem;

/**
 * This enum represents all {@link MenuItem}s slot types.
 */
public enum EnumSlotType {
	DEFAULT,
	FILL
	;

	public static EnumSlotType fromName(String name) {
		for (EnumSlotType value : values()) {
			if (value.name().equalsIgnoreCase(name)) {
				return value;
			}
		}
		return EnumSlotType.DEFAULT;
	}

}
