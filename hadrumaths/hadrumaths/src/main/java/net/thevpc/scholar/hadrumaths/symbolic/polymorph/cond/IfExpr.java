package net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.util.internal.CanProduceClass;
import net.thevpc.scholar.hadrumaths.util.internal.NonStateField;
import net.thevpc.scholar.hadrumaths.symbolic.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@CanProduceClass({DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class})
public abstract class IfExpr implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final boolean hasThen;
    private final boolean hasElse;
    private final Expr xargument;
    private final Expr yargument;
    private final Expr zargument;
    @NonStateField
    protected transient Domain _domain;
    private ComponentDimension componentDimension;

    protected IfExpr(Expr xarg) {
        this(xarg, null, null);
    }

    protected IfExpr(Expr xargument, Expr yargument, Expr zargument) {
        hasThen = yargument != null;
        hasElse = zargument != null;
        xargument = xargument == null ? Maths.ZERO : xargument.toDD();
        yargument = yargument == null ? Complex.ONE : yargument;
        zargument = zargument == null ? Complex.ZERO : zargument;
        componentDimension = Maths.expandComponentDimension(yargument.getComponentDimension(), zargument.getComponentDimension());
        yargument = Maths.expandComponentDimension(yargument, componentDimension);
        zargument = Maths.expandComponentDimension(zargument, componentDimension);
        this.xargument = xargument.toDD();
        this.yargument = yargument;
        this.zargument = zargument;
        this._domain = (xargument == null ? Complex.ONE : xargument).getDomain()
                .expand((yargument == null ? Complex.ONE : yargument).getDomain())
                .expand((zargument == null ? Complex.ZERO : zargument).getDomain());
    }

    protected IfExpr(Expr xarg, Expr yarg) {
        this(xarg, yarg, null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName(), hasThen, hasElse, _domain, xargument, yargument, zargument, componentDimension);
    }
    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        /**
         * $\begin{cases}
         * v1 & \text{if }a>0\\
         * v1 & \text{if }a>0
         * \end{cases}$
         *
         */
        sb.append("\\\\begin{cases}\n");
        sb.append("{").append(yargument.toLatex()).append("}").append(" & ").append("\\text{if }").append(xargument).append("\\\\\n");
        sb.append("{").append(zargument.toLatex()).append("}").append(" & ").append("\\text{Else}");
        sb.append("\\end{cases}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IfExpr ifExpr = (IfExpr) o;
        return hasThen == ifExpr.hasThen &&
                hasElse == ifExpr.hasElse &&
                Objects.equals(_domain, ifExpr._domain) &&
                Objects.equals(xargument, ifExpr.xargument) &&
                Objects.equals(yargument, ifExpr.yargument) &&
                Objects.equals(zargument, ifExpr.zargument) &&
                Objects.equals(componentDimension, ifExpr.componentDimension);
    }

    @Override
    public List<Expr> getChildren() {
        return Arrays.asList(xargument, yargument, zargument);
    }

    @Override
    public Expr getChild(int index) {
        return ExprDefaults.getChild(index, xargument, yargument, zargument);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return componentDimension;
    }

    @Override
    public Domain getDomain() {
        return _domain;
    }

    @Override
    public Expr newInstance(Expr... xargument) {
        return IfExpr.of(xargument[0], xargument[1], xargument[2]);
    }

    public static IfExpr of(Expr a, Expr b, Expr c) {
        switch (ExprDefaults.widest(b, c)) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new IfExprDoubleToDouble(a.toDD(), b, c);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new IfExprDoubleToComplex(a.toDD(), b, c);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new IfExprDoubleToVector(a.toDD(), b, c);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new IfExprDoubleToMatrix(a.toDD(), b, c);
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
//                return new IfExpr(a, b, c);
            }
        }
    }

    public IfExpr Then(Expr yarg) {
        if (hasThen) {
            throw new IllegalArgumentException("Multiple Then Expressions");
        }
        return IfExpr.of(getChild(0), yarg, hasElse ? getChild(2) : null);
    }

    public IfExpr Else(Expr zarg) {
        if (!hasThen) {
            throw new IllegalArgumentException("Multiple Missing Then expression");
        }
        if (hasElse) {
            throw new IllegalArgumentException("Multiple Else Expressions");
        }
        return IfExpr.of(getChild(0), getChild(1), zarg);
    }

    @Override
    public String getName() {
        return "If";
    }

}

class IfExprDoubleToComplex extends IfExpr implements DoubleToComplexDefaults.DoubleToComplexNormal {
    public IfExprDoubleToComplex(Expr xarg) {
        super(xarg);
    }

    public IfExprDoubleToComplex(Expr xarg, Expr yarg) {
        super(xarg, yarg);
    }

    public IfExprDoubleToComplex(Expr xarg, Expr yarg, Expr zarg) {
        super(xarg, yarg, zarg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("IfExpr");
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDC().evalComplex(x, defined);
                } else {
                    return getChild(2).toDC().evalComplex(x, defined);
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, y, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDC().evalComplex(x, y, defined);
                } else {
                    return getChild(2).toDC().evalComplex(x, y, defined);
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, y, z, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDC().evalComplex(x, y, z, defined);
                } else {
                    return getChild(2).toDC().evalComplex(x, y, z, defined);
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }
}

class IfExprDoubleToDouble extends IfExpr implements DoubleToDoubleDefaults.DoubleToDoubleNormal {
    public IfExprDoubleToDouble(Expr xarg) {
        super(xarg);
    }

    public IfExprDoubleToDouble(Expr xarg, Expr yarg) {
        super(xarg, yarg);
    }

    public IfExprDoubleToDouble(Expr xarg, Expr yarg, Expr zarg) {
        super(xarg, yarg, zarg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("IfExpr");
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, y, z, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDD().evalDouble(x, y, z, defined);
                } else {
                    return getChild(2).toDD().evalDouble(x, y, z, defined);
                }
            }
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDD().evalDouble(x, defined);
                } else {
                    return getChild(2).toDD().evalDouble(x, defined);
                }
            }
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, y, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDD().evalDouble(x, y, defined);
                } else {
                    return getChild(2).toDD().evalDouble(x, y, defined);
                }
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

}

class IfExprDoubleToVector extends IfExpr implements DoubleToVector/*Defaults.DoubleToVectorNormal*/ {
    private int cs;

    public IfExprDoubleToVector(Expr xarg) {
        super(xarg);
        cs = 1;
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("IfExpr");
    }

    public IfExprDoubleToVector(Expr xarg, Expr yarg) {
        super(xarg, yarg);
        cs = Expressions.widestComponentSize(yarg);
    }

    public IfExprDoubleToVector(Expr xarg, Expr yarg, Expr zarg) {
        super(xarg, yarg, zarg);
        cs = Expressions.widestComponentSize(yarg, zarg);
    }

    @Override
    public Expr getComponent(Axis a) {
        return IfExpr.of(getChild(0), getChild(1).toDV().getComponent(a), getChild(2).toDV().getComponent(a));
    }

    @Override
    public ComplexVector evalVector(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, y, z, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDV().evalVector(x, y, z, defined);
                } else {
                    return getChild(2).toDV().evalVector(x, y, z, defined);
                }
            }
        }
        return DoubleToVectorDefaults.Zero(this);
    }

    @Override
    public ComplexVector evalVector(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDV().evalVector(x, defined);
                } else {
                    return getChild(2).toDV().evalVector(x, defined);
                }
            }
        }
        return DoubleToVectorDefaults.Zero(this);
    }

    @Override
    public ComplexVector evalVector(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, y, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDV().evalVector(x, y, defined);
                } else {
                    return getChild(2).toDV().evalVector(x, y, defined);
                }
            }
        }
        return DoubleToVectorDefaults.Zero(this);
    }

    @Override
    public int getComponentSize() {
        return cs;
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public int hashCode() {
        //ok to call super
        return super.hashCode() * 31 + cs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        //OK super
        if (!super.equals(o)) return false;
        IfExprDoubleToVector that = (IfExprDoubleToVector) o;
        return cs == that.cs;
    }
}


class IfExprDoubleToMatrix extends IfExpr implements DoubleToMatrix/*Defaults.DoubleToVectorNormal*/ {
    public IfExprDoubleToMatrix(Expr xarg) {
        super(xarg);
    }

    public IfExprDoubleToMatrix(Expr xarg, Expr yarg) {
        super(xarg, yarg);
    }

    public IfExprDoubleToMatrix(Expr xarg, Expr yarg, Expr zarg) {
        super(xarg, yarg, zarg);
    }


    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("IfExpr");
    }


    @Override
    public ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDM().evalMatrix(x, defined);
                } else {
                    return getChild(2).toDM().evalMatrix(x, defined);
                }
            }
        }
        return DoubleToMatrixDefaults.Zero(this);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, y, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDM().evalMatrix(x, y, defined);
                } else {
                    return getChild(2).toDM().evalMatrix(x, y, defined);
                }
            }
        }
        return DoubleToMatrixDefaults.Zero(this);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getChild(0).toDD().evalDouble(x, y, z, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getChild(1).toDM().evalMatrix(x, y, z, defined);
                } else {
                    return getChild(2).toDM().evalMatrix(x, y, z, defined);
                }
            }
        }
        return DoubleToMatrixDefaults.Zero(this);
    }

    @Override
    public Expr getComponent(int row, int col) {
        Expr cond = getChild(0);
        Expr thenExpr = getChild(1);
        Expr elseExpr = getChild(2);
        return IfExpr.of(cond, thenExpr == null ? null : thenExpr.toDM().getComponent(row, col), elseExpr == null ? null : elseExpr.toDM().getComponent(row, col));
    }
}

