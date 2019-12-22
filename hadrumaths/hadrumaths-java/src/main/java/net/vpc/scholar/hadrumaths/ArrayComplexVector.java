package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by vpc on 4/11/16.
 */
public class ArrayComplexVector extends AbstractComplexVector implements Serializable {
    private static final long serialVersionUID = 1L;
    private Complex[] elements;

    public ArrayComplexVector(TVector<Complex> other) {
        super(other.isRow());
        elements = new Complex[other.size()];
        System.arraycopy(other.toArray(), 0, elements, 0, elements.length);
    }

    public ArrayComplexVector(File file) throws IOException {
        super(false);
        read(file);
    }

    public ArrayComplexVector(Complex[] elements, boolean row) {
        super(row);
        this.elements = elements;
    }

    public static final ComplexVector Row(Complex[] elements) {
        return new ArrayComplexVector(elements, true);
    }

    public static final ComplexVector Column(Complex[] elements) {
        return new ArrayComplexVector(elements, false);
    }

    @Override
    public ComplexVector transpose() {
        Complex[] elements0 = new Complex[size()];
        System.arraycopy(elements, 0, elements0, 0, elements0.length);
        return new ArrayComplexVector(elements0, !isRow());
    }

    public ComplexMatrix toMatrix() {
        if (rowType) {
            Complex[] e2 = new Complex[elements.length];
            System.arraycopy(elements, 0, e2, 0, e2.length);
            return MathsBase.rowMatrix(e2);
        } else {
            return MathsBase.columnMatrix(elements);
        }
    }

    public Complex get(int i) {
        return elements[i];
    }

    public ComplexVector set(int i, Complex complex) {
        elements[i] = complex;
        return this;
    }

    public Complex[] toArray() {
        return elements;
    }

    public int size() {
        return elements.length;
    }


    public void read(File file) throws IOException {
        ComplexMatrix m = MathsBase.loadMatrix(file);

        elements = m.getColumnCount() == 0 ? m.getRow(0).toArray() : m.getColumn(0).toArray();
        rowType = m.getColumnCount() == 0;
    }

    public void read(BufferedReader reader) throws IOException {
        ComplexMatrix m = MathsBase.zerosMatrix(1, 1);
        m.read(reader);
        elements = m.getColumnCount() == 0 ? m.getRow(0).toArray() : m.getColumn(0).toArray();
        rowType = m.getColumnCount() == 0;
    }

    public Complex scalarProduct(ComplexVector other) {
        int max = Math.max(elements.length, other.size());
        MutableComplex d = new MutableComplex();
        for (int i = 0; i < max; i++) {
            d.add(elements[i].mul(other.get(i)));
        }
        return d.toComplex();
    }

    public Complex scalarProductAll(ComplexVector... other) {
        int max = elements.length;
        for (ComplexVector v : other) {
            int size = v.size();
            if (size > max) {
                max = size;
            }
        }
        MutableComplex d = new MutableComplex();
        for (int i = 0; i < max; i++) {
            MutableComplex c = new MutableComplex(1, 0);
            c.mul(elements[i]);
            for (ComplexVector v : other) {
                c.mul(v.get(i));
            }
            d.add(c.toComplex());
        }
        return d.toComplex();
    }


    @Override
    public TypeName<Complex> getComponentType() {
        return MathsBase.$COMPLEX;
    }

    @Override
    public VectorSpace<Complex> getComponentVectorSpace() {
        return MathsBase.COMPLEX_VECTOR_SPACE;
    }
}
