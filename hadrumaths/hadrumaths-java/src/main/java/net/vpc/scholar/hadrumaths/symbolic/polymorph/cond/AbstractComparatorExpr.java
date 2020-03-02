package net.vpc.scholar.hadrumaths.symbolic.polymorph.cond;

import net.vpc.common.tson.impl.util.UnmodifiableArrayList;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.impl.AbstractObjectFormat;
import net.vpc.scholar.hadrumaths.random.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.double2double.AbstractDoubleToDouble;

import java.util.List;
import java.util.Objects;
@IgnoreRandomGeneration
public abstract class AbstractComparatorExpr extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(AbstractComparatorExpr.class, new AbstractObjectFormat<AbstractComparatorExpr>() {
            @Override
            public void format(AbstractComparatorExpr o, ObjectFormatContext context) {
                context.append("(");
                ObjectFormatParamSet requiredPars = context.getParams().add(FormatFactory.REQUIRED_PARS);
                context.format(o.getXArgument(), requiredPars);
//                context.append(" ");
                context.append(o.getOperatorName());
//                context.append(" ");
                context.format(o.getYArgument(), requiredPars);
                context.append(")");
            }
        });
    }

    protected UnmodifiableArrayList<DoubleToDouble> expressions;
    protected Domain domain;

    public AbstractComparatorExpr(DoubleToDouble xargument, DoubleToDouble yargument, Domain domain) {
        expressions = UnmodifiableArrayList.ofRef(new DoubleToDouble[]{xargument.toDD(), yargument.toDD()});
        this.domain = domain;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public List<Expr> getChildren() {
        return (List) expressions;
    }

    public abstract String getOperatorName();

    @Override
    public boolean isInvariant(Axis axis) {
        return ExprDefaults.isInvariantAll((List) expressions, axis);
    }

    @Override
    public boolean isNaN() {
        return ExprDefaults.isNaNAny((List) expressions);
    }

    @Override
    public boolean hasParams() {
        return ExprDefaults.hasParamsAny((List) expressions);
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return ExprDefaults.setParam(this, name, value);
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return ExprDefaults.compose(this, xreplacement, yreplacement, zreplacement);
    }

    @Override
    public Expr mul(Domain domain) {
        return newInstance(
                getXArgument().getDomain().intersect(domain),
                getYArgument().getDomain().intersect(domain)
        );
    }

    public DoubleToDouble getXArgument() {
        return expressions.get(0);
    }

    public DoubleToDouble getYArgument() {
        return expressions.get(1);
    }

    public abstract Expr newInstance(Expr... a);

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef ok = BooleanMarker.ref();
            double a = getXArgument().toDD().evalDouble(x, ok);
            if (!ok.get()) {
                return 0;
            }
            double b = getYArgument().toDD().evalDouble(x, ok);
            if (!ok.get()) {
                return 0;
            }
            defined.set();
            return compare(a, b);
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef ok = BooleanMarker.ref();
            double a = getXArgument().toDD().evalDouble(x, y, ok);
            if (!ok.get()) {
                return 0;
            }
            double b = getYArgument().toDD().evalDouble(x, y, ok);
            if (!ok.get()) {
                return 0;
            }
            defined.set();
            return compare(a, b);
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef ok = BooleanMarker.ref();
            double a = getXArgument().toDD().evalDouble(x, y, z, ok);
            if (!ok.get()) {
                return 0;
            }
            double b = getYArgument().toDD().evalDouble(x, y, z, ok);
            if (!ok.get()) {
                return 0;
            }
            defined.set();
            return compare(a, b);
        }
        return 0;
    }

    protected abstract int compare(double a, double b);

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName(),expressions, domain);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractComparatorExpr that = (AbstractComparatorExpr) o;
        return Objects.equals(expressions, that.expressions) &&
                Objects.equals(domain, that.domain);
    }
}
