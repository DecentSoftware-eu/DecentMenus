package eu.decent.menus.actions.impl;

import eu.decent.menus.actions.Action;
import eu.decent.menus.menu.Menu;
import eu.decent.menus.player.PlayerProfile;
import org.jetbrains.annotations.NotNull;

public class MenuBackAction extends Action {

    @Override
    public void execute(@NotNull PlayerProfile profile) {
        Menu menu = profile.getOpenMenu();
        if (menu == null) {
            return;
        }
        Menu previousMenu = menu.getPreviousMenu();
        if (previousMenu == null) {
            return;
        }
        menu.close();
        previousMenu.open();
    }

}
