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
    public MenuItem(char identifier, ConfigurationSection config) {
        this.identifier = identifier;
        this.config = config;
        this.conditionHolderMap = new EnumMap<>(ConditionIntent.class);
        this.actionHolderMap = new EnumMap<>(ConditionIntent.class);
        this.updating = true;
    }

    public boolean canClick(@NotNull PlayerProfile profile, @NotNull InventoryClickEvent e) {
        ClickType clickType = e.getClick();
        boolean globalConditions = conditionHolderMap.get(ConditionIntent.CLICK).checkAll(profile);
        if (globalConditions) {
            ConditionIntent conditionIntent = ConditionIntent.fromClickType(clickType);
            if (conditionIntent != null) {
                ConditionHolder conditionHolder = conditionHolderMap.get(conditionIntent);
                return conditionHolder == null || conditionHolder.checkAll(profile);
            }
        }
        return globalConditions;
    }

    public void onClick(@NotNull PlayerProfile profile, @NotNull InventoryClickEvent e) {
        if (!canClick(profile, e)) {
            return;
        }


    }

}
