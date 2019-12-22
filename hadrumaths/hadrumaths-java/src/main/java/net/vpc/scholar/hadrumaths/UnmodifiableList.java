package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class UnmodifiableList<T> extends AbstractTVector<T> implements Cloneable {

    private static final long serialVersionUID = 1L;
    private final int size;
    private final TVectorCell<T> vectorCell;
    private TypeName<T> componentType;

    public UnmodifiableList(TypeName<T> componentType, boolean row, int size, TVectorCell<T> vectorCell) {
        super(row);
        this.componentType = componentType;
        this.size = size;
        this.vectorCell = vectorCell;
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        return vectorCell.get(index);
    }

    @Override
    public TVector<T> set(int index, T e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public TVector<T> sort() {
        return copy().sort();
    }

    @Override
    public TVector<T> removeDuplicates() {
        return copy().removeDuplicates();
    }

//    @Override
//    public String toString() {
//        return "UnmodifiableList{" +
//                "size=" + size +
//                ", items=" + it +
//                '}';
//    }
}
