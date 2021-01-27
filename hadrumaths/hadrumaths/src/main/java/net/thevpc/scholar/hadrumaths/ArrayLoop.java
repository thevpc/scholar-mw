package net.thevpc.scholar.hadrumaths;

public class ArrayLoop implements Loop {
    private final Object[] value;
    private int index;

    public ArrayLoop(Object[] value) {
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
        return index < value.length;
    }

    @Override
    public Object get() {
        return value[index];
    }
}
