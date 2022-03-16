package eu.decent.menus.placeholders;

import lombok.Getter;

/**
 * This class represents a custom placeholder.
 */
@Getter
public class Placeholder {

    private final PlaceholderReplacementSupplier replacementSupplier;
    private final String defaultReplacement;

    /**
     * Create new {@link Placeholder}.
     *
     * @param replacementSupplier The supplier, that finds a replacement for this placeholder.
     */
    public Placeholder(PlaceholderReplacementSupplier replacementSupplier) {
        this(replacementSupplier, null);
    }

    /**
     * Create new {@link Placeholder}.
     *
     * @param replacementSupplier The supplier, that finds a replacement for this placeholder.
     * @param defaultReplacement The default replacement that will be used if the replacement supplier returns null.
     */
    public Placeholder(PlaceholderReplacementSupplier replacementSupplier, String defaultReplacement) {
        this.replacementSupplier = replacementSupplier;
        this.defaultReplacement = defaultReplacement;
    }

}
