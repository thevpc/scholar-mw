package net.vpc.scholar.hadrumaths;

import java.util.Collection;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class PreloadedList<T> extends AbstractTList<T> {
    private static final long serialVersionUID = 1L;
    private final TList<T> cache;
    private TypeReference<T> componentType;

    public PreloadedList(TypeReference<T> componentType, boolean row, int size, TVectorCell<T> it) {
        super(row);
        this.componentType = componentType;
        cache = Maths.list(componentType);
        for (int i = 0; i < size; i++) {
            cache.append(it.get(i));
        }
    }

    @Override
    public TypeReference<T> getComponentType() {
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
        return "PreloadedList{" +
                "cache=" + cache +
                '}';
    }
}
