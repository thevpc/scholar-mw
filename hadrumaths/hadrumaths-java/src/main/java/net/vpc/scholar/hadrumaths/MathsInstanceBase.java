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

public class MathsInstanceBase {

    //<editor-fold desc="constants functions">
    public final double PI = Math.PI;
    public final double E = Math.E;
    public final DoubleToDouble DDZERO = DoubleValue.valueOf(0, Domain.FULLX);
    public final DoubleToDouble DDNAN = DoubleValue.valueOf(Double.NaN, Domain.FULLX);
    public final DoubleToComplex DCZERO = DDZERO.toDC();
    public final DoubleToVector DVZERO3 = DefaultDoubleToVector.create(DCZERO, DCZERO, DCZERO);
    public final Expr EZERO = DDZERO;
    public final Expr EONE = DoubleValue.valueOf(1, Domain.FULLX);
    public final Expr X = new XX(Domain.FULLX);
    public final Expr Y = new YY(Domain.FULLXY);
    public final Expr Z = new ZZ(Domain.FULLXYZ);
    public final double HALF_PI = Math.PI / 2.0;
    public final Complex I = Complex.I;
    public final Complex CNaN = Complex.NaN;
    public final Complex CONE = Complex.ONE;
    public final Complex CZERO = Complex.ZERO;//    public boolean DEBUG = true;
    public final DoubleToVector DVZERO1 = DefaultDoubleToVector.create(DCZERO);
    public final DoubleToVector DVZERO2 = DefaultDoubleToVector.create(DCZERO, DCZERO);
    public final Complex î = Complex.I;
    public final Expr ê = EONE;
    public final Complex ĉ = CONE;
    public final double METER = 1;
    public final double HZ = 1;
    public final long BYTE = 1;
    public final long MILLISECOND = 1;
    /**
     * kibibyte
     */
    public final int KiBYTE = 1024;
    /**
     * mibibyte
     */
    public final int MiBYTE = 1024 * KiBYTE;
    /**
     * TEBI Byte
     */
    public final long GiBYTE = 1024 * MiBYTE;
    /**
     * TEBI Byte
     */
    public final long TiBYTE = 1024L * GiBYTE;
    /**
     * PEBI Byte
     */
    public final long PiBYTE = 1024L * TiBYTE;
    /**
     * exbibyte
     */
    public final long EiBYTE = 1024L * PiBYTE;

    public final double YOCTO = 1E-24;
    public final double ZEPTO = 1E-21;
    public final double ATTO = 1E-18;
    public final double FEMTO = 1E-15;
    public final double PICO = 1E-12;
    public final double NANO = 1E-9;
    public final double MICRO = 1E-6;
    public final double MILLI = 1E-3;
    public final double CENTI = 1E-2;
    public final double DECI = 1E-1;
    /**
     * DECA
     */
    public final int DECA = 10;
    /**
     * HECTO
     */
    public final int HECTO = 100;

    /**
     * KILO
     */
    public final int KILO = 1000;
    /**
     * MEGA
     */
    public final int MEGA = 1000 * KILO;
    /**
     * MEGA
     */
    public final long GIGA = 1000 * MEGA;
    /**
     * TERA
     */
    public final long TERA = 1000 * GIGA;
    /**
     * PETA
     */
    public final long PETA = 1000 * TERA;
    /**
     * EXA
     */
    public final long EXA = 1000 * PETA;
    /**
     * ZETTA
     */
    public final long ZETTA = 1000 * EXA;
    /**
     * YOTTA
     */
    public final long YOTTA = 1000 * ZETTA;
    public final long SECOND = 1000;
    public final long MINUTE = 60 * SECOND;
    public final long HOUR = 60 * MINUTE;
    public final long DAY = 24 * HOUR;
    public final double KHZ = 1E3;
    public final double MHZ = 1E6;
    public final double GHZ = 1E9;
    public final double MILLIMETER = 1E-3;
    public final double MM = 1E-3;
    public final double CM = 1E-2;
    public final double CENTIMETER = 1E-2;
    /**
     * light celerity. speed of light in vacuum
     */
//    public final int C = 300000000;
    public final int C = 299792458;//m.s^-1
    /**
     * Newtonian constant of gravitation
     */
    public final double G = 6.6738480E-11; //m3·kg^−1·s^−2;
    /**
     * Planck constant
     */
    public final double H = 6.6260695729E-34; //J·s;
    /**
     * Reduced Planck constant
     */
    public final double Hr = H / (2 * PI); //J·s;
    /**
     * magnetic constant (vacuum permeability)
     */
    public final double U0 = Math.PI * 4e-7; //N·A−2
    /**
     * electric constant (vacuum permittivity) =1/(u0*C^2)
     */
    public final double EPS0 = 8.854187817e-12;//F·m−1
    /**
     * characteristic impedance of vacuum =1/(u0*C)
     */
    public final double Z0 = 1 / (U0 * C);//F·m−1
    /**
     * Coulomb's constant
     */
    public final double Ke = 1 / (4 * PI * EPS0);//F·m−1
    /**
     * elementary charge
     */
    public final double Qe = 1.60217656535E-19;//C
    public final VectorSpace<Complex> COMPLEX_VECTOR_SPACE = new ComplexVectorSpace();
    public final VectorSpace<Expr> EXPR_VECTOR_SPACE = new ExprVectorSpace();
    public final VectorSpace<Double> DOUBLE_VECTOR_SPACE = new DoubleVectorSpace();
    public final int X_AXIS = 0;
    public final int Y_AXIS = 1;
    public final int Z_AXIS = 2;
    public final TStoreManager<ComplexMatrix> MATRIX_STORE_MANAGER = new TStoreManager<ComplexMatrix>() {
        @Override
        public void store(ComplexMatrix item, File file) {
            item.store(file);
        }

        @Override
        public ComplexMatrix load(File file) {
            return Config.getComplexMatrixFactory().load(file);
        }
    };
    public final TStoreManager<TMatrix> TMATRIX_STORE_MANAGER = new TStoreManager<TMatrix>() {
        @Override
        public void store(TMatrix item, File file) {
            item.store(file);
        }

        @Override
        public TMatrix load(File file) {
            return Config.getComplexMatrixFactory().load(file);
        }
    };

    public final TStoreManager<TVector> TVECTOR_STORE_MANAGER = new TStoreManager<TVector>() {
        @Override
        public void store(TVector item, File file) {
            item.store(file);
        }

        @Override
        public TVector load(File file) {
            return Config.getComplexMatrixFactory().load(file).toVector();
        }
    };

    public final TStoreManager<ComplexVector> VECTOR_STORE_MANAGER = new TStoreManager<ComplexVector>() {
        @Override
        public void store(ComplexVector item, File file) {
            item.store(file);
        }

        @Override
        public ComplexVector load(File file) {
            return Config.getComplexMatrixFactory().load(file).toVector();
        }
    };
    public final Converter IDENTITY = new IdentityConverter();
    public final Converter<Complex, Double> COMPLEX_TO_DOUBLE = new ComplexDoubleConverter();
    public final Converter<Double, Complex> DOUBLE_TO_COMPLEX = new DoubleComplexConverter();
    public final Converter<Double, TVector> DOUBLE_TO_TVECTOR = new DoubleTVectorConverter();
    public final Converter<TVector, Double> TVECTOR_TO_DOUBLE = new TVectorDoubleConverter();
    public final Converter<Complex, TVector> COMPLEX_TO_TVECTOR = new ComplexTVectorConverter();
    public final Converter<TVector, Complex> TVECTOR_TO_COMPLEX = new TVectorComplexConverter();
    public final Converter<Complex, Expr> COMPLEX_TO_EXPR = new ComplexExprConverter();
    public final Converter<Expr, Complex> EXPR_TO_COMPLEX = new ExprComplexConverter();
    public final Converter<Double, Expr> DOUBLE_TO_EXPR = new DoubleExprConverter();
    public final Converter<Expr, Double> EXPR_TO_DOUBLE = new ExprDoubleConverter();
    //    public String getAxisLabel(int axis){
//        switch(axis){
//            case X_AXIS:return "X";
//            case Y_AXIS:return "Y";
//            case Z_AXIS:return "Z";
//        }
//        throw new IllegalArgumentException("Unknown Axis "+axis);
//    }
    public final TypeName<String> $STRING = new TypeName(String.class.getName());
    public final TypeName<Complex> $COMPLEX = new TypeName(Complex.class.getName());
    public final TypeName<ComplexMatrix> $MATRIX = new TypeName(ComplexMatrix.class.getName());
    public final TypeName<ComplexVector> $VECTOR = new TypeName(ComplexVector.class.getName());
    public final TypeName<TMatrix<Complex>> $CMATRIX = new TypeName(TMatrix.class.getName(), $COMPLEX);
    public final TypeName<TVector<Complex>> $CVECTOR = new TypeName(TVector.class.getName(), $COMPLEX);
    public final TypeName<Double> $DOUBLE = new TypeName(Double.class.getName());
    public final TypeName<Boolean> $BOOLEAN = new TypeName(Boolean.class.getName());
    public final TypeName<Point> $POINT = new TypeName(Point.class.getName());
    public final TypeName<File> $FILE = new TypeName(File.class.getName());
    //</editor-fold>
    public final TypeName<Integer> $INTEGER = new TypeName(Integer.class.getName());
    public final TypeName<Long> $LONG = new TypeName(Long.class.getName());
    public final TypeName<Expr> $EXPR = new TypeName(Expr.class.getName());
    public final TypeName<TVector<Complex>> $CLIST = new TypeName(TVector.class.getName(), $COMPLEX);
    public final TypeName<TVector<Expr>> $ELIST = new TypeName(TVector.class.getName(), $EXPR);
    public final TypeName<TVector<Double>> $DLIST = new TypeName(TVector.class.getName(), $DOUBLE);
    public final TypeName<TVector<TVector<Double>>> $DLIST2 = new TypeName(TVector.class.getName(), $DLIST);
    public final TypeName<TVector<Integer>> $ILIST = new TypeName(TVector.class.getName(), $INTEGER);
    public final TypeName<TVector<Boolean>> $BLIST = new TypeName(TVector.class.getName(), $BOOLEAN);
    public final TypeName<TVector<ComplexMatrix>> $MLIST = new TypeName(TVector.class.getName(), $MATRIX);
    public final SimpleDateFormat UNIVERSAL_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final int ARCH_MODEL_BITS = Integer.valueOf(
            System.getProperty("sun.arch.data.model") != null ? System.getProperty("sun.arch.data.model")
                    : System.getProperty("os.arch").contains("64") ? "64" : "32"
    );
    private final int BYTE_BITS = 8;
    private final int WORD = ARCH_MODEL_BITS / BYTE_BITS;
    private final int JOBJECT_MIN_SIZE = 16;
    private final Logger $log = Logger.getLogger(MathsInstanceBase.class.getName());
    public final MathsConfig Config = MathsConfig.INSTANCE;
    public DistanceStrategy<Double> DISTANCE_DOUBLE = new DistanceStrategy<Double>() {
        @Override
        public double distance(Double a, Double b) {
            return Math.abs(b - a);
        }
    };
    public DistanceStrategy<Complex> DISTANCE_COMPLEX = Complex.DISTANCE;
    public DistanceStrategy<ComplexMatrix> DISTANCE_MATRIX = new DistanceStrategy<ComplexMatrix>() {
        @Override
        public double distance(ComplexMatrix a, ComplexMatrix b) {
            return a.getError(b);
        }
    };
    public DistanceStrategy<ComplexVector> DISTANCE_VECTOR = new DistanceStrategy<ComplexVector>() {
        @Override
        public double distance(ComplexVector a, ComplexVector b) {
            return a.toMatrix().getError(b.toMatrix());
        }
    };

    {
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

    protected MathsInstanceBase() {
    }

    public Domain xdomain(double min, double max) {
        return Domain.forBounds(min, max);
    }

    public Domain ydomain(double min, double max) {
        return Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, min, max);
    }

    public DomainExpr ydomain(DomainExpr min, DomainExpr max) {
        return DomainExpr.forBounds(Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, min, max);
    }

    public Domain zdomain(double min, double max) {
        return Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, min, max);
    }

    public DomainExpr zdomain(Expr min, Expr max) {
        return DomainExpr.forBounds(Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, min, max);
    }

    public Domain domain(RightArrowUplet2.Double u) {
        return Domain.forBounds(u.getFirst(), u.getSecond());
    }

    public Domain domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return Domain.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public Domain domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return Domain.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public Expr domain(RightArrowUplet2.Expr u) {
        if (u.getFirst().isDouble() && u.getSecond().isDouble()) {
            return Domain.forBounds(u.getFirst().toDouble(), u.getSecond().toDouble());
        }
        return DomainExpr.forBounds(u.getFirst(), u.getSecond());
    }

    public Expr domain(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy) {
        if (ux.getFirst().isDouble() && ux.getSecond().isDouble() && uy.getFirst().isDouble() && uy.getSecond().isDouble()) {
            return Domain.forBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble());
        }
        return DomainExpr.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public Expr domain(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy, RightArrowUplet2.Expr uz) {
        if (ux.getFirst().isDouble() && ux.getSecond().isDouble() && uy.getFirst().isDouble() && uy.getSecond().isDouble() && uz.getFirst().isDouble() && uz.getSecond().isDouble()) {
            return Domain.forBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble(), uz.getFirst().toDouble(), uz.getSecond().toDouble());
        }
        return DomainExpr.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public DomainExpr domain(Expr min, Expr max) {
        return DomainExpr.forBounds(min, max);
    }

    public Domain domain(double min, double max) {
        return Domain.forBounds(min, max);
    }

    public Domain domain(double xmin, double xmax, double ymin, double ymax) {
        return Domain.forBounds(xmin, xmax, ymin, ymax);
    }

    public DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return DomainExpr.forBounds(xmin, xmax, ymin, ymax);
    }

    public Domain domain(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return Domain.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return DomainExpr.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public Domain II(RightArrowUplet2.Double u) {
        return Domain.forBounds(u.getFirst(), u.getSecond());
    }

    public Domain II(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return Domain.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public Domain II(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return Domain.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public Expr II(RightArrowUplet2.Expr u) {
        if (u.getFirst().isDouble() && u.getSecond().isDouble()) {
            return Domain.forBounds(u.getFirst().toDouble(), u.getSecond().toDouble());
        }
        return DomainExpr.forBounds(u.getFirst(), u.getSecond());
    }

    public Expr II(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy) {
        if (ux.getFirst().isDouble() && ux.getSecond().isDouble() && uy.getFirst().isDouble() && uy.getSecond().isDouble()) {
            return Domain.forBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble());
        }
        return DomainExpr.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public Expr II(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy, RightArrowUplet2.Expr uz) {
        if (ux.getFirst().isDouble() && ux.getSecond().isDouble() && uy.getFirst().isDouble() && uy.getSecond().isDouble() && uz.getFirst().isDouble() && uz.getSecond().isDouble()) {
            return Domain.forBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble(), uz.getFirst().toDouble(), uz.getSecond().toDouble());
        }
        return DomainExpr.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public DomainExpr II(Expr min, Expr max) {
        return DomainExpr.forBounds(min, max);
    }

    public Domain II(double min, double max) {
        return Domain.forBounds(min, max);
    }

    public Domain II(double xmin, double xmax, double ymin, double ymax) {
        return Domain.forBounds(xmin, xmax, ymin, ymax);
    }

    public DomainExpr II(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return DomainExpr.forBounds(xmin, xmax, ymin, ymax);
    }

    public Domain II(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return Domain.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public DomainExpr II(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return DomainExpr.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    //<editor-fold desc="parameters functions">
    public DoubleParam param(String name) {
        return new DoubleParam(name);
    }

    public DoubleArrayParamSet doubleParamSet(Param param) {
        return new DoubleArrayParamSet(param);
    }

    public DoubleArrayParamSet paramSet(Param param, double[] values) {
        return new DoubleArrayParamSet(param, values);
    }

    public FloatArrayParamSet paramSet(Param param, float[] values) {
        return new FloatArrayParamSet(param, values);
    }

    public FloatArrayParamSet floatParamSet(Param param) {
        return new FloatArrayParamSet(param);
    }

    public LongArrayParamSet paramSet(Param param, long[] values) {
        return new LongArrayParamSet(param, values);
    }

    public LongArrayParamSet longParamSet(Param param) {
        return new LongArrayParamSet(param);
    }

    public <T> ArrayParamSet<T> paramSet(Param param, T[] values) {
        return new ArrayParamSet<T>(param, values);
    }

    public <T> ArrayParamSet<T> objectParamSet(Param param) {
        return new ArrayParamSet<T>(param);
    }

    public IntArrayParamSet paramSet(Param param, int[] values) {
        return new IntArrayParamSet(param, values);
    }

    public IntArrayParamSet intParamSet(Param param) {
        return new IntArrayParamSet(param);
    }

    public BooleanArrayParamSet paramSet(Param param, boolean[] values) {
        return new BooleanArrayParamSet(param, values);
    }

    public BooleanArrayParamSet boolParamSet(Param param) {
        return new BooleanArrayParamSet(param);
    }

    public XParamSet xParamSet(int xsamples) {
        return new XParamSet(xsamples);
    }

    public XParamSet xyParamSet(int xsamples, int ysamples) {
        return new XParamSet(xsamples, ysamples);
    }

    public XParamSet xyzParamSet(int xsamples, int ysamples, int zsamples) {
        return new XParamSet(xsamples, ysamples, zsamples);
    }
    //</editor-fold>

    //<editor-fold desc="Matrix functions">
    public ComplexMatrix zerosMatrix(ComplexMatrix other) {
        return Config.getComplexMatrixFactory().newZeros(other);
    }

    public ComplexMatrix constantMatrix(int dim, Complex value) {
        return Config.getComplexMatrixFactory().newConstant(dim, value);
    }

    public ComplexMatrix onesMatrix(int dim) {
        return Config.getComplexMatrixFactory().newOnes(dim);
    }

    public ComplexMatrix onesMatrix(int rows, int cols) {
        return Config.getComplexMatrixFactory().newOnes(rows, cols);
    }

    public ComplexMatrix constantMatrix(int rows, int cols, Complex value) {
        return Config.getComplexMatrixFactory().newConstant(rows, cols, value);
    }

    public ComplexMatrix zerosMatrix(int dim) {
        return Config.getComplexMatrixFactory().newZeros(dim);
    }

    public ComplexMatrix I(Complex[][] iValue) {
        return matrix(iValue).mul(I);
    }

    public ComplexMatrix zerosMatrix(int rows, int cols) {
        return Config.getComplexMatrixFactory().newZeros(rows, cols);
    }

    public ComplexMatrix identityMatrix(ComplexMatrix c) {
        return Config.getComplexMatrixFactory().newIdentity(c);
    }

    public ComplexMatrix NaNMatrix(int dim) {
        return Config.getComplexMatrixFactory().newNaN(dim);
    }

    public ComplexMatrix NaNMatrix(int rows, int cols) {
        return Config.getComplexMatrixFactory().newNaN(rows, cols);
    }

    public ComplexMatrix identityMatrix(int dim) {
        return Config.getComplexMatrixFactory().newIdentity(dim);
    }

    public ComplexMatrix identityMatrix(int rows, int cols) {
        return Config.getComplexMatrixFactory().newIdentity(rows, cols);
    }

    public ComplexMatrix matrix(ComplexMatrix matrix) {
        return Config.getComplexMatrixFactory().newMatrix(matrix);
    }

    public ComplexMatrix matrix(String string) {
        return Config.getComplexMatrixFactory().newMatrix(string);
    }

    public ComplexMatrix matrix(Complex[][] complex) {
        return Config.getComplexMatrixFactory().newMatrix(complex);
    }

    public ComplexMatrix matrix(double[][] complex) {
        return Config.getComplexMatrixFactory().newMatrix(complex);
    }

    public ComplexMatrix matrix(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newMatrix(rows, cols, cellFactory);
    }

    //    public Matrix matrix(int rows, int cols, Int2ToComplex cellFactory) {
//        return Config.getDefaultMatrixFactory().newMatrix(rows, cols, new I2ToTMatrixCell<Complex>(cellFactory));
//    }
    public ComplexMatrix columnMatrix(final Complex... values) {
        return Config.getComplexMatrixFactory().newColumnMatrix(values);
    }

    public ComplexMatrix columnMatrix(final double... values) {
        Complex[] d = new Complex[values.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = Complex.valueOf(values[i]);
        }
        return Config.getComplexMatrixFactory().newColumnMatrix(d);
    }

    public ComplexMatrix rowMatrix(final double... values) {
        Complex[] d = new Complex[values.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = Complex.valueOf(values[i]);
        }
        return Config.getComplexMatrixFactory().newRowMatrix(d);
    }

    public ComplexMatrix rowMatrix(final Complex... values) {
        return Config.getComplexMatrixFactory().newRowMatrix(values);
    }

    public ComplexMatrix columnMatrix(int rows, final TVectorCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newColumnMatrix(rows, cellFactory);
    }

    public ComplexMatrix rowMatrix(int columns, final TVectorCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newRowMatrix(columns, cellFactory);
    }

    public ComplexMatrix symmetricMatrix(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newSymmetric(rows, cols, cellFactory);
    }

    public ComplexMatrix hermitianMatrix(int rows, int cols, TMatrixCell<Complex> cellFactory) {
        return MathsBase.hermitianMatrix(rows, cols, cellFactory);
    }

    public ComplexMatrix diagonalMatrix(int rows, final TVectorCell<Complex> cellFactory) {
        return MathsBase.diagonalMatrix(rows, cellFactory);
    }

    public ComplexMatrix diagonalMatrix(final Complex... c) {
        return Config.getComplexMatrixFactory().newDiagonal(c);
    }

    public ComplexMatrix matrix(int dim, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newMatrix(dim, cellFactory);
    }

    public ComplexMatrix matrix(int rows, int columns) {
        return Config.getComplexMatrixFactory().newMatrix(rows, columns);
    }

    public ComplexMatrix symmetricMatrix(int dim, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newMatrix(dim, cellFactory);
    }

    public ComplexMatrix hermitianMatrix(int dim, TMatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newHermitian(dim, cellFactory);
    }

    public ComplexMatrix randomRealMatrix(int m, int n) {
        return Config.getComplexMatrixFactory().newRandomReal(m, n);
    }

    public ComplexMatrix randomRealMatrix(int m, int n, int min, int max) {
        return Config.getComplexMatrixFactory().newRandomReal(m, n, min, max);
    }

    public ComplexMatrix randomRealMatrix(int m, int n, double min, double max) {
        return Config.getComplexMatrixFactory().newRandomReal(m, n, min, max);
    }

    public ComplexMatrix randomImagMatrix(int m, int n, double min, double max) {
        return Config.getComplexMatrixFactory().newRandomImag(m, n, min, max);
    }

    public ComplexMatrix randomImagMatrix(int m, int n, int min, int max) {
        return Config.getComplexMatrixFactory().newRandomImag(m, n, min, max);
    }

    public ComplexMatrix randomMatrix(int m, int n, int minReal, int maxReal, int minImag, int maxImag) {
        return Config.getComplexMatrixFactory().newRandom(m, n, minReal, maxReal, minImag, maxImag);
    }

    public ComplexMatrix randomMatrix(int m, int n, double minReal, double maxReal, double minImag, double maxImag) {
        return Config.getComplexMatrixFactory().newRandom(m, n, minReal, maxReal, minImag, maxImag);
    }

    public ComplexMatrix randomMatrix(int m, int n, double min, double max) {
        return Config.getComplexMatrixFactory().newRandom(m, n, min, max);
    }

    public ComplexMatrix randomMatrix(int m, int n, int min, int max) {
        return Config.getComplexMatrixFactory().newRandom(m, n, min, max);
    }

    public ComplexMatrix randomImagMatrix(int m, int n) {
        return Config.getComplexMatrixFactory().newRandomImag(m, n);
    }

    public <T> TMatrix<T> loadTMatrix(TypeName<T> componentType, File file) throws UncheckedIOException {
        throw new IllegalArgumentException("TODO");
    }

    public ComplexMatrix loadMatrix(File file) throws UncheckedIOException {
        return Config.getComplexMatrixFactory().load(file);
    }

    public ComplexMatrix matrix(File file) throws UncheckedIOException {
        return Config.getComplexMatrixFactory().load(file);
    }

    public void storeMatrix(ComplexMatrix m, String file) throws UncheckedIOException {
        m.store(file == null ? (File) null : new File(Config.expandPath(file)));
    }

    public void storeMatrix(ComplexMatrix m, File file) throws UncheckedIOException {
        m.store(file);
    }

    public ComplexMatrix loadOrEvalMatrix(String file, TItem<ComplexMatrix> item) throws UncheckedIOException {
        return loadOrEvalMatrix(new File(Config.expandPath(file)), item);
    }

    public ComplexVector loadOrEvalVector(String file, TItem<TVector<Complex>> item) throws UncheckedIOException {
        return loadOrEvalVector(new File(Config.expandPath(file)), item);
    }

    public ComplexMatrix loadOrEvalMatrix(File file, TItem<ComplexMatrix> item) throws UncheckedIOException {
        return loadOrEval($MATRIX, file, item);
    }

    public ComplexVector loadOrEvalVector(File file, TItem<TVector<Complex>> item) throws UncheckedIOException {
        return loadOrEval($VECTOR, file, (TItem) item);
    }

    public <T> TMatrix loadOrEvalTMatrix(String file, TItem<TMatrix<T>> item) throws UncheckedIOException {
        return loadOrEvalTMatrix(new File(Config.expandPath(file)), item);
    }

    public <T> TVector<T> loadOrEvalTVector(String file, TItem<TVector<T>> item) throws UncheckedIOException {
        return loadOrEvalTVector(new File(Config.expandPath(file)), item);
    }

    public <T> TMatrix<T> loadOrEvalTMatrix(File file, TItem<TMatrix<T>> item) throws UncheckedIOException {
        return loadOrEval((TypeName) $CMATRIX, file, item);
    }

    public <T> TVector loadOrEvalTVector(File file, TItem<TVector<T>> item) throws UncheckedIOException {
        return loadOrEval($CVECTOR, file, (TItem) item);
    }

    public <T> T loadOrEval(TypeName<T> type, File file, TItem<T> item) throws UncheckedIOException {
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

    public ComplexMatrix loadMatrix(String file) throws UncheckedIOException {
        return Config.getComplexMatrixFactory().load(new File(Config.expandPath(file)));
    }

    public ComplexMatrix inv(ComplexMatrix c) {
        return c.inv();
    }

    public ComplexMatrix tr(ComplexMatrix c) {
        return c.transpose();
    }

    public ComplexMatrix trh(ComplexMatrix c) {
        return c.transposeHermitian();
    }

    public ComplexMatrix transpose(ComplexMatrix c) {
        return c.transpose();
    }

    public ComplexMatrix transposeHermitian(ComplexMatrix c) {
        return c.transposeHermitian();
    }

    //</editor-fold>
    //<editor-fold desc="Vector functions">
    public ComplexVector rowVector(Complex[] elems) {
        return ArrayComplexVector.Row(elems);
    }

    public ComplexVector constantColumnVector(int size, Complex c) {
        Complex[] arr = new Complex[size];
        for (int i = 0; i < size; i++) {
            arr[i] = c;
        }
        return ArrayComplexVector.Column(arr);
    }

    public ComplexVector constantRowVector(int size, Complex c) {
        Complex[] arr = new Complex[size];
        for (int i = 0; i < size; i++) {
            arr[i] = c;
        }
        return ArrayComplexVector.Row(arr);
    }

    public ComplexVector zerosVector(int size) {
        return zerosColumnVector(size);
    }

    public ComplexVector zerosColumnVector(int size) {
        return constantColumnVector(size, CZERO);
    }

    public ComplexVector zerosRowVector(int size) {
        return constantRowVector(size, CZERO);
    }

    public ComplexVector NaNColumnVector(int dim) {
        return constantColumnVector(dim, Complex.NaN);
    }

    public ComplexVector NaNRowVector(int dim) {
        return constantRowVector(dim, Complex.NaN);
    }

    public TVector<Expr> columnVector(Expr[] expr) {
        return MathsBase.columnVector(expr);
    }

    public TVector<Expr> rowVector(Expr[] expr) {
        return MathsBase.rowVector(expr);
    }

    public TVector<Expr> columnEVector(int rows, final TVectorCell<Expr> cellFactory) {
        return columnTVector($EXPR, rows, cellFactory);
    }

    public TVector<Expr> rowEVector(int rows, final TVectorCell<Expr> cellFactory) {
        return rowTVector($EXPR, rows, cellFactory);
    }

    public <T> TVector<T> updatableOf(TVector<T> vector) {
        return new UpdatableTVector<T>(
                vector.getComponentType(), new CachedTVectorUpdatableModel<T>(vector, vector.getComponentType()),
                false
        );
    }

    public Complex[][] copyOf(Complex[][] val) {
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

    public Complex[] copyOf(Complex[] val) {
        if (val == null) {
            return val;
        }
        return Arrays.copyOf(val, val.length);
    }

    public <T> TVector<T> copyOf(TVector<T> vector) {
        TVector<T> ts = list(vector.getComponentType(), vector.isRow(), vector.size());
        ts.appendAll(vector);
        return ts;
    }

    public <T> TVector<T> columnTVector(TypeName<T> cls, final TVectorModel<T> cellFactory) {
        return new ReadOnlyTList<T>(
                cls, false, cellFactory
        );
//        return new UpdatableTVector<>(
//                cls,new CachedTVectorUpdatableModel<>(cellFactory,cls),
//                false
//        );
    }

    public <T> TVector<T> rowTVector(TypeName<T> cls, final TVectorModel<T> cellFactory) {
        return new ReadOnlyTList<>(
                cls, true, cellFactory
        );
//        return new UpdatableTVector<>(
//                cls,new CachedTVectorUpdatableModel<>(cellFactory,cls),
//                true
//        );
    }

    public <T> TVector<T> columnTVector(TypeName<T> cls, int rows, final TVectorCell<T> cellFactory) {
        return columnTVector(cls, new TVectorModelFromCell<>(rows, cellFactory));
    }

    public <T> TVector<T> rowTVector(TypeName<T> cls, int rows, final TVectorCell<T> cellFactory) {
        return rowTVector(cls, new TVectorModelFromCell<>(rows, cellFactory));
    }

    public ComplexVector columnVector(int rows, final TVectorCell<Complex> cellFactory) {
        Complex[] arr = new Complex[rows];
        for (int i = 0; i < rows; i++) {
            arr[i] = cellFactory.get(i);
        }
        return columnVector(arr);
    }

    public ComplexVector rowVector(int columns, final TVectorCell<Complex> cellFactory) {
        Complex[] arr = new Complex[columns];
        for (int i = 0; i < columns; i++) {
            arr[i] = cellFactory.get(i);
        }
        return rowVector(arr);
    }

    public ComplexVector columnVector(Complex... elems) {
        return ArrayComplexVector.Column(elems);
    }

    public ComplexVector columnVector(double[] elems) {
        return ArrayComplexVector.Column(ArrayUtils.toComplex(elems));
    }

    public ComplexVector rowVector(double[] elems) {
        return ArrayComplexVector.Row(ArrayUtils.toComplex(elems));
    }

    public ComplexVector column(Complex[] elems) {
        return ArrayComplexVector.Column(elems);
    }

    public ComplexVector row(Complex[] elems) {
        return ArrayComplexVector.Row(elems);
    }

    public ComplexVector trh(ComplexVector c) {
        return c.transpose().conj();
    }

    public ComplexVector tr(ComplexVector c) {
        return c.transpose();
    }

    //</editor-fold>
    //<editor-fold desc="Complex functions">
    public Complex I(double iValue) {
        return Complex.I(iValue);
    }

    public Complex abs(Complex a) {
        return (a.abs());
    }

    public double absdbl(Complex a) {
        return a.absdbl();
    }
    //</editor-fold>

    public double[] getColumn(double[][] a, int index) {
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
    public double[] dtimes(double min, double max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return net.vpc.common.util.ArrayUtils.subArray1(dtimes(min, max, maxTimes), times, strategy);
    }

    public double[] dtimes(double min, double max, int times) {
        return net.vpc.common.util.ArrayUtils.dtimes(min, max, times);
    }

    public float[] ftimes(float min, float max, int times) {
        return net.vpc.common.util.ArrayUtils.ftimes(min, max, times);
    }

    public long[] ltimes(long min, long max, int times) {
        return net.vpc.common.util.ArrayUtils.ltimes(min, max, times);
    }

    public long[] lsteps(long min, long max, long step) {
        return net.vpc.common.util.ArrayUtils.lsteps(min, max, step);
    }

    public int[] itimes(int min, int max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return net.vpc.common.util.ArrayUtils.itimes(min, max, times, maxTimes, strategy);
    }

    public double[] dsteps(int max) {
        return dsteps(0, max, 1);
    }

    public double[] dsteps(double min, double max) {
        return dsteps(min, max, 1);
    }

    public double dstepsLength(double min, double max, double step) {
        return net.vpc.common.util.ArrayUtils.dstepsLength(min, max, step);
    }

    public double dstepsElement(double min, double max, double step, int index) {
        return net.vpc.common.util.ArrayUtils.dstepsElement(min, max, step, index);
    }

    public double[] dsteps(double min, double max, double step) {
        return net.vpc.common.util.ArrayUtils.dsteps(min, max, step);
    }

    //
    public float[] fsteps(float min, float max, float step) {
        return net.vpc.common.util.ArrayUtils.fsteps(min, max, step);
    }

    public int[] isteps(int min, int max, int step) {
        return net.vpc.common.util.ArrayUtils.isteps(min, max, step);
    }

    public int[] isteps(int min, int max, int step, IntFilter filter) {
        return net.vpc.common.util.ArrayUtils.isteps(min, max, step);
    }

    public int[] itimes(int min, int max, int times) {
        return net.vpc.common.util.ArrayUtils.itimes(min, max, times);
    }

    public int[] isteps(int max) {
        return isteps(0, max, 1);
    }

    public int[] isteps(int min, int max) {
        return isteps(min, max, 1);
    }

    public int[] itimes(int min, int max) {
        return itimes(min, max, max - min + 1);
    }

    /**
     * sqrt(a^2 + b^2) without under/overflow.
     *
     * @param a
     * @param b
     * @return
     */
    public double hypot(double a, double b) {
        return MathsTrigo.hypot(a, b);
    }

    public Complex sqr(Complex d) {
        return d.sqr();
    }

    //    public int signOf(double d){
//        return d<0?-1:d>0?1 : 0;
//    }
    public double sqr(double d) {
        return d * d;
    }

    public int sqr(int d) {
        return d * d;
    }

    public long sqr(long d) {
        return d * d;
    }

    public float sqr(float d) {
        return d * d;
    }

    public double sincard(double value) {
        return value == 0 ? 1 : (sin2(value) / value);
    }

    public int minusOnePower(int pow) {
        return (pow % 2 == 0) ? 1 : -1;
    }

    public Complex exp(Complex c) {
        return c.exp();
    }

    public Complex sin(Complex c) {
        return c.sin();
    }

    public Complex sinh(Complex c) {
        return c.sinh();
    }

    public Complex cos(Complex c) {
        return c.cos();
    }

    public Complex log(Complex c) {
        return c.log();
    }

    public Complex log10(Complex c) {
        return c.log10();
    }

    public double log10(double c) {
        return Math.log10(c);
    }

    public double log(double c) {
        return Math.log(c);
    }

    public double acotan(double c) {
        if (c == 0) {
            return HALF_PI;
        }
        return Math.atan(1 / c);
    }

    public double exp(double c) {
        return Math.exp(c);
    }

    public double arg(double c) {
        return 0;
    }

    public Complex db(Complex c) {
        return c.db();
    }

    public Complex db2(Complex c) {
        return c.db2();
    }

    public Complex cosh(Complex c) {
        return c.cosh();
    }

    public Complex csum(Complex... c) {
        MutableComplex x = new MutableComplex();
        for (Complex c1 : c) {
            x.add(c1);
        }
        return x.toComplex();
    }

    public Complex sum(Complex... c) {
        return csum(c);
    }

    public Complex csum(TVectorModel<Complex> c) {
        MutableComplex x = new MutableComplex();
        int size = c.size();
        for (int i = 0; i < size; i++) {
            x.add(c.get(i));
        }
        return x.toComplex();
    }

    public Complex csum(int size, TVectorCell<Complex> c) {
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
    public double chbevl(double x, double[] coef, int N) throws ArithmeticException {
        return MathsTrigo.chbevl(x, coef, N);
    }

    public long pgcd(long a, long b) {
        return MathsAlgebra.pgcd(a, b);
    }

    public int pgcd(int a, int b) {
        return MathsAlgebra.pgcd(a, b);
    }

    public double[][] toDouble(Complex[][] c, PlotDoubleConverter toDoubleConverter) {
        return MathsArrays.toDouble(c, toDoubleConverter);
    }

    public double[] toDouble(Complex[] c, PlotDoubleConverter toDoubleConverter) {
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
    public int[] rangeCC(double[] orderedValues, double min, double max) {
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
    public int[] rangeCO(double[] orderedValues, double min, double max) {
        return MathsArrays.rangeCO(orderedValues, min, max);
    }

    public Complex csqrt(double d) {
        return Complex.valueOf(d).sqrt();
    }

    public Complex sqrt(Complex d) {
        return d.sqrt();
    }

    public double dsqrt(double d) {
        return Math.sqrt(d);
    }

    public Complex cotanh(Complex c) {
        return c.cotanh();
    }

    public Complex tanh(Complex c) {
        return c.tanh();
    }

    public Complex inv(Complex c) {
        return c.inv();
    }

    public Complex tan(Complex c) {
        return c.tan();
    }

    public Complex cotan(Complex c) {
        return c.cotan();
    }

    public ComplexVector vector(TVector v) {
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

    public Complex pow(Complex a, Complex b) {
        return a.pow(b);
    }

    public Complex div(Complex a, Complex b) {
        return a.div(b);
    }

    public Complex add(Complex a, Complex b) {
        return a.add(b);
    }

    public Complex sub(Complex a, Complex b) {
        return a.sub(b);
    }

    public double norm(ComplexMatrix a) {
        return a.norm(NormStrategy.DEFAULT);
    }

    public double norm(ComplexVector a) {
        return a.norm();
    }

    public double norm1(ComplexMatrix a) {
        return a.norm1();
    }

    public double norm2(ComplexMatrix a) {
        return a.norm2();
    }

    public double norm3(ComplexMatrix a) {
        return a.norm3();
    }

    public double normInf(ComplexMatrix a) {
        return a.normInf();
    }

    public DoubleToComplex complex(DoubleToDouble fx) {
        if (fx.isZero()) {
            return DCZERO;
        }
        if (fx instanceof DoubleToComplex) {
            return (DoubleToComplex) fx;
        }
        return DD2DC.valueOf(fx);
    }

    public DoubleToComplex complex(DoubleToDouble fx, DoubleToDouble fy) {
        return DD2DC.valueOf(fx, fy);
    }

    public double randomDouble(double value) {
        return value * Math.random();
    }

    public double randomDouble(double min, double max) {
        return min + ((max - min) * Math.random());
    }

    public int randomInt(int value) {
        return (int) (value * Math.random());
    }

    public int randomInt(int min, int max) {
        return (int) (min + ((max - min) * Math.random()));
    }

    public Complex randomComplex() {
        double r = Math.random();
        double p = Math.random() * 2 * PI;
        return Complex.valueOf(r * Math.cos(r), r * sin(p));
    }

    public boolean randomBoolean() {
        return Math.random() <= 0.5;
    }

    public double[][] cross(double[] x, double[] y) {
        return MathsArrays.cross(x, y);
    }

    public double[][] cross(double[] x, double[] y, double[] z) {
        return MathsArrays.cross(x, y, z);
    }

    public double[][] cross(double[] x, double[] y, double[] z, Double3Filter filter) {
        return MathsArrays.cross(x, y, z, filter);
    }

    public double[][] cross(double[] x, double[] y, Double2Filter filter) {
        return MathsArrays.cross(x, y, filter);
    }

    public int[][] cross(int[] x, int[] y) {
        return MathsArrays.cross(x, y);
    }

    public int[][] cross(int[] x, int[] y, int[] z) {
        return MathsArrays.cross(x, y, z);
    }

    public TVector sineSeq(String borders, int m, int n, Domain domain) {
        return sineSeq(borders, m, n, domain, PlaneAxis.XY);
    }

    public TVector sineSeq(String borders, int m, int n, Domain domain, PlaneAxis plane) {
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

    public Expr sineSeq(String borders, DoubleParam m, DoubleParam n, Domain domain) {
        return new SinSeqXY(borders, m, n, domain);
    }

    public Expr rooftop(String borders, int nx, int ny, Domain domain) {
        return new Rooftop(borders, nx, ny, domain);
    }

    public Any any(double e) {
        return any(expr(e));
    }

    public Any any(Expr e) {
        return Any.wrap(e);
    }

    public Any any(Double e) {
        return any(expr(e));
    }

    public TVector<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax, Int2Filter filter) {
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

    public TVector<Expr> seq(Expr pattern, DoubleParam m, int max, IntFilter filter) {
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

    public TVector<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax, DoubleParam p, int pmax, Int3Filter filter) {
        double[][] cross = cross(dsteps(0, mmax), dsteps(0, nmax), dsteps(0, pmax), filter == null ? null : new Double3Filter() {
            @Override
            public boolean accept(double a, double b, double c) {
                return filter.accept((int) a, (int) b, (int) c);
            }
        });
        return seq(pattern, m, n, p, cross);
    }

    public TVector<Expr> seq(Expr pattern, DoubleParam m, int mmax, DoubleParam n, int nmax) {
        return seq(pattern, m, n, cross(dsteps(0, mmax), dsteps(0, nmax)));
    }

    public TVector<Expr> seq(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues) {
        return seq(pattern, m, n, cross(mvalues, nvalues));
    }

    public TVector<Expr> seq(final Expr pattern, final DoubleParam m, final DoubleParam n, final double[][] values) {
        return Config.getExprSequenceFactory().newUnmodifiableSequence(values.length, new SimpleSeq2(values, m, n, pattern));
    }

    public TVector<Expr> seq(final Expr pattern, final DoubleParam m, final DoubleParam n, final DoubleParam p, final double[][] values) {
        return Config.getExprSequenceFactory().newUnmodifiableSequence(values.length,
                new SimpleSeqMulti(pattern, new DoubleParam[]{m, n, p}, values)
        );
    }

    public TVector<Expr> seq(final Expr pattern, final DoubleParam[] m, final double[][] values) {
        return Config.getExprSequenceFactory().newUnmodifiableSequence(values.length, new SimpleSeqMulti(pattern, m, values));
    }

    public TVector<Expr> seq(final Expr pattern, final DoubleParam m, int min, int max) {
        return seq(pattern, m, dsteps(min, max, 1));
    }

    public TVector<Expr> seq(final Expr pattern, final DoubleParam m, final double[] values) {
        return Config.getExprSequenceFactory().newUnmodifiableSequence(values.length, new SimpleSeq1(values, m, pattern));
    }

    public ExprMatrix matrix(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues) {
        return MathsBase.matrix(pattern, m, mvalues, n, nvalues);
    }

    public ExprCube cube(final Expr pattern, final DoubleParam m, final double[] mvalues, final DoubleParam n, final double[] nvalues, final DoubleParam p, final double[] pvalues) {
        return Config.getExprCubeFactory().newUnmodifiableCube(mvalues.length, nvalues.length, pvalues.length, new SimpleSeq3b(pattern, m, mvalues, n, nvalues, p, pvalues));
    }

    public Expr derive(Expr f, Axis axis) {
        return Config.getFunctionDerivatorManager().derive(f, axis).simplify();
    }

    public boolean isReal(Expr e) {
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

    public boolean isImag(Expr e) {
//        if (e.isZero()) {
//            return false;
//        }
        return !isReal(e);
    }

    public Expr abs(Expr e) {
        return EXPR_VECTOR_SPACE.abs(e);
    }

    public Expr db(Expr e) {
        return EXPR_VECTOR_SPACE.db(e);
    }

    public Expr db2(Expr e) {
        return EXPR_VECTOR_SPACE.db2(e);
    }

    public Complex complex(int e) {
        return Complex.valueOf(e);
    }

    public Complex complex(double e) {
        return Complex.valueOf(e);
    }

    public Complex complex(double a, double b) {
        return Complex.valueOf(a, b);
    }

    public double Double(Expr e) {
        return e.simplify().toDouble();
    }

    public Expr real(Expr e) {
        return EXPR_VECTOR_SPACE.real(e);
    }

    public Expr imag(Expr e) {
        return EXPR_VECTOR_SPACE.imag(e);
    }

    public Complex Complex(Expr e) {
        return e.simplify().toComplex();
    }

    public Complex complex(Expr e) {
        return e.simplify().toComplex();
    }

    public double doubleValue(Expr e) {
        return e.simplify().toDouble();
    }

    public Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3) {
        return Discrete.create(domain, model, x, y, z, dx, dy, dz, axis1, axis2, axis3);
    }

    public Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz) {
        return Discrete.create(domain, model, x, y, z, dx, dy, dz);
    }

    public Discrete discrete(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z) {
        return Discrete.create(domain, model, x, y, z);
    }

    public Discrete discrete(Domain domain, Complex[][][] model, double dx, double dy, double dz) {
        return Discrete.create(domain, model, dx, dy, dz);
    }

    public Discrete discrete(Complex[][][] model, double[] x, double[] y, double[] z) {
        return Discrete.create(model, x, y, z);
    }

    public Discrete discrete(Complex[][] model, double[] x, double[] y) {
        return Discrete.create(model, x, y);
    }

    public Expr discrete(Expr expr, double[] xsamples, double[] ysamples, double[] zsamples) {
        return discrete(expr, Samples.absolute(xsamples, ysamples, zsamples));
    }

    public Expr discrete(Expr expr, Samples samples) {
        return MathsSampler.discrete(expr, samples);
    }

    public Expr abssqr(Expr e) {
        return getVectorSpace($EXPR).abssqr(e);
    }

    public AdaptiveResult1 adaptiveEval(Expr expr, AdaptiveConfig config) {
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

    public <T> AdaptiveResult1 adaptiveEval(AdaptiveFunction1<T> expr, DistanceStrategy<T> distance, DomainX domain, AdaptiveConfig config) {
        return MathsSampler.adaptiveEval(expr, distance, domain, config);
    }

    public Discrete discrete(Expr expr) {
        return MathsSampler.discrete(expr);
    }

    public VDiscrete vdiscrete(Expr expr) {
        return MathsSampler.vdiscrete(expr);
    }

    public Expr discrete(Expr expr, int xSamples) {
        return MathsSampler.discrete(expr, xSamples);
    }

    public Expr discrete(Expr expr, int xSamples, int ySamples) {
        return MathsSampler.discrete(expr, xSamples, ySamples);
    }

    public Expr discrete(Expr expr, int xSamples, int ySamples, int zSamples) {
        return MathsSampler.discrete(expr, xSamples, ySamples, zSamples);
    }

    public AxisFunction axis(Axis e) {
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

    public Expr transformAxis(Expr e, AxisFunction a1, AxisFunction a2, AxisFunction a3) {
        return transformAxis(e, a1.getAxis(), a2.getAxis(), a3.getAxis());
    }

    public Expr transformAxis(Expr e, Axis a1, Axis a2, Axis a3) {
        return new AxisTransform(e, new Axis[]{a1, a2, a3}, 3);
    }

    public Expr transformAxis(Expr e, AxisFunction a1, AxisFunction a2) {
        return transformAxis(e, a1.getAxis(), a2.getAxis());
    }

    public Expr transformAxis(Expr e, Axis a1, Axis a2) {
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

    public double sin2(double d) {
        return MathsTrigo.sin2(d);
    }

    public double cos2(double d) {
        return MathsTrigo.cos2(d);
    }

    public boolean isInt(double d) {
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
    public <T> T lcast(Object o, Class<T> type) {
        return o == null ? null : type.isInstance(o) ? (T) o : null;
    }

    /**
     * return a string representation (dump) of the given object. dump is
     * evaluated as follows : first
     *
     * @param o object to dump
     * @return string dump of the object
     */
    public String dump(Object o) {
        return Config.getDumpManager().getDumpDelegate(o, false).getDumpString(o);
    }

    public String dumpSimple(Object o) {
        return Config.getDumpManager().getDumpDelegate(o, true).getDumpString(o);
    }

    public DoubleToDouble expr(double value, Geometry geometry) {
        if (geometry == null) {
            geometry = Domain.FULLXY.toGeometry();
        }
        if (geometry.isRectangular()) {
            return DoubleValue.valueOf(value, geometry.getDomain());
        }
        return new Shape2D(value, geometry);
    }

    public DoubleToDouble expr(double value, Domain geometry) {
        if (geometry == null) {
            geometry = Domain.FULLXY;
        }
//        return geometry.toDD();
        if (geometry.isRectangular()) {
            return DoubleValue.valueOf(value, geometry.getDomain());
        }
        return new Shape2D(value, geometry);
    }

    public DoubleToDouble expr(Domain domain) {
        return domain.toDD();
    }

    public DoubleToDouble expr(Geometry domain) {
        return expr(1, domain);
    }

    public Expr expr(Complex a, Geometry geometry) {
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

    public Expr expr(Complex a, Domain geometry) {
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

    public <T extends Expr> TVector<T> preload(TVector<T> list) {
        return DefaultExprSequenceFactory.INSTANCE.newPreloadedSequence(list.length(), list);
    }

    public <T extends Expr> TVector<T> withCache(TVector<T> list) {
        //DefaultExprSequenceFactory.INSTANCE.newCachedSequence(length(), this);
        return DefaultExprSequenceFactory.INSTANCE.newCachedSequence(list.length(), list);
    }

    //    public <T extends Expr> TList<T> simplify(TList<T> list) {
//        //        return DefaultExprSequenceFactory.INSTANCE.newUnmodifiableSequence(length(), new SimplifiedSeq(this));
//        return DefaultExprSequenceFactory.INSTANCE.newUnmodifiableSequence(list.length(), new SimplifiedSeq(list));
//    }
    public <T> TVector<T> abs(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.abs(e);
            }
        });
    }

    public <T> TVector<T> db(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.db(e);
            }
        });
    }

    public <T> TVector<T> db2(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.db2(e);
            }
        });
    }

    public <T> TVector<T> real(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.real(e);
            }
        });
    }

    public <T> TVector<T> imag(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.imag(e);
            }
        });
    }

    public double real(Complex a) {
        return a.getReal();
    }

    public double imag(Complex a) {
        return a.getImag();
    }

    public boolean almostEqualRelative(float a, float b, float maxRelativeError) {
        if (a == b) {
            return true;
        }
        float relativeError = b == 0 ? a : ((a - b) / b);
        if (relativeError < 0) {
            relativeError = 0.0F - relativeError;
        }
        return relativeError <= maxRelativeError;
    }

    public boolean almostEqualRelative(double a, double b, double maxRelativeError) {
        if (a == b) {
            return true;
        }
        double relativeError = b == 0 ? a : ((a - b) / b);
        if (relativeError < 0) {
            relativeError = 0.0D - relativeError;
        }
        return relativeError <= maxRelativeError;
    }

    public boolean almostEqualRelative(Complex a, Complex b, double maxRelativeError) {
        if (a == b) {
            return true;
        }
        double relativeError = (b.isZero() ? a : ((a.sub(b)).div(b))).norm();
        return relativeError <= maxRelativeError;
    }

    public DoubleArrayParamSet dtimes(Param param, double min, double max, int times) {
        return new DoubleArrayParamSet(param, min, max, times);
    }

    public DoubleArrayParamSet dsteps(Param param, double min, double max, double step) {
        return new DoubleArrayParamSet(param, min, max, step);
    }

    public IntArrayParamSet itimes(Param param, int min, int max, int times) {
        return new IntArrayParamSet(param, min, max, times);
    }

    public IntArrayParamSet isteps(Param param, int min, int max, int step) {
        return new IntArrayParamSet(param, min, max, (double) step);
    }

    public FloatArrayParamSet ftimes(Param param, int min, int max, int times) {
        return new FloatArrayParamSet(param, min, max, times);
    }

    public FloatArrayParamSet fsteps(Param param, int min, int max, int step) {
        return new FloatArrayParamSet(param, min, max, (float) step);
    }

    public LongArrayParamSet ltimes(Param param, int min, int max, int times) {
        return new LongArrayParamSet(param, min, max, times);
    }

    public LongArrayParamSet lsteps(Param param, int min, int max, long step) {
        return new LongArrayParamSet(param, min, max, step);
    }

    public ComplexVector sin(ComplexVector v) {
        return v.sin();
    }

    public ComplexVector cos(ComplexVector v) {
        return v.cos();
    }

    public ComplexVector tan(ComplexVector v) {
        return v.tan();
    }

    public ComplexVector cotan(ComplexVector v) {
        return v.cotan();
    }

    public ComplexVector tanh(ComplexVector v) {
        return v.tanh();
    }

    public ComplexVector cotanh(ComplexVector v) {
        return v.cotanh();
    }

    public ComplexVector cosh(ComplexVector v) {
        return v.cosh();
    }

    public ComplexVector sinh(ComplexVector v) {
        return v.sinh();
    }

    public ComplexVector log(ComplexVector v) {
        return v.log();
    }

    public ComplexVector log10(ComplexVector v) {
        return v.log10();
    }

    public ComplexVector db(ComplexVector v) {
        return v.db();
    }

    public ComplexVector exp(ComplexVector v) {
        return v.exp();
    }

    public ComplexVector acosh(ComplexVector v) {
        return v.acosh();
    }

    public ComplexVector acos(ComplexVector v) {
        return v.acos();
    }

    public ComplexVector asinh(ComplexVector v) {
        return v.asinh();
    }

    public ComplexVector asin(ComplexVector v) {
        return v.asin();
    }

    public ComplexVector atan(ComplexVector v) {
        return v.atan();
    }

    public ComplexVector acotan(ComplexVector v) {
        return v.acotan();
    }

    public ComplexVector imag(ComplexVector v) {
        return v.imag();
    }

    public ComplexVector real(ComplexVector v) {
        return v.real();
    }

    public ComplexVector abs(ComplexVector v) {
        return v.abs();
    }

    public Complex[] abs(Complex[] v) {
        Complex[] r = new Complex[v.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = (v[i].abs());
        }
        return r;
    }

    public Complex avg(ComplexVector v) {
        return v.avg();
    }

    public Complex sum(ComplexVector v) {
        return v.sum();
    }

    public Complex prod(ComplexVector v) {
        return v.prod();
    }

    public ComplexMatrix abs(ComplexMatrix v) {
        return v.abs();
    }

    public ComplexMatrix sin(ComplexMatrix v) {
        return v.sin();
    }

    public ComplexMatrix cos(ComplexMatrix v) {
        return v.cos();
    }

    public ComplexMatrix tan(ComplexMatrix v) {
        return v.tan();
    }

    public ComplexMatrix cotan(ComplexMatrix v) {
        return v.cotan();
    }

    public ComplexMatrix tanh(ComplexMatrix v) {
        return v.tanh();
    }

    public ComplexMatrix cotanh(ComplexMatrix v) {
        return v.cotanh();
    }

    public ComplexMatrix cosh(ComplexMatrix v) {
        return v.cosh();
    }

    public ComplexMatrix sinh(ComplexMatrix v) {
        return v.sinh();
    }

    public ComplexMatrix log(ComplexMatrix v) {
        return v.log();
    }

    public ComplexMatrix log10(ComplexMatrix v) {
        return v.log10();
    }

    public ComplexMatrix db(ComplexMatrix v) {
        return v.db();
    }

    public ComplexMatrix exp(ComplexMatrix v) {
        return v.exp();
    }

    //
    public ComplexMatrix acosh(ComplexMatrix v) {
        return v.acosh();
    }

    public ComplexMatrix acos(ComplexMatrix v) {
        return v.acos();
    }

    public ComplexMatrix asinh(ComplexMatrix v) {
        return v.asinh();
    }

    public ComplexMatrix asin(ComplexMatrix v) {
        return v.asin();
    }

    public ComplexMatrix atan(ComplexMatrix v) {
        return v.atan();
    }

    public ComplexMatrix acotan(ComplexMatrix v) {
        return v.acotan();
    }

    public ComplexMatrix imag(ComplexMatrix v) {
        return v.imag();
    }

    public ComplexMatrix real(ComplexMatrix v) {
        return v.real();
    }

    public Complex[] real(Complex[] v) {
        Complex[] values = new Complex[v.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = (v[i].real());
        }
        return values;
    }

    public double[] realdbl(Complex[] v) {
        double[] values = new double[v.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = v[i].realdbl();
        }
        return values;
    }

    public Complex[] imag(Complex[] v) {
        Complex[] values = new Complex[v.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = (v[i].imag());
        }
        return values;
    }

    public double[] imagdbl(Complex[] v) {
        double[] values = new double[v.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = v[i].imagdbl();
        }
        return values;
    }

    public Complex avg(ComplexMatrix v) {
        return v.avg();
    }

    public Complex sum(ComplexMatrix v) {
        return v.sum();
    }

    public Complex prod(ComplexMatrix v) {
        return v.prod();
    }

    public boolean roundEquals(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    public double round(double val, double precision) {
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
    public double sqrt(double v, int n) {
        return n == 0 ? 1 : pow(v, 1.0 / n);
    }

    public double pow(double v, double n) {
        return Math.pow(v, n);
    }

    public double db(double x) {
        return 10 * Math.log10(x);
    }

    public double acosh(double x) {
        return Math.log(x + Math.sqrt(x * x - 1));
    }

    public double atanh(double x) {
        return Math.log((1 + x) / (1 - x)) / 2;
    }

    public double acotanh(double x) {
        if (true) {
            throw new IllegalArgumentException("TODO : Check me");
        }
        return 1 / atanh(1 / x);
    }

    public double asinh(double x) {
        if (x == Double.NEGATIVE_INFINITY) {
            return x;
        } else {
            return Math.log(x + Math.sqrt(x * x + 1));
        }
    }

    public double db2(double nbr) {
        return 20 * Math.log10(nbr);
    }

    public double sqrt(double nbr) {
        return Math.sqrt(nbr);
    }

    /**
     * erturn 1/x
     *
     * @param x
     * @return
     */
    public double inv(double x) {
        return 1.0 / x;
    }

    /**
     * return x
     *
     * @param x
     * @return
     */
    public double conj(double x) {
        return x;
    }
//</editor-fold>

    /////////////////////////////////////////////////////////////////
    // double[] functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="double[] functions">
    public double[] sin2(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = sin2(x[i]);
        }
        return y;
    }

    public double[] cos2(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = cos2(x[i]);
        }
        return y;
    }

    public double[] sin(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.sin(x[i]);
        }
        return y;
    }

    public double[] cos(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.cos(x[i]);
        }
        return y;
    }

    public double[] tan(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.tan(x[i]);
        }
        return y;
    }

    public double[] cotan(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = 1 / Math.tan(x[i]);
        }
        return y;
    }

    public double[] sinh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.sinh(x[i]);
        }
        return y;
    }

    public double[] cosh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.cosh(x[i]);
        }
        return y;
    }

    public double[] tanh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.tanh(x[i]);
        }
        return y;
    }

    public double[] cotanh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = 1 / Math.tanh(x[i]);
        }
        return y;
    }

    public double max(double a, double b) {
        return a > b ? a : b;
    }

    public int max(int a, int b) {
        return a > b ? a : b;
    }

    public long max(long a, long b) {
        return a > b ? a : b;
    }

    public double min(double a, double b) {
        return a < b ? a : b;
    }

    public double min(double[] arr) {
        return MathsArrays.min(arr);
    }

    public double max(double[] arr) {
        return MathsArrays.max(arr);
    }

    public double avg(double[] arr) {
        return MathsArrays.avg(arr);
    }

    public int min(int a, int b) {
        return a < b ? a : b;
    }

    public Complex min(Complex a, Complex b) {
        int i = a.compareTo(b);
        return i <= 0 ? a : b;
    }

    public Complex max(Complex a, Complex b) {
        int i = a.compareTo(b);
        return i >= 0 ? a : b;
    }

    public long min(long a, long b) {
        return a < b ? a : b;
    }

    public double[] minMax(double[] a) {
        return MathsArrays.minMax(a);
    }

    public double[] minMaxAbs(double[] a) {
        return MathsArrays.minMaxAbs(a);
    }

    public double[] minMaxAbsNonInfinite(double[] a) {
        return MathsArrays.minMaxAbsNonInfinite(a);
    }

    public double avgAbs(double[] arr) {
        return MathsArrays.avgAbs(arr);
    }

    public double[] distances(double[] arr) {
        return MathsArrays.distances(arr);
    }

    public double[] div(double[] a, double[] b) {
        int max = Math.max(a.length, b.length);
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] / b[i];
        }
        return ret;
    }

    public double[] mul(double[] a, double[] b) {
        int max = Math.max(a.length, b.length);
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] * b[i];
        }
        return ret;
    }

    public double[] sub(double[] a, double[] b) {
        int max = Math.max(a.length, b.length);
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] - b[i];
        }
        return ret;
    }

    public double[] sub(double[] a, double b) {
        int max = a.length;
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] - b;
        }
        return ret;
    }

    public double[] add(double[] a, double[] b) {
        int max = Math.max(a.length, b.length);
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] + b[i];
        }
        return ret;
    }

    public double[] db(double[] a) {
        double[] ret = new double[a.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = db(a[i]);
        }
        return ret;
    }

    public double[][] sin(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = sin(r[i]);
        }
        return r;
    }

    public double[][] sin2(double[][] c) {
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
    public double sin(double x) {
        return Math.sin(x);
    }

    public double cos(double x) {
        return Math.cos(x);
    }

    public double tan(double x) {
        return Math.tan(x);
    }

    public double cotan(double x) {
        return 1 / Math.tan(x);
    }

    public double sinh(double x) {
        return Math.sinh(x);
//        return (Math.exp(x) - Math.exp(-x)) / 2;
    }

    public double cosh(double x) {
        return Math.cosh(x);
//        return (Math.exp(x) + Math.exp(-x)) / 2;
    }

    public double tanh(double x) {
        return Math.tanh(x);
//        double a = Math.exp(+x);
//        double b = Math.exp(-x);
//        return a == Double.POSITIVE_INFINITY ? 1 : b == Double.POSITIVE_INFINITY ? -1 : (a - b) / (a + b);
    }

    public double abs(double a) {
        return Math.abs(a);
    }

    public int abs(int a) {
        return Math.abs(a);
    }

    public double cotanh(double x) {
        return 1 / Math.tanh(x);
    }

    public double acos(double x) {
        return Math.acos(x);
    }

    public double asin(double x) {
        return Math.asin(x);
    }

    public double atan(double x) {
        return Math.atan(x);
    }
    //</editor-fold>

    /////////////////////////////////////////////////////////////////
    // double[] functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="double[] functions">
    public double sum(double... c) {
        double x = 0;
        for (int i = 0; i < c.length; i++) {
            x += c[i];
        }
        return x;
    }

    public double[] mul(double[] a, double b) {
        int max = a.length;
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] * b;
        }
        return ret;
    }

    public double[] mulSelf(double[] x, double v) {
        for (int i = 0; i < x.length; i++) {
            x[i] = x[i] * v;
        }
        return x;
    }

    public double[] div(double[] a, double b) {
        int max = a.length;
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] / b;
        }
        return ret;
    }

    public double[] divSelf(double[] x, double v) {
        for (int i = 0; i < x.length; i++) {
            x[i] = x[i] / v;
        }
        return x;
    }

    public double[] add(double[] x, double v) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = x[i] + v;
        }
        return y;
    }

    public double[] addSelf(double[] x, double v) {
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
    public double[][] cos(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = cos(r[i]);
        }
        return r;
    }

    public double[][] tan(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = tan(r[i]);
        }
        return r;
    }

    public double[][] cotan(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = cotan(r[i]);
        }
        return r;
    }

    public double[][] sinh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = sinh(r[i]);
        }
        return r;
    }

    public double[][] cosh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = cosh(r[i]);
        }
        return r;
    }

    public double[][] tanh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = tanh(r[i]);
        }
        return r;
    }

    public double[][] cotanh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = tanh(r[i]);
        }
        return r;
    }

    public double[][] add(double[][] a, double[][] b) {
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

    public double[][] sub(double[][] a, double[][] b) {
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

    public double[][] div(double[][] a, double[][] b) {
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

    public double[][] mul(double[][] a, double[][] b) {
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

    public double[][] db(double[][] a) {
        double[][] ret = new double[a.length][];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new double[a[i].length];
            for (int j = 0; j < ret[i].length; j++) {
                ret[i][j] = db(a[i][j]);
            }
        }
        return ret;
    }

    public double[][] db2(double[][] a) {
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
    public Expr If(Expr cond, Expr exp1, Expr exp2) {
        return EXPR_VECTOR_SPACE.If(cond, exp1, exp2);
    }

    public Expr or(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.or(a, b);
    }

    public Expr and(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.and(a, b);
    }

    public Expr not(Expr a) {
        return EXPR_VECTOR_SPACE.not(a);
    }

    public Expr eq(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.eq(a, b);
    }

    public Expr ne(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.ne(a, b);
    }

    public Expr gte(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.gte(a, b);
    }

    public Expr gt(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.gt(a, b);
    }

    public Expr lte(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.lte(a, b);
    }

    public Expr lt(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.lt(a, b);
    }

    public Expr cos(Expr e) {
        return EXPR_VECTOR_SPACE.cos(e);
    }

    public Expr cosh(Expr e) {
        return EXPR_VECTOR_SPACE.cosh(e);
    }

    public Expr sin(Expr e) {
        return EXPR_VECTOR_SPACE.sin(e);
    }

    public Expr sincard(Expr e) {
        return EXPR_VECTOR_SPACE.sincard(e);
    }

    public Expr sinh(Expr e) {
        return EXPR_VECTOR_SPACE.sinh(e);
    }

    public Expr tan(Expr e) {
        return EXPR_VECTOR_SPACE.tan(e);
    }

    public Expr tanh(Expr e) {
        return EXPR_VECTOR_SPACE.tanh(e);
    }

    public Expr cotan(Expr e) {
        return EXPR_VECTOR_SPACE.cotan(e);
    }

    public Expr cotanh(Expr e) {
        return EXPR_VECTOR_SPACE.cotanh(e);
    }

    public Expr sqr(Expr e) {
        return EXPR_VECTOR_SPACE.sqr(e);
    }

    public Expr sqrt(Expr e) {
        return EXPR_VECTOR_SPACE.sqrt(e);
    }

    public Expr inv(Expr e) {
        return EXPR_VECTOR_SPACE.inv(e);
    }

    public Expr neg(Expr e) {
        return EXPR_VECTOR_SPACE.neg(e);
    }

    public Expr exp(Expr e) {
        return EXPR_VECTOR_SPACE.exp(e);
    }

    public Expr atan(Expr e) {
        return EXPR_VECTOR_SPACE.atan(e);
    }

    public Expr acotan(Expr e) {
        return EXPR_VECTOR_SPACE.acotan(e);
    }

    public Expr acos(Expr e) {
        return EXPR_VECTOR_SPACE.acos(e);
    }

    public Expr asin(Expr e) {
        return EXPR_VECTOR_SPACE.asin(e);
    }

    public Complex integrate(Expr e) {
        return integrate(e, null);
    }

    public Complex integrate(Expr e, Domain domain) {
        return Config.getIntegrationOperator().eval(domain, e);
    }

    public Expr esum(int size, TVectorCell<Expr> f) {
        return MathsBase.esum(seq(size, f));
    }

    public Expr esum(int size1, int size2, TMatrixCell<Expr> e) {
        RepeatableOp<Expr> c = EXPR_VECTOR_SPACE.addRepeatableOp();
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                c.append(e.get(i, j));
            }
        }
        return c.eval();
    }

    public Complex csum(int size1, int size2, TMatrixCell<Complex> e) {
        MutableComplex c = new MutableComplex();
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                c.add(e.get(i, j));
            }
        }
        return c.toComplex();
    }

    public TVector<Expr> seq(int size1, TVectorCell<Expr> f) {
        return new ReadOnlyTList<Expr>($EXPR, false, new TVectorModelFromCell(size1, f));
    }

    public TVector<Expr> seq(int size1, int size2, TMatrixCell<Expr> f) {
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

    private TMatrix<Complex> resolveBestScalarProductCache(Expr[] gp, Expr[] fn, AxisXY axis, ScalarProductOperator sp, ProgressMonitor monitor) {
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

    public TMatrix<Complex> scalarProductCache(Expr[] gp, Expr[] fn, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, AxisXY.XY, Config.getScalarProductOperator(), monitor);
    }

    public TMatrix<Complex> scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, AxisXY.XY, Config.getScalarProductOperator(), monitor);
    }

    public TMatrix<Complex> scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, AxisXY axis, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, axis, sp, monitor);
    }

    public TMatrix<Complex> scalarProductCache(Expr[] gp, Expr[] fn, AxisXY axis, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, axis, Config.getScalarProductOperator(), monitor);
    }

    public Expr gate(Axis axis, double a, double b) {
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

    public Expr gate(Expr axis, double a, double b) {
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

    public Expr gateX(double a, double b) {
        return DoubleValue.valueOf(1, Domain.forBounds(a, b));
    }

    public Expr gateY(double a, double b) {
        return DoubleValue.valueOf(1, Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, a, b));
    }

    public Expr gateZ(double a, double b) {
        return DoubleValue.valueOf(1, Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, a, b));
    }

    public double scalarProduct(DoubleToDouble f1, DoubleToDouble f2) {
        return Config.getScalarProductOperator().evalDD(null, f1, f2);
    }

    public ComplexVector scalarProduct(Expr f1, TVector<Expr> f2) {
        TVectorCell<Complex> spfact = new TVectorCell<Complex>() {
            @Override
            public Complex get(int index) {
                return scalarProduct(f1, f2.get(index));
            }
        };
        return f2.isColumn() ? columnVector(f2.size(), spfact) : rowVector(f2.size(), spfact);
    }

    public ComplexMatrix scalarProduct(Expr f1, TMatrix<Expr> f2) {
        return matrix(f2.getRowCount(), f2.getColumnCount(), new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return scalarProduct(f1, f2.get(row, column));
            }
        });
    }

    public ComplexVector scalarProduct(TVector<Expr> f2, Expr f1) {
        TVectorCell<Complex> spfact = new TVectorCell<Complex>() {
            @Override
            public Complex get(int index) {
                return scalarProduct(f2.get(index), f1);
            }
        };
        return f2.isColumn() ? columnVector(f2.size(), spfact) : rowVector(f2.size(), spfact);
    }

    public ComplexMatrix scalarProduct(TMatrix<Expr> f2, Expr f1) {
        return matrix(f2.getRowCount(), f2.getColumnCount(), new TMatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return scalarProduct(f2.get(row, column), f1);
            }
        });
    }

    public Complex scalarProduct(Domain domain, Expr f1, Expr f2) {
        return Config.getScalarProductOperator().eval(domain, f1, f2);
    }

    public Complex scalarProduct(Expr f1, Expr f2) {
        return Config.getScalarProductOperator().eval(f1, f2);
    }

    public ComplexMatrix scalarProductMatrix(TVector<Expr> g, TVector<Expr> f) {
        return matrix(Config.getScalarProductOperator().eval(g, f, null));
    }

    public TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f) {
        return Config.getScalarProductOperator().eval(g, f, null);
    }

    public TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, monitor);
    }

    public ComplexMatrix scalarProductMatrix(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return matrix(Config.getScalarProductOperator().eval(g, f, monitor));
    }

    public TMatrix<Complex> scalarProduct(TVector<Expr> g, TVector<Expr> f, AxisXY axis, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, axis, monitor);
    }

    public ComplexMatrix scalarProductMatrix(Expr[] g, Expr[] f) {
        return (ComplexMatrix) Config.getScalarProductOperator().eval(g, f, null).to($COMPLEX);
    }

    public Complex scalarProduct(ComplexMatrix g, ComplexMatrix f) {
        return g.scalarProduct(f);
    }

    public Expr scalarProduct(ComplexMatrix g, TVector<Expr> f) {
        return f.scalarProduct(g.to($EXPR));
    }

    public Expr scalarProductAll(ComplexMatrix g, TVector<Expr>... f) {
        return g.toVector().to($EXPR).scalarProductAll((TVector[]) f);
    }

    public TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f) {
        return Config.getScalarProductOperator().eval(g, f, null);
    }

    public TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, monitor);
    }

    public ComplexMatrix scalarProductMatrix(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return matrix(Config.getScalarProductOperator().eval(g, f, monitor));
    }

    public TMatrix<Complex> scalarProduct(Expr[] g, Expr[] f, AxisXY axis, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, axis, monitor);
    }

    //    public String scalarProductToMatlabString(DFunctionXY f1, DFunctionXY f2, DomainXY domain0, ToMatlabStringParam... format) {
//        return defaultScalarProduct.scalarProductToMatlabString(domain0, f1, f2, format) ;
//    }
//
//    public String scalarProductToMatlabString(DomainXY domain0,CFunctionXY f1, CFunctionXY f2, ToMatlabStringParam... format) {
//        return defaultScalarProduct.scalarProductToMatlabString(domain0, f1, f2, format) ;
//    }
    public ExprVector elist(int size) {
        return new ArrayExprVector(false, size);
    }

    public ExprVector elist(boolean row, int size) {
        return new ArrayExprVector(row, size);
    }

    public ExprVector elist(Expr... vector) {
        return new ArrayExprVector(false, vector);
    }

    public ExprVector elist(TVector<Complex> vector) {
        return new ArrayExprVector(false, vector);
    }

    public TVector<Complex> clist(TVector<Expr> vector) {
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

    public TVector<Complex> clist() {
        return new ArrayTVector<>($COMPLEX, false, 0);
    }

    public TVector<Complex> clist(int size) {
        return new ArrayTVector<>($COMPLEX, false, size);
    }

    public TVector<Complex> clist(Complex... vector) {
        return new ArrayTVector<>($COMPLEX, false, vector);
    }

    public TVector<ComplexMatrix> mlist() {
        return MathsBase.list($MATRIX, false, 0);
    }

    public TVector<ComplexMatrix> mlist(int size) {
        return MathsBase.list($MATRIX, false, size);
    }

    public TVector<ComplexMatrix> mlist(ComplexMatrix... items) {
        TVector<ComplexMatrix> list = MathsBase.list($MATRIX, false, items.length);
        list.appendAll(Arrays.asList(items));
        return list;
    }

    public TVector<TVector<Complex>> clist2() {
        return list($CLIST, false, 0);
    }

    public TVector<TVector<Expr>> elist2() {
        return MathsBase.list($ELIST, false, 0);
    }

    public TVector<TVector<Double>> dlist2() {
        return MathsBase.list($DLIST, false, 0);
    }

    public TVector<TVector<Integer>> ilist2() {
        return MathsBase.list($ILIST, false, 0);
    }

    public TVector<TVector<ComplexMatrix>> mlist2() {
        return MathsBase.list($MLIST, false, 0);
    }

    public TVector<TVector<Boolean>> blist2() {
        return list($BLIST, false, 0);
    }

    public <T> TVector<T> list(TypeName<T> type) {
        return list(type, false, 0);
    }

    public <T> TVector<T> list(TypeName<T> type, int initialSize) {
        return list(type, false, initialSize);
    }

    public <T> TVector<T> listro(TypeName<T> type, boolean row, TVectorModel<T> model) {
        if (type.equals(MathsBase.$DOUBLE)) {
            return (TVector<T>) new ArrayDoubleVector.ReadOnlyDoubleVector(row, (TVectorModel<Double>) model);
        }
        if (type.equals(MathsBase.$INTEGER)) {
            return (TVector<T>) new ArrayIntVector.ReadOnlyIntVector(row, (TVectorModel<Integer>) model);
        }
        if (type.equals(MathsBase.$LONG)) {
            return (TVector<T>) new ArrayLongVector.ReadOnlyLongVector(row, (TVectorModel<Long>) model);
        }
        if (type.equals(MathsBase.$BOOLEAN)) {
            return (TVector<T>) new ArrayBooleanVector.ReadOnlyBooleanVector(row, (TVectorModel<Boolean>) model);
        }
        return new ReadOnlyTList<T>(type, row, model);
    }

    public <T> TVector<T> list(TypeName<T> type, boolean row, int initialSize) {
        if (type.equals($EXPR)) {
            return (TVector<T>) elist(row, initialSize);
        }
        if (type.equals($DOUBLE)) {
            return (TVector<T>) dlist(row, initialSize);
        }
        if (type.equals($INTEGER)) {
            return (TVector<T>) ilist(row, initialSize);
        }
        if (type.equals($LONG)) {
            return (TVector<T>) llist(row, initialSize);
        }
        if (type.equals($BOOLEAN)) {
            return (TVector<T>) blist(row, initialSize);
        }
        return new ArrayTVector<T>(type, row, initialSize);
    }

    public <T> TVector<T> list(TVector<T> vector) {
        TVector<T> exprs = list(vector.getComponentType());
        for (T o : vector) {
            exprs.append(o);
        }
        return exprs;
    }

    public ExprVector elist(ComplexMatrix vector) {
        ComplexVector complexes = vector.toVector();
        ExprVector exprs = elist(complexes.size());
        exprs.appendAll((TVector) complexes);
        return exprs;
    }

    public <T> TVector<T> vscalarProduct(TVector<T> vector, TVector<TVector<T>> vectors) {
        return vector.vscalarProduct(vectors.toArray(new TVector[0]));
    }

    public ExprVector elist() {
        return new ArrayExprVector(false, 0);
    }

    public <T> TVector<T> concat(TVector<T>... a) {
        TVector<T> ts = list(a[0].getComponentType());
        for (TVector<T> t : a) {
            ts.appendAll(t);
        }
        return ts;
    }

    public TVector<Double> dlist() {
        return new ArrayDoubleVector();
    }

    public TVector<Double> dlist(ToDoubleArrayAware items) {
        return dlist(items.toDoubleArray());
    }

    public TVector<Double> dlist(double[] items) {
        DoubleVector doubles = new ArrayDoubleVector(items.length);
        doubles.appendAll(items);
        return doubles;
    }

    public TVector<Double> dlist(boolean row, int size) {
        return new ArrayDoubleVector(row, size);
    }

    public TVector<Double> dlist(int size) {
        return new ArrayDoubleVector(size);
    }

    public TVector<String> slist() {
        return new ArrayTVector<String>($STRING, false, 0);
    }

    public TVector<String> slist(String[] items) {
        TVector<String> doubles = new ArrayTVector<String>($STRING, false, items.length);
        for (String item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public TVector<String> slist(boolean row, int size) {
        return new ArrayTVector<String>($STRING, row, size);
    }

    public TVector<String> slist(int size) {
        return new ArrayTVector<String>($STRING, false, size);
    }

    public TVector<Boolean> blist() {
        return new ArrayBooleanVector(false, 0);
    }

    public TVector<Boolean> dlist(boolean[] items) {
        TVector<Boolean> doubles = new ArrayBooleanVector(false, items.length);
        for (boolean item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public TVector<Boolean> blist(boolean row, int size) {
        return new ArrayBooleanVector(row, size);
    }

    public TVector<Boolean> blist(int size) {
        return new ArrayBooleanVector(false, size);
    }

    public IntVector ilist() {
        return new ArrayIntVector(false, 0);
    }

    public TVector<Integer> ilist(int[] items) {
        TVector<Integer> doubles = new ArrayIntVector(false, items.length);
        for (int item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public TVector<Integer> ilist(int size) {
        return new ArrayIntVector(false, size);
    }

    public TVector<Integer> ilist(boolean row, int size) {
        return new ArrayIntVector(row, size);
    }

    public LongVector llist() {
        return new ArrayLongVector(false, 0);
    }

    public TVector<Long> llist(long[] items) {
        TVector<Long> doubles = new ArrayLongVector(false, items.length);
        for (long item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public TVector<Long> llist(int size) {
        return new ArrayLongVector(false, size);
    }

    public TVector<Long> llist(boolean row, int size) {
        return new ArrayLongVector(row, size);
    }

    public <T> T sum(TypeName<T> type, T... arr) {
        return MathsExpr.sum(type, arr);
    }

    public <T> T sum(TypeName<T> type, TVectorModel<T> arr) {
        return MathsExpr.sum(type, arr);
    }

    public <T> T sum(TypeName<T> type, int size, TVectorCell<T> arr) {
        return MathsExpr.sum(type, size, arr);
    }

    public <T> T mul(TypeName<T> type, T... arr) {
        return MathsExpr.mul(type, arr);
    }

    public <T> T mul(TypeName<T> type, TVectorModel<T> arr) {
        return MathsExpr.mul(type, arr);
    }

    public Complex avg(Discrete d) {
        return MathsSampler.avg(d);
    }

    public DoubleToVector vsum(VDiscrete d) {
        return MathsSampler.vsum(d);
    }

    public DoubleToVector vavg(VDiscrete d) {
        return MathsSampler.vavg(d);
    }

    public Complex avg(VDiscrete d) {
        return MathsSampler.avg(d);
    }

    public Expr sum(Expr... arr) {
        return MathsExpr.sum(arr);
    }

    public Expr esum(TVectorModel<Expr> arr) {
        return MathsExpr.esum(arr);
    }

    public <T> TMatrix<T> mul(TMatrix<T> a, TMatrix<T> b) {
        return a.mul(b);
    }

    public ComplexMatrix mul(ComplexMatrix a, ComplexMatrix b) {
        return a.mul(b);
    }

    public Expr mul(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.mul(a, b);
    }

    public TVector<Expr> edotmul(TVector<Expr>... arr) {
        return MathsExpr.edotmul(arr);
    }

    public TVector<Expr> edotdiv(TVector<Expr>... arr) {
        return MathsExpr.edotdiv(arr);
    }

    public Complex cmul(TVectorModel<Complex> arr) {
        return MathsExpr.cmul(arr);
    }

    public Expr emul(TVectorModel<Expr> arr) {
        return MathsExpr.emul(arr);
    }

    public Expr mul(Expr... e) {
        return MathsExpr.mul(e);
    }

    public Expr pow(Expr a, Expr b) {
        return new Pow(a, b);
    }

    public Expr sub(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.sub(a, b);
    }

    public Expr add(Expr a, double b) {
        return add(a, Complex.valueOf(b));
    }

    public Expr mul(Expr a, double b) {
        return mul(a, Complex.valueOf(b));
    }

    public Expr sub(Expr a, double b) {
        return sub(a, Complex.valueOf(b));
    }

    public Expr div(Expr a, double b) {
        return div(a, Complex.valueOf(b));
    }

    public Expr add(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.add(a, b);
    }

    public Expr add(Expr... a) {
        return MathsExpr.add(a);
    }

    public Expr div(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.div(a, b);
    }

    public Expr rem(Expr a, Expr b) {
        return EXPR_VECTOR_SPACE.rem(a, b);
    }

    public Expr expr(double value) {
        return DoubleValue.valueOf(value, Domain.FULLX);
    }

    public <T> TVector<Expr> expr(TVector<T> vector) {
        return vector.to($EXPR);
    }

    public TMatrix<Expr> expr(TMatrix<Complex> matrix) {
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

    public <T> TMatrix<T> tmatrix(TypeName<T> type, TMatrixModel<T> model) {
        return new ReadOnlyTMatrix<T>(type, model);
    }

    public <T> TMatrix<T> tmatrix(TypeName<T> type, int rows, int columns, TMatrixCell<T> model) {
        return tmatrix(type, new TMatrixCellToModel<>(rows, columns, model));
    }

    //    public Expr expr(Domain d) {
//        return DoubleValue.valueOf(1, d);
//    }
    public <T> T simplify(T a) {
        return MathsBase.simplify(a);
    }

    public Expr simplify(Expr a) {
        return MathsBase.simplify(a);
    }

    public <T> T simplify(T a, SimplifyOptions simplifyOptions) {
        return MathsBase.simplify(a, simplifyOptions);
    }

    public Expr simplify(Expr a, SimplifyOptions simplifyOptions) {
        return MathsBase.simplify(a, simplifyOptions);
    }

    public double norm(Expr a) {
        //TODO conjugate a
        Expr aCong = a;
        Complex c = Config.getScalarProductOperator().eval(a, aCong);
        return sqrt(c).absdbl();
    }

    public <T> TVector<T> normalize(TVector<T> a) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                Expr n = normalize((Expr) e).simplify();
                return (T) n;
            }
        });
    }

    public Expr normalize(Geometry a) {
        return normalize(expr(a));
    }

    public Expr normalize(Expr a) {
        Complex n = Complex.valueOf(1.0 / norm(a));
        if (n.equals(MathsBase.CONE)) {
            return a;
        }
        Expr mul = mul(a, n);
        //preserve names and properties
        mul = Any.copyProperties(a, mul);
        return mul;
    }

    public DoubleToVector vector(Expr fx, Expr fy) {
        if (fx.isZero() && fy.isZero()) {
            return DVZERO2;
        }
        return DefaultDoubleToVector.create(fx.toDC(), fy.toDC());
    }

    public DoubleToVector vector(Expr fx) {
        if (fx.isZero()) {
            return DVZERO1;
        }
        return DefaultDoubleToVector.create(fx.toDC());
    }

    public DoubleToVector vector(Expr fx, Expr fy, Expr fz) {
        if (fx.isZero() && fy.isZero() && fz.isZero()) {
            return DVZERO3;
        }
        return DefaultDoubleToVector.create(fx.toDC(), fy.toDC(), fz.toDC());
    }

    public <T> TVector<T> cos(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cos());
    }

    public <T> TVector<T> cosh(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cosh());
    }

    public <T> TVector<T> sin(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sin());
    }

    public <T> TVector<T> sinh(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sinh());
    }

    public <T> TVector<T> tan(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().tan());
    }

    public <T> TVector<T> tanh(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().tanh());
    }

    public <T> TVector<T> cotan(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cotan());
    }

    public <T> TVector<T> cotanh(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cotanh());
    }

    public <T> TVector<T> sqr(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sqr());
    }

    public <T> TVector<T> sqrt(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sqrt());
    }

    public <T> TVector<T> inv(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().inv());
    }

    public <T> TVector<T> neg(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().neg());
    }

    public <T> TVector<T> exp(TVector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().exp());
    }

    public <T> TVector<T> simplify(TVector<T> a) {
        return MathsBase.simplify(a);
    }

    public <T> TVector<T> addAll(TVector<T> e, T... expressions) {
        return MathsBase.addAll(e, expressions);
    }

    public <T> TVector<T> mulAll(TVector<T> e, T... expressions) {
        return MathsBase.mulAll(e, expressions);
    }

    public <T> TVector<T> pow(TVector<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.pow(e, b);
            }
        });
    }

    public <T> TVector<T> sub(TVector<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.sub(e, b);
            }
        });
    }

    public <T> TVector<T> div(TVector<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.div(e, b);
            }
        });
    }

    public <T> TVector<T> rem(TVector<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.rem(e, b);
            }
        });
    }

    public <T> TVector<T> add(TVector<T> a, T b) {
        return a.eval(new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = (VectorSpace<T>) getVectorSpace(a.getComponentType());
                return (T) vectorSpace.add(e, b);
            }
        });
    }

    public <T> TVector<T> mul(TVector<T> a, T b) {
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
    public void loopOver(Object[][] values, LoopAction action) {
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

    public void loopOver(Loop[] values, LoopAction action) {
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

    public String formatMemory() {
        return memoryInfo().toString();
    }

    public String formatMetric(double value) {
        return Config.getMetricFormatter().format(value);
    }

    public MemoryInfo memoryInfo() {
        return new MemoryInfo();
    }

    public MemoryMeter memoryMeter() {
        return new MemoryMeter();
    }

    public long inUseMemory() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory());
    }

    public long maxFreeMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.maxMemory() - (rt.totalMemory() - rt.freeMemory());
    }

    public String formatMemory(long bytes) {
        return Config.getMemorySizeFormatter().format(bytes);
    }

    public String formatFrequency(double frequency) {
        return Config.getFrequencyFormatter().format(frequency);
    }

    public String formatDimension(double dimension) {
        return Config.getMetricFormatter().format(dimension);
    }

    public String formatPeriodNanos(long period) {
        return Config.getTimePeriodFormat().formatNanos(period);
    }

    public String formatPeriodMillis(long period) {
        return Config.getTimePeriodFormat().formatMillis(period);
    }

    public int sizeOf(Class src) {
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

    //    public Complex csum(int size, Int2Complex f) {
//        MutableComplex c = new MutableComplex();
//        int i = 0;
//        while (i < size) {
//            c.add(f.eval(i));
//            i++;
//        }
//        return c.toComplex();
//    }
    public <T> T invokeMonitoredAction(ProgressMonitor mon, String messagePrefix, MonitoredAction<T> run) {
        return ProgressMonitorFactory.invokeMonitoredAction(mon, messagePrefix, run);
    }

    public Chronometer chrono() {
        return new Chronometer();
    }

    public Chronometer chrono(String name) {
        return new Chronometer(name);
    }

    public Chronometer chrono(String name, Runnable r) {
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

    public <V> V chrono(String name, Callable<V> r) {
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

    public SolverExecutorService solverExecutorService(int threads) {
        return new SolverExecutorService(threads);
    }

    public Chronometer chrono(Runnable r) {
        Chronometer c = new Chronometer(true);
        r.run();
        return c.stop();
    }

    public DoubleFormat percentFormat() {
        return Config.getPercentFormat();
    }

    public DoubleFormat frequencyFormat() {
        return Config.getFrequencyFormatter();
    }

    public DoubleFormat metricFormat() {
        return Config.getMetricFormatter();
    }

    public DoubleFormat memoryFormat() {
        return Config.getMemorySizeFormatter();
    }

    public DoubleFormat dblformat(String format) {
        return DoubleFormatterFactory.create(format);
    }

    public double[] resizePickFirst(double[] array, int newSize) {
        return MathsArrays.resizePickFirst(array, newSize);
    }

    public double[] resizePickAverage(double[] array, int newSize) {
        return MathsArrays.resizePickAverage(array, newSize);
    }

    public <T> T[] toArray(Class<T> t, Collection<T> coll) {
        return (T[]) coll.toArray((T[]) Array.newInstance(t, coll.size()));
    }

    public <T> T[] toArray(TypeName<T> t, Collection<T> coll) {
        return (T[]) coll.toArray((T[]) Array.newInstance(t.getTypeClass(), coll.size()));
    }

    public double rerr(double a, double b) {
        return MathsArrays.rerr(a, b);
    }

    public double rerr(Complex a, Complex b) {
        return MathsArrays.rerr(a, b);
    }

    public CustomCCFunctionXExpr define(String name, CustomCCFunctionX f) {
        return MathsBase.define(name,f);
    }

    public CustomDCFunctionXExpr define(String name, CustomDCFunctionX f) {
        return MathsBase.define(name,f);
    }

    public CustomDDFunctionXExpr define(String name, CustomDDFunctionX f) {
        return MathsBase.define(name,f);
    }

    public CustomDDFunctionXYExpr define(String name, CustomDDFunctionXY f) {
        return MathsBase.define(name,f);
    }

    public CustomDCFunctionXYExpr define(String name, CustomDCFunctionXY f) {
        return MathsBase.define(name,f);
    }

    public CustomCCFunctionXYExpr define(String name, CustomCCFunctionXY f) {
        return MathsBase.define(name,f);
    }

    public double rerr(ComplexMatrix a, ComplexMatrix b) {
        return b.getError(a);
    }

    public <T extends Expr> DoubleVector toDoubleArray(TVector<T> c) {
        DoubleVector a = new ArrayDoubleVector(c.size());
        for (T o : c) {
            a.append(o.toDouble());
        }
        return a;
    }

    public double toDouble(Complex c, PlotDoubleConverter d) {
        if (d == null) {
            return c.absdbl();
        }
        return d.toDouble(c);
    }

    public Expr conj(Expr e) {
        if (e.isDD()) {
            return e;
        }
        if (e.isComplex()) {
            return e.toComplex().conj();
        }
        return new Conj(e);
    }

    public Complex complex(TMatrix t) {
        return t.toComplex();
    }

    public ComplexMatrix matrix(TMatrix t) {
        return (ComplexMatrix) t.to($COMPLEX);
    }

    public TMatrix<Expr> ematrix(TMatrix t) {
        return new EMatrixFromTMatrix(t);
    }
    //</editor-fold>

    public <T> VectorSpace<T> getVectorSpace(TypeName<T> cls) {
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

    public DoubleVector refineSamples(TVector<Double> values, int n) {
        return MathsSampler.refineSamples(values, n);
    }

    /**
     * adds n points between each 2 points
     *
     * @param values initial sample
     * @return
     */
    public double[] refineSamples(double[] values, int n) {
        return MathsSampler.refineSamples(values, n);
    }

    private class IdentityConverter implements Converter, Serializable {

        @Override
        public Object convert(Object value) {
            return value;
        }
    }

    private class ComplexDoubleConverter implements Converter<Complex, Double>, Serializable {

        @Override
        public Double convert(Complex value) {
            return value.toDouble();
        }
    }

    private class DoubleComplexConverter implements Converter<Double, Complex>, Serializable {

        @Override
        public Complex convert(Double value) {
            return Complex.valueOf(value);
        }
    }

    private class DoubleTVectorConverter implements Converter<Double, TVector>, Serializable {

        @Override
        public TVector convert(Double value) {
            return MathsBase.columnVector(new Complex[]{Complex.valueOf(value)});
        }
    }

    private class TVectorDoubleConverter implements Converter<TVector, Double>, Serializable {

        @Override
        public Double convert(TVector value) {
            return value.toComplex().toDouble();
        }
    }

    private class ComplexTVectorConverter implements Converter<Complex, TVector>, Serializable {

        @Override
        public TVector convert(Complex value) {
            return MathsBase.columnVector(new Complex[]{value});
        }
    }

    private class TVectorComplexConverter implements Converter<TVector, Complex>, Serializable {

        @Override
        public Complex convert(TVector value) {
            return value.toComplex();
        }
    }

    private class ComplexExprConverter implements Converter<Complex, Expr>, Serializable {

        @Override
        public Expr convert(Complex value) {
            return value;
        }
    }

    private class ExprComplexConverter implements Converter<Expr, Complex>, Serializable {

        @Override
        public Complex convert(Expr value) {
            return value.toComplex();
        }
    }

    private class DoubleExprConverter implements Converter<Double, Expr>, Serializable {

        @Override
        public Expr convert(Double value) {
            return Complex.valueOf(value);
        }
    }

    private class ExprDoubleConverter implements Converter<Expr, Double>, Serializable {

        @Override
        public Double convert(Expr value) {
            return value.toComplex().toDouble();
        }
    }

//    private class StringTypeReference extends TypeName<String> {
//    }
//
//    private class MatrixTypeReference extends TypeName<Matrix> {
//    }
//
//    private class VectorTypeReference extends TypeName<Vector> {
//    }
//
//    private class TMatrixTypeReference extends TypeName<TMatrix<Complex>> {
//    }
//
//    private class TVectorTypeReference extends TypeName<TList<Complex>> {
//    }
//
//    private class ComplexTypeReference extends TypeName<Complex> {
//    }
//
//    private class DoubleTypeReference extends TypeName<Double> {
//    }
//
//    private class BooleanTypeReference extends TypeName<Boolean> {
//    }
//
//    private class PointTypeReference extends TypeName<Point> {
//
//    }
//
//    private class FileTypeReference extends TypeName<File> {
//    }
//
//    private class IntegerTypeReference extends TypeName<Integer> {
//    }
//
//    private class LongTypeReference extends TypeName<Long> {
//    }
//
//    private class ExprTypeReference extends TypeName<Expr> {
//    }
//
//    private class TListTypeReference extends TypeName<TList<Complex>> {
//    }
//
//    private class TListExprTypeReference extends TypeName<TList<Expr>> {
//    }
//
//    private class TListDoubleTypeReference extends TypeName<TList<Double>> {
//    }
//
//    private class TListIntegerTypeReference extends TypeName<TList<Integer>> {
//    }
//
//    private class TListBooleanTypeReference extends TypeName<TList<Boolean>> {
//    }
//
//    private class TListMatrixTypeReference extends TypeName<TList<Matrix>> {
//    }

    public String getHadrumathsVersion() {
        return HadrumathsInitializerService.getVersion();
    }

    public ComponentDimension expandComponentDimension(ComponentDimension d1, ComponentDimension d2) {
        return ComponentDimension.create(
                Math.max(d1.rows, d2.rows),
                Math.max(d1.columns, d2.columns)
        );
    }

    public Expr expandComponentDimension(Expr e, ComponentDimension d) {
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

    public double atan2(double y, double x) {
        return Math.atan2(y, x);
    }

    public double ceil(double a) {
        return Math.ceil(a);
    }

    public double floor(double a) {
        return Math.floor(a);
    }

    public long round(double a) {
        return Math.round(a);
    }

    public int round(float a) {
        return Math.round(a);
    }

    public double random() {
        return Math.random();
    }

    public <A, B> RightArrowUplet2<A, B> rightArrow(A a, B b) {
        return new RightArrowUplet2<A, B>(a, b);
    }

    public RightArrowUplet2.Double rightArrow(double a, double b) {
        return new RightArrowUplet2.Double(a, b);
    }

    public RightArrowUplet2.Complex rightArrow(Complex a, Complex b) {
        return new RightArrowUplet2.Complex(a, b);
    }

    public RightArrowUplet2.Expr rightArrow(Expr a, Expr b) {
        return new RightArrowUplet2.Expr(a, b);
    }

    public Expr parseExpression(String expression) {
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

    public ExpressionManager createExpressionEvaluator() {
        return ExpressionManagerFactory.createEvaluator();
    }

    public ExpressionManager createExpressionParser() {
        return ExpressionManagerFactory.createParser();
    }

    public Expr evalExpression(String expression) {
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

    public double toRadians(double a) {
        return Math.toRadians(a);
    }

    public double[] toRadians(double[] a) {
        double[] b = new double[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = Math.toRadians(a[i]);
        }
        return b;
    }

    public Complex det(ComplexMatrix m) {
        return m.det();

    }


    public int toInt(Object o) {
        return toInt(o, null);
    }

    public int toInt(Object o, Integer defaultValue) {
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


    public long toLong(Object o) {
        return toLong(o, null);
    }

    public long toLong(Object o, Long defaultValue) {
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

    public double toDouble(Object o) {
        return toDouble(o, null);
    }

    public double toDouble(Object o, Double defaultValue) {
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

    public float toFloat(Object o) {
        return toFloat(o, null);
    }

    public float toFloat(Object o, Float defaultValue) {
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

    public DoubleToComplex DC(Expr e) {
        return e == null ? null : e.toDC();
    }

    public DoubleToDouble DD(Expr e) {
        return e == null ? null : e.toDD();
    }

    public DoubleToVector DV(Expr e) {
        return e == null ? null : e.toDV();
    }

    public DoubleToMatrix DM(Expr e) {
        return e == null ? null : e.toDM();
    }

    public ComplexMatrix matrix(Expr e) {
        return e == null ? null : e.toMatrix();
    }
}
