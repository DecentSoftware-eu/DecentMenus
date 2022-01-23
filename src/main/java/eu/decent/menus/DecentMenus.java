package eu.decent.menus;

import eu.decent.library.DecentPlugin;
import eu.decent.library.command.CommandManager;
import eu.decent.library.config.CFG;
import eu.decent.library.ticker.Ticker;
import eu.decent.menus.commands.DecentMenusCommand;
import eu.decent.menus.commands.OpenMenuCommand;
import eu.decent.menus.menu.MenuListener;
import eu.decent.menus.menu.MenuRegistry;
import eu.decent.menus.player.PlayerListener;
import eu.decent.menus.player.PlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.io.File;

/**
 * A simple menu plugin with nice configuration and a lot of neat features.
 *
 * @author d0by
 */
public final class DecentMenus extends DecentPlugin {

    private static DecentMenus instance;
    private PlayerRegistry playerRegistry;
    private MenuRegistry menuRegistry;
    private CommandManager commandManager;
    private Ticker ticker;
    // The 'config.yml' file
    private File configFile;

    public DecentMenus() {
        instance = this;
    }

    @Override
    public void load() {

    }

    @Override
    public void enable() {
        Config.CONFIG = CFG.load(Config.class, getConfigFile());
        this.playerRegistry = new PlayerRegistry();
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
    }

    @Override
    public void disable() {
        this.ticker.shutdown();
        this.menuRegistry.shutdown();
        this.playerRegistry.shutdown();
        this.commandManager.unregisterCommand("decentmenus");
        this.commandManager.unregisterCommand("openmenu");
        this.commandManager.destroy();
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
     * Get the {@link PlayerRegistry} of this plugin.
     *
     * @return The PlayerRegistry.
     */
    public PlayerRegistry getPlayerRegistry() {
        return playerRegistry;
    }

    /**
     * Get the {@link MenuRegistry} of this plugin.
     *
     * @return The MenuRegistry.
     */
    public MenuRegistry getMenuRegistry() {
        return menuRegistry;
    }

    /**
     * Get the {@link CommandManager} of this plugin.
     *
     * @return The CommandManager.
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Get the {@link Ticker} of this plugin.
     *
     * @return The Ticker.
     */
    public Ticker getTicker() {
        return ticker;
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
