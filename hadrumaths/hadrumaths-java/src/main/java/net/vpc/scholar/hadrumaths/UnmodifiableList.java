package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class UnmodifiableList<T> extends AbstractTList<T> implements Cloneable {
    private static final long serialVersionUID = 1L;
    private final int size;
    private final TVectorCell<T> vectorCell;
    private TypeReference<T> componentType;

    public UnmodifiableList(TypeReference<T> componentType, boolean row, int size, TVectorCell<T> vectorCell) {
        super(row);
        this.componentType = componentType;
        this.size = size;
        this.vectorCell = vectorCell;
    }

    @Override
    public TypeReference<T> getComponentType() {
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
    public void set(int index, T e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

//    @Override
//    public String toString() {
//        return "UnmodifiableList{" +
//                "size=" + size +
//                ", items=" + it +
//                '}';
//    }

}
