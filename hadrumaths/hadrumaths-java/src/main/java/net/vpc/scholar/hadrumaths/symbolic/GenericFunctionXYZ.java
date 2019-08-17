package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.NonStateField;

import java.io.Serializable;

/**
 * Created by vpc on 4/30/14.
 */
public abstract class GenericFunctionXYZ extends AbstractComposedFunction {
    private static final long serialVersionUID = 1L;
    private final FunctionType functionType;
    @NonStateField
    protected transient Domain _cache_domain;
    @NonStateField
    private Expressions.TernaryExprHelper<GenericFunctionXYZ> expr3Helper = new GenericFunctionXYBinaryExprHelper();

    private Expr xargument;
    private Expr yargument;
    private Expr zargument;

    protected GenericFunctionXYZ(String name, Expr xargument, Expr yargument, Expr zargument) {
        this(name, xargument, yargument, zargument, null);
    }

    protected GenericFunctionXYZ(String name, Expr xargument, Expr yargument, Expr zargument, FunctionType lowerFunctionType) {
        super();
        ComponentDimension d = Maths.expandComponentDimension(xargument.getComponentDimension(), yargument.getComponentDimension());
        d = Maths.expandComponentDimension(d, zargument.getComponentDimension());
        xargument = Maths.expandComponentDimension(xargument, d);
        yargument = Maths.expandComponentDimension(yargument, d);
        zargument = Maths.expandComponentDimension(zargument, d);
        this.xargument = xargument;
        this.yargument = yargument;
        this.zargument = zargument;
        FunctionType functionType0 = null;
        if (xargument.isDD()) {
            functionType0 = FunctionType.DOUBLE;
        } else if (xargument.isDC()) {
            functionType0 = FunctionType.COMPLEX;
        } else if (xargument.isDM()) {
            functionType0 = FunctionType.MATRIX;
        } else {
            throw new IllegalArgumentException("Unknown functionType");
        }
        if (yargument.isDD()) {
            functionType0 = FunctionType.DOUBLE;
        } else if (yargument.isDC()) {
            functionType0 = FunctionType.COMPLEX;
        } else if (yargument.isDM()) {
            functionType0 = FunctionType.MATRIX;
        } else {
            throw new IllegalArgumentException("Unknown functionType");
        }
        if (zargument.isDD()) {
            functionType0 = FunctionType.DOUBLE;
        } else if (zargument.isDC()) {
            functionType0 = FunctionType.COMPLEX;
        } else if (zargument.isDM()) {
            functionType0 = FunctionType.MATRIX;
        } else {
            throw new IllegalArgumentException("Unknown functionType");
        }
        if (lowerFunctionType == null) {
            this.functionType = functionType0;
        } else {
            if (functionType0.ordinal() > lowerFunctionType.ordinal()) {
                this.functionType = functionType0;
            } else {
                this.functionType = lowerFunctionType;
            }
        }
    }

    public Expr getXArgument() {
        return xargument;
    }

    public Expr getYArgument() {
        return yargument;
    }

    public Expr getZArgument() {
        return zargument;
    }

    @Override
    public Expr[] getArguments() {
        return new Expr[]{xargument, yargument, zargument};
    }

    @Override
    public boolean isDoubleTyped() {
        return functionType == FunctionType.DOUBLE;
    }

    @Override
    public final Domain getDomain() {
        if (!Maths.Config.isCacheExpressionPropertiesEnabled()) {
            return getDomainImpl();
        }
        if (_cache_domain == null) {
            _cache_domain = getDomainImpl();
        }
        return _cache_domain;
    }

    @Override
    public Domain getDomainImpl() {
        return getXArgument().getDomain().intersect(getYArgument().getDomain()).intersect(getZArgument().getDomain());
//        switch (this.functionType) {
//            case DOUBLE: {
//                if (computeDoubleArg(0) == 0) {
//                    return getXArgument().getDomain().intersect(getYArgument().getDomain());
//                }
//            }
//            case COMPLEX: {
//                if (evalComplex(Complex.ZERO).isZero()) {
//                    return getXArgument().getDomain().intersect(getYArgument().getDomain());
//                }
//            }
//            case MATRIX: {
//                if (evalMM(Complex.ZERO.toMatrix()).isZero()) {
//                    return getXArgument().getDomain().intersect(getYArgument().getDomain());
//                }
//            }
//        }
//        return Domain.FULL(getArgument().getDomainDimension());

    }

    @Override
    public ComponentDimension getComponentDimension() {
        return getXArgument().getComponentDimension();
    }

    @Override
    public int getDomainDimension() {
        return Math.max(getXArgument().getDomainDimension(), getYArgument().getDomainDimension());
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, expr3Helper, x, y, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, expr3Helper, x, d0, ranges);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, expr3Helper, x, y, z, d0, ranges);
    }

    @Override
    public DoubleToDouble getRealDD() {
        return new Real(this);
    }

    @Override
    public DoubleToDouble getImagDD() {
        return new Imag(this);
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, expr3Helper, x, y, d0, ranges);
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, expr3Helper, x, y, z, d0, ranges);
    }

//    @Override
//    public Complex computeComplex(double x, double y, BooleanMarker defined) {
//        //TODO
//        defined.set();
//        return computeComplex(new double[]{x}, new double[]{y})[0][0];
//    }

//    @Override
//    public double computeDouble(double x) {
//        return computeDouble(new double[]{x})[0];
//    }

//    @Override
//    public double computeDouble(double x, double y, BooleanMarker defined) {
//        //TODO
//        defined.set();
//        return computeDouble(new double[]{x}, new double[]{y}, null, null)[0][0];
//    }

//    @Override
//    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
//        //TODO
//        defined.set();
//        return computeDouble(new double[]{x}, new double[]{y}, new double[]{z}, null, null)[0][0][0];
//    }

//    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
//        if (contains(x, y, z)) {
//            Complex xx = getXArgument().toDC().computeComplex(x, y, z, defined);
//            Complex yy = getYArgument().toDC().computeComplex(x, y, z, defined);
//            Complex zz = getZArgument().toDC().computeComplex(x, y, z, defined);
//            return evalComplex(xx, yy, zz);
//        }
//        return Complex.ZERO;
//    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, expr3Helper, x, d0, ranges);
    }

    @Override
    public Matrix computeMatrix(double x, double y) {
        if (contains(x, y)) {
            Matrix xx = getXArgument().toDM().computeMatrix(x, y);
            Matrix yy = getYArgument().toDM().computeMatrix(x, y);
            Matrix zz = getZArgument().toDM().computeMatrix(x, y);
            return evalMatrix(xx, yy, zz);
        }
        return Complex.ZERO.toMatrix();
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        if (contains(x, y, z)) {
            Matrix xx = getXArgument().toDM().computeMatrix(x, y, z);
            Matrix yy = getYArgument().toDM().computeMatrix(x, y, z);
            Matrix zz = getZArgument().toDM().computeMatrix(x, y, z);
            return evalMatrix(xx, yy, zz);
        }
        return Complex.ZERO.toMatrix();
    }


    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, expr3Helper, x, y, d0, ranges);
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, expr3Helper, x, d0, ranges);
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, expr3Helper, x, y, z, d0, ranges);
    }

    @Override
    public Vector computeVector(double x, double y) {
        if (contains(x, y)) {
            Vector xx = getXArgument().toDV().computeVector(x, y);
            Vector yy = getYArgument().toDV().computeVector(x, y);
            Vector zz = getZArgument().toDV().computeVector(x, y);
            return evalVector(xx, yy, zz);
        }
        return Complex.ZERO.toVector();
    }

    @Override
    public Vector computeVector(double x, double y, double z) {
        if (contains(x, y, z)) {
            Vector xx = getXArgument().toDV().computeVector(x, y, z);
            Vector yy = getYArgument().toDV().computeVector(x, y, z);
            Vector zz = getZArgument().toDV().computeVector(x, y, z);
            return evalVector(xx, yy, zz);
        }
        return Complex.ZERO.toVector();
    }


    @Override
    public Vector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeVector(this, expr3Helper, x, y, d0, ranges);
    }

    @Override
    public Vector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeVector(this, expr3Helper, x, d0, ranges);
    }

    @Override
    public Vector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeVector(this, expr3Helper, x, y, z, d0, ranges);
    }

    protected Matrix evalMatrix(Matrix x, Matrix y, Matrix z) {
        int columnCount = x.getColumnCount();
        int rowCount = x.getRowCount();
        if (
                columnCount != y.getColumnCount()
                        || rowCount != y.getRowCount()
        ) {
            throw new IllegalArgumentException("Dimension mismatch");
        }
        return Maths.matrix(rowCount, columnCount, new MatrixCell() {
            @Override
            public Complex get(int row, int column) {
                return evalComplex(x.get(row, column), y.get(row, column), z.get(row, column));
            }
        });
    }

    protected Vector evalVector(Vector x, Vector y, Vector z) {
        int rowCount = x.size();
        if (
                rowCount != y.size()
        ) {
            throw new IllegalArgumentException("Dimension mismatch");
        }
        return Maths.columnVector(rowCount, new VectorCell() {
            @Override
            public Complex get(int row) {
                return evalComplex(x.get(row), y.get(row), z.get(row));
            }
        });
    }
//
//    public Complex computeComplexArg(Complex c) {
//        return evalComplex(c);
//    }

    /**
     * evaluate expression and return the most "reduced" type
     *
     * @param x
     * @param y
     * @return
     */
    protected abstract Complex evalComplex(Complex x, Complex y, Complex z);

    protected abstract Complex evalComplex(double x, double y, double z);

    protected abstract double evalDouble(double x, double y, double z);

    public abstract Expr newInstance(Expr xargument, Expr yargument, Expr zargument);

//    @Override
//    public String toString() {
//        return getFunctionName() + "(" + getXArgument() + "," + getYArgument() + "," + getZArgument() + ")";
//    }


    public Expr newInstance(Expr... arguments) {
        if (arguments.length != 3) {
            throw new IllegalArgumentException();
        }
        return newInstance(arguments[0], arguments[1], arguments[2]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GenericFunctionXYZ that = (GenericFunctionXYZ) o;

        if (functionType != that.functionType) return false;
        if (xargument != null ? !xargument.equals(that.xargument) : that.xargument != null) return false;
        if (yargument != null ? !yargument.equals(that.yargument) : that.yargument != null) return false;
        return zargument != null ? zargument.equals(that.zargument) : that.zargument == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (functionType != null ? functionType.hashCode() : 0);
        result = 31 * result + (xargument != null ? xargument.hashCode() : 0);
        result = 31 * result + (yargument != null ? yargument.hashCode() : 0);
        result = 31 * result + (zargument != null ? zargument.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isDDImpl() {
        return functionType.ordinal() <= FunctionType.DOUBLE.ordinal();
    }

    @Override
    public boolean isDCImpl() {
        return functionType.ordinal() <= FunctionType.COMPLEX.ordinal();
    }

    @Override
    public boolean isDMImpl() {
        return functionType.ordinal() <= FunctionType.MATRIX.ordinal();
    }

    @Override
    public Complex toComplex() {
        if (!isComplex()) {
            Expr ss = simplify();
            boolean cc = ss.isComplex();
            if (cc) {
                return ss.toComplex();
            }
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Complex");
        }
        Expr e = simplify();
        if (e instanceof GenericFunctionXYZ) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Complex");
        }
        return e.toComplex();
    }

    @Override
    public double toDouble() {
        if (!isDouble()) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Double");
        }
        Expr e = simplify();
        if (e instanceof GenericFunctionXYZ) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Double");
        }
        return e.toDouble();
        //        return computeDoubleArg(Double.NaN);
    }

    @Override
    public Matrix toMatrix() {
        if (!isMatrix()) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Matrix");
        }
        Expr e = simplify();
        if (e instanceof GenericFunctionXYZ) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Matrix");
        }
        return e.toMatrix();
    }

    @Override
    public int getComponentSize() {
        return getXArgument().toDV().getComponentSize();
    }

    @Override
    public Expr setParam(String name, Expr value) {
        Expr xa = getXArgument();
        Expr xb = xa.setParam(name, value);
        Expr ya = getYArgument();
        Expr yb = ya.setParam(name, value);
        Expr za = getZArgument();
        Expr zb = za.setParam(name, value);
        if (xa != xb || ya != yb || za != zb) {
            Expr e = newInstance(xb, yb, zb);
            e = Any.copyProperties(this, e);
            return Any.updateTitleVars(e, name, value);
        }
        return this;
    }

    @Override
    public Expr getComponent(int row, int col) {
        if (isScalarExpr() && (row != col || col != 0)) {
            return FunctionFactory.DZEROXY;
        }
        return newInstance(getXArgument().toDM().getComponent(row, col), getYArgument().toDM().getComponent(row, col), getZArgument().toDM().getComponent(row, col));
    }

    private class GenericFunctionXYBinaryExprHelper implements Expressions.TernaryExprHelper<GenericFunctionXYZ>, Serializable {

        @Override
        public Expr getBaseExpr(GenericFunctionXYZ expr, int index) {
            return getArguments()[index];
        }

        @Override
        public double computeDouble(double a, double b, double c, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.isDefined3();
            if (def) {
                double d = evalDouble(a, b, c);
                defined.set();
                return d;
            } else {
                return 0;
            }
        }

        @Override
        public Complex computeComplex(Complex a, Complex b, Complex c, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.isDefined3();
            if (def) {
                Complex d = evalComplex(a, b, c);
                defined.set();
                return d;
            } else {
                return Complex.ZERO;
            }
        }

        @Override
        public Matrix computeMatrix(Matrix a, Matrix b, Matrix c, Matrix zero, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.isDefined3();
            if (def) {
                Matrix d = evalMatrix(a, b, c);
                defined.set();
                return d;
            } else {
                return zero;
            }
        }

        @Override
        public Vector computeVector(Vector a, Vector b, Vector c, Vector zero, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.isDefined3();
            if (def) {
                Vector d = evalVector(a, b, c);
                defined.set();
                return d;
            } else {
                return zero;
            }
        }
    }

}
