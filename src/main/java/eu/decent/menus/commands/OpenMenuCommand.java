package eu.decent.menus.commands;

import eu.decent.menus.Config;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.api.commands.CommandHandler;
import eu.decent.menus.api.commands.CommandInfo;
import eu.decent.menus.api.commands.DecentCommand;
import eu.decent.menus.api.commands.TabCompleteHandler;
import eu.decent.menus.menu.MenuRegistry;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CommandInfo(
        minArgs = 1,
        playerOnly = true,
        permission = "",
        usage = "/openmenu <menu>",
        description = "Open a menu from DecentMenus.",
        aliases = {"open", "menu"}
)
public class OpenMenuCommand extends DecentCommand {

    public OpenMenuCommand() {
        super("openmenu", Config.MENU_OPEN_USAGE);
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            MenuRegistry menuRegistry = DecentMenus.getInstance().getMenuRegistry();
            if (menuRegistry.contains(args[0])) {
                if (!menuRegistry.openMenu((Player) sender, args[0])) {
                    Config.tell(sender, Config.MENU_NO_PERM, args[0]);
                }
                return true;
            }
            Config.tell(sender, Config.MENU_DOES_NOT_EXIST, args[0]);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (!sender.hasPermission("decentmenus.tab_complete")) {
                return new ArrayList<>();
            }
            MenuRegistry menuRegistry = DecentMenus.getInstance().getMenuRegistry();
            List<String> list = new ArrayList<>(menuRegistry.getKeys());
            Collections.sort(list);
            return list;
        };
    }
}
