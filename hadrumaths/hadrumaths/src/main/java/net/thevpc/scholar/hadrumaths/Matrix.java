package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

import java.io.*;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public interface Matrix<T> extends Normalizable, Serializable, Iterable<Vector<T>> {

    DMatrix getErrorMatrix(Matrix<T> baseMatrix, double minErrorForZero, ErrorMatrixStrategy strategy);

    DMatrix getErrorMatrix(Matrix<T> baseMatrix);

    DMatrix getErrorMatrix(Matrix<T> baseMatrix, double minErrorForZero);

    double norm(NormStrategy ns);

    /**
     * One norm
     *
     * @return maximum column sum.
     */
    double norm1();

    /**
     * Frobenius norm
     *
     * @return
     */
    double norm2();

    /**
     * One norm
     *
     * @return maximum elemet absdbl.
     */
    double norm3();

    double getError(Matrix<T> baseMatrix);

    /**
     * term by term
     *
     * @param baseMatrix
     * @param minErrorForZero
     * @return
     */
    DMatrix getErrorMatrix2(Matrix<T> baseMatrix, double minErrorForZero);

    DMatrix getErrorMatrix3(Matrix<T> baseMatrix, double minErrorForZero);

    /**
     * Infinity norm
     *
     * @return maximum row sum.
     */
    double normInf();

    T avg();

    T sum();

    T prod();

    void set(Matrix<T>[][] subMatrixes);

    void set(T[][] elements);

    void setAll(T value);

    T[][] getArray();

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
    Matrix<T> getMatrix(int i0, int i1, int j0, int j1);

    /**
     * Get a submatrix.
     *
     * @param r Array of row indices.
     * @param c Array of column indices.
     * @return A(r ( :), c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    Matrix<T> getMatrix(int[] r, int[] c);

    /**
     * Get a submatrix.
     *
     * @param r1 Initial row index
     * @param r2 Final row index
     * @param c  Array of column indices.
     * @return A(i0 : r2, c ( :))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    Matrix<T> getMatrix(int r1, int r2, int[] c);

    /**
     * Get a submatrix.
     *
     * @param r  Array of row indices.
     * @param c1 Initial column index
     * @param c2 Final column index
     * @return A(r ( :), c1:c2)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    Matrix<T> getMatrix(int[] r, int c1, int c2);

    Matrix<T> div(double c);

    Matrix<T> div(T c);

    Matrix<T> mul(T c);

    Matrix<T> mul(double c);

    Matrix<T> neg();

    Matrix<T> add(T c);

    Matrix<T> sub(T c);

    Matrix<T> div(Matrix<T> other);

    Matrix<T> rem(Matrix<T> other);

    Matrix<T> mul(Matrix<T> other);

//    Matrix<T> multiply(Matrix<T> other);

    Matrix<T> dotmul(Matrix<T> other);

    Matrix<T> dotdiv(Matrix<T> other);

    Matrix<T> conj();

    Matrix<T> dotinv();

    Matrix<T> add(Matrix<T> other);

    Matrix<T> add(T[][] other);

    Matrix<T> sub(T[][] other);

    Matrix<T> mul(T[][] other);

    Matrix<T> sub(Matrix<T> other);

    T getScalar();

    T get(int row, int col);

    T apply(int row, int col);

    T get(int vectorIndex);

    T get(Enum anyEnum);

    T apply(int vectorIndex);

    T apply(Enum anyEnum);

    void add(int row, int col, T val);

    void mul(int row, int col, T val);

    void div(int row, int col, T val);

    void sub(int row, int col, T val);

    void set(int row, int col, T val);

    void update(int row, int col, T val);

    void set(int row, int col, Matrix<T> src, int srcRow, int srcCol, int rows, int cols);

    void set(int row, int col, Matrix<T> subMatrix);

    void update(int row, int col, Matrix<T> subMatrix);

    Matrix<T> subMatrix(int row, int col, int rows, int cols);

    Vector<Vector<T>> getRows();

    Vector<Vector<T>> getColumns();

    int getRowCount();

    ComponentDimension getComponentDimension();

    int getColumnCount();

    Matrix<T> transpose();

    /**
     * @return equivalent to transposeHermitian
     */
    Matrix<T> transjugate();

    /**
     * @return equivalent to transposeHermitian
     */
    Matrix<T> transposeConjugate();

    /**
     * Hermitian conjugate
     *
     * @return
     */
    Matrix<T> transposeHermitian();

    Matrix<T> arrayTranspose();

    Matrix<T> invCond();

    Matrix<T> inv(InverseStrategy invStr, ConditioningStrategy condStr, NormStrategy normStr);

    Matrix<T> inv();

    Matrix<T> inv(InverseStrategy st);

    Matrix<T> invSolve();

    /**
     * inversion using Bloc Matrix expansion
     *
     * @return inverse
     */
    Matrix<T> invBlock(InverseStrategy delegate, int precision);

    Matrix<T> invGauss();

    /**
     * Formula used to Calculate Inverse: inv(A) = 1/det(A) * adj(A)
     *
     * @return inverse
     */
    Matrix<T> invAdjoint();

    Matrix<T> coMatrix(int row, int col);

    Matrix<T> pow(double exp);

    Matrix<T> adjoint();

    T det();

    Matrix<T> upperTriangle();

    @Override
    String toString();

    String format();

    String format(String commentsChar, String varName);

    T[][] getArrayCopy();

    Matrix<T> solve(Matrix<T> B);

    Matrix<T> solve(Matrix<T> B, SolveStrategy solveStrategy);


    void store(String file) throws UncheckedIOException;

    void store(File file) throws UncheckedIOException;

    void store(PrintStream stream) throws UncheckedIOException;

    void store(String file, String commentsChar, String varName) throws UncheckedIOException;

    void store(File file, String commentsChar, String varName) throws UncheckedIOException;


    void store(PrintStream stream, String commentsChar, String varName) throws UncheckedIOException;

    void read(BufferedReader reader) throws UncheckedIOException;

    void read(File file) throws UncheckedIOException;

    void read(String reader);

    Vector<T> getRow(int row);

    Vector<T> row(int row);

    Vector<Vector<T>> rows();

    Vector<Vector<T>> columns();

    Vector<T> getColumn(int column);

    Vector<T> column(int column);

    boolean isColumn();

    boolean isRow();

    double[][] absdbls();

    Matrix<T> abs();

    Matrix<T> abssqr();

    Matrix<T> sqr();

    Matrix<T> sqrt();

    Matrix<T> sqrt(int n);

    double cond();

    double cond2();

    double condHadamard();

    Matrix<T> sparsify(double ceil);

    Complex complexValue();

    double doubleValue();

    float floatValue();

    int intValue();

    long longValue();

    double maxAbs();

    double minAbs();

    Matrix<T> pow(Matrix<T> power);

    Matrix<T> pow(T power);

    boolean isScalar();

    boolean isComplex();

    boolean isDouble();

    Complex toComplex();

    double toDouble();

    void set(Matrix<T> other);

    boolean isZero();

    void dispose();

    void resize(int rows, int columns);

    String getFactoryId();

    MatrixFactory<T> getFactory();

    void setFactory(MatrixFactory<T> factory);


    Vector<T> toVector();

    T scalarProduct(Matrix<T> m);

    T scalarProduct(Vector<T> v);

    Matrix<T> cos();

    Matrix<T> acos();

    Matrix<T> asin();

    Matrix<T> sin();

    Matrix<T> acosh();

    Matrix<T> cosh();

    Matrix<T> asinh();

    Matrix<T> sinh();

    Matrix<T> sincard();

    Matrix<T> arg();

    Matrix<T> atan();

    Matrix<T> cotan();

    Matrix<T> tan();

    Matrix<T> tanh();

    Matrix<T> cotanh();

//     Matrix dotabs();

    Matrix<T> getImag();

    Matrix<T> getReal();

//    DMatrix getRealDMatrix();

//    DMatrix getImagDMatrix();

//    DMatrix getAbsDMatrix();

//    DMatrix getAbsSquareDMatrix();

    Matrix<T> imag();

    Matrix<T> real();

//    double[][] getDoubleArray();
//
//    double[][] getRealArray();
//
//    double[][] getImagArray();
//
//    double[][] getAbsArray();
//
//    double[][] getAbsSquareArray();

    Matrix<T> acotan();

    Matrix<T> exp();

    Matrix<T> log();

    Matrix<T> log10();

    Matrix<T> db();

    Matrix<T> db2();

    Matrix<T> dotsqr();

    Matrix<T> dotsqrt();

    Matrix<T> dotsqrt(int n);

    Matrix<T> dotnpow(int n);

    Matrix<T> dotpow(double n);

    Matrix<T> dotpow(T n);

    TypeName getComponentType();

    VectorSpace<T> getComponentVectorSpace();

    Matrix<T> copy();

    <R> boolean isConvertibleTo(TypeName<R> type);

    <R> Matrix<R> to(TypeName<R> other);

    boolean isHermitian();

    boolean isSymmetric();

    boolean isSquare();

}
