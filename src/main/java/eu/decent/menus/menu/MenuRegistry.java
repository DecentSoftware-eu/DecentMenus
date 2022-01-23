package eu.decent.menus.menu;

import eu.decent.library.utils.Common;
import eu.decent.library.utils.collection.DMap;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.player.PlayerProfile;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

/**
 * This class holds all menu models.
 */
public class MenuRegistry {

    private final DMap<String, MenuModel> menuModelMap;
    private File menusDir;

    /**
     * Create new {@link MenuRegistry}.
     */
    public MenuRegistry() {
        this.menuModelMap = new DMap<>();
        this.menusDir = null;
        this.reload();
    }

    /**
     * Clear this registry and load all {@link MenuModel}s.
     */
    public void reload() {
        // Clear existing menu models
        clear();

        // Get the folder with all menu model files
        if (menusDir == null) {
            menusDir = new File(DecentMenus.getInstance().getDataFolder() + "/menus");
        }

        Common.log("Loading menus...");
        // Load all menu models
        int counter = 0;
        if (menusDir.exists() && menusDir.isDirectory()) {
            // Get all file names
            String[] fileNames = menusDir.list((dir1, name) -> name.matches("\\S+\\.yml"));
            if (fileNames != null && fileNames.length > 0) {
                for (String fileName : fileNames) {
                    MenuModel menuModel = MenuModel.fromFile(fileName);
                    register(menuModel);
                    counter++;
                }
            }
        }
        Common.log("Loaded %d menus!", counter);
    }

    /**
     * Clear this registry and close all menus.
     */
    public void clear() {
        if (menuModelMap.isEmpty()) {
            return;
        }
        menuModelMap.clear();

        // Close all menus
        Bukkit.getOnlinePlayers().forEach((p) -> {
            Inventory inventory = p.getOpenInventory().getTopInventory();
            if (inventory != null) {
                InventoryHolder holder = inventory.getHolder();
                if (holder instanceof Menu) {
                    Menu menu = (Menu) holder;
                    menu.close();
                }
            }
        });
    }

    /**
     * Open menu with the given model for a player.
     *
     * @param player The player.
     * @param model The models' name
     * @return Boolean whether the operation was successful.
     */
    public boolean openMenu(Player player, String model) {
        Validate.notNull(player);
        Validate.notNull(model);

        PlayerProfile playerProfile = DecentMenus.getInstance().getPlayerRegistry().get(player.getUniqueId());
        MenuModel menuModel = get(model);
        if (menuModel != null && menuModel.hasPermission(player)) {
            Menu menu = new Menu(menuModel, playerProfile);
            menu.open();
            return true;
        }
        return false;
    }

    /**
     * Shutdown this registry.
     */
    public void shutdown() {
        clear();
    }

    /**
     * Register a new {@link MenuModel}.
     *
     * @param menuModel The MenuModel.
     */
    public void register(MenuModel menuModel) {
        menuModelMap.put(menuModel.getName(), menuModel);
    }

    /**
     * Get a {@link MenuModel} by name.
     *
     * @param name The name.
     * @return The MenuModel.
     */
    public MenuModel get(String name) {
        return menuModelMap.get(name);
    }

    /**
     * Remove a {@link MenuModel} by name.
     *
     * @param name The name.
     * @return The MenuModel.
     */
    public MenuModel remove(String name) {
        return menuModelMap.remove(name);
    }

    /**
     * Check whether this registry contains a {@link MenuModel} with the given name.
     *
     * @param name The name.
     * @return True if this registry contains the MenuModel.
     */
    public boolean contains(String name) {
        return menuModelMap.containsKey(name);
    }

    /**
     * Get a list of all names of registered menu models.
     *
     * @return The list.
     */
    public Set<String> getMenuNames() {
        return menuModelMap.keySet();
    }

}
