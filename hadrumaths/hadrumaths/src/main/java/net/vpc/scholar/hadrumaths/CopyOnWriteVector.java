package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.util.Collection;

public class CopyOnWriteVector<T> extends AbstractVector<T> implements Cloneable {

    private static final long serialVersionUID = 1L;
    private final VectorModel<T> model;
    private final TypeName<T> componentType;
    private Vector<T> delegate;

    public CopyOnWriteVector(TypeName<T> componentType, boolean row, VectorModel<T> model) {
        super(row);
        this.componentType = componentType;
        this.model = model;
    }

    @Override
    public Vector<T> sort() {
        return copy().sort();
    }

    @Override
    public Vector<T> removeDuplicates() {
        return copy().removeDuplicates();
    }

    @Override
    public Vector<T> set(int index, T t) {
        prepareDelegate(0);
        delegate.set(index, t);
        return this;
    }

    protected void prepareDelegate(int count) {
        if (delegate == null) {
            int initialSize = model.size();
            delegate = createDelegate(initialSize + count);
            for (int i = 0; i < initialSize; i++) {
                delegate.append(model.get(i));
            }
        }
    }

    protected Vector<T> createDelegate(int initialSize) {
        return new ArrayVector<T>(getComponentType(), isRow(), initialSize);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Vector<T> toReadOnly() {
        return new ReadOnlyVector(getComponentType(), isRow(), this);
    }

    @Override
    public Vector<T> toMutable() {
        return this;
    }

    @Override
    public Vector<T> removeAt(int index) {
        prepareDelegate(0);
        delegate.removeAt(index);
        return this;
    }

    @Override
    public Vector<T> appendAt(int index, T e) {
        prepareDelegate(0);
        delegate.appendAt(index, e);
        return this;
    }

    @Override
    public Vector<T> append(T e) {
        prepareDelegate(1);
        delegate.append(e);
        return this;
    }

    @Override
    public Vector<T> appendAll(Collection<? extends T> e) {
        prepareDelegate(e.size());
        delegate.appendAll(e);
        return this;
    }

    @Override
    public Vector<T> appendAll(Vector<T> e) {
        prepareDelegate(e.size());
        delegate.appendAll(e);
        return this;
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public final T get(int index) {
        if (delegate != null) {
            return delegate.get(index);
        }
        return model.get(index);
    }

    @Override
    public int size() {
        if (delegate != null) {
            return delegate.length();
        }
        return model.size();
    }

}
