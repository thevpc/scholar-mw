package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

import java.util.Collection;

public class ReadOnlyVector<T> extends AbstractVector<T> implements Cloneable {

    private static final long serialVersionUID = 1L;
    private final VectorModel<T> model;
    private final TypeName<T> componentType;

    public ReadOnlyVector(TypeName<T> componentType, boolean row, VectorModel<T> model) {
        super(row);
        this.componentType = componentType;
        this.model = model;
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public final T get(int index) {
        return model.get(index);
    }

    @Override
    public int size() {
        return model.size();
    }


    @Override
    public Vector<T> appendAll(Vector<T> e) {
        throw throwReadOnly();
    }

    @Override
    public Vector<T> append(T e) {
        throw throwReadOnly();
    }

    @Override
    public Vector<T> appendAll(Collection<? extends T> e) {
        throw throwReadOnly();
    }

    @Override
    public Vector<T> sort() {
        return copy().sort();
    }

    @Override
    public Vector<T> removeDuplicates() {
        return copy().removeDuplicates();
    }

    protected IllegalArgumentException throwReadOnly() {
        throw new IllegalArgumentException("Unmodifiable Vector");
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Vector<T> toReadOnly() {
        return this;
    }

    @Override
    public Vector<T> toMutable() {
        return new ArrayVector<T>(getComponentType(),isRow(),this);
    }

    @Override
    public Vector<T> removeAt(int index) {
        throw throwReadOnly();
    }

    @Override
    public Vector<T> appendAt(int index, T e) {
        throw throwReadOnly();
    }
}
