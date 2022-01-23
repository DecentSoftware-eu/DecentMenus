package eu.decent.menus.menu.enums;

import eu.decent.menus.menu.item.MenuItem;
import eu.decent.menus.menu.item.NormalMenuItem;
import eu.decent.menus.menu.item.ServerMenuItem;
import eu.decent.menus.menu.item.ServersMenuItem;
import eu.decent.menus.utils.config.Configuration;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This enum represents all {@link MenuItem} types.
 */
public enum EnumMenuItemType {
	NORMAL(NormalMenuItem.class),
	SERVER(ServerMenuItem.class),
	SERVERS(ServersMenuItem.class),
	;

	@Getter
	private final Class<? extends MenuItem> clazz;

	EnumMenuItemType(Class<? extends MenuItem> clazz) {
		this.clazz = clazz;
	}

	public static EnumMenuItemType fromName(String name) {
		for (EnumMenuItemType value : values()) {
			if (value.name().equalsIgnoreCase(name)) {
				return value;
			}
		}
		return EnumMenuItemType.NORMAL;
	}

	/**
	 * Create new instance of {@link MenuItem} with this type.
	 *
	 * @param config The configuration section responsible for this item.
	 * @param identifier The items' config identifier.
	 * @return The MenuItem.
	 */
	public MenuItem create(Configuration config, char identifier) {
		try {
			Constructor<? extends MenuItem> constructor = clazz.getDeclaredConstructor(Configuration.class, char.class);
			return constructor.newInstance(config, identifier);
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}