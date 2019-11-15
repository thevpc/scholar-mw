package net.vpc.scholar.hadrumaths;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.util.*;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadruplot.PlotDoubleConverter;
import net.vpc.scholar.hadruplot.Samples;
import net.vpc.scholar.hadruplot.console.params.*;

import java.io.File;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.concurrent.Callable;

public class MathsInstanceSon {

    private final MathsInstanceBase mathsInstanceBase = new MathsInstanceBase();

    public Domain xdomain(double min, double max) {
        return mathsInstanceBase.xdomain(min, max);
    }

    public Domain ydomain(double min, double max) {
        return mathsInstanceBase.ydomain(min, max);
    }

    public DomainExpr ydomain(DomainExpr min, DomainExpr max) {
        return mathsInstanceBase.ydomain(min, max);
    }

    public Domain zdomain(double min, double max) {
        return mathsInstanceBase.zdomain(min, max);
    }

    public DomainExpr zdomain(Expr min, Expr max) {
        return mathsInstanceBase.zdomain(min, max);
    }

    public Domain domain(RightArrowUplet2.Double u) {
        return mathsInstanceBase.domain(u);
    }

    public Domain domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return mathsInstanceBase.domain(ux, uy);
    }

    public Domain domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return mathsInstanceBase.domain(ux, uy, uz);
    }

    public Expr domain(RightArrowUplet2.Expr u) {
        return mathsInstanceBase.domain(u);
    }

    public Expr domain(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy) {
        return mathsInstanceBase.domain(ux, uy);
    }

    public Expr domain(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy, RightArrowUplet2.Expr uz) {
        return mathsInstanceBase.domain(ux, uy, uz);
    }

    public DomainExpr domain(Expr min, Expr max) {
        return mathsInstanceBase.domain(min, max);
    }

    public Domain domain(double min, double max) {
        return mathsInstanceBase.domain(min, max);
    }

    public Domain domain(double xmin, double xmax, double ymin, double ymax) {
        return mathsInstanceBase.domain(xmin, xmax, ymin, ymax);
    }

    public DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return mathsInstanceBase.domain(xmin, xmax, ymin, ymax);
    }

    public Domain domain(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return mathsInstanceBase.domain(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return mathsInstanceBase.domain(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public Domain II(RightArrowUplet2.Double u) {
        return mathsInstanceBase.II(u);
    }

    public Domain II(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return mathsInstanceBase.II(ux, uy);
    }

    public Domain II(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return mathsInstanceBase.II(ux, uy, uz);
    }

    public Expr II(RightArrowUplet2.Expr u) {
        return mathsInstanceBase.II(u);
    }

    public Expr II(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy) {
        return mathsInstanceBase.II(ux, uy);
    }

    public Expr II(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy, RightArrowUplet2.Expr uz) {
        return mathsInstanceBase.II(ux, uy, uz);
    }

    public DomainExpr II(Expr min, Expr max) {
        return mathsInstanceBase.II(min, max);
    }

    public Domain II(double min, double max) {
        return mathsInstanceBase.II(min, max);
    }

    public Domain II(double xmin, double xmax, double ymin, double ymax) {
        return mathsInstanceBase.II(xmin, xmax, ymin, ymax);
    }

    public DomainExpr II(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return mathsInstanceBase.II(xmin, xmax, ymin, ymax);
    }

    public Domain II(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return mathsInstanceBase.II(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public DomainExpr II(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return mathsInstanceBase.II(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public DoubleParam param(String name) {
        return mathsInstanceBase.param(name);
    }

    public DoubleArrayParamSet doubleParamSet(Param param) {
        return mathsInstanceBase.doubleParamSet(param);
    }

    public DoubleArrayParamSet paramSet(Param param, double[] values) {
        return mathsInstanceBase.paramSet(param, values);
    }

    public FloatArrayParamSet paramSet(Param param, float[] values) {
        return mathsInstanceBase.paramSet(param, values);
    }

    public FloatArrayParamSet floatParamSet(Param param) {
        return mathsInstanceBase.floatParamSet(param);
    }

    public LongArrayParamSet paramSet(Param param, long[] values) {
        return mathsInstanceBase.paramSet(param, values);
    }

    public LongArrayParamSet longParamSet(Param param) {
        return mathsInstanceBase.longParamSet(param);
    }

    public <T> ArrayParamSet<T> paramSet(Param param, T[] values) {
        return mathsInstanceBase.paramSet(param, values);
    }

    public <T> ArrayParamSet<T> objectParamSet(Param param) {
        return mathsInstanceBase.objectParamSet(param);
    }

    public IntArrayParamSet paramSet(Param param, int[] values) {
        return mathsInstanceBase.paramSet(param, values);
    }

    public IntArrayParamSet intParamSet(Param param) {
        return mathsInstanceBase.intParamSet(param);
    }

    public BooleanArrayParamSet paramSet(Param param, boolean[] values) {
        return mathsInstanceBase.paramSet(param, values);
    }

    public BooleanArrayParamSet boolParamSet(Param param) {
        return mathsInstanceBase.boolParamSet(param);
    }

    public XParamSet xParamSet(int xsamples) {
        return mathsInstanceBase.xParamSet(xsamples);
    }

    public XParamSet xyParamSet(int xsamples, int ysamples) {
        return mathsInstanceBase.xyParamSet(xsamples, ysamples);
    }

    public XParamSet xyzParamSet(int xsamples, int ysamples, int zsamples) {
        return mathsInstanceBase.xyzParamSet(xsamples, ysamples, zsamples);
    }

    public Matrix zerosMatrix(Matrix other) {
        return mathsInstanceBase.zerosMatrix(other);
    }

    public Matrix constantMatrix(int dim, Complex value) {
        return mathsInstanceBase.constantMatrix(dim, value);
    }

    public Matrix onesMatrix(int dim) {
        return mathsInstanceBase.onesMatrix(dim);
    }

    public Matrix onesMatrix(int rows, int cols) {
        return mathsInstanceBase.onesMatrix(rows, cols);
    }

    public Matrix constantMatrix(int rows, int cols, Complex value) {
        return mathsInstanceBase.constantMatrix(rows, cols, value);
    }

    public Matrix zerosMatrix(int dim) {
        return mathsInstanceBase.zerosMatrix(dim);
    }

    public Matrix I(Complex[][] iValue) {
        return mathsInstanceBase.I(iValue);
    }

    public Matrix zerosMatrix(int rows, int cols) {
        return mathsInstanceBase.zerosMatrix(rows, cols);
    }

    public Matrix identityMatrix(Matrix c) {
        return mathsInstanceBase.identityMatrix(c);
    }

    public Matrix NaNMatrix(int dim) {
        return mathsInstanceBase.NaNMatrix(dim);
    }

    public Matrix NaNMatrix(int rows, int cols) {
        return mathsInstanceBase.NaNMatrix(rows, cols);
    }

    public Matrix identityMatrix(int dim) {
        return mathsInstanceBase.identityMatrix(dim);
    }

    public Matrix identityMatrix(int rows, int cols) {
        return mathsInstanceBase.identityMatrix(rows, cols);
    }

    public Matrix matrix(Matrix matrix) {
        return mathsInstanceBase.matrix(matrix);
    }

    public Matrix matrix(String string) {
        return mathsInstanceBase.matrix(string);
    }

    public Matrix matrix(Complex[][] complex) {
        return mathsInstanceBase.matrix(complex);
    }

    public Matrix matrix(double[][] complex) {
        return mathsInstanceBase.matrix(complex);
    }

    public Matrix matrix(int rows, int cols, MatrixCell cellFactory) {
        return mathsInstanceBase.matrix(rows, cols, cellFactory);
    }

    public Matrix columnMatrix(final Complex... values) {
        return mathsInstanceBase.columnMatrix(values);
    }

    public Matrix columnMatrix(final double... values) {
        return mathsInstanceBase.columnMatrix(values);
    }

    public Matrix rowMatrix(final double... values) {
        return mathsInstanceBase.rowMatrix(values);
    }

    public Matrix rowMatrix(final Complex... values) {
        return mathsInstanceBase.rowMatrix(values);
    }

    public Matrix columnMatrix(int rows, final VectorCell cellFactory) {
        return mathsInstanceBase.columnMatrix(rows, cellFactory);
    }

    public Matrix rowMatrix(int columns, final VectorCell cellFactory) {
        return mathsInstanceBase.rowMatrix(columns, cellFactory);
    }

    public Matrix symmetricMatrix(int rows, int cols, MatrixCell cellFactory) {
        return mathsInstanceBase.symmetricMatrix(rows, cols, cellFactory);
    }

    public Matrix hermitianMatrix(int rows, int cols, MatrixCell cellFactory) {
        return mathsInstanceBase.hermitianMatrix(rows, cols, cellFactory);
    }

    public Matrix diagonalMatrix(int rows, int cols, MatrixCell cellFactory) {
        return mathsInstanceBase.diagonalMatrix(rows, cols, cellFactory);
    }

    public Matrix diagonalMatrix(int rows, final VectorCell cellFactory) {
        return mathsInstanceBase.diagonalMatrix(rows, cellFactory);
    }

    public Matrix diagonalMatrix(final Complex... c) {
        return mathsInstanceBase.diagonalMatrix(c);
    }

    public Matrix matrix(int dim, MatrixCell cellFactory) {
        return mathsInstanceBase.matrix(dim, cellFactory);
    }

    public Matrix matrix(int rows, int columns) {
        return mathsInstanceBase.matrix(rows, columns);
    }

    public Matrix symmetricMatrix(int dim, MatrixCell cellFactory) {
        return mathsInstanceBase.symmetricMatrix(dim, cellFactory);
    }

    public Matrix hermitianMatrix(int dim, MatrixCell cellFactory) {
        return mathsInstanceBase.hermitianMatrix(dim, cellFactory);
    }

    public Matrix diagonalMatrix(int dim, MatrixCell cellFactory) {
        return mathsInstanceBase.diagonalMatrix(dim, cellFactory);
    }

    public Matrix randomRealMatrix(int m, int n) {
        return mathsInstanceBase.randomRealMatrix(m, n);
    }

    public Matrix randomRealMatrix(int m, int n, int min, int max) {
        return mathsInstanceBase.randomRealMatrix(m, n, min, max);
    }

    public Matrix randomRealMatrix(int m, int n, double min, double max) {
        return mathsInstanceBase.randomRealMatrix(m, n, min, max);
    }

    public Matrix randomImagMatrix(int m, int n, double min, double max) {
        return mathsInstanceBase.randomImagMatrix(m, n, min, max);
    }

    public Matrix randomImagMatrix(int m, int n, int min, int max) {
        return mathsInstanceBase.randomImagMatrix(m, n, min, max);
    }

    public Matrix randomMatrix(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        return mathsInstanceBase.randomMatrix(m, n, minReal, maxReal, minImag, maxImag);
    }

    public Matrix randomMatrix(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        return mathsInstanceBase.randomMatrix(m, n, minReal, maxReal, minImag, maxImag);
    }

    public Matrix randomMatrix(int m, int n, double min, double max) {
        return mathsInstanceBase.randomMatrix(m, n, min, max);
    }

    public Matrix randomMatrix(int m, int n, int min, int max) {
        return mathsInstanceBase.randomMatrix(m, n, min, max);
    }

    public Matrix randomImagMatrix(int m, int n) {
        return mathsInstanceBase.randomImagMatrix(m, n);
    }

    public <T> TMatrix<T> loadTMatrix(TypeName<T> componentType, File file) throws UncheckedIOException {
        return mathsInstanceBase.loadTMatrix(componentType, file);
    }

    public Matrix loadMatrix(File file) throws UncheckedIOException {
        return mathsInstanceBase.loadMatrix(file);
    }

    public Matrix matrix(File file) throws UncheckedIOException {
        return mathsInstanceBase.matrix(file);
    }

    public void storeMatrix(Matrix m, String file) throws UncheckedIOException {
        mathsInstanceBase.storeMatrix(m, file);
    }

    public void storeMatrix(Matrix m, File file) throws UncheckedIOException {
        mathsInstanceBase.storeMatrix(m, file);
    }

    public Matrix loadOrEvalMatrix(String file, TItem<Matrix> item) throws UncheckedIOException {
        return mathsInstanceBase.loadOrEvalMatrix(file, item);
    }

    public Vector loadOrEvalVector(String file, TItem<TVector<Complex>> item) throws UncheckedIOException {
        return mathsInstanceBase.loadOrEvalVector(file, item);
    }

    public Matrix loadOrEvalMatrix(File file, TItem<Matrix> item) throws UncheckedIOException {
        return mathsInstanceBase.loadOrEvalMatrix(file, item);
    }

    public Vector loadOrEvalVector(File file, TItem<TVector<Complex>> item) throws UncheckedIOException {
        return mathsInstanceBase.loadOrEvalVector(file, item);
    }

    public <T> TMatrix loadOrEvalTMatrix(String file, TItem<TMatrix<T>> item) throws UncheckedIOException {
        return mathsInstanceBase.loadOrEvalTMatrix(file, item);
    }

    public <T> TVector<T> loadOrEvalTVector(String file, TItem<TVector<T>> item) throws UncheckedIOException {
        return mathsInstanceBase.loadOrEvalTVector(file, item);
    }

    public <T> TMatrix<T> loadOrEvalTMatrix(File file, TItem<TMatrix<T>> item) throws UncheckedIOException {
        return mathsInstanceBase.loadOrEvalTMatrix(file, item);
    }

    public <T> TVector loadOrEvalTVector(File file, TItem<TVector<T>> item) throws UncheckedIOException {
        return mathsInstanceBase.loadOrEvalTVector(file, item);
    }

    public <T> T loadOrEval(TypeName<T> type, File file, TItem<T> item) throws UncheckedIOException {
        return mathsInstanceBase.loadOrEval(type, file, item);
    }

    public Matrix loadMatrix(String file) throws UncheckedIOException {
        return mathsInstanceBase.loadMatrix(file);
    }

    public Matrix inv(Matrix c) {
        return mathsInstanceBase.inv(c);
    }

    public Matrix tr(Matrix c) {
        return mathsInstanceBase.tr(c);
    }

    public Matrix trh(Matrix c) {
        return mathsInstanceBase.trh(c);
    }

    public Matrix transpose(Matrix c) {
        return mathsInstanceBase.transpose(c);
    }

    public Matrix transposeHermitian(Matrix c) {
        return mathsInstanceBase.transposeHermitian(c);
    }

    public Vector rowVector(Complex[] elems) {
        return mathsInstanceBase.rowVector(elems);
    }

    public Vector constantColumnVector(int size, Complex c) {
        return mathsInstanceBase.constantColumnVector(size, c);
    }

    public Vector constantRowVector(int size, Complex c) {
        return mathsInstanceBase.constantRowVector(size, c);
    }

    public Vector zerosVector(int size) {
        return mathsInstanceBase.zerosVector(size);
    }

    public Vector zerosColumnVector(int size) {
        return mathsInstanceBase.zerosColumnVector(size);
    }

    public Vector zerosRowVector(int size) {
        return mathsInstanceBase.zerosRowVector(size);
    }

    public Vector NaNColumnVector(int dim) {
        return mathsInstanceBase.NaNColumnVector(dim);
    }

    public Vector NaNRowVector(int dim) {
        return mathsInstanceBase.NaNRowVector(dim);
    }

    public TVector<Expr> columnVector(Expr[] expr) {
        return mathsInstanceBase.columnVector(expr);
    }

    public TVector<Expr> rowVector(Expr[] expr) {
        return mathsInstanceBase.rowVector(expr);
    }

    public TVector<Expr> columnEVector(int rows, final TVectorCell<Expr> cellFactory) {
        return mathsInstanceBase.columnEVector(rows, cellFactory);
    }

    public TVector<Expr> rowEVector(int rows, final TVectorCell<Expr> cellFactory) {
        return mathsInstanceBase.rowEVector(rows, cellFactory);
    }

    public <T> TVector<T> updatableOf(TVector<T> vector) {
        return mathsInstanceBase.updatableOf(vector);
    }

    public Complex[][] copyOf(Complex[][] val) {
        return mathsInstanceBase.copyOf(val);
    }

    public Complex[] copyOf(Complex[] val) {
        return mathsInstanceBase.copyOf(val);
    }

    public <T> TList<T> copyOf(TVector<T> vector) {
        return mathsInstanceBase.copyOf(vector);
    }

    public <T> TVector<T> columnTVector(TypeName<T> cls, final TVectorModel<T> cellFactory) {
        return mathsInstanceBase.columnTVector(cls, cellFactory);
    }

    public <T> TVector<T> rowTVector(TypeName<T> cls, final TVectorModel<T> cellFactory) {
        return mathsInstanceBase.rowTVector(cls, cellFactory);
    }

    public <T> TVector<T> columnTVector(TypeName<T> cls, int rows, final TVectorCell<T> cellFactory) {
        return mathsInstanceBase.columnTVector(cls, rows, cellFactory);
    }

    public <T> TVector<T> rowTVector(TypeName<T> cls, int rows, final TVectorCell<T> cellFactory) {
        return mathsInstanceBase.rowTVector(cls, rows, cellFactory);
    }

    public Vector columnVector(int rows, final VectorCell cellFactory) {
        return mathsInstanceBase.columnVector(rows, cellFactory);
    }

    public Vector rowVector(int columns, final VectorCell cellFactory) {
        return mathsInstanceBase.rowVector(columns, cellFactory);
    }

    public Vector columnVector(Complex... elems) {
        return mathsInstanceBase.columnVector(elems);
    }

    public Vector columnVector(double[] elems) {
        return mathsInstanceBase.columnVector(elems);
    }

    public Vector rowVector(double[] elems) {
        return mathsInstanceBase.rowVector(elems);
    }

    public Vector column(Complex[] elems) {
        return mathsInstanceBase.column(elems);
    }

    public Vector row(Complex[] elems) {
        return mathsInstanceBase.row(elems);
    }

    public Vector trh(Vector c) {
        return mathsInstanceBase.trh(c);
    }

    public Vector tr(Vector c) {
        return mathsInstanceBase.tr(c);
    }

    public Complex I(double iValue) {
        return mathsInstanceBase.I(iValue);
    }

    public Complex abs(Complex a) {
        return mathsInstanceBase.abs(a);
    }

    public double absdbl(Complex a) {
        return mathsInstanceBase.absdbl(a);
    }

    public double[] getColumn(double[][] a, int index) {
        return mathsInstanceBase.getColumn(a, index);
    }

    public double[] dtimes(double min, double max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return mathsInstanceBase.dtimes(min, max, times, maxTimes, strategy);
    }

    public double[] dtimes(double min, double max, int times) {
        return mathsInstanceBase.dtimes(min, max, times);
    }

    public float[] ftimes(float min, float max, int times) {
        return mathsInstanceBase.ftimes(min, max, times);
    }

    public long[] ltimes(long min, long max, int times) {
        return mathsInstanceBase.ltimes(min, max, times);
    }

    public long[] lsteps(long min, long max, long step) {
        return mathsInstanceBase.lsteps(min, max, step);
    }

    public int[] itimes(int min, int max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return mathsInstanceBase.itimes(min, max, times, maxTimes, strategy);
    }

    public double[] dsteps(int max) {
        return mathsInstanceBase.dsteps(max);
    }

    public double[] dsteps(double min, double max) {
        return mathsInstanceBase.dsteps(min, max);
    }

    public double dstepsLength(double min, double max, double step) {
        return mathsInstanceBase.dstepsLength(min, max, step);
    }

    public double dstepsElement(double min, double max, double step, int index) {
        return mathsInstanceBase.dstepsElement(min, max, step, index);
    }

    public double[] dsteps(double min, double max, double step) {
        return mathsInstanceBase.dsteps(min, max, step);
    }

    public float[] fsteps(float min, float max, float step) {
        return mathsInstanceBase.fsteps(min, max, step);
    }

    public int[] isteps(int min, int max, int step) {
        return mathsInstanceBase.isteps(min, max, step);
    }

    public int[] isteps(int min, int max, int step, IntFilter filter) {
        return mathsInstanceBase.isteps(min, max, step, filter);
    }

    public int[] itimes(int min, int max, int times) {
        return mathsInstanceBase.itimes(min, max, times);
    }

    public int[] isteps(int max) {
        return mathsInstanceBase.isteps(max);
    }

    public int[] isteps(int min, int max) {
        return mathsInstanceBase.isteps(min, max);
    }

    public int[] itimes(int min, int max) {
        return mathsInstanceBase.itimes(min, max);
    }

    public double hypot(double a, double b) {
        return mathsInstanceBase.hypot(a, b);
    }

    public Complex sqr(Complex d) {
        return mathsInstanceBase.sqr(d);
    }

    public double sqr(double d) {
        return mathsInstanceBase.sqr(d);
    }

    public int sqr(int d) {
        return mathsInstanceBase.sqr(d);
    }

    public long sqr(long d) {
        return mathsInstanceBase.sqr(d);
    }

    public float sqr(float d) {
        return mathsInstanceBase.sqr(d);
    }

    public double sincard(double value) {
        return mathsInstanceBase.sincard(value);
    }

    public int minusOnePower(int pow) {
        return mathsInstanceBase.minusOnePower(pow);
    }

    public Complex exp(Complex c) {
        return mathsInstanceBase.exp(c);
    }

    public Complex sin(Complex c) {
        return mathsInstanceBase.sin(c);
    }

    public Complex sinh(Complex c) {
        return mathsInstanceBase.sinh(c);
    }

    public Complex cos(Complex c) {
        return mathsInstanceBase.cos(c);
    }

    public Complex log(Complex c) {
        return mathsInstanceBase.log(c);
    }

    public Complex log10(Complex c) {
        return mathsInstanceBase.log10(c);
    }

    public double log10(double c) {
        return mathsInstanceBase.log10(c);
    }

    public double log(double c) {
        return mathsInstanceBase.log(c);
    }

    public double acotan(double c) {
        return mathsInstanceBase.acotan(c);
    }

    public double exp(double c) {
        return mathsInstanceBase.exp(c);
    }

    public double arg(double c) {
        return mathsInstanceBase.arg(c);
    }

    public Complex db(Complex c) {
        return mathsInstanceBase.db(c);
    }

    public Complex db2(Complex c) {
        return mathsInstanceBase.db2(c);
    }

    public Complex cosh(Complex c) {
        return mathsInstanceBase.cosh(c);
    }

    public Complex csum(Complex... c) {
        return mathsInstanceBase.csum(c);
    }

    public Complex sum(Complex... c) {
        return mathsInstanceBase.sum(c);
    }

    public Complex csum(TVectorModel<Complex> c) {
        return mathsInstanceBase.csum(c);
    }

    public Complex csum(int size, TVectorCell<Complex> c) {
        return mathsInstanceBase.csum(size, c);
    }

    public double chbevl(double x, double[] coef, int N) throws ArithmeticException {
        return mathsInstanceBase.chbevl(x, coef, N);
    }

    public long pgcd(long a, long b) {
        return mathsInstanceBase.pgcd(a, b);
    }

    public int pgcd(int a, int b) {
        return mathsInstanceBase.pgcd(a, b);
    }

    public double[][] toDouble(Complex[][] c, PlotDoubleConverter toDoubleConverter) {
        return mathsInstanceBase.toDouble(c, toDoubleConverter);
    }

    public double[] toDouble(Complex[] c, PlotDoubleConverter toDoubleConverter) {
        return mathsInstanceBase.toDouble(c, toDoubleConverter);
    }

    public int[] rangeCC(double[] orderedValues, double min, double max) {
        return mathsInstanceBase.rangeCC(orderedValues, min, max);
    }

    public int[] rangeCO(double[] orderedValues, double min, double max) {
        return mathsInstanceBase.rangeCO(orderedValues, min, max);
    }

    public Complex csqrt(double d) {
        return mathsInstanceBase.csqrt(d);
    }

    public Complex sqrt(Complex d) {
        return mathsInstanceBase.sqrt(d);
    }

    public double dsqrt(double d) {
        return mathsInstanceBase.dsqrt(d);
    }

    public Complex cotanh(Complex c) {
        return mathsInstanceBase.cotanh(c);
    }

    public Complex tanh(Complex c) {
        return mathsInstanceBase.tanh(c);
    }

    public Complex inv(Complex c) {
        return mathsInstanceBase.inv(c);
    }

    public Complex tan(Complex c) {
        return mathsInstanceBase.tan(c);
    }

    public Complex cotan(Complex c) {
        return mathsInstanceBase.cotan(c);
    }

    public Vector vector(TVector v) {
        return mathsInstanceBase.vector(v);
    }

    public Complex pow(Complex a, Complex b) {
        return mathsInstanceBase.pow(a, b);
    }

    public Complex div(Complex a, Complex b) {
        return mathsInstanceBase.div(a, b);
    }

    public Complex add(Complex a, Complex b) {
        return mathsInstanceBase.add(a, b);
    }

    public Complex sub(Complex a, Complex b) {
        return mathsInstanceBase.sub(a, b);
    }

    public double norm(Matrix a) {
        return mathsInstanceBase.norm(a);
    }

    public double norm(Vector a) {
        return mathsInstanceBase.norm(a);
    }

    public double norm1(Matrix a) {
        return mathsInstanceBase.norm1(a);
    }

    public double norm2(Matrix a) {
        return mathsInstanceBase.norm2(a);
    }

    public double norm3(Matrix a) {
        return mathsInstanceBase.norm3(a);
    }

    public double normInf(Matrix a) {
        return mathsInstanceBase.normInf(a);
    }

    public DoubleToComplex complex(DoubleToDouble fx) {
        return mathsInstanceBase.complex(fx);
    }

    public DoubleToComplex complex(DoubleToDouble fx, DoubleToDouble fy) {
        return mathsInstanceBase.complex(fx, fy);
    }

    public double randomDouble(double value) {
        return mathsInstanceBase.randomDouble(value);
    }

    public double randomDouble(double min, double max) {
        return mathsInstanceBase.randomDouble(min, max);
    }

    public int randomInt(int value) {
        return mathsInstanceBase.randomInt(value);
    }

    public int randomInt(int min, int max) {
        return mathsInstanceBase.randomInt(min, max);
    }

    public Complex randomComplex() {
        return mathsInstanceBase.randomComplex();
    }

    public boolean randomBoolean() {
        return mathsInstanceBase.randomBoolean();
    }

    public double[][] cross(double[] x, double[] y) {
        return mathsInstanceBase.cross(x, y);
    }

    public double[][] cross(double[] x, double[] y, double[] z) {
        return mathsInstanceBase.cross(x, y, z);
    }

    public double[][] cross(double[] x, double[] y, double[] z, Double3Filter filter) {
        return mathsInstanceBase.cross(x, y, z, filter);
    }

    public double[][] cross(double[] x, double[] y, Double2Filter filter) {
        return mathsInstanceBase.cross(x, y, filter);
    }

    public int[][] cross(int[] x, int[] y) {
        return mathsInstanceBase.cross(x, y);
    }

    public int[][] cross(int[] x, int[] y, int[] z) {
        return mathsInstanceBase.cross(x, y, z);
    }

    public TList sineSeq(String borders, int m, int n, Domain domain) {
        return mathsInstanceBase.sineSeq(borders, m, n, domain);
    }

    public TList sineSeq(String borders, int m, int n, Domain domain, PlaneAxis plane) {
        return mathsInstanceBase.sineSeq(borders, m, n, domain, plane);
    }

    public Expr sineSeq(String borders, DoubleParam m, DoubleParam n, Domain domain) {
        return mathsInstanceBase.sineSeq(borders, m, n, domain);
    }

    public Expr rooftop(String borders, int nx, int ny, Domain domain) {
        return mathsInstanceBase.rooftop(borders, nx, ny, domain);
    }

    public Any any(double e) {
        return mathsInstanceBase.any(e);
    }

    public Any any(Expr e) {
        return mathsInstanceBase.any(e);
    }

    public Any any(Double e) {
        return mathsInstanceBase.any(e);
    }

    public TList<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax, Int2Filter filter) {
        return mathsInstanceBase.seq(pattern, m, mmax, n, nmax, filter);
    }

    public TList<Expr> seq(Expr pattern, DoubleParam m, int max, IntFilter filter) {
        return mathsInstanceBase.seq(pattern, m, max, filter);
    }

    public TList<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax, DoubleParam p, int pmax, Int3Filter filter) {
        return mathsInstanceBase.seq(pattern, m, mmax, n, nmax, p, pmax, filter);
    }

    public TList<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax) {
        return mathsInstanceBase.seq(pattern, m, mmax, n, nmax);
    }

    public TList<Expr> seq(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues) {
        return mathsInstanceBase.seq(pattern, m, mvalues, n, nvalues);
    }

    public TList<Expr> seq(final Expr pattern, final DoubleParam m, final DoubleParam n, final double[][] values) {
        return mathsInstanceBase.seq(pattern, m, n, values);
    }

    public TList<Expr> seq(final Expr pattern, final DoubleParam m, final DoubleParam n, final DoubleParam p, final double[][] values) {
        return mathsInstanceBase.seq(pattern, m, n, p, values);
    }

    public TList<Expr> seq(final Expr pattern, final DoubleParam[] m, final double[][] values) {
        return mathsInstanceBase.seq(pattern, m, values);
    }

    public TList<Expr> seq(final Expr pattern, final DoubleParam m, int min, int max) {
        return mathsInstanceBase.seq(pattern, m, min, max);
    }

    public TList<Expr> seq(final Expr pattern, final DoubleParam m, final double[] values) {
        return mathsInstanceBase.seq(pattern, m, values);
    }

    public ExprMatrix2 matrix(final Expr pattern, final DoubleParam m, final double[] mvalues, final DoubleParam n, final double[] nvalues) {
        return mathsInstanceBase.matrix(pattern, m, mvalues, n, nvalues);
    }

    public ExprCube cube(final Expr pattern, final DoubleParam m, final double[] mvalues, final DoubleParam n, final double[] nvalues, final DoubleParam p, final double[] pvalues) {
        return mathsInstanceBase.cube(pattern, m, mvalues, n, nvalues, p, pvalues);
    }

    public Expr derive(Expr f, Axis axis) {
        return mathsInstanceBase.derive(f, axis);
    }

    public boolean isReal(Expr e) {
        return mathsInstanceBase.isReal(e);
    }

    public boolean isImag(Expr e) {
        return mathsInstanceBase.isImag(e);
    }

    public Expr abs(Expr e) {
        return mathsInstanceBase.abs(e);
    }

    public Expr db(Expr e) {
        return mathsInstanceBase.db(e);
    }

    public Expr db2(Expr e) {
        return mathsInstanceBase.db2(e);
    }

    public Complex complex(int e) {
        return mathsInstanceBase.complex(e);
    }

    public Complex complex(double e) {
        return mathsInstanceBase.complex(e);
    }

    public Complex complex(double a, double b) {
        return mathsInstanceBase.complex(a, b);
    }

    public double Double(Expr e) {
        return mathsInstanceBase.Double(e);
    }

    public Expr real(Expr e) {
        return mathsInstanceBase.real(e);
    }

    public Expr imag(Expr e) {
        return mathsInstanceBase.imag(e);
    }

    public Complex Complex(Expr e) {
        return mathsInstanceBase.Complex(e);
    }

    public Complex complex(Expr e) {
        return mathsInstanceBase.complex(e);
    }

    public double doubleValue(Expr e) {
        return mathsInstanceBase.doubleValue(e);
    }

    public Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3) {
        return mathsInstanceBase.discrete(domain, model, x, y, z, dx, dy, dz, axis1, axis2, axis3);
    }

    public Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz) {
        return mathsInstanceBase.discrete(domain, model, x, y, z, dx, dy, dz);
    }

    public Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z) {
        return mathsInstanceBase.discrete(domain, model, x, y, z);
    }

    public Discrete discrete(Domain domain, Complex[][][] model, double dx, double dy, double dz) {
        return mathsInstanceBase.discrete(domain, model, dx, dy, dz);
    }

    public Discrete discrete(Complex[][][] model, double[] x, double[] y, double[] z) {
        return mathsInstanceBase.discrete(model, x, y, z);
    }

    public Discrete discrete(Complex[][] model, double[] x, double[] y) {
        return mathsInstanceBase.discrete(model, x, y);
    }

    public Expr discrete(Expr expr, double[] xsamples, double[] ysamples, double[] zsamples) {
        return mathsInstanceBase.discrete(expr, xsamples, ysamples, zsamples);
    }

    public Expr discrete(Expr expr, Samples samples) {
        return mathsInstanceBase.discrete(expr, samples);
    }

    public Expr abssqr(Expr e) {
        return mathsInstanceBase.abssqr(e);
    }

    public AdaptiveResult1 adaptiveEval(Expr expr, AdaptiveConfig config) {
        return mathsInstanceBase.adaptiveEval(expr, config);
    }

    public <T> AdaptiveResult1 adaptiveEval(AdaptiveFunction1<T> expr, DistanceStrategy<T> distance, DomainX domain, AdaptiveConfig config) {
        return mathsInstanceBase.adaptiveEval(expr, distance, domain, config);
    }

    public Discrete discrete(Expr expr) {
        return mathsInstanceBase.discrete(expr);
    }

    public VDiscrete vdiscrete(Expr expr) {
        return mathsInstanceBase.vdiscrete(expr);
    }

    public Expr discrete(Expr expr, int xSamples) {
        return mathsInstanceBase.discrete(expr, xSamples);
    }

    public Expr discrete(Expr expr, int xSamples, int ySamples) {
        return mathsInstanceBase.discrete(expr, xSamples, ySamples);
    }

    public Expr discrete(Expr expr, int xSamples, int ySamples, int zSamples) {
        return mathsInstanceBase.discrete(expr, xSamples, ySamples, zSamples);
    }

    public AxisFunction axis(Axis e) {
        return mathsInstanceBase.axis(e);
    }

    public Expr transformAxis(Expr e, AxisFunction a1, AxisFunction a2, AxisFunction a3) {
        return mathsInstanceBase.transformAxis(e, a1, a2, a3);
    }

    public Expr transformAxis(Expr e, Axis a1, Axis a2, Axis a3) {
        return mathsInstanceBase.transformAxis(e, a1, a2, a3);
    }

    public Expr transformAxis(Expr e, AxisFunction a1, AxisFunction a2) {
        return mathsInstanceBase.transformAxis(e, a1, a2);
    }

    public Expr transformAxis(Expr e, Axis a1, Axis a2) {
        return mathsInstanceBase.transformAxis(e, a1, a2);
    }

    public double sin2(double d) {
        return mathsInstanceBase.sin2(d);
    }

    public double cos2(double d) {
        return mathsInstanceBase.cos2(d);
    }

    public boolean isInt(double d) {
        return mathsInstanceBase.isInt(d);
    }

    public <T> T lcast(Object o, Class<T> type) {
        return mathsInstanceBase.lcast(o, type);
    }

    public String dump(Object o) {
        return mathsInstanceBase.dump(o);
    }

    public String dumpSimple(Object o) {
        return mathsInstanceBase.dumpSimple(o);
    }

    public DoubleToDouble expr(double value, Geometry geometry) {
        return mathsInstanceBase.expr(value, geometry);
    }

    public DoubleToDouble expr(double value, Domain geometry) {
        return mathsInstanceBase.expr(value, geometry);
    }

    public DoubleToDouble expr(Domain domain) {
        return mathsInstanceBase.expr(domain);
    }

    public DoubleToDouble expr(Geometry domain) {
        return mathsInstanceBase.expr(domain);
    }

    public Expr expr(Complex a, Geometry geometry) {
        return mathsInstanceBase.expr(a, geometry);
    }

    public Expr expr(Complex a, Domain geometry) {
        return mathsInstanceBase.expr(a, geometry);
    }

    public <T extends Expr> TList<T> preload(TList<T> list) {
        return mathsInstanceBase.preload(list);
    }

    public <T extends Expr> TList<T> withCache(TList<T> list) {
        return mathsInstanceBase.withCache(list);
    }

    public <T> TList<T> abs(TList<T> a) {
        return mathsInstanceBase.abs(a);
    }

    public <T> TList<T> db(TList<T> a) {
        return mathsInstanceBase.db(a);
    }

    public <T> TList<T> db2(TList<T> a) {
        return mathsInstanceBase.db2(a);
    }

    public <T> TList<T> real(TList<T> a) {
        return mathsInstanceBase.real(a);
    }

    public <T> TList<T> imag(TList<T> a) {
        return mathsInstanceBase.imag(a);
    }

    public double real(Complex a) {
        return mathsInstanceBase.real(a);
    }

    public double imag(Complex a) {
        return mathsInstanceBase.imag(a);
    }

    public boolean almostEqualRelative(float a, float b, float maxRelativeError) {
        return mathsInstanceBase.almostEqualRelative(a, b, maxRelativeError);
    }

    public boolean almostEqualRelative(double a, double b, double maxRelativeError) {
        return mathsInstanceBase.almostEqualRelative(a, b, maxRelativeError);
    }

    public boolean almostEqualRelative(Complex a, Complex b, double maxRelativeError) {
        return mathsInstanceBase.almostEqualRelative(a, b, maxRelativeError);
    }

    public DoubleArrayParamSet dtimes(Param param, double min, double max, int times) {
        return mathsInstanceBase.dtimes(param, min, max, times);
    }

    public DoubleArrayParamSet dsteps(Param param, double min, double max, double step) {
        return mathsInstanceBase.dsteps(param, min, max, step);
    }

    public IntArrayParamSet itimes(Param param, int min, int max, int times) {
        return mathsInstanceBase.itimes(param, min, max, times);
    }

    public IntArrayParamSet isteps(Param param, int min, int max, int step) {
        return mathsInstanceBase.isteps(param, min, max, step);
    }

    public FloatArrayParamSet ftimes(Param param, int min, int max, int times) {
        return mathsInstanceBase.ftimes(param, min, max, times);
    }

    public FloatArrayParamSet fsteps(Param param, int min, int max, int step) {
        return mathsInstanceBase.fsteps(param, min, max, step);
    }

    public LongArrayParamSet ltimes(Param param, int min, int max, int times) {
        return mathsInstanceBase.ltimes(param, min, max, times);
    }

    public LongArrayParamSet lsteps(Param param, int min, int max, long step) {
        return mathsInstanceBase.lsteps(param, min, max, step);
    }

    public Vector sin(Vector v) {
        return mathsInstanceBase.sin(v);
    }

    public Vector cos(Vector v) {
        return mathsInstanceBase.cos(v);
    }

    public Vector tan(Vector v) {
        return mathsInstanceBase.tan(v);
    }

    public Vector cotan(Vector v) {
        return mathsInstanceBase.cotan(v);
    }

    public Vector tanh(Vector v) {
        return mathsInstanceBase.tanh(v);
    }

    public Vector cotanh(Vector v) {
        return mathsInstanceBase.cotanh(v);
    }

    public Vector cosh(Vector v) {
        return mathsInstanceBase.cosh(v);
    }

    public Vector sinh(Vector v) {
        return mathsInstanceBase.sinh(v);
    }

    public Vector log(Vector v) {
        return mathsInstanceBase.log(v);
    }

    public Vector log10(Vector v) {
        return mathsInstanceBase.log10(v);
    }

    public Vector db(Vector v) {
        return mathsInstanceBase.db(v);
    }

    public Vector exp(Vector v) {
        return mathsInstanceBase.exp(v);
    }

    public Vector acosh(Vector v) {
        return mathsInstanceBase.acosh(v);
    }

    public Vector acos(Vector v) {
        return mathsInstanceBase.acos(v);
    }

    public Vector asinh(Vector v) {
        return mathsInstanceBase.asinh(v);
    }

    public Vector asin(Vector v) {
        return mathsInstanceBase.asin(v);
    }

    public Vector atan(Vector v) {
        return mathsInstanceBase.atan(v);
    }

    public Vector acotan(Vector v) {
        return mathsInstanceBase.acotan(v);
    }

    public Vector imag(Vector v) {
        return mathsInstanceBase.imag(v);
    }

    public Vector real(Vector v) {
        return mathsInstanceBase.real(v);
    }

    public Vector abs(Vector v) {
        return mathsInstanceBase.abs(v);
    }

    public Complex[] abs(Complex[] v) {
        return mathsInstanceBase.abs(v);
    }

    public Complex avg(Vector v) {
        return mathsInstanceBase.avg(v);
    }

    public Complex sum(Vector v) {
        return mathsInstanceBase.sum(v);
    }

    public Complex prod(Vector v) {
        return mathsInstanceBase.prod(v);
    }

    public Matrix abs(Matrix v) {
        return mathsInstanceBase.abs(v);
    }

    public Matrix sin(Matrix v) {
        return mathsInstanceBase.sin(v);
    }

    public Matrix cos(Matrix v) {
        return mathsInstanceBase.cos(v);
    }

    public Matrix tan(Matrix v) {
        return mathsInstanceBase.tan(v);
    }

    public Matrix cotan(Matrix v) {
        return mathsInstanceBase.cotan(v);
    }

    public Matrix tanh(Matrix v) {
        return mathsInstanceBase.tanh(v);
    }

    public Matrix cotanh(Matrix v) {
        return mathsInstanceBase.cotanh(v);
    }

    public Matrix cosh(Matrix v) {
        return mathsInstanceBase.cosh(v);
    }

    public Matrix sinh(Matrix v) {
        return mathsInstanceBase.sinh(v);
    }

    public Matrix log(Matrix v) {
        return mathsInstanceBase.log(v);
    }

    public Matrix log10(Matrix v) {
        return mathsInstanceBase.log10(v);
    }

    public Matrix db(Matrix v) {
        return mathsInstanceBase.db(v);
    }

    public Matrix exp(Matrix v) {
        return mathsInstanceBase.exp(v);
    }

    public Matrix acosh(Matrix v) {
        return mathsInstanceBase.acosh(v);
    }

    public Matrix acos(Matrix v) {
        return mathsInstanceBase.acos(v);
    }

    public Matrix asinh(Matrix v) {
        return mathsInstanceBase.asinh(v);
    }

    public Matrix asin(Matrix v) {
        return mathsInstanceBase.asin(v);
    }

    public Matrix atan(Matrix v) {
        return mathsInstanceBase.atan(v);
    }

    public Matrix acotan(Matrix v) {
        return mathsInstanceBase.acotan(v);
    }

    public Matrix imag(Matrix v) {
        return mathsInstanceBase.imag(v);
    }

    public Matrix real(Matrix v) {
        return mathsInstanceBase.real(v);
    }

    public Complex[] real(Complex[] v) {
        return mathsInstanceBase.real(v);
    }

    public double[] realdbl(Complex[] v) {
        return mathsInstanceBase.realdbl(v);
    }

    public Complex[] imag(Complex[] v) {
        return mathsInstanceBase.imag(v);
    }

    public double[] imagdbl(Complex[] v) {
        return mathsInstanceBase.imagdbl(v);
    }

    public Complex avg(Matrix v) {
        return mathsInstanceBase.avg(v);
    }

    public Complex sum(Matrix v) {
        return mathsInstanceBase.sum(v);
    }

    public Complex prod(Matrix v) {
        return mathsInstanceBase.prod(v);
    }

    public boolean roundEquals(double a, double b, double epsilon) {
        return mathsInstanceBase.roundEquals(a, b, epsilon);
    }

    public double round(double val, double precision) {
        return mathsInstanceBase.round(val, precision);
    }

    public double sqrt(double v, int n) {
        return mathsInstanceBase.sqrt(v, n);
    }

    public double pow(double v, double n) {
        return mathsInstanceBase.pow(v, n);
    }

    public double db(double x) {
        return mathsInstanceBase.db(x);
    }

    public double acosh(double x) {
        return mathsInstanceBase.acosh(x);
    }

    public double atanh(double x) {
        return mathsInstanceBase.atanh(x);
    }

    public double acotanh(double x) {
        return mathsInstanceBase.acotanh(x);
    }

    public double asinh(double x) {
        return mathsInstanceBase.asinh(x);
    }

    public double db2(double nbr) {
        return mathsInstanceBase.db2(nbr);
    }

    public double sqrt(double nbr) {
        return mathsInstanceBase.sqrt(nbr);
    }

    public double inv(double x) {
        return mathsInstanceBase.inv(x);
    }

    public double conj(double x) {
        return mathsInstanceBase.conj(x);
    }

    public double[] sin2(double[] x) {
        return mathsInstanceBase.sin2(x);
    }

    public double[] cos2(double[] x) {
        return mathsInstanceBase.cos2(x);
    }

    public double[] sin(double[] x) {
        return mathsInstanceBase.sin(x);
    }

    public double[] cos(double[] x) {
        return mathsInstanceBase.cos(x);
    }

    public double[] tan(double[] x) {
        return mathsInstanceBase.tan(x);
    }

    public double[] cotan(double[] x) {
        return mathsInstanceBase.cotan(x);
    }

    public double[] sinh(double[] x) {
        return mathsInstanceBase.sinh(x);
    }

    public double[] cosh(double[] x) {
        return mathsInstanceBase.cosh(x);
    }

    public double[] tanh(double[] x) {
        return mathsInstanceBase.tanh(x);
    }

    public double[] cotanh(double[] x) {
        return mathsInstanceBase.cotanh(x);
    }

    public double max(double a, double b) {
        return mathsInstanceBase.max(a, b);
    }

    public int max(int a, int b) {
        return mathsInstanceBase.max(a, b);
    }

    public long max(long a, long b) {
        return mathsInstanceBase.max(a, b);
    }

    public double min(double a, double b) {
        return mathsInstanceBase.min(a, b);
    }

    public double min(double[] arr) {
        return mathsInstanceBase.min(arr);
    }

    public double max(double[] arr) {
        return mathsInstanceBase.max(arr);
    }

    public double avg(double[] arr) {
        return mathsInstanceBase.avg(arr);
    }

    public int min(int a, int b) {
        return mathsInstanceBase.min(a, b);
    }

    public Complex min(Complex a, Complex b) {
        return mathsInstanceBase.min(a, b);
    }

    public Complex max(Complex a, Complex b) {
        return mathsInstanceBase.max(a, b);
    }

    public long min(long a, long b) {
        return mathsInstanceBase.min(a, b);
    }

    public double[] minMax(double[] a) {
        return mathsInstanceBase.minMax(a);
    }

    public double[] minMaxAbs(double[] a) {
        return mathsInstanceBase.minMaxAbs(a);
    }

    public double[] minMaxAbsNonInfinite(double[] a) {
        return mathsInstanceBase.minMaxAbsNonInfinite(a);
    }

    public double avgAbs(double[] arr) {
        return mathsInstanceBase.avgAbs(arr);
    }

    public double[] distances(double[] arr) {
        return mathsInstanceBase.distances(arr);
    }

    public double[] div(double[] a, double[] b) {
        return mathsInstanceBase.div(a, b);
    }

    public double[] mul(double[] a, double[] b) {
        return mathsInstanceBase.mul(a, b);
    }

    public double[] sub(double[] a, double[] b) {
        return mathsInstanceBase.sub(a, b);
    }

    public double[] sub(double[] a, double b) {
        return mathsInstanceBase.sub(a, b);
    }

    public double[] add(double[] a, double[] b) {
        return mathsInstanceBase.add(a, b);
    }

    public double[] db(double[] a) {
        return mathsInstanceBase.db(a);
    }

    public double[][] sin(double[][] c) {
        return mathsInstanceBase.sin(c);
    }

    public double[][] sin2(double[][] c) {
        return mathsInstanceBase.sin2(c);
    }

    public double sin(double x) {
        return mathsInstanceBase.sin(x);
    }

    public double cos(double x) {
        return mathsInstanceBase.cos(x);
    }

    public double tan(double x) {
        return mathsInstanceBase.tan(x);
    }

    public double cotan(double x) {
        return mathsInstanceBase.cotan(x);
    }

    public double sinh(double x) {
        return mathsInstanceBase.sinh(x);
    }

    public double cosh(double x) {
        return mathsInstanceBase.cosh(x);
    }

    public double tanh(double x) {
        return mathsInstanceBase.tanh(x);
    }

    public double abs(double a) {
        return mathsInstanceBase.abs(a);
    }

    public int abs(int a) {
        return mathsInstanceBase.abs(a);
    }

    public double cotanh(double x) {
        return mathsInstanceBase.cotanh(x);
    }

    public double acos(double x) {
        return mathsInstanceBase.acos(x);
    }

    public double asin(double x) {
        return mathsInstanceBase.asin(x);
    }

    public double atan(double x) {
        return mathsInstanceBase.atan(x);
    }

    public double sum(double... c) {
        return mathsInstanceBase.sum(c);
    }

    public double[] mul(double[] a, double b) {
        return mathsInstanceBase.mul(a, b);
    }

    public double[] mulSelf(double[] x, double v) {
        return mathsInstanceBase.mulSelf(x, v);
    }

    public double[] div(double[] a, double b) {
        return mathsInstanceBase.div(a, b);
    }

    public double[] divSelf(double[] x, double v) {
        return mathsInstanceBase.divSelf(x, v);
    }

    public double[] add(double[] x, double v) {
        return mathsInstanceBase.add(x, v);
    }

    public double[] addSelf(double[] x, double v) {
        return mathsInstanceBase.addSelf(x, v);
    }

    public double[][] cos(double[][] c) {
        return mathsInstanceBase.cos(c);
    }

    public double[][] tan(double[][] c) {
        return mathsInstanceBase.tan(c);
    }

    public double[][] cotan(double[][] c) {
        return mathsInstanceBase.cotan(c);
    }

    public double[][] sinh(double[][] c) {
        return mathsInstanceBase.sinh(c);
    }

    public double[][] cosh(double[][] c) {
        return mathsInstanceBase.cosh(c);
    }

    public double[][] tanh(double[][] c) {
        return mathsInstanceBase.tanh(c);
    }

    public double[][] cotanh(double[][] c) {
        return mathsInstanceBase.cotanh(c);
    }

    public double[][] add(double[][] a, double[][] b) {
        return mathsInstanceBase.add(a, b);
    }

    public double[][] sub(double[][] a, double[][] b) {
        return mathsInstanceBase.sub(a, b);
    }

    public double[][] div(double[][] a, double[][] b) {
        return mathsInstanceBase.div(a, b);
    }

    public double[][] mul(double[][] a, double[][] b) {
        return mathsInstanceBase.mul(a, b);
    }

    public double[][] db(double[][] a) {
        return mathsInstanceBase.db(a);
    }

    public double[][] db2(double[][] a) {
        return mathsInstanceBase.db2(a);
    }

    public Expr If(Expr cond, Expr exp1, Expr exp2) {
        return mathsInstanceBase.If(cond, exp1, exp2);
    }

    public Expr or(Expr a, Expr b) {
        return mathsInstanceBase.or(a, b);
    }

    public Expr and(Expr a, Expr b) {
        return mathsInstanceBase.and(a, b);
    }

    public Expr not(Expr a) {
        return mathsInstanceBase.not(a);
    }

    public Expr eq(Expr a, Expr b) {
        return mathsInstanceBase.eq(a, b);
    }

    public Expr ne(Expr a, Expr b) {
        return mathsInstanceBase.ne(a, b);
    }

    public Expr gte(Expr a, Expr b) {
        return mathsInstanceBase.gte(a, b);
    }

    public Expr gt(Expr a, Expr b) {
        return mathsInstanceBase.gt(a, b);
    }

    public Expr lte(Expr a, Expr b) {
        return mathsInstanceBase.lte(a, b);
    }

    public Expr lt(Expr a, Expr b) {
        return mathsInstanceBase.lt(a, b);
    }

    public Expr cos(Expr e) {
        return mathsInstanceBase.cos(e);
    }

    public Expr cosh(Expr e) {
        return mathsInstanceBase.cosh(e);
    }

    public Expr sin(Expr e) {
        return mathsInstanceBase.sin(e);
    }

    public Expr sincard(Expr e) {
        return mathsInstanceBase.sincard(e);
    }

    public Expr sinh(Expr e) {
        return mathsInstanceBase.sinh(e);
    }

    public Expr tan(Expr e) {
        return mathsInstanceBase.tan(e);
    }

    public Expr tanh(Expr e) {
        return mathsInstanceBase.tanh(e);
    }

    public Expr cotan(Expr e) {
        return mathsInstanceBase.cotan(e);
    }

    public Expr cotanh(Expr e) {
        return mathsInstanceBase.cotanh(e);
    }

    public Expr sqr(Expr e) {
        return mathsInstanceBase.sqr(e);
    }

    public Expr sqrt(Expr e) {
        return mathsInstanceBase.sqrt(e);
    }

    public Expr inv(Expr e) {
        return mathsInstanceBase.inv(e);
    }

    public Expr neg(Expr e) {
        return mathsInstanceBase.neg(e);
    }

    public Expr exp(Expr e) {
        return mathsInstanceBase.exp(e);
    }

    public Expr atan(Expr e) {
        return mathsInstanceBase.atan(e);
    }

    public Expr acotan(Expr e) {
        return mathsInstanceBase.acotan(e);
    }

    public Expr acos(Expr e) {
        return mathsInstanceBase.acos(e);
    }

    public Expr asin(Expr e) {
        return mathsInstanceBase.asin(e);
    }

    public Complex integrate(Expr e) {
        return mathsInstanceBase.integrate(e);
    }

    public Complex integrate(Expr e, Domain domain) {
        return mathsInstanceBase.integrate(e, domain);
    }

    public Expr esum(int size, TVectorCell<Expr> f) {
        return mathsInstanceBase.esum(size, f);
    }

    public Expr esum(int size1, int size2, TMatrixCell<Expr> e) {
        return mathsInstanceBase.esum(size1, size2, e);
    }

    public Complex csum(int size1, int size2, TMatrixCell<Complex> e) {
        return mathsInstanceBase.csum(size1, size2, e);
    }

    public TVector<Expr> seq(int size1, TVectorCell<Expr> f) {
        return mathsInstanceBase.seq(size1, f);
    }

    public TVector<Expr> seq(int size1, int size2, TMatrixCell<Expr> f) {
        return mathsInstanceBase.seq(size1, size2, f);
    }

    public TMatrix<Complex> scalarProductCache(Expr[] gp, Expr[] fn, ProgressMonitor monitor) {
        return mathsInstanceBase.scalarProductCache(gp, fn, monitor);
    }

    public TMatrix<Complex> scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, ProgressMonitor monitor) {
        return mathsInstanceBase.scalarProductCache(sp, gp, fn, monitor);
    }

    public TMatrix<Complex> scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, AxisXY axis, ProgressMonitor monitor) {
        return mathsInstanceBase.scalarProductCache(sp, gp, fn, axis, monitor);
    }

    public TMatrix<Complex> scalarProductCache(Expr[] gp, Expr[] fn, AxisXY axis, ProgressMonitor monitor) {
        return mathsInstanceBase.scalarProductCache(gp, fn, axis, monitor);
    }

    public Expr gate(Axis axis, double a, double b) {
        return mathsInstanceBase.gate(axis, a, b);
    }

    public Expr gate(Expr axis, double a, double b) {
        return mathsInstanceBase.gate(axis, a, b);
    }

    public Expr gateX(double a, double b) {
        return mathsInstanceBase.gateX(a, b);
    }

    public Expr gateY(double a, double b) {
        return mathsInstanceBase.gateY(a, b);
    }

    public Expr gateZ(double a, double b) {
        return mathsInstanceBase.gateZ(a, b);
    }

    public double scalarProduct(DoubleToDouble f1, DoubleToDouble f2) {
        return mathsInstanceBase.scalarProduct(f1, f2);
    }

    public Vector scalarProduct(Expr f1, TVector<Expr> f2) {
        return mathsInstanceBase.scalarProduct(f1, f2);
    }

    public Matrix scalarProduct(Expr f1, TMatrix<Expr> f2) {
        return mathsInstanceBase.scalarProduct(f1, f2);
    }

    public Vector scalarProduct(TVector<Expr> f2, Expr f1) {
        return mathsInstanceBase.scalarProduct(f2, f1);
    }

    public Matrix scalarProduct(TMatrix<Expr> f2, Expr f1) {
        return mathsInstanceBase.scalarProduct(f2, f1);
    }

    public Complex scalarProduct(Domain domain, Expr f1, Expr f2) {
        return mathsInstanceBase.scalarProduct(domain, f1, f2);
    }

    public Complex scalarProduct(Expr f1, Expr f2) {
        return mathsInstanceBase.scalarProduct(f1, f2);
    }

    public Matrix scalarProductMatrix(TVector<Expr> g, TVector<Expr> f) {
        return mathsInstanceBase.scalarProductMatrix(g, f);
    }

    public TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f) {
        return mathsInstanceBase.scalarProduct(g, f);
    }

    public TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return mathsInstanceBase.scalarProduct(g, f, monitor);
    }

    public Matrix scalarProductMatrix(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return mathsInstanceBase.scalarProductMatrix(g, f, monitor);
    }

    public TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f, AxisXY axis, ProgressMonitor monitor) {
        return mathsInstanceBase.scalarProduct(g, f, axis, monitor);
    }

    public Matrix scalarProductMatrix(Expr[] g, Expr[] f) {
        return mathsInstanceBase.scalarProductMatrix(g, f);
    }

    public Complex scalarProduct(Matrix g, Matrix f) {
        return mathsInstanceBase.scalarProduct(g, f);
    }

    public Expr scalarProduct(Matrix g, TVector<Expr> f) {
        return mathsInstanceBase.scalarProduct(g, f);
    }

    public Expr scalarProductAll(Matrix g, TVector<Expr>... f) {
        return mathsInstanceBase.scalarProductAll(g, f);
    }

    public TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f) {
        return mathsInstanceBase.scalarProduct(g, f);
    }

    public TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return mathsInstanceBase.scalarProduct(g, f, monitor);
    }

    public Matrix scalarProductMatrix(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return mathsInstanceBase.scalarProductMatrix(g, f, monitor);
    }

    public TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f, AxisXY axis, ProgressMonitor monitor) {
        return mathsInstanceBase.scalarProduct(g, f, axis, monitor);
    }

    public ExprList elist(int size) {
        return mathsInstanceBase.elist(size);
    }

    public ExprList elist(boolean row, int size) {
        return mathsInstanceBase.elist(row, size);
    }

    public ExprList elist(Expr... vector) {
        return mathsInstanceBase.elist(vector);
    }

    public ExprList elist(TVector<Complex> vector) {
        return mathsInstanceBase.elist(vector);
    }

    public TList<Complex> clist(TVector<Expr> vector) {
        return mathsInstanceBase.clist(vector);
    }

    public TList<Complex> clist() {
        return mathsInstanceBase.clist();
    }

    public TList<Complex> clist(int size) {
        return mathsInstanceBase.clist(size);
    }

    public TList<Complex> clist(Complex... vector) {
        return mathsInstanceBase.clist(vector);
    }

    public TList<Matrix> mlist() {
        return mathsInstanceBase.mlist();
    }

    public TList<Matrix> mlist(int size) {
        return mathsInstanceBase.mlist(size);
    }

    public TList<Matrix> mlist(Matrix... items) {
        return mathsInstanceBase.mlist(items);
    }

    public TList<TList<Complex>> clist2() {
        return mathsInstanceBase.clist2();
    }

    public TList<TList<Expr>> elist2() {
        return mathsInstanceBase.elist2();
    }

    public TList<TList<Double>> dlist2() {
        return mathsInstanceBase.dlist2();
    }

    public TList<TList<Integer>> ilist2() {
        return mathsInstanceBase.ilist2();
    }

    public TList<TList<Matrix>> mlist2() {
        return mathsInstanceBase.mlist2();
    }

    public TList<TList<Boolean>> blist2() {
        return mathsInstanceBase.blist2();
    }

    public <T> TList<T> list(TypeName<T> type) {
        return mathsInstanceBase.list(type);
    }

    public <T> TList<T> list(TypeName<T> type, int initialSize) {
        return mathsInstanceBase.list(type, initialSize);
    }

    public <T> TList<T> listro(TypeName<T> type, boolean row, TVectorModel<T> model) {
        return mathsInstanceBase.listro(type, row, model);
    }

    public <T> TList<T> list(TypeName<T> type, boolean row, int initialSize) {
        return mathsInstanceBase.list(type, row, initialSize);
    }

    public <T> TList<T> list(TVector<T> vector) {
        return mathsInstanceBase.list(vector);
    }

    public ExprList elist(Matrix vector) {
        return mathsInstanceBase.elist(vector);
    }

    public <T> TVector<T> vscalarProduct(TVector<T> vector, TVector<TVector<T>> tVectors) {
        return mathsInstanceBase.vscalarProduct(vector, tVectors);
    }

    public TList<Expr> elist() {
        return mathsInstanceBase.elist();
    }

    public <T> TList<T> concat(TList<T>... a) {
        return mathsInstanceBase.concat(a);
    }

    public TList<Double> dlist() {
        return mathsInstanceBase.dlist();
    }

    public TList<Double> dlist(ToDoubleArrayAware items) {
        return mathsInstanceBase.dlist(items);
    }

    public TList<Double> dlist(double[] items) {
        return mathsInstanceBase.dlist(items);
    }

    public TList<Double> dlist(boolean row, int size) {
        return mathsInstanceBase.dlist(row, size);
    }

    public TList<Double> dlist(int size) {
        return mathsInstanceBase.dlist(size);
    }

    public TList<String> slist() {
        return mathsInstanceBase.slist();
    }

    public TList<String> slist(String[] items) {
        return mathsInstanceBase.slist(items);
    }

    public TList<String> slist(boolean row, int size) {
        return mathsInstanceBase.slist(row, size);
    }

    public TList<String> slist(int size) {
        return mathsInstanceBase.slist(size);
    }

    public TList<Boolean> blist() {
        return mathsInstanceBase.blist();
    }

    public TList<Boolean> dlist(boolean[] items) {
        return mathsInstanceBase.dlist(items);
    }

    public TList<Boolean> blist(boolean row, int size) {
        return mathsInstanceBase.blist(row, size);
    }

    public TList<Boolean> blist(int size) {
        return mathsInstanceBase.blist(size);
    }

    public IntList ilist() {
        return mathsInstanceBase.ilist();
    }

    public TList<Integer> ilist(int[] items) {
        return mathsInstanceBase.ilist(items);
    }

    public TList<Integer> ilist(int size) {
        return mathsInstanceBase.ilist(size);
    }

    public TList<Integer> ilist(boolean row, int size) {
        return mathsInstanceBase.ilist(row, size);
    }

    public LongList llist() {
        return mathsInstanceBase.llist();
    }

    public TList<Long> llist(long[] items) {
        return mathsInstanceBase.llist(items);
    }

    public TList<Long> llist(int size) {
        return mathsInstanceBase.llist(size);
    }

    public TList<Long> llist(boolean row, int size) {
        return mathsInstanceBase.llist(row, size);
    }

    public <T> T sum(TypeName<T> type, T... arr) {
        return mathsInstanceBase.sum(type, arr);
    }

    public <T> T sum(TypeName<T> type, TVectorModel<T> arr) {
        return mathsInstanceBase.sum(type, arr);
    }

    public <T> T sum(TypeName<T> type, int size, TVectorCell<T> arr) {
        return mathsInstanceBase.sum(type, size, arr);
    }

    public <T> T mul(TypeName<T> type, T... arr) {
        return mathsInstanceBase.mul(type, arr);
    }

    public <T> T mul(TypeName<T> type, TVectorModel<T> arr) {
        return mathsInstanceBase.mul(type, arr);
    }

    public Complex avg(Discrete d) {
        return mathsInstanceBase.avg(d);
    }

    public DoubleToVector vsum(VDiscrete d) {
        return mathsInstanceBase.vsum(d);
    }

    public DoubleToVector vavg(VDiscrete d) {
        return mathsInstanceBase.vavg(d);
    }

    public Complex avg(VDiscrete d) {
        return mathsInstanceBase.avg(d);
    }

    public Expr sum(Expr... arr) {
        return mathsInstanceBase.sum(arr);
    }

    public Expr esum(TVectorModel<Expr> arr) {
        return mathsInstanceBase.esum(arr);
    }

    public <T> TMatrix<T> mul(TMatrix<T> a, TMatrix<T> b) {
        return mathsInstanceBase.mul(a, b);
    }

    public Matrix mul(Matrix a, Matrix b) {
        return mathsInstanceBase.mul(a, b);
    }

    public Expr mul(Expr a, Expr b) {
        return mathsInstanceBase.mul(a, b);
    }

    public TVector<Expr> edotmul(TVector<Expr>... arr) {
        return mathsInstanceBase.edotmul(arr);
    }

    public TVector<Expr> edotdiv(TVector<Expr>... arr) {
        return mathsInstanceBase.edotdiv(arr);
    }

    public Complex cmul(TVectorModel<Complex> arr) {
        return mathsInstanceBase.cmul(arr);
    }

    public Expr emul(TVectorModel<Expr> arr) {
        return mathsInstanceBase.emul(arr);
    }

    public Expr mul(Expr... e) {
        return mathsInstanceBase.mul(e);
    }

    public Expr pow(Expr a, Expr b) {
        return mathsInstanceBase.pow(a, b);
    }

    public Expr sub(Expr a, Expr b) {
        return mathsInstanceBase.sub(a, b);
    }

    public Expr add(Expr a, double b) {
        return mathsInstanceBase.add(a, b);
    }

    public Expr mul(Expr a, double b) {
        return mathsInstanceBase.mul(a, b);
    }

    public Expr sub(Expr a, double b) {
        return mathsInstanceBase.sub(a, b);
    }

    public Expr div(Expr a, double b) {
        return mathsInstanceBase.div(a, b);
    }

    public Expr add(Expr a, Expr b) {
        return mathsInstanceBase.add(a, b);
    }

    public Expr add(Expr... a) {
        return mathsInstanceBase.add(a);
    }

    public Expr div(Expr a, Expr b) {
        return mathsInstanceBase.div(a, b);
    }

    public Expr rem(Expr a, Expr b) {
        return mathsInstanceBase.rem(a, b);
    }

    public Expr expr(double value) {
        return mathsInstanceBase.expr(value);
    }

    public <T> TVector<Expr> expr(TVector<T> vector) {
        return mathsInstanceBase.expr(vector);
    }

    public TMatrix<Expr> expr(TMatrix<Complex> matrix) {
        return mathsInstanceBase.expr(matrix);
    }

    public <T> TMatrix<T> tmatrix(TypeName<T> type, TMatrixModel<T> model) {
        return mathsInstanceBase.tmatrix(type, model);
    }

    public <T> TMatrix<T> tmatrix(TypeName<T> type, int rows, int columns, TMatrixCell<T> model) {
        return mathsInstanceBase.tmatrix(type, rows, columns, model);
    }

    public <T> T simplify(T a) {
        return mathsInstanceBase.simplify(a);
    }

    public Expr simplify(Expr a) {
        return mathsInstanceBase.simplify(a);
    }

    public double norm(Expr a) {
        return mathsInstanceBase.norm(a);
    }

    public <T> TList<T> normalize(TList<T> a) {
        return mathsInstanceBase.normalize(a);
    }

    public Expr normalize(Geometry a) {
        return mathsInstanceBase.normalize(a);
    }

    public Expr normalize(Expr a) {
        return mathsInstanceBase.normalize(a);
    }

    public DoubleToVector vector(Expr fx, Expr fy) {
        return mathsInstanceBase.vector(fx, fy);
    }

    public DoubleToVector vector(Expr fx) {
        return mathsInstanceBase.vector(fx);
    }

    public DoubleToVector vector(Expr fx, Expr fy, Expr fz) {
        return mathsInstanceBase.vector(fx, fy, fz);
    }

    public <T> TVector<T> cos(TVector<T> a) {
        return mathsInstanceBase.cos(a);
    }

    public <T> TVector<T> cosh(TVector<T> a) {
        return mathsInstanceBase.cosh(a);
    }

    public <T> TVector<T> sin(TVector<T> a) {
        return mathsInstanceBase.sin(a);
    }

    public <T> TVector<T> sinh(TVector<T> a) {
        return mathsInstanceBase.sinh(a);
    }

    public <T> TVector<T> tan(TVector<T> a) {
        return mathsInstanceBase.tan(a);
    }

    public <T> TVector<T> tanh(TVector<T> a) {
        return mathsInstanceBase.tanh(a);
    }

    public <T> TVector<T> cotan(TVector<T> a) {
        return mathsInstanceBase.cotan(a);
    }

    public <T> TVector<T> cotanh(TVector<T> a) {
        return mathsInstanceBase.cotanh(a);
    }

    public <T> TVector<T> sqr(TVector<T> a) {
        return mathsInstanceBase.sqr(a);
    }

    public <T> TVector<T> sqrt(TVector<T> a) {
        return mathsInstanceBase.sqrt(a);
    }

    public <T> TVector<T> inv(TVector<T> a) {
        return mathsInstanceBase.inv(a);
    }

    public <T> TVector<T> neg(TVector<T> a) {
        return mathsInstanceBase.neg(a);
    }

    public <T> TVector<T> exp(TVector<T> a) {
        return mathsInstanceBase.exp(a);
    }

    public <T> TVector<T> simplify(TVector<T> a) {
        return mathsInstanceBase.simplify(a);
    }

    public <T> TList<T> simplify(TList<T> a) {
        return mathsInstanceBase.simplify(a);
    }

    public <T> TList<T> addAll(TList<T> e, T... expressions) {
        return mathsInstanceBase.addAll(e, expressions);
    }

    public <T> TList<T> mulAll(TList<T> e, T... expressions) {
        return mathsInstanceBase.mulAll(e, expressions);
    }

    public <T> TList<T> pow(TList<T> a, T b) {
        return mathsInstanceBase.pow(a, b);
    }

    public <T> TList<T> sub(TList<T> a, T b) {
        return mathsInstanceBase.sub(a, b);
    }

    public <T> TList<T> div(TList<T> a, T b) {
        return mathsInstanceBase.div(a, b);
    }

    public <T> TList<T> rem(TList<T> a, T b) {
        return mathsInstanceBase.rem(a, b);
    }

    public <T> TList<T> add(TList<T> a, T b) {
        return mathsInstanceBase.add(a, b);
    }

    public <T> TList<T> mul(TList<T> a, T b) {
        return mathsInstanceBase.mul(a, b);
    }

    public void loopOver(Object[][] values, LoopAction action) {
        mathsInstanceBase.loopOver(values, action);
    }

    public void loopOver(Loop[] values, LoopAction action) {
        mathsInstanceBase.loopOver(values, action);
    }

    public String formatMemory() {
        return mathsInstanceBase.formatMemory();
    }

    public String formatMetric(double value) {
        return mathsInstanceBase.formatMetric(value);
    }

    public MemoryInfo memoryInfo() {
        return mathsInstanceBase.memoryInfo();
    }

    public MemoryMeter memoryMeter() {
        return mathsInstanceBase.memoryMeter();
    }

    public long inUseMemory() {
        return mathsInstanceBase.inUseMemory();
    }

    public long maxFreeMemory() {
        return mathsInstanceBase.maxFreeMemory();
    }

    public String formatMemory(long bytes) {
        return mathsInstanceBase.formatMemory(bytes);
    }

    public String formatFrequency(double frequency) {
        return mathsInstanceBase.formatFrequency(frequency);
    }

    public String formatDimension(double dimension) {
        return mathsInstanceBase.formatDimension(dimension);
    }

    public String formatPeriodNanos(long period) {
        return mathsInstanceBase.formatPeriodNanos(period);
    }

    public String formatPeriodMillis(long period) {
        return mathsInstanceBase.formatPeriodMillis(period);
    }

    public int sizeOf(Class src) {
        return mathsInstanceBase.sizeOf(src);
    }

    public <T> T invokeMonitoredAction(ProgressMonitor mon, String messagePrefix, MonitoredAction<T> run) {
        return mathsInstanceBase.invokeMonitoredAction(mon, messagePrefix, run);
    }

    public Chronometer chrono() {
        return mathsInstanceBase.chrono();
    }

    public Chronometer chrono(String name) {
        return mathsInstanceBase.chrono(name);
    }

    public Chronometer chrono(String name, Runnable r) {
        return mathsInstanceBase.chrono(name, r);
    }

    public <V> V chrono(String name, Callable<V> r) {
        return mathsInstanceBase.chrono(name, r);
    }

    public SolverExecutorService solverExecutorService(int threads) {
        return mathsInstanceBase.solverExecutorService(threads);
    }

    public Chronometer chrono(Runnable r) {
        return mathsInstanceBase.chrono(r);
    }

    public DoubleFormat percentFormat() {
        return mathsInstanceBase.percentFormat();
    }

    public DoubleFormat frequencyFormat() {
        return mathsInstanceBase.frequencyFormat();
    }

    public DoubleFormat metricFormat() {
        return mathsInstanceBase.metricFormat();
    }

    public DoubleFormat memoryFormat() {
        return mathsInstanceBase.memoryFormat();
    }

    public DoubleFormat dblformat(String format) {
        return mathsInstanceBase.dblformat(format);
    }

    public double[] resizePickFirst(double[] array, int newSize) {
        return mathsInstanceBase.resizePickFirst(array, newSize);
    }

    public double[] resizePickAverage(double[] array, int newSize) {
        return mathsInstanceBase.resizePickAverage(array, newSize);
    }

    public <T> T[] toArray(Class<T> t, Collection<T> coll) {
        return mathsInstanceBase.toArray(t, coll);
    }

    public <T> T[] toArray(TypeName<T> t, Collection<T> coll) {
        return mathsInstanceBase.toArray(t, coll);
    }

    public double rerr(double a, double b) {
        return mathsInstanceBase.rerr(a, b);
    }

    public double rerr(Complex a, Complex b) {
        return mathsInstanceBase.rerr(a, b);
    }

    public CustomCCFunctionXDefinition define(String name, CustomCCFunctionX f) {
        return mathsInstanceBase.define(name, f);
    }

    public CustomDCFunctionXDefinition define(String name, CustomDCFunctionX f) {
        return mathsInstanceBase.define(name, f);
    }

    public CustomDDFunctionXDefinition define(String name, CustomDDFunctionX f) {
        return mathsInstanceBase.define(name, f);
    }

    public CustomDDFunctionXYDefinition define(String name, CustomDDFunctionXY f) {
        return mathsInstanceBase.define(name, f);
    }

    public CustomDCFunctionXYDefinition define(String name, CustomDCFunctionXY f) {
        return mathsInstanceBase.define(name, f);
    }

    public CustomCCFunctionXYDefinition define(String name, CustomCCFunctionXY f) {
        return mathsInstanceBase.define(name, f);
    }

    public double rerr(Matrix a, Matrix b) {
        return mathsInstanceBase.rerr(a, b);
    }

    public <T extends Expr> DoubleList toDoubleArray(TList<T> c) {
        return mathsInstanceBase.toDoubleArray(c);
    }

    public double toDouble(Complex c, PlotDoubleConverter d) {
        return mathsInstanceBase.toDouble(c, d);
    }

    public Expr conj(Expr e) {
        return mathsInstanceBase.conj(e);
    }

    public Complex complex(TMatrix t) {
        return mathsInstanceBase.complex(t);
    }

    public Matrix matrix(TMatrix t) {
        return mathsInstanceBase.matrix(t);
    }

    public TMatrix<Expr> ematrix(TMatrix t) {
        return mathsInstanceBase.ematrix(t);
    }

    public <T> VectorSpace<T> getVectorSpace(TypeName<T> cls) {
        return mathsInstanceBase.getVectorSpace(cls);
    }

    public DoubleList refineSamples(TList<Double> values, int n) {
        return mathsInstanceBase.refineSamples(values, n);
    }

    public double[] refineSamples(double[] values, int n) {
        return mathsInstanceBase.refineSamples(values, n);
    }

    public String getHadrumathsVersion() {
        return mathsInstanceBase.getHadrumathsVersion();
    }

    public ComponentDimension expandComponentDimension(ComponentDimension d1, ComponentDimension d2) {
        return mathsInstanceBase.expandComponentDimension(d1, d2);
    }

    public Expr expandComponentDimension(Expr e, ComponentDimension d) {
        return mathsInstanceBase.expandComponentDimension(e, d);
    }

    public double atan2(double y, double x) {
        return mathsInstanceBase.atan2(y, x);
    }

    public double ceil(double a) {
        return mathsInstanceBase.ceil(a);
    }

    public double floor(double a) {
        return mathsInstanceBase.floor(a);
    }

    public long round(double a) {
        return mathsInstanceBase.round(a);
    }

    public int round(float a) {
        return mathsInstanceBase.round(a);
    }

    public double random() {
        return mathsInstanceBase.random();
    }

    public <A, B> RightArrowUplet2<A, B> rightArrow(A a, B b) {
        return mathsInstanceBase.rightArrow(a, b);
    }

    public RightArrowUplet2.Double rightArrow(double a, double b) {
        return mathsInstanceBase.rightArrow(a, b);
    }

    public RightArrowUplet2.Complex rightArrow(Complex a, Complex b) {
        return mathsInstanceBase.rightArrow(a, b);
    }

    public RightArrowUplet2.Expr rightArrow(Expr a, Expr b) {
        return mathsInstanceBase.rightArrow(a, b);
    }

    public Expr parseExpression(String expression) {
        return mathsInstanceBase.parseExpression(expression);
    }

    public ExpressionManager createExpressionEvaluator() {
        return mathsInstanceBase.createExpressionEvaluator();
    }

    public ExpressionManager createExpressionParser() {
        return mathsInstanceBase.createExpressionParser();
    }

    public Expr evalExpression(String expression) {
        return mathsInstanceBase.evalExpression(expression);
    }

    public double toRadians(double a) {
        return mathsInstanceBase.toRadians(a);
    }

    public double[] toRadians(double[] a) {
        return mathsInstanceBase.toRadians(a);
    }

    public Complex det(Matrix m) {
        return mathsInstanceBase.det(m);
    }

    public int toInt(Object o) {
        return mathsInstanceBase.toInt(o);
    }

    public int toInt(Object o, Integer defaultValue) {
        return mathsInstanceBase.toInt(o, defaultValue);
    }

    public long toLong(Object o) {
        return mathsInstanceBase.toLong(o);
    }

    public long toLong(Object o, Long defaultValue) {
        return mathsInstanceBase.toLong(o, defaultValue);
    }

    public double toDouble(Object o) {
        return mathsInstanceBase.toDouble(o);
    }

    public double toDouble(Object o, Double defaultValue) {
        return mathsInstanceBase.toDouble(o, defaultValue);
    }

    public float toFloat(Object o) {
        return mathsInstanceBase.toFloat(o);
    }

    public float toFloat(Object o, Float defaultValue) {
        return mathsInstanceBase.toFloat(o, defaultValue);
    }

    public DoubleToComplex DC(Expr e) {
        return mathsInstanceBase.DC(e);
    }

    public DoubleToDouble DD(Expr e) {
        return mathsInstanceBase.DD(e);
    }

    public DoubleToVector DV(Expr e) {
        return mathsInstanceBase.DV(e);
    }

    public DoubleToMatrix DM(Expr e) {
        return mathsInstanceBase.DM(e);
    }

    public Matrix matrix(Expr e) {
        return mathsInstanceBase.matrix(e);
    }
}
