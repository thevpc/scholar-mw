package net.vpc.scholar.hadrumaths.util.adapters;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.AbstractMatrix;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.VectorSpace;

import java.util.function.Function;

public class MatrixAdapter<R, T> extends AbstractMatrix<T> {
    private static final long serialVersionUID = 1L;
    protected Matrix<R> base;
    private final TypeName<T> componentType;
    private final Function<R, T> converterTo;
    private final Function<T, R> converterFrom;

    public MatrixAdapter(Matrix<R> base, TypeName<T> componentType) {
        this.base = base;
        this.componentType = componentType;
        VectorSpace<R> vs = base.getComponentVectorSpace();
        converterTo = vs.getConverterTo(componentType);
        converterFrom = vs.getConverterFrom(componentType);
    }

    @Override
    public T get(int row, int col) {
        return converterTo.apply(base.get(row, col));
    }

    @Override
    public void set(int row, int col, T val) {
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
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public <R> Matrix<R> to(TypeName<R> other) {
        if (other.equals(getComponentType())) {
            return (Matrix<R>) this;
        }
        return base.to(other);
    }

    @Override
    public void set(T[][] elements) {
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[i].length; j++) {
                set(i, j, elements[i][j]);
            }
        }
    }
}
