package eu.decent.menus.actions.impl;

import eu.decent.menus.DecentMenus;
import eu.decent.menus.actions.Action;
import eu.decent.menus.actions.ActionType;
import eu.decent.menus.hooks.PAPI;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StringListAction extends Action {

    private final ActionType type;
    private final List<String> stringList;

    public StringListAction(ActionType type, List<String> stringList) {
        this.type = type;
        this.stringList = stringList;
    }

    public StringListAction(long delay, double chance, ActionType type, List<String> stringList) {
        super(delay, chance);
        this.type = type;
        this.stringList = stringList;
    }

    @Override
    public void execute(@NotNull PlayerProfile profile) {
        switch (type) {
            case MESSAGE:
                Player player = profile.getPlayer();
                for (String line : stringList) {
                    line = DecentMenus.getInstance().getPlaceholderRegistry().replacePlaceholders(player, line);
                    line = PAPI.setPlaceholders(player, line);
                    Common.tell(player, line);
                }
                break;
            case BROADCAST:
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    for (String line : stringList) {
                        line = DecentMenus.getInstance().getPlaceholderRegistry().replacePlaceholders(onlinePlayer, line);
                        line = PAPI.setPlaceholders(onlinePlayer, line);
                        Common.tell(onlinePlayer, line);
                    }
                }
                break;
            default:
                break;
        }
    }

}
