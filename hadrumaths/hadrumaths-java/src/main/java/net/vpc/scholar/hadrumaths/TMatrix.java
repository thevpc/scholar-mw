package net.vpc.scholar.hadrumaths;

import net.vpc.common.io.RuntimeIOException;
import net.vpc.common.util.TypeReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:40:39
 */
public interface TMatrix<T> extends Normalizable, Serializable {


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

    double getError(TMatrix<T> baseMatrix);

    /**
     * term by term
     *
     * @param baseMatrix
     * @param minErrorForZero
     * @return
     */
    DMatrix getErrorMatrix2(TMatrix<T> baseMatrix, double minErrorForZero);

    DMatrix getErrorMatrix3(TMatrix<T> baseMatrix, double minErrorForZero);

    /**
     * Infinity norm
     *
     * @return maximum row sum.
     */
    double normInf();

    T avg();

    T sum();

    T prod();

    void set(TMatrix<T>[][] subMatrixes);

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
    TMatrix<T> getMatrix(int i0, int i1, int j0, int j1);

    /**
     * Get a submatrix.
     *
     * @param r Array of row indices.
     * @param c Array of column indices.
     * @return A(r ( :), c(:))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    TMatrix<T> getMatrix(int[] r, int[] c);

    /**
     * Get a submatrix.
     *
     * @param r1 Initial row index
     * @param r2 Final row index
     * @param c  Array of column indices.
     * @return A(i0 : r2, c ( :))
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    TMatrix<T> getMatrix(int r1, int r2, int[] c);

    /**
     * Get a submatrix.
     *
     * @param r  Array of row indices.
     * @param c1 Initial column index
     * @param c2 Final column index
     * @return A(r ( :), c1:c2)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    TMatrix<T> getMatrix(int[] r, int c1, int c2);

    TMatrix<T> div(double c);

    TMatrix<T> div(T c);

    TMatrix<T> mul(T c);

    TMatrix<T> mul(double c);

    TMatrix<T> neg();

    TMatrix<T> add(T c);

    TMatrix<T> sub(T c);

    TMatrix<T> div(TMatrix<T> other);

    TMatrix<T> mul(TMatrix<T> other);

//    TMatrix<T> multiply(TMatrix<T> other);

    TMatrix<T> dotmul(TMatrix<T> other);

    TMatrix<T> dotdiv(TMatrix<T> other);

    TMatrix<T> conj();

    TMatrix<T> dotinv();

    TMatrix<T> add(TMatrix<T> other);

    TMatrix<T> add(T[][] other);

    TMatrix<T> sub(T[][] other);

    TMatrix<T> mul(T[][] other);

    TMatrix<T> sub(TMatrix<T> other);

    T getScalar();

    T get(int row, int col);

    T apply(int row, int col);

    T get(int vectorIndex);

    T apply(int vectorIndex);

    void add(int row, int col, T val);

    void mul(int row, int col, T val);

    void div(int row, int col, T val);

    void sub(int row, int col, T val);

    void set(int row, int col, T val);

    void update(int row, int col, T val);

    void set(int row, int col, TMatrix<T> src, int srcRow, int srcCol, int rows, int cols);

    void set(int row, int col, TMatrix<T> subMatrix);

    void update(int row, int col, TMatrix<T> subMatrix);

    TMatrix<T> subMatrix(int row, int col, int rows, int cols);

    TVector<TVector<T>> getRows();

    TVector<TVector<T>> getColumns();

    int getRowCount();

    ComponentDimension getComponentDimension();

    int getColumnCount();

    TMatrix<T> transpose();

    /**
     * @return equivalent to transposeHermitian
     */
    TMatrix<T> transjugate();

    /**
     * @return equivalent to transposeHermitian
     */
    TMatrix<T> transposeConjugate();

    /**
     * Hermitian conjugate
     *
     * @return
     */
    TMatrix<T> transposeHermitian();

    TMatrix<T> arrayTranspose();

    TMatrix<T> invCond();

    TMatrix<T> inv(InverseStrategy invStr, ConditioningStrategy condStr, NormStrategy normStr);

    TMatrix<T> inv();

    TMatrix<T> inv(InverseStrategy st);

    TMatrix<T> invSolve();

    /**
     * inversion using Bloc Matrix expansion
     *
     * @return inverse
     */
    TMatrix<T> invBlock(InverseStrategy delegate, int precision);

    TMatrix<T> invGauss();

    /**
     * Formula used to Calculate Inverse: inv(A) = 1/det(A) * adj(A)
     *
     * @return inverse
     */
    TMatrix<T> invAdjoint();

    TMatrix<T> coMatrix(int row, int col);

    TMatrix<T> pow(double exp);

    TMatrix<T> adjoint();

    T det();

    TMatrix<T> upperTriangle();

    @Override
    String toString();

    String format();

    String format(String commentsChar, String varName);

    T[][] getArrayCopy();

    TMatrix<T> solve(TMatrix<T> B);

    TMatrix<T> solve(TMatrix<T> B, SolveStrategy solveStrategy);


    void store(String file) throws RuntimeIOException;

    void store(File file) throws RuntimeIOException;

    void store(PrintStream stream) throws RuntimeIOException;

    void store(String file, String commentsChar, String varName) throws RuntimeIOException;

    void store(File file, String commentsChar, String varName) throws RuntimeIOException;


    void store(PrintStream stream, String commentsChar, String varName) throws RuntimeIOException;

    void read(BufferedReader reader) throws RuntimeIOException;

    void read(File file) throws RuntimeIOException;

    void read(String reader);

    TVector<T> getRow(int row);

    TVector<T> row(int row);

    TVector<TVector<T>> rows();

    TVector<TVector<T>> columns();

    TVector<T> getColumn(int column);

    TVector<T> column(int column);

    boolean isColumn();

    boolean isRow();

    double[][] absdbls();

    TMatrix<T> abs();

    double cond();

    double cond2();

    double condHadamard();

    TMatrix<T> sparsify(double ceil);

    Complex complexValue();

    double doubleValue();

    float floatValue();

    int intValue();

    long longValue();

    double maxAbs();

    double minAbs();

    TMatrix<T> pow(TMatrix<T> power);

    TMatrix<T> pow(T power);

    boolean isScalar();

    boolean isComplex();

    boolean isDouble();

    Complex toComplex();

    double toDouble();

    void set(TMatrix<T> other);

    boolean isZero();

    void dispose();

    void resize(int rows, int columns);

    String getFactoryId();

    TMatrixFactory<T> getFactory();

    void setFactory(TMatrixFactory<T> factory);


    TVector<T> toVector();

    T scalarProduct(TMatrix<T> m);

    T scalarProduct(TVector<T> v);

    TMatrix<T> cos();

    TMatrix<T> acos();

    TMatrix<T> asin();

    TMatrix<T> sin();

    TMatrix<T> acosh();

    TMatrix<T> cosh();

    TMatrix<T> asinh();

    TMatrix<T> sinh();

    TMatrix<T> arg();

    TMatrix<T> atan();

    TMatrix<T> cotan();

    TMatrix<T> tan();

    TMatrix<T> tanh();

    TMatrix<T> cotanh();

//     Matrix dotabs();

    TMatrix<T> getImag();

    TMatrix<T> getReal();

//    DMatrix getRealDMatrix();

//    DMatrix getImagDMatrix();

//    DMatrix getAbsDMatrix();

//    DMatrix getAbsSquareDMatrix();

    TMatrix<T> imag();

    TMatrix<T> real();

//    double[][] getDoubleArray();
//
//    double[][] getRealArray();
//
//    double[][] getImagArray();
//
//    double[][] getAbsArray();
//
//    double[][] getAbsSquareArray();

    TMatrix<T> acotan();

    TMatrix<T> exp();

    TMatrix<T> log();

    TMatrix<T> log10();

    TMatrix<T> db();

    TMatrix<T> db2();

    TMatrix<T> dotsqr();

    TMatrix<T> dotsqrt();

    TMatrix<T> dotsqrt(int n);

    TMatrix<T> dotnpow(int n);

    TMatrix<T> dotpow(double n);

    TMatrix<T> dotpow(T n);

    TypeReference<T> getComponentType();

    VectorSpace<T> getComponentVectorSpace();

    TMatrix<T> copy();

    <R> boolean isConvertibleTo(TypeReference<R> type);

    <R> TMatrix<R> to(TypeReference<R> other);

    boolean isHermitian();

    boolean isSymmetric();

    boolean isSquare();

}
