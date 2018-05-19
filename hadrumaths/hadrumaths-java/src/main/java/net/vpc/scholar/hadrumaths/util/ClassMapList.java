package net.vpc.scholar.hadrumaths.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpc on 11/22/16.
 */
public class ClassMapList<T> extends ClassMap<List<T>> {
    public ClassMapList(int initialCapacity) {
        this(Object.class, initialCapacity);
    }

    public ClassMapList() {
        this(Object.class);
    }

    public ClassMapList(Class keyType) {
        super(keyType, (Class) List.class);
    }

    public ClassMapList(Class keyType, int initialCapacity) {
        super(keyType, (Class) List.class, initialCapacity);
    }

    public List<T> add(Class classKey, T value) {
        List<T> list = getOrCreate(classKey);
        list.add(value);
        return list;
    }

    public List<T> getOrCreate(Class classKey) {
        List<T> list = super.get(classKey);
        if (list == null) {
            list = new ArrayList<T>();
            put(classKey, list);
        }
        return list;
    }
}
