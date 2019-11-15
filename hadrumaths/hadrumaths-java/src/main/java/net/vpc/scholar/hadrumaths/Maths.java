package net.vpc.scholar.hadrumaths;

import java.io.UncheckedIOException;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.util.*;
import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.expeval.ExpressionManagerFactory;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.plot.*;
import net.vpc.scholar.hadrumaths.plot.filetypes.*;
import net.vpc.scholar.hadrumaths.scalarproducts.MatrixScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.MemComplexScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.MemDoubleScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;
import net.vpc.scholar.hadruplot.*;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;
import net.vpc.scholar.hadruplot.console.params.*;
import net.vpc.scholar.hadruplot.filetypes.PlotFileTypeJpeg;
import net.vpc.scholar.hadruplot.filetypes.PlotFileTypePng;
import sun.misc.Unsafe;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Maths {

    //<editor-fold desc="constants functions">
    public static  final double PI = Math.PI;
    public static  final double E = Math.E;
    public static  final DoubleToDouble DDZERO = MathsBase.DDZERO;
    public static  final DoubleToDouble DDNAN = MathsBase.DDNAN;
    public static  final DoubleToComplex DCZERO = MathsBase.DCZERO;
    public static  final DoubleToVector DVZERO3 = MathsBase.DVZERO3;
    public static  final Expr EZERO = MathsBase.EZERO;
    public static  final Expr EONE = MathsBase.EONE;
    public static  final Expr X = MathsBase.X;
    public static  final Expr Y = MathsBase.Y;
    public static  final Expr Z = MathsBase.Z;
    public static  final double HALF_PI = MathsBase.HALF_PI;
    public static  final Complex I = MathsBase.I;
    public static  final Complex CNaN = MathsBase.CNaN;
    public static  final Complex CONE = MathsBase.CONE;
    public static  final Complex CZERO = MathsBase.CZERO;//    public static  boolean DEBUG = true;
    public static  final DoubleToVector DVZERO1 = MathsBase.DVZERO1;
    public static  final DoubleToVector DVZERO2 = MathsBase.DVZERO2;
    public static  final Complex î = MathsBase.î;
    public static  final Expr ê = MathsBase.ê;
    public static  final Complex ĉ = MathsBase.ĉ;
    public static  final double METER = MathsBase.METER;
    public static  final double HZ = MathsBase.HZ;
    public static  final long BYTE = MathsBase.BYTE;
    public static  final long MILLISECOND = MathsBase.MILLISECOND;
    /**
     * kibibyte
     */
    public static  final int KiBYTE = MathsBase.KiBYTE;
    /**
     * mibibyte
     */
    public static  final int MiBYTE = MathsBase.MiBYTE;
    /**
     * TEBI Byte
     */
    public static  final long GiBYTE = MathsBase.GiBYTE;
    /**
     * TEBI Byte
     */
    public static  final long TiBYTE = MathsBase.TiBYTE;
    /**
     * PEBI Byte
     */
    public static  final long PiBYTE = MathsBase.PiBYTE;
    /**
     * exbibyte
     */
    public static  final long EiBYTE = MathsBase.EiBYTE;

    public static  final double YOCTO = MathsBase.YOCTO;
    public static  final double ZEPTO = MathsBase.ZEPTO;
    public static  final double ATTO = MathsBase.ATTO;
    public static  final double FEMTO = MathsBase.FEMTO;
    public static  final double PICO = MathsBase.PICO;
    public static  final double NANO = MathsBase.NANO;
    public static  final double MICRO = MathsBase.MICRO;
    public static  final double MILLI = MathsBase.MILLI;
    public static  final double CENTI = MathsBase.CENTI;
    public static  final double DECI = MathsBase.DECI;
    /**
     * DECA
     */
    public static  final int DECA = MathsBase.DECA;
    /**
     * HECTO
     */
    public static  final int HECTO = MathsBase.HECTO;

    /**
     * KILO
     */
    public static  final int KILO = MathsBase.KILO;
    /**
     * MEGA
     */
    public static  final int MEGA = MathsBase.MEGA;
    /**
     * MEGA
     */
    public static  final long GIGA = MathsBase.GIGA;
    /**
     * TERA
     */
    public static  final long TERA = MathsBase.TERA;
    /**
     * PETA
     */
    public static  final long PETA = MathsBase.PETA;
    /**
     * EXA
     */
    public static  final long EXA = MathsBase.EXA;
    /**
     * ZETTA
     */
    public static  final long ZETTA = MathsBase.ZETTA;
    /**
     * YOTTA
     */
    public static  final long YOTTA = MathsBase.YOTTA;
    public static  final long SECOND = MathsBase.SECOND;
    public static  final long MINUTE = MathsBase.MINUTE;
    public static  final long HOUR = MathsBase.HOUR;
    public static  final long DAY = MathsBase.DAY;
    public static  final double KHZ = MathsBase.KHZ;
    public static  final double MHZ = MathsBase.MHZ;
    public static  final double GHZ = MathsBase.GHZ;
    public static  final double MILLIMETER = MathsBase.MILLIMETER;
    public static  final double MM = MathsBase.MM;
    public static  final double CM = MathsBase.CM;
    public static  final double CENTIMETER = MathsBase.CENTIMETER;
    /**
     * light celerity. speed of light in vacuum
     */
//    public static  final int C = 300000000;
    public static  final int C = MathsBase.C;//m.s^-1
    /**
     * Newtonian constant of gravitation
     */
    public static  final double G = MathsBase.G; //m3·kg^−1·s^−2;
    /**
     * Planck constant
     */
    public static  final double H = MathsBase.H; //J·s;
    /**
     * Reduced Planck constant
     */
    public static  final double Hr = MathsBase.Hr; //J·s;
    /**
     * magnetic constant (vacuum permeability)
     */
    public static  final double U0 = MathsBase.U0; //N·A−2
    /**
     * electric constant (vacuum permittivity) =1/(u0*C^2)
     */
    public static  final double EPS0 = MathsBase.EPS0;//F·m−1
    /**
     * characteristic impedance of vacuum =1/(u0*C)
     */
    public static  final double Z0 = MathsBase.Z0;//F·m−1
    /**
     * Coulomb's constant
     */
    public static  final double Ke = MathsBase.Ke;//F·m−1
    /**
     * elementary charge
     */
    public static  final double Qe = MathsBase.Qe;//C
    public static  final VectorSpace<Complex> COMPLEX_VECTOR_SPACE = MathsBase.COMPLEX_VECTOR_SPACE;
    public static  final VectorSpace<Expr> EXPR_VECTOR_SPACE = MathsBase.EXPR_VECTOR_SPACE;
    public static  final VectorSpace<Double> DOUBLE_VECTOR_SPACE = MathsBase.DOUBLE_VECTOR_SPACE;
    public static  final int X_AXIS = MathsBase.X_AXIS;
    public static  final int Y_AXIS = MathsBase.Y_AXIS;
    public static  final int Z_AXIS = MathsBase.Z_AXIS;
    public static  final TStoreManager<Matrix> MATRIX_STORE_MANAGER = MathsBase.MATRIX_STORE_MANAGER;
    public static  final TStoreManager<TMatrix> TMATRIX_STORE_MANAGER = MathsBase.TMATRIX_STORE_MANAGER;

    public static  final TStoreManager<TVector> TVECTOR_STORE_MANAGER = MathsBase.TVECTOR_STORE_MANAGER;

    public static  final TStoreManager<Vector> VECTOR_STORE_MANAGER = MathsBase.VECTOR_STORE_MANAGER;
    public static  final Converter IDENTITY = MathsBase.IDENTITY;
    public static  final Converter<Complex, Double> COMPLEX_TO_DOUBLE = MathsBase.COMPLEX_TO_DOUBLE;
    public static  final Converter<Double, Complex> DOUBLE_TO_COMPLEX = MathsBase.DOUBLE_TO_COMPLEX;
    public static  final Converter<Double, TVector> DOUBLE_TO_TVECTOR = MathsBase.DOUBLE_TO_TVECTOR;
    public static  final Converter<TVector, Double> TVECTOR_TO_DOUBLE = MathsBase.TVECTOR_TO_DOUBLE;
    public static  final Converter<Complex, TVector> COMPLEX_TO_TVECTOR = MathsBase.COMPLEX_TO_TVECTOR;
    public static  final Converter<TVector, Complex> TVECTOR_TO_COMPLEX = MathsBase.TVECTOR_TO_COMPLEX;
    public static  final Converter<Complex, Expr> COMPLEX_TO_EXPR = MathsBase.COMPLEX_TO_EXPR;
    public static  final Converter<Expr, Complex> EXPR_TO_COMPLEX = MathsBase.EXPR_TO_COMPLEX;
    public static  final Converter<Double, Expr> DOUBLE_TO_EXPR = MathsBase.DOUBLE_TO_EXPR;
    public static  final Converter<Expr, Double> EXPR_TO_DOUBLE = MathsBase.EXPR_TO_DOUBLE;
    //    public static  String getAxisLabel(int axis){
//        switch(axis){
//            case X_AXIS:return "X";
//            case Y_AXIS:return "Y";
//            case Z_AXIS:return "Z";
//        }
//        throw new IllegalArgumentException("Unknown Axis "+axis);
//    }
    public static  final TypeName<String> $STRING = MathsBase.$STRING;
    public static  final TypeName<Complex> $COMPLEX = MathsBase.$COMPLEX;
    public static  final TypeName<Matrix> $MATRIX = MathsBase.$MATRIX;
    public static  final TypeName<Vector> $VECTOR = MathsBase.$VECTOR;
    public static  final TypeName<TMatrix<Complex>> $CMATRIX = MathsBase.$CMATRIX;
    public static  final TypeName<TVector<Complex>> $CVECTOR = MathsBase.$CVECTOR;
    public static  final TypeName<Double> $DOUBLE = MathsBase.$DOUBLE;
    public static  final TypeName<Boolean> $BOOLEAN = MathsBase.$BOOLEAN;
    public static  final TypeName<Point> $POINT = MathsBase.$POINT;
    public static  final TypeName<File> $FILE = MathsBase.$FILE;
    //</editor-fold>
    public static  final TypeName<Integer> $INTEGER = MathsBase.$INTEGER;
    public static  final TypeName<Long> $LONG = MathsBase.$LONG;
    public static  final TypeName<Expr> $EXPR = MathsBase.$EXPR;
    public static  final TypeName<TList<Complex>> $CLIST = MathsBase.$CLIST;
    public static  final TypeName<TList<Expr>> $ELIST = MathsBase.$ELIST;
    public static  final TypeName<TList<Double>> $DLIST = MathsBase.$DLIST;
    public static  final TypeName<TList<TList<Double>>> $DLIST2 = MathsBase.$DLIST2;
    public static  final TypeName<TList<Integer>> $ILIST = MathsBase.$ILIST;
    public static  final TypeName<TList<Boolean>> $BLIST = MathsBase.$BLIST;
    public static  final TypeName<TList<Matrix>> $MLIST = MathsBase.$MLIST;
    public static  final MathsConfig Config = MathsBase.Config;
    public static  DistanceStrategy<Double> DISTANCE_DOUBLE = MathsBase.DISTANCE_DOUBLE;
    public static  DistanceStrategy<Complex> DISTANCE_COMPLEX = MathsBase.DISTANCE_COMPLEX;
    public static  DistanceStrategy<Matrix> DISTANCE_MATRIX = MathsBase.DISTANCE_MATRIX;
    public static  DistanceStrategy<Vector> DISTANCE_VECTOR = MathsBase.DISTANCE_VECTOR;



    public static  Domain xdomain(double min, double max) {
        return  MathsBase.xdomain(min, max);
    }

    public static  Domain ydomain(double min, double max) {
        return  MathsBase.ydomain(min, max);
    }

    public static  DomainExpr ydomain(DomainExpr min, DomainExpr max) {
        return  MathsBase.ydomain(min, max);
    }

    public static  Domain zdomain(double min, double max) {
        return  MathsBase.zdomain(min, max);
    }

    public static  DomainExpr zdomain(Expr min, Expr max) {
        return  MathsBase.zdomain(min, max);
    }

    public static  Domain domain(RightArrowUplet2.Double u) {
        return  MathsBase.domain(u);
    }

    public static  Domain domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return  MathsBase.domain(ux, uy);
    }

    public static  Domain domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return  MathsBase.domain(ux, uy, uz);
    }

    public static  Expr domain(RightArrowUplet2.Expr u) {
        return  MathsBase.domain(u);
    }

    public static  Expr domain(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy) {
        return  MathsBase.domain(ux, uy);
    }

    public static  Expr domain(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy, RightArrowUplet2.Expr uz) {
        return  MathsBase.domain(ux, uy, uz);
    }

    public static  DomainExpr domain(Expr min, Expr max) {
        return  MathsBase.domain(min, max);
    }

    public static  Domain domain(double min, double max) {
        return  MathsBase.domain(min, max);
    }

    public static  Domain domain(double xmin, double xmax, double ymin, double ymax) {
        return  MathsBase.domain(xmin, xmax, ymin, ymax);
    }

    public static  DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return  MathsBase.domain(xmin, xmax, ymin, ymax);
    }

    public static  Domain domain(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return  MathsBase.domain(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static  DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return  MathsBase.domain(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static  Domain II(RightArrowUplet2.Double u) {
        return  MathsBase.II(u);
    }

    public static  Domain II(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return  MathsBase.II(ux, uy);
    }

    public static  Domain II(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return  MathsBase.II(ux, uy, uz);
    }

    public static  Expr II(RightArrowUplet2.Expr u) {
        return  MathsBase.II(u);
    }

    public static  Expr II(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy) {
        return  MathsBase.II(ux, uy);
    }

    public static  Expr II(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy, RightArrowUplet2.Expr uz) {
        return  MathsBase.II(ux, uy, uz);
    }

    public static DomainExpr II(Expr min, Expr max) {
        return  MathsBase.II(min, max);
    }

    public static Domain II(double min, double max) {
        return  MathsBase.II(min, max);
    }

    public static Domain II(double xmin, double xmax, double ymin, double ymax) {
        return  MathsBase.II(xmin, xmax, ymin, ymax);
    }

    public static DomainExpr II(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return  MathsBase.II(xmin, xmax, ymin, ymax);
    }

    public static Domain II(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return  MathsBase.II(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static DomainExpr II(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return  MathsBase.II(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static DoubleParam param(String name) {
        return  MathsBase.param(name);
    }

    public static DoubleArrayParamSet doubleParamSet(Param param) {
        return  MathsBase.doubleParamSet(param);
    }

    public static DoubleArrayParamSet paramSet(Param param, double[] values) {
        return  MathsBase.paramSet(param, values);
    }

    public static FloatArrayParamSet paramSet(Param param, float[] values) {
        return  MathsBase.paramSet(param, values);
    }

    public static FloatArrayParamSet floatParamSet(Param param) {
        return  MathsBase.floatParamSet(param);
    }

    public static LongArrayParamSet paramSet(Param param, long[] values) {
        return  MathsBase.paramSet(param, values);
    }

    public static LongArrayParamSet longParamSet(Param param) {
        return  MathsBase.longParamSet(param);
    }

    public static <T> ArrayParamSet<T> paramSet(Param param, T[] values) {
        return  MathsBase.paramSet(param, values);
    }

    public static <T> ArrayParamSet<T> objectParamSet(Param param) {
        return  MathsBase.objectParamSet(param);
    }

    public static IntArrayParamSet paramSet(Param param, int[] values) {
        return  MathsBase.paramSet(param, values);
    }

    public static IntArrayParamSet intParamSet(Param param) {
        return  MathsBase.intParamSet(param);
    }

    public static BooleanArrayParamSet paramSet(Param param, boolean[] values) {
        return  MathsBase.paramSet(param, values);
    }

    public static BooleanArrayParamSet boolParamSet(Param param) {
        return  MathsBase.boolParamSet(param);
    }

    public static XParamSet xParamSet(int xsamples) {
        return  MathsBase.xParamSet(xsamples);
    }

    public static XParamSet xyParamSet(int xsamples, int ysamples) {
        return  MathsBase.xyParamSet(xsamples, ysamples);
    }

    public static XParamSet xyzParamSet(int xsamples, int ysamples, int zsamples) {
        return  MathsBase.xyzParamSet(xsamples, ysamples, zsamples);
    }

    public static Matrix zerosMatrix(Matrix other) {
        return  MathsBase.zerosMatrix(other);
    }

    public static Matrix constantMatrix(int dim, Complex value) {
        return  MathsBase.constantMatrix(dim, value);
    }

    public static Matrix onesMatrix(int dim) {
        return  MathsBase.onesMatrix(dim);
    }

    public static Matrix onesMatrix(int rows, int cols) {
        return  MathsBase.onesMatrix(rows, cols);
    }

    public static Matrix constantMatrix(int rows, int cols, Complex value) {
        return  MathsBase.constantMatrix(rows, cols, value);
    }

    public static Matrix zerosMatrix(int dim) {
        return  MathsBase.zerosMatrix(dim);
    }

    public static Matrix I(Complex[][] iValue) {
        return  MathsBase.I(iValue);
    }

    public static Matrix zerosMatrix(int rows, int cols) {
        return  MathsBase.zerosMatrix(rows, cols);
    }

    public static Matrix identityMatrix(Matrix c) {
        return  MathsBase.identityMatrix(c);
    }

    public static Matrix NaNMatrix(int dim) {
        return  MathsBase.NaNMatrix(dim);
    }

    public static Matrix NaNMatrix(int rows, int cols) {
        return  MathsBase.NaNMatrix(rows, cols);
    }

    public static Matrix identityMatrix(int dim) {
        return  MathsBase.identityMatrix(dim);
    }

    public static Matrix identityMatrix(int rows, int cols) {
        return  MathsBase.identityMatrix(rows, cols);
    }

    public static Matrix matrix(Matrix matrix) {
        return  MathsBase.matrix(matrix);
    }

    public static Matrix matrix(String string) {
        return  MathsBase.matrix(string);
    }

    public static Matrix matrix(Complex[][] complex) {
        return  MathsBase.matrix(complex);
    }

    public static Matrix matrix(double[][] complex) {
        return  MathsBase.matrix(complex);
    }

    public static Matrix matrix(int rows, int cols, MatrixCell cellFactory) {
        return  MathsBase.matrix(rows, cols, cellFactory);
    }

    public static Matrix columnMatrix(final Complex... values) {
        return  MathsBase.columnMatrix(values);
    }

    public static Matrix columnMatrix(final double... values) {
        return  MathsBase.columnMatrix(values);
    }

    public static Matrix rowMatrix(final double... values) {
        return  MathsBase.rowMatrix(values);
    }

    public static Matrix rowMatrix(final Complex... values) {
        return  MathsBase.rowMatrix(values);
    }

    public static Matrix columnMatrix(int rows, final VectorCell cellFactory) {
        return  MathsBase.columnMatrix(rows, cellFactory);
    }

    public static Matrix rowMatrix(int columns, final VectorCell cellFactory) {
        return  MathsBase.rowMatrix(columns, cellFactory);
    }

    public static Matrix symmetricMatrix(int rows, int cols, MatrixCell cellFactory) {
        return  MathsBase.symmetricMatrix(rows, cols, cellFactory);
    }

    public static Matrix hermitianMatrix(int rows, int cols, MatrixCell cellFactory) {
        return  MathsBase.hermitianMatrix(rows, cols, cellFactory);
    }

    public static Matrix diagonalMatrix(int rows, int cols, MatrixCell cellFactory) {
        return  MathsBase.diagonalMatrix(rows, cols, cellFactory);
    }

    public static Matrix diagonalMatrix(int rows, final VectorCell cellFactory) {
        return  MathsBase.diagonalMatrix(rows, cellFactory);
    }

    public static Matrix diagonalMatrix(final Complex... c) {
        return  MathsBase.diagonalMatrix(c);
    }

    public static Matrix matrix(int dim, MatrixCell cellFactory) {
        return  MathsBase.matrix(dim, cellFactory);
    }

    public static Matrix matrix(int rows, int columns) {
        return  MathsBase.matrix(rows, columns);
    }

    public static Matrix symmetricMatrix(int dim, MatrixCell cellFactory) {
        return  MathsBase.symmetricMatrix(dim, cellFactory);
    }

    public static Matrix hermitianMatrix(int dim, MatrixCell cellFactory) {
        return  MathsBase.hermitianMatrix(dim, cellFactory);
    }

    public static Matrix diagonalMatrix(int dim, MatrixCell cellFactory) {
        return  MathsBase.diagonalMatrix(dim, cellFactory);
    }

    public static Matrix randomRealMatrix(int m, int n) {
        return  MathsBase.randomRealMatrix(m, n);
    }

    public static Matrix randomRealMatrix(int m, int n, int min, int max) {
        return  MathsBase.randomRealMatrix(m, n, min, max);
    }

    public static Matrix randomRealMatrix(int m, int n, double min, double max) {
        return  MathsBase.randomRealMatrix(m, n, min, max);
    }

    public static Matrix randomImagMatrix(int m, int n, double min, double max) {
        return  MathsBase.randomImagMatrix(m, n, min, max);
    }

    public static Matrix randomImagMatrix(int m, int n, int min, int max) {
        return  MathsBase.randomImagMatrix(m, n, min, max);
    }

    public static Matrix randomMatrix(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        return  MathsBase.randomMatrix(m, n, minReal, maxReal, minImag, maxImag);
    }

    public static Matrix randomMatrix(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        return  MathsBase.randomMatrix(m, n, minReal, maxReal, minImag, maxImag);
    }

    public static Matrix randomMatrix(int m, int n, double min, double max) {
        return  MathsBase.randomMatrix(m, n, min, max);
    }

    public static Matrix randomMatrix(int m, int n, int min, int max) {
        return  MathsBase.randomMatrix(m, n, min, max);
    }

    public static Matrix randomImagMatrix(int m, int n) {
        return  MathsBase.randomImagMatrix(m, n);
    }

    public static <T> TMatrix<T> loadTMatrix(TypeName<T> componentType, File file) throws UncheckedIOException {
        return  MathsBase.loadTMatrix(componentType, file);
    }

    public static Matrix loadMatrix(File file) throws UncheckedIOException {
        return  MathsBase.loadMatrix(file);
    }

    public static Matrix matrix(File file) throws UncheckedIOException {
        return  MathsBase.matrix(file);
    }

    public static void storeMatrix(Matrix m, String file) throws UncheckedIOException {
         MathsBase.storeMatrix(m, file);
    }

    public static void storeMatrix(Matrix m, File file) throws UncheckedIOException {
         MathsBase.storeMatrix(m, file);
    }

    public static Matrix loadOrEvalMatrix(String file, TItem<Matrix> item) throws UncheckedIOException {
        return  MathsBase.loadOrEvalMatrix(file, item);
    }

    public static Vector loadOrEvalVector(String file, TItem<TVector<Complex>> item) throws UncheckedIOException {
        return  MathsBase.loadOrEvalVector(file, item);
    }

    public static Matrix loadOrEvalMatrix(File file, TItem<Matrix> item) throws UncheckedIOException {
        return  MathsBase.loadOrEvalMatrix(file, item);
    }

    public static Vector loadOrEvalVector(File file, TItem<TVector<Complex>> item) throws UncheckedIOException {
        return  MathsBase.loadOrEvalVector(file, item);
    }

    public static <T> TMatrix loadOrEvalTMatrix(String file, TItem<TMatrix<T>> item) throws UncheckedIOException {
        return  MathsBase.loadOrEvalTMatrix(file, item);
    }

    public static <T> TVector<T> loadOrEvalTVector(String file, TItem<TVector<T>> item) throws UncheckedIOException {
        return  MathsBase.loadOrEvalTVector(file, item);
    }

    public static <T> TMatrix<T> loadOrEvalTMatrix(File file, TItem<TMatrix<T>> item) throws UncheckedIOException {
        return  MathsBase.loadOrEvalTMatrix(file, item);
    }

    public static <T> TVector loadOrEvalTVector(File file, TItem<TVector<T>> item) throws UncheckedIOException {
        return  MathsBase.loadOrEvalTVector(file, item);
    }

    public static <T> T loadOrEval(TypeName<T> type, File file, TItem<T> item) throws UncheckedIOException {
        return  MathsBase.loadOrEval(type, file, item);
    }

    public static Matrix loadMatrix(String file) throws UncheckedIOException {
        return  MathsBase.loadMatrix(file);
    }

    public static Matrix inv(Matrix c) {
        return  MathsBase.inv(c);
    }

    public static Matrix tr(Matrix c) {
        return  MathsBase.tr(c);
    }

    public static Matrix trh(Matrix c) {
        return  MathsBase.trh(c);
    }

    public static Matrix transpose(Matrix c) {
        return  MathsBase.transpose(c);
    }

    public static Matrix transposeHermitian(Matrix c) {
        return  MathsBase.transposeHermitian(c);
    }

    public static Vector rowVector(Complex[] elems) {
        return  MathsBase.rowVector(elems);
    }

    public static Vector constantColumnVector(int size, Complex c) {
        return  MathsBase.constantColumnVector(size, c);
    }

    public static Vector constantRowVector(int size, Complex c) {
        return  MathsBase.constantRowVector(size, c);
    }

    public static Vector zerosVector(int size) {
        return  MathsBase.zerosVector(size);
    }

    public static Vector zerosColumnVector(int size) {
        return  MathsBase.zerosColumnVector(size);
    }

    public static Vector zerosRowVector(int size) {
        return  MathsBase.zerosRowVector(size);
    }

    public static Vector NaNColumnVector(int dim) {
        return  MathsBase.NaNColumnVector(dim);
    }

    public static Vector NaNRowVector(int dim) {
        return  MathsBase.NaNRowVector(dim);
    }

    public static TVector<Expr> columnVector(Expr[] expr) {
        return  MathsBase.columnVector(expr);
    }

    public static TVector<Expr> rowVector(Expr[] expr) {
        return  MathsBase.rowVector(expr);
    }

    public static TVector<Expr> columnEVector(int rows, final TVectorCell<Expr> cellFactory) {
        return  MathsBase.columnEVector(rows, cellFactory);
    }

    public static TVector<Expr> rowEVector(int rows, final TVectorCell<Expr> cellFactory) {
        return  MathsBase.rowEVector(rows, cellFactory);
    }

    public static <T> TVector<T> updatableOf(TVector<T> vector) {
        return  MathsBase.updatableOf(vector);
    }

    public static Complex[][] copyOf(Complex[][] val) {
        return  MathsBase.copyOf(val);
    }

    public static Complex[] copyOf(Complex[] val) {
        return  MathsBase.copyOf(val);
    }

    public static <T> TList<T> copyOf(TVector<T> vector) {
        return  MathsBase.copyOf(vector);
    }

    public static <T> TVector<T> columnTVector(TypeName<T> cls, final TVectorModel<T> cellFactory) {
        return  MathsBase.columnTVector(cls, cellFactory);
    }

    public static <T> TVector<T> rowTVector(TypeName<T> cls, final TVectorModel<T> cellFactory) {
        return  MathsBase.rowTVector(cls, cellFactory);
    }

    public static <T> TVector<T> columnTVector(TypeName<T> cls, int rows, final TVectorCell<T> cellFactory) {
        return  MathsBase.columnTVector(cls, rows, cellFactory);
    }

    public static <T> TVector<T> rowTVector(TypeName<T> cls, int rows, final TVectorCell<T> cellFactory) {
        return  MathsBase.rowTVector(cls, rows, cellFactory);
    }

    public static Vector columnVector(int rows, final VectorCell cellFactory) {
        return  MathsBase.columnVector(rows, cellFactory);
    }

    public static Vector rowVector(int columns, final VectorCell cellFactory) {
        return  MathsBase.rowVector(columns, cellFactory);
    }

    public static Vector columnVector(Complex... elems) {
        return  MathsBase.columnVector(elems);
    }

    public static Vector columnVector(double[] elems) {
        return  MathsBase.columnVector(elems);
    }

    public static Vector rowVector(double[] elems) {
        return  MathsBase.rowVector(elems);
    }

    public static Vector column(Complex[] elems) {
        return  MathsBase.column(elems);
    }

    public static Vector row(Complex[] elems) {
        return  MathsBase.row(elems);
    }

    public static Vector trh(Vector c) {
        return  MathsBase.trh(c);
    }

    public static Vector tr(Vector c) {
        return  MathsBase.tr(c);
    }

    public static Complex I(double iValue) {
        return  MathsBase.I(iValue);
    }

    public static Complex abs(Complex a) {
        return  MathsBase.abs(a);
    }

    public static double absdbl(Complex a) {
        return  MathsBase.absdbl(a);
    }

    public static double[] getColumn(double[][] a, int index) {
        return  MathsBase.getColumn(a, index);
    }

    public static double[] dtimes(double min, double max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return  MathsBase.dtimes(min, max, times, maxTimes, strategy);
    }

    public static double[] dtimes(double min, double max, int times) {
        return  MathsBase.dtimes(min, max, times);
    }

    public static float[] ftimes(float min, float max, int times) {
        return  MathsBase.ftimes(min, max, times);
    }

    public static long[] ltimes(long min, long max, int times) {
        return  MathsBase.ltimes(min, max, times);
    }

    public static long[] lsteps(long min, long max, long step) {
        return  MathsBase.lsteps(min, max, step);
    }

    public static int[] itimes(int min, int max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return  MathsBase.itimes(min, max, times, maxTimes, strategy);
    }

    public static double[] dsteps(int max) {
        return  MathsBase.dsteps(max);
    }

    public static double[] dsteps(double min, double max) {
        return  MathsBase.dsteps(min, max);
    }

    public static double dstepsLength(double min, double max, double step) {
        return  MathsBase.dstepsLength(min, max, step);
    }

    public static double dstepsElement(double min, double max, double step, int index) {
        return  MathsBase.dstepsElement(min, max, step, index);
    }

    public static double[] dsteps(double min, double max, double step) {
        return  MathsBase.dsteps(min, max, step);
    }

    public static float[] fsteps(float min, float max, float step) {
        return  MathsBase.fsteps(min, max, step);
    }

    public static int[] isteps(int min, int max, int step) {
        return  MathsBase.isteps(min, max, step);
    }

    public static int[] isteps(int min, int max, int step, IntFilter filter) {
        return  MathsBase.isteps(min, max, step, filter);
    }

    public static int[] itimes(int min, int max, int times) {
        return  MathsBase.itimes(min, max, times);
    }

    public static int[] isteps(int max) {
        return  MathsBase.isteps(max);
    }

    public static int[] isteps(int min, int max) {
        return  MathsBase.isteps(min, max);
    }

    public static int[] itimes(int min, int max) {
        return  MathsBase.itimes(min, max);
    }

    public static double hypot(double a, double b) {
        return  MathsBase.hypot(a, b);
    }

    public static Complex sqr(Complex d) {
        return  MathsBase.sqr(d);
    }

    public static double sqr(double d) {
        return  MathsBase.sqr(d);
    }

    public static int sqr(int d) {
        return  MathsBase.sqr(d);
    }

    public static long sqr(long d) {
        return  MathsBase.sqr(d);
    }

    public static float sqr(float d) {
        return  MathsBase.sqr(d);
    }

    public static double sincard(double value) {
        return  MathsBase.sincard(value);
    }

    public static int minusOnePower(int pow) {
        return  MathsBase.minusOnePower(pow);
    }

    public static Complex exp(Complex c) {
        return  MathsBase.exp(c);
    }

    public static Complex sin(Complex c) {
        return  MathsBase.sin(c);
    }

    public static Complex sinh(Complex c) {
        return  MathsBase.sinh(c);
    }

    public static Complex cos(Complex c) {
        return  MathsBase.cos(c);
    }

    public static Complex log(Complex c) {
        return  MathsBase.log(c);
    }

    public static Complex log10(Complex c) {
        return  MathsBase.log10(c);
    }

    public static double log10(double c) {
        return  MathsBase.log10(c);
    }

    public static double log(double c) {
        return  MathsBase.log(c);
    }

    public static double acotan(double c) {
        return  MathsBase.acotan(c);
    }

    public static double exp(double c) {
        return  MathsBase.exp(c);
    }

    public static double arg(double c) {
        return  MathsBase.arg(c);
    }

    public static Complex db(Complex c) {
        return  MathsBase.db(c);
    }

    public static Complex db2(Complex c) {
        return  MathsBase.db2(c);
    }

    public static Complex cosh(Complex c) {
        return  MathsBase.cosh(c);
    }

    public static Complex csum(Complex... c) {
        return  MathsBase.csum(c);
    }

    public static Complex sum(Complex... c) {
        return  MathsBase.sum(c);
    }

    public static Complex csum(TVectorModel<Complex> c) {
        return  MathsBase.csum(c);
    }

    public static Complex csum(int size, TVectorCell<Complex> c) {
        return  MathsBase.csum(size, c);
    }

    public static double chbevl(double x, double[] coef, int N) throws ArithmeticException {
        return  MathsBase.chbevl(x, coef, N);
    }

    public static long pgcd(long a, long b) {
        return  MathsBase.pgcd(a, b);
    }

    public static int pgcd(int a, int b) {
        return  MathsBase.pgcd(a, b);
    }

    public static double[][] toDouble(Complex[][] c, PlotDoubleConverter toDoubleConverter) {
        return  MathsBase.toDouble(c, toDoubleConverter);
    }

    public static double[] toDouble(Complex[] c, PlotDoubleConverter toDoubleConverter) {
        return  MathsBase.toDouble(c, toDoubleConverter);
    }

    public static int[] rangeCC(double[] orderedValues, double min, double max) {
        return  MathsBase.rangeCC(orderedValues, min, max);
    }

    public static int[] rangeCO(double[] orderedValues, double min, double max) {
        return  MathsBase.rangeCO(orderedValues, min, max);
    }

    public static Complex csqrt(double d) {
        return  MathsBase.csqrt(d);
    }

    public static Complex sqrt(Complex d) {
        return  MathsBase.sqrt(d);
    }

    public static double dsqrt(double d) {
        return  MathsBase.dsqrt(d);
    }

    public static Complex cotanh(Complex c) {
        return  MathsBase.cotanh(c);
    }

    public static Complex tanh(Complex c) {
        return  MathsBase.tanh(c);
    }

    public static Complex inv(Complex c) {
        return  MathsBase.inv(c);
    }

    public static Complex tan(Complex c) {
        return  MathsBase.tan(c);
    }

    public static Complex cotan(Complex c) {
        return  MathsBase.cotan(c);
    }

    public static Vector vector(TVector v) {
        return  MathsBase.vector(v);
    }

    public static Complex pow(Complex a, Complex b) {
        return  MathsBase.pow(a, b);
    }

    public static Complex div(Complex a, Complex b) {
        return  MathsBase.div(a, b);
    }

    public static Complex add(Complex a, Complex b) {
        return  MathsBase.add(a, b);
    }

    public static Complex sub(Complex a, Complex b) {
        return  MathsBase.sub(a, b);
    }

    public static double norm(Matrix a) {
        return  MathsBase.norm(a);
    }

    public static double norm(Vector a) {
        return  MathsBase.norm(a);
    }

    public static double norm1(Matrix a) {
        return  MathsBase.norm1(a);
    }

    public static double norm2(Matrix a) {
        return  MathsBase.norm2(a);
    }

    public static double norm3(Matrix a) {
        return  MathsBase.norm3(a);
    }

    public static double normInf(Matrix a) {
        return  MathsBase.normInf(a);
    }

    public static DoubleToComplex complex(DoubleToDouble fx) {
        return  MathsBase.complex(fx);
    }

    public static DoubleToComplex complex(DoubleToDouble fx, DoubleToDouble fy) {
        return  MathsBase.complex(fx, fy);
    }

    public static double randomDouble(double value) {
        return  MathsBase.randomDouble(value);
    }

    public static double randomDouble(double min, double max) {
        return  MathsBase.randomDouble(min, max);
    }

    public static int randomInt(int value) {
        return  MathsBase.randomInt(value);
    }

    public static int randomInt(int min, int max) {
        return  MathsBase.randomInt(min, max);
    }

    public static Complex randomComplex() {
        return  MathsBase.randomComplex();
    }

    public static boolean randomBoolean() {
        return  MathsBase.randomBoolean();
    }

    public static double[][] cross(double[] x, double[] y) {
        return  MathsBase.cross(x, y);
    }

    public static double[][] cross(double[] x, double[] y, double[] z) {
        return  MathsBase.cross(x, y, z);
    }

    public static double[][] cross(double[] x, double[] y, double[] z, Double3Filter filter) {
        return  MathsBase.cross(x, y, z, filter);
    }

    public static double[][] cross(double[] x, double[] y, Double2Filter filter) {
        return  MathsBase.cross(x, y, filter);
    }

    public static int[][] cross(int[] x, int[] y) {
        return  MathsBase.cross(x, y);
    }

    public static int[][] cross(int[] x, int[] y, int[] z) {
        return  MathsBase.cross(x, y, z);
    }

    public static TList sineSeq(String borders, int m, int n, Domain domain) {
        return  MathsBase.sineSeq(borders, m, n, domain);
    }

    public static TList sineSeq(String borders, int m, int n, Domain domain, PlaneAxis plane) {
        return  MathsBase.sineSeq(borders, m, n, domain, plane);
    }

    public static Expr sineSeq(String borders, DoubleParam m, DoubleParam n, Domain domain) {
        return  MathsBase.sineSeq(borders, m, n, domain);
    }

    public static Expr rooftop(String borders, int nx, int ny, Domain domain) {
        return  MathsBase.rooftop(borders, nx, ny, domain);
    }

    public static Any any(double e) {
        return  MathsBase.any(e);
    }

    public static Any any(Expr e) {
        return  MathsBase.any(e);
    }

    public static Any any(Double e) {
        return  MathsBase.any(e);
    }

    public static TList<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax, Int2Filter filter) {
        return  MathsBase.seq(pattern, m, mmax, n, nmax, filter);
    }

    public static TList<Expr> seq(Expr pattern, DoubleParam m, int max, IntFilter filter) {
        return  MathsBase.seq(pattern, m, max, filter);
    }

    public static TList<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax, DoubleParam p, int pmax, Int3Filter filter) {
        return  MathsBase.seq(pattern, m, mmax, n, nmax, p, pmax, filter);
    }

    public static TList<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax) {
        return  MathsBase.seq(pattern, m, mmax, n, nmax);
    }

    public static TList<Expr> seq(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues) {
        return  MathsBase.seq(pattern, m, mvalues, n, nvalues);
    }

    public static TList<Expr> seq(final Expr pattern, final DoubleParam m, final DoubleParam n, final double[][] values) {
        return  MathsBase.seq(pattern, m, n, values);
    }

    public static TList<Expr> seq(final Expr pattern, final DoubleParam m, final DoubleParam n, final DoubleParam p, final double[][] values) {
        return  MathsBase.seq(pattern, m, n, p, values);
    }

    public static TList<Expr> seq(final Expr pattern, final DoubleParam[] m, final double[][] values) {
        return  MathsBase.seq(pattern, m, values);
    }

    public static TList<Expr> seq(final Expr pattern, final DoubleParam m, int min, int max) {
        return  MathsBase.seq(pattern, m, min, max);
    }

    public static TList<Expr> seq(final Expr pattern, final DoubleParam m, final double[] values) {
        return  MathsBase.seq(pattern, m, values);
    }

    public static ExprMatrix2 matrix(final Expr pattern, final DoubleParam m, final double[] mvalues, final DoubleParam n, final double[] nvalues) {
        return  MathsBase.matrix(pattern, m, mvalues, n, nvalues);
    }

    public static ExprCube cube(final Expr pattern, final DoubleParam m, final double[] mvalues, final DoubleParam n, final double[] nvalues, final DoubleParam p, final double[] pvalues) {
        return  MathsBase.cube(pattern, m, mvalues, n, nvalues, p, pvalues);
    }

    public static Expr derive(Expr f, Axis axis) {
        return  MathsBase.derive(f, axis);
    }

    public static boolean isReal(Expr e) {
        return  MathsBase.isReal(e);
    }

    public static boolean isImag(Expr e) {
        return  MathsBase.isImag(e);
    }

    public static Expr abs(Expr e) {
        return  MathsBase.abs(e);
    }

    public static Expr db(Expr e) {
        return  MathsBase.db(e);
    }

    public static Expr db2(Expr e) {
        return  MathsBase.db2(e);
    }

    public static Complex complex(int e) {
        return  MathsBase.complex(e);
    }

    public static Complex complex(double e) {
        return  MathsBase.complex(e);
    }

    public static Complex complex(double a, double b) {
        return  MathsBase.complex(a, b);
    }

    public static double Double(Expr e) {
        return  MathsBase.Double(e);
    }

    public static Expr real(Expr e) {
        return  MathsBase.real(e);
    }

    public static Expr imag(Expr e) {
        return  MathsBase.imag(e);
    }

    public static Complex Complex(Expr e) {
        return  MathsBase.Complex(e);
    }

    public static Complex Complex(double e) {
        return  MathsBase.Complex(e);
    }

    public static Complex complex(Expr e) {
        return  MathsBase.complex(e);
    }

    public static double doubleValue(Expr e) {
        return  MathsBase.doubleValue(e);
    }

    public static Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3) {
        return  MathsBase.discrete(domain, model, x, y, z, dx, dy, dz, axis1, axis2, axis3);
    }

    public static Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz) {
        return  MathsBase.discrete(domain, model, x, y, z, dx, dy, dz);
    }

    public static Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z) {
        return  MathsBase.discrete(domain, model, x, y, z);
    }

    public static Discrete discrete(Domain domain, Complex[][][] model, double dx, double dy, double dz) {
        return  MathsBase.discrete(domain, model, dx, dy, dz);
    }

    public static Discrete discrete(Complex[][][] model, double[] x, double[] y, double[] z) {
        return  MathsBase.discrete(model, x, y, z);
    }

    public static Discrete discrete(Complex[][] model, double[] x, double[] y) {
        return  MathsBase.discrete(model, x, y);
    }

    public static Expr discrete(Expr expr, double[] xsamples, double[] ysamples, double[] zsamples) {
        return  MathsBase.discrete(expr, xsamples, ysamples, zsamples);
    }

    public static Expr discrete(Expr expr, Samples samples) {
        return  MathsBase.discrete(expr, samples);
    }

    public static Expr abssqr(Expr e) {
        return  MathsBase.abssqr(e);
    }

    public static AdaptiveResult1 adaptiveEval(Expr expr, AdaptiveConfig config) {
        return  MathsBase.adaptiveEval(expr, config);
    }

    public static <T> AdaptiveResult1 adaptiveEval(AdaptiveFunction1<T> expr, DistanceStrategy<T> distance, DomainX domain, AdaptiveConfig config) {
        return  MathsBase.adaptiveEval(expr, distance, domain, config);
    }

    public static Discrete discrete(Expr expr) {
        return  MathsBase.discrete(expr);
    }

    public static VDiscrete vdiscrete(Expr expr) {
        return  MathsBase.vdiscrete(expr);
    }

    public static Expr discrete(Expr expr, int xSamples) {
        return  MathsBase.discrete(expr, xSamples);
    }

    public static Expr discrete(Expr expr, int xSamples, int ySamples) {
        return  MathsBase.discrete(expr, xSamples, ySamples);
    }

    public static Expr discrete(Expr expr, int xSamples, int ySamples, int zSamples) {
        return  MathsBase.discrete(expr, xSamples, ySamples, zSamples);
    }

    public static AxisFunction axis(Axis e) {
        return  MathsBase.axis(e);
    }

    public static Expr transformAxis(Expr e, AxisFunction a1, AxisFunction a2, AxisFunction a3) {
        return  MathsBase.transformAxis(e, a1, a2, a3);
    }

    public static Expr transformAxis(Expr e, Axis a1, Axis a2, Axis a3) {
        return  MathsBase.transformAxis(e, a1, a2, a3);
    }

    public static Expr transformAxis(Expr e, AxisFunction a1, AxisFunction a2) {
        return  MathsBase.transformAxis(e, a1, a2);
    }

    public static Expr transformAxis(Expr e, Axis a1, Axis a2) {
        return  MathsBase.transformAxis(e, a1, a2);
    }

    public static double sin2(double d) {
        return  MathsBase.sin2(d);
    }

    public static double cos2(double d) {
        return  MathsBase.cos2(d);
    }

    public static boolean isInt(double d) {
        return  MathsBase.isInt(d);
    }

    public static <T> T lcast(Object o, Class<T> type) {
        return  MathsBase.lcast(o, type);
    }

    public static String dump(Object o) {
        return  MathsBase.dump(o);
    }

    public static String dumpSimple(Object o) {
        return  MathsBase.dumpSimple(o);
    }

    public static DoubleToDouble expr(double value, Geometry geometry) {
        return  MathsBase.expr(value, geometry);
    }

    public static DoubleToDouble expr(double value, Domain geometry) {
        return  MathsBase.expr(value, geometry);
    }

    public static DoubleToDouble expr(Domain domain) {
        return  MathsBase.expr(domain);
    }

    public static DoubleToDouble expr(Geometry domain) {
        return  MathsBase.expr(domain);
    }

    public static Expr expr(Complex a, Geometry geometry) {
        return  MathsBase.expr(a, geometry);
    }

    public static Expr expr(Complex a, Domain geometry) {
        return  MathsBase.expr(a, geometry);
    }

    public static <T extends Expr> TList<T> preload(TList<T> list) {
        return  MathsBase.preload(list);
    }

    public static <T extends Expr> TList<T> withCache(TList<T> list) {
        return  MathsBase.withCache(list);
    }

    public static <T> TList<T> abs(TList<T> a) {
        return  MathsBase.abs(a);
    }

    public static <T> TList<T> db(TList<T> a) {
        return  MathsBase.db(a);
    }

    public static <T> TList<T> db2(TList<T> a) {
        return  MathsBase.db2(a);
    }

    public static <T> TList<T> real(TList<T> a) {
        return  MathsBase.real(a);
    }

    public static <T> TList<T> imag(TList<T> a) {
        return  MathsBase.imag(a);
    }

    public static double real(Complex a) {
        return  MathsBase.real(a);
    }

    public static double imag(Complex a) {
        return  MathsBase.imag(a);
    }

    public static boolean almostEqualRelative(float a, float b, float maxRelativeError) {
        return  MathsBase.almostEqualRelative(a, b, maxRelativeError);
    }

    public static boolean almostEqualRelative(double a, double b, double maxRelativeError) {
        return  MathsBase.almostEqualRelative(a, b, maxRelativeError);
    }

    public static boolean almostEqualRelative(Complex a, Complex b, double maxRelativeError) {
        return  MathsBase.almostEqualRelative(a, b, maxRelativeError);
    }

    public static DoubleArrayParamSet dtimes(Param param, double min, double max, int times) {
        return  MathsBase.dtimes(param, min, max, times);
    }

    public static DoubleArrayParamSet dsteps(Param param, double min, double max, double step) {
        return  MathsBase.dsteps(param, min, max, step);
    }

    public static IntArrayParamSet itimes(Param param, int min, int max, int times) {
        return  MathsBase.itimes(param, min, max, times);
    }

    public static IntArrayParamSet isteps(Param param, int min, int max, int step) {
        return  MathsBase.isteps(param, min, max, step);
    }

    public static FloatArrayParamSet ftimes(Param param, int min, int max, int times) {
        return  MathsBase.ftimes(param, min, max, times);
    }

    public static FloatArrayParamSet fsteps(Param param, int min, int max, int step) {
        return  MathsBase.fsteps(param, min, max, step);
    }

    public static LongArrayParamSet ltimes(Param param, int min, int max, int times) {
        return  MathsBase.ltimes(param, min, max, times);
    }

    public static LongArrayParamSet lsteps(Param param, int min, int max, long step) {
        return  MathsBase.lsteps(param, min, max, step);
    }

    public static Vector sin(Vector v) {
        return  MathsBase.sin(v);
    }

    public static Vector cos(Vector v) {
        return  MathsBase.cos(v);
    }

    public static Vector tan(Vector v) {
        return  MathsBase.tan(v);
    }

    public static Vector cotan(Vector v) {
        return  MathsBase.cotan(v);
    }

    public static Vector tanh(Vector v) {
        return  MathsBase.tanh(v);
    }

    public static Vector cotanh(Vector v) {
        return  MathsBase.cotanh(v);
    }

    public static Vector cosh(Vector v) {
        return  MathsBase.cosh(v);
    }

    public static Vector sinh(Vector v) {
        return  MathsBase.sinh(v);
    }

    public static Vector log(Vector v) {
        return  MathsBase.log(v);
    }

    public static Vector log10(Vector v) {
        return  MathsBase.log10(v);
    }

    public static Vector db(Vector v) {
        return  MathsBase.db(v);
    }

    public static Vector exp(Vector v) {
        return  MathsBase.exp(v);
    }

    public static Vector acosh(Vector v) {
        return  MathsBase.acosh(v);
    }

    public static Vector acos(Vector v) {
        return  MathsBase.acos(v);
    }

    public static Vector asinh(Vector v) {
        return  MathsBase.asinh(v);
    }

    public static Vector asin(Vector v) {
        return  MathsBase.asin(v);
    }

    public static Vector atan(Vector v) {
        return  MathsBase.atan(v);
    }

    public static Vector acotan(Vector v) {
        return  MathsBase.acotan(v);
    }

    public static Vector imag(Vector v) {
        return  MathsBase.imag(v);
    }

    public static Vector real(Vector v) {
        return  MathsBase.real(v);
    }

    public static Vector abs(Vector v) {
        return  MathsBase.abs(v);
    }

    public static Complex[] abs(Complex[] v) {
        return  MathsBase.abs(v);
    }

    public static Complex avg(Vector v) {
        return  MathsBase.avg(v);
    }

    public static Complex sum(Vector v) {
        return  MathsBase.sum(v);
    }

    public static Complex prod(Vector v) {
        return  MathsBase.prod(v);
    }

    public static Matrix abs(Matrix v) {
        return  MathsBase.abs(v);
    }

    public static Matrix sin(Matrix v) {
        return  MathsBase.sin(v);
    }

    public static Matrix cos(Matrix v) {
        return  MathsBase.cos(v);
    }

    public static Matrix tan(Matrix v) {
        return  MathsBase.tan(v);
    }

    public static Matrix cotan(Matrix v) {
        return  MathsBase.cotan(v);
    }

    public static Matrix tanh(Matrix v) {
        return  MathsBase.tanh(v);
    }

    public static Matrix cotanh(Matrix v) {
        return  MathsBase.cotanh(v);
    }

    public static Matrix cosh(Matrix v) {
        return  MathsBase.cosh(v);
    }

    public static Matrix sinh(Matrix v) {
        return  MathsBase.sinh(v);
    }

    public static Matrix log(Matrix v) {
        return  MathsBase.log(v);
    }

    public static Matrix log10(Matrix v) {
        return  MathsBase.log10(v);
    }

    public static Matrix db(Matrix v) {
        return  MathsBase.db(v);
    }

    public static Matrix exp(Matrix v) {
        return  MathsBase.exp(v);
    }

    public static Matrix acosh(Matrix v) {
        return  MathsBase.acosh(v);
    }

    public static Matrix acos(Matrix v) {
        return  MathsBase.acos(v);
    }

    public static Matrix asinh(Matrix v) {
        return  MathsBase.asinh(v);
    }

    public static Matrix asin(Matrix v) {
        return  MathsBase.asin(v);
    }

    public static Matrix atan(Matrix v) {
        return  MathsBase.atan(v);
    }

    public static Matrix acotan(Matrix v) {
        return  MathsBase.acotan(v);
    }

    public static Matrix imag(Matrix v) {
        return  MathsBase.imag(v);
    }

    public static Matrix real(Matrix v) {
        return  MathsBase.real(v);
    }

    public static Complex[] real(Complex[] v) {
        return  MathsBase.real(v);
    }

    public static double[] realdbl(Complex[] v) {
        return  MathsBase.realdbl(v);
    }

    public static Complex[] imag(Complex[] v) {
        return  MathsBase.imag(v);
    }

    public static double[] imagdbl(Complex[] v) {
        return  MathsBase.imagdbl(v);
    }

    public static Complex avg(Matrix v) {
        return  MathsBase.avg(v);
    }

    public static Complex sum(Matrix v) {
        return  MathsBase.sum(v);
    }

    public static Complex prod(Matrix v) {
        return  MathsBase.prod(v);
    }

    public static boolean roundEquals(double a, double b, double epsilon) {
        return  MathsBase.roundEquals(a, b, epsilon);
    }

    public static double round(double val, double precision) {
        return  MathsBase.round(val, precision);
    }

    public static double sqrt(double v, int n) {
        return  MathsBase.sqrt(v, n);
    }

    public static double pow(double v, double n) {
        return  MathsBase.pow(v, n);
    }

    public static double db(double x) {
        return  MathsBase.db(x);
    }

    public static double acosh(double x) {
        return  MathsBase.acosh(x);
    }

    public static double atanh(double x) {
        return  MathsBase.atanh(x);
    }

    public static double acotanh(double x) {
        return  MathsBase.acotanh(x);
    }

    public static double asinh(double x) {
        return  MathsBase.asinh(x);
    }

    public static double db2(double nbr) {
        return  MathsBase.db2(nbr);
    }

    public static double sqrt(double nbr) {
        return  MathsBase.sqrt(nbr);
    }

    public static double inv(double x) {
        return  MathsBase.inv(x);
    }

    public static double conj(double x) {
        return  MathsBase.conj(x);
    }

    public static double[] sin2(double[] x) {
        return  MathsBase.sin2(x);
    }

    public static double[] cos2(double[] x) {
        return  MathsBase.cos2(x);
    }

    public static double[] sin(double[] x) {
        return  MathsBase.sin(x);
    }

    public static double[] cos(double[] x) {
        return  MathsBase.cos(x);
    }

    public static double[] tan(double[] x) {
        return  MathsBase.tan(x);
    }

    public static double[] cotan(double[] x) {
        return  MathsBase.cotan(x);
    }

    public static double[] sinh(double[] x) {
        return  MathsBase.sinh(x);
    }

    public static double[] cosh(double[] x) {
        return  MathsBase.cosh(x);
    }

    public static double[] tanh(double[] x) {
        return  MathsBase.tanh(x);
    }

    public static double[] cotanh(double[] x) {
        return  MathsBase.cotanh(x);
    }

    public static double max(double a, double b) {
        return  MathsBase.max(a, b);
    }

    public static int max(int a, int b) {
        return  MathsBase.max(a, b);
    }

    public static long max(long a, long b) {
        return  MathsBase.max(a, b);
    }

    public static double min(double a, double b) {
        return  MathsBase.min(a, b);
    }

    public static double min(double[] arr) {
        return  MathsBase.min(arr);
    }

    public static double max(double[] arr) {
        return  MathsBase.max(arr);
    }

    public static double avg(double[] arr) {
        return  MathsBase.avg(arr);
    }

    public static int min(int a, int b) {
        return  MathsBase.min(a, b);
    }

    public static Complex min(Complex a, Complex b) {
        return  MathsBase.min(a, b);
    }

    public static Complex max(Complex a, Complex b) {
        return  MathsBase.max(a, b);
    }

    public static long min(long a, long b) {
        return  MathsBase.min(a, b);
    }

    public static double[] minMax(double[] a) {
        return  MathsBase.minMax(a);
    }

    public static double[] minMaxAbs(double[] a) {
        return  MathsBase.minMaxAbs(a);
    }

    public static double[] minMaxAbsNonInfinite(double[] a) {
        return  MathsBase.minMaxAbsNonInfinite(a);
    }

    public static double avgAbs(double[] arr) {
        return  MathsBase.avgAbs(arr);
    }

    public static double[] distances(double[] arr) {
        return  MathsBase.distances(arr);
    }

    public static double[] div(double[] a, double[] b) {
        return  MathsBase.div(a, b);
    }

    public static double[] mul(double[] a, double[] b) {
        return  MathsBase.mul(a, b);
    }

    public static double[] sub(double[] a, double[] b) {
        return  MathsBase.sub(a, b);
    }

    public static double[] sub(double[] a, double b) {
        return  MathsBase.sub(a, b);
    }

    public static double[] add(double[] a, double[] b) {
        return  MathsBase.add(a, b);
    }

    public static double[] db(double[] a) {
        return  MathsBase.db(a);
    }

    public static double[][] sin(double[][] c) {
        return  MathsBase.sin(c);
    }

    public static double[][] sin2(double[][] c) {
        return  MathsBase.sin2(c);
    }

    public static double sin(double x) {
        return  MathsBase.sin(x);
    }

    public static double cos(double x) {
        return  MathsBase.cos(x);
    }

    public static double tan(double x) {
        return  MathsBase.tan(x);
    }

    public static double cotan(double x) {
        return  MathsBase.cotan(x);
    }

    public static double sinh(double x) {
        return  MathsBase.sinh(x);
    }

    public static double cosh(double x) {
        return  MathsBase.cosh(x);
    }

    public static double tanh(double x) {
        return  MathsBase.tanh(x);
    }

    public static double abs(double a) {
        return  MathsBase.abs(a);
    }

    public static int abs(int a) {
        return  MathsBase.abs(a);
    }

    public static double cotanh(double x) {
        return  MathsBase.cotanh(x);
    }

    public static double acos(double x) {
        return  MathsBase.acos(x);
    }

    public static double asin(double x) {
        return  MathsBase.asin(x);
    }

    public static double atan(double x) {
        return  MathsBase.atan(x);
    }

    public static double sum(double... c) {
        return  MathsBase.sum(c);
    }

    public static double[] mul(double[] a, double b) {
        return  MathsBase.mul(a, b);
    }

    public static double[] mulSelf(double[] x, double v) {
        return  MathsBase.mulSelf(x, v);
    }

    public static double[] div(double[] a, double b) {
        return  MathsBase.div(a, b);
    }

    public static double[] divSelf(double[] x, double v) {
        return  MathsBase.divSelf(x, v);
    }

    public static double[] add(double[] x, double v) {
        return  MathsBase.add(x, v);
    }

    public static double[] addSelf(double[] x, double v) {
        return  MathsBase.addSelf(x, v);
    }

    public static double[][] cos(double[][] c) {
        return  MathsBase.cos(c);
    }

    public static double[][] tan(double[][] c) {
        return  MathsBase.tan(c);
    }

    public static double[][] cotan(double[][] c) {
        return  MathsBase.cotan(c);
    }

    public static double[][] sinh(double[][] c) {
        return  MathsBase.sinh(c);
    }

    public static double[][] cosh(double[][] c) {
        return  MathsBase.cosh(c);
    }

    public static double[][] tanh(double[][] c) {
        return  MathsBase.tanh(c);
    }

    public static double[][] cotanh(double[][] c) {
        return  MathsBase.cotanh(c);
    }

    public static double[][] add(double[][] a, double[][] b) {
        return  MathsBase.add(a, b);
    }

    public static double[][] sub(double[][] a, double[][] b) {
        return  MathsBase.sub(a, b);
    }

    public static double[][] div(double[][] a, double[][] b) {
        return  MathsBase.div(a, b);
    }

    public static double[][] mul(double[][] a, double[][] b) {
        return  MathsBase.mul(a, b);
    }

    public static double[][] db(double[][] a) {
        return  MathsBase.db(a);
    }

    public static double[][] db2(double[][] a) {
        return  MathsBase.db2(a);
    }

    public static Expr If(Expr cond, Expr exp1, Expr exp2) {
        return  MathsBase.If(cond, exp1, exp2);
    }

    public static Expr or(Expr a, Expr b) {
        return  MathsBase.or(a, b);
    }

    public static Expr and(Expr a, Expr b) {
        return  MathsBase.and(a, b);
    }

    public static Expr not(Expr a) {
        return  MathsBase.not(a);
    }

    public static Expr eq(Expr a, Expr b) {
        return  MathsBase.eq(a, b);
    }

    public static Expr ne(Expr a, Expr b) {
        return  MathsBase.ne(a, b);
    }

    public static Expr gte(Expr a, Expr b) {
        return  MathsBase.gte(a, b);
    }

    public static Expr gt(Expr a, Expr b) {
        return  MathsBase.gt(a, b);
    }

    public static Expr lte(Expr a, Expr b) {
        return  MathsBase.lte(a, b);
    }

    public static Expr lt(Expr a, Expr b) {
        return  MathsBase.lt(a, b);
    }

    public static Expr cos(Expr e) {
        return  MathsBase.cos(e);
    }

    public static Expr cosh(Expr e) {
        return  MathsBase.cosh(e);
    }

    public static Expr sin(Expr e) {
        return  MathsBase.sin(e);
    }

    public static Expr sincard(Expr e) {
        return  MathsBase.sincard(e);
    }

    public static Expr sinh(Expr e) {
        return  MathsBase.sinh(e);
    }

    public static Expr tan(Expr e) {
        return  MathsBase.tan(e);
    }

    public static Expr tanh(Expr e) {
        return  MathsBase.tanh(e);
    }

    public static Expr cotan(Expr e) {
        return  MathsBase.cotan(e);
    }

    public static Expr cotanh(Expr e) {
        return  MathsBase.cotanh(e);
    }

    public static Expr sqr(Expr e) {
        return  MathsBase.sqr(e);
    }

    public static Expr sqrt(Expr e) {
        return  MathsBase.sqrt(e);
    }

    public static Expr inv(Expr e) {
        return  MathsBase.inv(e);
    }

    public static Expr neg(Expr e) {
        return  MathsBase.neg(e);
    }

    public static Expr exp(Expr e) {
        return  MathsBase.exp(e);
    }

    public static Expr atan(Expr e) {
        return  MathsBase.atan(e);
    }

    public static Expr acotan(Expr e) {
        return  MathsBase.acotan(e);
    }

    public static Expr acos(Expr e) {
        return  MathsBase.acos(e);
    }

    public static Expr asin(Expr e) {
        return  MathsBase.asin(e);
    }

    public static Complex integrate(Expr e) {
        return  MathsBase.integrate(e);
    }

    public static Complex integrate(Expr e, Domain domain) {
        return  MathsBase.integrate(e, domain);
    }

    public static Expr esum(int size, TVectorCell<Expr> f) {
        return  MathsBase.esum(size, f);
    }

    public static Expr esum(int size1, int size2, TMatrixCell<Expr> e) {
        return  MathsBase.esum(size1, size2, e);
    }

    public static Complex csum(int size1, int size2, TMatrixCell<Complex> e) {
        return  MathsBase.csum(size1, size2, e);
    }

    public static TVector<Expr> seq(int size1, TVectorCell<Expr> f) {
        return  MathsBase.seq(size1, f);
    }

    public static TVector<Expr> seq(int size1, int size2, TMatrixCell<Expr> f) {
        return  MathsBase.seq(size1, size2, f);
    }

    public static TMatrix<Complex> scalarProductCache(Expr[] gp, Expr[] fn, ProgressMonitor monitor) {
        return  MathsBase.scalarProductCache(gp, fn, monitor);
    }

    public static TMatrix<Complex> scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, ProgressMonitor monitor) {
        return  MathsBase.scalarProductCache(sp, gp, fn, monitor);
    }

    public static TMatrix<Complex> scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, AxisXY axis, ProgressMonitor monitor) {
        return  MathsBase.scalarProductCache(sp, gp, fn, axis, monitor);
    }

    public static TMatrix<Complex> scalarProductCache(Expr[] gp, Expr[] fn, AxisXY axis, ProgressMonitor monitor) {
        return  MathsBase.scalarProductCache(gp, fn, axis, monitor);
    }

    public static Expr gate(Axis axis, double a, double b) {
        return  MathsBase.gate(axis, a, b);
    }

    public static Expr gate(Expr axis, double a, double b) {
        return  MathsBase.gate(axis, a, b);
    }

    public static Expr gateX(double a, double b) {
        return  MathsBase.gateX(a, b);
    }

    public static Expr gateY(double a, double b) {
        return  MathsBase.gateY(a, b);
    }

    public static Expr gateZ(double a, double b) {
        return  MathsBase.gateZ(a, b);
    }

    public static double scalarProduct(DoubleToDouble f1, DoubleToDouble f2) {
        return  MathsBase.scalarProduct(f1, f2);
    }

    public static Vector scalarProduct(Expr f1, TVector<Expr> f2) {
        return  MathsBase.scalarProduct(f1, f2);
    }

    public static Matrix scalarProduct(Expr f1, TMatrix<Expr> f2) {
        return  MathsBase.scalarProduct(f1, f2);
    }

    public static Vector scalarProduct(TVector<Expr> f2, Expr f1) {
        return  MathsBase.scalarProduct(f2, f1);
    }

    public static Matrix scalarProduct(TMatrix<Expr> f2, Expr f1) {
        return  MathsBase.scalarProduct(f2, f1);
    }

    public static Complex scalarProduct(Domain domain, Expr f1, Expr f2) {
        return  MathsBase.scalarProduct(domain, f1, f2);
    }

    public static Complex scalarProduct(Expr f1, Expr f2) {
        return  MathsBase.scalarProduct(f1, f2);
    }

    public static Matrix scalarProductMatrix(TVector<Expr> g, TVector<Expr> f) {
        return  MathsBase.scalarProductMatrix(g, f);
    }

    public static TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f) {
        return  MathsBase.scalarProduct(g, f);
    }

    public static TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return  MathsBase.scalarProduct(g, f, monitor);
    }

    public static Matrix scalarProductMatrix(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return  MathsBase.scalarProductMatrix(g, f, monitor);
    }

    public static TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f, AxisXY axis, ProgressMonitor monitor) {
        return  MathsBase.scalarProduct(g, f, axis, monitor);
    }

    public static Matrix scalarProductMatrix(Expr[] g, Expr[] f) {
        return  MathsBase.scalarProductMatrix(g, f);
    }

    public static Complex scalarProduct(Matrix g, Matrix f) {
        return  MathsBase.scalarProduct(g, f);
    }

    public static Expr scalarProduct(Matrix g, TVector<Expr> f) {
        return  MathsBase.scalarProduct(g, f);
    }

    public static Expr scalarProductAll(Matrix g, TVector<Expr>... f) {
        return  MathsBase.scalarProductAll(g, f);
    }

    public static TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f) {
        return  MathsBase.scalarProduct(g, f);
    }

    public static TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return  MathsBase.scalarProduct(g, f, monitor);
    }

    public static Matrix scalarProductMatrix(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return  MathsBase.scalarProductMatrix(g, f, monitor);
    }

    public static TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f, AxisXY axis, ProgressMonitor monitor) {
        return  MathsBase.scalarProduct(g, f, axis, monitor);
    }

    public static ExprList elist(int size) {
        return  MathsBase.elist(size);
    }

    public static ExprList elist(boolean row, int size) {
        return  MathsBase.elist(row, size);
    }

    public static ExprList elist(Expr... vector) {
        return  MathsBase.elist(vector);
    }

    public static ExprList elist(TVector<Complex> vector) {
        return  MathsBase.elist(vector);
    }

    public static TList<Complex> clist(TVector<Expr> vector) {
        return  MathsBase.clist(vector);
    }

    public static TList<Complex> clist() {
        return  MathsBase.clist();
    }

    public static TList<Complex> clist(int size) {
        return  MathsBase.clist(size);
    }

    public static TList<Complex> clist(Complex... vector) {
        return  MathsBase.clist(vector);
    }

    public static TList<Matrix> mlist() {
        return  MathsBase.mlist();
    }

    public static TList<Matrix> mlist(int size) {
        return  MathsBase.mlist(size);
    }

    public static TList<Matrix> mlist(Matrix... items) {
        return  MathsBase.mlist(items);
    }

    public static TList<TList<Complex>> clist2() {
        return  MathsBase.clist2();
    }

    public static TList<TList<Expr>> elist2() {
        return  MathsBase.elist2();
    }

    public static TList<TList<Double>> dlist2() {
        return  MathsBase.dlist2();
    }

    public static TList<TList<Integer>> ilist2() {
        return  MathsBase.ilist2();
    }

    public static TList<TList<Matrix>> mlist2() {
        return  MathsBase.mlist2();
    }

    public static TList<TList<Boolean>> blist2() {
        return  MathsBase.blist2();
    }

    public static <T> TList<T> list(TypeName<T> type) {
        return  MathsBase.list(type);
    }

    public static <T> TList<T> list(TypeName<T> type, int initialSize) {
        return  MathsBase.list(type, initialSize);
    }

    public static <T> TList<T> listro(TypeName<T> type, boolean row, TVectorModel<T> model) {
        return  MathsBase.listro(type, row, model);
    }

    public static <T> TList<T> list(TypeName<T> type, boolean row, int initialSize) {
        return  MathsBase.list(type, row, initialSize);
    }

    public static <T> TList<T> list(TVector<T> vector) {
        return  MathsBase.list(vector);
    }

    public static ExprList elist(Matrix vector) {
        return  MathsBase.elist(vector);
    }

    public static <T> TVector<T> vscalarProduct(TVector<T> vector, TVector<TVector<T>> tVectors) {
        return  MathsBase.vscalarProduct(vector, tVectors);
    }

    public static TList<Expr> elist() {
        return  MathsBase.elist();
    }

    public static <T> TList<T> concat(TList<T>... a) {
        return  MathsBase.concat(a);
    }

    public static TList<Double> dlist() {
        return  MathsBase.dlist();
    }

    public static TList<Double> dlist(ToDoubleArrayAware items) {
        return  MathsBase.dlist(items);
    }

    public static TList<Double> dlist(double[] items) {
        return  MathsBase.dlist(items);
    }

    public static TList<Double> dlist(boolean row, int size) {
        return  MathsBase.dlist(row, size);
    }

    public static TList<Double> dlist(int size) {
        return  MathsBase.dlist(size);
    }

    public static TList<String> slist() {
        return  MathsBase.slist();
    }

    public static TList<String> slist(String[] items) {
        return  MathsBase.slist(items);
    }

    public static TList<String> slist(boolean row, int size) {
        return  MathsBase.slist(row, size);
    }

    public static TList<String> slist(int size) {
        return  MathsBase.slist(size);
    }

    public static TList<Boolean> blist() {
        return  MathsBase.blist();
    }

    public static TList<Boolean> dlist(boolean[] items) {
        return  MathsBase.dlist(items);
    }

    public static TList<Boolean> blist(boolean row, int size) {
        return  MathsBase.blist(row, size);
    }

    public static TList<Boolean> blist(int size) {
        return  MathsBase.blist(size);
    }

    public static IntList ilist() {
        return  MathsBase.ilist();
    }

    public static TList<Integer> ilist(int[] items) {
        return  MathsBase.ilist(items);
    }

    public static TList<Integer> ilist(int size) {
        return  MathsBase.ilist(size);
    }

    public static TList<Integer> ilist(boolean row, int size) {
        return  MathsBase.ilist(row, size);
    }

    public static LongList llist() {
        return  MathsBase.llist();
    }

    public static TList<Long> llist(long[] items) {
        return  MathsBase.llist(items);
    }

    public static TList<Long> llist(int size) {
        return  MathsBase.llist(size);
    }

    public static TList<Long> llist(boolean row, int size) {
        return  MathsBase.llist(row, size);
    }

    public static <T> T sum(TypeName<T> type, T... arr) {
        return  MathsBase.sum(type, arr);
    }

    public static <T> T sum(TypeName<T> type, TVectorModel<T> arr) {
        return  MathsBase.sum(type, arr);
    }

    public static <T> T sum(TypeName<T> type, int size, TVectorCell<T> arr) {
        return  MathsBase.sum(type, size, arr);
    }

    public static <T> T mul(TypeName<T> type, T... arr) {
        return  MathsBase.mul(type, arr);
    }

    public static <T> T mul(TypeName<T> type, TVectorModel<T> arr) {
        return  MathsBase.mul(type, arr);
    }

    public static Complex avg(Discrete d) {
        return  MathsBase.avg(d);
    }

    public static DoubleToVector vsum(VDiscrete d) {
        return  MathsBase.vsum(d);
    }

    public static DoubleToVector vavg(VDiscrete d) {
        return  MathsBase.vavg(d);
    }

    public static Complex avg(VDiscrete d) {
        return  MathsBase.avg(d);
    }

    public static Expr sum(Expr... arr) {
        return  MathsBase.sum(arr);
    }

    public static Expr esum(TVectorModel<Expr> arr) {
        return  MathsBase.esum(arr);
    }

    public static <T> TMatrix<T> mul(TMatrix<T> a, TMatrix<T> b) {
        return  MathsBase.mul(a, b);
    }

    public static Matrix mul(Matrix a, Matrix b) {
        return  MathsBase.mul(a, b);
    }

    public static Expr mul(Expr a, Expr b) {
        return  MathsBase.mul(a, b);
    }

    public static TVector<Expr> edotmul(TVector<Expr>... arr) {
        return  MathsBase.edotmul(arr);
    }

    public static TVector<Expr> edotdiv(TVector<Expr>... arr) {
        return  MathsBase.edotdiv(arr);
    }

    public static Complex cmul(TVectorModel<Complex> arr) {
        return  MathsBase.cmul(arr);
    }

    public static Expr emul(TVectorModel<Expr> arr) {
        return  MathsBase.emul(arr);
    }

    public static Expr mul(Expr... e) {
        return  MathsBase.mul(e);
    }

    public static Expr pow(Expr a, Expr b) {
        return  MathsBase.pow(a, b);
    }

    public static Expr sub(Expr a, Expr b) {
        return  MathsBase.sub(a, b);
    }

    public static Expr add(Expr a, double b) {
        return  MathsBase.add(a, b);
    }

    public static Expr mul(Expr a, double b) {
        return  MathsBase.mul(a, b);
    }

    public static Expr sub(Expr a, double b) {
        return  MathsBase.sub(a, b);
    }

    public static Expr div(Expr a, double b) {
        return  MathsBase.div(a, b);
    }

    public static Expr add(Expr a, Expr b) {
        return  MathsBase.add(a, b);
    }

    public static Expr add(Expr... a) {
        return  MathsBase.add(a);
    }

    public static Expr div(Expr a, Expr b) {
        return  MathsBase.div(a, b);
    }

    public static Expr rem(Expr a, Expr b) {
        return  MathsBase.rem(a, b);
    }

    public static Expr expr(double value) {
        return  MathsBase.expr(value);
    }

    public static <T> TVector<Expr> expr(TVector<T> vector) {
        return  MathsBase.expr(vector);
    }

    public static TMatrix<Expr> expr(TMatrix<Complex> matrix) {
        return  MathsBase.expr(matrix);
    }

    public static <T> TMatrix<T> tmatrix(TypeName<T> type, TMatrixModel<T> model) {
        return  MathsBase.tmatrix(type, model);
    }

    public static <T> TMatrix<T> tmatrix(TypeName<T> type, int rows, int columns, TMatrixCell<T> model) {
        return  MathsBase.tmatrix(type, rows, columns, model);
    }

    public static <T> T simplify(T a) {
        return  MathsBase.simplify(a);
    }

    public static Expr simplify(Expr a) {
        return  MathsBase.simplify(a);
    }

    public static double norm(Expr a) {
        return  MathsBase.norm(a);
    }

    public static <T> TList<T> normalize(TList<T> a) {
        return  MathsBase.normalize(a);
    }

    public static Expr normalize(Geometry a) {
        return  MathsBase.normalize(a);
    }

    public static Expr normalize(Expr a) {
        return  MathsBase.normalize(a);
    }

    public static DoubleToVector vector(Expr fx, Expr fy) {
        return  MathsBase.vector(fx, fy);
    }

    public static DoubleToVector vector(Expr fx) {
        return  MathsBase.vector(fx);
    }

    public static DoubleToVector vector(Expr fx, Expr fy, Expr fz) {
        return  MathsBase.vector(fx, fy, fz);
    }

    public static <T> TVector<T> cos(TVector<T> a) {
        return  MathsBase.cos(a);
    }

    public static <T> TVector<T> cosh(TVector<T> a) {
        return  MathsBase.cosh(a);
    }

    public static <T> TVector<T> sin(TVector<T> a) {
        return  MathsBase.sin(a);
    }

    public static <T> TVector<T> sinh(TVector<T> a) {
        return  MathsBase.sinh(a);
    }

    public static <T> TVector<T> tan(TVector<T> a) {
        return  MathsBase.tan(a);
    }

    public static <T> TVector<T> tanh(TVector<T> a) {
        return  MathsBase.tanh(a);
    }

    public static <T> TVector<T> cotan(TVector<T> a) {
        return  MathsBase.cotan(a);
    }

    public static <T> TVector<T> cotanh(TVector<T> a) {
        return  MathsBase.cotanh(a);
    }

    public static <T> TVector<T> sqr(TVector<T> a) {
        return  MathsBase.sqr(a);
    }

    public static <T> TVector<T> sqrt(TVector<T> a) {
        return  MathsBase.sqrt(a);
    }

    public static <T> TVector<T> inv(TVector<T> a) {
        return  MathsBase.inv(a);
    }

    public static <T> TVector<T> neg(TVector<T> a) {
        return  MathsBase.neg(a);
    }

    public static <T> TVector<T> exp(TVector<T> a) {
        return  MathsBase.exp(a);
    }

    public static <T> TVector<T> simplify(TVector<T> a) {
        return  MathsBase.simplify(a);
    }

    public static <T> TList<T> simplify(TList<T> a) {
        return  MathsBase.simplify(a);
    }

    public static <T> TList<T> addAll(TList<T> e, T... expressions) {
        return  MathsBase.addAll(e, expressions);
    }

    public static <T> TList<T> mulAll(TList<T> e, T... expressions) {
        return  MathsBase.mulAll(e, expressions);
    }

    public static <T> TList<T> pow(TList<T> a, T b) {
        return  MathsBase.pow(a, b);
    }

    public static <T> TList<T> sub(TList<T> a, T b) {
        return  MathsBase.sub(a, b);
    }

    public static <T> TList<T> div(TList<T> a, T b) {
        return  MathsBase.div(a, b);
    }

    public static <T> TList<T> rem(TList<T> a, T b) {
        return  MathsBase.rem(a, b);
    }

    public static <T> TList<T> add(TList<T> a, T b) {
        return  MathsBase.add(a, b);
    }

    public static <T> TList<T> mul(TList<T> a, T b) {
        return  MathsBase.mul(a, b);
    }

    public static void loopOver(Object[][] values, LoopAction action) {
         MathsBase.loopOver(values, action);
    }

    public static void loopOver(Loop[] values, LoopAction action) {
         MathsBase.loopOver(values, action);
    }

    public static String formatMemory() {
        return  MathsBase.formatMemory();
    }

    public static String formatMetric(double value) {
        return  MathsBase.formatMetric(value);
    }

    public static MemoryInfo memoryInfo() {
        return  MathsBase.memoryInfo();
    }

    public static MemoryMeter memoryMeter() {
        return  MathsBase.memoryMeter();
    }

    public static long inUseMemory() {
        return  MathsBase.inUseMemory();
    }

    public static long maxFreeMemory() {
        return  MathsBase.maxFreeMemory();
    }

    public static String formatMemory(long bytes) {
        return  MathsBase.formatMemory(bytes);
    }

    public static String formatFrequency(double frequency) {
        return  MathsBase.formatFrequency(frequency);
    }

    public static String formatDimension(double dimension) {
        return  MathsBase.formatDimension(dimension);
    }

    public static String formatPeriodNanos(long period) {
        return  MathsBase.formatPeriodNanos(period);
    }

    public static String formatPeriodMillis(long period) {
        return  MathsBase.formatPeriodMillis(period);
    }

    public static int sizeOf(Class src) {
        return  MathsBase.sizeOf(src);
    }

    public static <T> T invokeMonitoredAction(ProgressMonitor mon, String messagePrefix, MonitoredAction<T> run) {
        return  MathsBase.invokeMonitoredAction(mon, messagePrefix, run);
    }

    public static Chronometer chrono() {
        return  MathsBase.chrono();
    }

    public static Chronometer chrono(String name) {
        return  MathsBase.chrono(name);
    }

    public static Chronometer chrono(String name, Runnable r) {
        return  MathsBase.chrono(name, r);
    }

    public static <V> V chrono(String name, Callable<V> r) {
        return  MathsBase.chrono(name, r);
    }

    public static SolverExecutorService solverExecutorService(int threads) {
        return  MathsBase.solverExecutorService(threads);
    }

    public static Chronometer chrono(Runnable r) {
        return  MathsBase.chrono(r);
    }

    public static DoubleFormat percentFormat() {
        return  MathsBase.percentFormat();
    }

    public static DoubleFormat frequencyFormat() {
        return  MathsBase.frequencyFormat();
    }

    public static DoubleFormat metricFormat() {
        return  MathsBase.metricFormat();
    }

    public static DoubleFormat memoryFormat() {
        return  MathsBase.memoryFormat();
    }

    public static DoubleFormat dblformat(String format) {
        return  MathsBase.dblformat(format);
    }

    public static double[] resizePickFirst(double[] array, int newSize) {
        return  MathsBase.resizePickFirst(array, newSize);
    }

    public static double[] resizePickAverage(double[] array, int newSize) {
        return  MathsBase.resizePickAverage(array, newSize);
    }

    public static <T> T[] toArray(Class<T> t, Collection<T> coll) {
        return  MathsBase.toArray(t, coll);
    }

    public static <T> T[] toArray(TypeName<T> t, Collection<T> coll) {
        return  MathsBase.toArray(t, coll);
    }

    public static double rerr(double a, double b) {
        return  MathsBase.rerr(a, b);
    }

    public static double rerr(Complex a, Complex b) {
        return  MathsBase.rerr(a, b);
    }

    public static CustomCCFunctionXDefinition define(String name, CustomCCFunctionX f) {
        return  MathsBase.define(name, f);
    }

    public static CustomDCFunctionXDefinition define(String name, CustomDCFunctionX f) {
        return  MathsBase.define(name, f);
    }

    public static CustomDDFunctionXDefinition define(String name, CustomDDFunctionX f) {
        return  MathsBase.define(name, f);
    }

    public static CustomDDFunctionXYDefinition define(String name, CustomDDFunctionXY f) {
        return  MathsBase.define(name, f);
    }

    public static CustomDCFunctionXYDefinition define(String name, CustomDCFunctionXY f) {
        return  MathsBase.define(name, f);
    }

    public static CustomCCFunctionXYDefinition define(String name, CustomCCFunctionXY f) {
        return  MathsBase.define(name, f);
    }

    public static double rerr(Matrix a, Matrix b) {
        return  MathsBase.rerr(a, b);
    }

    public static <T extends Expr> DoubleList toDoubleArray(TList<T> c) {
        return  MathsBase.toDoubleArray(c);
    }

    public static double toDouble(Complex c, PlotDoubleConverter d) {
        return  MathsBase.toDouble(c, d);
    }

    public static Expr conj(Expr e) {
        return  MathsBase.conj(e);
    }

    public static Complex complex(TMatrix t) {
        return  MathsBase.complex(t);
    }

    public static Matrix matrix(TMatrix t) {
        return  MathsBase.matrix(t);
    }

    public static TMatrix<Expr> ematrix(TMatrix t) {
        return  MathsBase.ematrix(t);
    }

    public static <T> VectorSpace<T> getVectorSpace(TypeName<T> cls) {
        return  MathsBase.getVectorSpace(cls);
    }

    public static DoubleList refineSamples(TList<Double> values, int n) {
        return  MathsBase.refineSamples(values, n);
    }

    public static double[] refineSamples(double[] values, int n) {
        return  MathsBase.refineSamples(values, n);
    }

    public static String getHadrumathsVersion() {
        return  MathsBase.getHadrumathsVersion();
    }

    public static ComponentDimension expandComponentDimension(ComponentDimension d1, ComponentDimension d2) {
        return  MathsBase.expandComponentDimension(d1, d2);
    }

    public static Expr expandComponentDimension(Expr e, ComponentDimension d) {
        return  MathsBase.expandComponentDimension(e, d);
    }

    public static double atan2(double y, double x) {
        return  MathsBase.atan2(y, x);
    }

    public static double ceil(double a) {
        return  MathsBase.ceil(a);
    }

    public static double floor(double a) {
        return  MathsBase.floor(a);
    }

    public static long round(double a) {
        return  MathsBase.round(a);
    }

    public static int round(float a) {
        return  MathsBase.round(a);
    }

    public static double random() {
        return  MathsBase.random();
    }

    public static <A, B> RightArrowUplet2<A, B> rightArrow(A a, B b) {
        return  MathsBase.rightArrow(a, b);
    }

    public static RightArrowUplet2.Double rightArrow(double a, double b) {
        return  MathsBase.rightArrow(a, b);
    }

    public static RightArrowUplet2.Complex rightArrow(Complex a, Complex b) {
        return  MathsBase.rightArrow(a, b);
    }

    public static RightArrowUplet2.Expr rightArrow(Expr a, Expr b) {
        return  MathsBase.rightArrow(a, b);
    }

    public static Expr parseExpression(String expression) {
        return  MathsBase.parseExpression(expression);
    }

    public static ExpressionManager createExpressionEvaluator() {
        return  MathsBase.createExpressionEvaluator();
    }

    public static ExpressionManager createExpressionParser() {
        return  MathsBase.createExpressionParser();
    }

    public static Expr evalExpression(String expression) {
        return  MathsBase.evalExpression(expression);
    }

    public static double toRadians(double a) {
        return  MathsBase.toRadians(a);
    }

    public static double[] toRadians(double[] a) {
        return  MathsBase.toRadians(a);
    }

    public static Complex det(Matrix m) {
        return  MathsBase.det(m);
    }

    public static int toInt(Object o) {
        return  MathsBase.toInt(o);
    }

    public static int toInt(Object o, Integer defaultValue) {
        return  MathsBase.toInt(o, defaultValue);
    }

    public static long toLong(Object o) {
        return  MathsBase.toLong(o);
    }

    public static long toLong(Object o, Long defaultValue) {
        return  MathsBase.toLong(o, defaultValue);
    }

    public static double toDouble(Object o) {
        return  MathsBase.toDouble(o);
    }

    public static double toDouble(Object o, Double defaultValue) {
        return  MathsBase.toDouble(o, defaultValue);
    }

    public static float toFloat(Object o) {
        return  MathsBase.toFloat(o);
    }

    public static float toFloat(Object o, Float defaultValue) {
        return  MathsBase.toFloat(o, defaultValue);
    }

    public static DoubleToComplex DC(Expr e) {
        return  MathsBase.DC(e);
    }

    public static DoubleToDouble DD(Expr e) {
        return  MathsBase.DD(e);
    }

    public static DoubleToVector DV(Expr e) {
        return  MathsBase.DV(e);
    }

    public static DoubleToMatrix DM(Expr e) {
        return  MathsBase.DM(e);
    }

    public static Matrix matrix(Expr e) {
        return MathsBase.matrix(e);
    }

    private Maths() {
    }

}
