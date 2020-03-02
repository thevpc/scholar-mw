package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.util.Collection;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class CachedVector<T> extends AbstractVector<T> {

    private static final long serialVersionUID = 1L;
    private ArrayVector<T> delegate;
    private final VectorCell<T> it;
    private boolean updated = false;
    private int modelSize;
    private final TypeName<T> componentType;

    public CachedVector(TypeName<T> componentType, boolean row, VectorModel<T> it) {
        super(row);
        this.componentType = componentType;
        int size = it.size();
        this.modelSize = size;
        this.delegate = new ArrayVector<T>(componentType,row, size);
        for (int i = 0; i < size; i++) {
            this.delegate.set(i,null);
        }
        this.it = it;
    }

    public CachedVector(TypeName<T> componentType, boolean row, int size, VectorCell<T> it) {
        super(row);
        this.componentType = componentType;
        this.modelSize = size;
        this.delegate = new ArrayVector<T>(componentType,row,size);
        for (int i = 0; i < size; i++) {
            this.delegate.set(i,null);
        }
        this.it = it;
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public T get(int index) {
        if (index < this.delegate.size() && !updated) {
            T old = delegate.get(index);
            if (old == null) {
                old = it.get(index);
                delegate.set(index, old);
            }
            return old;
        } else {
            return delegate.get(index);
        }
    }

    @Override
    public int size() {
        return this.delegate.size();
    }

    @Override
    public Vector<T> set(int index, T e) {
        this.delegate.set(index, e);
        return this;
    }

    @Override
    public Vector<T> appendAll(Vector<T> e) {
        setUpdated();
        this.delegate.appendAll(e);
        return this;
    }

    protected void setUpdated() {
        if (!updated) {
            updated = true;
            for (int i = 0; i < modelSize; i++) {
                if (delegate.get(i) == null) {
                    delegate.set(i, (it.get(i)));
                }
            }
        }
    }

    @Override
    public Vector<T> append(T e) {
        setUpdated();
        delegate.append(e);
        return this;
    }

    @Override
    public Vector<T> appendAll(Collection<? extends T> e) {
        setUpdated();
        delegate.appendAll(e);
        return this;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Vector<T> toReadOnly() {
        return new ReadOnlyVector<>(getComponentType(), isRow(),this);
    }

    @Override
    public Vector<T> toMutable() {
        return this;
    }

    @Override
    public Vector<T> appendAt(int index, T e) {
        setUpdated();
        return delegate.appendAt(index, e);
    }

    @Override
    public Vector<T> removeAt(int index) {
        setUpdated();
        return delegate.removeAt(index);
    }
//    @Override
//    public String toString() {
//        return "CachedVector{"
//                + "items=" + it
//                + ", size=" + size
//                + '}';
//    }

    @Override
    public Vector<T> sort() {
        return copy().sort();
    }

    @Override
    public Vector<T> removeDuplicates() {
        return copy().removeDuplicates();
    }
}
