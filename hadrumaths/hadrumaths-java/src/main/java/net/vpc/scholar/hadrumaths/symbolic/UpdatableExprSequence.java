package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.Collection;

public class UpdatableExprSequence<T> extends AbstractTList<T> implements Cloneable {
    private TList<T> delegate;
    private TVectorModel<T> model;

    public UpdatableExprSequence(Class<T> componentType, TVectorModel<T> model) {
        super(componentType);
        this.model=model;
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

    protected void prepareDelegate(int count){
        if (delegate == null) {
            int initialSize = model.size();
            delegate = Maths.listOf(getComponentType(), initialSize+count);
            for (int i = 0; i < initialSize; i++) {
                delegate.add(model.get(i));
            }
        }
    }
    @Override
    public TList<T> copy() {
        if (delegate != null) {
            return delegate.copy();
        }
        try {
            return (TList) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Unsupported Clone");
        }
    }
}
