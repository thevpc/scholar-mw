package net.vpc.scholar.hadrumaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by vpc on 4/11/16.
 */
public class ArrayVector extends AbstractVector implements Serializable {
    private static final long serialVersionUID = 1L;
    private Complex[] elements;

    public ArrayVector(TVector<Complex> other) {
        super(other.isRow());
        elements = new Complex[other.size()];
        System.arraycopy(other.toArray(), 0, elements, 0, elements.length);
    }

    public ArrayVector(File file) throws IOException {
        super(false);
        read(file);
    }

    public ArrayVector(Complex[] elements, boolean row) {
        super(row);
        this.elements = elements;
    }

    public static final Vector Row(Complex[] elements) {
        return new ArrayVector(elements, true);
    }

    public static final Vector Column(Complex[] elements) {
        return new ArrayVector(elements, false);
    }

    @Override
    public Vector transpose() {
        Complex[] elements0 = new Complex[size()];
        System.arraycopy(elements, 0, elements0, 0, elements0.length);
        return new ArrayVector(elements0, !isRow());
    }

    public Matrix toMatrix() {
        if (rowType) {
            Complex[] e2 = new Complex[elements.length];
            System.arraycopy(elements, 0, e2, 0, e2.length);
            return Maths.rowMatrix(e2);
        } else {
            return Maths.columnMatrix(elements);
        }
    }

    public Complex get(int i) {
        return elements[i];
    }

    public void set(int i, Complex complex) {
        elements[i] = complex;
    }

    public Complex[] toArray() {
        return elements;
    }

    public int size() {
        return elements.length;
    }


    public void read(File file) throws IOException {
        Matrix m = Maths.loadMatrix(file);

        elements = m.getColumnCount() == 0 ? m.getRow(0).toArray() : m.getColumn(0).toArray();
        rowType = m.getColumnCount() == 0;
    }

    public void read(BufferedReader reader) throws IOException {
        Matrix m = Maths.zerosMatrix(1, 1);
        m.read(reader);
        elements = m.getColumnCount() == 0 ? m.getRow(0).toArray() : m.getColumn(0).toArray();
        rowType = m.getColumnCount() == 0;
    }

    public Complex scalarProduct(Vector other) {
        int max = Math.max(elements.length, other.size());
        MutableComplex d = new MutableComplex();
        for (int i = 0; i < max; i++) {
            d.add(elements[i].mul(other.get(i)));
        }
        return d.toComplex();
    }

    public Complex scalarProductAll(Vector... other) {
        int max = elements.length;
        for (Vector v : other) {
            int size = v.size();
            if (size > max) {
                max = size;
            }
        }
        MutableComplex d = new MutableComplex();
        for (int i = 0; i < max; i++) {
            MutableComplex c = new MutableComplex(1, 0);
            c.mul(elements[i]);
            for (Vector v : other) {
                c.mul(v.get(i));
            }
            d.add(c.toComplex());
        }
        return d.toComplex();
    }


    @Override
    public TypeReference<Complex> getComponentType() {
        return Maths.$COMPLEX;
    }

    @Override
    public VectorSpace<Complex> getComponentVectorSpace() {
        return Maths.COMPLEX_VECTOR_SPACE;
    }
}
