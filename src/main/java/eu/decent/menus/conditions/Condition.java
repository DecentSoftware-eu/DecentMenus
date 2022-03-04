package eu.decent.menus.conditions;

import eu.decent.menus.actions.ActionHolder;
import eu.decent.menus.player.PlayerProfile;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a Condition. We can then
 * check whether the Condition is fulfilled.
 */
@Getter
@Setter
public abstract class Condition {

    protected boolean inverted;
    protected boolean required;
    protected ActionHolder metActions;
    protected ActionHolder notMetActions;

    public Condition() {
        this(false);
    }

    public Condition(boolean inverted) {
        this.inverted = inverted;
        this.required = true;
    }

    /**
     * Check whether this {@link Condition} is fulfilled.
     *
     * @param profile Profile of the player for whom we want to check the condition.
     * @return True if the condition is fulfilled, False otherwise.
     */
    public abstract boolean check(@NotNull PlayerProfile profile);

    /**
     * Create a {@link Condition} from the given string.
     *
     * @param string The string.
     * @return The condition or null if the string is not valid.
     */
    @Nullable
    public static Condition fromString(@NotNull String string) {

        return null;
    }

}
