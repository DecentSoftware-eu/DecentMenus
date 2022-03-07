package eu.decent.menus.api.events;

import eu.decent.menus.menu.Menu;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called before closing a menu.
 * This event can be cancelled result in the menu not closing.
 */
public class MenuCloseEvent extends CancellableMenuEvent {

    public MenuCloseEvent(@NotNull Menu menu) {
        super(menu);
    }

}
