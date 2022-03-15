package eu.decent.menus;

import eu.decent.menus.utils.Common;
import eu.decent.menus.utils.config.CFG;
import eu.decent.menus.utils.config.ConfigValue;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration of DecentMenus plugin. (config.yml)
 */
public final class Config {

    public static final String ADMIN_PERM = "ds.decentmenus.admin";

    @Getter
    private static YamlConfiguration config;

    /*
     *  Options
     */

    // -- General

    @ConfigValue("check-for-updates")
    public static boolean CHECK_FOR_UPDATES = true;

    // -- Defaults

    @ConfigValue("defaults.menu-title")
    public static String DEFAULT_MENU_TITLE = "Unnamed Menu";

    // -- Date & Time

    @ConfigValue("datetime.time-format")
    public static String DATETIME_TIME_FORMAT = "HH:mm:ss";
    @ConfigValue("datetime.date-format")
    public static String DATETIME_DATE_FORMAT = "dd:MM:yyyy";
    @ConfigValue("datetime.zone")
    public static String DATETIME_ZONE = "GMT+0";

    // -- Messages

    @ConfigValue("messages.prefix")
    public static String PREFIX = "&8[&3DecentMenus&8] &7";
    @ConfigValue("messages.usage")
    public static String USAGE = "{prefix}Use: &b/dm <args> &7or &b/openmenu <menu>.";
    @ConfigValue("messages.no-perm")
    public static String NO_PERM = "{prefix}&cYou are not allowed to do this.";
    @ConfigValue("messages.unknown-sub-command")
    public static String UNKNOWN_SUB_COMMAND = "{prefix}&cUnknown sub command.";
    @ConfigValue("messages.menu-no-perm")
    public static String MENU_NO_PERM = "{prefix}You cannot open that menu.";
    @ConfigValue("messages.menu-usage")
    public static String MENU_USAGE = "{prefix}Usage: &b/decentmenus <list|ver>";
    @ConfigValue("messages.menu-open-usage")
    public static String MENU_OPEN_USAGE = "{prefix}Usage: &b/openmenu <menu>";
    @ConfigValue("messages.menu-does-not-exist")
    public static String MENU_DOES_NOT_EXIST = "{prefix}Menu called '%s' doesn't exist.";
    @ConfigValue("messages.menu-list")
    public static String MENU_LIST = "{prefix}Menus: &b%s";

    // -- Pinger

    @ConfigValue("pinger.enabled")
    public static boolean PINGER_ENABLED = false;
    @ConfigValue(value = "pinger.update-interval", min = 20, max = 1200)
    public static long PINGER_UPDATE_INTERVAL = 20;
    @ConfigValue(value = "pinger.timeout", min = 50, max = 5000)
    public static int PINGER_TIMEOUT = 500;
    @ConfigValue("pinger.servers")
    public static List<String> PINGER_SERVERS = new ArrayList<>();
    @ConfigValue("pinger.status.online")
    public static String PINGER_STATUS_ONLINE = "&aOnline";
    @ConfigValue("pinger.status.offline")
    public static String PINGER_STATUS_OFFLINE = "&cOffline";
    @ConfigValue("pinger.trim-motd")
    public static boolean PINGER_TRIM_MOTD = true;

    // --

    /*
     *  General Methods
     */

    /**
     * Reload the configuration.
     */
    public static void reload() {
        Config.config = CFG.load(Config.class, DecentMenus.getInstance().getConfigFile());
    }

    /*
     *  Utility Methods
     */

    /**
     * Send a message to command sender. This method replaces '{prefix}' placeholder
     * and translates all color codes including hex colors.
     *
     * @param sender The command sender.
     * @param message The message.
     * @param args Arguments for java string formatting.
     */
    public static void send(CommandSender sender, String message, Object... args) {
        message = message.replace("{prefix}", PREFIX);
        Common.tell(sender, message, args);
    }

    /**
     * Send the version message to a command sender.
     *
     * @param sender The command sender.
     */
    public static void sendVersionMessage(CommandSender sender) {
        Common.tell(sender,
                "\n&fThis server is running &3DecentMenus v%s&f by &bd0by&f : &7%s",
                DecentMenus.getInstance().getDescription().getVersion(),
                "https://www.spigotmc.org/resources/96927/" // TODO edit id
        );
    }

    /**
     * Notify the given command sender about an update.
     *
     * @param sender The command sender.
     */
    public static void sendUpdateMessage(CommandSender sender) {
        Common.tell(sender,
                "\n&fA newer version of &3DecentMenus &fis available. Download it from: &7%s",
                "https://www.spigotmc.org/resources/96927/" // TODO edit id
        );
    }

}
