package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class CustomDDFunctionXYExpr extends GenericFunctionXY implements Cloneable{
    private CustomDDFunctionXYDefinition definition;
    public CustomDDFunctionXYExpr(Expr xarg, Expr yarg, CustomDDFunctionXYDefinition definition) {
        super(definition.getName(),xarg,yarg,FunctionType.COMPLEX);
        this.definition=definition;
    }

    @Override
    public String getFunctionName() {
        return definition.getName();
    }


    public Complex evalComplex(Complex x, Complex y){
        return Complex.valueOf(definition.getEval().evalDouble(x.toDouble(),y.toDouble()));
    }

    public Complex evalComplex(double x, double y){
        return Complex.valueOf(definition.getEval().evalDouble(x,y));
    }

    public double evalDouble(double x, double y){
        return definition.getEval().evalDouble(x,y);
    }

    @Override
    public Expr newInstance(Expr x,Expr y) {
        return new CustomDDFunctionXYExpr(x,y,definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CustomDDFunctionXYExpr that = (CustomDDFunctionXYExpr) o;

        return definition != null ? definition.equals(that.definition) : that.definition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
