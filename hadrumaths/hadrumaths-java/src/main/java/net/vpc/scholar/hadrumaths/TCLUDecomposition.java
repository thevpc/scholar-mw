package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;
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

    private static final long serialVersionUID = 1L;

    /*
     * ------------------------ Class variables ------------------------
     */
    /**
     * Array for internal storage of decomposition.
     *
     * @serial internal array storage.
     */
    private final Matrix<T> LU;
    /**
     * Row and column dimensions, and pivot sign.
     *
     * @serial column dimension.
     * @serial row dimension.
     * @serial pivot sign.
     */
    private final int m;
    private final int n;
    private int pivsign;
    /**
     * Internal storage of pivot vector.
     *
     * @serial pivot vector.
     */
    private final int[] piv;
    private final VectorSpace<T> space;
    private final MatrixFactory<T> matrixFactory;
    private final TypeName<T> componentType;
    /*
     * ------------------------ Constructor ------------------------
     */

    /**
     * LU Decomposition
     *
     * @param A Rectangular matrix return Structure to access L, U and piv.
     */
    public TCLUDecomposition(Matrix<T> A) {
        space = A.getComponentVectorSpace();
        matrixFactory = A.getFactory();
        // Use a "left-looking", dot-product, Crout/Doolittle algorithm.

        LU = A.copy();
        m = A.getRowCount();
        n = A.getColumnCount();
        piv = new int[m];
        for (int i = 0; i < m; i++) {
            piv[i] = i;
        }
        pivsign = 1;
//        T[] LUrowi;
        componentType = A.getComponentType();
        T[] LUcolj = ArrayUtils.newArray(componentType, m);

        // Outer loop.

        for (int j = 0; j < n; j++) {

            // Make a copy of the j-th column to localize references.

            for (int i = 0; i < m; i++) {
                LUcolj[i] = LU.get(i, j);
            }

            // Apply previous transformations.

            for (int i = 0; i < m; i++) {
                // Most of the time is spent in the following dot product.

                int kmax = (i <= j) ? i : j;
                T s = space.zero();
                for (int k = 0; k < kmax; k++) {
                    s = space.add(s, space.mul(LU.get(i, k), LUcolj[k]));
//                    System.out.println("\t k=" + k + " s=" + s);
                }
                LUcolj[i] = space.sub(LUcolj[i], s);
//                System.out.println("LUcolj(" + i + ")=" + LUcolj[i] + " for s=" + s);
                LU.set(i, j, LUcolj[i]);
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
                    T t = LU.get(p, k);
                    LU.set(p, k, LU.get(j, k));
                    LU.set(j, k, t);
                }
                int k = piv[p];
                piv[p] = piv[j];
                piv[j] = k;
                pivsign = -pivsign;
            }

            // Compute multipliers.

            if (j < m && (!(space.isZero(LU.get(j, j))))) {
                for (int i = j + 1; i < m; i++) {
                    LU.div(i, j, LU.get(j, j));
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
     * public LUDecomposition (Matrix<T> A, int linpackflag) { // Initialize. LU =
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
     * Return lower triangular factor
     *
     * @return L
     */
    public Matrix<T> getL() {
        Matrix<T> X = matrixFactory.newMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) {
                    X.set(i, j, LU.get(i, j));
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
    public Matrix<T> getU() {
        Matrix<T> X = matrixFactory.newMatrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i <= j) {
                    X.set(i, j, LU.get(i, j));
                } else {
                    X.set(i, j, space.zero());
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
            vals[i] = piv[i];
        }
        return vals;
    }

    /**
     * Determinant
     *
     * @return det(A)
     * @throws IllegalArgumentException Matrix<T> must be square
     */
    public T det() {
        if (m != n) {
            throw new IllegalArgumentException("Matrix<T> must be square.");
        }
        T d = space.convert(pivsign);
        for (int j = 0; j < n; j++) {
            d = space.mul(d, LU.get(j, j));
        }
        return d;
    }

    /**
     * Solve A*X = B
     *
     * @param B A Matrix<T> with as many rows as A and any number of columns.
     * @return X so that L*U*X = B(piv,:)
     * @throws IllegalArgumentException Matrix<T> row dimensions must agree.
     * @throws RuntimeException         Matrix<T> is singular.
     */
    public Matrix<T> solve(Matrix<T> B) {
        if (B.getRowCount() != m) {
            throw new IllegalArgumentException("Matrix<T> row dimensions must agree.");
        }
        if (!this.isNonsingular()) {
            throw new RuntimeException("Matrix<T> is singular.");
        }

        // Copy right hand side with pivoting
        int nx = B.getColumnCount();
        Matrix<T> Xmat = B.getMatrix(piv, 0, nx - 1);

        for (int k = 0; k < n; k++) {
            for (int i = k + 1; i < n; i++) {
                for (int j = 0; j < nx; j++) {
                    Xmat.set(i, j, space.sub(Xmat.get(i, j), space.mul(Xmat.get(k, j), LU.get(i, k))));
                }
            }
        }
        // Solve U*X = Y;
        for (int k = n - 1; k >= 0; k--) {
            for (int j = 0; j < nx; j++) {
                Xmat.set(k, j, space.div(Xmat.get(k, j), LU.get(k, k)));
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < nx; j++) {
                    Xmat.set(i, j, space.sub(Xmat.get(i, j), space.mul(Xmat.get(k, j), LU.get(i, k))));
                }
            }
        }
        return Xmat;
    }

    /**
     * Is the matrix nonsingular?
     *
     * @return true if U, and hence A, is nonsingular.
     */
    public boolean isNonsingular() {
        for (int j = 0; j < n; j++) {
            if (LU.get(j, j).equals(space.zero())) {
                return false;
            }
        }
        return true;
    }
}
