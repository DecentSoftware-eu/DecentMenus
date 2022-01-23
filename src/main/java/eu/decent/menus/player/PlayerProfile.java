package eu.decent.menus.player;

import eu.decent.menus.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * This class represents a players profile
 * and stores some session data about the player.
 */
public class PlayerProfile {

    private final UUID uid;
    private Menu openMenu;

    /**
     * Create new instance of {@link PlayerProfile}.
     *
     * @param uid The players UUID.
     */
    public PlayerProfile(UUID uid) {
        this.uid = uid;
    }

    /**
     * The bukkit instance of the owning player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uid);
    }

    /**
     * Get the players UUID.
     *
     * @return The UUID.
     */
    public UUID getUniqueId() {
        return uid;
    }

    /**
     * Get the current open menu.
     *
     * @return The menu.
     */
    @Nullable
    public Menu getOpenMenu() {
        return openMenu;
    }

    /**
     * Set the current open menu.
     *
     * @param openMenu The menu.
     */
    public void setOpenMenu(Menu openMenu) {
        this.openMenu = openMenu;
    }

}
