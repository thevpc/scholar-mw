package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class CustomCCFunctionXYExpr extends GenericFunctionXY implements Cloneable{
    private CustomCCFunctionXYDefinition definition;
    public CustomCCFunctionXYExpr(Expr xarg, Expr yarg, CustomCCFunctionXYDefinition definition) {
        super(definition.getName(),xarg,yarg,FunctionType.COMPLEX);
        this.definition=definition;
    }

    public Complex evalComplex(Complex x, Complex y){
        return definition.getEval().evalComplex(x,y);
    }

    public Complex evalComplex(double x, double y){
        return (definition.getEval().evalComplex(Complex.valueOf(x),Complex.valueOf(y)));
    }

    public double evalDouble(double x, double y){
        return definition.getEval().evalComplex(Complex.valueOf(x),Complex.valueOf(y)).toDouble();
    }

    @Override
    public Expr newInstance(Expr xargument,Expr yargument) {
        return new CustomCCFunctionXYExpr(xargument,yargument,definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CustomCCFunctionXYExpr that = (CustomCCFunctionXYExpr) o;

        return definition != null ? definition.equals(that.definition) : that.definition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
