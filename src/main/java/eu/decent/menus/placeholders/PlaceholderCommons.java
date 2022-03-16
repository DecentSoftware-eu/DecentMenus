package eu.decent.menus.placeholders;

import eu.decent.menus.Config;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.server.Server;
import eu.decent.menus.utils.BungeeUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@UtilityClass
public class PlaceholderCommons {

    private static final DecentMenus PLUGIN = DecentMenus.getInstance();

    public static int getFromServerOrServersInt(Player player, @NotNull String argument, @NotNull Function<Server, Integer> getValue) {
        // -- Get from multiple servers
        if (argument.contains(",")) {
            int total = 0;
            for (String s : argument.split(",")) {
                if (Config.PINGER_ENABLED && PLUGIN.getServerRegistry().contains(s)) {
                    Server server = PLUGIN.getServerRegistry().get(s);
                    if (server != null && server.isOnline()) {
                        total += getValue.apply(server);
                    }
                } else if (player != null) {
                    // -- If not pinged, get from bungee
                    try {
                        total += BungeeUtils.sendPlayerCountRequest(player, s).get();
                    } catch (InterruptedException | ExecutionException ignored) {}
                }
            }
            return total;
        }
        // -- Get from one server
        if (Config.PINGER_ENABLED && PLUGIN.getServerRegistry().contains(argument)) {
            Server server = PLUGIN.getServerRegistry().get(argument);
            if (server != null && server.isOnline()) {
                return getValue.apply(server);
            }
        } else if (player != null) {
            // -- If not pinged, get from bungee
            try {
                return BungeeUtils.sendPlayerCountRequest(player, argument).get();
            } catch (InterruptedException | ExecutionException ignored) {}
        }
        return -1;
    }

    public static int getFromWorldOrWorldsInt(@NotNull String argument, @NotNull Function<World, Integer> getValue) {
        // -- Get from multiple servers
        if (argument.contains(",")) {
            int total = 0;
            for (String s : argument.split(",")) {
                World world = Bukkit.getWorld(s);
                if (world != null) {
                    total += getValue.apply(world);
                }
            }
            return total;
        }
        // -- Get from one server
        World world = Bukkit.getWorld(argument);
        if (world != null) {
            return getValue.apply(world);
        }
        return -1;
    }

}
