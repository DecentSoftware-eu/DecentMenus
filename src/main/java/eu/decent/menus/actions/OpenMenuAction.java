package eu.decent.menus.actions;

import eu.decent.library.actions.IAction;
import eu.decent.menus.DecentMenus;
import org.bukkit.entity.Player;

public class OpenMenuAction implements IAction {

    private final String menu;

    public OpenMenuAction(String menu) {
        this.menu = menu;
    }

    @Override
    public boolean execute(Player player) {
        DecentMenus.getInstance().getMenuRegistry().openMenu(player, menu);
        return true;
    }

}
