package eu.decent.menus.conditions.impl;

import eu.decent.menus.conditions.Condition;
import eu.decent.menus.player.PlayerProfile;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexCondition extends Condition {

    private final Pattern pattern;
    private final String input;

    public RegexCondition(@NotNull Pattern pattern, @NotNull String input) {
        this(false, pattern, input);
    }

    public RegexCondition(boolean inverted, @NotNull Pattern pattern, @NotNull String input) {
        super(inverted);
        this.pattern = pattern;
        this.input = input;
    }

    @Override
    public boolean check(@NotNull PlayerProfile profile) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

}
