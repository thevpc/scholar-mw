package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.util.Collection;

public class ReadOnlyTList<T> extends AbstractTVector<T> implements Cloneable {

    private static final long serialVersionUID = 1L;
    private TVectorModel<T> model;
    private TypeName<T> componentType;

    public ReadOnlyTList(TypeName<T> componentType, boolean row, TVectorModel<T> model) {
        super(row);
        this.componentType = componentType;
        this.model = model;
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public int size() {
        return model.size();
    }

    @Override
    public final T get(int index) {
        return model.get(index);
    }

    @Override
    public TVector<T> appendAll(TVector<T> e) {
        throwUnmodifiable();
        return this;
    }

    @Override
    public TVector<T> append(T e) {
        throwUnmodifiable();
        return this;
    }

    @Override
    public TVector<T> appendAll(Collection<? extends T> e) {
        throwUnmodifiable();
        return this;
    }

    @Override
    public TVector<T> sort() {
        return copy().sort();
    }

    @Override
    public TVector<T> removeDuplicates() {
        return copy().removeDuplicates();
    }

    protected void throwUnmodifiable(){
        throw new IllegalArgumentException("Unmodifiable Vector");
    }
}
