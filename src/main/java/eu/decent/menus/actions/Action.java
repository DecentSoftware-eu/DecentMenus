package eu.decent.menus.actions;

import eu.decent.menus.player.PlayerProfile;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an action, that can be executed for a specific profile.
 */
public abstract class Action {

    /**
     * Execute this {@link Action} for the given {@link PlayerProfile}.
     *
     * @param profile The profile.
     */
    public abstract void execute(@NotNull PlayerProfile profile);

}
