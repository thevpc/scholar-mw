package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class CustomCCFunctionXExpr extends GenericFunctionX implements Cloneable{
    private static final long serialVersionUID = 1L;
    private CustomCCFunctionXDefinition definition;
    public CustomCCFunctionXExpr(Expr arg, CustomCCFunctionXDefinition definition) {
        super(definition.getName(),arg,FunctionType.COMPLEX);
        this.definition=definition;
    }
    @Override
    public String getFunctionName() {
        return definition.getName();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Complex computeComplexArg(Complex c, BooleanMarker defined){
        defined.set();
        return definition.getEval().evalComplex(c);
    }

    public double computeDoubleArg(double c, BooleanMarker defined){
        return computeComplexArg(Complex.valueOf(c), defined).toDouble();
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new CustomCCFunctionXExpr(argument,definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CustomCCFunctionXExpr that = (CustomCCFunctionXExpr) o;

        return definition != null ? definition.equals(that.definition) : that.definition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
