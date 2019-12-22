package net.vpc.scholar.hadrumaths;

/**
 * LU Decomposition.
 * <p/>
 * For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n unit
 * lower triangular matrix L, an n-by-n upper triangular matrix U, and a
 * permutation vector piv of length m so that A(piv,:) = L*U. If m < n, then L
 * is m-by-m and U is m-by-n. <p/>
 * The LU decomposition with pivoting always exists, even if the matrix is
 * singular, so the constructor will never fail. The primary use of the LU
 * decomposition is in the solution of square systems of simultaneous linear
 * equations. This will fail if isNonsingular() returns false.
 */
public class CLUDecomposition implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * ------------------------ Class variables ------------------------
     */
    /**
     * Array for internal storage of decomposition.
     *
     * @serial internal array storage.
     */
    private Complex[][] LU;
    /**
     * Row and column dimensions, and pivot sign.
     *
     * @serial column dimension.
     * @serial row dimension.
     * @serial pivot sign.
     */
    private int m, n, pivsign;
    /**
     * Internal storage of pivot vector.
     *
     * @serial pivot vector.
     */
    private int[] piv;

    /*
     * ------------------------ Constructor ------------------------
     */

    /**
     * LU Decomposition
     *
     * @param A Rectangular matrix return Structure to access L, U and piv.
     */
    public CLUDecomposition(ComplexMatrix A) {

        // Use a "left-looking", dot-product, Crout/Doolittle algorithm.

        LU = A.getArrayCopy();
        m = A.getRowCount();
        n = A.getColumnCount();
        piv = new int[m];
        for (int i = 0; i < m; i++) {
            piv[i] = i;
        }
        pivsign = 1;
        Complex[] LUrowi;
        Complex[] LUcolj = new Complex[m];

        // Outer loop.

        for (int j = 0; j < n; j++) {

            // Make a copy of the j-th column to localize references.

            for (int i = 0; i < m; i++) {
                LUcolj[i] = LU[i][j];
            }

            // Apply previous transformations.

            for (int i = 0; i < m; i++) {
                LUrowi = LU[i];

                // Most of the time is spent in the following dot product.

                int kmax = (i <= j) ? i : j;
                MutableComplex s = new MutableComplex();
                for (int k = 0; k < kmax; k++) {
                    s.add(LUrowi[k].mul(LUcolj[k]));
//                    System.out.println("\t k=" + k + " s=" + s);
                }
                LUcolj[i] = LUcolj[i].sub(s);
//                System.out.println("LUcolj(" + i + ")=" + LUcolj[i] + " for s=" + s);
                LUrowi[j] = LUcolj[i];
            }

            // Find pivot and exchange if necessary.

            int p = j;
            for (int i = j + 1; i < m; i++) {
                if (LUcolj[i].absdbl() > LUcolj[p].absdbl()) {
                    p = i;
                }
            }
            if (p != j) {
                for (int k = 0; k < n; k++) {
                    Complex t = LU[p][k];
                    LU[p][k] = LU[j][k];
                    LU[j][k] = t;
                }
                int k = piv[p];
                piv[p] = piv[j];
                piv[j] = k;
                pivsign = -pivsign;
            }

            // Compute multipliers.

            if (j < m && (!(LU[j][j].isZero()))) {
                for (int i = j + 1; i < m; i++) {
                    LU[i][j] = LU[i][j].div(LU[j][j]);
                }
            }
        }
//      System.out.println("LU="+new CMatrix(LU));
    }

    /*
     * ------------------------ Temporary, experimental code.
     * ------------------------ *\
     *
     * \** LU Decomposition, computed by Gaussian elimination. <P> This
     * constructor computes L and U with the "daxpy"-based elimination algorithm
     * used in LINPACK and MATLAB. In Java, we suspect the dot-product, Crout
     * algorithm will be faster. We have temporarily included this constructor
     * until timing experiments confirm this suspicion. <P> @param A Rectangular
     * matrix @param linpackflag Use Gaussian elimination. Actual value ignored.
     * @return Structure to access L, U and piv. \
     *
     * public LUDecomposition (Matrix A, int linpackflag) { // Initialize. LU =
     * A.getArrayCopy(); m = A.getRowCount(); n = A.getColumnCount();
     * piv = new int[m]; for (int i = 0; i < m; i++) { piv[i] = i; } pivsign =
     * 1; // Main loop. for (int k = 0; k < n; k++) { // Find pivot. int p = k;
     * for (int i = k+1; i < m; i++) { if (Math.absdbl(LU[i][k]) >
     * Math.absdbl(LU[p][k])) { p = i; } } // Exchange if necessary. if (p != k) {
     * for (int j = 0; j < n; j++) { double t = LU[p][j]; LU[p][j] = LU[k][j];
     * LU[k][j] = t; } int t = piv[p]; piv[p] = piv[k]; piv[k] = t; pivsign =
     * -pivsign; } // Compute multipliers and eliminate k-th column. if
     * (LU[k][k] != 0.0) { for (int i = k+1; i < m; i++) { LU[i][k] /= LU[k][k];
     * for (int j = k+1; j < n; j++) { LU[i][j] -= LU[i][k]*LU[k][j]; } } } } }
     *
     * \* ------------------------ End of temporary code.
     * ------------------------
     */

    /*
     * ------------------------ Public Methods ------------------------
     */

    /**
     * Is the matrix nonsingular?
     *
     * @return true if U, and hence A, is nonsingular.
     */
    public boolean isNonsingular() {
        for (int j = 0; j < n; j++) {
            if (LU[j][j].equals(Complex.ZERO)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return lower triangular factor
     *
     * @return L
     */
    public ComplexMatrix getL() {
        ComplexMatrix X = MathsBase.matrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) {
                    X.set(i, j, LU[i][j]);
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
                    U[i][j] = LU[i][j];
                } else {
                    U[i][j] = Complex.ZERO;
                }
            }
        }
        return MathsBase.matrix(U);
    }

    /**
     * Return pivot permutation vector
     *
     * @return piv
     */
    public int[] getPivot() {
        int[] p = new int[m];
        for (int i = 0; i < m; i++) {
            p[i] = piv[i];
        }
        return p;
    }

    /**
     * Return pivot permutation vector as a one-dimensional double array
     *
     * @return (double) piv
     */
    public double[] getDoublePivot() {
        double[] vals = new double[m];
        for (int i = 0; i < m; i++) {
            vals[i] = (double) piv[i];
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
        Complex d = Complex.valueOf(pivsign);
        for (int j = 0; j < n; j++) {
            d = d.mul(LU[j][j]);
        }
        return d;
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

        // Copy right hand side with pivoting
        int nx = B.getColumnCount();
        Complex[][] X = B.getMatrix(piv, 0, nx - 1).getArrayCopy();

        // Solve L*Y = B(piv,:)
        for (int k = 0; k < n; k++) {
            Complex[] xk = X[k];
            for (int i = k + 1; i < n; i++) {
                Complex[] xi = X[i];
                Complex[] lui = LU[i];
                for (int j = 0; j < nx; j++) {
                    xi[j] = xi[j].sub(xk[j].mul(lui[k]));
                }
            }
        }
        // Solve U*X = Y;
        for (int k = n - 1; k >= 0; k--) {
            Complex[] xk = X[k];
            Complex[] luk = LU[k];
            for (int j = 0; j < nx; j++) {
                xk[j] = xk[j].div(luk[k]);
            }
            for (int i = 0; i < k; i++) {
                Complex[] xi = X[i];
                Complex[] lui = LU[i];
                for (int j = 0; j < nx; j++) {
                    xi[j] = xi[j].sub(xk[j].mul(lui[k]));
                }
            }
        }
        return MathsBase.matrix(X);
    }
}
