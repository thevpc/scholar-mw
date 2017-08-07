package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.TList;
import net.vpc.scholar.hadrumaths.TVector;
import net.vpc.scholar.hadrumaths.TVectorModel;

import java.util.Collection;

public class UpdatableTList<T> extends AbstractTList<T> implements Cloneable {
    private TList<T> delegate;
    private TVectorModel<T> model;
    private Class<T> componentType;

    public UpdatableTList(Class<T> componentType, boolean row, TVectorModel<T> model) {
        super(row);
        this.componentType = componentType;
        this.model = model;
    }

    @Override
    public Class<T> getComponentType() {
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
            delegate = Maths.listOf(getComponentType(), initialSize + count);
            for (int i = 0; i < initialSize; i++) {
                delegate.append(model.get(i));
            }
        }
    }
}
