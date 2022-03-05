package eu.decent.menus.conditions;

import lombok.Getter;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This enum represents all the different intentions, that actions and conditions can be set on.
 */
public enum ConditionIntent {
    DISPLAY,
    // -- Click Types
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

    ConditionIntent() {
        this.clickType = null;
    }

    ConditionIntent(ClickType clickType) {
        this.clickType = clickType;
    }

    @Nullable
    public static ConditionIntent fromClickType(@NotNull ClickType clickType) {
        for (ConditionIntent intent : values()) {
            ClickType intentClickType = intent.getClickType();
            if (intentClickType != null && intentClickType.equals(clickType)) {
                return intent;
            }
        }
        return null;
    }

}