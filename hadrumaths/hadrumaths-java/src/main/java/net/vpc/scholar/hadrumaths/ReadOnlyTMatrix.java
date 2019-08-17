package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public class ReadOnlyTMatrix<T> extends AbstractTMatrix<T> {
    private static final long serialVersionUID = 1L;
    private TypeName<T> componentType;
    private TMatrixModel<T> f;

    public ReadOnlyTMatrix(TypeName<T> componentType, TMatrixModel<T> f) {
        this.componentType = componentType;
        this.f = f;
    }


    @Override
    public T get(int row, int col) {
        return f.get(row, col);
    }

    @Override
    public void set(int row, int col, T val) {
        throw new IllegalArgumentException("Unmodifiable Matrix");
    }

    @Override
    public int getRowCount() {
        return f.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return f.getColumnCount();
    }


    @Override
    public Complex toComplex() {
        return null;
    }

    @Override
    public void resize(int rows, int columns) {
        throw new IllegalArgumentException("Unmodifiable Matrix");
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }
}
