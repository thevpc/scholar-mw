package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public class DefaultMatrix<T> extends AbstractMatrix<T> implements Serializable {
    private static final long serialVersionUID = 1;

    private final VectorSpace<T> vs;
    private MatrixModel<T> elements;
    private MatrixStore<T> welements;

    public DefaultMatrix(MatrixModel<T> model, VectorSpace<T> vs) {
        this.elements = model;
        if (elements instanceof MatrixStore) {
            this.welements = (MatrixStore<T>) model;
        }
        this.vs = vs;
    }

    public DefaultMatrix(int rows, int cols, MatrixCell<T> model, VectorSpace<T> vs) {
        this.elements = new MatrixCellToModel<>(rows, cols, model);
        this.vs = vs;
    }

    /*To exchange two rows in a matrix*/
    public static <T> void exchange_row(T[][] M, int k, int l, int m, int n) {
        if (k <= 0 || l <= 0 || k > n || l > n || k == l) {
            return;
        }
        T tmp;
        for (int j = 0; j < n; j++) {
            tmp = M[k - 1][j];
            M[k - 1][j] = M[l - 1][j];
            M[l - 1][j] = tmp;
        }
    }

    private T[] newArr(int rows) {
        return (T[]) Array.newInstance(vs.getItemType().getTypeClass(), rows);
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
                s += vs.absdbl(get(r, c));
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
                f += vs.absdblsqr(get(r, c));
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
                f = Math.max(f, vs.absdbl(get(r, j)));
            }
        }
        return f;
    }

//    private GenericTMatrix<T> newMemMatrix(T[][] e) {
//        return new GenericTMatrix<T>(e,vs);
//    }

    /**
     * Infinity norm
     *
     * @return maximum row sum.
     */
    @Override
    public double normInf() {
        double f = 0;
        for (Vector<T> element : getRows()) {
            double s = 0;
            for (T anElement : element) {
                s += vs.absdbl(anElement);
            }
            f = Math.max(f, s);
        }
        return f;
    }

    @Override
    public void setAll(T value) {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                writableElements().set(i, j, value);
            }
        }
    }

    private MatrixStore<T> writableElements() {
        if (this.welements == null) {
            throw new IllegalArgumentException("Not writable");
        }
        return this.welements;
    }

    @Override
    public T[][] getArray() {
        T[][] t = newArr(getRowCount(), getColumnCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                t[i][j] = elements.get(i, j);
            }
        }
        return t;
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
    public Matrix<T> getMatrix(int i0, int i1, int j0, int j1) {
        DefaultMatrix<T> X = newMemMatrix(i1 - i0 + 1, j1 - j0 + 1);
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
    @Override
    public Matrix<T> getMatrix(int[] r, int[] c) {
        DefaultMatrix<T> X = newMemMatrix(r.length, c.length);
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = 0; j < c.length; j++) {
                    X.set(i, j, elements.get(r[i], c[j]));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }

    private DefaultMatrix<T> newMemMatrix(int rows, int cols) {
        return new DefaultMatrix<T>(new ArrayMatrixStore(rows, cols, vs), vs);
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
    public Matrix<T> getMatrix(int r1, int r2, int[] c) {
        DefaultMatrix<T> X = newMemMatrix(r2 - r1 + 1, c.length);
        try {
            for (int i = r1; i <= r2; i++) {
                for (int j = 0; j < c.length; j++) {
                    X.set(i - r1, j, elements.get(i, c[j]));
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
    public Matrix<T> getMatrix(int[] r, int c1, int c2) {
        Matrix<T> X = newMemMatrix(r.length, c2 - c1 + 1);
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = c1; j <= c2; j++) {
                    X.set(i, j - c1, elements.get(r[i], j));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }

    @Override
    public Matrix<T> div(double c) {
        Matrix<T> e = newMemMatrix(getRowCount(), getColumnCount());
        T converted = vs.convert(c);
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                e.set(i, j, vs.div(elements.get(i, j), converted));
            }
        }
        return e;
    }

    @Override
    public Matrix<T> div(T c) {
        Matrix<T> e = newMemMatrix(getRowCount(), getColumnCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                e.set(i, j, vs.div(elements.get(i, j), c));
            }
        }
        return e;
    }

    @Override
    public Matrix<T> mul(T c) {
        Matrix<T> e = newMemMatrix(getRowCount(), getColumnCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                e.set(i, j, vs.mul(elements.get(i, j), c));
            }
        }
        return e;
    }

    @Override
    public Matrix<T> mul(double c) {
        T c0 = vs.convert(c);
        Matrix<T> e = newMemMatrix(getRowCount(), getColumnCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                e.set(i, j, vs.mul(elements.get(i, j), c0));
            }
        }
        return e;
    }

    @Override
    public Matrix<T> neg() {
        Matrix<T> e = newMemMatrix(getRowCount(), getColumnCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                e.set(i, j, vs.neg(elements.get(i, j)));
            }
        }
        return e;
    }

    @Override
    public Matrix<T> add(T c) {
        Matrix<T> e = newMemMatrix(getRowCount(), getColumnCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                e.set(i, j, vs.add(elements.get(i, j), c));
            }
        }
        return e;
    }

    @Override
    public Matrix<T> sub(T c) {
        Matrix<T> e = newMemMatrix(getRowCount(), getColumnCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                e.set(i, j, vs.minus(elements.get(i, j), c));
            }
        }
        return e;
    }

    @Override
    public Matrix<T> mul(Matrix<T> other) {
        if (getColumnCount() != other.getRowCount()) {
            throw new IllegalArgumentException("The column dimension " + getColumnCount() + " of the left matrix does not match the row dimension " + other.getRowCount() + " of the right matrix!");
        }
        int a_rows = getRowCount();
        int b_cols = other.getColumnCount();
        int b_rows = other.getRowCount();
        MatrixStore<T> newElements = newArrStore(a_rows, b_cols);
        for (int i = 0; i < a_rows; i++) {
            for (int j = 0; j < b_cols; j++) {
                RepeatableOp<T> sum = vs.addRepeatableOp();
                for (int k = 0; k < b_rows; k++) {
                    sum.append(vs.mul(elements.get(i, k), other.get(k, j)));
                }
                newElements.set(i, j, sum.eval());
            }
        }
        return newMemMatrix(newElements);
    }

    @Override
    public Matrix<T> dotmul(Matrix<T> other) {
        int a_rows = getRowCount();
        int b_cols = getColumnCount();
        MatrixStore<T> newElements = newArrStore(a_rows, b_cols);
        for (int i = 0; i < a_rows; i++) {
            for (int j = 0; j < b_cols; j++) {
                newElements.set(i, j, vs.mul(elements.get(i, j), other.get(i, j)));
            }
        }
        return newMemMatrix(newElements);
    }

    @Override
    public Matrix<T> dotdiv(Matrix<T> other) {
        int a_rows = getRowCount();
        int b_cols = getColumnCount();
        MatrixStore<T> newElements = newArrStore(a_rows, b_cols);
        for (int i = 0; i < a_rows; i++) {
            for (int j = 0; j < b_cols; j++) {
                newElements.set(i, j, vs.div(elements.get(i, j), other.get(i, j)));
            }
        }
        return newMemMatrix(newElements);
    }

    @Override
    public Matrix<T> add(Matrix<T> other) {
        int a_rows = getRowCount();
        int b_cols = getColumnCount();
        MatrixStore<T> newElements = newArrStore(a_rows, b_cols);
        for (int i = 0; i < a_rows; i++) {
            for (int j = 0; j < b_cols; j++) {
                newElements.set(i, j, vs.add(elements.get(i, j), other.get(i, j)));
            }
        }
        return newMemMatrix(newElements);
    }

    @Override
    public Matrix<T> sub(Matrix<T> other) {
        int a_rows = getRowCount();
        int b_cols = getColumnCount();
        MatrixStore<T> newElements = newArrStore(a_rows, b_cols);
        for (int i = 0; i < a_rows; i++) {
            for (int j = 0; j < b_cols; j++) {
                newElements.set(i, j, vs.minus(elements.get(i, j), other.get(i, j)));
            }
        }
        return newMemMatrix(newElements);
    }

    @Override
    public T getScalar() {
        return get(0, 0);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.of(
                elements.getRowCount(),
                elements.getColumnCount()
        );
    }

    @Override
    public T get(int vectorIndex) {
        if (getColumnCount() == 1) {
            return elements.get(vectorIndex, 0);
        }
        if (getRowCount() == 1) {
            return elements.get(0, vectorIndex);
        }
        throw new IllegalArgumentException("No a valid vector");
    }

    @Override
    public void add(int row, int col, T val) {
        set(row, col, vs.add(elements.get(row, col), val));
    }

    @Override
    public void mul(int row, int col, T val) {
        set(row, col, vs.mul(elements.get(row, col), val));
    }

    @Override
    public void div(int row, int col, T val) {
        set(row, col, vs.div(elements.get(row, col), val));
    }

    @Override
    public void sub(int row, int col, T val) {
        set(row, col, vs.minus(elements.get(row, col), val));
    }

    @Override
    public void set(int row, int col, Matrix<T> src, int srcRow, int srcCol, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            src.getRow(i + srcRow).rangeCopy(srcCol, src.getRow(i + row), col, cols);
        }
    }

    @Override
    public void set(int row, int col, Matrix<T> subMatrix) {
        set(row, col, subMatrix, 0, 0, subMatrix.getRowCount(), subMatrix.getColumnCount());
    }

    /**
     * Hermitian conjugate
     *
     * @return
     */
    @Override
    public Matrix<T> transposeHermitian() {
        int r = elements.getRowCount();
        if (r == 0) {
            return this;
        }
        int c = elements.getColumnCount();
        MatrixStore<T> e = newArrStore(c, r);
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                e.set(j, i, vs.conj(elements.get(i, j)));
            }
        }
        return newMemMatrix(e);
    }

    @Override
    public Matrix<T> arrayTranspose() {
        int r = getRowCount();
        if (r == 0) {
            return this;
        }
        int c = getColumnCount();
        MatrixStore<T> e = newArrStore(c, r);
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                e.set(j, i, elements.get(i, j));
            }
        }
        return newMemMatrix(e);
    }

    @Override
    public Matrix<T> invSolve() {
        return solve(getFactory().newIdentity(elements.getRowCount()));
    }

    /**
     * inversion using Bloc Matrix<T> expansion
     *
     * @return inverse
     */
    @Override
    public Matrix<T> invBlock(InverseStrategy delegate, int precision) {
        int n = getRowCount();
        int p = getColumnCount();
        if (n != p || (n > 1 && n % 2 != 0) || (precision > 1 && n > 1 && n <= precision)) {
            return inv(delegate);
        }
        switch (n) {
            case 1: {
                T[][] e = newArr(1, 1);
                e[0][0] = vs.inv(elements.get(0, 0));
                return newMemMatrix(new ArrayMatrixStore<T>(e));
            }
            case 2: {
                T A = elements.get(0, 0);
                T B = elements.get(0, 1);
                T C = elements.get(1, 0);
                T D = elements.get(1, 1);
                T Ai = vs.inv(A);
                T CAi = vs.mul(C, Ai);
                T AiB = vs.mul(Ai, B);
                T DCABi = vs.inv(vs.minus(D, vs.mul(vs.mul(C, Ai), B)));
                T mDCABi = vs.mul(DCABi, vs.convert(-1));
                T AiBmDCABi = vs.mul(AiB, mDCABi);
                T[][] aa = newArr(2, 2);
                aa[0][0] = vs.minus(Ai, vs.mul(AiBmDCABi, CAi));
                aa[0][1] = AiBmDCABi;
                aa[1][0] = vs.mul(mDCABi, CAi);
                aa[1][1] = DCABi;
                return newMemMatrix(new ArrayMatrixStore<T>(aa));
            }
            default: {
                int n2 = n / 2;
                Matrix<T> A = getMatrix(0, n2 - 1, 0, n2 - 1);
                Matrix<T> B = getMatrix(n2, n - 1, 0, n2 - 1);
                Matrix<T> C = getMatrix(0, n2 - 1, n2, n - 1);
                Matrix<T> D = getMatrix(n2, n - 1, n2, n - 1);
                Matrix<T> Ai = A.invBlock(delegate, precision);
                Matrix<T> CAi = C.mul(Ai);
                Matrix<T> AiB = Ai.mul(B);
                Matrix<T> DCABi = D.sub(C.mul(Ai).mul(B)).invBlock(delegate, precision);
                Matrix<T> mDCABi = DCABi.mul(-1);
                Matrix<T> AiBmDCABi = AiB.mul(mDCABi);
                Matrix<T>[][] tMatrices = new Matrix[][]{
                        {Ai.sub(AiBmDCABi.mul(CAi)), AiBmDCABi},
                        {mDCABi.mul(CAi), DCABi}
                };
                return getFactory().newMatrix(tMatrices);
            }
            //case 2?
        }
    }

    private T[][] newArr(int rows, int cols) {
        return (T[][]) Array.newInstance(vs.getItemType().getTypeClass(), rows, cols);
    }

    @Override
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
        Matrix<T> A = newMemMatrix(m, n);
        Matrix<T> B = newMemMatrix(m, n);

        //Copie de M dans A et Mise en forme de B : B=I
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, elements.get(i, j));
                if (i == j) {
                    B.set(i, j, vs.one());
                } else {
                    B.set(i, j, vs.zero());
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
                        if (!vs.isZero(a)) {
                            J.add(l);
                            cnt_col++;
                            bl = false; //permet de sortir de la boucle car le pivot a été trouvé
                            for (int p = 0; p < n; p++) {
                                if (p != k) {
                                    tmp = A.get(p, l);
                                    for (int q = 0; q < n; q++) {
                                        A.set(p, q, vs.minus(A.get(p, q), vs.mul(A.get(k, q), vs.div(tmp, a))));
                                        B.set(p, q, vs.minus(B.get(p, q), vs.mul(B.get(k, q), (vs.div(tmp, a)))));
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
            throw new ArithmeticException("Matrix<T> is singular");
            //Le pivot n'a pas pu être trouve précédemment, ce qui a donne bk = false
        } else {
            //Réorganisation des colonnes de sorte que A=I et B=Inv(M). Méthode de Gauss-Jordan
            for (int l = 0; l < n; l++) {
                for (int k = 0; k < n; k++) {
                    a = A.get(k, l);
                    if (!vs.isZero(a)) {
                        A.set(k, l, vs.one());
                        for (int p = 0; p < n; p++) {
                            B.set(k, p, vs.div(B.get(k, p), a));
                        }
                        if (k != l) {
                            exchange_row(A, k + 1, l + 1, n, n);
                            exchange_row(B, k + 1, l + 1, n, n);
                        }
                        k = n; //Pour sortir de la boucle car le coefficient non nul a ete trouve
                    }
                }
            }
            return B;
        }
    }

    /**
     * Formula used to Calculate Inverse: inv(A) = 1/det(A) * adj(A)
     *
     * @return inverse
     */
    @Override
    public Matrix<T> invAdjoint() {
        // Formula used to Calculate Inverse:
        // inv(A) = 1/det(A) * adj(A)
        int tms = elements.getRowCount();

        Matrix<T> m = newMemMatrix(tms, tms);

        T det = det();

        if (vs.isZero(det)) {
            throw new ArithmeticException("Determinant Equals 0, Not Invertible.");
        }

//            System.out.println("determinant is " + det);
        T dd = vs.inv(det);
        if (tms == 1) {
            T[][] tt = newArr(1, 1);
            tt[0][0] = dd;
            return newMemMatrix(new ArrayMatrixStore<T>(tt));
        }
        DefaultMatrix<T> mm = adjoint();
        for (int i = 0; i < tms; i++) {
            for (int j = 0; j < tms; j++) {
                m.set(i, j, vs.mul(dd, mm.get(i, j)));
            }
        }

        return m;
    }

    @Override
    public Matrix<T> coMatrix(int row, int col) {
        int tms = elements.getRowCount();
        Matrix<T> ap = newMemMatrix(tms - 1, tms - 1);
        int ia = 0;
        int ja;
        for (int ii = 0; ii < row; ii++) {
            ja = 0;
            Vector<T> eii = getRow(ii);
            Vector<T> apia = ap.getRow(ia);
            for (int jj = 0; jj < col; jj++) {
                apia.set(ja, eii.get(jj));
                ja++;
            }
            for (int jj = col + 1; jj < tms; jj++) {
                apia.set(ja, eii.get(jj));
                ja++;
            }
            ia++;
        }
        for (int ii = row + 1; ii < tms; ii++) {
            ja = 0;
            Vector<T> eii = getRow(ii);
            Vector<T> apia = ap.getRow(ia);
            for (int jj = 0; jj < col; jj++) {
                apia.set(ja, eii.get(jj));
                ja++;
            }
            for (int jj = col + 1; jj < tms; jj++) {
                apia.set(ja, eii.get(jj));
                ja++;
            }
            ia++;
        }
        return ap;
    }

    @Override
    public DefaultMatrix<T> adjoint() {
        T minusOne = vs.convert(-1);
        int tms = elements.getRowCount();

        MatrixStore<T> m = newArrStore(tms, tms);

        for (int i = 0; i < tms; i++) {
            for (int j = 0; j < tms; j++) {
                T det = coMatrix(i, j).det();
                // (-1) power (i+j)
                if (((i + j) % 2) != 0) {
                    det = vs.mul(det, minusOne);
                }
                // transpose needed;
                m.set(j, i, det);
            }
        }
        return newMemMatrix(m);
    }

    @Override
    public T det() {
        int tms = elements.getRowCount();

        T det = vs.one();
        Matrix<T> mt = upperTriangle();
        if (mt instanceof DefaultMatrix) {
            for (int i = 0; i < tms; i++) {
                det = vs.mul(det, mt.get(i, i));
            }
        } else {
            for (int i = 0; i < tms; i++) {
                det = vs.mul(det, mt.get(i, i));
            }
        }
        // multiply down diagonal
//        int iDF = 1;
//        det = det.multiply(iDF);                    // adjust w/ determinant factor

        return det;
    }

    @Override
    public Matrix<T> upperTriangle() {
//        System.out.println(new java.util.Date() + " upperTriangle IN (" + primitiveElement3DS.length + ")");
        Matrix<T> o = getMatrixCopy();
        T f1;
        T temp;
        int tms = o.getRowCount();  // get This Matrix<T> Size (could be smaller than global)
        int v;

        int iDF = 1;

        for (int col = 0; col < tms - 1; col++) {
            for (int row = col + 1; row < tms; row++) {
                v = 1;

                outahere:
                while (vs.isZero(o.get(col, col))) // check if 0 in diagonal
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

                if (!vs.isZero(o.get(col, col))) {

                    f1 = vs.mul(vs.div(o.get(row, col), o.get(col, col)), vs.convert(-1));
                    for (int i = col; i < tms; i++) {
                        o.set(row, i, vs.add(vs.mul(f1, o.get(col, i)), o.get(row, i)));
                    }

                }

            }
        }

//        System.out.println(new java.util.Date() + " upperTriangle OUT");
        return o;
    }

    @Override
    public T[][] getArrayCopy() {
        return getArray();
    }

    @Override
    public double[][] absdbls() {
        double[][] d = new double[getRowCount()][getColumnCount()];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                d[i][j] = vs.absdbl(elements.get(i, j));
            }
        }
        return d;
    }

    @Override
    public Matrix<T> abs() {
        MatrixStore<T> d = newArrStore(getRowCount(), getColumnCount());
        for (int i = 0; i < d.getRowCount(); i++) {
            for (int j = 0; j < d.getColumnCount(); j++) {
                d.set(i, j, vs.abs(elements.get(i, j)));
            }
        }
        return newMemMatrix(d);
    }

    @Override
    public Matrix<T> abssqr() {
        MatrixStore<T> d = newArrStore(getRowCount(), getColumnCount());
        for (int i = 0; i < d.getRowCount(); i++) {
            for (int j = 0; j < d.getColumnCount(); j++) {
                d.set(i, j, vs.abssqr(elements.get(i, j)));
            }
        }
        return newMemMatrix(d);
    }

    @Override
    public double condHadamard() {
        T det = det();
        double x = 1;
        double alpha;
        for (Vector<T> row : getRows()) {
            alpha = 0;
            for (T complex : row) {
                alpha = alpha + vs.absdblsqr(complex);
            }
            x *= Maths.sqrt(alpha);
        }
        return vs.absdbl(det) / x;
    }

    @Override
    public Matrix<T> sparsify(double ceil) {
        Matrix<T> array = getMatrixCopy();
        double max = Double.NaN;
        for (Vector<T> complexes : array.getRows()) {
            for (T complex : complexes) {
                double d = vs.absdbl(complex);
                if (!Double.isNaN(d) && (Double.isNaN(max) || d > max)) {
                    max = d;
                }
            }
        }
        if (!Double.isNaN(max)) {
            for (int i = 0; i < array.getRowCount(); i++) {
                for (int j = 0; j < array.getColumnCount(); j++) {
                    double d = vs.absdbl(array.get(i, j));
                    if (!Double.isNaN(d)) {
                        d = d / max * 100;
                        if (d <= ceil) {
                            array.set(i, j, vs.zero());
                        }
                    }
                }
            }
        }
        return array;
    }

    @Override
    public double maxAbs() {
        double f = 0;
        double f0 = vs.absdbl(elements.get(0, 0));
        for (Vector<T> row : getRows()) {
            for (T cell : row) {
                f0 = vs.absdbl(cell);
                f = Math.max(f, f0);
            }
        }
        return f;
    }

    @Override
    public double minAbs() {
        double f = 0;
        double f0 = vs.absdbl(elements.get(0, 0));
        for (Vector<T> row : getRows()) {
            for (T cell : row) {
                f0 = vs.absdbl(cell);
                f = Math.min(f, f0);
            }
        }
        return f;
    }

    @Override
    public boolean isScalar() {
        return getRowCount() == 1 && getColumnCount() == 1;
    }

    @Override
    public boolean isDouble() {
        return isComplex() && toComplex().isReal();
    }

    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }

    @Override
    public boolean isZero() {
        for (Vector<T> row : getRows()) {
            for (T cell : row) {
                if (!vs.isZero(cell)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
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
    public void set(Matrix<T> other) {
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
    public Complex complexValue() {
        return isScalar() ? vs.toComplex(elements.get(0, 0)) : Complex.NaN;
    }

    @Override
    public boolean isComplex() {
        return isScalar()
                && vs.isComplex(elements.get(0, 0))
                ;
    }

    @Override
    public void resize(int rows, int columns) {
        int rr = getRowCount();
        int cc = getColumnCount();
        if (rows != rr || columns != cc) {
            MatrixStore<T> elements2 = newArrStore(rows, columns);
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    if (r < rr && c < cc) {
                        elements2.set(r, c, elements.get(r, c));
                    } else {
                        elements2.set(r, c, vs.zero());
                    }
                }
            }
            this.elements = elements2;
            this.welements = elements2;
        }
    }

    @Override
    public Complex toComplex() {
        if (isComplex()) {
            return vs.toComplex(elements.get(0, 0));
        }
        throw new ClassCastException();
    }

    @Override
    public void set(T[][] values) {
        int rows = values.length;
        int cols = values[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.set(i, j, values[i][j]);
            }
        }
    }

    public Matrix<T> getMatrixCopy() {
        Matrix<T> o = newMemMatrix(getRowCount(), getColumnCount());
        o.set(this);
        return o;
    }

    private MatrixStore<T> newArrStore(int rows, int cols) {
        return new ArrayMatrixStore<T>(rows, cols, vs);
    }

    private DefaultMatrix<T> newMemMatrix(MatrixStore<T> e) {
        return new DefaultMatrix<T>(e, vs);
    }

    @Override
    public T get(int row, int col) {
        return elements.get(row, col);
    }

    @Override
    public void set(int row, int col, T val) {
        writableElements().set(row, col, val);
    }

    @Override
    public int getRowCount() {
        return elements.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return elements.getColumnCount();
    }

    @Override
    public TypeName<T> getComponentType() {
        return vs.getItemType();
    }
}
