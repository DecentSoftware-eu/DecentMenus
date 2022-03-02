package eu.decent.menus.actions.impl;

import eu.decent.menus.DecentMenus;
import eu.decent.menus.actions.Action;
import eu.decent.menus.player.PlayerProfile;
import org.jetbrains.annotations.NotNull;

public class OpenMenuAction extends Action {

    private final String menu;

    public OpenMenuAction(String menu) {
        this.menu = menu;
    }

    @Override
    public void execute(@NotNull PlayerProfile profile) {
        DecentMenus.getInstance().getMenuRegistry().openMenu(profile.getPlayer(), menu);
    }

}
