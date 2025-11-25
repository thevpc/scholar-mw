package net.thevpc.scholar.hadrumaths;

import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JeepImported;
import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.common.util.*;
import net.thevpc.common.time.*;
import net.thevpc.scholar.hadrumaths.cache.PersistenceCacheBuilder;
import net.thevpc.scholar.hadrumaths.expeval.ExpressionManagerFactory;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadrumaths.scalarproducts.MatrixScalarProductCache;
import net.thevpc.scholar.hadrumaths.scalarproducts.MemComplexScalarProductCache;
import net.thevpc.scholar.hadrumaths.scalarproducts.MemDoubleScalarProductCache;
import net.thevpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.conv.DefaultDoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.ComplexParam;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2matrix.MatrixParam;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.DefaultDoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VectorParam;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.Any;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond.IfExpr;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.Conj;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.util.PlatformUtils;
import net.thevpc.scholar.hadrumaths.util.UnsafeHandler;

import java.io.File;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.lang.annotation.ElementType;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

@JeepImported(ElementType.TYPE)
public final class Maths {

    //<editor-fold desc="constants functions">
    public static final double PI = Math.PI;
    public static final double TWO_PI = 2 * Math.PI;
    public static final double HALF_PI = Math.PI / 2.0;
    public static final double π = Math.PI;
    public static final double E = Math.E;
    public static final DoubleToDouble ZERO = DefaultDoubleValue.ZERO1;//valueOf(0, Domain.FULLX);
    public static final DoubleExpr ONE = DoubleExpr.ONE;//valueOf(0, Domain.FULLX);
    public static final DoubleExpr TWO = DoubleExpr.TWO;//valueOf(0, Domain.FULLX);
    public static final DoubleToDouble DDNAN = DefaultDoubleValue.NAN1;//valueOf(Double.NaN, Domain.FULLX);
    public static final DoubleToComplex DCZERO = Complex.ZERO;
    public static final DoubleToComplex DCONE = Complex.ONE;
    public static final DoubleToVector DVZERO3 = DefaultDoubleToVector.of(DCZERO, DCZERO, DCZERO);
    public static final Expr EZERO = ZERO;
    public static final Expr EONE = DoubleExpr.ONE;//valueOf(1, Domain.FULLX);
    public static final Expr X = new XX(Domain.FULLX);
    public static final Expr Y = new YY(Domain.FULLXY);
    public static final Expr Z = new ZZ(Domain.FULLXYZ);
    public static final Complex I = Complex.I;
    public static final Complex CNaN = Complex.NaN;
    public static final Complex CONE = Complex.ONE;
    public static final Complex CZERO = Complex.ZERO;//    public static boolean DEBUG = true;
    public static final DoubleToVector DVZERO1 = DefaultDoubleToVector.of(DCZERO);
    public static final DoubleToVector DVZERO2 = DefaultDoubleToVector.of(DCZERO, DCZERO);
    public static final Complex î = Complex.I;
    public static final Expr ê = EONE;
    public static final Complex ĉ = CONE;

    public static final DoubleToDouble DZEROX = DefaultDoubleValue.ZERO1;//new DoubleValue(0, Domain.ZEROX).setTitle("0").toDD();
    public static final DoubleToDouble DZEROXY = DefaultDoubleValue.ZERO2;//DoubleValue.valueOf(0, Domain.ZEROXY).setTitle("0").toDD();
    public static final DoubleToDouble DZEROXYZ = DefaultDoubleValue.ZERO3;//DoubleValue.valueOf(0, Domain.ZEROXYZ).setTitle("0").toDD();

    public static final DoubleToComplex CZEROX = new DefaultComplexValue(CZERO, Domain.ZEROX).setTitle("0").toDC();
    public static final DoubleToComplex CZEROXY = new DefaultComplexValue(CZERO, Domain.ZEROXY).setTitle("0").toDC();
    public static final DoubleToComplex CZEROXYZ = new DefaultComplexValue(CZERO, Domain.ZEROXYZ).setTitle("0").toDC();

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
    public static final double μ0 = U0; //N·A−2
    /**
     * electric constant (vacuum permittivity) =1/(u0*C^2)
     */
    public static final double EPS0 = 8.854187817e-12;//F·m−1
    public static final double ε0 = EPS0;
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
    public static final double DEGREES_PER_RADIAN = (double) (180.0 / PI);
    public static final Function IDENTITY = new IdentityConverter();
    public static final Function<Complex, Double> COMPLEX_TO_DOUBLE = new ComplexDoubleConverter();
    public static final Function<Double, Complex> DOUBLE_TO_COMPLEX = new DoubleComplexConverter();
    public static final Function<Double, Vector> DOUBLE_TO_TVECTOR = new DoubleVectorConverter();
    public static final Function<Vector, Double> TVECTOR_TO_DOUBLE = new VectorDoubleConverter();
    public static final Function<Complex, Vector> COMPLEX_TO_TVECTOR = new ComplexTVectorConverter();
    public static final Function<Vector, Complex> TVECTOR_TO_COMPLEX = new VectorComplexConverter();
    public static final Function<Complex, Expr> COMPLEX_TO_EXPR = new ComplexExprConverter();
    public static final Function<Expr, Complex> EXPR_TO_COMPLEX = new ExprComplexConverter();
    public static final Function<Double, Expr> DOUBLE_TO_EXPR = new DoubleExprConverter();
    public static final Function<Expr, Double> EXPR_TO_DOUBLE = new ExprDoubleConverter();
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
    public static final TypeName<VDiscrete> $VDISCRETE = new TypeName(VDiscrete.class.getName());
    public static final TypeName<ComplexVector> $VECTOR = new TypeName(ComplexVector.class.getName());
    public static final TypeName<Matrix<Complex>> $CMATRIX = new TypeName(Matrix.class.getName(), $COMPLEX);
    public static final TypeName<Vector<Complex>> $CVECTOR = new TypeName(Vector.class.getName(), $COMPLEX);
    public static final TypeName<Double> $DOUBLE = new TypeName(Double.class.getName());
    public static final TypeName<Boolean> $BOOLEAN = new TypeName(Boolean.class.getName());
    public static final TypeName<Point> $POINT = new TypeName(Point.class.getName());
    public static final TypeName<File> $FILE = new TypeName(File.class.getName());
    //</editor-fold>
    public static final TypeName<Integer> $INTEGER = new TypeName(Integer.class.getName());
    public static final TypeName<Long> $LONG = new TypeName(Long.class.getName());
    public static final TypeName<Expr> $EXPR = new TypeName(Expr.class.getName());
    public static final TypeName<Vector<Complex>> $CLIST = new TypeName(Vector.class.getName(), $COMPLEX);
    public static final TypeName<Vector<Expr>> $EVECTOR = new TypeName(Vector.class.getName(), $EXPR);
    public static final TypeName<Vector<Double>> $DVECTOR = new TypeName(Vector.class.getName(), $DOUBLE);
    public static final TypeName<Vector<Vector<Double>>> $DLIST2 = new TypeName(Vector.class.getName(), $DVECTOR);
    public static final TypeName<Vector<Integer>> $IVECTOR = new TypeName(Vector.class.getName(), $INTEGER);
    public static final TypeName<Vector<Boolean>> $BVECTOR = new TypeName(Vector.class.getName(), $BOOLEAN);
    public static final TypeName<Vector<ComplexMatrix>> $MVECTOR = new TypeName(Vector.class.getName(), $MATRIX);
    public static final SimpleDateFormat UNIVERSAL_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final MathsConfig Config = MathsConfig.INSTANCE;
    public static final StoreManager<ComplexMatrix> MATRIX_STORE_MANAGER = new ComplexMatrixStoreManager();
    public static final StoreManager<Matrix> TMATRIX_STORE_MANAGER = new MatrixStoreManager();
    public static final StoreManager<Vector> TVECTOR_STORE_MANAGER = new VectorStoreManager();
    public static final StoreManager<ComplexVector> VECTOR_STORE_MANAGER = new ComplexVectorStoreManager();
    public static final DistanceStrategy<Double> DISTANCE_DOUBLE = new DoubleDistanceStrategy();
    public static final DistanceStrategy<Complex> DISTANCE_COMPLEX = Complex.DISTANCE;
    public static final DistanceStrategy<ComplexMatrix> DISTANCE_MATRIX = new ComplexMatrixDistanceStrategy();
    public static final DistanceStrategy<ComplexVector> DISTANCE_VECTOR = new ComplexVectorDistanceStrategy();
    /**
     * numeric scalar product operator
     */
    public static final ScalarProductOperator NUMERIC_SP = ScalarProductOperatorFactory.NUMERIC_SCALAR_PRODUCT_OPERATOR;
    /**
     * formal scalar product operator with error fallback
     */
    public static final ScalarProductOperator FORMAL_SP = ScalarProductOperatorFactory.HARD_FORMAL_SCALAR_PRODUCT_OPERATOR;
    /**
     * formal scalar product operator with numeric fallback
     */
    public static final ScalarProductOperator DEFAULT_SP = ScalarProductOperatorFactory.SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR;
    private static final Logger $log = Logger.getLogger(Maths.class.getName());
//    private static final Jeep defaultExpressionManager = ExpressionManagerFactory.createEvaluator();

    static {
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

    }

    private Maths() {
    }

    public static Domain xdomain(double min, double max) {
        return Domain.ofBounds(min, max);
    }

    public static Domain ydomain(double min, double max) {
        return Domain.ofBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, min, max);
    }

    public static DomainExpr ydomain(DomainExpr min, DomainExpr max) {
        return DomainExpr.ofBounds(Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, min, max);
    }

    public static Domain zdomain(double min, double max) {
        return Domain.ofBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, min, max);
    }

    public static DomainExpr zdomain(Expr min, Expr max) {
        return DomainExpr.ofBounds(Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, min, max);
    }

    public static Domain domain(RightArrowUplet2.Double u) {
        return Domain.ofBounds(u.getFirst(), u.getSecond());
    }

    public static Domain domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return Domain.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Domain domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return Domain.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static Expr domain(RightArrowUplet2.Expr u) {
        if (u.getFirst().isNarrow(ExprType.DOUBLE_NBR) && u.getSecond().isNarrow(ExprType.DOUBLE_NBR)) {
            return Domain.ofBounds(u.getFirst().toDouble(), u.getSecond().toDouble());
        }
        return DomainExpr.ofBounds(u.getFirst(), u.getSecond());
    }

    public static Expr domain(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy) {
        if (ux.getFirst().isNarrow(ExprType.DOUBLE_NBR) && ux.getSecond().isNarrow(ExprType.DOUBLE_NBR) && uy.getFirst().isNarrow(ExprType.DOUBLE_NBR) && uy.getSecond().isNarrow(ExprType.DOUBLE_NBR)) {
            return Domain.ofBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble());
        }
        return DomainExpr.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Expr domain(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy, RightArrowUplet2.Expr uz) {
        if (ux.getFirst().isNarrow(ExprType.DOUBLE_NBR) && ux.getSecond().isNarrow(ExprType.DOUBLE_NBR) && uy.getFirst().isNarrow(ExprType.DOUBLE_NBR) && uy.getSecond().isNarrow(ExprType.DOUBLE_NBR) && uz.getFirst().isNarrow(ExprType.DOUBLE_NBR) && uz.getSecond().isNarrow(ExprType.DOUBLE_NBR)) {
            return Domain.ofBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble(), uz.getFirst().toDouble(), uz.getSecond().toDouble());
        }
        return DomainExpr.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static DomainExpr domain(Expr min, Expr max) {
        return DomainExpr.ofBounds(min, max);
    }

    public static Domain domain(double min, double max) {
        return Domain.ofBounds(min, max);
    }

    public static Domain domain(double xmin, double xmax, double ymin, double ymax) {
        return Domain.ofBounds(xmin, xmax, ymin, ymax);
    }

    public static DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return DomainExpr.ofBounds(xmin, xmax, ymin, ymax);
    }

    public static Domain domain(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return Domain.ofBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return DomainExpr.ofBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static Domain II(RightArrowUplet2.Double u) {
        return Domain.ofBounds(u.getFirst(), u.getSecond());
    }

    public static Domain II(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return Domain.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Domain II(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return Domain.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static Expr II(RightArrowUplet2.Expr u) {
        if (u.getFirst().getDomain().isUnbounded() && u.getSecond().getDomain().isUnbounded()) {
            if (u.getFirst().isNarrow(ExprType.DOUBLE_NBR) && u.getSecond().isNarrow(ExprType.DOUBLE_NBR)) {
                return Domain.ofBounds(u.getFirst().toDouble(), u.getSecond().toDouble());
            }
        }
        return DomainExpr.ofBounds(u.getFirst(), u.getSecond());
    }

    public static Expr II(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy) {
        if (ux.getFirst().isNarrow(ExprType.DOUBLE_NBR) && ux.getSecond().isNarrow(ExprType.DOUBLE_NBR) && uy.getFirst().isNarrow(ExprType.DOUBLE_NBR) && uy.getSecond().isNarrow(ExprType.DOUBLE_NBR)) {
            return Domain.ofBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble());
        }
        return DomainExpr.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Expr II(RightArrowUplet2.Expr ux, RightArrowUplet2.Expr uy, RightArrowUplet2.Expr uz) {
        if (ux.getFirst().isNarrow(ExprType.DOUBLE_NBR) && ux.getSecond().isNarrow(ExprType.DOUBLE_NBR) && uy.getFirst().isNarrow(ExprType.DOUBLE_NBR) && uy.getSecond().isNarrow(ExprType.DOUBLE_NBR) && uz.getFirst().isNarrow(ExprType.DOUBLE_NBR) && uz.getSecond().isNarrow(ExprType.DOUBLE_NBR)) {
            return Domain.ofBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble(), uz.getFirst().toDouble(), uz.getSecond().toDouble());
        }
        return DomainExpr.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static DomainExpr II(Expr min, Expr max) {
        return DomainExpr.ofBounds(min, max);
    }

    public static Domain II(double min, double max) {
        return Domain.ofBounds(min, max);
    }

    public static Domain II(double xmin, double xmax, double ymin, double ymax) {
        return Domain.ofBounds(xmin, xmax, ymin, ymax);
    }

    public static DomainExpr II(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return DomainExpr.ofBounds(xmin, xmax, ymin, ymax);
    }

    public static Domain II(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return Domain.ofBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static DomainExpr II(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return DomainExpr.ofBounds(xmin, xmax, ymin, ymax, zmin, zmax);
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

    public static ComplexMatrix matrix(Complex[][] complex) {
        return Config.getComplexMatrixFactory().newMatrix(complex);
    }

    public static ComplexMatrix zerosMatrix(ComponentDimension dim) {
        return zerosMatrix(dim.rows, dim.columns);
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

    public static ComplexMatrix matrix(String string) {
        return Config.getComplexMatrixFactory().newMatrix(string);
    }

    public static ComplexMatrix matrix(double[][] complex) {
        return Config.getComplexMatrixFactory().newMatrix(complex);
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
            d[i] = Complex.of(values[i]);
        }
        return Config.getComplexMatrixFactory().newColumnMatrix(d);
    }

    public static ComplexMatrix rowMatrix(double... values) {
        Complex[] d = new Complex[values.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = Complex.of(values[i]);
        }
        return Config.getComplexMatrixFactory().newRowMatrix(d);
    }

    public static ComplexMatrix rowMatrix(Complex... values) {
        return Config.getComplexMatrixFactory().newRowMatrix(values);
    }

    public static ComplexMatrix columnMatrix(int rows, VectorCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newColumnMatrix(rows, cellFactory);
    }

    public static ComplexMatrix rowMatrix(int columns, VectorCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newRowMatrix(columns, cellFactory);
    }

    public static ComplexMatrix symmetricMatrix(int rows, int cols, MatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newSymmetric(rows, cols, cellFactory);
    }

    public static ComplexMatrix hermitianMatrix(int rows, int cols, MatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newHermitian(rows, cols, cellFactory);
    }

    public static ComplexMatrix diagonalMatrix(int dim, VectorCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newDiagonal(dim, cellFactory);
    }

    public static ComplexMatrix diagonalMatrix(Complex... c) {
        return Config.getComplexMatrixFactory().newDiagonal(c);
    }

    public static ComplexMatrix matrix(int dim, MatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newMatrix(dim, cellFactory);
    }

    public static ComplexMatrix matrix(int rows, int columns) {
        return Config.getComplexMatrixFactory().newMatrix(rows, columns);
    }

    public static ComplexMatrix symmetricMatrix(int dim, MatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newMatrix(dim, cellFactory);
    }

    public static ComplexMatrix hermitianMatrix(int dim, MatrixCell<Complex> cellFactory) {
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

    public static <T> Matrix<T> loadTMatrix(TypeName<T> componentType, File file) throws UncheckedIOException {
        if (componentType.equals($COMPLEX)) {
            return (Matrix<T>) Config.getComplexMatrixFactory().load(file);
        }
        throw new IllegalArgumentException("Unsupported Load for type " + componentType);
    }

    public static ComplexMatrix loadMatrix(File file) throws UncheckedIOException {
        return Config.getComplexMatrixFactory().load(file);
    }

    public static ComplexMatrix matrix(File file) throws UncheckedIOException {
        return Config.getComplexMatrixFactory().load(file);
    }

    public static void storeMatrix(ComplexMatrix m, String file) throws UncheckedIOException {
        m.store(file == null ? null : new File(Config.expandPath(file)));
    }

    public static void storeMatrix(ComplexMatrix m, File file) throws UncheckedIOException {
        m.store(file);
    }

    public static ComplexMatrix loadOrEvalMatrix(String file, Supplier<ComplexMatrix> item) throws UncheckedIOException {
        return loadOrEvalMatrix(new File(Config.expandPath(file)), item);
    }

    public static ComplexMatrix loadOrEvalMatrix(File file, Supplier<ComplexMatrix> item) throws UncheckedIOException {
        return loadOrEval($MATRIX, file, item);
    }

    public static <T> T loadOrEval(TypeName<T> ofType, File file, Supplier<T> item) throws UncheckedIOException {
        StoreManager<T> t = TStoreManagerFactory.create(ofType);
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

    public static Chronometer chrono() {
        return Chronometer.start();
    }

    public static ComplexVector loadOrEvalVector(String file, Supplier<Vector<Complex>> item) throws UncheckedIOException {
        return loadOrEvalVector(new File(Config.expandPath(file)), item);
    }

    public static ComplexVector loadOrEvalVector(File file, Supplier<Vector<Complex>> item) throws UncheckedIOException {
        return loadOrEval($VECTOR, file, (Supplier) item);
    }

    public static <T> Matrix loadOrEvalTMatrix(String file, Supplier<Matrix<T>> item) throws UncheckedIOException {
        return loadOrEvalTMatrix(new File(Config.expandPath(file)), item);
    }

    public static <T> Matrix<T> loadOrEvalTMatrix(File file, Supplier<Matrix<T>> item) throws UncheckedIOException {
        return loadOrEval((TypeName) $CMATRIX, file, item);
    }

    public static <T> Vector<T> loadOrEvalTVector(String file, Supplier<Vector<T>> item) throws UncheckedIOException {
        return loadOrEvalTVector(new File(Config.expandPath(file)), item);
    }

    public static <T> Vector loadOrEvalTVector(File file, Supplier<Vector<T>> item) throws UncheckedIOException {
        return loadOrEval($CVECTOR, file, (Supplier) item);
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

    public static ComplexVector zerosVector(int size) {
        return zerosColumnVector(size);
    }

    public static ComplexVector zerosColumnVector(int size) {
        return constantColumnVector(size, CZERO);
    }

    public static ComplexVector constantColumnVector(int size, Complex c) {
        Complex[] arr = new Complex[size];
        for (int i = 0; i < size; i++) {
            arr[i] = c;
        }
        return DefaultComplexVector.Column(arr);
    }

    public static ComplexVector zerosRowVector(int size) {
        return constantRowVector(size, CZERO);
    }

    public static ComplexVector constantRowVector(int size, Complex c) {
        Complex[] arr = new Complex[size];
        for (int i = 0; i < size; i++) {
            arr[i] = c;
        }
        return DefaultComplexVector.Row(arr);
    }

    public static ComplexVector NaNColumnVector(int dim) {
        return constantColumnVector(dim, Complex.NaN);
    }

    public static ComplexVector NaNRowVector(int dim) {
        return constantRowVector(dim, Complex.NaN);
    }

    public static ExprVector columnVector(Expr... expr) {
        return DefaultExprVector.of(false, expr);
    }

    public static ExprVector rowVector(Expr... expr) {
        return DefaultExprVector.of(true, expr);
    }

    public static ExprVector columnEVector(int rows, VectorCell<Expr> cellFactory) {
        return DefaultExprVector.of(false, rows, cellFactory);
    }

    public static ExprVector rowEVector(int rows, VectorCell<Expr> cellFactory) {
        return DefaultExprVector.of(true, rows, cellFactory);
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

    public static <T> Vector<T> copyOf(Vector<T> vector) {
        Vector<T> ts = vector(vector.getComponentType(), vector.isRow(), vector.size());
        ts.appendAll(vector);
        return ts;
    }

    public static <T> Vector<T> vector(TypeName<T> typeName, boolean row, int initialSize) {
        if (typeName.equals($EXPR)) {
            return (Vector<T>) evector(row, initialSize);
        }
        if (typeName.equals($DOUBLE)) {
            return (Vector<T>) dvector(row, initialSize);
        }
        if (typeName.equals($INTEGER)) {
            return (Vector<T>) ivector(row, initialSize);
        }
        if (typeName.equals($LONG)) {
            return (Vector<T>) lvector(row, initialSize);
        }
        if (typeName.equals($BOOLEAN)) {
            return (Vector<T>) bvector(row, initialSize);
        }
        return new ArrayVector<T>(typeName, row, initialSize);
    }

    public static ExprVector evector(boolean row, int size) {
        return DefaultExprVector.of(row, size);
    }

    public static Vector<Double> dvector(boolean row, int size) {
        return new ArrayDoubleVector(row, size);
    }

    public static Vector<Integer> ivector(boolean row, int size) {
        return new ArrayIntVector(row, size);
    }

    public static Vector<Long> lvector(boolean row, int size) {
        return new ArrayLongVector(row, size);
    }

    public static Vector<Boolean> bvector(boolean row, int size) {
        return new ArrayBooleanVector(row, size);
    }

    public static <T> Vector<T> columnVector(TypeName<T> cls, T... elements) {
        return new ReadOnlyVector<T>(
                cls, false, new VectorModel<T>() {
            @Override
            public int size() {
                return elements.length;
            }

            @Override
            public T get(int index) {
                return elements[index];
            }
        }
        ).copy();
    }

    public static <T> Vector<T> columnVector(TypeName<T> cls, int rows, VectorCell<T> cellFactory) {
        return columnVector(cls, new VectorModelFromCell<>(rows, cellFactory));
    }

    public static <T> Vector<T> columnVector(TypeName<T> cls, VectorModel<T> cellFactory) {
        return new ReadOnlyVector<T>(
                cls, false, cellFactory
        );
    }

    public static <T> Vector<T> rowTVector(TypeName<T> cls, int rows, VectorCell<T> cellFactory) {
        return rowTVector(cls, new VectorModelFromCell<>(rows, cellFactory));
    }

    public static <T> Vector<T> rowTVector(TypeName<T> cls, VectorModel<T> cellFactory) {
        return new ReadOnlyVector<>(
                cls, true, cellFactory
        );
    }

    public static ComplexVector columnVector(double[] elems) {
        return DefaultComplexVector.Column(ArrayUtils.toComplex(elems));
    }

    public static ComplexVector rowVector(double[] elems) {
        return DefaultComplexVector.Row(ArrayUtils.toComplex(elems));
    }

    public static ComplexVector column(Complex[] elems) {
        return DefaultComplexVector.Column(elems);
    }

    public static ComplexVector row(Complex[] elems) {
        return DefaultComplexVector.Row(elems);
    }

    public static ComplexVector trh(ComplexVector c) {
        return c.transpose().conj();
    }

    public static ComplexVector tr(ComplexVector c) {
        return c.transpose();
    }

    public static <T> ComplexVector complexVector(Vector<T> c) {
        return DefaultComplexVector.of(c.to($COMPLEX));
    }

    //</editor-fold>
    //<editor-fold desc="Complex functions">
    public static Complex I(double iValue) {
        return Complex.of(0, iValue);
    }

    public static Complex abs(Complex a) {
        return (a.abs());
    }
    //</editor-fold>

    public static double absdbl(Complex a) {
        return a.absdbl();
    }

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
        return net.thevpc.common.util.ArrayUtils.subArray1(dtimes(min, max, maxTimes), times, strategy);
    }

    public static double[] dtimes(double min, double max, int times) {
        return net.thevpc.common.util.ArrayUtils.dtimes(min, max, times);
    }

    public static float[] ftimes(float min, float max, int times) {
        return net.thevpc.common.util.ArrayUtils.ftimes(min, max, times);
    }

    public static long[] ltimes(long min, long max, int times) {
        return net.thevpc.common.util.ArrayUtils.ltimes(min, max, times);
    }

    public static long[] lsteps(long min, long max, long step) {
        return net.thevpc.common.util.ArrayUtils.lsteps(min, max, step);
    }

    public static int[] itimes(int min, int max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return net.thevpc.common.util.ArrayUtils.itimes(min, max, times, maxTimes, strategy);
    }

    public static double[] dsteps(int max) {
        return dsteps(0, max, 1);
    }

    public static double[] dsteps(double min, double max, double step) {
        return net.thevpc.common.util.ArrayUtils.dsteps(min, max, step);
    }

    public static double[] dstepsFrom(double min, int times, double step) {
        if (times <= 0) {
            return new double[0];
        }
        double[] d = new double[times];
        for (int i = 0; i < d.length; i++) {
            d[i] = min + i * step;
        }
        return d;
    }

    public static double dstepsLength(double min, double max, double step) {
        return net.thevpc.common.util.ArrayUtils.dstepsLength(min, max, step);
    }

    public static double dstepsElement(double min, double max, double step, int index) {
        return net.thevpc.common.util.ArrayUtils.dstepsElement(min, max, step, index);
    }

    //
    public static float[] fsteps(float min, float max, float step) {
        return net.thevpc.common.util.ArrayUtils.fsteps(min, max, step);
    }

    public static int[] isteps(int min, int max, int step, IntPredicate filter) {
        return net.thevpc.common.util.ArrayUtils.isteps(min, max, step, filter);
    }

    public static int[] isteps(int max) {
        return isteps(0, max, 1);
    }

    public static int[] isteps(int min, int max, int step) {
        return net.thevpc.common.util.ArrayUtils.isteps(min, max, step);
    }

    public static int[] isteps(int min, int max) {
        return isteps(min, max, 1);
    }

    public static int[] itimes(int min, int max) {
        return itimes(min, max, max - min + 1);
    }

    public static int[] itimes(int min, int max, int times) {
        return net.thevpc.common.util.ArrayUtils.itimes(min, max, times);
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

    public static double sin2(double d) {
        return Math.sin(d);
//        return MathsTrigo.sin2(d);
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

    public static Expr log(Expr c) {
        return c.log();
    }

    public static Expr log10(Expr c) {
        return c.log10();
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

    public static Complex sum(Complex... c) {
        return csum(c);
    }

    public static Complex csum(Expr... c) {
        Expr x = c[0];
        for (int i = 1; i < c.length; i++) {
            x = x.plus(c[i]);
        }
//        MutableComplex x = new MutableComplex();
//        for (Complex c1 : c) {
//            x.add(c1);
//        }
        return x.toComplex();
    }

    public static Complex csum(VectorModel<Complex> c) {
        MutableComplex x = new MutableComplex();
        int size = c.size();
        for (int i = 0; i < size; i++) {
            x.add(c.get(i));
        }
        return x.toComplex();
    }

    public static Complex csum(int size, VectorCell<Complex> c) {
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

    public static double[][] toDouble(Complex[][] c, ToDoubleFunction<Object> toDoubleConverter) {
        return MathsArrays.toDouble(c, toDoubleConverter);
    }

    public static double[] toDouble(Complex[] c, ToDoubleFunction<Object> toDoubleConverter) {
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
        return Complex.of(d).sqrt();
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

    public static Complex pow(Complex a, Complex b) {
        return a.pow(b);
    }

    public static Complex div(Complex a, Complex b) {
        return a.div(b);
    }

    public static Complex plus(Complex a, Complex b) {
        return a.plus(b);
    }

    public static Complex minus(Complex a, Complex b) {
        return a.minus(b);
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
        return DefaultDoubleToComplex.of(fx);
    }

    public static DoubleToComplex complex(DoubleToDouble fx, DoubleToDouble fy) {
        return DefaultDoubleToComplex.of(fx, fy);
    }

    public static double randomDouble(double value) {
        return value * Math.random();
    }

    public static double randomDouble(double min, double max) {
        return min + ((max - min) * Math.random());
    }

    public static <T> T randomArrayElement(T[] arr) {
        return arr[randomInt(arr.length)];
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
        return Complex.of(r * Math.cos(r), r * sin(p));
    }

    /////////////////////////////////////////////////////////////////
    // double functions

    /// //////////////////////////////////////////////////////////////
    //<editor-fold desc="double functions">
    public static double sin(double x) {
        return Math.sin(x);
    }

    public static boolean randomBoolean() {
        return Math.random() <= 0.5;
    }

    public static double[][] cross(double[] x, double[] y, double[] z) {
        return MathsArrays.cross(x, y, z);
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

    public static Vector sineSeq(String borders, int m, int n, Domain domain) {
        return sineSeq(borders, m, n, domain, PlaneAxis.XY);
    }

    public static Vector sineSeq(String borders, int m, int n, Domain domain, PlaneAxis plane) {
        DoubleParam mm = param("m");
        DoubleParam nn = param("n");
        switch (plane) {
            case XY: {
                return new SinSeqXY(borders, mm, nn, domain).inflate(mm.in(0, m).and(nn.in(0, n)));
            }
            case XZ: {
                return new SinSeqXZ(borders, mm, nn, domain).inflate(mm.in(0, m).and(nn.in(0, n)));
            }
            case YZ: {
                return new SinSeqYZ(borders, mm, nn, domain).inflate(mm.in(0, m).and(nn.in(0, n)));
            }
        }
        throw new IllegalArgumentException("Unsupported Plane " + plane);
    }

    //<editor-fold desc="parameters functions">
    public static DoubleParam param(String name) {
        return dparam(name);
    }

    public static DoubleParam dparam(String name) {
        return new DoubleParam(name);
    }

    public static ComplexParam cparam(String name) {
        return new ComplexParam(name);
    }

    public static VectorParam vparam(String name) {
        return new VectorParam(name);
    }

    public static MatrixParam mparam(String name) {
        return new MatrixParam(name);
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
        return Any.of(e);
    }

    public static Expr expr(double value) {
        return DoubleExpr.of(value);
    }

    public static Expr expr(Expr value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
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

    public static double[][] cross(double[] x, double[] y) {
        return MathsArrays.cross(x, y);
    }

    public static double[] dsteps(double min, double max) {
        return dsteps(min, max, 1);
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, DoubleParam n, double[][] values) {
        return DefaultExprVector.of(false, new SimpleSeq2(values, m, n, pattern));
    }

    public static ExprVector evector(Vector<Complex> vector) {
        return DefaultExprVector.of(false, vector);
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, double max, DoublePredicate filter) {
        if (filter != null) {
            ArrayDoubleVector list = new ArrayDoubleVector();
            double[] mm = dsteps(0, max);
            for (double cros : mm) {
                if (filter.test(cros)) {
                    list.add(cros);
                }
            }
            return seq(pattern, m, list.toDoubleArray());
        }
        return seq(pattern, m, 0, max);
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, double[] values) {
        return DefaultExprVector.of(false, new SimpleSeq1(values, m, pattern));
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, double min, double max) {
        return seq(pattern, m, dsteps(min, max, 1));
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

    public static double[][] cross(double[] x, double[] y, double[] z, Double3Filter filter) {
        return MathsArrays.cross(x, y, z, filter);
    }

    public static double[][] cross(double[] x, double[] y, double[] z, double[] t, Double4Filter filter) {
        return MathsArrays.cross(x, y, z, t, filter);
    }

    @Deprecated
    public static ExprVector seq(Expr pattern, DoubleParam m, DoubleParam n, DoubleParam p, double[][] values) {
        return DefaultExprVector.of(false, new SimpleSeqMulti(pattern, new DoubleParam[]{m, n, p}, values));
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
    public static ExprVector seq(Expr pattern, DoubleParam[] m, double[][] values) {
        return DefaultExprVector.of(false, new SimpleSeqMulti(pattern, m, values));
    }

    public static ExprMatrix matrix(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues) {
        return Config.getExprMatrixFactory().newMatrix(mvalues.length, nvalues.length, new SimpleSeq2b(pattern, m, mvalues, n, nvalues));
    }

    public static ExprCube cube(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues, DoubleParam p, double[] pvalues) {
        return Config.getExprCubeFactory().newUnmodifiableCube(mvalues.length, nvalues.length, pvalues.length, new SimpleSeq3b(pattern, m, mvalues, n, nvalues, p, pvalues));
    }

    public static Expr derive(Expr f, Axis axis) {
        f=f.simplify();
        return Config.getFunctionDerivatorManager().derive(f, axis).simplify();
    }

    public static boolean isReal(Expr e) {
        switch (e.getType()) {
            case DOUBLE_DOUBLE:
                return true;
            case DOUBLE_COMPLEX:
                return e.toDC().getImagDD().isZero();
            case DOUBLE_CVECTOR: {
                DoubleToVector v = e.toDV();
                for (Expr se : v.getChildren()) {
                    if (!isReal(se)) {
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
        return e.abs();
    }

    public static Expr db(Expr e) {
        return e.db();
    }

    public static Expr db2(Expr e) {
        return e.db2();
    }

    public static Complex complex(int e) {
        return Complex.of(e);
    }

    public static Complex complex(double e) {
        return Complex.of(e);
    }

    public static Complex complex(double a, double b) {
        return Complex.of(a, b);
    }

    public static double Double(Expr e) {
        return e.simplify().toDouble();
    }

    public static Expr real(Expr e) {
        return e.real();
    }

    public static Expr imag(Expr e) {
        return e.imag();
    }

    public static Complex Complex(double e) {
        return Complex.of(e);
    }

    public static Complex Complex(Expr e) {
        return e.simplify().toComplex();
    }

    public static Complex complex(Expr e) {
        return e.toComplex();
    }

    public static double doubleValue(Expr e) {
        return e.simplify().toDouble();
    }

    public static CDiscrete discrete(Domain domain, Complex[][][] model, Axis axis1, Axis axis2, Axis axis3) {
        return CDiscrete.of(domain, model, axis1, axis2, axis3);
    }

    public static CDiscrete discrete(Domain domain, Complex[][][] model) {
        return CDiscrete.of(domain, model);
    }

    public static double abssqr(double e) {
        return Maths.DOUBLE_VECTOR_SPACE.abssqr(e);
    }

    public static Expr abssqr(Expr e) {
        return getVectorSpace($EXPR).abssqr(e);
    }

    public static AdaptiveResult1 adaptiveEval(Expr expr, AdaptiveConfig config) {
        Domain domain = expr.getDomain();
        switch (domain.getDimension()) {
            case 1: {
                switch (expr.getType()) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE: {
                        return adaptiveEval(new AdaptiveFunction1<Double>() {
                            @Override
                            public Double eval(double x) {
                                return expr.toDD().evalDouble(x);
                            }
                        }, DISTANCE_DOUBLE, (DomainX) expr.getDomain().getDomainX(), config);
                    }
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        return adaptiveEval(new AdaptiveFunction1<Complex>() {
                            @Override
                            public Complex eval(double x) {
                                return expr.toDC().evalComplex(x);
                            }
                        }, Complex.DISTANCE, (DomainX) expr.getDomain().getDomainX(), config);
                    }
                }
            }
        }
        throw new IllegalArgumentException("Unsupported Dimension " + domain.getDimension());
    }

    public static <T> AdaptiveResult1 adaptiveEval(AdaptiveFunction1<T> expr, DistanceStrategy<T> distance, DomainX domain, AdaptiveConfig config) {
        return MathsSampler.adaptiveEval(expr, distance, domain, config);
    }

    public static CDiscrete discrete(Expr expr) {
        return MathsSampler.discrete(expr);
    }

    public static VDiscrete vdiscrete(Expr expr) {
        return MathsSampler.vdiscrete(expr);
    }

    public static Expr discrete(Expr expr, Domain domain, int xSamples) {
        return MathsSampler.discrete(expr, domain, xSamples);
    }

    public static Expr discrete(Expr expr, Domain domain, int xSamples, int ySamples) {
        return MathsSampler.discrete(expr, domain, xSamples, ySamples);
    }

    public static Expr discrete(Expr expr, Domain domain, int xSamples, int ySamples, int zSamples) {
        return MathsSampler.discrete(expr, domain, xSamples, ySamples, zSamples);
    }

    public static Expr discrete(Expr expr, int xSamples) {
        return MathsSampler.discrete(expr, expr.getDomain(), xSamples);
    }

    public static Expr discrete(Expr expr, int xSamples, int ySamples) {
        return MathsSampler.discrete(expr, expr.getDomain(), xSamples, ySamples);
    }

    public static Expr discrete(Expr expr, int xSamples, int ySamples, int zSamples) {
        return MathsSampler.discrete(expr, expr.getDomain(), xSamples, ySamples, zSamples);
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

    public static boolean isInt(double d) {
        return net.thevpc.common.util.PlatformUtils.isInt(d);
    }

    /**
     * lenient cast
     *
     * @param o      instance
     * @param toType cast to
     * @param <T>    toType
     * @return
     */
    public static <T> T lcast(Object o, Class<T> toType) {
        return o == null ? null : toType.isInstance(o) ? (T) o : null;
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

    public static Expr expr(String expr) {
        return Maths.parseExpression(expr);
    }

    public static Expr parseExpression(String expression) {
        JContext m = createExpressionParser();
        Object evaluated = m.evaluate(expression, null);
        if (evaluated instanceof Expr) {
            return (Expr) evaluated;
        }
        if (evaluated instanceof Number) {
            return expr(((Number) evaluated).doubleValue());
        }
        return (Expr) evaluated;
    }

    public static JContext createExpressionParser() {
        return ExpressionManagerFactory.createParser();
    }

    public static DoubleToDouble expr(Domain domain) {
        return domain.toDD();
    }

    public static Expr expr(Complex a, Geometry geometry) {
        if (geometry == null) {
            return a;
        }
        if (a.isReal()) {
            if (geometry.isRectangular()) {
                return DefaultDoubleValue.of(a.getReal(), geometry.getDomain());
            }
            return new Shape2D(a.getReal(), geometry);
        }
        if (geometry.isRectangular()) {
            return new DefaultComplexValue(a, geometry.getDomain());
        } else {
            throw new IllegalArgumentException("Not supported yet geometry with complex value");
        }
    }

    public static Expr expr(Complex a, Domain domain) {
        if (domain == null) {
            return a;
        }
        if (a.isReal()) {
            return DefaultDoubleValue.of(a.getReal(), domain.getDomain());
        }
        if (domain.isUnbounded1()) {
            return a;
        }
        return new DefaultComplexValue(a, domain.getDomain());
    }

    public static <T> Vector<T> abs(Vector<T> a) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.abs(e);
            }
        });
    }

    public static <T> Vector<T> db(Vector<T> a) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.db(e);
            }
        });
    }

    public static <T> Vector<T> db2(Vector<T> a) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.db2(e);
            }
        });
    }

    public static <T> Vector<T> real(Vector<T> a) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.real(e);
            }
        });
    }

    public static <T> Vector<T> imag(Vector<T> a) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.imag(e);
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
        double relativeError = (b.isZero() ? a : ((a.minus(b)).div(b))).norm();
        return relativeError <= maxRelativeError;
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

    //    public static Complex[] abs(Complex[] v) {
//        Complex[] r = new Complex[v.length];
//        for (int i = 0; i < r.length; i++) {
//            r[i] = (v[i].abs());
//        }
//        return r;
//    }
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

    public static double round(double value, double precision) {
        return (Math.round(value / precision)) * precision;
    }

    /////////////////////////////////////////////////////////////////
    // sample region functions
    /////////////////////////////////////////////////////////////////
    //<editor-fold desc="sample region functions">
//</editor-fold>
    /////////////////////////////////////////////////////////////////
    // double functions

    /// //////////////////////////////////////////////////////////////
    //<editor-fold desc="double functions">
    public static double sqrt(double v, int n) {
        return n == 0 ? 1 : pow(v, 1.0 / n);
    }

    public static double pow(double v, double n) {
        return Math.pow(v, n);
    }

    public static double acosh(double x) {
        return Math.log(x + Math.sqrt(x * x - 1));
    }

    public static double acotanh(double x) {
        if (true) {
            throw new IllegalArgumentException("TODO : Check me");
        }
        return 1 / atanh(1 / x);
    }

    public static double atanh(double x) {
        return Math.log((1 + x) / (1 - x)) / 2;
    }

    public static double asinh(double x) {
        if (x == Double.NEGATIVE_INFINITY) {
            return x;
        } else {
            return Math.log(x + Math.sqrt(x * x + 1));
        }
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

    public static double[] cos2(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = cos2(x[i]);
        }
        return y;
    }
//</editor-fold>

    public static double cos2(double d) {
        return Math.cos(d);
//        return MathsTrigo.cos2(d);
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

    public static double[] minus(double[] a, double[] b) {
        int max = Math.max(a.length, b.length);
        double[] ret = new double[max];
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] - b[i];
        }
        return ret;
    }

    public static double[] minus(double[] a, double b) {
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

    public static double db(double x) {
        return 10 * Math.log10(x);
    }

    public static double[][] sin(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = sin(r[i]);
        }
        return r;
    }

    public static double[] sin(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.sin(x[i]);
        }
        return y;
    }

    public static double[][] sin2(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = sin2(r[i]);
        }
        return r;
    }

    /////////////////////////////////////////////////////////////////
    // double[] functions

    /// //////////////////////////////////////////////////////////////
    //<editor-fold desc="double[] functions">
    public static double[] sin2(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = sin2(x[i]);
        }
        return y;
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
    //</editor-fold>

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

    /////////////////////////////////////////////////////////////////
    // double[] functions

    /// //////////////////////////////////////////////////////////////
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
    //</editor-fold>

    public static double[] plus(double[] x, double v) {
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

    /////////////////////////////////////////////////////////////////
    // double[][] functions

    /// //////////////////////////////////////////////////////////////
    //<editor-fold desc="double[][] functions">
    public static double[][] cos(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = cos(r[i]);
        }
        return r;
    }

    public static double[] cos(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.cos(x[i]);
        }
        return y;
    }

    public static double[][] tan(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = tan(r[i]);
        }
        return r;
    }

    public static double[] tan(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.tan(x[i]);
        }
        return y;
    }

    public static double[][] cotan(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = cotan(r[i]);
        }
        return r;
    }
    //</editor-fold>

    public static double[] cotan(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = 1 / Math.tan(x[i]);
        }
        return y;
    }

    public static double[][] sinh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = sinh(r[i]);
        }
        return r;
    }

    public static double[] sinh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.sinh(x[i]);
        }
        return y;
    }

    public static double[][] cosh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = cosh(r[i]);
        }
        return r;
    }

    public static double[] cosh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.cosh(x[i]);
        }
        return y;
    }

    public static double[][] tanh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = tanh(r[i]);
        }
        return r;
    }

    public static double[] tanh(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.tanh(x[i]);
        }
        return y;
    }

    public static double[][] cotanh(double[][] c) {
        double[][] r = new double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = tanh(r[i]);
        }
        return r;
    }

    public static double[][] plus(double[][] a, double[][] b) {
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

    public static double[][] minus(double[][] a, double[][] b) {
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
    //</editor-fold>

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

    public static double db2(double nbr) {
        return 20 * Math.log10(nbr);
    }

    /////////////////////////////////////////////////////////////////
    // expression functions

    /// //////////////////////////////////////////////////////////////
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
        return a.or(b);
    }

    public static Expr and(Expr a, Expr b) {
        return a.and(b);
    }

    public static Expr not(Expr a) {
        return a.not();
    }

    public static Expr eq(Expr a, Expr b) {
        return a.eq(b);
    }

    public static Expr ne(Expr a, Expr b) {
        return a.ne(b);
    }

    public static Expr gte(Expr a, Expr b) {
        return a.gte(b);
    }

    public static Expr gt(Expr a, Expr b) {
        return a.gt(b);
    }

    public static Expr lte(Expr a, Expr b) {
        return a.lte(b);
    }

    public static Expr lt(Expr a, Expr b) {
        return a.lt(b);
    }

    public static Expr cos(Expr e) {
        return e.cos();
    }

    public static Expr cosh(Expr e) {
        return e.cosh();
    }

    public static Expr sin(Expr e) {
        return e.sin();
    }

    public static Expr sincard(Expr e) {
        return e.sincard();
    }

    public static Expr sinh(Expr e) {
        return e.sinh();
    }

    public static Expr tan(Expr e) {
        return e.tan();
    }

    public static Expr tanh(Expr e) {
        return e.tanh();
    }

    public static Expr cotan(Expr e) {
        return e.cotan();
    }

    public static Expr cotanh(Expr e) {
        return e.cotanh();
    }

    public static Expr sqr(Expr e) {
        return e.sqr();
    }

    public static Expr inv(Expr e) {
        return e.inv();
    }

    public static Expr neg(Expr e) {
        return e.neg();
    }

    public static double neg(double e) {
        return -e;
    }

    public static Expr exp(Expr e) {
        return e.exp();
    }

    public static Expr atan(Expr e) {
        return e.atan();
    }

    public static Expr acotan(Expr e) {
        return e.acotan();
    }

    public static Expr acos(Expr e) {
        return e.acos();
    }

    public static Expr asin(Expr e) {
        return e.asin();
    }

    public static Complex integrate(Expr e) {
        return integrate(e, null);
    }

    public static Complex integrate(Expr e, Domain domain) {
        return Config.getIntegrationOperator().eval(domain, e);
    }

    public static Expr esum(int size, VectorCell<Expr> f) {
        return Maths.esum(seq(size, f));
    }

    public static Expr esum(VectorModel<Expr> arr) {
        return MathsExpr.esum(arr);
    }

    public static Vector<Expr> seq(int size1, VectorCell<Expr> f) {
        return new ReadOnlyVector<Expr>($EXPR, false, new VectorModelFromCell(size1, f));
    }

    public static Expr esum(int size1, int size2, MatrixCell<Expr> e) {
        RepeatableOp<Expr> c = EXPR_VECTOR_SPACE.addRepeatableOp();
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                c.append(e.get(i, j));
            }
        }
        return c.eval();
    }

    public static Complex csum(int size1, int size2, MatrixCell<Complex> e) {
        MutableComplex c = new MutableComplex();
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                c.add(e.get(i, j));
            }
        }
        return c.toComplex();
    }

    public static Vector<Expr> seq(int size1, int size2, MatrixCell<Expr> f) {
        int sizeFull = size1 * size2;
        VectorModel<Expr> tVectorModel = new VectorModel<Expr>() {
            @Override
            public int size() {
                return sizeFull;
            }

            @Override
            public Expr get(int index) {
                return f.get(index / size2, index % size2);
            }
        };
        return new ReadOnlyVector<Expr>($EXPR, false, tVectorModel);
    }

    public static ComplexMatrix scalarProductCache(Expr[] gp, Expr[] fn, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, AxisXY.XY, Config.getScalarProductOperator(), monitor);
    }

    private static ComplexMatrix resolveBestScalarProductCache(Expr[] gp, Expr[] fn, AxisXY axis, ScalarProductOperator sp, ProgressMonitor monitor) {
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
        ExprType narrowestType = ExprDefaults.widest(ExprDefaults.narrowest(fn), ExprDefaults.narrowest(gp));

        boolean doubleValue = narrowestType.out().nbr() == ExprNumberType.DOUBLE_TYPE;
        boolean scalarValue = narrowestType.out().dim() == ExprDim.SCALAR;
//        int maxF = fn.length;
//        int maxG = gp.length;

        if (!Config.memoryCanStores(24L * rows * columns)) {
            return new MatrixScalarProductCache(Config.getLargeMatrixFactory()).evaluate(sp, fn, gp, axis, monitor).toMatrix();
        }
        if (doubleValue) {
            return new MemDoubleScalarProductCache(scalarValue).evaluate(sp, fn, gp, axis, monitor).toMatrix();
        }
        return new MemComplexScalarProductCache(sp.isHermitian(), doubleValue, scalarValue).evaluate(sp, fn, gp, axis, monitor).toMatrix();
    }

    public static ComplexMatrix scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, AxisXY.XY, Config.getScalarProductOperator(), monitor);
    }

    public static ComplexMatrix scalarProductCache(ScalarProductOperator sp, Expr[] gp, Expr[] fn, AxisXY axis, ProgressMonitor monitor) {
        return resolveBestScalarProductCache(gp, fn, axis, sp, monitor);
    }

    public static ComplexMatrix scalarProductCache(Expr[] gp, Expr[] fn, AxisXY axis, ProgressMonitor monitor) {
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

    public static Expr gateX(double a, double b) {
        return expr(1, Domain.ofBounds(a, b));
    }

    public static Expr gateY(double a, double b) {
        return expr(1, Domain.ofBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, a, b));
    }

    public static Expr gateZ(double a, double b) {
        return expr(1, Domain.ofBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, a, b));
    }

    public static DoubleToDouble expr(double value, Domain domain) {
        if (domain == null || domain.isUnbounded1()) {
            return DoubleExpr.of(value);
        }
        return DefaultDoubleValue.of(value, domain.getDomain());
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
        throw new IllegalArgumentException("Unsupported axis " + axis);
    }

    public static double scalarProduct(DoubleToDouble f1, DoubleToDouble f2) {
        return Config.getScalarProductOperator().evalDD(null, f1, f2);
    }

    public static ComplexVector scalarProduct(Expr f1, Vector<Expr> f2) {
        VectorCell<Complex> spfact = index -> scalarProduct(f1, f2.get(index)).toComplex();
        return f2.isColumn() ? columnVector(f2.size(), spfact) : rowVector(f2.size(), spfact);
    }

    public static NumberExpr scalarProduct(Expr f1, Expr f2) {
        return Config.getScalarProductOperator().eval(f1, f2);
    }

    public static ComplexVector columnVector(int rows, VectorCell<Complex> cellFactory) {
        return DefaultComplexVector.Column(rows, cellFactory);
    }

    public static ComplexVector rowVector(int columns, VectorCell<Complex> cellFactory) {
        return DefaultComplexVector.Row(columns, cellFactory);
    }

    public static ComplexVector columnVector(Complex... elems) {
        return DefaultComplexVector.Column(elems);
    }

    //</editor-fold>
    //<editor-fold desc="Vector functions">
    public static ComplexVector rowVector(Complex[] elems) {
        return DefaultComplexVector.Row(elems);
    }

    public static ComplexMatrix scalarProduct(Expr f1, Matrix<Expr> f2) {
        return matrix(f2.getRowCount(), f2.getColumnCount(), new MatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return scalarProduct(f1, f2.get(row, column)).toComplex();
            }
        });
    }

    public static ComplexMatrix matrix(int rows, int cols, MatrixCell<Complex> cellFactory) {
        return Config.getComplexMatrixFactory().newMatrix(rows, cols, cellFactory);
    }

    public static ComplexVector scalarProduct(Vector<Expr> f2, Expr f1) {
        VectorCell<Complex> spfact = new VectorCell<Complex>() {
            @Override
            public Complex get(int index) {
                return scalarProduct(f2.get(index), f1).toComplex();
            }
        };
        return f2.isColumn() ? columnVector(f2.size(), spfact) : rowVector(f2.size(), spfact);
    }

    public static ComplexMatrix scalarProduct(Matrix<Expr> f2, Expr f1) {
        return matrix(f2.getRowCount(), f2.getColumnCount(), new MatrixCell<Complex>() {
            @Override
            public Complex get(int row, int column) {
                return scalarProduct(f2.get(row, column), f1).toComplex();
            }
        });
    }

    public static NumberExpr scalarProduct(Domain domain, Expr f1, Expr f2) {
        return Config.getScalarProductOperator().eval(domain, f1, f2);
    }

    public static ComplexMatrix scalarProductMatrix(Vector<Expr> g, Vector<Expr> f) {
        return matrix(Config.getScalarProductOperator().eval(g, f, null));
    }

    public static ComplexMatrix matrix(ComplexMatrix matrix) {
        return Config.getComplexMatrixFactory().newMatrix(matrix);
    }

    public static ComplexMatrix matrix(Matrix t) {
        return (ComplexMatrix) t.to($COMPLEX);
    }

    public static ComplexMatrix scalarProduct(Vector<Expr> g, Vector<Expr> f) {
        return Config.getScalarProductOperator().eval(g, f, null);
    }

    public static ComplexMatrix scalarProduct(Vector<Expr> g, Vector<Expr> f, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, monitor);
    }

    public static ComplexMatrix scalarProductMatrix(Vector<Expr> g, Vector<Expr> f, ProgressMonitor monitor) {
        return matrix(Config.getScalarProductOperator().eval(g, f, monitor));
    }

    public static ComplexMatrix scalarProduct(Vector<Expr> g, Vector<Expr> f, AxisXY axis, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, axis, monitor);
    }

    public static ComplexMatrix scalarProductMatrix(Expr[] g, Expr[] f) {
        return (ComplexMatrix) Config.getScalarProductOperator().eval(g, f, null).to($COMPLEX);
    }

    public static Complex scalarProduct(ComplexMatrix g, ComplexMatrix f) {
        return g.scalarProduct(f);
    }

    public static Expr scalarProduct(ComplexMatrix g, Vector<Expr> f) {
        return f.scalarProduct(g.to($EXPR));
    }

    public static Expr scalarProductAll(ComplexMatrix g, Vector<Expr>... f) {
        return g.toVector().to($EXPR).scalarProductAll(f);
    }

    public static ComplexMatrix scalarProduct(Expr[] g, Expr[] f) {
        return Config.getScalarProductOperator().eval(g, f, null);
    }

    public static ComplexMatrix scalarProduct(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, monitor);
    }

    public static ComplexMatrix scalarProductMatrix(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return matrix(Config.getScalarProductOperator().eval(g, f, monitor));
    }

    public static ComplexMatrix scalarProduct(Expr[] g, Expr[] f, AxisXY axis, ProgressMonitor monitor) {
        return Config.getScalarProductOperator().eval(g, f, axis, monitor);
    }

    public static ExprVector evector(Expr... vector) {
        return DefaultExprVector.of(false, vector);
    }

    public static ComplexVector cvector(Vector<Expr> vector) {
        return DefaultComplexVector.Column(new VectorModel<Complex>() {
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

    public static Vector<Complex> cvector() {
        return DefaultComplexVector.Column();
    }

    public static Vector<Complex> cvector(Complex... vector) {
        return DefaultComplexVector.Column(vector);
    }

    public static Vector<ComplexMatrix> mvector() {
        return Maths.vector($MATRIX, false, 0);
    }

    public static Vector<ComplexMatrix> mvector(int size) {
        return Maths.vector($MATRIX, false, size);
    }

    public static Vector<ComplexMatrix> mvector(ComplexMatrix... items) {
        Vector<ComplexMatrix> vec = Maths.vector($MATRIX, false, items.length);
        vec.appendAll(Arrays.asList(items));
        return vec;
    }

    public static Vector<Vector<Complex>> cvector2() {
        return vector($CLIST, false, 0);
    }

    public static Vector<Vector<Expr>> evector2() {
        return Maths.vector($EVECTOR, false, 0);
    }

    public static Vector<Vector<Double>> dvector2() {
        return Maths.vector($DVECTOR, false, 0);
    }

    public static Vector<Vector<Integer>> ivector2() {
        return Maths.vector($IVECTOR, false, 0);
    }

    public static Vector<Vector<ComplexMatrix>> mvector2() {
        return Maths.vector($MVECTOR, false, 0);
    }

    public static Vector<Vector<Boolean>> bvector2() {
        return vector($BVECTOR, false, 0);
    }

    public static <T> Vector<T> vector(TypeName<T> typeName, int initialSize) {
        return vector(typeName, false, initialSize);
    }

    public static <T> Vector<T> vectorro(TypeName<T> typeName, boolean row, VectorModel<T> model) {
        if (typeName.equals(Maths.$DOUBLE)) {
            return (Vector<T>) new ArrayDoubleVector.ReadOnlyDoubleVector(row, (VectorModel<Double>) model);
        }
        if (typeName.equals(Maths.$INTEGER)) {
            return (Vector<T>) new ArrayIntVector.ReadOnlyIntVector(row, (VectorModel<Integer>) model);
        }
        if (typeName.equals(Maths.$LONG)) {
            return (Vector<T>) new ArrayLongVector.ReadOnlyLongVector(row, (VectorModel<Long>) model);
        }
        if (typeName.equals(Maths.$BOOLEAN)) {
            return (Vector<T>) new ArrayBooleanVector.ReadOnlyBooleanVector(row, (VectorModel<Boolean>) model);
        }
        return new ReadOnlyVector<T>(typeName, row, model);
    }

    public static <T> Vector<T> list(Vector<T> vector) {
        Vector<T> exprs = vector(vector.getComponentType());
        for (T o : vector) {
            exprs.append(o);
        }
        return exprs;
    }

    public static <T> Vector<T> vector(TypeName<T> typeName) {
        return vector(typeName, false, 0);
    }

    public static ExprVector evector(ComplexMatrix vector) {
        ComplexVector complexes = vector.toVector();
        ExprVector exprs = evector(complexes.size());
        exprs.appendAll((Vector) complexes);
        return exprs;
    }

    //    public static String scalarProductToMatlabString(DFunctionXY f1, DFunctionXY f2, DomainXY domain0, ToMatlabStringParam... format) {
//        return defaultScalarProduct.scalarProductToMatlabString(domain0, f1, f2, format) ;
//    }
//
//    public static String scalarProductToMatlabString(DomainXY domain0,CFunctionXY f1, CFunctionXY f2, ToMatlabStringParam... format) {
//        return defaultScalarProduct.scalarProductToMatlabString(domain0, f1, f2, format) ;
//    }
    public static ExprVector evector(int size) {
        return DefaultExprVector.of(false, size);
    }

    public static <T> Vector<T> vscalarProduct(Vector<T> vector, Vector<Vector<T>> vectors) {
        return vector.vscalarProduct(vectors.toArray(new Vector[0]));
    }

    public static ExprVector evector() {
        return DefaultExprVector.of(false, 0);
    }

    public static <T> Vector<T> concat(Vector<T>... a) {
        Vector<T> ts = vector(a[0].getComponentType());
        for (Vector<T> t : a) {
            ts.appendAll(t);
        }
        return ts;
    }

    public static Vector<Double> dvector() {
        return new ArrayDoubleVector();
    }

    public static Vector<Double> dvector(ToDoubleArrayAware items) {
        return dvector(items.toDoubleArray());
    }

    public static Vector<Double> dvector(double[] items) {
        DoubleVector doubles = new ArrayDoubleVector(items.length);
        doubles.appendAll(items);
        return doubles;
    }

    public static Vector<Double> dvector(int size) {
        return new ArrayDoubleVector(size);
    }

    public static Vector<String> slist() {
        return new ArrayVector<String>($STRING, false, 0);
    }

    public static Vector<String> slist(String[] items) {
        Vector<String> doubles = new ArrayVector<String>($STRING, false, items.length);
        for (String item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static Vector<String> slist(boolean row, int size) {
        return new ArrayVector<String>($STRING, row, size);
    }

    public static Vector<String> slist(int size) {
        return new ArrayVector<String>($STRING, false, size);
    }

    public static Vector<Boolean> bvector() {
        return new ArrayBooleanVector(false, 0);
    }

    public static Vector<Boolean> dvector(boolean[] items) {
        Vector<Boolean> doubles = new ArrayBooleanVector(false, items.length);
        for (boolean item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static Vector<Boolean> bvector(int size) {
        return new ArrayBooleanVector(false, size);
    }

    public static IntVector ivector() {
        return new ArrayIntVector(false, 0);
    }

    public static Vector<Integer> ivector(int[] items) {
        Vector<Integer> doubles = new ArrayIntVector(false, items.length);
        for (int item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static Vector<Integer> ivector(int size) {
        return new ArrayIntVector(false, size);
    }

    public static LongVector lvector() {
        return new ArrayLongVector(false, 0);
    }

    public static Vector<Long> lvector(long[] items) {
        Vector<Long> doubles = new ArrayLongVector(false, items.length);
        for (long item : items) {
            doubles.append(item);
        }
        return doubles;
    }

    public static Vector<Long> lvector(int size) {
        return new ArrayLongVector(false, size);
    }

    public static <T> T sum(TypeName<T> ofType, T... arr) {
        return MathsExpr.sum(ofType, arr);
    }

    public static <T> T sum(TypeName<T> ofType, VectorModel<T> arr) {
        return MathsExpr.sum(ofType, arr);
    }

    public static <T> T sum(TypeName<T> ofType, int size, VectorCell<T> arr) {
        return MathsExpr.sum(ofType, size, arr);
    }

    public static <T> T mul(TypeName<T> ofType, T... arr) {
        return MathsExpr.mul(ofType, arr);
    }

    public static <T> T mul(TypeName<T> ofType, VectorModel<T> arr) {
        return MathsExpr.mul(ofType, arr);
    }

    public static Complex avg(CDiscrete d) {
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

    public static <T> Matrix<T> mul(Matrix<T> a, Matrix<T> b) {
        return a.mul(b);
    }

    public static ComplexMatrix mul(ComplexMatrix a, ComplexMatrix b) {
        return a.mul(b);
    }

    public static Vector<Expr> edotmul(Vector<Expr>... arr) {
        return MathsExpr.edotmul(arr);
    }

    public static Vector<Expr> edotdiv(Vector<Expr>... arr) {
        return MathsExpr.edotdiv(arr);
    }

    public static Complex cmul(VectorModel<Complex> arr) {
        return MathsExpr.cmul(arr);
    }

    public static Expr emul(VectorModel<Expr> arr) {
        return MathsExpr.emul(arr);
    }

    public static Expr prod(Expr... e) {
        return MathsExpr.mul(e);
    }

    public static Expr pow(Expr a, Expr b) {
        return a.pow(b);
    }

    public static Expr plus(Expr a, double b) {
        return a.plus(b);
    }

    public static Expr plus(Expr a, Expr b) {
        return a.plus(b);
    }

    public static Expr mul(Expr a, Expr b) {
        return a.mul(b);
    }

    public static Complex mul(Complex a, Complex b) {
        return a.mul(b);
    }

    public static Expr mul(Expr a, Complex b) {
        return a.mul(b);
    }

    public static Expr minus(Expr a, double b) {
        return minus(a, expr(b));
    }

    public static Expr minus(Expr a, Expr b) {
        return a.sub(b);
    }

    public static Expr div(Expr a, double b) {
        return div(a, expr(b));
    }

    public static Expr div(Expr a, Expr b) {
        return a.div(b);
    }

    public static Expr add(Expr... a) {
        return MathsExpr.add(a);
    }

    public static Expr rem(Expr a, Expr b) {
        return a.rem(b);
    }

    public static <T> Vector<Expr> expr(Vector<T> vector) {
        return vector.to($EXPR);
    }

    public static Matrix<Expr> expr(Matrix<Complex> matrix) {
        return new ReadOnlyMatrix<Expr>($EXPR, new MatrixModel<Expr>() {
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

    public static <T> Matrix<T> tmatrix(TypeName<T> ofType, int rows, int columns, MatrixCell<T> model) {
        return tmatrix(ofType, new MatrixCellToModel<>(rows, columns, model));
    }

    public static <T> Matrix<T> tmatrix(TypeName<T> ofType, MatrixModel<T> model) {
        return new ReadOnlyMatrix<T>(ofType, model);
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
        if (a instanceof Vector) {
            return (T) simplify((Vector) a, simplifyOptions);
        }
        return a;
    }

    public static Expr simplify(Expr a) {
        return a.simplify();
    }

    public static Expr simplify(Expr a, SimplifyOptions simplifyOptions) {
        return a.simplify(simplifyOptions);
    }

    public static <T> Vector<T> normalize(Vector<T> a) {
        if (a instanceof ExprVector) {
            return (Vector<T>) ((ExprVector) a).normalize();
        }
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                Expr n = normalize((Expr) e).simplify();
                return (T) n;
            }
        });
    }

    public static Expr normalize(Expr a) {
        return a.normalize();
    }

    public static double norm(Expr a) {
        return a.norm();
    }

    public static Expr mul(Expr a, double b) {
        return a.mul(b);
    }

    public static Expr mul(double a, Expr b) {
        return expr(a).mul(b);
    }

    public static Expr mul(Complex a, Expr b) {
        return expr(a).mul(b);
    }

    public static Expr sqrt(Expr e) {
        return e.sqrt();
    }

    public static Complex sqrt(Complex d) {
        return d.sqrt();
    }

    public static Expr normalize(Geometry a) {
        return normalize(expr(a));
    }

    public static DoubleToDouble expr(Geometry domain) {
        return expr(1, domain);
    }

    public static DoubleToDouble expr(double value, Geometry geometry) {
        if (geometry == null) {
            geometry = Domain.FULLXY.toGeometry();
        }
        if (geometry.isRectangular()) {
            return DefaultDoubleValue.of(value, geometry.getDomain());
        }
        return new Shape2D(value, geometry);
    }

    public static Expr vector(Expr fx) {
        if (fx.isZero()) {
            return DVZERO1;
        }
        return DefaultDoubleToVector.of(fx.toDC());
    }

    public static <T> Vector<T> cos(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cos());
    }

    public static <T> Vector<T> cosh(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cosh());
    }

    public static <T> Vector<T> sin(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sin());
    }

    public static <T> Vector<T> sinh(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sinh());
    }

    public static <T> Vector<T> tan(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().tan());
    }

    public static <T> Vector<T> tanh(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().tanh());
    }

    public static <T> Vector<T> cotan(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cotan());
    }

    public static <T> Vector<T> cotanh(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().cotanh());
    }

    public static <T> Vector<T> sqr(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sqr());
    }

    public static <T> Vector<T> sqrt(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().sqrt());
    }

    public static <T> Vector<T> inv(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().inv());
    }

    public static <T> Vector<T> neg(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().neg());
    }

    public static <T> Vector<T> exp(Vector<T> a) {
        return a.eval(getVectorSpace(a.getComponentType()).ops().exp());
    }

    public static <T> Vector<T> simplify(Vector<T> a) {
        return simplify(a, null);
    }

    public static <T> Vector<T> simplify(Vector<T> a, SimplifyOptions options) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return simplify(e, options);
            }
        });
    }

    public static <T> Vector<T> addAll(Vector<T> e, T... expressions) {
        TypeName<T> st = e.getComponentType();
        Vector<T> n = vector(st);
        VectorSpace<T> s = getVectorSpace(st);
        for (T x : e) {
            T t = sum(st, expressions);
            n.append(s.add(x, t));
        }
        return n;
    }

    public static <T> Vector<T> mulAll(Vector<T> e, T... expressions) {
        TypeName<T> st = e.getComponentType();
        Vector<T> n = vector(st);
        VectorSpace<T> s = getVectorSpace(st);
        for (T x : e) {
            T t = mul(st, expressions);
            n.append(s.mul(x, t));
        }
        return n;
    }

    public static <T> Vector<T> pow(Vector<T> a, T b) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.pow(e, b);
            }
        });
    }

    public static <T> Vector<T> minus(Vector<T> a, T b) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.minus(e, b);
            }
        });
    }

    public static <T> Vector<T> div(Vector<T> a, T b) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.div(e, b);
            }
        });
    }

    public static <T> Vector<T> rem(Vector<T> a, T b) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.rem(e, b);
            }
        });
    }

    public static <T> Vector<T> add(Vector<T> a, T b) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.add(e, b);
            }
        });
    }

    public static <T> Vector<T> mul(Vector<T> a, T b) {
        return a.eval(new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                VectorSpace<T> vectorSpace = getVectorSpace(a.getComponentType());
                return vectorSpace.mul(e, b);
            }
        });
    }

    //</editor-fold>
    /////////////////////////////////////////////////////////////////
    // general purpose functions

    /// //////////////////////////////////////////////////////////////
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

    public static MemoryInfo memoryInfo() {
        return new MemoryInfo();
    }

    public static String formatMetric(double value) {
        return Config.getMetricFormatter().format(value);
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
        return UnsafeHandler.get().sizeOf(src);
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
        return ProgressMonitors.invokeMonitoredAction(mon, messagePrefix, run);
    }

    public static Chronometer chrono(String name) {
        return new Chronometer(name);
    }

    public static Chronometer chrono(String name, Runnable r) {
        PlatformUtils.gc2();
        MemoryInfo memoryInfoBefore = Maths.memoryInfo();
        Chronometer c = Chronometer.start();
        r.run();
        c.stop();
        MemoryInfo memoryInfoAfter = Maths.memoryInfo();

        PlatformUtils.gc2();
        $log.log(Level.INFO, name + " : time= " + c.toString() + "  mem-usage= " + Maths.formatMemory(memoryInfoAfter.diff(memoryInfoBefore).inUseMemory()));
        return c;
    }

    public static String formatMemory(long bytes) {
        return Config.getMemorySizeFormatter().format(bytes);
    }

    public static <V> V chrono(String name, Callable<V> r) {
//        System.out.println("Start "+name);
        PlatformUtils.gc2();
        MemoryInfo memoryInfoBefore = Maths.memoryInfo();
        Chronometer c = Chronometer.start();
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
        Chronometer c = Chronometer.start();
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
        return coll.toArray((T[]) Array.newInstance(t, coll.size()));
    }

    public static <T> T[] toArray(TypeName<T> t, Collection<T> coll) {
        return coll.toArray((T[]) Array.newInstance(t.getTypeClass(), coll.size()));
    }

    public static double rerr(double a, double b) {
        return MathsArrays.rerr(a, b);
    }

    public static double rerr(Complex a, Complex b) {
        return MathsArrays.rerr(a, b);
    }

    public static CustomCCFunctionXExpr define(String name, FunctionCCX f) {
        return new CustomCCFunctionXDefinition(name, f).fct();
    }

    public static CustomDCFunctionXExpr define(String name, FunctionDCX f) {
        return new CustomDCFunctionXDefinition(name, f).fct();
    }

    public static CustomDDFunctionXExpr define(String name, FunctionDDX f) {
        return new CustomDDFunctionXDefinition(name, f).fct();
    }

    public static CustomDDFunctionXYExpr define(String name, FunctionDDXY f) {
        return new CustomDDFunctionXYDefinition(name, f).fct();
    }

    public static CustomDCFunctionXYExpr define(String name, FunctionDCXY f) {
        return new CustomDCFunctionXYDefinition(name, f).fct();
    }

    public static CustomCCFunctionXYExpr define(String name, FunctionCCXY f) {
        return new CustomCCFunctionXYDefinition(name, f).fct();
    }

    public static double rerr(ComplexMatrix a, ComplexMatrix b) {
        return b.getError(a);
    }

    public static <T extends Expr> DoubleVector toDoubleArray(Vector<T> c) {
        DoubleVector a = new ArrayDoubleVector(c.size());
        for (T o : c) {
            a.append(o.toDouble());
        }
        return a;
    }

    public static double toDouble(Complex c, ToDoubleFunction<Object> d) {
        if (d == null) {
            return c.absdbl();
        }
        return d.applyAsDouble(c);
    }

    public static Expr conj(Expr e) {
        switch (e.getType()) {
            case DOUBLE_DOUBLE:
                return e;
            case DOUBLE_COMPLEX:
                return e.toComplex().conj();
        }
        return Conj.of(e);
    }

    public static Expr complexValue(Complex c, Domain d) {
        return d.isUnbounded1() ? c : DefaultComplexValue.of(c, d);
    }

    public static Complex complex(Matrix t) {
        return t.toComplex();
    }
    //</editor-fold>

    public static Matrix<Expr> ematrix(Matrix t) {
        return new EMatrixFromMatrix(t);
    }

    public static <T> VectorSpace<T> getVectorSpace(TypeName<T> cls) {
        if ($COMPLEX.isAssignableFrom(cls)) {
            return (VectorSpace<T>) Maths.COMPLEX_VECTOR_SPACE;
        }
        if ($DOUBLE.isAssignableFrom(cls)) {
            return (VectorSpace<T>) Maths.DOUBLE_VECTOR_SPACE;
        }
        if ($EXPR.isAssignableFrom(cls)) {
            return (VectorSpace<T>) Maths.EXPR_VECTOR_SPACE;
        }
        if (ComplexMatrix.class.isAssignableFrom(cls.getTypeClass())) {
            return new MatrixVectorSpace($COMPLEX, COMPLEX_VECTOR_SPACE);
        }
        if (ExprMatrix.class.isAssignableFrom(cls.getTypeClass())) {
            return new MatrixVectorSpace($EXPR, EXPR_VECTOR_SPACE);
        }
        if (Matrix.class.isAssignableFrom(cls.getTypeClass())) {
            TypeName ii = cls.getParameters()[0];
            return new MatrixVectorSpace(ii, getVectorSpace(ii));
        }
        throw new NoSuchElementException("Vector space Not yet supported for " + cls);
    }

    public static DoubleVector refineSamples(Vector<Double> values, int n) {
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

    public static String getHadrumathsVersion() {
        return HadrumathsInitializerService.getVersion();
    }

    public static ComponentDimension expandComponentDimension(ComponentDimension d1, ComponentDimension d2) {
        return ComponentDimension.of(
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
        if (d.equals(ComponentDimension.SCALAR)) {
            switch (e.getType()) {
                case DOUBLE_NBR:
                case DOUBLE_EXPR:
                case DOUBLE_DOUBLE:
                case COMPLEX_NBR:
                case COMPLEX_EXPR:
                case DOUBLE_COMPLEX: {
                    return DefaultDoubleToVector.of(e.toDC());
                }
                case CVECTOR_NBR:
                case CVECTOR_EXPR:
                case DOUBLE_CVECTOR: {
                    DoubleToVector dv = e.toDV();
                    int cv = dv.getComponentSize();
                    switch (cv) {
                        case 0:
                            return DefaultDoubleToVector.of(Domain.FULLX.toDC());
                        case 1:
                            return DefaultDoubleToVector.of(dv.getComponent(Axis.X).toDC());
                    }
                    break;
                }
                case CMATRIX_NBR:
                case CMATRIX_EXPR:
                case DOUBLE_CMATRIX: {
                    DoubleToMatrix dv = e.toDM();
                    ComponentDimension cv = dv.getComponentDimension();
                    if (cv.columns == 1) {
                        switch (cv.rows) {
                            case 0:
                                return DefaultDoubleToVector.of(Domain.FULLX.toDC());
                            case 1:
                                return DefaultDoubleToVector.of(dv.getComponent(0, 0).toDC());
                        }
                    }
                    break;
                }
            }
        } else if (d.equals(ComponentDimension.VECTOR2)) {
            switch (e.getType()) {
                case DOUBLE_NBR:
                case DOUBLE_EXPR:
                case DOUBLE_DOUBLE:
                case COMPLEX_NBR:
                case COMPLEX_EXPR:
                case DOUBLE_COMPLEX: {
                    return DefaultDoubleToVector.of(e.toDC(), Domain.FULLX.toDC());
                }
                case CVECTOR_NBR:
                case CVECTOR_EXPR:
                case DOUBLE_CVECTOR: {
                    DoubleToVector dv = e.toDV();
                    int cv = dv.getComponentSize();
                    switch (cv) {
                        case 0:
                            return DefaultDoubleToVector.of(Domain.FULLX.toDC(), Domain.FULLX.toDC());
                        case 1:
                            return DefaultDoubleToVector.of(dv.getComponent(Axis.X).toDC(), Domain.FULLX.toDC());
                        case 2:
                            return dv;
                    }
                    break;
                }
                case CMATRIX_NBR:
                case CMATRIX_EXPR:
                case DOUBLE_CMATRIX: {
                    DoubleToMatrix dv = e.toDM();
                    ComponentDimension cv = dv.getComponentDimension();
                    if (cv.columns == 1) {
                        switch (cv.rows) {
                            case 0:
                                return DefaultDoubleToVector.of(Domain.FULLX.toDC(), Domain.FULLX.toDC());
                            case 1:
                                return DefaultDoubleToVector.of(dv.getComponent(0, 0).toDC(), Domain.FULLX.toDC());
                            case 2:
                                return DefaultDoubleToVector.of(dv.getComponent(0, 0).toDC(), dv.getComponent(1, 0).toDC());
                        }
                    }
                    break;
                }
            }
        } else if (d.equals(ComponentDimension.VECTOR3)) {
            switch (e.getType()) {
                case DOUBLE_NBR:
                case DOUBLE_EXPR:
                case DOUBLE_DOUBLE:
                case COMPLEX_NBR:
                case COMPLEX_EXPR:
                case DOUBLE_COMPLEX: {
                    return DefaultDoubleToVector.of(e.toDC(), Domain.FULLX.toDC(), Domain.FULLX.toDC());
                }
                case CVECTOR_NBR:
                case CVECTOR_EXPR:
                case DOUBLE_CVECTOR: {
                    DoubleToVector dv = e.toDV();
                    int cv = dv.getComponentSize();
                    switch (cv) {
                        case 0:
                            return DefaultDoubleToVector.of(Domain.FULLX.toDC(), Domain.FULLX.toDC(), Domain.FULLX.toDC());
                        case 1:
                            return DefaultDoubleToVector.of(dv.getComponent(Axis.X).toDC(), Domain.FULLX.toDC(), Domain.FULLX.toDC());
                        case 2:
                            return DefaultDoubleToVector.of(dv.getComponent(Axis.X).toDC(), dv.getComponent(Axis.Y).toDC(), Domain.FULLX.toDC());
                        case 3:
                            return dv;
                    }
                    break;
                }
                case CMATRIX_NBR:
                case CMATRIX_EXPR:
                case DOUBLE_CMATRIX: {
                    DoubleToMatrix dv = e.toDM();
                    ComponentDimension cv = dv.getComponentDimension();
                    if (cv.columns == 1) {
                        switch (cv.rows) {
                            case 0:
                                return DefaultDoubleToVector.of(Domain.FULLX.toDC(), Domain.FULLX.toDC());
                            case 1:
                                return DefaultDoubleToVector.of(dv.getComponent(0, 0).toDC(), Domain.FULLX.toDC(), Domain.FULLX.toDC());
                            case 2:
                                return DefaultDoubleToVector.of(dv.getComponent(0, 0).toDC(), dv.getComponent(1, 0).toDC(), Domain.FULLX.toDC());
                            case 3:
                                return DefaultDoubleToVector.of(dv.getComponent(0, 0).toDC(), dv.getComponent(1, 0).toDC(), dv.getComponent(2, 0).toDC());
                        }
                    }
                    break;
                }
            }
        }
        throw new IllegalArgumentException("Unable to expandComponentDimension component dim " + d0 + " -> " + d);
    }

    public static Expr vector(Expr fx, Expr fy) {
        if (fx.isZero() && fy.isZero()) {
            return DVZERO2;
        }
        return DefaultDoubleToVector.of(fx.toDC(), fy.toDC());
    }

    public static Expr vector(Expr fx, Expr fy, Expr fz) {
        if (fx.isZero() && fy.isZero() && fz.isZero()) {
            return DVZERO3;
        }
        return DefaultDoubleToVector.of(fx.toDC(), fy.toDC(), fz.toDC());
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

    //    private static class StringTypeReference extends TypeName<String> {
//    }
//
//    private static class MatrixTypeReference extends TypeName<Matrix> {
//    }
//
//    private static class VectorTypeReference extends TypeName<Vector> {
//    }
//
//    private static class TMatrixTypeReference extends TypeName<Matrix<Complex>> {
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

    public static JContext createExpressionEvaluator() {
        return ExpressionManagerFactory.createEvaluator();
    }

    public static Expr evalExpression(String expression) {
        JContext m = ExpressionManagerFactory.createEvaluator();
        Object evaluated = m.evaluate(expression, null);
        if (evaluated instanceof Expr) {
            return (Expr) evaluated;
        }
        if (evaluated instanceof Number) {
            return expr(((Number) evaluated).doubleValue());
        }
        return (Expr) evaluated;
    }

    public static JContext getExpressionParser() {
        return ExpressionManagerFactory.createParser();
    }

    public static double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double toDegrees(double radians) {
        return Math.toDegrees(radians);
    }

    public static double[] toRadians(double[] degrees) {
        double[] b = new double[degrees.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = Math.toRadians(degrees[i]);
        }
        return b;
    }

    public static double[] toDegrees(double[] radians) {
        double[] b = new double[radians.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = Math.toDegrees(radians[i]);
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
                return s.intValue();
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
                return s.intValue();
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

    /**
     * solve ax2+bx+c
     *
     * @param a a number
     * @param b b number
     * @param c c number
     * @return solutions
     */
    public static Complex[] solvePoly2(double a, double b, double c) {
        double delta = sqr(b) - 4 * a * c;
        if (delta == 0) {
            return new Complex[]{
                    complex(-b).div(2 * a)
            };
        }
        return new Complex[]{
                complex(-b).minus(csqrt(delta)).div(2 * a),
                complex(-b).plus(csqrt(delta)).div(2 * a),
        };
    }

    /**
     * solve ax2+bx+c
     *
     * @param a a number
     * @param b b number
     * @param c c number
     * @return solutions
     */
    public static double[] solvePoly2dbl(double a, double b, double c) {
        double delta = sqr(b) - 4 * a * c;
        if (delta < 0) {
            return new double[0];
        }
        double d1 = sqrt(delta);
        return new double[]{
                (-b - d1) / (2 * a),
                (-b + d1) / (2 * a),
        };
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
        if (e == null) {
            return null;
        }
        if (e instanceof ComplexMatrix) {
            return null;
        }
        e = e.simplify();
        if (e instanceof ComplexMatrix) {
            return null;
        }
        throw new ClassCastException("Cannot cast to Matrix " + e);
    }

    public static PersistenceCacheBuilder persistenceCache() {
        return new PersistenceCacheBuilder();
    }

    public static DoubleToDouble DZERO(int dim) {
        switch (dim) {
            case 1: {
                return DZEROX;
            }
            case 2: {
                return DZEROXY;
            }
            case 3: {
                return DZEROXYZ;
            }
        }
        throw new IllegalArgumentException("Invalid dimension " + dim);
    }

    public static DoubleToComplex CZERO(int dim) {
        switch (dim) {
            case 1: {
                return CZEROX;
            }
            case 2: {
                return CZEROXY;
            }
            case 3: {
                return CZEROXYZ;
            }
        }
        throw new IllegalArgumentException("Invalid dimension " + dim);
    }

    private static class IdentityConverter implements Function, Serializable {

        @Override
        public Object apply(Object value) {
            return value;
        }
    }

    private static class ComplexDoubleConverter implements Function<Complex, Double>, Serializable {

        @Override
        public Double apply(Complex value) {
            return value.toDouble();
        }
    }

    private static class DoubleComplexConverter implements Function<Double, Complex>, Serializable {

        @Override
        public Complex apply(Double value) {
            return Complex.of(value);
        }
    }

    private static class DoubleVectorConverter implements Function<Double, Vector>, Serializable {

        @Override
        public Vector apply(Double value) {
            return Maths.columnVector(Complex.of(value));
        }
    }

    private static class VectorDoubleConverter implements Function<Vector, Double>, Serializable {

        @Override
        public Double apply(Vector value) {
            return value.toComplex().toDouble();
        }
    }

    private static class ComplexTVectorConverter implements Function<Complex, Vector>, Serializable {

        @Override
        public Vector apply(Complex value) {
            return Maths.columnVector(value);
        }
    }

    private static class VectorComplexConverter implements Function<Vector, Complex>, Serializable {

        @Override
        public Complex apply(Vector value) {
            return value.toComplex();
        }
    }

    private static class ComplexExprConverter implements Function<Complex, Expr>, Serializable {

        @Override
        public Expr apply(Complex value) {
            return value;
        }
    }

    private static class ExprComplexConverter implements Function<Expr, Complex>, Serializable {

        @Override
        public Complex apply(Expr value) {
            return value.toComplex();
        }
    }

    private static class DoubleExprConverter implements Function<Double, Expr>, Serializable {

        @Override
        public Expr apply(Double value) {
            return expr(value);
        }
    }

    private static class ExprDoubleConverter implements Function<Expr, Double>, Serializable {

        @Override
        public Double apply(Expr value) {
            return value.toComplex().toDouble();
        }
    }

    private static class ComplexMatrixStoreManager implements StoreManager<ComplexMatrix> {

        @Override
        public void store(ComplexMatrix item, File file) {
            item.store(file);
        }

        @Override
        public ComplexMatrix load(File file) {
            return Config.getComplexMatrixFactory().load(file);
        }
    }

    private static class MatrixStoreManager implements StoreManager<Matrix> {

        @Override
        public void store(Matrix item, File file) {
            item.store(file);
        }

        @Override
        public Matrix load(File file) {
            return Config.getComplexMatrixFactory().load(file);
        }
    }

    private static class VectorStoreManager implements StoreManager<Vector> {

        @Override
        public void store(Vector item, File file) {
            item.store(file);
        }

        @Override
        public Vector load(File file) {
            return Config.getComplexMatrixFactory().load(file).toVector();
        }
    }

    private static class ComplexVectorStoreManager implements StoreManager<ComplexVector> {

        @Override
        public void store(ComplexVector item, File file) {
            item.store(file);
        }

        @Override
        public ComplexVector load(File file) {
            return Config.getComplexMatrixFactory().load(file).toVector();
        }
    }

    private static class DoubleDistanceStrategy implements DistanceStrategy<Double> {

        @Override
        public double distance(Double a, Double b) {
            return Math.abs(b - a);
        }
    }

    private static class ComplexMatrixDistanceStrategy implements DistanceStrategy<ComplexMatrix> {

        @Override
        public double distance(ComplexMatrix a, ComplexMatrix b) {
            return a.getError(b);
        }
    }

    private static class ComplexVectorDistanceStrategy implements DistanceStrategy<ComplexVector> {

        @Override
        public double distance(ComplexVector a, ComplexVector b) {
            return a.toMatrix().getError(b.toMatrix());
        }
    }
}
