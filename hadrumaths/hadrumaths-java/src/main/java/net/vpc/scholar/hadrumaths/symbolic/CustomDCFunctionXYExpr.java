package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class CustomDCFunctionXYExpr extends GenericFunctionXY implements Cloneable{
    private CustomDCFunctionXYDefinition definition;
    public CustomDCFunctionXYExpr(Expr xarg,Expr yarg, CustomDCFunctionXYDefinition definition) {
        super(definition.getName(),xarg,yarg,FunctionType.COMPLEX);
        this.definition=definition;
    }

    public Complex evalComplex(Complex x, Complex y){
        return definition.getEval().evalComplex(x.toDouble(),y.toDouble());
    }

    public Complex evalComplex(double x, double y){
        return (definition.getEval().evalComplex(x,y));
    }

    public double evalDouble(double x, double y){
        return definition.getEval().evalComplex(x,y).toDouble();
    }

    @Override
    public Expr newInstance(Expr xargument,Expr yargument) {
        return new CustomDCFunctionXYExpr(xargument,yargument,definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CustomDCFunctionXYExpr that = (CustomDCFunctionXYExpr) o;

        return definition != null ? definition.equals(that.definition) : that.definition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
