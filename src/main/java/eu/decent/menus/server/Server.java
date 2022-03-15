package eu.decent.menus.server;

import eu.decent.menus.utils.BungeeUtils;
import eu.decent.menus.utils.pinger.Pinger;
import eu.decent.menus.utils.pinger.PingerResponse;
import eu.decent.menus.utils.ticker.Ticked;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents a cached server.
 */
@Getter
public class Server extends Ticked {

    private final String name;
    private final Pinger pinger;
    private final AtomicBoolean online;
    private PingerResponse data;

    /**
     * Create new instance of {@link Server}.
     *
     * @param name Name of this server.
     * @param address The servers address.
     */
    public Server(@NotNull String name, @NotNull InetSocketAddress address) {
        super(20L);
        this.name = name;
        this.pinger = new Pinger(address);
        this.online = new AtomicBoolean(false);
        this.startTicking();
    }

    @Override
    public void tick() {
        this.update();
    }

    /**
     * Fetch the data using pinger.
     */
    public void update() {
        try {
            data = pinger.fetchData();
            online.set(true);
        } catch (IOException e) {
            online.set(false);
            e.printStackTrace();
        }
    }

    /**
     * Connect given player to this server.
     *
     * @param player The player.
     */
    public void connect(@NotNull Player player) {
        BungeeUtils.connect(player, name);
    }

    /**
     * Check whether this server is online.
     * 
     * @return The result boolean.
     */
    public boolean isOnline() {
        return data != null && online.get();
    }

}
