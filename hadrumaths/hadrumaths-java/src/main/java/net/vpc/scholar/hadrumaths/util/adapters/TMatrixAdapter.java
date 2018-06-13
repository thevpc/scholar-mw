package net.vpc.scholar.hadrumaths.util.adapters;

import net.vpc.common.util.Converter;
import net.vpc.scholar.hadrumaths.AbstractTMatrix;
import net.vpc.scholar.hadrumaths.TMatrix;
import net.vpc.common.util.TypeReference;
import net.vpc.scholar.hadrumaths.VectorSpace;

public class TMatrixAdapter<R, T> extends AbstractTMatrix<T> {
    private static final long serialVersionUID = 1L;
    protected TMatrix<R> base;
    private TypeReference<T> componentType;
    private Converter<R, T> converterTo;
    private Converter<T, R> converterFrom;

    public TMatrixAdapter(TMatrix<R> base, TypeReference<T> componentType) {
        this.base = base;
        this.componentType = componentType;
        VectorSpace<R> vs = base.getComponentVectorSpace();
        converterTo = vs.getConverterTo(componentType);
        converterFrom = vs.getConverterFrom(componentType);
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
    public TypeReference<T> getComponentType() {
        return componentType;
    }

    @Override
    public <R> TMatrix<R> to(TypeReference<R> other) {
        if (other.equals(getComponentType())) {
            return (TMatrix<R>) this;
        }
        return base.to(other);
    }
}
