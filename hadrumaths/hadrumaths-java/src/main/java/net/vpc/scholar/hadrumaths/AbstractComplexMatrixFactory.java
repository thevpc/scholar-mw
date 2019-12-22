package net.vpc.scholar.hadrumaths;

import java.io.UncheckedIOException;

import java.io.File;

/**
 * Created by vpc on 2/5/15.
 */
public abstract class AbstractComplexMatrixFactory implements ComplexMatrixFactory {
    @Override
    public ComplexMatrix newZeros(TMatrix<Complex> other) {
        return newConstant(other.getRowCount(), other.getColumnCount(), Complex.ZERO);
    }

    @Override
    public ComplexMatrix newConstant(int dim, Complex value) {
        return newConstant(dim, dim, value);
    }

    @Override
    public ComplexMatrix newOnes(int dim) {
        return newConstant(dim, dim, Complex.ONE);
    }

    @Override
    public ComplexMatrix newOnes(int rows, int cols) {
        return newConstant(rows, cols, Complex.ONE);
    }


    @Override
    public ComplexMatrix newImmutableConstant(int rows, int cols, Complex value) {
        return new AbstractUnmodifiableComplexMatrix(rows, cols, this) {
            @Override
            public Complex get(int row, int col) {
                return value;
            }
        };
    }

    @Override
    public ComplexMatrix newConstant(int rows, int cols, Complex value) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        ComplexMatrix e = newMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, value);
            }
        }
        return e;
    }

    @Override
    public ComplexMatrix newMatrix(TMatrix<Complex> other) {
        int rows = other.getRowCount();
        int cols = other.getColumnCount();
        ComplexMatrix e = newMatrix(rows, cols);
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
    public ComplexMatrix newZeros(int dim) {
        return newConstant(dim, dim, Complex.ZERO);
    }

    @Override
    public ComplexMatrix newZeros(int rows, int cols) {
        return newConstant(rows, cols, Complex.ZERO);
    }

    @Override
    public ComplexMatrix newIdentity(TMatrix<Complex> c) {
        return newIdentity(c.getRowCount(), c.getColumnCount());
    }

    @Override
    public ComplexMatrix newNaN(int dim) {
        return newNaN(dim, dim);
    }

    @Override
    public ComplexMatrix newNaN(int rows, int cols) {
        return newConstant(rows, cols, Complex.NaN);
    }

    @Override
    public ComplexMatrix newIdentity(int dim) {
        return newIdentity(dim, dim);
    }

    @Override
    public ComplexMatrix newIdentity(int rows, int cols) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        ComplexMatrix e = newMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, (i == j) ? Complex.ONE : Complex.ZERO);
            }
        }
        return e;
    }

    @Override
    public ComplexMatrix newMatrix(String string) {
        ComplexMatrix matrix = newMatrix(1, 1);
        matrix.read(string);
        return matrix;
    }

    @Override
    public ComplexMatrix newMatrix(Complex[][] complex) {
        int rows = complex.length;
        int cols = complex[0].length;
        ComplexMatrix e = newMatrix(rows, cols);
        e.set(complex);
        return e;
    }

    @Override
    public ComplexMatrix newMatrix(MutableComplex[][] complex) {
        int rows = complex.length;
        int cols = complex[0].length;
        ComplexMatrix e = newMatrix(rows, cols);
        e.set(complex);
        return e;
    }

    @Override
    public ComplexMatrix newMatrix(double[][] complex) {
        int rows = complex.length;
        int cols = complex[0].length;
        ComplexMatrix e = newMatrix(rows, cols);
        e.set(complex);
        return e;
    }

    @Override
    public ComplexMatrix newMatrix(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public ComplexMatrix newMatrix(int rows, int columns, CellIteratorType it, TMatrixCell<Complex> item) {
        ComplexMatrix e = newMatrix(rows, columns);
        switch (it) {
            case FULL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        e.set(i, j, item.get(i, j));
                    }
                }
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
                        e.set(i, j, e.get(j, i));
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
                        e.set(i, j, e.get(j, i).conj());
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
    public ComplexMatrix newColumnMatrix(final Complex... values) {
        Complex[][] d = new Complex[values.length][1];
        for (int i = 0; i < d.length; i++) {
            d[i][0] = values[i];
        }
        return newMatrix(d);
    }

    @Override
    public ComplexMatrix newRowMatrix(final Complex... values) {
        return newMatrix(new Complex[][]{values});
    }

    @Override
    public ComplexMatrix newColumnMatrix(int rows, final TVectorCell<Complex> cellFactory) {
        return newMatrix(rows, 1, CellIteratorType.FULL, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public ComplexMatrix newRowMatrix(int columns, final TVectorCell<Complex> cellFactory) {
        return newMatrix(1, columns, CellIteratorType.FULL, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(column);
            }
        });
    }

    @Override
    public ComplexMatrix newSymmetric(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.SYMETRIC, cellFactory);
    }

    @Override
    public ComplexMatrix newHermitian(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.HERMITIAN, cellFactory);
    }

//    @Override
//    public ComplexMatrix newDiagonal(int rows, int cols, TMatrixCell<Complex> cellFactory) {
//        return newMatrix(rows, cols, CellIteratorType.DIAGONAL, cellFactory);
//    }

    @Override
    public ComplexMatrix newDiagonal(int rows, final TVectorCell<Complex> cellFactory) {
        return newMatrix(rows, rows, CellIteratorType.DIAGONAL, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public ComplexMatrix newDiagonal(final Complex... c) {
        return newMatrix(c.length, c.length, CellIteratorType.DIAGONAL, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return c[row];
            }
        });
    }

    @Override
    public ComplexMatrix newMatrix(int dim, TMatrixCell<Complex> cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public ComplexMatrix newSymmetric(int dim, TMatrixCell<Complex> cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.SYMETRIC, cellFactory);
    }

    @Override
    public ComplexMatrix newHermitian(int dim, TMatrixCell<Complex> cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.HERMITIAN, cellFactory);
    }

//    @Override
//    public ComplexMatrix newDiagonal(int dim, TMatrixCell<Complex> cellFactory) {
//        return newMatrix(dim, dim, CellIteratorType.DIAGONAL, cellFactory);
//    }

    @Override
    public ComplexMatrix newRandomReal(int m, int n) {
        ComplexMatrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, MathsBase.random(), 0.0);
            }
        }
        return A;
    }

    @Override
    public ComplexMatrix newRandomReal(int m, int n, int min, int max) {
        ComplexMatrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, (((int) (MathsBase.random() * (max - min))) + min), 0);
            }
        }
        return A;
    }

    @Override
    public ComplexMatrix newRandomReal(int m, int n, double min, double max) {
        ComplexMatrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, (MathsBase.random() * (max - min) + min), 0);
            }
        }
        return A;
    }

    @Override
    public ComplexMatrix newRandomImag(int m, int n, double min, double max) {
        ComplexMatrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, 0, (MathsBase.random() * (max - min) + min));
            }
        }
        return A;
    }

    @Override
    public ComplexMatrix newRandomImag(int m, int n, int min, int max) {
        ComplexMatrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, 0, (((int) (MathsBase.random() * (max - min))) + min));
            }
        }
        return A;
    }

    @Override
    public ComplexMatrix newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        ComplexMatrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, ((int) (MathsBase.random() * (maxReal - minReal))) + minReal,
                        ((int) (MathsBase.random() * (maxImag - minImag))) + minImag);
            }
        }
        return A;
    }

    @Override
    public ComplexMatrix newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        ComplexMatrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, MathsBase.random() * (maxReal - minReal) + minReal, MathsBase.random() * (maxImag - minImag) + minImag);
            }
        }
        return A;
    }

    @Override
    public ComplexMatrix newRandom(int m, int n, double min, double max) {
        ComplexMatrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, MathsBase.random() * (max - min) + min, MathsBase.random() * (max - min) + min);
            }
        }
        return A;
    }

    @Override
    public ComplexMatrix newRandom(int m, int n, int min, int max) {
        ComplexMatrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, ((int) (MathsBase.random() * (max - min))) + min,
                        ((int) (MathsBase.random() * (max - min))) + min);
            }
        }
        return A;
    }

    @Override
    public ComplexMatrix newRandomImag(int m, int n) {
        ComplexMatrix A = newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, Complex.I(MathsBase.random()));
            }
        }
        return A;
    }

    @Override
    public ComplexMatrix load(File file) throws UncheckedIOException {
        ComplexMatrix m = newMatrix(1, 1);
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
//        MemComplexMatrix memMatrix = new MemComplexMatrix(new File(file));
//        memMatrix.setFactory(this);
//        return memMatrix;
//    }


    @Override
    public ComplexMatrix newMatrix(ComplexMatrix[][] blocs) {
        int rows = 0;
        int cols = 0;
        for (ComplexMatrix[] subMatrixe : blocs) {
            int r = 0;
            int c = 0;
            for (ComplexMatrix aSubMatrixe : subMatrixe) {
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

        ComplexMatrix m = newMatrix(rows, cols);

        int row = 0;
        int col;
        for (ComplexMatrix[] subMatrixe1 : blocs) {
            col = 0;
            for (ComplexMatrix aSubMatrixe1 : subMatrixe1) {
                m.set(row, col, aSubMatrixe1);
                col += aSubMatrixe1.getColumnCount();
            }
            row += subMatrixe1[0].getRowCount();
        }
        return m;
    }

    @Override
    public ComplexMatrix newMatrix(TMatrix[][] blocs) {
        int rows = 0;
        int cols = 0;
        for (TMatrix[] subMatrixe : blocs) {
            int r = 0;
            int c = 0;
            for (TMatrix aSubMatrixe : subMatrixe) {
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

        ComplexMatrix m = newMatrix(rows, cols);

        int row = 0;
        int col;
        for (TMatrix[] subMatrixe1 : blocs) {
            col = 0;
            for (TMatrix aSubMatrixe1 : subMatrixe1) {
                m.set(row, col, (TMatrix<Complex>) aSubMatrixe1);
                col += aSubMatrixe1.getColumnCount();
            }
            row += subMatrixe1[0].getRowCount();
        }
        return m;
    }

    @Override
    public String toString() {
        return getId() + ":" + getClass().getSimpleName();
    }



    @Override
    public ComplexMatrix newImmutableMatrix(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return newImmutableMatrix(rows, cols, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public ComplexMatrix newImmutableMatrix(int rows, int columns, CellIteratorType it, TMatrixCell<Complex> item) {
        switch (it) {
            case FULL: {
                return new AbstractUnmodifiableComplexMatrix(rows, columns, this) {
                    @Override
                    public Complex get(int row, int col) {
                        return item.get(row, col);
                    }
                };
            }
            case DIAGONAL: {
                return new AbstractUnmodifiableComplexMatrix(rows, columns, this) {
                    @Override
                    public Complex get(int row, int col) {
                        return row == col ? item.get(row, col) : Complex.ZERO;
                    }
                };
            }
            case SYMETRIC: {
                return new AbstractUnmodifiableComplexMatrix(rows, columns, this) {
                    @Override
                    public Complex get(int row, int col) {
                        return row <= col ? item.get(row, col) : item.get(col, row);
                    }
                };
            }
            case HERMITIAN: {
                return new AbstractUnmodifiableComplexMatrix(rows, columns, this) {
                    @Override
                    public Complex get(int row, int col) {
                        return row <= col ? item.get(row, col) : item.get(col, row).conj();
                    }
                };
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + it);
            }
        }
    }

    @Override
    public ComplexMatrix newImmutableColumnMatrix(int rows, TVectorCell<Complex> cellFactory) {
        return newImmutableMatrix(rows, 1, CellIteratorType.FULL, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public ComplexMatrix newImmutableSymmetric(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return newImmutableMatrix(rows, cols, CellIteratorType.SYMETRIC, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public ComplexMatrix newImmutableHermitian(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return newImmutableMatrix(rows, cols, CellIteratorType.HERMITIAN, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

//    @Override
//    public ComplexMatrix newImmutableDiagonal(int rows, int cols, TMatrixCell<Complex> cellFactory) {
//        return newImmutableMatrix(rows, cols, CellIteratorType.DIAGONAL, new TMatrixCell<Complex>() {
//            @Override
//            public Complex get(int row, int column) {
//                return cellFactory.get(row, column);
//            }
//        });
//    }

    @Override
    public ComplexMatrix newImmutableDiagonal(int rows, TVectorCell<Complex> cellFactory) {
        return newImmutableMatrix(rows, rows, CellIteratorType.DIAGONAL, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public ComplexMatrix newImmutableMatrix(int dim, TMatrixCell<Complex> cellFactory) {
        return newImmutableMatrix(dim, dim, CellIteratorType.FULL, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public ComplexMatrix newImmutableSymmetric(int dim, TMatrixCell<Complex> cellFactory) {
        return newImmutableMatrix(dim, dim, CellIteratorType.SYMETRIC, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public ComplexMatrix newImmutableHermitian(int dim, TMatrixCell<Complex> cellFactory) {
        return newImmutableMatrix(dim, dim, CellIteratorType.HERMITIAN, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

//    @Override
//    public ComplexMatrix newImmutableDiagonal(int dim, TMatrixCell<Complex> cellFactory) {
//        return newImmutableMatrix(dim, dim, CellIteratorType.DIAGONAL, new TMatrixCell<Complex>() {
//            @Override
//            public Complex get(int row, int column) {
//                return cellFactory.get(row, column);
//            }
//        });
//    }

    @Override
    public ComplexMatrix newImmutableIdentity(int dim) {
        return newImmutableIdentity(dim, dim);
    }

    @Override
    public ComplexMatrix newImmutableColumnMatrix(final Complex... values) {
        Complex[][] d = new Complex[values.length][1];
        for (int i = 0; i < d.length; i++) {
            d[i][0] = values[i];
        }
        return newMatrix(d);
    }

    @Override
    public ComplexMatrix newImmutableRowMatrix(int columns, TVectorCell<Complex> cellFactory) {
        return newImmutableMatrix(1, columns, CellIteratorType.FULL, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public ComplexMatrix newImmutableRowMatrix(final Complex... values) {
        return newMatrix(new Complex[][]{values});
    }





//    @Override
//    public  Matrix newImmutableColumnMatrix(int rows, final VectorCell cellFactory) {
//        return newImmutableMatrix(rows, 1, CellIteratorType.FULL, new MatrixCell() {
//            @Override
//            public Complex get(int row, int column) {
//                return cellFactory.get(row);
//            }
//        });
//    }
//    @Override
//    public  Matrix newImmutableColumnMatrix(int rows, final TVectorCell<Complex> cellFactory) {
//        return newImmutableMatrix(rows, 1, CellIteratorType.FULL, new MatrixCell() {
//            @Override
//            public Complex get(int row, int column) {
//                return cellFactory.get(row);
//            }
//        });
//    }
//
//    @Override
//    public  Matrix newImmutableRowMatrix(int columns, final TVectorCell<Complex> cellFactory) {
//        return newImmutableMatrix(1, columns, CellIteratorType.FULL, new MatrixCell() {
//            @Override
//            public Complex get(int row, int column) {
//                return cellFactory.get(column);
//            }
//        });
//    }

    @Override
    public TMatrix<Complex> newImmutableDiagonal(Complex... c) {
        return newImmutableMatrix(c.length, c.length, CellIteratorType.DIAGONAL, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return c[row];
            }
        });
    }

    @Override
    public ComplexMatrix newImmutableIdentity(int rows, int cols) {
        return newImmutableMatrix(rows, cols, CellIteratorType.DIAGONAL, new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return Complex.ONE;
            }
        });
    }

}
