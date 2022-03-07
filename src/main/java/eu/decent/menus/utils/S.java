package eu.decent.menus.utils;

import eu.decent.menus.DecentMenus;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
@SuppressWarnings({"unused", "deprecation"})
public final class S {

    private static final DecentMenus PLUGIN = DecentMenus.getInstance();

    public static void cancel(int id) {
        Bukkit.getScheduler().cancelTask(id);
    }

    public static void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, runnable);
    }

    public static void async(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, runnable, delay);
    }

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(PLUGIN, runnable);
    }

    public static void run(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(PLUGIN, runnable, delay);
    }

    public static int scheduleAsync(Runnable runnable, long interval) {
        return scheduleAsync(runnable, 0L, interval);
    }

    public static int scheduleAsync(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().scheduleAsyncRepeatingTask(PLUGIN, runnable, delay, interval);
    }

    public static int scheduleSync(Runnable runnable, long interval) {
        return scheduleSync(runnable, 0L, interval);
    }

    public static int scheduleSync(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(PLUGIN, runnable, delay, interval);
    }

}
