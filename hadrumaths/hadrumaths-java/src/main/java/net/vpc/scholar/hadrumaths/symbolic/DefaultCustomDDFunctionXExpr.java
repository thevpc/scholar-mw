package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

/**
 * Created by vpc on 4/30/14.
 */
public class DefaultCustomDDFunctionXExpr extends GenericFunctionX implements Cloneable,CustomDDFunctionXExpr {
    private static final long serialVersionUID = 1L;
    private CustomDDFunctionXDefinition definition;

    public DefaultCustomDDFunctionXExpr(DoubleToDouble arg, CustomDDFunctionXDefinition definition) {
        super(definition.getName(), arg.toDD(), FunctionType.DOUBLE);
        this.definition = definition;
    }

    public Expr newInstance(Expr... arguments){
        return new DefaultCustomDDFunctionXExpr(arguments[0].toDD(),getDefinition());
    }

    @Override
    public CustomDDFunctionXDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getFunctionName() {
        return definition.getName();
    }

    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return Complex.valueOf(definition.getEval().evalDouble(c.toDouble()));
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return definition.getEval().evalDouble(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new DefaultCustomDDFunctionXExpr(argument.toDD(), definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DefaultCustomDDFunctionXExpr that = (DefaultCustomDDFunctionXExpr) o;

        return definition != null ? definition.equals(that.definition) : that.definition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
