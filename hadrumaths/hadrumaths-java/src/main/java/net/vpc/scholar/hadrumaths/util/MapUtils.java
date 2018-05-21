package net.vpc.scholar.hadrumaths.util;

import java.util.*;

public class MapUtils {

    public static <K, V> Map<K, V> linkedMap(Entry<K, V>... values) {
        return map(new LinkedHashMap<>(values == null ? 1 : values.length), values);
    }

    public static <K, V> Map<K, V> map(Entry<K, V>... values) {
        return map(new HashMap<>(values == null ? 1 : values.length), values);
    }


    public static <K, V> Map<K, V> linkedMap(Object... values) {
        return map(new LinkedHashMap<>(values == null ? 1 : values.length), values);
    }

    public static <K, V> Map<K, V> map(Object... values) {
        return map(new HashMap<>(values == null ? 1 : values.length), values);
    }

    private static <K, V> Entry<K, V>[] entries(Object... values) {
        List<Entry<K, V>> list = new ArrayList<>();
        for (int i = 0; i < values.length; i += 2) {
            list.add(new Entry<K, V>((K) values[i], (V) values[i + 1]));
        }
        return list.toArray(new Entry[list.size()]);
    }

    public static <K, V> Map<K, V> map(Map<K, V> m, Object... values) {
        return map(m, entries(values));
    }

    public static <K, V> Map<K, V> map(Map<K, V> m, Entry<K, V>... values) {
        if (m == null) {
            m = new HashMap<>(values == null ? 1 : values.length);
        }
        if (values != null) {
            for (Entry<K, V> value : values) {
                if (value != null) {
                    m.put(value.key, value.value);
                }
            }
        }
        return m;
    }

    public static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
