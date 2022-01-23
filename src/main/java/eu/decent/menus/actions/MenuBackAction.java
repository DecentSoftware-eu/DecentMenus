package eu.decent.menus.actions;

import eu.decent.library.actions.IAction;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.menu.Menu;
import eu.decent.menus.player.PlayerProfile;
import org.bukkit.entity.Player;

public class MenuBackAction implements IAction {

    private static final DecentMenus PLUGIN = DecentMenus.getInstance();

    @Override
    public boolean execute(Player player) {
        PlayerProfile profile = PLUGIN.getPlayerRegistry().get(player.getUniqueId());
        if (profile == null || profile.getOpenMenu() == null) {
            return true;
        }
        Menu menu = profile.getOpenMenu();
        Menu previousMenu = menu.getPreviousMenu();
        if (previousMenu != null) {
            menu.close();
            previousMenu.open();
        }
        return true;
    }

}
