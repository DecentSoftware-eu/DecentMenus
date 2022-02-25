package eu.decent.menus.utils.ticker;

import eu.decent.library.ticker.Ticked;
import eu.decent.menus.DecentMenus;

public abstract class DecentMenusTicked extends Ticked {

    public DecentMenusTicked(long interval) {
        super(interval);
    }

    public DecentMenusTicked(String id, long interval) {
        super(id, interval);
    }

    /**
     * Register this ticked object into the plugins' Ticker.
     */
    protected void startTicking() {
        DecentMenus.getInstance().getTicker().register(this);
    }

    /**
     * Unregister this ticked object from the plugins' Ticker.
     */
    protected void stopTicking() {
        DecentMenus.getInstance().getTicker().unregister(this.getId());
    }

}
