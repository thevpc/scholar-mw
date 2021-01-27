package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;
import net.thevpc.scholar.hadrumaths.util.InternalUnmodifiableArrayList;

import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 4/30/14.
 */
public class DefaultCustomDDFunctionXYExpr extends AbstractComposedDoubleToDouble implements CustomDDFunctionXYExpr {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(DefaultCustomDDFunctionXYExpr.class, new ObjectFormat<DefaultCustomDDFunctionXYExpr>() {
            @Override
            public void format(DefaultCustomDDFunctionXYExpr o, ObjectFormatContext context) {
                context.append(o.getDefinition().getName()).append("(");
                context.format(o.getChild(0));
                context.append(",");
                context.format(o.getChild(1));
                context.append(")");
            }
        });
        ExpressionRewriterFactory.NAVIGATION_RULES.addRule(new ExpressionRewriterRule() {
            @Override
            public Class<? extends Expr>[] getTypes() {
                return new Class[]{DefaultCustomDDFunctionXYExpr.class};
            }

            @Override
            public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
                DefaultCustomDDFunctionXYExpr a = (DefaultCustomDDFunctionXYExpr) e;
                DoubleToDouble e10 = a.args.get(0);
                RewriteResult e1 = ruleset.rewrite(e10, ExprType.DOUBLE_DOUBLE);
                DoubleToDouble e20 = a.args.get(1);
                RewriteResult e2 = ruleset.rewrite(e20, ExprType.DOUBLE_DOUBLE);
                if (e1.isUnmodified() && e1.isUnmodified()) {
                    return RewriteResult.unmodified();
                }
                return RewriteResult.bestEffort(new DefaultCustomDDFunctionXYExpr(
                        e1.isUnmodified() ? e10 : e1.getValue().toDD(),
                        e2.isUnmodified() ? e20 : e2.getValue().toDD(),
                        a.definition));
            }
        });
        //DefaultCustomDDFunctionXExpr
    }

    private final CustomDDFunctionXYDefinition definition;
    private final Domain domain;
    private final InternalUnmodifiableArrayList<DoubleToDouble> args;

    public DefaultCustomDDFunctionXYExpr(DoubleToDouble xarg, DoubleToDouble yarg, CustomDDFunctionXYDefinition definition) {
        this.args = new InternalUnmodifiableArrayList(new DoubleToDouble[]{xarg, yarg});
        this.definition = definition;
        this.domain = xarg.getDomain().intersect(yarg.getDomain());
    }

    @Override
    public CustomDDFunctionXYDefinition getDefinition() {
        return definition;
    }

    public String getFunctionName() {
        return definition.getName();
    }

    public Expr newInstance(Expr[] a) {
        return new DefaultCustomDDFunctionXYExpr(a[0].toDD(), a[1].toDD(), definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(definition, domain, args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultCustomDDFunctionXYExpr that = (DefaultCustomDDFunctionXYExpr) o;
        return Objects.equals(definition, that.definition) &&
                Objects.equals(domain, that.domain) &&
                Objects.equals(args, that.args);
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        BooleanRef a = BooleanMarker.ref();
        double xx = args.array[0].evalDouble(x, y, z, a);
        if (!a.get()) {
            return 0;
        }
        a.unset();

        double yy = args.array[1].evalDouble(x, y, z, a);
        if (!a.get()) {
            return 0;
        }
        defined.set();
        return definition.getEval().eval(xx, yy);
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        BooleanRef a = BooleanMarker.ref();
        double xx = args.array[0].evalDouble(x, a);
        if (!a.get()) {
            return 0;
        }
        a.unset();

        double yy = args.array[1].evalDouble(x, a);
        if (!a.get()) {
            return 0;
        }
        defined.set();
        return definition.getEval().eval(xx, yy);
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        BooleanRef a = BooleanMarker.ref();
        double xx = args.array[0].evalDouble(x, y, a);
        if (!a.get()) {
            return 0;
        }
        a.unset();

        double yy = args.array[1].evalDouble(x, y, a);
        if (!a.get()) {
            return 0;
        }
        defined.set();
        return definition.getEval().eval(xx, yy);
    }

    @Override
    public List<Expr> getChildren() {
        return (List) args;
    }


    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }
}
