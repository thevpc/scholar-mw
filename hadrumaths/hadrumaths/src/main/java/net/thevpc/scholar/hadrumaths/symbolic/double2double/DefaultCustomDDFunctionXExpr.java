package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.symbolic.CustomDDFunctionXDefinition;
import net.thevpc.scholar.hadrumaths.symbolic.CustomDDFunctionXExpr;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.util.InternalUnmodifiableSingletonList;

import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 4/30/14.
 */
public class DefaultCustomDDFunctionXExpr extends AbstractComposedDoubleToDouble implements CustomDDFunctionXExpr {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(DefaultCustomDDFunctionXExpr.class, new ObjectFormat<DefaultCustomDDFunctionXExpr>() {
            @Override
            public void format(DefaultCustomDDFunctionXExpr o, ObjectFormatContext context) {
                context.append(o.getDefinition().getName()).append("(");
                context.format(o.getChild(0));
                context.append(")");
            }
        });
        //DefaultCustomDDFunctionXExpr
    }

    private final CustomDDFunctionXDefinition definition;
    private final InternalUnmodifiableSingletonList<DoubleToDouble> args;

    public DefaultCustomDDFunctionXExpr(DoubleToDouble arg, CustomDDFunctionXDefinition definition) {
        this.definition = definition;
        this.args = InternalUnmodifiableSingletonList.of(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("DefaultCustomDDFunctionXExpr");
    }


    @Override
    public Expr newInstance(Expr... arguments) {
        return new DefaultCustomDDFunctionXExpr(arguments[0].toDD(), getDefinition());
    }

    @Override
    public CustomDDFunctionXDefinition getDefinition() {
        return definition;
    }

    public String getFunctionName() {
        return definition.getName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(definition, args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultCustomDDFunctionXExpr that = (DefaultCustomDDFunctionXExpr) o;
        return Objects.equals(definition, that.definition) &&
                Objects.equals(args, that.args);
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        BooleanRef ref = BooleanMarker.ref();
        double xx = args.getValue().evalDouble(x, ref);
        if (ref.get()) {
            defined.set();
            return definition.getEval().eval(xx);
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        BooleanRef ref = BooleanMarker.ref();
        double xx = args.getValue().evalDouble(x, y, ref);
        if (ref.get()) {
            defined.set();
            return definition.getEval().eval(xx);
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        BooleanRef ref = BooleanMarker.ref();
        double xx = args.getValue().evalDouble(x, y, z, ref);
        if (ref.get()) {
            defined.set();
            return definition.getEval().eval(xx);
        }
        return 0;
    }

    @Override
    public List<Expr> getChildren() {
        return (List) args;
    }

    @Override
    public Domain getDomain() {
        return args.getValue().getDomain();
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }
}
