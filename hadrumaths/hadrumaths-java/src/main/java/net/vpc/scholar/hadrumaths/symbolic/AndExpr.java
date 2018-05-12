package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class AndExpr extends ComparatorExpr implements Cloneable {
    private static final long serialVersionUID = 1L;
    public AndExpr(Expr xarg, Expr yarg) {
        super(xarg, yarg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "&&";
    }

    public Complex computeComplexArg(Complex x, Complex y, boolean xdef, boolean ydef, OutBoolean defined) {
        if(!xdef && !ydef){
            return Complex.ZERO;
        }
        defined.set();
        return (x.isZero() || y.isZero()) ? Complex.ZERO : Complex.ONE;
    }

    public Complex computeComplexArg(double x, double y, boolean xdef, boolean ydef, OutBoolean defined) {
        if(!xdef && !ydef){
            return Complex.ZERO;
        }
        defined.set();
        return (x == 0 || y == 0) ? Complex.ZERO : Complex.ONE;
    }

    public double computeDoubleArg(double x, double y, boolean xdef, boolean ydef, OutBoolean defined) {
        if(!xdef && !ydef){
            return 0;
        }
        defined.set();
        return (x == 0 || y == 0) ? 0 : 1;
    }

    @Override
    public Expr newInstance(Expr xargument, Expr yargument) {
        return new AndExpr(xargument, yargument);
    }
}
