package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

import java.util.Collection;

public class UpdatableVector<T> extends AbstractVector<T> {
    private static final long serialVersionUID = 1L;
    private final VectorUpdatableModel<T> model;
    private final TypeName<T> componentType;

    public UpdatableVector(TypeName<T> componentType, VectorUpdatableModel<T> model, boolean row) {
        super(row);
        this.model = model;
        this.componentType = componentType;
    }

    @Override
    public Vector<T> set(int i, T value) {
        model.set(i, value);
        return this;
    }

    @Override
    public Vector<T> removeAt(int index) {
        model.removeAt(index);
        return this;
    }

    @Override
    public Vector<T> appendAt(int index, T e) {
        model.appendAt(model.size(),e);
        return this;
    }

    @Override
    public Vector<T> append(T e) {
        model.appendAt(model.size(),e);
        return this;
    }

    @Override
    public Vector<T> appendAll(Collection<? extends T> e) {
        for (T t : e) {
            append(t);
        }
        return this;
    }

    @Override
    public Vector<T> appendAll(Vector<T> e) {
        for (T t : e) {
            append(t);
        }
        return this;
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
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public T get(int i) {
        return model.get(i);
    }

    @Override
    public int size() {
        return model.size();
    }

}
