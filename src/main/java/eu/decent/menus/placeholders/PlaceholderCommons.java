package eu.decent.menus.placeholders;

import eu.decent.menus.DecentMenus;
import eu.decent.menus.server.Server;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@UtilityClass
public class PlaceholderCommons {

    private static final DecentMenus PLUGIN = DecentMenus.getInstance();

    public static int getFromServerOrServersInt(@NotNull String argument, @NotNull Function<Server, Integer> getValue) {
        // -- Get from multiple servers
        if (argument.contains(",")) {
            int total = 0;
            for (String s : argument.split(",")) {
                Server server = PLUGIN.getServerRegistry().get(argument);
                if (server != null && server.isOnline()) {
                    total += getValue.apply(server);
                }
            }
            return total;
        }
        // -- Get from one server
        Server server = PLUGIN.getServerRegistry().get(argument);
        if (server != null && server.isOnline()) {
            return getValue.apply(server);
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
