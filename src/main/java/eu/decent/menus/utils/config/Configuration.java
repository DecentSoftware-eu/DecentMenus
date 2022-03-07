package eu.decent.menus.utils.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Configuration extends YamlConfiguration {

    private final String fileName;
    private final JavaPlugin plugin;
    private File file;

    public Configuration(JavaPlugin javaPlugin, @NotNull String name) {
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
        return this.file = new File(plugin.getDataFolder(), this.fileName);
    }

    public void saveData() {
        this.file = new File(plugin.getDataFolder(), this.fileName);
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
            createData();
            saveData();
        }
    }

    @Override
    public void save(@NotNull File file) throws IOException {
        super.save(file);
    }

    /**
     * Reload this configuration.
     */
    public void reload() {
        try {
            this.loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void createData() {
        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();

            // If file isn't a resource, create from scratch
            if (plugin.getResource(this.fileName) == null) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                plugin.saveResource(this.fileName, false);
            }
        }
    }

    /**
     * Delete the file of this configuration.
     */
    public void delete() {
        if (this.file.exists()) {
            this.file.delete();
        }
    }

}