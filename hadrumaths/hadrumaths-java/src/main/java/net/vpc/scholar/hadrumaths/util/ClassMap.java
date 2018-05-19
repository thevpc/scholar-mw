package net.vpc.scholar.hadrumaths.util;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 13 juil. 2006 22:14:21
 */
public class ClassMap<V> {


    private static final long serialVersionUID = 1L;
    private HashMap<Class, V> values;
    private Class keyType;
    private Class<V> valueType;
    private HashMap<Class, V[]> cachedValues;
    private HashMap<Class, Class[]> cachedHierarchy;

    public ClassMap(Class keyType, Class<V> valueType) {
        this(keyType, valueType, 10);
    }

    public ClassMap(Class keyType, Class<V> valueType, int initialCapacity) {
        this.keyType = keyType;
        this.valueType = valueType;
        values = new HashMap<Class, V>(initialCapacity);
        cachedValues = new HashMap<Class, V[]>(initialCapacity * 2);
        cachedHierarchy = new HashMap<Class, Class[]>(initialCapacity * 2);
    }

    public V put(Class classKey, V value) {
        cachedValues.clear();
        return values.put(classKey, value);
    }

    public V remove(Class classKey) {
        cachedValues.clear();
        return values.remove(classKey);
    }

    public Class[] getKeys(Class classKey) {
        Class[] keis = cachedHierarchy.get(classKey);
        if (keis == null) {
            keis = PlatformUtils.findClassHierarchy(classKey, keyType);
            cachedHierarchy.put(classKey, keis);
        }
        return keis;
    }

    public V getRequired(Class key) {
        V[] found = getAllRequired(key);
        return found[0];
    }

    public V getExact(Class key) {
        return values.get(key);
    }

    public V get(Class key) {
        V[] found = getAll(key);
        if (found.length > 0) {
            return found[0];
        }
        return null;
    }

    public V[] getAllRequired(Class key) {
        V[] found = getAll(key);
        if (found.length > 0) {
            return found;
        }
        throw new NoSuchElementException(key.getName());
    }

    public V[] getAll(Class key) {
        V[] found = cachedValues.get(key);
        if (found == null) {
            Class[] keis = getKeys(key);
            List<V> all = new ArrayList<V>();
            for (Class c : keis) {
                V u = values.get(c);
                if (u != null) {
                    all.add(u);
                }
            }
            found = all.toArray((V[]) Array.newInstance(valueType, all.size()));
            cachedValues.put(key, found);
        }
        return found;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassMap)) return false;

        ClassMap classMap = (ClassMap) o;

        if (keyType != null ? !keyType.equals(classMap.keyType) : classMap.keyType != null) return false;
        if (valueType != null ? !valueType.equals(classMap.valueType) : classMap.valueType != null) return false;
        if (values != null ? !values.equals(classMap.values) : classMap.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        //eval map hashcode according to names and not class references
        if (values != null) {
            int h = 0;
            Iterator<Map.Entry<Class, V>> i = values.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<Class, V> next = i.next();
                h += (next.getKey().getName().hashCode() ^ (next.getValue() == null ? 0 : next.getValue().hashCode()));
            }
            result = h;
        }
        result = 31 * result + (keyType != null ? keyType.getName().hashCode() : 0);
        result = 31 * result + (valueType != null ? valueType.getName().hashCode() : 0);
        return result;
    }
}
