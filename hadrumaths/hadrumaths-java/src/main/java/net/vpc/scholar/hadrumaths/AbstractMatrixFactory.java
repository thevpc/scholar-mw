package net.vpc.scholar.hadrumaths;

import java.io.File;
import java.io.IOException;

/**
 * Created by vpc on 2/5/15.
 */
public abstract class AbstractMatrixFactory implements MatrixFactory{
    @Override
    public  Matrix newZeros(TMatrix<Complex> other) {
        return newConstant(other.getRowCount(), other.getColumnCount(), Complex.ZERO);
    }

    @Override
    public  Matrix newConstant(int dim, Complex value) {
        return newConstant(dim, dim, value);
    }

    @Override
    public  Matrix newOnes(int dim) {
        return newConstant(dim, dim, Complex.ONE);
    }

    @Override
    public  Matrix newOnes(int rows, int cols) {
        return newConstant(rows, cols, Complex.ONE);
    }

    @Override
    public  Matrix newConstant(int rows, int cols, Complex value) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        Matrix e = newMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, value);
            }
        }
        return e;
    }

    @Override
    public Matrix newMatrix(TMatrix<Complex> other) {
        int rows = other.getRowCount();
        int cols = other.getColumnCount();
        Matrix e = newMatrix(rows, cols);
        e.set(other);
//        for (int i = 0; i < rows; i++) {
//            TVector<Complex> row = other.getRow(i);
//            for (int j = 0; j < cols; j++) {
//                e.set(i, j, row.get(j));
//            }
//        }
        return e;
    }

    @Override
    public  Matrix newZeros(int dim) {
        return newConstant(dim, dim, Complex.ZERO);
    }

    @Override
    public  Matrix newZeros(int rows, int cols) {
        return newConstant(rows, cols, Complex.ZERO);
    }

    @Override
    public  Matrix newIdentity(TMatrix<Complex> c) {
        return newIdentity(c.getRowCount(), c.getColumnCount());
    }

    @Override
    public  Matrix newNaN(int dim) {
        return newNaN(dim, dim);
    }

    @Override
    public  Matrix newNaN(int rows, int cols) {
        return newConstant(rows, cols, Complex.NaN);
    }

    @Override
    public  Matrix newIdentity(int dim) {
        return newIdentity(dim, dim);
    }

    @Override
    public  Matrix newIdentity(int rows, int cols) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        Matrix e = newMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, (i == j) ? Complex.ONE : Complex.ZERO);
            }
        }
        return e;
    }

    @Override
    public  Matrix newMatrix(String string) {
        Matrix matrix = newMatrix(1, 1);
        matrix.read(string);
        return matrix;
    }

    @Override
    public  Matrix newMatrix(Complex[][] complex) {
        int rows = complex.length;
        int cols = complex[0].length;
        Matrix e = newMatrix(rows, cols);
        e.set(complex);
        return e;
    }

    @Override
    public  Matrix newMatrix(MutableComplex[][] complex) {
        int rows = complex.length;
        int cols = complex[0].length;
        Matrix e = newMatrix(rows, cols);
        e.set(complex);
        return e;
    }

    @Override
    public Matrix newMatrix(double[][] complex) {
        int rows = complex.length;
        int cols = complex[0].length;
        Matrix e = newMatrix(rows, cols);
        e.set(complex);
        return e;
    }

    @Override
    public Matrix newMatrix(int rows, int cols, MatrixCell cellFactory) {
        return newMatrix(rows,cols, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public Matrix newMatrix(int rows, int columns, CellIteratorType it, MatrixCell item) {
        Matrix e = newMatrix(rows, columns);
        switch (it) {
            case FULL: {
                e.set(e);
                break;
            }
            case DIAGONAL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        e.set(i, j, Complex.ZERO);
                    }
                    e.set(i, i, item.get(i, i));
                    for (int j = i + 1; j < columns; j++) {
                        e.set(i, j, Complex.ZERO);
                    }
                }
                break;
            }
            case SYMETRIC: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        e.set(i, j, e.get(j,i));
                    }
                    for (int j = i; j < columns; j++) {
                        e.set(i, j, item.get(i, j));
                    }
                }
                break;
            }
            case HERMITIAN: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        e.set(i, j, e.get(j,i).conj());
                    }
                    for (int j = i; j < columns; j++) {
                        e.set(i, j, item.get(i, j));
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + it);
            }
        }
        return e;
    }

    @Override
    public  Matrix newColumnMatrix(final Complex... values) {
        Complex[][] d = new Complex[values.length][1];
        for (int i = 0; i < d.length; i++) {
            d[i][0] = values[i];
        }
        return newMatrix(d);
    }

    @Override
    public  Matrix newRowMatrix(final Complex... values) {
        return newMatrix(new Complex[][]{values});
    }

    @Override
    public  Matrix newColumnMatrix(int rows, final VectorCell cellFactory) {
        return newMatrix(rows, 1, CellIteratorType.FULL, new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public  Matrix newRowMatrix(int columns, final VectorCell cellFactory) {
        return newMatrix(1, columns, CellIteratorType.FULL, new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(column);
            }
        });
    }

    @Override
    public Matrix newSymmetric(int rows, int cols, MatrixCell cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.SYMETRIC, cellFactory);
    }

    @Override
    public Matrix newHermitian(int rows, int cols, MatrixCell cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.HERMITIAN, cellFactory);
    }

    @Override
    public Matrix newDiagonal(int rows, int cols, MatrixCell cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.DIAGONAL, cellFactory);
    }

    @Override
    public Matrix newDiagonal(int rows, final VectorCell cellFactory) {
        return newMatrix(rows, rows, CellIteratorType.DIAGONAL, new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public Matrix newDiagonal(final Complex... c) {
        return newMatrix(c.length, c.length, CellIteratorType.DIAGONAL, new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return c[row];
            }
        });
    }

    @Override
    public  Matrix newMatrix(int dim, MatrixCell cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public  Matrix newSymmetric(int dim, MatrixCell cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.SYMETRIC, cellFactory);
    }

    @Override
    public  Matrix newHermitian(int dim, MatrixCell cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.HERMITIAN, cellFactory);
    }

    @Override
    public Matrix newDiagonal(int dim, MatrixCell cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.DIAGONAL, cellFactory);
    }

    @Override
    public Matrix newRandomReal(int m, int n) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Math.random(),0.0);
            }
        }
        return A;
    }

    @Override
    public Matrix newRandomReal(int m, int n, int min, int max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, (((int) (Math.random() * (max - min))) + min),0);
            }
        }
        return A;
    }

    @Override
    public Matrix newRandomReal(int m, int n, double min, double max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, (Math.random() * (max - min) + min),0);
            }
        }
        return A;
    }

    @Override
    public Matrix newRandomImag(int m, int n, double min, double max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, 0,(Math.random() * (max - min) + min));
            }
        }
        return A;
    }

    @Override
    public Matrix newRandomImag(int m, int n, int min, int max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, 0,(((int) (Math.random() * (max - min))) + min));
            }
        }
        return A;
    }

    @Override
    public Matrix newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, ((int) (Math.random() * (maxReal - minReal))) + minReal,
                        ((int) (Math.random() * (maxImag - minImag))) + minImag);
            }
        }
        return A;
    }

    @Override
    public Matrix newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Math.random() * (maxReal - minReal) + minReal, Math.random() * (maxImag - minImag) + minImag);
            }
        }
        return A;
    }

    @Override
    public Matrix newRandom(int m, int n, double min, double max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Math.random() * (max - min) + min, Math.random() * (max - min) + min);
            }
        }
        return A;
    }

    @Override
    public Matrix newRandom(int m, int n, int min, int max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, ((int) (Math.random() * (max - min))) + min,
                        ((int) (Math.random() * (max - min))) + min);
            }
        }
        return A;
    }

    @Override
    public Matrix newRandomImag(int m, int n) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.I(Math.random()));
            }
        }
        return A;
    }

    @Override
    public Matrix load(File file) throws RuntimeIOException {
        Matrix m = newMatrix(1, 1);
        m.read(file);
        return m;
    }

    @Override
    public void close() {

    }

    @Override
    public void reset() {

    }
    //    public Matrix load(String file) throws IOException {
//        MemMatrix memMatrix = new MemMatrix(new File(file));
//        memMatrix.setFactory(this);
//        return memMatrix;
//    }


    @Override
    public Matrix newMatrix(Matrix[][] blocs) {
        int rows = 0;
        int cols = 0;
        for (Matrix[] subMatrixe : blocs) {
            int r = 0;
            int c = 0;
            for (Matrix aSubMatrixe : subMatrixe) {
                c += aSubMatrixe.getColumnCount();
                if (r == 0) {
                    r = aSubMatrixe.getRowCount();
                } else if (r != aSubMatrixe.getRowCount()) {
                    throw new IllegalArgumentException("Column count does not match");
                }
            }
            if (cols == 0) {
                cols = c;
            } else if (cols != c) {
                throw new IllegalArgumentException("Column count does not match");
            }
            rows += r;
        }
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }

        Matrix m=newMatrix(rows, cols);

        int row = 0;
        int col;
        for (Matrix[] subMatrixe1 : blocs) {
            col = 0;
            for (Matrix aSubMatrixe1 : subMatrixe1) {
                m.set(row, col, aSubMatrixe1);
                col += aSubMatrixe1.getColumnCount();
            }
            row += subMatrixe1[0].getRowCount();
        }
        return m;
    }

    @Override
    public String toString() {
        return getId()+":"+getClass().getSimpleName();
    }
}
