package eu.decent.menus.commands;

import eu.decent.menus.Config;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.api.commands.CommandHandler;
import eu.decent.menus.api.commands.CommandInfo;
import eu.decent.menus.api.commands.DecentCommand;
import eu.decent.menus.api.commands.TabCompleteHandler;

@CommandInfo(
        permission = Config.ADMIN_PERM,
        usage = "/decentmenus <args>",
        description = "The main command of DecentMenus.",
        aliases = {"decentmenu", "dmenus", "dmenu", "dm"}
)
public class DecentMenusCommand extends DecentCommand {

    public DecentMenusCommand() {
        super("decentmenus", Config.MENU_USAGE);

        addSubCommand(new ReloadSubCommand());
        addSubCommand(new ListSubCommand());
        addSubCommand(new VersionSubCommand());
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            if (sender.hasPermission(Config.ADMIN_PERM)) {
                if (args.length == 0) {
                    Config.tell(sender, Config.MENU_USAGE);
                    return true;
                }
                Config.tell(sender, Config.UNKNOWN_SUB_COMMAND);
                Config.tell(sender, Config.MENU_USAGE);
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
            usage = "/decentmenus reload",
            description = "Reloads the plugin."
    )
    static class ReloadSubCommand extends DecentCommand {

        public ReloadSubCommand() {
            super("reload", null);
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                long startTime = System.currentTimeMillis();
                DecentMenus.getInstance().reload();
                long took = System.currentTimeMillis() - startTime;
                Config.tell(sender, Config.MENU_RELOADED.replace("{ms}", String.valueOf(took)));
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
                Config.tell(sender, Config.MENU_LIST, String.join(", ", menus));
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
