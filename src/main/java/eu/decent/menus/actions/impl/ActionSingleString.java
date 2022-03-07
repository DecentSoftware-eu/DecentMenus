package eu.decent.menus.actions.impl;

import eu.decent.library.hooks.PAPI;
import eu.decent.library.utils.Common;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.actions.Action;
import eu.decent.menus.actions.ActionType;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionSingleString extends Action {

    private final ActionType type;
    private final String data;

    public ActionSingleString(@NotNull ActionType type, @NotNull String data) {
        this(0, -1, type, data);
    }

    public ActionSingleString(long delay, double chance, @NotNull ActionType type, @NotNull String data) {
        super(delay, chance);
        this.type = type;
        this.data = data;
    }

    @Override
    public void execute(@NotNull PlayerProfile profile) {
        Player player = profile.getPlayer();
        String parsedData = PAPI.setPlaceholders(player, data);
        switch (type) {
            case MESSAGE:
                Common.tell(player, parsedData);
                break;
            case BROADCAST:
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Common.tell(onlinePlayer, PAPI.setPlaceholders(onlinePlayer, data));
                }
                break;
            case CHAT:
                player.chat(Common.colorize(parsedData));
                break;
            case COMMAND:
                Bukkit.dispatchCommand(player, parsedData);
                break;
            case CONSOLE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedData);
                break;
            case CONNECT:
                BungeeUtils.connect(player, parsedData);
                break;
            case OPEN_MENU:
                DecentMenus.getInstance().getMenuRegistry().openMenu(player, parsedData);
                break;
            default: break;
        }
    }

}
