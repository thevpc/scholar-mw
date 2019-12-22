package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public class ReadOnlyTMatrix<T> extends AbstractTMatrix<T> {
    private static final long serialVersionUID = 1L;
    private TypeName<T> componentType;
    private TMatrixModel<T> model;

    public ReadOnlyTMatrix(TypeName<T> componentType, TMatrixModel<T> model) {
        this.componentType = componentType;
        this.model = model;
    }


    @Override
    public T get(int row, int col) {
        return model.get(row, col);
    }

    @Override
    public void set(int row, int col, T val) {
        throw new IllegalArgumentException("Unmodifiable Matrix");
    }

    @Override
    public int getRowCount() {
        return model.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return model.getColumnCount();
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
