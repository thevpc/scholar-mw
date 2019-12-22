package net.vpc.scholar.hadrumaths;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.common.util.*;
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
import net.vpc.scholar.hadrumaths.symbolic.conv.DD2DC;
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
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MathsBase {

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
    public static final TStoreManager<ComplexMatrix> MATRIX_STORE_MANAGER = new TStoreManager<ComplexMatrix>() {
        @Override
        public void store(ComplexMatrix item, File file) {
            item.store(file);
        }

        @Override
        public ComplexMatrix load(File file) {
            return Config.getComplexMatrixFactory().load(file);
        }
    };
    public static final TStoreManager<TMatrix> TMATRIX_STORE_MANAGER = new TStoreManager<TMatrix>() {
        @Override
        public void store(TMatrix item, File file) {
            item.store(file);
        }

        @Override
        public TMatrix load(File file) {
            return Config.getComplexMatrixFactory().load(file);
        }
    };

    public static final TStoreManager<TVector> TVECTOR_STORE_MANAGER = new TStoreManager<TVector>() {
        @Override
        public void store(TVector item, File file) {
            item.store(file);
        }

        @Override
        public TVector load(File file) {
            return Config.getComplexMatrixFactory().load(file).toVector();
        }
    };

    public static final TStoreManager<ComplexVector> VECTOR_STORE_MANAGER = new TStoreManager<ComplexVector>() {
        @Override
        public void store(ComplexVector item, File file) {
            item.store(file);
        }

        @Override
        public ComplexVector load(File file) {
            return Config.getComplexMatrixFactory().load(file).toVector();
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
    public static final TypeName<String> $STRING = new TypeName(String.class.getName());
    public static final TypeName<Complex> $COMPLEX = new TypeName(Complex.class.getName());
    public static final TypeName<ComplexMatrix> $MATRIX = new TypeName(ComplexMatrix.class.getName());
    public static final TypeName<ComplexVector> $VECTOR = new TypeName(ComplexVector.class.getName());
    public static final TypeName<TMatrix<Complex>> $CMATRIX = new TypeName(TMatrix.class.getName(), $COMPLEX);
    public static final TypeName<TVector<Complex>> $CVECTOR = new TypeName(TVector.class.getName(), $COMPLEX);
    public static final TypeName<Double> $DOUBLE = new TypeName(Double.class.getName());
    public static final TypeName<Boolean> $BOOLEAN = new TypeName(Boolean.class.getName());
    public static final TypeName<Point> $POINT = new TypeName(Point.class.getName());
    public static final TypeName<File> $FILE = new TypeName(File.class.getName());
    //</editor-fold>
    public static final TypeName<Integer> $INTEGER = new TypeName(Integer.class.getName());
    public static final TypeName<Long> $LONG = new TypeName(Long.class.getName());
    public static final TypeName<Expr> $EXPR = new TypeName(Expr.class.getName());
    public static final TypeName<TVector<Complex>> $CLIST = new TypeName(TVector.class.getName(), $COMPLEX);
    public static final TypeName<TVector<Expr>> $ELIST = new TypeName(TVector.class.getName(), $EXPR);
    public static final TypeName<TVector<Double>> $DLIST = new TypeName(TVector.class.getName(), $DOUBLE);
    public static final TypeName<TVector<TVector<Double>>> $DLIST2 = new TypeName(TVector.class.getName(), $DLIST);
    public static final TypeName<TVector<Integer>> $ILIST = new TypeName(TVector.class.getName(), $INTEGER);
    public static final TypeName<TVector<Boolean>> $BLIST = new TypeName(TVector.class.getName(), $BOOLEAN);
    public static final TypeName<TVector<ComplexMatrix>> $MLIST = new TypeName(TVector.class.getName(), $MATRIX);
    public static final SimpleDateFormat UNIVERSAL_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int ARCH_MODEL_BITS = Integer.valueOf(
            System.getProperty("sun.arch.data.model") != null ? System.getProperty("sun.arch.data.model")
                    : System.getProperty("os.arch").contains("64") ? "64" : "32"
    );
    private static final int BYTE_BITS = 8;
    private static final int WORD = ARCH_MODEL_BITS / BYTE_BITS;
    private static final int JOBJECT_MIN_SIZE = 16;
    private static final Logger $log = Logger.getLogger(MathsBase.class.getName());
    public static final MathsConfig Config = MathsConfig.INSTANCE;
    public static DistanceStrategy<Double> DISTANCE_DOUBLE = new DistanceStrategy<Double>() {
        @Override
        public double distance(Double a, Double b) {
            return Math.abs(b - a);
        }
    };
    public static DistanceStrategy<Complex> DISTANCE_COMPLEX = Complex.DISTANCE;
    public static DistanceStrategy<ComplexMatrix> DISTANCE_MATRIX = new DistanceStrategy<ComplexMatrix>() {
        @Override
        public double distance(ComplexMatrix a, ComplexMatrix b) {
            return a.getError(b);
        }
    };
    public static DistanceStrategy<ComplexVector> DISTANCE_VECTOR = new DistanceStrategy<ComplexVector>() {
        @Override
        public double distance(ComplexVector a, ComplexVector b) {
            return a.toMatrix().getError(b.toMatrix());
        }
    };

    static {
        ComplexAsDoubleValues.init();
        ServiceLoader<HadrumathsService> loader = ServiceLoader.load(HadrumathsService.class);
        TreeMap<Integer, List<HadrumathsService>> all = new TreeMap<>();
        for (HadrumathsService hadrumathsService : loader) {
            HadrumathsServiceDesc d = hadrumathsService.getClass().getAnnotation(HadrumathsServiceDesc.class);
            if (d == null) {
                throw new IllegalArgumentException("Missing @HadrumathsServiceDesc for " + hadrumathsService.getClass());
            }
            List<HadrumathsService> values = all.get(d.order());
            if (values == null) {
                values = new ArrayList<HadrumathsService>();
                all.put(d.order(), values);
            }
            values.add(hadrumathsService);
        }
        for (Map.Entry<Integer, List<HadrumathsService>> listEntry : all.entrySet()) {
            for (HadrumathsService hadrumathsService : listEntry.getValue()) {
                hadrumathsService.installService();
            }
        }
        PlotConfigManager.Config = new MathsPlotConfig();
        PlotConfigManager.addPlotFileTypes(
                PlotFileTypePng.INSTANCE,
                PlotFileTypeMatlab.INSTANCE,
                PlotFileTypeJFig.INSTANCE,
                PlotFileTypeJObj.INSTANCE,
                PlotFileTypeBundle.INSTANCE,
                PlotFileTypeJpeg.INSTANCE,
                PlotFileTypeCsv.INSTANCE
        );
        PlotConfigManager.getPlotValueTypeFactory().registerType(new PlotValueComplexType());
        PlotConfigManager.getPlotValueTypeFactory().registerType(new PlotValueExprType());
        PlotConfigManager.getPlotValueTypeFactory().registerType(new PlotValuePointType());
        PlotConfigManager.getPlotValueTypeFactory().registerType(MathsPlotValueDoubleType.INSTANCE);
        PlotConfigManager.getPlotValueTypeFactory().registerConverter(new PlotValueTypeFactory.AbstractPlotValueTypeConverter("number", "complex") {
        });
        PlotConfigManager.getPlotValueTypeFactory().registerConverter(new PlotValueTypeFactory.AbstractPlotValueTypeConverter("complex", "expr") {
        });
        PlotConfigManager.addPlotCacheSupport(new MathsPlotConsoleCacheSupport());
        PlotConfigManager.addPlotModelPopupFactory(new ValuesPlotModelPopupFactory());
        PlotConfigManager.addPlotModelPanelFactory(new ExpressionsPlotModelPanelFactory());
        PlotConfigManager.addPlotBuilderSupport(new MathsPlotBuilderSupport());
        PlotConfigManager.addPlotValueFactory(MathsPlotValueFactory.INSTANCE);
        List<PlotModelFactory> old = new ArrayList<>();
        old.add(MathsPlotModelFactory.INSTANCE);
        old.add(DefaultPlotModelFactory.INSTANCE);
        old.addAll(Arrays.asList(PlotConfigManager.getPlotModelFactories()));
        PlotConfigManager.setPlotModelFactories(old.toArray(new PlotModelFactory[0]));
    }

    private MathsBase() {
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
    public static ComplexMatrix zerosMatrix(ComplexMatrix other) {
        return Config.getComplexMatrixFactory().newZeros(other);
    }

    public static ComplexMatrix constantMatrix(int dim, Complex value) {
        return Config.getComplexMatrixFactory().newConstant(dim, value);
    }

    public static ComplexMatrix onesMatrix(int dim) {
        return Config.getComplexMatrixFactory().newOnes(dim);
    }

    public static ComplexMatrix onesMatrix(int rows, int cols) {
        return Config.getComplexMatrixFactory().newOnes(rows, cols);
    }

    public static ComplexMatrix constantMatrix(int rows, int cols, Complex value) {
        return Config.getComplexMatrixFactory().newConstant(rows, cols, value);
    }

    public static ComplexMatrix zerosMatrix(int dim) {
        return Config.getComplexMatrixFactory().newZeros(dim);
    }

    public static ComplexMatrix I(Complex[][] iValue) {
        return matrix(iValue).mul(I);
    }

    public static ComplexMatrix zerosMatrix(int rows, int cols) {
        return Config.getComplexMatrixFactory().newZeros(rows, cols);
    }

    public static ComplexMatrix identityMatrix(ComplexMatrix c) {
        return Config.getComplexMatrixFactory().newIdentity(c);
    }

    public static ComplexMatrix NaNMatrix(int dim) {
        return Config.getComplexMatrixFactory().newNaN(dim);
    }

    public static ComplexMatrix NaNMatrix(int rows, int cols) {
        return Config.getComplexMatrixFactory().newNaN(rows, cols);
    }

    public static ComplexMatrix identityMatrix(int dim) {
        return Config.getComplexMatrixFactory().newIdentity(dim);
    }

    public static ComplexMatrix identityMatrix(int rows, int cols) {
        return Config.getComplexMatrixFactory().newIdentity(rows, cols);
    }

    public static ComplexMatrix matrix(ComplexMatrix matrix) {
        return Config.getComplexMatrixFactory().newMatrix(matrix);
    }

    public static ComplexMatrix matrix(String string) {
        return Config.getComplexMatrixFactory().newMatrix(string);
    }

    public static ComplexMatrix matrix(Complex[][] complex) {
        return Config.getComplexMatrixFactory().newMatrix(complex);
    }

    public static ComplexMatrix matrix(double[][] complex) {
        return Config.getComplexMatrixFactory().newMatrix(complex);
    }

    public static ComplexMatrix matrix(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newMatrix(rows, cols, cellFactory);
    }

    //    public static Matrix matrix(int rows, int cols, Int2ToComplex cellFactory) {
//        return Config.getDefaultMatrixFactory().newMatrix(rows, cols, new I2ToTMatrixCell<Complex>(cellFactory));
//    }
    public static ComplexMatrix columnMatrix(Complex... values) {
        return Config.getComplexMatrixFactory().newColumnMatrix(values);
    }

    public static ComplexMatrix columnMatrix(double... values) {
        Complex[] d = new Complex[values.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = Complex.valueOf(values[i]);
        }
        return Config.getComplexMatrixFactory().newColumnMatrix(d);
    }

    public static ComplexMatrix rowMatrix(double... values) {
        Complex[] d = new Complex[values.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = Complex.valueOf(values[i]);
        }
        return Config.getComplexMatrixFactory().newRowMatrix(d);
    }

    public static ComplexMatrix rowMatrix(Complex... values) {
        return Config.getComplexMatrixFactory().newRowMatrix(values);
    }

    public static ComplexMatrix columnMatrix(int rows, TVectorCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newColumnMatrix(rows, cellFactory);
    }

    public static ComplexMatrix rowMatrix(int columns, TVectorCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newRowMatrix(columns, cellFactory);
    }

    public static ComplexMatrix symmetricMatrix(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newSymmetric(rows, cols, cellFactory);
    }

    public static ComplexMatrix hermitianMatrix(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newHermitian(rows, cols, cellFactory);
    }

    public static ComplexMatrix diagonalMatrix(int dim, TVectorCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newDiagonal(dim, cellFactory);
    }

    public static ComplexMatrix diagonalMatrix(Complex... c) {
        return Config.getComplexMatrixFactory().newDiagonal(c);
    }

    public static ComplexMatrix matrix(int dim, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newMatrix(dim, cellFactory);
    }

    public static ComplexMatrix matrix(int rows, int columns) {
        return Config.getComplexMatrixFactory().newMatrix(rows, columns);
    }

    public static ComplexMatrix symmetricMatrix(int dim, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newMatrix(dim, cellFactory);
    }

    public static ComplexMatrix hermitianMatrix(int dim, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newHermitian(dim, cellFactory);
    }



    public static ComplexMatrix randomRealMatrix(int m, int n) {
        return Config.getComplexMatrixFactory().newRandomReal(m, n);
    }

    public static ComplexMatrix randomRealMatrix(int m, int n, int min, int max) {
        return Config.getComplexMatrixFactory().newRandomReal(m, n, min, max);
    }

    public static ComplexMatrix randomRealMatrix(int m, int n, double min, double max) {
        return Config.getComplexMatrixFactory().newRandomReal(m, n, min, max);
    }

    public static ComplexMatrix randomImagMatrix(int m, int n, double min, double max) {
        return Config.getComplexMatrixFactory().newRandomImag(m, n, min, max);
    }

    public static ComplexMatrix randomImagMatrix(int m, int n, int min, int max) {
        return Config.getComplexMatrixFactory().newRandomImag(m, n, min, max);
    }

    public static ComplexMatrix randomMatrix(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        return Config.getComplexMatrixFactory().newRandom(m, n, minReal, maxReal, minImag, maxImag);
    }

    public static ComplexMatrix randomMatrix(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        return Config.getComplexMatrixFactory().newRandom(m, n, minReal, maxReal, minImag, maxImag);
    }

    public static ComplexMatrix randomMatrix(int m, int n, double min, double max) {
        return Config.getComplexMatrixFactory().newRandom(m, n, min, max);
    }

    public static ComplexMatrix randomMatrix(int m, int n, int min, int max) {
        return Config.getComplexMatrixFactory().newRandom(m, n, min, max);
    }

    public static ComplexMatrix randomImagMatrix(int m, int n) {
        return Config.getComplexMatrixFactory().newRandomImag(m, n);
    }

    public static <T> TMatrix<T> loadTMatrix(TypeName<T> componentType, File file) throws UncheckedIOException {
        if(componentType.equals($COMPLEX)){
            return (TMatrix<T>) Config.getComplexMatrixFactory().load(file);
        }
        throw new IllegalArgumentException("Unsupported Load for type "+componentType);
    }

    public static ComplexMatrix loadMatrix(File file) throws UncheckedIOException {
        return Config.getComplexMatrixFactory().load(file);
    }

    public static ComplexMatrix matrix(File file) throws UncheckedIOException {
        return Config.getComplexMatrixFactory().load(file);
    }

    public static void storeMatrix(ComplexMatrix m, String file) throws UncheckedIOException {
        m.store(file == null ? (File) null : new File(Config.expandPath(file)));
    }

    public static void storeMatrix(ComplexMatrix m, File file) throws UncheckedIOException {
        m.store(file);
    }

    public static ComplexMatrix loadOrEvalMatrix(String file, TItem<ComplexMatrix> item) throws UncheckedIOException {
        return loadOrEvalMatrix(new File(Config.expandPath(file)), item);
    }

    public static ComplexVector loadOrEvalVector(String file, TItem<TVector<Complex>> item) throws UncheckedIOException {
        return loadOrEvalVector(new File(Config.expandPath(file)), item);
    }

    public static ComplexMatrix loadOrEvalMatrix(File file, TItem<ComplexMatrix> item) throws UncheckedIOException {
        return loadOrEval($MATRIX, file, item);
    }

    public static ComplexVector loadOrEvalVector(File file, TItem<TVector<Complex>> item) throws UncheckedIOException {
        return loadOrEval($VECTOR, file, (TItem) item);
    }

    public static <T> TMatrix loadOrEvalTMatrix(String file, TItem<TMatrix<T>> item) throws UncheckedIOException {
        return loadOrEvalTMatrix(new File(Config.expandPath(file)), item);
    }

    public static <T> TVector<T> loadOrEvalTVector(String file, TItem<TVector<T>> item) throws UncheckedIOException {
        return loadOrEvalTVector(new File(Config.expandPath(file)), item);
    }

    public static <T> TMatrix<T> loadOrEvalTMatrix(File file, TItem<TMatrix<T>> item) throws UncheckedIOException {
        return loadOrEval((TypeName) $CMATRIX, file, item);
    }

    public static <T> TVector loadOrEvalTVector(File file, TItem<TVector<T>> item) throws UncheckedIOException {
        return loadOrEval($CVECTOR, file, (TItem) item);
    }

    public static <T> T loadOrEval(TypeName<T> type, File file, TItem<T> item) throws UncheckedIOException {
        TStoreManager<T> t = TStoreManagerFactory.create(type);
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

    public static ComplexMatrix loadMatrix(String file) throws UncheckedIOException {
        return Config.getComplexMatrixFactory().load(new File(Config.expandPath(file)));
    }

    public static ComplexMatrix inv(ComplexMatrix c) {
        return c.inv();
    }

    public static ComplexMatrix tr(ComplexMatrix c) {
        return c.transpose();
    }

    public static ComplexMatrix trh(ComplexMatrix c) {
        return c.transposeHermitian();
    }

    public static ComplexMatrix transpose(ComplexMatrix c) {
        return c.transpose();
    }

    public static ComplexMatrix transposeHermitian(ComplexMatrix c) {
        return c.transposeHermitian();
    }

    //</editor-fold>
    //<editor-fold desc="Vector functions">
    public static ComplexVector rowVector(Complex[] elems) {
        return ArrayComplexVector.Row(elems);
    }

    public static ComplexVector constantColumnVector(int size, Complex c) {
        Complex[] arr = new Complex[size];
        for (int i = 0; i < size; i++) {
            arr[i] = c;
        }
        return ArrayComplexVector.Column(arr);
    }

    public static ComplexVector constantRowVector(int size, Complex c) {
        Complex[] arr = new Complex[size];
        for (int i = 0; i < size; i++) {
            arr[i] = c;
        }
        return ArrayComplexVector.Row(arr);
    }

    public static ComplexVector zerosVector(int size) {
        return zerosColumnVector(size);
    }

    public static ComplexVector zerosColumnVector(int size) {
        return constantColumnVector(size, CZERO);
    }

    public static ComplexVector zerosRowVector(int size) {
        return constantRowVector(size, CZERO);
    }

    public static ComplexVector NaNColumnVector(int dim) {
        return constantColumnVector(dim, Complex.NaN);
    }

    public static ComplexVector NaNRowVector(int dim) {
        return constantRowVector(dim, Complex.NaN);
    }

    public static ExprVector columnVector(Expr... expr) {
        return new ArrayExprVector(false, expr);
    }

    public static ExprVector rowVector(Expr... expr) {
        return new ArrayExprVector(true, expr);
    }

    public static ExprVector columnEVector(int rows, TVectorCell<Expr> cellFactory) {
        return new ArrayExprVector(false, rows, cellFactory);
    }

    public static ExprVector rowEVector(int rows, TVectorCell<Expr> cellFactory) {
        return new ArrayExprVector(true, rows, cellFactory);
    }

    public static <T> TVector<T> updatableOf(TVector<T> vector) {
        return new UpdatableTVector<T>(
                vector.getComponentType(), new CachedTVectorUpdatableModel<T>(vector, vector.getComponentType()),
                false
        );
    }

    public static Complex[][] copyOf(Complex[][] val) {
        if (val == null) {
            return val;
        }
        Complex[][] val2 = new Complex[val.length][];
        for (int i = 0; i < val2.length; i++) {
            if (val[i] != null) {
                val2[i] = Arrays.copyOf(val[i], val[i].length);
            }
        }
        return val2;
    }

    public static Complex[] copyOf(Complex[] val) {
        if (val == null) {
            return val;
        }
        return Arrays.copyOf(val, val.length);
    }

    public static <T> TVector<T> copyOf(TVector<T> vector) {
        TVector<T> ts = list(vector.getComponentType(), vector.isRow(), vector.size());
        ts.appendAll(vector);
        return ts;
    }

    public static <T> TVector<T> columnTVector(TypeName<T> cls, T... elements) {
        return new ReadOnlyTList<T>(
                cls, false, new TVectorModel<T>() {
            @Override
            public int size() {
                return elements.length;
            }

            @Override
            public T get(int index) {
                return elements[index];
            }
        }
        );
    }

    public static <T> TVector<T> columnTVector(TypeName<T> cls, TVectorModel<T> cellFactory) {
        return new ReadOnlyTList<T>(
                cls, false, cellFactory
        );
//        return new UpdatableTVector<>(
//                cls,new CachedTVectorUpdatableModel<>(cellFactory,cls),
//                false
//        );
    }

    public static <T> TVector<T> rowTVector(TypeName<T> cls, TVectorModel<T> cellFactory) {
        return new ReadOnlyTList<>(
                cls, true, cellFactory
        );
//        return new UpdatableTVector<>(
//                cls,new CachedTVectorUpdatableModel<>(cellFactory,cls),
//                true
//        );
    }

    public static <T> TVector<T> columnTVector(TypeName<T> cls, int rows, TVectorCell<T> cellFactory) {
        return columnTVector(cls, new TVectorModelFromCell<>(rows, cellFactory));
    }

    public static <T> TVector<T> rowTVector(TypeName<T> cls, int rows, TVectorCell<T> cellFactory) {
        return rowTVector(cls, new TVectorModelFromCell<>(rows, cellFactory));
    }

    public static ComplexVector columnVector(int rows, TVectorCell<Complex> cellFactory) {
        Complex[] arr = new Complex[rows];
        for (int i = 0; i < rows; i++) {
            arr[i] = cellFactory.get(i);
        }
        return columnVector(arr);
    }

    public static ComplexVector rowVector(int columns, TVectorCell<Complex> cellFactory) {
        Complex[] arr = new Complex[columns];
        for (int i = 0; i < columns; i++) {
            arr[i] = cellFactory.get(i);
        }
        return rowVector(arr);
    }

    public static ComplexVector columnVector(Complex... elems) {
        return ArrayComplexVector.Column(elems);
    }

    public static ComplexVector columnVector(double[] elems) {
        return ArrayComplexVector.Column(ArrayUtils.toComplex(elems));
    }

    public static ComplexVector rowVector(double[] elems) {
        return ArrayComplexVector.Row(ArrayUtils.toComplex(elems));
    }

    public static ComplexVector column(Complex[] elems) {
        return ArrayComplexVector.Column(elems);
    }

    public static ComplexVector row(Complex[] elems) {
        return ArrayComplexVector.Row(elems);
    }

    public static ComplexVector trh(ComplexVector c) {
        return c.transpose().conj();
    }

    public static ComplexVector tr(ComplexVector c) {
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
        return MathsArrays.getColumn(a, index);
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
        return net.vpc.common.util.ArrayUtils.subArray1(dtimes(min, max, maxTimes), times, strategy);
    }

    public static double[] dtimes(double min, double max, int times) {
        return net.vpc.common.util.ArrayUtils.dtimes(min, max, times);
    }

    public static float[] ftimes(float min, float max, int times) {
        return net.vpc.common.util.ArrayUtils.ftimes(min, max, times);
    }

    public static long[] ltimes(long min, long max, int times) {
        return net.vpc.common.util.ArrayUtils.ltimes(min, max, times);
    }

    public static long[] lsteps(long min, long max, long step) {
        return net.vpc.common.util.ArrayUtils.lsteps(min, max, step);
    }

    public static int[] itimes(int min, int max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return net.vpc.common.util.ArrayUtils.itimes(min, max, times, maxTimes, strategy);
    }

    public static double[] dsteps(int max) {
        return dsteps(0, max, 1);
    }

    public static double[] dsteps(double min, double max) {
        return dsteps(min, max, 1);
    }

    public static double dstepsLength(double min, double max, double step) {
        return net.vpc.common.util.ArrayUtils.dstepsLength(min, max, step);
    }

    public static double dstepsElement(double min, double max, double step, int index) {
        return net.vpc.common.util.ArrayUtils.dstepsElement(min, max, step, index);
    }

    public static double[] dsteps(double min, double max, double step) {
        return net.vpc.common.util.ArrayUtils.dsteps(min, max, step);
    }

    //
    public static float[] fsteps(float min, float max, float step) {
        return net.vpc.common.util.ArrayUtils.fsteps(min, max, step);
    }

    public static int[] isteps(int min, int max, int step) {
        return net.vpc.common.util.ArrayUtils.isteps(min, max, step);
    }

    public static int[] isteps(int min, int max, int step, IntFilter filter) {
        return net.vpc.common.util.ArrayUtils.isteps(min, max, step);
    }

    public static int[] itimes(int min, int max, int times) {
        return net.vpc.common.util.ArrayUtils.itimes(min, max, times);
    }

    public static int[] isteps(int max) {
        return isteps(0, max, 1);
    }

    public static int[] isteps(int min, int max) {
        return isteps(min, max, 1);
    }

    public static int[] itimes(int min, int max) {
        return itimes(min, max, max - min + 1);
    }

    /**
     * sqrt(a^2 + b^2) without under/overflow.
     *
     * @param a
     * @param b
     * @return
     */
    public static double hypot(double a, double b) {
        return MathsTrigo.hypot(a, b);
    }

    public static Complex sqr(Complex d) {
        return d.sqr();
    }

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
     * <p>
     * If coefficients are for the interval a to b, x must have been transformed
     * to x -> 2(2x - b - a)/(b-a) before entering the routine. This maps x from
     * (a, b) to (-1, 1), over which the Chebyshev polynomials are defined.
     * <p>
     * If the coefficients are for the inverted interval, in which (a, b) is
     * mapped to (1/b, 1/a), the transformation required is x -> 2(2ab/x - b -
     * a)/(b-a). If b is infinity, this becomes x -> 4a/x - 1.
     * <p>
     * SPEED:
     * <p>
     * Taking advantage of the recurrence properties of the Chebyshev
     * polynomials, the routine requires one more addition per loop than
     * evaluating a nested polynomial of the same degree.
     *
     * @param x    argument to the polynomial.
     * @param coef the coefficients of the polynomial.
     * @param N    the number of coefficients.
     */
    public static double chbevl(double x, double[] coef, int N) throws ArithmeticException {
        return MathsTrigo.chbevl(x, coef, N);
    }

    public static long pgcd(long a, long b) {
        return MathsAlgebra.pgcd(a, b);
    }

    public static int pgcd(int a, int b) {
        return MathsAlgebra.pgcd(a, b);
    }

    public static double[][] toDouble(Complex[][] c, PlotDoubleConverter toDoubleConverter) {
        return MathsArrays.toDouble(c, toDoubleConverter);
    }

    public static double[] toDouble(Complex[] c, PlotDoubleConverter toDoubleConverter) {
        return MathsArrays.toDouble(c, toDoubleConverter);
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
        return MathsArrays.rangeCC(orderedValues, min, max);
    }

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
        return MathsArrays.rangeCO(orderedValues, min, max);
    }

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

    public static ComplexVector vector(TVector v) {
        v = v.to($COMPLEX);
        if (v instanceof ComplexVector) {
            return (ComplexVector) v;
        }
        TVector finalV = v;
        return new ReadOnlyComplexVector(new TVectorModel<Complex>() {
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

    public static double norm(ComplexMatrix a) {
        return a.norm(NormStrategy.DEFAULT);
    }

    public static double norm(ComplexVector a) {
        return a.norm();
    }

    public static double norm1(ComplexMatrix a) {
        return a.norm1();
    }

    public static double norm2(ComplexMatrix a) {
        return a.norm2();
    }

    public static double norm3(ComplexMatrix a) {
        return a.norm3();
    }

    public static double normInf(ComplexMatrix a) {
        return a.normInf();
    }

    public static DoubleToComplex complex(DoubleToDouble fx) {
        if (fx.isZero()) {
            return DCZERO;
        }
        if (fx instanceof DoubleToComplex) {
            return (DoubleToComplex) fx;
        }
        return DD2DC.valueOf(fx);
    }

    public static DoubleToComplex complex(DoubleToDouble fx, DoubleToDouble fy) {
        return DD2DC.valueOf(fx, fy);
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

    public static double[][] cross(double[] x, double[] y) {
        return MathsArrays.cross(x, y);
    }

    public static double[][] cross(double[] x, double[] y, double[] z) {
        return MathsArrays.cross(x, y, z);
    }

    public static double[][] cross(double[] x, double[] y, double[] z, Double3Filter filter) {
        return MathsArrays.cross(x, y, z, filter);
    }

    public static double[][] cross(double[] x, double[] y, Double2Filter filter) {
        return MathsArrays.cross(x, y, filter);
    }

    public static int[][] cross(int[] x, int[] y) {
        return MathsArrays.cross(x, y);
    }

    public static int[][] cross(int[] x, int[] y, int[] z) {
        return MathsArrays.cross(x, y, z);
    }

    public static TVector sineSeq(String borders, int m, int n, Domain domain) {
        return sineSeq(borders, m, n, domain, PlaneAxis.XY);
    }

    public static TVector sineSeq(String borders, int m, int n, Domain domain, PlaneAxis plane) {
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

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, double mmax, DoubleParam n, double nmax, Double2Filter filter) {
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

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, double max, DoubleFilter filter) {
        if (filter != null) {
            ArrayDoubleVector list = new ArrayDoubleVector();
            double[] mm = dsteps(0, max);
            for (double cros : mm) {
                if (filter.accept((int) cros)) {
                    list.add(cros);
                }
            }
            return seq(pattern, m, list.toDoubleArray());
        }
        return seq(pattern, m, 0, max);
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, double mmax, DoubleParam n, double nmax, DoubleParam p, double pmax, Double3Filter filter) {
        double[][] cross = cross(dsteps(0, mmax), dsteps(0, nmax), dsteps(0, pmax), filter == null ? null : new Double3Filter() {
            @Override
            public boolean accept(double a, double b, double c) {
                return filter.accept((int) a, (int) b, (int) c);
            }
        });
        return seq(pattern, m, n, p, cross);
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, double mmax, DoubleParam n, double nmax) {
        return seq(pattern, m, n, cross(dsteps(0, mmax), dsteps(0, nmax)));
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues) {
        return seq(pattern, m, n, cross(mvalues, nvalues));
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, DoubleParam n, double[][] values) {
        return elist(Config.getExprSequenceFactory().newUnmodifiableSequence(values.length, new SimpleSeq2(values, m, n, pattern)));
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, DoubleParam n, DoubleParam p, double[][] values) {
        return elist(Config.getExprSequenceFactory().newUnmodifiableSequence(values.length,
                new SimpleSeqMulti(pattern, new DoubleParam[]{m, n, p}, values)
        ));
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam[] m, double[][] values) {
        return elist(Config.getExprSequenceFactory().newUnmodifiableSequence(values.length, new SimpleSeqMulti(pattern, m, values)));
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, double min, double max) {
        return seq(pattern, m, dsteps(min, max, 1));
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, double[] values) {
        return elist(Config.getExprSequenceFactory().newUnmodifiableSequence(values.length, new SimpleSeq1(values, m, pattern)));
    }

    public static ExprMatrix matrix(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues) {
        return Config.getExprMatrixFactory().newMatrix(mvalues.length, nvalues.length, new SimpleSeq2b(pattern, m, mvalues, n, nvalues));
    }

    public static ExprCube cube(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues, DoubleParam p, double[] pvalues) {
        return Config.getExprCubeFactory().newUnmodifiableCube(mvalues.length, nvalues.length, pvalues.length, new SimpleSeq3b(pattern, m, mvalues, n, nvalues, p, pvalues));
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
//        if (e.isZero()) {
//            return false;
//        }
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

    public static double Double(Expr e) {
        return e.simplify().toDouble();
    }

    public static Expr real(Expr e) {
        return EXPR_VECTOR_SPACE.real(e);
    }

    public static Expr imag(Expr e) {
        return EXPR_VECTOR_SPACE.imag(e);
    }

    public static Complex Complex(double e) {
        return Complex.valueOf(e);
    }

    public static Complex Complex(Expr e) {
        return e.simplify().toComplex();
    }

    public static Complex complex(Expr e) {
        return e.simplify().toComplex();
    }

    public static double doubleValue(Expr e) {
        return e.simplify().toDouble();
    }

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

    public static Discrete discrete(Complex[][] model, double[] x, double[] y) {
        return Discrete.create(model, x, y);
    }

    public static Expr discrete(Expr expr, double[] xsamples, double[] ysamples, double[] zsamples) {
        return discrete(expr, Samples.absolute(xsamples, ysamples, zsamples));
    }

    public static Expr discrete(Expr expr, Samples samples) {
        return MathsSampler.discrete(expr, samples);
    }

    public static Expr abssqr(Expr e) {
        return getVectorSpace($EXPR).abssqr(e);
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
        return MathsSampler.adaptiveEval(expr, distance, domain, config);
    }

    public static Discrete discrete(Expr expr) {
        return MathsSampler.discrete(expr);
    }

    public static VDiscrete vdiscrete(Expr expr) {
        return MathsSampler.vdiscrete(expr);
    }

    public static Expr discrete(Expr expr, int xSamples) {
        return MathsSampler.discrete(expr, xSamples);
    }

    public static Expr discrete(Expr expr, int xSamples, int ySamples) {
        return MathsSampler.discrete(expr, xSamples, ySamples);
    }

    public static Expr discrete(Expr expr, int xSamples, int ySamples, int zSamples) {
        return MathsSampler.discrete(expr, xSamples, ySamples, zSamples);
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
        Axis a3 = found.toArray(new Axis[0])[0];
        if (a3 == Axis.Z) {
            return new AxisTransform(e, new Axis[]{a1, a2, a3}, 2);
        } else {
            return new AxisTransform(e, new Axis[]{a1, a2, a3}, 3);
        }
    }

    public static double sin2(double d) {
        return MathsTrigo.sin2(d);
    }

    public static double cos2(double d) {
        return MathsTrigo.cos2(d);
    }

    public static boolean isInt(double d) {
        return net.vpc.common.util.PlatformUtils.isInt(d);
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
        return Config.getDumpManager().getDumpDelegate(o, false).getDumpString(o);
    }

    public static String dumpSimple(Object o) {
        return Config.getDumpManager().getDumpDelegate(o, true).getDumpString(o);
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

    public static <T extends Expr> TVector<T> preload(TVector<T> list) {
        return DefaultExprSequenceFactory.INSTANCE.newPreloadedSequence(list.length(), list);
    }

    public static <T extends Expr> TVector<T> withCache(TVector<T> list) {
        //DefaultExprSequenceFactory.INSTANCE.newCachedSequence(length(), this);
        return DefaultExprSequenceFactory.INSTANCE.newCachedSequence(list.length(), list);
    }

    //    public static <T extends Expr> TList<T> simplify(TList<T> list) {
//        //        return DefaultExprSequenceFactory.INSTANCE.newUnmodifiableSequence(length(), new SimplifiedSeq(this));
//        return DefaultExprSequenceFactory.INSTANCE.newUnmodifiableSequence(list.length(), new SimplifiedSeq(list));
//    }
    public static <T> TVector<T> abs(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.abs(e);
            }
        });
    }

    public static <T> TVector<T> db(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.db(e);
            }
        });
    }

    public static <T> TVector<T> db2(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.db2(e);
            }
        });
    }

    public static <T> TVector<T> real(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.real(e);
            }
        });
    }

    public static <T> TVector<T> imag(TVector<T> a) {
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

    public static ComplexVector sin(ComplexVector v) {
        return v.sin();
    }

    public static ComplexVector cos(ComplexVector v) {
        return v.cos();
    }

    public static ComplexVector tan(ComplexVector v) {
        return v.tan();
    }

    public static ComplexVector cotan(ComplexVector v) {
        return v.cotan();
    }

    public static ComplexVector tanh(ComplexVector v) {
        return v.tanh();
    }

    public static ComplexVector cotanh(ComplexVector v) {
        return v.cotanh();
    }

    public static ComplexVector cosh(ComplexVector v) {
        return v.cosh();
    }

    public static ComplexVector sinh(ComplexVector v) {
        return v.sinh();
    }

    public static ComplexVector log(ComplexVector v) {
        return v.log();
    }

    public static ComplexVector log10(ComplexVector v) {
        return v.log10();
    }

    public static ComplexVector db(ComplexVector v) {
        return v.db();
    }

    public static ComplexVector exp(ComplexVector v) {
        return v.exp();
    }

    public static ComplexVector acosh(ComplexVector v) {
        return v.acosh();
    }

    public static ComplexVector acos(ComplexVector v) {
        return v.acos();
    }

    public static ComplexVector asinh(ComplexVector v) {
        return v.asinh();
    }

    public static ComplexVector asin(ComplexVector v) {
        return v.asin();
    }

    public static ComplexVector atan(ComplexVector v) {
        return v.atan();
    }

    public static ComplexVector acotan(ComplexVector v) {
        return v.acotan();
    }

    public static ComplexVector imag(ComplexVector v) {
        return v.imag();
    }

    public static ComplexVector real(ComplexVector v) {
        return v.real();
    }

    public static ComplexVector abs(ComplexVector v) {
        return v.abs();
    }

    public static Complex[] abs(Complex[] v) {
        Complex[] r = new Complex[v.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = (v[i].abs());
        }
        return r;
    }

    public static Complex avg(ComplexVector v) {
        return v.avg();
    }

    public static Complex sum(ComplexVector v) {
        return v.sum();
    }

    public static Complex prod(ComplexVector v) {
        return v.prod();
    }

    public static ComplexMatrix abs(ComplexMatrix v) {
        return v.abs();
    }

    public static ComplexMatrix sin(ComplexMatrix v) {
        return v.sin();
    }

    public static ComplexMatrix cos(ComplexMatrix v) {
        return v.cos();
    }

    public static ComplexMatrix tan(ComplexMatrix v) {
        return v.tan();
    }

    public static ComplexMatrix cotan(ComplexMatrix v) {
        return v.cotan();
    }

    public static ComplexMatrix tanh(ComplexMatrix v) {
        return v.tanh();
    }

    public static ComplexMatrix cotanh(ComplexMatrix v) {
        return v.cotanh();
    }

    public static ComplexMatrix cosh(ComplexMatrix v) {
        return v.cosh();
    }

    public static ComplexMatrix sinh(ComplexMatrix v) {
        return v.sinh();
    }

    public static ComplexMatrix log(ComplexMatrix v) {
        return v.log();
    }

    public static ComplexMatrix log10(ComplexMatrix v) {
        return v.log10();
    }

    public static ComplexMatrix db(ComplexMatrix v) {
        return v.db();
    }

    public static ComplexMatrix exp(ComplexMatrix v) {
        return v.exp();
    }

    //
    public static ComplexMatrix acosh(ComplexMatrix v) {
        return v.acosh();
    }

    public static ComplexMatrix acos(ComplexMatrix v) {
        return v.acos();
    }

    public static ComplexMatrix asinh(ComplexMatrix v) {
        return v.asinh();
    }

    public static ComplexMatrix asin(ComplexMatrix v) {
        return v.asin();
    }

    public static ComplexMatrix atan(ComplexMatrix v) {
        return v.atan();
    }

    public static ComplexMatrix acotan(ComplexMatrix v) {
        return v.acotan();
    }

    public static ComplexMatrix imag(ComplexMatrix v) {
        return v.imag();
    }

    public static ComplexMatrix real(ComplexMatrix v) {
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

    public static Complex avg(ComplexMatrix v) {
        return v.avg();
    }

    public static Complex sum(ComplexMatrix v) {
        return v.sum();
    }

    public static Complex prod(ComplexMatrix v) {
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
        return MathsArrays.min(arr);
    }

    public static double max(double[] arr) {
        return MathsArrays.max(arr);
    }

    public static double avg(double[] arr) {
        return MathsArrays.avg(arr);
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
        return MathsArrays.minMax(a);
    }

    public static double[] minMaxAbs(double[] a) {
        return MathsArrays.minMaxAbs(a);
    }

    public static double[] minMaxAbsNonInfinite(double[] a) {
        return MathsArrays.minMaxAbsNonInfinite(a);
    }

    public static double avgAbs(double[] arr) {
        return MathsArrays.avgAbs(arr);
    }

    public static double[] distances(double[] arr) {
        return MathsArrays.distances(arr);
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
    public static IfExpr If(Expr cond, Expr exp1, Expr exp2) {
        return (IfExpr) EXPR_VECTOR_SPACE.If(cond, exp1, exp2);
    }

    public static IfExpr If(Expr cond) {
        return (IfExpr) EXPR_VECTOR_SPACE.If(cond, null, null);
    }

    public static IfExpr If(Expr cond, Expr exp1) {
        return (IfExpr) EXPR_VECTOR_SPACE.If(cond, exp1, null);
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
        return MathsBase.esum(seq(size, f));
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

    public static TVector<Expr> seq(int size1, TVectorCell<Expr> f) {
        return new ReadOnlyTList<Expr>($EXPR, false, new TVectorModelFromCell(size1, f));
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
        return new ReadOnlyTList<Expr>($EXPR, false, tVectorModel);
    }

    private static TMatrix<Complex> resolveBestScalarProductCache(Expr[] gp, Expr[] fn, AxisXY axis, ScalarProductOperator sp, ProgressMonitor monitor) {
        int rows = gp.length;
        int columns = fn.length;

//        if (doSimplifyAll) {
//            Expr[] finalFn = fn;
//            Expr[] finalGp = gp;
//            Expr[][] fg = MathsBase.invokeMonitoredAction(emonitor, "Simplify All", new MonitoredAction<Expr[][]>() {
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

    public static double scalarProduct(DoubleToDouble f1, DoubleToDouble f2) {
        return Config.getScalarProductOperator().evalDD(null, f1, f2);
    }

    public static ComplexVector scalarProduct(Expr f1, TVector<Expr> f2) {
        TVectorCell<Complex> spfact = new TVectorCell<Complex>() {
            @Override
            public Complex get(int index) {
                return scalarProduct(f1, f2.get(index));
            }
        };
        return f2.isColumn() ? columnVector(f2.size(), spfact) : rowVector(f2.size(), spfact);
    }

    public static ComplexMatrix scalarProduct(Expr f1, TMatrix<Expr> f2) {
        return matrix(f2.getRowCount(), f2.getColumnCount(), new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return scalarProduct(f1, f2.get(row, column));
            }
        });
    }

    public static ComplexVector scalarProduct(TVector<Expr> f2, Expr f1) {
        TVectorCell<Complex> spfact = new TVectorCell<Complex>() {
            @Override
            public Complex get(int index) {
                return scalarProduct(f2.get(index), f1);
            }
        };
        return f2.isColumn() ? columnVector(f2.size(), spfact) : rowVector(f2.size(), spfact);
    }

    public static ComplexMatrix scalarProduct(TMatrix<Expr> f2, Expr f1) {
        return matrix(f2.getRowCount(), f2.getColumnCount(), new TMatrixCell<Complex>() {
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

    public static ComplexMatrix scalarProductMatrix(TVector<Expr> g, TVector<Expr> f) {
        return matrix(Config.getScalarProductOperator().eval(g, f, null));
    }

    public static TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f) {
        return Config.getScalarProductOperator().eval(g, f, null);
    }

    public static TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, monitor);
    }

    public static ComplexMatrix scalarProductMatrix(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return matrix(Config.getScalarProductOperator().eval(g, f, monitor));
    }

    public static TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f, AxisXY axis, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, axis, monitor);
    }

    public static ComplexMatrix scalarProductMatrix(Expr[] g, Expr[] f) {
        return (ComplexMatrix) Config.getScalarProductOperator().eval(g, f, null).to($COMPLEX);
    }

    public static Complex scalarProduct(ComplexMatrix g, ComplexMatrix f) {
        return g.scalarProduct(f);
    }

    public static Expr scalarProduct(ComplexMatrix g, TVector<Expr> f) {
        return f.scalarProduct(g.to($EXPR));
    }

    public static Expr scalarProductAll(ComplexMatrix g, TVector<Expr>... f) {
        return g.toVector().to($EXPR).scalarProductAll((TVector[]) f);
    }

    public static TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f) {
        return Config.getScalarProductOperator().eval(g, f, null);
    }

    public static TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, monitor);
    }

    public static ComplexMatrix scalarProductMatrix(Expr[] g, Expr[] f, ProgressMonitor monitor) {
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
    public static ExprVector elist(int size) {
        return new ArrayExprVector(false, size);
    }

    public static ExprVector elist(boolean row, int size) {
        return new ArrayExprVector(row, size);
    }

    public static ExprVector elist(Expr... vector) {
        return new ArrayExprVector(false, vector);
    }

    public static ExprVector elist(TVector<Complex> vector) {
        return new ArrayExprVector(false, vector);
    }

    public static TVector<Complex> clist(TVector<Expr> vector) {
        return new ArrayTVector<Complex>($COMPLEX, false, new TVectorModel<Complex>() {
            @Override
            public int size() {
                return vector.size();
            }

            @Override
            public Complex get(int index) {
                return vector.get(index).simplify().toComplex();
            }
        });
    }

    public static TVector<Complex> clist() {
        return new ArrayTVector<>($COMPLEX, false, 0);
    }

    public static TVector<Complex> clist(int size) {
        return new ArrayTVector<>($COMPLEX, false, size);
    }

    public static TVector<Complex> clist(Complex... vector) {
        return new ArrayTVector<>($COMPLEX, false, vector);
    }

    public static TVector<ComplexMatrix> mlist() {
        return MathsBase.list($MATRIX, false, 0);
    }

    public static TVector<ComplexMatrix> mlist(int size) {
        return MathsBase.list($MATRIX, false, size);
    }

    public static TVector<ComplexMatrix> mlist(ComplexMatrix... items) {
        TVector<ComplexMatrix> list = MathsBase.list($MATRIX, false, items.length);
        list.appendAll(Arrays.asList(items));
        return list;
    }

    public static TVector<TVector<Complex>> clist2() {
        return list($CLIST, false, 0);
    }

    public static TVector<TVector<Expr>> elist2() {
        return MathsBase.list($ELIST, false, 0);
    }

    public static TVector<TVector<Double>> dlist2() {
        return MathsBase.list($DLIST, false, 0);
    }

    public static TVector<TVector<Integer>> ilist2() {
        return MathsBase.list($ILIST, false, 0);
    }

    public static TVector<TVector<ComplexMatrix>> mlist2() {
        return MathsBase.list($MLIST, false, 0);
    }

    public static TVector<TVector<Boolean>> blist2() {
        return list($BLIST, false, 0);
    }

    public static <T> TVector<T> list(TypeName<T> typeName) {
        return list(typeName, false, 0);
    }

    public static <T> TVector<T> list(TypeName<T> typeName, int initialSize) {
        return list(typeName, false, initialSize);
    }

    public static <T> TVector<T> listro(TypeName<T> typeName, boolean row, TVectorModel<T> model) {
        if (typeName.equals(MathsBase.$DOUBLE)) {
            return (TVector<T>) new ArrayDoubleVector.ReadOnlyDoubleVector(row, (TVectorModel<Double>) model);
        }
        if (typeName.equals(MathsBase.$INTEGER)) {
            return (TVector<T>) new ArrayIntVector.ReadOnlyIntVector(row, (TVectorModel<Integer>) model);
        }
        if (typeName.equals(MathsBase.$LONG)) {
            return (TVector<T>) new ArrayLongVector.ReadOnlyLongVector(row, (TVectorModel<Long>) model);
        }
        if (typeName.equals(MathsBase.$BOOLEAN)) {
            return (TVector<T>) new ArrayBooleanVector.ReadOnlyBooleanVector(row, (TVectorModel<Boolean>) model);
        }
        return new ReadOnlyTList<T>(typeName, row, model);
    }

    public static <T> TVector<T> list(TypeName<T> typeName, boolean row, int initialSize) {
        if (typeName.equals($EXPR)) {
            return (TVector<T>) elist(row, initialSize);
        }
        if (typeName.equals($DOUBLE)) {
            return (TVector<T>) dlist(row, initialSize);
        }
        if (typeName.equals($INTEGER)) {
            return (TVector<T>) ilist(row, initialSize);
        }
        if (typeName.equals($LONG)) {
            return (TVector<T>) llist(row, initialSize);
        }
        if (typeName.equals($BOOLEAN)) {
            return (TVector<T>) blist(row, initialSize);
        }
        return new ArrayTVector<T>(typeName, row, initialSize);
    }

    public static <T> TVector<T> list(TVector<T> vector) {
        TVector<T> exprs = list(vector.getComponentType());
        for (T o : vector) {
            exprs.append(o);
        }
        return exprs;
    }

    public static ExprVector elist(ComplexMatrix vector) {
        ComplexVector complexes = vector.toVector();
        ExprVector exprs = elist(complexes.size());
        exprs.appendAll((TVector) complexes);
        return exprs;
    }

    public static <T> TVector<T> vscalarProduct(TVector<T> vector, TVector<TVector<T>> vectors) {
        return vector.vscalarProduct(vectors.toArray(new TVector[0]));
    }

    public static ExprVector elist() {
        return new ArrayExprVector(false, 0);
    }

    public static <T> TVector<T> concat(TVector<T>... a) {
        TVector<T> ts = list(a[0].getComponentType());
        for (TVector<T> t : a) {
            ts.appendAll(t);
        }
        return ts;
    }

    public static TVector<Double> dlist() {
        return new ArrayDoubleVector();
    }

    public static TVector<Double> dlist(ToDoubleArrayAware items) {
        return dlist(items.toDoubleArray());
    }

    public static TVector<Double> dlist(double[] items) {
        DoubleVector doubles = new ArrayDoubleVector(items.length);
        doubles.appendAll(items);
        return doubles;
    }

    public static TVector<Double> dlist(boolean row, int size) {
        return new ArrayDoubleVector(row, size);
    }

    public static TVector<Double> dlist(int size) {
        return new ArrayDoubleVector(size);
    }

    public static TVector<String> slist() {
        return new ArrayTVector<String>($STRING, false, 0);
    }

    public static TVector<String> slist(String[] items) {
        TVector<String> doubles = new ArrayTVector<String>($STRING, false, items.length);
        for (String item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static TVector<String> slist(boolean row, int size) {
        return new ArrayTVector<String>($STRING, row, size);
    }

    public static TVector<String> slist(int size) {
        return new ArrayTVector<String>($STRING, false, size);
    }

    public static TVector<Boolean> blist() {
        return new ArrayBooleanVector(false, 0);
    }

    public static TVector<Boolean> dlist(boolean[] items) {
        TVector<Boolean> doubles = new ArrayBooleanVector(false, items.length);
        for (boolean item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static TVector<Boolean> blist(boolean row, int size) {
        return new ArrayBooleanVector(row, size);
    }

    public static TVector<Boolean> blist(int size) {
        return new ArrayBooleanVector(false, size);
    }

    public static IntVector ilist() {
        return new ArrayIntVector(false, 0);
    }

    public static TVector<Integer> ilist(int[] items) {
        TVector<Integer> doubles = new ArrayIntVector(false, items.length);
        for (int item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static TVector<Integer> ilist(int size) {
        return new ArrayIntVector(false, size);
    }

    public static TVector<Integer> ilist(boolean row, int size) {
        return new ArrayIntVector(row, size);
    }

    public static LongVector llist() {
        return new ArrayLongVector(false, 0);
    }

    public static TVector<Long> llist(long[] items) {
        TVector<Long> doubles = new ArrayLongVector(false, items.length);
        for (long item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static TVector<Long> llist(int size) {
        return new ArrayLongVector(false, size);
    }

    public static TVector<Long> llist(boolean row, int size) {
        return new ArrayLongVector(row, size);
    }

    public static <T> T sum(TypeName<T> type, T... arr) {
        return MathsExpr.sum(type, arr);
    }

    public static <T> T sum(TypeName<T> type, TVectorModel<T> arr) {
        return MathsExpr.sum(type, arr);
    }

    public static <T> T sum(TypeName<T> type, int size, TVectorCell<T> arr) {
        return MathsExpr.sum(type, size, arr);
    }

    public static <T> T mul(TypeName<T> type, T... arr) {
        return MathsExpr.mul(type, arr);
    }

    public static <T> T mul(TypeName<T> type, TVectorModel<T> arr) {
        return MathsExpr.mul(type, arr);
    }

    public static Complex avg(Discrete d) {
        return MathsSampler.avg(d);
    }

    public static DoubleToVector vsum(VDiscrete d) {
        return MathsSampler.vsum(d);
    }

    public static DoubleToVector vavg(VDiscrete d) {
        return MathsSampler.vavg(d);
    }

    public static Complex avg(VDiscrete d) {
        return MathsSampler.avg(d);
    }

    public static Expr sum(Expr... arr) {
        return MathsExpr.sum(arr);
    }

    public static Expr esum(TVectorModel<Expr> arr) {
        return MathsExpr.esum(arr);
    }

    public static <T> TMatrix<T> mul(TMatrix<T> a, TMatrix<T> b) {
        return a.mul(b);
    }

    public static ComplexMatrix mul(ComplexMatrix a, ComplexMatrix b) {
        return a.mul(b);
    }

    public static Expr mul(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.mul(a, b);
    }

    public static TVector<Expr> edotmul(TVector<Expr>... arr) {
        return MathsExpr.edotmul(arr);
    }

    public static TVector<Expr> edotdiv(TVector<Expr>... arr) {
        return MathsExpr.edotdiv(arr);
    }

    public static Complex cmul(TVectorModel<Complex> arr) {
        return MathsExpr.cmul(arr);
    }

    public static Expr emul(TVectorModel<Expr> arr) {
        return MathsExpr.emul(arr);
    }

    public static Expr mul(Expr... e) {
        return MathsExpr.mul(e);
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
        return MathsExpr.add(a);
    }

    public static Expr div(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.div(a, b);
    }

    public static Expr rem(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.rem(a, b);
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

    public static <T> TMatrix<T> tmatrix(TypeName<T> type, TMatrixModel<T> model) {
        return new ReadOnlyTMatrix<T>(type, model);
    }

    public static <T> TMatrix<T> tmatrix(TypeName<T> type, int rows, int columns, TMatrixCell<T> model) {
        return tmatrix(type, new TMatrixCellToModel<>(rows, columns, model));
    }

    //    public static Expr expr(Domain d) {
//        return DoubleValue.valueOf(1, d);
//    }
    public static <T> T simplify(T a) {
        return simplify(a, null);
    }

    public static <T> T simplify(T a, SimplifyOptions simplifyOptions) {
        if (a instanceof Expr) {
            return (T) ((Expr) a).simplify(simplifyOptions);
        }
        if (a instanceof ExprVector) {
            return (T) ((ExprVector) a).simplify(simplifyOptions);
        }
        if (a instanceof TVector) {
            return (T) simplify((TVector) a, simplifyOptions);
        }
        return a;
    }

    public static Expr simplify(Expr a) {
        return a.simplify();
    }

    public static Expr simplify(Expr a, SimplifyOptions simplifyOptions) {
        return a.simplify(simplifyOptions);
    }

    public static double norm(Expr a) {
        //TODO conjugate a
        Expr aCong = a;
        Complex c = Config.getScalarProductOperator().eval(a, aCong);
        return sqrt(c).absdbl();
    }

    public static <T> TVector<T> normalize(TVector<T> a) {
        if(a instanceof ExprVector){
            return (TVector<T>) ((ExprVector) a).normalize();
        }
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
        if (n.equals(MathsBase.CONE)) {
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
        return a.eval(getVectorSpace(a.getComponentType()).ops().cos());
    }

    public static <T> TVector<T> cosh(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cosh());
    }

    public static <T> TVector<T> sin(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sin());
    }

    public static <T> TVector<T> sinh(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sinh());
    }

    public static <T> TVector<T> tan(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().tan());
    }

    public static <T> TVector<T> tanh(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().tanh());
    }

    public static <T> TVector<T> cotan(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cotan());
    }

    public static <T> TVector<T> cotanh(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cotanh());
    }

    public static <T> TVector<T> sqr(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sqr());
    }

    public static <T> TVector<T> sqrt(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sqrt());
    }

    public static <T> TVector<T> inv(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().inv());
    }

    public static <T> TVector<T> neg(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().neg());
    }

    public static <T> TVector<T> exp(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().exp());
    }

    public static <T> TVector<T> simplify(TVector<T> a) {
        return simplify(a, null);
    }

    public static <T> TVector<T> simplify(TVector<T> a, SimplifyOptions options) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) simplify(e, options);
            }
        });
    }

    public static <T> TVector<T> addAll(TVector<T> e, T... expressions) {
        TypeName<T> st = e.getComponentType();
        TVector<T> n = list(st);
        VectorSpace<T> s = getVectorSpace(st);
        for (T x : e) {
            T t = sum(st, expressions);
            n.append((T) s.add(x, t));
        }
        return n;
    }

    public static <T> TVector<T> mulAll(TVector<T> e, T... expressions) {
        TypeName<T> st = e.getComponentType();
        TVector<T> n = list(st);
        VectorSpace<T> s = getVectorSpace(st);
        for (T x : e) {
            T t = mul(st, expressions);
            n.append((T) s.mul(x, t));
        }
        return n;
    }

    public static <T> TVector<T> pow(TVector<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.pow(e, b);
            }
        });
    }

    public static <T> TVector<T> sub(TVector<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.sub(e, b);
            }
        });
    }

    public static <T> TVector<T> div(TVector<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.div(e, b);
            }
        });
    }

    public static <T> TVector<T> rem(TVector<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.rem(e, b);
            }
        });
    }

    public static <T> TVector<T> add(TVector<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.add(e, b);
            }
        });
    }

    public static <T> TVector<T> mul(TVector<T> a, T b) {
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
        return Config.getMetricFormatter().format(value);
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
        return Config.getTimePeriodFormat().formatNanos(period);
    }

    public static String formatPeriodMillis(long period) {
        return Config.getTimePeriodFormat().formatMillis(period);
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
            if (offset > maxOffset) {
                maxOffset = offset;
            }
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
        MemoryInfo memoryInfoBefore = MathsBase.memoryInfo();
        Chronometer c = new Chronometer().start();
        r.run();
        c.stop();
        MemoryInfo memoryInfoAfter = MathsBase.memoryInfo();

        PlatformUtils.gc2();
        $log.log(Level.INFO, name + " : time= " + c.toString() + "  mem-usage= " + MathsBase.formatMemory(memoryInfoAfter.diff(memoryInfoBefore).inUseMemory()));
        return c;
    }

    public static <V> V chrono(String name, Callable<V> r) {
//        System.out.println("Start "+name);
        PlatformUtils.gc2();
        MemoryInfo memoryInfoBefore = MathsBase.memoryInfo();
        Chronometer c = new Chronometer().start();
        V v = null;
        try {
            v = r.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        c.stop();
        MemoryInfo memoryInfoAfter = MathsBase.memoryInfo();
        PlatformUtils.gc2();
        $log.log(Level.INFO, name + " : time= " + c.toString() + "  mem-usage= " + MathsBase.formatMemory(memoryInfoAfter.diff(memoryInfoBefore).inUseMemory()));
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

    public static DoubleFormat percentFormat() {
        return Config.getPercentFormat();
    }

    public static DoubleFormat frequencyFormat() {
        return Config.getFrequencyFormatter();
    }

    public static DoubleFormat metricFormat() {
        return Config.getMetricFormatter();
    }

    public static DoubleFormat memoryFormat() {
        return Config.getMemorySizeFormatter();
    }

    public static DoubleFormat dblformat(String format) {
        return DoubleFormatterFactory.create(format);
    }

    public static double[] resizePickFirst(double[] array, int newSize) {
        return MathsArrays.resizePickFirst(array, newSize);
    }

    public static double[] resizePickAverage(double[] array, int newSize) {
        return MathsArrays.resizePickAverage(array, newSize);
    }

    public static <T> T[] toArray(Class<T> t, Collection<T> coll) {
        return (T[]) coll.toArray((T[]) Array.newInstance(t, coll.size()));
    }

    public static <T> T[] toArray(TypeName<T> t, Collection<T> coll) {
        return (T[]) coll.toArray((T[]) Array.newInstance(t.getTypeClass(), coll.size()));
    }

    public static double rerr(double a, double b) {
        return MathsArrays.rerr(a, b);
    }

    public static double rerr(Complex a, Complex b) {
        return MathsArrays.rerr(a, b);
    }

    public static CustomCCFunctionXExpr define(String name, CustomCCFunctionX f) {
        return new CustomCCFunctionXDefinition(name, f).fct();
    }

    public static CustomDCFunctionXExpr define(String name, CustomDCFunctionX f) {
        return new CustomDCFunctionXDefinition(name, f).fct();
    }

    public static CustomDDFunctionXExpr define(String name, CustomDDFunctionX f) {
        return new CustomDDFunctionXDefinition(name, f).fct();
    }

    public static CustomDDFunctionXYExpr define(String name, CustomDDFunctionXY f) {
        return new CustomDDFunctionXYDefinition(name, f).fct();
    }

    public static CustomDCFunctionXYExpr define(String name, CustomDCFunctionXY f) {
        return new CustomDCFunctionXYDefinition(name, f).fct();
    }

    public static CustomCCFunctionXYExpr define(String name, CustomCCFunctionXY f) {
        return new CustomCCFunctionXYDefinition(name, f).fct();
    }

    public static double rerr(ComplexMatrix a, ComplexMatrix b) {
        return b.getError(a);
    }

    public static <T extends Expr> DoubleVector toDoubleArray(TVector<T> c) {
        DoubleVector a = new ArrayDoubleVector(c.size());
        for (T o : c) {
            a.append(o.toDouble());
        }
        return a;
    }

    public static double toDouble(Complex c, PlotDoubleConverter d) {
        if (d == null) {
            return c.absdbl();
        }
        return d.toDouble(c);
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

    public static ComplexMatrix matrix(TMatrix t) {
        return (ComplexMatrix) t.to($COMPLEX);
    }

    public static TMatrix<Expr> ematrix(TMatrix t) {
        return new EMatrixFromTMatrix(t);
    }
    //</editor-fold>

    public static <T> VectorSpace<T> getVectorSpace(TypeName<T> cls) {
        if ($COMPLEX.isAssignableFrom(cls)) {
            return (VectorSpace<T>) MathsBase.COMPLEX_VECTOR_SPACE;
        }
        if ($DOUBLE.isAssignableFrom(cls)) {
            return (VectorSpace<T>) MathsBase.DOUBLE_VECTOR_SPACE;
        }
        if ($EXPR.isAssignableFrom(cls)) {
            return (VectorSpace<T>) MathsBase.EXPR_VECTOR_SPACE;
        }
        if (TMatrix.class.isAssignableFrom(cls.getTypeClass())) {
            TypeName ii = cls.getParameters()[0];
            return new TMatrixVectorSpace(ii, getVectorSpace(ii));
        }
        throw new NoSuchElementException("Vector space Not yet supported for " + cls);
    }

    public static DoubleVector refineSamples(TVector<Double> values, int n) {
        return MathsSampler.refineSamples(values, n);
    }

    /**
     * adds n points between each 2 points
     *
     * @param values initial sample
     * @return
     */
    public static double[] refineSamples(double[] values, int n) {
        return MathsSampler.refineSamples(values, n);
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
            return MathsBase.columnVector(new Complex[]{Complex.valueOf(value)});
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
            return MathsBase.columnVector(new Complex[]{value});
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

//    private static class StringTypeReference extends TypeName<String> {
//    }
//
//    private static class MatrixTypeReference extends TypeName<Matrix> {
//    }
//
//    private static class VectorTypeReference extends TypeName<Vector> {
//    }
//
//    private static class TMatrixTypeReference extends TypeName<TMatrix<Complex>> {
//    }
//
//    private static class TVectorTypeReference extends TypeName<TList<Complex>> {
//    }
//
//    private static class ComplexTypeReference extends TypeName<Complex> {
//    }
//
//    private static class DoubleTypeReference extends TypeName<Double> {
//    }
//
//    private static class BooleanTypeReference extends TypeName<Boolean> {
//    }
//
//    private static class PointTypeReference extends TypeName<Point> {
//
//    }
//
//    private static class FileTypeReference extends TypeName<File> {
//    }
//
//    private static class IntegerTypeReference extends TypeName<Integer> {
//    }
//
//    private static class LongTypeReference extends TypeName<Long> {
//    }
//
//    private static class ExprTypeReference extends TypeName<Expr> {
//    }
//
//    private static class TListTypeReference extends TypeName<TList<Complex>> {
//    }
//
//    private static class TListExprTypeReference extends TypeName<TList<Expr>> {
//    }
//
//    private static class TListDoubleTypeReference extends TypeName<TList<Double>> {
//    }
//
//    private static class TListIntegerTypeReference extends TypeName<TList<Integer>> {
//    }
//
//    private static class TListBooleanTypeReference extends TypeName<TList<Boolean>> {
//    }
//
//    private static class TListMatrixTypeReference extends TypeName<TList<Matrix>> {
//    }

    public static String getHadrumathsVersion() {
        return HadrumathsInitializerService.getVersion();
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
        ExpressionManager m = createExpressionParser();
        Object evaluated = m.createEvaluator(expression).evaluate();
        if (evaluated instanceof Expr) {
            return (Expr) evaluated;
        }
        if (evaluated instanceof Number) {
            return expr(((Number) evaluated).doubleValue());
        }
        return (Expr) evaluated;
    }

    public static ExpressionManager createExpressionEvaluator() {
        return ExpressionManagerFactory.createEvaluator();
    }

    public static ExpressionManager createExpressionParser() {
        return ExpressionManagerFactory.createParser();
    }

    public static Expr evalExpression(String expression) {
        ExpressionManager m = createExpressionEvaluator();
        Object evaluated = m.createEvaluator(expression).evaluate();
        if (evaluated instanceof Expr) {
            return (Expr) evaluated;
        }
        if (evaluated instanceof Number) {
            return expr(((Number) evaluated).doubleValue());
        }
        return (Expr) evaluated;
    }

    public static double toRadians(double a) {
        return Math.toRadians(a);
    }

    public static double[] toRadians(double[] a) {
        double[] b = new double[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = Math.toRadians(a[i]);
        }
        return b;
    }

    public static Complex det(ComplexMatrix m) {
        return m.det();

    }


    public static int toInt(Object o) {
        return toInt(o, null);
    }

    public static int toInt(Object o, Integer defaultValue) {
        if (o == null) {
            if (defaultValue != null) {
                return defaultValue.intValue();
            }
            throw new NullPointerException();
        }
        try {
            if (o instanceof Number) {
                Number s = (Number) o;
                return (int) s.intValue();
            }
            if (o instanceof String) {
                String s = (String) o;
                if (s.contains(".")) {

                    return (int) Double.parseDouble(s);
                }
                return Integer.parseInt(s);
            }
            if (o instanceof Expr) {
                Expr s = (Expr) o;
                s = s.simplify();
                Complex c = (Complex) s;
                return (int) c.toDouble();
            }
        } catch (RuntimeException ex) {
            if (defaultValue != null) {
                return defaultValue;
            }
            throw ex;
        }
        if (defaultValue != null) {
            return defaultValue;
        }
        throw new ClassCastException();
    }


    public static long toLong(Object o) {
        return toLong(o, null);
    }

    public static long toLong(Object o, Long defaultValue) {
        if (o == null) {
            if (defaultValue != null) {
                return defaultValue.intValue();
            }
            throw new NullPointerException();
        }
        try {
            if (o instanceof Number) {
                Number s = (Number) o;
                return (int) s.intValue();
            }
            if (o instanceof String) {
                String s = (String) o;
                if (s.contains(".")) {

                    return (long) Double.parseDouble(s);
                }
                return Long.parseLong(s);
            }
            if (o instanceof Expr) {
                Expr s = (Expr) o;
                s = s.simplify();
                Complex c = (Complex) s;
                return (long) c.toDouble();
            }
        } catch (RuntimeException ex) {
            if (defaultValue != null) {
                return defaultValue;
            }
            throw ex;
        }
        if (defaultValue != null) {
            return defaultValue;
        }
        throw new ClassCastException();
    }

    public static double toDouble(Object o) {
        return toDouble(o, null);
    }

    public static double toDouble(Object o, Double defaultValue) {
        if (o == null) {
            if (defaultValue != null) {
                return defaultValue.doubleValue();
            }
            throw new NullPointerException();
        }
        try {
            if (o instanceof Number) {
                Number s = (Number) o;
                return s.doubleValue();
            }
            if (o instanceof String) {
                String s = (String) o;
                return Double.parseDouble(s);
            }
            if (o instanceof Expr) {
                Expr s = (Expr) o;
                s = s.simplify();
                Complex c = (Complex) s;
                return c.toDouble();
            }
        } catch (RuntimeException ex) {
            if (defaultValue != null) {
                return defaultValue;
            }
            throw ex;
        }
        if (defaultValue != null) {
            return defaultValue;
        }
        throw new ClassCastException();
    }

    public static float toFloat(Object o) {
        return toFloat(o, null);
    }

    public static float toFloat(Object o, Float defaultValue) {
        if (o == null) {
            if (defaultValue != null) {
                return defaultValue.floatValue();
            }
            throw new NullPointerException();
        }
        try {
            if (o instanceof Number) {
                Number s = (Number) o;
                return s.floatValue();
            }
            if (o instanceof String) {
                String s = (String) o;
                return Float.parseFloat(s);
            }
            if (o instanceof Expr) {
                Expr s = (Expr) o;
                s = s.simplify();
                Complex c = (Complex) s;
                return (float) c.toDouble();
            }
        } catch (RuntimeException ex) {
            if (defaultValue != null) {
                return defaultValue;
            }
            throw ex;
        }
        if (defaultValue != null) {
            return defaultValue;
        }
        throw new ClassCastException();
    }

    public static DoubleToComplex DC(Expr e) {
        return e == null ? null : e.toDC();
    }

    public static DoubleToDouble DD(Expr e) {
        return e == null ? null : e.toDD();
    }

    public static DoubleToVector DV(Expr e) {
        return e == null ? null : e.toDV();
    }

    public static DoubleToMatrix DM(Expr e) {
        return e == null ? null : e.toDM();
    }

    public static ComplexMatrix matrix(Expr e) {
        return e == null ? null : e.toMatrix();
    }
}
