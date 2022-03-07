package eu.decent.menus.actions;

import eu.decent.menus.player.PlayerProfile;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents an action, that can be executed for a specific profile.
 */
@Getter
public abstract class Action {

    protected final long delay;
    protected final double chance;

    /**
     * Create new {@link Action}.
     */
    public Action() {
        this(0, -1);
    }

    /**
     * Create new {@link Action}.
     *
     * @param delay Delay of this action.
     * @param chance Chance of this action.
     */
    public Action(long delay, double chance) {
        this.delay = delay;
        this.chance = chance;
    }

    /**
     * Execute this action for the given profile.
     *
     * @param profile The profile.
     */
    public abstract void execute(@NotNull PlayerProfile profile);

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
