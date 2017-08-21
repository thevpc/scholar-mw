package net.vpc.scholar.hadrumaths;

import java.util.Collection;

public class ReadOnlyTList<T> extends AbstractTList<T> implements Cloneable {
    private static final long serialVersionUID = 1L;
    private TVectorModel<T> model;
    private TypeReference<T> componentType;

    public ReadOnlyTList(TypeReference<T> componentType, boolean row, TVectorModel<T> model) {
        super(row);
        this.componentType = componentType;
        this.model = model;
    }

    @Override
    public TypeReference<T> getComponentType() {
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
    public void appendAll(TVector<T> e) {
        throw new IllegalArgumentException("Unmodifiable List");
    }

    @Override
    public void append(T e) {
        throw new IllegalArgumentException("Unmodifiable List");
    }

    @Override
    public void appendAll(Collection<? extends T> e) {
        throw new IllegalArgumentException("Unmodifiable List");
    }
}
