package eu.decent.menus.utils.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import eu.decent.menus.utils.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configuration extends YamlConfiguration {

    private final String fileName;
    private final JavaPlugin plugin;
    private File file;

    public Configuration(JavaPlugin javaPlugin, String name) {
        this.plugin = javaPlugin;
        this.fileName = name.endsWith(".yml") ? name : name + ".yml";

        loadFile();
        createData();

        try {
            loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() throws IOException, InvalidConfigurationException {
        this.load(file);
    }

    public File loadFile() {
        this.file = new File(this.plugin.getDataFolder(), this.fileName);
        return this.file;
    }

    public void saveData() {
        this.file = new File(this.plugin.getDataFolder(), this.fileName);
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Attempting to fix error...");
            createData();
            saveData();
        }
    }

    @Override
    public void save(File file) throws IOException {
        super.save(file);
    }

    public void reload() {
        try {
            this.loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void createData() {
        if (!file.exists()) {
            if (!this.plugin.getDataFolder().exists()) {
                this.plugin.getDataFolder().mkdirs();
            }

            // If file isn't a resource, create from scratch
            if (this.plugin.getResource(this.fileName) == null) {
                try {
                    this.file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                this.plugin.saveResource(this.fileName, false);
            }
        }
    }

    public void delete() {
        if (this.file.exists()) {
            this.file.delete();
        }
    }

    public Set<String> getSectionKeys(String path) {
        if (!contains(path)) {
            return Sets.newHashSet();
        }
        return getConfigurationSection(path).getKeys(false);
    }

    public Object getOrDefault(String path, Object defaultValue) {
        if (!contains(path)) {
            set(path, defaultValue);
            return defaultValue;
        }
        return get(path);
    }

    public ItemStack getItem(String path) {
        return getItemWrapper(path).parse();
    }

    public ItemWrapper getItemWrapper(String path) {
        String materialName = getString(path + ".material");
        Material material = materialName == null ? null : Material.getMaterial(materialName);
        String name = getString(path + ".name", null);
        int amount = getInt(path + ".amount", 1);
        short durability = (short) getInt(path + ".durability", 0);
        List<String> lore = getStringList(path + ".lore");
        Map<Enchantment, Integer> enchantments = Maps.newHashMap();
        this.getStringList(path + ".enchantments").forEach(s -> {
            String[] spl = s.split(":");
            Enchantment enchantment = Enchantment.getByName(spl[0]);
            int level = Integer.parseInt(spl[1]);
            enchantments.put(enchantment, level);
        });
        String skullOwnerName = getString(path + ".skull.owner");
        String skullTexture = getString(path + ".skull.texture");
        ItemFlag[] flags = getStringList(path + ".flags").stream().map(ItemFlag::valueOf).toArray(ItemFlag[]::new);
        boolean unbreakable = getBoolean(path + ".unbreakable", false);
        return new ItemWrapper(material, name, skullOwnerName, skullTexture, amount, durability, lore, enchantments, flags, unbreakable);
    }

}