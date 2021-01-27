package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

public class ReadOnlyMatrix<T> extends AbstractMatrix<T> {
    private static final long serialVersionUID = 1L;
    private final TypeName<T> componentType;
    private final MatrixModel<T> model;

    public ReadOnlyMatrix(TypeName<T> componentType, MatrixModel<T> model) {
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
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public Complex toComplex() {
        return null;
    }

    @Override
    public void resize(int rows, int columns) {
        throw new IllegalArgumentException("Unmodifiable Matrix");
    }
}
