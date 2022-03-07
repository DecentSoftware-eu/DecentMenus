package eu.decent.menus.utils.ticker;

import eu.decent.menus.DecentMenus;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Ticked implements ITicked {

    @Getter
    private final String id;
    private final AtomicLong interval;

    public Ticked(long interval) {
        this(UUID.randomUUID().toString(), interval);
    }

    public Ticked(String id, long interval) {
        this.id = id;
        this.interval = new AtomicLong(interval);
    }

    @Override
    public long getInterval() {
        return interval.get();
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
