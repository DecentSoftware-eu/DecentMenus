package eu.decent.menus.menu.item;

import eu.decent.menus.actions.ActionHolder;
import eu.decent.menus.conditions.ConditionHolder;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.item.ItemWrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class represents a menu item.
 */
@Getter
@Setter
public class MenuItem {

    private final String name;
    private final ConfigurationSection config;
    private final Map<MenuItemIntent, ConditionHolder> conditionHolderMap;
    private final Map<MenuItemIntent, ActionHolder> actionHolderMap;
    private ItemWrapper itemWrapper;
    private ItemWrapper errorItemWrapper;
    private MenuSlotType slotType;
    private int slot;
    private boolean updating;

    /**
     * Create new {@link MenuItem}.
     *
     * @param name The items' identifier from config.
     * @param config The configuration section responsible for this item.
     */
    public MenuItem(@NotNull String name, @NotNull ConfigurationSection config, @NotNull ItemWrapper itemWrapper) {
        this(name, config, itemWrapper, null);
    }

    /**
     * Create new {@link MenuItem}.
     *
     * @param name The items' identifier from config.
     * @param config The configuration section responsible for this item.
     */
    public MenuItem(@NotNull String name, @NotNull ConfigurationSection config, @NotNull ItemWrapper itemWrapper, ItemWrapper errorItemWrapper) {
        this.name = name;
        this.config = config;
        this.itemWrapper = itemWrapper;
        this.errorItemWrapper = errorItemWrapper;
        this.conditionHolderMap = new EnumMap<>(MenuItemIntent.class);
        this.actionHolderMap = new EnumMap<>(MenuItemIntent.class);
        this.slotType = MenuSlotType.DEFAULT;
        this.slot = -1;
        this.updating = true;
    }

    /**
     * Check whether the given profile is allowed to perform the intended action.
     *
     * @param profile The profile.
     * @param intent The intention.
     * @return The requested boolean.
     */
    public boolean isAllowed(@NotNull PlayerProfile profile, @NotNull MenuItemIntent intent) {
        ConditionHolder conditionHolder = conditionHolderMap.get(intent);
        return conditionHolder == null || conditionHolder.checkAll(profile);
    }

    /**
     * Perform all actions assigned to the given intention.
     *
     * @param profile The profile that performs these actions.
     * @param intent The intention.
     */
    public void performActions(@NotNull PlayerProfile profile, @NotNull MenuItemIntent intent) {
        ActionHolder actionHolder = actionHolderMap.get(intent);
        if (actionHolder != null) {
            actionHolder.execute(profile);
        }
    }

    /**
     * Check whether the given {@link PlayerProfile} can click this {@link MenuItem}.
     *
     * @param profile The profile.
     * @param e The click event that's responsible for this click.
     * @return The requested boolean.
     */
    public boolean canClick(@NotNull PlayerProfile profile, @NotNull InventoryClickEvent e) {
        // Check the global conditions (any click type)
        boolean globalConditionsMet = isAllowed(profile, MenuItemIntent.CLICK);
        if (globalConditionsMet) {
            // Check the specific conditions (per click type)
            ClickType clickType = e.getClick();
            MenuItemIntent menuItemIntent = MenuItemIntent.fromClickType(clickType);
            if (menuItemIntent != null) {
                return isAllowed(profile, menuItemIntent);
            }
        }
        return globalConditionsMet;
    }

    /**
     * Perform a click by the given {@link PlayerProfile}.
     * <p>
     *     This method also checks the profiles ability to click this {@link MenuItem}.
     * </p>
     *
     * @param profile The profile.
     * @param e The click event that's responsible for this click.
     */
    public void onClick(@NotNull PlayerProfile profile, @NotNull InventoryClickEvent e) {
        // Check the profiles' ability to click
        if (!canClick(profile, e)) {
            return;
        }

        // Execute the global actions (any click type)
        performActions(profile, MenuItemIntent.CLICK);

        // Execute the specific actions (per click type)
        ClickType clickType = e.getClick();
        MenuItemIntent menuItemIntent = MenuItemIntent.fromClickType(clickType);
        if (menuItemIntent != null) {
            performActions(profile, menuItemIntent);
        }
    }

}
