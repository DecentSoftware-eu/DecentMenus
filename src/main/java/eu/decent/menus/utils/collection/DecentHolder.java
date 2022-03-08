package eu.decent.menus.utils.collection;

/**
 * This class represents an object that holds a list of something.
 *
 * @param <T> Type of the held objects.
 */
@SuppressWarnings("unused")
public class DecentHolder<T> {

    /**
     * The list storing all held objects.
     */
    private final DList<T> list;

    /**
     * Create a new {@link DecentHolder}.
     */
    public DecentHolder() {
        this.list = new DList<>();
    }

    /**
     * Create a new {@link DecentHolder}.
     *
     * @param cap The maximum expected size of this holder.
     */
    public DecentHolder(int cap) {
        this.list = new DList<>(cap);
    }

    /**
     * Add a new object on the last place in this holder.
     *
     * @param t The object.
     * @return Instance of this.
     */
    public DecentHolder<T> append(T t) {
        list.add(t);
        return this;
    }

    /**
     * Add a new object on the first place in this holder.
     *
     * @param t The object.
     * @return Instance of this.
     */
    public DecentHolder<T> prepend(T t) {
        list.addFirst(t);
        return this;
    }

    /**
     * Insert a new object on the specified index in this holder.
     *
     * @param index The index.
     * @param t The object.
     * @return Instance of this.
     */
    public DecentHolder<T> insert(int index, T t) {
        if (inBounds(index)) {
            list.add(index, t);
        }
        return this;
    }

    /**
     * Set a new object on the specified index in this holder.
     *
     * @param index The index.
     * @param t The object.
     * @return Instance of this.
     */
    public DecentHolder<T> set(int index, T t) {
        if (inBounds(index)) {
            list.set(index, t);
        }
        return this;
    }

    /**
     * Remove the object at the specified index in this holder.
     *
     * @param index The index.
     * @return The removed object.
     */
    public T remove(int index) {
        if (inBounds(index)) {
            return list.remove(index);
        }
        return null;
    }

    /**
     * Get the object at the specified index in this holder.
     *
     * @param index The index.
     * @return The object.
     */
    public T get(int index) {
        if (inBounds(index)) {
            return list.get(index);
        }
        return null;
    }

    /**
     * Get this holder as a list.
     *
     * @return The list.
     */
    public DList<T> asList() {
        return list;
    }

    /**
     * Check whether the given index is in bounds of this holder.
     *
     * @param index The index.
     * @return The requested boolean.
     */
    public boolean inBounds(int index) {
        return index >= 0 && index < list.size();
    }

}
