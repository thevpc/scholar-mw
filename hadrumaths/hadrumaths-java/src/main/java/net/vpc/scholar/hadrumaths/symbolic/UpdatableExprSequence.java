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
        if (delegate == null) {
            delegate = Maths.listOf(getComponentType());
        }
        delegate.appendAll(e);
    }

    @Override
    public void append(T e) {
        if (delegate == null) {
            delegate = Maths.listOf(getComponentType());
        }
        delegate.append(e);
    }

    @Override
    public void appendAll(Collection<? extends T> e) {
        if (delegate == null) {
            delegate = Maths.listOf(getComponentType());
        }
        delegate.appendAll(e);
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
