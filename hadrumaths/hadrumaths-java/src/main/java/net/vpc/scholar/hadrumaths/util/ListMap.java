package net.vpc.scholar.hadrumaths.util;

import java.util.*;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class ListMap<K, V> {
    private Map<K, List<V>> map = new HashMap<K, List<V>>();

    public ListMap(Map<K, List<V>> map) {
        this.map = createMap();
        if (map != null) {
            this.map.putAll(map);
        }
    }

    public ListMap() {
        this.map = createMap();
    }

    public void add(K k, V v) {
        get(k).add(v);
    }

    public void remove(K k, V v) {
        get(k).remove(v);
    }

    public List<V> get(K k) {
        List<V> list = map.get(k);
        if (list == null) {
            list = createList();
            map.put(k, list);
        }
        return list;
    }

    public int keySize() {
        return map.size();
    }

    public int size() {
        int count = 0;
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            count += entry.getValue().size();
        }
        return count;
    }

    public Set<Map.Entry<K, List<V>>> entrySet() {
        return map.entrySet();
    }

    protected Map<K, List<V>> createMap() {
        return new HashMap<K, List<V>>();
    }

    protected List<V> createList() {
        return new ArrayList<V>();
    }
}
