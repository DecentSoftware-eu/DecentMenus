package eu.decent.menus.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

/**
 * This class handles Menu related events.
 */
public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        executeIfMenu(e.getInventory(), (menu) -> menu.onClick(e));
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        executeIfMenu(e.getInventory(), (menu) -> menu.onDrag(e));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        executeIfMenu(e.getInventory(), (menu) -> menu.onClose(e));
    }

    private void executeIfMenu(Inventory inventory, Consumer<Menu> consumer) {
        if (inventory != null) {
            InventoryHolder holder = inventory.getHolder();
            if (holder instanceof Menu) {
                consumer.accept((Menu) holder);
            }
        }
    }

}
