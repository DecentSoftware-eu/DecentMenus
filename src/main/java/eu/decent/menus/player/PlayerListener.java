package eu.decent.menus.player;

import eu.decent.menus.DecentMenus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This class handles player related events.
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        DecentMenus.getInstance().getPlayerRegistry().register(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        DecentMenus.getInstance().getPlayerRegistry().remove(player.getUniqueId());
    }

}
