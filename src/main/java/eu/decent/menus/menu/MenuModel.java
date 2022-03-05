package eu.decent.menus.menu;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import eu.decent.library.utils.collection.DMap;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.utils.config.ConfigUtils;
import eu.decent.menus.utils.config.Configuration;
import eu.decent.menus.utils.item.ItemWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MenuModel {

    private final String name;
    private final Configuration config;
    private final String title;
    private final String permission;
    private final List<String> slots;
    private final Map<Character, MenuItem> items;
    private final boolean updating;
    private final int updateInterval;

    /**
     * Check whether the given player is allowed to open the menu.
     *
     * @param player The player.
     * @return The result.
     */
    public boolean hasPermission(Player player) {
        return permission == null || player.hasPermission(permission);
    }

    /**
     * Load a {@link MenuModel} from file.
     *
     * @param fileName The files name.
     * @return The MenuModel.
     */
    public static MenuModel fromFile(@NotNull String fileName) {
        // TODO: check whether the file exists

        Configuration config = new Configuration(DecentMenus.getInstance(), "menus/" + fileName);

        // Parse model name
        String name;
        if (fileName.toLowerCase().startsWith("menu_") && fileName.length() > "menu_".length()) {
            name = fileName.substring("menu_".length(), fileName.length() - 4);
        } else {
            name = fileName.substring(0, fileName.length() - 4);
        }
        String title = config.getString("title", "Unnamed Menu");
        String permission = config.getString("permission", null);
        boolean updating = config.getBoolean("updating", false);
        int updateInterval = config.getInt("update_interval", 20);

        // Get the slots
        List<String> slots = config.getStringList("slots");
        if (slots.isEmpty()) {
            slots = Lists.newArrayList(Strings.repeat(" ", 9));
        }

        DMap<Character, MenuItem> itemMap = new DMap<>();
        if (!config.contains("items")) {
            config.createSection("items");
        }
        // Load the items
        ConfigurationSection items = config.getConfigurationSection("items");
        if (items != null) {
            for (String key : items.getKeys(false)) {
                if (key.length() != 1) {
                    continue;
                }
                char ch = key.charAt(0);
                ConfigurationSection section = items.getConfigurationSection(key);
                ItemWrapper itemWrapper = ConfigUtils.getItemWrapper(section, "item");
                itemMap.put(ch, new MenuItem(ch, section, itemWrapper));
            }
        }
        return new MenuModel(name, config, title, permission, slots, itemMap, updating, updateInterval);
    }

}
