package eu.decent.menus.server;

import eu.decent.menus.Config;
import eu.decent.menus.utils.Common;
import eu.decent.menus.utils.collection.Registry;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * This class holds all pinged servers.
 *
 * @see Server
 */
public class ServerRegistry extends Registry<String, Server> {

    public ServerRegistry() {
        this.reload();
    }

    /**
     * Reload all servers.
     */
    @Override
    public void reload() {
        this.clear();

        if (Config.PINGER_ENABLED && !Config.PINGER_SERVERS.isEmpty()) {
            long startMillis = System.currentTimeMillis();
            Common.log("Loading server(s)...");
            int counter = 0;
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
                InetSocketAddress inetSocketAddress = new InetSocketAddress(serverAddress, serverPort);
                Server server = new Server(serverName, inetSocketAddress);
                this.register(server);
                counter++;
            }
            long took = System.currentTimeMillis() - startMillis;
            Common.log("Successfully loaded %d server(s) in %d ms!", counter, took);
        }
    }

    @Override
    public void clear() {
        // -- Stop the existing servers from ticking
        for (Server server : getValues()) {
            server.stopTicking();
        }
        super.clear();
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
