package net.thevpc.scholar.hadrumaths;

import java.util.List;

public class ListLoop<T> implements Loop {
    private final List value;
    private int index;

    public ListLoop(List value) {
        this.value = value;
    }

    @Override
    public void reset() {
        index = 0;
    }

    @Override
    public void next() {
        index++;
    }

    @Override
    public boolean hasNext() {
        return index < value.size();
    }

    @Override
    public Object get() {
        return value.get(index);
    }
}
