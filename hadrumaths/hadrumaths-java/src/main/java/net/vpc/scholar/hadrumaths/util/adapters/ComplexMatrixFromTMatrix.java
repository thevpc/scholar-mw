package net.vpc.scholar.hadrumaths.util.adapters;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.Converter;

/**
 * Created by vpc on 3/23/17.
 */
public class ComplexMatrixFromTMatrix<R> extends AbstractMatrix implements Matrix {
    private static final long serialVersionUID = 1L;

    protected TMatrix<R> base;
    private Converter<R, Complex> converterTo;
    private Converter<Complex, R> converterFrom;

    public ComplexMatrixFromTMatrix(TMatrix<R> base) {
        this.base = base;
        VectorSpace<R> vs = base.getComponentVectorSpace();
        converterTo = vs.getConverterTo(Maths.$COMPLEX);
        converterFrom = vs.getConverterFrom(Maths.$COMPLEX);
    }

    @Override
    public Complex get(int row, int col) {
        return converterTo.convert(base.get(row, col));
    }

    @Override
    public void set(int row, int col, Complex val) {
        base.set(row, col, converterFrom.convert(val));
    }

    @Override
    public int getRowCount() {
        return base.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return base.getColumnCount();
    }

    @Override
    public TypeReference<Complex> getComponentType() {
        return Maths.$COMPLEX;
    }

    @Override
    public <R> TMatrix<R> to(TypeReference<R> other) {
        if (other.equals(getComponentType())) {
            return (TMatrix<R>) this;
        }
        return base.to(other);
    }


}
