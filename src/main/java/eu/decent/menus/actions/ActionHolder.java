package eu.decent.menus.actions;

import eu.decent.library.objects.DecentHolder;
import eu.decent.menus.player.PlayerProfile;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a holder for actions.
 */
public class ActionHolder extends DecentHolder<Action> {

    /**
     * Execute all Actions in this holder for the given {@link PlayerProfile}.
     *
     * @param profile The profile.
     */
    public void execute(@NotNull PlayerProfile profile) {
        for (Action action : asList()) {
            action.execute(profile);
        }
    }

}
