package eu.decent.menus.actions;

import eu.decent.library.actions.IAction;
import eu.decent.menus.utils.BungeeUtils;
import org.bukkit.entity.Player;

public class ConnectAction implements IAction {

    private final String server;

    public ConnectAction(String server) {
        this.server = server;
    }

    @Override
    public boolean execute(Player player) {
        BungeeUtils.connect(player, server);
        return true;
    }

}
