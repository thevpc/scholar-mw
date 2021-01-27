package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.util.adapters.ComplexMatrixFromComplexMatrix;
import net.thevpc.scholar.hadrumaths.util.adapters.DoubleMatrixFromMatrix;
import net.thevpc.scholar.hadrumaths.util.adapters.ExprMatrixFromMatrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public abstract class AbstractMatrix<T> implements Matrix<T> {

    private static final long serialVersionUID = 1L;
    private transient MatrixFactory<T> factory;
    private transient String factoryId;

    public AbstractMatrix() {
    }

    /*To exchange two rows in a matrix*/
    public static <T> void exchange_row(Matrix<T> M, int k, int l, int m, int n) {
        if (k <= 0 || l <= 0 || k > n || l > n || k == l) {
            return;
        }
        T tmp;
        for (int j = 0; j < n; j++) {
            tmp = M.get(k - 1, j);
            M.set(k - 1, j, M.get(l - 1, j));
            M.set(l - 1, j, tmp);
        }
    }

    @Override
    public double getDistance(Normalizable other) {
        return getError((Matrix<T>) other);
    }

    public double norm() {
        return norm(NormStrategy.DEFAULT);
    }

    protected Matrix<T> createMatrix(int rows, int cols) {
        MatrixFactory<T> f = getFactory();
        if (f == null) {
            f = Maths.Config.getComplexMatrixFactory(getComponentType());
        }
        return f.newMatrix(rows, cols);
    }

    protected MatrixFactory<T> createDefaultFactory() {
        return Maths.Config.getComplexMatrixFactory(getComponentType());
    }

    public DMatrix getErrorMatrix(Matrix<T> baseMatrix, double minErrorForZero, ErrorMatrixStrategy strategy) {
        switch (strategy) {
            case ABSOLUTE: {
                return getErrorMatrix(baseMatrix, minErrorForZero);
            }
            case RELATIVE: {
                return getErrorMatrix2(baseMatrix, minErrorForZero);
            }
            case RELATIVE_RI: {
                return getErrorMatrix3(baseMatrix, minErrorForZero);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public DMatrix getErrorMatrix(Matrix<T> baseMatrix) {
        return getErrorMatrix(baseMatrix, Double.NaN);
    }

    public DMatrix getErrorMatrix(Matrix<T> baseMatrix, double minErrorForZero) {
        Matrix<T> m = baseMatrix.sub(this).div(baseMatrix.norm3());
        T[][] mm = m.getArray();
        double[][] d = new double[mm.length][mm[0].length];
        for (int i = 0; i < mm.length; i++) {
            T[] complexes = mm[i];
            for (int j = 0; j < complexes.length; j++) {
                if (!Double.isNaN(minErrorForZero) && getComponentVectorSpace().absdbl(complexes[j]) < minErrorForZero) {
                    d[i][j] = 0;
                } else {
                    d[i][j] = getComponentVectorSpace().absdbl(complexes[j]);
                }
            }
        }
        return new DMatrix(d);
    }

    public double norm(NormStrategy ns) {
        switch (ns) {
            case DEFAULT:
            case NORM2: {
                return norm2();
            }
            case NORM3: {
                return norm3();
            }
            case NORM1: {
                return norm1();
            }
            case NORM_INF: {
                return normInf();
            }
        }
        throw new UnsupportedOperationException();
    }

    /**
     * One norm
     *
     * @return maximum column sum.
     */
    public double norm1() {
        double f = 0;
        int columnDimension = getColumnCount();
        int rows = getRowCount();
        for (int c = 0; c < columnDimension; c++) {
            double s = 0;
            for (int r = 0; r < rows; r++) {
                s += getComponentVectorSpace().absdbl(get(r, c));
            }
            f = Math.max(f, s);
        }
        return f;
    }

    public double norm2() {
        double f = 0;
        int columnDimension = getColumnCount();
        int rows = getRowCount();
        for (int c = 0; c < columnDimension; c++) {
            for (int r = 0; r < rows; r++) {
                f += Maths.sqr(getComponentVectorSpace().absdbl(get(r, c)));
            }
        }
        return Maths.sqrt(f);
    }

    /**
     * One norm
     *
     * @return maximum elemet absdbl.
     */
    public double norm3() {
        double f = 0;
        int columnDimension = getColumnCount();
        int rows = getRowCount();
        for (int j = 0; j < columnDimension; j++) {
            for (int r = 0; r < rows; r++) {
                f = Math.max(f, getComponentVectorSpace().absdbl(get(r, j)));
            }
        }
        return f;
    }

    public double getError(Matrix<T> baseMatrix) {
        double norm = baseMatrix.norm(NormStrategy.DEFAULT);
        if (norm == 0) {
            return this.norm(NormStrategy.DEFAULT);
        }
        return (this.sub(baseMatrix).norm(NormStrategy.DEFAULT) / norm);
    }

    /**
     * term by term
     *
     * @param baseMatrix
     * @param minErrorForZero
     * @return
     */
    public DMatrix getErrorMatrix2(Matrix<T> baseMatrix, double minErrorForZero) {
        Matrix<T> m = baseMatrix.sub(this);
        T[][] mm = m.getArray();
        double[][] d = new double[mm.length][mm[0].length];
        for (int i = 0; i < mm.length; i++) {
            T[] complexes = mm[i];
            for (int j = 0; j < complexes.length; j++) {
                double baseAbs = getComponentVectorSpace().absdbl(baseMatrix.get(i, j));
                double newAbs = getComponentVectorSpace().absdbl(complexes[j]);
                double dd = baseAbs == 0 ? newAbs : (newAbs / baseAbs);
                if (!Double.isNaN(minErrorForZero) && dd < minErrorForZero) {
                    d[i][j] = 0;
                } else {
                    d[i][j] = dd;
                }
            }
        }
        return new DMatrix(d);
    }

    public DMatrix getErrorMatrix3(Matrix<T> baseMatrix, double minErrorForZero) {
        Matrix<T> m = baseMatrix.sub(this);
        T[][] mm = m.getArray();
        double[][] d = new double[mm.length][mm[0].length];
        for (int i = 0; i < mm.length; i++) {
            T[] complexes = mm[i];
            for (int j = 0; j < complexes.length; j++) {
                T base = baseMatrix.get(i, j);
                T val = complexes[j];
                double baseR = getComponentVectorSpace().absdbl(getComponentVectorSpace().real(base));
                double baseI = getComponentVectorSpace().absdbl(getComponentVectorSpace().imag(base));
                double valR = getComponentVectorSpace().absdbl(getComponentVectorSpace().real(val));
                double valI = getComponentVectorSpace().absdbl(getComponentVectorSpace().imag(val));
                double ddR = baseR == 0 ? valR : (valR / baseR);
                double ddI = baseI == 0 ? valI : (valI / baseI);
                double dd = Math.max(ddR, ddI);
                if (!Double.isNaN(minErrorForZero) && dd < minErrorForZero) {
                    d[i][j] = 0;
                } else {
                    d[i][j] = dd;
                }
            }
        }
        return new DMatrix(d);
    }

    /**
     * Infinity norm
     *
     * @return maximum row sum.
     */
    public double normInf() {
        double f = 0;
        int rows = getRowCount();
        int columns = getColumnCount();
        for (int r = 0; r < rows; r++) {
            double s = 0;
            for (int c = 0; c < columns; c++) {
                s += getComponentVectorSpace().absdbl(get(r, c));
            }
            f = Math.max(f, s);
        }
        return f;
    }

    @Override
    public T avg() {
        T f = getComponentVectorSpace().zero();
        int rows = getRowCount();
        int columns = getColumnCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                f = getComponentVectorSpace().add(f, get(r, c));
            }
        }
        f = getComponentVectorSpace().div(f, getComponentVectorSpace().convert(rows * columns));
        return f;
    }

    @Override
    public T sum() {
        T f = getComponentVectorSpace().zero();
        int rows = getRowCount();
        int columns = getColumnCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                f = getComponentVectorSpace().add(f, get(r, c));
            }
        }
        return f;
    }

    @Override
    public T prod() {
        T f = getComponentVectorSpace().zero();
        int rows = getRowCount();
        int columns = getColumnCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                f = getComponentVectorSpace().mul(f, get(r, c));
            }
        }
        return f;
    }

    public void set(Matrix<T>[][] subMatrixes) {
        int rows = 0;
        int cols = 0;
        for (Matrix<T>[] subMatrixe : subMatrixes) {
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
        if (rows != getColumnCount() || rows != getRowCount()) {
            throw new IllegalArgumentException("Columns or Rows count does not match");
        }
        int row = 0;
        int col;
        for (Matrix<T>[] subMatrixe1 : subMatrixes) {
            col = 0;
            for (Matrix<T> aSubMatrixe1 : subMatrixe1) {
                set(row, col, aSubMatrixe1);
                col += aSubMatrixe1.getColumnCount();
            }
            row += subMatrixe1[0].getRowCount();
        }
    }

    @Override
    public void set(T[][] elements) {
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[i].length; j++) {
                set(i, j, elements[i][j]);
            }
        }
    }

    public void setAll(T value) {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                set(i, j, value);
            }
        }
    }

    public T[][] getArray() {
        T[][] arr = newT(getRowCount(), getColumnCount());
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[0].length; c++) {
                arr[r][c] = get(r, c);
            }
        }
        return arr;
    }

    /**
     * Get a submatrix.
     * TODO check me pleaze
     *
     * @param i0 Initial row index
     * @param i1 Final row index
     * @param j0 Initial column index
     * @param j1 Final column index
     * @return A(i0 : i1, j0 : j1)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public Matrix<T> getMatrix(int i0, int i1, int j0, int j1) {
        Matrix<T> X = createMatrix(i1 - i0 + 1, j1 - j0 + 1);
        //T[][] B = X.primitiveElement3DS;// X.getArray();
        try {
            for (int i = i0; i <= i1; i++) {

                for (int k = 0; k < j1 + 1 - j0; k++) {
                    X.set(i - i0, k, get(i, j0 + k));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }

    /**
     * Get a submatrix.
     *
     * @param r Array of row indices.
     * @param c Array of column indices.
     * @return A(r ( :), c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public Matrix<T> getMatrix(int[] r, int[] c) {
        Matrix<T> X = createMatrix(r.length, c.length);
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = 0; j < c.length; j++) {
                    X.set(i, j, get(r[i], c[j]));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }

    /**
     * Get a submatrix.
     *
     * @param r1 Initial row index
     * @param r2 Final row index
     * @param c  Array of column indices.
     * @return A(i0 : i1, c ( :))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public Matrix<T> getMatrix(int r1, int r2, int[] c) {
        Matrix<T> X = createMatrix(r2 - r1 + 1, c.length);
        try {
            for (int i = r1; i <= r2; i++) {
                for (int j = 0; j < c.length; j++) {
                    X.set(i - r1, j, get(i, c[j]));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }

    /**
     * Get a submatrix.
     *
     * @param r  Array of row indices.
     * @param c1 Initial column index
     * @param c2 Final column index
     * @return A(r ( :), j0:j1)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public Matrix<T> getMatrix(int[] r, int c1, int c2) {
        Matrix<T> X = createMatrix(r.length, c2 - c1 + 1);
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = c1; j <= c2; j++) {
                    X.set(i, j - c1, get(r[i], j));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }

    public Matrix<T> div(double c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().div(get(i, j), getComponentVectorSpace().convert(c)));
            }
        }
        return X;
    }

    public Matrix<T> div(T c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().div(get(i, j), c));
            }
        }
        return X;
    }

    public Matrix<T> mul(T c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().mul(get(i, j), c));
            }
        }
        return X;
    }

    public Matrix<T> mul(double c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().mul(get(i, j), getComponentVectorSpace().convert(c)));
            }
        }
        return X;
    }

    public Matrix<T> neg() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().neg(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> add(T c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().add(get(i, j), c));
            }
        }
        return X;
    }

    public Matrix<T> sub(T c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().minus(get(i, j), c));
            }
        }
        return X;
    }

    public Matrix<T> div(Matrix<T> other) {
        return mul(other.inv());
    }

    public Matrix<T> rem(Matrix<T> other) {
        return (Matrix<T>) (toComplex().rem(other.toComplex()).toMatrix());
    }

    public Matrix<T> mul(Matrix<T> other) {
        if (getColumnCount() != other.getRowCount()) {
            throw new IllegalArgumentException("The column dimension " + getColumnCount() + " of the left matrix does not match the row dimension " + other.getRowCount() + " of the right matrix!");
        }
        int a_rows = getRowCount();
        int b_cols = other.getColumnCount();
        int b_rows = other.getRowCount();
        Matrix<T> newElements = createMatrix(a_rows, b_cols);
        for (int i = 0; i < a_rows; i++) {
            for (int j = 0; j < b_cols; j++) {
                T sum = getComponentVectorSpace().zero();
                for (int k = 0; k < b_rows; k++) {
                    sum = getComponentVectorSpace().add(sum, getComponentVectorSpace().mul(get(i, k), (other.get(k, j))));
                }
                newElements.set(i, j, sum);
            }
        }
        return newElements;
    }

    public Matrix<T> dotmul(Matrix<T> other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().mul(get(i, j), other.get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> dotdiv(Matrix<T> other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().div(get(i, j), other.get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> conj() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().conj(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> dotinv() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().inv(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> add(Matrix<T> other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().add(get(i, j), other.get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> add(T[][] other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().add(get(i, j), other[i][j]));
            }
        }
        return X;
    }

    public Matrix<T> sub(T[][] other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().minus(get(i, j), other[i][j]));
            }
        }
        return X;
    }

    public Matrix<T> mul(T[][] other) {
        int a_rows = getRowCount();
        int b_cols = other[0].length;
        int b_rows = other.length;
        Matrix<T> newElements = createMatrix(a_rows, b_cols);
        for (int i = 0; i < a_rows; i++) {
            for (int j = 0; j < b_cols; j++) {
                T sum = getComponentVectorSpace().zero();
                for (int k = 0; k < b_rows; k++) {
                    sum = getComponentVectorSpace().add(sum, getComponentVectorSpace().mul(get(i, k), (other[k][j])));
                }
                newElements.set(i, j, sum);
            }
        }
        return newElements;
    }

    public Matrix<T> sub(Matrix<T> other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().minus(get(i, j), other.get(i, j)));
            }
        }
        return X;
    }

    public T getScalar() {
        return get(0, 0);
    }

    @Override
    public T apply(int row, int col) {
        return get(row, col);
    }

    public T get(int vectorIndex) {
        if (getColumnCount() == 1) {
            return get(vectorIndex, 0);
        }
        if (getRowCount() == 1) {
            return get(0, vectorIndex);
        }
        throw new IllegalArgumentException("No a valid vector");
    }

    @Override
    public T get(Enum anyEnum) {
        return get(anyEnum.ordinal());
    }

    @Override
    public T apply(int vectorIndex) {
        return get(vectorIndex);
    }

    @Override
    public T apply(Enum vectorIndex) {
        return get(vectorIndex.ordinal());
    }

    public void add(int row, int col, T val) {
        set(row, col, getComponentVectorSpace().add(get(row, col), val));
    }

    public void mul(int row, int col, T val) {
        set(row, col, getComponentVectorSpace().mul(get(row, col), val));
    }

    public void div(int row, int col, T val) {
        set(row, col, getComponentVectorSpace().div(get(row, col), val));
    }

    public void sub(int row, int col, T val) {
        set(row, col, getComponentVectorSpace().minus(get(row, col), val));
    }

    @Override
    public void update(int row, int col, T val) {
        set(row, col, val);
    }

    public void set(int row, int col, Matrix<T> src, int srcRow, int srcCol, int rows, int cols) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                set(r + row, col + c, src.get(r + srcRow, c + srcCol));
            }
        }
    }

//    public double[][] getDoubleArray() {
//        int rows = getRowCount();
//        int columns = getColumnCount();
//        double[][] X = new double[rows][columns];
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                X[i][j] = get(i, j).toDouble();
//            }
//        }
//        return X;
//    }
//
//    public double[][] getRealArray() {
//        int rows = getRowCount();
//        int columns = getColumnCount();
//        double[][] X = new double[rows][columns];
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                X[i][j] = get(i, j).getReal();
//            }
//        }
//        return X;
//    }
//
//    public double[][] getImagArray() {
//        int rows = getRowCount();
//        int columns = getColumnCount();
//        double[][] X = new double[rows][columns];
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                X[i][j] = get(i, j).getReal();
//            }
//        }
//        return X;
//    }
//
//    public double[][] getAbsArray() {
//        int rows = getRowCount();
//        int columns = getColumnCount();
//        double[][] X = new double[rows][columns];
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                X[i][j] = get(i, j).absdbl();
//            }
//        }
//        return X;
//    }
//
//    public double[][] getAbsSquareArray() {
//        int rows = getRowCount();
//        int columns = getColumnCount();
//        double[][] X = new double[rows][columns];
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                X[i][j] = Maths.sqr(get(i, j).absdbl());
//            }
//        }
//        return X;
//    }

    public void set(int row, int col, Matrix<T> subMatrix) {
        set(row, col, subMatrix, 0, 0, subMatrix.getRowCount(), subMatrix.getColumnCount());
    }

    @Override
    public void update(int row, int col, Matrix<T> subMatrix) {
        set(row, col, subMatrix);
    }

    public Matrix<T> subMatrix(int row, int col, int rows, int cols) {
        Matrix<T> m = createMatrix(rows, cols);
        m.set(row, col, this);
        return m;
    }

    @Override
    public Vector<Vector<T>> getRows() {
        return (Vector<Vector<T>>) Maths.columnVector(TypeName.of(Vector.class, getComponentType()), new VectorModel<Vector<T>>() {
            @Override
            public int size() {
                return getRowCount();
            }

            @Override
            public Vector<T> get(int index) {
                return getRow(index);
            }
        });
    }

    @Override
    public Vector<Vector<T>> getColumns() {
        return (Vector<Vector<T>>) Maths.columnVector(TypeName.of(Vector.class, getComponentType()), new VectorModel<Vector<T>>() {
            @Override
            public int size() {
                return getColumnCount();
            }

            @Override
            public Vector<T> get(int index) {
                return getColumn(index);
            }
        });
    }

    public ComponentDimension getComponentDimension() {
        return ComponentDimension.of(
                getRowCount(),
                getColumnCount()
        );
    }

    public Matrix<T> transpose() {
        return arrayTranspose();
    }

    /**
     * @return equivalent to transposeHermitian
     */
    public Matrix<T> transjugate() {
        return transposeHermitian();
    }

    /**
     * @return equivalent to transposeHermitian
     */
    public Matrix<T> transposeConjugate() {
        return transposeHermitian();
    }

    /**
     * Hermitian conjugate
     *
     * @return
     */
    public Matrix<T> transposeHermitian() {
        int r = getRowCount();
        if (r == 0) {
            return this;
        }
        int c = getColumnCount();
        Matrix<T> e = createMatrix(c, r);
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                e.set(j, i, getComponentVectorSpace().conj(get(i, j)));
            }
        }
        return e;
    }

    public Matrix<T> arrayTranspose() {
        int r = getRowCount();
        if (r == 0) {
            return this;
        }
        int c = getColumnCount();
        Matrix<T> e = createMatrix(c, r);
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                e.set(j, i, get(i, j));
            }
        }
        return e;
    }

    public Matrix<T> invCond() {
        return inv(Maths.Config.getDefaultMatrixInverseStrategy(), ConditioningStrategy.DEFAULT, NormStrategy.DEFAULT);
    }

    public Matrix<T> inv(InverseStrategy invStr, ConditioningStrategy condStr, NormStrategy normStr) {
        switch (condStr) {
            case DEFAULT:
            case NORM: {
                double n = norm(normStr);
                return div(n).inv(invStr).div(n);
            }
            case NONE: {
                return inv(invStr);
            }
        }
        throw new UnsupportedOperationException();
    }

    public Matrix<T> inv() {
        return inv(Maths.Config.getDefaultMatrixInverseStrategy());
    }

    public Matrix<T> inv(InverseStrategy st) {
        switch (st) {
            case DEFAULT: {
                return inv(Maths.Config.getDefaultMatrixInverseStrategy());
            }
            case BLOCK_SOLVE: {
                return invBlock(InverseStrategy.SOLVE, Maths.Config.getMatrixBlockPrecision());
            }
            case BLOCK_ADJOINT: {
                return invBlock(InverseStrategy.ADJOINT, Maths.Config.getMatrixBlockPrecision());
            }
            case BLOCK_GAUSS: {
                return invBlock(InverseStrategy.GAUSS, Maths.Config.getMatrixBlockPrecision());
            }
            case SOLVE: {
                return invSolve();
            }
            case ADJOINT: {
                return invAdjoint();
            }
            case GAUSS: {
                return invGauss();
            }
//            case BLOCK_OJALGO: {
//                return invBlock(InverseStrategy.OJALGO, 64);
//            }
//            case OJALGO: {
//                throw new IllegalArgumentException("Unsupported");
////                return OjalgoHelper.INSTANCE.inv(this);
//            }
        }
        throw new UnsupportedOperationException("[" + getClass().getName() + "]" + "strategy " + st.toString());
    }

    public Matrix<T> invSolve() {
        return solve(getFactory().newIdentity(getRowCount()));
    }

    /**
     * inversion using Bloc Matrix expansion
     *
     * @return inverse
     */
    public Matrix<T> invBlock(InverseStrategy delegate, int precision) {
        int n = getRowCount();
        int p = getColumnCount();
        if (n != p || (n > 1 && n % 2 != 0) || (precision > 1 && n > 1 && n <= precision)) {
            return inv(delegate);
        }
        switch (n) {
            case 1: {
                T[][] ts = newT(1, 1);
                ts[0][0] = getComponentVectorSpace().inv(get(0, 0));
                return getFactory().newMatrix(ts);
            }
            case 2: {
                T A = get(0, 0);
                T B = get(0, 1);
                T C = get(1, 0);
                T D = get(1, 1);
                T Ai = getComponentVectorSpace().inv(A);
                T CAi = getComponentVectorSpace().mul(C, Ai);
                T AiB = getComponentVectorSpace().mul(Ai, B);
                T DCABi = getComponentVectorSpace().inv(getComponentVectorSpace().minus(D, getComponentVectorSpace().mul(getComponentVectorSpace().mul(C, Ai), B)));
                T mDCABi = getComponentVectorSpace().mul(DCABi, minusOne());
                T AiBmDCABi = getComponentVectorSpace().mul(AiB, mDCABi);
                T[][] t = newT(2, 2);
                t[0][0] = getComponentVectorSpace().minus(Ai, (getComponentVectorSpace().mul(AiBmDCABi, CAi)));
                t[0][1] = AiBmDCABi;
                t[1][0] = getComponentVectorSpace().mul(mDCABi, CAi);
                t[1][1] = DCABi;
                return getFactory().newMatrix(t);
            }
            default: {
                int n2 = n / 2;
                Matrix<T> A = getMatrix(0, n2 - 1, 0, n2 - 1);
                Matrix<T> B = getMatrix(n2, n - 1, 0, n2);
                Matrix<T> C = getMatrix(0, n2 - 1, n2, n - 1);
                Matrix<T> D = getMatrix(n2, n - 1, n2, n - 1);
                Matrix<T> Ai = A.invBlock(delegate, precision);
                Matrix<T> CAi = C.mul(Ai);
                Matrix<T> AiB = Ai.mul(B);
                Matrix<T> DCABi = D.sub(C.mul(Ai).mul(B)).invBlock(delegate, precision);
                Matrix<T> mDCABi = DCABi.mul(-1);
                Matrix<T> AiBmDCABi = AiB.mul(mDCABi);
                Matrix<T> mm = createMatrix(getRowCount(), getColumnCount());
                mm.set(new Matrix[][]{
                        {Ai.sub(AiBmDCABi.mul(CAi)), AiBmDCABi},
                        {mDCABi.mul(CAi), DCABi}
                });
                return mm;
            }
            //case 2?
        }
    }

    public Matrix<T> invGauss() {
        int m = getRowCount();
        int n = getColumnCount();
        if (m != n) {
            throw new ArithmeticException("Determinant Equals 0, Not Invertible.");
        }

        //Pour stocker les lignes pour lesquels un pivot a déjà été trouvé
        ArrayList<Integer> I = new ArrayList<Integer>();

        //Pour stocker les colonnes pour lesquels un pivot a déjà été trouvé
        ArrayList<Integer> J = new ArrayList<Integer>();

        //Pour calculer l'inverse de la matrice initiale
        Matrix<T> A = createMatrix(m, n);
        Matrix<T> B = createMatrix(m, n);

        //Copie de M dans A et Mise en forme de B : B=I
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, get(i, j));
                if (i == j) {
                    B.set(i, j, getComponentVectorSpace().one());
                } else {
                    B.set(i, j, getComponentVectorSpace().zero());
                }
            }
        }

        //Paramètres permettant l'arrêt prématuré des boucles ci-dessous si calcul impossible
        boolean bk = true;
        boolean bl = true;

        //Paramètres de contrôle pour la recherche de pivot
        int cnt_row = 0;
        int cnt_col = 0;

        //paramètre de stockage de coefficients
        T a, tmp;

        for (int k = 0; k < n && bk; k++) {
            if (!I.contains(k)) {
                I.add(k);
                cnt_row++;
                bl = true;
                for (int l = 0; l < n && bl; l++) {
                    if (!J.contains(l)) {
                        a = A.get(k, l);
                        if (!getComponentVectorSpace().isZero(a)) {
                            J.add(l);
                            cnt_col++;
                            bl = false; //permet de sortir de la boucle car le pivot a été trouvé
                            for (int p = 0; p < n; p++) {
                                if (p != k) {
                                    tmp = A.get(p, l);
                                    for (int q = 0; q < n; q++) {
                                        A.set(p, q, getComponentVectorSpace().minus(A.get(p, q), getComponentVectorSpace().mul(A.get(k, q), getComponentVectorSpace().div(tmp, a))));
                                        B.set(p, q, getComponentVectorSpace().minus(B.get(p, q), getComponentVectorSpace().mul(B.get(k, q), (getComponentVectorSpace().div(tmp, a)))));
                                    }
                                }
                            }
                        }
                    }
                }
                if (cnt_row != cnt_col) {
                    //Matrix<T> is singular";
                    //Pas de pivot possible, donc pas d'inverse possible! On sort de la boucle
                    bk = false;
                    k = n;
                }
            }
        }

        if (!bk) {
            throw new ArithmeticException("Matrix is singular");
            //Le pivot n'a pas pu être trouve précédemment, ce qui a donne bk = false
        } else {
            //Réorganisation des colonnes de sorte que A=I et B=Inv(M). Méthode de Gauss-Jordan
            for (int l = 0; l < n; l++) {
                for (int k = 0; k < n; k++) {
                    a = A.get(k, l);
                    if (!getComponentVectorSpace().isZero(a)) {
                        A.set(k, l, getComponentVectorSpace().one());
                        for (int p = 0; p < n; p++) {
                            B.set(k, p, getComponentVectorSpace().div(B.get(k, p), a));
                        }
                        if (k != l) {
                            exchange_row(A, k + 1, l + 1, n, n);
                            exchange_row(B, k + 1, l + 1, n, n);
                        }
                        k = n; //Pour sortir de la boucle car le coefficient non nul a ete trouve
                    }
                }
            }
            A.dispose();
            return B;
        }
    }

    /**
     * Formula used to Calculate Inverse: inv(A) = 1/det(A) * adj(A)
     *
     * @return inverse
     */
    public Matrix<T> invAdjoint() {
        // Formula used to Calculate Inverse:
        // inv(A) = 1/det(A) * adj(A)
        int tms = getRowCount();

        T[][] m = newT(tms, tms);

        T det = det();

        if (det.equals(Complex.ZERO)) {
            throw new ArithmeticException("Determinant Equals 0, Not Invertible.");
        }

//            System.out.println("determinant is " + det);
        T dd = getComponentVectorSpace().inv(det);
        if (tms == 1) {
            T[][] t0 = newT(1, 1);
            t0[0][0] = dd;
            return getFactory().newMatrix(t0);
        }
        Matrix<T> mm = adjoint();
        for (int i = 0; i < tms; i++) {
            for (int j = 0; j < tms; j++) {
                m[i][j] = getComponentVectorSpace().mul(dd, mm.get(i, j));
            }
        }

        return mm;
    }

    public Matrix<T> coMatrix(int row, int col) {
        int tms = getRowCount();
        Matrix<T> ap = createMatrix(tms - 1, tms - 1);
//                for (ii = 0; ii < tms; ii++) {
//                    for (jj = 0; jj < tms; jj++) {
//                        if ((ii != i) && (jj != j)) {
//                            ap[ia][ja] = primitiveElement3DS[ii][jj];
//                            ja++;
//                        }
//                    }
//                    if ((ii != i) && (jj != j)) {
//                        ia++;
//                    }
//                    ja = 0;
//                }
        int ia = 0;
        int ja;
        for (int ii = 0; ii < row; ii++) {
            ja = 0;
//            T[] apia = ap[ia];
            for (int jj = 0; jj < col; jj++) {
                ap.set(ia, ja, get(ii, jj));
                ja++;
            }
            for (int jj = col + 1; jj < tms; jj++) {
                ap.set(ia, ja, get(ii, jj));
                ja++;
            }
            ia++;
        }
        for (int ii = row + 1; ii < tms; ii++) {
            ja = 0;
            for (int jj = 0; jj < col; jj++) {
                ap.set(ia, ja, get(ii, jj));
                ja++;
            }
            for (int jj = col + 1; jj < tms; jj++) {
                ap.set(ia, ja, get(ii, jj));
                ja++;
            }
            ia++;
        }
        return ap;
    }

    public Matrix<T> pow(double rexp) {
        checkSquare();
        int exp = (int) rexp;
        if (exp != rexp) {
            throw new IllegalArgumentException("Unable to raise Matrix to Non integer power");
        }

        switch (exp) {
            case 0: {
                return getFactory().newIdentity(getColumnCount());
            }
            case 1: {
                return this;
            }
            case -1: {
                return inv();
            }
            default: {
                if (exp > 0) {
                    Matrix<T> m = this;
                    while (exp > 1) {
                        m = m.mul(this);
                        exp--;
                    }
                    return m;
                } else {
                    Matrix<T> m = this;
                    int t = -exp;
                    while (t > 1) {
                        m = m.mul(this);
                        t--;
                    }
                    m = m.inv();
                    return m;
                }
            }
        }
    }

    public Matrix<T> adjoint() {
        int tms = getRowCount();

        Matrix<T> m = createMatrix(tms, tms);

        for (int i = 0; i < tms; i++) {
            for (int j = 0; j < tms; j++) {
                T det = coMatrix(i, j).det();
                // (-1) power (i+j)
                if (((i + j) % 2) != 0) {
                    det = getComponentVectorSpace().mul(det, minusOne());
                }
                // transpose needed;
                m.set(j, i, det);
            }
        }
        return m;
    }

    public T det() {
        int tms = getRowCount();

        T det = getComponentVectorSpace().one();
        Matrix<T> matrix = upperTriangle();

        for (int i = 0; i < tms; i++) {
            det = getComponentVectorSpace().mul(det, matrix.get(i, i));
        }      // multiply down diagonal
//        int iDF = 1;
//        det = det.multiply(iDF);                    // adjust w/ determinant factor

        return det;
    }

    public Matrix<T> upperTriangle() {
//        System.out.println(new java.util.Date() + " upperTriangle IN (" + primitiveElement3DS.length + ")");
        VectorSpace<T> cs = getComponentVectorSpace();
        Matrix<T> o = createMatrix(getRowCount(), getColumnCount());
        o.set(this);

        T f1;
        T temp;
        int tms = getRowCount();  // get This Matrix Size (could be smaller than global)
        int v;

        int iDF = 1;

        for (int col = 0; col < tms - 1; col++) {
            for (int row = col + 1; row < tms; row++) {
                v = 1;

                outahere:
                while (o.get(col, col).equals(Complex.ZERO)) // check if 0 in diagonal
                {                                   // if so switch until not
                    if (col + v >= tms) // check if switched all rows
                    {
                        iDF = 0;
                        break outahere;
                    } else {
                        for (int c = 0; c < tms; c++) {
                            temp = o.get(col, c);
                            o.set(col, c, o.get(col + v, c));       // switch rows
                            o.set(col + v, c, temp);
                        }
                        v++;                            // count row switchs
                        iDF = iDF * -1;                 // each switch changes determinant factor
                    }
                }

                if (!o.get(col, col).equals(Complex.ZERO)) {

                    f1 = cs.mul(cs.div(o.get(row, col), o.get(col, col)), minusOne());
                    for (int i = col; i < tms; i++) {
                        o.set(row, i, cs.add(cs.mul(f1, o.get(col, i)), o.get(row, i)));
                    }

                }

            }
        }

//        System.out.println(new java.util.Date() + " upperTriangle OUT");
        return o;
    }

    @Override
    public String format() {
        return format(null, null);
    }

    public String format(String commentsChar, String varName) {
        StringBuilder sb = new StringBuilder();
        if (commentsChar != null) {
            sb.append(commentsChar).append(" dimension [").append(getRowCount()).append(",").append(getColumnCount()).append("]").append(System.getProperty("line.separator"));
        }
        if (varName != null) {
            sb.append(varName).append(" = ");
        }

        int columns = getColumnCount();
        int rows = getRowCount();
        int[] colsWidth = new int[columns];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int len = String.valueOf(get(r, c)).length();
                if (len > colsWidth[c]) {
                    colsWidth[c] = len;
                }
            }
        }

        String lineSep = System.getProperty("line.separator");
        sb.append("[");
        for (int i = 0; i < rows; i++) {
            if (i > 0 || rows > 1) {
                sb.append(lineSep);
            }
            for (int j = 0; j < columns; j++) {
                StringBuilder sbl = new StringBuilder(colsWidth[j]);
                //sbl.clear();
                if (j > 0) {
                    sbl.append(' ');
                }
                String disp = String.valueOf(get(i, j));
                sbl.append(disp);
                int x = colsWidth[j] - disp.length();
                while (x > 0) {
                    sbl.append(' ');
                    x--;
                }
                //sbl.append(' ');
                sb.append(sbl.toString());
            }
        }
        if (rows > 1) {
            sb.append(lineSep);
        }
        sb.append("]");
        return sb.toString();
    }

    public T[][] getArrayCopy() {
        T[][] arr = newT(getRowCount(), getColumnCount());
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[0].length; c++) {
                arr[r][c] = get(r, c);
            }
        }
        return arr;
    }

    public Matrix<T> solve(Matrix<T> B) {
        return solve(B, Maths.Config.getDefaultMatrixSolveStrategy());
    }

    public Matrix<T> solve(Matrix<T> B, SolveStrategy solveStrategy) {
        switch (solveStrategy) {
            case DEFAULT: {
                if (getRowCount() == getColumnCount()) {
                    return new TCLUDecomposition<T>(this).solve(B);
                }
                throw new IllegalArgumentException("Not a square matrix");
            }
//            case OJALGO: {
//                throw new IllegalArgumentException("Unsupported");
//            }
        }
        throw new IllegalArgumentException("Invalid SolveStrategy");
    }

    //    /*****************************************
//     * Solves one of the matrix equations.
//     *    A * X = B
//     *
//     * @param A
//     *   The A matrix.
//     * @param B
//     *   The B matrix.
//     *   Thrown if a paramater is invalid.
//     ****************************************/
//    public static CMatrix
//            solve_AX_B(boolean upper, boolean nounit,
//                       CMatrix A, CMatrix B) {
//        ZVector2 tmpVec1 = new ZVector2();
//        ZVector2 tmpVec2 = new ZVector2();
////        CMatrix tmpVec3 = null;
//        T elt1 = T.ZERO;
//        T elt2 = T.ZERO;
//
//        int m, n, i, j, k;
//
//        m = B.getRowCount();
//        n = B.getColumnCount();
//
//        B = new CMatrix(B); // copy of it
//
//        // Left Side
//        // B = alpha * inv(A) * B
//        if (upper) {
//            for (j = 0; j < n; j++) {
//                for (k = m - 1; k >= 0; k--) {
//                    elt2 = B.get(m, j);
//                    if (!elt2.equals(T.ZERO)) {
//                        if (nounit) {
//                            B.set(elt2.divide(A.get(k, k)), k, j);
//                        }
//                        elt2 = elt2.multiply(-1);
//                        for(int x=0;x<k;x++){
//                            B.set(B.get(x,j).add(B.get(x,k).multiply(elt2)),x,j);
//                        }
//                    }
//                }
//            }
//
//            // !upper
//        } else {
//            for (j = 0; j < n; j++) {
//                for (k = 0; k < m; k++) {
//                    elt2=B.get(k,j);
//                    if (!elt2.equals(T.ZERO)) {
//                        if (nounit) {
//                            elt1 = A.get(k, k);
//                            elt2=elt2.divide(elt1);
//                            B.set(elt2, k, j);
//                        }
//                        elt2 =elt2.multiply(-1);
//                        for(int x=0;x<(m - k - 1);x++){
//                            B.set(B.get(x + (k+1),j).add(B.get(x + (k+1),k).multiply(elt2)),x,j);
//                        }
//                    }
//                }
//            }
//        }
//        return B;
    //    }
    public void store(String file) {
        store(new File(Maths.Config.expandPath(file)));
    }

    public void store(File file) {
        FileOutputStream fileOutputStream = null;
        try {
            try {
                store(new PrintStream(fileOutputStream = new FileOutputStream(file)));
            } finally {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void store(PrintStream stream) {
        store(stream, null, null);
    }

    public void store(String file, String commentsChar, String varName) {
        PrintStream out = null;
        try {
            try {
                out = new PrintStream(file);
                store(out, commentsChar, varName);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void store(File file, String commentsChar, String varName) {
        PrintStream out = null;
        try {
            try {
                out = new PrintStream(file);
                store(out, commentsChar, varName);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void store(PrintStream stream, String commentsChar, String varName) {
        int columns = getColumnCount();
        int rows = getRowCount();
        int[] colsWidth = new int[columns];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int len = String.valueOf(get(r, c)).length();
                if (len > colsWidth[c]) {
                    colsWidth[c] = len;
                }
            }
        }
        if (commentsChar != null) {
            stream.print(commentsChar);
            stream.print(" dimension [" + getRowCount() + "," + getColumnCount() + "]");
            stream.println();
        }
        if (varName != null) {
            stream.print(varName);
            stream.print(" = ");
        }
        stream.print("[");
        stream.println();

        for (int i = 0; i < rows; i++) {
            if (i > 0) {
                stream.println();
            }
            for (int j = 0; j < columns; j++) {
                StringBuilder sbl = new StringBuilder(colsWidth[j]);
                //sbl.clear();
                if (j > 0) {
                    sbl.append(' ');
                }
                String disp = String.valueOf(get(i, j));
                sbl.append(disp);
                sbl.append(' ');
                int x = colsWidth[j] - disp.length();
                while (x > 0) {
                    sbl.append(' ');
                    x--;
                }
                stream.print(sbl.toString());
            }
        }
        stream.println();
        stream.print("]");

    }

    public void read(BufferedReader reader) {
        int rows = getRowCount();
        int columns = getColumnCount();
        ArrayList<ArrayList<T>> l = new ArrayList<ArrayList<T>>(rows > 0 ? rows : 10);
        String line;
        int cols = 0;
        final int START = 0;
        final int LINE = 1;
        final int END = 2;
        int pos = START;
        try {
            try {
                while (pos != END) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    line = line.trim();
                    if (pos == START) {
                        boolean isFirstLine = (line.startsWith("["));
                        if (!isFirstLine) {
                            throw new IOException("Expected a '[' but found '" + line + "'");
                        }
                        line = line.substring(1);
                        pos = LINE;
                    }
                    if (line.endsWith("]")) {
                        line = line.substring(0, line.length() - 1);
                        pos = END;
                    }
                    StringTokenizer stLines = new StringTokenizer(line, ";");
                    while (stLines.hasMoreTokens()) {
                        StringTokenizer stRow = new StringTokenizer(stLines.nextToken(), " \t");
                        ArrayList<T> c = new ArrayList<T>();
                        int someCols = 0;
                        while (stRow.hasMoreTokens()) {
                            c.add(getComponentVectorSpace().parse(stRow.nextToken()));
                            someCols++;
                        }
                        cols = Math.max(cols, someCols);
                        if (c.size() > 0) {
                            l.add(c);
                        }
                    }
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        if (rows == l.size() && columns == cols) {
            //
        } else {
            resize(l.size(), cols);
        }
        for (int i = 0; i < l.size(); i++) {
            for (int j = 0; j < cols; j++) {
                set(i, j, l.get(i).get(j));
            }
        }
    }

    public void read(File file) {
        BufferedReader r = null;
        try {
            try {
                read(r = new BufferedReader(new FileReader(file)));
            } finally {
                if (r != null) {
                    r.close();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void read(String reader) {
        BufferedReader r = null;
        try {
            read(r = new BufferedReader(new StringReader(reader)));
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace(); // should'nt be thrown
                }
            }
        }
    }

    public Vector<T> getRow(final int row) {
        return new MatrixRow<T>(row, this);
    }

    @Override
    public Vector<T> row(int row) {
        return getRow(row);
    }

    @Override
    public final Vector<Vector<T>> rows() {
        return getRows();
    }

    @Override
    public final Vector<Vector<T>> columns() {
        return getColumns();
    }

    public Vector<T> getColumn(final int column) {
        return new MatrixColumn<T>(column, this);
    }

    @Override
    public Vector<T> column(int column) {
        return getColumn(column);
    }

    public boolean isColumn() {
        return getColumnCount() == 1;
    }

    public boolean isRow() {
        return getRowCount() == 1;
    }

    public double[][] absdbls() {
        double[][] d = new double[getRowCount()][getColumnCount()];
        VectorSpace<T> componentVectorSpace = getComponentVectorSpace();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                d[i][j] = componentVectorSpace.absdbl(get(i, j));
            }
        }
        return d;
    }

    public Matrix<T> abs() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> componentVectorSpace = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, (componentVectorSpace.abs(get(i, j))));
            }
        }
        return X;
    }

    @Override
    public Matrix<T> abssqr() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> componentVectorSpace = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, (componentVectorSpace.abssqr(get(i, j))));
            }
        }
        return X;
    }

    public Matrix<T> sqr() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, ss.sqr(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> sqrt() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, ss.sqrt(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> sqrt(int n) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, ss.sqrt(get(i, j), n));
            }
        }
        return X;
    }

    public double cond() {
        return this.norm1() * this.inv().norm1();
    }

    public double cond2() {
        return this.norm2() * this.inv().norm2();
    }

    public double condHadamard() {
        T det = det();
        double x = 1;
        double alpha;
        int rows = getRowCount();
        int cols = getColumnCount();
        VectorSpace<T> componentVectorSpace = getComponentVectorSpace();
        for (int r = 0; r < rows; r++) {
            alpha = 0;
            for (int c = 0; c < cols; c++) {
                alpha = alpha + Maths.sqr(componentVectorSpace.absdbl(get(r, c)));
            }
            x *= Maths.sqrt(alpha);
        }
        return componentVectorSpace.absdbl(det) / x;
    }

    private T minusOne() {
        return getComponentVectorSpace().neg(getComponentVectorSpace().one());
    }

    public Matrix<T> sparsify(double ceil) {
        Matrix<T> array = createMatrix(getRowCount(), getColumnCount());
        double max = Double.NaN;
        int rows = getRowCount();
        int cols = getColumnCount();
        VectorSpace<T> componentVectorSpace = getComponentVectorSpace();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double d = componentVectorSpace.absdbl(get(r, c));
                if (!Double.isNaN(d) && (Double.isNaN(max) || d > max)) {
                    max = d;
                }
            }
        }
        if (!Double.isNaN(max)) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    T v = get(r, c);
                    double d = componentVectorSpace.absdbl(v);
                    if (!Double.isNaN(d)) {
                        d = d / max * 100;
                        if (d <= ceil) {
                            v = componentVectorSpace.zero();
                        }
                    }
                    array.set(r, c, v);
                }
            }
        }
        return array;
    }

    public Complex complexValue() {
        return (getRowCount() == 1 && getColumnCount() == 1) ? getComponentVectorSpace().toComplex(get(0, 0)) : Complex.NaN;
    }

    @Override
    public double doubleValue() {
        return complexValue().doubleValue();
    }

    @Override
    public float floatValue() {
        return complexValue().floatValue();
    }

    @Override
    public int intValue() {
        return complexValue().intValue();
    }

    @Override
    public long longValue() {
        return complexValue().longValue();
    }

    public double maxAbs() {
        double f = 0;
        double f0 = 0;
        int rows = getRowCount();
        int cols = getColumnCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                f0 = getComponentVectorSpace().absdbl(get(r, c));
                f = Math.max(f, f0);
            }
        }
        return f;
    }

    public double minAbs() {
        double f = 0;
        double f0 = 0;
        int rows = getRowCount();
        int cols = getColumnCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                f0 = getComponentVectorSpace().absdbl(get(r, c));
                f = Math.min(f, f0);
            }
        }
        return f;
    }

    public Matrix<T> pow(Matrix<T> power) {
        if (power.isScalar()) {
            return pow(power.get(0, 0));
        } else {
            throw new IllegalArgumentException("Unable to raise Matrix to Matrix Power");
        }

    }

    public Matrix<T> pow(T power) {
        if (isScalar()) {
            return getFactory().newConstant(1, getComponentVectorSpace().pow(get(0, 0), power));
        } else {
            if (getComponentVectorSpace().isComplex(power)) {
                Complex r = getComponentVectorSpace().toComplex(power);
                return pow(r);
            } else {
                throw new IllegalArgumentException("Unable to raise Matrix to Complex power");
            }
        }
    }

    public boolean isScalar() {
        return getRowCount() == 1 && getColumnCount() == 1;
    }

    @Override
    public boolean isComplex() {
        return getRowCount() == 1 && getColumnCount() == 1 && get(0, 0) instanceof Complex;
    }

    public boolean isDouble() {
        return isComplex() && toComplex().isReal();
    }

    @Override
    public Complex toComplex() {
        if (isComplex()) {
            return getComponentVectorSpace().convertTo(get(0, 0), Complex.class);
        }
        throw new ClassCastException();
    }

    public double toDouble() {
        return toComplex().toDouble();
    }

    public void set(Matrix<T> other) {
        int cols = other.getColumnCount();
        int rows = other.getRowCount();
        if (rows != getColumnCount() || rows != getRowCount()) {
            throw new IllegalArgumentException("Columns or Rows count does not match");
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                set(r, c, other.get(r, c));
            }
        }
    }

    public boolean isZero() {
        int columns = getColumnCount();
        int rows = getRowCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (!getComponentVectorSpace().isZero(get(r, c))) {
                    return false;
                }
            }
        }

        return true;
    }

    public void dispose() {

    }

    @Override
    public void resize(int rows, int columns) {
        throw new IllegalArgumentException("Unsupported Resize");
    }

    @Override
    public String getFactoryId() {
        return factoryId;
    }

    @Override
    public MatrixFactory<T> getFactory() {
        if (factory != null) {
            return factory;
        }
        if (factoryId != null) {
            MatrixFactory f = Maths.Config.getTMatrixFactory(factoryId);
            if (f != null) {
                return factory = f;
            }
        }

        MatrixFactory<T> t = createDefaultFactory();
        if (t == null) {
            throw new IllegalArgumentException("Invalid Factory");
        }
        return factory = t;
    }

    @Override
    public void setFactory(MatrixFactory<T> factory) {
        this.factory = factory;
        this.factoryId = factory == null ? null : factory.getId();
    }

    public Vector<T> toVector() {
        if (isColumn()) {
            return getColumn(0);
        }
        if (isRow()) {
            return getRow(0);
        }
        throw new RuntimeException("Not a vector");
    }

    public T scalarProduct(Matrix<T> m) {
        return toVector().scalarProduct(m.toVector());
    }

    public T scalarProduct(Vector<T> v) {
        return toVector().scalarProduct(v);
    }

    public Matrix<T> cos() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().cos(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> acos() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().acos(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> asin() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().asin(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> sin() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().sin(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> acosh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().acosh(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> cosh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().cosh(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> asinh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, ss.asinh(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> sinh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, ss.sinh(get(i, j)));
            }
        }
        return X;
    }

//    public Complex complexValue() {
//        return (getRowCount() == 1 && getColumnCount() == 1) ? get(0, 0) : componentVectorSpace.nan();
//    }

    public Matrix<T> sincard() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, ss.sincard(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> arg() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().arg(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> atan() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().atan(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> cotan() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().cotan(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> tan() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().tan(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> tanh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().tanh(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> cotanh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().cotanh(get(i, j)));
            }
        }
        return X;
    }

    //    public Matrix dotabs() {
//        int rows = getRowCount();
//        int columns = getColumnCount();
//        Matrix X = createMatrix(rows, columns);
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                X.set(i, j, new Complex(get(i, j).absdbl()));
//            }
//        }
//        return X;
//    }
//
    public Matrix<T> getImag() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, (getComponentVectorSpace().imag(get(i, j))));
            }
        }
        return X;
    }

    public Matrix<T> getReal() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().real(get(i, j)));
            }
        }
        return X;
    }

    @Override
    public Matrix<T> imag() {
        return getImag();
    }

    @Override
    public Matrix<T> real() {
        return getReal();
    }

    public Matrix<T> acotan() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, ss.acotan(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> exp() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, ss.exp(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> log() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().log(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> log10() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        VectorSpace<T> ss = getComponentVectorSpace();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, ss.log10(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> db() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().db(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> db2() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().db2(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> dotsqr() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().sqr(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> dotsqrt() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().sqrt(get(i, j)));
            }
        }
        return X;
    }

    public Matrix<T> dotsqrt(int n) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().sqrt(get(i, j), n));
            }
        }
        return X;
    }

    public Matrix<T> dotnpow(int n) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().npow(get(i, j), n));
            }
        }
        return X;
    }

    public Matrix<T> dotpow(double n) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().pow(get(i, j), n));
            }
        }
        return X;
    }

    public Matrix<T> dotpow(T n) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix<T> X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, getComponentVectorSpace().pow(get(i, j), n));
            }
        }
        return X;
    }

    @Override
    public VectorSpace<T> getComponentVectorSpace() {
        return Maths.getVectorSpace(getComponentType());
    }

    @Override
    public Matrix<T> copy() {
        return getFactory().newMatrix(this);
    }

    @Override
    public <R> boolean isConvertibleTo(TypeName<R> other) {
        if (
                Maths.$COMPLEX.equals(other)
                        || Maths.$EXPR.equals(other)
        ) {
            return true;
        }
        if (other.isAssignableFrom(getComponentType())) {
            return true;
        }
        VectorSpace<T> vs = Maths.getVectorSpace(getComponentType());
        for (Vector<T> ts : getRows()) {
            if (!ts.isConvertibleTo(other)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <R> Matrix<R> to(TypeName<R> other) {
        if (other.equals(getComponentType())) {
            return (Matrix<R>) this;
        }
        if (other.equals(Maths.$EXPR)) {
            return (Matrix<R>) new ExprMatrixFromMatrix<T>(this);
        }
        if (other.equals(Maths.$COMPLEX)) {
            return (Matrix<R>) new ComplexMatrixFromComplexMatrix<T>(this);
        }
        if (other.equals(Maths.$DOUBLE)) {
            return (Matrix<R>) new DoubleMatrixFromMatrix<T>(this);
        }
        throw new IllegalArgumentException("Unsupported");
//        //should i check default types?
//        if (Maths.$COMPLEX.isAssignableFrom(other)) {
//            return (Vector<R>) new ReadOnlyVector(
//                    new VectorModel() {
//                        @Override
//                        public Complex get(int index) {
//                            T v = AbstractVector.this.get(index);
//                            return getComponentVectorSpace().convertTo(v, Complex.class);
//                        }
//
//                        @Override
//                        public int size() {
//                            return AbstractVector.this.size();
//                        }
//                    }, isRow()
//            );
//        }
//        return newReadOnlyInstanceFromModel(
//                other, isRow(), new VectorModel<R>() {
//                    @Override
//                    public R get(int index) {
//                        T t = AbstractVector.this.get(index);
//                        return (R) t;
//                    }
//
//                    @Override
//                    public int size() {
//                        return AbstractVector.this.size();
//                    }
//                }
//        );
    }

    @Override
    public boolean isHermitian() {
        return isSquare() && equals(transposeConjugate());
    }

    @Override
    public boolean isSymmetric() {
        return isSquare() && equals(transpose());
    }

    public boolean isSquare() {
        return getColumnCount() == getRowCount();
    }

    private T[][] newT(int size1, int size2) {
        return ArrayUtils.newArray(getComponentType().getTypeClass(), size1, size2);
    }

    private T[] newT(int size) {
        return ArrayUtils.newArray(getComponentType().getTypeClass(), size);
    }

    private void checkSquare() {
        if (!isSquare()) {
            throw new IllegalArgumentException("Expected Square Matrix");
        }
    }

    public Matrix<T> pow(Complex power) {
        if (power.isReal()) {
            return pow(power.toReal());
        }
        throw new IllegalArgumentException("Unable to raise Matrix to Complex Power");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        int columnCount = getColumnCount();
        int rowCount = getRowCount();
        hash = 89 * hash + columnCount;
        hash = 89 * hash + rowCount;
        for (int c = 0; c < columnCount; c++) {
            for (int r = 0; r < rowCount; r++) {
                T t = get(r, c);
                if (t != null) {
                    hash = 89 * hash + t.hashCode();
                }
            }
        }
        return hash;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Matrix)) {
            return false;
        }

        Matrix<?> that = (Matrix<?>) o;
        int columnCount = getColumnCount();
        int rowCount = getRowCount();
        if (that.getColumnCount() != columnCount) {
            return false;
        }
        if (that.getRowCount() != rowCount) {
            return false;
        }
        if (!that.getComponentType().equals(getComponentType())) {
            return false;
        }
        for (int c = 0; c < columnCount; c++) {
            for (int r = 0; r < rowCount; r++) {
                if (!Objects.equals(get(r, c), that.get(r, c))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return format();
    }

    @Override
    public Iterator<Vector<T>> iterator() {
        return getRows().toList().iterator();
    }


}
