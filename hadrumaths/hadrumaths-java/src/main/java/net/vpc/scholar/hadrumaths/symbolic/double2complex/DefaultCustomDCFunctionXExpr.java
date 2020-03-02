package net.vpc.scholar.hadrumaths.symbolic.double2complex;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.InternalUnmodifiableSingletonList;

import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 4/30/14.
 */
public class DefaultCustomDCFunctionXExpr implements CustomDCFunctionXExpr {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(DefaultCustomDCFunctionXExpr.class, new ObjectFormat<DefaultCustomDCFunctionXExpr>() {
            @Override
            public void format(DefaultCustomDCFunctionXExpr o, ObjectFormatContext context) {
                context.append(o.getDefinition().getName()).append("(");
                context.format(o.getChild(0));
                context.append(")");
            }
        });

    }

    private final CustomDCFunctionXDefinition definition;
    private final InternalUnmodifiableSingletonList<DoubleToDouble> args;

    public DefaultCustomDCFunctionXExpr(DoubleToDouble arg, CustomDCFunctionXDefinition definition) {
        this.args = InternalUnmodifiableSingletonList.of(arg);
        this.definition = definition;
    }

    @Override
    public CustomDCFunctionXDefinition getDefinition() {
        return definition;
    }

    public String getName() {
        return definition.getName();
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        BooleanRef ref = BooleanMarker.ref();
        double v = args.getValue().evalDouble(x, ref);
        if (ref.get()) {
            defined.set();
            return definition.getEval().eval(v);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        BooleanRef ref = BooleanMarker.ref();
        double v = args.get(0).evalDouble(x, y, ref);
        if (ref.get()) {
            defined.set();
            return definition.getEval().eval(v);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        BooleanRef ref = BooleanMarker.ref();
        double v = args.get(0).evalDouble(x, y, z, ref);
        if (ref.get()) {
            defined.set();
            return definition.getEval().eval(v);
        }
        return Complex.ZERO;
    }

    @Override
    public DoubleToComplex toDC() {
        return this;
    }

    @Override
    public List<Expr> getChildren() {
        return (List) args;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return ExprDefaults.setParam(this, name, value);
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Domain getDomain() {
        return args.get(0).getDomain();
    }

    @Override
    public Expr newInstance(Expr[] argument) {
        return new DefaultCustomDCFunctionXExpr(argument[0].toDD(), definition);
    }

    @Override
    public int hashCode() {
        return definition.hashCode()*31+args.get(0).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultCustomDCFunctionXExpr that = (DefaultCustomDCFunctionXExpr) o;
        return Objects.equals(definition, that.definition) &&
                Objects.equals(args, that.args);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }
}
