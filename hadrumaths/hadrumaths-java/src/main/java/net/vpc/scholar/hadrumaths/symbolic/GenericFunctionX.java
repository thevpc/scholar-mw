package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.NonStateField;

import java.io.Serializable;

/**
 * Created by vpc on 4/30/14.
 */
public abstract class GenericFunctionX extends AbstractComposedFunction {
    private static final long serialVersionUID = 1L;
    private final FunctionType functionType;
    @NonStateField
    protected transient Domain _cache_domain;
    @NonStateField
    private Expressions.UnaryExprHelper<GenericFunctionX> exprHelper = new GenericFunctionXUnaryExprHelper();
    private Expr argument;

    protected GenericFunctionX(String functionName, Expr argument) {
        this(functionName, argument, null);
    }

    protected GenericFunctionX(String functionName, Expr argument, FunctionType lowerFunctionType) {
        super();
//        if(this instanceof Acosh){
//            System.out.println("Why");
//        }
        this.argument = argument;
        FunctionType functionType0 = null;
        if (argument.isDD()) {
            functionType0 = FunctionType.DOUBLE;
        } else if (argument.isDC()) {
            functionType0 = FunctionType.COMPLEX;
        } else if (argument.isDM()) {
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

    @Override
    public boolean isDoubleTyped() {
        return functionType == FunctionType.DOUBLE;
    }

    public Expr getArgument() {
        return argument;
    }

    @Override
    public Expr[] getArguments() {
        return new Expr[]{argument};
    }

    //    @Override
//    public Domain getDomain() {
//        return Domain.FULLX;
////        if(getArgument().isDDx()) {
////            return (getArgument().toDDx()).getDomain();
////        }
////        boolean dDx = isDDx();
////        throw new ClassCastException();
//    }
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
        //will always return arg domain
        return getArgument().getDomain();
//        switch (this.functionType) {
//            case DOUBLE: {
//                if (computeDoubleArg(0, NoneOutBoolean.INSTANCE) == 0) {
//                    return getArgument().getDomain();
//                }
//            }
//            case COMPLEX: {
//                if (computeComplexArg(Complex.ZERO, NoneOutBoolean.INSTANCE).isZero()) {
//                    return getArgument().getDomain();
//                }
//            }
//            case MATRIX: {
//                if (evalMM(Complex.ZERO.toMatrix()).isZero()) {
//                    return getArgument().getDomain();
//                }
//            }
//        }
//        return Domain.FULL(getArgument().getDomainDimension());
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return getArgument().getComponentDimension();
    }

    @Override
    public int getDomainDimension() {
        return getArgument().getDomainDimension();
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

//    @Override
//    public Complex computeComplex(double x, double y, BooleanMarker defined) {
//        return computeComplex(x, defined);
//    }

    @Override
    public Matrix computeMatrix(double x, double y) {
        if (contains(x, y)) {
            Matrix cc = getArgument().toDM().computeMatrix(x, y);
            return evalMM(cc);
        }
        return Complex.ZERO.toMatrix();
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        if (contains(x, y, z)) {
            Matrix cc = getArgument().toDM().computeMatrix(x, y, z);
            return evalMM(cc);
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

    protected double evalCD(Complex x, BooleanMarker defined) {
        return computeComplexArg(x, defined).toDouble();
    }

    protected Complex evalDC(double x, BooleanMarker defined) {
        return computeComplexArg(Complex.valueOf(x), defined);
    }

    protected Complex evalMC(Matrix x) {
        return computeComplexArg(x.toComplex(), NoneOutBoolean.INSTANCE);
    }

    protected Double evalMD(Matrix x) {
        return computeComplexArg(x.toComplex(), NoneOutBoolean.INSTANCE).toDouble();
    }

    protected Matrix evalDM(double x) {
        return computeComplexArg(Complex.valueOf(x), NoneOutBoolean.INSTANCE).toMatrix();
    }

    protected Matrix evalCM(Complex x) {
        return computeComplexArg(x, NoneOutBoolean.INSTANCE).toMatrix();
    }

    public Matrix computeMatrix(Matrix c) {
        return evalMM(c);
    }

    protected Matrix evalMM(final Matrix x) {
        Complex[][] arrayCopy = x.getArrayCopy();
        for (int i = 0; i < arrayCopy.length; i++) {
            for (int j = 0; j < arrayCopy[i].length; j++) {
                arrayCopy[i][j] = computeComplexArg(arrayCopy[i][j], NoneOutBoolean.INSTANCE);
            }
        }
        return Maths.matrix(arrayCopy);
    }

    /**
     * Should call    defined.set();
     * @param c
     * @param defined
     * @return
     */
    public abstract Complex computeComplexArg(Complex c, BooleanMarker defined);

    /**
     * should call defined.set();
     * @param c
     * @param defined
     * @return
     */
    protected abstract double computeDoubleArg(double c, BooleanMarker defined);

    public double computeDouble(double x, double y, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        double d = getArgument().toDD().computeDouble(x, y, rdefined);
        if (rdefined.get()) {
            defined.set();
            return computeDoubleArg(d, defined);
        }
        return 0;
    }

    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        double d = getArgument().toDD().computeDouble(x, y, z, rdefined);
        if (rdefined.get()) {
            defined.set();
            return computeDoubleArg(d, defined);
        }
        return 0;
    }

    public double computeDouble(double x, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        double d = getArgument().toDD().computeDouble(x, rdefined);
        if (rdefined.get()) {
            defined.set();
            return computeDoubleArg(d, defined);
        }
        return 0;
    }

    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        Complex d = getArgument().toDC().computeComplex(x, y, rdefined);
        if (rdefined.get()) {
            defined.set();
            return computeComplexArg(d, defined);
        }
        return Complex.ZERO;
    }

    public Complex computeComplex(double x, double y, double z,BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        Complex d = getArgument().toDC().computeComplex(x, y, z,rdefined);
        if (rdefined.get()) {
            defined.set();
            return computeComplexArg(d, defined);
        }
        return Complex.ZERO;
    }

    public Complex computeComplex(double x, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        Complex d = getArgument().toDC().computeComplex(x, rdefined);
        if (rdefined.get()) {
            defined.set();
            return computeComplexArg(d, defined);
        }
        return Complex.ZERO;
    }

//    @Override
//    public String toString() {
//        return getFunctionName() + "(" + getArgument() + ")";
//    }

    public abstract Expr newInstance(Expr argument);

    public Expr newInstance(Expr... arguments) {
        if (arguments.length != 1) {
            throw new IllegalArgumentException();
        }
        return newInstance(arguments[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GenericFunctionX that = (GenericFunctionX) o;

        if (functionType != that.functionType) return false;
        return argument != null ? argument.equals(that.argument) : that.argument == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (functionType != null ? functionType.hashCode() : 0);
        result = 31 * result + (argument != null ? argument.hashCode() : 0);
        return result;
    }

//    @Override
//    public boolean isDoubleExprImpl() {
//        return false;
//    }

    @Override
    public boolean isDDImpl() {
//        if (!super.isDoubleExprImpl()) {
//            return false;
//        }
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
        if (e instanceof GenericFunctionX) {
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
        if (e instanceof GenericFunctionX) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Double");
        }
        return e.toDouble();
//        if (e instanceof Complex) {
//            Complex cc = (Complex) e;
//            if (cc.isReal()) {
//                return cc.getReal();
//            }
//        }
//        throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Double");
//        return computeDoubleArg(Double.NaN);
    }

    @Override
    public Matrix toMatrix() {
        if (!isMatrix()) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Matrix");
        }
        Expr e = simplify();
        if (e instanceof GenericFunctionX) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Matrix");
        }
        return e.toMatrix();
    }

    @Override
    public int getComponentSize() {
        return getArgument().toDV().getComponentSize();
    }

    private class GenericFunctionXUnaryExprHelper implements Expressions.UnaryExprHelper<GenericFunctionX>, Serializable {

        @Override
        public Expr getBaseExpr(GenericFunctionX expr) {
            return expr.getArgument();
        }

        @Override
        public double computeDouble(double x, BooleanMarker defined) {
            return GenericFunctionX.this.computeDoubleArg(x, defined);
        }

        @Override
        public Complex computeComplex(Complex x, BooleanMarker defined) {
            return GenericFunctionX.this.computeComplexArg(x, defined);
        }

        @Override
        public Matrix computeMatrix(Matrix x) {
            return evalMM(x);
        }
    }

    @Override
    public Expr setParam(String name, Expr value) {
        Expr a = getArgument();
        Expr b = a.setParam(name, value);
        if (a != b) {
            Expr e = newInstance(b);
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
        return newInstance(getArgument().toDM().getComponent(row, col));
    }

    @Override
    public Expr mul(Domain domain) {
        return newInstance(getArgument().mul(getArgument().getDomain().intersect(domain)));
    }

}
