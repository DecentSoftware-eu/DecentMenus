package eu.decent.menus.api.events;

import eu.decent.menus.menu.Menu;
import lombok.Getter;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a player clicks inside a menu.
 * This event can be cancelled resulting in the click being cancelled too.
 */
@Getter
public class MenuClickEvent extends CancellableMenuEvent {

    private final ClickType clickType;
    private final int slot;

    public MenuClickEvent(@NotNull Menu menu, @NotNull ClickType clickType, int slot) {
        super(menu);
        this.clickType = clickType;
        this.slot = slot;
    }

}
