package eu.decent.menus.utils.ticker;

import eu.decent.menus.DecentMenus;

/**
 * This interface represents an object that can be ticked every X ticks.
 */
public interface ITicked {

    /**
     * Perform a tick on this ticked object.
     */
    void tick();

    /**
     * Get the id of this ticked object.
     *
     * @return The id.
     */
    String getId();

    /**
     * Get the tick interval in this ticked object.
     *
     * @return The interval.
     */
    long getInterval();

    /**
     * Check whether this ticked should be ticked on the given tick.
     *
     * @param tick The tick to check.
     * @return The requested boolean.
     */
    default boolean shouldTick(long tick) {
        return tick % getInterval() == 0;
    }

    /**
     * Register this ticked object into the plugins' Ticker.
     */
    default void startTicking() {
        DecentMenus.getInstance().getTicker().register(this);
    }

    /**
     * Unregister this ticked object from the plugins' Ticker.
     */
    default void stopTicking() {
        DecentMenus.getInstance().getTicker().unregister(this.getId());
    }

}
