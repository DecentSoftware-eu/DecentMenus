package eu.decent.menus.conditions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This enum represents all possible Condition types.
 *
 * TODO:
 *  - ADD: Time, World, Weather, Hand Item, Health, Hunger, Durability
 *  - maybe a little structure rework for condition
 */
@SuppressWarnings("SpellCheckingInspection")
public enum ConditionType {
    MONEY("money", "hasmoney"),
    PERMISSION("permission", "haspermission", "perm", "hasperm"),
    REGEX("regex", "regex_matches", "matchesregex", "compareregex"),
    DISTANCE("distance", "near", "isnear"),
    EXP("exp", "hasexp", "xp", "hasxp"),
    ITEM("item", "hasitem"),
    JAVASCRIPT("javascript", "js"),

    // -- Numbers
    EQUAL("==", "equals", "equal", "equalto", "isequal", "isequalto"),
    LESS("<", "less", "isless", "lessthan", "islessthan"),
    LESS_EQUAL("<=", "lessorequal", "islessorequal", "lessthanorequal", "islessthanorequal"),
    GREATER(">", "greater", "isgreater", "greaterthan", "isgreaterthan"),
    GREATER_EQUAL(">=", "greaterorequal", "isgreaterorequal", "greaterthanorequal", "isgreaterthanorequal"),

    // -- Strings
    STRING_EQUAL("stringequals"),
    STRING_EQUAL_IGNORECASE("stringequalsignorecase"),
    STRING_CONTAINS("stringcontains"),
    ;

    @Getter
    private final Set<String> aliases;

    ConditionType(String... aliases) {
        this.aliases = new HashSet<>();
        if (aliases != null) {
            this.aliases.addAll(Arrays.asList(aliases));
        }
    }

    /**
     * Find a {@link ConditionType} by the given string.
     *
     * @param string The string.
     * @return The ConditionType or null if the string doesn't match any.
     */
    @Nullable
    public static ConditionType fromString(@NotNull String string) {
        for (ConditionType conditionType : values()) {
            if (conditionType.getAliases().contains(string.trim().toLowerCase())) {
                return conditionType;
            }
        }
        return null;
    }

}
