package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class NeExpr extends ComparatorExpr implements Cloneable{
    public NeExpr(Expr xarg, Expr yarg) {
        super("<>",xarg,yarg,FunctionType.DOUBLE);
    }

    public Complex evalComplex(Complex x, Complex y){
        return (!x.equals(y))?Complex.ONE:Complex.ZERO;
    }

    public Complex evalComplex(double x, double y){
        return x!=y ?Complex.ONE:Complex.ZERO;
    }

    public double evalDouble(double x, double y){
        return x!=y ?1:0;
    }

    @Override
    public Expr newInstance(Expr xargument,Expr yargument) {
        return new NeExpr(xargument,yargument);
    }
}
