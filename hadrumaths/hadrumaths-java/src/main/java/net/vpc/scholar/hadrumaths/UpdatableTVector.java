package net.vpc.scholar.hadrumaths;

public class UpdatableTVector<T> extends AbstractTVector<T> {
    private static final long serialVersionUID = 1L;
    private TVectorUpdatableModel<T> model;
    private TypeReference<T> componentType;
    public UpdatableTVector(TypeReference<T> componentType, TVectorUpdatableModel<T> model, boolean row) {
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
        model.set(i,value);
    }

    @Override
    public int size() {
        return model.size();
    }

}
