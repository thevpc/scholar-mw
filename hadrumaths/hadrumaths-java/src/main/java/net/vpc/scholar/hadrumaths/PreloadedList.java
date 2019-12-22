package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.util.Collection;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class PreloadedList<T> extends AbstractTVector<T> {

    private static final long serialVersionUID = 1L;
    private final TVector<T> cache;
    private TypeName<T> componentType;

    public PreloadedList(TypeName<T> componentType, boolean row, int size, TVectorCell<T> it) {
        super(row);
        this.componentType = componentType;
        cache = MathsBase.list(componentType);
        for (int i = 0; i < size; i++) {
            cache.append(it.get(i));
        }
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public T get(int index) {
        return cache.get(index);
    }

    @Override
    public TVector<T> set(int index, T e) {
        cache.set(index, e);
        return this;
    }

    @Override
    public TVector<T> appendAll(TVector<T> e) {
        return cache.appendAll(e);
    }

    @Override
    public TVector<T> append(T e) {
        return cache.append(e);
    }

    @Override
    public TVector<T> appendAll(Collection<? extends T> e) {
        return cache.appendAll(e);
    }

    @Override
    public String toString() {
        return "PreloadedList{"
                + "cache=" + cache
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
