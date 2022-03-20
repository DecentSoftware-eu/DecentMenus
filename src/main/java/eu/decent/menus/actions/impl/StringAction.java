package eu.decent.menus.actions.impl;

import eu.decent.menus.DecentMenus;
import eu.decent.menus.actions.Action;
import eu.decent.menus.actions.ActionType;
import eu.decent.menus.hooks.PAPI;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.BungeeUtils;
import eu.decent.menus.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StringAction extends Action {

    private final ActionType type;
    private final String data;

    public StringAction(@NotNull ActionType type, @NotNull String data) {
        this(0, -1, type, data);
    }

    public StringAction(long delay, double chance, @NotNull ActionType type, @NotNull String data) {
        super(delay, chance);
        this.type = type;
        this.data = data;
    }

    @Override
    public void execute(@NotNull PlayerProfile profile) {
        Player player = profile.getPlayer();
        String processed = PAPI.setPlaceholders(player, data);
        switch (type) {
            case CHAT:
                player.chat(Common.colorize(processed));
                break;
            case COMMAND:
                Bukkit.dispatchCommand(player, processed);
                break;
            case CONSOLE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), processed);
                break;
            case CONNECT:
                BungeeUtils.connect(player, processed);
                break;
            case OPEN_MENU:
                DecentMenus.getInstance().getMenuRegistry().openMenu(player, processed);
                break;
            default: break;
        }
    }

}
