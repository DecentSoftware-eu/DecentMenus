package eu.decent.menus.server;

import eu.decent.library.utils.collection.DMap;

import java.util.Map;

/**
 * This class holds all pinged servers.
 *
 * @see Server
 */
public class ServerRegistry {

    private final Map<String, Server> serverMap;

    /**
     * Create new {@link ServerRegistry}.
     */
    public ServerRegistry() {
        this.serverMap = new DMap<>();
    }

    /**
     * Reload all servers.
     */
    public void reload() {

    }

    /**
     * Shutdown this registry.
     */
    public void shutdown() {
        serverMap.clear();
    }

    /**
     * Register a new server.
     *
     * @param server The server.
     */
    public void register(Server server) {
        serverMap.put(server.getName(), server);
    }

    /**
     * Get a server by name.
     *
     * @param name The name.
     * @return The server.
     */
    public Server get(String name) {
        return serverMap.get(name);
    }

    /**
     * Remove a server by name.
     *
     * @param name The name.
     * @return The server.
     */
    public Server remove(String name) {
        return serverMap.remove(name);
    }

    /**
     * Check whether this registry contains a server with the given name.
     *
     * @param name The name.
     * @return True if this registry contains the server.
     */
    public boolean contains(String name) {
        return serverMap.containsKey(name);
    }

}
