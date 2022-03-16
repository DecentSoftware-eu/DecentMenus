package eu.decent.menus.placeholders;

import eu.decent.menus.Config;
import eu.decent.menus.DecentMenus;
import eu.decent.menus.server.Server;
import eu.decent.menus.utils.DatetimeUtils;
import eu.decent.menus.utils.collection.Registry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This registry holds all custom placeholders.
 */
public class PlaceholderRegistry extends Registry<String, Placeholder> {

    private static final DecentMenus PLUGIN = DecentMenus.getInstance();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\S+(:\\S+)?)}");

    public PlaceholderRegistry() {
        this.registerDefaultPlaceholders();
    }

    @Override
    public void reload() {}

    /**
     * Replace all registered placeholders, that the given String contains.
     *
     * @param player The player to replace them for.
     * @param string The string.
     * @return The resulting String.
     */
    public String replacePlaceholders(@NotNull Player player, @NotNull String string) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(string);
        while (matcher.find()) {
            String replacement = this.getReplacement(player, matcher.group(1).trim());
            if (replacement != null) {
                string = string.replace(matcher.group(), replacement);
            }
            matcher = PLACEHOLDER_PATTERN.matcher(string);
        }
        return string;
    }

    /**
     * Get the replacement for the given placeholder string.
     *
     * @param player The player to get the replacement for.
     * @param placeholderString The placeholder string.
     * @return The replacement for the placeholder string or null
     *  if the given placeholder string cannot be replaced.
     */
    @Nullable
    private String getReplacement(@NotNull Player player, @NotNull String placeholderString) {
        String placeholderIdentifier;
        String placeholderArgument;
        if (placeholderString.contains(":")) {
            String[] spl = placeholderString.split(":", 1);
            placeholderIdentifier = spl[0];
            placeholderArgument = spl[1];
        } else {
            placeholderIdentifier = placeholderString;
            placeholderArgument = null;
        }

        Placeholder placeholder = this.get(placeholderIdentifier);
        if (placeholder != null) {
            String replacement = placeholder.getReplacementSupplier().getReplacement(player, placeholderArgument);
            return replacement == null ? placeholder.getDefaultReplacement() : replacement;
        }
        return null;
    }

    /**
     * Register the default built-in placeholders.
     */
    private void registerDefaultPlaceholders() {
        // -- Player placeholders

        this.register("player", new Placeholder(
                (player, argument) -> player.getName(),
                "You")
        );
        this.register("display_name", new Placeholder(
                (player, argument) -> player.getDisplayName(),
                "You")
        );
        this.register("ping", new Placeholder(
                (player, argument) -> String.valueOf(player.getPing()),
                "0")
        );

        // -- Global placeholders

        this.register("time", new Placeholder(
                (player, argument) -> DatetimeUtils.getTimeFormatted(),
                "-")
        );
        this.register("date", new Placeholder(
                (player, argument) -> DatetimeUtils.getDateFormatted(),
                "-")
        );

        // -- World placeholders

        this.register("world", new Placeholder(
                (player, argument) -> {
                    int online;
                    if (argument != null) {
                        // -- Given worlds
                        online = PlaceholderCommons.getFromWorldOrWorldsInt(
                                argument, (world) -> world.getPlayers().size()
                        );
                    } else if (player != null) {
                        // -- Player world
                        online = player.getWorld().getPlayers().size();
                    } else {
                        online = -1;
                    }
                    return online >= 0 ? String.valueOf(online) : null;
                }, "0")
        );

        // -- Server & Pinger placeholders

        this.register("online", new Placeholder(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        int online = PlaceholderCommons.getFromServerOrServersInt(
                                player, argument, (server) -> server.getData().getPlayers().getOnline()
                        );
                        if (online >= 0) {
                            return String.valueOf(online);
                        }
                    } else {
                        // -- This server
                        return String.valueOf(Bukkit.getOnlinePlayers().size());
                    }
                    return null;
                }, "0")
        );
        this.register("max_players", new Placeholder(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        int online = PlaceholderCommons.getFromServerOrServersInt(
                                player, argument, (server) -> server.getData().getPlayers().getMax()
                        );
                        if (online >= 0) {
                            return String.valueOf(online);
                        }
                    } else {
                        // -- This server
                        return String.valueOf(Bukkit.getServer().getMaxPlayers());
                    }
                    return null;
                }, "0")
        );
        this.register("motd", new Placeholder(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        Server server = PLUGIN.getServerRegistry().get(argument);
                        if (server != null && server.isOnline()) {
                            return server.getData().getDescription();
                        }
                    } else {
                        // -- This server
                        return Bukkit.getServer().getMotd();
                    }
                    return null;
                }, "")
        );
        this.register("status", new Placeholder(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        Server server = PLUGIN.getServerRegistry().get(argument);
                        if (server != null && server.isOnline()) {
                            return Config.PINGER_STATUS_ONLINE;
                        }
                    } else {
                        // -- This server
                        return Config.PINGER_STATUS_ONLINE;
                    }
                    return null;
                }, Config.PINGER_STATUS_OFFLINE)
        );
        // TODO: implement all placeholders

    }

}
