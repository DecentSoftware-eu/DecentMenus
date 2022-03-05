package eu.decent.menus.conditions.impl;

import eu.decent.menus.conditions.Condition;
import eu.decent.menus.player.PlayerProfile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ExpCondition extends Condition {

    private final int minLevel;

    public ExpCondition(int minLevel) {
        this(false, minLevel);
    }

    public ExpCondition(boolean inverted, int minLevel) {
        super(inverted);
        this.minLevel = minLevel;
    }

    @Override
    public boolean check(@NotNull PlayerProfile profile) {
        Player player = profile.getPlayer();
        return player.getLevel() >= minLevel;
    }

}
