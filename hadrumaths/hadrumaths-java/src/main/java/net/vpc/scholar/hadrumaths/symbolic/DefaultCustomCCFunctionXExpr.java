package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

/**
 * Created by vpc on 4/30/14.
 */
public class DefaultCustomCCFunctionXExpr extends GenericFunctionX implements Cloneable,CustomCCFunctionXExpr {
    private static final long serialVersionUID = 1L;
    private CustomCCFunctionXDefinition definition;

    public DefaultCustomCCFunctionXExpr(Expr arg, CustomCCFunctionXDefinition definition) {
        super(definition.getName(), arg, FunctionType.COMPLEX);
        this.definition = definition;
    }

    @Override
    public String getFunctionName() {
        return definition.getName();
    }

    public Expr newInstance(Expr... arguments){
        return new DefaultCustomCCFunctionXExpr(arguments[0].toDD(),getDefinition());
    }

    @Override
    public CustomCCFunctionXDefinition getDefinition() {
        return definition;
    }

    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return definition.getEval().evalComplex(c);
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        return computeComplexArg(Complex.valueOf(c), defined).toDouble();
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new DefaultCustomCCFunctionXExpr(argument, definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DefaultCustomCCFunctionXExpr that = (DefaultCustomCCFunctionXExpr) o;

        return definition != null ? definition.equals(that.definition) : that.definition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
