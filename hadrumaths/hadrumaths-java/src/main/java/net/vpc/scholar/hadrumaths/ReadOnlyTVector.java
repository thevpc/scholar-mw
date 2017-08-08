package net.vpc.scholar.hadrumaths;

public class ReadOnlyTVector<T> extends AbstractTVector<T> {
    private TVectorModel<T> model;
    private Class<T> componentType;
    public ReadOnlyTVector(Class<T> componentType, boolean row, TVectorModel<T> model) {
        super(row);
        this.model = model;
        this.componentType = componentType;
    }

    @Override
    public Class<T> getComponentType(){
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
