package eu.decent.menus.conditions.impl;

import eu.decent.menus.conditions.Condition;
import eu.decent.menus.player.PlayerProfile;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DistanceCondition extends Condition {

    private final Location location;
    private final double maxDistanceSquared;

    public DistanceCondition(@NotNull Location location, double maxDistance) {
        this(false, location, maxDistance);
    }

    public DistanceCondition(boolean inverted, @NotNull Location location, double maxDistance) {
        super(inverted);
        this.location = location;
        this.maxDistanceSquared = maxDistance * maxDistance;
    }

    @Override
    public boolean check(@NotNull PlayerProfile profile) {
        Player player = profile.getPlayer();
        Location playerLocation = player.getLocation();
        World playerWorld = playerLocation.getWorld();
        World world = location.getWorld();
        return playerWorld != null && world != null &&
                playerWorld.getName().equals(world.getName()) &&
                playerLocation.distanceSquared(location) < maxDistanceSquared;
    }

}
