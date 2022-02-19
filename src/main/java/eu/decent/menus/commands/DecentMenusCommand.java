package eu.decent.menus.commands;

import eu.decent.library.command.CommandHandler;
import eu.decent.library.command.CommandInfo;
import eu.decent.library.command.DecentCommand;
import eu.decent.library.command.TabCompleteHandler;
import eu.decent.menus.Config;
import eu.decent.menus.DecentMenus;

@CommandInfo(
        permission = Config.ADMIN_PERM,
        usage = "/decentmenus <args>",
        description = "The main command of DecentMenus.",
        aliases = {"decentmenu", "dmenus", "dm"}
)
public class DecentMenusCommand extends DecentCommand {

    public DecentMenusCommand() {
        super("decentmenus", Config.MENU_USAGE);

        addSubCommand(new ListSubCommand());
        addSubCommand(new VersionSubCommand());
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            if (sender.hasPermission(Config.ADMIN_PERM)) {
                if (args.length == 0) {
                    Config.send(sender, Config.MENU_USAGE);
                    return true;
                }
                Config.send(sender, Config.UNKNOWN_SUB_COMMAND);
                Config.send(sender, Config.MENU_USAGE);
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

    @CommandInfo(
            permission = Config.ADMIN_PERM,
            usage = "/decentmenus list",
            description = "Displays a list of all existing menus."
    )
    static class ListSubCommand extends DecentCommand {

        public ListSubCommand() {
            super("list", null);
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                String[] menus = DecentMenus.getInstance().getMenuRegistry().getKeys().toArray(new String[0]);
                Config.send(sender, Config.MENU_LIST, String.join(", ", menus));
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return null;
        }

    }

    @CommandInfo(
            permission = Config.ADMIN_PERM,
            usage = "/decentmenus version",
            description = "Displays a list of all existing menus.",
            aliases = {"ver"}
    )
    static class VersionSubCommand extends DecentCommand {

        public VersionSubCommand() {
            super("version", null);
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                Config.sendVersionMessage(sender);
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return null;
        }

    }

}
