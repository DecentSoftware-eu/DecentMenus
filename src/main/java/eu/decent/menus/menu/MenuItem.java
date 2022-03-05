package eu.decent.menus.menu;

import eu.decent.menus.actions.ActionHolder;
import eu.decent.menus.conditions.ConditionHolder;
import eu.decent.menus.conditions.ConditionIntent;
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
 *
 * TODO:
 *  - implement everything
 *  - I/O impl
 *  - PAPI, colors support
 */
@Getter
@Setter
public class MenuItem {

    // Both options have to work
    // Option #1
    private String name;
    private int slot;
    // Option #2
    private final char identifier;

    private final ConfigurationSection config;
    private final Map<ConditionIntent, ConditionHolder> conditionHolderMap;
    private final Map<ConditionIntent, ActionHolder> actionHolderMap;
    private ItemWrapper itemWrapper;
    private MenuSlotType slotType;
    private boolean updating;

    /**
     * Create new {@link MenuItem}.
     *
     * @param identifier The items' identifier from config.
     * @param config The configuration section responsible for this item.
     */
    public MenuItem(char identifier, @NotNull ConfigurationSection config, @NotNull ItemWrapper itemWrapper) {
        this.identifier = identifier;
        this.config = config;
        this.itemWrapper = itemWrapper;
        this.conditionHolderMap = new EnumMap<>(ConditionIntent.class);
        this.actionHolderMap = new EnumMap<>(ConditionIntent.class);
        this.updating = true;
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
        ConditionHolder globalConditionHolder = conditionHolderMap.get(ConditionIntent.CLICK);
        boolean globalConditionsMet = globalConditionHolder == null || globalConditionHolder.checkAll(profile);
        if (globalConditionsMet) {
            // Check the specific conditions (per click type)
            ClickType clickType = e.getClick();
            ConditionIntent conditionIntent = ConditionIntent.fromClickType(clickType);
            if (conditionIntent != null) {
                ConditionHolder conditionHolder = conditionHolderMap.get(conditionIntent);
                return conditionHolder == null || conditionHolder.checkAll(profile);
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
        ActionHolder globalActionHolder = actionHolderMap.get(ConditionIntent.CLICK);
        if (globalActionHolder != null) {
            globalActionHolder.execute(profile);
        }

        // Execute the specific actions (per click type)
        ClickType clickType = e.getClick();
        ConditionIntent conditionIntent = ConditionIntent.fromClickType(clickType);
        if (conditionIntent != null) {
            ActionHolder actionHolder = actionHolderMap.get(conditionIntent);
            if (actionHolder != null) {
                actionHolder.execute(profile);
            }
        }
    }

}
