package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.util.Collection;

public class UpdatableTList<T> extends AbstractTVector<T> implements Cloneable {

    private static final long serialVersionUID = 1L;
    private TVector<T> delegate;
    private TVectorModel<T> model;
    private TypeName<T> componentType;

    public UpdatableTList(TypeName<T> componentType, boolean row, TVectorModel<T> model) {
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
    public TVector<T> appendAll(TVector<T> e) {
        prepareDelegate(e.size());
        delegate.appendAll(e);
        return this;
    }

    @Override
    public TVector<T> append(T e) {
        prepareDelegate(1);
        delegate.append(e);
        return this;
    }

    @Override
    public TVector<T> appendAll(Collection<? extends T> e) {
        prepareDelegate(e.size());
        delegate.appendAll(e);
        return this;
    }

    protected void prepareDelegate(int count) {
        if (delegate == null) {
            int initialSize = model.size();
            delegate = MathsBase.list(getComponentType(), initialSize + count);
            for (int i = 0; i < initialSize; i++) {
                delegate.append(model.get(i));
            }
        }
    }

    @Override
    public TVector<T> sort() {
        return copy().sort();
    }

    @Override
    public TVector<T> removeDuplicates() {
        return copy().removeDuplicates();
    }

}
