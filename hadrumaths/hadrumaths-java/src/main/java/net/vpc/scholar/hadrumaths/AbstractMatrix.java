package net.vpc.scholar.hadrumaths;

//import net.vpc.scholar.hadrumaths.interop.ojalgo.OjalgoHelper;

import net.vpc.scholar.hadrumaths.util.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public abstract class AbstractMatrix extends AbstractTMatrix<Complex> implements Matrix {

    private static final long serialVersionUID = -1010101010101001044L;
    private transient MatrixFactory factory;
    private String factoryId;

//    /*To exchange two rows in a matrix*/
//    public static void exchange_row(TMatrix<Complex> M, int k, int l, int m, int n) {
//        if (k <= 0 || l <= 0 || k > n || l > n || k == l) {
//            return;
//        }
//        Complex tmp;
//        for (int j = 0; j < n; j++) {
//            tmp = M.get(k - 1, j);
//            M.set(k - 1, j, M.get(l - 1, j));
//            M.set(l - 1, j, tmp);
//        }
//    }

    public double norm() {
        return norm(NormStrategy.DEFAULT);
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
                s += get(r, c).absdbl();
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
                f += get(r, c).absdblsqr();
            }
        }
        return Math.sqrt(f);
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
                f = Math.max(f, get(r, j).absdbl());
            }
        }
        return f;
    }

    public double getError(TMatrix<Complex> baseMatrix) {
        double norm = baseMatrix.norm(NormStrategy.DEFAULT);
        if (norm == 0) {
            return this.norm(NormStrategy.DEFAULT);
        }
        return (this.sub(baseMatrix).norm(NormStrategy.DEFAULT) / norm);
    }

    @Override
    public double getDistance(Normalizable other) {
        return getError((TMatrix<Complex>) other);
    }

    public DMatrix getErrorMatrix(TMatrix<Complex> baseMatrix, double minErrorForZero, ErrorMatrixStrategy strategy) {
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

    public DMatrix getErrorMatrix(TMatrix<Complex> baseMatrix) {
        return getErrorMatrix(baseMatrix, Double.NaN);
    }

    public DMatrix getErrorMatrix(TMatrix<Complex> baseMatrix, double minErrorForZero) {
        TMatrix<Complex> m = baseMatrix.sub(this).div(baseMatrix.norm3());
        Complex[][] mm = m.getArray();
        double[][] d = new double[mm.length][mm[0].length];
        for (int i = 0; i < mm.length; i++) {
            Complex[] complexes = mm[i];
            for (int j = 0; j < complexes.length; j++) {
                if (!Double.isNaN(minErrorForZero) && complexes[j].absdbl() < minErrorForZero) {
                    d[i][j] = 0;
                } else {
                    d[i][j] = complexes[j].absdbl();
                }
            }
        }
        return new DMatrix(d);
    }

    /**
     * term by term
     *
     * @param baseMatrix
     * @param minErrorForZero
     * @return
     */
    public DMatrix getErrorMatrix2(TMatrix<Complex> baseMatrix, double minErrorForZero) {
        TMatrix<Complex> m = baseMatrix.sub(this);
        Complex[][] mm = m.getArray();
        double[][] d = new double[mm.length][mm[0].length];
        for (int i = 0; i < mm.length; i++) {
            Complex[] complexes = mm[i];
            for (int j = 0; j < complexes.length; j++) {
                double baseAbs = baseMatrix.get(i, j).absdbl();
                double newAbs = complexes[j].absdbl();
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

    public DMatrix getErrorMatrix3(TMatrix<Complex> baseMatrix, double minErrorForZero) {
        TMatrix<Complex> m = baseMatrix.sub(this);
        Complex[][] mm = m.getArray();
        double[][] d = new double[mm.length][mm[0].length];
        for (int i = 0; i < mm.length; i++) {
            Complex[] complexes = mm[i];
            for (int j = 0; j < complexes.length; j++) {
                Complex base = baseMatrix.get(i, j);
                Complex val = complexes[j];
                double baseR = Math.abs(base.getReal());
                double baseI = Math.abs(base.getImag());
                double valR = Math.abs(val.getReal());
                double valI = Math.abs(val.getImag());
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
                s += get(r, c).absdbl();
            }
            f = Math.max(f, s);
        }
        return f;
    }

    @Override
    public Complex avg() {
        MutableComplex f = MutableComplex.Zero();
        int rows = getRowCount();
        int columns = getColumnCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                f.add(get(r, c));
            }
        }
        f.div(rows * columns);
        return f.toComplex();
    }

    @Override
    public Complex sum() {
        MutableComplex f = MutableComplex.Zero();
        int rows = getRowCount();
        int columns = getColumnCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                f.add(get(r, c));
            }
        }
        return f.toComplex();
    }

    @Override
    public Complex prod() {
        MutableComplex f = MutableComplex.One();
        int rows = getRowCount();
        int columns = getColumnCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                f.mul(get(r, c));
            }
        }
        return f.toComplex();
    }

    public void setAll(Complex value) {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                set(i, j, value);
            }
        }
    }

    public Complex[][] getArray() {
        Complex[][] arr = new Complex[getRowCount()][getColumnCount()];
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[0].length; c++) {
                arr[r][c] = get(r, c);
            }
        }
        return arr;
    }

    protected Matrix createMatrix(int rows, int cols) {
        MatrixFactory f = getFactory();
        if (f == null) {
            f = Maths.Config.getDefaultMatrixFactory();
        }
        return f.newMatrix(rows, cols);
    }

    /**
     * Get a submatrix.
     * TODO check me pleaze
     *
     * @param i0 Initial row index
     * @param i1 Final row index
     * @param j0 Initial column index
     * @param j1 Final column index
     * @return A(i0:i1, j0:j1)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public Matrix getMatrix(int i0, int i1, int j0, int j1) {
        Matrix X = createMatrix(i1 - i0 + 1, j1 - j0 + 1);
        //Complex[][] B = X.elements;// X.getArray();
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
     * @return A(r(:), c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public Matrix getMatrix(int[] r, int[] c) {
        Matrix X = createMatrix(r.length, c.length);
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
     * @return A(i0:i1, c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public Matrix getMatrix(int r1, int r2, int[] c) {
        Matrix X = createMatrix(r2 - r1 + 1, c.length);
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
     * @return A(r(:), j0:j1)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public Matrix getMatrix(int[] r, int c1, int c2) {
        Matrix X = createMatrix(r.length, c2 - c1 + 1);
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

    public Matrix div(double c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).div(c));
            }
        }
        return X;
    }

    public Matrix div(Complex c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).div(c));
            }
        }
        return X;
    }

    public Matrix mul(Complex c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).mul(c));
            }
        }
        return X;
    }

    public Matrix mul(double c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).mul(c));
            }
        }
        return X;
    }

    public Matrix neg() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).neg());
            }
        }
        return X;
    }

    public Matrix add(Complex c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).add(c));
            }
        }
        return X;
    }

    public Matrix sub(Complex c) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).sub(c));
            }
        }
        return X;
    }

    public Matrix div(TMatrix<Complex> other) {
        return mul(other.inv());
    }

    public Matrix mul(TMatrix<Complex> other) {
        if (getColumnCount() != other.getRowCount()) {
            throw new IllegalArgumentException("The column dimension " + getColumnCount() + " of the left matrix does not match the row dimension " + other.getRowCount() + " of the right matrix!");
        }
        int a_rows = getRowCount();
        int b_cols = other.getColumnCount();
        int b_rows = other.getRowCount();
        Matrix newElements = createMatrix(a_rows, b_cols);
        for (int i = 0; i < a_rows; i++) {
            for (int j = 0; j < b_cols; j++) {
                MutableComplex sum = MutableComplex.Zero();
                for (int k = 0; k < b_rows; k++) {
                    sum.addProduct(get(i, k), (other.get(k, j)));
                }
                newElements.set(i, j, sum);
            }
        }
        return newElements;
    }

    public Matrix mul(Complex[][] other) {
        if (getColumnCount() != other.length) {
            throw new IllegalArgumentException("The column dimension " + getColumnCount() + " of the left matrix does not match the row dimension " + other.length + " of the right matrix!");
        }
        int a_rows = getRowCount();
        int b_cols = other[0].length;
        int b_rows = other.length;
        Matrix newElements = createMatrix(a_rows, b_cols);
        for (int i = 0; i < a_rows; i++) {
            for (int j = 0; j < b_cols; j++) {
                MutableComplex sum = MutableComplex.Zero();
                for (int k = 0; k < b_rows; k++) {
                    sum.addProduct(get(i, k), (other[k][j]));
                }
                newElements.set(i, j, sum);
            }
        }
        return newElements;
    }

    public Matrix dotmul(TMatrix<Complex> other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).mul(other.get(i, j)));
            }
        }
        return X;
    }

    public Matrix dotdiv(TMatrix<Complex> other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).div(other.get(i, j)));
            }
        }
        return X;
    }

    public Matrix conj() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).conj());
            }
        }
        return X;
    }

    public Matrix dotinv() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).inv());
            }
        }
        return X;
    }

    public Matrix cos() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).cos());
            }
        }
        return X;
    }

    public Matrix acos() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).acos());
            }
        }
        return X;
    }

    public Matrix asin() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).asin());
            }
        }
        return X;
    }


    public Matrix sin() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).sin());
            }
        }
        return X;
    }

    public Matrix acosh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).acosh());
            }
        }
        return X;
    }

    public Matrix cosh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).cosh());
            }
        }
        return X;
    }

    public Matrix asinh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).asinh());
            }
        }
        return X;
    }

    public Matrix sinh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).sinh());
            }
        }
        return X;
    }

    public Matrix arg() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).arg());
            }
        }
        return X;
    }

    public Matrix atan() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).atan());
            }
        }
        return X;
    }

    public Matrix cotan() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).cotan());
            }
        }
        return X;
    }

    public Matrix tan() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).tan());
            }
        }
        return X;
    }

    public Matrix tanh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).tanh());
            }
        }
        return X;
    }

    public Matrix cotanh() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).cotanh());
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
    public Matrix getImag() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, Complex.valueOf(get(i, j).getImag()));
            }
        }
        return X;
    }

    @Override
    public DMatrix getRealDMatrix() {
        int rows = getRowCount();
        int columns = getColumnCount();
        double[][] X = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = (get(i, j).getReal());
            }
        }
        return new DMatrix(X);
    }

    @Override
    public DMatrix getImagDMatrix() {
        int rows = getRowCount();
        int columns = getColumnCount();
        double[][] X = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = (get(i, j).getImag());
            }
        }
        return new DMatrix(X);
    }

    @Override
    public DMatrix getAbsDMatrix() {
        int rows = getRowCount();
        int columns = getColumnCount();
        double[][] X = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = (get(i, j).absdbl());
            }
        }
        return new DMatrix(X);
    }

    @Override
    public DMatrix getAbsSquareDMatrix() {
        int rows = getRowCount();
        int columns = getColumnCount();
        double[][] X = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = (get(i, j).absdblsqr());
            }
        }
        return new DMatrix(X);
    }

    @Override
    public Matrix imag() {
        return getImag();
    }

    @Override
    public Matrix real() {
        return getReal();
    }

    public Matrix getReal() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, Complex.valueOf(get(i, j).getReal()));
            }
        }
        return X;
    }

    public double[][] getDoubleArray() {
        int rows = getRowCount();
        int columns = getColumnCount();
        double[][] X = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = get(i, j).toDouble();
            }
        }
        return X;
    }

    public double[][] getRealArray() {
        int rows = getRowCount();
        int columns = getColumnCount();
        double[][] X = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = get(i, j).getReal();
            }
        }
        return X;
    }

    public double[][] getImagArray() {
        int rows = getRowCount();
        int columns = getColumnCount();
        double[][] X = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = get(i, j).getReal();
            }
        }
        return X;
    }

    public double[][] getAbsArray() {
        int rows = getRowCount();
        int columns = getColumnCount();
        double[][] X = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = get(i, j).absdbl();
            }
        }
        return X;
    }

    public double[][] getAbsSquareArray() {
        int rows = getRowCount();
        int columns = getColumnCount();
        double[][] X = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = get(i, j).absdblsqr();
            }
        }
        return X;
    }

    public Matrix acotan() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).acotan());
            }
        }
        return X;
    }

    public Matrix exp() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).exp());
            }
        }
        return X;
    }

    public Matrix log() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).log());
            }
        }
        return X;
    }

    public Matrix log10() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).log10());
            }
        }
        return X;
    }

    public Matrix db() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).db());
            }
        }
        return X;
    }

    public Matrix db2() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).db2());
            }
        }
        return X;
    }

    public Matrix dotsqr() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).sqr());
            }
        }
        return X;
    }

    public Matrix dotsqrt() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).sqrt());
            }
        }
        return X;
    }

    public Matrix dotsqrt(int n) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).sqrt(n));
            }
        }
        return X;
    }

    public Matrix dotnpow(int n) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).npow(n));
            }
        }
        return X;
    }

    public Matrix dotpow(double n) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).pow(n));
            }
        }
        return X;
    }

    public Matrix dotpow(Complex n) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).pow(n));
            }
        }
        return X;
    }

    public Matrix add(TMatrix<Complex> other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).add(other.get(i, j)));
            }
        }
        return X;
    }

    public Matrix add(Complex[][] other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).add(other[i][j]));
            }
        }
        return X;
    }

    public Matrix sub(Complex[][] other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).sub(other[i][j]));
            }
        }
        return X;
    }

    public Matrix sub(TMatrix<Complex> other) {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).sub(other.get(i, j)));
            }
        }
        return X;
    }

    public Complex getScalar() {
        return get(0, 0);
    }

    public ComponentDimension getComponentDimension() {
        return ComponentDimension.create(
                getRowCount(),
                getColumnCount()
        );
    }

    public Complex get(int vectorIndex) {
        if (getColumnCount() == 1) {
            return get(vectorIndex, 0);
        }
        if (getRowCount() == 1) {
            return get(0, vectorIndex);
        }
        throw new IllegalArgumentException("No a valid vector");
    }

    public void add(int row, int col, Complex val) {
        set(row, col, get(row, col).add(val));
    }

    public void mul(int row, int col, Complex val) {
        set(row, col, get(row, col).mul(val));
    }

    public void div(int row, int col, Complex val) {
        set(row, col, get(row, col).div(val));
    }

    public void sub(int row, int col, Complex val) {
        set(row, col, get(row, col).sub(val));
    }


    public void set(int row, int col, TMatrix<Complex> src, int srcRow, int srcCol, int rows, int cols) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                set(r + row, col + c, src.get(r + srcRow, c + srcCol));
            }
        }
    }

    public void set(int row, int col, TMatrix<Complex> subMatrix) {
        set(row, col, subMatrix, 0, 0, subMatrix.getRowCount(), subMatrix.getColumnCount());
    }

    public Matrix subMatrix(int row, int col, int rows, int cols) {
        Matrix m = createMatrix(rows, cols);
        m.set(row, col, this);
        return m;
    }

    public final Matrix transpose() {
        return arrayTranspose();
    }

    /**
     * @return equivalent to transposeHermitian
     */
    public final Matrix transjugate() {
        return transposeHermitian();
    }

    /**
     * @return equivalent to transposeHermitian
     */
    public final Matrix transposeConjugate() {
        return transposeHermitian();
    }

    /**
     * Hermitian conjugate
     *
     * @return
     */
    public Matrix transposeHermitian() {
        int r = getRowCount();
        if (r == 0) {
            return this;
        }
        int c = getColumnCount();
        Matrix e = createMatrix(c, r);
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                e.set(j, i, get(i, j).conj());
            }
        }
        return e;
    }

    public Matrix arrayTranspose() {
        int r = getRowCount();
        if (r == 0) {
            return this;
        }
        int c = getColumnCount();
        Matrix e = createMatrix(c, r);
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                e.set(j, i, get(i, j));
            }
        }
        return e;
    }

    public Matrix invCond() {
        return inv(Maths.Config.getDefaultMatrixInverseStrategy(), ConditioningStrategy.DEFAULT, NormStrategy.DEFAULT);
    }

    public Matrix inv(InverseStrategy invStr, ConditioningStrategy condStr, NormStrategy normStr) {
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

    public Matrix inv() {
        return inv(Maths.Config.getDefaultMatrixInverseStrategy());
    }

    public Matrix inv(InverseStrategy st) {
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
//                return OjalgoHelper.INSTANCE.inv(this);
//            }
        }
        throw new UnsupportedOperationException("strategy " + st.toString());
    }

    public Matrix invSolve() {
        return solve(Maths.identityMatrix(getRowCount()));
    }

    /**
     * inversion using Bloc Matrix expansion
     *
     * @return inverse
     */
    public Matrix invBlock(InverseStrategy delegate, int precision) {
        int n = getRowCount();
        int p = getColumnCount();
        if (n != p || (n > 1 && n % 2 != 0) || (precision > 1 && n > 1 && n <= precision)) {
            return inv(delegate);
        }
        switch (n) {
            case 1: {
                return getFactory().newMatrix(new Complex[][]{{get(0, 0).inv()}});
            }
            case 2: {
                Complex A = get(0, 0);
                Complex B = get(0, 1);
                Complex C = get(1, 0);
                Complex D = get(1, 1);
                Complex Ai = A.inv();
                Complex CAi = C.mul(Ai);
                Complex AiB = Ai.mul(B);
                Complex DCABi = D.sub(C.mul(Ai).mul(B)).inv();
                Complex mDCABi = DCABi.mul(-1);
                Complex AiBmDCABi = AiB.mul(mDCABi);
                return Maths.matrix(new Complex[][]{
                        {Ai.sub(AiBmDCABi.mul(CAi)), AiBmDCABi},
                        {mDCABi.mul(CAi), DCABi}
                });
            }
            default: {
                int n2 = n / 2;
                Matrix A = getMatrix(0, n2 - 1, 0, n2 - 1);
                Matrix B = getMatrix(n2, n - 1, 0, n2 - 1);
                Matrix C = getMatrix(0, n2 - 1, n2, n - 1);
                Matrix D = getMatrix(n2, n - 1, n2, n - 1);
                Matrix Ai = A.invBlock(delegate, precision);
                Matrix CAi = C.mul(Ai);
                Matrix AiB = Ai.mul(B);
                Matrix DCABi = D.sub(C.mul(Ai).mul(B)).invBlock(delegate, precision);
                Matrix mDCABi = DCABi.mul(-1);
                Matrix AiBmDCABi = AiB.mul(mDCABi);
                Matrix mm = createMatrix(getRowCount(), getColumnCount());
                mm.set(new Matrix[][]{
                        {Ai.sub(AiBmDCABi.mul(CAi)), AiBmDCABi},
                        {mDCABi.mul(CAi), DCABi}
                });
                return mm;
            }
            //case 2?
        }
    }

    public Matrix invGauss() {
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
        Matrix A = createMatrix(m, n);
        Matrix B = createMatrix(m, n);

        //Copie de M dans A et Mise en forme de B : B=I
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, get(i, j));
                if (i == j) {
                    B.set(i, j, Complex.ONE);
                } else {
                    B.set(i, j, Complex.ZERO);
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
        Complex a, tmp;

        for (int k = 0; k < n && bk; k++) {
            if (!I.contains(k)) {
                I.add(k);
                cnt_row++;
                bl = true;
                for (int l = 0; l < n && bl; l++) {
                    if (!J.contains(l)) {
                        a = A.get(k, l);
                        if (!a.isZero()) {
                            J.add(l);
                            cnt_col++;
                            bl = false; //permet de sortir de la boucle car le pivot a été trouvé
                            for (int p = 0; p < n; p++) {
                                if (p != k) {
                                    tmp = A.get(p, l);
                                    for (int q = 0; q < n; q++) {
                                        A.set(p, q, A.get(p, q).sub(A.get(k, q).mul(tmp.div(a))));
                                        B.set(p, q, B.get(p, q).sub(B.get(k, q).mul((tmp.div(a)))));
                                    }
                                }
                            }
                        }
                    }
                }
                if (cnt_row != cnt_col) {
                    //Matrix is singular";
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
                    if (!a.isZero()) {
                        A.set(k, l, Complex.ONE);
                        for (int p = 0; p < n; p++) {
                            B.set(k, p, B.get(k, p).div(a));
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
    public Matrix invAdjoint() {
        // Formula used to Calculate Inverse:
        // inv(A) = 1/det(A) * adj(A)
        int tms = getRowCount();

        Complex[][] m = new Complex[tms][tms];

        Complex det = det();

        if (det.equals(Complex.ZERO)) {
            throw new ArithmeticException("Determinant Equals 0, Not Invertible.");
        }

//            System.out.println("determinant is " + det);
        Complex dd = det.inv();
        if (tms == 1) {
            return getFactory().newMatrix(new Complex[][]{{dd}});
        }
        Matrix mm = adjoint();
        for (int i = 0; i < tms; i++) {
            for (int j = 0; j < tms; j++) {
                m[i][j] = dd.mul(mm.get(i, j));
            }
        }

        return mm;
    }

    public Matrix coMatrix(int row, int col) {
        int tms = getRowCount();
        Matrix ap = createMatrix(tms - 1, tms - 1);
        int ia = 0;
        int ja;
        for (int ii = 0; ii < row; ii++) {
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

    public Matrix pow(int exp) {
        switch (exp) {
            case 0: {
                return Maths.identityMatrix(this);
            }
            case 1: {
                return this;
            }
            case -1: {
                return inv();
            }
            default: {
                if (exp > 0) {
                    Matrix m = this;
                    while (exp > 1) {
                        m = m.mul(this);
                        exp--;
                    }
                    return m;
                } else {
                    Matrix m = this;
                    int t = -exp;
                    while (t > 1) {
                        m = m.mul(this);
                        t--;
                    }
                    ;
                    m = m.inv();
                    return m;
                }
            }
        }
    }

    public Matrix adjoint() {
        int tms = getRowCount();

        Matrix m = createMatrix(tms, tms);

        for (int i = 0; i < tms; i++) {
            for (int j = 0; j < tms; j++) {
                Complex det = coMatrix(i, j).det();
                // (-1) power (i+j)
                if (((i + j) % 2) != 0) {
                    det = det.mul(-1);
                }
                // transpose needed;
                m.set(j, i, det);
            }
        }
        return m;
    }

    public Complex det() {
        int tms = getRowCount();

        Complex det = Complex.ONE;
        Matrix matrix = upperTriangle();

        for (int i = 0; i < tms; i++) {
            det = det.mul(matrix.get(i, i));
        }      // multiply down diagonal
//        int iDF = 1;
//        det = det.multiply(iDF);                    // adjust w/ determinant factor

        return det;
    }

    public Matrix upperTriangle() {
//        System.out.println(new java.util.Date() + " upperTriangle IN (" + elements.length + ")");
        Matrix o = createMatrix(getRowCount(), getColumnCount());
        o.set(this);

        Complex f1;
        Complex temp;
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

                    f1 = o.get(row, col).div(o.get(col, col)).mul(-1);
                    for (int i = col; i < tms; i++) {
                        o.set(row, i, f1.mul(o.get(col, i)).add(o.get(row, i)));
                    }

                }

            }
        }

//        System.out.println(new java.util.Date() + " upperTriangle OUT");
        return o;
    }

    @Override
    public String toString() {
        return format();
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

    public Complex[][] getArrayCopy() {
        Complex[][] arr = new Complex[getRowCount()][getColumnCount()];
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[0].length; c++) {
                arr[r][c] = get(r, c);
            }
        }
        return arr;
    }

    public Matrix solve(TMatrix<Complex> B) {
        return solve(B, Maths.Config.getDefaultMatrixSolveStrategy());
    }

    protected Matrix castToMatrix(TMatrix<Complex> a) {
        if (a instanceof Matrix) {
            return (Matrix) a;
        }
        throw new IllegalArgumentException("Not Supported yet");
    }

    public Matrix solve(TMatrix<Complex> B, SolveStrategy solveStrategy) {
        switch (solveStrategy) {
            case DEFAULT: {
                if (getRowCount() == getColumnCount()) {
                    return new CLUDecomposition(this).solve(castToMatrix(B));
                }
                throw new IllegalArgumentException("Not a square matrix");
            }
//            case OJALGO: {
//                return OjalgoHelper.INSTANCE.solve(this, castToMatrix(B));
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
//        Complex elt1 = Complex.ZERO;
//        Complex elt2 = Complex.ZERO;
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
//                    if (!elt2.equals(Complex.ZERO)) {
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
//                    if (!elt2.equals(Complex.ZERO)) {
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
    public void store(String file) throws RuntimeIOException {
        store(new File(Maths.Config.expandPath(file)));
    }

    public void store(File file) throws RuntimeIOException {
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
            throw new RuntimeIOException(ex);
        }
    }

    public void store(PrintStream stream) throws RuntimeIOException {
        store(stream, null, null);
    }


    public void store(String file, String commentsChar, String varName) throws RuntimeIOException {
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
            throw new RuntimeIOException(ex);
        }
    }

    public void store(File file, String commentsChar, String varName) throws RuntimeIOException {
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
            throw new RuntimeIOException(ex);
        }
    }


    public void store(PrintStream stream, String commentsChar, String varName) throws RuntimeIOException {
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

    public void read(File file) throws RuntimeIOException {
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
            throw new RuntimeIOException(ex);
        }
    }

    public void read(BufferedReader reader) throws RuntimeIOException {
        int rows = getRowCount();
        int columns = getColumnCount();
        ArrayList<ArrayList<Complex>> l = new ArrayList<ArrayList<Complex>>(rows > 0 ? rows : 10);
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
                            throw new RuntimeIOException("Expected a '[' but found '" + line + "'");
                        }
                        line = line.substring(1, line.length());
                        pos = LINE;
                    }
                    if (line.endsWith("]")) {
                        line = line.substring(0, line.length() - 1);
                        pos = END;
                    }
                    StringTokenizer stLines = new StringTokenizer(line, ";");
                    while (stLines.hasMoreTokens()) {
                        StringTokenizer stRow = new StringTokenizer(stLines.nextToken(), " \t");
                        ArrayList<Complex> c = new ArrayList<Complex>();
                        int someCols = 0;
                        while (stRow.hasMoreTokens()) {
                            c.add(Complex.valueOf(stRow.nextToken()));
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
            throw new RuntimeIOException(ex);
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

    @Override
    public TVector<TVector<Complex>> getRows() {
        return (TVector<TVector<Complex>>) Maths.<TVector<Complex>>columnTVector(Maths.$CVECTOR, new TVectorModel<TVector<Complex>>() {
            @Override
            public int size() {
                return getRowCount();
            }

            @Override
            public TVector<Complex> get(int index) {
                return getRow(index);
            }
        });
    }

    @Override
    public TVector<TVector<Complex>> getColumns() {
        return (TVector<TVector<Complex>>) Maths.<TVector<Complex>>columnTVector(Maths.$CVECTOR, new TVectorModel<TVector<Complex>>() {
            @Override
            public int size() {
                return getColumnCount();
            }

            @Override
            public TVector<Complex> get(int index) {
                return getColumn(index);
            }
        });
    }

    @Override
    public Vector row(int row) {
        return getRow(row);
    }

    @Override
    public Vector column(int column) {
        return getColumn(column);
    }

    public Vector getRow(final int row) {
        return new MatrixRow(row, this);
    }

    public Vector toVector() {
        if (isColumn()) {
            return getColumn(0);
        }
        if (isRow()) {
            return getRow(0);
        }
        throw new RuntimeException("Not a vector " + getRowCount() + "x" + getColumnCount());
    }

    public Complex scalarProduct(boolean hermitian, TMatrix<Complex> m) {
        return toVector().scalarProduct(hermitian, castToMatrix(m).toVector());
    }

    public Complex scalarProduct(boolean hermitian, TVector<Complex> v) {
        return toVector().scalarProduct(hermitian, v);
    }

    public boolean isColumn() {
        return getColumnCount() == 1;
    }

    public boolean isRow() {
        return getRowCount() == 1;
    }

    @Override
    public void update(int row, int col, Complex val) {
        set(row, col, val);
    }

    @Override
    public void update(int row, int col, TMatrix<Complex> subMatrix) {
        set(row, col, subMatrix);
    }

    public Vector getColumn(final int column) {
        return new MatrixColumn(column, this);
    }

    public double[][] absdbls() {
        double[][] d = new double[getRowCount()][getColumnCount()];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                d[i][j] = get(i, j).absdbl();
            }
        }
        return d;
    }

    public Matrix abs() {
        int rows = getRowCount();
        int columns = getColumnCount();
        Matrix X = createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X.set(i, j, get(i, j).absdbl(), 0);
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
        Complex det = det();
        double x = 1;
        double alpha;
        int rows = getRowCount();
        int cols = getColumnCount();
        for (int r = 0; r < rows; r++) {
            alpha = 0;
            for (int c = 0; c < cols; c++) {
                alpha = alpha + get(r, c).absdblsqr();
            }
            x *= Math.sqrt(alpha);
        }

        return det.absdbl() / x;
    }

    public Matrix sparsify(double ceil) {
        Matrix array = createMatrix(getRowCount(), getColumnCount());
        double max = Double.NaN;
        int rows = getRowCount();
        int cols = getColumnCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double d = get(r, c).absdbl();
                if (!Double.isNaN(d) && (Double.isNaN(max) || d > max)) {
                    max = d;
                }
            }
        }
        if (!Double.isNaN(max)) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    Complex v = get(r, c);
                    double d = v.absdbl();
                    if (!Double.isNaN(d)) {
                        d = d / max * 100;
                        if (d <= ceil) {
                            v = Complex.ZERO;
                        }
                    }
                    array.set(r, c, v);
                }
            }
        }
        return array;
    }

    public Complex complexValue() {
        return (getRowCount() == 1 && getColumnCount() == 1) ? get(0, 0) : Complex.NaN;
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
                f0 = get(r, c).absdbl();
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
                f0 = get(r, c).absdbl();
                f = Math.min(f, f0);
            }
        }
        return f;
    }

    public Matrix pow(Matrix power) {
        if (power.isScalar()) {
            return pow(power.get(0, 0));
        } else {
            throw new IllegalArgumentException("Unable to raise Matrix to Matrix Power");
        }

    }

    @Override
    public Matrix pow(Complex power) {
        if (isScalar()) {
            return Maths.constantMatrix(1, get(0, 0).pow(power));
        } else {
            if (power.isReal()) {
                double r = power.getReal();
                int ir = (int) r;
                if (r == ir) {
                    return pow(ir);
                } else {
                    throw new IllegalArgumentException("Unable to raise Matrix to Non integer power");
                }
            } else {
                throw new IllegalArgumentException("Unable to raise Matrix to Complex power");
            }
        }
    }

    public boolean isScalar() {
        return getRowCount() == 1 && getColumnCount() == 1;
    }

    public boolean isComplex() {
        return getRowCount() == 1 && getColumnCount() == 1;
    }

    public boolean isDouble() {
        return isComplex() && toComplex().isDouble();
    }

    public Complex toComplex() {
        if (isComplex()) {
            return get(0, 0);
        }
        throw new ClassCastException();
    }

    public double toDouble() {
        return toComplex().toDouble();
    }


    public boolean isZero() {
        int columns = getColumnCount();
        int rows = getRowCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (!get(r, c).isZero()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void set(Matrix[][] subMatrixes) {
        set((TMatrix<Complex>[][]) subMatrixes);
    }

    public void set(TMatrix<Complex>[][] subMatrixes) {
        int rows = 0;
        int cols = 0;
        for (TMatrix<Complex>[] subMatrixe : subMatrixes) {
            int r = 0;
            int c = 0;
            for (TMatrix<Complex> aSubMatrixe : subMatrixe) {
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
        for (TMatrix<Complex>[] subMatrixe1 : subMatrixes) {
            col = 0;
            for (TMatrix<Complex> aSubMatrixe1 : subMatrixe1) {
                set(row, col, aSubMatrixe1);
                col += aSubMatrixe1.getColumnCount();
            }
            row += subMatrixe1[0].getRowCount();
        }
    }

    public void set(TMatrix<Complex> other) {
        int cols = other.getColumnCount();
        int rows = other.getRowCount();
        if (cols != getColumnCount() || rows != getRowCount()) {
            throw new IllegalArgumentException("Columns or Rows count does not match");
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                set(r, c, other.get(r, c));
            }
        }
    }

    public void dispose() {

    }

    @Override
    public String getFactoryId() {
        return factoryId;
    }

    @Override
    public MatrixFactory getFactory() {
        if (factory != null) {
            return factory;
        }
        if (factoryId != null) {
            return factory = (MatrixFactory) Maths.Config.getTMatrixFactory(factoryId);
        }
        MatrixFactory df = Maths.Config.getDefaultMatrixFactory();
        if (df == null) {
            throw new IllegalArgumentException("Invalid Factory");
        }
        return df;
    }

    @Override
    public void setFactory(TMatrixFactory<Complex> factory) {
        this.factory = (MatrixFactory) factory;
        this.factoryId = factory == null ? null : factory.getId();
    }

    @Override
    public Complex apply(int row, int col) {
        return get(row, col);
    }

    @Override
    public Complex apply(int vectorIndex) {
        return get(vectorIndex);
    }



    private void checkSquare() {
        if (!isSquare()) {
            throw new IllegalArgumentException("Expected Square Matrix");
        }
    }

    public Matrix pow(double rexp) {
        checkSquare();
        int exp = (int) rexp;
        if (exp != rexp) {
            throw new IllegalArgumentException("Unable to raise Matrix to Non integer power");
        }
        return pow(exp);
    }

    @Override
    public Matrix pow(TMatrix<Complex> power) {
        if (power.isScalar()) {
            return pow(power.get(0, 0));
        } else {
            throw new IllegalArgumentException("Unable to raise Matrix to Matrix Power");
        }
    }

    @Override
    public TypeReference<Complex> getComponentType() {
        return Maths.$COMPLEX;
    }

    @Override
    public VectorSpace<Complex> getComponentVectorSpace() {
        return Maths.COMPLEX_VECTOR_SPACE;
    }

    //////////////////////////////////////////////////////////////////

    @Override
    public Complex scalarProduct(TMatrix<Complex> m) {
        return scalarProduct(false, m);
    }

    @Override
    public Complex scalarProduct(TVector<Complex> v) {
        return scalarProduct(false, v);
    }

    @Override
    public Complex hscalarProduct(TMatrix<Complex> m) {
        return scalarProduct(true, m);
    }

    @Override
    public Complex hscalarProduct(TVector<Complex> v) {
        return scalarProduct(true, v);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TMatrix)) {
            return false;
        }

        TMatrix<?> that = (TMatrix<?>) o;
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
    public int hashCode() {
        int hash = 7;
        int columnCount = getColumnCount();
        int rowCount = getRowCount();
        hash = 89 * hash + columnCount;
        hash = 89 * hash + rowCount;
        for (int c = 0; c < columnCount; c++) {
            for (int r = 0; r < rowCount; r++) {
                Complex t = get(r, c);
                if (t != null) {
                    hash = 89 * hash + t.hashCode();
                }
            }
        }
        return hash;

    }

    @Override
    public void resize(int rows, int columns) {
        throw new IllegalArgumentException("Unsupported resize Matrix");
    }


    @Override
    public Matrix copy() {
        return getFactory().newMatrix(this);
    }

    public void set(int row, int col, double real, double imag) {
        set(row, col, Complex.valueOf(real, imag));
    }

    @Override
    public void set(int row, int col, MutableComplex value) {
        set(row, col, value.getReal(), value.getImag());
    }

    @Override
    public void set(Complex[][] values) {
        int rows = values.length;
        int cols = values[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.set(i, j, values[i][j]);
            }
        }
    }

    @Override
    public void set(MutableComplex[][] values) {
        int rows = values.length;
        int cols = values[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                MutableComplex r = values[i][j];
                this.set(i, j, r.getReal(), r.getImag());
            }
        }
    }

    @Override
    public void set(double[][] values) {
        int rows = values.length;
        int cols = values[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.set(i, j, values[i][j], 0);
            }
        }
    }


    @Override
    public <R> boolean isConvertibleTo(TypeReference<R> other) {
        if (
                Maths.$COMPLEX.equals(other)
                        || Maths.$EXPR.equals(other)
                ) {
            return true;
        }
        if (other.isAssignableFrom(getComponentType())) {
            return true;
        }
        for (TVector<Complex> ts : getRows()) {
            if(!ts.isConvertibleTo(other)){
                return false;
            }
        }
        return true;
    }
}
