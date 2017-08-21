package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class AndExpr extends ComparatorExpr implements Cloneable{
    public AndExpr(Expr xarg, Expr yarg) {
        super(xarg,yarg,FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "&&";
    }

    public Complex evalComplex(Complex x, Complex y){
        return (x.isZero() || y.isZero())?Complex.ZERO:Complex.ONE;
    }

    public Complex evalComplex(double x, double y){
        return (x==0 || y==0) ?Complex.ZERO:Complex.ONE;
    }

    public double evalDouble(double x, double y){
        return (x==0 || y==0) ?0:1;
    }

    @Override
    public Expr newInstance(Expr xargument,Expr yargument) {
        return new AndExpr(xargument,yargument);
    }
}
