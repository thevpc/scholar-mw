package net.vpc.scholar.hadrumaths;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public interface ComplexMatrix extends Normalizable, TMatrix<Complex> {


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
    ComplexMatrix getMatrix(int i0, int i1, int j0, int j1);

    /**
     * Get a submatrix.
     *
     * @param r Array of row indices.
     * @param c Array of column indices.
     * @return A(r ( :), c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    ComplexMatrix getMatrix(int[] r, int[] c);

    /**
     * Get a submatrix.
     *
     * @param r1 Initial row index
     * @param r2 Final row index
     * @param c  Array of column indices.
     * @return A(i0 : r2, c ( :))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    ComplexMatrix getMatrix(int r1, int r2, int[] c);

    /**
     * Get a submatrix.
     *
     * @param r  Array of row indices.
     * @param c1 Initial column index
     * @param c2 Final column index
     * @return A(r ( :), c1:c2)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    ComplexMatrix getMatrix(int[] r, int c1, int c2);

    //
    ComplexMatrix div(double c);

    //
    ComplexMatrix div(Complex c);

    //
    ComplexMatrix mul(Complex c);

    //
    ComplexMatrix mul(double c);

    //
    ComplexMatrix neg();

    //
    ComplexMatrix add(Complex c);

    //
    ComplexMatrix sub(Complex c);

    //
    ComplexMatrix div(TMatrix<Complex> other);

    ComplexMatrix rem(TMatrix<Complex> other);

    //
    ComplexMatrix mul(TMatrix<Complex> other);

    //
    ComplexMatrix dotmul(TMatrix<Complex> other);

    //
    ComplexMatrix dotdiv(TMatrix<Complex> other);

    //
    ComplexMatrix conj();

    //
    ComplexMatrix dotinv();

    //
    ComplexMatrix add(TMatrix<Complex> other);

    //
    ComplexMatrix add(Complex[][] other);

    ComplexMatrix sub(Complex[][] other);

    ComplexMatrix mul(Complex[][] other);

    ComplexMatrix sub(TMatrix<Complex> other);

    Complex getScalar();

    Complex get(int row, int col);

    Complex apply(int row, int col);

    Complex get(int vectorIndex);

    Complex apply(int vectorIndex);

    //    void add(int row, int col, Complex val);
//
//    void mul(int row, int col, Complex val);
//
//    void div(int row, int col, Complex val);
//
//    void sub(int row, int col, Complex val);
//
//    void set(int row, int col, Complex val);
//
//    void update(int row, int col, Complex val);
//
    void set(int row, int col, TMatrix<Complex> src, int srcRow, int srcCol, int rows, int cols);

    void set(int row, int col, TMatrix<Complex> subMatrix);

    void update(int row, int col, TMatrix<Complex> subMatrix);

    ComplexMatrix subMatrix(int row, int col, int rows, int cols);

    ComplexMatrix transpose();

    //
//    /**
//     * @return equivalent to transposeHermitian
//     */
    ComplexMatrix transjugate();

    //
//    /**
//     * @return equivalent to transposeHermitian
//     */
    ComplexMatrix transposeConjugate();

    //
//    /**
//     * Hermitian conjugate
//     *
//     * @return
//     */
    ComplexMatrix transposeHermitian();

    //
    ComplexMatrix arrayTranspose();

    //
    ComplexMatrix invCond();

    //
    ComplexMatrix inv(InverseStrategy invStr, ConditioningStrategy condStr, NormStrategy normStr);

    ComplexMatrix inv();

    ComplexMatrix inv(InverseStrategy st);

    ComplexMatrix invSolve();

    /**
     * inversion using Bloc Matrix expansion
     *
     * @return inverse
     */
    ComplexMatrix invBlock(InverseStrategy delegate, int precision);

    ComplexMatrix invGauss();

    /**
     * Formula used to Calculate Inverse: inv(A) = 1/det(A) * adj(A)
     *
     * @return inverse
     */
    ComplexMatrix invAdjoint();

    ComplexMatrix coMatrix(int row, int col);

    ComplexMatrix pow(int exp);

    ComplexMatrix adjoint();

    Complex det();

    ComplexMatrix upperTriangle();

    //
//    @Override
//    String toString();
//
//    String format();
//
//    String format(String commentsChar, String varName);
//
    ComplexMatrix copy();

    ComplexMatrix solve(TMatrix<Complex> B);

    ComplexMatrix solve(TMatrix<Complex> B, SolveStrategy solveStrategy);

    //
//
//    void store(String file) throws IOException;
//
//    void store(File file) throws IOException;
//
//    void store(PrintStream stream) throws IOException;
//
//    void store(String file, String commentsChar, String varName) throws IOException;
//
//    void store(File file, String commentsChar, String varName) throws IOException;
//
//
//    void store(PrintStream stream, String commentsChar, String varName) throws IOException;
//
//    void read(BufferedReader reader) throws IOException;
//
//    void read(File file) throws IOException;
//
//    void read(String reader);
//
    ComplexVector getRow(int row);

    ComplexVector row(int row);

    ComplexVector getColumn(int column);

    ComplexVector column(int column);

    //
//    boolean isColumn();
//
//    boolean isRow();
//
    double[][] absdbls();

    //
    ComplexMatrix abs();

    //
//    double cond();
//
//    double cond2();
//
//    double condHadamard();
//
    ComplexMatrix sparsify(double ceil);

    //
//    Complex complexValue();
//
//    @Override
//    double doubleValue();
//
//    @Override
//    float floatValue();
//
//    @Override
//    int intValue();
//
//    @Override
//    long longValue();
//
//    double maxAbs();
//
//    double minAbs();
//
    ComplexMatrix pow(TMatrix<Complex> power);

    //
    ComplexMatrix pow(Complex power);

    //
//    boolean isScalar();
//
//    boolean isComplex();
//
//    boolean isDouble();
//
//    Complex toComplex();
//
//    double toDouble();
//
//    void set(Matrix other);
//
//    boolean isZero();
//
//    void dispose();
//
//    void resize(int rows, int columns);
//
//    MatrixFactory getFactory();
//
//    void setFactory(MatrixFactory factory);
//
//
    ComplexVector toVector();

    //
//    Complex scalarProduct(Matrix m);
//
//    Complex scalarProduct(Vector v);
//
    ComplexMatrix cos();

    ComplexMatrix acos();

    ComplexMatrix asin();

    ComplexMatrix sin();

    ComplexMatrix acosh();

    ComplexMatrix cosh();

    ComplexMatrix asinh();

    ComplexMatrix sinh();

    ComplexMatrix arg();

    ComplexMatrix atan();

    ComplexMatrix cotan();

    ComplexMatrix tan();

    ComplexMatrix tanh();

    ComplexMatrix cotanh();

//    Matrix dotabs();

    ComplexMatrix getImag();

    ComplexMatrix getReal();

    DMatrix getRealDMatrix();

    DMatrix getImagDMatrix();

    DMatrix getAbsDMatrix();

    DMatrix getAbsSquareDMatrix();

    ComplexMatrix imag();

    ComplexMatrix real();

    ComplexMatrix acotan();

    ComplexMatrix exp();

    ComplexMatrix log();

    ComplexMatrix log10();

    ComplexMatrix db();

    ComplexMatrix db2();

    ComplexMatrix dotsqr();

    ComplexMatrix dotsqrt();

    ComplexMatrix dotsqrt(int n);

    ComplexMatrix dotnpow(int n);

    ComplexMatrix dotpow(double n);

    ComplexMatrix dotpow(Complex n);

    void set(int row, int col, double real, double imag);

    void set(Complex[][] values);

    void set(int row, int col, MutableComplex value);

    void set(MutableComplex[][] values);

    void set(double[][] values);

    void set(TMatrix<Complex> values);

}
