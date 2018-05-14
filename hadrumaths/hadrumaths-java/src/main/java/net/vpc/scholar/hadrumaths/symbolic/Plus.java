/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author vpc
 */
public class Plus extends AbstractExprOperator implements Cloneable {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Plus.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Plus e = (Plus) expression;
                ExpressionTransform t = transform;
                Expr[] e2 = new Expr[e.expressions.length];
                for (int i = 0; i < e2.length; i++) {
                    e2[i] = ExpressionTransformFactory.transform(e.expressions[i], transform);
                }
                return new Plus(e.expressions);
            }
        });
    }

    private Expr[] expressions;
    private int domainDim;
    protected transient Domain _cache_domain;

    private static Expressions.BinaryExprHelper<Plus> binaryExprHelper = new Expressions.BinaryExprHelper<Plus>() {
        @Override
        public int getBaseExprCount(Plus expr) {
            return expr.expressions.length;
        }

        @Override
        public Expr getBaseExpr(Plus expr, int index) {
            return expr.expressions[index];
        }

        @Override
        public double computeDouble(double a, double b, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined || options.value2Defined;
            if (def) {
                double d = a + b;
                defined.set();
                return d;
            } else {
                return 0;
            }
        }

        @Override
        public Complex computeComplex(Complex a, Complex b, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined || options.value2Defined;
            if (def) {
                Complex d = a.add(b);
                defined.set();
                return d;
            } else {
                return Complex.ZERO;
            }
        }

        @Override
        public Matrix computeMatrix(Matrix a, Matrix b, Matrix zero, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined || options.value2Defined;
            if (def) {
                Matrix d = a.add(b);
                defined.set();
                return d;
            } else {
                return zero;
            }
        }
    };

    public Plus(TVector<Expr> list) {
        this(list.toArray());
    }

    public Plus(List<Expr> expressions) {
        this(expressions.toArray(new Expr[expressions.size()]));
    }

    public Plus(Expr... expressions) {
        this.expressions = expressions;
//        boolean someUnconstrained=false;
//        boolean someConstrained=false;
        for (Expr expression : expressions) {
            int d = expression.getDomainDimension();
            if (d > domainDim) {
                domainDim = d;
            }
//            if(expression.getDomain().isUnconstrained()){
//                someUnconstrained=true;
//            }else{
//                someConstrained=true;
//            }
        }
//        if(someConstrained && someUnconstrained){
//            System.out.println("Plus : someConstrained && someUnconstrained");
//        }
    }

    public Expr clone() {
        Plus cloned = (Plus) super.clone();
        cloned.expressions = new Expr[cloned.expressions.length];
        for (int i = 0; i < cloned.expressions.length; i++) {
            cloned.expressions[i] = expressions[i].clone();
        }
        return cloned;
    }


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
            if (expression.isNaN()) {
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


    public int size() {
        return expressions.length;
    }

    public Expr getExpression(int index) {
        return expressions[index];
    }


//    public boolean isDDx() {
//        for (Expr e : expressions) {
//            if (!e.isDDx()) {
//                return false;
//            }
//        }
//        return true;
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
        Domain d = Domain.NaNX;
        for (Expr expression : getSubExpressions()) {
            if (!expression.isZero()) {
                d = d.expand(expression.getDomain());
            }
        }
//        if(d.isNaN())
        return d.toDomain(getDomainDimension());
    }

//    @Override
//    public Domain getDomain() {
//        Domain d = Domain.NaNX;
//        if (isDDx()) {
//            for (Expr expression : expressions) {
//                d = d.expand(expression.toDDx().getDomain());
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
            DoubleToMatrix[] m = new DoubleToMatrix[expressions.length];
            ComponentDimension dd = null;

            for (int i = 0; i < expressions.length; i++) {
                m[i] = expressions[i].toDM();
                ComponentDimension d = m[i].getComponentDimension();
                if (d.equals(ComponentDimension.SCALAR)) {
                    //scalar , ok
                } else {
                    if (dd == null) {
                        dd = d;
                    } else if (!dd.equals(d)) {
                        throw new IllegalArgumentException("Matrix Dimensions does not match");
                    }
                }
            }
            if (dd == null) {
                dd = ComponentDimension.SCALAR;
            }
            if (dd.equals(ComponentDimension.SCALAR)) {
                Expr[] inner = new Expr[expressions.length];
                for (int i = 0; i < inner.length; i++) {
                    inner[i] = m[i].getComponent(row, col);
                }
                return new Plus(inner);
            } else {
                Expr[] inner = new Expr[expressions.length];
                for (int i = 0; i < inner.length; i++) {
                    if (m[i].isScalarExpr()) {
                        inner[i] = m[i].getComponent(0, 0);
                    } else {
                        inner[i] = m[i].getComponent(row, col);
                    }
                }
                return new Plus(inner);
            }
        } else {
            throw new ClassCastException();
        }
    }

    //    @Override
//    public double computeDouble(double x) {
//        double ret = expressions[0].toDDx().computeDouble(x);
//        for (int i = 1; i < expressions.length; i++) {
//            double val = expressions[i].toDDx().computeDouble(x);
//            ret = ret + val;
//        }
//        return ret;
//    }
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

    public double computeDouble(double x, BooleanMarker defined) {
        double d = 0;
        for (Expr expression : expressions) {
            d += expression.toDD().computeDouble(x, defined);
        }
        return d;
    }

    public double computeDouble(double x, double y, BooleanMarker defined) {
        double d = 0;
        for (Expr expression : expressions) {
            d += expression.toDD().computeDouble(x, y, defined);
        }
        return d;
    }

    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        double d = 0;
        for (Expr expression : expressions) {
            d += expression.toDD().computeDouble(x, y, z, defined);
        }
        return d;
    }

    public Matrix computeMatrix(double x) {
        return Expressions.computeMatrix(this, x);
    }

    public List<Expr> getSubExpressions() {
        return Arrays.asList(expressions);
    }


    @Override
    public Complex toComplex() {
        MutableComplex c = new MutableComplex();
        for (Expr e : expressions) {
            c.add(e.toComplex());
        }
        return c.toComplex();
    }

    @Override
    public double toDouble() {
        double c = 0;
        for (Expr e : expressions) {
            c += (e.toDouble());
        }
        return c;
    }

    @Override
    public Matrix toMatrix() {
        Matrix c = expressions[0].toMatrix();
        for (int i = 1; i < expressions.length; i++) {
            c = c.add(expressions[i].toMatrix());
        }
        return c;
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
            Expr e = Maths.sum(updated);
            e = Any.copyProperties(this, e);
            return Any.updateTitleVars(e, name, value);
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
            Expr e = new Plus(updated);
            e = Any.copyProperties(this, e);
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
            Expr e = Maths.sum(updated);
            e = Any.copyProperties(this, e);
            return e;
        }
        return this;
    }

    //    @Override
//    public String toString() {
//        String op = "+";
//        StringBuilder sb = new StringBuilder();
//        for (Expression expression : getSubExpressions()) {
//            if (sb.length() > 0) {
//                sb.append(op);
//            }
//            sb.append("(").append(expression).append(")");
//        }
//        return sb.toString();
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Plus plus = (Plus) o;

        if (domainDim != plus.domainDim) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(expressions, plus.expressions)) return false;
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

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        MutableComplex c = new MutableComplex();
        for (Expr expression : expressions) {
            c.add(expression.toDC().computeComplex(x, y, z, defined));
        }
        return c.toComplex();
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        MutableComplex c = new MutableComplex();
        for (Expr expression : expressions) {
            c.add(expression.toDC().computeComplex(x, y, defined));
        }
        return c.toComplex();
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        MutableComplex c = new MutableComplex();
        for (Expr expression : expressions) {
            c.add(expression.toDC().computeComplex(x, defined));
        }
        return c.toComplex();
    }


    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        Matrix c = expressions[0].toDM().computeMatrix(x, y, z);
        for (int i = 1; i < expressions.length; i++) {
            c = c.add(expressions[i].toDM().computeMatrix(x, y, z));
        }
        return c;
    }

//    @Override
//    public Complex computeComplexArg(double x,BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplexArg(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }


    public DoubleToDouble getImagDD() {
        boolean allDD = true;
        for (Expr e : expressions) {
            if (!e.isDD()) {
                allDD = false;
                break;
            }
        }
        if (allDD) {
            return FunctionFactory.DZERO(getDomainDimension());
        }
        Expr[] expressions2 = new Expr[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            expressions2[i] = expressions[i].toDC().getImagDD();
        }
        return Maths.sum(expressions2).toDD();
    }

    public DoubleToDouble getRealDD() {
        boolean allDD = true;
        for (Expr e : expressions) {
            if (!e.isDD()) {
                allDD = false;
                break;
            }
        }
        if (allDD) {
            return this;
        }
        Expr[] expressions2 = new Expr[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            expressions2[i] = expressions[i].toDC().getRealDD();
        }
        return Maths.sum(expressions2).toDD();
    }

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

    @Override
    public Expr mul(Domain domain) {
        Expr[] expr2 = ArrayUtils.copy(expressions);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(domain);
        }
        return new Plus(expr2);
    }

    @Override
    public Expr mul(double other) {
        if (other == 0) {
            return Maths.DDZERO;
        }
        Expr[] expr2 = ArrayUtils.copy(expressions);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(other);
        }
        return new Plus(expr2);
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isZero()) {
            return Maths.DDZERO;
        }
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        Expr[] expr2 = ArrayUtils.copy(expressions);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(other);
        }
        return new Plus(expr2);
    }
}
