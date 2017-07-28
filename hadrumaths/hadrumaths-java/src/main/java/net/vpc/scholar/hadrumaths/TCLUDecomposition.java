package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.ArrayUtils;

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
public class TCLUDecomposition<T> implements java.io.Serializable {

    private static final long serialVersionUID = -1010101010101001012L;

    /*
     * ------------------------ Class variables ------------------------
     */
    /**
     * Array for internal storage of decomposition.
     *
     * @serial internal array storage.
     */
    private T[][] LU;
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
    private VectorSpace<T> space;
    private TMatrixFactory<T> matrixFactory;
    private Class<T> componentType;
    /*
     * ------------------------ Constructor ------------------------
     */

    /**
     * LU Decomposition
     *
     * @param A Rectangular matrix return Structure to access L, U and piv.
     */
    public TCLUDecomposition(TMatrix<T> A) {
        space = A.getComponentVectorSpace();
        matrixFactory=A.getFactory();
        // Use a "left-looking", dot-product, Crout/Doolittle algorithm.

        LU = A.getArrayCopy();
        m = A.getRowCount();
        n = A.getColumnCount();
        piv = new int[m];
        for (int i = 0; i < m; i++) {
            piv[i] = i;
        }
        pivsign = 1;
        T[] LUrowi;
        componentType = A.getComponentType();
        T[] LUcolj = ArrayUtils.newArray(componentType,m);

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
                T s = space.zero();
                for (int k = 0; k < kmax; k++) {
                    s = space.add(s,space.mul(LUrowi[k],LUcolj[k]));
//                    System.out.println("\t k=" + k + " s=" + s);
                }
                LUcolj[i] = space.sub(LUcolj[i],s);
//                System.out.println("LUcolj(" + i + ")=" + LUcolj[i] + " for s=" + s);
                LUrowi[j] = LUcolj[i];
            }

            // Find pivot and exchange if necessary.

            int p = j;
            for (int i = j + 1; i < m; i++) {
                if (space.absdbl(LUcolj[i]) > space.absdbl(LUcolj[p])) {
                    p = i;
                }
            }
            if (p != j) {
                for (int k = 0; k < n; k++) {
                    T t = LU[p][k];
                    LU[p][k] = LU[j][k];
                    LU[j][k] = t;
                }
                int k = piv[p];
                piv[p] = piv[j];
                piv[j] = k;
                pivsign = -pivsign;
            }

            // Compute multipliers.

            if (j < m && (!(space.isZero(LU[j][j])))) {
                for (int i = j + 1; i < m; i++) {
                    LU[i][j] = space.div(LU[i][j],LU[j][j]);
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
     * public LUDecomposition (TMatrix<T> A, int linpackflag) { // Initialize. LU =
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
            if (LU[j][j].equals(space.zero())) {
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
    public TMatrix<T> getL() {
        TMatrix<T> X = matrixFactory.newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) {
                    X.set(i, j, LU[i][j]);
                } else if (i == j) {
                    X.set(i, j, space.one());
                } else {
                    X.set(i, j, space.zero());
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
    public TMatrix<T> getU() {
        TMatrix<T> X = matrixFactory.newMatrix(n, n);
        T[][] U = X.getArray();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i <= j) {
                    U[i][j] = LU[i][j];
                } else {
                    U[i][j] = space.zero();
                }
            }
        }
        return X;
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
     * @throws IllegalArgumentException TMatrix<T> must be square
     */
    public T det() {
        if (m != n) {
            throw new IllegalArgumentException("TMatrix<T> must be square.");
        }
        T d = space.convert(pivsign);
        for (int j = 0; j < n; j++) {
            d = space.mul(d,LU[j][j]);
        }
        return d;
    }

    /**
     * Solve A*X = B
     *
     * @param B A TMatrix<T> with as many rows as A and any number of columns.
     * @return X so that L*U*X = B(piv,:)
     * @throws IllegalArgumentException TMatrix<T> row dimensions must agree.
     * @throws RuntimeException         TMatrix<T> is singular.
     */
    public TMatrix<T> solve(TMatrix<T> B) {
        if (B.getRowCount() != m) {
            throw new IllegalArgumentException("TMatrix<T> row dimensions must agree.");
        }
        if (!this.isNonsingular()) {
            throw new RuntimeException("TMatrix<T> is singular.");
        }

        // Copy right hand side with pivoting
        int nx = B.getColumnCount();
        TMatrix<T> Xmat = B.getMatrix(piv, 0, nx - 1);
        T[][] X = Xmat.getArray();

        // Solve L*Y = B(piv,:)
        for (int k = 0; k < n; k++) {
            T[] xk = X[k];
            for (int i = k + 1; i < n; i++) {
                T[] xi = X[i];
                T[] lui = LU[i];
                for (int j = 0; j < nx; j++) {
                    xi[j] = space.sub(xi[j],space.mul(xk[j],lui[k]));
                }
            }
        }
        // Solve U*X = Y;
        for (int k = n - 1; k >= 0; k--) {
            T[] xk = X[k];
            T[] luk = LU[k];
            for (int j = 0; j < nx; j++) {
                xk[j] = space.div(xk[j],luk[k]);
            }
            for (int i = 0; i < k; i++) {
                T[] xi = X[i];
                T[] lui = LU[i];
                for (int j = 0; j < nx; j++) {
                    xi[j] = space.sub(xi[j],space.mul(xk[j],lui[k]));
                }
            }
        }
        return Xmat;
    }
}
