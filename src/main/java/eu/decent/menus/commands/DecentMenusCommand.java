package eu.decent.menus.commands;

import eu.decent.library.command.CommandHandler;
import eu.decent.library.command.CommandInfo;
import eu.decent.library.command.DecentCommand;
import eu.decent.library.command.TabCompleteHandler;
import eu.decent.menus.Config;

@CommandInfo(
        minArgs = 1,
        playerOnly = true,
        permission = Config.ADMIN_PERM,
        usage = "/decentmenus <args>",
        description = "The main command of DecentMenus.",
        aliases = {"decentmenu", "dmenus", "dm"}
)
public class DecentMenusCommand extends DecentCommand {

    public DecentMenusCommand() {
        super("decentmenus", Config.MENU_USAGE);
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            if (sender.hasPermission(Config.ADMIN_PERM)) {
                if (args.length == 0) {
                    Config.send(sender, Config.USE_HELP);
                    return true;
                }
                Config.send(sender, Config.UNKNOWN_SUB_COMMAND);
                Config.send(sender, Config.USE_HELP);
            } else {
                Config.sendVersionMessage(sender);
            }
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return null;
    }

    // TODO list, open, ver

}
