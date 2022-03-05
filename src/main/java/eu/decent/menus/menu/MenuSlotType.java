package eu.decent.menus.menu;

/**
 * This enum represents all {@link MenuItem}s slot types.
 */
public enum MenuSlotType {
	DEFAULT,
	FILL
	;

	public static MenuSlotType fromName(String name) {
		for (MenuSlotType value : values()) {
			if (value.name().equalsIgnoreCase(name)) {
				return value;
			}
		}
		return MenuSlotType.DEFAULT;
	}

}
