package eu.decent.menus.server;

import eu.decent.menus.utils.collection.Registry;

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
    public void reload() {}

    /**
     * Register a new server.
     *
     * @param server The server.
     */
    public void register(Server server) {
        register(server.getName(), server);
    }

}
