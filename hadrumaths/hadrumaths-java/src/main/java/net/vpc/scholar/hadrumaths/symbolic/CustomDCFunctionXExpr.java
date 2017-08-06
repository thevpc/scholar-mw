package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class CustomDCFunctionXExpr extends GenericFunctionX implements Cloneable{
    private CustomDCFunctionXDefinition definition;
    public CustomDCFunctionXExpr(Expr arg, CustomDCFunctionXDefinition definition) {
        super(definition.getName(),arg,FunctionType.COMPLEX);
        this.definition=definition;
    }
    @Override
    public String getFunctionName() {
        return definition.getName();
    }

    public Complex evalComplex(Complex c){
        return definition.getEval().evalComplex(c.toDouble());
    }

    protected double evalDouble(double c){
        return evalComplex(Complex.valueOf(c)).toDouble();
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new CustomDCFunctionXExpr(argument,definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CustomDCFunctionXExpr that = (CustomDCFunctionXExpr) o;

        return definition != null ? definition.equals(that.definition) : that.definition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
