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
public class Reminder extends AbstractExprOperatorBinary implements Cloneable {
    private static final long serialVersionUID = 1L;

    private static Expressions.BinaryExprHelper<Reminder> binaryExprHelper = new Expressions.BinaryExprHelper<Reminder>() {
        @Override
        public int getBaseExprCount(Reminder expr) {
            return 2;
        }

        @Override
        public Expr getBaseExpr(Reminder expr, int index) {
            return expr.expressions[index];
        }

        @Override
        public double computeDouble(double a, double b, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined && options.value2Defined;
            if (def) {
                double d = a % b;
                defined.set();
                return d;
            } else {
                return 0;
            }
        }

        @Override
        public Complex computeComplex(Complex a, Complex b, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined && options.value2Defined;
            if (def) {
                Complex d = a.rem(b);
                defined.set();
                return d;
            } else {
                return Complex.ZERO;
            }
        }

        @Override
        public ComplexMatrix computeMatrix(ComplexMatrix a, ComplexMatrix b, ComplexMatrix zero, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined && options.value2Defined;
            if (def) {
                defined.set();
                ComplexMatrix d = a.div(b);
                //defined.set();
                return d;
            } else {
                return zero;
            }
        }
        @Override
        public ComplexVector computeVector(ComplexVector a, ComplexVector b, ComplexVector zero, BooleanMarker defined, Expressions.ComputeDefOptions options) {
            boolean def = options.value1Defined && options.value2Defined;
            if (def) {
                defined.set();
                ComplexVector d = a.div(b.toComplex());
                //defined.set();
                return d;
            } else {
                return zero;
            }
        }
    };

    static {
        ExpressionTransformFactory.setExpressionTransformer(Reminder.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Reminder e = (Reminder) expression;
                return new Reminder(ExpressionTransformFactory.transform(e.getFirst(), transform), ExpressionTransformFactory.transform(e.getSecond(), transform));
            }
        });
    }

    @NonStateField
    protected transient Domain _cache_domain;
    private Expr[] expressions;
    private int domainDim;


    public Reminder(Expr first, Expr second) {
        this.expressions = new Expr[]{first, second};
        domainDim = Math.max(first.getDomainDimension(), second.getDomainDimension());
    }

    public boolean isZeroImpl() {
        return getFirst().isZero() && !getSecond().isZero();
    }


    public Expr getFirst() {
        return expressions[0];
    }

    public Expr getSecond() {
        return expressions[1];
    }


    @Override
    protected Expressions.BinaryExprHelper getBinaryExprHelper() {
        return binaryExprHelper;
    }

    @Override
    public final Domain getDomain() {
        if (!MathsBase.Config.isCacheExpressionPropertiesEnabled()) {
            return getDomainImpl();
        }
        if (_cache_domain == null) {
            _cache_domain = getDomainImpl();
        }
        return _cache_domain;
    }

    @Override
    public Domain getDomainImpl() {
//        if (isZero()) {
//            return Domain.ZERO(getDomainDimension());
//        }
        Domain d = Domain.FULL(getDomainDimension());
        for (Expr expression : getSubExpressions()) {
            if (!expression.isZero()) {
                d = d.intersect(expression.getDomain());
            }
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
            Expr a = getFirst().toDM().getComponent(row, col);
            Expr b = getSecond().isScalarExpr() ? getSecond().toDM().getComponent(0, 0) : getSecond().toDM().getComponent(row, col);
            return new Reminder(a, b);
        } else {
            throw new ClassCastException();
        }
    }


    public Expr clone() {
        Reminder cloned = (Reminder) super.clone();
        cloned.expressions = new Expr[]{getFirst().clone(), getSecond().clone()};
        return cloned;
    }

    public List<Expr> getSubExpressions() {
        return Arrays.asList(expressions);
    }


    @Override
    public Complex toComplex() {
        Complex firstComplex = getFirst().toComplex();
        if (firstComplex.isNaN()) {
            return firstComplex;
        }
        Complex secondComplex = getSecond().toComplex();
        if (secondComplex.isNaN()) {
            return secondComplex;
        }
        return firstComplex.div(secondComplex);
    }

    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }

    @Override
    public ComplexMatrix toMatrix() {
        return getFirst().toMatrix().div(getSecond().toMatrix());
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
            Expr e = new Reminder(updated[0], updated[1]);
            e = Any.copyProperties(this, e);
            return Any.updateTitleVars(e, name, value);
        }
        return this;
    }

    @Override
    public Expr compose(Axis axis,Expr xreplacement) {
        Expr[] updated = new Expr[expressions.length];
        boolean changed = false;
        for (int i = 0; i < updated.length; i++) {
            Expr s1 = expressions[i];
            Expr s2 = s1.compose(axis,xreplacement);
            if (s1 != s2) {
                changed = true;
            }
            updated[i] = s2;
        }
        if (changed) {
            Expr e = new Reminder(updated[0], updated[1]);
            e = Any.copyProperties(this, e);
            return e;
        }
        return this;
    }

    public Expr[] getExpressions() {
        return expressions;
    }

    public void setExpressions(Expr[] expressions) {
        this.expressions = expressions;
    }

    public int getDomainDim() {
        return domainDim;
    }

    public void setDomainDim(int domainDim) {
        this.domainDim = domainDim;
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return Expressions.getMatrixExpressionTitleByChildren(this, row, col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Reminder div = (Reminder) o;

        if (domainDim != div.domainDim) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(expressions, div.expressions);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(expressions);
        result = 31 * result + domainDim;
        return result;
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
    public int getDomainDimension() {
        return domainDim;
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
        if (getFirst().isDC() && getSecond().isDC()) {
            DoubleToComplex a = getFirst().toDC();
            DoubleToComplex b = getSecond().toDC();
            boolean aReal = a.getImagDD().isZero();
            boolean aImag = a.getRealDD().isZero();
            boolean bReal = b.getImagDD().isZero();
            boolean bImag = b.getRealDD().isZero();
            if ((aReal && bReal) || (aImag && bImag)) {
                return FunctionFactory.DZEROXY;
            } else if ((aReal && bImag) || (bReal && aImag)) {
                return this;
            }
        }
        return new Imag(toDC());
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
        if (getFirst().isDC() && getSecond().isDC()) {
            DoubleToComplex a = getFirst().toDC();
            DoubleToComplex b = getSecond().toDC();
            boolean aReal = a.getImagDD().isZero();
            boolean aImag = a.getRealDD().isZero();
            boolean bReal = b.getImagDD().isZero();
            boolean bImag = b.getRealDD().isZero();
            if (aReal && bReal) {
                return this;
            } else if (aImag && bImag) {
                return this;
            } else if ((aReal && bImag) || (bReal && aImag)) {
                return FunctionFactory.DZEROXY;
            }
        }
        return new Real(toDC());
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
    public ComplexMatrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, binaryExprHelper, x, d0, ranges);
    }

    @Override
    public ComplexMatrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, binaryExprHelper, x, y, d0, ranges);
    }

    @Override
    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, binaryExprHelper, x, y, z, d0, ranges);
    }

    @Override
    public Expr mul(Domain domain) {
        Expr[] expr2 = ArrayUtils.copy(expressions);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(domain);
        }
        return new Reminder(expr2[0], expr2[1]);
    }

    @Override
    public Expr mul(double other) {
        if (other == 0) {
            return MathsBase.DDZERO;
        }
        Expr[] expr2 = ArrayUtils.copy(expressions);
        expr2[0] = expr2[0].mul(other);
        return new Mul(expr2[0], expr2[1]);
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isZero()) {
            return MathsBase.DDZERO;
        }
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        Expr[] expr2 = ArrayUtils.copy(expressions);
        expr2[0] = expr2[0].mul(other);
        return new Mul(expr2[0], expr2[1]);
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef defined2 = BooleanMarker.ref();
            Complex a = getFirst().toDC().computeComplex(x, defined2);
            if (!defined2.get()) {
                return Complex.ZERO;
            }
            defined2.reset();
            Complex b = getSecond().toDC().computeComplex(x, defined2);
            defined2.set();
            if (!defined2.get()) {
                return Complex.ZERO;
            }
            defined.set();
            return a.div(b);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef defined2 = BooleanMarker.ref();
            Complex a = getFirst().toDC().computeComplex(x, y, defined2);
            if (!defined2.get()) {
                return Complex.ZERO;
            }
            defined2.reset();
            Complex b = getSecond().toDC().computeComplex(x, y, defined2);
            defined2.set();
            if (!defined2.get()) {
                return Complex.ZERO;
            }
            defined.set();
            return a.div(b);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef defined2 = BooleanMarker.ref();
            Complex a = getFirst().toDC().computeComplex(x, y, z, defined2);
            if (!defined2.get()) {
                return Complex.ZERO;
            }
            defined2.reset();
            Complex b = getSecond().toDC().computeComplex(x, y, z, defined2);
            defined2.set();
            if (!defined2.get()) {
                return Complex.ZERO;
            }
            defined.set();
            return a.div(b);
        }
        return Complex.ZERO;
    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef defined2 = BooleanMarker.ref();
            double a = getFirst().toDD().computeDouble(x, defined2);
            if (!defined2.get()) {
                return 0;
            }
            defined2.reset();
            double b = getSecond().toDD().computeDouble(x, defined2);
            defined2.set();
            if (!defined2.get()) {
                return 0;
            }
            defined.set();
            return a / b;
        }
        return 0;
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef defined2 = BooleanMarker.ref();
            double a = getFirst().toDD().computeDouble(x, y, defined2);
            if (!defined2.get()) {
                return 0;
            }
            defined2.reset();
            double b = getSecond().toDD().computeDouble(x, y, defined2);
            defined2.set();
            if (!defined2.get()) {
                return 0;
            }
            defined.set();
            return a / b;
        }
        return 0;
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef defined2 = BooleanMarker.ref();
            double a = getFirst().toDD().computeDouble(x, y, z, defined2);
            if (!defined2.get()) {
                return 0;
            }
            defined2.reset();
            double b = getSecond().toDD().computeDouble(x, y, z, defined2);
            defined2.set();
            if (!defined2.get()) {
                return 0;
            }
            defined.set();
            return a / b;
        }
        return 0;
    }

}