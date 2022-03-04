package eu.decent.menus.conditions.impl;

import eu.decent.menus.conditions.Condition;
import eu.decent.menus.player.PlayerProfile;
import org.jetbrains.annotations.NotNull;

public class ComparingCondition extends Condition {

    private final String compare;
    private final String input;

    public ComparingCondition(@NotNull String compare, @NotNull String input) {
        this(false, compare, input);
    }

    public ComparingCondition(boolean inverted, @NotNull String compare, @NotNull String input) {
        super(inverted);
        this.compare = compare;
        this.input = input;
    }

    @Override
    public boolean check(@NotNull PlayerProfile profile) {

        return false;
    }

}
