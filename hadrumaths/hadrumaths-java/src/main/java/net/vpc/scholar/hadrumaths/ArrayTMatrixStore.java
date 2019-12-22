package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.lang.reflect.Array;

public class ArrayTMatrixStore<T> implements TMatrixStore<T>{
    private T[][] items;

    public ArrayTMatrixStore(int rows, int cols, VectorSpace clazz) {
        this(rows,cols,clazz.getItemType().getTypeClass());
    }

    public ArrayTMatrixStore(int rows, int cols, TypeName clazz) {
        this(rows,cols,clazz.getTypeClass());
    }

    public ArrayTMatrixStore(int rows,int cols,Class clazz) {
        items=(T[][]) Array.newInstance(clazz,rows,cols);
    }
    public ArrayTMatrixStore(T[][] items) {
        this.items = items;
    }

    @Override
    public T set(int row, int column, T value) {
        return items[row][column]=value;
    }

    @Override
    public int getColumnCount() {
        return items.length;
    }

    @Override
    public int getRowCount() {
        return items.length==0?0:items[0].length;
    }

    @Override
    public T get(int row, int column) {
        return items[row][column];
    }
}
