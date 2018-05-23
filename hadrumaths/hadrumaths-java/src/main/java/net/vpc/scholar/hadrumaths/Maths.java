package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.*;
import net.vpc.common.util.mon.*;
import net.vpc.scholar.hadrumaths.cache.CacheEnabled;
import net.vpc.scholar.hadrumaths.cache.CacheMode;
import net.vpc.scholar.hadrumaths.derivation.FormalDifferentiation;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.dump.DumpManager;
import net.vpc.scholar.hadrumaths.expeval.ExpressionEvaluator;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.integration.IntegrationOperator;
import net.vpc.scholar.hadrumaths.interop.jblas.JBlasMatrixFactory;
import net.vpc.scholar.hadrumaths.interop.ojalgo.OjalgoMatrixFactory;
import net.vpc.scholar.hadrumaths.io.FailStrategy;
import net.vpc.scholar.hadrumaths.io.FolderHFileSystem;
import net.vpc.scholar.hadrumaths.io.HFileSystem;
import net.vpc.scholar.hadrumaths.io.IOUtils;
import net.vpc.scholar.hadrumaths.plot.ComplexAsDouble;
import net.vpc.scholar.hadrumaths.plot.JColorArrayPalette;
import net.vpc.scholar.hadrumaths.plot.JColorPalette;
import net.vpc.scholar.hadrumaths.plot.console.params.*;
import net.vpc.scholar.hadrumaths.scalarproducts.MatrixScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.MemComplexScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.MemDoubleScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.util.*;
import net.vpc.scholar.hadrumaths.util.Converter;
import net.vpc.scholar.hadrumaths.util.LogUtils;
import sun.misc.Unsafe;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;
public final class Maths {
    private static double getit(double d) {
        return d;
    }

    //<editor-fold desc="constants functions">
    public static final double PI = Math.PI;
    public static final double E = Math.E;
    public static final DoubleToDouble DDZERO = DoubleValue.valueOf(0, Domain.FULLX);
    public static final DoubleToDouble DDNAN = DoubleValue.valueOf(Double.NaN, Domain.FULLX);
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
    public static final VectorSpace<Double> DOUBLE_VECTOR_SPACE = new DoubleVectorSpace();
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int Z_AXIS = 2;
    public static final TStoreManager<Matrix> MATRIX_STORE_MANAGER = new TStoreManager<Matrix>() {
        @Override
        public void store(Matrix item, File file) {
            item.store(file);
        }

        @Override
        public Matrix load(File file) {
            return Config.defaultMatrixFactory.load(file);
        }
    };
    public static final TStoreManager<TMatrix> TMATRIX_STORE_MANAGER = new TStoreManager<TMatrix>() {
        @Override
        public void store(TMatrix item, File file) {
            item.store(file);
        }

        @Override
        public TMatrix load(File file) {
            return Config.defaultMatrixFactory.load(file);
        }
    };

    public static final TStoreManager<TVector> TVECTOR_STORE_MANAGER = new TStoreManager<TVector>() {
        @Override
        public void store(TVector item, File file) {
            item.store(file);
        }

        @Override
        public TVector load(File file) {
            return Config.defaultMatrixFactory.load(file).toVector();
        }
    };

    public static final TStoreManager<Vector> VECTOR_STORE_MANAGER = new TStoreManager<Vector>() {
        @Override
        public void store(Vector item, File file) {
            item.store(file);
        }

        @Override
        public Vector load(File file) {
            return Config.defaultMatrixFactory.load(file).toVector();
        }
    };
    public static final Converter IDENTITY = new IdentityConverter();
    public static final Converter<Complex, Double> COMPLEX_TO_DOUBLE = new ComplexDoubleConverter();
    public static final Converter<Double, Complex> DOUBLE_TO_COMPLEX = new DoubleComplexConverter();
    public static final Converter<Double, TVector> DOUBLE_TO_TVECTOR = new DoubleTVectorConverter();
    public static final Converter<TVector, Double> TVECTOR_TO_DOUBLE = new TVectorDoubleConverter();
    public static final Converter<Complex, TVector> COMPLEX_TO_TVECTOR = new ComplexTVectorConverter();
    public static final Converter<TVector, Complex> TVECTOR_TO_COMPLEX = new TVectorComplexConverter();
    public static final Converter<Complex, Expr> COMPLEX_TO_EXPR = new ComplexExprConverter();
    public static final Converter<Expr, Complex> EXPR_TO_COMPLEX = new ExprComplexConverter();
    public static final Converter<Double, Expr> DOUBLE_TO_EXPR = new DoubleExprConverter();
    public static final Converter<Expr, Double> EXPR_TO_DOUBLE = new ExprDoubleConverter();
    //    public static String getAxisLabel(int axis){
//        switch(axis){
//            case X_AXIS:return "X";
//            case Y_AXIS:return "Y";
//            case Z_AXIS:return "Z";
//        }
//        throw new IllegalArgumentException("Unknown Axis "+axis);
//    }
    public static final TypeReference<String> $STRING = new StringTypeReference();
    public static final TypeReference<Matrix> $MATRIX = new MatrixTypeReference();
    public static final TypeReference<Vector> $VECTOR = new VectorTypeReference();
    public static final TypeReference<TMatrix<Complex>> $CMATRIX = new TMatrixTypeReference();
    public static final TypeReference<TVector<Complex>> $CVECTOR = new TVectorTypeReference();
    public static final TypeReference<Complex> $COMPLEX = new ComplexTypeReference();
    public static final TypeReference<Double> $DOUBLE = new DoubleTypeReference();
    public static final TypeReference<Boolean> $BOOLEAN = new BooleanTypeReference();
    public static final TypeReference<Point> $POINT = new PointTypeReference();
    public static final TypeReference<File> $FILE = new FileTypeReference();
    //</editor-fold>
    public static final TypeReference<Integer> $INTEGER = new IntegerTypeReference();
    public static final TypeReference<Long> $LONG = new LongTypeReference();
    public static final TypeReference<Expr> $EXPR = new ExprTypeReference();
    public static final TypeReference<TList<Complex>> $CLIST = new TListTypeReference();
    public static final TypeReference<TList<Expr>> $ELIST = new TListExprTypeReference();
    public static final TypeReference<TList<Double>> $DLIST = new TListDoubleTypeReference();
    public static final TypeReference<TList<Integer>> $ILIST = new TListIntegerTypeReference();
    public static final TypeReference<TList<Boolean>> $BLIST = new TListBooleanTypeReference();
    public static final TypeReference<TList<Matrix>> $MLIST = new TListMatrixTypeReference();
    public static final SimpleDateFormat UNIVERSAL_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int ARCH_MODEL_BITS = Integer.valueOf(
            System.getProperty("sun.arch.data.model") != null ? System.getProperty("sun.arch.data.model") :
                    System.getProperty("os.arch").contains("64") ? "64" : "32"
    );
    private static final int BYTE_BITS = 8;
    private static final int WORD = ARCH_MODEL_BITS / BYTE_BITS;
    private static final int JOBJECT_MIN_SIZE = 16;
    private static final Logger $log = Logger.getLogger(Maths.class.getName());
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

    public static JColorPalette DEFAULT_PALETTE = new JColorArrayPalette(new Color[]{
            new Color(0xFF, 0x55, 0x55),
            new Color(0x55, 0x55, 0xFF),
            new Color(0x55, 0xFF, 0x55),
            new Color(0xFF, 0xFF, 0x55),
            new Color(0xFF, 0x55, 0xFF),
            new Color(0x55, 0xFF, 0xFF),
            Color.pink,
            Color.gray,
            new Color(0xc0, 0x00, 0x00),
            new Color(0x00, 0x00, 0xC0),
            new Color(0x00, 0xC0, 0x00),
            new Color(0xC0, 0xC0, 0x00),
            new Color(0xC0, 0x00, 0xC0),
            new Color(0x00, 0xC0, 0xC0),
            new Color(64, 64, 64),
            new Color(0xFF, 0x40, 0x40),
            new Color(0x40, 0x40, 0xFF),
            new Color(0x40, 0xFF, 0x40),
            new Color(0xFF, 0xFF, 0x40),
            new Color(0xFF, 0x40, 0xFF),
            new Color(0x40, 0xFF, 0xFF),
            new Color(192, 192, 192),
            new Color(0x80, 0x00, 0x00),
            new Color(0x00, 0x00, 0x80),
            new Color(0x00, 0x80, 0x00),
            new Color(0x80, 0x80, 0x00),
            new Color(0x80, 0x00, 0x80),
            new Color(0x00, 0x80, 0x80),
            new Color(0xFF, 0x80, 0x80),
            new Color(0x80, 0x80, 0xFF),
            new Color(0x80, 0xFF, 0x80),
            new Color(0xFF, 0xFF, 0x80),
            new Color(0x00, 0x80, 0x00),
            new Color(0x80, 0xFF, 0xFF)
    });

    static {
        ServiceLoader<HadrumathsService> loader = ServiceLoader.load(HadrumathsService.class);
        TreeMap<Integer, List<HadrumathsService>> all = new TreeMap<>();
        for (HadrumathsService hadrumathsService : loader) {
            HadrumathsServiceDesc d = hadrumathsService.getClass().getAnnotation(HadrumathsServiceDesc.class);
            if (d == null) {
                throw new IllegalArgumentException("Missing @HadrumathsServiceDesc for " + hadrumathsService.getClass());
            }
            all.computeIfAbsent(d.order(), k -> new ArrayList<>()).add(hadrumathsService);
        }
        for (Map.Entry<Integer, List<HadrumathsService>> listEntry : all.entrySet()) {
            for (HadrumathsService hadrumathsService : listEntry.getValue()) {
                hadrumathsService.installService();
            }
        }
    }

    private Maths() {
    }


    public static Domain xdomain(double min, double max) {
        return Domain.forBounds(min, max);
    }

    public static Domain ydomain(double min, double max) {
        return Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, min, max);
    }

    public static DomainExpr ydomain(DomainExpr min, DomainExpr max) {
        return DomainExpr.forBounds(Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, min, max);
    }

    public static Domain zdomain(double min, double max) {
        return Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, min, max);
    }

    public static DomainExpr zdomain(Expr min, Expr max) {
        return DomainExpr.forBounds(Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, min, max);
    }

    public static Domain domain(RightArrowUplet2.Double u) {
        return Domain.forBounds(u.getFirst(), u.getSecond());
    }

    public static Domain domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return Domain.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Domain domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return Domain.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static Expr domain(RightArrowUplet2.Expr u) {
        if (u.getFirst().isDouble() && u.getSecond().isDouble()) {
            return Domain.forBounds(u.getFirst().toDouble(), u.getSecond().toDouble());
        }
        return DomainExpr.forBounds(u.getFirst(), u.getSecond());
    }

    public static Expr domain(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy) {
        if (ux.getFirst().isDouble() && ux.getSecond().isDouble() && uy.getFirst().isDouble() && uy.getSecond().isDouble()) {
            return Domain.forBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble());
        }
        return DomainExpr.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Expr domain(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy, RightArrowUplet2.Expr uz) {
        if (ux.getFirst().isDouble() && ux.getSecond().isDouble() && uy.getFirst().isDouble() && uy.getSecond().isDouble() && uz.getFirst().isDouble() && uz.getSecond().isDouble()) {
            return Domain.forBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble(), uz.getFirst().toDouble(), uz.getSecond().toDouble());
        }
        return DomainExpr.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static DomainExpr domain(Expr min, Expr max) {
        return DomainExpr.forBounds(min, max);
    }

    public static Domain domain(double min, double max) {
        return Domain.forBounds(min, max);
    }

    public static Domain domain(double xmin, double xmax, double ymin, double ymax) {
        return Domain.forBounds(xmin, xmax, ymin, ymax);
    }

    public static DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return DomainExpr.forBounds(xmin, xmax, ymin, ymax);
    }

    public static Domain domain(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return Domain.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return DomainExpr.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static Domain II(RightArrowUplet2.Double u) {
        return Domain.forBounds(u.getFirst(), u.getSecond());
    }

    public static Domain II(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return Domain.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Domain II(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return Domain.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static Expr II(RightArrowUplet2.Expr u) {
        if (u.getFirst().isDouble() && u.getSecond().isDouble()) {
            return Domain.forBounds(u.getFirst().toDouble(), u.getSecond().toDouble());
        }
        return DomainExpr.forBounds(u.getFirst(), u.getSecond());
    }

    public static Expr II(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy) {
        if (ux.getFirst().isDouble() && ux.getSecond().isDouble() && uy.getFirst().isDouble() && uy.getSecond().isDouble()) {
            return Domain.forBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble());
        }
        return DomainExpr.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Expr II(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy, RightArrowUplet2.Expr uz) {
        if (ux.getFirst().isDouble() && ux.getSecond().isDouble() && uy.getFirst().isDouble() && uy.getSecond().isDouble() && uz.getFirst().isDouble() && uz.getSecond().isDouble()) {
            return Domain.forBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble(), uz.getFirst().toDouble(), uz.getSecond().toDouble());
        }
        return DomainExpr.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static DomainExpr II(Expr min, Expr max) {
        return DomainExpr.forBounds(min, max);
    }

    public static Domain II(double min, double max) {
        return Domain.forBounds(min, max);
    }

    public static Domain II(double xmin, double xmax, double ymin, double ymax) {
        return Domain.forBounds(xmin, xmax, ymin, ymax);
    }

    public static DomainExpr II(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return DomainExpr.forBounds(xmin, xmax, ymin, ymax);
    }

    public static Domain II(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return Domain.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static DomainExpr II(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return DomainExpr.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
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

    public static Matrix matrix(int rows, int cols, MatrixCell cellFactory) {
        return Config.getDefaultMatrixFactory().newMatrix(rows, cols, cellFactory);
    }

//    public static Matrix matrix(int rows, int cols, Int2ToComplex cellFactory) {
//        return Config.defaultMatrixFactory.newMatrix(rows, cols, new I2ToComplexMatrixCell(cellFactory));
//    }

    public static Matrix columnMatrix(final Complex... values) {
        return Config.getDefaultMatrixFactory().newColumnMatrix(values);
    }

    public static Matrix columnMatrix(final double... values) {
        Complex[] d = new Complex[values.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = Complex.valueOf(values[i]);
        }
        return Config.getDefaultMatrixFactory().newColumnMatrix(d);
    }

    public static Matrix rowMatrix(final double... values) {
        Complex[] d = new Complex[values.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = Complex.valueOf(values[i]);
        }
        return Config.getDefaultMatrixFactory().newRowMatrix(d);
    }

    public static Matrix rowMatrix(final Complex... values) {
        return Config.getDefaultMatrixFactory().newRowMatrix(values);
    }

    public static Matrix columnMatrix(int rows, final VectorCell cellFactory) {
        return Config.getDefaultMatrixFactory().newColumnMatrix(rows, cellFactory);
    }

    public static Matrix rowMatrix(int columns, final VectorCell cellFactory) {
        return Config.getDefaultMatrixFactory().newRowMatrix(columns, cellFactory);
    }

//    public static Matrix columnMatrix(int rows, final Int2Complex cellFactory) {
//        return Config.getDefaultMatrixFactory().newColumnMatrix(rows, new Int2ComplexCellFactory(cellFactory));
//    }
//    public static Matrix rowMatrix(int columns, final Int2Complex cellFactory) {
//        return Config.getDefaultMatrixFactory().newRowMatrix(columns, new Int2ComplexCellFactory(cellFactory));
//    }
//
//    public static Matrix symmetricMatrix(int rows, int cols, Int2ToComplex cellFactory) {
//        return Config.getDefaultMatrixFactory().newSymmetric(rows, cols, new I2ToComplexMatrixCell(cellFactory));
//    }

    public static Matrix symmetricMatrix(int rows, int cols, MatrixCell cellFactory) {
        return Config.getDefaultMatrixFactory().newSymmetric(rows, cols, cellFactory);
    }

    public static Matrix hermitianMatrix(int rows, int cols, MatrixCell cellFactory) {
        return Config.getDefaultMatrixFactory().newHermitian(rows, cols, cellFactory);
    }

//    public static Matrix hermitianMatrix(int rows, int cols, Int2ToComplex cellFactory) {
//        return Config.getDefaultMatrixFactory().newHermitian(rows, cols, new I2ToComplexMatrixCell(cellFactory));
//    }

    public static Matrix diagonalMatrix(int rows, int cols, MatrixCell cellFactory) {
        return Config.getDefaultMatrixFactory().newDiagonal(rows, cols, cellFactory);
    }

    public static Matrix diagonalMatrix(int rows, final VectorCell cellFactory) {
        return Config.getDefaultMatrixFactory().newDiagonal(rows, cellFactory);
    }

    public static Matrix diagonalMatrix(final Complex... c) {
        return Config.getDefaultMatrixFactory().newDiagonal(c);
    }

    public static Matrix matrix(int dim, MatrixCell cellFactory) {
        return Config.getDefaultMatrixFactory().newMatrix(dim, cellFactory);
    }

//    public static Matrix matrix(int dim, Int2ToComplex cellFactory) {
//        return Config.getDefaultMatrixFactory().newMatrix(dim, new I2ToComplexMatrixCell(cellFactory));
//    }

    public static Matrix matrix(int rows, int columns) {
        return Config.getDefaultMatrixFactory().newMatrix(rows, columns);
    }

    public static Matrix symmetricMatrix(int dim, MatrixCell cellFactory) {
        return Config.getDefaultMatrixFactory().newMatrix(dim, cellFactory);
    }

    public static Matrix hermitianMatrix(int dim, MatrixCell cellFactory) {
        return Config.getDefaultMatrixFactory().newHermitian(dim, cellFactory);
    }

    public static Matrix diagonalMatrix(int dim, MatrixCell cellFactory) {
        return Config.getDefaultMatrixFactory().newDiagonal(dim, cellFactory);
    }

//    public static Matrix symmetricMatrix(int dim, Int2ToComplex cellFactory) {
//        return Config.getDefaultMatrixFactory().newMatrix(dim, new I2ToComplexMatrixCell(cellFactory));
//    }

//    public static Matrix hermitianMatrix(int dim, Int2ToComplex cellFactory) {
//        return Config.getDefaultMatrixFactory().newHermitian(dim, new I2ToComplexMatrixCell(cellFactory));
//    }

//    public static Matrix diagonalMatrix(int dim, Int2ToComplex cellFactory) {
//        return Config.getDefaultMatrixFactory().newDiagonal(dim, new I2ToComplexMatrixCell(cellFactory));
//    }

    public static Matrix randomRealMatrix(int m, int n) {
        return Config.getDefaultMatrixFactory().newRandomReal(m, n);
    }

    public static Matrix randomRealMatrix(int m, int n, int min, int max) {
        return Config.getDefaultMatrixFactory().newRandomReal(m, n, min, max);
    }

    public static Matrix randomRealMatrix(int m, int n, double min, double max) {
        return Config.getDefaultMatrixFactory().newRandomReal(m, n, min, max);
    }

    public static Matrix randomImagMatrix(int m, int n, double min, double max) {
        return Config.getDefaultMatrixFactory().newRandomImag(m, n, min, max);
    }

    public static Matrix randomImagMatrix(int m, int n, int min, int max) {
        return Config.getDefaultMatrixFactory().newRandomImag(m, n, min, max);
    }

    public static Matrix randomMatrix(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        return Config.getDefaultMatrixFactory().newRandom(m, n, minReal, maxReal, minImag, maxImag);
    }

    public static Matrix randomMatrix(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        return Config.getDefaultMatrixFactory().newRandom(m, n, minReal, maxReal, minImag, maxImag);
    }

    public static Matrix randomMatrix(int m, int n, double min, double max) {
        return Config.getDefaultMatrixFactory().newRandom(m, n, min, max);
    }

    public static Matrix randomMatrix(int m, int n, int min, int max) {
        return Config.getDefaultMatrixFactory().newRandom(m, n, min, max);
    }

    public static Matrix randomImagMatrix(int m, int n) {
        return Config.getDefaultMatrixFactory().newRandomImag(m, n);
    }

    public static <T> TMatrix<T> loadTMatrix(TypeReference<T> componentType, File file) throws RuntimeIOException {
        throw new IllegalArgumentException("TODO");
    }

    public static Matrix loadMatrix(File file) throws RuntimeIOException {
        return Config.getDefaultMatrixFactory().load(file);
    }

    public static Matrix matrix(File file) throws RuntimeIOException {
        return Config.getDefaultMatrixFactory().load(file);
    }

    public static void storeMatrix(Matrix m, String file) throws RuntimeIOException {
        m.store(file == null ? (File) null : new File(Config.expandPath(file)));
    }

    public static void storeMatrix(Matrix m, File file) throws RuntimeIOException {
        m.store(file);
    }

    public static Matrix loadOrEvalMatrix(String file, TItem<Matrix> item) throws RuntimeIOException {
        return loadOrEvalMatrix(new File(Config.expandPath(file)), item);
    }

    public static Vector loadOrEvalVector(String file, TItem<TVector<Complex>> item) throws RuntimeIOException {
        return loadOrEvalVector(new File(Config.expandPath(file)), item);
    }

    public static Matrix loadOrEvalMatrix(File file, TItem<Matrix> item) throws RuntimeIOException {
        return loadOrEval($MATRIX, file, item);
    }

    public static Vector loadOrEvalVector(File file, TItem<TVector<Complex>> item) throws RuntimeIOException {
        return loadOrEval($VECTOR, file, (TItem) item);
    }

    public static <T> TMatrix loadOrEvalTMatrix(String file, TItem<TMatrix<T>> item) throws RuntimeIOException {
        return loadOrEvalTMatrix(new File(Config.expandPath(file)), item);
    }

    public static <T> TVector<T> loadOrEvalTVector(String file, TItem<TVector<T>> item) throws RuntimeIOException {
        return loadOrEvalTVector(new File(Config.expandPath(file)), item);
    }

    public static <T> TMatrix<T> loadOrEvalTMatrix(File file, TItem<TMatrix<T>> item) throws RuntimeIOException {
        return loadOrEval((TypeReference) $CMATRIX, file, item);
    }

    public static <T> TVector loadOrEvalTVector(File file, TItem<TVector<T>> item) throws RuntimeIOException {
        return loadOrEval($CVECTOR, file, (TItem) item);
    }

    public static <T> T loadOrEval(TypeReference<T> type, File file, TItem<T> item) throws RuntimeIOException {
        TStoreManager<T> t = null;
        if (type.equals($MATRIX)) {
            t = (TStoreManager<T>) MATRIX_STORE_MANAGER;
        } else if (type.equals($VECTOR)) {
            t = (TStoreManager<T>) VECTOR_STORE_MANAGER;
        } else if (type.getTypeClass().equals(TVector.class)) {
            t = (TStoreManager<T>) TVECTOR_STORE_MANAGER;
        } else if (type.getTypeClass().equals(TMatrix.class)) {
            t = (TStoreManager<T>) TMATRIX_STORE_MANAGER;
        } else {
            throw new IllegalArgumentException("Unsupported store type " + type);
        }
        if (file.exists()) {
            $log.log(Level.INFO, "loading " + file.getAbsolutePath() + " ...");
            T load = t.load(file);
            return load;
        } else {
            Chronometer cr = chrono();
            T tt = item.get();
            cr.stop();
            Chronometer cr2 = chrono();
            t.store(tt, file);
            cr2.stop();
            $log.log(Level.INFO, "exec time " + cr + ". stored in " + cr2 + " to file " + file.getAbsolutePath() + " ...");
            return tt;
        }
    }

    public static Matrix loadMatrix(String file) throws RuntimeIOException {
        return Config.getDefaultMatrixFactory().load(new File(Config.expandPath(file)));
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
        return constantColumnVector(dim, Complex.NaN);
    }

    public static Vector NaNRowVector(int dim) {
        return constantRowVector(dim, Complex.NaN);
    }


    public static TVector<Expr> columnVector(Expr[] expr) {
        return new ArrayTVector<Expr>(EXPR_VECTOR_SPACE, expr, false);
    }

    public static TVector<Expr> rowVector(Expr[] expr) {
        return new ArrayTVector<Expr>(EXPR_VECTOR_SPACE, expr, true);
    }

    public static TVector<Expr> columnEVector(int rows, final TVectorCell<Expr> cellFactory) {
        return columnTVector($EXPR, rows, cellFactory);
    }

    public static TVector<Expr> rowEVector(int rows, final TVectorCell<Expr> cellFactory) {
        return rowTVector($EXPR, rows, cellFactory);
    }

    public static <T> TVector<T> updatableOf(TVector<T> vector) {
        return new UpdatableTVector<T>(
                vector.getComponentType(), new CachedTVectorUpdatableModel<T>(vector, vector.getComponentType()),
                false
        );
    }

    public static <T> TList<T> copyOf(TVector<T> vector) {
        TList<T> ts = list(vector.getComponentType(), vector.isRow(), vector.size());
        ts.appendAll(vector);
        return ts;
    }

    public static <T> TVector<T> columnTVector(TypeReference<T> cls, final TVectorModel<T> cellFactory) {
        return new ReadOnlyTVector<T>(
                cls, false, cellFactory
        );
//        return new UpdatableTVector<>(
//                cls,new CachedTVectorUpdatableModel<>(cellFactory,cls),
//                false
//        );
    }

    public static <T> TVector<T> rowTVector(TypeReference<T> cls, final TVectorModel<T> cellFactory) {
        return new ReadOnlyTVector<>(
                cls, true, cellFactory
        );
//        return new UpdatableTVector<>(
//                cls,new CachedTVectorUpdatableModel<>(cellFactory,cls),
//                true
//        );
    }

    public static <T> TVector<T> columnTVector(TypeReference<T> cls, int rows, final TVectorCell<T> cellFactory) {
        return columnTVector(cls, new TVectorModelFromCell<>(rows, cellFactory));
    }

    public static <T> TVector<T> rowTVector(TypeReference<T> cls, int rows, final TVectorCell<T> cellFactory) {
        return rowTVector(cls, new TVectorModelFromCell<>(rows, cellFactory));
    }

    public static Vector columnVector(int rows, final VectorCell cellFactory) {
        Complex[] arr = new Complex[rows];
        for (int i = 0; i < rows; i++) {
            arr[i] = cellFactory.get(i);
        }
        return columnVector(arr);
    }

    public static Vector rowVector(int columns, final VectorCell cellFactory) {
        Complex[] arr = new Complex[columns];
        for (int i = 0; i < columns; i++) {
            arr[i] = cellFactory.get(i);
        }
        return rowVector(arr);
    }

//    public static Vector columnVector(int rows, final Int2Complex cellFactory) {
//        Int2ComplexCellFactory cc = new Int2ComplexCellFactory(cellFactory);
//        Complex[] arr = new Complex[rows];
//        for (int i = 0; i < rows; i++) {
//            arr[i] = cc.get(i);
//        }
//        return columnVector(arr);
//    }

//    public static Vector rowVector(int columns, final Int2Complex cellFactory) {
//        Int2ComplexCellFactory cc = new Int2ComplexCellFactory(cellFactory);
//        Complex[] arr = new Complex[columns];
//        for (int i = 0; i < columns; i++) {
//            arr[i] = cc.get(i);
//        }
//        return rowVector(arr);
//    }


    public static Vector columnVector(Complex[] elems) {
        return ArrayVector.Column(elems);
    }

    public static Vector columnVector(double[] elems) {
        return ArrayVector.Column(ArrayUtils.toComplex(elems));
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

    public static double dstepsLength(double min, double max, double step) {
        if (step >= 0) {
            if (max < min) {
                return 0;
            }
            return (int) Math.abs((max - min) / step) + 1;
        } else {
            if (min < max) {
                return 0;
            }
            return (int) Math.abs((max - min) / step) + 1;
        }
    }

    public static double dstepsElement(double min, double max, double step, int index) {
        if (step >= 0) {
            if (max < min) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            return min + index * step;
        } else {
            if (min < max) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            return min + index * step;
        }
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
        if (c == 0) {
            return HALF_PI;
        }
        return Math.atan(1 / c);
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

    public static Complex csum(Complex... c) {
        MutableComplex x = new MutableComplex();
        for (Complex c1 : c) {
            x.add(c1);
        }
        return x.toComplex();
    }

    public static Complex sum(Complex... c) {
        return csum(c);
    }

    public static Complex csum(TVectorModel<Complex> c) {
        MutableComplex x = new MutableComplex();
        int size = c.size();
        for (int i = 0; i < size; i++) {
            x.add(c.get(i));
        }
        return x.toComplex();
    }

    public static Complex csum(int size, TVectorCell<Complex> c) {
        MutableComplex x = new MutableComplex();
        for (int i = 0; i < size; i++) {
            x.add(c.get(i));
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
        if (complexAsDouble == null) {
            complexAsDouble = ComplexAsDouble.REAL;
        }
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
                case ARG: {
                    z = ArrayUtils.getArg(c);
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
                case ARG: {
                    z = ArrayUtils.getArg(c);
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

    public static Vector vector(TVector v) {
        v = v.to($COMPLEX);
        if (v instanceof Vector) {
            return (Vector) v;
        }
        TVector finalV = v;
        return new ReadOnlyVector(new TVectorModel<Complex>() {
            @Override
            public int size() {
                return finalV.size();
            }

            @Override
            public Complex get(int index) {
                return (Complex) finalV.get(index);
            }
        }, v.isRow());
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
        return Complex.valueOf(r * Math.cos(r), r * sin(p));
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

    public static TList sineSeq(String borders, int m, int n, Domain domain) {
        return sineSeq(borders, m, n, domain, PlaneAxis.XY);
    }

    public static TList sineSeq(String borders, int m, int n, Domain domain, PlaneAxis plane) {
        DoubleParam mm = new DoubleParam("m");
        DoubleParam nn = new DoubleParam("n");
        switch (plane) {
            case XY: {
                return ParamExprList.create(false, new SinSeqXY(borders, mm, nn, domain), new DoubleParam[]{mm, nn}, new int[]{m, n});
            }
            case XZ: {
                return ParamExprList.create(false, new SinSeqXZ(borders, mm, nn, domain), new DoubleParam[]{mm, nn}, new int[]{m, n});
            }
            case YZ: {
                return ParamExprList.create(false, new SinSeqYZ(borders, mm, nn, domain), new DoubleParam[]{mm, nn}, new int[]{m, n});
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
        return Any.wrap(e);
    }

    public static Any any(Double e) {
        return any(expr(e));
    }

//    public static Any any(Domain e) {
//        return new Any(expr(e));
//    }

    @Deprecated
    public static TList<Expr> seq() {
        return elist();
    }

    public static TList<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax, Int2Filter filter) {
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

    public static TList<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax, DoubleParam p, int pmax, Int3Filter filter) {
        double[][] cross = cross(dsteps(0, mmax), dsteps(0, nmax), dsteps(0, pmax), filter == null ? null : new Double3Filter() {
            @Override
            public boolean accept(double a, double b, double c) {
                return filter.accept((int) a, (int) b, (int) c);
            }
        });
        return seq(pattern, m, n, p, cross);
    }

    public static TList<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax) {
        return seq(pattern, m, n, cross(dsteps(0, mmax), dsteps(0, nmax)));
    }

    public static TList<Expr> seq(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues) {
        return seq(pattern, m, n, cross(mvalues, nvalues));
    }

    public static TList<Expr> seq(final Expr pattern, final DoubleParam m, final DoubleParam n, final double[][] values) {
        return Config.DEFAULT_EXPR_SEQ_FACTORY.newUnmodifiableSequence(values.length, new SimpleSeq2(values, m, n, pattern));
    }

    public static TList<Expr> seq(final Expr pattern, final DoubleParam m, final DoubleParam n, final DoubleParam p, final double[][] values) {
        return Config.DEFAULT_EXPR_SEQ_FACTORY.newUnmodifiableSequence(values.length,
                new SimpleSeqMulti(pattern, new DoubleParam[]{m, n, p}, values)
        );
    }

    public static TList<Expr> seq(final Expr pattern, final DoubleParam[] m, final double[][] values) {
        return Config.DEFAULT_EXPR_SEQ_FACTORY.newUnmodifiableSequence(values.length, new SimpleSeqMulti(pattern, m, values));
    }

    public static TList<Expr> seq(final Expr pattern, final DoubleParam m, int min, int max) {
        return seq(pattern, m, dsteps(min, max, 1));
    }

    public static TList<Expr> seq(final Expr pattern, final DoubleParam m, final double[] values) {
        return Config.DEFAULT_EXPR_SEQ_FACTORY.newUnmodifiableSequence(values.length, new SimpleSeq1(values, m, pattern));
    }

    public static ExprMatrix2 matrix(final Expr pattern, final DoubleParam m, final double[] mvalues, final DoubleParam n, final double[] nvalues) {
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
//    public static TList<Expr>[] seq(ExprArrayList pattern, DoubleParam m, double[] values) {
//        ExprArrayList[] list = new ExprArrayList[values.length];
//        for (int i = 0; i < values.length; i++) {
//            list[i] = pattern.setParam(m, values[i]);
//        }
//        return list;
//    }
//
//    public static TList<Expr>[][] seq(ExprArrayList[] pattern, DoubleParam m, double[] values) {
//        ExprArrayList[][] list = new ExprArrayList[values.length][];
//        for (int i = 0; i < values.length; i++) {
//            ExprArrayList[] sub = new ExprArrayList[pattern.length];
//            for (int j = 0; j < sub.length; j++) {
//                sub[j] = pattern[j].setParam(m, values[i]);
//            }
//            list[i] = sub;
//        }
//        return list;
//    }
//
//    public static TList<Expr>[][][] seq(ExprArrayList[][] pattern, DoubleParam m, double[] values) {
//        ExprArrayList[][][] list = new ExprArrayList[values.length][][];
//        for (int i = 0; i < values.length; i++) {
//            ExprArrayList[][] sub = new ExprArrayList[pattern.length][];
//            for (int j = 0; j < sub.length; j++) {
//                ExprArrayList[] sub2 = new ExprArrayList[sub.length];
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
        if (e.isDD()) {
            return true;
        }
        if (e.isDC()) {
            return e.toDC().getImagDD().isZero();
        }
        if (e.isDM()) {
            ComponentDimension cd = e.getComponentDimension();
            for (int i = 0; i < cd.rows; i++) {
                for (int j = 0; j < cd.columns; j++) {
                    if (!isReal(e.toDM().getComponent(i, j))) {
                        return false;
                    }
                }
            }
        }
        return false;
        //return e.toDC().getImagDD().isZero();
    }

    public static boolean isImag(Expr e) {
        if (e.isZero()) {
            return false;
        }
        return !isReal(e);
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

    public static Complex complex(int e) {
        return Complex.valueOf(e);
    }

    public static Complex complex(double e) {
        return Complex.valueOf(e);
    }

    public static Complex complex(double a, double b) {
        return Complex.valueOf(a, b);
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
//        Complex[][] complexes2 = dc.computeComplexArg(dsteps,dsteps);
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

    public static AxisFunction axis(Axis e) {
        if (e == null) {
            return null;
        }
        switch (e) {
            case X:
                return (AxisFunction) X;
            case Y:
                return (AxisFunction) Y;
            case Z:
                return (AxisFunction) Z;
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public static Expr transformAxis(Expr e, AxisFunction a1, AxisFunction a2, AxisFunction a3) {
        return transformAxis(e, a1.getAxis(), a2.getAxis(), a3.getAxis());
    }

    public static Expr transformAxis(Expr e, Axis a1, Axis a2, Axis a3) {
        return new AxisTransform(e, new Axis[]{a1, a2, a3}, 3);
    }

    public static Expr transformAxis(Expr e, AxisFunction a1, AxisFunction a2) {
        return transformAxis(e, a1.getAxis(), a2.getAxis());
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
        return ((int) (d)) == d;
//        return Math.floor(d) == d;
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
            geometry = Domain.FULLXY.toGeometry();
        }
        if (geometry.isRectangular()) {
            return DoubleValue.valueOf(value, geometry.getDomain());
        }
        return new Shape2D(value, geometry);
    }

    public static DoubleToDouble expr(double value, Domain geometry) {
        if (geometry == null) {
            geometry = Domain.FULLXY;
        }
//        return geometry.toDD();
        if (geometry.isRectangular()) {
            return DoubleValue.valueOf(value, geometry.getDomain());
        }
        return new Shape2D(value, geometry);
    }

    public static DoubleToDouble expr(Domain domain) {
        return domain.toDD();
    }

    public static DoubleToDouble expr(Geometry domain) {
        return expr(1, domain);
    }

    public static Expr expr(Complex a, Geometry geometry) {
        if (geometry == null) {
            geometry = Domain.FULLXY.toGeometry();
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

    public static Expr expr(Complex a, Domain geometry) {
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

    public static <T extends Expr> TList<T> preload(TList<T> list) {
        return DefaultExprSequenceFactory.INSTANCE.newPreloadedSequence(list.length(), list);
    }

    public static <T extends Expr> TList<T> withCache(TList<T> list) {
        //DefaultExprSequenceFactory.INSTANCE.newCachedSequence(length(), this);
        return DefaultExprSequenceFactory.INSTANCE.newCachedSequence(list.length(), list);
    }

//    public static <T extends Expr> TList<T> simplify(TList<T> list) {
//        //        return DefaultExprSequenceFactory.INSTANCE.newUnmodifiableSequence(length(), new SimplifiedSeq(this));
//        return DefaultExprSequenceFactory.INSTANCE.newUnmodifiableSequence(list.length(), new SimplifiedSeq(list));
//    }

    public static <T> TList<T> abs(TList<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.abs(e);
            }
        });
    }

    public static <T> TList<T> db(TList<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.db(e);
            }
        });
    }

    public static <T> TList<T> db2(TList<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.db2(e);
            }
        });
    }


    public static <T> TList<T> real(TList<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.real(e);
            }
        });
    }

    public static <T> TList<T> imag(TList<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.imag(e);
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

    public static double max(double a, double b) {
        return a > b ? a : b;
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static long max(long a, long b) {
        return a > b ? a : b;
    }

    public static double min(double a, double b) {
        return a < b ? a : b;
    }

    public static double min(double[] arr) {
        if (arr.length == 0) {
            return Double.NaN;
        }
        double min = Double.NaN;
        for (int i = 0; i < arr.length; i++) {
            if (Double.isNaN(min) || (!Double.isNaN(arr[i]) && arr[i] < min)) {
                min = arr[i];
            }
        }
        return min;
    }

    public static double max(double[] arr) {
        if (arr.length == 0) {
            return Double.NaN;
        }
        double max = Double.NaN;
        for (int i = 0; i < arr.length; i++) {
            if (Double.isNaN(max) || (!Double.isNaN(arr[i]) && arr[i] > max)) {
                max = arr[i];
            }
        }
        return max;
    }

    public static double avg(double[] arr) {
        if (arr.length == 0) {
            return Double.NaN;
        }
        double max = 0;
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((!Double.isNaN(arr[i]))) {
                max += arr[i];
                count++;
            }
        }
        if (count == 0) {
            return Double.NaN;
        }
        return max / count;
    }

    public static int min(int a, int b) {
        return a < b ? a : b;
    }

    public static Complex min(Complex a, Complex b) {
        int i = a.compareTo(b);
        return i <= 0 ? a : b;
    }

    public static Complex max(Complex a, Complex b) {
        int i = a.compareTo(b);
        return i >= 0 ? a : b;
    }

    public static long min(long a, long b) {
        return a < b ? a : b;
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
        return Math.sinh(x);
//        return (Math.exp(x) - Math.exp(-x)) / 2;
    }

    public static double cosh(double x) {
        return Math.cosh(x);
//        return (Math.exp(x) + Math.exp(-x)) / 2;
    }

    public static double tanh(double x) {
        return Math.tanh(x);
//        double a = Math.exp(+x);
//        double b = Math.exp(-x);
//        return a == Double.POSITIVE_INFINITY ? 1 : b == Double.POSITIVE_INFINITY ? -1 : (a - b) / (a + b);
    }

    public static double abs(double a) {
        return Math.abs(a);
    }

    public static int abs(int a) {
        return Math.abs(a);
    }

    public static double cotanh(double x) {
        return 1 / Math.tanh(x);
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
        return Config.getIntegrationOperator().eval(domain, e);
    }

    public static Expr esum(int size, TVectorCell<Expr> f) {
        return Maths.esum(seq(size, f));
    }

    public static Expr esum(int size1, int size2, TMatrixCell<Expr> e) {
        RepeatableOp<Expr> c = EXPR_VECTOR_SPACE.addRepeatableOp();
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                c.append(e.get(i, j));
            }
        }
        return c.eval();
    }

    public static Complex csum(int size1, int size2, TMatrixCell<Complex> e) {
        MutableComplex c = new MutableComplex();
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                c.add(e.get(i, j));
            }
        }
        return c.toComplex();
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
////        ScalarProductCache d = scalarProductCache(gp, fn, ProgressMonitorFactory.out().temporize(1000));
////        Complex gf = d.gf(2, 2);
////        System.out.println(gf);
//    }

    public static TVector<Expr> seq(int size1, TVectorCell<Expr> f) {
        return new ReadOnlyTVector<Expr>($EXPR, false, new TVectorModelFromCell(size1, f));
    }

    public static TVector<Expr> seq(int size1, int size2, TMatrixCell<Expr> f) {
        int sizeFull = size1 * size2;
        TVectorModel<Expr> tVectorModel = new TVectorModel<Expr>() {
            @Override
            public int size() {
                return sizeFull;
            }

            @Override
            public Expr get(int index) {
                return f.get(index / size2, index % size2);
            }
        };
        return new ReadOnlyTVector<Expr>($EXPR, false, tVectorModel);
    }

    private static TMatrix<Complex> resolveBestScalarProductCache(Expr[] gp, Expr[] fn, AxisXY axis, ScalarProductOperator sp, ProgressMonitor monitor) {
        int rows = gp.length;
        int columns = fn.length;

//        if (doSimplifyAll) {
//            Expr[] finalFn = fn;
//            Expr[] finalGp = gp;
//            Expr[][] fg = Maths.invokeMonitoredAction(emonitor, "Simplify All", new MonitoredAction<Expr[][]>() {
//                @Override
//                public Expr[][] process(ProgressMonitor monitor, String messagePrefix) throws Exception {
//                    Expr[][] fg = new Expr[2][];
//                    fg[0] = simplifyAll(finalFn, hmon[0]);
//
//                    fg[1] = simplifyAll(finalGp, hmon[1]);
//                    return fg;
//                }
//            });
//            fn = fg[0];
//            gp = fg[1];
//        }
        boolean doubleValue = true;
        boolean scalarValue = true;
//        int maxF = fn.length;
//        int maxG = gp.length;
        for (Expr expr : fn) {
            if (!expr.isScalarExpr()) {
                scalarValue = false;
                break;
            }
        }
        if (scalarValue) {
            for (Expr expr : gp) {
                if (!expr.isScalarExpr()) {
                    scalarValue = false;
                    break;
                }
            }
        }
        for (Expr expr : fn) {
            if (!expr.isDoubleTyped()) {
                doubleValue = false;
                break;
            }
        }
        if (doubleValue) {
            for (Expr expr : gp) {
                if (!expr.isDoubleTyped()) {
                    doubleValue = false;
                    break;
                }
            }
        }

        if (!Config.memoryCanStores(24L * rows * columns)) {
            return new MatrixScalarProductCache(Config.getLargeMatrixFactory()).evaluate(sp, fn, gp, axis, monitor).toMatrix();
        }
        if (doubleValue) {
            return new MemDoubleScalarProductCache(scalarValue).evaluate(sp, fn, gp, axis, monitor).toMatrix();
        }
        return new MemComplexScalarProductCache(sp.isHermitian(), doubleValue, scalarValue).evaluate(sp, fn, gp, axis, monitor).toMatrix();
    }

    public static TMatrix<Complex> scalarProductCache(Expr[] gp, Expr[] fn, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, AxisXY.XY, Config.getScalarProductOperator(), monitor);
    }

    public static TMatrix<Complex> scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, AxisXY.XY, Config.getScalarProductOperator(), monitor);
    }

    public static TMatrix<Complex> scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, AxisXY axis, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, axis, sp, monitor);
    }

    public static TMatrix<Complex> scalarProductCache(Expr[] gp, Expr[] fn, AxisXY axis, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, axis, Config.getScalarProductOperator(), monitor);
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
        throw new IllegalArgumentException("Unsupported axis " + axis);
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

//    public static <T> Expr sum(TList<T> sequence) {
//        int len = sequence.length();
//        if (len == 0) {
//            return CZERO;
//        }
//        List<Expr> all = new ArrayList<>();
//        MutableComplex c = new MutableComplex();
//        Queue<Expr> t = new LinkedList<>();
//        for (int i = 0; i < len; i++) {
//            Expr e = (Expr)sequence.get(i);
//            t.add(e);
//            while (!t.isEmpty()) {
//                Expr e2 = t.remove();
//                if (e2 instanceof Plus) {
//                    t.addAll(e2.getSubExpressions());
//                } else {
//                    if (e2.isComplex()) {
//                        c.add(e2.toComplex());
//                    } else {
//                        all.add(e2);
//                    }
//                }
//            }
//        }
//        if (all.isEmpty()) {
//            return c.toComplex();
//        }
//        if (!c.isZero()) {
//            all.add(c.toComplex());
//        }
//        return new Plus(all);
//    }

    public static double scalarProduct(DoubleToDouble f1, DoubleToDouble f2) {
        return Config.getScalarProductOperator().evalDD(null, f1, f2);
    }

    public static Vector scalarProduct(Expr f1, TVector<Expr> f2) {
        VectorCell spfact = new VectorCell() {
            @Override
            public Complex get(int index) {
                return scalarProduct(f1, f2.get(index));
            }
        };
        return f2.isColumn() ? columnVector(f2.size(), spfact) : rowVector(f2.size(), spfact);
    }

    public static Matrix scalarProduct(Expr f1, TMatrix<Expr> f2) {
        return matrix(f2.getRowCount(), f2.getColumnCount(), new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return scalarProduct(f1, f2.get(row, column));
            }
        });
    }

    public static Vector scalarProduct(TVector<Expr> f2, Expr f1) {
        VectorCell spfact = new VectorCell() {
            @Override
            public Complex get(int index) {
                return scalarProduct(f2.get(index), f1);
            }
        };
        return f2.isColumn() ? columnVector(f2.size(), spfact) : rowVector(f2.size(), spfact);
    }

    public static Matrix scalarProduct(TMatrix<Expr> f2, Expr f1) {
        return matrix(f2.getRowCount(), f2.getColumnCount(), new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return scalarProduct(f2.get(row, column), f1);
            }
        });
    }

    public static Complex scalarProduct(Domain domain, Expr f1, Expr f2) {
        return Config.getScalarProductOperator().eval(domain, f1, f2);
    }

    public static Complex scalarProduct(Expr f1, Expr f2) {
        return Config.getScalarProductOperator().eval(f1, f2);
    }

    public static Matrix scalarProductMatrix(TVector<Expr> g, TVector<Expr> f) {
        return matrix(Config.getScalarProductOperator().eval(g, f, null));
    }

    public static TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f) {
        return Config.getScalarProductOperator().eval(g, f, null);
    }

    public static TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, monitor);
    }

    public static Matrix scalarProductMatrix(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return matrix(Config.getScalarProductOperator().eval(g, f, monitor));
    }

    public static TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f, AxisXY axis, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, axis, monitor);
    }

    public static Matrix scalarProductMatrix(Expr[] g, Expr[] f) {
        return (Matrix) Config.getScalarProductOperator().eval(g, f, null).to($COMPLEX);
    }

    public static Complex scalarProduct(Matrix g, Matrix f) {
        return g.scalarProduct(f);
    }

    public static Expr scalarProduct(Matrix g, TVector<Expr> f) {
        return f.scalarProduct(g.to($EXPR));
    }

    public static Expr scalarProductAll(Matrix g, TVector<Expr>... f) {
        return g.toVector().to($EXPR).scalarProductAll((TVector[]) f);
    }

    public static TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f) {
        return Config.getScalarProductOperator().eval(g, f, null);
    }

    public static TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, monitor);
    }


    public static Matrix scalarProductMatrix(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return matrix(Config.getScalarProductOperator().eval(g, f, monitor));
    }


    public static TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f, AxisXY axis, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, axis, monitor);
    }

    //    public static String scalarProductToMatlabString(DFunctionXY f1, DFunctionXY f2, DomainXY domain0, ToMatlabStringParam... format) {
//        return defaultScalarProduct.scalarProductToMatlabString(domain0, f1, f2, format) ;
//    }
//
//    public static String scalarProductToMatlabString(DomainXY domain0,CFunctionXY f1, CFunctionXY f2, ToMatlabStringParam... format) {
//        return defaultScalarProduct.scalarProductToMatlabString(domain0, f1, f2, format) ;
//    }
    public static ExprList elist(int size) {
        return new ExprArrayList(false, size);
    }

    public static ExprList elist(boolean row, int size) {
        return new ExprArrayList(row, size);
    }

    public static ExprList elist(Expr... vector) {
        return new ExprArrayList(false, vector);
    }

    public static TList<Complex> clist() {
        return new ArrayTList<>($COMPLEX, false, 0);
    }

    public static TList<Complex> clist(int size) {
        return new ArrayTList<>($COMPLEX, false, size);
    }

    public static TList<Complex> clist(Complex... vector) {
        return new ArrayTList<>($COMPLEX, false, vector);
    }

    public static TList<Matrix> mlist() {
        return Maths.list($MATRIX, false, 0);
    }

    public static TList<Matrix> mlist(int size) {
        return Maths.list($MATRIX, false, size);
    }

    public static TList<Matrix> mlist(Matrix... items) {
        TList<Matrix> list = Maths.list($MATRIX, false, items.length);
        list.appendAll(Arrays.asList(items));
        return list;
    }

    public static TList<TList<Complex>> clist2() {
        return list($CLIST, false, 0);
    }

    public static TList<TList<Expr>> elist2() {
        return Maths.list($ELIST, false, 0);
    }

    public static TList<TList<Double>> dlist2() {
        return Maths.list($DLIST, false, 0);
    }

    public static TList<TList<Integer>> ilist2() {
        return Maths.list($ILIST, false, 0);
    }

    public static TList<TList<Matrix>> mlist2() {
        return Maths.list($MLIST, false, 0);
    }

    public static TList<TList<Boolean>> blist2() {
        return list($BLIST, false, 0);
    }

    public static <T> TList<T> list(TypeReference<T> type) {
        return list(type, false, 0);
    }

    public static <T> TList<T> list(TypeReference<T> type, int initialSize) {
        return list(type, false, initialSize);
    }

    public static <T> TList<T> listro(TypeReference<T> type, boolean row, TVectorModel<T> model) {
        if (type.equals(Maths.$DOUBLE)) {
            return (TList<T>) new DoubleArrayList.DoubleReadOnlyList(row, (TVectorModel<Double>) model);
        }
        if (type.equals(Maths.$INTEGER)) {
            return (TList<T>) new IntArrayList.IntReadOnlyList(row, (TVectorModel<Integer>) model);
        }
        if (type.equals(Maths.$LONG)) {
            return (TList<T>) new LongArrayList.LongReadOnlyList(row, (TVectorModel<Long>) model);
        }
        if (type.equals(Maths.$BOOLEAN)) {
            return (TList<T>) new BooleanArrayList.BooleanReadOnlyList(row, (TVectorModel<Boolean>) model);
        }
        return new ReadOnlyTList<T>(type, row, model);
    }

    public static <T> TList<T> list(TypeReference<T> type, boolean row, int initialSize) {
        if (type.equals($EXPR)) {
            return (TList<T>) elist(row, initialSize);
        }
        if (type.equals($DOUBLE)) {
            return (TList<T>) dlist(row, initialSize);
        }
        if (type.equals($INTEGER)) {
            return (TList<T>) ilist(row, initialSize);
        }
        if (type.equals($LONG)) {
            return (TList<T>) llist(row, initialSize);
        }
        if (type.equals($BOOLEAN)) {
            return (TList<T>) blist(row, initialSize);
        }
        return new ArrayTList<T>(type, row, initialSize);
    }

    public static <T> TList<T> list(TVector<T> vector) {
        TList<T> exprs = list(vector.getComponentType());
        for (T o : vector) {
            exprs.append(o);
        }
        return exprs;
    }

    public static ExprList elist(Matrix vector) {
        Vector complexes = vector.toVector();
        ExprList exprs = elist(complexes.size());
        exprs.appendAll((TVector) complexes);
        return exprs;
    }

    public static <T> TVector<T> vscalarProduct(TVector<T> vector, TVector<TVector<T>> vectors) {
        return vector.vscalarProduct(vectors.toArray(new TVector[vectors.size()]));
    }

    public static TList<Expr> elist() {
        return new ExprArrayList(false, 0);
    }

    public static <T> TList<T> concat(TList<T>... a) {
        TList<T> ts = list(a[0].getComponentType());
        for (TList<T> t : a) {
            ts.appendAll(t);
        }
        return ts;
    }

    public static TList<Double> dlist() {
        return new DoubleArrayList();
    }

    public static TList<Double> dlist(ToDoubleArrayAware items) {
        return dlist(items.toDoubleArray());
    }

    public static TList<Double> dlist(double[] items) {
        DoubleList doubles = new DoubleArrayList(items.length);
        doubles.appendAll(items);
        return doubles;
    }

    public static TList<Double> dlist(boolean row, int size) {
        return new DoubleArrayList(row, size);
    }

    public static TList<Double> dlist(int size) {
        return new DoubleArrayList(size);
    }

    public static TList<String> slist() {
        return new ArrayTList<String>($STRING, false, 0);
    }

    public static TList<String> slist(String[] items) {
        TList<String> doubles = new ArrayTList<String>($STRING, false, items.length);
        for (String item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static TList<String> slist(boolean row, int size) {
        return new ArrayTList<String>($STRING, row, size);
    }

    public static TList<String> slist(int size) {
        return new ArrayTList<String>($STRING, false, size);
    }

    public static TList<Boolean> blist() {
        return new BooleanArrayList(false, 0);
    }

    public static TList<Boolean> dlist(boolean[] items) {
        TList<Boolean> doubles = new BooleanArrayList(false, items.length);
        for (boolean item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static TList<Boolean> blist(boolean row, int size) {
        return new BooleanArrayList(row, size);
    }

    public static TList<Boolean> blist(int size) {
        return new BooleanArrayList(false, size);
    }

    public static IntList ilist() {
        return new IntArrayList(false, 0);
    }

    public static TList<Integer> ilist(int[] items) {
        TList<Integer> doubles = new IntArrayList(false, items.length);
        for (int item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static TList<Integer> ilist(int size) {
        return new IntArrayList(false, size);
    }

    public static TList<Integer> ilist(boolean row, int size) {
        return new IntArrayList(row, size);
    }

    public static LongList llist() {
        return new LongArrayList(false, 0);
    }

    public static TList<Long> llist(long[] items) {
        TList<Long> doubles = new LongArrayList(false, items.length);
        for (long item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static TList<Long> llist(int size) {
        return new LongArrayList(false, size);
    }

    public static TList<Long> llist(boolean row, int size) {
        return new LongArrayList(row, size);
    }

    public static <T> T sum(TypeReference<T> type, T... arr) {
        if ($COMPLEX.isAssignableFrom(type)) {
            return (T) sum((Complex[]) arr);
        }
        if ($EXPR.isAssignableFrom(type)) {
            return (T) sum((Expr[]) arr);
        }
        VectorSpace<T> s = getVectorSpace(type);
        T a = s.zero();
        for (int i = 0; i < arr.length; i++) {
            a = s.add(a, arr[i]);
        }
        return a;
    }

    public static <T> T sum(TypeReference<T> type, TVectorModel<T> arr) {
        if ($COMPLEX.isAssignableFrom(type)) {
            return (T) csum((TVectorModel<Complex>) arr);
        }
        if ($EXPR.isAssignableFrom(type)) {
            return (T) esum((TVectorModel<Expr>) arr);
        }
        VectorSpace<T> s = getVectorSpace(type);
        T a = s.zero();
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            a = s.add(a, arr.get(i));
        }
        return a;
    }

    public static <T> T sum(TypeReference<T> type, int size, TVectorCell<T> arr) {
        return sum(type, new TVectorModelFromCell<>(size, arr));
    }

    public static <T> T mul(TypeReference<T> type, T... arr) {
        if ($COMPLEX.isAssignableFrom(type)) {
            return (T) mul((Complex[]) arr);
        }
        if ($EXPR.isAssignableFrom(type)) {
            return (T) mul((Expr[]) arr);
        }
        VectorSpace<T> s = getVectorSpace(type);
        T a = s.one();
        for (int i = 0; i < arr.length; i++) {
            a = s.mul(a, arr[i]);
        }
        return a;
    }

    public static <T> T mul(TypeReference<T> type, TVectorModel<T> arr) {
        if ($COMPLEX.isAssignableFrom(type)) {
            return (T) cmul((TVectorModel<Complex>) arr);
        }
        if ($EXPR.isAssignableFrom(type)) {
            return (T) emul((TVectorModel<Expr>) arr);
        }
        VectorSpace<T> s = getVectorSpace(type);
        T a = s.one();
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            a = s.mul(a, arr.get(i));
        }
        return a;
    }

    public static Expr sum(Expr... arr) {
        int len = arr.length;
        if (len == 0) {
            return CZERO;
        }
        List<Expr> all = new ArrayList<>();
        MutableComplex c = new MutableComplex();
        Queue<Expr> t = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            Expr e = arr[i];
            t.add(e);
            while (!t.isEmpty()) {
                Expr e2 = t.remove();
                if (e2 instanceof Plus) {
                    t.addAll(e2.getSubExpressions());
                } else {
                    if (e2.isComplex()) {
                        c.add(e2.toComplex());
                    } else {
                        all.add(e2);
                    }
                }
            }
        }
        if (all.isEmpty()) {
            return c.toComplex();
        }
        if (!c.isZero()) {
            all.add(c.toComplex());
        }
        if (all.size() == 1) {
            return all.get(0);
        }
        return new Plus(all);
    }

    public static Expr esum(TVectorModel<Expr> arr) {
        int len = arr.size();
        if (len == 0) {
            return CZERO;
        }
        List<Expr> all = new ArrayList<>();
        MutableComplex c = new MutableComplex();
        Queue<Expr> t = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            Expr e = arr.get(i);
            t.add(e);
            while (!t.isEmpty()) {
                Expr e2 = t.remove();
                if (e2 instanceof Plus) {
                    t.addAll(e2.getSubExpressions());
                } else {
                    if (e2.isComplex()) {
                        c.add(e2.toComplex());
                    } else {
                        all.add(e2);
                    }
                }
            }
        }
        if (all.isEmpty()) {
            return c.toComplex();
        }
        if (!c.isZero()) {
            all.add(c.toComplex());
        }
        return new Plus(all);
    }

    public static <T> TMatrix<T> mul(TMatrix<T> a, TMatrix<T> b) {
        return a.mul(b);
    }

    public static Matrix mul(Matrix a, Matrix b) {
        return a.mul(b);
    }

    public static Expr mul(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.mul(a, b);
    }

    public static TVector<Expr> edotmul(TVector<Expr>... arr) {
        TypeReference cls = arr[0].getComponentType();
        for (int i = 0; i < arr.length; i++) {
            cls = ReflectUtils.lowestCommonAncestor(cls, arr[i].getComponentType());
        }
        VectorSpace<Expr> componentVectorSpace = Maths.getVectorSpace(cls);
        return new ReadOnlyTVector<>(arr[0].getComponentType(), arr[0].isRow(), new TVectorModel<Expr>() {
            @Override
            public int size() {
                return arr[0].size();
            }

            @Override
            public Expr get(int index) {
                Expr e = arr[0].get(index);
                for (int i = 1; i < arr.length; i++) {
                    TVector<Expr> v = arr[i];
                    e = componentVectorSpace.mul(e, v.get(index));
                }
                return e;
            }
        });
    }

    public static TVector<Expr> edotdiv(TVector<Expr>... arr) {
        VectorSpace<Expr> componentVectorSpace = arr[0].getComponentVectorSpace();
        return new ReadOnlyTVector<>(arr[0].getComponentType(), arr[0].isRow(), new TVectorModel<Expr>() {
            @Override
            public int size() {
                return arr[0].size();
            }

            @Override
            public Expr get(int index) {
                Expr e = arr[0].get(index);
                for (int i = 1; i < arr.length; i++) {
                    TVector<Expr> v = arr[i];
                    e = componentVectorSpace.div(e, v.get(index));
                }
                return e;
            }
        });
    }

    public static Complex cmul(TVectorModel<Complex> arr) {
        int len = arr.size();
        if (len == 0) {
            return CZERO;
        }
        MutableComplex c = new MutableComplex(1, 0);
        for (int i = 0; i < len; i++) {
            Complex complex = arr.get(i);
            if (complex.isZero()) {
                return CZERO;
            }
            c.mul(complex);
        }
        return c.toComplex();
    }

    public static Expr emul(TVectorModel<Expr> arr) {
        int len = arr.size();
        if (len == 0) {
            return CZERO;
        }
        List<Expr> all = new ArrayList<>();
        Domain d = null;
        MutableComplex c = new MutableComplex(1, 0);
        Queue<Expr> t = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            Expr e = arr.get(i);
            t.add(e);
            while (!t.isEmpty()) {
                Expr e2 = t.remove();
                if (e2 instanceof Mul) {
                    t.addAll(e2.getSubExpressions());
                } else {
                    if (e2.isComplexExpr()) {
                        Complex v = e2.toComplex();
                        if (c.isZero()) {
                            return CZERO;
                        }
                        c.mul(v);
                        if (d == null) {
                            d = e2.getDomain();
                        } else {
                            d = d.intersect(e2.getDomain());
                        }
                    } else {
                        all.add(e2);
                    }
                }
            }
        }
        Complex complex = c.toComplex();
        Expr complexExpr = d == null ? complex : complex.mul(d);
        if (all.isEmpty()) {
            return complexExpr;
        }
        if (!complexExpr.equals(CONE)) {
            all.add(0, complexExpr);
        }
        return new Mul(all.toArray(new Expr[all.size()]));
    }

    public static Expr mul(Expr... e) {
        return emul(new TVectorModel<Expr>() {
            @Override
            public int size() {
                return e.length;
            }

            @Override
            public Expr get(int index) {
                return e[index];
            }
        });
    }

    public static Expr pow(Expr a, Expr b) {
        return new Pow(a, b);
    }

    public static Expr sub(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.sub(a, b);
    }

    public static Expr add(Expr a, double b) {
        return add(a, Complex.valueOf(b));
    }

    public static Expr mul(Expr a, double b) {
        return mul(a, Complex.valueOf(b));
    }

    public static Expr sub(Expr a, double b) {
        return sub(a, Complex.valueOf(b));
    }

    public static Expr div(Expr a, double b) {
        return div(a, Complex.valueOf(b));
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

    public static <T> TVector<Expr> expr(TVector<T> vector) {
        return vector.to($EXPR);
    }

    public static TMatrix<Expr> expr(TMatrix<Complex> matrix) {
        return new ReadOnlyTMatrix<Expr>($EXPR, new TMatrixModel<Expr>() {
            @Override
            public Expr get(int row, int column) {
                return matrix.get(row, column);
            }

            @Override
            public int getColumnCount() {
                return matrix.getColumnCount();
            }

            @Override
            public int getRowCount() {
                return matrix.getRowCount();
            }
        });
    }

    public static <T> TMatrix<T> tmatrix(TypeReference<T> type, TMatrixModel<T> model) {
        return new ReadOnlyTMatrix<T>(type, model);
    }

    public static <T> TMatrix<T> tmatrix(TypeReference<T> type, int rows, int columns, TMatrixCell<T> model) {
        return tmatrix(type, new TMatrixModel<T>() {
            @Override
            public int getColumnCount() {
                return rows;
            }

            @Override
            public int getRowCount() {
                return columns;
            }

            @Override
            public T get(int row, int column) {
                return model.get(row, column);
            }
        });
    }

//    public static Expr expr(Domain d) {
//        return DoubleValue.valueOf(1, d);
//    }

    public static <T> T simplify(T a) {
        if (a instanceof Expr) {
            return (T) simplify((Expr) a);
        }
        if (a instanceof TList) {
            return (T) simplify((TList) a);
        }
        if (a instanceof TVector) {
            return (T) simplify((TVector) a);
        }
        return a;
    }

    public static Expr simplify(Expr a) {
        return ExpressionRewriterFactory.getComputationSimplifier().rewriteOrSame(a);
    }

    public static double norm(Expr a) {
        //TODO conjugate a
        Expr aCong = a;
        Complex c = Config.getScalarProductOperator().eval(a, aCong);
        return sqrt(c).absdbl();
    }

    public static <T> TList<T> normalize(TList<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                Expr n = normalize((Expr) e).simplify();
                return (T) n;
            }
        });
    }

    public static Expr normalize(Geometry a) {
        return normalize(expr(a));
    }

    public static Expr normalize(Expr a) {
        Complex n = Complex.valueOf(1.0 / norm(a));
        if (n.equals(Maths.CONE)) {
            return a;
        }
        Expr mul = mul(a, n);
        //preserve names and properties
        mul = Any.copyProperties(a, mul);
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

    public static <T> TVector<T> cos(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.cos(e);
            }
        });
    }

    public static <T> TVector<T> cosh(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.cosh(e);
            }
        });
    }

    public static <T> TVector<T> sin(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.sin(e);
            }
        });
    }

    public static <T> TVector<T> sinh(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.sinh(e);
            }
        });
    }

    public static <T> TVector<T> tan(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.tan(e);
            }
        });
    }

    public static <T> TVector<T> tanh(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.tanh(e);
            }
        });
    }

    public static <T> TVector<T> cotan(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.cotan(e);
            }
        });
    }

    public static <T> TVector<T> cotanh(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.cotanh(e);
            }
        });
    }


    public static <T> TVector<T> sqr(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.sqr(e);
            }
        });
    }

    public static <T> TVector<T> sqrt(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.sqrt(e);
            }
        });
    }


    public static <T> TVector<T> inv(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.inv(e);
            }
        });
    }

    public static <T> TVector<T> neg(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.neg(e);
            }
        });
    }

    public static <T> TVector<T> exp(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.exp(e);
            }
        });
    }

    public static <T> TVector<T> simplify(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) simplify(e);
            }
        });
    }

    public static <T> TList<T> simplify(TList<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) simplify((Expr) e);
            }
        });
    }


    public static <T> TList<T> addAll(TList<T> e, T... expressions) {
        TypeReference<T> st = e.getComponentType();
        TList<T> n = list(st);
        VectorSpace<T> s = getVectorSpace(st);
        for (T x : e) {
            T t = sum(st, expressions);
            n.append((T) s.add(x, t));
        }
        return n;
    }

    public static <T> TList<T> mulAll(TList<T> e, T... expressions) {
        TypeReference<T> st = e.getComponentType();
        TList<T> n = list(st);
        VectorSpace<T> s = getVectorSpace(st);
        for (T x : e) {
            T t = mul(st, expressions);
            n.append((T) s.mul(x, t));
        }
        return n;
    }

    public static <T> TList<T> pow(TList<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.pow(e, b);
            }
        });
    }

    public static <T> TList<T> sub(TList<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.sub(e, b);
            }
        });
    }

    public static <T> TList<T> div(TList<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.div(e, b);
            }
        });
    }

    public static <T> TList<T> add(TList<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.add(e, b);
            }
        });
    }

    public static <T> TList<T> mul(TList<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.mul(e, b);
            }
        });
    }

//</editor-fold>

    /////////////////////////////////////////////////////////////////
    // general purpose functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="general purpose functions">

    public static void loopOver(Object[][] values, LoopAction action) {
        int[] indexes = new int[values.length];
        for (int i = 0; i < indexes.length; i++) {
            if (values[i].length == 0) {
                return;
            }
            indexes[i] = 0;
        }
        while (true) {
            Object[] uplet = new Object[indexes.length];
            for (int i = 0; i < uplet.length; i++) {
//                try {
                uplet[i] = values[i][indexes[i]];
//                } catch (ArrayIndexOutOfBoundsException ex) {
//                    System.out.println("");
//                }
            }
            action.next(uplet);

            int depth = 0;
            while (depth < indexes.length) {
                indexes[depth]++;
                if (indexes[depth] >= values[depth].length) {
                    indexes[depth] = 0;
                    depth++;
                } else {
                    break;
                }
            }
            if (depth >= indexes.length) {
                break;
            }
        }
    }

    public static void loopOver(Loop[] values, LoopAction action) {
//        int[] indexes=new int[values.length];
        for (int i = 0; i < values.length; i++) {
            if (!values[i].hasNext()) {
                return;
            }
            values[i].reset();
        }
        while (true) {
            Object[] uplet = new Object[values.length];
            for (int i = 0; i < uplet.length; i++) {
                uplet[i] = values[i].get();
            }

            action.next(uplet);

            int depth = 0;
            while (depth < values.length) {
                values[depth].next();
                if (!values[depth].hasNext()) {
                    values[depth].reset();
                    depth++;
                } else {
                    break;
                }
            }
            if (depth >= values.length) {
                break;
            }
        }
    }

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

    public static String formatFrequency(double frequency) {
        return Config.getFrequencyFormatter().format(frequency);
    }

    public static String formatDimension(double dimension) {
        return Config.getMetricFormatter().format(dimension);
    }

    public static String formatPeriodNanos(long period) {
        return Config.TIME_PERIOD_FORMATTER.formatNanos(period);
    }

    public static String formatPeriodMillis(long period) {
        return Config.TIME_PERIOD_FORMATTER.formatMillis(period);
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


//    public static Complex csum(int size, Int2Complex f) {
//        MutableComplex c = new MutableComplex();
//        int i = 0;
//        while (i < size) {
//            c.add(f.eval(i));
//            i++;
//        }
//        return c.toComplex();
//    }


    public static <T> T invokeMonitoredAction(ProgressMonitor mon, String messagePrefix, MonitoredAction<T> run) {
        return ProgressMonitorFactory.invokeMonitoredAction(mon, messagePrefix, run);
    }

    public static Chronometer chrono() {
        return new Chronometer();
    }

    public static Chronometer chrono(String name) {
        return new Chronometer(name);
    }

    public static Chronometer chrono(String name, Runnable r) {
        PlatformUtils.gc2();
        MemoryInfo memoryInfoBefore = Maths.memoryInfo();
        Chronometer c = new Chronometer().start();
        r.run();
        c.stop();
        MemoryInfo memoryInfoAfter = Maths.memoryInfo();

        PlatformUtils.gc2();
        $log.log(Level.INFO, name + " : time= " + c.toString() + "  mem-usage= " + Maths.formatMemory(memoryInfoAfter.diff(memoryInfoBefore).inUseMemory()));
        return c;
    }

    public static <V> V chrono(String name, Callable<V> r) {
//        System.out.println("Start "+name);
        PlatformUtils.gc2();
        MemoryInfo memoryInfoBefore = Maths.memoryInfo();
        Chronometer c = new Chronometer().start();
        V v = null;
        try {
            v = r.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        c.stop();
        MemoryInfo memoryInfoAfter = Maths.memoryInfo();
        PlatformUtils.gc2();
        $log.log(Level.INFO, name + " : time= " + c.toString() + "  mem-usage= " + Maths.formatMemory(memoryInfoAfter.diff(memoryInfoBefore).inUseMemory()));
        return v;
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
                return new BytesSizeFormatter(subFormat);
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

    public static <T> T[] toArray(TypeReference<T> t, Collection<T> coll) {
        return (T[]) coll.toArray((T[]) Array.newInstance(t.getTypeClass(), coll.size()));
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
        return Math.abs(b - a) / Math.abs(a);
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

    public static <T extends Expr> DoubleList toDoubleArray(TList<T> c) {
        DoubleList a = new DoubleArrayList(c.size());
        for (T o : c) {
            a.append(o.toDouble());
        }
        return a;
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
            case ARG:
                return c.arg().getReal();
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

    public static Matrix matrix(TMatrix t) {
        return (Matrix) t.to($COMPLEX);
    }

    public static TMatrix<Expr> ematrix(TMatrix t) {
        return new EMatrixFromTMatrix(t);
    }
    //</editor-fold>

    public static <T> VectorSpace<T> getVectorSpace(TypeReference<T> cls) {
        if ($COMPLEX.isAssignableFrom(cls)) {
            return (VectorSpace<T>) Maths.COMPLEX_VECTOR_SPACE;
        }
        if ($DOUBLE.isAssignableFrom(cls)) {
            return (VectorSpace<T>) Maths.DOUBLE_VECTOR_SPACE;
        }
        if ($EXPR.isAssignableFrom(cls)) {
            return (VectorSpace<T>) Maths.EXPR_VECTOR_SPACE;
        }
        throw new IllegalArgumentException("Not yet supported " + cls);
    }

    public static DoubleList refineSamples(TList<Double> values, int n) {
        DoubleList values2 = (DoubleList) values.to($DOUBLE);
        return (DoubleList) dlist(refineSamples(values2.toDoubleArray(), n));
    }

    /**
     * adds n points between each 2 points
     *
     * @param values initial sample
     * @return
     */
    public static double[] refineSamples(double[] values, int n) {
        if (n == 0) {
            return Arrays.copyOf(values, values.length);
        }
        double[] d2 = new double[values.length + n * (values.length - 1)];
        for (int i = 0; i < values.length - 1; i++) {
            int s = i * (1 + n);
            double[] d3 = dtimes(values[i], values[i + 1], n + 2);
            System.arraycopy(d3, 0, d2, s, d3.length - 1);
        }
        d2[d2.length - 1] = values[values.length - 1];
        return d2;
    }

    public static class Config {

        private static final DumpManager dumpManager = new DumpManager();
        private static final String defaultRootCachePath = "${user.home}/.cache/mathcache";
        static MatrixFactory DEFAULT_LARGE_MATRIX_FACTORY = null;
        private static String largeMatrixCachePath = "${cache.folder}/large-matrix";
        private static int simplifierCacheSize = 2000;
        //        private static float largeMatrixThreshold = 0.7f;
        private static boolean debugExpressionRewrite = false;
        private static boolean strictComputationMonitor = false;
        private static float maxMemoryThreshold = 0.7f;
        private static FrequencyFormatter frequencyFormatter = new FrequencyFormatter();
        private static BytesSizeFormatter memorySizeFormatter = new BytesSizeFormatter();
        private static MetricFormatter metricFormatter = new MetricFormatter();
        private static TimePeriodFormatter TIME_PERIOD_FORMATTER = new DefaultTimePeriodFormatter();
        private static ExprSequenceFactory DEFAULT_EXPR_SEQ_FACTORY = DefaultExprSequenceFactory.INSTANCE;
        private static ExprMatrixFactory DEFAULT_EXPR_MATRIX_FACTORY = DefaultExprMatrixFactory.INSTANCE;
        private static ExprCubeFactory DEFAULT_EXPR_CUBE_FACTORY = DefaultExprCubeFactory.INSTANCE;
        private static int matrixBlockPrecision = 256;
        private static InverseStrategy defaultMatrixInverseStrategy = InverseStrategy.BLOCK_SOLVE;
        private static SolveStrategy defaultMatrixSolveStrategy = SolveStrategy.DEFAULT;
        private static MatrixFactory defaultMatrixFactory = SmartMatrixFactory.INSTANCE;
        private static CacheMode persistenceCacheMode = CacheMode.ENABLED;
        private static boolean cacheEnabled = true;
        private static boolean expressionWriterCacheEnabled = true;
        private static boolean cacheExpressionPropertiesEnabled = true;
        private static boolean cacheExpressionPropertiesEnabledEff = true;
        private static boolean developmentMode = false;
        private static boolean compressCache = true;
        private static String rootCachePath = defaultRootCachePath;
        private static String appCacheName = "default";
        //        private static String largeMatrixCachePath = "${cache.folder}/large-matrix";
        //    public static final ScalarProduct NUMERIC_SIMP_SCALAR_PRODUCT = new NumericSimplifierScalarProduct();
        private static ScalarProductOperator defaultScalarProductOperator = null;
        private static IntegrationOperator defaultIntegrationOperator = null;
        private static FunctionDifferentiatorManager functionDifferentiatorManager = new FormalDifferentiation();
        private static Map<ClassPair, Converter> converters = new HashMap<>();
        private static Map<String, TMatrixFactory> matrixFactories = new HashMap<>();

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

        static {
            registerConverter(Double.class, Complex.class, DOUBLE_TO_COMPLEX);
            registerConverter(Complex.class, Double.class, COMPLEX_TO_DOUBLE);
            registerConverter(Double.class, TVector.class, DOUBLE_TO_TVECTOR);
            registerConverter(TVector.class, Double.class, TVECTOR_TO_DOUBLE);
            registerConverter(Double.class, Expr.class, DOUBLE_TO_EXPR);
            registerConverter(Expr.class, Double.class, EXPR_TO_DOUBLE);

            registerConverter(Complex.class, TVector.class, COMPLEX_TO_TVECTOR);
            registerConverter(TVector.class, Complex.class, TVECTOR_TO_COMPLEX);
            registerConverter(Complex.class, Expr.class, COMPLEX_TO_EXPR);
            registerConverter(Expr.class, Complex.class, EXPR_TO_COMPLEX);
        }

        public static boolean isCompressCache() {
            return compressCache;
        }

        public static void setCompressCache(boolean compressCache) {
            Config.compressCache = compressCache;
        }

        public static boolean memoryCanStores(long bytesToStore) {
            float maxMemoryThreshold = getMaxMemoryThreshold();
            if (maxMemoryThreshold <= 0) {
                return true;
            }
            return (bytesToStore <= (maxFreeMemory() * ((double) maxMemoryThreshold)));
        }

        public static float getMaxMemoryThreshold() {
            return maxMemoryThreshold;
        }

        public static void setMaxMemoryThreshold(float maxMemoryThreshold) {
            Config.maxMemoryThreshold = maxMemoryThreshold;
        }

        public static TMatrixFactory getTMatrixFactory(String id) {
            TMatrixFactory fac = matrixFactories.get(id);
            if (fac == null) {
                if (SmartMatrixFactory.INSTANCE.getId().equals(id)) {
                    registerTMatrixFactory(SmartMatrixFactory.INSTANCE);
                    return SmartMatrixFactory.INSTANCE;
                }
                if (MemMatrixFactory.INSTANCE.getId().equals(id)) {
                    registerTMatrixFactory(MemMatrixFactory.INSTANCE);
                    return MemMatrixFactory.INSTANCE;
                }
                if (OjalgoMatrixFactory.INSTANCE.getId().equals(id)) {
                    registerTMatrixFactory(OjalgoMatrixFactory.INSTANCE);
                    return OjalgoMatrixFactory.INSTANCE;
                }
                if (JBlasMatrixFactory.INSTANCE.getId().equals(id)) {
                    registerTMatrixFactory(JBlasMatrixFactory.INSTANCE);
                    return JBlasMatrixFactory.INSTANCE;
                }
                String id1 = DBLargeMatrixFactory.createId(id);
                if (id1 != null) {
                    DBLargeMatrixFactory dbLargeMatrixFactory = new DBLargeMatrixFactory(id);
                    registerTMatrixFactory(dbLargeMatrixFactory);
                    return dbLargeMatrixFactory;
                }
                throw new IllegalArgumentException("Factory not Found : " + id);
            } else {
                return fac;
            }
        }

        public static void registerTMatrixFactory(TMatrixFactory factory) {
            TMatrixFactory fac = matrixFactories.get(factory.getId());
            if (fac == null) {
                matrixFactories.put(factory.getId(), factory);
            } else {
                throw new IllegalArgumentException("Already registered");
            }
        }

        public static <A, B> void registerConverter(Class<A> a, Class<B> b, Converter<A, B> c) {
            ClassPair k = new ClassPair(a, b);
            if (c == null) {
                converters.remove(k);
            } else {
                converters.put(k, c);
            }
        }

        public static <A, B> Converter<A, B> getRegisteredConverter(Class<A> a, Class<B> b) {
            ClassPair k = new ClassPair(a, b);
            return converters.get(k);
        }

        public static <A, B> Converter<A, B> getConverter(Class<A> a, Class<B> b) {
            if (a.equals(b)) {
                return IDENTITY;
            }
            Converter converter = getRegisteredConverter(a, b);
            if (converter == null) {
                throw new NoSuchElementException("No such element : converter for " + a + " and " + b);
            }
            return converter;
        }

        public static <A, B> Converter<A, B> getConverter(TypeReference<A> a, TypeReference<B> b) {
            return getConverter(a.getTypeClass(), b.getTypeClass());
        }

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

        public static BytesSizeFormatter getMemorySizeFormatter() {
            return memorySizeFormatter;
        }

        public static void setMemorySizeFormatter(BytesSizeFormatter memorySizeFormatter) {
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

        public static <T> TMatrixFactory<T> getDefaultMatrixFactory(TypeReference<T> baseType) {
            throw new IllegalArgumentException("Not Yet Supported");
        }

        public static String getRootCachePath(boolean expand) {
            return expand ? Config.expandPath(rootCachePath) : rootCachePath;
        }

        public static void setRootCachePath(String rootCachePath) {
            Config.rootCachePath = rootCachePath;
        }

        public static String getDefaultCacheFolderName(boolean expand) {
            return expand ? Config.expandPath(appCacheName) : appCacheName;
        }

        public static void setAppCacheName(String appCacheName) {
            Config.appCacheName = appCacheName;
        }


        public static boolean deleteAllCache() {
            return getCacheFileSystem().get("/").deleteFolderTree(null, FailStrategy.FAIL_SAFE);
        }

        public static HFileSystem getCacheFileSystem() {
            return new FolderHFileSystem(new File(getCacheFolder()));
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

        public static ExpressionRewriter getScalarProductSimplifier() {
            return getScalarProductOperator().getExpressionRewriter();
        }

        public static ExpressionRewriter getIntegrationSimplifier() {
            return getIntegrationOperator().getExpressionRewriter();
        }

        public static ExpressionRewriter getComputationSimplifier() {
            return ExpressionRewriterFactory.getComputationSimplifier();
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

        public static ScalarProductOperator getScalarProductOperator() {
            if (defaultScalarProductOperator == null) {
                defaultScalarProductOperator = ScalarProductOperatorFactory.formal();
            }
            return defaultScalarProductOperator;
        }

        public static void setScalarProductOperator(ScalarProductOperator sp) {
            defaultScalarProductOperator = sp == null ? ScalarProductOperatorFactory.defaultValue() : sp;
        }

        public static IntegrationOperator getIntegrationOperator() {
            if (defaultIntegrationOperator == null) {
                defaultIntegrationOperator = IntegrationOperatorFactory.defaultValue();
            }
            return defaultIntegrationOperator;
        }

        public static void setIntegrationOperator(IntegrationOperator op) {
            defaultIntegrationOperator = op == null ? IntegrationOperatorFactory.defaultValue() : op;
        }


        public static String getLargeMatrixCachePath(boolean expand) {
            if (expand) {
                return Config.expandPath(largeMatrixCachePath);
            }
            return largeMatrixCachePath;
        }

        public static void seLargeMatrixCachePath(String largeMatrixPath) {
            largeMatrixCachePath = largeMatrixPath;
        }

        public static MatrixFactory getMatrixFactory() {
            return defaultMatrixFactory;
        }

        public static MatrixFactory getLargeMatrixFactory() {
            if (DEFAULT_LARGE_MATRIX_FACTORY == null) {
                synchronized (Maths.class) {
                    if (DEFAULT_LARGE_MATRIX_FACTORY == null) {
                        LargeMatrixFactory s = (LargeMatrixFactory) getTMatrixFactory(
                                DBLargeMatrixFactory.createLocalId(Config.getLargeMatrixCachePath(false), true, null)
                        );
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
            Logger logger = LogProgressMonitor.getDefaultLogger();
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

        public static String expandPath(String format) {
            if (format == null) {
                return format;
            }
            String s = replaceVars(format);
            if (format.equals("~")) {
                return System.getProperty("user.home");
            }
            if (format.startsWith("~") && format.length() > 1 && (format.charAt(1) == '/' || format.charAt(1) == '\\')) {
                return System.getProperty("user.home") + format.substring(1);
            }
            if (format.equals("~~")) {
                return replaceVars(defaultRootCachePath);
            }
            if (format.startsWith("~~") && format.length() > 2 && (format.charAt(2) == '/' || format.charAt(2) == '\\')) {
                return replaceVars(defaultRootCachePath) + format.substring(2);
            }
            s = replaceVars(s);
            return s;
        }

        public static String replaceVars(String format) {
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

    private static class IdentityConverter implements Converter, Serializable {
        @Override
        public Object convert(Object value) {
            return value;
        }
    }

    private static class ComplexDoubleConverter implements Converter<Complex, Double>, Serializable {
        @Override
        public Double convert(Complex value) {
            return value.toDouble();
        }
    }

    private static class DoubleComplexConverter implements Converter<Double, Complex>, Serializable {
        @Override
        public Complex convert(Double value) {
            return Complex.valueOf(value);
        }
    }

    private static class DoubleTVectorConverter implements Converter<Double, TVector>, Serializable {
        @Override
        public TVector convert(Double value) {
            return Maths.columnVector(new Complex[]{Complex.valueOf(value)});
        }
    }

    private static class TVectorDoubleConverter implements Converter<TVector, Double>, Serializable {
        @Override
        public Double convert(TVector value) {
            return value.toComplex().toDouble();
        }
    }

    private static class ComplexTVectorConverter implements Converter<Complex, TVector>, Serializable {
        @Override
        public TVector convert(Complex value) {
            return Maths.columnVector(new Complex[]{value});
        }
    }

    private static class TVectorComplexConverter implements Converter<TVector, Complex>, Serializable {
        @Override
        public Complex convert(TVector value) {
            return value.toComplex();
        }
    }

    private static class ComplexExprConverter implements Converter<Complex, Expr>, Serializable {
        @Override
        public Expr convert(Complex value) {
            return value;
        }
    }

    private static class ExprComplexConverter implements Converter<Expr, Complex>, Serializable {
        @Override
        public Complex convert(Expr value) {
            return value.toComplex();
        }
    }

    private static class DoubleExprConverter implements Converter<Double, Expr>, Serializable {
        @Override
        public Expr convert(Double value) {
            return Complex.valueOf(value);
        }
    }

    private static class ExprDoubleConverter implements Converter<Expr, Double>, Serializable {
        @Override
        public Double convert(Expr value) {
            return value.toComplex().toDouble();
        }
    }

    private static class StringTypeReference extends TypeReference<String> {
    }

    private static class MatrixTypeReference extends TypeReference<Matrix> {
    }

    private static class VectorTypeReference extends TypeReference<Vector> {
    }

    private static class TMatrixTypeReference extends TypeReference<TMatrix<Complex>> {
    }

    private static class TVectorTypeReference extends TypeReference<TVector<Complex>> {
    }

    private static class ComplexTypeReference extends TypeReference<Complex> {
    }

    private static class DoubleTypeReference extends TypeReference<Double> {
    }

    private static class BooleanTypeReference extends TypeReference<Boolean> {
    }

    private static class PointTypeReference extends TypeReference<Point> {

    }

    private static class FileTypeReference extends TypeReference<File> {
    }

    private static class IntegerTypeReference extends TypeReference<Integer> {
    }

    private static class LongTypeReference extends TypeReference<Long> {
    }

    private static class ExprTypeReference extends TypeReference<Expr> {
    }

    private static class TListTypeReference extends TypeReference<TList<Complex>> {
    }

    private static class TListExprTypeReference extends TypeReference<TList<Expr>> {
    }

    private static class TListDoubleTypeReference extends TypeReference<TList<Double>> {
    }

    private static class TListIntegerTypeReference extends TypeReference<TList<Integer>> {
    }

    private static class TListBooleanTypeReference extends TypeReference<TList<Boolean>> {
    }

    private static class TListMatrixTypeReference extends TypeReference<TList<Matrix>> {
    }

    public static String getHadrumathsVersion() {
        return IOUtils.getArtifactVersionOrDev("net.vpc.scholar", "hadrumaths");
    }

    public static ComponentDimension expandComponentDimension(ComponentDimension d1, ComponentDimension d2) {
        return ComponentDimension.create(
                Math.max(d1.rows, d2.rows),
                Math.max(d1.columns, d2.columns)
        );
    }

    public static Expr expandComponentDimension(Expr e, ComponentDimension d) {
        ComponentDimension d0 = e.getComponentDimension();
        if (d0.equals(d)) {
            return e;
        }
        if (d0.rows > d.rows || d0.columns > d.columns) {
            throw new IllegalArgumentException("Unable to shrink component dim " + d0 + " -> " + d);
        }
        if (d.equals(ComponentDimension.VECTOR2)) {
            return vector(e, Domain.FULLX);
        }
        if (d.equals(ComponentDimension.VECTOR3)) {
            DoubleToMatrix dm = e.toDM();
            return vector(dm.getComponent(0, 0), d.columns >= 2 ? e.toDM().getComponent(1, 0) : Domain.FULLX, Domain.FULLX);
        }
        throw new IllegalArgumentException("Unable to expandComponentDimension component dim " + d0 + " -> " + d);
    }

    public static double atan2(double y, double x) {
        return Math.atan2(y, x);
    }

    public static double ceil(double a) {
        return Math.ceil(a);
    }

    public static double floor(double a) {
        return Math.floor(a);
    }

    public static long round(double a) {
        return Math.round(a);
    }

    public static int round(float a) {
        return Math.round(a);
    }

    public static double random() {
        return Math.random();
    }

    public static <A, B> RightArrowUplet2<A, B> rightArrow(A a, B b) {
        return new RightArrowUplet2<A, B>(a, b);
    }

    public static RightArrowUplet2.Double rightArrow(double a, double b) {
        return new RightArrowUplet2.Double(a, b);
    }

    public static RightArrowUplet2.Complex rightArrow(Complex a, Complex b) {
        return new RightArrowUplet2.Complex(a, b);
    }

    public static RightArrowUplet2.Expr rightArrow(Expr a, Expr b) {
        return new RightArrowUplet2.Expr(a, b);
    }

    public static Expr parseExpression(String expression) {
        ExpressionEvaluator m = createExpressionParser();
        Object evaluated = m.evaluate(expression);
        if (evaluated instanceof Expr) {
            return (Expr) evaluated;
        }
        if (evaluated instanceof Number) {
            return expr(((Number) evaluated).doubleValue());
        }
        return (Expr) evaluated;
    }

    public static ExpressionEvaluator createExpressionEvaluator() {
        return ExpressionEvaluatorFactory.createEvaluator();
    }

    public static ExpressionEvaluator createExpressionParser() {
        return ExpressionEvaluatorFactory.createParser();
    }

    public static Expr evalExpression(String expression) {
        ExpressionEvaluator m = createExpressionEvaluator();
        Object evaluated = m.evaluate(expression);
        if (evaluated instanceof Expr) {
            return (Expr) evaluated;
        }
        if (evaluated instanceof Number) {
            return expr(((Number) evaluated).doubleValue());
        }
        return (Expr) evaluated;
    }
}
