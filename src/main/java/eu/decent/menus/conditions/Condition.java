package eu.decent.menus.conditions;

import eu.decent.menus.actions.ActionHolder;
import eu.decent.menus.player.PlayerProfile;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a Condition. We can then
 * check whether the Condition is fulfilled.
 */
@Getter
@Setter
public abstract class Condition {

    protected boolean inverted;
    protected ActionHolder metActions;
    protected ActionHolder notMetActions;

    public Condition() {
        this(false);
    }

    public Condition(boolean inverted) {
        this.inverted = inverted;
    }

    /**
     * Check whether this {@link Condition} is fulfilled.
     *
     * @param profile Profile of the player for whom we want to check the condition.
     * @return True if the condition is fulfilled, False otherwise.
     */
    public abstract boolean check(@NotNull PlayerProfile profile);

}