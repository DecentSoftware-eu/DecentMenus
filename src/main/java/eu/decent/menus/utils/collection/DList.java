package eu.decent.menus.utils.collection;

import eu.decent.menus.utils.Common;
import org.jetbrains.annotations.Contract;

import java.util.*;

@SuppressWarnings("unused")
public class DList<T> extends ArrayList<T> {

    public DList(int initialCapacity) {
        super(initialCapacity);
    }

    public DList(Collection<T> collection) {
        super();
        this.add(collection);
    }

    @SafeVarargs
    public DList(T... array) {
        super();
        this.add(array);
    }

    @Contract(mutates = "this")
    @SafeVarargs
    public final void add(T... ts) {
        if (ts != null && ts.length > 0) {
            this.addAll(Arrays.asList(ts));
        }
    }

    public void add(Collection<T> collection) {
        if (collection != null && !collection.isEmpty()) {
            this.addAll(collection);
        }
    }

    public void addFirst(T t) {
        add(0, t);
    }

    public T first() {
        if (size() < 1) return null;
        return get(0);
    }

    public T random() {
        return get(randomIndex());
    }

    public T last() {
        if (size() < 1) return null;
        return get(lastIndex());
    }

    /**
     * Pop the first item off this list and return it
     *
     * @return The item or null if the list is empty
     */
    public T pop() {
        if (isEmpty()) return null;
        return remove(0);
    }

    /**
     * Pop the last item off this list and return it
     *
     * @return The item or null if the list is empty
     */
    public T popLast() {
        if (isEmpty()) return null;
        return remove(lastIndex());
    }

    /**
     * Pop a random item from this list and return it.
     *
     * @return The item or null if the list is empty.
     */
    public T popRandom() {
        if (isEmpty()) return null;
        if (size() == 1) {
            return pop();
        }
        return remove(randomIndex());
    }

    /**
     * Check whether this list is NOT empty.
     *
     * @return The requested boolean.
     */
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * Check whether this list is NOT empty.
     *
     * @return The requested boolean.
     */
    public boolean hasElements() {
        return !isEmpty();
    }

    /**
     * Get the index of the last item.
     *
     * @return index of the last item.
     */
    public int lastIndex() {
        return size() - 1;
    }

    /**
     * Get a random index in bounds of this list.
     *
     * @return The index.
     */
    public int randomIndex() {
        return Common.irand(0, size() - 1);
    }

    /**
     * Check whether this list has any duplicate items.
     *
     * @return The requested boolean.
     */
    public boolean hasDuplicates() {
        return size() != new LinkedHashSet<>(this).size();
    }

    /**
     * Check whether the given index is in bounds of this list.
     *
     * @param index The index.
     * @return The requested boolean.
     */
    public boolean inBounds(int index) {
        return index >= 0 && index < size();
    }

    /**
     * Create a copy of this list and return it.
     *
     * @return The copy.
     */
    public DList<T> copy() {
        return new DList<>(this);
    }

    /**
     * Sort this list.
     *
     * @return Instance of this.
     */
    public DList<T> sort() {
        sort(null);
        return this;
    }

    /**
     * Create a copy of this list and sort it.
     *
     * @return The copy.
     */
    public DList<T> sortCopy() {
        DList<T> list = copy();
        list.sort(null);
        return list;
    }

    /**
     * Shuffle this list.
     *
     * @return Instance of this.
     */
    public DList<T> shuffle() {
        Collections.shuffle(this);
        return this;
    }

    /**
     * Create a copy of this list and shuffle it.
     *
     * @return The copy.
     */
    public DList<T> shuffleCopy() {
        DList<T> list = copy();
        Collections.shuffle(list);
        return list;
    }

    /**
     * Reverse this list.
     *
     * @return Instance of this.
     */
    public DList<T> reverse() {
        Collections.reverse(this);
        return this;
    }

    /**
     * Create a copy of this list and reverse it.
     *
     * @return The copy.
     */
    public DList<T> reverseCopy() {
        DList<T> list = copy();
        Collections.reverse(list);
        return list;
    }

    /**
     * Create a copy of this list and parse all of its items to Strings.
     *
     * @return The copy.
     */
    public DList<String> toStringList() {
        DList<String> list = new DList<>();
        for (T t : this) {
            list.add(t.toString());
        }
        return list;
    }

    /**
     * Get a sublist.
     *
     * @param start The start index. (Including)
     * @param end The end index. (Excluding)
     * @return The sublist.
     */
    @Override
    public DList<T> subList(int start, int end) {
        DList<T> list = new DList<>();
        for (int i = start; i < Math.min(size(), end); i++) {
            list.add(get(i));
        }
        return list;
    }

}
