package net.vpc.scholar.hadrumaths.util.adapters;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.*;

import java.util.function.Function;

/**
 * Created by vpc on 3/23/17.
 */
public class ComplexMatrixFromComplexMatrix<R> extends AbstractComplexMatrix implements ComplexMatrix {
    private static final long serialVersionUID = 1L;

    protected Matrix<R> base;
    private final Function<R, Complex> converterTo;
    private final Function<Complex, R> converterFrom;

    public ComplexMatrixFromComplexMatrix(Matrix<R> base) {
        this.base = base;
        VectorSpace<R> vs = base.getComponentVectorSpace();
        converterTo = vs.getConverterTo(Maths.$COMPLEX);
        converterFrom = vs.getConverterFrom(Maths.$COMPLEX);
    }

    @Override
    public Complex get(int row, int col) {
        return converterTo.apply(base.get(row, col));
    }

    @Override
    public void set(int row, int col, Complex val) {
        base.set(row, col, converterFrom.apply(val));
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
    public <R> Matrix<R> to(TypeName<R> other) {
        if (other.equals(getComponentType())) {
            return (Matrix<R>) this;
        }
        return base.to(other);
    }

    @Override
    public TypeName<Complex> getComponentType() {
        return Maths.$COMPLEX;
    }


}
