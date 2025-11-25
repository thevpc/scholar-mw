package net.thevpc.scholar.hadrumaths.symbolic.complex2complex;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * TODO : add Test
 * Created by vpc on 4/30/14.
 * NO_NARROW
 */
public class DefaultCustomCCFunctionXExpr implements CustomCCFunctionXExpr {
    private static final long serialVersionUID = 1L;
    private final CustomCCFunctionXDefinition definition;
    private final DoubleToComplex arg;

    public DefaultCustomCCFunctionXExpr(DoubleToComplex arg, CustomCCFunctionXDefinition definition) {
        this.arg = arg;
        this.definition = definition;
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("DefaultCustomCCFunctionXExpr");
    }

    @Override
    public int hashCode() {
        return definition.hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultCustomCCFunctionXExpr that = (DefaultCustomCCFunctionXExpr) o;
        return Objects.equals(definition, that.definition) &&
                Objects.equals(arg, that.arg);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public List<Expr> getChildren() {
        return Arrays.asList(arg);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Expr newInstance(Expr[] argument) {
        return new DefaultCustomCCFunctionXExpr(argument[0].toDC(), definition);
    }

    @Override
    public Domain getDomain() {
        return arg.getDomain();
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    @Override
    public CustomCCFunctionXDefinition getDefinition() {
        return definition;
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        BooleanRef def = BooleanMarker.ref();
        Complex a = arg.evalComplex(x, def);
        if (def.get()) {
            defined.set();
            return definition.getEval().eval(a);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        BooleanRef def = BooleanMarker.ref();
        Complex a = arg.evalComplex(x, y, def);
        if (def.get()) {
            defined.set();
            return definition.getEval().eval(a);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        BooleanRef def = BooleanMarker.ref();
        Complex a = arg.evalComplex(x, y, z, def);
        if (def.get()) {
            defined.set();
            return definition.getEval().eval(a);
        }
        return Complex.ZERO;
    }
    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
