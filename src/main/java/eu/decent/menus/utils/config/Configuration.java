package eu.decent.menus.utils.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Configuration extends YamlConfiguration {

    private final String fileName;
    private final JavaPlugin plugin;
    private final String path;
    private File file;

    public Configuration(JavaPlugin javaPlugin, String name) {
        this(javaPlugin, name, null);
    }

    public Configuration(JavaPlugin javaPlugin, String name, String path) {
        this.plugin = javaPlugin;
        this.fileName = name.endsWith(".yml") ? name : name + ".yml";
        this.path = path;

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
            createData();
            saveData();
        }
    }

    @Override
    public void save(File file) throws IOException {
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

    /**
     * Delete the file of this configuration.
     */
    public void delete() {
        if (this.file.exists()) {
            this.file.delete();
        }
    }

}