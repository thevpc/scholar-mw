package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * @author taha.bensalah@gmail.com on 7/21/16.
 */
public class TransposedVector<T> extends CopyOnWriteVector<T> {
    private static final long serialVersionUID = 1L;
    private final Vector<T> other;

    public TransposedVector(Vector<T> other) {
        super(other.getComponentType(), !other.isRow(), new VectorModel<T>() {
            @Override
            public int size() {
                return other.size();
            }

            @Override
            public T get(int index) {
                return other.get(index);
            }
        });
        this.other = other;
    }

    @Override
    public Vector<T> transpose() {
        return other;
    }

    @Override
    public Vector<T> set(int i, T complex) {
        other.set(i, complex);
        return this;
    }

    @Override
    public TypeName<T> getComponentType() {
        return other.getComponentType();
    }

}
