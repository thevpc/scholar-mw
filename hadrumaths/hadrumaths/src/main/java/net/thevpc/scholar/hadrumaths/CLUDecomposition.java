package net.thevpc.scholar.hadrumaths;

import java.util.stream.IntStream;

/**
 * LU Decomposition with partial pivoting of a complex matrix.
 * <p/>
 * For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n unit
 * lower triangular matrix L, an n-by-n upper triangular matrix U, and a
 * permutation vector piv of length m so that A(piv,:) = L*U. If m < n, then L
 * is m-by-m and U is m-by-n. <p/>
 * The LU decomposition with pivoting always exists, even if the matrix is
 * singular, so the constructor will never fail. The primary use of the LU
 * decomposition is in the solution of square systems of simultaneous linear
 * equations. This will fail if isNonsingular() returns false.
 * <p/>
 * Storage and arithmetic are on two flat row-major {@code double[]} arrays (real and
 * imaginary parts), not on {@code Complex} objects: the elimination runs on contiguous
 * memory the JIT can vectorise, with no allocation in the inner loops. The rows of each
 * trailing update are independent and are processed in parallel once the trailing block
 * is large enough. Every element is still updated by the same sequence of operations as
 * in a serial run, so the factors do not depend on the thread count.
 * <p/>
 * The pivot is the largest |a_ik|^2 in the column (the same choice as the largest
 * |a_ik|, ties going to the first row), computed without a square root.
 */
public class CLUDecomposition implements java.io.Serializable {

    private static final long serialVersionUID = 2L;

    /** Below this many trailing elements (rows x columns) a step runs on the calling thread. */
    private static final long PARALLEL_THRESHOLD = 128L * 128L;

    /** Row-major factors: L below the diagonal (unit diagonal implied), U on and above it. */
    private final double[] luRe;
    private final double[] luIm;
    private final int m;
    private final int n;
    private int pivsign;
    private final int[] piv;

    /**
     * LU Decomposition
     *
     * @param A Rectangular matrix
     */
    public CLUDecomposition(ComplexMatrix A) {
        m = A.getRowCount();
        n = A.getColumnCount();
        if (A instanceof MemRawD1ComplexMatrix) {
            MemRawD1ComplexMatrix raw = (MemRawD1ComplexMatrix) A;
            luRe = raw.rawReals().clone();
            luIm = raw.rawImags().clone();
        } else {
            luRe = new double[m * n];
            luIm = new double[m * n];
            for (int i = 0; i < m; i++) {
                int ri = i * n;
                for (int j = 0; j < n; j++) {
                    Complex c = A.get(i, j);
                    luRe[ri + j] = c.getReal();
                    luIm[ri + j] = c.getImag();
                }
            }
        }
        piv = new int[m];
        for (int i = 0; i < m; i++) {
            piv[i] = i;
        }
        pivsign = 1;
        factor();
    }

    /** Right-looking elimination with partial pivoting (LINPACK/MATLAB "daxpy" form). */
    private void factor() {
        final double[] re = luRe;
        final double[] im = luIm;
        final int cols = n;
        int steps = Math.min(m, n);
        for (int k = 0; k < steps; k++) {
            // pivot: largest modulus in column k, rows k..m-1
            int p = k;
            double best = re[k * cols + k] * re[k * cols + k] + im[k * cols + k] * im[k * cols + k];
            for (int i = k + 1; i < m; i++) {
                int ik = i * cols + k;
                double v = re[ik] * re[ik] + im[ik] * im[ik];
                if (v > best) {
                    best = v;
                    p = i;
                }
            }
            if (p != k) {
                int rp = p * cols;
                int rk = k * cols;
                for (int j = 0; j < cols; j++) {
                    double t = re[rp + j];
                    re[rp + j] = re[rk + j];
                    re[rk + j] = t;
                    t = im[rp + j];
                    im[rp + j] = im[rk + j];
                    im[rk + j] = t;
                }
                int t = piv[p];
                piv[p] = piv[k];
                piv[k] = t;
                pivsign = -pivsign;
            }
            if (best == 0) {
                // the whole column below the diagonal is zero: nothing to eliminate
                continue;
            }
            final int kk = k;
            final double pr = re[k * cols + k];
            final double pi = im[k * cols + k];
            final double d = pr * pr + pi * pi;
            long work = (long) (m - k - 1) * (cols - k - 1);
            if (work >= PARALLEL_THRESHOLD) {
                IntStream.range(k + 1, m).parallel().forEach(i -> eliminateRow(re, im, cols, kk, i, pr, pi, d));
            } else {
                for (int i = k + 1; i < m; i++) {
                    eliminateRow(re, im, cols, kk, i, pr, pi, d);
                }
            }
        }
    }

    /** Row i: multiplier l = a_ik / a_kk stored in place, then a_ij -= l * a_kj for j > k. */
    private static void eliminateRow(double[] re, double[] im, int cols, int k, int i,
                                     double pr, double pi, double d) {
        int ik = i * cols + k;
        double ar = re[ik];
        double ai = im[ik];
        double lr = (ar * pr + ai * pi) / d;
        double li = (ai * pr - ar * pi) / d;
        re[ik] = lr;
        im[ik] = li;
        if (lr == 0 && li == 0) {
            return;
        }
        int ri = i * cols;
        int rk = k * cols;
        for (int j = k + 1; j < cols; j++) {
            double ur = re[rk + j];
            double ui = im[rk + j];
            re[ri + j] -= lr * ur - li * ui;
            im[ri + j] -= lr * ui + li * ur;
        }
    }

    /**
     * Return lower triangular factor
     *
     * @return L
     */
    public ComplexMatrix getL() {
        ComplexMatrix X = Maths.matrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) {
                    X.set(i, j, Complex.of(luRe[i * n + j], luIm[i * n + j]));
                } else if (i == j) {
                    X.set(i, j, Complex.ONE);
                } else {
                    X.set(i, j, Complex.ZERO);
                }
            }
        }
        return X;
    }

    /**
     * Return upper triangular factor
     *
     * @return U
     */
    public ComplexMatrix getU() {
        Complex[][] U = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i <= j) {
                    U[i][j] = Complex.of(luRe[i * n + j], luIm[i * n + j]);
                } else {
                    U[i][j] = Complex.ZERO;
                }
            }
        }
        return Maths.matrix(U);
    }

    /**
     * Return pivot permutation vector
     *
     * @return piv
     */
    public int[] getPivot() {
        return piv.clone();
    }

    /**
     * Return pivot permutation vector as a one-dimensional double array
     *
     * @return (double) piv
     */
    public double[] getDoublePivot() {
        double[] vals = new double[m];
        for (int i = 0; i < m; i++) {
            vals[i] = piv[i];
        }
        return vals;
    }

    /**
     * Determinant
     *
     * @return det(A)
     * @throws IllegalArgumentException Matrix must be square
     */
    public Complex det() {
        if (m != n) {
            throw new IllegalArgumentException("Matrix must be square.");
        }
        double dr = pivsign;
        double di = 0;
        for (int j = 0; j < n; j++) {
            double ur = luRe[j * n + j];
            double ui = luIm[j * n + j];
            double t = dr * ur - di * ui;
            di = dr * ui + di * ur;
            dr = t;
        }
        return Complex.of(dr, di);
    }

    /**
     * Solve A*X = B
     *
     * @param B A Matrix with as many rows as A and any number of columns.
     * @return X so that L*U*X = B(piv,:)
     * @throws IllegalArgumentException Matrix row dimensions must agree.
     * @throws RuntimeException         Matrix is singular.
     */
    public ComplexMatrix solve(ComplexMatrix B) {
        if (B.getRowCount() != m) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        }
        if (!this.isNonsingular()) {
            throw new RuntimeException("Matrix is singular.");
        }
        int nx = B.getColumnCount();
        // one right-hand side per column: x[c] holds column c, rows permuted by piv
        double[][] xr = new double[nx][n];
        double[][] xi = new double[nx][n];
        for (int r = 0; r < n; r++) {
            int src = piv[r];
            for (int c = 0; c < nx; c++) {
                Complex b = B.get(src, c);
                xr[c][r] = b.getReal();
                xi[c][r] = b.getImag();
            }
        }
        if (nx > 1 && (long) nx * n * n >= PARALLEL_THRESHOLD * 8) {
            IntStream.range(0, nx).parallel().forEach(c -> substitute(xr[c], xi[c]));
        } else {
            for (int c = 0; c < nx; c++) {
                substitute(xr[c], xi[c]);
            }
        }
        Complex[][] X = new Complex[n][nx];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < nx; c++) {
                X[r][c] = Complex.of(xr[c][r], xi[c][r]);
            }
        }
        return Maths.matrix(X);
    }

    /** Forward substitution with unit-diagonal L, then back substitution with U, in place. */
    private void substitute(double[] br, double[] bi) {
        for (int i = 1; i < n; i++) {
            int ri = i * n;
            double sr = br[i];
            double si = bi[i];
            for (int k = 0; k < i; k++) {
                double lr = luRe[ri + k];
                double li = luIm[ri + k];
                sr -= lr * br[k] - li * bi[k];
                si -= lr * bi[k] + li * br[k];
            }
            br[i] = sr;
            bi[i] = si;
        }
        for (int i = n - 1; i >= 0; i--) {
            int ri = i * n;
            double sr = br[i];
            double si = bi[i];
            for (int k = i + 1; k < n; k++) {
                double ur = luRe[ri + k];
                double ui = luIm[ri + k];
                sr -= ur * br[k] - ui * bi[k];
                si -= ur * bi[k] + ui * br[k];
            }
            double ur = luRe[ri + i];
            double ui = luIm[ri + i];
            double d = ur * ur + ui * ui;
            br[i] = (sr * ur + si * ui) / d;
            bi[i] = (si * ur - sr * ui) / d;
        }
    }

    /**
     * Is the matrix nonsingular?
     *
     * @return true if U, and hence A, is nonsingular.
     */
    public boolean isNonsingular() {
        for (int j = 0; j < n; j++) {
            if (luRe[j * n + j] == 0 && luIm[j * n + j] == 0) {
                return false;
            }
        }
        return true;
    }
}
