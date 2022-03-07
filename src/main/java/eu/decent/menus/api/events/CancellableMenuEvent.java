package eu.decent.menus.api.events;

import eu.decent.menus.menu.Menu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public abstract class CancellableMenuEvent extends MenuEvent implements Cancellable {

    private boolean cancelled;

    public CancellableMenuEvent(Menu menu) {
        super(menu);
        this.cancelled = false;
    }

}
