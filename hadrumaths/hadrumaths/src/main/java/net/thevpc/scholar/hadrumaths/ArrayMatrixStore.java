package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.reflect.NTypeName;
import net.thevpc.nuts.reflect.NTypeNameDomain;
import net.thevpc.nuts.reflect.NTypeNamePlatformDomain;

import java.lang.reflect.Array;

public class ArrayMatrixStore<T> implements MatrixStore<T> {
    private final T[][] items;

    public ArrayMatrixStore(int rows, int cols, VectorSpace clazz) {
        this(rows, cols, NTypeNamePlatformDomain.of().getTypeClass(clazz.getItemType()));
    }

    public ArrayMatrixStore(int rows, int cols, Class clazz) {
        items = (T[][]) Array.newInstance(clazz, rows, cols);
    }

    public ArrayMatrixStore(int rows, int cols, NTypeName clazz) {
        this(rows, cols, NTypeNamePlatformDomain.of().getTypeClass(clazz));
    }

    public ArrayMatrixStore(T[][] items) {
        this.items = items;
    }

    @Override
    public T set(int row, int column, T value) {
        return items[row][column] = value;
    }

    @Override
    public int getColumnCount() {
        return items.length;
    }

    @Override
    public int getRowCount() {
        return items.length == 0 ? 0 : items[0].length;
    }

    @Override
    public T get(int row, int column) {
        return items[row][column];
    }
}
