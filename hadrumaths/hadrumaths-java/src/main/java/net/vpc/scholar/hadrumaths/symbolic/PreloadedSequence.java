package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.TList;
import net.vpc.scholar.hadrumaths.TVector;
import net.vpc.scholar.hadrumaths.TVectorCell;

import java.util.Collection;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class PreloadedSequence<T> extends AbstractTList<T> {
    final TList<T> cache;
    private Class<T> componentType;

    public PreloadedSequence(Class<T> componentType, boolean row, int size, TVectorCell<T> it) {
        super(row);
        this.componentType = componentType;
        cache = Maths.listOf(componentType);
        for (int i = 0; i < size; i++) {
            cache.append(it.get(i));
        }
    }

    @Override
    public Class<T> getComponentType() {
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
    public void set(int index, T e) {
        cache.set(index, e);
    }

    @Override
    public void appendAll(TVector<T> e) {
        cache.appendAll(e);
    }

    @Override
    public void append(T e) {
        cache.append(e);
    }

    @Override
    public void appendAll(Collection<? extends T> e) {
        cache.appendAll(e);
    }

    @Override
    public String toString() {
        return "PreloadedSequence{" +
                "cache=" + cache +
                '}';
    }
}
