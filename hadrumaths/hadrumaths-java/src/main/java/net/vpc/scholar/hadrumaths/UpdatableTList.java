package net.vpc.scholar.hadrumaths;

import java.util.Collection;

public class UpdatableTList<T> extends AbstractTList<T> implements Cloneable {
    private static final long serialVersionUID = 1L;
    private TList<T> delegate;
    private TVectorModel<T> model;
    private TypeReference<T> componentType;

    public UpdatableTList(TypeReference<T> componentType, boolean row, TVectorModel<T> model) {
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
        if (delegate != null) {
            return delegate.length();
        }
        return model.size();
    }

    @Override
    public final T get(int index) {
        if (delegate != null) {
            return (T) delegate.get(index);
        }
        return model.get(index);
    }

    @Override
    public void appendAll(TVector<T> e) {
        prepareDelegate(e.size());
        delegate.appendAll(e);
    }

    @Override
    public void append(T e) {
        prepareDelegate(1);
        delegate.append(e);
    }

    @Override
    public void appendAll(Collection<? extends T> e) {
        prepareDelegate(e.size());
        delegate.appendAll(e);
    }

    protected void prepareDelegate(int count) {
        if (delegate == null) {
            int initialSize = model.size();
            delegate = Maths.list(getComponentType(), initialSize + count);
            for (int i = 0; i < initialSize; i++) {
                delegate.append(model.get(i));
            }
        }
    }
}
