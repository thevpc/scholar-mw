package net.vpc.scholar.hadrumaths;

import java.io.UncheckedIOException;

import java.io.File;

/**
 * Created by vpc on 2/5/15.
 */
public abstract class AbstractTMatrixFactory<T> implements TMatrixFactory<T> {
    private VectorSpace<T> vs;

    public AbstractTMatrixFactory(VectorSpace<T> vs) {
        this.vs = vs;
    }

    @Override
    public TMatrix<T> newZeros(TMatrix<T> other) {
        return newConstant(other.getRowCount(), other.getColumnCount(), vs.zero());
    }

    @Override
    public TMatrix<T> newConstant(int dim, T value) {
        return newConstant(dim, dim, value);
    }

    @Override
    public TMatrix<T> newOnes(int dim) {
        return newConstant(dim, dim, vs.one());
    }

    @Override
    public TMatrix<T> newOnes(int rows, int cols) {
        return newConstant(rows, cols, vs.one());
    }


    @Override
    public TMatrix<T> newImmutableConstant(int rows, int cols, T value) {
        return new AbstractUnmodifiableTMatrix<T>(rows, cols, this,vs) {
            @Override
            public T get(int row, int col) {
                return value;
            }
        };
    }

    @Override
    public TMatrix<T> newConstant(int rows, int cols, T value) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        TMatrix<T> e = newMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, value);
            }
        }
        return e;
    }


    @Override
    public TMatrix<T> newZeros(int dim) {
        return newConstant(dim, dim, vs.zero());
    }

    @Override
    public TMatrix<T> newZeros(int rows, int cols) {
        return newConstant(rows, cols, vs.zero());
    }

    @Override
    public TMatrix<T> newIdentity(TMatrix<T> c) {
        return newIdentity(c.getRowCount(), c.getColumnCount());
    }

    @Override
    public TMatrix<T> newNaN(int dim) {
        return newNaN(dim, dim);
    }

    @Override
    public TMatrix<T> newNaN(int rows, int cols) {
        return newConstant(rows, cols, vs.nan());
    }

    @Override
    public TMatrix<T> newIdentity(int dim) {
        return newIdentity(dim, dim);
    }

    @Override
    public TMatrix<T> newIdentity(int rows, int cols) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        TMatrix<T> e = newMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, (i == j) ? vs.one() : vs.zero());
            }
        }
        return e;
    }

    @Override
    public TMatrix<T> newMatrix(String string) {
        TMatrix<T> matrix = newMatrix(1, 1);
        matrix.read(string);
        return matrix;
    }

    @Override
    public TMatrix<T> newMatrix(T[][] complex) {
        int rows = complex.length;
        int cols = complex[0].length;
        TMatrix<T> e = newMatrix(rows, cols);
        e.set(complex);
        return e;
    }

//    @Override
//    public TMatrix<T> newMatrix(MutableComplex[][] complex) {
//        int rows = complex.length;
//        int cols = complex[0].length;
//        TMatrix<T> e = newMatrix(rows, cols);
//        e.set(complex);
//        return e;
//    }
//
//    @Override
//    public TMatrix<T> newMatrix(double[][] complex) {
//        int rows = complex.length;
//        int cols = complex[0].length;
//        TMatrix<T> e = newMatrix(rows, cols);
//        e.set(complex);
//        return e;
//    }

    @Override
    public TMatrix<T> newMatrix(int rows, int cols, TMatrixCell<T> cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.FULL, cellFactory);
    }

//    @Override
//    public TMatrix<T> newMatrix(int rows, int columns, CellIteratorType it, TMatrixCell<T> item) {
//        TMatrix<T> e = newMatrix(rows, columns);
//        switch (it) {
//            case FULL: {
//                for (int i = 0; i < rows; i++) {
//                    for (int j = 0; j < columns; j++) {
//                        e.set(i, j, item.get(i, j));
//                    }
//                }
//                break;
//            }
//            case DIAGONAL: {
//                for (int i = 0; i < rows; i++) {
//                    for (int j = 0; j < i; j++) {
//                        e.set(i, j, vs.zero());
//                    }
//                    e.set(i, i, item.get(i, i));
//                    for (int j = i + 1; j < columns; j++) {
//                        e.set(i, j, vs.zero());
//                    }
//                }
//                break;
//            }
//            case SYMETRIC: {
//                for (int i = 0; i < rows; i++) {
//                    for (int j = 0; j < i; j++) {
//                        e.set(i, j, e.get(j, i));
//                    }
//                    for (int j = i; j < columns; j++) {
//                        e.set(i, j, item.get(i, j));
//                    }
//                }
//                break;
//            }
//            case HERMITIAN: {
//                for (int i = 0; i < rows; i++) {
//                    for (int j = 0; j < i; j++) {
//                        e.set(i, j, e.get(j, i).conj());
//                    }
//                    for (int j = i; j < columns; j++) {
//                        e.set(i, j, item.get(i, j));
//                    }
//                }
//                break;
//            }
//            default: {
//                throw new IllegalArgumentException("Unsupported " + it);
//            }
//        }
//        return e;
//    }

    @Override
    public TMatrix<T> newColumnMatrix(final T... values) {
        return newColumnMatrix(values.length, new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return values[index];
            }
        });
    }

    @Override
    public TMatrix<T> newRowMatrix(final T... values) {
        return newRowMatrix(values.length, new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return values[index];
            }
        });
    }


//    @Override
//    public TMatrix<T> newSymmetric(int rows, int cols, TMatrixCell<T> cellFactory) {
//        return newMatrix(rows, cols, CellIteratorType.SYMETRIC, cellFactory);
//    }
//
//    @Override
//    public TMatrix<T> newHermitian(int rows, int cols, TMatrixCell<T> cellFactory) {
//        return newMatrix(rows, cols, CellIteratorType.HERMITIAN, cellFactory);
//    }
//
//    @Override
//    public TMatrix<T> newDiagonal(int rows, int cols, TMatrixCell<T> cellFactory) {
//        return newMatrix(rows, cols, CellIteratorType.DIAGONAL, cellFactory);
//    }

    @Override
    public TMatrix<T> newDiagonal(final T... c) {
        return newMatrix(c.length, c.length, CellIteratorType.DIAGONAL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return c[row];
            }
        });
    }

//    @Override
//    public TMatrix<T> newMatrix(int dim, MatrixCell cellFactory) {
//        return newMatrix(dim, dim, CellIteratorType.FULL, cellFactory);
//    }
//
//    @Override
//    public TMatrix<T> newSymmetric(int dim, MatrixCell cellFactory) {
//        return newMatrix(dim, dim, CellIteratorType.SYMETRIC, cellFactory);
//    }
//
//    @Override
//    public TMatrix<T> newHermitian(int dim, MatrixCell cellFactory) {
//        return newMatrix(dim, dim, CellIteratorType.HERMITIAN, cellFactory);
//    }
//
//    @Override
//    public TMatrix<T> newDiagonal(int dim, MatrixCell cellFactory) {
//        return newMatrix(dim, dim, CellIteratorType.DIAGONAL, cellFactory);
//    }

//    @Override
//    public TMatrix<T> newRandomReal(int m, int n) {
//        TMatrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, Maths.random(), 0.0);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public TMatrix<T> newRandomReal(int m, int n, int min, int max) {
//        TMatrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, (((int) (Maths.random() * (max - min))) + min), 0);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public TMatrix<T> newRandomReal(int m, int n, double min, double max) {
//        TMatrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, (Maths.random() * (max - min) + min), 0);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public TMatrix<T> newRandomImag(int m, int n, double min, double max) {
//        TMatrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, 0, (Maths.random() * (max - min) + min));
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public TMatrix<T> newRandomImag(int m, int n, int min, int max) {
//        TMatrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, 0, (((int) (Maths.random() * (max - min))) + min));
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public TMatrix<T> newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
//        TMatrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, ((int) (Maths.random() * (maxReal - minReal))) + minReal,
//                        ((int) (Maths.random() * (maxImag - minImag))) + minImag);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public TMatrix<T> newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
//        TMatrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, Maths.random() * (maxReal - minReal) + minReal, Maths.random() * (maxImag - minImag) + minImag);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public TMatrix<T> newRandom(int m, int n, double min, double max) {
//        TMatrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, Maths.random() * (max - min) + min, Maths.random() * (max - min) + min);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public TMatrix<T> newRandom(int m, int n, int min, int max) {
//        TMatrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, ((int) (Maths.random() * (max - min))) + min,
//                        ((int) (Maths.random() * (max - min))) + min);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public TMatrix<T> newRandomImag(int m, int n) {
//        TMatrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, T.I(Maths.random()));
//            }
//        }
//        return A;
//    }

    @Override
    public TMatrix<T> load(File file) throws UncheckedIOException {
        TMatrix<T> m = newMatrix(1, 1);
        m.read(file);
        return m;
    }

    @Override
    public void close() {

    }

    @Override
    public void reset() {

    }
    //    public TMatrix<T> load(String file) throws IOException {
//        MemComplexMatrix memMatrix = new MemComplexMatrix(new File(file));
//        memMatrix.setFactory(this);
//        return memMatrix;
//    }


    @Override
    public TMatrix<T> newMatrix(TMatrix<T> other) {
        int rows = other.getRowCount();
        int cols = other.getColumnCount();
        TMatrix<T> e = newMatrix(rows, cols);
        e.set(other);
//        for (int i = 0; i < rows; i++) {
//            TVector<T> row = other.getRow(i);
//            for (int j = 0; j < cols; j++) {
//                e.set(i, j, row.get(j));
//            }
//        }
        return e;
    }

    @Override
    public TMatrix<T> newMatrix(TMatrix<T>[][] blocs) {
        int rows = 0;
        int cols = 0;
        for (TMatrix<T>[] subMatrixe : blocs) {
            int r = 0;
            int c = 0;
            for (TMatrix<T> aSubMatrixe : subMatrixe) {
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

        TMatrix<T> m = newMatrix(rows, cols);

        int row = 0;
        int col;
        for (TMatrix<T>[] subMatrixe1 : blocs) {
            col = 0;
            for (TMatrix<T> aSubMatrixe1 : subMatrixe1) {
                m.set(row, col, aSubMatrixe1);
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

//    @Override
//    public TMatrix<T> newMatrix(int rows, int cols, TMatrixCell<T> cellFactory) {
//        return newMatrix(rows, cols, CellIteratorType.FULL, cellFactory);
//    }

    @Override
    public TMatrix<T> newMatrix(int rows, int columns, CellIteratorType it, TMatrixCell<T> item) {
        TMatrix<T> e = newMatrix(rows, columns);
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
                        e.set(i, j, vs.zero());
                    }
                    e.set(i, i, item.get(i, i));
                    for (int j = i + 1; j < columns; j++) {
                        e.set(i, j, vs.zero());
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
                        e.set(i, j, vs.conj(e.get(j, i)));
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
    public TMatrix<T> newColumnMatrix(int rows, TVectorCell<T> cellFactory) {
        return newMatrix(rows, 1, CellIteratorType.FULL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public TMatrix<T> newRowMatrix(int columns, TVectorCell<T> cellFactory) {
        return newMatrix(1, columns, CellIteratorType.FULL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public TMatrix<T> newSymmetric(int rows, int cols, TMatrixCell<T> cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.SYMETRIC, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newHermitian(int rows, int cols, TMatrixCell<T> cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.HERMITIAN, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newDiagonal(int rows, int cols, TMatrixCell<T> cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.DIAGONAL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newDiagonal(int rows, TVectorCell<T> cellFactory) {
        return newMatrix(rows, rows, CellIteratorType.DIAGONAL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public TMatrix<T> newMatrix(int dim, TMatrixCell<T> cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.FULL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newSymmetric(int dim, TMatrixCell<T> cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.SYMETRIC, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newHermitian(int dim, TMatrixCell<T> cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.HERMITIAN, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newDiagonal(int dim, TMatrixCell<T> cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.DIAGONAL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }


    @Override
    public TMatrix<T> newImmutableMatrix(int rows, int cols, TMatrixCell<T> cellFactory) {
        return newImmutableMatrix(rows, cols, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public TMatrix<T> newImmutableMatrix(int rows, int columns, CellIteratorType it, TMatrixCell<T> item) {
        switch (it) {
            case FULL: {
                return new AbstractUnmodifiableTMatrix<T>(rows, columns, this,vs) {
                    @Override
                    public T get(int row, int col) {
                        return item.get(row, col);
                    }
                };
            }
            case DIAGONAL: {
                return new AbstractUnmodifiableTMatrix<T>(rows, columns, this,vs) {
                    @Override
                    public T get(int row, int col) {
                        return row == col ? item.get(row, col) : vs.zero();
                    }
                };
            }
            case SYMETRIC: {
                return new AbstractUnmodifiableTMatrix<T>(rows, columns, this,vs) {
                    @Override
                    public T get(int row, int col) {
                        return row <= col ? item.get(row, col) : item.get(col, row);
                    }
                };
            }
            case HERMITIAN: {
                return new AbstractUnmodifiableTMatrix<T>(rows, columns, this,vs) {
                    @Override
                    public T get(int row, int col) {
                        return row <= col ? item.get(row, col) : vs.conj(item.get(col, row));
                    }
                };
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + it);
            }
        }
    }

    @Override
    public TMatrix<T> newImmutableColumnMatrix(int rows, TVectorCell<T> cellFactory) {
        return newImmutableMatrix(rows, 1, CellIteratorType.FULL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }


    @Override
    public TMatrix<T> newImmutableSymmetric(int rows, int cols, TMatrixCell<T> cellFactory) {
        return newImmutableMatrix(rows, cols, CellIteratorType.SYMETRIC, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableHermitian(int rows, int cols, TMatrixCell<T> cellFactory) {
        return newImmutableMatrix(rows, cols, CellIteratorType.HERMITIAN, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableDiagonal(int rows, int cols, TMatrixCell<T> cellFactory) {
        return newImmutableMatrix(rows, cols, CellIteratorType.DIAGONAL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableDiagonal(int rows, TVectorCell<T> cellFactory) {
        return newImmutableMatrix(rows, rows, CellIteratorType.DIAGONAL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableMatrix(int dim, TMatrixCell<T> cellFactory) {
        return newImmutableMatrix(dim, dim, CellIteratorType.FULL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableSymmetric(int dim, TMatrixCell<T> cellFactory) {
        return newImmutableMatrix(dim, dim, CellIteratorType.SYMETRIC, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableHermitian(int dim, TMatrixCell<T> cellFactory) {
        return newImmutableMatrix(dim, dim, CellIteratorType.HERMITIAN, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableDiagonal(int dim, TMatrixCell<T> cellFactory) {
        return newImmutableMatrix(dim, dim, CellIteratorType.DIAGONAL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableIdentity(int dim) {
        return newImmutableIdentity(dim, dim);
    }

    @Override
    public TMatrix<T> newImmutableColumnMatrix(final T... values) {
        return newImmutableMatrix(values.length, 1, CellIteratorType.FULL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return values[row];
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableRowMatrix(int columns, TVectorCell<T> cellFactory) {
        return newImmutableMatrix(1, columns, CellIteratorType.FULL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableRowMatrix(final T... values) {
        return newImmutableMatrix(1, values.length, CellIteratorType.FULL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return values[row];
            }
        });
    }


//    @Override
//    public  TMatrix<T> newImmutableColumnMatrix(int rows, final VectorCell cellFactory) {
//        return newImmutableMatrix(rows, 1, CellIteratorType.FULL, new MatrixCell() {
//            @Override
//            public T get(int row, int column) {
//                return cellFactory.get(row);
//            }
//        });
//    }
//    @Override
//    public  TMatrix<T> newImmutableColumnMatrix(int rows, final TVectorCell<T> cellFactory) {
//        return newImmutableMatrix(rows, 1, CellIteratorType.FULL, new MatrixCell() {
//            @Override
//            public T get(int row, int column) {
//                return cellFactory.get(row);
//            }
//        });
//    }
//
//    @Override
//    public  TMatrix<T> newImmutableRowMatrix(int columns, final TVectorCell<T> cellFactory) {
//        return newImmutableMatrix(1, columns, CellIteratorType.FULL, new MatrixCell() {
//            @Override
//            public T get(int row, int column) {
//                return cellFactory.get(column);
//            }
//        });
//    }

    @Override
    public TMatrix<T> newImmutableDiagonal(T... c) {
        return newImmutableMatrix(c.length, c.length, CellIteratorType.DIAGONAL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return c[row];
            }
        });
    }

    @Override
    public TMatrix<T> newImmutableIdentity(int rows, int cols) {
        return newImmutableMatrix(rows, cols, CellIteratorType.DIAGONAL, new TMatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return vs.one();
            }
        });
    }

}
