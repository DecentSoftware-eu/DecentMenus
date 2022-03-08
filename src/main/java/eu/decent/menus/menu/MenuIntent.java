package eu.decent.menus.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MenuIntent {
    OPEN,
    CLOSE,
    WINDOW_BORDER_RIGHT_CLICK,
    WINDOW_BORDER_LEFT_CLICK
    ;

    @Nullable
    public static MenuIntent fromString(@NotNull String string) {
        for (MenuIntent menuIntent : values()) {
            if (menuIntent.name().equalsIgnoreCase(string)) {
                return menuIntent;
            }
        }
        return null;
    }

}
