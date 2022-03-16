package eu.decent.menus;

import eu.decent.menus.api.commands.CommandManager;
import eu.decent.menus.commands.DecentMenusCommand;
import eu.decent.menus.commands.OpenMenuCommand;
import eu.decent.menus.menu.MenuListener;
import eu.decent.menus.menu.MenuRegistry;
import eu.decent.menus.placeholders.PlaceholderRegistry;
import eu.decent.menus.player.PlayerListener;
import eu.decent.menus.player.PlayerRegistry;
import eu.decent.menus.server.ServerRegistry;
import eu.decent.menus.utils.BungeeUtils;
import eu.decent.menus.utils.ticker.Ticker;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * A simple menu plugin with nice configuration and a lot of neat features.
 *
 * @author d0by
 */
@Getter
public final class DecentMenus extends JavaPlugin {

    private static DecentMenus instance;
    private PlayerRegistry playerRegistry;
    private ServerRegistry serverRegistry;
    private PlaceholderRegistry placeholderRegistry;
    private MenuRegistry menuRegistry;
    private CommandManager commandManager;
    private Ticker ticker;
    // The 'config.yml' file
    private File configFile;

    public DecentMenus() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Config.reload();

        this.playerRegistry = new PlayerRegistry();
        this.serverRegistry = new ServerRegistry();
        this.placeholderRegistry = new PlaceholderRegistry();
        this.menuRegistry = new MenuRegistry();
        this.commandManager = new CommandManager();
        this.ticker = new Ticker();

        // Register listeners
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new MenuListener(), this);

        // Register commands
        commandManager.registerCommand(new DecentMenusCommand());
        commandManager.registerCommand(new OpenMenuCommand());

        // Register Bungee channel
        BungeeUtils.init();
    }

    @Override
    public void onDisable() {
        this.ticker.shutdown();
        this.menuRegistry.shutdown();
        this.serverRegistry.shutdown();
        this.playerRegistry.shutdown();
        this.commandManager.destroy();

        // Unregister Bungee channel
        BungeeUtils.shutdown();

        HandlerList.unregisterAll(this);
    }

    /**
     * Reload this plugin.
     */
    public void reload() {
        Config.reload();

        this.menuRegistry.reload();
        this.serverRegistry.reload();
        this.playerRegistry.reload();
    }

    /**
     * Get the 'config.yml' file.
     *
     * @return The file.
     */
    public File getConfigFile() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        return configFile;
    }

    /**
     * Get the instance of this plugin.
     *
     * @return The instance.
     */
    public static DecentMenus getInstance() {
        return instance;
    }

}
