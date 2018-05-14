package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by vpc on 4/11/16.
 */
public class ArrayTVector<T> extends AbstractTVector<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T[] elements;
    private transient VectorSpace<T> componentVectorSpace;
    private TypeReference<T> componentType;

    public ArrayTVector(TVector<T> other) throws IOException {
        super(other.isRow());
        this.componentType = other.getComponentType();
        this.componentVectorSpace = other.getComponentVectorSpace();
        elements = ArrayUtils.newArray(this.componentType, other.size());
        System.arraycopy(other.toArray(), 0, elements, 0, elements.length);
    }

    public ArrayTVector(VectorSpace<T> vectorSpace, T[] elements, boolean row) {
        super(row);
        this.elements = elements;
        this.componentVectorSpace = vectorSpace;
        this.componentType = componentVectorSpace.getItemType();
    }

    public ArrayTVector(File file) throws IOException {
        super(false);
        read(file);
    }

    public static <T> TVector<T> Row(VectorSpace<T> vectorSpace, T[] elements) {
        return new ArrayTVector<T>(vectorSpace, elements, true);
    }

    public static <T> TVector<T> Column(VectorSpace<T> vectorSpace, T[] elements) {
        return new ArrayTVector<T>(vectorSpace, elements, false);
    }

    @Override
    public TypeReference<T> getComponentType() {
        return componentType;
    }

    @Override
    public TVector<T> transpose() {
        T[] elements0 = ArrayUtils.newArray(getComponentType(), size());
        System.arraycopy(elements, 0, elements0, 0, elements0.length);
        return new ArrayTVector<T>(getComponentVectorSpace(), elements0, !isRow());
    }


    public TMatrix<T> toMatrix() {
        if (rowType) {
            T[] e2 = ArrayUtils.newArray(getComponentType(), elements.length);
            System.arraycopy(elements, 0, e2, 0, e2.length);
            return Maths.Config.getDefaultMatrixFactory(getComponentType()).newRowMatrix(e2);
        } else {
            return Maths.Config.getDefaultMatrixFactory(getComponentType()).newColumnMatrix(elements);
        }
    }

    public T get(int i) {
        return elements[i];
    }

    public void set(int i, T complex) {
        elements[i] = complex;
    }

    public T[] toArray() {
        return elements;
    }

    public int size() {
        return elements.length;
    }


    public void read(File file) throws IOException {
        TMatrix<T> m = Maths.loadTMatrix(getComponentType(), file);

        elements = m.getColumnCount() == 0 ? m.getRow(0).toArray() : m.getColumn(0).toArray();
        rowType = m.getColumnCount() == 0;
    }

    public void read(BufferedReader reader) throws IOException {
        TMatrix<T> m = Maths.Config.getDefaultMatrixFactory(getComponentType()).newZeros(1, 1);
        m.read(reader);
        elements = m.getColumnCount() == 0 ? m.getRow(0).toArray() : m.getColumn(0).toArray();
        rowType = m.getColumnCount() == 0;
    }

    public T scalarProduct(boolean hermitian, TVector<T> other) {
        int max = Maths.max(elements.length, other.size());
        VectorSpace<T> space = getComponentVectorSpace();
        T d = space.zero();
        for (int i = 0; i < max; i++) {
            d = space.add(d, space.mul(elements[i], other.get(i)));
        }
        return d;
    }

    public T scalarProductAll(boolean hermitian, TVector<T>... other) {
        int max = elements.length;
        for (TVector<T> v : other) {
            int size = v.size();
            if (size > max) {
                max = size;
            }
        }
        VectorSpace<T> space = getComponentVectorSpace();
        T d = space.zero();
        for (int i = 0; i < max; i++) {
            T el = elements[i];
            for (TVector<T> v : other) {
                el = space.mul(el, v.get(i));
            }
            d = space.add(d, el);
        }
        return d;
    }

    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        if(componentVectorSpace==null){
            componentVectorSpace=Maths.getVectorSpace(componentType);
        }
        return componentVectorSpace;
    }

}
