package net.vpc.scholar.hadrumaths.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vpc on 5/11/14.
 */
public class ClassPairMapList<V> {
    private ClassPairMap<List<V>> base;

    public ClassPairMapList(Class baseKey1Type,Class baseKey2Type,Class<V> valueType, boolean symmetric) {
        base=new ClassPairMap<List<V>>(baseKey1Type,baseKey2Type,(Class) List.class,symmetric);
    }

    public void add(Class classKey1, Class classKey2, V value) {
        List<V> t = base.getExact(classKey1, classKey2);
        if(t==null){
            t=new ArrayList<V>();
            base.put(classKey1, classKey2,t);
        }
        t.add(value);
    }

    public void remove(Class classKey1, Class classKey2, V value) {
        List<V> t = base.getExact(classKey1, classKey2);
        if(t!=null){
            t.remove(value);
        }
    }

    public List<V> getAll(Class classKey1, Class classKey2) {
        ArrayList<V> all=new ArrayList<V>();
        for (List<V> vs : base.getAll(classKey1, classKey2)) {
            all.addAll(vs);
        }
        return all;
    }

    public List<V> getExact(Class classKey1, Class classKey2) {
        List<V> t = base.getExact(classKey1, classKey2);
        if(t==null){
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassPairMapList)) return false;

        ClassPairMapList that = (ClassPairMapList) o;

        if (base != null ? !base.equals(that.base) : that.base != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return base != null ? base.hashCode() : 0;
    }
}
