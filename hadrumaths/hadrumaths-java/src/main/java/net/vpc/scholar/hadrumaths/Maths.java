package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.cache.CacheMode;
import net.vpc.scholar.hadrumaths.derivation.FormalDifferentiation;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.plot.ComplexAsDouble;
import net.vpc.scholar.hadrumaths.plot.console.params.*;
import net.vpc.scholar.hadrumaths.scalarproducts.MatrixScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.MemScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.util.*;
import net.vpc.scholar.hadrumaths.util.dump.DumpManager;
import sun.misc.Unsafe;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;
public final class Maths {
    //<editor-fold desc="constants functions">
    public static final double PI = Math.PI;
    public static final double E = Math.E;
    public static final DoubleToDouble DDZERO = DoubleValue.valueOf(0, Domain.FULLX);
    public static final DoubleToComplex DCZERO = DDZERO.toDC();
    public static final DoubleToVector DVZERO3 = DefaultDoubleToVector.create(DCZERO, DCZERO, DCZERO);
    public static final Expr EZERO = DDZERO;
    public static final Expr EONE = DoubleValue.valueOf(1, Domain.FULLX);
    public static final Expr X = new XX(Domain.FULLX);
    public static final Expr Y = new YY(Domain.FULLXY);
    public static final Expr Z = new ZZ(Domain.FULLXYZ);
    public static final double HALF_PI = Math.PI / 2.0;
    public static final Complex I = Complex.I;
    public static final Complex CNaN = Complex.NaN;
    public static final Complex CONE = Complex.ONE;
    public static final Complex CZERO = Complex.ZERO;//    public static boolean DEBUG = true;
    public static final DoubleToVector DVZERO1 = DefaultDoubleToVector.create(DCZERO);
    public static final DoubleToVector DVZERO2 = DefaultDoubleToVector.create(DCZERO, DCZERO);
    public static final Complex î = Complex.I;
    public static final Expr ê = EONE;
    public static final Complex ĉ = CONE;
    public static final double METER = 1;
    public static final double HZ = 1;
    public static final long BYTE = 1;
    public static final long MILLISECOND = 1;
    /**
     * kibibyte
     */
    public static final int KiBYTE = 1024;
    /**
     * mibibyte
     */
    public static final int MiBYTE = 1024 * KiBYTE;
    /**
     * TEBI Byte
     */
    public static final long GiBYTE = 1024 * MiBYTE;
    /**
     * TEBI Byte
     */
    public static final long TiBYTE = 1024L * GiBYTE;
    /**
     * PEBI Byte
     */
    public static final long PiBYTE = 1024L * TiBYTE;
    /**
     * exbibyte
     */
    public static final long EiBYTE = 1024L * PiBYTE;

    public static final double YOCTO = 1E-24;
    public static final double ZEPTO = 1E-21;
    public static final double ATTO = 1E-18;
    public static final double FEMTO = 1E-15;
    public static final double PICO = 1E-12;
    public static final double NANO = 1E-9;
    public static final double MICRO = 1E-6;
    public static final double MILLI = 1E-3;
    public static final double CENTI = 1E-2;
    public static final double DECI = 1E-1;
    /**
     * DECA
     */
    public static final int DECA = 10;
    /**
     * HECTO
     */
    public static final int HECTO = 100;

    /**
     * KILO
     */
    public static final int KILO = 1000;
    /**
     * MEGA
     */
    public static final int MEGA = 1000 * KILO;
    /**
     * MEGA
     */
    public static final long GIGA = 1000 * MEGA;
    /**
     * TERA
     */
    public static final long TERA = 1000 * GIGA;
    /**
     * PETA
     */
    public static final long PETA = 1000 * TERA;
    /**
     * EXA
     */
    public static final long EXA = 1000 * PETA;
    /**
     * ZETTA
     */
    public static final long ZETTA = 1000 * EXA;
    /**
     * YOTTA
     */
    public static final long YOTTA = 1000 * ZETTA;
    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final double KHZ = 1E3;
    public static final double MHZ = 1E6;
    public static final double GHZ = 1E9;
    public static final double MILLIMETER = 1E-3;
    public static final double MM = 1E-3;
    public static final double CM = 1E-2;
    public static final double CENTIMETER = 1E-2;
    /**
     * light celerity. speed of light in vacuum
     */
//    public static final int C = 300000000;
    public static final int C = 299792458;//m.s^-1
    /**
     * Newtonian constant of gravitation
     */
    public static final double G = 6.6738480E-11; //m3·kg^−1·s^−2;
    /**
     * Planck constant
     */
    public static final double H = 6.6260695729E-34; //J·s;
    /**
     * Reduced Planck constant
     */
    public static final double Hr = H / (2 * PI); //J·s;
    /**
     * magnetic constant (vacuum permeability)
     */
    public static final double U0 = Math.PI * 4e-7; //N·A−2
    /**
     * electric constant (vacuum permittivity) =1/(u0*C^2)
     */
    public static final double EPS0 = 8.854187817e-12;//F·m−1
    /**
     * characteristic impedance of vacuum =1/(u0*C)
     */
    public static final double Z0 = 1 / (U0 * C);//F·m−1
    /**
     * Coulomb's constant
     */
    public static final double Ke = 1 / (4 * PI * EPS0);//F·m−1
    /**
     * elementary charge
     */
    public static final double Qe = 1.60217656535E-19;//C
    public static final VectorSpace<Complex> COMPLEX_VECTOR_SPACE = new ComplexVectorSpace();
    public static final VectorSpace<Expr> EXPR_VECTOR_SPACE = new ExprVectorSpace();
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int Z_AXIS = 2;
    private static final int ARCH_MODEL_BITS = Integer.valueOf(
            System.getProperty("sun.arch.data.model") != null ? System.getProperty("sun.arch.data.model") :
                    System.getProperty("os.arch").contains("64") ? "64" : "32"
    );
    private static final int BYTE_BITS = 8;
    private static final int WORD = ARCH_MODEL_BITS / BYTE_BITS;
    private static final int JOBJECT_MIN_SIZE = 16;
    public static DistanceStrategy<Double> DISTANCE_DOUBLE = new DistanceStrategy<Double>() {
        @Override
        public double distance(Double a, Double b) {
            return Math.abs(b - a);
        }
    };
    public static DistanceStrategy<Complex> DISTANCE_COMPLEX = Complex.DISTANCE;
    public static DistanceStrategy<Matrix> DISTANCE_MATRIX = new DistanceStrategy<Matrix>() {
        @Override
        public double distance(Matrix a, Matrix b) {
            return a.getError(b);
        }
    };
    public static DistanceStrategy<Vector> DISTANCE_VECTOR = new DistanceStrategy<Vector>() {
        @Override
        public double distance(Vector a, Vector b) {
            return a.toMatrix().getError(b.toMatrix());
        }
    };
    //</editor-fold>

    static {
        MathsInitializerService.initialize();
        ServiceLoader<HadrumathsService> loader = ServiceLoader.load(HadrumathsService.class);
        for (HadrumathsService hadrumathsService : loader) {
            try {
                hadrumathsService.installService();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //    public static String getAxisLabel(int axis){
//        switch(axis){
//            case X_AXIS:return "X";
//            case Y_AXIS:return "Y";
//            case Z_AXIS:return "Z";
//        }
//        throw new IllegalArgumentException("Unknown Axis "+axis);
//    }


    private Maths() {
    }

    //<editor-fold desc="parameters functions">
    public static DoubleParam param(String name) {
        return new DoubleParam(name);
    }

    public static DoubleArrayParamSet doubleParamSet(Param param) {
        return new DoubleArrayParamSet(param);
    }

    public static DoubleArrayParamSet paramSet(Param param, double[] values) {
        return new DoubleArrayParamSet(param, values);
    }

    public static FloatArrayParamSet paramSet(Param param, float[] values) {
        return new FloatArrayParamSet(param, values);
    }

    public static FloatArrayParamSet floatParamSet(Param param) {
        return new FloatArrayParamSet(param);
    }

    public static LongArrayParamSet paramSet(Param param, long[] values) {
        return new LongArrayParamSet(param, values);
    }

    public static LongArrayParamSet longParamSet(Param param) {
        return new LongArrayParamSet(param);
    }

    public static <T> ArrayParamSet<T> paramSet(Param param, T[] values) {
        return new ArrayParamSet<T>(param, values);
    }

    public static <T> ArrayParamSet<T> objectParamSet(Param param) {
        return new ArrayParamSet<T>(param);
    }

    public static IntArrayParamSet paramSet(Param param, int[] values) {
        return new IntArrayParamSet(param, values);
    }

    public static IntArrayParamSet intParamSet(Param param) {
        return new IntArrayParamSet(param);
    }

    public static BooleanArrayParamSet paramSet(Param param, boolean[] values) {
        return new BooleanArrayParamSet(param, values);
    }

    public static BooleanArrayParamSet boolParamSet(Param param) {
        return new BooleanArrayParamSet(param);
    }

    public static XParamSet xParamSet(int xsamples) {
        return new XParamSet(xsamples);
    }

    public static XParamSet xyParamSet(int xsamples, int ysamples) {
        return new XParamSet(xsamples, ysamples);
    }

    public static XParamSet xyzParamSet(int xsamples, int ysamples, int zsamples) {
        return new XParamSet(xsamples, ysamples, zsamples);
    }
    //</editor-fold>

    //<editor-fold desc="Matrix functions">
    public static Matrix zerosMatrix(Matrix other) {
        return Config.defaultMatrixFactory.newZeros(other);
    }

    public static Matrix constantMatrix(int dim, Complex value) {
        return Config.defaultMatrixFactory.newConstant(dim, value);
    }

    public static Matrix onesMatrix(int dim) {
        return Config.defaultMatrixFactory.newOnes(dim);
    }

    public static Matrix onesMatrix(int rows, int cols) {
        return Config.defaultMatrixFactory.newOnes(rows, cols);
    }

    public static Matrix constantMatrix(int rows, int cols, Complex value) {
        return Config.defaultMatrixFactory.newConstant(rows, cols, value);
    }

    public static Matrix zerosMatrix(int dim) {
        return Config.defaultMatrixFactory.newZeros(dim);
    }

    public static Matrix I(Complex[][] iValue) {
        return matrix(iValue).mul(I);
    }


    public static Matrix zerosMatrix(int rows, int cols) {
        return Config.defaultMatrixFactory.newZeros(rows, cols);
    }

    public static Matrix identityMatrix(Matrix c) {
        return Config.defaultMatrixFactory.newIdentity(c);
    }

    public static Matrix NaNMatrix(int dim) {
        return Config.defaultMatrixFactory.newNaN(dim);
    }

    public static Matrix NaNMatrix(int rows, int cols) {
        return Config.defaultMatrixFactory.newNaN(rows, cols);
    }

    public static Matrix identityMatrix(int dim) {
        return Config.defaultMatrixFactory.newIdentity(dim);
    }

    public static Matrix identityMatrix(int rows, int cols) {
        return Config.defaultMatrixFactory.newIdentity(rows, cols);
    }

    public static Matrix matrix(Matrix matrix) {
        return Config.defaultMatrixFactory.newMatrix(matrix);
    }

    public static Matrix matrix(String string) {
        return Config.defaultMatrixFactory.newMatrix(string);
    }

    public static Matrix matrix(Complex[][] complex) {
        return Config.defaultMatrixFactory.newMatrix(complex);
    }

    public static Matrix matrix(double[][] complex) {
        return Config.defaultMatrixFactory.newMatrix(complex);
    }

    public static Matrix matrix(int rows, int cols, CellFactory cellFactory) {
        return Config.defaultMatrixFactory.newMatrix(rows, cols, cellFactory);
    }

//    public static Matrix matrix(int rows, int cols, Int2ToComplex cellFactory) {
//        return Config.defaultMatrixFactory.newMatrix(rows, cols, new I2ToComplexCellFactory(cellFactory));
//    }

    public static Matrix columnMatrix(final Complex... values) {
        return Config.defaultMatrixFactory.newColumnMatrix(values);
    }

    public static Matrix columnMatrix(final double... values) {
        Complex[] d = new Complex[values.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = Complex.valueOf(values[i]);
        }
        return Config.defaultMatrixFactory.newColumnMatrix(d);
    }

    public static Matrix rowMatrix(final double... values) {
        Complex[] d = new Complex[values.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = Complex.valueOf(values[i]);
        }
        return Config.defaultMatrixFactory.newRowMatrix(d);
    }

    public static Matrix rowMatrix(final Complex... values) {
        return Config.defaultMatrixFactory.newRowMatrix(values);
    }

    public static Matrix columnMatrix(int rows, final VCellFactory cellFactory) {
        return Config.defaultMatrixFactory.newColumnMatrix(rows, cellFactory);
    }

    public static Matrix rowMatrix(int columns, final VCellFactory cellFactory) {
        return Config.defaultMatrixFactory.newRowMatrix(columns, cellFactory);
    }

//    public static Matrix columnMatrix(int rows, final Int2Complex cellFactory) {
//        return Config.defaultMatrixFactory.newColumnMatrix(rows, new Int2ComplexCellFactory(cellFactory));
//    }
//    public static Matrix rowMatrix(int columns, final Int2Complex cellFactory) {
//        return Config.defaultMatrixFactory.newRowMatrix(columns, new Int2ComplexCellFactory(cellFactory));
//    }
//
//    public static Matrix symmetricMatrix(int rows, int cols, Int2ToComplex cellFactory) {
//        return Config.defaultMatrixFactory.newSymmetric(rows, cols, new I2ToComplexCellFactory(cellFactory));
//    }

    public static Matrix symmetricMatrix(int rows, int cols, CellFactory cellFactory) {
        return Config.defaultMatrixFactory.newSymmetric(rows, cols, cellFactory);
    }

    public static Matrix hermitianMatrix(int rows, int cols, CellFactory cellFactory) {
        return Config.defaultMatrixFactory.newHermitian(rows, cols, cellFactory);
    }

//    public static Matrix hermitianMatrix(int rows, int cols, Int2ToComplex cellFactory) {
//        return Config.defaultMatrixFactory.newHermitian(rows, cols, new I2ToComplexCellFactory(cellFactory));
//    }

    public static Matrix diagonalMatrix(int rows, int cols, CellFactory cellFactory) {
        return Config.defaultMatrixFactory.newDiagonal(rows, cols, cellFactory);
    }

    public static Matrix diagonalMatrix(int rows, final VCellFactory cellFactory) {
        return Config.defaultMatrixFactory.newDiagonal(rows, cellFactory);
    }

    public static Matrix diagonalMatrix(final Complex... c) {
        return Config.defaultMatrixFactory.newDiagonal(c);
    }

    public static Matrix matrix(int dim, CellFactory cellFactory) {
        return Config.defaultMatrixFactory.newMatrix(dim, cellFactory);
    }

//    public static Matrix matrix(int dim, Int2ToComplex cellFactory) {
//        return Config.defaultMatrixFactory.newMatrix(dim, new I2ToComplexCellFactory(cellFactory));
//    }

    public static Matrix matrix(int rows, int columns) {
        return Config.defaultMatrixFactory.newMatrix(rows, columns);
    }

    public static Matrix symmetricMatrix(int dim, CellFactory cellFactory) {
        return Config.defaultMatrixFactory.newMatrix(dim, cellFactory);
    }

    public static Matrix hermitianMatrix(int dim, CellFactory cellFactory) {
        return Config.defaultMatrixFactory.newHermitian(dim, cellFactory);
    }

    public static Matrix diagonalMatrix(int dim, CellFactory cellFactory) {
        return Config.defaultMatrixFactory.newDiagonal(dim, cellFactory);
    }

//    public static Matrix symmetricMatrix(int dim, Int2ToComplex cellFactory) {
//        return Config.defaultMatrixFactory.newMatrix(dim, new I2ToComplexCellFactory(cellFactory));
//    }

//    public static Matrix hermitianMatrix(int dim, Int2ToComplex cellFactory) {
//        return Config.defaultMatrixFactory.newHermitian(dim, new I2ToComplexCellFactory(cellFactory));
//    }

//    public static Matrix diagonalMatrix(int dim, Int2ToComplex cellFactory) {
//        return Config.defaultMatrixFactory.newDiagonal(dim, new I2ToComplexCellFactory(cellFactory));
//    }

    public static Matrix randomRealMatrix(int m, int n) {
        return Config.defaultMatrixFactory.newRandomReal(m, n);
    }

    public static Matrix randomRealMatrix(int m, int n, int min, int max) {
        return Config.defaultMatrixFactory.newRandomReal(m, n, min, max);
    }

    public static Matrix randomRealMatrix(int m, int n, double min, double max) {
        return Config.defaultMatrixFactory.newRandomReal(m, n, min, max);
    }

    public static Matrix randomImagMatrix(int m, int n, double min, double max) {
        return Config.defaultMatrixFactory.newRandomImag(m, n, min, max);
    }

    public static Matrix randomImagMatrix(int m, int n, int min, int max) {
        return Config.defaultMatrixFactory.newRandomImag(m, n, min, max);
    }

    public static Matrix randomMatrix(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        return Config.defaultMatrixFactory.newRandom(m, n, minReal, maxReal, minImag, maxImag);
    }

    public static Matrix randomMatrix(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        return Config.defaultMatrixFactory.newRandom(m, n, minReal, maxReal, minImag, maxImag);
    }

    public static Matrix randomMatrix(int m, int n, double min, double max) {
        return Config.defaultMatrixFactory.newRandom(m, n, min, max);
    }

    public static Matrix randomMatrix(int m, int n, int min, int max) {
        return Config.defaultMatrixFactory.newRandom(m, n, min, max);
    }

    public static Matrix randomImagMatrix(int m, int n) {
        return Config.defaultMatrixFactory.newRandomImag(m, n);
    }

    public static <T> TMatrix<T> loadTMatrix(Class<T> componentType, File file) throws IOException {
        throw new IllegalArgumentException("TODO");
    }

    public static Matrix loadMatrix(File file) throws IOException {
        return Config.defaultMatrixFactory.load(file);
    }

    public static Matrix matrix(File file) throws IOException {
        return Config.defaultMatrixFactory.load(file);
    }

    public static void storeMatrix(Matrix m, String file) throws IOException {
        m.store(file == null ? (File) null : new File(file));
    }

    public static void storeMatrix(Matrix m, File file) throws IOException {
        m.store(file);
    }

    public static Matrix loadMatrix(String file) throws IOException {
        return Config.defaultMatrixFactory.load(new File(file));
    }

    public static Matrix inv(Matrix c) {
        return c.inv();
    }

    public static Matrix tr(Matrix c) {
        return c.transpose();
    }

    public static Matrix trh(Matrix c) {
        return c.transposeHermitian();
    }

    public static Matrix transpose(Matrix c) {
        return c.transpose();
    }

    public static Matrix transposeHermitian(Matrix c) {
        return c.transposeHermitian();
    }

    //</editor-fold>

    //<editor-fold desc="Vector functions">
    public static Vector rowVector(Complex[] elems) {
        return ArrayVector.Row(elems);
    }

    public static Vector constantColumnVector(int size, Complex c) {
        Complex[] arr = new Complex[size];
        for (int i = 0; i < size; i++) {
            arr[i] = c;
        }
        return ArrayVector.Column(arr);
    }

    public static Vector constantRowVector(int size, Complex c) {
        Complex[] arr = new Complex[size];
        for (int i = 0; i < size; i++) {
            arr[i] = c;
        }
        return ArrayVector.Row(arr);
    }

    public static Vector zerosColumnVector(int size) {
        return constantColumnVector(size, CZERO);
    }

    public static Vector zerosRowVector(int size) {
        return constantRowVector(size, CZERO);
    }

    public static Vector NaNColumnVector(int dim) {
        return constantColumnVector(dim, CNaN);
    }

    public static Vector NaNRowVector(int dim) {
        return constantRowVector(dim, CNaN);
    }


    public static Vector columnVector(int rows, final VCellFactory cellFactory) {
        Complex[] arr = new Complex[rows];
        for (int i = 0; i < rows; i++) {
            arr[i] = cellFactory.item(i);
        }
        return columnVector(arr);
    }

    public static Vector rowVector(int columns, final VCellFactory cellFactory) {
        Complex[] arr = new Complex[columns];
        for (int i = 0; i < columns; i++) {
            arr[i] = cellFactory.item(i);
        }
        return rowVector(arr);
    }

//    public static Vector columnVector(int rows, final Int2Complex cellFactory) {
//        Int2ComplexCellFactory cc = new Int2ComplexCellFactory(cellFactory);
//        Complex[] arr = new Complex[rows];
//        for (int i = 0; i < rows; i++) {
//            arr[i] = cc.item(i);
//        }
//        return columnVector(arr);
//    }

//    public static Vector rowVector(int columns, final Int2Complex cellFactory) {
//        Int2ComplexCellFactory cc = new Int2ComplexCellFactory(cellFactory);
//        Complex[] arr = new Complex[columns];
//        for (int i = 0; i < columns; i++) {
//            arr[i] = cc.item(i);
//        }
//        return rowVector(arr);
//    }


    public static Vector columnVector(Complex[] elems) {
        return ArrayVector.Column(elems);
    }

    public static Vector column(Complex[] elems) {
        return ArrayVector.Column(elems);
    }

    public static Vector row(Complex[] elems) {
        return ArrayVector.Row(elems);
    }

    public static Vector trh(Vector c) {
        return c.transpose().conj();
    }

    public static Vector tr(Vector c) {
        return c.transpose();
    }


    //</editor-fold>

    //<editor-fold desc="Complex functions">
    public static Complex I(double iValue) {
        return Complex.I(iValue);
    }

    public static Complex abs(Complex a) {
        return (a.abs());
    }

    public static double absdbl(Complex a) {
        return a.absdbl();
    }
    //</editor-fold>


    public static double[] getColumn(double[][] a, int index) {
        int maxi = a.length;
        double[] ret = new double[maxi];
        for (int i = 0; i < maxi; i++) {
            ret[i] = a[i][index];
        }
        return ret;
    }

    /**
     * iterates from 'min' to 'max' 'times' times, the step of descritisation is
     * specified by maxTimes this is useful if we want to reuse older
     * calculations so we do for instance dtimes(0, 10, 2, 10) ==> 0,10
     * dtimes(0, 10, 4, 10) ==> 0,3,7,10 (reuse all from 0,1,2,3, ...,10)
     * dtimes(0, 10, 9, 10) ==> 0,2,3,...,9,10 (reuse all from 0,1,2,3, ...,10)
     *
     * @param min
     * @param max
     * @param maxTimes
     * @param times
     * @param strategy
     * @return
     */
    public static double[] dtimes(double min, double max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return subArray1(dtimes(min, max, maxTimes), times, strategy);
    }

    //    public static double log10(double nbr,double base){
//        return Math.log(nbr)/Math.log(base);
//    }
//    public static double log10(double nbr){
//        return Math.log(nbr)/Math.log(10);
//    }

    public static double[] dtimes(double min, double max, int times) {
        double[] d = new double[times];
        if (times == 1) {
            d[0] = min;
        } else {
            double step = (max - min) / (times - 1);
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
        }
        return d;
    }

    public static float[] ftimes(float min, float max, int times) {
        float[] d = new float[times];
        if (times == 1) {
            d[0] = min;
        } else {
            float step = (max - min) / (times - 1);
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
        }
        return d;
    }

    public static long[] ltimes(long min, long max, int times) {
        long[] d = new long[times];
        if (times == 1) {
            d[0] = min;
        } else {
            long step = (max - min) / (times - 1);
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
        }
        return d;
    }

    public static long[] lsteps(long min, long max, long step) {
        int times = (int) Math.abs((max - min) / step) + 1;
        long[] d = new long[times];
        for (int i = 0; i < d.length; i++) {
            d[i] = min + i * step;
        }
        return d;
    }

    public static int[] itimes(int min, int max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return subArray1(itimes(min, max, maxTimes), times, strategy);
    }

    public static double[] dsteps(int max) {
        return dsteps(0, max, 1);
    }

    public static double[] dsteps(double min, double max) {
        return dsteps(min, max, 1);
    }

    public static double[] dsteps(double min, double max, double step) {
        if (step >= 0) {
            if (max < min) {
                return new double[0];
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            double[] d = new double[times];
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
            return d;
        } else {
            if (min < max) {
                return new double[0];
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            double[] d = new double[times];
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
            return d;
        }
    }

    public static float[] fsteps(float min, float max, float step) {
        if (max < min) {
            return new float[0];
        }
        int times = (int) Math.abs((max - min) / step) + 1;
        float[] d = new float[times];
        for (int i = 0; i < d.length; i++) {
            d[i] = min + i * step;
        }
        return d;
    }

    public static int[] isteps(int max) {
        return isteps(0, max, 1);
    }

    public static int[] isteps(int min, int max) {
        return isteps(min, max, 1);
    }

    public static int[] isteps(int min, int max, int step) {
        if (max < min) {
            return new int[0];
        }
        int times = Math.abs((max - min) / step) + 1;
        int[] d = new int[times];
        for (int i = 0; i < d.length; i++) {
            d[i] = min + i * step;
        }
        return d;
    }

    public static int[] itimes(int min, int max) {
        return itimes(min, max, max - min + 1);
    }

    public static int[] itimes(int min, int max, int times) {
        int[] d = new int[times];
        if (times == 1) {
            d[0] = min;
        } else {
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * (max - min) / (times - 1);
            }
        }
        return d;
    }

    /**
     * sqrt(a^2 + b^2) without under/overflow.
     *
     * @param a
     * @param b
     * @return
     */
    public static double hypot(double a, double b) {
        double r;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            r = Math.abs(a) * Math.sqrt(1 + r * r);
        } else if (b != 0) {
            r = a / b;
            r = Math.abs(b) * Math.sqrt(1 + r * r);
        } else {
            r = 0.0;
        }
        return r;
    }

    public static Complex sqr(Complex d) {
        return d.sqr();
    }


//	public static double sinh(double x){
//		return (Math.exp(x) - Math.exp(-x))/2;
//	}
//
//	public static double cosh(double x){
//		return (Math.exp(x) + Math.exp(-x))/2;
//	}
//
//	public static double tanh(double x){
//		double a=Math.exp(x);
//		double b=Math.exp(-x);
//		return (a - b)/(a + b);
//	}
//
//	public static double cotanh(double x){
//		double a=Math.exp(x);
//		double b=Math.exp(-x);
//		return (a + b)/(a - b);
//	}

//    public static int signOf(double d){
//        return d<0?-1:d>0?1 : 0;
//    }

    public static double sqr(double d) {
        return d * d;
    }

    public static int sqr(int d) {
        return d * d;
    }

    public static long sqr(long d) {
        return d * d;
    }

    public static float sqr(float d) {
        return d * d;
    }

    public static double sincard(double value) {
        return value == 0 ? 1 : (sin2(value) / value);
    }

    public static int minusOnePower(int pow) {
        return (pow % 2 == 0) ? 1 : -1;
    }

    public static Complex exp(Complex c) {
        return c.exp();
    }

    public static Complex sin(Complex c) {
        return c.sin();
    }

    public static Complex sinh(Complex c) {
        return c.sinh();
    }

    public static Complex cos(Complex c) {
        return c.cos();
    }

    public static Complex log(Complex c) {
        return c.log();
    }

    public static Complex log10(Complex c) {
        return c.log10();
    }

    public static double log10(double c) {
        return Math.log10(c);
    }

    public static double log(double c) {
        return Math.log(c);
    }

    public static double acotan(double c) {
        return 1.0 / Math.atan(c);
    }

    public static double exp(double c) {
        return Math.exp(c);
    }

    public static double arg(double c) {
        return 0;
    }

    public static Complex db(Complex c) {
        return c.db();
    }

    public static Complex db2(Complex c) {
        return c.db2();
    }

    public static Complex cosh(Complex c) {
        return c.cosh();
    }

    public static Complex sum(Complex... c) {
        MutableComplex x = new MutableComplex(0, 0);
        for (Complex c1 : c) {
            x.add(c1);
        }
        return x.toComplex();
    }


    /**
     * ----------- from colt Evaluates the series of Chebyshev polynomials Ti at
     * argument x/2. The series is given by
     * <pre>
     *        N-1
     *         - '
     *  y  =   >   coef[i] T (x/2)
     *         -            i
     *        i=0
     * </pre> Coefficients are stored in reverse order, i.e. the zero order term
     * is last in the array. Note N is the number of coefficients, not the
     * order.
     *
     * If coefficients are for the interval a to b, x must have been transformed
     * to x -> 2(2x - b - a)/(b-a) before entering the routine. This maps x from
     * (a, b) to (-1, 1), over which the Chebyshev polynomials are defined.
     *
     * If the coefficients are for the inverted interval, in which (a, b) is
     * mapped to (1/b, 1/a), the transformation required is x -> 2(2ab/x - b -
     * a)/(b-a). If b is infinity, this becomes x -> 4a/x - 1.
     *
     * SPEED:
     *
     * Taking advantage of the recurrence properties of the Chebyshev
     * polynomials, the routine requires one more addition per loop than
     * evaluating a nested polynomial of the same degree.
     *
     * @param x    argument to the polynomial.
     * @param coef the coefficients of the polynomial.
     * @param N    the number of coefficients.
     */
    public static double chbevl(double x, double coef[], int N) throws ArithmeticException {
        double b0, b1, b2;

        int p = 0;
        int i;

        b0 = coef[p++];
        b1 = 0.0;
        i = N - 1;

        do {
            b2 = b1;
            b1 = b0;
            b0 = x * b1 - b2 + coef[p++];
        } while (--i > 0);

        return (0.5 * (b0 - b2));
    }

    public static long pgcd(long a, long b) {
        long r, i;
        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public static int pgcd(int a, int b) {
        int r, i;
        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public static double[][] toDouble(Complex[][] c, ComplexAsDouble complexAsDouble) {
        double[][] z = null;
        if (c != null) {
            switch (complexAsDouble) {
                case ABS: {
                    z = ArrayUtils.absdbl(c);
                    break;
                }
                case REAL: {
                    z = ArrayUtils.getReal(c);
                    break;
                }
                case IMG: {
                    z = ArrayUtils.getImag(c);
                    break;
                }
                case DB: {
                    z = ArrayUtils.getDb(c);
                    break;
                }
                case DB2: {
                    z = ArrayUtils.getDb2(c);
                    break;
                }
                case COMPLEX: {
                    z = ArrayUtils.absdbl(c);
                    break;
                }
            }
        }
        return z;
    }

    public static double[] toDouble(Complex[] c, ComplexAsDouble complexAsDouble) {
        double[] z = null;
        if (c != null) {
            switch (complexAsDouble) {
                case ABS: {
                    z = ArrayUtils.absdbl(c);
                    break;
                }
                case REAL: {
                    z = ArrayUtils.getReal(c);
                    break;
                }
                case IMG: {
                    z = ArrayUtils.getImag(c);
                    break;
                }
                case DB: {
                    z = ArrayUtils.getDb(c);
                    break;
                }
                case DB2: {
                    z = ArrayUtils.getDb2(c);
                    break;
                }
                case COMPLEX: {
                    z = ArrayUtils.absdbl(c);
                    break;
                }
            }
        }
        return z;
    }

    /**
     * range closed min (inclusive) and closed max (inclusive)
     *
     * @param orderedValues array to look for range into. must be
     *                      <strong>ordered</strong>
     * @param min           min value accepted in range (inclusive)
     * @param max           max value accepted in range (inclusive)
     * @return array of two integers defining first and last indices (all
     * inclusive) accepted in range
     */
    public static int[] rangeCC(double[] orderedValues, double min, double max) {
        //System.out.printf("[%f..%f] from [%f,%f]=",range[0],range[range.length-1],min,max);
        int len = orderedValues.length;
        int a = Arrays.binarySearch(orderedValues, min);
        if (a < 0) {
            a = -a - 1;
            if (a == len) {
                //System.out.printf("0%n");
                return null;
            }
        }
        int b = Arrays.binarySearch(orderedValues, max);
        if (b < 0) {
            b = -b - 2;
            if (b < 0) {
                //System.out.printf("0%n");
                return null;
            } else if (b == len) {
                b = len - 1;
            }
        }
        //System.out.printf("[%f,%f] as [%d,%d]%n",range[a],range[b],a,b);
        return new int[]{a, b};
    }

//    public static void main(String[] args) {
////        System.out.println(DumpManager.dump(rangeCO(new double[]{1,2,3,4,5,6,7}, -2, 2.3)));
////        System.out.println(DumpManager.dump(rangeCO(new double[]{1,2,3,4,5,6,7}, -2, -1)));
////        System.out.println(DumpManager.dump(rangeCO(new double[]{1,2,3,4,5,6,7}, -2, 1)));
//        System.out.println(dump(rangeCO(new double[]{1, 2, 3, 4, 5, 6, 7}, -2, 1)));
//    }

    /**
     * range closed min (inclusive) and open max (exclusive)
     *
     * @param orderedValues array to look for range into. must be
     *                      <strong>ordered</strong>
     * @param min           min value accepted in range (inclusive)
     * @param max           max value accepted in range (exclusive)
     * @return array of two integers defining first and last indices (all
     * inclusive) accepted in range
     */
    public static int[] rangeCO(double[] orderedValues, double min, double max) {
        //System.out.printf("[%f..%f] from [%f,%f]=",range[0],range[range.length-1],min,max);
        if (orderedValues == null) {
            return null;
        }
        int len = orderedValues.length;
        int a = Arrays.binarySearch(orderedValues, min);
        if (a < 0) {
            a = -a - 1;
            if (a == len) {
                //System.out.printf("0%n");
                return null;
            }
        }
        int b = Arrays.binarySearch(orderedValues, max);
        if (b < 0) {
            b = -b - 2;
            if (b < 0) {
                //System.out.printf("0%n");
                return null;
            } else if (b == len) {
                b = len - 1;
            }
        } else if (b == 0) {
            return null;
        } else {
            b = b - 1;
        }
        if (b < a) {
            return null;
        }
        //System.out.printf("[%f,%f] as [%d,%d]%n",range[a],range[b],a,b);
        return new int[]{a, b};
    }

    //    public static double scalarProduct(DFunctionVector2D f1, DFunctionVector2D f2) {
//        return defaultScalarProductOperator.process(f1, f2);
//    }
//
//    public static double scalarProduct(DomainXY domain, DFunctionVector2D f1, DFunctionVector2D f2) {
//        return defaultScalarProductOperator.process(domain, f1, f2);
//    }
//    public static Complex scalarProduct(IVDCxy f1, IVDCxy f2) {
//        return defaultScalarProductOperator.process(f1, f2);
//    }
//    public static Complex scalarProduct(DomainXY domain, IVDCxy f1, IVDCxy f2) {
//        return defaultScalarProductOperator.process(domain, f1, f2);
//    }
    public static Complex csqrt(double d) {
        return Complex.valueOf(d).sqrt();
    }

    public static Complex sqrt(Complex d) {
        return d.sqrt();
    }

    public static double dsqrt(double d) {
        return Math.sqrt(d);
    }

    public static Complex cotanh(Complex c) {
        return c.cotanh();
    }

    public static Complex tanh(Complex c) {
        return c.tanh();
    }

    public static Complex inv(Complex c) {
        return c.inv();
    }


    public static Complex tan(Complex c) {
        return c.tan();
    }

    public static Complex cotan(Complex c) {
        return c.cotan();
    }

    private static double[] subArray1(double[] values, int count, IndexSelectionStrategy sel) {
        switch (sel) {
            case BALANCED: {
                int[] ints = itimes(0, values.length - 1, count);
                double[] xx = new double[ints.length];
                for (int i = 0; i < ints.length; i++) {
                    xx[i] = values[ints[i]];
                }
                return xx;
            }
            case FIRST: {
                double[] xx = new double[count];
                System.arraycopy(values, 0, xx, 0, count);
                return xx;
            }
            case LAST: {
                double[] xx = new double[count];
                System.arraycopy(values, values.length - count, xx, 0, count);
                return xx;
            }
        }
        return null;
    }

    private static int[] subArray1(int[] values, int count, IndexSelectionStrategy sel) {
        switch (sel) {
            case BALANCED: {
                int[] ints = itimes(0, values.length - 1, count);
                int[] xx = new int[ints.length];
                for (int i = 0; i < ints.length; i++) {
                    xx[i] = values[ints[i]];
                }
                return xx;
            }
            case FIRST: {
                int[] xx = new int[count];
                System.arraycopy(values, 0, xx, 0, count);
                return xx;
            }
            case LAST: {
                int[] xx = new int[count];
                System.arraycopy(values, values.length - count, xx, 0, count);
                return xx;
            }
        }
        return null;
    }


    public static Complex pow(Complex a, Complex b) {
        return a.pow(b);
    }

    public static Complex div(Complex a, Complex b) {
        return a.div(b);
    }

    public static Complex add(Complex a, Complex b) {
        return a.add(b);
    }

    public static Complex sub(Complex a, Complex b) {
        return a.sub(b);
    }


    public static double norm(Matrix a) {
        return a.norm(NormStrategy.DEFAULT);
    }

    public static double norm(Vector a) {
        return a.norm();
    }

    public static double norm1(Matrix a) {
        return a.norm1();
    }

    public static double norm2(Matrix a) {
        return a.norm2();
    }

    public static double norm3(Matrix a) {
        return a.norm3();
    }

    public static double normInf(Matrix a) {
        return a.normInf();
    }


    public static DoubleToComplex complex(DoubleToDouble fx) {
        if (fx.isZero()) {
            return DCZERO;
        }
        if (fx instanceof DoubleToComplex) {
            return (DoubleToComplex) fx;
        }
        return DCxy.valueOf(fx);
    }

    public static DoubleToComplex complex(DoubleToDouble fx, DoubleToDouble fy) {
        return DCxy.valueOf(fx, fy);
    }


    public static double randomDouble(double value) {
        return value * Math.random();
    }

    public static double randomDouble(double min, double max) {
        return min + ((max - min) * Math.random());
    }

    public static int randomInt(int value) {
        return (int) (value * Math.random());
    }

    public static int randomInt(int min, int max) {
        return (int) (min + ((max - min) * Math.random()));
    }

    public static Complex randomComplex() {
        double r = Math.random();
        double p = Math.random() * 2 * PI;
        return Complex.valueOf(r * cos(r), r * sin(p));
    }

    public static boolean randomBoolean() {
        return Math.random() <= 0.5;
    }

    //    public static Domain domain(double xmin, double xmax) {
//        return Domain.forBounds(xmin, xmax);
//    }
//
//    public static Domain domain(double xmin, double xmax, double ymin, double ymax) {
//        return Domain.forBounds(xmin, xmax, ymin, ymax);
//    }
//
//    public static Domain domain(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
//        return Domain.forBounds(xmin, xmax, ymin, ymax,zmin,zmax);
//    }
//    public static Domain cornerBounds(double xmin, double ymin, double xmax, double ymax) {
//        return Domain.forBounds(xmin, xmax, ymin, ymax);
//    }
//
//    public static Domain cornerSizes(double xmin, double ymin, double xlen, double ylen) {
//        return Domain.forBounds(xmin, xmin + xlen, ymin, ymin + ylen);
//    }

    public static double[][] cross(double[] x, double[] y) {
        double[][] r = new double[x.length * y.length][2];
        int p = 0;
        for (double aX : x) {
            for (double aY : y) {
                double[] v = r[p];
                v[0] = aX;
                v[1] = aY;
                p++;
            }
        }
        return r;
    }

    public static double[][] cross(double[] x, double[] y, double[] z) {
        double[][] r = new double[x.length * y.length * z.length][3];
        int p = 0;
        for (double aX : x) {
            for (double aY : y) {
                for (double aZ : z) {
                    double[] v = r[p];
                    v[0] = aX;
                    v[1] = aY;
                    v[2] = aZ;
                    p++;
                }
            }
        }
        return r;
    }

    public static double[][] cross(double[] x, double[] y, double[] z, Double3Filter filter) {
        List<double[]> r = new ArrayList<>(x.length * y.length * z.length);
        for (double aX : x) {
            for (double aY : y) {
                for (double aZ : z) {
                    double[] v = new double[3];
                    v[0] = aX;
                    v[1] = aY;
                    v[2] = aZ;
                    if (filter == null || filter.accept(aX, aY, aZ)) {
                        r.add(v);
                    }
                }
            }
        }
        return r.toArray(new double[r.size()][]);
    }

    public static int[][] cross(int[] x, int[] y) {
        int[][] r = new int[x.length * y.length][2];
        int p = 0;
        for (int aX : x) {
            for (int aY : y) {
                int[] v = r[p];
                v[0] = aX;
                v[1] = aY;
                p++;
            }
        }
        return r;
    }

    public static int[][] cross(int[] x, int[] y, int[] z) {
        int[][] r = new int[x.length * y.length * z.length][3];
        int p = 0;
        for (int aX : x) {
            for (int aY : y) {
                for (int aZ : z) {
                    int[] v = r[p];
                    v[0] = aX;
                    v[1] = aY;
                    v[2] = aZ;
                    p++;
                }
            }
        }
        return r;
    }

    public static ExprList sineSeq(String borders, int m, int n, Domain domain) {
        return sineSeq(borders, m, n, domain, PlaneAxis.XY);
    }

    public static ExprList sineSeq(String borders, int m, int n, Domain domain, PlaneAxis plane) {
        DoubleParam mm = new DoubleParam("m");
        DoubleParam nn = new DoubleParam("n");
        switch (plane) {
            case XY: {
                return ParamExprList.create(new SinSeqXY(borders, mm, nn, domain), new DoubleParam[]{mm, nn}, new int[]{m, n});
            }
            case XZ: {
                return ParamExprList.create(new SinSeqXZ(borders, mm, nn, domain), new DoubleParam[]{mm, nn}, new int[]{m, n});
            }
            case YZ: {
                return ParamExprList.create(new SinSeqYZ(borders, mm, nn, domain), new DoubleParam[]{mm, nn}, new int[]{m, n});
            }
        }
        throw new IllegalArgumentException("Unsupported Plane " + plane);
    }

    public static Expr sineSeq(String borders, DoubleParam m, DoubleParam n, Domain domain) {
        return new SinSeqXY(borders, m, n, domain);
    }

    public static Expr rooftop(String borders, int nx, int ny, Domain domain) {
        return new Rooftop(borders, nx, ny, domain);
    }

    public static Any any(double e) {
        return any(expr(e));
    }

    public static Any any(Expr e) {
        return new Any(e);
    }

    public static Any any(Double e) {
        return new Any(expr(e));
    }

    public static Any any(Domain e) {
        return new Any(expr(e));
    }

    public static ExprList seq() {
        return new ArrayExprList();
    }

    public static ExprList seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax, Int2Filter filter) {
        double[][] cross = cross(dsteps(0, mmax), dsteps(0, nmax));
        if (filter != null) {
            List<double[]> list = new ArrayList<>();
            for (double[] cros : cross) {
                if (filter.accept((int) cros[0], (int) cros[1])) {
                    list.add(cros);
                }
            }
            double[][] cross2 = new double[list.size()][];
            for (int i = 0; i < list.size(); i++) {
                cross2[i] = list.get(i);
            }
            cross = cross2;
        }
        return seq(pattern, m, n, cross);
    }

    public static ExprList seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax, DoubleParam p, int pmax, Int3Filter filter) {
        double[][] cross = cross(dsteps(0, mmax), dsteps(0, nmax), dsteps(0, pmax), filter == null ? null : new Double3Filter() {
            @Override
            public boolean accept(double a, double b, double c) {
                return filter.accept((int) a, (int) b, (int) c);
            }
        });
        return seq(pattern, m, n, p, cross);
    }

    public static ExprList seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax) {
        return seq(pattern, m, n, cross(dsteps(0, mmax), dsteps(0, nmax)));
    }

    public static ExprList seq(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues) {
        return seq(pattern, m, n, cross(mvalues, nvalues));
    }

    public static ExprList seq(final Expr pattern, final DoubleParam m, final DoubleParam n, final double[][] values) {
        return Config.DEFAULT_EXPR_SEQ_FACTORY.newUnmodifiableSequence(values.length, new SimpleSeq2(values, m, n, pattern));
    }

    public static ExprList seq(final Expr pattern, final DoubleParam m, final DoubleParam n, final DoubleParam p, final double[][] values) {
        return Config.DEFAULT_EXPR_SEQ_FACTORY.newUnmodifiableSequence(values.length,
                new SimpleSeqMulti(pattern, new DoubleParam[]{m, n, p}, values)
        );
    }

    public static ExprList seq(final Expr pattern, final DoubleParam[] m, final double[][] values) {
        return Config.DEFAULT_EXPR_SEQ_FACTORY.newUnmodifiableSequence(values.length, new SimpleSeqMulti(pattern, m, values));
    }

    public static ExprList seq(final Expr pattern, final DoubleParam m, int min, int max) {
        return seq(pattern, m, dsteps(min, max, 1));
    }

    public static ExprList seq(final Expr pattern, final DoubleParam m, final double[] values) {
        return Config.DEFAULT_EXPR_SEQ_FACTORY.newUnmodifiableSequence(values.length, new SimpleSeq1(values, m, pattern));
    }

    public static ExprMatrix matrix(final Expr pattern, final DoubleParam m, final double[] mvalues, final DoubleParam n, final double[] nvalues) {
        return Config.DEFAULT_EXPR_MATRIX_FACTORY.newUnmodifiableMatrix(mvalues.length, nvalues.length, new SimpleSeq2b(pattern, m, mvalues, n, nvalues));
    }

    public static ExprCube cube(final Expr pattern, final DoubleParam m, final double[] mvalues, final DoubleParam n, final double[] nvalues, final DoubleParam p, final double[] pvalues) {
        return Config.DEFAULT_EXPR_CUBE_FACTORY.newUnmodifiableCube(mvalues.length, nvalues.length, pvalues.length, new SimpleSeq3b(pattern, m, mvalues, n, nvalues, p, pvalues));
    }

    //    public static void main(String[] args) {
//        DoubleParam m=param("m");
//        DoubleParam n=param("n");
//        DoubleParam p=param("p");
//        DoubleToVector v = vector(m, n, p);
////        System.out.println(DumpManager.dumpSimple(seq2(v, m, dsteps(1, 4), n, dsteps(1, 4))));
//        System.out.println(DumpManager.dumpSimple(seq3(v, m, dsteps(1, 2), n, dsteps(1, 3), p, dsteps(1, 4))));
//    }
//    public static ArrayExprList[] seq(ArrayExprList pattern, DoubleParam m, double[] values) {
//        ArrayExprList[] list = new ArrayExprList[values.length];
//        for (int i = 0; i < values.length; i++) {
//            list[i] = pattern.setParam(m, values[i]);
//        }
//        return list;
//    }
//
//    public static ArrayExprList[][] seq(ArrayExprList[] pattern, DoubleParam m, double[] values) {
//        ArrayExprList[][] list = new ArrayExprList[values.length][];
//        for (int i = 0; i < values.length; i++) {
//            ArrayExprList[] sub = new ArrayExprList[pattern.length];
//            for (int j = 0; j < sub.length; j++) {
//                sub[j] = pattern[j].setParam(m, values[i]);
//            }
//            list[i] = sub;
//        }
//        return list;
//    }
//
//    public static ArrayExprList[][][] seq(ArrayExprList[][] pattern, DoubleParam m, double[] values) {
//        ArrayExprList[][][] list = new ArrayExprList[values.length][][];
//        for (int i = 0; i < values.length; i++) {
//            ArrayExprList[][] sub = new ArrayExprList[pattern.length][];
//            for (int j = 0; j < sub.length; j++) {
//                ArrayExprList[] sub2 = new ArrayExprList[sub.length];
//                for (int k = 0; k < sub2.length; k++) {
//                    sub2[k] = pattern[j][k].setParam(m, values[i]);
//                }
//            }
//            list[i] = sub;
//        }
//        return list;
//    }
    //a koa ca sert??????? j'ai déja range
    @Deprecated
    public static int[] range2(double[] x, double min, double max) {
        //new
        int a = 0;

        int low = 0;
        int high = x.length - 1;

        while (low <= high) {
            int mid = (low + high) >> 1;
            double midVal = x[mid];

            int cmp;
            if (midVal < min) {
                cmp = -1;   // Neither val is NaN, thisVal is smaller
            } else if (midVal > min) {
                cmp = 1;    // Neither val is NaN, thisVal is larger
            } else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(min);
                cmp = (midBits == keyBits ? 0
                        : // Values are equal
                        (midBits < keyBits ? -1
                                : // (-0.0, 0.0) or (!NaN, NaN)
                                1));                     // (0.0, -0.0) or (NaN, !NaN)
            }

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                low = mid;
                break;
            }
        }
        a = low;

        int b = a;

        low = a;
        high = x.length - 1;

        while (low <= high) {
            int mid = (low + high) >> 1;
            double midVal = x[mid];

            int cmp;
            if (midVal < max) {
                cmp = -1;   // Neither val is NaN, thisVal is smaller
            } else if (midVal > max) {
                cmp = 1;    // Neither val is NaN, thisVal is larger
            } else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(max);
                cmp = (midBits == keyBits ? 0
                        : // Values are equal
                        (midBits < keyBits ? -1
                                : // (-0.0, 0.0) or (!NaN, NaN)
                                1));                     // (0.0, -0.0) or (NaN, !NaN)
            }

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                high = mid;
                break;
            }
        }
        b = high;

        return (a < 0 || a >= x.length || b < a) ? null : new int[]{a, b};
    }

    public static Expr derive(Expr f, Axis axis) {
        return Config.getFunctionDerivatorManager().derive(f, axis).simplify();
    }

    public static boolean isReal(Expr e) {
        return e.toDC().getImag().isZero();
    }

    public static boolean isImag(Expr e) {
        return e.toDC().getReal().isZero();
    }

    public static Expr abs(Expr e) {
        return EXPR_VECTOR_SPACE.abs(e);
    }

    public static Expr db(Expr e) {
        return EXPR_VECTOR_SPACE.db(e);
    }

    public static Expr db2(Expr e) {
        return EXPR_VECTOR_SPACE.db2(e);
    }

    public static Complex complex(Expr e) {
        return e.toComplex();
    }

    public static double Double(Expr e) {
        return e.toDouble();
    }

    public static Expr real(Expr e) {
        return EXPR_VECTOR_SPACE.real(e);
    }

    public static Expr imag(Expr e) {
        return EXPR_VECTOR_SPACE.imag(e);
    }

    public static ExprList imag(ExprList e) {
        ArrayExprList item2 = new ArrayExprList();
        for (Expr expr : e) {
            item2.add(imag(expr));
        }
        return item2;
    }

    public static ExprList real(ExprList e) {
        ArrayExprList item2 = new ArrayExprList();
        for (Expr expr : e) {
            item2.add(real(expr));
        }
        return item2;
    }

    public static Complex complexValue(Expr e) {
        return e.simplify().toComplex();
    }

    public static double doubleValue(Expr e) {
        return e.simplify().toDouble();
    }

    //    public static Discrete discrete(Domain domain, Complex value, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3) {
//        return Discrete.cst(domain, value, dx, dy, dz, axis1, axis2, axis3);
//    }
    public static Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3) {
        return Discrete.create(domain, model, x, y, z, dx, dy, dz, axis1, axis2, axis3);
    }

    public static Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz) {
        return Discrete.create(domain, model, x, y, z, dx, dy, dz);
    }

    public static Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z) {
        return Discrete.create(domain, model, x, y, z);
    }

    public static Discrete discrete(Domain domain, Complex[][][] model, double dx, double dy, double dz) {
        return Discrete.create(domain, model, dx, dy, dz);
    }

    public static Discrete discrete(Complex[][][] model, double[] x, double[] y, double[] z) {
        return Discrete.create(model, x, y, z);
    }

    public static Expr discretize(Expr expr, int xSamples, int ySamples, int zSamples) {
        if (expr.isScalarExpr()) {
            if (expr.isDD()) {
                return DDiscrete.discretize(expr, xSamples, ySamples, zSamples);
            } else {
                return Discrete.discretize(expr, xSamples, ySamples, zSamples);
            }
        } else {
            return VDiscrete.discretize(expr, xSamples, ySamples, zSamples);
        }
    }

    public static Expr discretize(Expr expr, Samples samples) {
        if (expr.isScalarExpr()) {
            if (expr.isDD()) {
                return DDiscrete.discretize(expr, null, samples);
            } else {
                return Discrete.discretize(expr, null, samples);
            }
        } else {
            return VDiscrete.discretize(expr, null, samples);
        }
    }

    public static Expr discrete(Expr expr, int xSamples, int ySamples) {
        if (expr instanceof Discrete || expr instanceof VDiscrete) {
            return expr;
        }
        if (expr.isScalarExpr()) {
            AbsoluteSamples samples = expr.getDomain().times(xSamples, ySamples);
            Complex[][][] model = expr.toDC().computeComplex(samples.getX(), samples.getY(), samples.getZ(), null, null);
            return Discrete.create(model, samples.getX(), samples.getY(), samples.getZ());
        } else {
            DoubleToVector v = expr.toDV();
            ComponentDimension d = v.getComponentDimension();
            if (d.columns == 1) {
                if (d.rows == 1) {
                    AbsoluteSamples samples = expr.getDomain().times(xSamples, ySamples);
                    Complex[][][] model = expr.toDC().computeComplex(samples.getX(), samples.getY(), samples.getZ(), null, null);
                    return Discrete.create(model, samples.getX(), samples.getY(), samples.getZ());
                } else if (d.rows == 2) {
                    return new VDiscrete(
                            (Discrete) discrete(v.getComponent(Axis.X), xSamples, ySamples),
                            (Discrete) discrete(v.getComponent(Axis.Y), xSamples, ySamples),
                            null
                    );
                } else if (d.rows == 3) {
                    return new VDiscrete(
                            (Discrete) discrete(v.getComponent(Axis.X), xSamples, ySamples),
                            (Discrete) discrete(v.getComponent(Axis.Y), xSamples, ySamples),
                            (Discrete) discrete(v.getComponent(Axis.Z), xSamples, ySamples)
                    );
                }
            }
            throw new IllegalArgumentException("Unsupported");

        }
    }

    public static AdaptiveResult1 adaptiveEval(Expr expr, AdaptiveConfig config) {
        Domain domain = expr.getDomain();
        switch (domain.getDimension()) {
            case 1: {
                if (expr.isDD()) {
                    return adaptiveEval(new AdaptiveFunction1<Double>() {
                        @Override
                        public Double eval(double x) {
                            return expr.toDD().computeDouble(x);
                        }
                    }, DISTANCE_DOUBLE, (DomainX) expr.getDomain().getDomainX(), config);
                }
                if (expr.isDC()) {
                    return adaptiveEval(new AdaptiveFunction1<Complex>() {
                        @Override
                        public Complex eval(double x) {
                            return expr.toDC().computeComplex(x);
                        }
                    }, Complex.DISTANCE, (DomainX) expr.getDomain().getDomainX(), config);
                }
            }
        }
        throw new IllegalArgumentException("Unsupported Dimension " + domain.getDimension());
    }

    public static <T> AdaptiveResult1 adaptiveEval(AdaptiveFunction1<T> expr, DistanceStrategy<T> distance, DomainX domain, AdaptiveConfig config) {
        if (config == null) {
            config = new AdaptiveConfig();
        }
        double xmin = domain.xmin();
        double xmax = domain.xmax();
        int minSteps = config.getMinimumXSamples();
        int maxSamples = config.getMaximumXSamples();
        double err = config.getError();
        SamplifyListener listener = config.getListener();
        double[] dsteps = dtimes(xmin, xmax, minSteps);
        if (err <= 0) {
            err = 0.1;
        }
        double yerr = 0.02;
//        DoubleToComplex dc = expr.toDC();
//        Complex[][] complexes2 = dc.computeComplex(dsteps,dsteps);
        AdaptiveResult1 s = new AdaptiveResult1();
        s.x.addAll(dsteps);
        for (double x : dsteps) {
            T v = expr.eval(x);
            s.values.add(v);
        }
        BooleanArray stop = new BooleanArray();

        for (int i = 0; i < s.size(); i++) {
            stop.add(false);
        }

        if (listener != null) {
            listener.onNewElements(new AdaptiveEvent(0, s.size(), Double.NaN, 0, s));
        }

        class Diff implements Comparable<Diff> {
            String name;
            int type;
            int minIndex = -1;
            int maxIndex = -1;
            double minValue = 0;
            double maxValue = Double.POSITIVE_INFINITY;
            double ratio = 0;

            public Diff(String name, int type) {
                this.name = name;
                this.type = type;
            }

            void regRegular(int index, double v) {
                if (Double.isNaN(v) || Double.isInfinite(v)) {
                    return;
                }
                reg(index, v);
            }

            void regRegularOrMax(int index, double v, double max) {
                if (Double.isNaN(v) || Double.isInfinite(v)) {
                    return;
                }
                if (v > max) {
                    v = max;
                }
                reg(index, v);
            }

            void reg(int index, double v) {
                if (minIndex < 0) {
                    minIndex = index;
                    maxIndex = index;
                    minValue = v;
                    maxValue = v;
                } else if (v < minValue) {
                    minValue = v;
                    minIndex = index;
                } else if (v > maxValue) {
                    maxValue = v;
                    maxIndex = index;
                }
                if (minValue == 0) {
                    ratio = maxValue;
                } else {
                    ratio = (maxValue - minValue) / minValue;
                }
            }

            @Override
            public String toString() {
                return "Diff{" +
                        "name='" + name + '\'' +
                        ", type=" + type +
                        ", minIndex=" + minIndex +
                        ", maxIndex=" + maxIndex +
                        ", minValue=" + minValue +
                        ", maxValue=" + maxValue +
                        ", ratio=" + ratio +
                        '}';
            }

            @Override
            public int compareTo(Diff o) {
                return Double.compare(ratio, o.ratio);
            }
        }

        int TYPE_ERROR = 1;
        int TYPE_WIDTH = 2;
        int TYPE_DERIVE = 3;
        while (s.x.size() < maxSamples) {
            Diff ediff = new Diff("error", TYPE_ERROR);//error diff
            Diff wdiff = new Diff("width", TYPE_WIDTH);//width diff
            Diff ddiff = new Diff("derive", TYPE_DERIVE);//derive diff
            List<Diff> diffs = new ArrayList<>(Arrays.asList(ediff, wdiff, ddiff));
//            double maxErr = 0;
//            double minErr = Double.POSITIVE_INFINITY;
//            double minWidth = Double.POSITIVE_INFINITY;
//            double maxWidth = 0;
//            int indexWithMaxError = -1;
//            int indexWithMinWidth = -1;
//            int indexWithMaxWidth = -1;
            int count = s.values.size();
            for (int i = count - 2; i >= 0; i--) {
                if (!stop.get(i)) {
                    T c1 = (T) s.values.get(i);
                    T c2 = (T) s.values.get(i + 1);
                    double d1 = s.x.get(i);
                    double d2 = s.x.get(i + 1);
                    double d = d2 - d1;
                    ediff.regRegularOrMax(i, distance.distance(c1, c2), 200);
                    wdiff.regRegular(i, d);
                    ddiff.regRegularOrMax(i, err / d, 200);
                }
            }
            Collections.sort(diffs);

            Diff worst = diffs.get(diffs.size() - 1);
            int bestIndex = worst.maxIndex;
            s.error = worst.ratio;
            if (bestIndex == -1) {
                break;
            }
            if (s.error < err) {
                stop.add(bestIndex, true);
                continue;
            }
            double d1 = s.x.get(bestIndex);
            double d2 = s.x.get(bestIndex + 1);
            T c1 = (T) s.values.get(bestIndex);
            T c2 = (T) s.values.get(bestIndex + 1);
            double d = (d1 + d2) / 2.0;
            if (d == d1 || d == d2) {
                stop.set(bestIndex, true);
            } else {
                T c = expr.eval(d);
                if (distance.distance(c, c1) <= yerr || distance.distance(c, c2) <= yerr) {
                    //no need to add this !
                    stop.add(bestIndex, true);
                } else {


                    s.x.add(bestIndex + 1, d);
                    s.values.add(bestIndex + 1, c);
                    stop.add(bestIndex + 1, false);
                    if (listener != null) {
                        listener.onNewElements(new AdaptiveEvent(bestIndex + 1, 1, worst.ratio, worst.type, s));
                    }
                }
            }
        }
        return s;
        //now check if i have to
    }

    public static Expr discrete(Expr expr, int xSamples) {
        if (expr instanceof Discrete || expr instanceof VDiscrete) {
            return expr;
        }
        if (expr.isScalarExpr()) {
            AbsoluteSamples samples = expr.getDomain().times(xSamples);
            Complex[][][] model = expr.toDC().computeComplex(samples.getX(), samples.getY(), samples.getZ(), null, null);
            return Discrete.create(model, samples.getX(), samples.getY(), samples.getZ());
        } else {
            DoubleToVector v = expr.toDV();
            ComponentDimension d = v.getComponentDimension();
            if (d.columns == 1) {
                if (d.rows == 1) {
                    AbsoluteSamples samples = expr.getDomain().times(xSamples);
                    Complex[][][] model = expr.toDC().computeComplex(samples.getX(), samples.getY(), samples.getZ(), null, null);
                    return Discrete.create(model, samples.getX(), samples.getY(), samples.getZ());
                } else if (d.rows == 2) {
                    return new VDiscrete(
                            (Discrete) discrete(v.getComponent(Axis.X), xSamples),
                            (Discrete) discrete(v.getComponent(Axis.Y), xSamples),
                            null
                    );
                } else if (d.rows == 3) {
                    return new VDiscrete(
                            (Discrete) discrete(v.getComponent(Axis.X), xSamples),
                            (Discrete) discrete(v.getComponent(Axis.Y), xSamples),
                            (Discrete) discrete(v.getComponent(Axis.Z), xSamples)
                    );
                }
            }
            throw new IllegalArgumentException("Unsupported");

        }
    }

    public static Expr transformAxis(Expr e, Axis a1, Axis a2, Axis a3) {
        return new AxisTransform(e, new Axis[]{a1, a2, a3}, 3);
    }

    public static Expr transformAxis(Expr e, Axis a1, Axis a2) {
        EnumSet<Axis> found = EnumSet.copyOf(Arrays.asList(Axis.values()));
        found.remove(a1);
        found.remove(a2);
        Axis a3 = found.toArray(new Axis[found.size()])[0];
        if (a3 == Axis.Z) {
            return new AxisTransform(e, new Axis[]{a1, a2, a3}, 2);
        } else {
            return new AxisTransform(e, new Axis[]{a1, a2, a3}, 3);
        }
    }

    public static double sin2(double d) {
        if (d == 0) {
            return 0.0;
        }
        double f = d / Math.PI;
        if (isInt(f)) {
            return 0;
        }
        return Math.sin(d);
    }

    public static double cos2(double d) {
        if (d == 0) {
            return 1;
        }
        double f = d / (Math.PI / 2);
        if (isInt(f)) {
            int r = ((int) Math.floor(Math.abs(f))) % 4;
            switch (r) {
                case 0: {
                    return 1;
                }
                case 1: {
                    return 0;
                }
                case 2: {
                    return -1;
                }
                case 3: {
                    return 0;
                }
                default: {
                    return Math.cos(d);
                }
            }
        }
        return Math.cos(d);
    }

    public static boolean isInt(double d) {
        return Math.floor(d) == d;
    }

    /**
     * lenient cast
     *
     * @param o    instance
     * @param type cast to
     * @param <T>  type
     * @return
     */
    public static <T> T lcast(Object o, Class<T> type) {
        return o == null ? null : type.isInstance(o) ? (T) o : null;
    }

    /**
     * return a string representation (dump) of the given object. dump is
     * evaluated as follows : first
     *
     * @param o object to dump
     * @return string dump of the object
     */
    public static String dump(Object o) {
        return Config.dumpManager.getDumpDelegate(o, false).getDumpString(o);
    }

    public static String dumpSimple(Object o) {
        return Config.dumpManager.getDumpDelegate(o, true).getDumpString(o);
    }

    public static DoubleToDouble expr(double value, Geometry geometry) {
        if (geometry == null) {
            geometry = Domain.FULLXY;
        }
        if (geometry.isRectangular()) {
            return DoubleValue.valueOf(value, geometry.getDomain());
        }
        return new Shape(value, geometry);
    }

    public static DoubleToDouble expr(Geometry domain) {
        return expr(1, domain);
    }

    public static Expr expr(Complex a, Geometry geometry) {
        if (geometry == null) {
            geometry = Domain.FULLXY;
        }
        if (a.isReal()) {
            return expr(a.getReal(), geometry);
        }
        if (geometry.isRectangular()) {
            return new ComplexValue(a, geometry.getDomain());
        } else {
            throw new IllegalArgumentException("Not supported yet geometry with complex value");
        }
    }

    public static ExprList abs(ExprList a) {
        return a.transform(new ElementOp() {
            @Override
            public Expr eval(int index, Expr e) {
                return abs(e);
            }
        });
    }

    public static ExprList db(ExprList a) {
        return a.transform(new ElementOp() {
            @Override
            public Expr eval(int index, Expr e) {
                return db(e);
            }
        });
    }

    public static ExprList db2(ExprList a) {
        return a.transform(new ElementOp() {
            @Override
            public Expr eval(int index, Expr e) {
                return db2(e);
            }
        });
    }


    public static double real(Complex a) {
        return a.getReal();
    }

    public static double imag(Complex a) {
        return a.getImag();
    }


    public static boolean almostEqualRelative(float a, float b, float maxRelativeError) {
        if (a == b) {
            return true;
        }
        float relativeError = b == 0 ? a : ((a - b) / b);
        if (relativeError < 0) {
            relativeError = 0.0F - relativeError;
        }
        return relativeError <= maxRelativeError;
    }

    public static boolean almostEqualRelative(double a, double b, double maxRelativeError) {
        if (a == b) {
            return true;
        }
        double relativeError = b == 0 ? a : ((a - b) / b);
        if (relativeError < 0) {
            relativeError = 0.0D - relativeError;
        }
        return relativeError <= maxRelativeError;
    }

    public static boolean almostEqualRelative(Complex a, Complex b, double maxRelativeError) {
        if (a == b) {
            return true;
        }
        double relativeError = (b.isZero() ? a : ((a.sub(b)).div(b))).norm();
        return relativeError <= maxRelativeError;
    }

    public static DoubleArrayParamSet dtimes(Param param, double min, double max, int times) {
        return new DoubleArrayParamSet(param, min, max, times);
    }

    public static DoubleArrayParamSet dsteps(Param param, double min, double max, double step) {
        return new DoubleArrayParamSet(param, min, max, step);
    }

    public static IntArrayParamSet itimes(Param param, int min, int max, int times) {
        return new IntArrayParamSet(param, min, max, times);
    }

    public static IntArrayParamSet isteps(Param param, int min, int max, int step) {
        return new IntArrayParamSet(param, min, max, (double) step);
    }

    public static FloatArrayParamSet ftimes(Param param, int min, int max, int times) {
        return new FloatArrayParamSet(param, min, max, times);
    }

    public static FloatArrayParamSet fsteps(Param param, int min, int max, int step) {
        return new FloatArrayParamSet(param, min, max, (float) step);
    }

    public static LongArrayParamSet ltimes(Param param, int min, int max, int times) {
        return new LongArrayParamSet(param, min, max, times);
    }

    public static LongArrayParamSet lsteps(Param param, int min, int max, long step) {
        return new LongArrayParamSet(param, min, max, step);
    }


    public static Vector sin(Vector v) {
        return v.sin();
    }

    public static Vector cos(Vector v) {
        return v.cos();
    }

    public static Vector tan(Vector v) {
        return v.tan();
    }

    public static Vector cotan(Vector v) {
        return v.cotan();
    }

    public static Vector tanh(Vector v) {
        return v.tanh();
    }

    public static Vector cotanh(Vector v) {
        return v.cotanh();
    }

    public static Vector cosh(Vector v) {
        return v.cosh();
    }

    public static Vector sinh(Vector v) {
        return v.sinh();
    }

    public static Vector log(Vector v) {
        return v.log();
    }

    public static Vector log10(Vector v) {
        return v.log10();
    }

    public static Vector db(Vector v) {
        return v.db();
    }

    public static Vector exp(Vector v) {
        return v.exp();
    }

    public static Vector acosh(Vector v) {
        return v.acosh();
    }

    public static Vector acos(Vector v) {
        return v.acos();
    }

    public static Vector asinh(Vector v) {
        return v.asinh();
    }

    public static Vector asin(Vector v) {
        return v.asin();
    }

    public static Vector atan(Vector v) {
        return v.atan();
    }

    public static Vector acotan(Vector v) {
        return v.acotan();
    }

    public static Vector imag(Vector v) {
        return v.imag();
    }

    public static Vector real(Vector v) {
        return v.real();
    }

    public static Vector abs(Vector v) {
        return v.abs();
    }

    public static Complex[] abs(Complex[] v) {
        Complex[] r = new Complex[v.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = (v[i].abs());
        }
        return r;
    }

    public static Complex avg(Vector v) {
        return v.avg();
    }

    public static Complex sum(Vector v) {
        return v.sum();
    }

    public static Complex prod(Vector v) {
        return v.prod();
    }

    public static Matrix abs(Matrix v) {
        return v.abs();
    }

    public static Matrix sin(Matrix v) {
        return v.sin();
    }

    public static Matrix cos(Matrix v) {
        return v.cos();
    }

    public static Matrix tan(Matrix v) {
        return v.tan();
    }

    public static Matrix cotan(Matrix v) {
        return v.cotan();
    }

    public static Matrix tanh(Matrix v) {
        return v.tanh();
    }

    public static Matrix cotanh(Matrix v) {
        return v.cotanh();
    }

    public static Matrix cosh(Matrix v) {
        return v.cosh();
    }

    public static Matrix sinh(Matrix v) {
        return v.sinh();
    }

    public static Matrix log(Matrix v) {
        return v.log();
    }

    public static Matrix log10(Matrix v) {
        return v.log10();
    }

    public static Matrix db(Matrix v) {
        return v.db();
    }

    public static Matrix exp(Matrix v) {
        return v.exp();
    }

    //
    public static Matrix acosh(Matrix v) {
        return v.acosh();
    }

    public static Matrix acos(Matrix v) {
        return v.acos();
    }

    public static Matrix asinh(Matrix v) {
        return v.asinh();
    }

    public static Matrix asin(Matrix v) {
        return v.asin();
    }

    public static Matrix atan(Matrix v) {
        return v.atan();
    }

    public static Matrix acotan(Matrix v) {
        return v.acotan();
    }

    public static Matrix imag(Matrix v) {
        return v.imag();
    }

    public static Matrix real(Matrix v) {
        return v.real();
    }

    public static Complex[] real(Complex[] v) {
        Complex[] values = new Complex[v.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = (v[i].real());
        }
        return values;
    }

    public static double[] realdbl(Complex[] v) {
        double[] values = new double[v.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = v[i].realdbl();
        }
        return values;
    }

    public static Complex[] imag(Complex[] v) {
        Complex[] values = new Complex[v.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = (v[i].imag());
        }
        return values;
    }

    public static double[] imagdbl(Complex[] v) {
        double[] values = new double[v.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = v[i].imagdbl();
        }
        return values;
    }

    public static Complex avg(Matrix v) {
        return v.avg();
    }

    public static Complex sum(Matrix v) {
        return v.sum();
    }

    public static Complex prod(Matrix v) {
        return v.prod();
    }


    public static boolean roundEquals(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    public static double round(double val, double precision) {
        return (Math.round(val / precision)) * precision;
    }


    /////////////////////////////////////////////////////////////////
    // sample region functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="sample region functions">
//</editor-fold>

    /////////////////////////////////////////////////////////////////
    // double functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="double functions">
    public static double sqrt(double v, int n) {
        return n == 0 ? 1 : pow(v, 1.0 / n);
    }

    public static double pow(double v, double n) {
        return Math.pow(v, n);
    }

    public static double db(double x) {
        return 10 * Math.log10(x);
    }

    public static double acosh(double x) {
        return Math.log(x + Math.sqrt(x * x - 1));
    }

    public static double atanh(double x) {
        return Math.log((1 + x) / (1 - x)) / 2;
    }

    public static double acotanh(double x) {
        if (true) {
            throw new IllegalArgumentException("TODO : Check me");
        }
        return 1 / atanh(1 / x);
    }

    public static double asinh(double x) {
        if (x == Double.NEGATIVE_INFINITY) {
            return x;
        } else {
            return Math.log(x + Math.sqrt(x * x + 1));
        }
    }

    public static double db2(double nbr) {
        return 20 * Math.log10(nbr);
    }

    public static double sqrt(double nbr) {
        return Math.sqrt(nbr);
    }

    /**
     * erturn 1/x
     *
     * @param x
     * @return
     */
    public static double inv(double x) {
        return 1.0 / x;
    }

    /**
     * return x
     *
     * @param x
     * @return
     */
    public static double conj(double x) {
        return x;
    }
//</editor-fold>

    /////////////////////////////////////////////////////////////////
    // double[] functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="double[] functions">
    public static double[] sin2(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = sin2(x[i]);
        }
        return y;
    }

    public static double[] cos2(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = cos2(x[i]);
        }
        return y;
    }

    public static double[] sin(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.sin(x[i]);
        }
        return y;
    }

    public static double[] cos(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.cos(x[i]);
        }
        return y;
    }

    public static double[] tan(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.tan(x[i]);
        }
        return y;
    }

    public static double[] cotan(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = 1 / Math.tan(x[i]);
        }
        return y;
    }

    public static double[] sinh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.sinh(x[i]);
        }
        return y;
    }

    public static double[] cosh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.cosh(x[i]);
        }
        return y;
    }

    public static double[] tanh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.tanh(x[i]);
        }
        return y;
    }

    public static double[] cotanh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = 1 / Math.tanh(x[i]);
        }
        return y;
    }

    public static double[] minMax(double[] a) {
        double min = Double.MIN_VALUE;
        double max = Double.MAX_VALUE;
        for (double value : a) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        return new double[]{min, max};
    }

    public static double[] minMaxAbs(double[] a) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double anA : a) {
            double abs = Math.abs(anA);
            min = Math.min(min, abs);
            max = Math.max(max, abs);
        }
        return new double[]{min, max};
    }

    public static double[] minMaxAbsNonInfinite(double[] a) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        boolean b = false;
        for (double anA : a) {
            if (!Double.isNaN(anA) && !Double.isInfinite(anA)) {
                double abs = Math.abs(anA);
                min = Math.min(min, abs);
                max = Math.max(max, abs);
                b = true;
            }
        }
        if (b) {
            return new double[]{min, max};
        } else {
            return new double[]{0, 0};
        }
    }

    public static double avgAbs(double[] arr) {
        double avg = 0;
        for (double anArr : arr) {
            avg += Math.abs(anArr);
        }
        return avg / arr.length;
    }

    public static double[] distances(double[] arr) {
        double[] distances = new double[arr.length - 1];
        for (int i = 1; i < arr.length; i++) {
            distances[i - 1] = arr[i] - arr[i - 1];
        }
        return distances;
    }

    public static double[] div(double[] a, double[] b) {
        int max = Math.max(a.length, b.length);
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] / b[i];
        }
        return ret;
    }

    public static double[] mul(double[] a, double[] b) {
        int max = Math.max(a.length, b.length);
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] * b[i];
        }
        return ret;
    }

    public static double[] sub(double[] a, double[] b) {
        int max = Math.max(a.length, b.length);
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] - b[i];
        }
        return ret;
    }

    public static double[] sub(double[] a, double b) {
        int max = a.length;
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] - b;
        }
        return ret;
    }

    public static double[] add(double[] a, double[] b) {
        int max = Math.max(a.length, b.length);
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] + b[i];
        }
        return ret;
    }

    public static double[] db(double[] a) {
        double[] ret = new double[a.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = db(a[i]);
        }
        return ret;
    }

    public static double[][] sin(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = sin(r[i]);
        }
        return r;
    }

    public static double[][] sin2(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = sin2(r[i]);
        }
        return r;
    }
    //</editor-fold>

    /////////////////////////////////////////////////////////////////
    // double functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="double functions">
    public static double sin(double x) {
        return Math.sin(x);
    }

    public static double cos(double x) {
        return Math.cos(x);
    }

    public static double tan(double x) {
        return Math.tan(x);
    }

    public static double cotan(double x) {
        return 1 / Math.tan(x);
    }

    public static double sinh(double x) {
        return (Math.exp(x) - Math.exp(-x)) / 2;
    }

    public static double cosh(double x) {
        return (Math.exp(x) + Math.exp(-x)) / 2;
    }

    public static double tanh(double x) {
        double a = Math.exp(+x);
        double b = Math.exp(-x);
        return a == Double.POSITIVE_INFINITY ? 1 : b == Double.POSITIVE_INFINITY ? -1 : (a - b) / (a + b);
    }

    public static double abs(double a) {
        return Math.abs(a);
    }

    public static double cotanh(double x) {
        return 1 / tanh(x);
    }

    public static double acos(double x) {
        return Math.acos(x);
    }

    public static double asin(double x) {
        return Math.asin(x);
    }

    public static double atan(double x) {
        return Math.atan(x);
    }
    //</editor-fold>

    /////////////////////////////////////////////////////////////////
    // double[] functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="double[] functions">
    public static double sum(double... c) {
        double x = 0;
        for (int i = 0; i < c.length; i++) {
            x += c[i];
        }
        return x;
    }


    public static double[] mul(double[] a, double b) {
        int max = a.length;
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] * b;
        }
        return ret;
    }

    public static double[] mulSelf(double[] x, double v) {
        for (int i = 0; i < x.length; i++) {
            x[i] = x[i] * v;
        }
        return x;
    }

    public static double[] div(double[] a, double b) {
        int max = a.length;
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] / b;
        }
        return ret;
    }

    public static double[] divSelf(double[] x, double v) {
        for (int i = 0; i < x.length; i++) {
            x[i] = x[i] / v;
        }
        return x;
    }

    public static double[] add(double[] x, double v) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = x[i] + v;
        }
        return y;
    }

    public static double[] addSelf(double[] x, double v) {
        for (int i = 0; i < x.length; i++) {
            x[i] = x[i] + v;
        }
        return x;
    }
    //</editor-fold>


    /////////////////////////////////////////////////////////////////
    // double[][] functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="double[][] functions">

    public static double[][] cos(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = cos(r[i]);
        }
        return r;
    }

    public static double[][] tan(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = tan(r[i]);
        }
        return r;
    }

    public static double[][] cotan(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = cotan(r[i]);
        }
        return r;
    }

    public static double[][] sinh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = sinh(r[i]);
        }
        return r;
    }

    public static double[][] cosh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = cosh(r[i]);
        }
        return r;
    }

    public static double[][] tanh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = tanh(r[i]);
        }
        return r;
    }

    public static double[][] cotanh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = tanh(r[i]);
        }
        return r;
    }

    public static double[][] add(double[][] a, double[][] b) {
        int maxi = Math.max(a.length, b.length);
        int maxj = Math.max(a.length == 0 ? 0 : a[0].length, b.length == 0 ? 0 : b[0].length);
        double[][] ret = new double[maxi][maxj];
        for (int i = 0; i < maxi; i++) {
            for (int j = 0; j < maxj; j++) {
                ret[i][j] = a[i][j] + b[i][j];
            }
        }
        return ret;
    }

    public static double[][] sub(double[][] a, double[][] b) {
        int maxi = Math.max(a.length, b.length);
        int maxj = Math.max(a.length == 0 ? 0 : a[0].length, b.length == 0 ? 0 : b[0].length);
        double[][] ret = new double[maxi][maxj];
        for (int i = 0; i < maxi; i++) {
            for (int j = 0; j < maxj; j++) {
                ret[i][j] = a[i][j] - b[i][j];
            }
        }
        return ret;
    }

    public static double[][] div(double[][] a, double[][] b) {
        int maxi = Math.max(a.length, b.length);
        int maxj = Math.max(a.length == 0 ? 0 : a[0].length, b.length == 0 ? 0 : b[0].length);
        double[][] ret = new double[maxi][maxj];
        for (int i = 0; i < maxi; i++) {
            for (int j = 0; j < maxj; j++) {
                ret[i][j] = a[i][j] / b[i][j];
            }
        }
        return ret;
    }

    public static double[][] mul(double[][] a, double[][] b) {
        int maxi = Math.max(a.length, b.length);
        int maxj = Math.max(a.length == 0 ? 0 : a[0].length, b.length == 0 ? 0 : b[0].length);
        double[][] ret = new double[maxi][maxj];
        for (int i = 0; i < maxi; i++) {
            for (int j = 0; j < maxj; j++) {
                ret[i][j] = a[i][j] * b[i][j];
            }
        }
        return ret;
    }

    public static double[][] db(double[][] a) {
        double[][] ret = new double[a.length][];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new double[a[i].length];
            for (int j = 0; j < ret[i].length; j++) {
                ret[i][j] = db(a[i][j]);
            }
        }
        return ret;
    }

    public static double[][] db2(double[][] a) {
        double[][] ret = new double[a.length][];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new double[a[i].length];
            for (int j = 0; j < ret[i].length; j++) {
                ret[i][j] = db2(a[i][j]);
            }
        }
        return ret;
    }
    //</editor-fold>

    /////////////////////////////////////////////////////////////////
    // expression functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="expression functions">
    public static Expr If(Expr cond, Expr exp1, Expr exp2) {
        return EXPR_VECTOR_SPACE.If(cond, exp1, exp2);
    }

    public static Expr or(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.or(a, b);
    }

    public static Expr and(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.and(a, b);
    }

    public static Expr not(Expr a) {
        return EXPR_VECTOR_SPACE.not(a);
    }

    public static Expr eq(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.eq(a, b);
    }

    public static Expr ne(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.ne(a, b);
    }

    public static Expr gte(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.gte(a, b);
    }

    public static Expr gt(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.gt(a, b);
    }

    public static Expr lte(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.lte(a, b);
    }

    public static Expr lt(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.lt(a, b);
    }

    public static Expr cos(Expr e) {
        return EXPR_VECTOR_SPACE.cos(e);
    }

    public static Expr cosh(Expr e) {
        return EXPR_VECTOR_SPACE.cosh(e);
    }

    public static Expr sin(Expr e) {
        return EXPR_VECTOR_SPACE.sin(e);
    }

    public static Expr sincard(Expr e) {
        return EXPR_VECTOR_SPACE.sincard(e);
    }

    public static Expr sinh(Expr e) {
        return EXPR_VECTOR_SPACE.sinh(e);
    }

    public static Expr tan(Expr e) {
        return EXPR_VECTOR_SPACE.tan(e);
    }

    public static Expr tanh(Expr e) {
        return EXPR_VECTOR_SPACE.tanh(e);
    }

    public static Expr cotan(Expr e) {
        return EXPR_VECTOR_SPACE.cotan(e);
    }

    public static Expr cotanh(Expr e) {
        return EXPR_VECTOR_SPACE.cotanh(e);
    }

    public static Expr sqr(Expr e) {
        return EXPR_VECTOR_SPACE.sqr(e);
    }

    public static Expr sqrt(Expr e) {
        return EXPR_VECTOR_SPACE.sqrt(e);
    }

    public static Expr inv(Expr e) {
        return EXPR_VECTOR_SPACE.inv(e);
    }

    public static Expr neg(Expr e) {
        return EXPR_VECTOR_SPACE.neg(e);
    }

    public static Expr exp(Expr e) {
        return EXPR_VECTOR_SPACE.exp(e);
    }

    public static Expr atan(Expr e) {
        return EXPR_VECTOR_SPACE.atan(e);
    }

    public static Expr acotan(Expr e) {
        return EXPR_VECTOR_SPACE.acotan(e);
    }

    public static Expr acos(Expr e) {
        return EXPR_VECTOR_SPACE.acos(e);
    }

    public static Expr asin(Expr e) {
        return EXPR_VECTOR_SPACE.asin(e);
    }

    public static Complex integrate(Expr e) {
        return integrate(e, null);
    }

    public static Complex integrate(Expr e, Domain domain) {
        if (e instanceof Any) {
            e = Any.unwrap(e);
        }
        if (e instanceof Mul) {
            Mul m = (Mul) e;
            if (m.size() == 2) {
                Expr expression = m.getExpression(0);
                //scalar product will conjugate the first argument!
                expression = conj(expression);
                return scalarProduct(domain, expression, m.getExpression(1));
            } else if (m.size() > 2) {
                List<Expr> first = new ArrayList<Expr>();
                List<Expr> second = new ArrayList<Expr>();
                for (int i = 0; i < m.size(); i++) {
                    if (i < 2) {
                        first.add(m.getExpression(i));
                    } else {
                        second.add(m.getExpression(i));
                    }
                }
                Expr firsts = conj(new Mul(first.toArray(new Expr[first.size()])));
                Mul seconds = new Mul(second.toArray(new Expr[second.size()]));
                return scalarProduct(domain, firsts,
                        seconds
                );
            }
        }
        throw new RuntimeException("Unsupported");
    }

    public static Expr sum(int size, Int2Expr f) {
        return Maths.sum(seq(size, f));
    }

    public static Expr sum(int size1, int size2, Int2ToExpr e) {
        return Maths.sum(seq(size1, size2, e));
    }
//    public static void main(String[] args) {
//        Maths.Config.setCacheEnabled(false);
//        System.out.println("before=\n"+formatMemory());
//        long start = inUseMemory();
//        System.out.println(formatMemory(start));
//        Domain[] gp=new Domain[10000000];
//        Domain[] fn=new Domain[10000000];
//        for (int i = 0; i < gp.length; i++) {
//            gp[i]=(Domain.forWidth(0,10,0,10,0,10));
//        }
//        for (int i = 0; i < fn.length; i++) {
//            fn[i]=(Domain.forWidth(0,10,0,10,0,10));
//        }
//        System.out.println("after=\n"+formatMemory());
//        long end = inUseMemory();
//        System.out.println(formatMemory(end));
//        System.out.println("Memoire utilisee "+formatMemory(end-start));
////        ScalarProductCache d = scalarProductCache(gp, fn, ComputationMonitorFactory.out().temporize(1000));
////        Complex gf = d.gf(2, 2);
////        System.out.println(gf);
//    }

    public static ExprList seq(int size1, Int2Expr f) {
        return new AbstractUpdatableExprSequence() {
            @Override
            public int lengthImpl() {
                return size1;
            }

            @Override
            public Expr getImpl(int index) {
                return f.eval(index);
            }
        };
    }

    public static ExprList seq(int size1, int size2, Int2ToExpr f) {
        int sizeFull = size1 * size2;
        return new AbstractUpdatableExprSequence() {
            @Override
            public int lengthImpl() {
                return sizeFull;
            }

            @Override
            public Expr getImpl(int index) {
                return f.eval(index / size2, index % size2);
            }
        };
    }

    private static ScalarProductCache resolveBestScalarProductCache(int rows, int columns) {
        long bytesNeeded = 24L * rows * columns;
        long free = maxFreeMemory();
        if (bytesNeeded > free * ((double) Config.getLargeMatrixThreshold())) {
            return new MatrixScalarProductCache(Config.getLargeMatrixFactory());
        }
        return new MemScalarProductCache();
    }

    public static ScalarProductCache scalarProductCache(Expr[] gp, Expr[] fn, ComputationMonitor monitor) {
        ScalarProductCache c = resolveBestScalarProductCache(gp.length, fn.length);
        c.evaluate(null, fn, gp, AxisXY.XY, monitor);
        return c;
    }

    public static ScalarProductCache scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, ComputationMonitor monitor) {
        ScalarProductCache c = resolveBestScalarProductCache(gp.length, fn.length);
        c.evaluate(sp, fn, gp, AxisXY.XY, monitor);
        return c;
    }

    public static ScalarProductCache scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, AxisXY axis, ComputationMonitor monitor) {
        ScalarProductCache c = resolveBestScalarProductCache(gp.length, fn.length);
        c.evaluate(sp, fn, gp, axis, monitor);
        return c;
    }

    public static ScalarProductCache scalarProductCache(Expr[] gp, Expr[] fn, AxisXY axis, ComputationMonitor monitor) {
        ScalarProductCache c = resolveBestScalarProductCache(gp.length, fn.length);
        c.evaluate(null, fn, gp, axis, monitor);
        return c;
    }

    public static Expr gate(Axis axis, double a, double b) {
        switch (axis) {
            case X:
                return gateX(a, b);
            case Y:
                return gateY(a, b);
            case Z:
                return gateZ(a, b);
        }
        throw new IllegalArgumentException("Insupported axis " + axis);
    }

    public static Expr gate(Expr axis, double a, double b) {
        if (X.equals(axis)) {
            return gateX(a, b);
        }
        if (Y.equals(axis)) {
            return gateY(a, b);
        }
        if (Z.equals(axis)) {
            return gateZ(a, b);
        }
        throw new IllegalArgumentException("Insupported axis " + axis);
    }

    public static Expr gateX(double a, double b) {
        return DoubleValue.valueOf(1, Domain.forBounds(a, b));
    }

    public static Expr gateY(double a, double b) {
        return DoubleValue.valueOf(1, Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, a, b));
    }

    public static Expr gateZ(double a, double b) {
        return DoubleValue.valueOf(1, Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, a, b));
    }

    public static Expr sum(ExprList sequence) {
        int len = sequence.length();
        if (len == 0) {
            return CZERO;
        }
        List<Expr> all = new ArrayList<>();
        Complex c = Complex.ZERO;
        Queue<Expr> t = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            Expr e = sequence.get(i);
            t.add(e);
            while (!t.isEmpty()) {
                Expr e2 = t.remove();
                if (e2 instanceof Plus) {
                    t.addAll(e2.getSubExpressions());
                } else {
                    if (e2.isComplex()) {
                        c = c.add(e2.toComplex());
                    } else {
                        all.add(e2);
                    }
                }
            }
        }
        if(all.isEmpty()){
            return c;
        }
        if(!c.isZero()){
            all.add(c);
        }
        return new Plus(all);
    }

    public static double scalarProduct(DoubleToDouble f1, DoubleToDouble f2) {
        return Config.getDefaultScalarProductOperator().evalDD(null, f1, f2);
    }

    public static Complex scalarProduct(Domain domain, Expr f1, Expr f2) {
        return Config.getDefaultScalarProductOperator().eval(domain, f1, f2);
    }

    public static Complex scalarProduct(Expr f1, Expr f2) {
        return Config.getDefaultScalarProductOperator().eval(f1, f2);
    }
//    public static Complex scalarProduct(DomainXY domain, IDCxy f1, IDCxy f2) {
//        return getDefaultScalarProductOperator().process(domain, f1, f2);
//    }//

    public static Matrix scalarProductMatrix(ExprList g, ExprList f) {
        return Config.getDefaultScalarProductOperator().eval(g, f, null).toMatrix();
    }

    public static ScalarProductCache scalarProduct(ExprList g, ExprList f) {
        return Config.getDefaultScalarProductOperator().eval(g, f, null);
    }

    public static ScalarProductCache scalarProduct(ExprList g, ExprList f, ComputationMonitor monitor) {
        return Config.getDefaultScalarProductOperator().eval(g, f, monitor);
    }

    public static Matrix scalarProductMatrix(ExprList g, ExprList f, ComputationMonitor monitor) {
        return Config.getDefaultScalarProductOperator().eval(g, f, monitor).toMatrix();
    }

    public static ScalarProductCache scalarProduct(ExprList g, ExprList f, AxisXY axis, ComputationMonitor monitor) {
        return Config.getDefaultScalarProductOperator().eval(g, f, axis, monitor);
    }

    public static Matrix scalarProductMatrix(Expr[] g, Expr[] f) {
        return Config.getDefaultScalarProductOperator().eval(g, f, null).toMatrix();
    }

    public static ScalarProductCache scalarProduct(Expr[] g, Expr[] f) {
        return Config.getDefaultScalarProductOperator().eval(g, f, null);
    }

    public static ScalarProductCache scalarProduct(Expr[] g, Expr[] f, ComputationMonitor monitor) {
        return Config.getDefaultScalarProductOperator().eval(g, f, monitor);
    }

    public static Matrix scalarProductMatrix(Expr[] g, Expr[] f, ComputationMonitor monitor) {
        return Config.getDefaultScalarProductOperator().eval(g, f, monitor).toMatrix();
    }

    public static ScalarProductCache scalarProduct(Expr[] g, Expr[] f, AxisXY axis, ComputationMonitor monitor) {
        return Config.getDefaultScalarProductOperator().eval(g, f, axis, monitor);
    }

    //    public static String scalarProductToMatlabString(DFunctionXY f1, DFunctionXY f2, DomainXY domain0, ToMatlabStringParam... format) {
//        return defaultScalarProduct.scalarProductToMatlabString(domain0, f1, f2, format) ;
//    }
//
//    public static String scalarProductToMatlabString(DomainXY domain0,CFunctionXY f1, CFunctionXY f2, ToMatlabStringParam... format) {
//        return defaultScalarProduct.scalarProductToMatlabString(domain0, f1, f2, format) ;
//    }
    public static ExprList exprList(Expr... vector) {
        return new ArrayExprList(vector);
    }

    public static ExprList exprList(Vector vector) {
        ArrayExprList exprs = new ArrayExprList(vector.size());
        for (Complex o : vector) {
            exprs.add(o);
        }
        return exprs;
    }

    public static ExprList exprList(Matrix vector) {
        return exprList(vector.toVector());
    }

    public static ExprList exprList() {
        return new ArrayExprList();
    }

    public static Expr sum(Expr... e) {
        ExprList all = new ArrayExprList();
        //this is needed not to provoke StackOverFlow Exception on evaluation mainly if a "plus" is performed in a loop!
        for (Expr expr : e) {
            if (expr instanceof Plus) {
                all.addAll(expr.getSubExpressions());
            } else {
                all.add(expr);
            }
        }
        return new Plus(all);
    }

    public static Expr mul(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.mul(a, b);
    }

    public static Expr mul(Expr... e) {
        ArrayExprList all = new ArrayExprList();
        //this is needed not to provoke StackOverFlow Exception on evaluation mainly if a "plus" is performed in a loop!
        for (Expr expr : e) {
            if (expr instanceof Mul) {
                all.addAll(expr.getSubExpressions());
            } else {
                all.add(expr);
            }
        }
        return new Mul(all);
    }

    public static Expr pow(Expr a, Expr b) {
        return new Pow(a, b);
    }

    public static Expr sub(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.sub(a, b);
    }

    public static Expr add(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.add(a, b);
    }

    public static Expr add(Expr... a) {
        switch (a.length) {
            case 0: {
                throw new IllegalArgumentException("Missing arguments for add");
            }
            case 1: {
                return a[0];
            }
            case 2: {
                return new Plus(a[0], a[1]);
            }
            default: {
                Plus p = new Plus(a[0], a[1]);
                for (int i = 2; i < a.length; i++) {
                    p = new Plus(p, a[i]);
                }
                return p;
            }
        }
    }

    public static Expr div(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.div(a, b);
    }

    public static Expr expr(double value) {
        return DoubleValue.valueOf(value, Domain.FULLX);
    }

    public static Expr expr(Domain d) {
        return DoubleValue.valueOf(1, d);
    }

    public static Expr simplify(Expr a) {
        return ExpressionRewriterFactory.getComputationOptimizer().rewriteOrSame(a);
    }

    public static double norm(Expr a) {
        //TODO conjugate a
        Expr aCong = a;
        Complex c = Config.getDefaultScalarProductOperator().eval(a, aCong);
        return sqrt(c).absdbl();
    }

    public static ExprList normalize(ExprList a) {
        return a.transform(new ElementOp() {
            @Override
            public Expr eval(int index, Expr e) {
                return normalize(e);
            }
        });
    }

    public static Expr normalize(Expr a) {
        Complex n = Complex.valueOf(1.0 / norm(a));
        if (n.equals(Maths.CONE)) {
            return a;
        }
        Expr mul = mul(a, n);
        //preserve names and properties
        mul = AbstractExprPropertyAware.copyProperties(a, mul);
        return mul;
    }

    public static DoubleToVector vector(Expr fx, Expr fy) {
        if (fx.isZero() && fy.isZero()) {
            return DVZERO2;
        }
        return DefaultDoubleToVector.create(fx.toDC(), fy.toDC());
    }

    public static DoubleToVector vector(Expr fx) {
        if (fx.isZero()) {
            return DVZERO1;
        }
        return DefaultDoubleToVector.create(fx.toDC());
    }

    public static DoubleToVector vector(Expr fx, Expr fy, Expr fz) {
        if (fx.isZero() && fy.isZero() && fz.isZero()) {
            return DVZERO3;
        }
        return DefaultDoubleToVector.create(fx.toDC(), fy.toDC(), fz.toDC());
    }

    public static ExprList cos(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(cos(x));
        }
        return n;
    }

    public static ExprList cosh(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(cosh(x));
        }
        return n;
    }

    public static ExprList sin(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(sin(x));
        }
        return n;
    }

    public static ExprList sinh(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(sinh(x));
        }
        return n;
    }

    public static ExprList tan(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(tan(x));
        }
        return n;
    }

    public static ExprList tanh(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(tanh(x));
        }
        return n;
    }

    public static ExprList cotan(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(cotan(x));
        }
        return n;
    }

    public static ExprList cotanh(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(cotanh(x));
        }
        return n;
    }

    public static ExprList sqr(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(sqr(x));
        }
        return n;
    }

    public static ExprList sqrt(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(sqrt(x));
        }
        return n;
    }

    public static ExprList inv(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(inv(x));
        }
        return n;
    }

    public static ExprList neg(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(neg(x));
        }
        return n;
    }

    public static ExprList exp(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(exp(x));
        }
        return n;
    }

    public static ExprList add(ExprList e, Expr... expressions) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(sum(x, sum(expressions)));
        }
        return n;
    }

    public static ExprList mul(ExprList e, Expr... expressions) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(mul(x, mul(expressions)));
        }
        return n;
    }

    public static ExprList pow(ExprList e, Expr b) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(pow(x, b));
        }
        return n;
    }

    public static ExprList sub(ExprList e, Expr b) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(sub(x, b));
        }
        return n;
    }

    public static ExprList div(ExprList e, Expr b) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(div(x, b));
        }
        return n;
    }

    public static ExprList simplify(ExprList e) {
        ExprList n = new ArrayExprList(e.size());
        for (Expr x : e) {
            n.add(simplify(x));
        }
        return n;
    }

//</editor-fold>

    /////////////////////////////////////////////////////////////////
    // general purpose functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="general purpose functions">

    public static String formatMemory() {
        return memoryInfo().toString();
    }

    public static String formatMetric(double value) {
        return Config.metricFormatter.format(value);
    }

    public static MemoryInfo memoryInfo() {
        return new MemoryInfo();
    }

    public static MemoryMeter memoryMeter() {
        return new MemoryMeter();
    }

    public static long inUseMemory() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory());
    }

    public static long maxFreeMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.maxMemory() - (rt.totalMemory() - rt.freeMemory());
    }

    public static String formatMemory(long bytes) {
        return Config.getMemorySizeFormatter().format(bytes);
    }

    public static String formatFrequency(long frequency) {
        return Config.getFrequencyFormatter().format(frequency);
    }

    public static String formatFrequency(double frequency) {
        return Config.getFrequencyFormatter().format(frequency);
    }

    public static String formatPeriod(long period) {
        return Config.TIME_PERIOD_FORMATTER.format(period);
    }

    public static int sizeOf(Class src) {
        List<Field> instanceFields = new LinkedList<Field>();
        while (instanceFields.isEmpty()) {
            if (src == null || src == Object.class) {
                return JOBJECT_MIN_SIZE;
            }
            for (Field f : src.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    instanceFields.add(f);
                }
            }
            src = src.getSuperclass();
        }
        ;
        long maxOffset = 0;
        Unsafe unsafeInstance = PrivateUnsafe.getUnsafeInstance();
        for (Field f : instanceFields) {
            long offset = unsafeInstance.objectFieldOffset(f);
            if (offset > maxOffset) maxOffset = offset;
        }
        return (((int) maxOffset / WORD) + 1) * WORD;
    }


    public static Complex sumc(int size, Int2Complex f) {
        MutableComplex c = new MutableComplex();
        int i = 0;
        while (i < size) {
            c.add(f.eval(i));
            i++;
        }
        return c.toComplex();
    }


    public static <T> T invokeMonitoredAction(ComputationMonitor mon, String messagePrefix, MonitoredAction<T> run) {
        return ComputationMonitorFactory.invokeMonitoredAction(mon, messagePrefix, run);
    }

    public static Chronometer chrono() {
        return new Chronometer();
    }

    public static Chronometer chrono(String name) {
        return new Chronometer(name);
    }

    public static SolverExecutorService solverExecutorService(int threads) {
        return new SolverExecutorService(threads);
    }

    public static Chronometer chrono(Runnable r) {
        Chronometer c = new Chronometer(true);
        r.run();
        return c.stop();
    }

    public static DoubleFormatter percentFormat() {
        return Config.percentFormatter;
    }

    public static DoubleFormatter frequencyFormat() {
        return Config.getFrequencyFormatter();
    }

    public static DoubleFormatter metricFormat() {
        return Config.getMetricFormatter();
    }

    public static DoubleFormatter memoryFormat() {
        return Config.getMemorySizeFormatter();
    }

    public static DoubleFormatter dblformat(String format) {
        if (StringUtils.isEmpty(format)) {
            format = "";
        }
        String[] a = format.split(" ");
        String type = "";
        String subFormat = "";
        if (a.length == 0) {
            //
        } else {
            type = a[0];
            subFormat = format.substring(type.length());
        }
        switch (StringUtils.trim(format).toLowerCase()) {
            case "hz":
            case "freq":
            case "frequency": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Config.getFrequencyFormatter();
                }
                return new FrequencyFormatter(subFormat);
            }
            case "m":
            case "metric": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Config.getMetricFormatter();
                }
                return new MetricFormatter(subFormat);
            }
            case "b":
            case "mem":
            case "memory": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Config.getMemorySizeFormatter();
                }
                return new MemorySizeFormatter(subFormat);
            }
            case "%":
            case "percent": {
                if (StringUtils.isEmpty(subFormat)) {
                    return percentFormat();
                }
                return new DecimalDoubleFormatter(subFormat);
            }
            case "d":
            case "double": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Config.defaultDblFormat;
                }
                return new DecimalDoubleFormatter(subFormat);
            }
        }
        if (StringUtils.isEmpty(subFormat)) {
            return Config.defaultDblFormat;
        }
        return new DecimalDoubleFormatter(subFormat);
    }

    public static double[] resizePickFirst(double[] array, int newSize) {
        int oldSize = array.length;
        if (newSize == oldSize) {
            double[] array2 = new double[newSize];
            System.arraycopy(array, 0, array2, 0, newSize);
            return array2;
        } else if (newSize > oldSize) {
            throw new IllegalArgumentException("Unsupported yet");
        } else {
            int windowSize = oldSize / newSize;
            double[] array2 = new double[newSize];
            for (int i = 0; i < newSize; i++) {
                array2[i] = array[i * windowSize];
            }
            return array2;
        }
    }

    public static double[] resizePickAverage(double[] array, int newSize) {
        int oldSize = array.length;
        if (newSize == oldSize) {
            double[] array2 = new double[newSize];
            System.arraycopy(array, 0, array2, 0, newSize);
            return array2;
        } else if (newSize > oldSize) {
            throw new IllegalArgumentException("Unsupported yet");
        } else {
            int windowSize = oldSize / newSize;
            double[] array2 = new double[newSize];
            for (int i = 0; i < newSize; i++) {
                int m = 0;
                double a = 0;
                for (int j = 0; j < windowSize && i * windowSize + j < oldSize; j++) {
                    m++;
                    a += array[i * windowSize + j];
                }
                array2[i] = a / m;
            }
            return array2;
        }
    }

    public static <T> T[] toArray(Class<T> t, Collection<T> coll) {
        return (T[]) coll.toArray((T[]) Array.newInstance(t, coll.size()));
    }

    public static double rerr(double a, double b) {
        if (a == b) {
            return 0;
        }
        if (Double.isNaN(a) && Double.isNaN(b)) {
            return 0;
        }
        if (Double.isNaN(a) || Double.isNaN(b)) {
            return Double.NaN;
        }
        if (Double.isInfinite(a) || Double.isInfinite(b)) {
            return Double.POSITIVE_INFINITY;
        }
        return Maths.abs(b - a) / abs(a);
    }

    public static double rerr(Complex a, Complex b) {
        if (a.equals(b)) {
            return 0;
        }
        if (a.isNaN() && b.isNaN()) {
            return 0;
        }
        if (a.isNaN() || b.isNaN()) {
            return Double.NaN;
        }
        if (a.isInfinite() || b.isInfinite()) {
            return Double.POSITIVE_INFINITY;
        }
        return (b.sub(a)).absdbl() / a.absdbl();
    }

    public static CustomCCFunctionXDefinition define(String name, CustomCCFunctionX f) {
        return new CustomCCFunctionXDefinition(name, f);
    }

    public static CustomDCFunctionXDefinition define(String name, CustomDCFunctionX f) {
        return new CustomDCFunctionXDefinition(name, f);
    }

    public static CustomDDFunctionXDefinition define(String name, CustomDDFunctionX f) {
        return new CustomDDFunctionXDefinition(name, f);
    }

//    public static CustomFunctionDefinition define(String name,CustomFunction f){
//        if(f instanceof CustomCCFunctionX){
//            return new CustomCCFunctionXDefinition(name,(CustomCCFunctionX) f);
//        }
//        if(f instanceof CustomDDFunctionX){
//            return new CustomDDFunctionXDefinition(name,(CustomDDFunctionX) f);
//        }
//        throw new IllegalArgumentException("Unsupported function definition");
//    }

    public static CustomDDFunctionXYDefinition define(String name, CustomDDFunctionXY f) {
        return new CustomDDFunctionXYDefinition(name, f);
    }

    public static CustomDCFunctionXYDefinition define(String name, CustomDCFunctionXY f) {
        return new CustomDCFunctionXYDefinition(name, f);
    }

    public static CustomCCFunctionXYDefinition define(String name, CustomCCFunctionXY f) {
        return new CustomCCFunctionXYDefinition(name, f);
    }

    public static double rerr(Matrix a, Matrix b) {
        return b.getError(a);
    }

    public static double toDouble(Complex c, ComplexAsDouble d) {
        if (d == null) {
            return c.absdbl();
        }
        switch (d) {
            case ABS:
                return c.absdbl();
            case REAL:
                return c.realdbl();
            case IMG:
                return c.imagdbl();
            case DB:
                return db(c.absdbl());
            case DB2:
                return db2(c.absdbl());
            case COMPLEX:
                return c.absdbl();
        }
        return Double.NaN;
    }

    public static Expr conj(Expr e) {
        if (e.isDD()) {
            return e;
        }
        if (e.isComplex()) {
            return e.toComplex().conj();
        }
        return new Conj(e);
    }

    public static Complex complex(TMatrix t) {
        return t.toComplex();
    }
    //</editor-fold>

    public static class Config {

        private static final DumpManager dumpManager = new DumpManager();
        static MatrixFactory DEFAULT_LARGE_MATRIX_FACTORY = null;
        private static int simplifierCacheSize = 2000;
        private static float largeMatrixThreshold = 0.7f;
        private static boolean debugExpressionRewrite = false;
        private static boolean strictComputationMonitor = false;
        private static FrequencyFormatter frequencyFormatter = new FrequencyFormatter();
        private static MemorySizeFormatter memorySizeFormatter = new MemorySizeFormatter();
        private static MetricFormatter metricFormatter = new MetricFormatter();
        private static TimePeriodFormatter TIME_PERIOD_FORMATTER = new TimePeriodFormatter();
        private static ExprSequenceFactory DEFAULT_EXPR_SEQ_FACTORY = DefaultExprSequenceFactory.INSTANCE;
        private static ExprMatrixFactory DEFAULT_EXPR_MATRIX_FACTORY = DefaultExprMatrixFactory.INSTANCE;
        private static ExprCubeFactory DEFAULT_EXPR_CUBE_FACTORY = DefaultExprCubeFactory.INSTANCE;
        private static int matrixBlockPrecision = 256;
        private static InverseStrategy defaultMatrixInverseStrategy = InverseStrategy.BLOCK_SOLVE;
        private static SolveStrategy defaultMatrixSolveStrategy = SolveStrategy.DEFAULT;
        private static MatrixFactory defaultMatrixFactory = MemMatrixFactory.INSTANCE;
        private static CacheMode persistenceCacheMode = CacheMode.ENABLED;
        private static boolean cacheEnabled = true;
        private static boolean expressionWriterCacheEnabled = true;
        private static boolean cacheExpressionPropertiesEnabled = true;
        private static boolean cacheExpressionPropertiesEnabledEff = true;
        private static boolean developmentMode = false;
        private static String rootCachePath = "${user.home}/.cache/mathcache";
        private static String defaultCacheFolderName = "default";
        private static String largeMatrixCachePath = "${cache.folder}/large-matrix";
        //    public static final ScalarProduct NUMERIC_SIMP_SCALAR_PRODUCT = new NumericSimplifierScalarProduct();
        private static ScalarProductOperator defaultScalarProductOperator = null;
        private static FunctionDifferentiatorManager functionDifferentiatorManager = new FormalDifferentiation();
        private static PropertyChangeSupport pcs = new PropertyChangeSupport(Config.class);
        private static DoubleFormatter defaultDblFormat = new DoubleFormatter() {
            @Override
            public String formatDouble(double value) {
                return String.valueOf(value);
            }
        };
        private static DoubleFormatter percentFormatter = new DoubleFormatter() {
            private final DecimalFormat d = new DecimalFormat("0.00%");

            @Override
            public String formatDouble(double value) {
                return d.format(value);
            }
        };

        public static boolean isDevelopmentMode() {
            return developmentMode;
        }

        public static void setDevelopmentMode(boolean developmentMode) {
            Config.developmentMode = developmentMode;
        }

        public static FrequencyFormatter getFrequencyFormatter() {
            return frequencyFormatter;
        }

        public static void setFrequencyFormatter(FrequencyFormatter frequencyFormatter) {
            Config.frequencyFormatter = frequencyFormatter;
        }

        public static MemorySizeFormatter getMemorySizeFormatter() {
            return memorySizeFormatter;
        }

        public static void setMemorySizeFormatter(MemorySizeFormatter memorySizeFormatter) {
            Config.memorySizeFormatter = memorySizeFormatter;
        }

        public static MetricFormatter getMetricFormatter() {
            return metricFormatter;
        }

        public static void setMetricFormatter(MetricFormatter metricFormatter) {
            Config.metricFormatter = metricFormatter;
        }

        public static MatrixFactory getDefaultMatrixFactory() {
            return defaultMatrixFactory;
        }

        public static void setDefaultMatrixFactory(MatrixFactory defaultMatrixFactory) {
            Config.defaultMatrixFactory = defaultMatrixFactory;
        }

        public static <T> TMatrixFactory<T> getDefaultMatrixFactory(Class<T> baseType) {
            throw new IllegalArgumentException("Not Yet Supported");
        }

        public static String getRootCachePath(boolean expand) {
            return expand ? replaceVars(rootCachePath) : rootCachePath;
        }

        public static void setRootCachePath(String rootCachePath) {
            Config.rootCachePath = rootCachePath;
        }

        public static String getDefaultCacheFolderName(boolean expand) {
            return expand ? replaceVars(defaultCacheFolderName) : defaultCacheFolderName;
        }

        public static void setDefaultCacheFolderName(String defaultCacheFolderName) {
            Config.defaultCacheFolderName = defaultCacheFolderName;
        }


        public static String getCacheFolder() {
            return getCacheFolder(null);
        }

        public static String getCacheFolder(String folder) {
            String baseCacheFolder = getRootCachePath(true);
            if (folder == null) {
                return baseCacheFolder + "/" + getDefaultCacheFolderName(true);
            } else if (
                    (!new File(folder).isAbsolute())
                            && !folder.startsWith("./")
                            && !folder.startsWith("../")
                            && !folder.startsWith(".\\")
                            && !folder.startsWith("..\\")
                            && !folder.equals(".")
                            && !folder.equals("..")
                    ) {//folder.indexOf('/') < 0 && folder.indexOf('\\') < 0
                return (baseCacheFolder + "/" + folder);
            } else {
                return (folder);
            }
        }

        public static boolean isExpressionWriterCacheEnabled() {
            return cacheEnabled && expressionWriterCacheEnabled;
        }

        public static void setExpressionWriterCacheEnabled(boolean enabled) {
            boolean old = expressionWriterCacheEnabled;
            expressionWriterCacheEnabled = enabled;
            pcs.firePropertyChange("expressionWriterCacheEnabled", old, enabled);
        }

        public static CacheMode getPersistenceCacheMode() {
            return cacheEnabled ? persistenceCacheMode : CacheMode.DISABLED;
        }

        public static void setPersistenceCacheMode(CacheMode persistenceCacheMode) {
            if (persistenceCacheMode == null) {
                persistenceCacheMode = CacheMode.DISABLED;
            }
            switch (persistenceCacheMode) {
                case INHERITED: {
                    persistenceCacheMode = CacheMode.ENABLED;
                    break;
                }
            }
            Config.persistenceCacheMode = persistenceCacheMode;

        }

        public static ExpressionRewriter getComputationOptimizer() {
            return ExpressionRewriterFactory.getComputationOptimizer();
        }

        public static float getLargeMatrixThreshold() {
            return largeMatrixThreshold;
        }

        public static void setLargeMatrixThreshold(float largeMatrixThreshold) {
            Config.largeMatrixThreshold = largeMatrixThreshold;
        }

        public static void seLargeMatrixCachePath(String largeMatrixPath) {
            Config.largeMatrixCachePath = largeMatrixPath;
        }

        public static String getLargeMatrixCachePath(boolean expand) {
            return expand ? replaceVars(largeMatrixCachePath) : largeMatrixCachePath;
        }

        public static void addConfigChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public static void addConfigChangeListener(String property, PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(property, listener);
        }

        public static void removeConfigChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }

        public static void removeConfigChangeListener(String property, PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(property, listener);
        }

        public static boolean isCacheEnabled() {
            return cacheEnabled;
        }

        public static void setCacheEnabled(boolean enabled) {
            boolean old = cacheEnabled;
            cacheEnabled = enabled;
            cacheExpressionPropertiesEnabledEff = cacheExpressionPropertiesEnabled && cacheEnabled;
            pcs.firePropertyChange("cacheEnabled", old, cacheEnabled);
        }

        public static boolean isCacheExpressionPropertiesEnabled() {
            return cacheExpressionPropertiesEnabledEff;
        }

        public static void setCacheExpressionPropertiesEnabled(boolean cacheExpressionPropertiesEnabled) {
            boolean old = Config.cacheExpressionPropertiesEnabled;
            Config.cacheExpressionPropertiesEnabled = cacheExpressionPropertiesEnabled;
            cacheExpressionPropertiesEnabledEff = cacheExpressionPropertiesEnabled && cacheEnabled;
            pcs.firePropertyChange("cacheEnabled", old, Config.cacheExpressionPropertiesEnabled);
        }

        public static void setCacheExpressionRewriterSize(ExpressionRewriter ew, int size) {
            if (ew instanceof CacheEnabled) {
                ((CacheEnabled) ew).setCacheSize(size);
            }
        }

        public static FunctionDifferentiatorManager getFunctionDerivatorManager() {
            if (getFunctionDifferentiatorManager() == null) {
                setFunctionDifferentiatorManager(new FormalDifferentiation());
            }
            return getFunctionDifferentiatorManager();
        }

        public static void setFunctionDerivatorManager(FunctionDifferentiatorManager manager) {
            setFunctionDifferentiatorManager(manager);
        }

        public static ScalarProductOperator getDefaultScalarProductOperator() {
            if (defaultScalarProductOperator == null) {
                defaultScalarProductOperator = ScalarProductOperatorFactory.formal();
            }
            return defaultScalarProductOperator;
        }

        public static void setDefaultScalarProductOperator(ScalarProductOperator sp) {
            defaultScalarProductOperator = sp == null ? ScalarProductOperatorFactory.defaultValue() : sp;
        }

        public static MatrixFactory getMatrixFactory() {
            return defaultMatrixFactory;
        }

        public static MatrixFactory getLargeMatrixFactory() {
            if (DEFAULT_LARGE_MATRIX_FACTORY == null) {
                synchronized (Maths.class) {
                    if (DEFAULT_LARGE_MATRIX_FACTORY == null) {
                        LargeMatrixFactory s = DBLargeMatrixFactory.createLocalSparseStorage(null, new File(
                                Config.getLargeMatrixCachePath(true)
                        ));
                        s.setResetOnClose(true);
                        DEFAULT_LARGE_MATRIX_FACTORY = s;
                    }
                }
            }
            return DEFAULT_LARGE_MATRIX_FACTORY;
        }

        public static void setDefaultLargeMatrixFactory(MatrixFactory m) {
            synchronized (Maths.class) {
                if (DEFAULT_LARGE_MATRIX_FACTORY != null) {
                    DEFAULT_LARGE_MATRIX_FACTORY.close();
                }
                DEFAULT_LARGE_MATRIX_FACTORY = m;
            }
        }

        public static void setLogMonitorLevel(Level level) {
            Logger logger = LogComputationMonitor.getDefaultLogger();
            logger.setLevel(level);
            Handler handler = null;
            for (Handler h : logger.getHandlers()) {
                handler = h;
                h.setLevel(level);
            }
            if (handler == null) {
                handler = new ConsoleHandler();
                handler.setLevel(level);
                handler.setFormatter(LogUtils.LOG_FORMATTER_2);
                logger.addHandler(handler);
            }
        }

        private static String replaceVars(String format) {
            return StringUtils.replaceVars(format, new StringMapper() {
                @Override
                public String get(String key) {
                    String val = System.getProperty(key);
                    if (val == null) {
                        switch (key) {
                            case "cache.root": {
                                val = getRootCachePath(true);
                                break;
                            }
                            case "cache.folder": {
                                val = getCacheFolder();
                                break;
                            }
                            case "cache.large-matrix": {
                                val = getLargeMatrixCachePath(true);
                                break;
                            }
                            default: {
                                val = "${" + key + "}";
                            }
                        }
                    }
                    return val;
                }
            });
        }

        public static int getMatrixBlockPrecision() {
            return matrixBlockPrecision;
        }

        public static void setMatrixBlockPrecision(int matrixBlockPrecision) {
            Config.matrixBlockPrecision = matrixBlockPrecision;
        }

        public static InverseStrategy getDefaultMatrixInverseStrategy() {
            return defaultMatrixInverseStrategy;
        }

        public static void setDefaultMatrixInverseStrategy(InverseStrategy defaultMatrixInverseStrategy) {
            Config.defaultMatrixInverseStrategy = defaultMatrixInverseStrategy;
        }

        public static SolveStrategy getDefaultMatrixSolveStrategy() {
            return defaultMatrixSolveStrategy;
        }

        public static void setDefaultMatrixSolveStrategy(SolveStrategy defaultMatrixSolveStrategy) {
            Config.defaultMatrixSolveStrategy = defaultMatrixSolveStrategy;
        }

        public static FunctionDifferentiatorManager getFunctionDifferentiatorManager() {
            return functionDifferentiatorManager;
        }

        public static void setFunctionDifferentiatorManager(FunctionDifferentiatorManager functionDifferentiatorManager) {
            Config.functionDifferentiatorManager = functionDifferentiatorManager;
        }

        public static int getSimplifierCacheSize() {
            return simplifierCacheSize;
        }

        public static void setSimplifierCacheSize(int simplifierCacheSize) {
            Config.simplifierCacheSize = simplifierCacheSize;
        }

        public static boolean isDebugExpressionRewrite() {
            return debugExpressionRewrite;
        }

        public static void setDebugExpressionRewrite(boolean debugExpressionRewrite) {
            Config.debugExpressionRewrite = debugExpressionRewrite;
        }

        public static boolean isStrictComputationMonitor() {
            return strictComputationMonitor;
        }

        public static void setStrictComputationMonitor(boolean strictComputationMonitor) {
            Config.strictComputationMonitor = strictComputationMonitor;
        }
    }

}
