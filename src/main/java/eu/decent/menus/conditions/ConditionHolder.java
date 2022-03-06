package eu.decent.menus.conditions;

import eu.decent.library.objects.DecentHolder;
import eu.decent.menus.actions.ActionHolder;
import eu.decent.menus.menu.MenuIntent;
import eu.decent.menus.menu.item.MenuItemIntent;
import eu.decent.menus.player.PlayerProfile;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a holder for conditions.
 */
public class ConditionHolder extends DecentHolder<Condition> {

    /**
     * Checks all Conditions stored in this holder. This method also executes
     * all 'met' or 'not met' actions of the checked conditions.
     *
     * @param profile Profile of the player for whom we want to check the conditions.
     * @return True if all the conditions are fulfilled, False otherwise.
     */
    public boolean checkAll(@NotNull PlayerProfile profile) {
        boolean success = true;
        for (Condition condition : asList()) {
            // Check and flip if inverted.
            boolean fulfilled = condition.isInverted() != condition.check(profile);
            ActionHolder actions;
            if (!fulfilled) {
                // Not met
                if ((actions = condition.getNotMetActions()) != null && actions.asList().isNotEmpty()) {
                    // Execute 'not met' actions if any.
                    actions.execute(profile);
                }
                if (condition.isRequired()) {
                    // Not all required conditions are fulfilled.
                    success = false;
                }
            } else if ((actions = condition.getMetActions()) != null && actions.asList().isNotEmpty()) {
                // Execute 'met' actions if any.
                actions.execute(profile);
            }
        }
        // All conditions are fulfilled.
        return success;
    }

    @Nullable
    public static ConditionHolder load(@NotNull ConfigurationSection config, boolean forMenuItem) {
        // TODO
        return null;
    }

}