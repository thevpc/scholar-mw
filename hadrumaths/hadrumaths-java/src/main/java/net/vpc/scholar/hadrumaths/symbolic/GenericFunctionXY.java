package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.NonStateField;

import java.io.Serializable;

/**
 * Created by vpc on 4/30/14.
 */
public abstract class GenericFunctionXY extends AbstractComposedFunction {
    private static final long serialVersionUID = 1L;
    private FunctionType functionType;
    @NonStateField
    protected transient Domain _cache_domain;
    @NonStateField
    protected Expressions.BinaryExprHelper<GenericFunctionXY> exprHelper = new GenericFunctionXYBinaryExprHelperAnd();
    private Expr xargument;
    private Expr yargument;

    protected GenericFunctionXY() {
    }

    protected GenericFunctionXY(Expr xargument, Expr yargument) {
        this(xargument, yargument, null);
    }

    protected GenericFunctionXY(Expr xargument, Expr yargument, FunctionType lowerFunctionType) {
        init(xargument, yargument, lowerFunctionType, true);
    }

    protected void init(Expr xargument, Expr yargument, FunctionType lowerFunctionType, boolean checkDim) {
//        if(checkDim) {
//            if (!xargument.getComponentDimension().equals(yargument.getComponentDimension())) {
//                ComponentDimension d=Maths.expandComponentDimension(xargument.getComponentDimension(),yargument.getComponentDimension());
//                xargument=Maths.expandComponentDimension(xargument,d);
//                yargument=Maths.expandComponentDimension(yargument,d);
//            }
//        }
        ComponentDimension cd = xargument.getComponentDimension().expand(yargument.getComponentDimension());
        this.xargument = Maths.expandComponentDimension(xargument, cd);
        this.yargument = Maths.expandComponentDimension(yargument, cd);
        resetFunctionType(lowerFunctionType);
//        if(this.functionType==FunctionType.MATRIX){
//            System.out.println("Why");
//        }
    }

    public void resetFunctionType(FunctionType lowerFunctionType) {
        FunctionType functionType0 = null;
        if (xargument.isDD()) {
            functionType0 = FunctionType.DOUBLE;
        } else if (xargument.isDC()) {
            functionType0 = FunctionType.COMPLEX;
        } else if (xargument.isDM()) {
//            xargument.isDC();
            functionType0 = FunctionType.MATRIX;
        } else {
            throw new IllegalArgumentException("Unknown functionType");
        }
        if (yargument.isDD() && functionType0.ordinal() <= FunctionType.DOUBLE.ordinal()) {
            functionType0 = FunctionType.DOUBLE;
        } else if (yargument.isDC() && functionType0.ordinal() <= FunctionType.COMPLEX.ordinal()) {
            functionType0 = FunctionType.COMPLEX;
        } else if (yargument.isDM() && functionType0.ordinal() <= FunctionType.MATRIX.ordinal()) {
//            yargument.isDC();
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

    public FunctionType getFunctionType() {
        return functionType;
    }

    @Override
    public boolean isDoubleTyped() {
        return functionType == FunctionType.DOUBLE;
    }


    public Expr getXArgument() {
        return xargument;
    }

    public Expr getYArgument() {
        return yargument;
    }

    @Override
    public Expr[] getArguments() {
        return new Expr[]{xargument, yargument};
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
        return getXArgument().getDomain().intersect(getYArgument().getDomain());
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
        return Expressions.computeComplex(this, exprHelper, x, y, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, exprHelper, x, d0, ranges);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, exprHelper, x, y, z, d0, ranges);
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
        return Expressions.computeDouble(this, exprHelper, x, y, d0, ranges);
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, exprHelper, x, y, z, d0, ranges);
    }

    @Override
    public Matrix computeMatrix(double x, double y) {
        if (contains(x, y)) {
            Matrix xx = getXArgument().toDM().computeMatrix(x, y);
            Matrix yy = getYArgument().toDM().computeMatrix(x, y);
            return evalMatrix(xx, yy);
        }
        return Complex.ZERO.toMatrix();
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        if (contains(x, y, z)) {
            Matrix xx = getXArgument().toDM().computeMatrix(x, y, z);
            Matrix yy = getYArgument().toDM().computeMatrix(x, y, z);
            return evalMatrix(xx, yy);
        }
        return Complex.ZERO.toMatrix();
    }


    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, exprHelper, x, d0, ranges);
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, exprHelper, x, y, d0, ranges);
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, exprHelper, x, d0, ranges);
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, exprHelper, x, y, z, d0, ranges);
    }

    //    protected double evalCD(Complex x) {
//        return evalComplex(x).toDouble();
//    }
//
//    protected Complex evalDC(double x) {
//        return evalComplex(Complex.valueOf(x));
//    }
//
//    protected Complex evalMC(Matrix x) {
//        return evalComplex(x.toComplex());
//    }
//
//    protected Double evalMD(Matrix x) {
//        return evalComplex(x.toComplex()).toDouble();
//    }
//
//    protected Matrix evalDM(double x) {
//        return evalComplex(Complex.valueOf(x)).toMatrix();
//    }
//
//    protected Matrix evalCM(Complex x) {
//        return evalComplex(x).toMatrix();
//    }
//
//    public Matrix computeMatrix(Matrix c) {
//        return evalMM(c);
//    }
//
    protected Matrix evalMatrix(Matrix x, Matrix y) {
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
                return computeComplexArg(x.get(row, column), y.get(row, column), true, true, NoneOutBoolean.INSTANCE);
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
     * @param xdef
     * @param ydef
     * @param defined
     * @return
     */
    public abstract Complex computeComplexArg(Complex x, Complex y, boolean xdef, boolean ydef, BooleanMarker defined);

    public abstract Complex computeComplexArg(double x, double y, boolean xdef, boolean ydef, BooleanMarker defined);

    public abstract double computeDoubleArg(double x, double y, boolean xdef, boolean ydef, BooleanMarker defined);

    public abstract Expr newInstance(Expr xargument, Expr yargument);


    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            double a = getXArgument().toDD().computeDouble(x, xdefined);
            double b = getYArgument().toDD().computeDouble(x, ydefined);
            return computeDoubleArg(a, b, ydefined.get(), xdefined.get(), defined);
        }
        return 0;
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            double a = getXArgument().toDD().computeDouble(x, y, xdefined);
            double b = getYArgument().toDD().computeDouble(x, y, ydefined);
            return computeDoubleArg(a, b, ydefined.get(), xdefined.get(), defined);
        }
        return 0;
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            double a = getXArgument().toDD().computeDouble(x, y, z, xdefined);
            double b = getYArgument().toDD().computeDouble(x, y, z, ydefined);
            return computeDoubleArg(a, b, ydefined.get(), xdefined.get(), defined);
        }
        return 0;
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            Complex a = getXArgument().toDC().computeComplex(x, xdefined);
            Complex b = getYArgument().toDC().computeComplex(x, ydefined);
            return computeComplexArg(a, b, xdefined.get(), ydefined.get(), defined);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            Complex a = getXArgument().toDC().computeComplex(x, y, xdefined);
            Complex b = getYArgument().toDC().computeComplex(x, y, ydefined);
            return computeComplexArg(a, b, xdefined.get(), ydefined.get(), defined);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            Complex a = getXArgument().toDC().computeComplex(x, y, z, xdefined);
            Complex b = getYArgument().toDC().computeComplex(x, y, z, ydefined);
            return computeComplexArg(a, b, xdefined.get(), ydefined.get(), defined);
        }
        return Complex.ZERO;
    }


    //    @Override
//    public String toString() {
//        return getFunctionName() + "(" + getXArgument() + "," + getYArgument() + ")";
//    }


    public Expr newInstance(Expr... arguments) {
        if (arguments.length != 2) {
            throw new IllegalArgumentException();
        }
        return newInstance(arguments[0], arguments[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GenericFunctionXY that = (GenericFunctionXY) o;

        if (functionType != that.functionType) return false;
        if (xargument != null ? !xargument.equals(that.xargument) : that.xargument != null) return false;
        return yargument != null ? yargument.equals(that.yargument) : that.yargument == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (functionType != null ? functionType.hashCode() : 0);
        result = 31 * result + (xargument != null ? xargument.hashCode() : 0);
        result = 31 * result + (yargument != null ? yargument.hashCode() : 0);
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
            ss.isComplex();
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Complex");
        }
        Expr e = simplify();
        if (e instanceof GenericFunctionXY) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Complex");
        }
        return e.toComplex();
        //return evalComplex(Complex.NaN);
    }

    @Override
    public double toDouble() {
        if (!isDouble()) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Double");
        }
        Expr e = simplify();
        if (e instanceof GenericFunctionXY) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Double");
        }
        return e.toDouble();
    }

    @Override
    public Matrix toMatrix() {
        if (!isMatrix()) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Matrix");
        }
        Expr e = simplify();
        if (e instanceof GenericFunctionXY) {
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
        if (xa != xb || ya != yb) {
            Expr e = newInstance(xb, yb);
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
        return newInstance(getXArgument().toDM().getComponent(row, col), getYArgument().toDM().getComponent(row, col));
    }

    private class GenericFunctionXYBinaryExprHelperAnd implements Expressions.BinaryExprHelper<GenericFunctionXY>, Serializable {

        @Override
        public int getBaseExprCount(GenericFunctionXY expr) {
            return 2;
        }

        @Override
        public Expr getBaseExpr(GenericFunctionXY expr, int index) {
            return getArguments()[index];
        }

        @Override
        public double computeDouble(double a, double b, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            double d = GenericFunctionXY.this.computeDoubleArg(a, b, options.value1Defined, options.value2Defined, defined);
            return d;
        }

        @Override
        public Complex computeComplex(Complex a, Complex b, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            Complex d = GenericFunctionXY.this.computeComplexArg(a, b, options.value1Defined, options.value2Defined, defined);
            return d;
        }

        @Override
        public Matrix computeMatrix(Matrix a, Matrix b, Matrix zero, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined && options.value2Defined;
            if (def) {
                Matrix d = evalMatrix(a, b);
                defined.set();
                return d;
            } else {
                return zero;
            }
        }
    }


}
