package eu.decent.menus.conditions;

import eu.decent.library.objects.DecentHolder;
import eu.decent.menus.actions.ActionHolder;
import eu.decent.menus.player.PlayerProfile;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a holder for conditions.
 */
public class ConditionHolder extends DecentHolder<Condition> {

    /**
     * Checks all Conditions stored in this holder. This method also executes
     * all 'met' or 'not met' actions of the checked conditions if needed.
     *
     * @param profile Profile of the player for whom we want to check the conditions.
     * @return True if all the conditions are fulfilled, False otherwise.
     */
    public boolean checkAll(@NotNull PlayerProfile profile) {
        for (Condition condition : asList()) {
            // Check and flip if inverted.
            boolean fulfilled = condition.isInverted() != condition.check(profile);
            ActionHolder actions;
            if (!fulfilled) {
                // Not met
                if ((actions = condition.getNotMetActions()) != null) {
                    // Execute 'not met' actions if any.
                    actions.execute(profile);
                }
                // Return false; Not all conditions are fulfilled.
                return false;
            } else if ((actions = condition.getMetActions()) != null) {
                // Execute 'met' actions if any.
                actions.execute(profile);
            }
        }
        // All conditions are fulfilled.
        return true;
    }

}