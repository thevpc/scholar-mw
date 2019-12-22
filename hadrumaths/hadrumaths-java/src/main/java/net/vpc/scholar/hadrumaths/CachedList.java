package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class CachedList<T> extends AbstractTVector<T> {

    private static final long serialVersionUID = 1L;
    private int size;
    private List<T> cache = new ArrayList<>();
    private TVectorCell<T> it;
    private boolean updated = false;
    private TypeName<T> componentType;

    public CachedList(TypeName<T> componentType, boolean row, int size, T[] cache, TVectorCell<T> it) {
        super(row);
        this.componentType = componentType;
        this.size = size;
        this.cache.addAll(Arrays.asList(cache));
        this.it = it;
    }

    @Override
    public TypeName<T> getComponentType() {
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
    public TVector<T> set(int index, T e) {
        cache.set(index, e);
        return this;
    }

    @Override
    public TVector<T> appendAll(TVector<T> e) {
        setUpdated();
        for (T expr : e) {
            cache.add(expr);
        }
        return this;
    }

    @Override
    public TVector<T> append(T e) {
        setUpdated();
        cache.add(e);
        return this;
    }

    @Override
    public TVector<T> appendAll(Collection<? extends T> e) {
        setUpdated();
        cache.addAll(e);
        return this;
    }

    @Override
    public String toString() {
        return "CachedList{"
                + "items=" + it
                + ", size=" + size
                + '}';
    }

    @Override
    public TVector<T> sort() {
        return copy().sort();
    }

    @Override
    public TVector<T> removeDuplicates() {
        return copy().removeDuplicates();
    }
}
