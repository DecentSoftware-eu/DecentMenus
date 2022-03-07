package eu.decent.menus.utils.ticker;

public interface ITicked {

    String getId();

    void tick();

    long getInterval();

    default boolean shouldTick(long tick) {
        return tick % getInterval() == 0;
    }

}
