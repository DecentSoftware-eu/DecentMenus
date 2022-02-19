package eu.decent.menus.player;

import eu.decent.menus.utils.Registry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * This class holds all cached player profiles.
 *
 * @see PlayerProfile
 */
public class PlayerRegistry extends Registry<UUID, PlayerProfile> {

    /**
     * Create new {@link PlayerRegistry}.
     */
    public PlayerRegistry() {
        this.reload();
    }

    @Override
    public void reload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            register(player.getUniqueId());
        }
    }

    /**
     * Register a new player profile.
     *
     * @param uid The players UUID.
     */
    public void register(UUID uid) {
        register(uid, new PlayerProfile(uid));
    }

}
