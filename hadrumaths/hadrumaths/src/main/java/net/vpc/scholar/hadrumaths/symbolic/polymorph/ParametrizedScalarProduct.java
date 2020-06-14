package net.vpc.scholar.hadrumaths.symbolic.polymorph;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.impl.AbstractObjectFormat;
import net.vpc.scholar.hadrumaths.symbolic.*;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.vpc.scholar.hadrumaths.util.internal.NonStateField;

public abstract class ParametrizedScalarProduct implements OperatorExpr {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(ParametrizedScalarProduct.class, new AbstractObjectFormat<ParametrizedScalarProduct>() {
            @Override
            public void format(ParametrizedScalarProduct o, ObjectFormatContext context) {
                ObjectFormatParamSet format = context.getParams();
                boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
                format = format.add(FormatFactory.REQUIRED_PARS);
                if (par) {
                    context.append("(");
                }
                context.format(o.getChild(0), format);
//                sb.append(" ").append(o.getName()).append(" ");
                context.append(o.getName());
                context.format(o.getChild(1), format);
                if (par) {
                    context.append(")");
                }
            }
        });
    }

    private final Expr xargument;
    private final Expr yargument;

    @NonStateField
    private transient WeakReference<Expr> meSimplified = new WeakReference<Expr>(null);

    protected ParametrizedScalarProduct(Expr xargument, Expr yargument) {
        ComponentDimension cd = xargument.getComponentDimension().expand(yargument.getComponentDimension());
        this.xargument = Maths.expandComponentDimension(xargument, cd);
        this.yargument = Maths.expandComponentDimension(yargument, cd);
    }

    @Override
    public String toLatex() {
        String a="{"+this.xargument.toLatex()+"}";
        String b="{"+this.yargument.toLatex()+"}";
        return "\\left\\langle "+a+","+b+"\\right\\rangle";
    }

    public static ParametrizedScalarProduct of(Expr a,Expr b) {
        switch (ExprDefaults.widest(a,b)) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new ParametrizedScalarProductDoubleToDouble(a,b);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new ParametrizedScalarProductDoubleToComplex(a,b);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR:
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX:{
                for (Expr expression : new Expr[]{a,b}) {
                    ExprType narrowType = expression.toDV().getNarrowType();
                    switch (narrowType){
                        case DOUBLE_NBR:
                        case DOUBLE_EXPR:
                        case DOUBLE_DOUBLE:{
                            break;
                        }
                        default:{
                            return new ParametrizedScalarProductDoubleToComplex(a,b);
                        }
                    }
                }
                return new ParametrizedScalarProductDoubleToDouble(a,b);
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName(),xargument, yargument, meSimplified);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParametrizedScalarProduct that = (ParametrizedScalarProduct) o;
        return Objects.equals(xargument, that.xargument) &&
                Objects.equals(yargument, that.yargument) &&
                Objects.equals(meSimplified, that.meSimplified);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public List<Expr> getChildren() {
        return Arrays.asList(xargument, yargument);
    }

    @Override
    public Expr getChild(int index) {
        return ExprDefaults.getChild(index, xargument, yargument);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Expr mul(Complex other) {
        return ParametrizedScalarProduct.of(getChild(0).mul(other), getChild(1));
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return true;
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public Complex toComplex() {
        return getSimplifiedExpr().toComplex();
    }

    @Override
    public double toDouble() {
        return getSimplifiedExpr().toDouble();
    }

    @Override
    public NumberExpr toNumber() {
        return getSimplifiedExpr().toNumber();
    }

    @Override
    public Expr mul(Domain other) {
        return ParametrizedScalarProduct.of(getChild(0).mul(other), getChild(1));
    }

    @Override
    public Expr mul(double other) {
        return ParametrizedScalarProduct.of(getChild(0).mul(other), getChild(1));
    }

    @Override
    public final Domain getDomain() {
        return Domain.FULLX;
    }

    @Override
    public Expr newInstance(Expr... xarguments) {
        return ParametrizedScalarProduct.of(xarguments[0], xarguments[1]);
    }

    @Override
    public String getName() {
        return "**";
    }

    protected Expr getSimplifiedExpr() {
        Expr e;
        if (meSimplified == null) {
            e = this.simplify();
            meSimplified = new WeakReference<Expr>(e);
        } else {
            e = meSimplified.get();
            if (e == null) {
                e = this.simplify();
                meSimplified = new WeakReference<Expr>(e);
            }
        }
        return e;
    }
}

class ParametrizedScalarProductDoubleToDouble extends ParametrizedScalarProduct implements DoubleToDoubleDefaults.DoubleToDoubleNormal {
    public ParametrizedScalarProductDoubleToDouble(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        defined.set();
        return getSimplifiedExpr().toDD().evalDouble(x, y, z, defined);
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        return getSimplifiedExpr().toDD().evalDouble(x, defined);
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        return getSimplifiedExpr().toDD().evalDouble(x, y, defined);
    }
}

class ParametrizedScalarProductDoubleToComplex extends ParametrizedScalarProduct implements DoubleToComplexDefaults.DoubleToComplexNormal {
    public ParametrizedScalarProductDoubleToComplex(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        return getSimplifiedExpr().toDC().evalComplex(x, defined);
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        return getSimplifiedExpr().toDC().evalComplex(x, y, defined);
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        defined.set();
        return getSimplifiedExpr().toDC().evalComplex(x, y, z, defined);
    }
}
