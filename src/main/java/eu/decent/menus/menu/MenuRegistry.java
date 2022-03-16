package eu.decent.menus.menu;

import eu.decent.menus.DecentMenus;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.Common;
import eu.decent.menus.utils.collection.Registry;
import eu.decent.menus.utils.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * This class holds all menu models.
 */
public class MenuRegistry extends Registry<String, MenuModel> {

    private static final DecentMenus PLUGIN = DecentMenus.getInstance();
    private File menusDir;

    /**
     * Create new {@link MenuRegistry}.
     */
    public MenuRegistry() {
        this.menusDir = null;
        this.reload();
    }

    /**
     * Clear this registry and load all {@link MenuModel}s.
     */
    @Override
    public void reload() {
        // Clear existing menu models
        this.clear();

        // Get the folder with all menu model files
        if (menusDir == null) {
            menusDir = new File(PLUGIN.getDataFolder(), "menus");
        }

        long startMillis = System.currentTimeMillis();
        Common.log("Loading menu(s)...");
        // Load all menu models
        int counter = 0;
        if (menusDir.exists() && menusDir.isDirectory()) {
            // Get all file names
            String[] fileNames = menusDir.list((dir1, name) -> name.matches("\\w+\\.yml"));
            if (fileNames != null && fileNames.length > 0) {
                for (String fileName : fileNames) {
                    Configuration configuration = new Configuration(PLUGIN, "menus/" + fileName);
                    // Parse model name
                    String name = fileName.substring(0, fileName.length() - 4);
                    // Register the model
                    MenuModel menuModel = new MenuModel(name, configuration);
                    register(menuModel);
                    counter++;
                }
            }
        }
        long took = System.currentTimeMillis() - startMillis;
        Common.log("Successfully loaded %d menu(s) in %d ms!", counter, took);
    }

    /**
     * Clear this registry and close all menus.
     */
    @Override
    public void clear() {
        super.clear();

        // -- Close all menus
        this.closeAll();
    }

    /**
     * Close all open menus.
     */
    public void closeAll() {
        Bukkit.getOnlinePlayers().forEach((p) -> {
            Inventory inventory = p.getOpenInventory().getTopInventory();
            InventoryHolder holder = inventory.getHolder();
            if (holder instanceof Menu) {
                ((Menu) holder).close();
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
    public boolean openMenu(@NotNull Player player, @NotNull String model) {
        PlayerProfile playerProfile = PLUGIN.getPlayerRegistry().get(player.getUniqueId());
        MenuModel menuModel = get(model);
        if (menuModel != null) {
            Menu menu = new Menu(menuModel, playerProfile);
            menu.open();
            return true;
        }
        return false;
    }

    /**
     * Register a new {@link MenuModel}.
     *
     * @param menuModel The MenuModel.
     */
    public void register(@NotNull MenuModel menuModel) {
        this.register(menuModel.getName(), menuModel);
    }

}
