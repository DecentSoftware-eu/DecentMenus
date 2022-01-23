package eu.decent.menus.utils;

import eu.decent.library.ticker.Ticked;
import eu.decent.menus.DecentMenus;

public abstract class DecentMenusTicked extends Ticked {

    public DecentMenusTicked(long interval) {
        super(interval);
    }

    public DecentMenusTicked(String id, long interval) {
        super(id, interval);
    }

    protected void startTicking() {
        DecentMenus.getInstance().getTicker().register(this);
    }

    protected void stopTicking() {
        DecentMenus.getInstance().getTicker().unregister(this.getId());
    }

}
