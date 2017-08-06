package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.TVector;
import net.vpc.scholar.hadrumaths.TVectorCell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class CachedSequence<T> extends AbstractTList<T> {
    private int size;
    private List<T> cache = new ArrayList<>();
    private TVectorCell<T> it;
    private boolean updated = false;
    private Class<T> componentType;

    public CachedSequence(Class<T> componentType, boolean row, int size, T[] cache, TVectorCell<T> it) {
        super(row);
        this.componentType = componentType;
        this.size = size;
        this.cache.addAll(Arrays.asList(cache));
        this.it = it;
    }

    @Override
    public Class<T> getComponentType() {
        return componentType;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        if (index < size && !updated) {
            T old = cache.get(index);
            if (old == null) {
                old = it.get(index);
                cache.set(index, old);
            }
            return old;
        } else {
            return cache.get(index);
        }
    }

    protected void setUpdated() {
        if (!updated) {
            updated = true;
            for (int i = 0; i < size; i++) {
                if (cache.get(i) == null) {
                    cache.set(i, (it.get(i)));
                }
            }
        }
    }

    @Override
    public void set(int index, T e) {
        cache.set(index, e);
    }

    @Override
    public void appendAll(TVector<T> e) {
        setUpdated();
        for (T expr : e) {
            cache.add(expr);
        }
    }

    @Override
    public void append(T e) {
        setUpdated();
        cache.add(e);
    }

    @Override
    public void appendAll(Collection<? extends T> e) {
        setUpdated();
        cache.addAll(e);
    }

    @Override
    public String toString() {
        return "CachedSequence{" +
                "items=" + it +
                ", size=" + size +
                '}';
    }
}
