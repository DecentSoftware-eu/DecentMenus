package eu.decent.menus;

import eu.decent.menus.utils.Common;
import eu.decent.menus.utils.config.ConfigValue;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Configuration of DecentMenus plugin. (config.yml)
 */
public final class Config {

    public static YamlConfiguration CONFIG;

    public static final String ADMIN_PERM = "decentmenus.admin";

    /*
     *  Messages
     */

    @ConfigValue("prefix")
    public static String PREFIX = "&8[&3DecentMenus&8] &7";
    @ConfigValue("messages.usage")
    public static String USAGE = "{prefix}Use: &b/dm <args> &7or &b/openmenu <menu>.";
    @ConfigValue("messages.no_perm")
    public static String NO_PERM = "{prefix}&cYou are not allowed to do this.";
    @ConfigValue("messages.unknown_sub_command")
    public static String UNKNOWN_SUB_COMMAND = "{prefix}&cUnknown sub command.";
    @ConfigValue("messages.menu_no_perm")
    public static String MENU_NO_PERM = "{prefix}You cannot open that menu.";
    @ConfigValue("messages.menu_usage")
    public static String MENU_USAGE = "{prefix}Usage: &b/decentmenus <list|ver>";
    @ConfigValue("messages.menu_open_usage")
    public static String MENU_OPEN_USAGE = "{prefix}Usage: &b/openmenu <menu>";
    @ConfigValue("messages.menu_does_not_exist")
    public static String MENU_DOES_NOT_EXIST = "{prefix}Menu called '%s' doesn't exist.";
    @ConfigValue("messages.menu_list")
    public static String MENU_LIST = "{prefix}Menus: &b%s";

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
