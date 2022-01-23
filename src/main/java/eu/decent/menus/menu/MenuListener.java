package eu.decent.menus.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * This class handles Menu related events.
 */
public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        InventoryHolder holder = inventory.getHolder();
        // If the clicked inventory is a Menu, make it handle the event.
        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            menu.onClick(e);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        InventoryHolder holder = inventory.getHolder();
        // If the dragged inventory is a Menu, make it handle the event.
        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            menu.onDrag(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        Inventory inventory = e.getInventory();
        InventoryHolder holder = inventory.getHolder();
        // If the closed inventory is a Menu, make it handle the event.
        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            menu.onClose(e);
        }
    }

}
