package eu.decent.menus.menu.item;

import eu.decent.menus.Config;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.menu.Menu;
import eu.decent.menus.menu.enums.EnumMenuItemType;
import eu.decent.menus.server.Server;
import eu.decent.menus.utils.config.Configuration;
import eu.decent.menus.utils.pinger.PingerResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ServerMenuItem extends NormalMenuItem {

    private String serverName;

    public ServerMenuItem(Configuration config, char identifier) {
        super(config, identifier, EnumMenuItemType.SERVER);
    }

    public ServerMenuItem(Configuration config, char identifier, EnumMenuItemType type) {
        super(config, identifier, type);
    }

    @Override
    public boolean canDisplay() {
        Server server = DecentMenus.getInstance().getServerRegistry().get(serverName);
        switch (displayType) {
            case ONLINE:
                return server.isOnline();
            case OFFLINE:
                return !server.isOnline();
        }
        return super.canDisplay();
    }

    @Override
    public String setPlaceholders(Player player, String string) {
        Server server = DecentMenus.getInstance().getServerRegistry().get(serverName);
        if (server == null) {
            return super.setPlaceholders(player, string);
        }

        PingerResponse data = server.getData();
        if (data == null) {
            return super.setPlaceholders(player, string);
        }

        return super.setPlaceholders(player, string
                .replace("{server_name}", server.getName())
                .replace("{server_motd}", data.getDescription())
                .replace("{server_players_online}", String.valueOf(data.getPlayers().getOnline()))
                .replace("{server_players_max}", String.valueOf(data.getPlayers().getMax()))
                .replace("{server_online}", server.isOnline() ? Config.ONLINE_MARKER : Config.OFFLINE_MARKER)
        );
    }

    @Override
    public void onClick(Menu menu, InventoryClickEvent event) {
        Server server = DecentMenus.getInstance().getServerRegistry().get(serverName);
        if (server != null) {
            server.connect((Player) event.getWhoClicked());
        }
    }

    @Override
    public void load() {
        super.load();
        this.serverName = config.getString("server");
    }
}
