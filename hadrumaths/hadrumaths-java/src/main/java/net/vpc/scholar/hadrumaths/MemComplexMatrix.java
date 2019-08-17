package net.vpc.scholar.hadrumaths;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public final class MemComplexMatrix extends AbstractMatrix implements Serializable {
    private static final long serialVersionUID = 1;

    private Complex[][] elements;

    MemComplexMatrix(Complex[][] elements) {
        this.elements = elements;
    }

    MemComplexMatrix(int rows, int cols) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        elements = new Complex[rows][cols];
    }


    private static MemComplexMatrix newMemMatrix(Complex[][] e) {
        return new MemComplexMatrix(e);
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
        for (int j = 0; j < elements[0].length; j++) {
            double s = 0;
            for (Complex[] element : elements) {
                s += element[j].absdbl();
            }
            f = Math.max(f, s);
        }
        return f;
    }

    public double norm2() {
        double f = 0;
        for (int j = 0; j < elements[0].length; j++) {
            for (Complex[] element : elements) {
                f += element[j].absdblsqr();
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
        for (int j = 0; j < elements[0].length; j++) {
            for (Complex[] element : elements) {
                f = Math.max(f, element[j].absdbl());
            }
        }
        return f;
    }

    /**
     * Infinity norm
     *
     * @return maximum row sum.
     */
    @Override
    public double normInf() {
        double f = 0;
        for (Complex[] element : elements) {
            double s = 0;
            for (Complex anElement : element) {
                s += anElement.absdbl();
            }
            f = Math.max(f, s);
        }
        return f;
    }

    @Override
    public void setAll(Complex value) {
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[i].length; j++) {
                this.elements[i][j] = value;
            }
        }
    }

    @Override
    public Complex[][] getArray() {
        return elements;
    }

    /**
     * Get a submatrix.
     *
     * @param i0 Initial row index
     * @param i1 Final row index
     * @param j0 Initial column index
     * @param j1 Final column index
     * @return A(i0 : i1, j0 : j1)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    @Override
    public Matrix getMatrix(int i0, int i1, int j0, int j1) {
        MemComplexMatrix X = new MemComplexMatrix(i1 - i0 + 1, j1 - j0 + 1);
        Complex[][] B = X.elements;// X.getArray();
        try {
            for (int i = i0; i <= i1; i++) {
                Complex[] brow = B[i - i0];
                Complex[] erow = elements[i];
                System.arraycopy(erow, j0, brow, j0 - j0, j1 + 1 - j0);
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
    @Override
    public Matrix getMatrix(int[] r, int[] c) {
        MemComplexMatrix X = new MemComplexMatrix(r.length, c.length);
        Complex[][] B = X.elements;
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = 0; j < c.length; j++) {
                    B[i][j] = elements[r[i]][c[j]];
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
    @Override
    public Matrix getMatrix(int r1, int r2, int[] c) {
        MemComplexMatrix X = new MemComplexMatrix(r2 - r1 + 1, c.length);
        Complex[][] B = X.elements;
        try {
            for (int i = r1; i <= r2; i++) {
                for (int j = 0; j < c.length; j++) {
                    B[i - r1][j] = elements[i][c[j]];
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
    @Override
    public Matrix getMatrix(int[] r, int c1, int c2) {
        MemComplexMatrix X = new MemComplexMatrix(r.length, c2 - c1 + 1);
        Complex[][] B = X.elements;
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = c1; j <= c2; j++) {
                    B[i][j - c1] = elements[r[i]][j];
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }

    @Override
    public Matrix div(double c) {
        Complex[][] e = new Complex[elements.length][];
        for (int i = 0; i < elements.length; i++) {
            e[i] = new Complex[elements[i].length];
            for (int j = 0; j < elements[i].length; j++) {
                e[i][j] = elements[i][j].div(c);
            }
        }
        return newMemMatrix(e);
    }

    @Override
    public Matrix div(Complex c) {
        Complex[][] e = new Complex[elements.length][];
        for (int i = 0; i < elements.length; i++) {
            e[i] = new Complex[elements[i].length];
            for (int j = 0; j < elements[i].length; j++) {
                e[i][j] = elements[i][j].div(c);
            }
        }
        return newMemMatrix(e);
    }

    @Override
    public Matrix mul(Complex c) {
        Complex[][] e = new Complex[elements.length][];
        for (int i = 0; i < elements.length; i++) {
            e[i] = new Complex[elements[i].length];
            for (int j = 0; j < elements[i].length; j++) {
                e[i][j] = elements[i][j].mul(c);
            }
        }
        return newMemMatrix(e);
    }

    @Override
    public Matrix mul(double c) {
        Complex[][] e = new Complex[elements.length][];
        for (int i = 0; i < elements.length; i++) {
            e[i] = new Complex[elements[i].length];
            for (int j = 0; j < elements[i].length; j++) {
                e[i][j] = elements[i][j].mul(c);
            }
        }
        return newMemMatrix(e);
    }

    @Override
    public Matrix neg() {
        Complex[][] e = new Complex[elements.length][];
        for (int i = 0; i < elements.length; i++) {
            e[i] = new Complex[elements[i].length];
            for (int j = 0; j < elements[i].length; j++) {
                e[i][j] = elements[i][j].neg();
            }
        }
        return newMemMatrix(e);
    }

    @Override
    public Matrix add(Complex c) {
        Complex[][] e = new Complex[elements.length][];
        for (int i = 0; i < elements.length; i++) {
            e[i] = new Complex[elements[i].length];
            for (int j = 0; j < elements[i].length; j++) {
                e[i][j] = elements[i][j].add(c);
            }
        }
        return newMemMatrix(e);
    }

    @Override
    public Matrix sub(Complex c) {
        Complex[][] e = new Complex[elements.length][];
        for (int i = 0; i < elements.length; i++) {
            e[i] = new Complex[elements[i].length];
            for (int j = 0; j < elements[i].length; j++) {
                e[i][j] = elements[i][j].sub(c);
            }
        }
        return newMemMatrix(e);
    }

    @Override
    public Matrix mul(TMatrix<Complex> other) {
        if (getColumnCount() != other.getRowCount()) {
            throw new IllegalArgumentException("The column dimension " + getColumnCount() + " of the left matrix does not match the row dimension " + other.getRowCount() + " of the right matrix!");
        }
        if (other instanceof MemComplexMatrix) {
            MemComplexMatrix mm = (MemComplexMatrix) other;
            MutableComplex sum = MutableComplex.Zero();
            int a_rows = elements.length;
            int b_cols = mm.elements[0].length;
            int b_rows = mm.elements.length;
            Complex[][] newElements = new Complex[a_rows][b_cols];
            for (int i = 0; i < a_rows; i++) {
                for (int j = 0; j < b_cols; j++) {
                    sum.setZero();
                    ;
                    for (int k = 0; k < b_rows; k++) {
                        sum.add(elements[i][k].mul(mm.elements[k][j]));
                    }
                    newElements[i][j] = sum.toImmutable();
                }
            }
            return newMemMatrix(newElements);
        } else {
            MutableComplex sum = MutableComplex.Zero();
            int a_rows = elements.length;
            int b_cols = other.getColumnCount();
            int b_rows = other.getRowCount();
            Complex[][] newElements = new Complex[a_rows][b_cols];
            for (int i = 0; i < a_rows; i++) {
                for (int j = 0; j < b_cols; j++) {
                    sum.setZero();
                    ;
                    for (int k = 0; k < b_rows; k++) {
                        sum.add(elements[i][k].mul(other.get(k, j)));
                    }
                    newElements[i][j] = sum.toImmutable();
                }
            }
            return newMemMatrix(newElements);
        }
    }

    @Override
    public Matrix dotmul(TMatrix<Complex> other) {
        if (other instanceof MemComplexMatrix) {
            MemComplexMatrix mm = (MemComplexMatrix) other;
            Complex[][] newElements = new Complex[elements.length][mm.elements[0].length];
            for (int i = 0; i < newElements.length; i++) {
                for (int j = 0; j < newElements[i].length; j++) {
                    newElements[i][j] = elements[i][j].mul(mm.elements[i][j]);
                }
            }
            return newMemMatrix(newElements);
        } else {
            Complex[][] newElements = new Complex[getRowCount()][getColumnCount()];
            for (int i = 0; i < newElements.length; i++) {
                for (int j = 0; j < newElements[i].length; j++) {
                    newElements[i][j] = elements[i][j].mul(other.get(i, j));
                }
            }
            return newMemMatrix(newElements);
        }
    }

    @Override
    public Matrix dotdiv(TMatrix<Complex> other) {
        if (other instanceof MemComplexMatrix) {
            MemComplexMatrix mm = (MemComplexMatrix) other;
            Complex[][] newElements = new Complex[elements.length][mm.elements[0].length];
            for (int i = 0; i < newElements.length; i++) {
                for (int j = 0; j < newElements[i].length; j++) {
                    newElements[i][j] = elements[i][j].div(mm.elements[i][j]);
                }
            }
            return newMemMatrix(newElements);
        } else {
            Complex[][] newElements = new Complex[getRowCount()][getColumnCount()];
            for (int i = 0; i < newElements.length; i++) {
                for (int j = 0; j < newElements[i].length; j++) {
                    newElements[i][j] = elements[i][j].div(other.get(i, j));
                }
            }
            return newMemMatrix(newElements);
        }
    }

    @Override
    public Matrix add(TMatrix<Complex> other) {
        if (other instanceof MemComplexMatrix) {
            MemComplexMatrix mm = (MemComplexMatrix) other;
            Complex[][] e = new Complex[elements.length][];
            for (int i = 0; i < elements.length; i++) {
                e[i] = new Complex[elements[i].length];
                for (int j = 0; j < elements[i].length; j++) {
                    e[i][j] = elements[i][j].add(mm.elements[i][j]);
                }
            }
            return newMemMatrix(e);
        } else {
            Complex[][] e = new Complex[elements.length][];
            for (int i = 0; i < elements.length; i++) {
                e[i] = new Complex[elements[i].length];
                for (int j = 0; j < elements[i].length; j++) {
                    e[i][j] = elements[i][j].add(other.get(i, j));
                }
            }
            return newMemMatrix(e);
        }
    }

    @Override
    public Matrix sub(TMatrix<Complex> other) {
        if (other instanceof MemComplexMatrix) {
            MemComplexMatrix mm = (MemComplexMatrix) other;
            Complex[][] e = new Complex[elements.length][];
            for (int i = 0; i < elements.length; i++) {
                e[i] = new Complex[elements[i].length];
                for (int j = 0; j < elements[i].length; j++) {
                    e[i][j] = elements[i][j].sub(mm.elements[i][j]);
                }
            }
            return newMemMatrix(e);
        } else {
            Complex[][] e = new Complex[elements.length][];
            for (int i = 0; i < elements.length; i++) {
                e[i] = new Complex[elements[i].length];
                for (int j = 0; j < elements[i].length; j++) {
                    e[i][j] = elements[i][j].sub(other.get(i, j));
                }
            }
            return newMemMatrix(e);
        }
    }

    @Override
    public Complex getScalar() {
        return get(0, 0);
    }

    @Override
    public Complex get(int row, int col) {
        return elements[row][col];
    }

    @Override
    public Complex get(int vectorIndex) {
        if (getColumnCount() == 1) {
            return elements[vectorIndex][0];
        }
        if (getRowCount() == 1) {
            return elements[0][vectorIndex];
        }
        throw new IllegalArgumentException("No a valid vector");
    }

    @Override
    public void add(int row, int col, Complex val) {
        elements[row][col] = elements[row][col].add(val);
    }

    @Override
    public void mul(int row, int col, Complex val) {
        elements[row][col] = elements[row][col].mul(val);
    }

    @Override
    public void div(int row, int col, Complex val) {
        elements[row][col] = elements[row][col].div(val);
    }

    @Override
    public void sub(int row, int col, Complex val) {
        elements[row][col] = elements[row][col].sub(val);
    }

    @Override
    public void set(int row, int col, Complex val) {
        elements[row][col] = val;
    }

    @Override
    public void set(int row, int col, TMatrix<Complex> src, int srcRow, int srcCol, int rows, int cols) {
        if (src instanceof MemComplexMatrix) {
            MemComplexMatrix mm = (MemComplexMatrix) src;
            for (int i = 0; i < rows; i++) {
                System.arraycopy(mm.elements[i + srcRow], srcCol, elements[i + row], col, cols);
            }
        } else {
            for (int i = 0; i < rows; i++) {
                System.arraycopy(src.getRow(i + srcRow).toArray(), srcCol, elements[i + row], col, cols);
            }
        }
    }

    @Override
    public void set(int row, int col, TMatrix<Complex> subMatrix) {
        set(row, col, subMatrix, 0, 0, subMatrix.getRowCount(), subMatrix.getColumnCount());
    }

    @Override
    public int getRowCount() {
        return elements.length;
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.create(
                elements.length,
                elements.length == 0 ? 0 : elements[0].length
        );
    }

    @Override
    public int getColumnCount() {
        return elements.length == 0 ? 0 : elements[0].length;
    }

    /**
     * Hermitian conjugate
     *
     * @return
     */
    @Override
    public Matrix transposeHermitian() {
        int r = elements.length;
        if (r == 0) {
            return this;
        }
        int c = elements[0].length;
        Complex[][] e = new Complex[c][r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                e[j][i] = elements[i][j].conj();
            }
//            for (int j = 0; j < i; j++) {
//                e[j][i] = elements[i][j];
//            }
//            for (int j = i+1; j < c; j++) {
//                e[j][i] = elements[i][j];
//            }
        }
        return newMemMatrix(e);
    }

    @Override
    public Matrix arrayTranspose() {
        int r = elements.length;
        if (r == 0) {
            return this;
        }
        int c = elements[0].length;
        Complex[][] e = new Complex[c][r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                e[j][i] = elements[i][j];
            }
        }
        return newMemMatrix(e);
    }


    @Override
    public Matrix invSolve() {
        return solve(getFactory().newIdentity(elements.length));
    }

    /**
     * inversion using Bloc Matrix expansion
     *
     * @return inverse
     */
    @Override
    public Matrix invBlock(InverseStrategy delegate, int precision) {
        int n = getRowCount();
        int p = getColumnCount();
        if (n != p || (n > 1 && n % 2 != 0) || (precision > 1 && n > 1 && n <= precision)) {
            return inv(delegate);
        }
        switch (n) {
            case 1: {
                return newMemMatrix(new Complex[][]{{elements[0][0].inv()}});
            }
            case 2: {
                Complex A = elements[0][0];
                Complex B = elements[0][1];
                Complex C = elements[1][0];
                Complex D = elements[1][1];
                Complex Ai = A.inv();
                Complex CAi = C.mul(Ai);
                Complex AiB = Ai.mul(B);
                Complex DCABi = D.sub(C.mul(Ai).mul(B)).inv();
                Complex mDCABi = DCABi.mul(-1);
                Complex AiBmDCABi = AiB.mul(mDCABi);
                return newMemMatrix(new Complex[][]{
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
                return getFactory().newMatrix(new Matrix[][]{
                        {Ai.sub(AiBmDCABi.mul(CAi)), AiBmDCABi},
                        {mDCABi.mul(CAi), DCABi}
                });
            }
            //case 2?
        }
    }

    @Override
    public MemComplexMatrix invGauss() {
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
                A[i][j] = elements[i][j];
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
            return newMemMatrix(B);
        }
    }

    /**
     * Formula used to Calculate Inverse: inv(A) = 1/det(A) * adj(A)
     *
     * @return inverse
     */
    @Override
    public MemComplexMatrix invAdjoint() {
        // Formula used to Calculate Inverse:
        // inv(A) = 1/det(A) * adj(A)
        int tms = elements.length;

        Complex[][] m = new Complex[tms][tms];

        Complex det = det();

        if (det.equals(Complex.ZERO)) {
            throw new ArithmeticException("Determinant Equals 0, Not Invertible.");
        }

//            System.out.println("determinant is " + det);
        Complex dd = det.inv();
        if (tms == 1) {
            return newMemMatrix(new Complex[][]{{dd}});
        }
        Complex[][] mm = adjoint().elements;
        for (int i = 0; i < tms; i++) {
            for (int j = 0; j < tms; j++) {
                m[i][j] = dd.mul(mm[i][j]);
            }
        }

        return newMemMatrix(m);
    }

    @Override
    public MemComplexMatrix coMatrix(int row, int col) {
        int tms = elements.length;
        Complex ap[][] = new Complex[tms - 1][tms - 1];
        int ia = 0;
        int ja;
        for (int ii = 0; ii < row; ii++) {
            ja = 0;
            Complex[] eii = elements[ii];
            Complex[] apia = ap[ia];
            for (int jj = 0; jj < col; jj++) {
                apia[ja] = eii[jj];
                ja++;
            }
            for (int jj = col + 1; jj < tms; jj++) {
                apia[ja] = eii[jj];
                ja++;
            }
            ia++;
        }
        for (int ii = row + 1; ii < tms; ii++) {
            ja = 0;
            Complex[] eii = elements[ii];
            Complex[] apia = ap[ia];
            for (int jj = 0; jj < col; jj++) {
                apia[ja] = eii[jj];
                ja++;
            }
            for (int jj = col + 1; jj < tms; jj++) {
                apia[ja] = eii[jj];
                ja++;
            }
            ia++;
        }
        return newMemMatrix(ap);
    }


    @Override
    public MemComplexMatrix adjoint() {
        int tms = elements.length;

        Complex[][] m = new Complex[tms][tms];

        for (int i = 0; i < tms; i++) {
            for (int j = 0; j < tms; j++) {
                Complex det = coMatrix(i, j).det();
                // (-1) power (i+j)
                if (((i + j) % 2) != 0) {
                    det = det.mul(-1);
                }
                // transpose needed;
                m[j][i] = det;
            }
        }
        return newMemMatrix(m);
    }

    @Override
    public Complex det() {
        int tms = elements.length;

        Complex det = Complex.ONE;
        Matrix mt = upperTriangle();
        if (mt instanceof MemComplexMatrix) {
            Complex[][] matrix = ((MemComplexMatrix) mt).elements;

            for (int i = 0; i < tms; i++) {
                det = det.mul(matrix[i][i]);
            }
        } else {
            for (int i = 0; i < tms; i++) {
                det = det.mul(mt.get(i, i));
            }
        }
        // multiply down diagonal
//        int iDF = 1;
//        det = det.multiply(iDF);                    // adjust w/ determinant factor

        return det;
    }

    @Override
    public Matrix upperTriangle() {
//        System.out.println(new java.util.Date() + " upperTriangle IN (" + elements.length + ")");
        MemComplexMatrix o = newMemMatrix(this.getArrayCopy());

        Complex f1;
        Complex temp;
        int tms = o.elements.length;  // get This Matrix Size (could be smaller than global)
        int v;

        int iDF = 1;

        for (int col = 0; col < tms - 1; col++) {
            for (int row = col + 1; row < tms; row++) {
                v = 1;

                outahere:
                while (o.elements[col][col].equals(Complex.ZERO)) // check if 0 in diagonal
                {                                   // if so switch until not
                    if (col + v >= tms) // check if switched all rows
                    {
                        iDF = 0;
                        break outahere;
                    } else {
                        for (int c = 0; c < tms; c++) {
                            temp = o.elements[col][c];
                            o.elements[col][c] = o.elements[col + v][c];       // switch rows
                            o.elements[col + v][c] = temp;
                        }
                        v++;                            // count row switchs
                        iDF = iDF * -1;                 // each switch changes determinant factor
                    }
                }

                if (!o.elements[col][col].equals(Complex.ZERO)) {

                    f1 = o.elements[row][col].div(o.elements[col][col]).mul(-1);
                    for (int i = col; i < tms; i++) {
                        o.elements[row][i] = f1.mul(o.elements[col][i]).add(o.elements[row][i]);
                    }

                }

            }
        }

//        System.out.println(new java.util.Date() + " upperTriangle OUT");
        return o;
    }


    @Override
    public Complex[][] getArrayCopy() {
        Complex[][] C = new Complex[getRowCount()][getColumnCount()];
        for (int i = 0; i < C.length; i++) {
            System.arraycopy(elements[i], 0, C[i], 0, C[i].length);
        }
        return C;
    }

    @Override
    public double[][] absdbls() {
        double[][] d = new double[getRowCount()][getColumnCount()];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                d[i][j] = elements[i][j].absdbl();
            }
        }
        return d;
    }

    @Override
    public Matrix abs() {
        Complex[][] d = new Complex[getRowCount()][getColumnCount()];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                d[i][j] = Complex.valueOf(elements[i][j].absdbl());
            }
        }
        return newMemMatrix(d);
    }

    @Override
    public Matrix abssqr() {
        Complex[][] d = new Complex[getRowCount()][getColumnCount()];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                d[i][j] = Complex.valueOf(elements[i][j].absdblsqr());
            }
        }
        return newMemMatrix(d);
    }


    @Override
    public double condHadamard() {
        Complex det = det();
        double x = 1;
        double alpha;
        for (Complex[] element : elements) {
            alpha = 0;
            for (Complex complex : element) {
                alpha = alpha + complex.absdblsqr();
            }
            x *= Maths.sqrt(alpha);
        }
        return det.absdbl() / x;
    }

    @Override
    public MemComplexMatrix sparsify(double ceil) {
        Complex[][] array = getArrayCopy();
        double max = Double.NaN;
        for (Complex[] complexes : array) {
            for (Complex complex : complexes) {
                double d = complex.absdbl();
                if (!Double.isNaN(d) && (Double.isNaN(max) || d > max)) {
                    max = d;
                }
            }
        }
        if (!Double.isNaN(max)) {
            for (Complex[] complexes : array) {
                for (int j = 0; j < complexes.length; j++) {
                    double d = complexes[j].absdbl();
                    if (!Double.isNaN(d)) {
                        d = d / max * 100;
                        if (d <= ceil) {
                            complexes[j] = Complex.ZERO;
                        }
                    }
                }
            }
        }
        return newMemMatrix(array);
    }

    @Override
    public Complex complexValue() {
        return elements.length == 0 ? Complex.NaN : elements[0][0];
    }

    @Override
    public double maxAbs() {
        double f = 0;
        double f0 = elements[0][0].absdbl();
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[0].length; j++) {
                f0 = elements[i][j].absdbl();
                f = Math.max(f, f0);
            }
        }
        return f;
    }

    @Override
    public double minAbs() {
        double f = 0;
        double f0 = elements[0][0].absdbl();
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[0].length; j++) {
                f0 = elements[i][j].absdbl();
                f = Math.min(f, f0);
            }
        }
        return f;
    }

    @Override
    public boolean isScalar() {
        return elements.length == 1 && elements[0].length == 1;
    }

    @Override
    public boolean isComplex() {
        return elements.length == 1 && elements[0].length == 1;
    }

    @Override
    public boolean isDouble() {
        return isComplex() && toComplex().isDouble();
    }

    @Override
    public Complex toComplex() {
        if (isComplex()) {
            return elements[0][0];
        }
        throw new ClassCastException();
    }

    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }


    @Override
    public boolean isZero() {
        for (Complex[] row : elements) {
            for (Complex cell : row) {
                if (!cell.isZero()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
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

    @Override
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

    public void dispose() {

    }

    @Override
    public void resize(int rows, int columns) {
        int rr = getRowCount();
        int cc = getColumnCount();
        if (rows != rr || columns != cc) {
            Complex[][] elements2 = new Complex[rows][columns];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    if (r < rr && c < cc) {
                        elements2[r][c] = elements[r][c];
                    } else {
                        elements2[r][c] = Complex.ZERO;
                    }
                }
            }
            this.elements = elements2;
        }
    }


//    private void writeObject(ObjectOutputStream oos)
//            throws IOException {
//        // default serialization
//        oos.defaultWriteObject();
//        int rowCount = getRowCount();
//        oos.writeInt(rowCount);
//        int columnCount = getColumnCount();
//        oos.writeInt(columnCount);
//        for (int i = 0; i < rowCount; i++) {
//            for (int j = 0; j < columnCount; j++) {
//                Complex.writeObjectHelper(get(i, j),oos);
//            }
//        }
//    }
//
//    private void readObject(ObjectInputStream ois)
//            throws ClassNotFoundException, IOException {
//        // default deserialization
//        ois.defaultReadObject();
//        int rowCount = ois.readInt(); // Replace with real deserialization
//        int columnCount = ois.readInt(); // Replace with real deserialization
//        elements=new Complex[rowCount][columnCount];
//        for (int i = 0; i < rowCount; i++) {
//            for (int j = 0; j < columnCount; j++) {
//                elements[i][j]=Complex.readObjectResolveHelper(ois);
//            }
//        }
//    }

}
