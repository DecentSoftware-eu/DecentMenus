package eu.decent.menus.actions;

import eu.decent.menus.player.PlayerProfile;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an action, that can be executed for a specific profile.
 */
@Getter
@Setter
public class Action {

    private final ActionType type;
    private String data;
    private long delay;

    /**
     * Create new {@link Action}.
     *
     * @param type Type of the new action.
     * @param data Data, used by the new action.
     */
    public Action(ActionType type, String data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Execute this action for the given profile.
     *
     * @param profile The profile.
     */
    public void execute(@NotNull PlayerProfile profile) {
        type.execute(profile, data);
    }

}
