package eu.decent.menus.utils.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import eu.decent.menus.utils.item.ItemWrapper;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ConfigUtils {

    /**
     * Load an ItemWrapper from the given ConfigurationSection.
     *
     * @param config The ConfigurationSection.
     * @param path The path.
     * @return The ItemWrapper.
     */
    @NotNull
    public static ItemWrapper getItemWrapper(@NotNull ConfigurationSection config, String path) {
        path = preparePath(path);
        String materialName = config.getString(path + "material");
        Material material = materialName == null ? null : Material.getMaterial(materialName);
        String name = config.getString(path + "name", null);
        int amount = config.getInt(path + "amount", 1);
        short durability = (short) config.getInt(path + "durability", 0);
        List<String> lore = config.getStringList(path + "lore");
        Map<Enchantment, Integer> enchantments = Maps.newHashMap();
        config.getStringList(path + "enchantments").forEach(s -> {
            String[] spl = s.split(":");
            Enchantment enchantment = Enchantment.getByName(spl[0]);
            int level = Integer.parseInt(spl[1]);
            enchantments.put(enchantment, level);
        });
        String skullOwnerName = config.getString(path + "skull.owner");
        String skullTexture = config.getString(path + "skull.texture");
        int customModelData = config.getInt(path + "customModelData");
        ItemFlag[] flags = config.getStringList(path + "flags").stream().map(ItemFlag::valueOf).toArray(ItemFlag[]::new);
        boolean unbreakable = config.getBoolean(path + "unbreakable", false);
        return new ItemWrapper(material, name, skullOwnerName, skullTexture, customModelData, amount, durability, lore, enchantments, flags, unbreakable);
    }

    /**
     * Save the given {@link ItemWrapper} into a ConfigurationSection.
     *
     * @param config The ConfigurationSection.
     * @param path The path.
     * @param itemWrapper The ItemWrapper.
     */
    public static void setItemWrapper(@NotNull ConfigurationSection config, String path, @NotNull ItemWrapper itemWrapper) {
        path = preparePath(path);
        config.set(path + "material", itemWrapper.getMaterial().name());
        config.set(path + "name", itemWrapper.getName());
        config.set(path + "amount", itemWrapper.getAmount());
        config.set(path + "durability", itemWrapper.getDurability());
        config.set(path + "lore", itemWrapper.getLore());
        config.set(path + "enchantments", itemWrapper.getEnchantments().entrySet().stream()
                .map(e -> String.format("%s:%d", e.getKey().getName(), e.getValue()))
                .collect(Collectors.toList()));
        config.set(path + "skull.owner", itemWrapper.getSkullOwner());
        config.set(path + "skull.texture", itemWrapper.getSkullTexture());
        config.set(path + "customModelData", itemWrapper.getCustomModelData());
        config.set(path + "flags", Arrays.stream(itemWrapper.getFlags()).map(Enum::name).collect(Collectors.toList()));
        config.set(path + "unbreakable", itemWrapper.isUnbreakable());
    }

    /**
     * Get all keys from a ConfigurationSection on the given path in the given ConfigurationSection.
     *
     * @param config The ConfigurationSection.
     * @param path The path.
     * @return Set of the keys.
     */
    public Set<String> getSectionKeys(ConfigurationSection config, String path) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            return Sets.newHashSet();
        }
        return section.getKeys(false);
    }

    @NotNull
    private static String preparePath(String path) {
        if (path == null) {
            path = "";
        } else if (path.length() > 0 && !path.endsWith(".")) {
            path += ".";
        }
        return path;
    }

}
