package eu.decent.menus.menu.item;

import com.google.common.collect.Sets;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.menu.enums.EnumMenuItemType;
import eu.decent.menus.server.Server;
import eu.decent.menus.utils.config.Configuration;
import eu.decent.menus.utils.pinger.PingerResponse;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class ServersMenuItem extends NormalMenuItem {

    private Set<String> serverNames;

    public ServersMenuItem(Configuration config, char identifier) {
        super(config, identifier, EnumMenuItemType.SERVERS);
    }

    @Override
    public String setPlaceholders(Player player, String string) {
        int onlinePlayers = 0;
        int maxPlayers = 0;
        int onlineServers = 0;
        int offlineServers = 0;
        int totalServers = 0;

        for (String serverName : serverNames) {
            Server server = DecentMenus.getInstance().getServerRegistry().get(serverName);
            if (server == null) {
                continue;
            }

            PingerResponse data = server.getData();
            onlinePlayers += data.getPlayers().getOnline();
            maxPlayers += data.getPlayers().getMax();
            onlineServers += server.isOnline() ? 1 : 0;
            offlineServers += server.isOnline() ? 0 : 1;
            totalServers++;
        }

        return super.setPlaceholders(player, string
                .replace("{servers_players_online}", String.valueOf(onlinePlayers))
                .replace("{servers_players_max}"   , String.valueOf(maxPlayers))
                .replace("{servers_count_online}"  , String.valueOf(onlineServers))
                .replace("{servers_count_offline}" , String.valueOf(offlineServers))
                .replace("{servers_count_total}"   , String.valueOf(totalServers))
        );
    }

    @Override
    public void load() {
        super.load();
        List<String> servers = config.getStringList("items." + identifier + ".servers");
        serverNames = Sets.newHashSet(servers);
    }
}
