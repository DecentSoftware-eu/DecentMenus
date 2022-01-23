package eu.decent.menus.addons;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents an addon to DecentMenus.
 */
public abstract class DecentMenusAddon {

    private final String name;
    private final AtomicBoolean enabled;

    /**
     * Create new {@link DecentMenusAddon}.
     *
     * @param name Name of the addon.
     */
    public DecentMenusAddon(String name) {
        this.name = name;
        this.enabled = new AtomicBoolean(false);
    }

    protected abstract void onEnable();

    protected abstract void onDisable();

    protected abstract void reload();

    /**
     * Toggle this addon. Enable when disabled or display when enabled.
     */
    public void toggle() {
        if (enabled.get()) {
            disable();
        } else {
            enable();
        }
    }

    /**
     * Enable this addon.
     */
    public void enable() {
        onEnable();
        enabled.set(true);
    }

    /**
     * Disable this addon.
     */
    public void disable() {
        enabled.set(false);
        onDisable();
    }

    /**
     * Get the name of this addon.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

}
