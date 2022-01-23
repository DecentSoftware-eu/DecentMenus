package eu.decent.menus.server;

import eu.decent.menus.utils.BungeeUtils;
import eu.decent.menus.utils.DecentMenusTicked;
import eu.decent.menus.utils.pinger.Pinger;
import eu.decent.menus.utils.pinger.PingerResponse;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * This class represents a cached server.
 */
public class Server extends DecentMenusTicked {

    private final String name;
    private final Pinger pinger;
    private PingerResponse data;

    /**
     * Create new instance of {@link Server}.
     *
     * @param name Name of this server.
     * @param address The servers address.
     */
    public Server(String name, InetSocketAddress address) {
        super(20L);
        this.name = name;
        this.pinger = new Pinger(address);
        this.update();
    }

    /**
     * Fetch the data using pinger.
     */
    public void update() {
        try {
            data = pinger.fetchData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect given player to this server.
     *
     * @param player The player.
     */
    public void connect(Player player) {
        BungeeUtils.connect(player, name);
    }

    @Override
    public void tick() {
        update();
    }

    /**
     * Get the name of this server.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the pinger of this server.
     *
     * @return The pinger.
     */
    public Pinger getPinger() {
        return pinger;
    }

    /**
     * Get the data of this server.
     *
     * @return The data.
     */
    public PingerResponse getData() {
        return data;
    }

    /**
     * Check whether this server is online.
     * 
     * @return The result boolean.
     */
    public boolean isOnline() {
        return data != null && data.getPlayers().getMax() > 0;
    }

}
