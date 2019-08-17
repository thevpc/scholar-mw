package net.vpc.scholar.hadrumaths;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public final class MemRawD1Matrix extends AbstractMatrix implements Serializable {
    private static final long serialVersionUID = 1;

    private double[] reals;
    private double[] imags;
    private int rowsCount;
    private int columnsCount;

    private MemRawD1Matrix(int rows, int cols, double[] reals, double[] imags) {
        this.rowsCount = rows;
        this.columnsCount = cols;
        this.reals = reals;
        this.imags = imags;
    }

    MemRawD1Matrix(int rows, int cols) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        resize(rows, cols);
    }


    /*To exchange two rows in a matrix*/
    public static void exchange_row(Complex[][] M, int k, int l, int m, int n) {
        if (k <= 0 || l <= 0 || k > n || l > n || k == l) {
            return;
        }
        Complex tmp;
        for (int j = 0; j < n; j++) {
            tmp = M[k - 1][j];
            M[k - 1][j] = M[l - 1][j];
            M[l - 1][j] = tmp;
        }
    }


    /**
     * One norm
     *
     * @return maximum column sum.
     */
    public double norm1() {
        double f = 0;
        for (int jj = 0; jj < columnsCount; jj++) {
            double s = 0;
            for (int ii = 0; ii < rowsCount; ii++) {
                double vr = reals[jj];
                double vi = imags[jj];
                double vv = Maths.dsqrt(vi * vi + vr * vr);
                s += vv;
            }
            f = Math.max(f, s);
        }
        return f;
    }

    public double norm2() {
        double f = 0;
        for (int i = 0; i < reals.length; i++) {
            double ii = imags[i];
            double rr = reals[i];
            f += rr * rr + ii * ii;
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
        for (int i = 0; i < reals.length; i++) {
            double ii = imags[i];
            double rr = reals[i];
            f = Math.max(f, rr * rr + ii * ii);
        }
        return Maths.sqrt(f);
    }

    /**
     * Infinity norm
     *
     * @return maximum row sum.
     */
    @Override
    public double normInf() {
        double f = 0;
        for (int ii = 0; ii < rowsCount; ii++) {
            double s = 0;
            for (int jj = 0; jj < columnsCount; jj++) {
                double vr = reals[jj];
                double vi = imags[jj];
                double vv = Maths.dsqrt(vi * vi + vr * vr);
                s += vv;
            }
            f = Math.max(f, s);
        }
        return f;
    }

    @Override
    public void setAll(Complex value) {
        double r = value.getReal();
        double ii = value.getImag();
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                _setr(i, j, r);
                _seti(i, j, ii);
            }
        }
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
    @Override
    public Matrix getMatrix(int[] r, int c1, int c2) {
        MemRawD1Matrix X = new MemRawD1Matrix(r.length, c2 - c1 + 1);
        double[] Br = X.reals;
        double[] Bi = X.imags;
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = c1; j <= c2; j++) {
                    _set(i, j - c1, Br, X.columnsCount, _getr(r[i], j));
                    _set(i, j - c1, Bi, X.columnsCount, _geti(r[i], j));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }

    @Override
    public Matrix div(double c) {
        double[] r2 = new double[reals.length];
        double[] i2 = new double[imags.length];
        for (int i = 0; i < r2.length; i++) {
            r2[i] = reals[i] / c;
            i2[i] = imags[i] / c;
        }
        return new MemRawD1Matrix(rowsCount, columnsCount, r2, i2);
    }

    @Override
    public Matrix div(Complex c) {
        if (c.isReal()) {
            return div(c.getReal());
        }
        double[] er = new double[reals.length];
        double[] ei = new double[imags.length];
        for (int i = 0; i < er.length; i++) {
            MutableComplex mc = new MutableComplex(reals[i], imags[i]);
            mc.div(c);
            er[i] = mc.getReal();
            ei[i] = mc.getReal();
        }
        return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
    }

    @Override
    public Matrix mul(Complex c) {
        if (c.isReal()) {
            return mul(c.getReal());
        }
        double[] er = new double[reals.length];
        double[] ei = new double[imags.length];
        for (int i = 0; i < er.length; i++) {
            MutableComplex mc = new MutableComplex(reals[i], imags[i]);
            mc.mul(c);
            er[i] = mc.getReal();
            ei[i] = mc.getReal();
        }
        return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
    }

    @Override
    public Matrix mul(double c) {
        double[] er = new double[reals.length];
        double[] ei = new double[imags.length];
        if (c != 0) {
            for (int i = 0; i < er.length; i++) {
                er[i] = reals[i] * c;
                ei[i] = imags[i] * c;
            }
        }
        return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
    }

    @Override
    public Matrix neg() {
        double[] er = new double[reals.length];
        double[] ei = new double[imags.length];
        for (int i = 0; i < er.length; i++) {
            er[i] = -reals[i];
            ei[i] = -imags[i];
        }
        return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
    }

    @Override
    public Matrix add(Complex c) {
        double[] er = new double[reals.length];
        double[] ei = new double[imags.length];
        double ii = c.getImag();
        double rr = c.getReal();
        for (int i = 0; i < er.length; i++) {
            er[i] = reals[i] + rr;
            ei[i] = imags[i] + ii;
        }
        return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
    }

    @Override
    public Matrix sub(Complex c) {
        double[] er = new double[reals.length];
        double[] ei = new double[imags.length];
        double ii = c.getImag();
        double rr = c.getReal();
        for (int i = 0; i < er.length; i++) {
            er[i] = reals[i] - rr;
            ei[i] = imags[i] - ii;
        }
        return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
    }

    @Override
    public Matrix dotmul(TMatrix<Complex> other) {
        if (other instanceof MemRawD1Matrix) {
            MemRawD1Matrix mm = (MemRawD1Matrix) other;
            double[] er = new double[reals.length];
            double[] ei = new double[imags.length];
            for (int i = 0; i < er.length; i++) {
                MutableComplex c = new MutableComplex(reals[i], imags[i]);
                c.mul(mm.reals[i], mm.imags[i]);
                er[i] = c.getReal();
                ei[i] = c.getImag();
            }
            return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
        } else {
            double[] er = new double[reals.length];
            double[] ei = new double[imags.length];
            for (int i = 0; i < er.length; i++) {
                int rr = i / columnsCount;
                int ii = i % columnsCount;
                MutableComplex c = new MutableComplex(reals[i], imags[i]);
                c.mul(other.get(rr, ii));
                er[i] = c.getReal();
                ei[i] = c.getImag();
            }
            return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
        }
    }

    @Override
    public Matrix dotdiv(TMatrix<Complex> other) {
        if (other instanceof MemRawD1Matrix) {
            MemRawD1Matrix mm = (MemRawD1Matrix) other;
            double[] er = new double[reals.length];
            double[] ei = new double[imags.length];
            for (int i = 0; i < er.length; i++) {
                MutableComplex c = new MutableComplex(reals[i], imags[i]);
                c.div(mm.reals[i], mm.imags[i]);
                er[i] = c.getReal();
                ei[i] = c.getImag();
            }
            return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
        } else {
            double[] er = new double[reals.length];
            double[] ei = new double[imags.length];
            for (int i = 0; i < er.length; i++) {
                int rr = i / columnsCount;
                int ii = i % columnsCount;
                MutableComplex c = new MutableComplex(reals[i], imags[i]);
                c.div(other.get(rr, ii));
                er[i] = c.getReal();
                ei[i] = c.getImag();
            }
            return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
        }
    }

    @Override
    public Matrix add(TMatrix<Complex> other) {
        if (other instanceof MemRawD1Matrix) {
            MemRawD1Matrix mm = (MemRawD1Matrix) other;
            double[] er = new double[reals.length];
            double[] ei = new double[imags.length];
            for (int i = 0; i < er.length; i++) {
                er[i] = reals[i] + mm.reals[i];
                ei[i] = imags[i] + mm.imags[i];
            }
            return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
        } else {
            double[] er = new double[reals.length];
            double[] ei = new double[imags.length];
            for (int i = 0; i < er.length; i++) {
                int rr = i / columnsCount;
                int ii = i % columnsCount;
                Complex complex = other.get(rr, ii);
                er[i] = reals[i] + complex.getReal();
                ei[i] = imags[i] + complex.getImag();
            }
            return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
        }
    }

    @Override
    public Matrix sub(TMatrix<Complex> other) {
        if (other instanceof MemRawD1Matrix) {
            MemRawD1Matrix mm = (MemRawD1Matrix) other;
            double[] er = new double[reals.length];
            double[] ei = new double[imags.length];
            for (int i = 0; i < er.length; i++) {
                er[i] = reals[i] - mm.reals[i];
                ei[i] = imags[i] - mm.imags[i];
            }
            return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
        } else {
            double[] er = new double[reals.length];
            double[] ei = new double[imags.length];
            for (int i = 0; i < er.length; i++) {
                int rr = i / columnsCount;
                int ii = i % columnsCount;
                Complex complex = other.get(rr, ii);
                er[i] = reals[i] - complex.getReal();
                ei[i] = imags[i] - complex.getImag();
            }
            return new MemRawD1Matrix(rowsCount, columnsCount, er, ei);
        }
    }

    @Override
    public Complex getScalar() {
        return get(0, 0);
    }

    @Override
    public Complex get(int row, int col) {
        return Complex.valueOf(_getr(row, col), _geti(row, col));
    }

    @Override
    public void add(int row, int col, Complex val) {
        set(row, col, _getr(row, col) + val.getReal(), _geti(row, col) + val.getImag());
    }

//    @Override
//    public void mul(int row, int col, Complex val) {
//        elements[row][col] = elements[row][col].mul(val);
//    }
//
//    @Override
//    public void div(int row, int col, Complex val) {
//        elements[row][col] = elements[row][col].div(val);
//    }

    @Override
    public void sub(int row, int col, Complex val) {
        set(row, col, _getr(row, col) - val.getReal(), _geti(row, col) - val.getImag());
    }

    public void set(int row, int col, double valr, double vali) {
        _setr(row, col, valr);
        _seti(row, col, vali);
    }

    @Override
    public void set(int row, int col, Complex val) {
        set(row, col, val.getReal(), val.getImag());
    }

    @Override
    public void set(int row, int col, TMatrix<Complex> src, int srcRow, int srcCol, int rows, int cols) {
        if (src instanceof MemRawD1Matrix) {
            //TODO
//            MemRawD1Matrix mm = (MemRawD1Matrix) src;
//            for (int i = 0; i < rows; i++) {
//                System.arraycopy(mm.elements[i + srcRow], srcCol, elements[i + row], col, cols);
//            }
            super.set(row, col, src, srcRow, srcCol, rows, cols);
        } else {
            super.set(row, col, src, srcRow, srcCol, rows, cols);
        }
    }

    @Override
    public int getRowCount() {
        return rowsCount;
    }


    @Override
    public int getColumnCount() {
        return columnsCount;
    }

    @Override
    public Matrix arrayTranspose() {
        int r = rowsCount;
        if (r == 0) {
            return this;
        }
        int c = columnsCount;
        double[] er = new double[c * r];
        double[] ei = new double[c * r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                _set(j, i, er, rowsCount, _getr(i, j));
                _set(j, i, ei, rowsCount, _geti(i, j));
            }
        }
        return new MemRawD1Matrix(columnsCount, rowsCount, er, ei);
    }


    /**
     * Hermitian conjugate
     *
     * @return
     */
    @Override
    public Matrix transposeHermitian() {
        int r = rowsCount;
        if (r == 0) {
            return this;
        }
        int c = columnsCount;
        double[] er = new double[c * r];
        double[] ei = new double[c * r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                _set(j, i, er, rowsCount, _getr(i, j));
                _set(j, i, ei, rowsCount, -_geti(i, j));
            }
        }
        return new MemRawD1Matrix(columnsCount, rowsCount, er, ei);
    }

    @Override
    public Matrix invSolve() {
        return solve(getFactory().newIdentity(rowsCount));
    }

    @Override
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
        Complex[][] A = new Complex[m][n];
        Complex[][] B = new Complex[m][n];

        //Copie de M dans A et Mise en forme de B : B=I
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = get(i, j);
                if (i == j) {
                    B[i][j] = Complex.ONE;
                } else {
                    B[i][j] = Complex.ZERO;
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
                        a = A[k][l];
                        if (!a.isZero()) {
                            J.add(l);
                            cnt_col++;
                            bl = false; //permet de sortir de la boucle car le pivot a été trouvé
                            for (int p = 0; p < n; p++) {
                                if (p != k) {
                                    tmp = A[p][l];
                                    for (int q = 0; q < n; q++) {
                                        A[p][q] = A[p][q].sub(A[k][q].mul(tmp.div(a)));
                                        B[p][q] = B[p][q].sub(B[k][q].mul((tmp.div(a))));
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
                    a = A[k][l];
                    if (!a.isZero()) {
                        A[k][l] = Complex.ONE;
                        for (int p = 0; p < n; p++) {
                            B[k][p] = B[k][p].div(a);
                        }
                        if (k != l) {
                            exchange_row(A, k + 1, l + 1, n, n);
                            exchange_row(B, k + 1, l + 1, n, n);
                        }
                        k = n; //Pour sortir de la boucle car le coefficient non nul a ete trouve
                    }
                }
            }
            return getFactory().newMatrix(B);
        }
    }

    @Override
    public MemRawD1Matrix coMatrix(int row, int col) {
        int tms = getRowCount();
        MemRawD1Matrix ap = (MemRawD1Matrix) createMatrix(tms - 1, tms - 1);
        int ia = 0;
        int ja;
        for (int ii = 0; ii < row; ii++) {
            ja = 0;
            for (int jj = 0; jj < col; jj++) {
                ap.set(ia, ja, _getr(ii, jj), _geti(ii, jj));
                ja++;
            }
            for (int jj = col + 1; jj < tms; jj++) {
                ap.set(ia, ja, _getr(ii, jj), _geti(ii, jj));
                ja++;
            }
            ia++;
        }
        for (int ii = row + 1; ii < tms; ii++) {
            ja = 0;
            for (int jj = 0; jj < col; jj++) {
                ap.set(ia, ja, _getr(ii, jj), _geti(ii, jj));
                ja++;
            }
            for (int jj = col + 1; jj < tms; jj++) {
                ap.set(ia, ja, _getr(ii, jj), _geti(ii, jj));
                ja++;
            }
            ia++;
        }
        return ap;
    }


    @Override
    public Complex[][] getArrayCopy() {
        Complex[][] C = new Complex[getRowCount()][getColumnCount()];
        for (int i = 0; i < C.length; i++) {
            for (int j = 0; j < C[i].length; j++) {
                C[i][j] = get(i, j);
            }
        }
        return C;
    }

    @Override
    public double[][] absdbls() {
        double[][] d = new double[getRowCount()][getColumnCount()];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                double rr = _getr(i, j);
                double ii = _getr(i, j);
                d[i][j] = Maths.sqrt(rr * rr + ii * ii);
            }
        }
        return d;
    }

    @Override
    public Matrix abs() {
        Complex[][] d = new Complex[getRowCount()][getColumnCount()];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                double rr = _getr(i, j);
                double ii = _getr(i, j);
                double v = Maths.sqrt(rr * rr + ii * ii);
                d[i][j] = Complex.valueOf(v);
            }
        }
        return getFactory().newMatrix(d);
    }

    @Override
    public Matrix abssqr() {
        Complex[][] d = new Complex[getRowCount()][getColumnCount()];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                double rr = _getr(i, j);
                double ii = _getr(i, j);
                double v = (rr * rr + ii * ii);
                d[i][j] = Complex.valueOf(v);
            }
        }
        return getFactory().newMatrix(d);
    }

    @Override
    public double condHadamard() {
        Complex det = det();
        double x = 1;
        double alpha;
        int rows = getRowCount();
        int cols = getColumnCount();
        for (int r = 0; r < rows; r++) {
            alpha = 0;
            for (int c = 0; c < cols; c++) {
                double rr = _getr(r, c);
                double ii = _getr(r, c);
                alpha = alpha + ((rr * rr + ii * ii));
            }
            x *= Maths.sqrt(alpha);
        }

        return det.absdbl() / x;
    }

    @Override
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
                            continue;
                        }
                    }
                    array.set(r, c, v);
                }
            }
        }
        return array;
    }

    @Override
    public double maxAbs() {
        double f0 = reals[0] * reals[0] + imags[0] * imags[0];
        double f = f0;
        for (int i = 1; i < reals.length; i++) {
            f0 = reals[i] * reals[i] + imags[i] * imags[i];
            f = Math.max(f, f0);
        }
        return f;
    }

    @Override
    public double minAbs() {
        double f0 = reals[0] * reals[0] + imags[0] * imags[0];
        double f = f0;
        for (int i = 1; i < reals.length; i++) {
            f0 = reals[i] * reals[i] + imags[i] * imags[i];
            f = Math.min(f, f0);
        }
        return f;
    }

    @Override
    public boolean isScalar() {
        return reals.length == 1;
    }

    @Override
    public boolean isComplex() {
        return reals.length == 1;
    }

    @Override
    public boolean isDouble() {
        return isComplex() && toComplex().isDouble();
    }

    @Override
    public Complex toComplex() {
        if (isComplex()) {
            return Complex.valueOf(_getr(0, 0), _geti(0, 0));
        }
        throw new ClassCastException();
    }

    @Override
    public boolean isZero() {
        for (int i = 0; i < reals.length; i++) {
            if (reals[i] != 0) {
                return false;
            }
            if (imags[i] != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void set(TMatrix<Complex> other) {
        if (other instanceof MemRawD1Matrix) {
            //TODO may be optimized
            super.set(other);
        } else {
            super.set(other);
        }
    }

    public void dispose() {

    }

    @Override
    public void resize(int rows, int columns) {
        int rr = getRowCount();
        int cc = getColumnCount();
        if (rows != rr || columns != cc) {
            double[] elements2_r = new double[rows * columns];
            double[] elements2_i = new double[rows * columns];
            int rows0 = Math.min(rows, rr);
            int columns0 = Math.min(columns, cc);
            for (int r = 0; r < rows0; r++) {
                for (int c = 0; c < columns0; c++) {
                    if (r < rr && c < cc) {
                        _set(r, c, elements2_r, columns, _get(r, c, reals, columnsCount));
                        _set(r, c, elements2_i, columns, _get(r, c, imags, columnsCount));
                    } else {
                        //elements2[r][c] = Complex.ZERO;
                    }
                }
            }
            this.reals = elements2_r;
            this.imags = elements2_i;
            this.rowsCount = rows;
            this.columnsCount = columns;
        }
    }

    @Override
    public void set(int row, int col, MutableComplex value) {
        _setr(row, col, value.getReal());
        _seti(row, col, value.getImag());
    }

    private final double _getr(int r, int c) {
        return reals[r * columnsCount + c];
    }

    private double _geti(int r, int c) {
        return imags[r * columnsCount + c];
    }

    private double _setr(int r, int c, double v) {
        return reals[r * columnsCount + c] = v;
    }

    private double _seti(int r, int c, double v) {
        return imags[r * columnsCount + c] = v;
    }

    private double _get(int r, int c, double[] reals, int C) {
        return reals[r * C + c];
    }

    private double _set(int r, int c, double[] reals, int C, double v) {
        return reals[r * C + c] = v;
    }

}
