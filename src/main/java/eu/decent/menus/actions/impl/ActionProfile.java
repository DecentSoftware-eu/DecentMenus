package eu.decent.menus.actions.impl;

import eu.decent.menus.actions.Action;
import eu.decent.menus.actions.ActionType;
import eu.decent.menus.menu.Menu;
import eu.decent.menus.player.PlayerProfile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionProfile extends Action {

    private final ActionType type;

    public ActionProfile(@NotNull ActionType type) {
        this.type = type;
    }

    public ActionProfile(long delay, double chance, @NotNull ActionType type) {
        super(delay, chance);
        this.type = type;
    }

    @Override
    public void execute(@NotNull PlayerProfile profile) {
        Player player = profile.getPlayer();
        Menu menu = profile.getOpenMenu();
        switch (type) {
            case CLOSE_MENU:
                if (menu != null) {
                    menu.close();
                }
                break;
            case REFRESH_MENU:
                if (menu != null) {
                    menu.open();
                }
                break;
            case PREVIOUS_MENU:
                if (menu != null) {
                    Menu previousMenu = menu.getPreviousMenu();
                    if (previousMenu == null) {
                        break;
                    }
                    menu.close();
                    previousMenu.open();
                }
                break;
            default: break;
        }
    }

}
