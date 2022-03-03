package eu.decent.menus.menu.item;

import eu.decent.library.actions.IAction;
import eu.decent.library.hooks.PAPI;
import eu.decent.menus.actions.ActionHolder;
import eu.decent.menus.menu.Menu;
import eu.decent.menus.menu.enums.EnumDisplayType;
import eu.decent.menus.menu.enums.EnumMenuItemType;
import eu.decent.menus.menu.enums.EnumSlotType;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class represents a menu item.
 */
@Getter
public abstract class MenuItem {

    protected final ConfigurationSection config;
    protected final char identifier;
    protected final ActionHolder actionHolder;
    protected final EnumMenuItemType type;
    protected EnumDisplayType displayType;
    protected EnumSlotType slotType;
    protected String permission;
    protected boolean updating; // TODO

    /**
     * Create new {@link MenuItem}.
     *
     * @param config The configuration section responsible for this item.
     * @param identifier The items' identifier from config.
     * @param type The items' type.
     */
    public MenuItem(ConfigurationSection config, char identifier, EnumMenuItemType type) {
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
        for (String actionString: actions) {
            // TODO
//            actionHolder.append(null);
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
    public List<String> setPlaceholders(Player player, @NotNull List<String> strings) {
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
    public void onClick(@NotNull Menu menu, InventoryClickEvent event) {
        actionHolder.execute(menu.getOwner());
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

}
