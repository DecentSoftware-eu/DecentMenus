package eu.decent.menus.utils.ticker;

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

}
