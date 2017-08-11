package net.vpc.scholar.hadrumaths;

public class ReadOnlyTVector<T> extends AbstractTVector<T> {
    private TVectorModel<T> model;
    private TypeReference<T> componentType;
    public ReadOnlyTVector(TypeReference<T> componentType, boolean row, TVectorModel<T> model) {
        super(row);
        this.model = model;
        this.componentType = componentType;
    }

    @Override
    public TypeReference<T> getComponentType(){
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
