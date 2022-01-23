package eu.decent.menus.player;

import eu.decent.library.utils.collection.DMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * This class holds all cached player profiles.
 *
 * @see PlayerProfile
 */
public class PlayerRegistry {

    private final Map<UUID, PlayerProfile> playerMap;

    /**
     * Create new {@link PlayerRegistry}.
     */
    public PlayerRegistry() {
        this.playerMap = new DMap<>();

        // Create profiles for all online players (to make it reload-proof)
        for (Player player : Bukkit.getOnlinePlayers()) {
            register(player.getUniqueId());
        }
    }

    /**
     * Shutdown this registry.
     */
    public void shutdown() {
        playerMap.clear();
    }

    /**
     * Register a new player profile.
     *
     * @param uid The players UUID.
     */
    public void register(UUID uid) {
        playerMap.put(uid, new PlayerProfile(uid));
    }

    /**
     * Get a players profile.
     *
     * @param uid The players UUID.
     * @return The profile.
     */
    public PlayerProfile get(UUID uid) {
        return playerMap.get(uid);
    }

    /**
     * Remove a players profile.
     *
     * @param uid The players UUID.
     * @return The profile.
     */
    public PlayerProfile remove(UUID uid) {
        return playerMap.remove(uid);
    }

    /**
     * Check whether this registry contains a profile of player with the given UUID.
     *
     * @param uid The player UUID.
     * @return True if this registry contains the profile.
     */
    public boolean contains(UUID uid) {
        return playerMap.containsKey(uid);
    }

}
