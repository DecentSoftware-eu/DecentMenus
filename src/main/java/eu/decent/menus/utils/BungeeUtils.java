package eu.decent.menus.utils;

import eu.decent.menus.DecentMenus;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Utility class for Bungee Messaging Channel.
 */
public class BungeeUtils {

    /**
     * Connect player to the given server.
     *
     * @param player The player.
     * @param server The servers name.
     */
    public static void connect(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
        player.sendPluginMessage(DecentMenus.getInstance(), "BungeeCord", b.toByteArray());
    }

}
