package eu.decent.menus.api.events;

import eu.decent.menus.menu.Menu;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called before opening a menu.
 * This event can be cancelled resulting in the menu not opening.
 */
public class MenuOpenEvent extends MenuEvent {

    public MenuOpenEvent(@NotNull Menu menu) {
        super(menu);
    }

}
