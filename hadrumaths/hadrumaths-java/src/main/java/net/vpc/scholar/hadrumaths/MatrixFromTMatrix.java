package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/23/17.
 */
public class MatrixFromTMatrix<T extends Expr> extends AbstractMatrix {
    private TMatrix<T> matrix;

    public MatrixFromTMatrix(TMatrix<T> matrix) {
        this.matrix= matrix;
    }

    @Override
    public Complex get(int row, int col) {
        return matrix.get(row,col).toComplex();
    }

    @Override
    public void set(int row, int col, Complex val) {
        matrix.set(row,col,(T)val);
    }

    @Override
    public int getColumnCount() {
        return matrix.getColumnCount();
    }

    @Override
    public int getRowCount() {
        return matrix.getRowCount();
    }

    @Override
    public void resize(int rows, int columns) {
        matrix.resize(rows,columns);
    }
}
