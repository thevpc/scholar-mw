package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.NonStateField;

import java.io.Serializable;

/**
 * Created by vpc on 4/30/14.
 */
public abstract class GenericFunctionX extends AbstractComposedFunction {
    private final FunctionType functionType;
    @NonStateField
    protected Domain _cache_domain;
    @NonStateField
    private Expressions.UnaryExprHelper<GenericFunctionX> exprHelper = new GenericFunctionXUnaryExprHelper();
    private Expr argument;
    protected GenericFunctionX(String functionName,Expr argument) {
        this(functionName,argument, null);
        this.argument=argument;
    }

    protected GenericFunctionX(String functionName,Expr argument, FunctionType lowerFunctionType) {
        super();
        this.argument=argument;
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
        return functionType==FunctionType.DOUBLE;
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
        if ( _cache_domain == null) {
            _cache_domain = getDomainImpl();
        }
        return _cache_domain;
    }

    @Override
    public Domain getDomainImpl() {
        switch (this.functionType) {
            case DOUBLE: {
                if (evalDouble(0) == 0) {
                    return getArgument().getDomain();
                }
            }
            case COMPLEX: {
                if (evalComplex(Complex.ZERO).isZero()) {
                    return getArgument().getDomain();
                }
            }
            case MATRIX: {
                if (evalMM(Complex.ZERO.toMatrix()).isZero()) {
                    return getArgument().getDomain();
                }
            }
        }
        return Domain.FULL(getArgument().getDomainDimension());

//        if(getArgument() instanceof ExprXY) {
//            return ((ExprXY)getArgument()).getDomain();
//        }
//        if(getArgument() instanceof ExprX) {
//            return new DomainXY(((ExprX)getArgument()).getDomain());
//        }
//        return DomainXY.FULL;
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

    @Override
    public Complex computeComplex(double x, double y) {
        return computeComplex(new double[]{x}, new double[]{y})[0][0];
    }

    @Override
    public double computeDouble(double x) {
        return computeDouble(new double[]{x})[0];
    }

    @Override
    public double computeDouble(double x, double y) {
        return computeDouble(new double[]{x}, new double[]{y}, null, null)[0][0];
    }

    @Override
    public double computeDouble(double x, double y, double z) {
        return computeDouble(new double[]{x}, new double[]{y}, new double[]{z}, null, null)[0][0][0];
    }

    public Complex computeComplex(double x, double y, double z) {
        if (getDomain().contains(x, y, z)) {
            Complex cc = getArgument().toDC().computeComplex(x, y, z);
            return evalComplex(cc);
        }
        return Complex.ZERO;
    }

    @Override
    public Matrix computeMatrix(double x, double y) {
        if (getDomain().contains(x, y)) {
            Matrix cc = getArgument().toDM().computeMatrix(x, y);
            return evalMM(cc);
        }
        return Complex.ZERO.toMatrix();
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        if (getDomain().contains(x, y, z)) {
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

    protected double evalCD(Complex x) {
        return evalComplex(x).toDouble();
    }

    protected Complex evalDC(double x) {
        return evalComplex(Complex.valueOf(x));
    }

    protected Complex evalMC(Matrix x) {
        return evalComplex(x.toComplex());
    }

    protected Double evalMD(Matrix x) {
        return evalComplex(x.toComplex()).toDouble();
    }

    protected Matrix evalDM(double x) {
        return evalComplex(Complex.valueOf(x)).toMatrix();
    }

    protected Matrix evalCM(Complex x) {
        return evalComplex(x).toMatrix();
    }

    public Matrix computeMatrix(Matrix c) {
        return evalMM(c);
    }

    protected Matrix evalMM(final Matrix x) {
        Complex[][] arrayCopy = x.getArrayCopy();
        for (int i = 0; i < arrayCopy.length; i++) {
            for (int j = 0; j < arrayCopy[i].length; j++) {
                arrayCopy[i][j] = evalComplex(arrayCopy[i][j]);
            }
        }
        return Maths.matrix(arrayCopy);
    }

    public Complex computeComplex(Complex c) {
        return evalComplex(c);
    }

    /**
     * evaluate expression and return the most "reduced" type
     *
     * @param c
     * @return
     */
//    protected abstract Expr evalEE(Expr c);
    public Complex evalComplex(Complex c) {
        return c;
    }

    protected double evalDouble(double c) {
        return c;
    }

    @Override
    public String toString() {
        return getFunctionName() + "(" + getArgument() + ")";
    }

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

    @Override
    public boolean isDoubleExprImpl() {
        return false;
    }

    @Override
    public boolean isDDImpl() {
        if(! super.isDoubleExprImpl()){
            return false;
        }
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
        if(e instanceof GenericFunctionX){
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
        if(e instanceof GenericFunctionX){
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
//        return evalDouble(Double.NaN);
    }

    @Override
    public Matrix toMatrix() {
        if (!isMatrix()) {
            throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Matrix");
        }
        Expr e = simplify();
        if(e instanceof GenericFunctionX){
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
        public double computeDouble(double x) {
            return evalDouble(x);
        }

        @Override
        public Complex computeComplex(Complex x) {
            return evalComplex(x);
        }

        @Override
        public Matrix computeMatrix(Matrix x) {
            return evalMM(x);
        }
    }

    @Override
    public Expr setParam(String name, Expr value) {
        Expr a = getArgument();
        Expr b = a.setParam(name,value);
        if(a!=b){
            Expr e = newInstance(b);
            e= Any.copyProperties(this, e);
            return Any.updateTitleVars(e,name,value);
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
