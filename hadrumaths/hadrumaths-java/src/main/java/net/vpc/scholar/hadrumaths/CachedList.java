package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class CachedList<T> extends AbstractTList<T> {
    private static final long serialVersionUID = 1L;
    private int size;
    private List<T> cache = new ArrayList<>();
    private TVectorCell<T> it;
    private boolean updated = false;
    private TypeReference<T> componentType;

    public CachedList(TypeReference<T> componentType, boolean row, int size, T[] cache, TVectorCell<T> it) {
        super(row);
        this.componentType = componentType;
        this.size = size;
        this.cache.addAll(Arrays.asList(cache));
        this.it = it;
    }

    @Override
    public TypeReference<T> getComponentType() {
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
        return "CachedList{" +
                "items=" + it +
                ", size=" + size +
                '}';
    }
}
