package eu.decent.menus.actions;

import eu.decent.library.objects.DecentHolder;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.player.PlayerProfile;
import org.bukkit.Bukkit;
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
            // Check the chance
            if (action.getChance() >= 0 && !action.checkChance()) {
                continue;
            }
            // Execute with delay if needed
            long delay = action.getDelay();
            if (delay > 0) {
                Bukkit.getScheduler().runTaskLater(DecentMenus.getInstance(), () -> action.execute(profile), delay);
            } else {
                action.execute(profile);
            }
        }
    }

}
