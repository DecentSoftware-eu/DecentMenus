package eu.decent.menus.conditions.impl;

import eu.decent.menus.conditions.Condition;
import eu.decent.menus.player.PlayerProfile;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link Condition} that checks
 * whether the player has a specified permission.
 */
public class PermissionCondition extends Condition {

    private final String permission;

    public PermissionCondition(@NotNull String permission) {
        this(false, permission);
    }

    public PermissionCondition(boolean inverted, @NotNull String permission) {
        super(inverted);
        this.permission = permission;
    }

    @Override
    public boolean check(@NotNull PlayerProfile profile) {
        return profile.getPlayer().hasPermission(permission);
    }

}
