package eu.decent.menus.server;

import eu.decent.menus.utils.collection.Registry;
import org.jetbrains.annotations.NotNull;

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
