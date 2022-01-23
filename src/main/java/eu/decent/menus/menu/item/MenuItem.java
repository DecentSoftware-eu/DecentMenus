package eu.decent.menus.menu.item;

import eu.decent.library.actions.ActionHolder;
import eu.decent.library.hooks.PAPI;
import eu.decent.menus.Config;
import eu.decent.menus.menu.Menu;
import eu.decent.menus.menu.MenuModel;
import eu.decent.menus.menu.enums.EnumDisplayType;
import eu.decent.menus.menu.enums.EnumMenuItemType;
import eu.decent.menus.menu.enums.EnumSlotType;
import eu.decent.menus.utils.config.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * This class represents a menu item.
 */
public abstract class MenuItem {

    protected final Configuration config;
    protected final char identifier;
    protected final ActionHolder actionHolder;
    protected final EnumMenuItemType type;
    protected EnumDisplayType displayType;
    protected EnumSlotType slotType;
    protected String permission;

    /**
     * Create new {@link MenuItem}.
     *
     * @param config The configuration section responsible for this item.
     * @param identifier The items' identifier from config.
     * @param type The items' type.
     */
    public MenuItem(Configuration config, char identifier, EnumMenuItemType type) {
        this.config = config;
        this.identifier = identifier;
        this.type = type;
        this.actionHolder = new ActionHolder();
        this.load();
    }

    /*
     *  General Methods
     */

    /**
     * Construct an {@link ItemStack} from this menu item.
     *
     * @param menu The context menu.
     * @return The item stack.
     */
    public abstract ItemStack construct(Menu menu);

    /**
     * Load this item from its config.
     */
    public void load() {
        displayType = EnumDisplayType.fromName(config.getString("display", "DEFAULT"));
        slotType = EnumSlotType.fromName(config.getString("slot", "DEFAULT"));

        List<String> actions = config.getStringList("actions");
        for (String s: actions) {
            // TODO this.addAction(new Action(s));
        }
    }

    /**
     * Replace all placeholders in the given string for the given player.
     *
     * @param player The player.
     * @param string The string.
     * @return The result.
     */
    public String setPlaceholders(Player player, String string) {
        return PAPI.setPlaceholders(player, string);
    }

    /**
     * Replace all placeholders in the given string list for the given player.
     *
     * @param player The player.
     * @param strings The string list.
     * @return The result.
     */
    public List<String> setPlaceholders(Player player, List<String> strings) {
        strings.replaceAll((string) -> setPlaceholders(player, string));
        return strings;
    }

    /*
     *  Event methods
     */

    /**
     * Handle {@link InventoryClickEvent}.
     *
     * @param menu The menu in which the click was performed.
     * @param event The event.
     */
    public void onClick(Menu menu, InventoryClickEvent event) {
        Player player = menu.getOwner().getPlayer();
        if (player != null) {
            actionHolder.executeActions(player);
        }
    }

    /*
     *  Permission methods
     */

    /**
     * Check whether the given player can click this item.
     *
     * @param player The player.
     * @return The resulting boolean.
     */
    public boolean canClick(Player player) {
        return permission == null || player.hasPermission(permission);
    }

    /**
     * Check whether the given player can be shown this item.
     *
     * @param player The player.
     * @return The resulting boolean.
     */
    public boolean canDisplayTo(Player player) {
        return !displayType.name().equals("PERMISSION") || player.hasPermission(permission);
    }

    /**
     * Check whether this item can be displayed right now.
     *
     * @return The resulting boolean.
     */
    public boolean canDisplay() {
        return true;
    }

    /*
     *  Getters & Setters
     */

    /**
     * Get the configuration section responsible for this item.
     *
     * @return The configuration section.
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * Get the items' identifier from config.
     *
     * @return The identifier.
     */
    public char getIdentifier() {
        return identifier;
    }

    /**
     * Get the action holder of this item.
     *
     * @return The action holder.
     * @see ActionHolder
     */
    public ActionHolder getActionHolder() {
        return actionHolder;
    }

    /**
     * Get the items' type.
     *
     * @return The type.
     */
    public EnumMenuItemType getType() {
        return type;
    }

    /**
     * Get the {@link EnumDisplayType} of this item.
     *
     * @return The display type.
     */
    public EnumDisplayType getDisplayType() {
        return displayType;
    }

    /**
     * Get the {@link EnumSlotType} of this item.
     *
     * @return The slot type.
     */
    public EnumSlotType getSlotType() {
        return slotType;
    }

    /**
     * Get the permission for this item.
     *
     * @return The permission.
     */
    public String getPermission() {
        return permission;
    }

}
