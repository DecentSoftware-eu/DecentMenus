package eu.decent.menus.api.events;

import eu.decent.menus.menu.Menu;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents any menu-related event.
 */
@Getter
public abstract class MenuEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Menu menu;

    /**
     * Create a new instance of {@link MenuEvent}.
     *
     * @param menu The events' menu.
     */
    public MenuEvent(@NotNull Menu menu) {
        this.menu = menu;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
