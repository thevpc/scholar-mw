package net.thevpc.scholar.hadrumaths.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class HLRUMap<K, V> extends LinkedHashMap<K, V> {
    private int maxSize;

    public HLRUMap(int maxSize) {
        super(Math.max(4, maxSize), 0.75f, true);
        this.maxSize = Math.max(1, maxSize);
    }

    public static <K, V> HLRUMap<K, V> of(int maxSize) {
        return new HLRUMap<>(maxSize);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void resize(int newSize) {
        this.maxSize = Math.max(1, newSize);
        while (size() > maxSize && !isEmpty()) {
            K eldestKey = entrySet().iterator().next().getKey();
            remove(eldestKey);
        }
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
