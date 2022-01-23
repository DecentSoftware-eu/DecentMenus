package eu.decent.menus.commands;

import eu.decent.library.command.CommandHandler;
import eu.decent.library.command.CommandInfo;
import eu.decent.library.command.DecentCommand;
import eu.decent.library.command.TabCompleteHandler;
import eu.decent.menus.Config;
import eu.decent.menus.DecentMenus;
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
                    Config.send(sender, Config.MENU_NO_PERM, args[0]);
                }
                return true;
            }
            Config.send(sender, Config.MENU_DOES_NOT_EXIST, args[0]);
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
            List<String> list = new ArrayList<>(menuRegistry.getMenuNames());
            Collections.sort(list);
            return list;
        };
    }
}
