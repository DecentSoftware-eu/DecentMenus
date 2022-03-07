package eu.decent.menus.actions;

import eu.decent.menus.actions.impl.ActionProfile;
import eu.decent.menus.actions.impl.ActionSingleString;
import eu.decent.menus.actions.impl.ActionSound;
import eu.decent.menus.actions.impl.ActionTeleport;
import eu.decent.menus.conditions.Condition;
import eu.decent.menus.conditions.ConditionType;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.config.ConfigUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents an action, that can be executed for a specific profile.
 */
@Getter
public abstract class Action {

    protected final long delay;
    protected final double chance;

    /**
     * Create new {@link Action}.
     */
    public Action() {
        this(0, -1);
    }

    /**
     * Create new {@link Action}.
     *
     * @param delay Delay of this action.
     * @param chance Chance of this action.
     */
    public Action(long delay, double chance) {
        this.delay = delay;
        this.chance = chance;
    }

    /**
     * Execute this action for the given profile.
     *
     * @param profile The profile.
     */
    public abstract void execute(@NotNull PlayerProfile profile);

    /**
     * Check the chance of this action.
     *
     * @return Boolean whether this action should be executed.
     */
    public boolean checkChance() {
        if (chance < 0d || chance >= 100d) {
            return true;
        }
        double random = ThreadLocalRandom.current().nextDouble() * 100d;
        return random > chance;
    }

    /**
     * Create a {@link Action} from the given config.
     *
     * @param config The config.
     * @return The new Action or null.
     */
    @Nullable
    public static Action load(@NotNull ConfigurationSection config) {
        if (!config.isString("type")) {
            return null;
        }

        // -- Get type of the action
        String typeName = config.getString("type", "");
        ActionType type = ActionType.fromString(typeName);
        if (type == null) {
            return null;
        }

        // -- Create the action if possible
        Action action = null;
        switch (type) {
            case SOUND:
            case BROADCAST_SOUND:
            case BROADCAST_SOUND_WORLD:
                String soundName = config.getString("sound");
                if (soundName != null) {
                    float volume = (float) config.getDouble("volume", 1.0f);
                    float pitch = (float) config.getDouble("pitch", 1.0f);
                    try {
                        Sound sound = Sound.valueOf(soundName.toUpperCase());
                        action = new ActionSound(type, sound, volume, pitch);
                    } catch (Exception ignored) {}
                }
                break;
            case MESSAGE:
            case BROADCAST:
            case CHAT:
                String message = config.getString("message");
                if (message != null) {
                    action = new ActionSingleString(type, message);
                }
                break;
            case COMMAND:
            case CONSOLE:
                String command = config.getString("command");
                if (command != null) {
                    action = new ActionSingleString(type, command);
                }
                break;
            case CONNECT:
                String server = config.getString("server");
                if (server != null) {
                    action = new ActionSingleString(type, server);
                }
                break;
            case OPEN_MENU:
                String menu = config.getString("menu");
                if (menu != null) {
                    action = new ActionSingleString(type, menu);
                }
                break;
            case CLOSE_MENU:
            case REFRESH_MENU:
            case PREVIOUS_MENU:
                action = new ActionProfile(type);
                break;
            case TELEPORT:
                Location location = ConfigUtils.getLocation(config, "");
                if (location != null) {
                    action = new ActionTeleport(location);
                }
                break;
            default: break;
        }
        return action;
    }

}
