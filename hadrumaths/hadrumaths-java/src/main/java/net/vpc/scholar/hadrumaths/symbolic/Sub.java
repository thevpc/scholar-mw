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
import net.vpc.scholar.hadrumaths.util.NonStateField;

import java.util.Arrays;
import java.util.List;

/**
 * @author vpc
 */
public class Sub extends AbstractExprOperatorBinary implements Cloneable {
    private static final long serialVersionUID = 1L;
    private static Expressions.BinaryExprHelper<Sub> binaryExprHelper = new Expressions.BinaryExprHelper<Sub>() {
        @Override
        public int getBaseExprCount(Sub expr) {
            return 2;
        }

        @Override
        public Expr getBaseExpr(Sub expr, int index) {
            return expr.expressions[index];
        }

        @Override
        public double computeDouble(double a, double b, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined || options.value2Defined;
            if (def) {
                double d = a - b;
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
                Complex d = a.sub(b);
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
                Matrix d = a.sub(b);
                defined.set();
                return d;
            } else {
                return zero;
            }
        }
        @Override
        public Vector computeVector(Vector a, Vector b, Vector zero, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined || options.value2Defined;
            if (def) {
                Vector d = a.sub(b);
                defined.set();
                return d;
            } else {
                return zero;
            }
        }
    };

    static {
        ExpressionTransformFactory.setExpressionTransformer(Sub.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Sub e = (Sub) expression;
                return new Sub(ExpressionTransformFactory.transform(e.getFirst(), transform), ExpressionTransformFactory.transform(e.getSecond(), transform));
            }
        });
    }

    private Expr[] expressions;
    private int domainDim;
    @NonStateField
    protected transient Domain _cache_domain;


    public Sub(Expr first, Expr second) {
        this.expressions = new Expr[]{first, second};
        domainDim = Math.max(first.getDomainDimension(), second.getDomainDimension());

    }

    @Override
    protected Expressions.BinaryExprHelper getBinaryExprHelper() {
        return binaryExprHelper;
    }

    @Override
    public int getComponentSize() {
        int s = 1;
        for (int i = 0; i < expressions.length; i++) {
            int s2 = expressions[i].toDV().getComponentSize();
            if (s2 > s) {
                s = s2;
            }
        }
        return s;
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


    public Expr getFirst() {
        return expressions[0];
    }

    public Expr getSecond() {
        return expressions[1];
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
        Domain d = Domain.NaNX;
        for (Expr expression : getSubExpressions()) {
            //if (!expression.isZero()) {
            d = d.expand(expression.getDomain());
            //}
        }
        return d.toDomain(getDomainDimension());

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
            return new Sub(inner[0], inner[1]);
        } else {
            throw new ClassCastException();
        }
    }


    public Expr clone() {

        Sub cloned = (Sub) super.clone();
        cloned.expressions = new Expr[]{getFirst().clone(), getSecond().clone()};
        return cloned;
    }


    public List<Expr> getSubExpressions() {
        return Arrays.asList(expressions);
    }

    @Override
    public Complex toComplex() {
        return getFirst().toComplex().sub(getSecond().toComplex());
    }

    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }

    @Override
    public Matrix toMatrix() {
        return getFirst().toMatrix().sub(getSecond().toMatrix());
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
            Expr e = new Sub(updated[0], updated[1]);
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
            Expr e = new Sub(updated[0], updated[1]);
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
            Expr e = new Sub(updated[0], updated[1]);
            e = Any.copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Sub sub = (Sub) o;

        if (domainDim != sub.domainDim) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(expressions, sub.expressions)) return false;
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
        BooleanRef rdefined1 = BooleanMarker.ref();
        BooleanRef rdefined2 = BooleanMarker.ref();
        Complex a = getFirst().toDC().computeComplex(x, y, z, rdefined1);
        Complex c = getSecond().toDC().computeComplex(x, y, z, rdefined2);
        if (!rdefined1.get() && !rdefined2.get()) {
            return Complex.ZERO;
        }
        defined.set();
        return a.sub(c);
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        BooleanRef rdefined1 = BooleanMarker.ref();
        BooleanRef rdefined2 = BooleanMarker.ref();
        double a = getFirst().toDD().computeDouble(x, y, z, rdefined1);
        double b = getSecond().toDD().computeDouble(x, y, z, rdefined2);
        if (!rdefined1.get() && !rdefined2.get()) {
            return 0;
        }
        defined.set();
        return a - b;
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        BooleanRef rdefined1 = BooleanMarker.ref();
        BooleanRef rdefined2 = BooleanMarker.ref();
        Complex a = getFirst().toDC().computeComplex(x, y, rdefined1);
        Complex c = getSecond().toDC().computeComplex(x, y, rdefined2);
        if (!rdefined1.get() && !rdefined2.get()) {
            return Complex.ZERO;
        }
        defined.set();
        return a.sub(c);
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        BooleanRef rdefined1 = BooleanMarker.ref();
        BooleanRef rdefined2 = BooleanMarker.ref();
        double a = getFirst().toDD().computeDouble(x, y, rdefined1);
        double b = getSecond().toDD().computeDouble(x, y, rdefined2);
        if (!rdefined1.get() && !rdefined2.get()) {
            return 0;
        }
        defined.set();
        return a - b;
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        BooleanRef rdefined1 = BooleanMarker.ref();
        BooleanRef rdefined2 = BooleanMarker.ref();
        Complex a = getFirst().toDC().computeComplex(x, rdefined1);
        Complex c = getSecond().toDC().computeComplex(x, rdefined2);
        if (!rdefined1.get() && !rdefined2.get()) {
            return Complex.ZERO;
        }
        defined.set();
        return a.sub(c);
    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        BooleanRef rdefined1 = BooleanMarker.ref();
        BooleanRef rdefined2 = BooleanMarker.ref();
        double a = getFirst().toDD().computeDouble(x, rdefined1);
        double b = getSecond().toDD().computeDouble(x, rdefined2);
        if (!rdefined1.get() && !rdefined2.get()) {
            return 0;
        }
        defined.set();
        return a - b;
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        return getFirst().toDM().computeMatrix(x, y, z).sub(getSecond().toDM().computeMatrix(x, y, z));
    }

//    @Override
//    public Complex computeComplexArg(double x,BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplexArg(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    public Matrix computeMatrix(double x) {
        return Expressions.computeMatrix(this, x);
    }


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
        return super.getImagDD();
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
        return super.getRealDD();
    }

    @Override
    public Expr mul(Domain domain) {
        Expr[] expr2 = ArrayUtils.copy(expressions);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(domain);
        }
        return new Sub(expr2[0], expr2[1]);
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
        return new Sub(expr2[0], expr2[1]);
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
        return new Sub(expr2[0], expr2[1]);
    }


}
