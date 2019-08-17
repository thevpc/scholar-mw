package net.vpc.scholar.hadrumaths.util.adapters;

import net.vpc.common.util.Converter;
import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.AbstractTMatrix;
import net.vpc.scholar.hadrumaths.TMatrix;
import net.vpc.scholar.hadrumaths.VectorSpace;

public class TMatrixAdapter<R, T> extends AbstractTMatrix<T> {
    private static final long serialVersionUID = 1L;
    protected TMatrix<R> base;
    private TypeName<T> componentType;
    private Converter<R, T> converterTo;
    private Converter<T, R> converterFrom;

    public TMatrixAdapter(TMatrix<R> base, TypeName<T> componentType) {
        this.base = base;
        this.componentType = componentType;
        VectorSpace<R> vs = base.getComponentVectorSpace();
        converterTo = vs.getConverterTo(componentType);
        converterFrom = vs.getConverterFrom(componentType);
    }

    @Override
    public void set(T[][] elements) {
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[i].length; j++) {
                set(i,j,elements[i][j]);
            }
        }
    }

    @Override
    public T get(int row, int col) {
        return converterTo.convert(base.get(row, col));
    }

    @Override
    public void set(int row, int col, T val) {
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
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public <R> TMatrix<R> to(TypeName<R> other) {
        if (other.equals(getComponentType())) {
            return (TMatrix<R>) this;
        }
        return base.to(other);
    }
}
