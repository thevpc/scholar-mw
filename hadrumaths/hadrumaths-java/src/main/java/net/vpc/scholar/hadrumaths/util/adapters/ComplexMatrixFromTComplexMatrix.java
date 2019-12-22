package net.vpc.scholar.hadrumaths.util.adapters;

import net.vpc.common.util.Converter;
import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 3/23/17.
 */
public class ComplexMatrixFromTComplexMatrix<R> extends AbstractComplexMatrix implements ComplexMatrix {
    private static final long serialVersionUID = 1L;

    protected TMatrix<R> base;
    private Converter<R, Complex> converterTo;
    private Converter<Complex, R> converterFrom;

    public ComplexMatrixFromTComplexMatrix(TMatrix<R> base) {
        this.base = base;
        VectorSpace<R> vs = base.getComponentVectorSpace();
        converterTo = vs.getConverterTo(MathsBase.$COMPLEX);
        converterFrom = vs.getConverterFrom(MathsBase.$COMPLEX);
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
    public TypeName<Complex> getComponentType() {
        return MathsBase.$COMPLEX;
    }

    @Override
    public <R> TMatrix<R> to(TypeName<R> other) {
        if (other.equals(getComponentType())) {
            return (TMatrix<R>) this;
        }
        return base.to(other);
    }


}
