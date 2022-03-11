package eu.decent.menus.utils;

import eu.decent.menus.utils.colors.IridiumColorAPI;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Level;

/**
 * Utility class with some useful methods.
 */
@UtilityClass
public final class Common {

    /*
     * 	General
     */

    /**
     * Generate a random integer between min and max.
     *
     * @param min The min value.
     * @param max The max value.
     * @return The random number.
     */
    public static int irand(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    /*
     * 	Colorize
     */

    /**
     * Colorize the given string replacing all color codes, hex colors, gradients and rainbows.
     *
     * @param string The string.
     * @return The colorized string.
     */
    @NotNull
    public static String colorize(String string) {
        return IridiumColorAPI.process(string);
    }

    /**
     * Colorize the given string list replacing all color codes, hex colors, gradients and rainbows.
     *
     * @param list The string list.
     * @return The colorized string list.
     */
    public static List<String> colorize(@NotNull List<String> list) {
        list.replaceAll(Common::colorize);
        return list;
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
