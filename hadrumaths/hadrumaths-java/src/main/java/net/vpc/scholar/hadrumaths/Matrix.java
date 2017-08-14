package net.vpc.scholar.hadrumaths;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public abstract class Matrix extends Number implements Normalizable, TMatrix<Complex> {

    private static final long serialVersionUID = -1010101010101001044L;


    //    public abstract double norm(NormStrategy ns);
//
//    /**
//     * One norm
//     *
//     * @return maximum column sum.
//     */
//    public abstract double norm1();
//
//    public abstract double norm2();
//
//    /**
//     * One norm
//     *
//     * @return maximum elemet absdbl.
//     */
//    public abstract double norm3();
//
//    public abstract double getError(Matrix baseMatrix);
//
    public abstract DMatrix getErrorMatrix(TMatrix<Complex> baseMatrix, double minErrorForZero, ErrorMatrixStrategy strategy);

    public abstract DMatrix getErrorMatrix(TMatrix<Complex> baseMatrix);

    public abstract DMatrix getErrorMatrix(TMatrix<Complex> baseMatrix, double minErrorForZero);

    /**
     * term by term
     *
     * @param baseMatrix
     * @param minErrorForZero
     * @return
     */
    public abstract DMatrix getErrorMatrix2(TMatrix<Complex> baseMatrix, double minErrorForZero);

    public abstract DMatrix getErrorMatrix3(TMatrix<Complex> baseMatrix, double minErrorForZero);
//
//    /**
//     * Infinity norm
//     *
//     * @return maximum row sum.
//     */
//    public abstract double normInf();
//
//    public abstract Complex avg();
//
//    public abstract Complex sum();
//
//    public abstract Complex prod();
//
//    public abstract void set(Matrix[][] subMatrixes);
//
//    public abstract void setAll(Complex value);
//
//    public abstract Complex[][] getArray();
//

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
    public abstract Matrix getMatrix(int i0, int i1, int j0, int j1);

    /**
     * Get a submatrix.
     *
     * @param r Array of row indices.
     * @param c Array of column indices.
     * @return A(r(:), c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public abstract Matrix getMatrix(int[] r, int[] c);

    /**
     * Get a submatrix.
     *
     * @param r1 Initial row index
     * @param r2 Final row index
     * @param c  Array of column indices.
     * @return A(i0:r2, c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public abstract Matrix getMatrix(int r1, int r2, int[] c);

    /**
     * Get a submatrix.
     *
     * @param r  Array of row indices.
     * @param c1 Initial column index
     * @param c2 Final column index
     * @return A(r(:), c1:c2)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public abstract Matrix getMatrix(int[] r, int c1, int c2);

    //
    public abstract Matrix div(double c);

    //
    public abstract Matrix div(Complex c);

    //
    public abstract Matrix mul(Complex c);

    //
    public abstract Matrix mul(double c);

    //
    public abstract Matrix neg();

    //
    public abstract Matrix add(Complex c);

    //
    public abstract Matrix sub(Complex c);

    //
    public abstract Matrix div(TMatrix<Complex> other);

    //
    public abstract Matrix mul(TMatrix<Complex> other);

    //
    public abstract Matrix dotmul(TMatrix<Complex> other);

    //
    public abstract Matrix dotdiv(TMatrix<Complex> other);

    //
    public abstract Matrix conj();

    //
    public abstract Matrix dotinv();

    //
    public abstract Matrix add(TMatrix<Complex> other);

    //
    public abstract Matrix add(Complex[][] other);

    public abstract Matrix sub(Complex[][] other);

    public abstract Matrix mul(Complex[][] other);

    public abstract Matrix sub(TMatrix<Complex> other);

    public abstract Complex getScalar();

    public abstract Complex get(int row, int col);

    public abstract Complex apply(int row, int col);

    public abstract Complex get(int vectorIndex);

    public abstract Complex apply(int vectorIndex);

    //    public abstract void add(int row, int col, Complex val);
//
//    public abstract void mul(int row, int col, Complex val);
//
//    public abstract void div(int row, int col, Complex val);
//
//    public abstract void sub(int row, int col, Complex val);
//
//    public abstract void set(int row, int col, Complex val);
//
//    public abstract void update(int row, int col, Complex val);
//
    public abstract void set(int row, int col, TMatrix<Complex> src, int srcRow, int srcCol, int rows, int cols);

    public abstract void set(int row, int col, TMatrix<Complex> subMatrix);

    public abstract void update(int row, int col, TMatrix<Complex> subMatrix);

    public abstract Matrix subMatrix(int row, int col, int rows, int cols);

    //    public abstract TVector<Vector> getRows();
//
//    public abstract TVector<Vector> getColumns();
//
//    public abstract List<TVector<Complex>> rows();
//    //
//    public abstract List<TVector<Complex>> columns();
//    //
//
//    public abstract int getRowCount();
//
//    public abstract ComponentDimension getComponentDimension();
//
//    public abstract int getColumnCount();
//
    public abstract Matrix transpose();

    //
//    /**
//     * @return equivalent to transposeHermitian
//     */
    public abstract Matrix transjugate();

    //
//    /**
//     * @return equivalent to transposeHermitian
//     */
    public abstract Matrix transposeConjugate();

    //
//    /**
//     * Hermitian conjugate
//     *
//     * @return
//     */
    public abstract Matrix transposeHermitian();

    //
    public abstract Matrix arrayTranspose();

    //
    public abstract Matrix invCond();

    //
    public abstract Matrix inv(InverseStrategy invStr, ConditioningStrategy condStr, NormStrategy normStr);

    public abstract Matrix inv();

    public abstract Matrix inv(InverseStrategy st);

    public abstract Matrix invSolve();

    /**
     * inversion using Bloc Matrix expansion
     *
     * @return inverse
     */
    public abstract Matrix invBlock(InverseStrategy delegate, int precision);

    public abstract Matrix invGauss();

    /**
     * Formula used to Calculate Inverse: inv(A) = 1/det(A) * adj(A)
     *
     * @return inverse
     */
    public abstract Matrix invAdjoint();

    public abstract Matrix coMatrix(int row, int col);

    public abstract Matrix pow(int exp);

    public abstract Matrix adjoint();

    public abstract Complex det();

    public abstract Matrix upperTriangle();

    //
//    @Override
//    public abstract String toString();
//
//    public abstract String format();
//
//    public abstract String format(String commentsChar, String varName);
//
    public abstract Matrix copy();

    public abstract Matrix solve(TMatrix<Complex> B);

    public abstract Matrix solve(TMatrix<Complex> B, SolveStrategy solveStrategy);

    //
//
//    public abstract void store(String file) throws IOException;
//
//    public abstract void store(File file) throws IOException;
//
//    public abstract void store(PrintStream stream) throws IOException;
//
//    public abstract void store(String file, String commentsChar, String varName) throws IOException;
//
//    public abstract void store(File file, String commentsChar, String varName) throws IOException;
//
//
//    public abstract void store(PrintStream stream, String commentsChar, String varName) throws IOException;
//
//    public abstract void read(BufferedReader reader) throws IOException;
//
//    public abstract void read(File file) throws IOException;
//
//    public abstract void read(String reader);
//
    public abstract Vector getRow(int row);

    public abstract Vector row(int row);

    public abstract Vector getColumn(int column);

    public abstract Vector column(int column);

    //
//    public abstract boolean isColumn();
//
//    public abstract boolean isRow();
//
    public abstract double[][] absdbls();

    //
    public abstract Matrix abs();

    //
//    public abstract double cond();
//
//    public abstract double cond2();
//
//    public abstract double condHadamard();
//
    public abstract Matrix sparsify(double ceil);

    //
//    public abstract Complex complexValue();
//
//    @Override
//    public abstract double doubleValue();
//
//    @Override
//    public abstract float floatValue();
//
//    @Override
//    public abstract int intValue();
//
//    @Override
//    public abstract long longValue();
//
//    public abstract double maxAbs();
//
//    public abstract double minAbs();
//
    public abstract Matrix pow(TMatrix<Complex> power);

    //
    public abstract Matrix pow(Complex power);

    //
//    public abstract boolean isScalar();
//
//    public abstract boolean isComplex();
//
//    public abstract boolean isDouble();
//
//    public abstract Complex toComplex();
//
//    public abstract double toDouble();
//
//    public abstract void set(Matrix other);
//
//    public abstract boolean isZero();
//
//    public abstract void dispose();
//
//    public abstract void resize(int rows, int columns);
//
//    public abstract MatrixFactory getFactory();
//
//    public abstract void setFactory(MatrixFactory factory);
//
//
    public abstract Vector toVector();

    //
//    public abstract Complex scalarProduct(Matrix m);
//
//    public abstract Complex scalarProduct(Vector v);
//
    public abstract Matrix cos();

    public abstract Matrix acos();

    public abstract Matrix asin();

    public abstract Matrix sin();

    public abstract Matrix acosh();

    public abstract Matrix cosh();

    public abstract Matrix asinh();

    public abstract Matrix sinh();

    public abstract Matrix arg();

    public abstract Matrix atan();

    public abstract Matrix cotan();

    public abstract Matrix tan();

    public abstract Matrix tanh();

    public abstract Matrix cotanh();

//    public abstract Matrix dotabs();

    public abstract Matrix getImag();

    public abstract Matrix getReal();

    public abstract DMatrix getRealDMatrix();

    public abstract DMatrix getImagDMatrix();

    public abstract DMatrix getAbsDMatrix();

    public abstract DMatrix getAbsSquareDMatrix();

    //
    public abstract Matrix imag();

    public abstract Matrix real();

    //
//    public abstract double[][] getDoubleArray();
//
//    public abstract double[][] getRealArray();
//
//    public abstract double[][] getImagArray();
//
//    public abstract double[][] getAbsArray();
//
//    public abstract double[][] getAbsSquareArray();
//
    public abstract Matrix acotan();

    public abstract Matrix exp();

    public abstract Matrix log();

    public abstract Matrix log10();

    public abstract Matrix db();

    public abstract Matrix db2();

    public abstract Matrix dotsqr();

    public abstract Matrix dotsqrt();

    public abstract Matrix dotsqrt(int n);

    public abstract Matrix dotnpow(int n);

    public abstract Matrix dotpow(double n);

    public abstract Matrix dotpow(Complex n);

    public abstract void set(int row, int col, double real, double imag);

    public abstract void set(Complex[][] values);

    public abstract void set(int row,int col, MutableComplex value);

    public abstract void set(MutableComplex[][] values);

    public abstract void set(double[][] values);

    public abstract void set(TMatrix<Complex> values);

    public abstract boolean isHermitian();

    public abstract boolean isSymmetric();

    public abstract boolean isSquare();
}
