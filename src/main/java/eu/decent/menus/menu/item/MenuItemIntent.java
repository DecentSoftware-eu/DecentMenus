package eu.decent.menus.menu.item;

import lombok.Getter;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MenuItemIntent {
    DISPLAY,
    CLICK,
    LEFT_CLICK(ClickType.LEFT),
    SHIFT_LEFT_CLICK(ClickType.SHIFT_LEFT),
    RIGHT_CLICK(ClickType.RIGHT),
    SHIFT_RIGHT_CLICK(ClickType.SHIFT_RIGHT),
    WINDOW_BORDER_RIGHT_CLICK(ClickType.WINDOW_BORDER_RIGHT),
    WINDOW_BORDER_LEFT_CLICK(ClickType.WINDOW_BORDER_LEFT),
    ;

    @Getter
    private final ClickType clickType;

    MenuItemIntent() {
        this(null);
    }

    MenuItemIntent(ClickType clickType) {
        this.clickType = clickType;
    }

    @Nullable
    public static MenuItemIntent fromClickType(@NotNull ClickType clickType) {
        for (MenuItemIntent intent : values()) {
            ClickType intentClickType = intent.getClickType();
            if (intentClickType != null && intentClickType.equals(clickType)) {
                return intent;
            }
        }
        return null;
    }

    @Nullable
    public static MenuItemIntent fromString(@NotNull String string) {
        for (MenuItemIntent menuItemIntent : values()) {
            if (menuItemIntent.name().equalsIgnoreCase(string)) {
                return menuItemIntent;
            }
        }
        return null;
    }

}
