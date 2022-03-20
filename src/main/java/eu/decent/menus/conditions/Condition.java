package eu.decent.menus.conditions;

import eu.decent.menus.actions.ActionHolder;
import eu.decent.menus.conditions.impl.ComparingCondition;
import eu.decent.menus.conditions.impl.PermissionCondition;
import eu.decent.menus.conditions.impl.RegexCondition;
import eu.decent.menus.player.PlayerProfile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

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
     * Create a {@link Condition} from the given config.
     *
     * @param config The config.
     * @return The new Condition or null.
     */
    @Nullable
    public static Condition load(@NotNull ConfigurationSection config) {
        if (!config.isString("type")) {
            return null;
        }

        // -- Get type of the condition
        String typeName = config.getString("type", "");
        String typeNameLowerTrimmed = typeName.trim().toLowerCase();
        boolean inverted = typeNameLowerTrimmed.startsWith("!") || typeNameLowerTrimmed.startsWith("not");
        ConditionType type = ConditionType.fromString(typeName);
        if (type == null) {
            return null;
        }

        // -- Create the condition if possible
        Condition condition = null;
        switch (type) {
            case STRING_EQUAL:
            case STRING_EQUAL_IGNORECASE:
            case STRING_CONTAINS:
            case EQUAL:
            case GREATER_EQUAL:
            case LESS_EQUAL:
            case LESS:
            case GREATER:
                if (config.isString("compare") && config.isString("input")) {
                    String compare = config.getString("compare", "");
                    String input = config.getString("input", "");
                    condition = new ComparingCondition(inverted, type, compare, input);
                }
                break;
            case PERMISSION:
                if (config.isString("permission")) {
                    String permission = config.getString("permission", "");
                    condition = new PermissionCondition(inverted, permission);
                }
                break;
            case REGEX:
                if (config.isString("regex") && config.isString("input")) {
                    String regex = config.getString("regex", "");
                    String input = config.getString("input", "");
                    condition = new RegexCondition(inverted, Pattern.compile(regex), input);
                }
                break;
            // TODO finish
        }

        // -- Load actions if possible
        if (condition != null) {
            // Met actions
            ConfigurationSection metActionsSection = config.getConfigurationSection("met_actions");
            if (metActionsSection != null) {
                condition.setMetActions(ActionHolder.load(metActionsSection));
            }
            // Not Met actions
            ConfigurationSection notMetActionsSection = config.getConfigurationSection("not_met_actions");
            if (notMetActionsSection != null) {
                condition.setNotMetActions(ActionHolder.load(notMetActionsSection));
            }
        }
        return condition;
    }


}
