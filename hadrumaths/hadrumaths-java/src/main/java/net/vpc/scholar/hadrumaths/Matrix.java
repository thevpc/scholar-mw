package net.vpc.scholar.hadrumaths;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public interface Matrix extends Normalizable, TMatrix<Complex> {

    DMatrix getErrorMatrix(TMatrix<Complex> baseMatrix, double minErrorForZero, ErrorMatrixStrategy strategy);

    DMatrix getErrorMatrix(TMatrix<Complex> baseMatrix);

    DMatrix getErrorMatrix(TMatrix<Complex> baseMatrix, double minErrorForZero);

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
    Matrix getMatrix(int i0, int i1, int j0, int j1);

    /**
     * Get a submatrix.
     *
     * @param r Array of row indices.
     * @param c Array of column indices.
     * @return A(r(:), c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    Matrix getMatrix(int[] r, int[] c);

    /**
     * Get a submatrix.
     *
     * @param r1 Initial row index
     * @param r2 Final row index
     * @param c  Array of column indices.
     * @return A(i0:r2, c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    Matrix getMatrix(int r1, int r2, int[] c);

    /**
     * Get a submatrix.
     *
     * @param r  Array of row indices.
     * @param c1 Initial column index
     * @param c2 Final column index
     * @return A(r(:), c1:c2)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    Matrix getMatrix(int[] r, int c1, int c2);

    //
    Matrix div(double c);

    //
    Matrix div(Complex c);

    //
    Matrix mul(Complex c);

    //
    Matrix mul(double c);

    //
    Matrix neg();

    //
    Matrix add(Complex c);

    //
    Matrix sub(Complex c);

    //
    Matrix div(TMatrix<Complex> other);

    //
    Matrix mul(TMatrix<Complex> other);

    //
    Matrix dotmul(TMatrix<Complex> other);

    //
    Matrix dotdiv(TMatrix<Complex> other);

    //
    Matrix conj();

    //
    Matrix dotinv();

    //
    Matrix add(TMatrix<Complex> other);

    //
    Matrix add(Complex[][] other);

    Matrix sub(Complex[][] other);

    Matrix mul(Complex[][] other);

    Matrix sub(TMatrix<Complex> other);

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

    Matrix subMatrix(int row, int col, int rows, int cols);

    //    TVector<Vector> getRows();
//
//    TVector<Vector> getColumns();
//
//    List<TVector<Complex>> rows();
//    //
//    List<TVector<Complex>> columns();
//    //
//
//    int getRowCount();
//
//    ComponentDimension getComponentDimension();
//
//    int getColumnCount();
//
    Matrix transpose();

    //
//    /**
//     * @return equivalent to transposeHermitian
//     */
    Matrix transjugate();

    //
//    /**
//     * @return equivalent to transposeHermitian
//     */
    Matrix transposeConjugate();

    //
//    /**
//     * Hermitian conjugate
//     *
//     * @return
//     */
    Matrix transposeHermitian();

    //
    Matrix arrayTranspose();

    //
    Matrix invCond();

    //
    Matrix inv(InverseStrategy invStr, ConditioningStrategy condStr, NormStrategy normStr);

    Matrix inv();

    Matrix inv(InverseStrategy st);

    Matrix invSolve();

    /**
     * inversion using Bloc Matrix expansion
     *
     * @return inverse
     */
    Matrix invBlock(InverseStrategy delegate, int precision);

    Matrix invGauss();

    /**
     * Formula used to Calculate Inverse: inv(A) = 1/det(A) * adj(A)
     *
     * @return inverse
     */
    Matrix invAdjoint();

    Matrix coMatrix(int row, int col);

    Matrix pow(int exp);

    Matrix adjoint();

    Complex det();

    Matrix upperTriangle();

    //
//    @Override
//    String toString();
//
//    String format();
//
//    String format(String commentsChar, String varName);
//
    Matrix copy();

    Matrix solve(TMatrix<Complex> B);

    Matrix solve(TMatrix<Complex> B, SolveStrategy solveStrategy);

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
    Vector getRow(int row);

    Vector row(int row);

    Vector getColumn(int column);

    Vector column(int column);

    //
//    boolean isColumn();
//
//    boolean isRow();
//
    double[][] absdbls();

    //
    Matrix abs();

    //
//    double cond();
//
//    double cond2();
//
//    double condHadamard();
//
    Matrix sparsify(double ceil);

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
    Matrix pow(TMatrix<Complex> power);

    //
    Matrix pow(Complex power);

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
    Vector toVector();

    //
//    Complex scalarProduct(Matrix m);
//
//    Complex scalarProduct(Vector v);
//
    Matrix cos();

    Matrix acos();

    Matrix asin();

    Matrix sin();

    Matrix acosh();

    Matrix cosh();

    Matrix asinh();

    Matrix sinh();

    Matrix arg();

    Matrix atan();

    Matrix cotan();

    Matrix tan();

    Matrix tanh();

    Matrix cotanh();

//    Matrix dotabs();

    Matrix getImag();

    Matrix getReal();

    DMatrix getRealDMatrix();

    DMatrix getImagDMatrix();

    DMatrix getAbsDMatrix();

    DMatrix getAbsSquareDMatrix();

    Matrix imag();

    Matrix real();

    Matrix acotan();

    Matrix exp();

    Matrix log();

    Matrix log10();

    Matrix db();

    Matrix db2();

    Matrix dotsqr();

    Matrix dotsqrt();

    Matrix dotsqrt(int n);

    Matrix dotnpow(int n);

    Matrix dotpow(double n);

    Matrix dotpow(Complex n);

    void set(int row, int col, double real, double imag);

    void set(Complex[][] values);

    void set(int row,int col, MutableComplex value);

    void set(MutableComplex[][] values);

    void set(double[][] values);

    void set(TMatrix<Complex> values);

}
