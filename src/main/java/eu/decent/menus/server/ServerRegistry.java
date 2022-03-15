package eu.decent.menus.server;

import eu.decent.menus.Config;
import eu.decent.menus.utils.collection.Registry;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * This class holds all pinged servers.
 *
 * @see Server
 */
public class ServerRegistry extends Registry<String, Server> {

    /**
     * Reload all servers.
     */
    @Override
    public void reload() {
        this.clear();

        if (Config.PINGER_ENABLED) {
            for (String serverString : Config.PINGER_SERVERS) {
                if (!serverString.matches("\\S+:(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3}):\\d{1,5}")) {
                    continue;
                }
                // -- Parse data
                String[] spl = serverString.split(":");
                String serverName = spl[0];
                String serverAddress = spl[1];
                int serverPort = Integer.parseInt(spl[2]);
                // -- Register the server
                Server server = new Server(serverName, new InetSocketAddress(serverAddress, serverPort));
                this.register(server);
            }
        }
    }

    /**
     * Register a new server.
     *
     * @param server The server.
     */
    public void register(@NotNull Server server) {
        register(server.getName(), server);
    }

}
