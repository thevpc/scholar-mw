package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.interop.ojalgo.OjalgoHelper;
import net.vpc.scholar.hadrumaths.util.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public final class MemMatrix extends AbstractMatrix implements Serializable {

    private static final long serialVersionUID = -1010101010101001044L;
    public static int BLOCK_PRECISION = 256;
    private static InverseStrategy DEFAULT_INVERSE_STRATEGY = InverseStrategy.BLOCK_SOLVE;
    private static SolveStrategy DEFAULT_SOLVE_STRATEGY = SolveStrategy.DEFAULT;

    private Complex[][] elements;

    //    public static void main(String[] args) {
//        System.out.println(new CMatrix(4, 4, CellIterator.HERMITIAN, new MatrixCell() {
//            public Complex get(int row, int column) {
//                return new Complex(row + 1, column + 1);
//            }
//        }));
//    }
    MemMatrix(int rows, int columns, CellIteratorType it, MatrixCell item) {
        elements = new Complex[rows][columns];
        switch (it) {
            case FULL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        elements[i][j] = item.get(i, j);
                    }
                }
                break;
            }
            case DIAGONAL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        elements[i][j] = Complex.ZERO;
                    }
                    elements[i][i] = item.get(i, i);
                    for (int j = i + 1; j < columns; j++) {
                        elements[i][j] = Complex.ZERO;
                    }
                }
                break;
            }
            case SYMETRIC: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        elements[i][j] = elements[j][i];
                    }
                    for (int j = i; j < columns; j++) {
                        elements[i][j] = item.get(i, j);
                    }
                }
                break;
            }
            case HERMITIAN: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        elements[i][j] = elements[j][i].conj();
                    }
                    for (int j = i; j < columns; j++) {
                        elements[i][j] = item.get(i, j);
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + it);
            }
        }
    }

    MemMatrix(String matrix) {
        this(0, 0, matrix);
    }

    MemMatrix(int m, int n, String matrix) {
        elements = new Complex[m][n];
        BufferedReader r = null;
        try {
            read(r = new BufferedReader(new StringReader(matrix)));
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

    public MemMatrix(File file) throws RuntimeIOException {
        elements = new Complex[0][0];
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

    MemMatrix(DMatrix matrix) {
        double[][] d = matrix.getArray();
        if (d.length == 0 || d[0].length == 0) {
            throw new EmptyMatrixException();
        } else {
            elements = new Complex[d.length][d[0].length];
            for (int i = 0; i < elements.length; i++) {
                for (int j = 0; j < elements[0].length; j++) {
                    elements[i][j] = Complex.valueOf(d[i][j]);
                }
            }
        }
    }

    MemMatrix(MemMatrix matrix) {
        if (matrix.elements.length == 0 || matrix.elements[0].length == 0) {
            throw new EmptyMatrixException();
        } else {
            elements = new Complex[matrix.elements.length][matrix.elements[0].length];
            for (int i = 0; i < elements.length; i++) {
                System.arraycopy(matrix.elements[i], 0, elements[i], 0, elements[i].length);
            }
        }
    }

    MemMatrix(Complex[] vector) {
        this.elements = new Complex[vector.length][1];
        for (int i = 0; i < vector.length; i++) {
            this.elements[i][0] = vector[i];
        }
    }

    MemMatrix(double[] vector) {
        this.elements = new Complex[vector.length][1];
        for (int i = 0; i < vector.length; i++) {
            this.elements[i][0] = Complex.valueOf(vector[i]);
        }
    }

    MemMatrix(Complex[][] elements) {
//        if (elements.length == 0 || elements[0].length == 0) {
//            throw new EmptyMatrixException();
//        }
        this.elements = elements;
    }

    MemMatrix(double[][] doubleElements) {
        if (doubleElements.length == 0 || doubleElements[0].length == 0) {
            throw new EmptyMatrixException();
        }
        elements = new Complex[doubleElements.length][doubleElements[0].length];
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[i].length; j++) {
                elements[i][j] = Complex.valueOf(doubleElements[i][j]);
            }
        }
    }

    MemMatrix(double[][] doubleElements, int minRow, int maxRow, int minCol, int maxCol) {
        if ((maxRow - minRow) < 0 || (maxCol - minCol) < 0) {
            throw new EmptyMatrixException();
        }
        elements = new Complex[(maxRow - minRow) + 1][(maxCol - minCol) + 1];
        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minCol; j <= maxCol; j++) {
                elements[i - minRow][j - minCol] = Complex.valueOf(doubleElements[i][j]);
            }
        }
    }

    MemMatrix(Complex[][] complexElements, int minRow, int maxRow, int minCol, int maxCol) {
        if ((maxRow - minRow) < 0 || (maxCol - minCol) < 0) {
            throw new EmptyMatrixException();
        }
        elements = new Complex[(maxRow - minRow) + 1][(maxCol - minCol) + 1];
        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minCol; j <= maxCol; j++) {
                elements[i - minRow][j - minCol] = complexElements[i][j];
            }
        }
    }

    MemMatrix(String[][] complexElements) {
        if (complexElements.length == 0 || complexElements[0].length == 0) {
            throw new EmptyMatrixException();
        }
        elements = new Complex[complexElements.length][complexElements[0].length];
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[i].length; j++) {
                elements[i][j] = Complex.valueOf(complexElements[i][j]);
            }
        }
    }

    MemMatrix(MemMatrix[][] subMatrixes) {
        int rows = 0;
        int cols = 0;
        for (MemMatrix[] subMatrixe : subMatrixes) {
            int r = 0;
            int c = 0;
            for (MemMatrix aSubMatrixe : subMatrixe) {
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

        elements = new Complex[rows][cols];

        int row = 0;
        int col;
        for (MemMatrix[] subMatrixe1 : subMatrixes) {
            col = 0;
            for (MemMatrix aSubMatrixe1 : subMatrixe1) {
                set(row, col, aSubMatrixe1);
                col += aSubMatrixe1.getColumnCount();
            }
            row += subMatrixe1[0].getRowCount();
        }

//        int row = 0;
//        int col = 0;
//        for (int i = 0; i < subMatrixes.length; i++) {
//            col = 0;
//            int r = 0;
//            int c = 0;
//            for (int j = 0; j < subMatrixes[i].length; j++) {
//                r = row;
//                for (int x = 0; x < subMatrixes[i][j].elements.length; x++) {
//                    c = col;
//                    for (int y = 0; y < subMatrixes[i][j].elements[x].length; x++) {
//                        elements[r][c] = subMatrixes[i][j].elements[x][j];
//                        c++;
//                    }
//                    r++;
//                }
//            }
//            row += subMatrixes[i][0].getRowCount();
//        }
    }

    MemMatrix(Matrix[][] subMatrixes) {
        int rows = 0;
        int cols = 0;
        for (Matrix[] subMatrixe : subMatrixes) {
            int r = 0;
            int c = 0;
            for (Matrix aSubMatrixe : subMatrixe) {
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

        elements = new Complex[rows][cols];

        int row = 0;
        int col;
        for (Matrix[] subMatrixe1 : subMatrixes) {
            col = 0;
            for (Matrix aSubMatrixe1 : subMatrixe1) {
                set(row, col, aSubMatrixe1);
                col += aSubMatrixe1.getColumnCount();
            }
            row += subMatrixe1[0].getRowCount();
        }

//        int row = 0;
//        int col = 0;
//        for (int i = 0; i < subMatrixes.length; i++) {
//            col = 0;
//            int r = 0;
//            int c = 0;
//            for (int j = 0; j < subMatrixes[i].length; j++) {
//                r = row;
//                for (int x = 0; x < subMatrixes[i][j].elements.length; x++) {
//                    c = col;
//                    for (int y = 0; y < subMatrixes[i][j].elements[x].length; x++) {
//                        elements[r][c] = subMatrixes[i][j].elements[x][j];
//                        c++;
//                    }
//                    r++;
//                }
//            }
//            row += subMatrixes[i][0].getRowCount();
//        }
    }

    protected MemMatrix(int rows, int cols, MemMatrix src, int srcRow, int srcCol) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        elements = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(src.elements[i + srcRow], srcCol, elements[i], 0, cols);
        }
    }

    MemMatrix(int rows, int cols) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        elements = new Complex[rows][cols];
    }

    MemMatrix(int rows, int cols, Complex defaultValue) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        elements = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                elements[i][j] = defaultValue;
            }
        }
    }

    public static MemMatrix newZeros(MemMatrix other) {
        return newConstant(other.getRowCount(), other.getColumnCount(), Complex.ZERO);
    }

    public static MemMatrix newConstant(int dim, Complex value) {
        return newConstant(dim, dim, value);
    }

    public static MemMatrix newOnes(int dim) {
        return newConstant(dim, dim, Complex.ONE);
    }

    public static MemMatrix newOnes(int rows, int cols) {
        return newConstant(rows, cols, Complex.ONE);
    }

    public static MemMatrix newConstant(int rows, int cols, Complex value) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        Complex[][] e = new Complex[rows][cols];
        for (int i = 0; i < e.length; i++) {
            for (int j = 0; j < e[i].length; j++) {
                e[i][j] = value;
            }
        }
        return new MemMatrix(e);
    }

    public static MemMatrix newZeros(int dim) {
        return newConstant(dim, dim, Complex.ZERO);
    }

    public static MemMatrix newZeros(int rows, int cols) {
        return newConstant(rows, cols, Complex.ZERO);
    }

    public static MemMatrix newIdentity(MemMatrix c) {
        return newIdentity(c.getRowCount(), c.getColumnCount());
    }

    public static MemMatrix newNaN(int dim) {
        return newNaN(dim, dim);
    }

    public static MemMatrix newNaN(int rows, int cols) {
        return newConstant(rows, cols, Complex.NaN);
    }

    public static MemMatrix newIdentity(int dim) {
        return newIdentity(dim, dim);
    }

    public static MemMatrix newIdentity(int rows, int cols) {
        if (rows == 0 || cols == 0) {
            throw new EmptyMatrixException();
        }
        Complex[][] e = new Complex[rows][cols];
        for (int i = 0; i < e.length; i++) {
            for (int j = 0; j < e[i].length; j++) {
                e[i][j] = (i == j) ? Complex.ONE : Complex.ZERO;
            }
        }
        return new MemMatrix(e);
    }

    public static MemMatrix newMatrix(String string) {
        return new MemMatrix(string);
    }

    public static MemMatrix newMatrix(Complex[][] complex) {
        return new MemMatrix(complex);
    }

    public static MemMatrix newMatrix(double[][] complex) {
        return new MemMatrix(complex);
    }

    public static MemMatrix newMatrix(int rows, int cols, MatrixCell cellFactory) {
        return new MemMatrix(rows, cols, CellIteratorType.FULL, cellFactory);
    }

    public static MemMatrix newColumnVector(final Complex... values) {
        Complex[][] d = new Complex[values.length][1];
        for (int i = 0; i < d.length; i++) {
            d[i][0] = values[i];
        }
        return new MemMatrix(d);
    }

    public static MemMatrix newRowVector(final Complex... values) {
        return new MemMatrix(new Complex[][]{values});
    }

    public static MemMatrix newColumnVector(int rows, final VectorCell cellFactory) {
        return new MemMatrix(rows, 1, CellIteratorType.FULL, new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    public static MemMatrix newRowVector(int columns, final VectorCell cellFactory) {
        return new MemMatrix(1, columns, CellIteratorType.FULL, new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(column);
            }
        });
    }

    public static MemMatrix newSymmetric(int rows, int cols, MatrixCell cellFactory) {
        return new MemMatrix(rows, cols, CellIteratorType.SYMETRIC, cellFactory);
    }

    public static MemMatrix newHermitian(int rows, int cols, MatrixCell cellFactory) {
        return new MemMatrix(rows, cols, CellIteratorType.HERMITIAN, cellFactory);
    }

    public static MemMatrix newDiagonal(int rows, int cols, MatrixCell cellFactory) {
        return new MemMatrix(rows, cols, CellIteratorType.DIAGONAL, cellFactory);
    }

    public static MemMatrix newDiagonal(int rows, final VectorCell cellFactory) {
        return new MemMatrix(rows, rows, CellIteratorType.DIAGONAL, new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return cellFactory.get(row);
            }
        });
    }

    public static MemMatrix newDiagonal(final Complex... c) {
        return new MemMatrix(c.length, c.length, CellIteratorType.DIAGONAL, new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return c[row];
            }
        });
    }

    public static MemMatrix newMatrix(int dim, MatrixCell cellFactory) {
        return new MemMatrix(dim, dim, CellIteratorType.FULL, cellFactory);
    }

    public static MemMatrix newSymmetric(int dim, MatrixCell cellFactory) {
        return new MemMatrix(dim, dim, CellIteratorType.SYMETRIC, cellFactory);
    }

    public static MemMatrix newHermitian(int dim, MatrixCell cellFactory) {
        return new MemMatrix(dim, dim, CellIteratorType.HERMITIAN, cellFactory);
    }

    public static MemMatrix newDiagonal(int dim, MatrixCell cellFactory) {
        return new MemMatrix(dim, dim, CellIteratorType.DIAGONAL, cellFactory);
    }

    public static MemMatrix newRandomReal(int m, int n) {
        MemMatrix A = new MemMatrix(m, n);
        Complex[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Complex.valueOf(Math.random());
            }
        }
        return A;
    }

    public static MemMatrix newRandomReal(int m, int n, int min, int max) {
        MemMatrix A = new MemMatrix(m, n);
        Complex[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Complex.valueOf(((int) (Math.random() * (max - min))) + min);
            }
        }
        return A;
    }

    public static MemMatrix newRandomReal(int m, int n, double min, double max) {
        MemMatrix A = new MemMatrix(m, n);
        Complex[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Complex.valueOf(Math.random() * (max - min) + min);
            }
        }
        return A;
    }

    public static MemMatrix newRandomImag(int m, int n, double min, double max) {
        MemMatrix A = new MemMatrix(m, n);
        Complex[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Complex.I(Math.random() * (max - min) + min);
            }
        }
        return A;
    }

    public static MemMatrix newRandomImag(int m, int n, int min, int max) {
        MemMatrix A = new MemMatrix(m, n);
        Complex[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Complex.I(((int) (Math.random() * (max - min))) + min);
            }
        }
        return A;
    }

    public static MemMatrix newRandom(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        MemMatrix A = new MemMatrix(m, n);
        Complex[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Complex.valueOf(((int) (Math.random() * (maxReal - minReal))) + minReal,
                        ((int) (Math.random() * (maxImag - minImag))) + minImag);
            }
        }
        return A;
    }

    public static MemMatrix newRandom(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        MemMatrix A = new MemMatrix(m, n);
        Complex[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Complex.valueOf(Math.random() * (maxReal - minReal) + minReal, Math.random() * (maxImag - minImag) + minImag);
            }
        }
        return A;
    }

    public static MemMatrix newRandom(int m, int n, double min, double max) {
        MemMatrix A = new MemMatrix(m, n);
        Complex[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Complex.valueOf(Math.random() * (max - min) + min, Math.random() * (max - min) + min);
            }
        }
        return A;
    }

    public static MemMatrix newRandom(int m, int n, int min, int max) {
        MemMatrix A = new MemMatrix(m, n);
        Complex[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Complex.valueOf(((int) (Math.random() * (max - min))) + min,
                        ((int) (Math.random() * (max - min))) + min);
            }
        }
        return A;
    }

    public static MemMatrix newRandomImag(int m, int n) {
        MemMatrix A = new MemMatrix(m, n);
        Complex[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Complex.I(Math.random());
            }
        }
        return A;
    }

    public static MemMatrix load(File file) throws IOException {
        return new MemMatrix(file);
    }

    public static void store(MemMatrix m, String file) throws IOException {
        m.store(file == null ? (File) null : new File(IOUtils.expandPath(file)));
    }

    public static void store(MemMatrix m, File file) throws IOException {
        m.store(file);
    }

    public static MemMatrix load(String file) throws IOException {
        return new MemMatrix(new File(IOUtils.expandPath(file)));
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
                f += element[j].absSquare();
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

    @Override
    public double getError(TMatrix<Complex> baseMatrix) {
        return (this.sub(baseMatrix).norm(NormStrategy.DEFAULT) / baseMatrix.norm(NormStrategy.DEFAULT));
    }

    @Override
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

    @Override
    public DMatrix getErrorMatrix(TMatrix<Complex> baseMatrix) {
        return getErrorMatrix(baseMatrix, Double.NaN);
    }

    @Override
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
    @Override
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

    @Override
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
     * @return A(i0:i1, j0:j1)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    @Override
    public Matrix getMatrix(int i0, int i1, int j0, int j1) {
        MemMatrix X = new MemMatrix(i1 - i0 + 1, j1 - j0 + 1);
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
     * @return A(r(:), c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    @Override
    public Matrix getMatrix(int[] r, int[] c) {
        MemMatrix X = new MemMatrix(r.length, c.length);
        Complex[][] B = X.getArray();
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
     * @return A(i0:i1, c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    @Override
    public Matrix getMatrix(int r1, int r2, int[] c) {
        MemMatrix X = new MemMatrix(r2 - r1 + 1, c.length);
        Complex[][] B = X.getArray();
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
     * @return A(r(:), j0:j1)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    @Override
    public Matrix getMatrix(int[] r, int c1, int c2) {
        MemMatrix X = new MemMatrix(r.length, c2 - c1 + 1);
        Complex[][] B = X.getArray();
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
        return new MemMatrix(e);
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
        return new MemMatrix(e);
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
        return new MemMatrix(e);
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
        return new MemMatrix(e);
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
        return new MemMatrix(e);
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
        return new MemMatrix(e);
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
        return new MemMatrix(e);
    }


    @Override
    public Matrix div(TMatrix<Complex> other) {
        return mul(other.inv());
    }

    @Override
    public Matrix mul(TMatrix<Complex> other) {
        if (other instanceof MemMatrix) {
            MemMatrix mm = (MemMatrix) other;
            Complex sum;
            int a_rows = elements.length;
            int b_cols = mm.elements[0].length;
            int b_rows = mm.elements.length;
            Complex[][] newElements = new Complex[a_rows][b_cols];
            for (int i = 0; i < a_rows; i++) {
                for (int j = 0; j < b_cols; j++) {
                    sum = Complex.ZERO;
                    for (int k = 0; k < b_rows; k++) {
                        sum = sum.add(elements[i][k].mul(mm.elements[k][j]));
                    }
                    newElements[i][j] = sum;
                }
            }
            return new MemMatrix(newElements);
        } else {
            Complex sum;
            int a_rows = elements.length;
            int b_cols = other.getColumnCount();
            int b_rows = other.getRowCount();
            Complex[][] newElements = new Complex[a_rows][b_cols];
            for (int i = 0; i < a_rows; i++) {
                for (int j = 0; j < b_cols; j++) {
                    sum = Complex.ZERO;
                    for (int k = 0; k < b_rows; k++) {
                        sum = sum.add(elements[i][k].mul(other.get(k, j)));
                    }
                    newElements[i][j] = sum;
                }
            }
            return new MemMatrix(newElements);
        }
    }

    @Override
    public Matrix dotmul(TMatrix<Complex> other) {
        if (other instanceof MemMatrix) {
            MemMatrix mm = (MemMatrix) other;
            Complex[][] newElements = new Complex[elements.length][mm.elements[0].length];
            for (int i = 0; i < newElements.length; i++) {
                for (int j = 0; j < newElements[i].length; j++) {
                    newElements[i][j] = elements[i][j].mul(mm.elements[i][j]);
                }
            }
            return new MemMatrix(newElements);
        } else {
            Complex[][] newElements = new Complex[getRowCount()][getColumnCount()];
            for (int i = 0; i < newElements.length; i++) {
                for (int j = 0; j < newElements[i].length; j++) {
                    newElements[i][j] = elements[i][j].mul(other.get(i, j));
                }
            }
            return new MemMatrix(newElements);
        }
    }

    @Override
    public Matrix dotdiv(TMatrix<Complex> other) {
        if (other instanceof MemMatrix) {
            MemMatrix mm = (MemMatrix) other;
            Complex[][] newElements = new Complex[elements.length][mm.elements[0].length];
            for (int i = 0; i < newElements.length; i++) {
                for (int j = 0; j < newElements[i].length; j++) {
                    newElements[i][j] = elements[i][j].div(mm.elements[i][j]);
                }
            }
            return new MemMatrix(newElements);
        } else {
            Complex[][] newElements = new Complex[getRowCount()][getColumnCount()];
            for (int i = 0; i < newElements.length; i++) {
                for (int j = 0; j < newElements[i].length; j++) {
                    newElements[i][j] = elements[i][j].div(other.get(i, j));
                }
            }
            return new MemMatrix(newElements);
        }
    }

    @Override
    public Matrix add(TMatrix<Complex> other) {
        if (other instanceof MemMatrix) {
            MemMatrix mm = (MemMatrix) other;
            Complex[][] e = new Complex[elements.length][];
            for (int i = 0; i < elements.length; i++) {
                e[i] = new Complex[elements[i].length];
                for (int j = 0; j < elements[i].length; j++) {
                    e[i][j] = elements[i][j].add(mm.elements[i][j]);
                }
            }
            return new MemMatrix(e);
        } else {
            Complex[][] e = new Complex[elements.length][];
            for (int i = 0; i < elements.length; i++) {
                e[i] = new Complex[elements[i].length];
                for (int j = 0; j < elements[i].length; j++) {
                    e[i][j] = elements[i][j].add(other.get(i, j));
                }
            }
            return new MemMatrix(e);
        }
    }

    @Override
    public Matrix sub(TMatrix<Complex> other) {
        if (other instanceof MemMatrix) {
            MemMatrix mm = (MemMatrix) other;
            Complex[][] e = new Complex[elements.length][];
            for (int i = 0; i < elements.length; i++) {
                e[i] = new Complex[elements[i].length];
                for (int j = 0; j < elements[i].length; j++) {
                    e[i][j] = elements[i][j].sub(mm.elements[i][j]);
                }
            }
            return new MemMatrix(e);
        } else {
            Complex[][] e = new Complex[elements.length][];
            for (int i = 0; i < elements.length; i++) {
                e[i] = new Complex[elements[i].length];
                for (int j = 0; j < elements[i].length; j++) {
                    e[i][j] = elements[i][j].sub(other.get(i, j));
                }
            }
            return new MemMatrix(e);
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
        if (src instanceof MemMatrix) {
            MemMatrix mm = (MemMatrix) src;
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
    public Matrix subMatrix(int row, int col, int rows, int cols) {
        return new MemMatrix(rows, cols, this, row, col);
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

    @Override
    public Matrix transpose() {
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
        return new MemMatrix(e);
    }

    /**
     * @return equivalent to transposeHermitian
     */
    @Override
    public Matrix transjugate() {
        return transposeHermitian();
    }

    /**
     * @return equivalent to transposeHermitian
     */
    @Override
    public Matrix transposeConjugate() {
        return transposeHermitian();
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
        return new MemMatrix(e);
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
//            for (int j = 0; j < i; j++) {
//                e[j][i] = elements[i][j];
//            }
//            for (int j = i+1; j < c; j++) {
//                e[j][i] = elements[i][j];
//            }
        }
        return new MemMatrix(e);
    }

    @Override
    public Matrix invCond() {
        return inv(DEFAULT_INVERSE_STRATEGY, ConditioningStrategy.DEFAULT, NormStrategy.DEFAULT);
    }

    @Override
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

    @Override
    public Matrix inv() {
        return inv(DEFAULT_INVERSE_STRATEGY);
    }

    @Override
    public Matrix inv(InverseStrategy st) {
        switch (st) {
            case DEFAULT: {
                return inv(DEFAULT_INVERSE_STRATEGY);
            }
            case BLOCK_SOLVE: {
                return invBlock(InverseStrategy.SOLVE, BLOCK_PRECISION);
            }
            case BLOCK_ADJOINT: {
                return invBlock(InverseStrategy.ADJOINT, BLOCK_PRECISION);
            }
            case BLOCK_GAUSS: {
                return invBlock(InverseStrategy.GAUSS, BLOCK_PRECISION);
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
            case BLOCK_OJALGO: {
                return invBlock(InverseStrategy.OJALGO, 64);
            }
            case OJALGO: {
                return OjalgoHelper.INSTANCE.inv(this);
            }
        }
        throw new UnsupportedOperationException("strategy " + st.toString());
    }

    @Override
    public Matrix invSolve() {
        return solve(newIdentity(elements.length));
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
                return new MemMatrix(new Complex[][]{{elements[0][0].inv()}});
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
                return new MemMatrix(new Complex[][]{
                        {Ai.sub(AiBmDCABi.mul(CAi)), AiBmDCABi},
                        {mDCABi.mul(CAi), DCABi}
                });
            }
            default: {
                int n2 = n / 2;
                Matrix A = getMatrix(0, n2 - 1, 0, n2 - 1);
                Matrix B = getMatrix(n2, n - 1, 0, n2);
                Matrix C = getMatrix(0, n2 - 1, n2, n - 1);
                Matrix D = getMatrix(n2, n - 1, n2, n - 1);
                Matrix Ai = A.invBlock(delegate, precision);
                Matrix CAi = C.mul(Ai);
                Matrix AiB = Ai.mul(B);
                Matrix DCABi = D.sub(C.mul(Ai).mul(B)).invBlock(delegate, precision);
                Matrix mDCABi = DCABi.mul(-1);
                Matrix AiBmDCABi = AiB.mul(mDCABi);
                return new MemMatrix(new Matrix[][]{
                        {Ai.sub(AiBmDCABi.mul(CAi)), AiBmDCABi},
                        {mDCABi.mul(CAi), DCABi}
                });
            }
            //case 2?
        }
    }

    @Override
    public MemMatrix invGauss() {
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
            return new MemMatrix(B);
        }
    }

    /**
     * Formula used to Calculate Inverse: inv(A) = 1/det(A) * adj(A)
     *
     * @return inverse
     */
    @Override
    public MemMatrix invAdjoint() {
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
            return new MemMatrix(new Complex[][]{{dd}});
        }
        Complex[][] mm = adjoint().elements;
        for (int i = 0; i < tms; i++) {
            for (int j = 0; j < tms; j++) {
                m[i][j] = dd.mul(mm[i][j]);
            }
        }

        return new MemMatrix(m);
    }

    @Override
    public MemMatrix coMatrix(int row, int col) {
        int tms = elements.length;
        Complex ap[][] = new Complex[tms - 1][tms - 1];
//                for (ii = 0; ii < tms; ii++) {
//                    for (jj = 0; jj < tms; jj++) {
//                        if ((ii != i) && (jj != j)) {
//                            ap[ia][ja] = elements[ii][jj];
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
        return new MemMatrix(ap);
    }

    @Override
    public Matrix pow(int exp) {
        switch (exp) {
            case 0: {
                return newIdentity(this);
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

    @Override
    public MemMatrix adjoint() {
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
        return new MemMatrix(m);
    }

    @Override
    public Complex det() {
        int tms = elements.length;

        Complex det = Complex.ONE;
        Matrix mt = upperTriangle();
        if (mt instanceof MemMatrix) {
            Complex[][] matrix = ((MemMatrix) mt).elements;

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
        MemMatrix o = new MemMatrix(this);

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
    public Matrix solve(TMatrix<Complex> B) {
        return solve(B, DEFAULT_SOLVE_STRATEGY);
    }

    @Override
    public Matrix solve(TMatrix<Complex> B, SolveStrategy solveStrategy) {
        switch (solveStrategy) {
            case DEFAULT: {
                if (getRowCount() == getColumnCount()) {
                    return new CLUDecomposition(this).solve(castToMatrix(B));
                }
                throw new IllegalArgumentException("Not a square matrix");
            }
            case OJALGO: {
                return OjalgoHelper.INSTANCE.solve(this, castToMatrix(B));
            }
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
    @Override
    public void store(String file) {
        store(new File(IOUtils.expandPath(file)));
    }

    @Override
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
        }catch(IOException ex){
            throw new RuntimeIOException(ex);
        }
    }

    @Override
    public void store(PrintStream stream) {
        store(stream, null, null);
    }


    @Override
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
        }catch(IOException ex){
            throw new RuntimeIOException(ex);
        }
    }

    @Override
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
        }catch(IOException ex){
            throw new RuntimeIOException(ex);
        }
    }


    @Override
    public void store(PrintStream stream, String commentsChar, String varName) {
        int[] colsWidth = new int[getColumnCount()];
        for (Complex[] element : elements) {
            for (int j = 0; j < element.length; j++) {
                int len = String.valueOf(element[j]).length();
                if (len > colsWidth[j]) {
                    colsWidth[j] = len;
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

        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                stream.println();
            }
            for (int j = 0; j < elements[i].length; j++) {
                StringBuilder sbl = new StringBuilder(colsWidth[j]);
                //sbl.clear();
                if (j > 0) {
                    sbl.append(' ');
                }
                String disp = String.valueOf(elements[i][j]);
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


    @Override
    public void read(BufferedReader reader) {
        ArrayList<ArrayList<Complex>> l = new ArrayList<ArrayList<Complex>>(elements.length > 0 ? elements.length : 10);
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
        Complex[][] e;
        if (elements.length == l.size() && elements[0].length == cols) {
            e = elements;
        } else {
            e = new Complex[l.size()][cols];
        }
        for (int i = 0; i < e.length; i++) {
            for (int j = 0; j < e[i].length; j++) {
                e[i][j] = l.get(i).get(j);
            }
        }
        elements = e;
    }

//    public Vector getRow(int row) {
//        Complex[] r = elements[row];
//        Complex[] c = new Complex[r.length];
//        System.arraycopy(r, 0, c, 0, c.length);
//        return new Vector(c, true);
//    }
//
//    public Vector getColumn(int column) {
//        Complex[] c = new Complex[elements.length];
//        for (int i = 0; i < c.length; i++) {
//            c[i] = elements[i][column];
//        }
//        return new Vector(c, false);
//    }

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
        return new MemMatrix(d);
    }

    @Override
    public double cond() {
        return this.norm1() * this.inv().norm1();
    }

    @Override
    public double cond2() {
        return this.norm2() * this.inv().norm2();
    }

    @Override
    public double condHadamard() {
        Complex det = det();
        double x = 1;
        double alpha;
        for (Complex[] element : elements) {
            alpha = 0;
            for (Complex complex : element) {
                alpha = alpha + complex.absSquare();
            }
            x *= Math.sqrt(alpha);
        }
        return det.absdbl() / x;
    }

    @Override
    public MemMatrix sparsify(double ceil) {
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
        return new MemMatrix(array);
    }

    @Override
    public Complex complexValue() {
        return elements.length == 0 ? Complex.NaN : elements[0][0];
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
            return newConstant(1, get(0, 0).pow(power));
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
        if (rows != getColumnCount() || rows != getRowCount()) {
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

//        int row = 0;
//        int col = 0;
//        for (int i = 0; i < subMatrixes.length; i++) {
//            col = 0;
//            int r = 0;
//            int c = 0;
//            for (int j = 0; j < subMatrixes[i].length; j++) {
//                r = row;
//                for (int x = 0; x < subMatrixes[i][j].elements.length; x++) {
//                    c = col;
//                    for (int y = 0; y < subMatrixes[i][j].elements[x].length; x++) {
//                        elements[r][c] = subMatrixes[i][j].elements[x][j];
//                        c++;
//                    }
//                    r++;
//                }
//            }
//            row += subMatrixes[i][0].getRowCount();
//        }
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

    @Override
    protected MatrixFactory createDefaultFactory() {
        return MemMatrixFactory.INSTANCE;
    }
}
