package eu.decent.menus.menu;

import eu.decent.menus.actions.ActionHolder;
import eu.decent.menus.conditions.ConditionHolder;
import eu.decent.menus.menu.item.MenuItem;
import eu.decent.menus.menu.item.MenuItemIntent;
import eu.decent.menus.menu.item.MenuSlotType;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.config.ConfigUtils;
import eu.decent.menus.utils.config.Configuration;
import eu.decent.menus.utils.item.ItemWrapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * This class represents a model that can be used to create a menu.
 */
@Getter
@Setter
@Builder
public class MenuModel {

    private final String name;
    private final Configuration config;
    private final Map<MenuIntent, ConditionHolder> conditionHolderMap;
    private final Map<MenuIntent, ActionHolder> actionHolderMap;
    private final Map<String, MenuItem> menuItemMap;
    private String title;
    private int size;
    private int lines;
    private List<String> slots;
    private boolean updating;
    private int updateInterval;

    /**
     * Create new {@link MenuModel}.
     *
     * @param name Name of the new model.
     * @param config Configuration of the new model.
     */
    public MenuModel(@NotNull String name, @NotNull Configuration config) {
        this.name = name;
        this.config = config;
        this.conditionHolderMap = new EnumMap<>(MenuIntent.class);
        this.actionHolderMap = new EnumMap<>(MenuIntent.class);
        this.menuItemMap = new HashMap<>();
        this.load();
    }

    /**
     * Check whether the given profile is allowed to perform the intended action.
     *
     * @param profile The profile.
     * @param intent The intention.
     * @return The requested boolean.
     */
    public boolean isAllowed(@NotNull PlayerProfile profile, @NotNull MenuIntent intent) {
        ConditionHolder conditionHolder = conditionHolderMap.get(intent);
        return conditionHolder == null || conditionHolder.checkAll(profile);
    }

    /**
     * Perform all actions assigned to the given intention.
     *
     * @param profile The profile that performs these actions.
     * @param intent The intention.
     */
    public void performActions(@NotNull PlayerProfile profile, @NotNull MenuIntent intent) {
        ActionHolder actionHolder = actionHolderMap.get(intent);
        if (actionHolder != null) {
            actionHolder.execute(profile);
        }
    }

    /**
     * Get the inventory size of this {@link MenuModel} safely.
     * <p>
     *     This is the only safe way to get the size as this method
     *     always returns a valid inventory size.
     * </p>
     *
     * @return The size.
     */
    public int getInventorySize() {
        if (size >= 9 && size <= 54 && size % 9 == 0) {
            return size;
        } else if (lines >= 1 && lines <= 6) {
            return lines * 9;
        } else if (slots != null && slots.size() > 0) {
            return slots.size() > 6 ? 54 : slots.size() * 9;
        }
        return 27;
    }

    /**
     * Load this {@link MenuModel} from its configuration.
     */
    private void load() {
        // -- Load settings
        this.title = config.getString("title", "Unnamed Menu");
        this.size = config.getInt("size", -1);
        this.lines = config.getInt("lines", -1);
        this.slots = config.getStringList("slots");
        this.updating = config.getBoolean("updating", true);
        this.updateInterval = config.getInt("update_interval", 20);

        // -- Load conditions
        executeForEachSection(config, "conditions", (key, section) -> {
            MenuIntent menuIntent = MenuIntent.fromString(key);
            if (menuIntent != null) {
                ConditionHolder conditionHolder = ConditionHolder.load(section);
                conditionHolderMap.put(menuIntent, conditionHolder);
            }
        });

        // -- Load actions
        executeForEachSection(config, "actions", (key, section) -> {
            MenuIntent menuIntent = MenuIntent.fromString(key);
            if (menuIntent != null) {
                ActionHolder actionHolder = ActionHolder.load(section);
                actionHolderMap.put(menuIntent, actionHolder);
            }
        });

        // -- Load items
        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection == null) {
                    continue;
                }
                // -- Load item
                int slot = itemSection.getInt("slot", -1);
                MenuSlotType slotType = MenuSlotType.fromName(itemSection.getString("slot_type", "DEFAULT"));
                ItemWrapper itemWrapper = ConfigUtils.getItemWrapper(itemSection, "item");
                ItemWrapper errorItemWrapper = ConfigUtils.getItemWrapper(itemSection, "error_item");

                MenuItem menuItem = new MenuItem(key, itemSection, itemWrapper, errorItemWrapper);
                menuItem.setSlotType(slotType);
                menuItem.setSlot(slot);

                // -- Load item conditions
                executeForEachSection(itemSection, "conditions", (sectionKey, section) -> {
                    MenuItemIntent menuItemIntent = MenuItemIntent.fromString(sectionKey);
                    if (menuItemIntent != null) {
                        ConditionHolder conditionHolder = ConditionHolder.load(section);
                        menuItem.getConditionHolderMap().put(menuItemIntent, conditionHolder);
                    }
                });

                // -- Load item actions
                executeForEachSection(itemSection, "conditions", (sectionKey, section) -> {
                    MenuItemIntent menuItemIntent = MenuItemIntent.fromString(sectionKey);
                    if (menuItemIntent != null) {
                        ActionHolder actionHolder = ActionHolder.load(section);
                        menuItem.getActionHolderMap().put(menuItemIntent, actionHolder);
                    }
                });

                this.menuItemMap.put(key, menuItem);
            }
        }
    }

    @Contract(" -> new")
    public static @NotNull MenuModelBuilder create() {
        return new MenuModelBuilder();
    }

    /*
     *  Utility Methods
     */

    private void executeForEachSection(@NotNull ConfigurationSection config,
                                       @NotNull String path,
                                       @NotNull BiConsumer<String, ConfigurationSection> execute) {
        ConfigurationSection actionsSection = config.getConfigurationSection(path);
        if (actionsSection != null) {
            for (String key : actionsSection.getKeys(false)) {
                ConfigurationSection section = actionsSection.getConfigurationSection(key);
                if (section == null) {
                    continue;
                }
                execute.accept(key, section);
            }
        }
    }

}
