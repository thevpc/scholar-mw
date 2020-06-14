package net.vpc.scholar.hadrumaths.symbolic.polymorph.cond;

import net.vpc.common.tson.impl.util.UnmodifiableArrayList;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.impl.AbstractObjectFormat;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.double2double.AbstractDoubleToDouble;

import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 4/30/14.
 */
public class NotExpr extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(NotExpr.class, new AbstractObjectFormat<NotExpr>() {
            @Override
            public void format(NotExpr o, ObjectFormatContext context) {
                context.append("not(");
                context.format(o.getXArgument());
                context.append(")");
            }
        });
    }

    private final UnmodifiableArrayList<DoubleToDouble> expressions;

    private NotExpr(Expr arg) {
        expressions = UnmodifiableArrayList.ofRef(new DoubleToDouble[]{arg.toDD()});
    }

    @Override
    public List<Expr> getChildren() {
        return (List) expressions;
    }

    @Override
    public Domain getDomain() {
        return expressions.get(0).getDomain();
    }

    public String getOperatorName() {
        return "not";
    }

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

    //    @Override
    public Expr newInstance(Expr... arguments) {
        return NotExpr.of(arguments[0]);
    }

    public static NotExpr of(Expr e) {
        return new NotExpr(e.toDD());
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef r = BooleanMarker.ref();
            double a = getXArgument().toDD().evalDouble(x, y, z, r);
            if (r.get()) {
                defined.set();
                return compare(a);
            }
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef r = BooleanMarker.ref();
            double a = getXArgument().toDD().evalDouble(x, r);
            if (r.get()) {
                defined.set();
                return compare(a);
            }
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef r = BooleanMarker.ref();
            double a = getXArgument().toDD().evalDouble(x, y, r);
            if (r.get()) {
                defined.set();
                return compare(a);
            }
        }
        return 0;
    }

    public DoubleToDouble getXArgument() {
        return expressions.get(0);
    }

    public int compare(double a) {
        return a == 0 ? 1 : 0;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+expressions.get(0).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotExpr notExpr = (NotExpr) o;
        return Objects.equals(expressions, notExpr.expressions);
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("\\lnot");
        sb.append("{").append(getXArgument()).append("}");
        return sb.toString();
    }
}
