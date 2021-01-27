package net.thevpc.scholar.hadrumaths;

import java.io.File;
import java.io.UncheckedIOException;

/**
 * Created by vpc on 2/5/15.
 */
public abstract class AbstractMatrixFactory<T> implements MatrixFactory<T> {
    private final VectorSpace<T> vs;

    public AbstractMatrixFactory(VectorSpace<T> vs) {
        this.vs = vs;
    }

    @Override
    public Matrix<T> newMatrix(Matrix<T> other) {
        int rows = other.getRowCount();
        int cols = other.getColumnCount();
        Matrix<T> e = newMatrix(rows, cols);
        e.set(other);
//        for (int i = 0; i < rows; i++) {
//            Vector<T> row = other.getRow(i);
//            for (int j = 0; j < cols; j++) {
//                e.set(i, j, row.get(j));
//            }
//        }
        return e;
    }

    @Override
    public Matrix<T> newMatrix(Matrix<T>[][] blocs) {
        int rows = 0;
        int cols = 0;
        for (Matrix<T>[] subMatrixe : blocs) {
            int r = 0;
            int c = 0;
            for (Matrix<T> aSubMatrixe : subMatrixe) {
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

        Matrix<T> m = newMatrix(rows, cols);

        int row = 0;
        int col;
        for (Matrix<T>[] subMatrixe1 : blocs) {
            col = 0;
            for (Matrix<T> aSubMatrixe1 : subMatrixe1) {
                m.set(row, col, aSubMatrixe1);
                col += aSubMatrixe1.getColumnCount();
            }
            row += subMatrixe1[0].getRowCount();
        }
        return m;
    }

    @Override
    public Matrix<T> newZeros(Matrix<T> other) {
        return newConstant(other.getRowCount(), other.getColumnCount(), vs.zero());
    }

    @Override
    public Matrix<T> newConstant(int dim, T value) {
        return newConstant(dim, dim, value);
    }

    @Override
    public Matrix<T> newOnes(int dim) {
        return newConstant(dim, dim, vs.one());
    }

    @Override
    public Matrix<T> newOnes(int rows, int cols) {
        return newConstant(rows, cols, vs.one());
    }

    @Override
    public Matrix<T> newConstant(int rows, int cols, T value) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        Matrix<T> e = newMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, value);
            }
        }
        return e;
    }

    @Override
    public Matrix<T> newZeros(int dim) {
        return newConstant(dim, dim, vs.zero());
    }

    @Override
    public Matrix<T> newZeros(int rows, int cols) {
        return newConstant(rows, cols, vs.zero());
    }

    @Override
    public Matrix<T> newIdentity(Matrix<T> c) {
        return newIdentity(c.getRowCount(), c.getColumnCount());
    }

    @Override
    public Matrix<T> newNaN(int dim) {
        return newNaN(dim, dim);
    }

    @Override
    public Matrix<T> newNaN(int rows, int cols) {
        return newConstant(rows, cols, vs.nan());
    }

    @Override
    public Matrix<T> newIdentity(int dim) {
        return newIdentity(dim, dim);
    }

    @Override
    public Matrix<T> newIdentity(int rows, int cols) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        Matrix<T> e = newMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                e.set(i, j, (i == j) ? vs.one() : vs.zero());
            }
        }
        return e;
    }

    @Override
    public Matrix<T> newMatrix(String string) {
        Matrix<T> matrix = newMatrix(1, 1);
        matrix.read(string);
        return matrix;
    }

//    @Override
//    public Matrix<T> newMatrix(MutableComplex[][] complex) {
//        int rows = complex.length;
//        int cols = complex[0].length;
//        Matrix<T> e = newMatrix(rows, cols);
//        e.set(complex);
//        return e;
//    }
//
//    @Override
//    public Matrix<T> newMatrix(double[][] complex) {
//        int rows = complex.length;
//        int cols = complex[0].length;
//        Matrix<T> e = newMatrix(rows, cols);
//        e.set(complex);
//        return e;
//    }

    @Override
    public Matrix<T> newMatrix(T[][] complex) {
        int rows = complex.length;
        int cols = complex[0].length;
        Matrix<T> e = newMatrix(rows, cols);
        e.set(complex);
        return e;
    }

//    @Override
//    public Matrix<T> newMatrix(int rows, int columns, CellIteratorType it, MatrixCell<T> item) {
//        Matrix<T> e = newMatrix(rows, columns);
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
    public Matrix<T> newMatrix(int rows, int cols, MatrixCell<T> cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public Matrix<T> newMatrix(int rows, int columns, CellIteratorType it, MatrixCell<T> item) {
        Matrix<T> e = newMatrix(rows, columns);
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
    public Matrix<T> newColumnMatrix(final T... values) {
        return newColumnMatrix(values.length, MatrixCells.vectorForArray(values));
    }

//    @Override
//    public Matrix<T> newMatrix(int dim, MatrixCell cellFactory) {
//        return newMatrix(dim, dim, CellIteratorType.FULL, cellFactory);
//    }
//
//    @Override
//    public Matrix<T> newSymmetric(int dim, MatrixCell cellFactory) {
//        return newMatrix(dim, dim, CellIteratorType.SYMETRIC, cellFactory);
//    }
//
//    @Override
//    public Matrix<T> newHermitian(int dim, MatrixCell cellFactory) {
//        return newMatrix(dim, dim, CellIteratorType.HERMITIAN, cellFactory);
//    }
//
//    @Override
//    public Matrix<T> newDiagonal(int dim, MatrixCell cellFactory) {
//        return newMatrix(dim, dim, CellIteratorType.DIAGONAL, cellFactory);
//    }

//    @Override
//    public Matrix<T> newRandomReal(int m, int n) {
//        Matrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, Maths.random(), 0.0);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public Matrix<T> newRandomReal(int m, int n, int min, int max) {
//        Matrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, (((int) (Maths.random() * (max - min))) + min), 0);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public Matrix<T> newRandomReal(int m, int n, double min, double max) {
//        Matrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, (Maths.random() * (max - min) + min), 0);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public Matrix<T> newRandomImag(int m, int n, double min, double max) {
//        Matrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, 0, (Maths.random() * (max - min) + min));
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public Matrix<T> newRandomImag(int m, int n, int min, int max) {
//        Matrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, 0, (((int) (Maths.random() * (max - min))) + min));
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public Matrix<T> newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
//        Matrix<T> A = newMatrix(m, n);
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
//    public Matrix<T> newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
//        Matrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, Maths.random() * (maxReal - minReal) + minReal, Maths.random() * (maxImag - minImag) + minImag);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public Matrix<T> newRandom(int m, int n, double min, double max) {
//        Matrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, Maths.random() * (max - min) + min, Maths.random() * (max - min) + min);
//            }
//        }
//        return A;
//    }
//
//    @Override
//    public Matrix<T> newRandom(int m, int n, int min, int max) {
//        Matrix<T> A = newMatrix(m, n);
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
//    public Matrix<T> newRandomImag(int m, int n) {
//        Matrix<T> A = newMatrix(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                A.set(i, j, T.I(Maths.random()));
//            }
//        }
//        return A;
//    }

    @Override
    public Matrix<T> newRowMatrix(final T... values) {
        return newRowMatrix(values.length, MatrixCells.vectorForArray(values));
    }

    @Override
    public Matrix<T> newColumnMatrix(int rows, VectorCell<T> cellFactory) {
        return newMatrix(rows, 1, CellIteratorType.FULL, new MatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    @Override
    public Matrix<T> newRowMatrix(int columns, VectorCell<T> cellFactory) {
        return newMatrix(1, columns, CellIteratorType.FULL, new MatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }
    //    public Matrix<T> load(String file) throws IOException {
//        MemComplexMatrix memMatrix = new MemComplexMatrix(new File(file));
//        memMatrix.setFactory(this);
//        return memMatrix;
//    }

    @Override
    public Matrix<T> newSymmetric(int rows, int cols, MatrixCell<T> cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.SYMETRIC, new MatrixCell<T>() {
            @Override
            public T get(int row, int column) {
                return cellFactory.get(row, column);
            }
        });
    }

    @Override
    public Matrix<T> newHermitian(int rows, int cols, MatrixCell<T> cellFactory) {
        return newMatrix(rows, cols, CellIteratorType.HERMITIAN, cellFactory);
    }

    @Override
    public Matrix<T> newDiagonal(int rows, VectorCell<T> cellFactory) {
        return newMatrix(rows, rows, CellIteratorType.DIAGONAL, MatrixCells.columnForVector(cellFactory));
    }

//    @Override
//    public Matrix<T> newMatrix(int rows, int cols, MatrixCell<T> cellFactory) {
//        return newMatrix(rows, cols, CellIteratorType.FULL, cellFactory);
//    }

    @Override
    public Matrix<T> newDiagonal(final T... c) {
        return newDiagonal(c.length, MatrixCells.vectorForArray(c));
    }

    @Override
    public Matrix<T> newMatrix(int dim, MatrixCell<T> cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public Matrix<T> newSymmetric(int dim, MatrixCell<T> cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.SYMETRIC, cellFactory);
    }

    @Override
    public Matrix<T> newHermitian(int dim, MatrixCell<T> cellFactory) {
        return newMatrix(dim, dim, CellIteratorType.HERMITIAN, cellFactory);
    }

    @Override
    public Matrix<T> load(File file) throws UncheckedIOException {
        Matrix<T> m = newMatrix(1, 1);
        m.read(file);
        return m;
    }

//    @Override
//    public Matrix<T> newDiagonal(int rows, int cols, MatrixCell<T> cellFactory) {
//        return newMatrix(rows, cols, CellIteratorType.DIAGONAL, new MatrixCell<T>() {
//            @Override
//            public T get(int row, int column) {
//                return cellFactory.get(row, column);
//            }
//        });
//    }

    @Override
    public void close() {

    }

    @Override
    public void reset() {

    }

    @Override
    public Matrix<T> newImmutableIdentity(int dim) {
        return newImmutableIdentity(dim, dim);
    }

    @Override
    public Matrix<T> newImmutableConstant(int rows, int cols, T value) {
        return new AbstractUnmodifiableMatrix<T>(rows, cols, this, vs) {
            @Override
            public T get(int row, int col) {
                return value;
            }
        };
    }

//    @Override
//    public Matrix<T> newDiagonal(int dim, MatrixCell<T> cellFactory) {
//        return newMatrix(dim, dim, CellIteratorType.DIAGONAL, new MatrixCell<T>() {
//            @Override
//            public T get(int row, int column) {
//                return cellFactory.get(row, column);
//            }
//        });
//    }

    @Override
    public Matrix<T> newImmutableMatrix(int rows, int cols, MatrixCell<T> cellFactory) {
        return newImmutableMatrix(rows, cols, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public Matrix<T> newImmutableMatrix(int rows, int columns, CellIteratorType it, MatrixCell<T> item) {
        switch (it) {
            case FULL: {
                return new AbstractUnmodifiableMatrix<T>(rows, columns, this, vs) {
                    @Override
                    public T get(int row, int col) {
                        return item.get(row, col);
                    }
                };
            }
            case DIAGONAL: {
                return new AbstractUnmodifiableMatrix<T>(rows, columns, this, vs) {
                    @Override
                    public T get(int row, int col) {
                        return row == col ? item.get(row, col) : vs.zero();
                    }
                };
            }
            case SYMETRIC: {
                return new AbstractUnmodifiableMatrix<T>(rows, columns, this, vs) {
                    @Override
                    public T get(int row, int col) {
                        return row <= col ? item.get(row, col) : item.get(col, row);
                    }
                };
            }
            case HERMITIAN: {
                return new AbstractUnmodifiableMatrix<T>(rows, columns, this, vs) {
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
    public Matrix<T> newImmutableColumnMatrix(final T... values) {

        return newImmutableMatrix(values.length, 1, CellIteratorType.FULL, MatrixCells.columnForArray(values));
    }

    @Override
    public Matrix<T> newImmutableRowMatrix(final T... values) {
        return newImmutableMatrix(1, values.length, CellIteratorType.FULL, MatrixCells.rowForArray(values));
    }

    @Override
    public Matrix<T> newImmutableColumnMatrix(int rows, VectorCell<T> cellFactory) {
        return newImmutableMatrix(rows, 1, CellIteratorType.FULL, MatrixCells.columnForVector(cellFactory));
    }

//    @Override
//    public Matrix<T> newImmutableDiagonal(int rows, int cols, MatrixCell<T> cellFactory) {
//        return newImmutableMatrix(rows, cols, CellIteratorType.DIAGONAL, new MatrixCell<T>() {
//            @Override
//            public T get(int row, int column) {
//                return cellFactory.get(row, column);
//            }
//        });
//    }

    @Override
    public Matrix<T> newImmutableRowMatrix(int columns, VectorCell<T> cellFactory) {
        return newImmutableMatrix(1, columns, CellIteratorType.FULL, MatrixCells.rowForVector(cellFactory));
    }

    @Override
    public Matrix<T> newImmutableSymmetric(int rows, int cols, MatrixCell<T> cellFactory) {
        return newImmutableMatrix(rows, cols, CellIteratorType.SYMETRIC, cellFactory);
    }

    @Override
    public Matrix<T> newImmutableHermitian(int rows, int cols, MatrixCell<T> cellFactory) {
        return newImmutableMatrix(rows, cols, CellIteratorType.HERMITIAN, cellFactory);
    }

    @Override
    public Matrix<T> newImmutableDiagonal(int rows, VectorCell<T> cellFactory) {
        return newImmutableMatrix(rows, rows, CellIteratorType.DIAGONAL, MatrixCells.columnForVector(cellFactory));
    }

//    @Override
//    public Matrix<T> newImmutableDiagonal(int dim, MatrixCell<T> cellFactory) {
//        return newImmutableMatrix(dim, dim, CellIteratorType.DIAGONAL, new MatrixCell<T>() {
//            @Override
//            public T get(int row, int column) {
//                return cellFactory.get(row, column);
//            }
//        });
//    }

    @Override
    public Matrix<T> newImmutableDiagonal(T... values) {
        return newImmutableMatrix(values.length, values.length, CellIteratorType.DIAGONAL, MatrixCells.columnForArray(values));
    }

    @Override
    public Matrix<T> newImmutableMatrix(int dim, MatrixCell<T> cellFactory) {
        return newImmutableMatrix(dim, dim, CellIteratorType.FULL, cellFactory);
    }

    @Override
    public Matrix<T> newImmutableSymmetric(int dim, MatrixCell<T> cellFactory) {
        return newImmutableMatrix(dim, dim, CellIteratorType.SYMETRIC, cellFactory);
    }

    @Override
    public Matrix<T> newImmutableHermitian(int dim, MatrixCell<T> cellFactory) {
        return newImmutableMatrix(dim, dim, CellIteratorType.HERMITIAN, cellFactory);
    }


//    @Override
//    public  Matrix<T> newImmutableColumnMatrix(int rows, final VectorCell cellFactory) {
//        return newImmutableMatrix(rows, 1, CellIteratorType.FULL, new MatrixCell() {
//            @Override
//            public T get(int row, int column) {
//                return cellFactory.get(row);
//            }
//        });
//    }
//    @Override
//    public  Matrix<T> newImmutableColumnMatrix(int rows, final VectorCell<T> cellFactory) {
//        return newImmutableMatrix(rows, 1, CellIteratorType.FULL, new MatrixCell() {
//            @Override
//            public T get(int row, int column) {
//                return cellFactory.get(row);
//            }
//        });
//    }
//
//    @Override
//    public  Matrix<T> newImmutableRowMatrix(int columns, final VectorCell<T> cellFactory) {
//        return newImmutableMatrix(1, columns, CellIteratorType.FULL, new MatrixCell() {
//            @Override
//            public T get(int row, int column) {
//                return cellFactory.get(column);
//            }
//        });
//    }

    @Override
    public Matrix<T> newImmutableIdentity(int rows, int cols) {
        return newImmutableMatrix(rows, cols, CellIteratorType.DIAGONAL, MatrixCells.matrixForConstant(vs.one()));
    }

    @Override
    public String toString() {
        return getId() + ":" + getClass().getSimpleName();
    }


}
