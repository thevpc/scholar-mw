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
        for (int i = 0; i < rows; i++) {
            TVector<Complex> row = other.getRow(i);
            for (int j = 0; j < cols; j++) {
                e.set(i, j, row.get(j));
            }
        }
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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, complex[i][j]);
            }
        }
        return e;
    }

    @Override
    public  Matrix newMatrix(MutableComplex[][] complex) {
        int rows = complex.length;
        int cols = complex[0].length;
        Matrix e = newMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, complex[i][j].toComplex());
            }
        }
        return e;
    }

    @Override
    public Matrix newMatrix(double[][] complex) {
        int rows = complex.length;
        int cols = complex[0].length;
        Matrix e = newMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, Complex.valueOf(complex[i][j]));
            }
        }
        return e;
    }

    @Override
    public Matrix newMatrix(int rows, int cols, CellFactory cellFactory) {
        return newMatrix(rows,cols, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public Matrix newMatrix(int rows, int columns, CellIteratorType it, CellFactory item) {
        Matrix e = newMatrix(rows, columns);
        switch (it) {
            case FULL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        e.set(i, j, item.item(i, j));
                    }
                }
                break;
            }
            case DIAGONAL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        e.set(i, j, Complex.ZERO);
                    }
                    e.set(i, i, item.item(i, i));
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
                        e.set(i, j, item.item(i, j));
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
                        e.set(i, j, item.item(i, j));
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
    public  Matrix newColumnMatrix(int rows, final VCellFactory cellFactory) {
        return newMatrix(rows, 1, CellIteratorType.FULL, new CellFactory() {
            @Override
            public Complex item(int row, int column) {
                return cellFactory.item(row);
            }
        });
    }

    @Override
    public  Matrix newRowMatrix(int columns, final VCellFactory cellFactory) {
        return newMatrix(1, columns, CellIteratorType.FULL, new CellFactory() {
            @Override
            public Complex item(int row, int column) {
                return cellFactory.item(column);
            }
        });
    }

    @Override
    public Matrix newSymmetric(int rows, int cols, CellFactory cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.SYMETRIC, cellFactory);
    }

    @Override
    public Matrix newHermitian(int rows, int cols, CellFactory cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.HERMITIAN, cellFactory);
    }

    @Override
    public Matrix newDiagonal(int rows, int cols, CellFactory cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.DIAGONAL, cellFactory);
    }

    @Override
    public Matrix newDiagonal(int rows, final VCellFactory cellFactory) {
        return newMatrix(rows, rows, CellIteratorType.DIAGONAL, new CellFactory() {
            @Override
            public Complex item(int row, int column) {
                return cellFactory.item(row);
            }
        });
    }

    @Override
    public Matrix newDiagonal(final Complex... c) {
        return newMatrix(c.length, c.length, CellIteratorType.DIAGONAL, new CellFactory() {
            @Override
            public Complex item(int row, int column) {
                return c[row];
            }
        });
    }

    @Override
    public  Matrix newMatrix(int dim, CellFactory cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public  Matrix newSymmetric(int dim, CellFactory cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.SYMETRIC, cellFactory);
    }

    @Override
    public  Matrix newHermitian(int dim, CellFactory cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.HERMITIAN, cellFactory);
    }

    @Override
    public Matrix newDiagonal(int dim, CellFactory cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.DIAGONAL, cellFactory);
    }

    @Override
    public Matrix newRandomReal(int m, int n) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.valueOf(Math.random()));
            }
        }
        return A;
    }

    @Override
    public Matrix newRandomReal(int m, int n, int min, int max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.valueOf(((int) (Math.random() * (max - min))) + min));
            }
        }
        return A;
    }

    @Override
    public Matrix newRandomReal(int m, int n, double min, double max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.valueOf(Math.random() * (max - min) + min));
            }
        }
        return A;
    }

    @Override
    public Matrix newRandomImag(int m, int n, double min, double max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.I(Math.random() * (max - min) + min));
            }
        }
        return A;
    }

    @Override
    public Matrix newRandomImag(int m, int n, int min, int max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.I(((int) (Math.random() * (max - min))) + min));
            }
        }
        return A;
    }

    @Override
    public Matrix newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.valueOf(((int) (Math.random() * (maxReal - minReal))) + minReal,
                        ((int) (Math.random() * (maxImag - minImag))) + minImag));
            }
        }
        return A;
    }

    @Override
    public Matrix newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.valueOf(Math.random() * (maxReal - minReal) + minReal, Math.random() * (maxImag - minImag) + minImag));
            }
        }
        return A;
    }

    @Override
    public Matrix newRandom(int m, int n, double min, double max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.valueOf(Math.random() * (max - min) + min, Math.random() * (max - min) + min));
            }
        }
        return A;
    }

    @Override
    public Matrix newRandom(int m, int n, int min, int max) {
        Matrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.valueOf(((int) (Math.random() * (max - min))) + min,
                        ((int) (Math.random() * (max - min))) + min));
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
    public Matrix load(File file) throws IOException {
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
}