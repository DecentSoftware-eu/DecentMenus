package eu.decent.menus.utils;

import eu.decent.menus.utils.colors.IridiumColorAPI;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Level;

@UtilityClass
public final class Common {

    /*
     * 	General
     */

    public static int irand(int f, int t) {
        return f + (int) (Math.random() * ((t - f) + 1));
    }

    public static String formatSeconds(int seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    /*
     * 	Colorize
     */

    @NotNull
    public static String colorize(String string) {
        return IridiumColorAPI.process(string);
    }

    public static List<String> colorize(@NotNull List<String> list) {
        list.replaceAll(Common::colorize);
        return list;
    }

    public static String stripColors(String string) {
        return ChatColor.stripColor(IridiumColorAPI.stripColorFormatting(string));
    }

    /*
     * 	Log
     */

    /**
     * Log a message into console.
     *
     * @param message The message.
     */
    public static void log(String message) {
        log(Level.INFO, message);
    }

    /**
     * Log a message into console.
     * <p>
     *     This method formats given arguments in the message.
     * </p>
     *
     * @param message The message.
     * @param args The arguments
     */
    public static void log(String message, Object... args) {
        log(String.format(message, args));
    }

    /**
     * Log a message into console.
     *
     * @param level Level of this message.
     * @param message The message.
     */
    public static void log(Level level, String message) {
        Bukkit.getServer().getLogger().log(level, String.format("[DecentMenus] %s", message));
    }

    /**
     * Log a message into console.
     * <p>
     *     This method formats given arguments in the message.
     * </p>
     *
     * @param level Level of this message.
     * @param message The message.
     * @param args The arguments.
     */
    public static void log(Level level, String message, Object... args) {
        log(level, String.format(message, args));
    }

    /*
     * 	Debug
     */

    /**
     * Print an object into console.
     *
     * @param o Object to print.
     */
    public static void debug(Object o) {
        System.out.println(o);
    }

    /*
     * 	Tell
     */

    /**
     * Send a message to given CommandSender.
     * <p>
     *     This method will colorize the message.
     * </p>
     *
     * @param player The CommandSender receiving the message.
     * @param message The message.
     */
    public static void tell(@NotNull CommandSender player, String message) {
        player.sendMessage(colorize(message));
    }

    /**
     * Send a message to given CommandSender.
     * <p>
     *     This method will colorize the message and formats given arguments to the message.
     * </p>
     *
     * @param player The CommandSender receiving the message.
     * @param message The message.
     * @param args The arguments.
     */
    public static void tell(CommandSender player, String message, Object... args) {
        tell(player, String.format(message, args));
    }

}
