package imgui;

import java.util.ArrayList;
import java.util.List;

// TODO: Implement as needed
public final class ImVector<T> {
    private final List<T> list;

    public ImVector() {
        list = new ArrayList<>();
    }

    public void clear() {
        list.clear();
    }

    public boolean empty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public T get(int idx) {
        return list.get(idx);
    }

    public void set(int idx, T value) {
        list.set(idx, value);
    }

    public T[] data(T[] arr) {
        return list.toArray(arr);
    }

    public T back() {
        return list.get(list.size() - 1);
    }

    public void pushBack(T t) {
        list.add(t);
    }
}
