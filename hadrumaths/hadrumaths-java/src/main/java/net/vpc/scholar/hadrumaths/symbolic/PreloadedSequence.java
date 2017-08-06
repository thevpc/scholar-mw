package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.Collection;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class PreloadedSequence<T> extends AbstractTList<T> {
    final TList<T> cache;

    public PreloadedSequence(Class<T> componenetType,int size, TVectorCell<T> it) {
        super(componenetType);
        cache = Maths.listOf(componenetType);
        for (int i = 0; i < size; i++) {
            cache.append(it.get(i));
        }
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
