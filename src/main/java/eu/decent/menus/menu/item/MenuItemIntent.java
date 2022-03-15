package eu.decent.menus.menu.item;

import lombok.Getter;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MenuItemIntent {
    DISPLAY,
    /**
     * Actions & Conditions mapped to this intent will be used
     * every time before the other intents.
     */
    CLICK,
    LEFT_CLICK(ClickType.LEFT),
    SHIFT_LEFT_CLICK(ClickType.SHIFT_LEFT),
    RIGHT_CLICK(ClickType.RIGHT),
    SHIFT_RIGHT_CLICK(ClickType.SHIFT_RIGHT),
    DROP(ClickType.DROP),
    CONTROL_DROP(ClickType.CONTROL_DROP),
    NUMBER_1(ClickType.NUMBER_KEY),
    NUMBER_2(ClickType.NUMBER_KEY),
    NUMBER_3(ClickType.NUMBER_KEY),
    NUMBER_4(ClickType.NUMBER_KEY),
    NUMBER_5(ClickType.NUMBER_KEY),
    NUMBER_6(ClickType.NUMBER_KEY),
    NUMBER_7(ClickType.NUMBER_KEY),
    NUMBER_8(ClickType.NUMBER_KEY),
    NUMBER_9(ClickType.NUMBER_KEY),
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
    public static MenuItemIntent fromEvent(@NotNull InventoryClickEvent e) {
        ClickType clickType = e.getClick();
        if (clickType.equals(ClickType.NUMBER_KEY)) {
            return MenuItemIntent.fromNumber(e.getHotbarButton());
        }
        return MenuItemIntent.fromClickType(clickType);
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
    public static MenuItemIntent fromNumber(int number) {
        switch (number) {
            case 1: return NUMBER_1;
            case 2: return NUMBER_2;
            case 3: return NUMBER_3;
            case 4: return NUMBER_4;
            case 5: return NUMBER_5;
            case 6: return NUMBER_6;
            case 7: return NUMBER_7;
            case 8: return NUMBER_8;
            case 9: return NUMBER_9;
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
