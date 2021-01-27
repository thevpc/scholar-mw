package net.thevpc.scholar.hadrumaths.symbolic.double2complex;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.util.internal.NonStateField;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 4/30/14.
 */
public class DefaultCustomDCFunctionXYExpr implements CustomDCFunctionXYExpr, DoubleToComplexDefaults.DoubleToComplexNormal {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(DefaultCustomDCFunctionXYExpr.class, new ObjectFormat<DefaultCustomDCFunctionXYExpr>() {
            @Override
            public void format(DefaultCustomDCFunctionXYExpr o, ObjectFormatContext context) {
                context.append(o.getDefinition().getName()).append("(");
                context.format(o.getChild(0));
//                context.append(", ");
                context.append(",");
                context.format(o.getChild(1));
                context.append(")");
            }
        });

    }

    private final CustomDCFunctionXYDefinition definition;
    private final Expr xargument;
    private final Expr yargument;
    @NonStateField
    private final Domain domain;

    public DefaultCustomDCFunctionXYExpr(DoubleToDouble xarg, DoubleToDouble yarg, CustomDCFunctionXYDefinition definition) {
        ComponentDimension cd = xarg.getComponentDimension().expand(yarg.getComponentDimension());
        this.xargument = Maths.expandComponentDimension(xarg, cd);
        this.yargument = Maths.expandComponentDimension(yarg, cd);
        this.domain = xargument.getDomain().expand(yargument.getDomain());
        this.definition = definition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(definition, xargument, yargument, domain);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultCustomDCFunctionXYExpr that = (DefaultCustomDCFunctionXYExpr) o;
        return Objects.equals(definition, that.definition) &&
                Objects.equals(xargument, that.xargument) &&
                Objects.equals(yargument, that.yargument) &&
                Objects.equals(domain, that.domain);
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
    public final Domain getDomain() {
        return domain;
    }

    public Expr newInstance(Expr... arguments) {
        return new DefaultCustomDCFunctionXYExpr(arguments[0].toDD(), arguments[1].toDD(), getDefinition());
    }

    @Override
    public CustomDCFunctionXYDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        BooleanRef r = BooleanMarker.ref();
        double a = xargument.toDD().evalDouble(x, r);
        if (r.get()) {
            double b = yargument.toDD().evalDouble(x, r);
            defined.set();
            return definition.getEval().eval(a, b);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        BooleanRef r = BooleanMarker.ref();
        double a = xargument.toDD().evalDouble(x, y, r);
        if (r.get()) {
            double b = yargument.toDD().evalDouble(x, y, r);
            defined.set();
            return definition.getEval().eval(a, b);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        BooleanRef r = BooleanMarker.ref();
        double a = xargument.toDD().evalDouble(x, y, z, r);
        if (r.get()) {
            double b = yargument.toDD().evalDouble(x, y, z, r);
            defined.set();
            return definition.getEval().eval(a, b);
        }
        return Complex.ZERO;
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }
}
