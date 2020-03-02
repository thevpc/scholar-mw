package net.vpc.scholar.hadrumaths.symbolic.complex2complex;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.random.NonStateField;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * TODO : add Test
 * Created by vpc on 4/30/14.
 * NO_NARROW
 * //TODO : must implement CompleToComple and ComplexDomainExpr
 */
public class DefaultCustomCCFunctionXYExpr implements CustomCCFunctionXYExpr {
    private static final long serialVersionUID = 1L;
    private final CustomCCFunctionXYDefinition definition;
    private final DoubleToComplex xargument;
    private final DoubleToComplex yargument;
    @NonStateField
    private final Domain domain;

    public DefaultCustomCCFunctionXYExpr(DoubleToComplex xarg, DoubleToComplex yarg, CustomCCFunctionXYDefinition definition) {
        this.xargument = xarg;
        this.yargument = yarg;
        this.domain = xarg.getDomain().expand(yarg.getDomain());
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
        DefaultCustomCCFunctionXYExpr that = (DefaultCustomCCFunctionXYExpr) o;
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
        return new DefaultCustomCCFunctionXYExpr(arguments[0].toDC(), arguments[1].toDC(), getDefinition());
    }

    @Override
    public CustomCCFunctionXYDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        BooleanRef def = BooleanMarker.ref();
        Complex a = xargument.evalComplex(x, def);
        if (def.get()) {
            Complex b = xargument.evalComplex(x, def);
            defined.set();
            return definition.getEval().eval(a, b);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        BooleanRef def = BooleanMarker.ref();
        Complex a = xargument.evalComplex(x, y, def);
        if (def.get()) {
            Complex b = xargument.evalComplex(x, y, def);
            defined.set();
            return definition.getEval().eval(a, b);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        BooleanRef def = BooleanMarker.ref();
        Complex a = xargument.evalComplex(x, y, z, def);
        if (def.get()) {
            Complex b = xargument.evalComplex(x, y, z, def);
            defined.set();
            return definition.getEval().eval(a, b);
        }
        return Complex.ZERO;
    }
}
