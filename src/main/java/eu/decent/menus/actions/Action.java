package eu.decent.menus.actions;

import eu.decent.menus.player.PlayerProfile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents an action, that can be executed for a specific profile.
 */
@Getter
@Setter
public class Action {

    private final ActionType type;
    private String data;
    private long delay;
    private double chance;

    /**
     * Create new {@link Action}.
     *
     * @param type Type of the new action.
     * @param data Data, used by the new action.
     */
    public Action(@NotNull ActionType type, String data) {
        this.type = type;
        this.data = data;
        // Default values
        this.delay = 0;
        this.chance = -1d; // Negative means ignore
    }

    /**
     * Execute this action for the given profile.
     *
     * @param profile The profile.
     */
    public void execute(@NotNull PlayerProfile profile) {
        type.execute(profile, data);
    }

    /**
     * Check the chance of this action.
     *
     * @return Boolean whether this action should be executed.
     */
    public boolean checkChance() {
        if (chance < 0d || chance >= 100d) {
            return true;
        }
        double random = ThreadLocalRandom.current().nextDouble() * 100d;
        return random > chance;
    }

    /**
     * Create a {@link Action} from the given config.
     *
     * @param config The config.
     * @return The new Action or null.
     */
    @Nullable
    public static Action load(@NotNull ConfigurationSection config) {

        return null;
    }

}
