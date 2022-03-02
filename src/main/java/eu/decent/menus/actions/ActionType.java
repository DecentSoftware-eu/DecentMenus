package eu.decent.menus.actions;

import eu.decent.library.hooks.PAPI;
import eu.decent.library.utils.Common;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.menu.Menu;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.BungeeUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum ActionType {

    // -- Message Actions

    /**
     * Send the given message to the player.
     */
    MESSAGE() {
        @Override
        public void execute(@NotNull PlayerProfile profile, String data) {
            if (data != null) {
                Player player = profile.getPlayer();
                data = PAPI.setPlaceholders(player, data);
                Common.tell(player, data);
            }
        }
    },
    /**
     * Broadcast the given message across the server.
     */
    BROADCAST("broadcast_message") {
        @Override
        public void execute(@NotNull PlayerProfile profile, String data) {
            if (data != null) {
                Player player = profile.getPlayer();
                data = PAPI.setPlaceholders(player, data);
                Bukkit.broadcastMessage(Common.colorize(data));
            }
        }
    },

    // -- Command Actions

    /**
     * Execute the given command as the player.
     */
    COMMAND("player") {
        @Override
        public void execute(@NotNull PlayerProfile profile, String data) {
            if (data != null && !data.isEmpty()) {
                Player player = profile.getPlayer();
                Bukkit.dispatchCommand(profile.getPlayer(), PAPI.setPlaceholders(player, data));
            }
        }
    },
    /**
     * Execute the given command as console.
     */
    CONSOLE() {
        @Override
        public void execute(@NotNull PlayerProfile profile, String data) {
            if (data != null && !data.isEmpty()) {
                Player player = profile.getPlayer();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PAPI.setPlaceholders(player, data));
            }
        }
    },
    /**
     * Send the given message into chat as the player.
     */
    CHAT() {
        @Override
        public void execute(@NotNull PlayerProfile profile, String data) {
            if (data != null && !data.isEmpty()) {
                Player player = profile.getPlayer();
                player.chat(Common.colorize(PAPI.setPlaceholders(player, data)));
            }
        }
    },

    // -- Server Actions

    /**
     * Send player to the given server.
     */
    CONNECT("server") {
        @Override
        public void execute(@NotNull PlayerProfile profile, String data) {
            if (data != null && !data.isEmpty()) {
                BungeeUtils.connect(profile.getPlayer(), data);
            }
        }
    },

    // -- Menu Actions

    /**
     * Open the given menu.
     */
    OPEN_MENU("open", "menu") {
        @Override
        public void execute(@NotNull PlayerProfile profile, String data) {
            if (data != null && !data.isEmpty()) {
                DecentMenus.getInstance().getMenuRegistry().openMenu(profile.getPlayer(), data);
            }
        }
    },
    /**
     * Open the previous menu if possible.
     */
    PREVIOUS_MENU("previous", "prev", "prev_menu") {
        @Override
        public void execute(@NotNull PlayerProfile profile, String data) {
            Menu menu = profile.getOpenMenu();
            if (menu == null) {
                return;
            }
            Menu previousMenu = menu.getPreviousMenu();
            if (previousMenu == null) {
                return;
            }
            menu.close();
            previousMenu.open();
        }
    },
    /**
     * Refresh the players current menu if possible.
     */
    REFRESH_MENU("update_menu", "refresh", "update") {
        @Override
        public void execute(@NotNull PlayerProfile profile, String data) {
            Menu menu = profile.getOpenMenu();
            if (menu != null) {
                menu.open();
            }
        }
    },
    /**
     * Close the players current menu if possible.
     */
    CLOSE_MENU("close") {
        @Override
        public void execute(@NotNull PlayerProfile profile, String data) {
            Menu menu = profile.getOpenMenu();
            if (menu != null) {
                menu.close();
            }
        }
    }
    ;

    private final Set<String> aliases;

    ActionType(String... aliases) {
        this.aliases = new HashSet<>();
        for (String alias : aliases) {
            this.aliases.add(alias.toLowerCase());
        }
    }

    /**
     * Execute this {@link ActionType} for the given {@link PlayerProfile} with the given data.
     *
     * @param profile The profile.
     * @param data The data.
     */
    public abstract void execute(@NotNull PlayerProfile profile, String data);

    /**
     * Get an {@link ActionType} from string if possible.
     *
     * @param string The string.
     * @return The matching ActionType or null if the string doesn't match any type.
     */
    @Nullable
    public static ActionType fromString(@NotNull String string) {
        for (ActionType actionType : values()) {
            if (actionType.name().equalsIgnoreCase(string) || actionType.getAliases().contains(string.toLowerCase())) {
                return actionType;
            }
        }
        return null;
    }

}
