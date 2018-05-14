/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;
import net.vpc.scholar.hadrumaths.util.NonStateField;

import java.util.Arrays;
import java.util.List;

/**
 * @author vpc
 */
//TODO fix me like Mul plz!!! (pbm in compute, domain is no so valid)
public class Pow extends AbstractExprOperator implements Cloneable {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Pow.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Pow e = (Pow) expression;
                return new Pow(ExpressionTransformFactory.transform(e.getFirst(), transform), ExpressionTransformFactory.transform(e.getSecond(), transform));
            }
        });
    }

    private Expr[] expressions;
    private int domainDim;
    @NonStateField
    protected transient Domain _cache_domain;

    public Pow(Expr first, Expr second) {
        this.expressions = new Expr[]{first, second};
        domainDim = Maths.max(first.getDomainDimension(), second.getDomainDimension());
    }


    private static Expressions.BinaryExprHelper<Pow> binaryExprHelper = new Expressions.BinaryExprHelper<Pow>() {
        @Override
        public int getBaseExprCount(Pow expr) {
            return 2;
        }

        @Override
        public Expr getBaseExpr(Pow expr, int index) {
            return expr.expressions[index];
        }

        @Override
        public double computeDouble(double a, double b, OutBoolean defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined && options.value2Defined;
            if (def) {
                double d = Maths.pow(a, b);
                defined.set();
                return d;
            } else {
                return 0;
            }
        }

        @Override
        public Complex computeComplex(Complex a, Complex b, OutBoolean defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined && options.value2Defined;
            if (def) {
                Complex d = a.pow(b);
                defined.set();
                return d;
            } else {
                return Complex.ZERO;
            }
        }

        @Override
        public Matrix computeMatrix(Matrix a, Matrix b, Matrix zero, OutBoolean defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined && options.value2Defined;
            if (def) {
                Matrix d = a.pow(b);
                defined.set();
                return d;
            } else {
                return zero;
            }
        }
    };

    public boolean isZeroImpl() {
        for (Expr expression : expressions) {
            if (!expression.isZero()) {
                return false;
            }
        }
        return true;
    }

    public boolean isNaNImpl() {
        for (Expr expression : expressions) {
            if (!expression.isNaN()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInfiniteImpl() {
        for (Expr expression : expressions) {
            if (expression.isInfinite()) {
                return true;
            }
        }
        return false;
    }


    public Expr getFirst() {
        return expressions[0];
    }

    public Expr getSecond() {
        return expressions[1];
    }

    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, x, y, d0, ranges);
    }

    public Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, x, y, d0, ranges);
    }

    public Matrix computeMatrix(double x, double y) {
        return Expressions.computeMatrix(this, x, y);
    }

    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

//    public double computeDouble(double x, double y) {
//        return Expressions.computeDouble(this, x, y);
//    }

//    public double computeDouble(double x) {
//        return Expressions.computeDouble(this, x);
//    }


    @Override
    public Complex computeComplex(double x, double y, double z, OutBoolean defined) {
        ReadableOutBoolean rdefined = OutBoolean.createReadable();
        Complex a = getFirst().toDC().computeComplex(x, y, z, rdefined);
        if(!rdefined.isSet()){
            return Complex.ZERO;
        }
        Complex b = getSecond().toDC().computeComplex(x, y, z, rdefined);
        if(!rdefined.isSet()){
            return Complex.ZERO;
        }
        defined.set();
        return a.pow(b);
    }

    @Override
    public Complex computeComplex(double x, double y, OutBoolean defined) {
        ReadableOutBoolean rdefined = OutBoolean.createReadable();
        Complex a = getFirst().toDC().computeComplex(x, y, rdefined);
        if(!rdefined.isSet()){
            return Complex.ZERO;
        }
        Complex b = getSecond().toDC().computeComplex(x, y, rdefined);
        if(!rdefined.isSet()){
            return Complex.ZERO;
        }
        defined.set();
        return a.pow(b);
    }

    @Override
    public Complex computeComplex(double x, OutBoolean defined) {
        ReadableOutBoolean rdefined = OutBoolean.createReadable();
        Complex a = getFirst().toDC().computeComplex(x, rdefined);
        if(!rdefined.isSet()){
            return Complex.ZERO;
        }
        Complex b = getSecond().toDC().computeComplex(x, rdefined);
        if(!rdefined.isSet()){
            return Complex.ZERO;
        }
        defined.set();
        return a.pow(b);
    }

    @Override
    public double computeDouble(double x, double y, double z, OutBoolean defined) {
        ReadableOutBoolean rdefined = OutBoolean.createReadable();
        double a = getFirst().toDD().computeDouble(x, y, z, rdefined);
        if(!rdefined.isSet()){
            return 0;
        }
        double b = getSecond().toDD().computeDouble(x, y, z, rdefined);
        if(!rdefined.isSet()){
            return 0;
        }
        defined.set();
        return Maths.pow(a,b);
    }

    @Override
    public double computeDouble(double x, double y, OutBoolean defined) {
        ReadableOutBoolean rdefined = OutBoolean.createReadable();
        double a = getFirst().toDD().computeDouble(x, y, rdefined);
        if(!rdefined.isSet()){
            return 0;
        }
        double b = getSecond().toDD().computeDouble(x, y, rdefined);
        if(!rdefined.isSet()){
            return 0;
        }
        defined.set();
        return Maths.pow(a,b);
    }

    @Override
    public double computeDouble(double x, OutBoolean defined) {
        ReadableOutBoolean rdefined = OutBoolean.createReadable();
        double a = getFirst().toDD().computeDouble(x, rdefined);
        if(!rdefined.isSet()){
            return 0;
        }
        double b = getSecond().toDD().computeDouble(x, rdefined);
        if(!rdefined.isSet()){
            return 0;
        }
        defined.set();
        return Maths.pow(a,b);
    }

    @Override
    public final Domain getDomain() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return getDomainImpl();
        }
        if( _cache_domain==null){
            _cache_domain=getDomainImpl();
        }
        return _cache_domain;
    }

    @Override
    public Domain getDomainImpl() {
        if (isZero()) {
            return Domain.ZERO(getDomainDimension());
        }
        Domain d = Domain.FULL(getDomainDimension());
        for (Expr expression : getSubExpressions()) {
            if (!expression.isZero()) {
                d = d.intersect(expression.getDomain());
            }
        }
        return d.toDomain(getDomainDimension());
    }

    @Override
    protected boolean isDoubleImpl() {
        //if operand is not integer may return complex (-1^0.5 for instance)
        return false;
    }

    //    @Override
//    public Domain getDomain() {
//        Domain d = Domain.NaNX;
//        if (isDDx()) {
//            for (Expr expression : expressions) {
//                d = d.intersect(expression.toDDx().getDomain());
//            }
//        } else {
//            throw new ClassCastException();
//        }
//        return d;
//    }

    @Override
    public ComponentDimension getComponentDimension() {
        for (Expr expression : expressions) {
            ComponentDimension d = expression.getComponentDimension();
            if (d.rows != 1 || d.columns != 1) {
                return d;
            }
        }
        return ComponentDimension.SCALAR;
    }

    public Expr getComponent(int row, int col) {
        if (isDM()) {
            if (isScalarExpr() && (row != col || col != 0)) {
                return FunctionFactory.DZEROXY;
            }
            Expr[] inner = new Expr[expressions.length];
            for (int i = 0; i < inner.length; i++) {
                inner[i] = expressions[i].toDM().getComponent(row, col);
            }
            return new Pow(inner[0], inner[1]);
        } else {
            throw new ClassCastException();
        }
    }


    public DoubleToDouble getRealDD() {
        return new Real(toDC());
    }

    public DoubleToDouble getImagDD() {
        return new Imag(toDC());
    }

    public Expr clone() {
        Pow cloned = (Pow) super.clone();
        cloned.expressions = new Expr[]{getFirst().clone(), getSecond().clone()};
        return cloned;
    }

    public List<Expr> getSubExpressions() {
        return Arrays.asList(expressions);
    }


    @Override
    public Complex toComplex() {
        return getFirst().toComplex().pow(getSecond().toComplex());
    }

    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }

    @Override
    public Matrix toMatrix() {
        return getFirst().toMatrix().pow(getSecond().toMatrix());
    }

    @Override
    public Expr setParam(String name, Expr value) {
        Expr[] updated = new Expr[expressions.length];
        boolean changed = false;
        for (int i = 0; i < updated.length; i++) {
            Expr s1 = expressions[i];
            Expr s2 = s1.setParam(name, value);
            if (s1 != s2) {
                changed = true;
            }
            updated[i] = s2;
        }
        if (changed) {
            Expr e = new Pow(updated[0], updated[1]);
            e= Any.copyProperties(this, e);
            return Any.updateTitleVars(e,name,value);
        }
        return this;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        Expr[] updated = new Expr[expressions.length];
        boolean changed = false;
        for (int i = 0; i < updated.length; i++) {
            Expr s1 = expressions[i];
            Expr s2 = s1.composeX(xreplacement);
            if (s1 != s2) {
                changed = true;
            }
            updated[i] = s2;
        }
        if (changed) {
            Expr e = new Pow(updated[0], updated[1]);
            e= Any.copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        Expr[] updated = new Expr[expressions.length];
        boolean changed = false;
        for (int i = 0; i < updated.length; i++) {
            Expr s1 = expressions[i];
            Expr s2 = s1.composeY(yreplacement);
            if (s1 != s2) {
                changed = true;
            }
            updated[i] = s2;
        }
        if (changed) {
            Expr e = new Pow(updated[0], updated[1]);
            e= Any.copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Pow pow = (Pow) o;

        if (domainDim != pow.domainDim) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(expressions, pow.expressions)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(expressions);
        result = 31 * result + domainDim;
        return result;
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return Expressions.getMatrixExpressionTitleByChildren(this, row, col);
    }


    @Override
    public DoubleToComplex getComponent(Axis a) {
        switch (a) {
            case X: {
                return getComponent(0, 0).toDC();
            }
            case Y: {
                return getComponent(1, 0).toDC();
            }
            case Z: {
                return getComponent(2, 0).toDC();
            }
        }
        throw new IllegalArgumentException("Illegal axis");
    }

    @Override
    public DoubleToVector toDV() {
        if (!isDV()) {
            throw new ClassCastException();
        }
        return this;
    }

    @Override
    public int getDomainDimension() {
        return domainDim;
    }


//    @Override
//    public Complex computeComplex(double x, double y, double z) {
//        return getFirst().toDC().computeComplex(x, y, z).pow(getSecond().toDC().computeComplex(x, y, z));
//    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        return getFirst().toDM().computeMatrix(x, y, z).pow(getSecond().toDM().computeMatrix(x, y, z));
    }

//    @Override
//    public Complex computeComplexArg(double x,OutBoolean defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplexArg(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        return Expressions.computeDouble(this, binaryExprHelper, x, d0, range);
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, binaryExprHelper, x, y, d0, ranges);
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, binaryExprHelper, x, y, z, d0, ranges);
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, binaryExprHelper, x, y, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, binaryExprHelper, x, d0, ranges);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, binaryExprHelper, x, y, z, d0, ranges);
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, binaryExprHelper, x, d0, ranges);
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, binaryExprHelper, x, y, d0, ranges);
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, binaryExprHelper, x, y, z, d0, ranges);
    }
}
