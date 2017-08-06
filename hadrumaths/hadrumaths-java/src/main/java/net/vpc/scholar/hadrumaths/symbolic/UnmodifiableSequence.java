package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.TVectorCell;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class UnmodifiableSequence<T> extends AbstractTList<T> implements Cloneable {
    private final int size;
    private final TVectorCell<T> it;

    public UnmodifiableSequence(Class<T> componentType,int size, TVectorCell<T> it) {
        super(componentType);
        this.size = size;
        this.it = it;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        return it.get(index);
    }

    @Override
    public void set(int index, T e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public String toString() {
        return "UnmodifiableSequence{" +
                "size=" + size +
                ", items=" + it +
                '}';
    }

}
