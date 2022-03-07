package eu.decent.menus.actions.impl;

import eu.decent.menus.actions.Action;
import eu.decent.menus.player.PlayerProfile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionTeleport extends Action {

    private final Location location;

    public ActionTeleport(@NotNull Location location) {
        this.location = location;
    }

    public ActionTeleport(long delay, double chance, @NotNull Location location) {
        super(delay, chance);
        this.location = location;
    }

    @Override
    public void execute(@NotNull PlayerProfile profile) {
        Player player = profile.getPlayer();
        player.teleport(location);
    }

}
