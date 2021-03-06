package eu.decent.menus.player;

import eu.decent.menus.menu.Menu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * This class represents a players profile
 * and stores some session data about the player.
 */
@Getter
@Setter
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

}
