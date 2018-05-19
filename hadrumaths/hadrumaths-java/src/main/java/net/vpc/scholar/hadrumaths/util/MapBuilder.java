package net.vpc.scholar.hadrumaths.util;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {
    private Map<K, V> map;

    public static <K, V> MapBuilder<K, V> create() {
        return new MapBuilder<>();
    }

    public static <K, V> MapBuilder<K, V> of(K k, V v) {
        return MapBuilder.<K, V>create().put(k, v);
    }

    public static <K, V> MapBuilder<K, V> of(K k, V v, K k1, V v1) {
        return MapBuilder.<K, V>create().putValues(k, v, k1, v1);
    }

    public static <K, V> MapBuilder<K, V> of(K k, V v, K k1, V v1, K k2, V v2) {
        return MapBuilder.<K, V>create().putValues(k, v, k1, v1, k2, v2);
    }

    public static <K, V> MapBuilder<K, V> of(K k, V v, K k1, V v1, K k2, V v2, K k3, V v3) {
        return MapBuilder.<K, V>create().putValues(k, v, k1, v1, k2, v2, k3, v3);
    }

    public static <K, V> MapBuilder<K, V> of(K k, V v, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return MapBuilder.<K, V>create().putValues(k, v, k1, v1, k2, v2, k3, v3, k4, v4);
    }

    public MapBuilder() {
        this(new HashMap<>());
    }

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public MapBuilder<K, V> put(K k, V v) {
        map.put(k, v);
        return this;
    }

    public MapBuilder<K, V> putValues(K k, V v) {
        map.put(k, v);
        return this;
    }

    public MapBuilder<K, V> putValues(K k, V v, K k1, V v1) {
        map.put(k, v);
        map.put(k1, v1);
        return this;
    }

    public MapBuilder<K, V> putValues(K k, V v, K k1, V v1, K k2, V v2) {
        map.put(k, v);
        map.put(k1, v1);
        map.put(k2, v2);
        return this;
    }

    public MapBuilder<K, V> putValues(K k, V v, K k1, V v1, K k2, V v2, K k3, V v3) {
        map.put(k, v);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return this;
    }

    public MapBuilder<K, V> putValues(K k, V v, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        map.put(k, v);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return this;
    }

    public MapBuilder<K, V> remove(K k) {
        map.remove(k);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }
}
