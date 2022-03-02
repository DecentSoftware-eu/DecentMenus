package eu.decent.menus.actions.impl;

import eu.decent.menus.actions.Action;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.BungeeUtils;
import org.jetbrains.annotations.NotNull;

public class ConnectAction extends Action {

    private final String server;

    public ConnectAction(String server) {
        this.server = server;
    }

    @Override
    public void execute(@NotNull PlayerProfile profile) {
        BungeeUtils.connect(profile.getPlayer(), server);
    }

}
