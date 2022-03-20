package eu.decent.menus.actions.impl;

import eu.decent.menus.actions.Action;
import eu.decent.menus.actions.ActionType;
import eu.decent.menus.player.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundAction extends Action {

    private final ActionType type;
    private final Sound sound;
    private final float volume;
    private final float pitch;

    public SoundAction(@NotNull ActionType type, @NotNull Sound sound, float volume, float pitch) {
        this(0, -1, type, sound, volume, pitch);
    }

    public SoundAction(long delay, double chance, @NotNull ActionType type, @NotNull Sound sound, float volume, float pitch) {
        super(delay, chance);
        this.type = type;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void execute(@NotNull PlayerProfile profile) {
        Player player = profile.getPlayer();
        World world = player.getWorld();
        switch (type) {
            case SOUND:
                player.playSound(player.getLocation(), sound, volume, pitch);
                break;
            case BROADCAST_SOUND:
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), sound, volume, pitch);
                }
                break;
            case BROADCAST_SOUND_WORLD:
                for (Player worldPlayer : world.getPlayers()) {
                    worldPlayer.playSound(worldPlayer.getLocation(), sound, volume, pitch);
                }
                break;
            default: break;
        }
    }

}
