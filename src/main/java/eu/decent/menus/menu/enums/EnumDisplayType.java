package eu.decent.menus.menu.enums;

import eu.decent.menus.menu.item.MenuItem;

/**
 * This enum represents all {@link MenuItem}s display types.
 */
public enum EnumDisplayType {
	DEFAULT,
	/**
	 * Only display to players with required permission.
	 */
	PERMISSION,
	/**
	 * Only display if the items' server is online.
	 */
	ONLINE,
	/**
	 * Only display if the items' server is offline.
	 */
	OFFLINE
	;

	public static EnumDisplayType fromName(String name) {
		for (EnumDisplayType value : values()) {
			if (value.name().equalsIgnoreCase(name)) {
				return value;
			}
		}
		return EnumDisplayType.DEFAULT;
	}

}
