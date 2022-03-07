package eu.decent.menus.utils.collection;

public class DecentHolder<T> {

    private final DList<T> list;

    public DecentHolder() {
        this.list = new DList<>();
    }

    public DecentHolder(int cap) {
        this.list = new DList<>(cap);
    }

    public DecentHolder<T> append(T t) {
        list.add(t);
        return this;
    }

    public DecentHolder<T> prepend(T t) {
        list.addFirst(t);
        return this;
    }

    public DecentHolder<T> insert(int index, T t) {
        if (inBounds(index)) {
            list.add(index, t);
        }
        return this;
    }

    public DecentHolder<T> get(int index, T t) {
        if (inBounds(index)) {
            list.set(index, t);
        }
        return this;
    }

    public DecentHolder<T> set(int index, T t) {
        if (inBounds(index)) {
            list.set(index, t);
        }
        return this;
    }

    public T remove(int index) {
        if (inBounds(index)) {
            return list.remove(index);
        }
        return null;
    }

    public T get(int index) {
        if (inBounds(index)) {
            return list.get(index);
        }
        return null;
    }

    public DList<T> asList() {
        return list;
    }

    public boolean inBounds(int index) {
        return index >= 0 && index < list.size();
    }

}
