/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.util;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author vpc
 */
public class IndexedMap<K, V> {

    private List<K> list = new ArrayList<K>();
    private Map<K, V> map = new HashMap<K, V>();

    public V getValueAt(int i) {
        return map.get(list.get(i));
    }

    public void clear() {
        list.clear();
        map.clear();
    }

    public int indexOfKey(K key) {
        for (int i = 0; i < list.size(); i++) {
            K e = list.get(i);
            if (e.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    public V get(K key) {
        return map.get(key);
    }

    public K getKeyAt(int i) {
        return list.get(i);
    }

    public Set<K> keySet() {
        return new LinkedHashSet<K>(list);
    }

    public Set<Entry<K, V>> entrySet() {
        LinkedHashSet<Entry<K, V>> es = new LinkedHashSet<Entry<K, V>>();
        for (K k : list) {
            es.add(new LMEntry(k, map.get(k)));
        }
        return es;
    }

    public Collection<V> values() {
        List<V> es = new ArrayList<V>();
        for (K k : list) {
            es.add(map.get(k));
        }
        return es;
    }

    public int size() {
        return list.size();
    }

    public V remove(K key) {
        if (map.containsKey(key)) {
            int i = indexOfKey(key);
            list.remove(i);
            return map.remove(key);
        }
        return null;
    }

    public V put(K key, V value) {
        if (map.containsKey(key)) {
            return map.put(key, value);
        }
        list.add(key);
        return map.put(key, value);
    }

    private class LMEntry implements Map.Entry<K, V> {

        private K key;
        private V value;

        public LMEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            return put(key, value);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.key != null ? this.key.hashCode() : 0);
            hash = 29 * hash + (this.value != null ? this.value.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LMEntry other = (LMEntry) obj;
            if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
                return false;
            }
            if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
                return false;
            }
            return true;
        }

    }
}
