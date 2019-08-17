package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public class ReadOnlyTVector<T> extends AbstractTVector<T> {
    private static final long serialVersionUID = 1L;
    private TVectorModel<T> model;
    private TypeName<T> componentType;

    public ReadOnlyTVector(TypeName<T> componentType, boolean row, TVectorModel<T> model) {
        super(row);
        this.model = model;
        this.componentType = componentType;
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
    public void set(int i, T value) {
        throw new IllegalArgumentException("Read Only");
    }

    @Override
    public int size() {
        return model.size();
    }

}
