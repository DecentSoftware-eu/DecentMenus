package eu.decent.menus.placeholders;

import eu.decent.menus.utils.collection.Registry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This registry holds all custom placeholders.
 */
public class PlaceholderRegistry extends Registry<String, Placeholder> {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\S+(:\\S+)?)}");

    public PlaceholderRegistry() {
        this.registerDefaultPlaceholders();
    }

    @Override
    public void reload() {}

    /**
     * Register the default built-in placeholders.
     */
    private void registerDefaultPlaceholders() {
        this.register("player", new Placeholder(
                (player, argument) -> player.getName(),
                "You")
        );
        this.register("displayName", new Placeholder(
                (player, argument) -> player.getDisplayName(),
                "You")
        );
        this.register("ping", new Placeholder(
                (player, argument) -> String.valueOf(player.getPing()),
                "0")
        );
        // TODO: implement all placeholders

    }

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
            string = string.replace(matcher.group(), String.valueOf(replacement));
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

}
