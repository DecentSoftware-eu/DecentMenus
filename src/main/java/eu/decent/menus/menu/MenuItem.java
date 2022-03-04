package eu.decent.menus.menu;

import eu.decent.menus.actions.ActionHolder;
import eu.decent.menus.conditions.ConditionHolder;
import eu.decent.menus.conditions.ConditionIntent;
import eu.decent.menus.menu.enums.EnumSlotType;
import eu.decent.menus.utils.item.ItemWrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class represents a menu item.
 *
 * TODO:
 *  - implement everything
 *  - I/O impl
 *  - remove old menu items
 *  - PAPI, colors support
 */
@Getter
@Setter
public class MenuItem {

    private final char identifier;
    private final ConfigurationSection config;
    private final Map<ConditionIntent, ConditionHolder> conditionHolderMap;
    private final Map<ConditionIntent, ActionHolder> actionHolderMap;
    private ItemWrapper itemWrapper;
    private EnumSlotType slotType;
    private boolean updating;

    /**
     * Create new {@link MenuItem}.
     *
     * @param identifier The items' identifier from config.
     * @param config The configuration section responsible for this item.
     */
    public MenuItem(char identifier, ConfigurationSection config) {
        this.identifier = identifier;
        this.config = config;
        this.conditionHolderMap = new EnumMap<>(ConditionIntent.class);
        this.actionHolderMap = new EnumMap<>(ConditionIntent.class);
        this.updating = true;
    }



}
