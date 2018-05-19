package net.vpc.scholar.hadrumaths.util;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by vpc on 5/11/14.
 */
public class ClassPairMap<V> {
    private static final long serialVersionUID = 1L;
    private boolean symmetric;
    private Class baseKey1Type;
    private Class baseKey2Type;
    private Class<V> valueType;
    private HashMap<ClassPair, V> values = new HashMap<ClassPair, V>();
    private HashMap<ClassPair, V[]> cachedValues = new HashMap<ClassPair, V[]>();
    private HashMap<ClassPair, ClassPair[]> cachedHierarchy = new HashMap<ClassPair, ClassPair[]>();

    public ClassPairMap(Class baseKey1Type, Class baseKey2Type, Class<V> valueType, boolean symmetric) {
        this.baseKey1Type = baseKey1Type;
        this.baseKey2Type = baseKey2Type;
        this.valueType = valueType;
        this.symmetric = symmetric;
    }

    public V put(Class classKey1, Class classKey2, V value) {
        cachedValues.clear();
        return values.put(createKey(classKey1, classKey2), value);
    }

    public V remove(Class classKey1, Class classKey2) {
        cachedValues.clear();
        return values.remove(createKey(classKey1, classKey2));
    }

    public ClassPair[] getKeis(Class classKey1, Class classKey2) {
        return getKeis(createKey(classKey1, classKey2));
    }

    private ClassPair[] getKeis(ClassPair classKey) {
        ClassPair[] keis = cachedHierarchy.get(classKey);
        if (keis == null) {
            keis = evalHierarchy(classKey);
            cachedHierarchy.put(classKey, keis);
        }
        return keis;
    }

    public V getRequired(Class classKey1, Class classKey2) {
        return getRequired(createKey(classKey1, classKey2));
    }

    private V getRequired(ClassPair key) {
        V[] found = getAllRequired(key);
        return found[0];
    }

    public V getExact(Class classKey1, Class classKey2) {
        return values.get(createKey(classKey1, classKey2));
    }

    public V get(Class classKey1, Class classKey2) {
        return get(createKey(classKey1, classKey2));
    }

    private V get(ClassPair key) {
        V[] found = getAll(key);
        if (found.length > 0) {
            return found[0];
        }
        return null;
    }

    public V[] getAllRequired(Class classKey1, Class classKey2) {
        return getAllRequired(createKey(classKey1, classKey2));
    }

    private V[] getAllRequired(ClassPair classKey) {
        V[] found = getAll(classKey);
        if (found.length > 0) {
            return found;
        }
        throw new NoSuchElementException(classKey.toString());
    }

    public V[] getAll(Class classKey1, Class classKey2) {
        return getAll(createKey(classKey1, classKey2));
    }

    private V[] getAll(ClassPair classKey) {
        V[] found = cachedValues.get(classKey);
        if (found == null) {
            ClassPair[] keis = getKeis(classKey);
            List<V> all = new ArrayList<V>();
            for (ClassPair c : keis) {
                V u = values.get(c);
                if (u != null) {
                    all.add(u);
                }
            }
            found = all.toArray((V[]) Array.newInstance(valueType, all.size()));
            cachedValues.put(classKey, found);
        }
        return found;
    }


    public ClassPair[] evalHierarchy(ClassPair clazz) {
        Class[] first = PlatformUtils.findClassHierarchy(clazz.getFirst(), baseKey1Type);
        Class[] second = PlatformUtils.findClassHierarchy(clazz.getSecond(), baseKey2Type);
        HashSet<ClassPair> seen = new HashSet<ClassPair>();
        List<IndexComparable<ClassPair>> result = new LinkedList<IndexComparable<ClassPair>>();
        for (int i1 = 0; i1 < first.length; i1++) {
            Class aClass = first[i1];
            for (int i2 = 0; i2 < second.length; i2++) {
                Class bClass = second[i2];
                ClassPair i = createKey(aClass, bClass);
                if (!seen.contains(i)) {
                    seen.add(i);
                    result.add(new IndexComparable(i1 + i2, i));
                }
            }
        }
        Collections.sort(result);
        List<ClassPair> result2 = new ArrayList<ClassPair>(result.size());
        for (IndexComparable<ClassPair> ic : result) {
            result2.add(ic.v);
        }
        return result2.toArray(new ClassPair[result.size()]);
    }

    private static class IndexComparable<T> implements Comparable<IndexComparable> {
        int i;
        T v;

        @Override
        public int compareTo(IndexComparable o) {
            return i - o.i;
        }

        private IndexComparable(int i, T v) {
            this.i = i;
            this.v = v;
        }
    }

    protected ClassPair createKey(Class first, Class second) {
        if (symmetric) {
            int c = first.getName().compareTo(second.getName());
            if (c > 0) {
                Class p = second;
                second = first;
                first = p;
            }
        }
        return new ClassPair(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassPairMap)) return false;

        ClassPairMap that = (ClassPairMap) o;

        if (symmetric != that.symmetric) return false;
        if (baseKey1Type != null ? !baseKey1Type.equals(that.baseKey1Type) : that.baseKey1Type != null) return false;
        if (baseKey2Type != null ? !baseKey2Type.equals(that.baseKey2Type) : that.baseKey2Type != null) return false;
        if (valueType != null ? !valueType.equals(that.valueType) : that.valueType != null) return false;
        if (values != null ? !values.equals(that.values) : that.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (symmetric ? 1 : 0);
        result = 31 * result + (baseKey1Type != null ? baseKey1Type.hashCode() : 0);
        result = 31 * result + (baseKey2Type != null ? baseKey2Type.hashCode() : 0);
        result = 31 * result + (valueType != null ? valueType.hashCode() : 0);
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }
}
